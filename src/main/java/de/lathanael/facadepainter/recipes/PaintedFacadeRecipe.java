/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import crazypants.enderio.base.conduit.facade.ItemConduitFacade;
import crazypants.enderio.base.paint.PaintUtil;
import crazypants.enderio.base.recipe.IMachineRecipe;
import crazypants.enderio.base.recipe.MachineRecipeRegistry;
import crazypants.enderio.base.recipe.painter.AbstractPainterTemplate;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.init.ItemRegistry;
import de.lathanael.facadepainter.network.SyncedConfig;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class PaintedFacadeRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private final ResourceLocation group;
    private final ItemStack output;
    public final NonNullList<Ingredient> input;
    
    public PaintedFacadeRecipe(@Nullable final ResourceLocation group, final NonNullList<Ingredient> input, final ItemStack output) {
        if (group != null) {
            this.group = group;
        } else {
            this.group = new ResourceLocation(FacadePainter.MODID);
        }        
        this.input = input;
        this.output = output.copy();
    }

    @Override
    public boolean matches(final InventoryCrafting inventory, final World worldIn) {
        int ingredientCount = 0;
        ItemStack facade = null;
        ItemStack paintSource = null;
        ItemStack chamaeleo = null;
        
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                ++ingredientCount;
                // Get the Facade ItemStack
                if (itemstack.getItem() instanceof ItemConduitFacade) {
                    facade = itemstack;
                    continue;
                }
                // Get the Chamaeleo Paint item
                if (itemstack.getItem() == ItemRegistry.itemChamaeleoPaint) {
                    chamaeleo = itemstack;
                    continue;
                }
                // Since it is neither the facade nor the Chamaeleo Paint it must be the intended paint source,
                // we check later if it is valid! This gets overwritten if more than one different stack is
                // present in the crafting grid but the ingredient count check will catch that.
                paintSource = itemstack;
            }
        }
        if (ingredientCount < 2) {
            return false;
        } else if (ingredientCount > 3) {
            return false;
        } else if (ingredientCount < 3 && SyncedConfig.useChamaeleoPaint) {
            return false;
        } else if (ingredientCount > 2 && !SyncedConfig.useChamaeleoPaint) {
            return false;
        }
        if (facade == null || paintSource == null) {
            return false;
        }
        if (SyncedConfig.useChamaeleoPaint && chamaeleo == null) {
            return false;
        }

        // Check the EIO painter recipe registry if a valid recipe exists
        Map<String, IMachineRecipe> painterRecipes = MachineRecipeRegistry.instance.getRecipesForMachine(MachineRecipeRegistry.PAINTER);
        for (IMachineRecipe rec : painterRecipes.values()) {
            if (rec instanceof AbstractPainterTemplate<?>) {
                AbstractPainterTemplate<?> temp = (AbstractPainterTemplate<?>) rec;
                if (temp.isPartialRecipe(paintSource, facade)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inventory) {
        NonNullList<ItemStack> returnStack = NonNullList.<ItemStack>withSize(inventory.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < returnStack.size(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof ItemConduitFacade || itemstack.getItem() == ItemRegistry.itemChamaeleoPaint) {
                    continue;
                }
                // TODO: Find a better (?) solution for stacks with a size greater than 1.
                if (itemstack.getCount() > 1) {
                    itemstack = itemstack.copy();
                    itemstack.setCount(1);
                    returnStack.set(i, itemstack);
                } else {
                    returnStack.set(i, itemstack.copy());
                }
            }
        }

        return returnStack;
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inventory) {
        ItemStack facade = null;
        ItemStack paintSource = null;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof ItemConduitFacade) {
                    facade = itemstack.copy();
                    continue;
                }
                if (itemstack.getItem() == ItemRegistry.itemChamaeleoPaint) {
                    continue;
                }
                paintSource = itemstack;
            }
        }
        Block paintBlock = PaintUtil.getBlockFromItem(paintSource);
        if (paintBlock == null) {
            return ItemStack.EMPTY;
        }
        IBlockState paintState = PaintUtil.Block$getBlockFromItem_stack$getItem___$getStateFromMeta_stack$getMetadata___(paintSource, paintBlock);
        if (paintState == null) {
            return ItemStack.EMPTY;
        }
        facade.setCount(1);
        PaintUtil.setPaintSource(facade, paintSource);
        PaintUtil.setSourceBlock(facade, paintState);

        return facade;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public String getGroup() {
        return group.toString();
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return input;
    }
    
    @Override
    public boolean canFit(final int width, final int height) {
        if (SyncedConfig.useChamaeleoPaint) {
            return width * height > 2;
        }
        return width * height > 1;
    }
    
    public static class Factory implements IRecipeFactory {

        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json) {
            final String group = JsonUtils.getString(json, "group", "");
            final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

            final NonNullList<Ingredient> ingredients = NonNullList.create();
            ingredients.add(CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "facade"), context));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for facade painting recipe");
            }

            return new PaintedFacadeRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, result);
        }
    }
}