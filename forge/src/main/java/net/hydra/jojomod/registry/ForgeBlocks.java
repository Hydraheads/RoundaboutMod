package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.item.FogBlockItem;
import net.hydra.jojomod.item.FogCoatBlockItem;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import static net.hydra.jojomod.block.ModBlocks.*;
import static net.hydra.jojomod.registry.ForgeCreativeTab.FOG_TAB_ITEMS;
import static net.hydra.jojomod.registry.ForgeCreativeTab.addToFogTab;
import static net.hydra.jojomod.registry.ForgeItems.ITEMS;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Roundabout.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Roundabout.MOD_ID);

    public static final RegistryObject<Block> ANCIENT_METEOR = BLOCKS.register("ancient_meteor",
            () -> ModBlocks.ANCIENT_METEOR_PROPERTIES
    );
    public static final RegistryObject<Block> METEOR_BLOCK = BLOCKS.register("meteor_block",
            () -> ModBlocks.METEOR_BLOCK_PROPERTIES
            );
    public static final RegistryObject<Block> IMPACT_MOUND = BLOCKS.register("impact_mound",
            () -> IMPACT_MOUND_PROPERTIES
    );
    public static final RegistryObject<Block> EQUIPPABLE_STONE_MASK_BLOCK = BLOCKS.register("stone_mask",
            () -> ModBlocks.EQUIPPABLE_STONE_MASK_PROPERTIES
    );
    public static final RegistryObject<Block> BLOODY_STONE_MASK_BLOCK = BLOCKS.register("bloody_stone_mask",
            () -> BLOODY_STONE_MASK_PROPERTIES
    );
    public static final RegistryObject<Block> COFFIN_BLOCK = BLOCKS.register("coffin_block",
            () -> COFFIN_BLOCK_PROPERTIES
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
    public static final RegistryObject<Block> WOODEN_MANOR_CHAIR = BLOCKS.register("wooden_manor_chair",
            () -> ModBlocks.WOODEN_MANOR_CHAIR_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_WHITE = BLOCKS.register("wool_slab_white",
            () -> ModBlocks.WOOL_SLAB_WHITE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_BLACK = BLOCKS.register("wool_slab_black",
            () -> ModBlocks.WOOL_SLAB_BLACK_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_RED = BLOCKS.register("wool_slab_red",
            () -> ModBlocks.WOOL_SLAB_RED_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_LIGHT_GREY = BLOCKS.register("wool_slab_light_grey",
            () -> ModBlocks.WOOL_SLAB_LIGHT_GREY_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_BROWN = BLOCKS.register("wool_slab_brown",
            () -> ModBlocks.WOOL_SLAB_BROWN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_BLUE = BLOCKS.register("wool_slab_blue",
            () -> ModBlocks.WOOL_SLAB_BLUE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_CYAN = BLOCKS.register("wool_slab_cyan",
            () -> ModBlocks.WOOL_SLAB_CYAN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_DARK_GREEN = BLOCKS.register("wool_slab_dark_green",
            () -> ModBlocks.WOOL_SLAB_DARK_GREEN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_DARK_GREY = BLOCKS.register("wool_slab_dark_grey",
            () -> ModBlocks.WOOL_SLAB_DARK_GREY_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_GREEN = BLOCKS.register("wool_slab_green",
            () -> ModBlocks.WOOL_SLAB_GREEN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_LIGHT_BLUE = BLOCKS.register("wool_slab_light_blue",
            () -> ModBlocks.WOOL_SLAB_LIGHT_BLUE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_MAGENTA = BLOCKS.register("wool_slab_magenta",
            () -> ModBlocks.WOOL_SLAB_MAGENTA_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_ORANGE = BLOCKS.register("wool_slab_orange",
            () -> ModBlocks.WOOL_SLAB_ORANGE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_PURPLE = BLOCKS.register("wool_slab_purple",
            () -> ModBlocks.WOOL_SLAB_PURPLE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_PINK = BLOCKS.register("wool_slab_pink",
            () -> ModBlocks.WOOL_SLAB_PINK_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_SLAB_YELLOW = BLOCKS.register("wool_slab_yellow",
            () -> ModBlocks.WOOL_SLAB_YELLOW_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_WHITE = BLOCKS.register("wool_stairs_white",
            () -> ModBlocks.WOOL_STAIRS_WHITE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_BLUE = BLOCKS.register("wool_stairs_blue",
            () -> ModBlocks.WOOL_STAIRS_BLUE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_CYAN = BLOCKS.register("wool_stairs_cyan",
            () -> ModBlocks.WOOL_STAIRS_CYAN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_DARK_GREEN = BLOCKS.register("wool_stairs_dark_green",
            () -> ModBlocks.WOOL_STAIRS_DARK_GREEN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_DARK_GREY = BLOCKS.register("wool_stairs_dark_grey",
            () -> ModBlocks.WOOL_STAIRS_DARK_GREY_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_GREEN = BLOCKS.register("wool_stairs_green",
            () -> ModBlocks.WOOL_STAIRS_GREEN_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_LIGHT_BLUE = BLOCKS.register("wool_stairs_light_blue",
            () -> ModBlocks.WOOL_STAIRS_LIGHT_BLUE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_MAGENTA = BLOCKS.register("wool_stairs_magenta",
            () -> ModBlocks.WOOL_STAIRS_MAGENTA_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_ORANGE = BLOCKS.register("wool_stairs_orange",
            () -> ModBlocks.WOOL_STAIRS_ORANGE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_PURPLE = BLOCKS.register("wool_stairs_purple",
            () -> ModBlocks.WOOL_STAIRS_PURPLE_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_PINK = BLOCKS.register("wool_stairs_pink",
            () -> ModBlocks.WOOL_STAIRS_PINK_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_RED = BLOCKS.register("wool_stairs_red",
            () -> ModBlocks.WOOL_STAIRS_RED_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_YELLOW = BLOCKS.register("wool_stairs_yellow",
            () -> ModBlocks.WOOL_STAIRS_YELLOW_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_LIGHT_GREY = BLOCKS.register("wool_stairs_light_grey",
            () -> ModBlocks.WOOL_STAIRS_LIGHT_GREY_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_BLACK = BLOCKS.register("wool_stairs_black",
            () -> ModBlocks.WOOL_STAIRS_BLACK_PROPERTIES
    );
    public static final RegistryObject<Block> WOOL_STAIRS_BROWN = BLOCKS.register("wool_stairs_brown",
            () -> ModBlocks.WOOL_STAIRS_BROWN_PROPERTIES
    );

    public static final RegistryObject<Block> WALL_LANTERN = BLOCKS.register("wall_lantern",
            () -> ModBlocks.WALL_LANTERN_PROPERTIES
    );
    public static final RegistryObject<Block> GLASS_DOOR = BLOCKS.register("glass_door",
            () -> ModBlocks.GLASS_DOOR_PROPERTIES
    );

    public static final RegistryObject<Block> CULTIVATION_POT = BLOCKS.register("cultivation_pot",
            () -> ModBlocks.cultivationPot(Blocks.AIR)
    );
    public static final RegistryObject<Block> CULTIVATED_CHERRY_SAPLING = BLOCKS.register("cultivated_cherry_sapling",
            () -> ModBlocks.cultivationPot(Blocks.CHERRY_SAPLING)
    );
    public static final RegistryObject<Block> CULTIVATED_OAK_SAPLING = BLOCKS.register("cultivated_oak_sapling",
            () -> ModBlocks.cultivationPot(Blocks.OAK_SAPLING)
    );

    public static final RegistryObject<Block> MELON_PARFAIT = BLOCKS.register("melon_parfait",
            () -> MELON_PARFAIT_PROPERTIES);


    public static final RegistryObject<Block> LOCACACA_CACTUS = BLOCKS.register("locacaca_cactus",
            () -> ModBlocks.LOCACACA_CACTUS_PROPERTIES
    );
    public static final RegistryObject<Block> LOCACACA_BLOCK = BLOCKS.register("locacaca_plant",
            () -> ModBlocks.LOCACACA_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> NEW_LOCACACA_BLOCK = BLOCKS.register("new_locacaca_plant",
            () -> ModBlocks.NEW_LOCACACA_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> CULTIVATED_LOCACACA = BLOCKS.register("cultivated_locacaca",
            () -> ModBlocks.cultivationPot(NEW_LOCACACA_BLOCK.get()));

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
    public static final RegistryObject<Block> FLESH_BLOCK = BLOCKS.register("flesh_block",
            () -> ModBlocks.FLESH_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> FOG_DIRT_COATING = BLOCKS.register("fog_dirt_coating",
            ModBlocks::getFogCoatingBlock);
    public static final RegistryObject<Block> FOG_DIRT = BLOCKS.register("fog_dirt",
            ModBlocks::getFogBlock);
    public static final RegistryObject<Block> FOG_TRAP = BLOCKS.register("fog_trap",ModBlocks::getFogTrapBlock);
    public static final RegistryObject<Block> STEREO = BLOCKS.register("stereo",
            () -> ModBlocks.STEREO_PROPERTIES);
    public static final RegistryObject<Block> STAND_FIRE = BLOCKS.register("stand_fire",
            () -> ModBlocks.STAND_FIRE_PROPERTIES);
    public static final RegistryObject<Block> BUBBLE_SCAFFOLD = BLOCKS.register("bubble_scaffold",
            () -> ModBlocks.BUBBLE_SCAFFOLD_BLOCK_PROPERTIES);
    public static final RegistryObject<Block> INVISIBLOCK = BLOCKS.register("invisible_block",
            () -> ModBlocks.INVISIBLE_BLOCK_PROPERTIES);
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
    public static final RegistryObject<BlockEntityType<InvisiBlockEntity>> INVISIBLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("invisible_block",
            () -> BlockEntityType.Builder.of(InvisiBlockEntity::new, INVISIBLOCK.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "invisible_block")));
    public static final RegistryObject<BlockEntityType<FogTrapBlockEntity>> FOG_TRAP_BLOCK_ENTITY = BLOCK_ENTITIES.register("fog_trap",
            () -> BlockEntityType.Builder.of(FogTrapBlockEntity::new, FOG_TRAP.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "fog_trap")));
    public static final RegistryObject<BlockEntityType<D4CLightBlockEntity>> D4C_LIGHT_BLOCK_ENTITY = BLOCK_ENTITIES.register("d4c_light_block",
            () -> BlockEntityType.Builder.of(D4CLightBlockEntity::new, D4C_LIGHT_BLOCK.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "d4c_light_block")));

    static boolean genned = false;

    public static Map<ResourceLocation,Block> fogCoatingBlocks = new HashMap<>();
    public static Map<ResourceLocation,Block> fogBlocks = new HashMap<>();
    @SubscribeEvent
    public static void registerDynamicFogBlocks(RegisterEvent event)
    {

        if(event.getForgeRegistry() == null || genned){
            return;
        }


        //Resource location of new fog block + instance of original block.

        // TODO: dynamically generate blockstates and then it's ready for use
        for (Block b : ForgeRegistries.BLOCKS)
        {
            ResourceLocation i = ForgeRegistries.BLOCKS.getKey(b);
            // fix for not registering our own blocks as fog blocks, would result in a deadlock (or an error tbh)
            if (!i.getNamespace().equals("minecraft") || ForgeRegistries.BLOCKS.containsKey(new ResourceLocation("roundabout", "fog_" + i.getPath())) || blockBlacklist.contains(i.getPath()) || dontGen.contains("fog_"+i.getPath())) {
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
                }
            }
            if (b.defaultBlockState().getProperties().isEmpty() && rightSize) {

                ForgeRegistries.BLOCKS.register(
                        new ResourceLocation(Roundabout.MOD_ID, "fog_" + i.getPath()),
                        getFogBlock());
                Block fb = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Roundabout.MOD_ID, "fog_" + i.getPath()));
                fogBlocks.put(new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath()), fb);

                if(!blockBlacklist.contains("fog_" + i.getPath() + "_coating")) {

                    ForgeRegistries.BLOCKS.register(
                            new ResourceLocation(Roundabout.MOD_ID, "fog_" + i.getPath() + "_coating"),
                            getFogCoatingBlock());
                    Block fc = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath() + "_coating"));

                    fogCoatingBlocks.put(new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath() + "_coating"),fc);
                    ForgeRegistries.ITEMS.register(
                            new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath() + "_coating"),
                            (Item) new FogCoatBlockItem(fc, new Item.Properties(), b)
                    );
                    gennedFogItems.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath() + "_coating")));

                }

                ForgeRegistries.ITEMS.register(new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath()),
                        (Item) new FogBlockItem(fb, new Item.Properties(), b));
                gennedFogItems.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Roundabout.MOD_ID,"fog_" + i.getPath())));


            } else {
                //Roundabout.LOGGER.warn("Skipping block {} as it has unsupported properties", i);
                continue;
            }
        }
        for (Item i : gennedFogItems){
            FOG_TAB_ITEMS.add(() -> i);
        }

        /*
        event.register(ForgeRegistries.Keys.BLOCKS,blockRegisterHelper -> {
            for(Map.Entry<ResourceLocation,Block> entry : fogBlockOGs.entrySet()){
                blockRegisterHelper.register(entry.getKey(),getFogBlock());
                fogBlocks.put(entry.getKey(), (Block) event.getForgeRegistry().getValue(entry.getKey()));

            }
            for(Map.Entry<ResourceLocation,Block> entry : fogBlockCoatOGs.entrySet()){
                blockRegisterHelper.register(entry.getKey(),getFogCoatingBlock());
                fogCoatingBlocks.put(entry.getKey(), (Block) event.getForgeRegistry().getValue(entry.getKey()));
            }
        });
        event.register(ForgeRegistries.Keys.ITEMS,helper->{
            for(Map.Entry<ResourceLocation,Block> entry : fogBlocks.entrySet()){
                helper.register(entry.getKey(), new FogBlockItem(entry.getValue(), new Item.Properties(), fogBlockOGs.get(entry.getKey())));
            }
            for(Map.Entry<ResourceLocation,Block> entry : fogCoatingBlocks.entrySet()){
                helper.register(entry.getKey(), new FogCoatBlockItem(entry.getValue(), new Item.Properties(), fogBlockCoatOGs.get(entry.getKey())));
            }

        });

         */


        genned = true;
    }
}
