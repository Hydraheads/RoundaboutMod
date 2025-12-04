package net.hydra.jojomod.item;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.VisageStoreEntry;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static Item STAND_ARROW;
    public static Item STAND_BEETLE_ARROW;
    public static Item WORTHY_ARROW;
    public static Item STAND_DISC_STAR_PLATINUM;
    public static Item MAX_STAND_DISC_STAR_PLATINUM;
    public static Item STAND_DISC_THE_WORLD;
    public static Item MAX_STAND_DISC_THE_WORLD;
    public static Item STAND_DISC_JUSTICE;
    public static Item MAX_STAND_DISC_JUSTICE;
    public static Item MAX_STAND_DISC_MAGICIANS_RED;
    public static Item STAND_DISC_MAGICIANS_RED;
    public static Item STAND_DISC_D4C;
    public static Item MAX_STAND_DISC_D4C;
    public static Item STAND_DISC_RATT;
    public static Item MAX_STAND_DISC_RATT;
    public static Item STAND_DISC_ANUBIS;
    public static Item MAX_STAND_DISC_ANUBIS;
    public static Item STAND_DISC_WALKING_HEART;
    public static Item MAX_STAND_DISC_WALKING_HEART;
    public static Item STAND_DISC_GREEN_DAY;
    public static Item MAX_STAND_DISC_GREEN_DAY;
    public static Item STAND_DISC_SOFT_AND_WET;
    public static Item MAX_STAND_DISC_SOFT_AND_WET;
    public static Item STAND_DISC_KILLER_QUEEN;
    public static Item MAX_STAND_DISC_KILLER_QUEEN;
    public static Item STAND_DISC_CINDERELLA;
    public static Item STAND_DISC_HEY_YA;
    public static Item STAND_DISC_MANDOM;
    public static Item STAND_DISC_SURVIVOR;
    public static Item STAND_DISC_ACHTUNG;
    public static Item STAND_DISC_DIVER_DOWN;
    public static Item MAX_STAND_DISC_DIVER_DOWN;
    public static Item STAND_DISC_CREAM;
    public static Item MAX_STAND_DISC_CREAM;
    public static Item STAND_DISC;
    public static Item STREET_SIGN_DIO_BLOCK_ITEM;
    public static Item STREET_SIGN_RIGHT_BLOCK_ITEM;
    public static Item STREET_SIGN_STOP_BLOCK_ITEM;
    public static Item STREET_SIGN_YIELD_BLOCK_ITEM;
    public static Item STREET_SIGN_DANGER_BLOCK_ITEM;
    public static Item LIGHT_BULB;
    public static Item LOCACACA_PIT;
    public static Item LOCACACA;
    public static Item LOCACACA_BRANCH;
    public static Item NEW_LOCACACA;
    public static Item COFFEE_GUM;
    public static Item METEORITE;
    public static Item METEORITE_INGOT;
    public static Item TERRIER_SPAWN_EGG;
    public static Item AESTHETICIAN_SPAWN_EGG;
    public static Item ZOMBIE_AESTHETICIAN_SPAWN_EGG;
    public static Item ROAD_ROLLER;

    public static Item KNIFE;
    public static Item KNIFE_BUNDLE;

    public static Item MATCH;
    public static Item MATCH_BUNDLE;

    public static Item HAIRSPRAY;

    public static Item GASOLINE_CAN;
    public static Item GASOLINE_BUCKET;
    public static Item LUCK_SWORD;
    public static Item LUCK_UPGRADE;
    public static Item EXECUTION_UPGRADE;

    public static Item HARPOON;
    public static Item SCISSORS;
    public static Item SACRIFICIAL_DAGGER;
    public static Item BOWLER_HAT;
    public static Item EXECUTIONER_AXE;
    public static Item BODY_BAG;
    public static Item CREATIVE_BODY_BAG;
    public static Item WOODEN_GLAIVE;
    public static Item STONE_GLAIVE;
    public static Item IRON_GLAIVE;
    public static Item GOLDEN_GLAIVE;
    public static Item DIAMOND_GLAIVE;
    public static Item NETHERITE_GLAIVE;
    public static Item MUSIC_DISC_TORTURE_DANCE;
    public static Item MUSIC_DISC_HALLELUJAH;
    public static Item LUCKY_LIPSTICK;
    public static Item BLANK_MASK;
    public static Item MODIFICATION_MASK;
    public static Item JOTARO_MASK;
    public static Item JOTARO_4_MASK;
    public static Item JOTARO_6_MASK;
    public static Item DIO_MASK;
    public static Item DIO_VAMPIRE_MASK;
    public static Item SPEEDWAGON_MASK;
    public static Item ENYA_MASK;
    public static Item ENYA_OVA_MASK;
    public static Item AVDOL_MASK;
    public static Item DIEGO_MASK;
    public static Item VALENTINE_MASK;
    public static Item JOSUKE_PART_EIGHT_MASK;
    public static Item AYA_MASK;
    public static Item AESTHETICIAN_MASK_1;
    public static Item AESTHETICIAN_MASK_2;
    public static Item AESTHETICIAN_MASK_3;
    public static Item AESTHETICIAN_MASK_4;
    public static Item AESTHETICIAN_MASK_5;
    public static Item AESTHETICIAN_MASK_ZOMBIE;
    public static Item POCOLOCO_MASK;
    public static Item RINGO_MASK;
    public static Item GUCCIO_MASK;
    public static Item HATO_MASK;
    public static Item SHIZUKA_MASK;
    public static Item INTERDIMENSIONAL_KEY;

    public static Item ANUBIS_ITEM;

    public static Item SNUBNOSE_REVOLVER;
    public static Item SNUBNOSE_AMMO;


    public static Item PAINTING_VAN_GOUGH;
    public static Item PAINTING_MONA_LISA;
    public static Item PAINTING_BIRTH_OF_VENUS;

    public static Item FLESH_BUCKET;
    public static Item CHERRIES;

    public static CreativeModeTab FOG_BLOCK_ITEMS;

    public static ArrayList<StandDiscItem> STAND_ARROW_POOL = Lists.newArrayList();
    public static ArrayList<StandDiscItem> STAND_ARROW_SECONDARY_STAND_POOL = Lists.newArrayList();
    public static ArrayList<StandDiscItem> STAND_ARROW_POOL_FOR_MOBS = Lists.newArrayList();
    public static ArrayList<StandDiscItem> STAND_ARROW_POOL_FOR_HUMANOID_MOBS = Lists.newArrayList();
    public static ArrayList<VisageStoreEntry> VISAGE_STORE_ENTRIES = Lists.newArrayList();


    public static ArrayList<StandDiscItem> getPoolForMob(LivingEntity LE){
        if (MainUtil.isHumanoid(LE))
            return STAND_ARROW_POOL_FOR_HUMANOID_MOBS;
        return STAND_ARROW_POOL_FOR_MOBS;
    }

    public static ArrayList<VisageStoreEntry> getVisageStore(){
        if (VISAGE_STORE_ENTRIES == null){
            VISAGE_STORE_ENTRIES = Lists.newArrayList();
        }
        return VISAGE_STORE_ENTRIES;
    }
    public static void initializeVisageStore(){
        addToVisageStore(LUCKY_LIPSTICK,0,
                ClientNetworking.getAppropriateConfig().cinderellaSettings.levelCostLipstick,
                ClientNetworking.getAppropriateConfig().cinderellaSettings.emeraldCostLipstick);
        addToVisageStore(BLANK_MASK,0,
                ClientNetworking.getAppropriateConfig().cinderellaSettings.levelCostGlassVisage,
                ClientNetworking.getAppropriateConfig().cinderellaSettings.emeraldCostGlassVisage);
        addToVisageStore(MODIFICATION_MASK,0,
                ClientNetworking.getAppropriateConfig().cinderellaSettings.levelCostModificationVisage,
                ClientNetworking.getAppropriateConfig().cinderellaSettings.emeraldCostModificationVisage);
        int characterCostExp = ClientNetworking.getAppropriateConfig().cinderellaSettings.levelCostCharacterVisage;
        int characterCostEmerald = ClientNetworking.getAppropriateConfig().cinderellaSettings.emeraldCostCharacterVisage;

        addToVisageStore(DIO_VAMPIRE_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(SPEEDWAGON_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(JOTARO_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(AVDOL_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(DIO_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(ENYA_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(JOTARO_4_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(AYA_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(JOTARO_6_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(GUCCIO_MASK,1, characterCostExp, characterCostEmerald);
        addToVisageStore(DIEGO_MASK,2, characterCostExp, characterCostEmerald);
        addToVisageStore(POCOLOCO_MASK,2, characterCostExp, characterCostEmerald);
        addToVisageStore(RINGO_MASK,2, characterCostExp, characterCostEmerald);
        addToVisageStore(VALENTINE_MASK,2, characterCostExp, characterCostEmerald);
        addToVisageStore(JOSUKE_PART_EIGHT_MASK,2, characterCostExp, characterCostEmerald);
        addToVisageStore(HATO_MASK,2, characterCostExp, characterCostEmerald);
        if (ClientNetworking.getAppropriateConfig().cinderellaSettings.enableJojoveinVisagesInShop){
            addToVisageStore(SHIZUKA_MASK,3, characterCostExp, characterCostEmerald);
        }
        ComposterBlock.COMPOSTABLES.put(LOCACACA_PIT, 0.3f);
        ComposterBlock.COMPOSTABLES.put(NEW_LOCACACA, 0.65f);
        ComposterBlock.COMPOSTABLES.put(LOCACACA, 0.65f);
    }
    public static void addToVisageStore(Item item, int page, int costL, int costE){
        VISAGE_STORE_ENTRIES.add(new VisageStoreEntry(item.getDefaultInstance(), page, costL, costE));
    }
    public static void addToVisageStore(ItemStack item, int page, int costL, int costE){
        VISAGE_STORE_ENTRIES.add(new VisageStoreEntry(item, page, costL, costE));
    }

    /// Registers a stand disc and a max stand disc by the powers and base disc item name.
    /// @param powers The stand powers. Usually from new Powers(null);
    /// @param itemName The stand disc name. An example being "the_world_disc"
    /// @return Returns a list of the items created. First index is the regular stand disc, second being maximum.
    public static <T extends StandPowers> List<Item> registerStand(T powers, String itemName)
    {
        return registerStand(powers, itemName, true);
    }

    /// Registers a stand disc (and a max stand disc when requested) by the powers and base disc item name.
    /// @param powers The stand powers. Usually from new Powers(null);
    /// @param itemName The stand disc name. An example being "the_world_disc"
    /// @return Returns a list of the items created. First index is the regular stand disc, second being maximum.
    public static <T extends StandPowers> List<Item> registerStand(T powers, String itemName, boolean maxDisc)
    {
        Item.Properties properties = new Item.Properties().stacksTo(1);
        List<Item> discs = new ArrayList<>();

        discs.add(
                registerItem(itemName, new StandDiscItem(properties, powers))
        );

        if (maxDisc)
            discs.add(
                    registerItem("max_"+itemName, new MaxStandDiscItem(properties, powers, (StandDiscItem) discs.get(0)))
            );

        return discs;
    }

    /// Registers a stand disc and a max stand disc by the powers and base disc item name.
    /// @param powers The stand powers. Usually from new Powers(null);
    /// @param itemLocation The stand disc name. An example being <code>new ResourceLocation(Roundabout.MOD_ID, "the_world_disc")</code>
    /// @return Returns a list of the items created. First index is the regular stand disc, second being maximum.
    public static <T extends StandPowers> List<Item> registerStand(T powers, ResourceLocation itemLocation)
    {
        return registerStand(powers, itemLocation, true);
    }

    /// Registers a stand disc (and a max stand disc when requested) by the powers and base disc item name.
    /// @param powers The stand powers. Usually from new Powers(null);
    /// @param itemLocation The stand disc location. An example being <code>new ResourceLocation(Roundabout.MOD_ID, "the_world_disc")</code>
    /// @return Returns a list of the items created. First index is the regular stand disc, second being maximum.
    public static <T extends StandPowers> List<Item> registerStand(T powers, ResourceLocation itemLocation, boolean maxDisc)
    {
        Item.Properties properties = new Item.Properties().stacksTo(1);
        List<Item> discs = new ArrayList<>();

        discs.add(
                registerItem(itemLocation, new StandDiscItem(properties, powers))
        );

        if (maxDisc)
        {
            ResourceLocation maxLocation = new ResourceLocation(itemLocation.getNamespace(), "max_" + itemLocation.getPath());
            discs.add(
                    registerItem(maxLocation, new MaxStandDiscItem(properties, powers, (StandDiscItem) discs.get(0)))
            );
        }

        return discs;
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(BuiltInRegistries.ITEM, Roundabout.location(name), item);
    }

    private static Item registerItem(ResourceLocation location, Item item){
        return Registry.register(BuiltInRegistries.ITEM, location, item);
    }
}
