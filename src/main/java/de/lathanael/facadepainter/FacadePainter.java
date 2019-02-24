package de.lathanael.facadepainter;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

//import crazypants.enderio.base.recipe.IMachineRecipe;
//import crazypants.enderio.base.recipe.MachineRecipeRegistry;
//import crazypants.enderio.base.recipe.painter.AbstractPainterTemplate;

@Mod(modid = FacadePainter.MODID, name = FacadePainter.NAME, version = FacadePainter.VERSION)
public class FacadePainter
{
    public static final String MODID = "facadepainter";
    public static final String NAME = "Facade Painter";
    public static final String VERSION = "@VERSION@";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
