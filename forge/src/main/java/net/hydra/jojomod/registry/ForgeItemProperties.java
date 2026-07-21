package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.StrayCatItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ForgeItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ForgeItems.STRAY_CAT_ANIME.get(), new ResourceLocation(Roundabout.MOD_ID,"anim"), (itemStack, clientLevel, livingEntity, i) ->  !itemStack.isEmpty() ? ((StrayCatItem)(itemStack.getItem())).getCurrentPredicateValue(clientLevel, itemStack) : 0.0f);
        ItemProperties.register(ForgeItems.STRAY_CAT_MANGA.get(), new ResourceLocation(Roundabout.MOD_ID,"anim"), (itemStack, clientLevel, livingEntity, i) ->  !itemStack.isEmpty() ? ((StrayCatItem)(itemStack.getItem())).getCurrentPredicateValue(clientLevel, itemStack) : 0.0f);
        ItemProperties.register(ForgeItems.FANCY_LIGHTER.get(), new ResourceLocation(Roundabout.MOD_ID,"islit"), (itemStack, clientLevel, livingEntity, i) ->  !itemStack.isEmpty() ? ((FancyLighterItem)(itemStack.getItem())).getCurrentPredicateValue(clientLevel, itemStack) : 0.0f);
    }
}
