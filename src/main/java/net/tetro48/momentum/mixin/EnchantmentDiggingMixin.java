package net.tetro48.momentum.mixin;

import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentDigging;
import net.minecraft.src.EnumEnchantmentType;
import net.tetro48.momentum.EnchantmentMomentum;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentDigging.class)
public abstract class EnchantmentDiggingMixin extends Enchantment {
	protected EnchantmentDiggingMixin(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {
		super(par1, par2, par3EnumEnchantmentType);
	}

	@Override
	public boolean canApplyTogether(Enchantment par1Enchantment) {
		return !(par1Enchantment instanceof EnchantmentMomentum) && super.canApplyTogether(par1Enchantment);
	}
}
