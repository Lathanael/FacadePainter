package de.lathanael.facadepainter.integration;

import de.lathanael.facadepainter.integration.jei.FacadePaintingRecipeList;
import de.lathanael.facadepainter.integration.jei.JEIBlacklistUpdater;

import net.minecraftforge.fml.common.Loader;

public class ModIntegration {

    public static FacadePaintingRecipeList recipeList;
    public static JEIBlacklistUpdater jeiBlacklistUpdater;
    public static boolean isJEILoaded = false;

    public static void preInit() {
        isJEILoaded = Loader.isModLoaded("jei");
        if(isJEILoaded) {
            recipeList = new FacadePaintingRecipeList();
            jeiBlacklistUpdater = new JEIBlacklistUpdater();
        }
    }
    
    public static void updateJEIRecipeList() {
        if(isJEILoaded) {
            jeiBlacklistUpdater.handleBlacklisting();
        }
    }
}