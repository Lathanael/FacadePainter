/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.network;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.config.Configs;

import io.netty.buffer.ByteBuf;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketConfigSync implements IMessage {

    public boolean enableChamaeleoPaint;
    public boolean hideJEIFacadePaintingRecipeCategory;
    public boolean useChamaeleoPaint;
    public boolean enableShapelessClearingRecipe;

    public PacketConfigSync() {
        MinecraftForge.EVENT_BUS.register(PacketConfigSync.class);
        enableChamaeleoPaint = Configs.features.enableChamaeleoPaint;
        hideJEIFacadePaintingRecipeCategory = Configs.features.hideJEIFacadePaintingRecipeCategory;
        useChamaeleoPaint = Configs.recipes.useChamaeleoPaint;
        enableShapelessClearingRecipe = Configs.recipes.enableShapelessClearingRecipe;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        enableChamaeleoPaint = buf.readBoolean();
        hideJEIFacadePaintingRecipeCategory = buf.readBoolean();
        useChamaeleoPaint = buf.readBoolean();
        enableShapelessClearingRecipe = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(enableChamaeleoPaint);
        buf.writeBoolean(hideJEIFacadePaintingRecipeCategory);
        buf.writeBoolean(useChamaeleoPaint);
        buf.writeBoolean(enableShapelessClearingRecipe);
    }

    @SubscribeEvent
    public static void onPlayerLeftServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        FacadePainter.logger.info("Disconnected from server - restoring client side configs");
        SyncedConfig.enableChamaeleoPaint = Configs.features.enableChamaeleoPaint;
        SyncedConfig.hideJEIFacadePaintingRecipeCategory = Configs.features.hideJEIFacadePaintingRecipeCategory;
        SyncedConfig.useChamaeleoPaint = Configs.recipes.useChamaeleoPaint;
        SyncedConfig.enableShapelessClearingRecipe = Configs.recipes.enableShapelessClearingRecipe;
        FacadePainter.logger.info("Disconnected from server - client side configs restored");
    }
}