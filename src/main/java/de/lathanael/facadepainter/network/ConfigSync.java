package de.lathanael.facadepainter.network;

import de.lathanael.facadepainter.config.Configs;

import io.netty.buffer.ByteBuf;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

@Mod.EventBusSubscriber
public class ConfigSync implements IMessage {

    public static boolean useChamaeleoPaint = Configs.recipes.useChamaeleoPaint;
    
    @Override
    public void fromBytes(ByteBuf buf) {
        // TODO Auto-generated method stub

    }

    @Override
    public void toBytes(ByteBuf buf) {
        // TODO Auto-generated method stub

    }

    @SubscribeEvent
    public static void EventClientDisconnectionFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        useChamaeleoPaint = Configs.recipes.useChamaeleoPaint;
    }
}