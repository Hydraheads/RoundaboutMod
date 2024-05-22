package net.hydra.jojomod.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final CreativeModeTab JOJO_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo"))
                    .icon(() -> new ItemStack(ModItems.STAND_ARROW)).displayItems((displayContext, entries) -> {

                        //Add all items from the Jojo mod tab here

                        entries.accept(ModItems.STAND_ARROW);
                        entries.accept(ModItems.TERRIER_SPAWN_EGG);
                        entries.accept(ModItems.STAND_DISC);
                        entries.accept(ModItems.COFFEE_GUM);
                        entries.accept(ModItems.METEORITE);

                        entries.accept(ModBlocks.METEOR_BLOCK);

                    }).build());
    public static void registerItemGroups(){
        Roundabout.LOGGER.info("Registering Item Groups For " + Roundabout.MOD_ID);
    }
}
