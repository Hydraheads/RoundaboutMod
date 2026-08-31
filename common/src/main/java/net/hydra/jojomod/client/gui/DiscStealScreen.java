package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class DiscStealScreen extends Screen implements NoCancelInputScreen {
    private static final ResourceLocation BACKGROUND = StandIcons.WHITESNAKE_DISC_STEAL_MENU;
    private static final int[][] POSITIONS = {{31, 0}, {0, -31}, {-31, 0}, {0, 31}};

    private final List<DiscSlot> slots = Lists.newArrayList();
    private int currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;

    public DiscStealScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        super.init();
        Player player = Minecraft.getInstance().player;
        if (player != null && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers) {
            currentlyHovered = powers.getSelectedDisc();
            for (int id = 0; id < POSITIONS.length; id++) {
                if (!WhitesnakeDiscUtil.isDiscStealEnabled((byte) id)) continue;
                DiscIcon icon = new DiscIcon(id, POSITIONS[id][0], POSITIONS[id][1] + 31);
                slots.add(new DiscSlot(icon, width / 2 + icon.xoff - 13, height / 2 + icon.yoff - 44));
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        applySelection();
        minecraft.setScreen(null);
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (checkToClose()) return;
        graphics.pose().pushPose();
        RenderSystem.enableBlend();
        graphics.blit(BACKGROUND, width / 2 - 62, height / 2 - 70, 0, 0, 125, 24, 256, 256);
        graphics.pose().popPose();
        super.render(graphics, mouseX, mouseY, delta);

        Component title = currentlyHovered < 0
                ? Component.translatable("roundabout.whitesnake.no_disc_steal_types")
                : switch (currentlyHovered) {
            case 1 -> Component.translatable("roundabout.whitesnake.sight_disc");
            case 2 -> Component.translatable("roundabout.whitesnake.memory_disc");
            case 3 -> Component.translatable("roundabout.whitesnake.hearing_disc");
            default -> Component.translatable("roundabout.whitesnake.stand_disc");
        };
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 63, -1);

        if (!setFirstMousePos) {
            firstMouseX = mouseX;
            firstMouseY = mouseY;
            setFirstMousePos = true;
        }
        boolean unchanged = firstMouseX == mouseX && firstMouseY == mouseY;
        for (DiscSlot slot : slots) {
            slot.render(graphics, mouseX, mouseY, delta);
            slot.setSelected(currentlyHovered == slot.icon.id);
            if (!unchanged && slot.isHoveredOrFocused()) currentlyHovered = slot.icon.id;
        }
    }

    private void applySelection() {
        if (minecraft == null || minecraft.player == null || currentlyHovered < 0) return;
        if (((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersWhitesnake powers) {
            powers.tryIntPower(PowersWhitesnake.DISC_SELECTION, true, currentlyHovered);
            powers.tryIntPowerPacket(PowersWhitesnake.DISC_SELECTION, currentlyHovered);
        }
    }

    private boolean checkToClose() {
        if (minecraft == null) return false;
        KeyMapping key = KeyInputRegistry.abilityOneKey;
        long window = minecraft.getWindow().getWindow();
        boolean held = InputConstants.isKeyDown(window, ((IKeyMapping) key).roundabout$justTellMeTheKey().getValue())
                || (key.same(minecraft.options.keyLoadHotbarActivator)
                && InputConstants.isKeyDown(window, ((IKeyMapping) minecraft.options.keyLoadHotbarActivator)
                .roundabout$justTellMeTheKey().getValue()))
                || (key.same(minecraft.options.keySaveHotbarActivator)
                && InputConstants.isKeyDown(window, ((IKeyMapping) minecraft.options.keySaveHotbarActivator)
                .roundabout$justTellMeTheKey().getValue()));
        if (!held) {
            applySelection();
            minecraft.setScreen(null);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private record DiscIcon(int id, int xoff, int yoff) {
    }

    private class DiscSlot extends AbstractWidget {
        private final DiscIcon icon;
        private boolean selected;

        private DiscSlot(DiscIcon icon, int x, int y) {
            super(x, y, 26, 26, Component.empty());
            this.icon = icon;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            graphics.blit(BACKGROUND, getX(), getY(), 144, 26, 26, 26, 256, 256);
            if (selected) graphics.blit(BACKGROUND, getX(), getY(), 170, 26, 26, 26, 256, 256);
            graphics.blit(StandIcons.WHITESNAKE_DISC_TYPES[icon.id], getX() + 4, getY() + 4,
                    0, 0, 18, 18, 18, 18);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return super.isHoveredOrFocused() || selected;
        }

        private void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
