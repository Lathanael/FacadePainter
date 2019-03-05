package de.lathanael.facadepainter.compat.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class FacadePainterPlugin implements IModPlugin {

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        // TODO Auto-generated method stub
        IModPlugin.super.registerCategories(registry);
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        // TODO Auto-generated method stub
        IModPlugin.super.register(registry);
    }
}
