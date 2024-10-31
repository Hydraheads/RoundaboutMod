package net.hydra.jojomod;

import net.hydra.jojomod.Utils.ForgeCommonConfig;
import net.hydra.jojomod.Utils.ForgeItemModifiers;
import net.hydra.jojomod.Utils.commands.ForgeCommandRegistry;
import net.hydra.jojomod.biome_modifiers.BiomeCodec;
import net.hydra.jojomod.item.DispenserRegistry;
import net.hydra.jojomod.networking.ForgePacketHandler;
import net.hydra.jojomod.registry.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(Roundabout.MOD_ID)
public class RoundaboutModForge {

    public RoundaboutModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeCommonConfig.SPEC, "roundabout-client.toml");
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeCommonConfig.SPEC, "roundabout-common.toml");

        ForgeEffects.POTION_EFFECTS.register(bus);
        ForgeEntities.ENTITY_TYPES.register(bus);
        ForgeSounds.SOUNDS.register(bus);
        ForgeBlocks.BLOCKS.register(bus);
        ForgeBlocks.BLOCK_ENTITIES.register(bus);
        ForgeItems.ITEMS.register(bus);
        ForgeItems.POTIONS.register(bus);
        ForgeCreativeTab.TABS.register(bus);
        ForgeParticles.PARTICLES.register(bus);
        ForgeGamerules.registerGamerules();

        //
        BiomeCodec.BIOME_MODIFIER_SERIALIZERS.register(bus);
        ForgeLootModifiers.LOOT_MODIFIERS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::loadComplete);


        Roundabout.LOGGER.info("Hello Forge world!");
        Roundabout.init();

        //ForgeItems.assignStupidForge();
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeCommandRegistry());
    }

    private void updateConfigs(){
        Roundabout.canBreathInTS = ForgeCommonConfig.CAN_BREATHE_IN_TS.get();
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(() ->
                ForgePacketHandler.register()
        );
    }



}