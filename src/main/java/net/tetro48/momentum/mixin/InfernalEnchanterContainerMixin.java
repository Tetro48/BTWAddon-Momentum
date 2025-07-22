package net.tetro48.momentum.mixin;

import btw.community.momentum.MomentumAddon;
import btw.inventory.container.InfernalEnchanterContainer;
import net.minecraft.src.Enchantment;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfernalEnchanterContainer.class)
public abstract class InfernalEnchanterContainerMixin {
	@Inject(method = "doesEnchantmentConflictWithExistingOnes", at = @At("HEAD"), cancellable = true)
	private void makeMomentumConflictWithEfficiency(int iEnchantmentIndex, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

		NBTTagList enchantmentTagList = itemStack.getEnchantmentTagList();
		if (enchantmentTagList != null) {
			int iCurrentNumberOfItemEnchants = itemStack.getEnchantmentTagList().tagCount();

			for(int iTemp = 0; iTemp < iCurrentNumberOfItemEnchants; ++iTemp) {
				int iTempEnchantmentIndex = ((NBTTagCompound)enchantmentTagList.tagAt(iTemp)).getShort("id");
				if (iTempEnchantmentIndex == iEnchantmentIndex) {
					cir.setReturnValue(true);
					return;
				}

				if (iEnchantmentIndex == MomentumAddon.enchantmentMomentum.effectId && iTempEnchantmentIndex == Enchantment.efficiency.effectId || iEnchantmentIndex == Enchantment.efficiency.effectId && iTempEnchantmentIndex == MomentumAddon.enchantmentMomentum.effectId) {
					cir.setReturnValue(true);
				}
			}
		}
	}
}
