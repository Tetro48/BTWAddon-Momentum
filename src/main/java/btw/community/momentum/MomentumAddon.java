package btw.community.momentum;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.crafting.recipe.RecipeManager;
import btw.item.BTWItems;
import btw.util.sounds.AddonSoundRegistryEntry;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.tetro48.momentum.EnchantmentMomentum;

public class MomentumAddon extends BTWAddon {
    private static MomentumAddon instance;

    public static final int MOMENTUM_ID = 61;
    public static Enchantment enchantmentMomentum = new EnchantmentMomentum(MOMENTUM_ID, 0, EnumEnchantmentType.digger);

    public MomentumAddon() {
        super();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        RecipeManager.addRecipe(new ItemStack(BTWItems.arcaneScroll, 1, MOMENTUM_ID), new Object[]{" X ", "XSX", " X ", 'X', Item.netherStar, 'S', new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.efficiency.effectId)});
    }
}