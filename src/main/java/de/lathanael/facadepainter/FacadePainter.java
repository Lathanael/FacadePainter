package de.lathanael.facadepainter;

import de.lathanael.facadepainter.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

//import crazypants.enderio.base.recipe.IMachineRecipe;
//import crazypants.enderio.base.recipe.MachineRecipeRegistry;
//import crazypants.enderio.base.recipe.painter.AbstractPainterTemplate;

@Mod(modid = FacadePainter.MODID, name = FacadePainter.NAME, version = FacadePainter.VERSION, dependencies = "required-after:forge@[14.23.4.2806,)", useMetadata = true)
public class FacadePainter {

    public static final String MODID = "facadepainter";
    public static final String NAME = "Facade Painter";
    public static final String VERSION = "@VERSION@";

    @SidedProxy(clientSide = "de.lathanael.facadepainter.proxy.ClientProxy", serverSide = "de.lathanael.facadepainter.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static FacadePainter instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

//    @Mod.EventHandler
//    public void serverLoad(FMLServerStartingEvent event) {
//        event.registerServerCommand(new TeleportCommand());
//    }
}
