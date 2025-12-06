package net.hydra.jojomod.client.gui.config;

import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public class EditValueScreen extends Screen {
    private final Screen parent;
    private final Field field;
    private final Object instance;
    private EditBox inputBox;
    private Button doneButton;
    private final ConfigType configType;
    private final double savedScrollAmount;

    public EditValueScreen(Screen parent, Field field, Object instance, ConfigType configType, double savedScrollAmount) {
        super(Component.translatable("config.roundabout.major.edit_value"));
        this.parent = parent;
        this.field = field;
        this.instance = instance;
        this.configType = configType;
        this.savedScrollAmount = savedScrollAmount;
    }

    @Override
    protected void init() {
        this.inputBox = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Component.translatable("config.roundabout.major.value"));
        try {
            this.inputBox.setValue(String.valueOf(field.get(instance)));
        } catch (IllegalAccessException e) {
            this.inputBox.setValue("Error");
        }
        this.addRenderableWidget(inputBox);

        this.doneButton = Button.builder(Component.translatable("config.roundabout.major.done"), btn -> {
            try {
                String text = inputBox.getValue();
                Class<?> type = field.getType();

                if (type == int.class || type == Integer.class) {
                    field.set(instance, Integer.parseInt(text));
                } else if (type == float.class || type == Float.class) {
                    field.set(instance, Float.parseFloat(text));
                } else if (type == String.class) {
                    field.set(instance, text);
                } else {
                    throw new RuntimeException("Unsupported type caught while creating EditValueScreen!");
                }

                switch (configType)
                {
                    case COMMON -> ConfigManager.saveLocalConfig();
                    case CLIENT -> ConfigManager.saveClientConfig();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.minecraft.setScreen(parent);
            if (parent instanceof ConfigScreen cs){
                cs.listWidget.setScrollAmount(savedScrollAmount);
            }
        }).pos(this.width / 2 - 40, this.height / 2 + 20).size(80, 20).build();

        this.addRenderableWidget(doneButton);
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        drawContext.drawCenteredString(this.font, Component.translatable("config.roundabout.major.edit").getString()+" " + field.getName(), this.width / 2, 20, 0xFFFFFF);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}