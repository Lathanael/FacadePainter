/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.init;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.items.ItemChamaeleoPaint;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRegistry {

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemChamaeleoPaint());
    }

    @GameRegistry.ObjectHolder(FacadePainter.MODID + ":chamaeleo_paint")
    public static ItemChamaeleoPaint itemChamaeleoPaint;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        itemChamaeleoPaint.initModel();
    }
}