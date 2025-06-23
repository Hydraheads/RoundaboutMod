package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.*;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public static final RegistryObject<Block> REGAL_WALL = BLOCKS.register("regal_wall",
            () -> ModBlocks.REGAL_WALL_PROPERTIES
    );
    public static final RegistryObject<Block> REGAL_FLOOR = BLOCKS.register("regal_floor",
            () -> ModBlocks.REGAL_FLOOR_PROPERTIES
    );
    public static final RegistryObject<Block> WOODEN_MANOR_TABLE = BLOCKS.register("wooden_manor_table",
            () -> ModBlocks.WOODEN_MANOR_TABLE_PROPERTIES
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
    public static final RegistryObject<Block> STREET_SIGN_DIO = BLOCKS.register("street_sign_dio",
            ModBlocks::getStreetSignBlockProperties);
    public static final RegistryObject<Block> STREET_SIGN_RIGHT = BLOCKS.register("street_sign_right",
            ModBlocks::getStreetSignBlockProperties);
    public static final RegistryObject<Block> STREET_SIGN_STOP = BLOCKS.register("street_sign_stop",
            ModBlocks::getStreetSignBlockProperties);
    public static final RegistryObject<Block> STREET_SIGN_YIELD = BLOCKS.register("street_sign_yield",
            ModBlocks::getStreetSignBlockProperties);
    public static final RegistryObject<Block> STREET_SIGN_DANGER = BLOCKS.register("street_sign_danger",
            ModBlocks::getStreetSignBlockProperties);

    public static final RegistryObject<Block> WALL_STREET_SIGN_DIO = BLOCKS.register("wall_street_sign_dio",
            ModBlocks::getWallStreetSignBlockProperties);
    public static final RegistryObject<Block> WALL_STREET_SIGN_RIGHT = BLOCKS.register("wall_street_sign_right",
            ModBlocks::getWallStreetSignBlockProperties);
    public static final RegistryObject<Block> WALL_STREET_SIGN_STOP = BLOCKS.register("wall_street_sign_stop",
            ModBlocks::getWallStreetSignBlockProperties);
    public static final RegistryObject<Block> WALL_STREET_SIGN_YIELD = BLOCKS.register("wall_street_sign_yield",
            ModBlocks::getWallStreetSignBlockProperties);
    public static final RegistryObject<Block> WALL_STREET_SIGN_DANGER = BLOCKS.register("wall_street_sign_danger",
            ModBlocks::getWallStreetSignBlockProperties);
    public static final RegistryObject<Block> CEILING_LIGHT = BLOCKS.register("ceiling_light",
            () -> ModBlocks.CEILING_LIGHT_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> MIRROR = BLOCKS.register("mirror",
            ModBlocks::getMirrorBlockProperties);
    public static final RegistryObject<Block> MINING_ALERT_BLOCK = BLOCKS.register("mining_alert_block",
            () -> ModBlocks.MINING_ALERT_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> FOG_DIRT = BLOCKS.register("fog_dirt",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_DIRT_COATING = BLOCKS.register("fog_dirt_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_CLAY = BLOCKS.register("fog_clay",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_CLAY_COATING = BLOCKS.register("fog_clay_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_GRAVEL = BLOCKS.register("fog_gravel",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_GRAVEL_COATING = BLOCKS.register("fog_gravel_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_SAND = BLOCKS.register("fog_sand",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_SAND_COATING = BLOCKS.register("fog_sand_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_OAK_PLANKS = BLOCKS.register("fog_oak_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_OAK_PLANKS_COATING = BLOCKS.register("fog_oak_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_SPRUCE_PLANKS = BLOCKS.register("fog_spruce_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_SPRUCE_PLANKS_COATING = BLOCKS.register("fog_spruce_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_BIRCH_PLANKS = BLOCKS.register("fog_birch_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_BIRCH_PLANKS_COATING = BLOCKS.register("fog_birch_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_JUNGLE_PLANKS = BLOCKS.register("fog_jungle_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_JUNGLE_PLANKS_COATING = BLOCKS.register("fog_jungle_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_ACACIA_PLANKS = BLOCKS.register("fog_acacia_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_ACACIA_PLANKS_COATING = BLOCKS.register("fog_acacia_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_DARK_OAK_PLANKS = BLOCKS.register("fog_dark_oak_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_DARK_OAK_PLANKS_COATING = BLOCKS.register("fog_dark_oak_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_MANGROVE_PLANKS = BLOCKS.register("fog_mangrove_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_MANGROVE_PLANKS_COATING = BLOCKS.register("fog_mangrove_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_CHERRY_PLANKS = BLOCKS.register("fog_cherry_planks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_CHERRY_PLANKS_COATING = BLOCKS.register("fog_cherry_planks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_STONE = BLOCKS.register("fog_stone",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_STONE_COATING = BLOCKS.register("fog_stone_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_COAL_ORE = BLOCKS.register("fog_coal_ore",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_IRON_ORE = BLOCKS.register("fog_iron_ore",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_GOLD_ORE = BLOCKS.register("fog_gold_ore",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_LAPIS_ORE = BLOCKS.register("fog_lapis_ore",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_DIAMOND_ORE = BLOCKS.register("fog_diamond_ore",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_COBBLESTONE = BLOCKS.register("fog_cobblestone",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_COBBLESTONE_COATING = BLOCKS.register("fog_cobblestone_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_MOSSY_COBBLESTONE = BLOCKS.register("fog_mossy_cobblestone",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_MOSSY_COBBLESTONE_COATING = BLOCKS.register("fog_mossy_cobblestone_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_STONE_BRICKS = BLOCKS.register("fog_stone_bricks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_STONE_BRICKS_COATING = BLOCKS.register("fog_stone_bricks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_DEEPSLATE = BLOCKS.register("fog_deepslate",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_DEEPSLATE_COATING = BLOCKS.register("fog_deepslate_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_NETHERRACK = BLOCKS.register("fog_netherrack",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_NETHERRACK_COATING = BLOCKS.register("fog_netherrack_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_NETHER_BRICKS = BLOCKS.register("fog_nether_bricks",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_NETHER_BRICKS_COATING = BLOCKS.register("fog_nether_bricks_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> STEREO = BLOCKS.register("stereo",
            () -> ModBlocks.STEREO_PROPERTIES);
    public static final RegistryObject<Block> STAND_FIRE = BLOCKS.register("stand_fire",
            () -> ModBlocks.STAND_FIRE_PROPERTIES);
    public static final RegistryObject<Block> BUBBLE_SCAFFOLD = BLOCKS.register("bubble_scaffold",
            () -> ModBlocks.BUBBLE_SCAFFOLD_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> D4C_LIGHT_BLOCK = BLOCKS.register("d4c_light_block",
            () -> ModBlocks.D4C_LIGHT_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> ORANGE_FIRE = BLOCKS.register("colored_fire_orange",
            () -> ModBlocks.ORANGE_FIRE_PROPERTIES);
    public static final RegistryObject<Block> BLUE_FIRE = BLOCKS.register("colored_fire_blue",
            () -> ModBlocks.BLUE_FIRE_PROPERTIES);
    public static final RegistryObject<Block> PURPLE_FIRE = BLOCKS.register("colored_fire_purple",
            () -> ModBlocks.PURPLE_FIRE_PROPERTIES);
    public static final RegistryObject<Block> GREEN_FIRE = BLOCKS.register("colored_fire_green",
            () -> ModBlocks.GREEN_FIRE_PROPERTIES);
    public static final RegistryObject<Block> DREAD_FIRE = BLOCKS.register("colored_fire_dread",
            () -> ModBlocks.DREAD_FIRE_PROPERTIES);
    public static final RegistryObject<Block> CREAM_FIRE = BLOCKS.register("colored_fire_cream",
            () -> ModBlocks.CREAM_FIRE_PROPERTIES);
    public static final RegistryObject<BlockEntityType<MirrorBlockEntity>> MIRROR_BLOCK_ENTITY = BLOCK_ENTITIES.register("mirror",
            () -> BlockEntityType.Builder.of(MirrorBlockEntity::new, MIRROR.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "mirror")));
    public static final RegistryObject<BlockEntityType<StereoBlockEntity>> STEREO_BLOCK_ENTITY = BLOCK_ENTITIES.register("stereo",
            () -> BlockEntityType.Builder.of(StereoBlockEntity::new, STEREO.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "stereo")));
    public static final RegistryObject<BlockEntityType<StandFireBlockEntity>> STAND_FIRE_BLOCK_ENTITY = BLOCK_ENTITIES.register("stand_fire",
            () -> BlockEntityType.Builder.of(StandFireBlockEntity::new, STAND_FIRE.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "stand_fire")));
    public static final RegistryObject<BlockEntityType<BubbleScaffoldBlockEntity>> BUBBLE_SCAFFOLD_BLOCK_ENTITY = BLOCK_ENTITIES.register("block_scaffold",
            () -> BlockEntityType.Builder.of(BubbleScaffoldBlockEntity::new, BUBBLE_SCAFFOLD.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "block_scaffold")));
    public static final RegistryObject<BlockEntityType<D4CLightBlockEntity>> D4C_LIGHT_BLOCK_ENTITY = BLOCK_ENTITIES.register("d4c_light_block",
            () -> BlockEntityType.Builder.of(D4CLightBlockEntity::new, BUBBLE_SCAFFOLD.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "d4c_light_block")));

    static
    {
        //ModBlocks.registerDynamicFogBlocks();
    }
}