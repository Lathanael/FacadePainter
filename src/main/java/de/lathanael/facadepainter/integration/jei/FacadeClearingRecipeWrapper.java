package de.lathanael.facadepainter.integration.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import de.lathanael.facadepainter.integration.ModIntegration;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;

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
    }
}