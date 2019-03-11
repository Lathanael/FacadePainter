package de.lathanael.facadepainter.integration.jei;

import java.util.List;

import net.minecraft.item.ItemStack;

public class FacadeClearingRecipe {

    public final ItemStack output;
    public final List<ItemStack> inputs;

    public FacadeClearingRecipe(ItemStack output, List<ItemStack> inputs){
        this.output = output;
        this.inputs = inputs;
    }
}