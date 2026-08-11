package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class WhitesnakeDisguiseScreen extends Screen {
    private EditBox username;

    public WhitesnakeDisguiseScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
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
        setInitialFocus(username);
    }

    private void submit() {
        String name = username.getValue().trim();
        if (!name.isEmpty()) C2SPacketUtil.whitesnakeDisguise(name);
        onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            onClose();
            return true;
        }
        if (keyCode == 257 || keyCode == 335) {
            submit();
            return true;
        }
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
        graphics.drawCenteredString(font, Component.translatable("roundabout.whitesnake.disguise.title"),
                width / 2, height / 2 - 36, -1);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
