package de.lathanael.facadepainter.integration.jei;

import de.lathanael.facadepainter.config.Configs;
import de.lathanael.facadepainter.init.ItemRegistry;

import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class JEIBlacklistUpdater {

    private boolean isFacadePaintingCategoryHidden;
    private boolean isChamaeleoPaintEnabled;
    
    public JEIBlacklistUpdater() {
        isChamaeleoPaintEnabled = Configs.features.enableChamaeleoPaint;
        isFacadePaintingCategoryHidden = Configs.features.hideJEIFacadePaintingRecipeCategory;
    }

    public void handleBlacklisting() {
        boolean status = Configs.features.enableChamaeleoPaint;
        if (status != isChamaeleoPaintEnabled) {
            handleChamaeleoPaintRecipe(status);
            isChamaeleoPaintEnabled = status;
        }

        status = Configs.features.hideJEIFacadePaintingRecipeCategory;
        if (status != isFacadePaintingCategoryHidden) {
            handleFacadePaintingCategory(status);
            isFacadePaintingCategoryHidden = status;
        }
    }

    private void handleFacadePaintingCategory(final boolean hide) {
        if (hide) {
            JEIFacadePainterPlugin.INSTANCE.getJEIRuntime().getRecipeRegistry().hideRecipeCategory(FacadePaintingRecipeCategory.UID);
        } else {
            JEIFacadePainterPlugin.INSTANCE.getJEIRuntime().getRecipeRegistry().unhideRecipeCategory(FacadePaintingRecipeCategory.UID);
        }
    }

    private void handleChamaeleoPaintRecipe(final boolean hide) {
        if (!hide) {
            JEIFacadePainterPlugin.INSTANCE.getJEIHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ItemRegistry.itemChamaeleoPaint, 1, OreDictionary.WILDCARD_VALUE));
            IRecipeRegistry registry = JEIFacadePainterPlugin.INSTANCE.getJEIRuntime().getRecipeRegistry();
            for (IRecipeWrapper recipe : JEIFacadePainterPlugin.INSTANCE.getToggleableShapelessRecipes()) {
                registry.hideRecipe(recipe, VanillaRecipeCategoryUid.CRAFTING);
            }
        } else {
            JEIFacadePainterPlugin.INSTANCE.getJEIHelpers().getIngredientBlacklist().removeIngredientFromBlacklist(new ItemStack(ItemRegistry.itemChamaeleoPaint, 1, OreDictionary.WILDCARD_VALUE));
            IRecipeRegistry registry = JEIFacadePainterPlugin.INSTANCE.getJEIRuntime().getRecipeRegistry();
            for (IRecipeWrapper recipe : JEIFacadePainterPlugin.INSTANCE.getToggleableShapelessRecipes()) {
                registry.unhideRecipe(recipe, VanillaRecipeCategoryUid.CRAFTING);
            }
        }
    }
}