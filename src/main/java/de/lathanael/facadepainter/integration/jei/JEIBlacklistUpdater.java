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

import java.util.Arrays;

import crazypants.enderio.base.conduit.facade.ItemConduitFacade;

import de.lathanael.facadepainter.init.ItemRegistry;
import de.lathanael.facadepainter.network.SyncedConfig;
import de.lathanael.facadepainter.recipes.ToggleableShapelessRecipe;

import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import net.minecraft.item.ItemStack;

public class JEIBlacklistUpdater {

    private boolean isFacadeClearingRecipeEnabled;
    private boolean isFacadePaintingCategoryHidden;
    private boolean isChamaeleoPaintEnabled;

    public JEIBlacklistUpdater() {
        isChamaeleoPaintEnabled = SyncedConfig.enableChamaeleoPaint;
        isFacadePaintingCategoryHidden = SyncedConfig.hideJEIFacadePaintingRecipeCategory;
        isFacadeClearingRecipeEnabled = SyncedConfig.enableShapelessClearingRecipe;
    }

    public void handleBlacklisting() {
        if (JEIFacadePainterPlugin.instance != null && JEIFacadePainterPlugin.instance.getJEIRuntime() != null) {
            boolean status = SyncedConfig.enableChamaeleoPaint;
            if (status != isChamaeleoPaintEnabled) {
                handleChamaeleoPaintRecipe(status);
                isChamaeleoPaintEnabled = status;
            }

            status = SyncedConfig.hideJEIFacadePaintingRecipeCategory;
            if (status != isFacadePaintingCategoryHidden) {
                handleFacadePaintingCategory(status);
                isFacadePaintingCategoryHidden = status;
            }

            status = SyncedConfig.enableShapelessClearingRecipe;
            if (status != isFacadeClearingRecipeEnabled) {
                handleFacadeClearingRecipe(status);
                isFacadePaintingCategoryHidden = status;
            }
        }
    }

    private void handleFacadePaintingCategory(final boolean hide) {
        if (hide) {
            JEIFacadePainterPlugin.instance.getJEIRuntime().getRecipeRegistry().hideRecipeCategory(FacadePaintingRecipeCategory.UID);
        } else {
            JEIFacadePainterPlugin.instance.getJEIRuntime().getRecipeRegistry().unhideRecipeCategory(FacadePaintingRecipeCategory.UID);
        }
    }

    private void handleChamaeleoPaintRecipe(final boolean hide) {
        IRecipeRegistry registry = JEIFacadePainterPlugin.instance.getJEIRuntime().getRecipeRegistry();
        if (!hide) {
            JEIFacadePainterPlugin.instance.getJEIModRegistry().getIngredientRegistry().removeIngredientsAtRuntime(VanillaTypes.ITEM, Arrays.asList(new ItemStack(ItemRegistry.itemChamaeleoPaint)));
            for (Object recipe : JEIFacadePainterPlugin.instance.getToggleableShapelessRecipes()) {
                if (recipe instanceof ToggleableShapelessRecipe && !(((ToggleableShapelessRecipe) recipe).getRecipeOutput().getItem() instanceof ItemConduitFacade)) {
                    registry.hideRecipe(registry.getRecipeWrapper((ToggleableShapelessRecipe) recipe, VanillaRecipeCategoryUid.CRAFTING) , VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        } else {
            JEIFacadePainterPlugin.instance.getJEIModRegistry().getIngredientRegistry().addIngredientsAtRuntime(VanillaTypes.ITEM, Arrays.asList(new ItemStack(ItemRegistry.itemChamaeleoPaint)));
            for (Object recipe : JEIFacadePainterPlugin.instance.getToggleableShapelessRecipes()) {
                if (recipe instanceof ToggleableShapelessRecipe && !(((ToggleableShapelessRecipe) recipe).getRecipeOutput().getItem() instanceof ItemConduitFacade)) {
                    registry.unhideRecipe(registry.getRecipeWrapper((ToggleableShapelessRecipe) recipe, VanillaRecipeCategoryUid.CRAFTING) , VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
    }

    private void handleFacadeClearingRecipe(final boolean hide) {
        IRecipeRegistry registry = JEIFacadePainterPlugin.instance.getJEIRuntime().getRecipeRegistry();
        if (!hide) {
            for (Object recipeWrapper : JEIFacadePainterPlugin.instance.getToggleableShapelessRecipes()) {
                if (recipeWrapper instanceof FacadeClearingRecipeWrapper) {
                    registry.hideRecipe((FacadeClearingRecipeWrapper) recipeWrapper, VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        } else {
            for (Object recipeWrapper : JEIFacadePainterPlugin.instance.getToggleableShapelessRecipes()) {
                if (recipeWrapper instanceof FacadeClearingRecipeWrapper) {
                    registry.unhideRecipe((FacadeClearingRecipeWrapper) recipeWrapper, VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
    }
}