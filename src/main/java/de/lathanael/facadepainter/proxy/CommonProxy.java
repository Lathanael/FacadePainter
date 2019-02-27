package de.lathanael.facadepainter.proxy;

import de.lathanael.facadepainter.init.ItemRegistry;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        // TODO Auto-generated method stub
        
    }

    public void init(FMLInitializationEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ItemRegistry.registerItems(event);
    }
}
