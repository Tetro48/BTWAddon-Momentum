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
public abstract class EntityPlayerMixin extends EntityLivingBase implements MomentumAffected {
	@Shadow public InventoryPlayer inventory;

	public EntityPlayerMixin(World par1World) {
		super(par1World);
	}

	@Shadow public abstract void playSound(String par1Str, float par2, float par3);

	@Shadow public abstract World getEntityWorld();

	@Shadow public abstract boolean canHarvestBlock(Block par1Block, int i, int j, int k);

	@Unique private int previousBlockID = 0;
	@Unique
	private int blocksBroken = 0;

	@Override
	public void momentum$setBlockID(int blockID) {
		if (previousBlockID != blockID) {
			previousBlockID = blockID;
			momentum$resetBlocksBroken();
		}
	}

	@Override
	public int momentum$getBlocksBroken() {
		return blocksBroken;
	}

	@Override
	public void momentum$incrementBlocksBroken() {
		blocksBroken++;
	}

	@Override
	public void momentum$resetBlocksBroken() {
		blocksBroken = 0;
	}

	@Inject(method = "getCurrentPlayerStrVsBlock", at = @At("RETURN"), cancellable = true)
	private void increaseSpeed(Block par1Block, int i, int j, int k, CallbackInfoReturnable<Float> cir) {
		if (EnchantmentHelper.getEnchantmentLevel(MomentumAddon.MOMENTUM_ID, inventory.getCurrentItem()) == 0 ||
				!canHarvestBlock(par1Block, i, j, k) ||
				(!this.isSneaking() && !this.isPotionActive(Potion.digSpeed)) ||
				(this.isSprinting() && (!this.isPotionActive(Potion.digSpeed) || this.getActivePotionEffect(Potion.digSpeed).getAmplifier() < 1))
		) {
			return;
		}
		if (par1Block.blockID != previousBlockID) {
			return;
		}
		float curSpeed = cir.getReturnValue();
		float speedFactor = (float) Math.pow(Math.pow(2, -1.0 / 16 * par1Block.blockHardness + 3.0 / 16) + 1, blocksBroken + 1);
		curSpeed *= speedFactor;
		cir.setReturnValue(curSpeed);
	}
}
