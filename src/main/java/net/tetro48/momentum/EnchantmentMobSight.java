package net.tetro48.momentum;

import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.ItemStack;

public class EnchantmentMobSight extends Enchantment {
	public EnchantmentMobSight(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {
		super(par1, par2, par3EnumEnchantmentType);
		this.setName("mob_sight");
	}

	@Override
	public boolean canBeAppliedByVanillaEnchanter() {
		return false;
	}

	@Override
	public boolean canApply(ItemStack par1ItemStack) {
		return super.canApply(par1ItemStack) && par1ItemStack.itemID == BTWItems.enderSpectacles.itemID;
	}
}
