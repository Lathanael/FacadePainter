/*******************************************************************************
 * Copyright (c) 2019 Lathanael.
 * This program and the accompanying materials
 * are made available under the terms of the MIT 
 * License which accompanies this distribution, 
 * and is available at http://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package de.lathanael.facadepainter.config;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.integration.ModIntegration;
import de.lathanael.facadepainter.network.PacketConfigSync;
import de.lathanael.facadepainter.network.NetworkHandler;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

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
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(FacadePainter.MODID)) {
                ConfigManager.sync(FacadePainter.MODID, Config.Type.INSTANCE);
                ModIntegration.updateJEIRecipeList();
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
            if (event.player.world.isRemote) {
                return;
            }

            FacadePainter.logger.info("Sending server configs to client: " + event.player.getName());
            PacketConfigSync message = new PacketConfigSync();
            message.enableChamaeleoPaint = Configs.features.enableChamaeleoPaint;
            message.hideJEIFacadePaintingRecipeCategory = Configs.features.hideJEIFacadePaintingRecipeCategory;
            message.useChamaeleoPaint = Configs.recipes.useChamaeleoPaint;
            message.enableShapelessClearingRecipe = Configs.recipes.enableShapelessClearingRecipe;
            NetworkHandler.sendToClient(message, (EntityPlayerMP) event.player);
        }
    }
}