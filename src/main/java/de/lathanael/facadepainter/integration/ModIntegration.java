package de.lathanael.facadepainter.integration;

import de.lathanael.facadepainter.integration.jei.FacadePaintingRecipeList;

import net.minecraftforge.fml.common.Loader;

public class ModIntegration {

    public static FacadePaintingRecipeList recipeList;

    public static void preInit() {
        if(Loader.isModLoaded("jei")) {
            recipeList = new FacadePaintingRecipeList();
        }
    }
}