package com.example.jjf.forge;

import com.example.jjf.JJFMod;
import com.example.jjf.forge.network.ForgeJJFNetwork;
import com.example.jjf.forge.registry.ForgeJJFRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;

public final class JJFEmbeddedForge {

    private static final ResourceKey<CreativeModeTab> WIP_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            new ResourceLocation("roundabout", "jojo_wip_features")
    );

    private JJFEmbeddedForge() {
    }

    public static void initialize(IEventBus bus) {
        JJFMod.setConfigDirectory(FMLPaths.CONFIGDIR.get());
        ForgeJJFRegistries.register(bus);
        ForgeJJFNetwork.register();
        bus.addListener(JJFEmbeddedForge::registerAttributes);
        bus.addListener(JJFEmbeddedForge::addCreativeItems);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ForgeJJFRegistries.JUMPIN_JACK_FLASH.get(),
                Mob.createMobAttributes().build()
        );
    }

    private static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(WIP_TAB)) {
            event.accept(ForgeJJFRegistries.JJF_STAND_DISC);
            event.accept(ForgeJJFRegistries.JJF_STAND_DISC_MAX);
        }
    }
}