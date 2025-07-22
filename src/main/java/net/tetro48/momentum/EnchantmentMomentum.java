package net.tetro48.momentum;

import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentDigging;
import net.minecraft.src.EnumEnchantmentType;

public class EnchantmentMomentum extends Enchantment {
	public EnchantmentMomentum(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {
		super(par1, par2, par3EnumEnchantmentType);
		this.setName("momentum");
	}

	@Override
	public boolean canBeAppliedByVanillaEnchanter() {
		return false;
	}

	@Override
	public boolean canApplyTogether(Enchantment par1Enchantment) {
		return !(par1Enchantment instanceof EnchantmentDigging) && super.canApplyTogether(par1Enchantment);
	}
}
