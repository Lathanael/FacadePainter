package de.lathanael.facadepainter.init;

import de.lathanael.facadepainter.FacadePainter;
import de.lathanael.facadepainter.items.ItemChamaeleoPaint;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemChamaeleoPaint());
    }
    
    @GameRegistry.ObjectHolder(FacadePainter.MODID + ":chamaeleo_paint")
    public static ItemChamaeleoPaint itemChamaeleoPaint;

}
