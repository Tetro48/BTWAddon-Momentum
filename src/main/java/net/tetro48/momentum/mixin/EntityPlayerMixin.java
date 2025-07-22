package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.src.*;
import net.tetro48.momentum.MomentumAffected;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin implements MomentumAffected {
	@Shadow public InventoryPlayer inventory;

	@Shadow public abstract void playSound(String par1Str, float par2, float par3);

	@Shadow public abstract World getEntityWorld();

	@Unique
	private int blocksBroken = 0;
	@Override
	public int momentum$getBlocksBroken() {
		return blocksBroken;
	}

	@Override
	public void momentum$incrementBlocksBroken() {
		blocksBroken++;
	}

	@Override
	public void momentum$decayBlocksBroken() {
		blocksBroken = Math.max(0, (int)(blocksBroken * 0.9d - 1));
	}

	@Override
	public void momentum$resetBlocksBroken() {
		blocksBroken = 0;
	}

	@Inject(method = "getCurrentPlayerStrVsBlock", at = @At("RETURN"), cancellable = true)
	private void increaseSpeed(Block par1Block, int i, int j, int k, CallbackInfoReturnable<Float> cir) {
		float curSpeed = cir.getReturnValue();
		float speedFactor = 1 + (blocksBroken / 10f);
		curSpeed *= speedFactor;
		cir.setReturnValue(curSpeed);
	}
}
