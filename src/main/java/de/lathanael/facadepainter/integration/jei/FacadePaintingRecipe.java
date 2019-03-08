package de.lathanael.facadepainter.integration.jei;

import java.util.List;

import net.minecraft.item.ItemStack;

public class FacadePaintingRecipe {

    public final ItemStack output;
    public final List<ItemStack> inputs;

    public FacadePaintingRecipe(ItemStack output, List<ItemStack> inputs) {
        this.output = output;
        this.inputs = inputs;
    }
}