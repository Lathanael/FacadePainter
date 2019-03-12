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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import crazypants.enderio.base.conduit.facade.ItemConduitFacade;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.network.SyncedConfig;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ToggleableShapelessRecipe extends ShapelessOreRecipe {
    
    public ToggleableShapelessRecipe(ResourceLocation group, NonNullList<Ingredient> input, ItemStack result) {
        super(group, input, result);
    }

    @Override
    public boolean matches(final InventoryCrafting inventory, final World worldIn) {
        int ingredientCount = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                ++ingredientCount;
                if (!SyncedConfig.enableShapelessClearingRecipe && itemstack.getItem() instanceof ItemConduitFacade) {
                    return false;
                }
            }
        }
        if (!SyncedConfig.enableChamaeleoPaint && ingredientCount > 3) {
            return false;
        }

        return super.matches(inventory, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof ItemConduitFacade) {
                    return new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage());
                }
            }
        }

        return super.getCraftingResult(inventory);
    }

    public static class Factory implements IRecipeFactory {

        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json) {
            final String group = JsonUtils.getString(json, "group", "");
            final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

            final NonNullList<Ingredient> ingredients = NonNullList.create();
            for (JsonElement element : JsonUtils.getJsonArray(json, "ingredients")) {
                ingredients.add(CraftingHelper.getIngredient(element, context));
            }
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for toggleable shapeless recipe");
            }

            return new ToggleableShapelessRecipe(group.isEmpty() ? new ResourceLocation(FacadePainter.MODID) : new ResourceLocation(group), ingredients, result);
        }
    }
}