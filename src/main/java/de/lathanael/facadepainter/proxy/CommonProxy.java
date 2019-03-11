/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.proxy;

import de.lathanael.facadepainter.init.ItemRegistry;
import de.lathanael.facadepainter.integration.ModIntegration;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {}

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ItemRegistry.registerItems(event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
        ModIntegration.preInit();
    }
}