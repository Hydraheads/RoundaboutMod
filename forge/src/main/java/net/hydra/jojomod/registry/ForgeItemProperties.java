package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.StrayCats.AbstractStrayCat;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ForgeItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ForgeItems.STRAY_CAT_ANIME.get(), new ResourceLocation(Roundabout.MOD_ID,"anim"), (itemStack, clientLevel, livingEntity, i) ->  !itemStack.isEmpty() ? ((AbstractStrayCat)(itemStack.getItem())).getCurrentPredicateValue(clientLevel) : 0.0f);
        ItemProperties.register(ForgeItems.STRAY_CAT_MANGA.get(), new ResourceLocation(Roundabout.MOD_ID,"anim"), (itemStack, clientLevel, livingEntity, i) ->  !itemStack.isEmpty() ? ((AbstractStrayCat)(itemStack.getItem())).getCurrentPredicateValue(clientLevel) : 0.0f);
    }
}
