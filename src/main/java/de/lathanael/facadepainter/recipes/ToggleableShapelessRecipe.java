package de.lathanael.facadepainter.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.config.Configs;

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
        if (!Configs.features.enableChamaeleoPaint) {
            return false;
        }
        return super.matches(inventory, worldIn);
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