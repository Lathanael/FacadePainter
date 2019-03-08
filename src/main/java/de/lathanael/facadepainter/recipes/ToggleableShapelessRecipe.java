package de.lathanael.facadepainter.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.config.Configs;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.RecipeItemHelper;
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
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ToggleableShapelessRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private final ResourceLocation group;
    private final ItemStack output;
    private final NonNullList<Ingredient> inputs;
    private boolean isSimple = true;

    public ToggleableShapelessRecipe(@Nullable final ResourceLocation group, final NonNullList<Ingredient> inputs, final ItemStack output) {
        if (group != null) {
            this.group = group;
        } else {
            this.group = new ResourceLocation(FacadePainter.MODID);
        }        
        this.inputs = inputs;
        this.output = output.copy();
        for (Ingredient i : inputs)
            this.isSimple &= i.isSimple();
    }

    @Override
    public boolean matches(final InventoryCrafting inventory, final World worldIn) {
        if (!Configs.features.enableChamaeleoPaint) {
            return false;
        }
        int ingredientCount = 0;
        RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
        List<ItemStack> items = Lists.newArrayList();

        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                ++ingredientCount;
                if (this.isSimple) {
                    recipeItemHelper.accountStack(itemstack, 1);
                } else {
                    items.add(itemstack);
                }
            }
        }

        if (ingredientCount != this.inputs.size()) {
            return false;
        }
        if (this.isSimple) {
            return recipeItemHelper.canCraft(this, null);
        }

        return RecipeMatcher.findMatches(items, this.inputs) != null;
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inventory) {
        return output.copy();
    }

    @Override
    public boolean canFit(final int width, final int height) {
        return width * height >= this.inputs.size();
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
        return inputs;
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

            return new ToggleableShapelessRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, result);
        }
    }
}