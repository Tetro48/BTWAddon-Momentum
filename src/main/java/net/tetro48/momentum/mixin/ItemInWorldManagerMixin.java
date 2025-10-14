package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin {

	@Shadow public EntityPlayerMP thisPlayerMP;

	@Shadow public World theWorld;

	@Inject(method = "tryHarvestBlock(IIII)Z", at = @At("HEAD"))
	private void onHarvestAttempt(int i, int j, int k, int iFromSide, CallbackInfoReturnable<Boolean> cir) {
		boolean hasMomentum = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentMomentum.effectId, thisPlayerMP.getHeldItem()) > 0;
		if (hasMomentum) {
			NBTTagCompound itemTagCompound = thisPlayerMP.getHeldItem().getTagCompound();
			NBTTagCompound tagCompound;
			int miningBlockID = this.theWorld.getBlockId(i, j, k);
			if (itemTagCompound.hasKey("momentum")) {
				tagCompound = itemTagCompound.getCompoundTag("momentum");
				long blocksMined = tagCompound.getLong("blocksMined");
				int prevBlockID = tagCompound.getInteger("blockID");
				if (prevBlockID != miningBlockID && miningBlockID != 0) {
					blocksMined = 0;
					tagCompound.setInteger("blockID", miningBlockID);
				}
				tagCompound.setLong("blocksMined", blocksMined + 1L);
			}
			else {
				tagCompound = new NBTTagCompound("momentum");
				tagCompound.setInteger("blockID", miningBlockID);
				tagCompound.setLong("blocksMined", 1L);
				itemTagCompound.setCompoundTag("momentum", tagCompound);
			}
		}
	}
}
