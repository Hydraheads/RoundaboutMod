package net.hydra.jojomod;

import net.hydra.jojomod.Utils.ForgeItemModifiers;
import net.hydra.jojomod.biome_modifiers.BiomeCodec;
import net.hydra.jojomod.item.DispenserRegistry;
import net.hydra.jojomod.networking.ForgePacketHandler;
import net.hydra.jojomod.registry.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(Roundabout.MOD_ID)
public class RoundaboutModForge {

    public RoundaboutModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeEntities.ENTITY_TYPES.register(bus);
        ForgeSounds.SOUNDS.register(bus);
        ForgeBlocks.BLOCKS.register(bus);
        ForgeBlocks.BLOCK_ENTITIES.register(bus);
        ForgeItems.ITEMS.register(bus);
        ForgeCreativeTab.TABS.register(bus);
        ForgeEffects.POTION_EFFECTS.register(bus);
        ForgeParticles.PARTICLES.register(bus);
        //
        BiomeCodec.BIOME_MODIFIER_SERIALIZERS.register(bus);
        ForgeLootModifiers.LOOT_MODIFIERS.register(bus);

        bus.addListener(this::commonSetup);

        Roundabout.LOGGER.info("Hello Forge world!");
        Roundabout.init();

        //ForgeItems.assignStupidForge();
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(() ->
                ForgePacketHandler.register()
        );
    }



}