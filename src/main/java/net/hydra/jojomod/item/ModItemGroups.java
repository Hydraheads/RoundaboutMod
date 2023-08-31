package net.hydra.jojomod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup JOJO_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(RoundaboutMod.MOD_ID, "jojo"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.jojo"))
                    .icon(() -> new ItemStack(ModItems.STAND_ARROW)).entries((displayContext, entries) -> {

                        //Add all items from the Jojo mod tab here

                        entries.add(ModItems.STAND_ARROW);

                        entries.add(ModBlocks.METEOR_BLOCK);

                    }).build());
    public static void registerItemGroups(){
        RoundaboutMod.LOGGER.info("Registering Item Groups For " + RoundaboutMod.MOD_ID);
    }
}
