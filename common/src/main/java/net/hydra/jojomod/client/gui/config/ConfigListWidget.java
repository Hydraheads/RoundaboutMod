package net.hydra.jojomod.client.gui.config;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.config.annotation.Hidden;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ConfigListWidget extends ContainerObjectSelectionList<ConfigListWidget.Entry> {
    private final ConfigScreen parent;
    private final HashMap<String, Object> configFields = new HashMap<>();
    private final ConfigType configType;

    public static int constant1 = 340;
    public static int constant2 = constant1-40;

    public ConfigListWidget(ConfigScreen parent, Minecraft client, ConfigType selectedType) {
        super(client, constant1, parent.height, 20, parent.height - 40, 32);
        this.setLeftPos((parent.width - constant1) / 2);
        this.parent = parent;
        this.configType = selectedType;

        switch (selectedType)
        {
            case COMMON -> this.addEntry(new CommentEntry("§r§6§l"+Component.translatable("config.roundabout.major.common_config").getString()));
            case CLIENT -> this.addEntry(new CommentEntry("§r§6§l"+Component.translatable("config.roundabout.major.client_config").getString()));
        }

        Object instance = null;

        switch (selectedType)
        {
            case COMMON -> instance = Config.getLocalInstance();
            case CLIENT -> instance = ClientConfig.getLocalInstance();
        }

        Class<?> clazz = instance.getClass();

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
                } else if (value instanceof String) {
                    this.addEntry(new StringEntry(field, instance));
                } else if(!field.isAnnotationPresent(Hidden.class)) {
                    Object nestedObject = field.get(instance);
                    this.addEntry(new CommentEntry(
                            Component.translatable("config.roundabout." + field.getName() + ".name").getString()));

                    if (nestedObject == null) {
                        continue;
                    }

                    for (Field clazzField : nestedObject.getClass().getFields()) {
                        clazzField.setAccessible(true);
                        Object clazzValue = clazzField.get(nestedObject);
                        if (!clazzField.isAnnotationPresent(Hidden.class)) {
                            if (clazzValue instanceof Boolean) {
                                this.addEntry(new BooleanEntry(clazzField, nestedObject));
                            } else if (clazzValue instanceof Integer || clazzValue instanceof Float) {
                                this.addEntry(new NumberEntry(clazzField, nestedObject));
                            } else if (clazzValue instanceof String) {
                                this.addEntry(new StringEntry(clazzField, nestedObject));
                            }
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to parse configFields", e);
            }
        }
        if (selectedType == ConfigType.CLIENT) {
            this.addEntry(new AnubisEntry(ConfigManager.getClientConfigPath()));
        }

    }

    @Override
    public int getRowWidth() {
        return constant2;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowLeft() + 20 + getRowWidth() - 6;
    }

    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
    }

    public class AnubisEntry extends Entry {
        private Button button;
        public Path config_path;
        public AnubisEntry(Path p) {
            config_path = p;
            button = Button.builder(Component.translatable("config.roundabout.anubisMemoryOpen.name"), (button) -> {
                Util.getPlatform().openUri(config_path.toUri());
            }).build();
        }
        @Override
        public void render(GuiGraphics drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            button.setX(x+entryWidth/4);
            button.setY(y);
            button.render(drawContext, mouseX, mouseY, delta);
        }


        @Override
        public List<? extends NarratableEntry> narratables() {return List.of(button);}
        @Override
        public List<? extends GuiEventListener> children() {return List.of(button);}
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

            toggleButton = Button.builder(Component.translatable("config.roundabout."+field.getName()+".name").append(getNewValueRender(currentValue)),
                    btn -> {
                        try {
                            Boolean newVal = !(Boolean) field.get(instance);
                            field.set(instance, newVal);

                            switch (configType)
                            {
                                case COMMON -> ConfigManager.saveLocalConfig();
                                case CLIENT -> ConfigManager.saveClientConfig();
                            }

                            btn.setMessage(Component.translatable("config.roundabout."+field.getName()+".name").append(getNewValueRender(newVal)));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }).size(constant2, 20).build();
        }

        public Component getNewValueRender(boolean newVal){
            if (newVal)
                return Component.literal(": ").append(Component.literal(("")+newVal).withStyle(ChatFormatting.AQUA));
            return Component.literal(": ").append(Component.literal(("")+newVal).withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        @Override
        public void render(GuiGraphics drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            toggleButton.setX(x);
            toggleButton.setY(y);
            toggleButton.render(drawContext, mouseX, mouseY, delta);
            renderHover(drawContext,index,y,x,width,height,mouseX,mouseY,hovered,delta,field, toggleButton);
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


    public void renderHover(GuiGraphics drawContext, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTic,
                            Field field, Button editButton){
        if (editButton.isHovered()){
            List<Component> compList = Lists.newArrayList();
            String[] strung2 = ClientUtil.splitIntoLine(Component.translatable("config.roundabout."+field.getName()+".desc").getString(), 35);
            for (String s : strung2) {
                compList.add(Component.literal(s).withStyle(ChatFormatting.BLUE));
            }
            drawContext.renderTooltip(Minecraft.getInstance().font, compList, Optional.empty(), x, y+8-(10*strung2.length));
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
                Minecraft.getInstance().setScreen(new EditValueScreen(ConfigListWidget.this.parent, field, instance, configType,getScrollAmount()));
            }).size(constant2, 20).build();
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
            editButton.setX(x);
            editButton.setY(y);
            editButton.setMessage(Component.translatable("config.roundabout."+field.getName()+".name"));
            editButton.render(drawContext, mouseX, mouseY, partialTick);
            renderHover(drawContext,index,y,x,width,height,mouseX,mouseY,hovering,partialTick,field, editButton);

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


    public class StringEntry extends Entry {
        private final Field field;
        private final Object instance;
        private final Button editButton;

        public StringEntry(Field field, Object instance) {
            this.field = field;
            this.instance = instance;

            String display = getFieldDisplay();
            editButton = Button.builder(Component.literal(display), btn -> {
                Minecraft.getInstance().setScreen(new EditValueScreen(ConfigListWidget.this.parent, field, instance, configType,getScrollAmount()));
            }).size(constant2, 20).build();
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
            editButton.setX(x);
            editButton.setY(y);
            editButton.setMessage(Component.translatable("config.roundabout."+field.getName()+".name"));
            editButton.render(drawContext, mouseX, mouseY, partialTick);
            renderHover(drawContext,index,y,x,width,height,mouseX,mouseY,hovering,partialTick,field, editButton);

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
                    Component.literal("§e§l" + comment + "§r"),
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