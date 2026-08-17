package net.tetro48.momentum.mixin;

import api.block.blocks.CropsBlock;
import btw.block.BTWBlocks;
import btw.block.blocks.*;
import btw.community.momentum.MomentumAddon;
import btw.item.BTWItems;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin {

	@Shadow public EntityPlayerMP thisPlayerMP;

	@Shadow public World theWorld;

	@Inject(method = "tryHarvestBlock(IIII)Z", at = @At("HEAD"))
	private void preHarvestAttempt(int i, int j, int k, int iFromSide, CallbackInfoReturnable<Boolean> cir) {
		int blockId = this.theWorld.getBlockId(i, j, k);
		boolean hasMomentum = EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentMomentum.effectId, thisPlayerMP.getHeldItem()) > 0;
		if (hasMomentum) {
			NBTTagCompound itemTagCompound = thisPlayerMP.getHeldItem().getTagCompound();
			NBTTagCompound tagCompound;
			if (itemTagCompound.hasKey("momentum")) {
				tagCompound = itemTagCompound.getCompoundTag("momentum");
				long blocksMined = tagCompound.getLong("blocksMined");
				int prevBlockID = tagCompound.getInteger("blockID");
				if (prevBlockID != blockId && blockId != 0) {
					blocksMined = 0;
					tagCompound.setInteger("blockID", blockId);
				}
				tagCompound.setLong("blocksMined", blocksMined + 1L);
			}
			else {
				tagCompound = new NBTTagCompound("momentum");
				tagCompound.setInteger("blockID", blockId);
				tagCompound.setLong("blocksMined", 1L);
				itemTagCompound.setCompoundTag("momentum", tagCompound);
			}
		}
	}

	@Unique
	public ItemStack getPlayerItemStack(ItemStack par1ItemStack) {

		for(int var3 = 0; var3 < thisPlayerMP.inventory.mainInventory.length; ++var3) {
			if (thisPlayerMP.inventory.mainInventory[var3] != null && thisPlayerMP.inventory.mainInventory[var3].isItemEqual(par1ItemStack)) {
				return thisPlayerMP.inventory.mainInventory[var3];
			}
		}

		return null;
	}
	@Unique
	public int getPlayerItemSlotID(ItemStack par1ItemStack) {

		for(int var3 = 0; var3 < thisPlayerMP.inventory.mainInventory.length; ++var3) {
			if (thisPlayerMP.inventory.mainInventory[var3] != null && thisPlayerMP.inventory.mainInventory[var3].isItemEqual(par1ItemStack)) {
				return var3;
			}
		}

		return -1;
	}

	@Unique
	private void replantCropAt(int x, int y, int z, int blockID, int seedID) {
		Block block = Block.blocksList[blockID];
		boolean doesPlayerHaveCorrespondingSeed;
		if (block instanceof CropsBlock) {
			ItemStack itemStack = getPlayerItemStack(new ItemStack(seedID, 1, 0));
			int itemSlotID = getPlayerItemSlotID(new ItemStack(seedID, 1, 0));
			doesPlayerHaveCorrespondingSeed = itemStack != null && itemStack.itemID == seedID && itemStack.stackSize >= 1;
			if (doesPlayerHaveCorrespondingSeed) {
				thisPlayerMP.inventory.decrStackSize(itemSlotID, 1);
				this.theWorld.setBlock(x, y, z, blockID);
			}
		}
	}

	@WrapMethod(method = "survivalTryHarvestBlock(IIII)Z")
	private boolean onHarvestCrop(int i, int j, int k, int iFromSide, Operation<Boolean> original) {
		boolean operationValue;
		int blockID = this.theWorld.getBlockId(i, j, k);
		Block block = Block.blocksList[blockID];
		operationValue = original.call(i, j, k, iFromSide);
		if (block instanceof CropsBlock cropBlock && EnchantmentHelper.getEnchantmentLevel(MomentumAddon.enchantmentReplenish.effectId, thisPlayerMP.getHeldItem()) > 0) {
			if (cropBlock instanceof WheatCropTopBlock) {
				replantCropAt(i, j-1, k, BTWBlocks.wheatCrop.blockID, BTWItems.wheatSeeds.itemID);
			}
			else if (cropBlock instanceof WheatCropBlock) {
				replantCropAt(i, j, k, blockID, BTWItems.wheatSeeds.itemID);
			}
			else if (cropBlock instanceof PotatoBlock) {
				replantCropAt(i, j, k, blockID, Item.potato.itemID);
			}
			else if (cropBlock instanceof CarrotBlockFlowers) {
				replantCropAt(i, j, k, blockID, BTWItems.carrot.itemID);
			}
			else if (cropBlock instanceof CarrotBlock) {
				replantCropAt(i, j, k, blockID, BTWItems.carrotSeeds.itemID);
			}
			else if (cropBlock instanceof HempCropBlockRoots) {
				replantCropAt(i, j, k, blockID, BTWItems.hempSeeds.itemID);
			}
		}
		return operationValue;
	}
}
