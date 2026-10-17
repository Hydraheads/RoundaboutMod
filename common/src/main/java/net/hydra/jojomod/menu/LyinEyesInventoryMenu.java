package net.hydra.jojomod.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LyinEyesInventoryMenu extends AbstractContainerMenu {
    private static final int TARGET_SLOTS = 36; // 4 rows of 9
    private final Player target;
    private final Container targetContainer;

    //viewer inventory is still required, even if it's never used, because abstract container menu requires it. bruh.
    public LyinEyesInventoryMenu(int containerId, Inventory viewerInventory) {
        this(containerId, viewerInventory, null);
    }

    public LyinEyesInventoryMenu(int containerId, Inventory viewerInventory, Player target) {
        super(ModMenus.LYIN_EYES_INVENTORY, containerId);
        this.target = target;
        this.targetContainer = new SimpleContainer(TARGET_SLOTS);

        refreshTargetContainer();

        // Rows 0-2: Target's Main Inventory (slots 9 - 35)
        // Row 3: Target's Hotbar (slots 0 - 8)
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = col + row * 9;
                if (row != 3) {
                    this.addSlot(new Slot(targetContainer, slotIndex, 8 + col * 18, 39 + row * 18) {
                        @Override
                        public boolean mayPickup(Player player) {
                            return false; // read-only
                        }

                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            return false; // read-only
                        }
                    });
                }
                else {
                    this.addSlot(new Slot(targetContainer, slotIndex, 8 + col * 18, 43 + row * 18) {
                            @Override
                            public boolean mayPickup(Player player) {
                                return false; // read-only
                            }

                            @Override
                            public boolean mayPlace(ItemStack stack) {
                                return false; // read-only
                            }
                        });
                }
            }
        }
    }

    private void refreshTargetContainer() {
        if (target == null) return;
        Inventory inv = target.getInventory();

        // rows 1 - 3 for main inventory
        for (int i = 9; i < 36; i++) {
            targetContainer.setItem(i - 9, inv.items.get(i));
        }

        // row 4 for hotbar
        for (int i = 0; i < 9; i++) {
            targetContainer.setItem(27 + i, inv.items.get(i));
        }
    }

    @Override
    public void broadcastChanges() {
        refreshTargetContainer();
        super.broadcastChanges();
    }

    public static void open(ServerPlayer viewer, ServerPlayer target) {
        viewer.openMenu(new SimpleMenuProvider(
                (containerId, inventory, player) -> new LyinEyesInventoryMenu(containerId, inventory, target),
                // purposefully left blank.
                Component.translatable("container.roundabout.lyin_eyes_inventory", target.getDisplayName())
        ));
    }

    // you can read the target's inventory if you're within 20 blocks, but it will stay open until you're further than 50
    @Override
    public boolean stillValid(Player player) {
        return target == null || (target.isAlive() && player.distanceTo(target) <= 50.0F);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // shift-clicking disabled for read-only inspection
        return ItemStack.EMPTY;
    }
}