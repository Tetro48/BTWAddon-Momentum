package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet14BlockDig;
import net.tetro48.momentum.MomentumAffected;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMixin {
	@Shadow public EntityPlayerMP playerEntity;

	@Inject(method = "handleBlockDig", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemInWorldManager;uncheckedTryHarvestBlock(IIII)V"))
	private void onHarvestAttempt(Packet14BlockDig par1Packet14BlockDig, CallbackInfo ci) {
		boolean hasMomentum = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentMomentum.effectId, playerEntity.getHeldItem()) > 0;
		if (hasMomentum) {
			((MomentumAffected) this.playerEntity).momentum$incrementBlocksBroken();
		}
		else {
			((MomentumAffected) this.playerEntity).momentum$resetBlocksBroken();
		}
	}
}
