/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.integration.jei;

import crazypants.enderio.base.conduit.facade.ItemConduitFacade;

import de.lathanael.facadepainter.config.Configs;
import de.lathanael.facadepainter.init.ItemRegistry;
import de.lathanael.facadepainter.recipes.ToggleableShapelessRecipe;

import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

public class JEIBlacklistUpdater {

    private boolean isFacadeClearingRecipeEnabled;
    private boolean isFacadePaintingCategoryHidden;
    private boolean isChamaeleoPaintEnabled;
    
    public JEIBlacklistUpdater() {
        isChamaeleoPaintEnabled = Configs.features.enableChamaeleoPaint;
        isFacadePaintingCategoryHidden = Configs.features.hideJEIFacadePaintingRecipeCategory;
        isFacadeClearingRecipeEnabled = Configs.recipes.enableShapelessClearingRecipe;
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

        status = Configs.recipes.enableShapelessClearingRecipe;
        if (status != isFacadeClearingRecipeEnabled) {
            handleFacadeClearingRecipe(status);
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
        IRecipeRegistry registry = JEIFacadePainterPlugin.INSTANCE.getJEIRuntime().getRecipeRegistry();
        if (!hide) {
            JEIFacadePainterPlugin.INSTANCE.getJEIHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ItemRegistry.itemChamaeleoPaint, 1, OreDictionary.WILDCARD_VALUE));
            for (Object recipe : JEIFacadePainterPlugin.INSTANCE.getToggleableShapelessRecipes()) {
                if (recipe instanceof ToggleableShapelessRecipe && !(((ToggleableShapelessRecipe) recipe).getRecipeOutput().getItem() instanceof ItemConduitFacade)) {
                    registry.hideRecipe(registry.getRecipeWrapper((ToggleableShapelessRecipe) recipe, VanillaRecipeCategoryUid.CRAFTING) , VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        } else {
            JEIFacadePainterPlugin.INSTANCE.getJEIHelpers().getIngredientBlacklist().removeIngredientFromBlacklist(new ItemStack(ItemRegistry.itemChamaeleoPaint, 1, OreDictionary.WILDCARD_VALUE));
            for (Object recipe : JEIFacadePainterPlugin.INSTANCE.getToggleableShapelessRecipes()) {
                if (recipe instanceof ToggleableShapelessRecipe && !(((ToggleableShapelessRecipe) recipe).getRecipeOutput().getItem() instanceof ItemConduitFacade)) {
                    registry.unhideRecipe(registry.getRecipeWrapper((ToggleableShapelessRecipe) recipe, VanillaRecipeCategoryUid.CRAFTING) , VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
    }

    private void handleFacadeClearingRecipe(final boolean hide) {
        IRecipeRegistry registry = JEIFacadePainterPlugin.INSTANCE.getJEIRuntime().getRecipeRegistry();
        if (!hide) {
            for (Object recipeWrapper : JEIFacadePainterPlugin.INSTANCE.getToggleableShapelessRecipes()) {
                if (recipeWrapper instanceof FacadeClearingRecipeWrapper) {
                    registry.hideRecipe((FacadeClearingRecipeWrapper) recipeWrapper, VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        } else {
            for (Object recipeWrapper : JEIFacadePainterPlugin.INSTANCE.getToggleableShapelessRecipes()) {
                if (recipeWrapper instanceof FacadeClearingRecipeWrapper) {
                    registry.unhideRecipe((FacadeClearingRecipeWrapper) recipeWrapper, VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
    }
}