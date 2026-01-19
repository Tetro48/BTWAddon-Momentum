package btw.community.momentum;

import api.AddonHandler;
import api.BTWAddon;
import btw.crafting.recipe.RecipeManager;
import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.tetro48.momentum.EnchantmentMobSight;
import net.tetro48.momentum.EnchantmentMomentum;
import net.tetro48.momentum.EnchantmentSwift;

import java.util.Arrays;

public class MomentumAddon extends BTWAddon {
    private static MomentumAddon instance;

    public static final int MOMENTUM_ID = 61;
    public static final int SWIFT_ID = 62;
    public static final int MOB_SIGHT_ID = 63;
    public static Enchantment enchantmentMomentum = new EnchantmentMomentum(MOMENTUM_ID, 0, EnumEnchantmentType.digger);
    public static Enchantment enchantmentSwift = new EnchantmentSwift(SWIFT_ID, 0, EnumEnchantmentType.digger);
    public static Enchantment enchantmentMobSight = new EnchantmentMobSight(MOB_SIGHT_ID, 0, EnumEnchantmentType.all);

    public MomentumAddon() {
        super();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        RecipeManager.addShapelessRecipe(new ItemStack(BTWItems.arcaneScroll, 1, MOMENTUM_ID), new Object[]{
                Item.netherStar, new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.efficiency.effectId), BTWItems.soulFlux, Item.ingotGold}
        );
        RecipeManager.addShapelessRecipe(new ItemStack(BTWItems.arcaneScroll, 1, SWIFT_ID), new Object[]{
                Item.netherStar, new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.efficiency.effectId), BTWItems.soulFlux, Item.sugar}
        );
        RecipeManager.addShapelessRecipe(new ItemStack(BTWItems.arcaneScroll, 1, MOB_SIGHT_ID), new Object[]{
                Item.netherStar, Item.paper, BTWItems.soulFlux, Item.spiderEye, Item.spiderEye, Item.spiderEye, Item.spiderEye, Item.spiderEye, Item.spiderEye
        });
        BTWItems.enderSpectacles.setInfernalMaxNumEnchants(1);
        BTWItems.enderSpectacles.setInfernalMaxEnchantmentCost(30);
    }
}