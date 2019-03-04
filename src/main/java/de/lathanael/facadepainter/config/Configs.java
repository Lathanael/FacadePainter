package de.lathanael.facadepainter.config;

import de.lathanael.facadepainter.FacadePainter;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = FacadePainter.MODID)
public class Configs {

    public static FeatureConfigs features = new FeatureConfigs();
    public static class FeatureConfigs {
        @Comment("Enable the Chamaeleo Paint item.")
        @RequiresMcRestart
        public boolean enableChamaeleoPaint = false;
    }

    public static RecipeConfigs recipes = new RecipeConfigs();
    public static class RecipeConfigs {
        @Comment({
            "Enable the usage of Chamaeleo Paint in the facade recipe.", 
            "ONLY enable this if the Chamaeleo Paint item is also enabled!"
        })
        public boolean useChamaeleoPaint = false;
    }
}