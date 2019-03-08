package de.lathanael.facadepainter.integration.jei;

import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import net.minecraft.item.ItemStack;

public class FacadePaintingRecipeWrapper implements IRecipeWrapper {

    private final List<ItemStack> input;
    private final ItemStack output;

    public FacadePaintingRecipeWrapper(@Nonnull FacadePaintingRecipe recipe) {
        this.input = recipe.inputs;
        this.output = recipe.output;
    }
    
    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, input);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}