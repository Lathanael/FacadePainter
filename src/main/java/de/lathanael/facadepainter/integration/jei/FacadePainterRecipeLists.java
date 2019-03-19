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
import crazypants.enderio.base.init.ModObject;
import crazypants.enderio.base.paint.IPaintable;
import crazypants.enderio.base.paint.PaintUtil;
import crazypants.enderio.base.recipe.IMachineRecipe;
import crazypants.enderio.base.recipe.MachineRecipeRegistry;
import crazypants.enderio.base.recipe.painter.AbstractPainterTemplate;
import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.init.ItemRegistry;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.FluidUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FacadePainterRecipeLists {

    private final @Nonnull List<FacadePaintingRecipe> VALID_RECIPES = new ArrayList<>();
    private final @Nonnull List<List<ItemStack>> FACADE_RESULTS = new ArrayList<>();
    private final @Nonnull ItemStack FACADE = new ItemStack(ModObject.itemConduitFacade.getItem(), 1);
    private final @Nonnull ItemStack TRANSPARENT_FACADE = new ItemStack(ModObject.itemConduitFacade.getItem(), 1, 2);
    private final @Nonnull ItemStack HARDENED_FACADE = new ItemStack(ModObject.itemConduitFacade.getItem(), 1, 1);
    private final @Nonnull ItemStack TRANSPARENT_HARDENED_FACADE = new ItemStack(ModObject.itemConduitFacade.getItem(), 1, 3);
    private final @Nonnull ItemStack CHAMAELEO_PAINT = new ItemStack(ItemRegistry.itemChamaeleoPaint, 1);

    public FacadePainterRecipeLists(IModRegistry registry) {
        generate(registry);
    }

    private void generate(IModRegistry registry) {
        Iterator<ItemStack> listIterator = registry.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM).iterator();
        List<ItemStack> facadeResults = new ArrayList<>();
        List<ItemStack> transparentFacadeResults = new ArrayList<>();
        List<ItemStack> hardenedFacadeResults = new ArrayList<>();
        List<ItemStack> transparentHardenedFacadeResults = new ArrayList<>();

        while (listIterator.hasNext()) {
            ItemStack tempStack = listIterator.next();
            if (tempStack.isEmpty()) {
                continue;
            }
            if (tempStack.getItem() instanceof ItemConduitFacade) {
                continue;
            }
            FluidStack tempFluid = FluidUtil.getFluidTypeFromItem(tempStack);
            if (tempFluid != null && tempFluid.getFluid() != null) {
                continue;
            }
            Block tempBlock = PaintUtil.getBlockFromItem(tempStack);
            if (tempBlock != null && tempBlock instanceof IPaintable) {
                continue;
            }
            if (tempBlock == Blocks.AIR || tempStack.getItem() == Items.AIR) {
                continue;
            }
            Map<String, IMachineRecipe> painterRecipes = MachineRecipeRegistry.instance.getRecipesForMachine(MachineRecipeRegistry.PAINTER);
            boolean isRecipe = false;
            for (IMachineRecipe rec : painterRecipes.values()) {
                if (rec instanceof AbstractPainterTemplate<?>) {
                    AbstractPainterTemplate<?> recipe = (AbstractPainterTemplate<?>) rec;
                    try {
                        isRecipe = recipe.isRecipe(tempStack, FACADE);
                    } catch (Exception ex) {
                        isRecipe = false;
                    }
                    if (isRecipe) {
                        try {
                            VALID_RECIPES.add(new FacadePaintingRecipe(recipe.getCompletedResult(tempStack, FACADE), Arrays.asList(tempStack, FACADE, CHAMAELEO_PAINT)));
                            facadeResults.add(recipe.getCompletedResult(tempStack, FACADE));
                        } catch (Exception ex) {
                            FacadePainter.logger.debug("Empty ingredients list supplied for: " + tempStack.toString());
                        }
                    }
                    try {
                        isRecipe = recipe.isRecipe(tempStack, HARDENED_FACADE);
                    } catch (Exception ex) {
                        isRecipe = false;
                    }
                    if (isRecipe) {
                        hardenedFacadeResults.add(recipe.getCompletedResult(tempStack, HARDENED_FACADE));
                    }
                    try {
                        isRecipe = recipe.isRecipe(tempStack, TRANSPARENT_FACADE);
                    } catch (Exception ex) {
                        isRecipe = false;
                    }
                    if (isRecipe) {
                        transparentFacadeResults.add(recipe.getCompletedResult(tempStack, TRANSPARENT_FACADE));
                    }
                    try {
                        isRecipe = recipe.isRecipe(tempStack, TRANSPARENT_HARDENED_FACADE);
                    } catch (Exception ex) {
                        isRecipe = false;
                    }
                    if (isRecipe) {
                        transparentHardenedFacadeResults.add(recipe.getCompletedResult(tempStack, TRANSPARENT_HARDENED_FACADE));
                    }
                }
            }
            FACADE_RESULTS.add(facadeResults);
            FACADE_RESULTS.add(hardenedFacadeResults);
            FACADE_RESULTS.add(transparentFacadeResults);
            FACADE_RESULTS.add(transparentHardenedFacadeResults);
        }
    }

    public List<FacadePaintingRecipe> getRecipeList() {
        return VALID_RECIPES;
    }

    public List<List<ItemStack>> getClearingRecipeList() {
        return FACADE_RESULTS;
    }

    public List<FacadeClearingRecipe> getPseudoClearingRecipeList() {
        List<FacadeClearingRecipe> pseudoClearingList = new ArrayList<>();
        pseudoClearingList.add(new FacadeClearingRecipe(FACADE, Arrays.asList(FACADE)));
        pseudoClearingList.add(new FacadeClearingRecipe(HARDENED_FACADE, Arrays.asList(HARDENED_FACADE)));
        pseudoClearingList.add(new FacadeClearingRecipe(TRANSPARENT_FACADE, Arrays.asList(TRANSPARENT_FACADE)));
        pseudoClearingList.add(new FacadeClearingRecipe(TRANSPARENT_HARDENED_FACADE, Arrays.asList(TRANSPARENT_HARDENED_FACADE)));
        return pseudoClearingList;
    }
}