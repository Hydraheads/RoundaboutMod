package net.hydra.jojomod.client.gui.config;

import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class ConfigListWidget extends ContainerObjectSelectionList<ConfigListWidget.Entry> {
    private final ConfigScreen parent;
    private final HashMap<String, Object> configFields = new HashMap<>();

    public ConfigListWidget(ConfigScreen parent, Minecraft client) {
        super(client, parent.width + 45, parent.height, 20, parent.height, 32);
        this.parent = parent;
        this.addEntry(new CommentEntry("Common Config"));

        Config instance = Config.getLocalInstance();
        Class<? extends Config> clazz = instance.getClass();

        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(instance);
                configFields.put(field.getName(), value);

                if (value instanceof Boolean) {
                    this.addEntry(new BooleanEntry(field, instance));
                } else if (value instanceof Integer || value instanceof Float) {
                    this.addEntry(new NumberEntry(field, instance));
                }
                else {
                    Object nestedObject = field.get(instance);
                    this.addEntry(new CommentEntry(field.getName()));

                    for (Field clazzField : nestedObject.getClass().getFields()) {
                        clazzField.setAccessible(true);
                        Object clazzValue = clazzField.get(nestedObject);

                        if (clazzValue instanceof Boolean) {
                            this.addEntry(new BooleanEntry(clazzField, nestedObject));
                        } else if (clazzValue instanceof Integer || clazzValue instanceof Float) {
                            this.addEntry(new NumberEntry(clazzField, nestedObject));
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to parse configFields", e);
            }
        }
    }

    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
    }

    public class BooleanEntry extends Entry {
        private final Field field;
        private final Object instance;
        private final Button toggleButton;

        public BooleanEntry(Field field, Object instance) {
            this.field = field;
            this.instance = instance;

            boolean currentValue = false;
            try {
                currentValue = (Boolean) field.get(instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            toggleButton = Button.builder(Component.literal(field.getName() + ": " + currentValue),
                    btn -> {
                        try {
                            Boolean newVal = !(Boolean) field.get(instance);
                            field.set(instance, newVal);

                            ConfigManager.saveLocalConfig();

                            btn.setMessage(Component.literal(field.getName() + ": " + newVal));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }).size(200, 20).build();
        }

        @Override
        public void render(GuiGraphics drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            toggleButton.setX(x + 10);
            toggleButton.setY(y);
            toggleButton.render(drawContext, mouseX, mouseY, delta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(toggleButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(toggleButton);
        }
    }

    public class NumberEntry extends Entry {
        private final Field field;
        private final Object instance;
        private final Button editButton;

        public NumberEntry(Field field, Object instance) {
            this.field = field;
            this.instance = instance;

            String display = getFieldDisplay();
            editButton = Button.builder(Component.literal(display), btn -> {
                Minecraft.getInstance().setScreen(new EditValueScreen(ConfigListWidget.this.parent, field, instance));
            }).size(200, 20).build();
        }

        private String getFieldDisplay() {
            try {
                return field.getName() + ": " + field.get(instance);
            } catch (IllegalAccessException e) {
                return field.getName() + ": [error]";
            }
        }

        @Override
        public void render(GuiGraphics drawContext, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            editButton.setX(x+10);
            editButton.setY(y);
            editButton.setMessage(Component.literal(getFieldDisplay()));
            editButton.render(drawContext, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(editButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(editButton);
        }
    }

    public class CommentEntry extends Entry {
        private final String comment;

        public CommentEntry(String comment) {
            this.comment = comment;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float delta) {
            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    Component.literal("§l" + comment + "§r"),
                    x + entryWidth / 2,
                    y + 6,
                    0xFFFFFF
            );
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }
    }
}