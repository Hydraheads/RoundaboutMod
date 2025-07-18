package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class PlunderScreen extends Screen implements NoCancelInputScreen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation MOB_SWITCHER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/pose_switcher.png");
    private static final int SPRITE_SHEET_WIDTH = 256;
    private static final int SPRITE_SHEET_HEIGHT = 256;
    private static final int SLOT_AREA = 26;
    private static final int SLOT_PADDING = 5;
    private static final int SLOT_AREA_PADDED = 31;
    private static final int HELP_TIPS_OFFSET_Y = 5;
    private static final int ALL_SLOTS_WIDTH = standRerollIcon.VALUES.length * 31 - 5;
    private standRerollIcon currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    public ItemStack arrow = ItemStack.EMPTY;

    private final List<PlunderSlot> slots = Lists.newArrayList();

    public PlunderScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }

    public PlunderScreen(ItemStack arrow) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
        this.arrow = arrow;
    }
    private ShapeShifts getDefaultSelected() {
        Player pl = Minecraft.getInstance().player;
        if (pl != null){
            return ShapeShifts.getShiftFromByte(((IPlayerEntity)pl).roundabout$getShapeShift());
        }
        return ShapeShifts.PLAYER;
    }

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;


        this.currentlyHovered = standRerollIcon.NONE;
            for (int i = 0; i < standRerollIcon.VALUES.length; ++i) {
                standRerollIcon pIcon = standRerollIcon.VALUES[i];
                this.slots.add(new PlunderSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
            }
    }
    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityOneKey)) {
            this.switchToHoveredGameMode();
            this.minecraft.setScreen(null);
            if (this.minecraft.player != null){
                StandUser SU = ((StandUser) this.minecraft.player);
                if (SU.roundabout$getStandPowers().isBarraging()){
                    //This prevents barrage canceling
                } else {
                    this.minecraft.options.keyUse.setDown(false);
                }
            }
        }
        return false;
        //return super.keyReleased($$0, $$1, $$2);
    }
    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if ($$2 == 0) {
            this.switchToHoveredGameMode();
            this.minecraft.setScreen(null);
            this.minecraft.options.keyUse.setDown(false);
            return false;
        } else {
            if ($$2 == 1) {

            }
            return false;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (this.checkToClose()) {
            return;
        }
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 62;
        int l = this.height / 2 - 90;
        guiGraphics.blit(MOB_SWITCHER_LOCATION, k, l, 0.0f, 63.0f, 125, 22, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);
        if (this.currentlyHovered != null) {
            guiGraphics.drawCenteredString(this.font, this.currentlyHovered.getName(), this.width / 2, l+7, -1);
            if (this.currentlyHovered.id != 0) {
                List<Component> compList = Lists.newArrayList();
                String[] strung2 = splitIntoLine(this.currentlyHovered.desc.getString(), 30);
                for (String s : strung2) {
                    compList.add(Component.literal(s));
                }
                guiGraphics.renderTooltip(this.font, compList, Optional.empty(), i, j);
            }
        }
        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        caughtSomething = false;
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
        for (PlunderSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            caughtSomething = true;

            if (MobSlot.icon != standRerollIcon.NONE) {
                setWidgetNumber(MobSlot.icon.id);
                if (this.currentlyHovered == standRerollIcon.NONE) {
                    SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
                    soundmanager.play(SimpleSoundInstance.forUI(ModSounds.BUBBLE_HOVERED_OVER_EVENT, (float) (0.95 + (Math.random() * 0.1F))));
                }
            }
            this.currentlyHovered = MobSlot.icon;
        }

        if (!caughtSomething){
            this.currentlyHovered = standRerollIcon.NONE;
        }

    }
    boolean caughtSomething = false;

    public String[] splitIntoLine(String input, int maxCharInLine){

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while(word.length() > maxCharInLine){
                output.append(word.substring(0, maxCharInLine-lineLen) + "\n");
                word = word.substring(maxCharInLine-lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }

    private void switchToHoveredGameMode() {
        switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }

    private void switchToHoveredGameMode(Minecraft minecraft, standRerollIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }
            //ModPacketHandler.PACKET_ACCESS.byteToServerPacket(pIcon3.id, PacketDataIndex.BYTE_CHANGE_MORPH);
    }

    public boolean roundabout$sameKeyOne(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.minecraft.options.keyLoadHotbarActivator) && this.minecraft.options.keyLoadHotbarActivator.isDown())
                || (key1.same(this.minecraft.options.keySaveHotbarActivator) && this.minecraft.options.keySaveHotbarActivator.isDown())
        );
    }
    public boolean sameKeyOne(KeyMapping key1, Options options){
        return (key1.isDown() || (key1.same(options.keyLoadHotbarActivator) && options.keyLoadHotbarActivator.isDown())
                || (key1.same(options.keySaveHotbarActivator) && options.keySaveHotbarActivator.isDown())
        );
    }
    public boolean sameKeyOneX(KeyMapping key1, Options options){
        return (InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)key1).roundabout$justTellMeTheKey().getValue())
                || (key1.same(options.keyLoadHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keyLoadHotbarActivator).roundabout$justTellMeTheKey().getValue()))
                || (key1.same(options.keySaveHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keySaveHotbarActivator).roundabout$justTellMeTheKey().getValue()))
        );
    }
    private boolean checkToClose() {
        if (minecraft != null) {
            if (sameKeyOneX(KeyInputRegistry.abilityTwoKey, this.minecraft.options)) {
                this.switchToHoveredGameMode();
                this.minecraft.setScreen(null);
                return true;
            }
        }
        Options options = Minecraft.getInstance().options;
        return false;
    }

    public void tick() {
        /**Forge force freezes all inputs regardless of settings in a secreen so it needs to be evaporated*/
        super.tick();
        /***
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && !mc.isPaused()) {
            mc.player.aiStep();
        }
         **/
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Prevent the screen from handling WASD, space, shift, etc.
        if (Minecraft.getInstance().options.keyUp.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyDown.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyLeft.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyRight.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyJump.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyShift.matches(keyCode, scanCode)) {
            return false; // Let these go through to the player
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public enum standRerollIcon {
        ITEM_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.items"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/item.png"),PlunderTypes.ITEM.id,-43,31, Component.translatable("roundabout.soft_and_wet_plunder.items.desc")),
        SOUND_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.sound"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/sound.png"),PlunderTypes.SOUND.id,-28,1, Component.translatable("roundabout.soft_and_wet_plunder.sound.desc")),
        FRICTION_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.friction"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/friction.png"),PlunderTypes.FRICTION.id,42,31, Component.translatable("roundabout.soft_and_wet_plunder.friction.desc")),
        SIGHT_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.sight"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/sight.png"),PlunderTypes.SIGHT.id,0,-16, Component.translatable("roundabout.soft_and_wet_plunder.sight.desc")),
        OXYGEN_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.oxygen"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/oxygen.png"),PlunderTypes.OXYGEN.id,28,1, Component.translatable("roundabout.soft_and_wet_plunder.oxygen.desc")),
        MOB_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.mobs"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/mobs.png"),PlunderTypes.MOBS.id,28,61, Component.translatable("roundabout.soft_and_wet_plunder.mobs.desc")),
        POTION_EFFECT_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.potion_effects"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/effects.png"),PlunderTypes.POTION_EFFECTS.id,-28,61, Component.translatable("roundabout.soft_and_wet_plunder.potion_effects.desc")),
        MOISTURE_PLUNDER(Component.translatable("roundabout.soft_and_wet_plunder.moisture"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/moisture.png"),PlunderTypes.MOISTURE.id,0,78, Component.translatable("roundabout.soft_and_wet_plunder.moisture.desc")),

        NONE(Component.translatable("roundabout.soft_and_wet_plunder.none"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/main_stand.png"),(byte)0,0,31, Component.translatable("roundabout.stand_switch.main.desc"));

        static standRerollIcon getByte(PlunderTypes pose) {
            return switch (pose) {
                default -> throw new IncompatibleClassChangeError();
                case NONE -> NONE;
                case ITEM -> ITEM_PLUNDER;
                case SOUND -> SOUND_PLUNDER;
                case FRICTION -> FRICTION_PLUNDER;
                case OXYGEN -> OXYGEN_PLUNDER;
                case SIGHT -> SIGHT_PLUNDER;
                case MOISTURE -> MOISTURE_PLUNDER;
                case POTION_EFFECTS -> POTION_EFFECT_PLUNDER;
                case MOBS -> MOB_PLUNDER;
            };
        }
        protected static final standRerollIcon[] VALUES;
        private static final int ICON_AREA = 16;
        protected static final int ICON_TOP_LEFT = 5;
        final Component name;
        final Component desc;
        final ResourceLocation rl;
        final byte id;

        final int xoff;
        final int yoff;

        private standRerollIcon(Component component, ResourceLocation rl, byte id, int xoff, int yoff, Component desc) {
            this.name = component;
            this.rl = rl;
            this.id = id;
            this.xoff = xoff;
            this.yoff = yoff;
            this.desc = desc;
        }

        void drawIcon(GuiGraphics guiGraphics, int i, int j) {
            guiGraphics.blit(rl, i-1, j-1, 0, 0, 18, 18, 18, 18);
        }

        Component getName() {
            return this.name;
        }

        static {
            VALUES = new standRerollIcon[]{
                    ITEM_PLUNDER,
                    SOUND_PLUNDER,
                    FRICTION_PLUNDER,
                    OXYGEN_PLUNDER,
                    SIGHT_PLUNDER,
                    MOB_PLUNDER,
                    POTION_EFFECT_PLUNDER,
                    MOISTURE_PLUNDER,
                    NONE
            };
        }
    }

    public void setWidgetNumber(byte swap){
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.dynamicSettings != null){
            if (clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble != (int) swap) {
                clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble = (int) swap;
                ConfigManager.saveClientConfig();
            }
        }
    }
    public byte getWidgetNumber(){
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.dynamicSettings != null){
            return clientConfig.dynamicSettings.SoftAndWetCurrentlySelectedBubble.byteValue();
        }
        return 0;
    }
    public class PlunderSlot
            extends AbstractWidget {
        final standRerollIcon icon;
        private boolean isSelected;

        public PlunderSlot(standRerollIcon pIcon, int i, int j) {
            super(i, j, 26, 26, pIcon.getName());
            this.icon = pIcon;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            RenderSystem.enableBlend();
            if (!this.icon.equals(standRerollIcon.NONE)) {

                if (this.icon.id == getWidgetNumber()){
                    if (this.isSelected) {
                        this.drawSlot4(guiGraphics);
                    } else {
                        this.drawSlot3(guiGraphics);
                    }
                } else {
                    if (this.isSelected) {
                        this.drawSlot2(guiGraphics);
                    } else {
                        this.drawSlot(guiGraphics);
                    }
                }
                this.icon.drawIcon(guiGraphics, this.getX() + 5, this.getY() + 5);
            }
            RenderSystem.disableBlend();
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }

        private void drawSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX(), this.getY(), 133.0f, 63.0f, 26, 26, 256, 256);
        }
        private void drawSlot2(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX()-3, this.getY()-3, 160.0f, 60.0f, 32, 32, 256, 256);
        }

        private void drawSlot3(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX(), this.getY(), 196.0f, 63.0f, 26, 26, 256, 256);
        }
        private void drawSlot4(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX()-3, this.getY()-3, 223.0f, 60.0f, 32, 32, 256, 256);
        }

        private void drawSelection(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX(), this.getY(), 170.0f, 0.0f, 26, 26, 256, 256);
        }
    }

    protected boolean isSurelyHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return p_97772_ >= (double)(p_97768_) && p_97772_ < (double)(p_97768_ + p_97770_) && p_97773_ >= (double)(p_97769_) && p_97773_ < (double)(p_97769_ + p_97771_);
    }
}
