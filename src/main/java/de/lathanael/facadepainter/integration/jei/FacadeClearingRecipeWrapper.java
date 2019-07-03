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

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.integration.ModIntegration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class FacadeClearingRecipeWrapper implements ICustomCraftingRecipeWrapper {

    private final List<ItemStack> input;
    private final ItemStack output;

    public FacadeClearingRecipeWrapper(@Nonnull FacadeClearingRecipe recipe) {
        this.input = recipe.inputs;
        this.output = recipe.output;
    }

    @Override
    public void getIngredients(final IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, input);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }

    @Override
    public void setRecipe(final IRecipeLayout recipeLayout, final IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 18, 18);
        recipeLayout.getItemStacks().init(1, false, 94, 18);

        List<ItemStack> inputs = new ArrayList<>();
        if (ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0).getItemDamage() == 0) {
            inputs = ModIntegration.recipeList.getClearingRecipeList().get(0);
        } else if (ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0).getItemDamage() == 1) {
            inputs = ModIntegration.recipeList.getClearingRecipeList().get(1);
        } else if (ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0).getItemDamage() == 2) {
            inputs = ModIntegration.recipeList.getClearingRecipeList().get(2);
        } else if (ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0).getItemDamage() == 3) {
            inputs = ModIntegration.recipeList.getClearingRecipeList().get(3);
        }
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        recipeLayout.getItemStacks().set(0, inputs);
        recipeLayout.getItemStacks().set(1, outputs.get(0).get(0));
        recipeLayout.setShapeless();
        recipeLayout.getItemStacks().addTooltipCallback(new PaintedFacadeTooltip());
    }

     private class PaintedFacadeTooltip implements ITooltipCallback<ItemStack> {

        @Override
        public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
            if (slotIndex == 0 && ingredient.getItem() instanceof ItemConduitFacade) {
                tooltip.add(I18n.format("gui."+ FacadePainter.MODID + ".jei.facade_clearing.tooltip"));
            }
        }
     }
}