package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.*;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Roundabout.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Roundabout.MOD_ID);

    public static final RegistryObject<Block> ANCIENT_METEOR = BLOCKS.register("ancient_meteor",
            () -> ModBlocks.ANCIENT_METEOR_PROPERTIES
    );
    public static final RegistryObject<Block> METEOR_BLOCK = BLOCKS.register("meteor_block",
            () -> ModBlocks.METEOR_BLOCK_PROPERTIES
            );
    public static final RegistryObject<Block> LOCACACA_CACTUS = BLOCKS.register("locacaca_cactus",
            () -> ModBlocks.LOCACACA_CACTUS_PROPERTIES
    );
    public static final RegistryObject<Block> LOCACACA_BLOCK = BLOCKS.register("locacaca_plant",
            () -> ModBlocks.LOCACACA_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> NEW_LOCACACA_BLOCK = BLOCKS.register("new_locacaca_plant",
            () -> ModBlocks.NEW_LOCACACA_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> GASOLINE_SPLATTER = BLOCKS.register("gasoline_splatter",
            () -> ModBlocks.GASOLINE_SPLATTER_PROPERTIES);
    public static final RegistryObject<Block> BLOOD_SPLATTER = BLOCKS.register("blood_splatter",
            () -> ModBlocks.BLOOD_SPLATTER_PROPERTIES);
    public static final RegistryObject<Block> BLUE_BLOOD_SPLATTER = BLOCKS.register("blue_blood_splatter",
            () -> ModBlocks.BLUE_BLOOD_SPLATTER_PROPERTIES);
    public static final RegistryObject<Block> ENDER_BLOOD_SPLATTER = BLOCKS.register("ender_blood_splatter",
            () -> ModBlocks.ENDER_BLOOD_SPLATTER_PROPERTIES);
    public static final RegistryObject<Block> WIRE_TRAP = BLOCKS.register("wire_trap",
            () -> ModBlocks.WIRE_TRAP_PROPERTIES);
    public static final RegistryObject<Block> BARBED_WIRE = BLOCKS.register("barbed_wire",
            () -> ModBlocks.BARBED_WIRE_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> BARBED_WIRE_BUNDLE = BLOCKS.register("barbed_wire_bundle",
            () -> ModBlocks.BARBED_WIRE_BUNDLE_PROPERTIES);
    public static final RegistryObject<Block> GODDESS_STATUE_BLOCK = BLOCKS.register("goddess_statue",
            () -> ModBlocks.GODDESS_STATUE_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> STEREO = BLOCKS.register("stereo",
            () -> ModBlocks.STEREO_PROPERTIES);
    public static final RegistryObject<BlockEntityType<StereoBlockEntity>> STEREO_BLOCK_ENTITY = BLOCK_ENTITIES.register("stereo",
            () -> BlockEntityType.Builder.of(StereoBlockEntity::new, STEREO.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "stereo")));
}
