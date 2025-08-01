package net.hydra.jojomod.client.gui.config;

import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ConfigScreen extends Screen {
    public ConfigListWidget listWidget;
    private Button doneButton;
    private Button resetButton;
    private Button configTypeButton;
    private ConfigType selectedType;
    private final Screen lastScreen;

    public ConfigScreen(ConfigType type, Screen lastScreen) {
        super(GameNarrator.NO_TITLE);
        this.selectedType = type;
        this.lastScreen =lastScreen;
    }

    @Override
    protected void init() {
        int buttonWidth = 100;
        int buttonHeight = 20;
        int padding = 10;
        int spacing = 10;
        int totalWidth = 3 * buttonWidth + 2 * spacing;
        int startX = (this.width - totalWidth) / 2;
        int y = this.height - buttonHeight - padding;

        this.listWidget = new ConfigListWidget(this, this.minecraft, selectedType);
        this.addRenderableWidget(listWidget);

        this.configTypeButton = Button.builder(Component.literal("Config: " + selectedType.name()), btn -> {
                    ConfigType[] types = ConfigType.values();
                    int index = (selectedType.ordinal() + 1) % types.length;
                    selectedType = types[index];
                    this.minecraft.setScreen(new ConfigScreen(selectedType,lastScreen));
                }).size(buttonWidth, buttonHeight)
                .pos(startX, y)
                .build();

        this.doneButton = Button.builder(Component.translatable("config.roundabout.major.done"), btn -> {
                    this.minecraft.setScreen(lastScreen);
                }).size(buttonWidth, buttonHeight)
                .pos(startX + buttonWidth + spacing, y)
                .build();

        this.resetButton = Button.builder(Component.translatable("config.roundabout.major.reset"), btn -> {
                    switch (selectedType) {
                        case COMMON -> {
                            ConfigManager.resetLocal();
                            ConfigManager.saveLocalConfig();
                        }
                        case CLIENT -> {
                            ConfigManager.resetClient();
                            ConfigManager.saveClientConfig();
                        }
                    }

                    this.minecraft.setScreen(new ConfigScreen(selectedType,lastScreen));
                }).size(buttonWidth, buttonHeight)
                .pos(startX + 2 * (buttonWidth + spacing), y)
                .build();

        this.addRenderableWidget(doneButton);
        this.addRenderableWidget(configTypeButton);
        this.addRenderableWidget(resetButton);
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(drawContext);
        this.listWidget.render(drawContext, mouseX, mouseY, tickDelta);
        super.render(drawContext, mouseX, mouseY, tickDelta);
    }
}