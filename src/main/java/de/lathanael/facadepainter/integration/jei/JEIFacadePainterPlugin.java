package de.lathanael.facadepainter.integration.jei;

import de.lathanael.facadepainter.integration.ModIntegration;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class JEIFacadePainterPlugin implements IModPlugin {

    private IJeiRuntime jeiRuntime;

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new FacadePaintingRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipes(ModIntegration.recipeList.getRecipeList(), FacadePaintingRecipeCategory.UID);
        registry.handleRecipes(FacadePaintingRecipe.class, recipe -> new FacadePaintingRecipeWrapper(recipe), FacadePaintingRecipeCategory.UID);
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    public IJeiRuntime getJEIRuntime() {
        return jeiRuntime;
    }
}