package net.hydra.jojomod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.AnimalTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemImbueScreen extends Screen {
    private static final ResourceLocation MENU_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/ge_item_imbue.png");

    private final int imageWidth = 195;
    private final int imageHeight = 136;
    private int leftPos;
    private int topPos;

    private float scrollOffs = 0.0F;
    private boolean isScrolling = false;

    private final int totalColumns = 9;
    private final int visibleRows = 5;

    private final List<ImbueOption> options = new ArrayList<>();

    private record ImbueOption(ItemStack icon, Component title, Component description, Runnable onSelect) {}

    public ItemImbueScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.options.clear();

        // Animal imbue for slot 0
        this.options.add(new ImbueOption(
                new ItemStack(Items.CHICKEN_SPAWN_EGG),
                Component.translatable("roundabout.gold_experience_item_imbue.animal"),
                Component.translatable("roundabout.gold_experience_item_imbue.animal.desc"),
                () -> selectImbue(PowersGoldExperience.IMBUE_ANIMAL)
        ));
        this.options.add(new ImbueOption(
                new ItemStack(Items.OAK_SAPLING),
                Component.translatable("roundabout.gold_experience_item_imbue.tree"),
                Component.translatable("roundabout.gold_experience_item_imbue.tree.desc"),
                () -> selectImbue(PowersGoldExperience.IMBUE_TREE)
        ));
    }

    private void selectImbue(byte imbueType) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        if (((StandUser) this.minecraft.player).roundabout$getStandPowers() instanceof PowersGoldExperience powers) {
            byte animalId = AnimalTypes.FROG.id;
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                animalId = clientConfig.dynamicSettings.goldExperienceCurrentAnimal.byteValue();
            }

            int packedData = PowersGoldExperience.packImbueData(imbueType, animalId);
            powers.tryIntPower(PowersGoldExperience.ITEM_IMBUE, true, packedData);
            powers.tryIntPowerPacket(PowersGoldExperience.ITEM_IMBUE, packedData);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(MENU_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int scrollBarX = this.leftPos + 175;
        int scrollBarYMin = this.topPos + 18;
        int scrollBarYMax = this.topPos + 108;
        int scrollRange = scrollBarYMax - scrollBarYMin - 15;
        int currentScrollY = scrollBarYMin + (int) (this.scrollOffs * scrollRange);

        guiGraphics.blit(MENU_TEXTURE, scrollBarX, currentScrollY, 195, 0, 12, 15);

        ImbueOption hoveredOption = null;
        int startRow = getScrollRow();

        for (int i = 0; i < visibleRows * totalColumns; i++) {
            int itemIndex = (startRow * totalColumns) + i;
            if (itemIndex >= this.options.size()) break;

            int col = i % totalColumns;
            int row = i / totalColumns;

            int slotX = this.leftPos + 9 + (col * 18);
            int slotY = this.topPos + 18 + (row * 18);

            ImbueOption option = this.options.get(itemIndex);
            guiGraphics.renderItem(option.icon(), slotX, slotY);

            if (isHoveringSlot(slotX, slotY, mouseX, mouseY)) {
                hoveredOption = option;
                guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80FFFFFF);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if (hoveredOption != null) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(hoveredOption.title().copy().withStyle(ChatFormatting.GOLD));
            tooltip.add(hoveredOption.description().copy().withStyle(ChatFormatting.GRAY));
            guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.canScroll()) {
            int totalRows = Mth.ceil((float) this.options.size() / (float) this.totalColumns);
            int hiddenRows = totalRows - this.visibleRows;

            this.scrollOffs = (float) ((double) this.scrollOffs - delta / (double) hiddenRows);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.isHoveringScrollbar(mouseX, mouseY)) {
                this.isScrolling = this.canScroll();
                return true;
            }

            int startRow = getScrollRow();
            for (int i = 0; i < visibleRows * totalColumns; i++) {
                int itemIndex = (startRow * totalColumns) + i;
                if (itemIndex >= this.options.size()) break;

                int col = i % totalColumns;
                int row = i / totalColumns;

                int slotX = this.leftPos + 8 + (col * 18);
                int slotY = this.topPos + 18 + (row * 18);

                if (isHoveringSlot(slotX, slotY, mouseX, mouseY)) {
                    if (this.minecraft != null) {
                        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    this.options.get(itemIndex).onSelect().run();
                    this.onClose();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isScrolling) {
            int scrollBarYMin = this.topPos + 18;
            int scrollBarYMax = this.topPos + 108;
            int scrollRange = scrollBarYMax - scrollBarYMin - 15;

            this.scrollOffs = ((float) mouseY - (float) scrollBarYMin - 7.5F) / (float) scrollRange;
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isScrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isHoveringSlot(int slotX, int slotY, double mouseX, double mouseY) {
        return mouseX >= slotX && mouseX < slotX + 16 && mouseY >= slotY && mouseY < slotY + 16;
    }

    private boolean isHoveringScrollbar(double mouseX, double mouseY) {
        int scrollXMin = this.leftPos + 175;
        int scrollXMax = scrollXMin + 12;
        int scrollYMin = this.topPos + 18;
        int scrollYMax = this.topPos + 108;
        return mouseX >= scrollXMin && mouseX <= scrollXMax && mouseY >= scrollYMin && mouseY <= scrollYMax;
    }

    private boolean canScroll() {
        return this.options.size() > this.visibleRows * this.totalColumns;
    }

    private int getScrollRow() {
        int totalRows = Mth.ceil((float) this.options.size() / (float) this.totalColumns);
        int hiddenRows = Math.max(0, totalRows - this.visibleRows);
        return (int) ((double) (this.scrollOffs * (float) hiddenRows) + 0.5D);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}