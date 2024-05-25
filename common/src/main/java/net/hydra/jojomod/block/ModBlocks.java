package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

    //If you middle mouse click iron block, you can see vanilla block constructors
    //FabricBlockSettings.copyOf or FabricBlockSettings.create for these
    public static Block METEOR_BLOCK;
    public static void registerModBlocks(){
        Roundabout.LOGGER.info("Registering ModBlocks for "+ Roundabout.MOD_ID);
    }
}
