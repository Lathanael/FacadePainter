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

public class FacadePaintingRecipe {

    public final ItemStack output;
    public final List<ItemStack> inputs;

    public FacadePaintingRecipe(ItemStack output, List<ItemStack> inputs) throws Exception {
        this.output = output;
        if (inputs.isEmpty()) {
            throw new Exception("Empty ingredients list is not allowed!");
        }
        this.inputs = inputs;
    }
}