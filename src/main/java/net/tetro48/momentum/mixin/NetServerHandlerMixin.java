package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.tetro48.momentum.MomentumAffected;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMixin {
	@Shadow public EntityPlayerMP playerEntity;

	@Shadow @Final public MinecraftServer mcServer;

	@Inject(method = "handleBlockDig", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemInWorldManager;uncheckedTryHarvestBlock(IIII)V"))
	private void onHarvestAttempt(Packet14BlockDig par1Packet14BlockDig, CallbackInfo ci) {
		boolean hasMomentum = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentMomentum.effectId, playerEntity.getHeldItem()) > 0;
		WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
		int blockX = par1Packet14BlockDig.xPosition;
		int blockY = par1Packet14BlockDig.yPosition;
		int blockZ = par1Packet14BlockDig.zPosition;
		if (hasMomentum) {
			((MomentumAffected) this.playerEntity).momentum$setBlockID(var2.getBlockId(blockX, blockY, blockZ));
			((MomentumAffected) this.playerEntity).momentum$incrementBlocksBroken();
		}
		else {
			((MomentumAffected) this.playerEntity).momentum$resetBlocksBroken();
		}
	}
}
