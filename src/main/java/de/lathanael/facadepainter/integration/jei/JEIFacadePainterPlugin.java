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

import de.lathanael.facadepainter.init.ItemRegistry;
import de.lathanael.facadepainter.integration.ModIntegration;
import de.lathanael.facadepainter.network.SyncedConfig;
import de.lathanael.facadepainter.recipes.ToggleableShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class JEIFacadePainterPlugin implements IModPlugin {

    public static JEIFacadePainterPlugin INSTANCE;

    private IJeiRuntime jeiRuntime;
    private IJeiHelpers jeiHelpers;
    private List<Object> toggleableShapelessRecipes = new ArrayList<>();

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new FacadePaintingRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        INSTANCE = this;
        jeiHelpers = registry.getJeiHelpers();
        // Hide Chamaeleo Paint item if it is not enabled
        if (!SyncedConfig.enableChamaeleoPaint) {
            jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ItemRegistry.itemChamaeleoPaint, 1, OreDictionary.WILDCARD_VALUE));
        }
        // Recipes to clear a painted facade
        registry.addRecipes(ModIntegration.recipeList.getPseudoClearingRecipeList(), VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(FacadeClearingRecipe.class, recipe -> new FacadeClearingRecipeWrapper(recipe), VanillaRecipeCategoryUid.CRAFTING);
        // Facade painting recipe
        registry.addRecipes(ModIntegration.recipeList.getRecipeList(), FacadePaintingRecipeCategory.UID);
        registry.handleRecipes(FacadePaintingRecipe.class, recipe -> new FacadePaintingRecipeWrapper(recipe), FacadePaintingRecipeCategory.UID);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
        // Fetch the wrapper/recipe object for ToggleableShapelessRecipe and FacadeClearing recipes and also hide them if their recipe is disabled
        for (Entry<ResourceLocation, IRecipe> recipeEntry : ForgeRegistries.RECIPES.getEntries()) {
            IRecipe recipe =  recipeEntry.getValue();
            if (recipe instanceof ToggleableShapelessRecipe) {
                toggleableShapelessRecipes.add(recipe);
                if (!SyncedConfig.enableChamaeleoPaint && !(recipe.getRecipeOutput().getItem() instanceof ItemConduitFacade)) {
                    jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING), VanillaRecipeCategoryUid.CRAFTING);
                }
                if (recipe.getRecipeOutput().getItem() instanceof ItemConduitFacade) {
                    jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING), VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
        for (Object recipeWrapper : jeiRuntime.getRecipeRegistry().getRecipeWrappers(jeiRuntime.getRecipeRegistry().getRecipeCategory(VanillaRecipeCategoryUid.CRAFTING))) {
            if (recipeWrapper instanceof FacadeClearingRecipeWrapper) {
                toggleableShapelessRecipes.add(recipeWrapper);
                if (!SyncedConfig.enableShapelessClearingRecipe) {
                    jeiRuntime.getRecipeRegistry().hideRecipe((FacadeClearingRecipeWrapper) recipeWrapper, VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
        // Hide the facade painting recipe category if the config value is set to true
        if (SyncedConfig.hideJEIFacadePaintingRecipeCategory) {
            jeiRuntime.getRecipeRegistry().hideRecipeCategory(FacadePaintingRecipeCategory.UID);
        }
    }

    public IJeiRuntime getJEIRuntime() {
        return jeiRuntime;
    }

    public IJeiHelpers getJEIHelpers() {
        return jeiHelpers;
    }

    public List<Object> getToggleableShapelessRecipes() {
        return toggleableShapelessRecipes;
    }
}