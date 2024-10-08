package net.hydra.jojomod.registry;

import com.mojang.datafixers.types.Type;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.*;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class FabricBlocks {
    public static final Block ANCIENT_METEOR = registerBlock("ancient_meteor", ModBlocks.ANCIENT_METEOR_PROPERTIES
    );
    public static final Block METEOR_BLOCK = registerBlock("meteor_block", ModBlocks.METEOR_BLOCK_PROPERTIES
    );
    public static final Block LOCACACA_CACTUS = registerBlock("locacaca_cactus", ModBlocks.LOCACACA_CACTUS_PROPERTIES
    );
    public static final Block LOCACACA_BLOCK = registerBlock("locacaca_plant", ModBlocks.LOCACACA_BLOCK_PROPERTIES
    );
    public static final Block NEW_LOCACACA_BLOCK = registerBlock("new_locacaca_plant", ModBlocks.NEW_LOCACACA_BLOCK_PROPERTIES
    );
    public static final Block GASOLINE_SPLATTER = registerBlock("gasoline_splatter", ModBlocks.GASOLINE_SPLATTER_PROPERTIES
    );
    public static final Block BLOOD_SPLATTER = registerBlockItemless("blood_splatter", ModBlocks.BLOOD_SPLATTER_PROPERTIES
    );
    public static final Block BLUE_BLOOD_SPLATTER = registerBlockItemless("blue_blood_splatter", ModBlocks.BLUE_BLOOD_SPLATTER_PROPERTIES
    );
    public static final Block ENDER_BLOOD_SPLATTER = registerBlockItemless("ender_blood_splatter", ModBlocks.ENDER_BLOOD_SPLATTER_PROPERTIES
    );
    public static final Block WIRE_TRAP = registerBlock("wire_trap", ModBlocks.WIRE_TRAP_PROPERTIES
    );
    public static final Block BARBED_WIRE = registerBlock("barbed_wire",ModBlocks.BARBED_WIRE_BLOCK_PROPERTIES
    );
    public static final Block BARBED_WIRE_BUNDLE = registerBlock("barbed_wire_bundle",ModBlocks.BARBED_WIRE_BUNDLE_PROPERTIES
    );
    public static final Block GODDESS_STATUE_BLOCK = registerBlockUnstackable("goddess_statue",ModBlocks.GODDESS_STATUE_BLOCK_PROPERTIES
    , 1);
    public static final Block STEREO = registerBlock("stereo",ModBlocks.STEREO_PROPERTIES);

    public static final BlockEntityType<StereoBlockEntity> STEREO_BLOCK_ENTITY =
           registerBE("stereo",BlockEntityType.Builder.of(StereoBlockEntity::new, STEREO));


    private static <T extends BlockEntity> BlockEntityType<T> registerBE(String $$0, BlockEntityType.Builder<T> $$1) {
        Type<?> $$2 = Util.fetchChoiceType(References.BLOCK_ENTITY, $$0);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Roundabout.MOD_ID,$$0), $$1.build($$2));
    }

    private static Block registerBlockItemless(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }
    private static Block registerBlockUnstackable(String name, Block block, int stacksize) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new BlockItem(block, new Item.Properties().stacksTo(stacksize)));
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
    }

    public static void register(){
        ModBlocks.ANCIENT_METEOR = ANCIENT_METEOR;
        ModBlocks.METEOR_BLOCK = METEOR_BLOCK;
        ModBlocks.LOCACACA_CACTUS = LOCACACA_CACTUS;
        ModBlocks.LOCACACA_BLOCK = LOCACACA_BLOCK;
        ModBlocks.NEW_LOCACACA_BLOCK = NEW_LOCACACA_BLOCK;
        ModBlocks.GASOLINE_SPLATTER = GASOLINE_SPLATTER;
        ModBlocks.BLOOD_SPLATTER = BLOOD_SPLATTER;
        ModBlocks.BLUE_BLOOD_SPLATTER = BLUE_BLOOD_SPLATTER;
        ModBlocks.ENDER_BLOOD_SPLATTER = ENDER_BLOOD_SPLATTER;
        ModBlocks.BARBED_WIRE = BARBED_WIRE;
        ModBlocks.BARBED_WIRE_BUNDLE = BARBED_WIRE_BUNDLE;
        ModBlocks.WIRE_TRAP = WIRE_TRAP;
        ModBlocks.GODDESS_STATUE_BLOCK = GODDESS_STATUE_BLOCK;
        ModBlocks.STEREO = STEREO;
        ModBlocks.STEREO_BLOCK_ENTITY = STEREO_BLOCK_ENTITY;
        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();
    }
}
