package de.lathanael.facadepainter.items;

import de.lathanael.facadepainter.FacadePainter;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChamaeleoPaint extends Item {

    public ItemChamaeleoPaint() {
        setRegistryName(new ResourceLocation(FacadePainter.MODID, "chamaeleo_paint"));
        setTranslationKey(FacadePainter.MODID + ".chamaeleo_paint");
        setCreativeTab(CreativeTabs.MISC);
        
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}