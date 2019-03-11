package de.lathanael.facadepainter.integration.jei;

import com.google.common.collect.Lists;

import crazypants.enderio.base.init.ModObject;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.config.Configs;

import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class FacadePaintingRecipeCategory implements IRecipeCategory<FacadePaintingRecipeWrapper> {

    public static final String UID = FacadePainter.MODID + ".crafting";

    private static final int INPUT_SLOT = 0;
    private static final int FACADE_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int CHAMAELEO_SLOT = 3;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public FacadePaintingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(FacadePainter.MODID, "textures/gui/jei/facade_painting.png");;
        background = guiHelper.createDrawable(location, 0, 0, 96, 36);
        localizedName = I18n.format("gui."+ FacadePainter.MODID + ".jei.facade_painting");
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModObject.itemConduitFacade.getItem()));
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return FacadePainter.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = Lists.newArrayList();
        if (mouseX > 42 && mouseX < 59 && mouseY > 11 && mouseY < 26) {
            tooltip.add(I18n.format("gui."+ FacadePainter.MODID + ".jei.shapeless_recipe"));
        }
        return tooltip;
    }

    // TODO: Make outputs cycle as well
    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull FacadePaintingRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 0);
        recipeLayout.getItemStacks().init(CHAMAELEO_SLOT, true, 0, 18);
        recipeLayout.getItemStacks().init(FACADE_SLOT, true, 18, 0);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 9);

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        ItemStack input = inputs.get(0).get(0);
        ItemStack facade = inputs.get(1).get(0);
        facade.setItemDamage(OreDictionary.WILDCARD_VALUE);
        ItemStack chamaeleo_paint = inputs.get(2).get(0);
        ItemStack output = outputs.get(0).get(0);

        recipeLayout.getItemStacks().set(INPUT_SLOT, input);
        recipeLayout.getItemStacks().set(FACADE_SLOT, facade);
        if (Configs.recipes.useChamaeleoPaint) {
            recipeLayout.getItemStacks().set(CHAMAELEO_SLOT, chamaeleo_paint);
        } else {
            recipeLayout.getItemStacks().set(CHAMAELEO_SLOT, ItemStack.EMPTY);
        }
        recipeLayout.getItemStacks().set(OUTPUT_SLOT, output);
    }
}