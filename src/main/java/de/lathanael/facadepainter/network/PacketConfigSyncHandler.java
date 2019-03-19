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
import de.lathanael.facadepainter.integration.ModIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfigSyncHandler implements IMessageHandler<PacketConfigSync, IMessage> {

    @Override
    public IMessage onMessage(PacketConfigSync message, MessageContext context) {
        IThreadListener iThreadListener = Minecraft.getMinecraft();
        iThreadListener.addScheduledTask(new Runnable() {

            @Override
            public void run() {
                FacadePainter.logger.info("Connected to server - synching server configs to client");
                SyncedConfig.enableChamaeleoPaint = message.enableChamaeleoPaint;
                SyncedConfig.hideJEIFacadePaintingRecipeCategory = message.hideJEIFacadePaintingRecipeCategory;
                SyncedConfig.useChamaeleoPaint = message.useChamaeleoPaint;
                SyncedConfig.enableShapelessClearingRecipe = message.enableShapelessClearingRecipe;
                if (ModIntegration.isJEILoaded) {
                    ModIntegration.jeiBlacklistUpdater.handleBlacklisting();
                }
                FacadePainter.logger.info("Connected to server - server configs synched");
            }
        });
        return null;
    }
}