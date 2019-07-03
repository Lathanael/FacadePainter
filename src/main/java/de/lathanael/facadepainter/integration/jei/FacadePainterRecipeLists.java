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
    private final ItemStack[] FACADES = {
            new ItemStack(ModObject.itemConduitFacade.getItem(), 1),
            new ItemStack(ModObject.itemConduitFacade.getItem(), 1, 1),
            new ItemStack(ModObject.itemConduitFacade.getItem(), 1, 2),
            new ItemStack(ModObject.itemConduitFacade.getItem(), 1, 3)};
    private final @Nonnull ItemStack CHAMAELEO_PAINT = new ItemStack(ItemRegistry.itemChamaeleoPaint, 1);

    public FacadePainterRecipeLists(IModRegistry registry) {
        generate(registry);
    }

    private void generate(IModRegistry registry) {
        Iterator<ItemStack> listIterator = registry.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM).iterator();
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
            boolean isRecipe;
            List <ItemStack> results;
            for (ItemStack facade : FACADES) {
                results = new ArrayList<>();
                for (IMachineRecipe rec : painterRecipes.values()) {
                    if (rec instanceof AbstractPainterTemplate<?>) {
                        AbstractPainterTemplate<?> recipe = (AbstractPainterTemplate<?>) rec;
                        try {
                            isRecipe = recipe.isRecipe(tempStack, facade);
                        } catch (Exception ex) {
                            isRecipe = false;
                        }
                        if (isRecipe) {
                            try {
                                VALID_RECIPES.add(new FacadePaintingRecipe(recipe.getCompletedResult(tempStack, facade), Arrays.asList(tempStack, facade, CHAMAELEO_PAINT)));
                                results.add(recipe.getCompletedResult(tempStack, facade));
                            } catch (Exception ex) {
                                FacadePainter.logger.debug("Empty ingredients list supplied for: " + tempStack.toString());
                            }
                        }

                    }
                }
                FACADE_RESULTS.add(results);
            }
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
        for (int i = 0; i < 4; i++ ) {
            pseudoClearingList.add(new FacadeClearingRecipe(FACADES[i], Arrays.asList(FACADES[i])));
        }
        return pseudoClearingList;
    }
}