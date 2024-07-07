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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
    /**This is where blocks are listed and called upon.
     * Forge and fabric files must define these variables so they are not empty.**/
    public static Block METEOR_BLOCK;
    public static Block GASOLINE_SPLATTER;

    public static final IntegerProperty GAS_CAN_LEVEL = IntegerProperty.create("level", 0, 2);

}
