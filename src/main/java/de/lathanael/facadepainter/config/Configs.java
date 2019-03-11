package de.lathanael.facadepainter.config;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.integration.ModIntegration;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = FacadePainter.MODID)
public class Configs {

    public static FeatureConfigs features = new FeatureConfigs();
    public static class FeatureConfigs {
        @Comment("Enable the Chamaeleo Paint item. [default=false]")
        public boolean enableChamaeleoPaint = false;

        @Comment("Set to true to hide the 'Facade Painting' catergory in JEI. [default=false]")
        public boolean hideJEIFacadePaintingRecipeCategory = false;
    }

    public static RecipeConfigs recipes = new RecipeConfigs();
    public static class RecipeConfigs {
        @Comment({"Enable the usage of Chamaeleo Paint in the facade recipe. [default=false]",
                    "",
                    "If enabled and the Chamaeleo Paint item is disabled the facade painting recipe will",
                    "become uncraftable without user added recipes for the Chamaeleo Paint item!"
        })
        public boolean useChamaeleoPaint = false;

        @Comment({"If set to false the shapeless recipe to clear a painted facade by putting it into",
                    "any valid crafting grid is removed. [default=true]"})
        public boolean enableShapelessClearingRecipe = true;
    }

    @Mod.EventBusSubscriber
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(FacadePainter.MODID)) {
                ConfigManager.sync(FacadePainter.MODID, Config.Type.INSTANCE);
                ModIntegration.updateJEIRecipeList();
            }
        }
    }
}