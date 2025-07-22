package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.Minecraft;
import net.minecraft.src.PlayerControllerMP;
import net.tetro48.momentum.MomentumAffected;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {
	@Shadow private int blockHitDelay;
	@Shadow @Final private Minecraft mc;
	@Unique private int blocksDestroyed = 0;
	@Unique public int ticksUntilDecay = 0;

	@ModifyConstant(method = "onPlayerDamageBlock", constant = @Constant(intValue = 5))
	private int changeBlockDelay(int constant) {
		return constant - (blocksDestroyed / 10);
	}
	@Inject(method = "onPlayerDamageBlock", at = @At("HEAD"))
	private void whileBreaking(int par1, int par2, int par3, int par4, CallbackInfo ci) {
		ticksUntilDecay = 20;
	}
	@Inject(method = "onPlayerDestroyBlock", at = @At(ordinal = 1, value = "FIELD", target = "Lnet/minecraft/src/PlayerControllerMP;blockHitDelay:I", shift = At.Shift.AFTER), cancellable = true)
	private void countBlockBreaks(int par1, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir) {
		this.blockHitDelay -= blocksDestroyed / 10;
		if (EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentMomentum.effectId, this.mc.thePlayer.getHeldItem()) > 0) {
			ticksUntilDecay = 20;
			((MomentumAffected)this.mc.thePlayer).momentum$incrementBlocksBroken();
		}
	}
	@Inject(method = "resetBlockRemoving", at = @At("RETURN"))
	private void resetBlockBreaks(CallbackInfo ci) {
	}
	@Inject(method = "updateController", at = @At("HEAD"))
	private void decrementTicksUntilDecay(CallbackInfo ci) {
		ticksUntilDecay--;
		if (ticksUntilDecay <= 0) {
			((MomentumAffected)this.mc.thePlayer).momentum$decayBlocksBroken();
		}
		blocksDestroyed = ((MomentumAffected)this.mc.thePlayer).momentum$getBlocksBroken();
	}
}
