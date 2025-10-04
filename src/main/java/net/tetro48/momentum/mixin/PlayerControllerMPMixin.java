package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.src.*;
import net.tetro48.momentum.MomentumAffected;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {
	@Shadow private int blockHitDelay;
	@Shadow @Final private Minecraft mc;
	@Unique private int curSwiftLevel = 0;

	@ModifyConstant(method = "onPlayerDamageBlock", constant = @Constant(intValue = 5))
	private int changeBlockDelay(int constant) {
		return constant - curSwiftLevel;
	}
	@Inject(method = "onPlayerDestroyBlock", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(ordinal = 1, value = "FIELD", target = "Lnet/minecraft/src/PlayerControllerMP;blockHitDelay:I", shift = At.Shift.AFTER))
	private void onBlockBreak(int par1, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir, WorldClient var5, Block block) {
		int swiftLevel = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentSwift.effectId, this.mc.thePlayer.getHeldItem());
		curSwiftLevel = swiftLevel;
		this.blockHitDelay -= swiftLevel;
		boolean hasMomentum = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentMomentum.effectId, this.mc.thePlayer.getHeldItem()) > 0;
		if (hasMomentum) {
			((MomentumAffected) this.mc.thePlayer).momentum$setBlockID(block.blockID);
			((MomentumAffected) this.mc.thePlayer).momentum$incrementBlocksBroken();
		}
		else {
			((MomentumAffected) this.mc.thePlayer).momentum$resetBlocksBroken();
		}
	}
}
