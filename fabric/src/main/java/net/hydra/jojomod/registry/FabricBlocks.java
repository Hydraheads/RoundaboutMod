package net.hydra.jojomod.registry;

import com.mojang.datafixers.types.Type;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.*;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

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
    public static final Block STREET_SIGN_DIO = registerBlockUnstackable("street_sign_dio",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_RIGHT = registerBlockUnstackable("street_sign_right",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_STOP = registerBlockUnstackable("street_sign_stop",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_YIELD = registerBlockUnstackable("street_sign_yield",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_DANGER = registerBlockUnstackable("street_sign_danger",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STEREO = registerBlock("stereo",ModBlocks.STEREO_PROPERTIES);
    public static final Block STAND_FIRE = registerBlock("stand_fire",ModBlocks.STAND_FIRE_PROPERTIES);
    public static final Block ORANGE_FIRE = registerBlock("colored_fire_orange",ModBlocks.ORANGE_FIRE_PROPERTIES);
    public static final Block BLUE_FIRE = registerBlock("colored_fire_blue",ModBlocks.BLUE_FIRE_PROPERTIES);
    public static final Block PURPLE_FIRE = registerBlock("colored_fire_purple",ModBlocks.PURPLE_FIRE_PROPERTIES);
    public static final Block GREEN_FIRE = registerBlock("colored_fire_green",ModBlocks.GREEN_FIRE_PROPERTIES);
    public static final Block DREAD_FIRE = registerBlock("colored_fire_dread",ModBlocks.DREAD_FIRE_PROPERTIES);
    public static final Block CREAM_FIRE = registerBlock("colored_fire_cream",ModBlocks.CREAM_FIRE_PROPERTIES);

    public static final Block FOG_DIRT = registerBlockItemless("fog_dirt",ModBlocks.getFogBlock());
    public static final Block FOG_DIRT_COATING = registerBlockItemless("fog_dirt_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_CLAY = registerBlockItemless("fog_clay",ModBlocks.getFogBlock());
    public static final Block FOG_CLAY_COATING = registerBlockItemless("fog_clay_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_GRAVEL = registerBlockItemless("fog_gravel",ModBlocks.getFogBlock());
    public static final Block FOG_GRAVEL_COATING = registerBlockItemless("fog_gravel_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_SAND = registerBlockItemless("fog_sand",ModBlocks.getFogBlock());
    public static final Block FOG_SAND_COATING = registerBlockItemless("fog_sand_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_STONE = registerBlockItemless("fog_stone",ModBlocks.getFogBlock());
    public static final Block FOG_STONE_COATING = registerBlockItemless("fog_stone_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_COBBLESTONE = registerBlockItemless("fog_cobblestone",ModBlocks.getFogBlock());
    public static final Block FOG_COBBLESTONE_COATING = registerBlockItemless("fog_cobblestone_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_MOSSY_COBBLESTONE = registerBlockItemless("fog_mossy_cobblestone",ModBlocks.getFogBlock());
    public static final Block FOG_MOSSY_COBBLESTONE_COATING = registerBlockItemless("fog_mossy_cobblestone_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_OAK_PLANKS = registerBlockItemless("fog_oak_planks",ModBlocks.getFogBlock());
    public static final Block FOG_OAK_PLANKS_COATING = registerBlockItemless("fog_oak_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_SPRUCE_PLANKS = registerBlockItemless("fog_spruce_planks",ModBlocks.getFogBlock());
    public static final Block FOG_SPRUCE_PLANKS_COATING = registerBlockItemless("fog_spruce_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_BIRCH_PLANKS = registerBlockItemless("fog_birch_planks",ModBlocks.getFogBlock());
    public static final Block FOG_BIRCH_PLANKS_COATING = registerBlockItemless("fog_birch_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_JUNGLE_PLANKS = registerBlockItemless("fog_jungle_planks",ModBlocks.getFogBlock());
    public static final Block FOG_JUNGLE_PLANKS_COATING = registerBlockItemless("fog_jungle_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_ACACIA_PLANKS = registerBlockItemless("fog_acacia_planks",ModBlocks.getFogBlock());
    public static final Block FOG_ACACIA_PLANKS_COATING = registerBlockItemless("fog_acacia_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_DARK_OAK_PLANKS = registerBlockItemless("fog_dark_oak_planks",ModBlocks.getFogBlock());
    public static final Block FOG_DARK_OAK_PLANKS_COATING = registerBlockItemless("fog_dark_oak_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_MANGROVE_PLANKS = registerBlockItemless("fog_mangrove_planks",ModBlocks.getFogBlock());
    public static final Block FOG_MANGROVE_PLANKS_COATING = registerBlockItemless("fog_mangrove_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_CHERRY_PLANKS = registerBlockItemless("fog_cherry_planks",ModBlocks.getFogBlock());
    public static final Block FOG_CHERRY_PLANKS_COATING = registerBlockItemless("fog_cherry_planks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_COAL_ORE = registerBlockItemless("fog_coal_ore",ModBlocks.getFogBlock());
    public static final Block FOG_IRON_ORE = registerBlockItemless("fog_iron_ore",ModBlocks.getFogBlock());
    public static final Block FOG_GOLD_ORE = registerBlockItemless("fog_gold_ore",ModBlocks.getFogBlock());
    public static final Block FOG_LAPIS_ORE = registerBlockItemless("fog_lapis_ore",ModBlocks.getFogBlock());
    public static final Block FOG_DIAMOND_ORE = registerBlockItemless("fog_diamond_ore",ModBlocks.getFogBlock());
    public static final Block FOG_STONE_BRICKS = registerBlockItemless("fog_stone_bricks",ModBlocks.getFogBlock());
    public static final Block FOG_STONE_BRICKS_COATING = registerBlockItemless("fog_stone_bricks_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_DEEPSLATE = registerBlockItemless("fog_deepslate",ModBlocks.getFogBlock());
    public static final Block FOG_DEEPSLATE_COATING = registerBlockItemless("fog_deepslate_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_NETHERRACK = registerBlockItemless("fog_netherrack",ModBlocks.getFogBlock());
    public static final Block FOG_NETHERRACK_COATING = registerBlockItemless("fog_netherrack_coating",ModBlocks.getFogCoatingBlock());
    public static final Block FOG_NETHER_BRICKS = registerBlockItemless("fog_nether_bricks",ModBlocks.getFogBlock());
    public static final Block FOG_NETHER_BRICKS_COATING = registerBlockItemless("fog_nether_bricks_coating",ModBlocks.getFogCoatingBlock());

    public static final BlockEntityType<StereoBlockEntity> STEREO_BLOCK_ENTITY =
           registerBE("stereo",BlockEntityType.Builder.of(StereoBlockEntity::new, STEREO));
    public static final BlockEntityType<StandFireBlockEntity> STAND_FIRE_BLOCK_ENTITY =
            registerBE("stand_fire",BlockEntityType.Builder.of(StandFireBlockEntity::new, STAND_FIRE));


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
        ModBlocks.STREET_SIGN_DIO = STREET_SIGN_DIO;
        ModBlocks.STREET_SIGN_RIGHT = STREET_SIGN_RIGHT;
        ModBlocks.STREET_SIGN_STOP = STREET_SIGN_STOP;
        ModBlocks.STREET_SIGN_YIELD = STREET_SIGN_YIELD;
        ModBlocks.STREET_SIGN_DANGER = STREET_SIGN_DANGER;
        ModBlocks.STEREO = STEREO;
        ModBlocks.STAND_FIRE = STAND_FIRE;
        ModBlocks.ORANGE_FIRE = ORANGE_FIRE;
        ModBlocks.BLUE_FIRE = BLUE_FIRE;
        ModBlocks.PURPLE_FIRE = PURPLE_FIRE;
        ModBlocks.GREEN_FIRE = GREEN_FIRE;
        ModBlocks.DREAD_FIRE = DREAD_FIRE;
        ModBlocks.CREAM_FIRE = CREAM_FIRE;
        ModBlocks.STEREO_BLOCK_ENTITY = STEREO_BLOCK_ENTITY;
        ModBlocks.STAND_FIRE_BLOCK_ENTITY = STAND_FIRE_BLOCK_ENTITY;
        ModBlocks.FOG_DIRT = FOG_DIRT;
        ModBlocks.FOG_DIRT_COATING = FOG_DIRT_COATING;
        ModBlocks.FOG_CLAY = FOG_CLAY;
        ModBlocks.FOG_CLAY_COATING = FOG_CLAY_COATING;
        ModBlocks.FOG_GRAVEL = FOG_GRAVEL;
        ModBlocks.FOG_GRAVEL_COATING = FOG_GRAVEL_COATING;
        ModBlocks.FOG_SAND = FOG_SAND;
        ModBlocks.FOG_SAND_COATING = FOG_SAND_COATING;
        ModBlocks.FOG_OAK_PLANKS = FOG_OAK_PLANKS;
        ModBlocks.FOG_OAK_PLANKS_COATING = FOG_OAK_PLANKS_COATING;
        ModBlocks.FOG_SPRUCE_PLANKS = FOG_SPRUCE_PLANKS;
        ModBlocks.FOG_SPRUCE_PLANKS_COATING = FOG_SPRUCE_PLANKS_COATING;
        ModBlocks.FOG_BIRCH_PLANKS = FOG_BIRCH_PLANKS;
        ModBlocks.FOG_BIRCH_PLANKS_COATING = FOG_BIRCH_PLANKS_COATING;
        ModBlocks.FOG_JUNGLE_PLANKS = FOG_JUNGLE_PLANKS;
        ModBlocks.FOG_JUNGLE_PLANKS_COATING = FOG_JUNGLE_PLANKS_COATING;
        ModBlocks.FOG_ACACIA_PLANKS = FOG_ACACIA_PLANKS;
        ModBlocks.FOG_ACACIA_PLANKS_COATING = FOG_ACACIA_PLANKS_COATING;
        ModBlocks.FOG_DARK_OAK_PLANKS = FOG_DARK_OAK_PLANKS;
        ModBlocks.FOG_DARK_OAK_PLANKS_COATING = FOG_DARK_OAK_PLANKS_COATING;
        ModBlocks.FOG_MANGROVE_PLANKS = FOG_MANGROVE_PLANKS;
        ModBlocks.FOG_MANGROVE_PLANKS_COATING = FOG_MANGROVE_PLANKS_COATING;
        ModBlocks.FOG_CHERRY_PLANKS = FOG_CHERRY_PLANKS;
        ModBlocks.FOG_CHERRY_PLANKS_COATING = FOG_CHERRY_PLANKS_COATING;
        ModBlocks.FOG_STONE = FOG_STONE;
        ModBlocks.FOG_STONE_COATING = FOG_STONE_COATING;
        ModBlocks.FOG_COBBLESTONE = FOG_COBBLESTONE;
        ModBlocks.FOG_COBBLESTONE_COATING = FOG_COBBLESTONE_COATING;
        ModBlocks.FOG_MOSSY_COBBLESTONE = FOG_MOSSY_COBBLESTONE;
        ModBlocks.FOG_MOSSY_COBBLESTONE_COATING = FOG_MOSSY_COBBLESTONE_COATING;
        ModBlocks.FOG_COAL_ORE = FOG_COAL_ORE;
        ModBlocks.FOG_IRON_ORE = FOG_IRON_ORE;
        ModBlocks.FOG_GOLD_ORE = FOG_GOLD_ORE;
        ModBlocks.FOG_LAPIS_ORE = FOG_LAPIS_ORE;
        ModBlocks.FOG_DIAMOND_ORE = FOG_DIAMOND_ORE;
        ModBlocks.FOG_STONE_BRICKS = FOG_STONE_BRICKS;
        ModBlocks.FOG_NETHERRACK = FOG_NETHERRACK;
        ModBlocks.FOG_NETHERRACK_COATING = FOG_NETHERRACK_COATING;
        ModBlocks.FOG_DEEPSLATE = FOG_DEEPSLATE;
        ModBlocks.FOG_DEEPSLATE_COATING = FOG_DEEPSLATE_COATING;
        ModBlocks.FOG_STONE_BRICKS_COATING = FOG_STONE_BRICKS_COATING;
        ModBlocks.FOG_NETHER_BRICKS = FOG_NETHER_BRICKS;
        ModBlocks.FOG_NETHER_BRICKS_COATING = FOG_NETHER_BRICKS_COATING;
        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        //ModBlocks.registerDynamicFogBlocks();
    }
}
