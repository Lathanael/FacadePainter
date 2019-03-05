package de.lathanael.facadepainter.config;

import de.lathanael.facadepainter.FacadePainter;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = FacadePainter.MODID)
public class Configs {

    public static RecipeConfigs recipes = new RecipeConfigs();
    public static class RecipeConfigs {
        @Comment("Enable the usage of Chamaeleo Paint in the facade recipe.")
        public boolean useChamaeleoPaint = false;
    }

    @Mod.EventBusSubscriber
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(FacadePainter.MODID)) {
                ConfigManager.sync(FacadePainter.MODID, Config.Type.INSTANCE);
            }
        }
    }
}