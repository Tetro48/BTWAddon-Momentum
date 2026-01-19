package net.tetro48.momentum.mixin;

import api.world.WorldUtils;
import btw.community.momentum.MomentumAddon;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin {
	@Shadow @Final private Minecraft mc;

	@Shadow @Final private Random rand;

	@Unique
	private double projectIntoSmallerSpace(double pivot, double input, double compressionFactor) {
		return pivot + (input - pivot) / compressionFactor;
	}

	@Unique
	private void spawnMobSightParticle(World world, double x, double y, double z, double distanceFromPlayer) {
		EntityFX particleEntity = new EntitySpellParticleFX(world, x, y, z, 0d, 0d, 0d);
		float brightness = (float) (1f / (1f + distanceFromPlayer));
		particleEntity.setRBGColorF(brightness, brightness * 0.25F, brightness);
		this.mc.effectRenderer.addEffect(particleEntity);
	}

	@Inject(method = "addTrueSightParticles", at = @At("HEAD"))
	private void showMobsWithParticles(CallbackInfo ci) {
		if (!this.mc.getIsGamePaused()) {
			EntityPlayer player = this.mc.thePlayer;
			if (EnchantmentHelper.getEnchantmentLevel(MomentumAddon.MOB_SIGHT_ID, player.inventory.armorInventory[3]) == 0) {
				return;
			}
			World world = this.mc.theWorld;
			List<Entity> entityList = world.loadedEntityList;
			int iParticleSetting = this.mc.gameSettings.particleSetting;
			for (Entity entity : entityList) {
				if (entity instanceof EntityMob livingEntity) {
					if (!livingEntity.isDead) {
						if (this.rand.nextInt(12) <= 2 - iParticleSetting) {
							double totalDistanceSq = livingEntity.getDistanceSqToEntity(player);
							double particleX = projectIntoSmallerSpace(player.posX, livingEntity.posX, 32d);
							double particleY = projectIntoSmallerSpace(player.posY, livingEntity.posY, 32d);
							double particleZ = projectIntoSmallerSpace(player.posZ, livingEntity.posZ, 32d);
							if (totalDistanceSq > 12*12) this.spawnMobSightParticle(world, particleX, particleY, particleZ, Math.sqrt(totalDistanceSq)/32d);
						}
					}
				}
			}
		}
	}
}
