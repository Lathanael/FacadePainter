package de.lathanael.facadepainter.integration.jei;

import crazypants.enderio.base.init.ModObject;
import crazypants.enderio.base.paint.IPaintable;
import crazypants.enderio.base.paint.PaintUtil;
import crazypants.enderio.base.recipe.IMachineRecipe;
import crazypants.enderio.base.recipe.MachineRecipeRegistry;
import crazypants.enderio.base.recipe.painter.AbstractPainterTemplate;
import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.init.ItemRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FacadePaintingRecipeList {

    private static final @Nonnull List<FacadePaintingRecipe> VALID_RECIPES = new ArrayList<>();
    private static final @Nonnull ItemStack FACADE = new ItemStack(ModObject.itemConduitFacade.getItem(), 1);
    private static final @Nonnull ItemStack CHAMAELEO_PAINT = new ItemStack(ItemRegistry.itemChamaeleoPaint, 1);

    public FacadePaintingRecipeList() {
        generate();
    }

    private void generate() {
        Iterator<Item> listIterator = ForgeRegistries.ITEMS.getValuesCollection().iterator();
        while (listIterator.hasNext()) {
            ItemStack tempStack = new ItemStack(listIterator.next());
            if (tempStack.isEmpty()) {
                continue;
            }
            Block tempBlock = PaintUtil.getBlockFromItem(tempStack);
            if (tempBlock != null && tempBlock instanceof IPaintable) {
                continue;
            }
            if (tempBlock == Blocks.AIR || tempStack.getItem() == Items.AIR) {
                continue;
            }
            Map<String, IMachineRecipe> painterRecipes = MachineRecipeRegistry.instance.getRecipesForMachine(MachineRecipeRegistry.PAINTER);
            for (IMachineRecipe rec : painterRecipes.values()) {
                if (rec instanceof AbstractPainterTemplate<?>) {
                    AbstractPainterTemplate<?> recipe = (AbstractPainterTemplate<?>) rec;
                    if (recipe.isPartialRecipe(tempStack, FACADE)) {
                        try {
                            VALID_RECIPES.add(new FacadePaintingRecipe(recipe.getCompletedResult(tempStack, FACADE), Arrays.asList(tempStack, FACADE, CHAMAELEO_PAINT)));
                        } catch (Exception e) {
                            FacadePainter.logger.debug("Empty ingredients list supplied for: " + tempStack.toString());
                        }
                    }
                }
            }
        }
    }

    public List<FacadePaintingRecipe> getRecipeList() {
        return VALID_RECIPES;
    }
}