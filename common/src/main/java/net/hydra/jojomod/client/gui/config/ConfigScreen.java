package net.hydra.jojomod.client.gui.config;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class ConfigScreen extends Screen {
    private ConfigListWidget listWidget;

    public ConfigScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        this.listWidget = new ConfigListWidget(this, this.minecraft);
        this.addRenderableWidget(listWidget);
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(drawContext);
        this.listWidget.render(drawContext, mouseX, mouseY, tickDelta);
        super.render(drawContext, mouseX, mouseY, tickDelta);
    }
}