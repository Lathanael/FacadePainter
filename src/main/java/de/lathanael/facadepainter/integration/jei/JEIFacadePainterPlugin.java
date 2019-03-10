package de.lathanael.facadepainter.integration.jei;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.config.Configs;
import de.lathanael.facadepainter.init.ItemRegistry;
import de.lathanael.facadepainter.integration.ModIntegration;
import de.lathanael.facadepainter.recipes.ToggleableShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import crazypants.enderio.base.conduit.facade.ItemConduitFacade;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class JEIFacadePainterPlugin implements IModPlugin {

    public static JEIFacadePainterPlugin INSTANCE;

    private IJeiRuntime jeiRuntime;
    private IJeiHelpers jeiHelpers;
    private List<IRecipeWrapper> toggleableShapelessRecipes = new ArrayList<>();

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new FacadePaintingRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        INSTANCE = this;
        jeiHelpers = registry.getJeiHelpers();
        if (!Configs.features.enableChamaeleoPaint) {
            // Hide Chamaeleo Paint item if it is not enabled
            jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ItemRegistry.itemChamaeleoPaint, 1, OreDictionary.WILDCARD_VALUE));
        }
        // Recipes to clear a painted facade
        if (Configs.recipes.enableShapelessClearingRecipe) {
            registry.addRecipes(ModIntegration.recipeList.getPseudoClearingRecipeList(), VanillaRecipeCategoryUid.CRAFTING);
            registry.handleRecipes(FacadeClearingRecipe.class, recipe -> new FacadeClearingRecipeWrapper(recipe), VanillaRecipeCategoryUid.CRAFTING);
        }
        // Facade painting recipe
        registry.addRecipes(ModIntegration.recipeList.getRecipeList(), FacadePaintingRecipeCategory.UID);
        registry.handleRecipes(FacadePaintingRecipe.class, recipe -> new FacadePaintingRecipeWrapper(recipe), FacadePaintingRecipeCategory.UID);
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
        // Fetch the wrapper object for ToggleableShapelessRecipe recipes and also hide them if their recipe is disabled
        for (Entry<ResourceLocation, IRecipe> recipeEntry : ForgeRegistries.RECIPES.getEntries()) {
            IRecipe recipe =  recipeEntry.getValue();
            if (recipe instanceof ToggleableShapelessRecipe) {
                toggleableShapelessRecipes.add(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING));
                if (!Configs.features.enableChamaeleoPaint && !(recipe.getRecipeOutput().getItem() instanceof ItemConduitFacade)) {
                    jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING), VanillaRecipeCategoryUid.CRAFTING);
                } else {
                    jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING), VanillaRecipeCategoryUid.CRAFTING);
                }
            }
            if (recipe instanceof FacadeClearingRecipe) {
                toggleableShapelessRecipes.add(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING));
                if (!Configs.recipes.enableShapelessClearingRecipe) {
                    jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING), VanillaRecipeCategoryUid.CRAFTING);
                }
            }
        }
        if (Configs.features.hideJEIFacadePaintingRecipeCategory) {
            jeiRuntime.getRecipeRegistry().hideRecipeCategory(FacadePaintingRecipeCategory.UID);
        }
    }

    public IJeiRuntime getJEIRuntime() {
        return jeiRuntime;
    }

    public IJeiHelpers getJEIHelpers() {
        return jeiHelpers;
    }

    public List<IRecipeWrapper> getToggleableShapelessRecipes() {
        return toggleableShapelessRecipes;
    }
}