package net.hydra.jojomod.client.gui.diverdown;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class DiverDownDisguiseScreen extends Screen {
    private EditBox username;
    private Button armorToggleButton;
    private boolean showArmor = true;

    private static final ItemStack ARMOR_ON_ICON = new ItemStack(Items.IRON_CHESTPLATE);
    private static final ItemStack ARMOR_OFF_ICON = new ItemStack(Items.BARRIER);

    public DiverDownDisguiseScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        if (Minecraft.getInstance().player instanceof StandUser su) {
            StandPowers sp = su.roundabout$getStandPowers();
            if (sp instanceof PowersDiverDown dd) {
                this.showArmor = dd.shouldShowDisguiseArmor();
            }
        }

        int centerX = width / 2;
        int centerY = height / 2;
        username = new EditBox(font, centerX - 100, centerY - 10, 200, 20,
                Component.translatable("roundabout.whitesnake.disguise.username"));
        username.setMaxLength(16);
        addRenderableWidget(username);
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> submit())
                .bounds(centerX - 100, centerY + 18, 98, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(centerX + 2, centerY + 18, 98, 20).build());
        armorToggleButton = Button.builder(Component.empty(), button -> toggleShowArmor())
                .bounds(centerX + 104, centerY - 10, 20, 20)
                .build();
        addRenderableWidget(armorToggleButton);
        setInitialFocus(username);
    }

    private void toggleShowArmor() {
        this.showArmor = !this.showArmor;
        if (Minecraft.getInstance().player instanceof StandUser su) {
            StandPowers sp = su.roundabout$getStandPowers();
            if (sp instanceof PowersDiverDown dd) {
                dd.setShowDisguiseArmor(this.showArmor);
            }
        }
    }

    private void submit() {
        String name = username.getValue().trim();
        if (!name.isEmpty()) {
            C2SPacketUtil.diverDownDisguise(name);
        }
        onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { onClose(); return true; }
        if (keyCode == 257 || keyCode == 335) { submit(); return true; }
        username.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        username.charTyped(codePoint, modifiers);
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, Component.translatable("roundabout.diver_down.disguise.title"),
                width / 2, height / 2 - 36, -1);
        super.render(graphics, mouseX, mouseY, delta);
        if (armorToggleButton != null) {
            ItemStack icon = showArmor ? ARMOR_ON_ICON : ARMOR_OFF_ICON;
            graphics.renderItem(icon, armorToggleButton.getX() + 2, armorToggleButton.getY() + 2);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}