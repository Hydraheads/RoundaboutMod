package net.hydra.jojomod.client.gui.diverdown;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
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

public class DiverDownAfflictionSelection extends Screen implements NoCancelInputScreen {
    //Bytes for all the affliction used in the move.
    // NOTE: IF YOU EVER CHANGE THE BYTES FOR THE AFFLICTIONS IN PowersDiverDown, BE SURE TO CHANGE THEM HERE TOO!!!
    private static final byte
            DISGUISE = 71,
            EMBED_POTION = 72,
            DIVER_LEGS = 73,
            EFFECT_CURE = 74,
            COUNTER = 75,
            RIBCAGE_TRAP = 76,
            BONE_BOMB = 77,
            SPRING_LEGS = 78;

    /**
     * Apparently the soft and wet and killer queen UI both change a config
     * in order to have their settings saved. Since this UI immediately opens
     * a affliction UI without saving anything, a private variable is
     * a better fit for this.
     */
    private byte selectedAffliction = 0;

    //Check out GamemodeSwitcherScreen
    static final ResourceLocation WORKBENCH_SELECT_GUI = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/workbench_icons/placeholder.png");
    private AfflictionType currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    public ItemStack arrow = ItemStack.EMPTY;

    private final List<AfflictionSlot> slots = Lists.newArrayList();

    public DiverDownAfflictionSelection() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }

    public DiverDownAfflictionSelection(ItemStack arrow) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
        this.arrow = arrow;
    }

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;


        this.currentlyHovered = AfflictionType.NONE;
            for (int i = 0; i < AfflictionType.VALUES.length; ++i) {
                AfflictionType affliction = AfflictionType.VALUES[i];
                this.slots.add(new AfflictionSlot(affliction, this.width / 2 + affliction.xoff - 13, this.height / 2 + affliction.yoff - 44));
            }
    }
    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityTwoKey)) {
            boolean isDisguise = this.currentlyHovered == AfflictionType.DISGUISE_ID;
            this.selectHoveredAffliction();
            if (!isDisguise) {
                this.minecraft.setScreen(null);
            }
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

    /**    (non-Javadoc)
     * mouseReleased indicates when a GUI element is clicked
     * 
     * @param $$0 is an indicator for the x coordinate of the mouse
     * @param $$1 is an indicator for the y coordinate of the mouse
     * @param $$2 indicates what mouse button was clicked. 0 is for left click, 1 is for right.
     * @see net.minecraft.client.gui.components.events.ContainerEventHandler#mouseReleased(double, double, int)
     */
    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if ($$2 == 0 && (this.currentlyHovered != AfflictionType.NONE)) {
            boolean isDisguise = this.currentlyHovered == AfflictionType.DISGUISE_ID;
            this.selectHoveredAffliction();
            if (!isDisguise) {
                this.minecraft.setScreen(null);
            }
            this.minecraft.options.keyUse.setDown(false);
            return true;
        }

        return false;
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
        guiGraphics.blit(WORKBENCH_SELECT_GUI, k, l, 0.0f, 63.0f, 125, 22, 256, 256);
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
        for (AfflictionSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            caughtSomething = true;

            if (MobSlot.icon != AfflictionType.NONE) {
                setSelectedAffliction(MobSlot.icon.id);
                if (this.currentlyHovered == AfflictionType.NONE) {
                    SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
                    soundmanager.play(SimpleSoundInstance.forUI(ModSounds.DIVER_DOWN_UI_SELECT_EVENT, (float) (0.95 + (Math.random() * 0.1F))));
                }
            }
            this.currentlyHovered = MobSlot.icon;
        }

        if (!caughtSomething){
            this.currentlyHovered = AfflictionType.NONE;
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

    /**
     * I should probably delete this since i don't think it does anything here. hmm...
     * 
     * or i could also just forget about it lol. gl to anybody copying this as a reference.
     * 
     * UPDATE: I read the code some more, it seems like this is the code that updates what thing you select, maybe.
     * I'll have to read it some more, but basically this updates, the overload selects. again, maybe.
     */
    private void selectHoveredAffliction() {
        selectHoveredAffliction(this.minecraft, this.currentlyHovered);
    }

    private void selectHoveredAffliction(Minecraft minecraft, AfflictionType affliction) {
        if (minecraft.gameMode == null || minecraft.player == null || affliction == null || affliction == AfflictionType.NONE) {
            return;
        }

        if (((StandUser) minecraft.player).roundabout$getStandPowers()instanceof PowersDiverDown powers) {
            powers.tryIntPower(PowersDiverDown.ACCESS_AFFLICTIONS, true, affliction.id);
            powers.tryIntPowerPacket(PowersDiverDown.ACCESS_AFFLICTIONS, affliction.id);
        }
        /**
         * test to see if the selection even works in the first place.
         * comment this out when unneeded anymore :thumbsup:
         */
        //minecraft.player.displayClientMessage(Component.literal("Selection menu works :thumbsup:"),false);
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

    /**
     * AfflictionType is what gets and places the icons for the UI. 
     * The first translatable is for the name, while the second
     * is for the description box.
     */
    public enum AfflictionType {
        DIVER_LEGS_ID(Component.translatable("roundabout.diver_affliction.crafting"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/affliction_icons/diver_legs.png"),DIVER_LEGS,-43,31, Component.translatable("roundabout.diver_affliction.crafting.desc")),
        EFFECT_CURE_ID(Component.translatable("roundabout.diver_affliction.loom"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/affliction_icons/placeholder.png"),EFFECT_CURE,-28,1, Component.translatable("roundabout.diver_affliction.loom.desc")),
        COUNTER_ID(Component.translatable("roundabout.diver_affliction.stonecutter"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/affliction_icons/placeholder.png"),COUNTER,43,31, Component.translatable("roundabout.diver_affliction.stonecutter.desc")),
        DISGUISE_ID(Component.translatable("roundabout.diver_affliction.anvil"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/affliction_icons/disguise.png"),DISGUISE,0,-16, Component.translatable("roundabout.diver_affliction.anvil.desc")),
        RIBCAGE_TRAP_ID(Component.translatable("roundabout.diver_affliction.smithing"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/affliction_icons/placeholder.png"),RIBCAGE_TRAP,28,1, Component.translatable("roundabout.diver_affliction.smithing.desc")),
        BONE_BOMB_ID(Component.translatable("roundabout.diver_affliction.stonecutter"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/diver_down/affliction_icons/placeholder.png"),BONE_BOMB,28,61, Component.translatable("roundabout.diver_affliction.stonecutter.desc")),
        SPRING_LEGS_ID(Component.translatable("roundabout.diver_affliction.anvil"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/diver_down/affliction_icons/placeholder.png"),SPRING_LEGS,-28,61, Component.translatable("roundabout.diver_affliction.anvil.desc")),
        EMBED_POTION_ID(Component.translatable("roundabout.diver_affliction.smithing"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/diver_down/affliction_icons/effects.png"),EMBED_POTION,0,78, Component.translatable("roundabout.diver_affliction.smithing.desc")),
        NONE(Component.translatable("roundabout.diver_affliction.none"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/main_stand.png"),(byte)0,0,75, Component.translatable("roundabout.stand_switch.main.desc"));

        protected static final AfflictionType[] VALUES;
        protected static final int ICON_TOP_LEFT = 5;
        final Component name;
        final Component desc;
        final ResourceLocation rl;
        final byte id;

        final int xoff;
        final int yoff;

        private AfflictionType(Component component, ResourceLocation rl, byte id, int xoff, int yoff, Component desc) {
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
            VALUES = new AfflictionType[]{
                    DIVER_LEGS_ID,
                    EFFECT_CURE_ID,
                    COUNTER_ID,
                    DISGUISE_ID,
                    RIBCAGE_TRAP_ID,
                    BONE_BOMB_ID,
                    SPRING_LEGS_ID,
                    EMBED_POTION_ID
            };
        }
    }

    public class AfflictionSlot
            extends AbstractWidget {
        final AfflictionType icon;
        private boolean isSelected;

        public AfflictionSlot(AfflictionType affliction, int i, int j) {
            super(i, j, 26, 26, affliction.getName());
            this.icon = affliction;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            RenderSystem.enableBlend();
            if (!this.icon.equals(AfflictionType.NONE)) {

                if (this.icon.id == getSelectedAffliction()){
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
                this.icon.drawIcon(guiGraphics, this.getX() + 4, this.getY() + 4);
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
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX(), this.getY(), 133.0f, 63.0f, 26, 26, 256, 256);
        }
        private void drawSlot2(GuiGraphics guiGraphics) {
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX()-3, this.getY()-3, 160.0f, 60.0f, 32, 32, 256, 256);
        }

        private void drawSlot3(GuiGraphics guiGraphics) {
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX(), this.getY(), 196.0f, 63.0f, 26, 26, 256, 256);
        }
        private void drawSlot4(GuiGraphics guiGraphics) {
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX()-3, this.getY()-3, 223.0f, 60.0f, 32, 32, 256, 256);
        }

        private void drawSelection(GuiGraphics guiGraphics) {
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX(), this.getY(), 170.0f, 0.0f, 26, 26, 256, 256);
        }
    }

    protected boolean isSurelyHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return p_97772_ >= (double)(p_97768_) && p_97772_ < (double)(p_97768_ + p_97770_) && p_97773_ >= (double)(p_97769_) && p_97773_ < (double)(p_97769_ + p_97771_);
    }

    /**
     * getter and setter methods. Much like how the selectedAffliction
     * variable was simplified to a byte, the code here can also be
     * simplified, as there is no need to save anything.
     */
    public void setSelectedAffliction(byte id){
        this.selectedAffliction = id;
    }
    public byte getSelectedAffliction(){
        return this.selectedAffliction;
    }
}
