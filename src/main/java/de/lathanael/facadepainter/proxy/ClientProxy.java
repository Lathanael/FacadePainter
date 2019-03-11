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

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ItemRegistry.initModels();
    }
}