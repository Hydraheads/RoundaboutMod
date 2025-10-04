package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Roundabout.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> EXAMPLE_TAB_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends ItemLike>> DISC_TAB_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends ItemLike>> BUILDING_TAB_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends ItemLike>> WIP_TAB_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends ItemLike>> FOG_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> JOJO_GROUP = TABS.register("jojo",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.jojo"))
                    .icon(ForgeItems.STAND_ARROW.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            EXAMPLE_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
                    .build()
    );
    public static final RegistryObject<CreativeModeTab> DISC_GROUP = TABS.register("jojo_discs",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.jojo_discs"))
                    .icon(ForgeItems.STAND_DISC_STAR_PLATINUM.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            DISC_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
                    .build()
    );
    public static final RegistryObject<CreativeModeTab> JOJO_BUILDING_GROUP = TABS.register("jojo_building_blocks",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.jojo_building_blocks"))
                    .icon(ForgeItems.WOODEN_MANOR_TABLE_ITEM.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            BUILDING_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
                    .build()
    );
    public static final RegistryObject<CreativeModeTab> WIP_GROUP = TABS.register("jojo_wip_features",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.jojo_wip_features")).hideTitle()
                    .icon(ForgeItems.LIGHT_BULB.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            WIP_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
                    .build()
    );
    public static final RegistryObject<CreativeModeTab> FOG_GROUP = TABS.register("justice_fog_items",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.justice_fog_items")).hideTitle()
                    .icon(ForgeItems.FOG_DIRT.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            FOG_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
                    .build()
    );

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        EXAMPLE_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> RegistryObject<T> addToDiscTab(RegistryObject<T> itemLike) {
        DISC_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> RegistryObject<T> addToBuildingTab(RegistryObject<T> itemLike) {
        BUILDING_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> RegistryObject<T> addToWIPTab(RegistryObject<T> itemLike) {
        WIP_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> RegistryObject<T> addToFogTab(RegistryObject<T> itemLike) {
        FOG_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.getEntries().putAfter(Items.CUT_COPPER_SLAB.getDefaultInstance(), ForgeItems.METEOR_BLOCK_ITEM.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if(event.getTab() == JOJO_GROUP.get()) {
            //event.accept(Items.CROSSBOW);
        }
    }
}
