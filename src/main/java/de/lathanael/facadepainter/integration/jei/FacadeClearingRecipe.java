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