package net.tetro48.momentum;

import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;

public class EnchantmentSwift extends Enchantment {
	public EnchantmentSwift(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {
		super(par1, par2, par3EnumEnchantmentType);
		this.setName("swift");
	}

	@Override
	public boolean canBeAppliedByVanillaEnchanter() {
		return false;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}
}
