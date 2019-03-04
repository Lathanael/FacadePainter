package de.lathanael.facadepainter.items;

import de.lathanael.facadepainter.FacadePainter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemChamaeleoPaint extends Item {

    public ItemChamaeleoPaint() {
        setRegistryName(new ResourceLocation(FacadePainter.MODID, "chamaeleo_paint"));
        setTranslationKey(FacadePainter.MODID + ".chamaeleo_paint");
        setCreativeTab(CreativeTabs.MISC);
    }

}
