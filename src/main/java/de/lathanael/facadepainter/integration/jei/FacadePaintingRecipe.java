package de.lathanael.facadepainter.integration.jei;

import java.util.ArrayList;
import java.util.List;

import de.lathanael.facadepainter.config.Configs;
import de.lathanael.facadepainter.init.ItemRegistry;

import net.minecraft.item.ItemStack;

public class FacadePaintingRecipe {

    public final ItemStack output;
    public final List<ItemStack> inputs;
    public final ItemStack chamaeleoPaint = new ItemStack(ItemRegistry.itemChamaeleoPaint, 1);

    public FacadePaintingRecipe(ItemStack output, List<ItemStack> inputs) {
        this.output = output;
        this.inputs = getRecipe(inputs);
    }

    private List<ItemStack> getRecipe(List<ItemStack> list) {
        List<ItemStack> recipe = new ArrayList<ItemStack>(3);
        recipe.addAll(list);
        if (Configs.recipes.useChamaeleoPaint) {
            recipe.add(chamaeleoPaint);
        } else {
            recipe.add(ItemStack.EMPTY);
        }
        return recipe; 
    }
}