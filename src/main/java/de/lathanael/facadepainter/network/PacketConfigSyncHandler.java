package de.lathanael.facadepainter.network;

import de.lathanael.facadepainter.FacadePainter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfigSyncHandler implements IMessageHandler<PacketConfigSync, IMessage> {

    @Override
    public IMessage onMessage(PacketConfigSync message, MessageContext ctx) {
        IThreadListener iThreadListener = Minecraft.getMinecraft();
        iThreadListener.addScheduledTask(new Runnable() {

            @Override
            public void run() {
                FacadePainter.logger.info("Connected to server - synching server configs to client");
                SyncedConfig.enableChamaeleoPaint = message.enableChamaeleoPaint;
                SyncedConfig.hideJEIFacadePaintingRecipeCategory = message.hideJEIFacadePaintingRecipeCategory;
                SyncedConfig.useChamaeleoPaint = message.useChamaeleoPaint;
                SyncedConfig.enableShapelessClearingRecipe = message.enableShapelessClearingRecipe;
                FacadePainter.logger.info("Connected to server - server configs synched");
            }
        });
        return null;
    }
}