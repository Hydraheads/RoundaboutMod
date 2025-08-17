package net.hydra.jojomod.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.CreativeInventoryListener;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class FogInventoryScreen extends EffectRenderingInventoryScreen<FogInventoryScreen.ItemPickerMenu> {
    private static final ResourceLocation CREATIVE_TABS_LOCATION = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    private static final int NUM_ROWS = 5;
    private static final int NUM_COLS = 9;
    private static final int TAB_WIDTH = 26;
    private static final int TAB_HEIGHT = 32;
    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    static final SimpleContainer CONTAINER = new SimpleContainer(45);
    private static final Component TRASH_SLOT_TOOLTIP = Component.translatable("inventory.binSlot");
    private static final int TEXT_COLOR = 16777215;
    private static CreativeModeTab selectedTab = ModItems.FOG_BLOCK_ITEMS;
    private float scrollOffs;
    private boolean scrolling;
    private EditBox searchBox;
    @Nullable
    private List<Slot> originalSlots;
    @Nullable
    private Slot destroyItemSlot;
    private CreativeInventoryListener listener;
    private boolean ignoreTextInput;
    private boolean hasClickedOutside;
    private final Set<TagKey<Item>> visibleTags = new HashSet<>();
    private final boolean displayOperatorCreativeTab;

    public FogInventoryScreen(Player $$0, FeatureFlagSet $$1, boolean $$2) {
        super(new FogInventoryScreen.ItemPickerMenu($$0), $$0.getInventory(), CommonComponents.EMPTY);
        $$0.containerMenu = this.menu;
        this.imageHeight = 136;
        this.imageWidth = 195;
        this.displayOperatorCreativeTab = $$2;
        CreativeModeTabs.tryRebuildTabContents($$1, this.hasPermissions($$0), $$0.level().registryAccess());
    }

    private boolean hasPermissions(Player $$0) {
        return $$0.canUseGameMasterBlocks() && this.displayOperatorCreativeTab;
    }

    private void tryRefreshInvalidatedTabs(FeatureFlagSet $$0, boolean $$1, HolderLookup.Provider $$2) {
        if (CreativeModeTabs.tryRebuildTabContents($$0, $$1, $$2)) {
            for (CreativeModeTab $$3 : CreativeModeTabs.allTabs()) {
                Collection<ItemStack> $$4 = $$3.getDisplayItems();
                if ($$3 == selectedTab) {
                    if ($$3.getType() == CreativeModeTab.Type.CATEGORY && $$4.isEmpty()) {
                        this.selectTab(CreativeModeTabs.getDefaultTab());
                    } else {
                        this.refreshCurrentTabContents($$4);
                    }
                }
            }
        }
    }

    private void refreshCurrentTabContents(Collection<ItemStack> $$0) {
        int $$1 = this.menu.getRowIndexForScroll(this.scrollOffs);
        this.menu.items.clear();
        if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
            this.refreshSearchResults();
        } else {
            this.menu.items.addAll($$0);
        }

        this.scrollOffs = this.menu.getScrollForRowIndex($$1);
        this.menu.scrollTo(this.scrollOffs);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.minecraft != null) {
            if (this.minecraft.player != null) {
                this.tryRefreshInvalidatedTabs(
                        this.minecraft.player.connection.enabledFeatures(),
                        this.hasPermissions(this.minecraft.player),
                        this.minecraft.player.level().registryAccess()
                );
            }

                this.searchBox.tick();
        }
    }

    @Override
    protected void slotClicked(@Nullable Slot $$0, int $$1, int $$2, ClickType $$3) {
        if ($$3 == ClickType.SWAP || $$3 == ClickType.QUICK_MOVE) {
            return;
        }
        if (this.isCreativeSlot($$0)) {
            this.searchBox.moveCursorToEnd();
            this.searchBox.setHighlightPos(0);
        }

        boolean flag = $$3 == ClickType.QUICK_MOVE;
        $$3 = $$1 == -999 && $$3 == ClickType.PICKUP ? ClickType.THROW : $$3;
        if ($$0 == null && selectedTab.getType() != CreativeModeTab.Type.INVENTORY && $$3 != ClickType.QUICK_CRAFT) {
            if (!this.menu.getCarried().isEmpty() && this.hasClickedOutside) {
                if ($$2 == 0) {
                    this.minecraft.player.drop(this.menu.getCarried(), true);

                    this.menu.setCarried(ItemStack.EMPTY);
                }

                if ($$2 == 1) {
                    ItemStack $$18 = this.menu.getCarried().split(1);
                    this.minecraft.player.drop($$18, true);
                    C2SPacketUtil.inventoryToServerPacket(-1,
                            $$18,PacketDataIndex.ADD_FOG_ITEM);
                }
            }
        } else {
            if ($$0 != null && !$$0.mayPickup(this.minecraft.player)) {
                return;
            }

            if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
                if ($$0 == this.destroyItemSlot) {
                    this.menu.setCarried(ItemStack.EMPTY);
                } else if ($$3 == ClickType.THROW && $$0 != null && $$0.hasItem()) {
                    ItemStack $$6 = $$0.remove($$2 == 0 ? 1 : $$0.getItem().getMaxStackSize());
                    ItemStack $$7 = $$0.getItem();
                    this.minecraft.player.drop($$6, true);
                    C2SPacketUtil.inventoryToServerPacket(-1,
                            $$6,PacketDataIndex.ADD_FOG_ITEM);
                    //this.minecraft.gameMode.handleCreativeModeItemAdd($$7, ((CreativeModeInventoryScreen.SlotWrapper)$$0).target.index);
                } else if ($$3 == ClickType.THROW && !this.menu.getCarried().isEmpty()) {
                    this.minecraft.player.drop(this.menu.getCarried(), true);
                    C2SPacketUtil.inventoryToServerPacket(-1,
                            this.menu.getCarried(),PacketDataIndex.ADD_FOG_ITEM);
                    this.menu.setCarried(ItemStack.EMPTY);
                } else {
                    this.minecraft
                            .player
                            .inventoryMenu
                            .clicked($$0 == null ? $$1 : ((FogInventoryScreen.SlotWrapper)$$0).target.index, $$2, $$3, this.minecraft.player);
                    this.minecraft.player.inventoryMenu.broadcastChanges();
                }
            } else if ($$3 != ClickType.QUICK_CRAFT && $$0.container == CONTAINER) {
                ItemStack $$8 = this.menu.getCarried();
                ItemStack $$9 = $$0.getItem();
                if ($$3 == ClickType.SWAP) {
                    if (!$$9.isEmpty()) {
                        this.minecraft.player.getInventory().setItem($$2, $$9.copyWithCount($$9.getMaxStackSize()));
                        this.minecraft.player.inventoryMenu.broadcastChanges();
                    }

                    return;
                }

                if ($$3 == ClickType.CLONE) {
                    if (this.menu.getCarried().isEmpty() && $$0.hasItem()) {
                        ItemStack $$10 = $$0.getItem();
                        if ($$10.getItem() instanceof BlockItem BI && BI.getBlock() instanceof FogBlock) {
                            this.menu.setCarried($$10.copyWithCount($$10.getMaxStackSize()));
                            if (this.minecraft.player != null) {
                                this.minecraft.player.inventoryMenu.broadcastChanges();
                            }
                        }
                    }

                    return;
                }

                if ($$3 == ClickType.THROW) {
                    if (!$$9.isEmpty()) {
                        ItemStack $$11 = $$9.copyWithCount($$2 == 0 ? 1 : $$9.getMaxStackSize());
                        this.minecraft.player.drop($$11, true);
                        C2SPacketUtil.inventoryToServerPacket(-1,
                                $$11,PacketDataIndex.ADD_FOG_ITEM);
                    }

                    return;
                }

                if (!$$8.isEmpty() && !$$9.isEmpty() && ItemStack.isSameItemSameTags($$8, $$9)) {
                    if ($$2 == 0) {
                        if (flag) {
                            $$8.setCount($$8.getMaxStackSize());
                        } else if ($$8.getCount() < $$8.getMaxStackSize()) {
                            $$8.grow(1);
                        }
                    } else {
                        $$8.shrink(1);
                    }
                } else if (!$$9.isEmpty() && $$8.isEmpty()) {
                    int $$12 = flag ? $$9.getMaxStackSize() : $$9.getCount();
                    this.menu.setCarried($$9.copyWithCount($$12));
                } else if ($$2 == 0) {
                    this.minecraft.player.drop($$8, true);
                    C2SPacketUtil.inventoryToServerPacket(-1,
                            $$8,PacketDataIndex.ADD_FOG_ITEM);
                    this.menu.setCarried(ItemStack.EMPTY);
                } else if (!this.menu.getCarried().isEmpty()) {
                    C2SPacketUtil.inventoryToServerPacket(-1,
                            this.menu.getCarried().copyWithCount(1),PacketDataIndex.ADD_FOG_ITEM);
                    this.menu.getCarried().shrink(1);
                }
            } else if (this.menu != null) {
                ItemStack $$13 = $$0 == null ? ItemStack.EMPTY : this.menu.getSlot($$0.index).getItem();
                this.menu.clicked($$0 == null ? $$1 : $$0.index, $$2, $$3, this.minecraft.player);
                if (AbstractContainerMenu.getQuickcraftHeader($$2) == 2) {
                    for (int $$14 = 0; $$14 < 9; $$14++) {
                        C2SPacketUtil.inventoryToServerPacket(36 + $$14,
                                this.menu.getSlot(45 + $$14).getItem(),PacketDataIndex.ADD_FOG_ITEM);
                    }
                } else if ($$0 != null) {
                    ItemStack $$15 = this.menu.getSlot($$0.index).getItem();

                    C2SPacketUtil.inventoryToServerPacket($$0.index - this.menu.slots.size() + 9 + 36,
                            $$15,PacketDataIndex.ADD_FOG_ITEM);
                    int $$16 = 45 + $$2;
                    if ($$3 == ClickType.SWAP) {
                        C2SPacketUtil.inventoryToServerPacket($$16 - this.menu.slots.size() + 9 + 36,
                                $$13,PacketDataIndex.ADD_FOG_ITEM);
                    } else if ($$3 == ClickType.THROW && !$$13.isEmpty()) {
                        ItemStack $$17 = $$13.copyWithCount($$2 == 0 ? 1 : $$13.getMaxStackSize());
                        this.minecraft.player.drop($$17, true);
                        C2SPacketUtil.inventoryToServerPacket(-1,
                                $$17,PacketDataIndex.ADD_FOG_ITEM);
                    }

                    this.minecraft.player.inventoryMenu.broadcastChanges();
                }
            }
        }
    }


    private boolean isCreativeSlot(@Nullable Slot $$0) {
        return $$0 != null && $$0.container == CONTAINER;
    }

    @Override
    protected void init() {
            super.init();
            this.searchBox = new EditBox(this.font, this.leftPos + 82, this.topPos + 6, 80, 9, Component.translatable("itemGroup.search"));
            this.searchBox.setMaxLength(50);
            this.searchBox.setBordered(false);
            this.searchBox.setVisible(false);
            this.searchBox.setTextColor(16777215);
            this.addWidget(this.searchBox);
            CreativeModeTab $$0 = selectedTab;
            selectedTab = ModItems.FOG_BLOCK_ITEMS;
            this.selectTab($$0);
            this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
            this.listener = new CreativeInventoryListener(this.minecraft);
            this.minecraft.player.inventoryMenu.addSlotListener(this.listener);
    }

    @Override
    public void resize(Minecraft $$0, int $$1, int $$2) {
        int $$3 = this.menu.getRowIndexForScroll(this.scrollOffs);
        String $$4 = this.searchBox.getValue();
        this.init($$0, $$1, $$2);
        this.searchBox.setValue($$4);
        if (!this.searchBox.getValue().isEmpty()) {
            this.refreshSearchResults();
        }

        this.scrollOffs = this.menu.getScrollForRowIndex($$3);
        this.menu.scrollTo(this.scrollOffs);
    }

    @Override
    public void removed() {
        super.removed();
        if (this.minecraft.player != null && this.minecraft.player.getInventory() != null) {
            this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
        }
    }

    @Override
    public boolean charTyped(char $$0, int $$1) {
        if (this.ignoreTextInput) {
            return false;
        } else if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
            return false;
        } else {
            String $$2 = this.searchBox.getValue();
            if (this.searchBox.charTyped($$0, $$1)) {
                if (!Objects.equals($$2, this.searchBox.getValue())) {
                    this.refreshSearchResults();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        this.ignoreTextInput = false;
        if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
            if (this.minecraft.options.keyChat.matches($$0, $$1)) {
                this.ignoreTextInput = true;
                this.selectTab(CreativeModeTabs.searchTab());
                return true;
            } else {
                return super.keyPressed($$0, $$1, $$2);
            }
        } else {
            boolean $$3 = !this.isCreativeSlot(this.hoveredSlot) || this.hoveredSlot.hasItem();
            boolean $$4 = InputConstants.getKey($$0, $$1).getNumericKeyValue().isPresent();
            if ($$3 && $$4 && this.checkHotbarKeyPressed($$0, $$1)) {
                this.ignoreTextInput = true;
                return true;
            } else {
                String $$5 = this.searchBox.getValue();
                if (this.searchBox.keyPressed($$0, $$1, $$2)) {
                    if (!Objects.equals($$5, this.searchBox.getValue())) {
                        this.refreshSearchResults();
                    }

                    return true;
                } else {
                    return this.searchBox.isFocused() && this.searchBox.isVisible() && $$0 != 256 ? true : super.keyPressed($$0, $$1, $$2);
                }
            }
        }
    }

    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        this.ignoreTextInput = false;
        return super.keyReleased($$0, $$1, $$2);
    }

    private void refreshSearchResults() {
        this.menu.items.clear();
        this.visibleTags.clear();
        String $$0 = this.searchBox.getValue();
        if ($$0.isEmpty()) {
            this.menu.items.addAll(selectedTab.getDisplayItems());
        } else {
            SearchTree<ItemStack> $$1;
            if ($$0.startsWith("#")) {
                $$0 = $$0.substring(1);
                $$1 = this.minecraft.getSearchTree(SearchRegistry.CREATIVE_TAGS);
                this.updateVisibleTags($$0);
            } else {
                $$1 = this.minecraft.getSearchTree(SearchRegistry.CREATIVE_NAMES);
            }

            this.menu.items.addAll($$1.search($$0.toLowerCase(Locale.ROOT)));
        }

        this.scrollOffs = 0.0F;
        this.menu.scrollTo(0.0F);
    }

    private void updateVisibleTags(String $$0) {
        int $$1 = $$0.indexOf(58);
        Predicate<ResourceLocation> $$2;
        if ($$1 == -1) {
            $$2 = $$1x -> $$1x.getPath().contains($$0);
        } else {
            String $$3 = $$0.substring(0, $$1).trim();
            String $$4 = $$0.substring($$1 + 1).trim();
            $$2 = $$2x -> $$2x.getNamespace().contains($$3) && $$2x.getPath().contains($$4);
        }

        BuiltInRegistries.ITEM.getTagNames().filter($$1x -> $$2.test($$1x.location())).forEach(this.visibleTags::add);
    }

    @Override
    protected void renderLabels(GuiGraphics $$0, int $$1, int $$2) {
        if (selectedTab.showTitle()) {
            $$0.drawString(this.font, selectedTab.getDisplayName(), 8, 6, 4210752, false);
        }
    }


    private boolean isHovering2(Slot $$0, double $$1, double $$2) {
        return this.isHovering($$0.x, $$0.y, 16, 16, $$1, $$2);
    }
    @Nullable
    private Slot findSlot2(double $$0, double $$1) {
        for (int $$2 = 0; $$2 < this.menu.slots.size(); $$2++) {
            Slot $$3 = this.menu.slots.get($$2);
            if (this.isHovering2($$3, $$0, $$1) && $$3.isActive()) {
                return $$3;
            }
        }

        return null;
    }

    @Override
    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        if ($$2 == 0) {
            double $$3 = $$0 - (double)this.leftPos;
            double $$4 = $$1 - (double)this.topPos;


            if (selectedTab.getType() != CreativeModeTab.Type.INVENTORY && this.insideScrollbar($$0, $$1)) {
                this.scrolling = this.canScroll();
                return true;
            }
        }
        boolean $$3 = this.minecraft.options.keyPickItem.matchesMouse($$2) && !this.minecraft.gameMode.hasInfiniteItems();
        if ($$3) {
            Slot $$4 = this.findSlot2($$0, $$1);
            int $$6 = this.leftPos;
            int $$7 = this.topPos;
            boolean $$8 = this.hasClickedOutside($$0, $$1, $$6, $$7, $$2);
            int $$9 = -1;
            if ($$4 != null) {
                $$9 = $$4.index;
            }

            if ($$8) {
                $$9 = -999;
            }
            this.slotClicked($$4, $$9, $$2, ClickType.CLONE);
        }

        return super.mouseClicked($$0, $$1, $$2);
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if ($$2 == 0) {
            double $$3 = $$0 - (double)this.leftPos;
            double $$4 = $$1 - (double)this.topPos;
            this.scrolling = false;

        }

        return super.mouseReleased($$0, $$1, $$2);
    }

    private boolean canScroll() {
        return selectedTab.canScroll() && this.menu.canScroll();
    }

    private void selectTab(CreativeModeTab $$0) {
        CreativeModeTab $$1 = selectedTab;
        selectedTab = $$0;
        this.quickCraftSlots.clear();
        this.menu.items.clear();
        this.clearDraggingState();
        if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR) {
            HotbarManager $$2 = this.minecraft.getHotbarManager();

            for (int $$3 = 0; $$3 < 9; $$3++) {
                Hotbar $$4 = $$2.get($$3);
                if ($$4.isEmpty()) {
                    for (int $$5 = 0; $$5 < 9; $$5++) {
                        if ($$5 == $$3) {
                            ItemStack $$6 = new ItemStack(Items.PAPER);
                            $$6.getOrCreateTagElement("CustomCreativeLock");
                            Component $$7 = this.minecraft.options.keyHotbarSlots[$$3].getTranslatedKeyMessage();
                            Component $$8 = this.minecraft.options.keySaveHotbarActivator.getTranslatedKeyMessage();
                            $$6.setHoverName(Component.translatable("inventory.hotbarInfo", $$8, $$7));
                            this.menu.items.add($$6);
                        } else {
                            this.menu.items.add(ItemStack.EMPTY);
                        }
                    }
                } else {
                    this.menu.items.addAll($$4);
                }
            }
        } else if (selectedTab.getType() == CreativeModeTab.Type.CATEGORY) {
            this.menu.items.addAll(selectedTab.getDisplayItems());
        }

        if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
            AbstractContainerMenu $$9 = this.minecraft.player.inventoryMenu;
            if (this.originalSlots == null) {
                this.originalSlots = ImmutableList.copyOf(this.menu.slots);
            }

            this.menu.slots.clear();

            for (int $$10 = 0; $$10 < $$9.slots.size(); $$10++) {
                int $$14;
                int $$15;
                if ($$10 >= 5 && $$10 < 9) {
                    int $$11 = $$10 - 5;
                    int $$12 = $$11 / 2;
                    int $$13 = $$11 % 2;
                    $$14 = 54 + $$12 * 54;
                    $$15 = 6 + $$13 * 27;
                } else if ($$10 >= 0 && $$10 < 5) {
                    $$14 = -2000;
                    $$15 = -2000;
                } else if ($$10 == 45) {
                    $$14 = 35;
                    $$15 = 20;
                } else {
                    int $$20 = $$10 - 9;
                    int $$21 = $$20 % 9;
                    int $$22 = $$20 / 9;
                    $$14 = 9 + $$21 * 18;
                    if ($$10 >= 36) {
                        $$15 = 112;
                    } else {
                        $$15 = 54 + $$22 * 18;
                    }
                }

                Slot $$26 = new FogInventoryScreen.SlotWrapper($$9.slots.get($$10), $$10, $$14, $$15);
                this.menu.slots.add($$26);
            }

            this.destroyItemSlot = new Slot(CONTAINER, 0, 173, 112);
            this.menu.slots.add(this.destroyItemSlot);
        } else if ($$1.getType() == CreativeModeTab.Type.INVENTORY) {
            this.menu.slots.clear();
            this.menu.slots.addAll(this.originalSlots);
            this.originalSlots = null;
        }

        if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
            this.searchBox.setVisible(true);
            this.searchBox.setCanLoseFocus(false);
            this.searchBox.setFocused(true);
            if ($$1 != $$0) {
                this.searchBox.setValue("");
            }

            this.refreshSearchResults();
        } else {
            this.searchBox.setVisible(false);
            this.searchBox.setCanLoseFocus(true);
            this.searchBox.setFocused(false);
            this.searchBox.setValue("");
        }

        this.scrollOffs = 0.0F;
        this.menu.scrollTo(0.0F);
    }

    @Override
    public boolean mouseScrolled(double $$0, double $$1, double $$2) {
        if (!this.canScroll()) {
            return false;
        } else {
            this.scrollOffs = this.menu.subtractInputFromScroll(this.scrollOffs, $$2);
            this.menu.scrollTo(this.scrollOffs);
            return true;
        }
    }

    @Override
    protected boolean hasClickedOutside(double $$0, double $$1, int $$2, int $$3, int $$4) {
        boolean $$5 = $$0 < (double)$$2 || $$1 < (double)$$3 || $$0 >= (double)($$2 + this.imageWidth) || $$1 >= (double)($$3 + this.imageHeight);
        this.hasClickedOutside = $$5 && !this.checkTabClicked(selectedTab, $$0, $$1);
        return this.hasClickedOutside;
    }

    protected boolean insideScrollbar(double $$0, double $$1) {
        int $$2 = this.leftPos;
        int $$3 = this.topPos;
        int $$4 = $$2 + 175;
        int $$5 = $$3 + 18;
        int $$6 = $$4 + 14;
        int $$7 = $$5 + 112;
        return $$0 >= (double)$$4 && $$1 >= (double)$$5 && $$0 < (double)$$6 && $$1 < (double)$$7;
    }

    @Override
    public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
        if (this.scrolling) {
            int $$5 = this.topPos + 18;
            int $$6 = $$5 + 112;
            this.scrollOffs = ((float)$$1 - (float)$$5 - 7.5F) / ((float)($$6 - $$5) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.menu.scrollTo(this.scrollOffs);
            return true;
        } else {
            return super.mouseDragged($$0, $$1, $$2, $$3, $$4);
        }
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        this.renderBackground($$0);
        super.render($$0, $$1, $$2, $$3);


        if (this.destroyItemSlot != null
                && selectedTab.getType() == CreativeModeTab.Type.INVENTORY
                && this.isHovering(this.destroyItemSlot.x, this.destroyItemSlot.y, 16, 16, (double)$$1, (double)$$2)) {
            $$0.renderTooltip(this.font, TRASH_SLOT_TOOLTIP, $$1, $$2);
        }

        this.renderTooltip($$0, $$1, $$2);
    }

    @Override
    public List<Component> getTooltipFromContainerItem(ItemStack $$0) {
        boolean $$1 = this.hoveredSlot != null && this.hoveredSlot instanceof FogInventoryScreen.CustomCreativeSlot;
        boolean $$2 = selectedTab.getType() == CreativeModeTab.Type.CATEGORY;
        boolean $$3 = selectedTab.getType() == CreativeModeTab.Type.SEARCH;
        TooltipFlag.Default $$4 = this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
        TooltipFlag $$5 = $$1 ? $$4.asCreative() : $$4;
        List<Component> $$6 = $$0.getTooltipLines(this.minecraft.player, $$5);
        if ($$2 && $$1) {
            return $$6;
        } else {
            List<Component> $$7 = Lists.newArrayList($$6);
            if ($$3 && $$1) {
                this.visibleTags.forEach($$2x -> {
                    if ($$0.is((TagKey<Item>)$$2x)) {
                        $$7.add(1, Component.literal("#" + $$2x.location()).withStyle(ChatFormatting.DARK_PURPLE));
                    }
                });
            }

            int $$8 = 1;

            for (CreativeModeTab $$9 : CreativeModeTabs.tabs()) {
                if ($$9.getType() != CreativeModeTab.Type.SEARCH && $$9.contains($$0)) {
                    $$7.add($$8++, $$9.getDisplayName().copy().withStyle(ChatFormatting.BLUE));
                }
            }

            return $$7;
        }
    }

    @Override
    protected void renderBg(GuiGraphics drawContext, float partialTick, int mouseX, int mouseY) {

        drawContext.blit(
                new ResourceLocation("textures/gui/container/creative_inventory/tab_" + selectedTab.getBackgroundSuffix()),
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );
        this.searchBox.render(drawContext, mouseX, mouseY, partialTick);
        int $$5 = this.leftPos + 175;
        int $$6 = this.topPos + 18;
        int $$7 = $$6 + 112;
        if (selectedTab.canScroll()) {
            drawContext.blit(CREATIVE_TABS_LOCATION, $$5, $$6 + (int)((float)($$7 - $$6 - 17) * this.scrollOffs), 232 + (this.canScroll() ? 0 : 12), 0, 12, 15);
        }

    }

    private int getTabX(CreativeModeTab $$0) {
        int $$1 = $$0.column();
        int $$2 = 27;
        int $$3 = 27 * $$1;
        if ($$0.isAlignedRight()) {
            $$3 = this.imageWidth - 27 * (7 - $$1) + 1;
        }

        return $$3;
    }

    private int getTabY(CreativeModeTab $$0) {
        int $$1 = 0;
        if ($$0.row() == CreativeModeTab.Row.TOP) {
            $$1 -= 32;
        } else {
            $$1 += this.imageHeight;
        }

        return $$1;
    }

    protected boolean checkTabClicked(CreativeModeTab $$0, double $$1, double $$2) {
        int $$3 = this.getTabX($$0);
        int $$4 = this.getTabY($$0);
        return $$1 >= (double)$$3 && $$1 <= (double)($$3 + 26) && $$2 >= (double)$$4 && $$2 <= (double)($$4 + 32);
    }

    protected boolean checkTabHovering(GuiGraphics $$0, CreativeModeTab $$1, int $$2, int $$3) {
        int $$4 = this.getTabX($$1);
        int $$5 = this.getTabY($$1);
        if (this.isHovering($$4 + 3, $$5 + 3, 21, 27, (double)$$2, (double)$$3)) {
            $$0.renderTooltip(this.font, $$1.getDisplayName(), $$2, $$3);
            return true;
        } else {
            return false;
        }
    }

    protected void renderTabButton(GuiGraphics $$0, CreativeModeTab $$1) {
        boolean $$2 = $$1 == selectedTab;
        boolean $$3 = $$1.row() == CreativeModeTab.Row.TOP;
        int $$4 = $$1.column();
        int $$5 = $$4 * 26;
        int $$6 = 0;
        int $$7 = this.leftPos + this.getTabX($$1);
        int $$8 = this.topPos;
        int $$9 = 32;
        if ($$2) {
            $$6 += 32;
        }

        if ($$3) {
            $$8 -= 28;
        } else {
            $$6 += 64;
            $$8 += this.imageHeight - 4;
        }

        $$0.blit(CREATIVE_TABS_LOCATION, $$7, $$8, $$5, $$6, 26, 32);
        $$0.pose().pushPose();
        $$0.pose().translate(0.0F, 0.0F, 100.0F);
        $$7 += 5;
        $$8 += 8 + ($$3 ? 1 : -1);
        ItemStack $$10 = $$1.getIconItem();
        $$0.renderItem($$10, $$7, $$8);
        $$0.renderItemDecorations(this.font, $$10, $$7, $$8);
        $$0.pose().popPose();
    }

    public boolean isInventoryOpen() {
        return selectedTab.getType() == CreativeModeTab.Type.INVENTORY;
    }

    public static void handleHotbarLoadOrSave(Minecraft $$0, int $$1, boolean $$2, boolean $$3) {
        LocalPlayer $$4 = $$0.player;
        HotbarManager $$5 = $$0.getHotbarManager();
        Hotbar $$6 = $$5.get($$1);
        if ($$2) {
            for (int $$7 = 0; $$7 < Inventory.getSelectionSize(); $$7++) {
                ItemStack $$8 = $$6.get($$7);
                ItemStack $$9 = $$8.isItemEnabled($$4.level().enabledFeatures()) ? $$8.copy() : ItemStack.EMPTY;
                $$4.getInventory().setItem($$7, $$9);
            }

            $$4.inventoryMenu.broadcastChanges();
        } else if ($$3) {
            for (int $$10 = 0; $$10 < Inventory.getSelectionSize(); $$10++) {
                $$6.set($$10, $$4.getInventory().getItem($$10).copy());
            }

            Component $$11 = $$0.options.keyHotbarSlots[$$1].getTranslatedKeyMessage();
            Component $$12 = $$0.options.keyLoadHotbarActivator.getTranslatedKeyMessage();
            Component $$13 = Component.translatable("inventory.hotbarSaved", $$12, $$11);
            $$0.gui.setOverlayMessage($$13, false);
            $$0.getNarrator().sayNow($$13);
            $$5.save();
        }
    }

    static class CustomCreativeSlot extends Slot {
        public CustomCreativeSlot(Container $$0, int $$1, int $$2, int $$3) {
            super($$0, $$1, $$2, $$3);
        }

        @Override
        public boolean mayPickup(Player $$0) {
            ItemStack $$1 = this.getItem();
            return super.mayPickup($$0) && !$$1.isEmpty()
                    ? $$1.isItemEnabled($$0.level().enabledFeatures()) && $$1.getTagElement("CustomCreativeLock") == null
                    : $$1.isEmpty();
        }
    }

    public static class ItemPickerMenu extends AbstractContainerMenu {
        public final NonNullList<ItemStack> items = NonNullList.create();
        private final AbstractContainerMenu inventoryMenu;

        public ItemPickerMenu(Player $$0) {
            super(null, 0);
            this.inventoryMenu = $$0.inventoryMenu;
            Inventory $$1 = $$0.getInventory();

            for (int $$2 = 0; $$2 < 5; $$2++) {
                for (int $$3 = 0; $$3 < 9; $$3++) {
                    this.addSlot(
                            new FogInventoryScreen.CustomCreativeSlot(FogInventoryScreen.CONTAINER, $$2 * 9 + $$3, 9 + $$3 * 18, 18 + $$2 * 18)
                    );
                }
            }

            for (int $$4 = 0; $$4 < 9; $$4++) {
                this.addSlot(new Slot($$1, $$4, 9 + $$4 * 18, 112));
            }

            this.scrollTo(0.0F);
        }

        @Override
        public void clicked(int $$0, int $$1, ClickType $$2, Player $$3) {
            if ($$2 == ClickType.CLONE && !$$3.getAbilities().instabuild && this.getCarried().isEmpty() && $$0 >= 0) {
                Slot $$32 = this.slots.get($$0);
                if ($$32.hasItem()) {
                    ItemStack $$33 = $$32.getItem();
                    if ($$33.getItem() instanceof BlockItem BI && BI.getBlock() instanceof FogBlock) {
                        this.setCarried($$33.copyWithCount($$33.getMaxStackSize()));
                    }
                }
                this.inventoryMenu.broadcastChanges();
            } else {

                super.clicked($$0,$$1,$$2,$$3);
            }
        }


        @Override
        public boolean stillValid(Player $$0) {
            return true;
        }

        protected int calculateRowCount() {
            return Mth.positiveCeilDiv(this.items.size(), 9) - 5;
        }

        protected int getRowIndexForScroll(float $$0) {
            return Math.max((int)((double)($$0 * (float)this.calculateRowCount()) + 0.5), 0);
        }

        protected float getScrollForRowIndex(int $$0) {
            return Mth.clamp((float)$$0 / (float)this.calculateRowCount(), 0.0F, 1.0F);
        }

        protected float subtractInputFromScroll(float $$0, double $$1) {
            return Mth.clamp($$0 - (float)($$1 / (double)this.calculateRowCount()), 0.0F, 1.0F);
        }

        public void scrollTo(float $$0) {
            int $$1 = this.getRowIndexForScroll($$0);

            for (int $$2 = 0; $$2 < 5; $$2++) {
                for (int $$3 = 0; $$3 < 9; $$3++) {
                    int $$4 = $$3 + ($$2 + $$1) * 9;
                    if ($$4 >= 0 && $$4 < this.items.size()) {
                        FogInventoryScreen.CONTAINER.setItem($$3 + $$2 * 9, this.items.get($$4));
                    } else {
                        FogInventoryScreen.CONTAINER.setItem($$3 + $$2 * 9, ItemStack.EMPTY);
                    }
                }
            }
        }

        public boolean canScroll() {
            return this.items.size() > 45;
        }

        @Override
        public ItemStack quickMoveStack(Player $$0, int $$1) {
            if ($$1 >= this.slots.size() - 9 && $$1 < this.slots.size()) {
                Slot $$2 = this.slots.get($$1);
                if ($$2 != null && $$2.hasItem()) {
                    $$2.setByPlayer(ItemStack.EMPTY);
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public boolean canTakeItemForPickAll(ItemStack $$0, Slot $$1) {
            return $$1.container != FogInventoryScreen.CONTAINER;
        }

        @Override
        public boolean canDragTo(Slot $$0) {
            return $$0.container != FogInventoryScreen.CONTAINER;
        }

        @Override
        public ItemStack getCarried() {
            return this.inventoryMenu.getCarried();
        }

        @Override
        public void setCarried(ItemStack $$0) {
            this.inventoryMenu.setCarried($$0);
            C2SPacketUtil.itemContextToServerPacket(PacketDataIndex.FOG_CHECK,$$0);

        }

    }

    static class SlotWrapper extends Slot {
        final Slot target;

        public SlotWrapper(Slot $$0, int $$1, int $$2, int $$3) {
            super($$0.container, $$1, $$2, $$3);
            this.target = $$0;
        }

        @Override
        public void onTake(Player $$0, ItemStack $$1) {
            this.target.onTake($$0, $$1);
        }

        @Override
        public boolean mayPlace(ItemStack $$0) {
            return this.target.mayPlace($$0);
        }

        @Override
        public ItemStack getItem() {
            return this.target.getItem();
        }

        @Override
        public boolean hasItem() {
            return this.target.hasItem();
        }

        @Override
        public void setByPlayer(ItemStack $$0) {
            this.target.setByPlayer($$0);
        }

        @Override
        public void set(ItemStack $$0) {
            this.target.set($$0);
        }

        @Override
        public void setChanged() {
            this.target.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return this.target.getMaxStackSize();
        }

        @Override
        public int getMaxStackSize(ItemStack $$0) {
            return this.target.getMaxStackSize($$0);
        }

        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return this.target.getNoItemIcon();
        }

        @Override
        public ItemStack remove(int $$0) {
            return this.target.remove($$0);
        }

        @Override
        public boolean isActive() {
            return this.target.isActive();
        }

        @Override
        public boolean mayPickup(Player $$0) {
            return this.target.mayPickup($$0);
        }
    }
}
