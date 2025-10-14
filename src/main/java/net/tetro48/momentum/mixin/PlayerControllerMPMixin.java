package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {
	@Shadow private int blockHitDelay;
	@Shadow @Final private Minecraft mc;
	@Unique private int curSwiftLevel = 0;

	@ModifyConstant(method = "onPlayerDamageBlock", constant = @Constant(intValue = 5))
	private int changeBlockDelay(int constant) {
		return constant - curSwiftLevel;
	}
	@Inject(method = "onPlayerDestroyBlock", at = @At(ordinal = 1, value = "FIELD", target = "Lnet/minecraft/src/PlayerControllerMP;blockHitDelay:I", shift = At.Shift.AFTER))
	private void onBlockBreak(int par1, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir) {
		int swiftLevel = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentSwift.effectId, this.mc.thePlayer.getHeldItem());
		curSwiftLevel = swiftLevel;
		this.blockHitDelay -= swiftLevel;
	}
}
