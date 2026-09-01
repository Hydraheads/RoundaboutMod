package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class MemoryReadingScreen extends Screen {
    private static final int PANEL_WIDTH = 230;
    private static final int PLAYER_PANEL_HEIGHT = 205;
    private static final int MOB_PANEL_HEIGHT = 92;
    private static final int SLOT_SIZE = 18;
    private final ItemStack disc;
    private final InteractionHand hand;
    private final CompoundTag reading;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(41, ItemStack.EMPTY);
    private final byte kind;

    public MemoryReadingScreen(ItemStack disc, InteractionHand hand) {
        super(GameNarrator.NO_TITLE);
        this.disc = disc.copy();
        this.hand = hand;
        this.reading = DiscItemData.getMemoryReading(this.disc);
        this.kind = reading.getByte(DiscItemData.READING_KIND);
        if (kind == DiscItemData.READING_PLAYER
                && reading.contains(DiscItemData.READING_INVENTORY, Tag.TAG_LIST)) {
            ListTag items = reading.getList(DiscItemData.READING_INVENTORY, Tag.TAG_COMPOUND);
            for (int index = 0; index < items.size(); index++) {
                CompoundTag saved = items.getCompound(index);
                int slot = saved.getInt(DiscItemData.READING_SLOT);
                if (slot >= 0 && slot < inventory.size()) inventory.set(slot, ItemStack.of(saved));
            }
        }
    }

    @Override
    protected void init() {
        if (!DiscItemData.isCreeperMemory(disc)) return;
        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - MOB_PANEL_HEIGHT) / 2;
        addRenderableWidget(Button.builder(
                        Component.translatable("screen.roundabout.memory_create_explosive"),
                        button -> {
                            C2SPacketUtil.whitesnakeMemoryDiscConversion(hand);
                            onClose();
                        })
                .bounds(left + 25, top + 63, PANEL_WIDTH - 50, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        int panelHeight = kind == DiscItemData.READING_PLAYER ? PLAYER_PANEL_HEIGHT : MOB_PANEL_HEIGHT;
        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - panelHeight) / 2;
        graphics.fill(left, top, left + PANEL_WIDTH, top + panelHeight, 0xE0101018);
        graphics.fill(left + 2, top + 2, left + PANEL_WIDTH - 2, top + panelHeight - 2, 0xE0282832);
        graphics.drawCenteredString(font, Component.translatable("screen.roundabout.memory_reading"),
                width / 2, top + 8, 0xFFFFFF);

        String owner = DiscItemData.getOwnerName(disc);
        graphics.drawString(font, Component.translatable("screen.roundabout.memory_owner",
                owner.isEmpty() ? "Unknown" : owner), left + 10, top + 23, 0xC8C8C8, false);

        ItemStack hovered = ItemStack.EMPTY;
        if (kind == DiscItemData.READING_PLAYER) {
            renderPlayerDetails(graphics, left, top);
            hovered = renderPlayerInventory(graphics, left, top, mouseX, mouseY);
        } else if (kind == DiscItemData.READING_VILLAGER) {
            renderVillagerDetails(graphics, left, top);
        } else {
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_unavailable"),
                    left + 10, top + 42, 0xAAAAAA, false);
        }
        if (!hovered.isEmpty()) graphics.renderTooltip(font, hovered, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, delta);
    }

    private void renderPlayerDetails(GuiGraphics graphics, int left, int top) {
        if (reading.contains(DiscItemData.READING_SPAWN_POS, Tag.TAG_LONG)) {
            BlockPos pos = BlockPos.of(reading.getLong(DiscItemData.READING_SPAWN_POS));
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_spawn",
                    pos.getX(), pos.getY(), pos.getZ()), left + 10, top + 36, 0xB9D7FF, false);
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_dimension",
                    reading.getString(DiscItemData.READING_SPAWN_DIMENSION)),
                    left + 10, top + 48, 0x909EBD, false);
        } else {
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_spawn_unknown"),
                    left + 10, top + 36, 0xAAAAAA, false);
        }
        graphics.drawString(font, Component.translatable("screen.roundabout.memory_inventory"),
                left + 10, top + 62, 0xFFFFFF, false);
    }

    private ItemStack renderPlayerInventory(GuiGraphics graphics, int left, int top,
                                             int mouseX, int mouseY) {
        ItemStack hovered = ItemStack.EMPTY;
        int gridX = left + 34;
        int gridY = top + 75;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                int slot = 9 + row * 9 + column;
                ItemStack stack = renderSlot(graphics, inventory.get(slot),
                        gridX + column * SLOT_SIZE, gridY + row * SLOT_SIZE, mouseX, mouseY);
                if (!stack.isEmpty()) hovered = stack;
            }
        }
        int hotbarY = gridY + 59;
        for (int column = 0; column < 9; column++) {
            ItemStack stack = renderSlot(graphics, inventory.get(column),
                    gridX + column * SLOT_SIZE, hotbarY, mouseX, mouseY);
            if (!stack.isEmpty()) hovered = stack;
        }
        graphics.drawString(font, Component.translatable("screen.roundabout.memory_equipment"),
                left + 10, top + 157, 0xFFFFFF, false);
        int equipmentX = left + 70;
        int equipmentY = top + 174;
        for (int slot = 36; slot <= 40; slot++) {
            ItemStack stack = renderSlot(graphics, inventory.get(slot),
                    equipmentX + (slot - 36) * SLOT_SIZE, equipmentY, mouseX, mouseY);
            if (!stack.isEmpty()) hovered = stack;
        }
        return hovered;
    }

    private void renderVillagerDetails(GuiGraphics graphics, int left, int top) {
        if (reading.contains(DiscItemData.READING_JOB_POS, Tag.TAG_LONG)) {
            BlockPos pos = BlockPos.of(reading.getLong(DiscItemData.READING_JOB_POS));
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_job_site",
                    pos.getX(), pos.getY(), pos.getZ()), left + 10, top + 42, 0xD6B9FF, false);
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_dimension",
                    reading.getString(DiscItemData.READING_JOB_DIMENSION)),
                    left + 10, top + 55, 0xA590BD, false);
        } else {
            graphics.drawString(font, Component.translatable("screen.roundabout.memory_job_unknown"),
                    left + 10, top + 42, 0xAAAAAA, false);
        }
    }

    private ItemStack renderSlot(GuiGraphics graphics, ItemStack stack, int x, int y,
                                 int mouseX, int mouseY) {
        graphics.fill(x, y, x + 18, y + 18, 0xFF8B8B8B);
        graphics.fill(x + 1, y + 1, x + 17, y + 17, 0xFF373737);
        if (!stack.isEmpty()) {
            graphics.renderItem(stack, x + 1, y + 1);
            graphics.renderItemDecorations(font, stack, x + 1, y + 1);
        }
        return mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18
                ? stack : ItemStack.EMPTY;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
