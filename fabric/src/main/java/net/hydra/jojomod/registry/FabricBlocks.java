package net.hydra.jojomod.registry;

import com.mojang.datafixers.types.Type;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.item.*;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.hydra.jojomod.block.ModBlocks.*;

public class FabricBlocks {
    public static final Block ANCIENT_METEOR = registerBlock("ancient_meteor", ModBlocks.ANCIENT_METEOR_PROPERTIES
    );
    public static final Block IMPACT_MOUND = registerBlock("impact_mound", IMPACT_MOUND_PROPERTIES
    );
    public static final Block METEOR_BLOCK = registerBlock("meteor_block", ModBlocks.METEOR_BLOCK_PROPERTIES
    );
    public static final Block REGAL_FLOOR = registerBlock("regal_floor", ModBlocks.REGAL_FLOOR_PROPERTIES
    );
    public static final Block REGAL_WALL = registerBlock("regal_wall", ModBlocks.REGAL_WALL_PROPERTIES
    );
    public static final Block WOODEN_MANOR_TABLE = registerBlock("wooden_manor_table", ModBlocks.WOODEN_MANOR_TABLE_PROPERTIES
    );
    public static final Block WOODEN_MANOR_CHAIR = registerBlock("wooden_manor_chair", ModBlocks.WOODEN_MANOR_CHAIR_PROPERTIES
    );
    public static final Block WOOL_SLAB_WHITE = registerBlock("wool_slab_white", ModBlocks.WOOL_SLAB_WHITE_PROPERTIES
    );
    public static final Block WOOL_SLAB_BLACK = registerBlock("wool_slab_black", ModBlocks.WOOL_SLAB_BLACK_PROPERTIES
    );
    public static final Block WOOL_SLAB_RED = registerBlock("wool_slab_red", ModBlocks.WOOL_SLAB_RED_PROPERTIES
    );
    public static final Block WOOL_SLAB_LIGHT_GREY = registerBlock("wool_slab_light_grey", ModBlocks.WOOL_SLAB_LIGHT_GREY_PROPERTIES
    );
    public static final Block WOOL_SLAB_BROWN = registerBlock("wool_slab_brown", ModBlocks.WOOL_SLAB_BROWN_PROPERTIES
    );
    public static final Block WOOL_SLAB_BLUE = registerBlock("wool_slab_blue", ModBlocks.WOOL_SLAB_BLUE_PROPERTIES
    );
    public static final Block WOOL_SLAB_CYAN = registerBlock("wool_slab_cyan", ModBlocks.WOOL_SLAB_CYAN_PROPERTIES
    );
    public static final Block WOOL_SLAB_DARK_GREEN = registerBlock("wool_slab_dark_green", ModBlocks.WOOL_SLAB_DARK_GREEN_PROPERTIES
    );
    public static final Block WOOL_SLAB_DARK_GREY = registerBlock("wool_slab_dark_grey", ModBlocks.WOOL_SLAB_DARK_GREY_PROPERTIES
    );
    public static final Block WOOL_SLAB_GREEN = registerBlock("wool_slab_green", ModBlocks.WOOL_SLAB_GREEN_PROPERTIES
    );
    public static final Block WOOL_SLAB_LIGHT_BLUE = registerBlock("wool_slab_light_blue", ModBlocks.WOOL_SLAB_LIGHT_BLUE_PROPERTIES
    );
    public static final Block WOOL_SLAB_MAGENTA = registerBlock("wool_slab_magenta", ModBlocks.WOOL_SLAB_MAGENTA_PROPERTIES
    );
    public static final Block WOOL_SLAB_ORANGE = registerBlock("wool_slab_orange", ModBlocks.WOOL_SLAB_ORANGE_PROPERTIES
    );
    public static final Block WOOL_SLAB_PURPLE = registerBlock("wool_slab_purple", ModBlocks.WOOL_SLAB_PURPLE_PROPERTIES
    );
    public static final Block WOOL_SLAB_PINK = registerBlock("wool_slab_pink", ModBlocks.WOOL_SLAB_PINK_PROPERTIES
    );
    public static final Block WOOL_SLAB_YELLOW = registerBlock("wool_slab_yellow", ModBlocks.WOOL_SLAB_YELLOW_PROPERTIES
    );
    public static final Block WOOL_STAIRS_WHITE = registerBlock("wool_stairs_white", ModBlocks.WOOL_STAIRS_WHITE_PROPERTIES
    );
    public static final Block WOOL_STAIRS_BLUE = registerBlock("wool_stairs_blue", ModBlocks.WOOL_STAIRS_BLUE_PROPERTIES
    );
    public static final Block WOOL_STAIRS_CYAN = registerBlock("wool_stairs_cyan", ModBlocks.WOOL_STAIRS_CYAN_PROPERTIES
    );
    public static final Block WOOL_STAIRS_DARK_GREEN = registerBlock("wool_stairs_dark_green", ModBlocks.WOOL_STAIRS_DARK_GREEN_PROPERTIES
    );
    public static final Block WOOL_STAIRS_DARK_GREY = registerBlock("wool_stairs_dark_grey", ModBlocks.WOOL_STAIRS_DARK_GREY_PROPERTIES
    );
    public static final Block WOOL_STAIRS_GREEN = registerBlock("wool_stairs_green", ModBlocks.WOOL_STAIRS_GREEN_PROPERTIES
    );
    public static final Block WOOL_STAIRS_LIGHT_BLUE = registerBlock("wool_stairs_light_blue", ModBlocks.WOOL_STAIRS_LIGHT_BLUE_PROPERTIES
    );
    public static final Block WOOL_STAIRS_MAGENTA = registerBlock("wool_stairs_magenta", ModBlocks.WOOL_STAIRS_MAGENTA_PROPERTIES
    );
    public static final Block WOOL_STAIRS_ORANGE = registerBlock("wool_stairs_orange", ModBlocks.WOOL_STAIRS_ORANGE_PROPERTIES
    );
    public static final Block WOOL_STAIRS_PURPLE = registerBlock("wool_stairs_purple", ModBlocks.WOOL_STAIRS_PURPLE_PROPERTIES
    );
    public static final Block WOOL_STAIRS_PINK = registerBlock("wool_stairs_pink", ModBlocks.WOOL_STAIRS_PINK_PROPERTIES
    );
    public static final Block WOOL_STAIRS_RED = registerBlock("wool_stairs_red", ModBlocks.WOOL_STAIRS_RED_PROPERTIES
    );
    public static final Block WOOL_STAIRS_YELLOW = registerBlock("wool_stairs_yellow", ModBlocks.WOOL_STAIRS_YELLOW_PROPERTIES
    );
    public static final Block WOOL_STAIRS_LIGHT_GREY = registerBlock("wool_stairs_light_grey", ModBlocks.WOOL_STAIRS_LIGHT_GREY_PROPERTIES
    );
    public static final Block WOOL_STAIRS_BLACK = registerBlock("wool_stairs_black", ModBlocks.WOOL_STAIRS_BLACK_PROPERTIES
    );
    public static final Block WOOL_STAIRS_BROWN = registerBlock("wool_stairs_brown", ModBlocks.WOOL_STAIRS_BROWN_PROPERTIES
    );
    public static final Block WALL_LANTERN = registerBlock("wall_lantern", ModBlocks.WALL_LANTERN_PROPERTIES
    );
    public static final Block GLASS_DOOR = registerBlock("glass_door", ModBlocks.GLASS_DOOR_PROPERTIES
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
    public static final Block STREET_SIGN_DIO = registerBlockUnstackableItemless("street_sign_dio",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_RIGHT = registerBlockUnstackableItemless("street_sign_right",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_STOP = registerBlockUnstackableItemless("street_sign_stop",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_YIELD = registerBlockUnstackableItemless("street_sign_yield",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block STREET_SIGN_DANGER = registerBlockUnstackableItemless("street_sign_danger",ModBlocks.getStreetSignBlockProperties()
            , 1);
    public static final Block WALL_STREET_SIGN_DIO = registerBlock("wall_street_sign_dio",ModBlocks.getWallStreetSignBlockProperties());
    public static final Block WALL_STREET_SIGN_RIGHT = registerBlock("wall_street_sign_right",ModBlocks.getWallStreetSignBlockProperties());
    public static final Block WALL_STREET_SIGN_STOP = registerBlock("wall_street_sign_stop",ModBlocks.getWallStreetSignBlockProperties());
    public static final Block WALL_STREET_SIGN_YIELD = registerBlock("wall_street_sign_yield",ModBlocks.getWallStreetSignBlockProperties());
    public static final Block WALL_STREET_SIGN_DANGER = registerBlock("wall_street_sign_danger",ModBlocks.getWallStreetSignBlockProperties());
    public static final Block CEILING_LIGHT = registerBlock("ceiling_light",ModBlocks.CEILING_LIGHT_BLOCK_PROPERTIES);
    public static final Block MIRROR = registerBlock("mirror",ModBlocks.getMirrorBlockProperties());
    public static final Block BUBBLE_SCAFFOLD = registerBlockItemless("bubble_scaffold",ModBlocks.BUBBLE_SCAFFOLD_BLOCK_PROPERTIES);
    public static final Block INVISIBLOCK = registerBlockItemless("invisible_block",ModBlocks.INVISIBLE_BLOCK_PROPERTIES);
    public static final Block D4C_LIGHT_BLOCK = registerBlockItemless("d4c_light_block",ModBlocks.D4C_LIGHT_BLOCK_PROPERTIES);

    public static final Block STEREO = registerBlock("stereo",ModBlocks.STEREO_PROPERTIES);
    public static final Block CULTIVATION_POT = registerBlock("cultivation_pot",ModBlocks.cultivationPot(Blocks.AIR));
    public static final Block CULTIVATED_CHERRY_SAPLING = registerBlockItemless("cultivated_cherry_sapling",ModBlocks.cultivationPot(Blocks.CHERRY_SAPLING));
    public static final Block CULTIVATED_OAK_SAPLING = registerBlockItemless("cultivated_oak_sapling",ModBlocks.cultivationPot(Blocks.OAK_SAPLING));
    public static final Block CULTIVATED_LOCACACA = registerBlockItemless("cultivated_locacaca",ModBlocks.cultivationPot(NEW_LOCACACA_BLOCK));

    public static final Block FLESH_BLOCK = registerFleshBlockFood("flesh_block",ModBlocks.FLESH_BLOCK_PROPERTIES, ModFoodComponents.FLESH_CHUNK);
    public static final Block MINING_ALERT_BLOCK = registerBlockItemless("mining_alert_block",ModBlocks.MINING_ALERT_BLOCK_PROPERTIES);
    public static final Block STAND_FIRE = registerBlockItemless("stand_fire",ModBlocks.STAND_FIRE_PROPERTIES);
    public static final Block ORANGE_FIRE = registerBlockItemless("colored_fire_orange",ModBlocks.ORANGE_FIRE_PROPERTIES);
    public static final Block BLUE_FIRE = registerBlockItemless("colored_fire_blue",ModBlocks.BLUE_FIRE_PROPERTIES);
    public static final Block PURPLE_FIRE = registerBlockItemless("colored_fire_purple",ModBlocks.PURPLE_FIRE_PROPERTIES);
    public static final Block GREEN_FIRE = registerBlockItemless("colored_fire_green",ModBlocks.GREEN_FIRE_PROPERTIES);
    public static final Block DREAD_FIRE = registerBlockItemless("colored_fire_dread",ModBlocks.DREAD_FIRE_PROPERTIES);
    public static final Block CREAM_FIRE = registerBlockItemless("colored_fire_cream",ModBlocks.CREAM_FIRE_PROPERTIES);

    public static final Block FOG_DIRT_COATING = registerBlockItemless("fog_dirt_coating", getFogCoatingBlock());
    public static final Block FOG_DIRT = registerBlockItemless("fog_dirt", getFogBlock());
    public static final Block FOG_TRAP = registerBlockItemless("fog_trap",getFogTrapBlock());

    public static final Block EQUIPPABLE_STONE_MASK = registerStoneMask("stone_mask", ModBlocks.EQUIPPABLE_STONE_MASK_PROPERTIES);
    public static final Block BLOODY_STONE_MASK = registerStoneMaskBloody("bloody_stone_mask", BLOODY_STONE_MASK_PROPERTIES);


    public static final BlockEntityType<StereoBlockEntity> STEREO_BLOCK_ENTITY =
           registerBE("stereo",BlockEntityType.Builder.of(StereoBlockEntity::new, STEREO));
    public static final BlockEntityType<StandFireBlockEntity> STAND_FIRE_BLOCK_ENTITY =
            registerBE("stand_fire",BlockEntityType.Builder.of(StandFireBlockEntity::new, STAND_FIRE));
    public static final BlockEntityType<MirrorBlockEntity> MIRROR_BLOCK_ENTITY =
            registerBE("mirror",BlockEntityType.Builder.of(MirrorBlockEntity::new, MIRROR));
    public static final BlockEntityType<BubbleScaffoldBlockEntity> BUBBLE_SCAFFOLD_BLOCK_ENTITY =
            registerBE("bubble_scaffold",BlockEntityType.Builder.of(BubbleScaffoldBlockEntity::new, BUBBLE_SCAFFOLD));
    public static final BlockEntityType<InvisiBlockEntity> INVISI_BLOCK_ENTITY =
            registerBE("invisible_block",BlockEntityType.Builder.of(InvisiBlockEntity::new, INVISIBLOCK));
    public static final BlockEntityType<FogTrapBlockEntity> FOGTRAP_BLOCKENTITY =
            registerBE("fog_trap",BlockEntityType.Builder.of(FogTrapBlockEntity::new, FOG_TRAP) );
    public static final BlockEntityType<D4CLightBlockEntity> D4C_LIGHT_BLOCK_ENTITY =
            registerBE("d4c_light_block",BlockEntityType.Builder.of(D4CLightBlockEntity::new, D4C_LIGHT_BLOCK));


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
    private static Block registerFleshBlockFood(String name, Block block, FoodProperties food) {
        // a little lazy, but I don't fully understand java yet and I doubt there will be anything similar soon
        registerFleshBlockFoodItem(name, block,food);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);

    }
    private static Block registerBlockUnstackable(String name, Block block, int stacksize) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new BlockItem(block, new Item.Properties().stacksTo(stacksize)));
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }
    private static Block registerBlockUnstackableItemless(String name, Block block, int stacksize) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }

    private static Item registerFleshBlockFoodItem(String name, Block block, FoodProperties food) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new FleshChunkItem(block, new Item.Properties().food(food)));
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
    }

    private static Block registerStoneMask(String name, Block block) {
        registerStoneMaskBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }
    private static Item registerStoneMaskBlockItem(String name, Block block){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new StoneMaskBlockItem(block, new Item.Properties().stacksTo(1)));
    }
    private static Block registerStoneMaskBloody(String name, Block block) {
        registerStoneMaskBloodyBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }
    private static Item registerStoneMaskBloodyBlockItem(String name, Block block){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new BloodyStoneMaskBlockItem(block, new Item.Properties().stacksTo(1)));
    }

    public static void register(){
        ModBlocks.ANCIENT_METEOR = ANCIENT_METEOR;
        ModBlocks.IMPACT_MOUND = IMPACT_MOUND;
        ModBlocks.METEOR_BLOCK = METEOR_BLOCK;
        ModBlocks.REGAL_FLOOR = REGAL_FLOOR;
        ModBlocks.REGAL_WALL = REGAL_WALL;
        ModBlocks.WOODEN_MANOR_TABLE = WOODEN_MANOR_TABLE;
        ModBlocks.WOODEN_MANOR_CHAIR = WOODEN_MANOR_CHAIR;
        ModBlocks.WOOL_SLAB_WHITE = WOOL_SLAB_WHITE;
        ModBlocks.WOOL_STAIRS_WHITE = WOOL_STAIRS_WHITE;
        ModBlocks.WOOL_SLAB_BLACK = WOOL_SLAB_BLACK;
        ModBlocks.WOOL_STAIRS_BLACK = WOOL_STAIRS_BLACK;
        ModBlocks.WOOL_SLAB_BLUE = WOOL_SLAB_BLUE;
        ModBlocks.WOOL_STAIRS_BLUE = WOOL_STAIRS_BLUE;
        ModBlocks.WOOL_SLAB_BROWN = WOOL_SLAB_BROWN;
        ModBlocks.WOOL_STAIRS_BROWN = WOOL_STAIRS_BROWN;
        ModBlocks.WOOL_SLAB_CYAN = WOOL_SLAB_CYAN;
        ModBlocks.WOOL_STAIRS_CYAN = WOOL_STAIRS_CYAN;
        ModBlocks.WOOL_SLAB_DARK_GREEN = WOOL_SLAB_DARK_GREEN;
        ModBlocks.WOOL_STAIRS_DARK_GREEN = WOOL_STAIRS_DARK_GREEN;
        ModBlocks.WOOL_SLAB_DARK_GREY = WOOL_SLAB_DARK_GREY;
        ModBlocks.WOOL_STAIRS_DARK_GREY = WOOL_STAIRS_DARK_GREY;
        ModBlocks.WOOL_SLAB_GREEN = WOOL_SLAB_GREEN;
        ModBlocks.WOOL_STAIRS_GREEN = WOOL_STAIRS_GREEN;
        ModBlocks.WOOL_SLAB_LIGHT_BLUE = WOOL_SLAB_LIGHT_BLUE;
        ModBlocks.WOOL_STAIRS_LIGHT_BLUE = WOOL_STAIRS_LIGHT_BLUE;
        ModBlocks.WOOL_SLAB_LIGHT_GREY = WOOL_SLAB_LIGHT_GREY;
        ModBlocks.WOOL_STAIRS_LIGHT_GREY = WOOL_STAIRS_LIGHT_GREY;
        ModBlocks.WOOL_SLAB_MAGENTA = WOOL_SLAB_MAGENTA;
        ModBlocks.WOOL_STAIRS_MAGENTA = WOOL_STAIRS_MAGENTA;
        ModBlocks.WOOL_SLAB_ORANGE = WOOL_SLAB_ORANGE;
        ModBlocks.WOOL_STAIRS_ORANGE = WOOL_STAIRS_ORANGE;
        ModBlocks.WOOL_SLAB_PURPLE = WOOL_SLAB_PURPLE;
        ModBlocks.WOOL_STAIRS_PURPLE = WOOL_STAIRS_PURPLE;
        ModBlocks.WOOL_SLAB_PINK = WOOL_SLAB_PINK;
        ModBlocks.WOOL_STAIRS_PINK = WOOL_STAIRS_PINK;
        ModBlocks.WOOL_SLAB_RED = WOOL_SLAB_RED;
        ModBlocks.WOOL_STAIRS_RED = WOOL_STAIRS_RED;
        ModBlocks.WOOL_SLAB_YELLOW = WOOL_SLAB_YELLOW;
        ModBlocks.WOOL_STAIRS_YELLOW = WOOL_STAIRS_YELLOW;
        ModBlocks.WALL_LANTERN = WALL_LANTERN;
        ModBlocks.GLASS_DOOR = GLASS_DOOR;
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
        ModBlocks.WALL_STREET_SIGN_DIO = WALL_STREET_SIGN_DIO;
        ModBlocks.WALL_STREET_SIGN_RIGHT = WALL_STREET_SIGN_RIGHT;
        ModBlocks.WALL_STREET_SIGN_STOP = WALL_STREET_SIGN_STOP;
        ModBlocks.WALL_STREET_SIGN_YIELD = WALL_STREET_SIGN_YIELD;
        ModBlocks.WALL_STREET_SIGN_DANGER = WALL_STREET_SIGN_DANGER;
        ModBlocks.CEILING_LIGHT = CEILING_LIGHT;
        ModBlocks.MIRROR = MIRROR;
        ModBlocks.STEREO = STEREO;
        ModBlocks.MINING_ALERT_BLOCK = MINING_ALERT_BLOCK;
        ModBlocks.BUBBLE_SCAFFOLD = BUBBLE_SCAFFOLD;
        ModBlocks.FLESH_BLOCK = FLESH_BLOCK;
        ModBlocks.CULTIVATION_POT = CULTIVATION_POT;
        ModBlocks.CULTIVATED_CHERRY_SAPLING = CULTIVATED_CHERRY_SAPLING;
        ModBlocks.CULTIVATED_OAK_SAPLING = CULTIVATED_OAK_SAPLING;
        ModBlocks.CULTIVATED_LOCACACA = CULTIVATED_LOCACACA;

        ModBlocks.INVISIBLOCK = INVISIBLOCK;
        ModBlocks.STAND_FIRE = STAND_FIRE;
        ModBlocks.ORANGE_FIRE = ORANGE_FIRE;
        ModBlocks.BLUE_FIRE = BLUE_FIRE;
        ModBlocks.PURPLE_FIRE = PURPLE_FIRE;
        ModBlocks.GREEN_FIRE = GREEN_FIRE;
        ModBlocks.DREAD_FIRE = DREAD_FIRE;
        ModBlocks.CREAM_FIRE = CREAM_FIRE;
        ModBlocks.STEREO_BLOCK_ENTITY = STEREO_BLOCK_ENTITY;
        ModBlocks.STAND_FIRE_BLOCK_ENTITY = STAND_FIRE_BLOCK_ENTITY;
        ModBlocks.MIRROR_BLOCK_ENTITY = MIRROR_BLOCK_ENTITY;
        ModBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY = BUBBLE_SCAFFOLD_BLOCK_ENTITY;
        ModBlocks.INVISIBLE_BLOCK_ENTITY = INVISI_BLOCK_ENTITY;
        FOG_TRAP_BLOCK_ENTITY = FOGTRAP_BLOCKENTITY;
        ModBlocks.D4C_LIGHT_BLOCK_ENTITY = D4C_LIGHT_BLOCK_ENTITY;
        ModBlocks.D4C_LIGHT_BLOCK = D4C_LIGHT_BLOCK;
        ModBlocks.FOG_DIRT = FOG_DIRT;
        ModBlocks.FOG_DIRT_COATING = FOG_DIRT_COATING;
        ModBlocks.FOG_TRAP = FOG_TRAP;
        ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK = EQUIPPABLE_STONE_MASK;
        ModBlocks.BLOODY_STONE_MASK_BLOCK = BLOODY_STONE_MASK;

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        registerDynamicFogBlocks();
    }
    public static void registerDynamicFogBlocks()
    {
        // TODO: dynamically generate blockstates and then it's ready for use
        for (Block b : BuiltInRegistries.BLOCK)
        {
            ResourceLocation i = BuiltInRegistries.BLOCK.getKey(b);
            // fix for not registering our own blocks as fog blocks, would result in a deadlock (or an error tbh)
            if (!i.getNamespace().equals("minecraft") || BuiltInRegistries.BLOCK.containsKey(new ResourceLocation("roundabout", "fog_" + i.getPath())) || blockBlacklist.contains(i.getPath())) {
                dontGenState.add("fog_" + i.getPath());
                continue;
            }
            //Roundabout.LOGGER.info("Registering block \"roundabout:fog_{}\"",i.getPath());
            boolean rightSize = false;
            try {
                VoxelShape vshape = b.defaultBlockState().getCollisionShape(null, null);
                rightSize = !vshape.isEmpty() && vshape.bounds().getYsize() == 1.0;
            } catch (Exception e) {
                try{
                    rightSize = b.defaultBlockState().isCollisionShapeFullBlock(null,null);
                } catch (Exception ex) {
                    Roundabout.LOGGER.debug("Couldn't get the shape of " + i.getPath());
                    rightSize = true;
                }
            }
            if (b.defaultBlockState().getProperties().isEmpty() && rightSize) {
                Block fb = Registry.register(BuiltInRegistries.BLOCK,
                        new ResourceLocation(Roundabout.MOD_ID, "fog_" + i.getPath()),
                        getFogBlock());
                if(!blockBlacklist.contains("fog_" + i.getPath() + "_coating")) {
                    Block fc = Registry.register(BuiltInRegistries.BLOCK,
                            new ResourceLocation(Roundabout.MOD_ID, "fog_" + i.getPath() + "_coating"),
                            getFogCoatingBlock());
                    gennedFogItems.add(Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath() + "_coating"), (Item) new FogCoatBlockItem(fc, new Item.Properties(), b)));
                }
                gennedFogItems.add(Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath()), (Item) new FogBlockItem(fb, new Item.Properties(), b)));


            } else {
                //Roundabout.LOGGER.warn("Skipping block {} as it has unsupported properties", i);
                continue;
            }
        }
    }
}

