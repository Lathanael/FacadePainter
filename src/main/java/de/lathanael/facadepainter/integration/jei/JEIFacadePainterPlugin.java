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

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
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
    private List<IRecipe> toggleableShapelessRecipes = new ArrayList<>();

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
        // Facade painting recipe
        registry.addRecipes(ModIntegration.recipeList.getRecipeList(), FacadePaintingRecipeCategory.UID);
        registry.handleRecipes(FacadePaintingRecipe.class, recipe -> new FacadePaintingRecipeWrapper(recipe), FacadePaintingRecipeCategory.UID);
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
        // Fetch the wrapper object for the chamaeleo paint recipe and also hide it if the recipe is disabled
        for (Entry<ResourceLocation, IRecipe> recipeEntry : ForgeRegistries.RECIPES.getEntries()) {
            if (recipeEntry.getKey().getNamespace().equalsIgnoreCase(FacadePainter.MODID) && recipeEntry.getValue() instanceof ToggleableShapelessRecipe) {
                toggleableShapelessRecipes.add(recipeEntry.getValue());
                if (!Configs.features.enableChamaeleoPaint) {
                    jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(recipeEntry.getValue(), VanillaRecipeCategoryUid.CRAFTING), VanillaRecipeCategoryUid.CRAFTING);
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

    public List<IRecipe> getToggleableShapelessRecipes() {
        return toggleableShapelessRecipes;
    }
}