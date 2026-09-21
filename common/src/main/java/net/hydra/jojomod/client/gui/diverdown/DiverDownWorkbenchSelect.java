package net.hydra.jojomod.client.gui.diverdown;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
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

public class DiverDownWorkbenchSelect extends Screen implements NoCancelInputScreen {
    //Bytes for all the workbenches used in the move.
    // NOTE: IF YOU EVER CHANGE THE BYTES FOR THE CRAFTING RECIPES IN PowersDiverDown, BE SURE TO CHANGE THEM HERE TOO!!!
    private static final byte
        CRAFTING_TABLE = 55,
        LOOM = 56,
        STONECUTTER = 57,
        ANVIL = 58,
        SMITHING_TABLE = 59;

    /**
     * Apparently the soft and wet and killer queen UI both change a config
     * in order to have their settings saved. Since this UI immediately opens
     * a workbench UI without saving anything, a private variable is
     * a better fit for this.
     */
    private byte selectedWorkbench = 0;

    //Check out GamemodeSwitcherScreen
    static final private ResourceLocation WORKBENCH_SELECT_GUI = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/diver_gui.png");
    private WorkbenchType currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    public ItemStack arrow = ItemStack.EMPTY;

    private final List<WorkbenchSlot> slots = Lists.newArrayList();

    public DiverDownWorkbenchSelect() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }

    public DiverDownWorkbenchSelect(ItemStack arrow) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
        this.arrow = arrow;
    }

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;


        this.currentlyHovered = WorkbenchType.NONE;
            for (int i = 0; i < WorkbenchType.VALUES.length; ++i) {
                WorkbenchType workbench = WorkbenchType.VALUES[i];
                this.slots.add(new WorkbenchSlot(workbench, this.width / 2 + workbench.xoff - 18, this.height / 2 + workbench.yoff - 44));
            }
    }
    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityFourKey)) {
            this.selectHoveredWorkbench();
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
        if ($$2 == 0 && (this.currentlyHovered != WorkbenchType.NONE)) {
            this.selectHoveredWorkbench();
            this.minecraft.setScreen(null);
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
        int k = this.width / 2 - 135/2;
        int l = this.height / 2 - 103;
        guiGraphics.blit(WORKBENCH_SELECT_GUI, k, l, 117.0f, 5.0f, 135, 38, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);
        if (this.currentlyHovered != null) {
            int textY = l + (38 - this.font.lineHeight) / 2;
            Component name = this.currentlyHovered.getName();
            boolean isUnlocked = true;
            int reqLevel = 0;

            Player player = Minecraft.getInstance().player;
            if (player != null && ((StandUser) player).roundabout$getStandPowers() instanceof PowersDiverDown powers) {
                reqLevel = this.currentlyHovered.getRequiredLevel(powers);
                isUnlocked = powers.canExecuteMoveWithLevel(reqLevel);
                if (!isUnlocked) {
                    name = Component.translatable("ability.roundabout.locked");
                }
            }

            guiGraphics.drawCenteredString(this.font, name, this.width / 2, textY + 8, -1);

            // for the tooltip
            if (this.currentlyHovered != WorkbenchType.NONE) {
                List<Component> compList = Lists.newArrayList();
                if (!isUnlocked) {
                    compList.add(Component.translatable("ability.roundabout.locked.desc", reqLevel));
                } else {
                    String[] descLines = splitIntoLine(this.currentlyHovered.desc.getString(), 30);
                    for (String s : descLines) {
                        compList.add(Component.literal(s));
                    }
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
        for (WorkbenchSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            caughtSomething = true;

            if (MobSlot.icon != WorkbenchType.NONE) {
                setSelectedWorkbench(MobSlot.icon.id);
                if (this.currentlyHovered == WorkbenchType.NONE) {
                    SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
                    soundmanager.play(SimpleSoundInstance.forUI(ModSounds.DIVER_DOWN_UI_SELECT_EVENT, (float) (0.95 + (Math.random() * 0.1F))));
                }
            }
            this.currentlyHovered = MobSlot.icon;
        }

        if (!caughtSomething){
            this.currentlyHovered = WorkbenchType.NONE;
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
    private void selectHoveredWorkbench() {
        selectHoveredWorkbench(this.minecraft, this.currentlyHovered);
    }

    private void selectHoveredWorkbench(Minecraft minecraft, WorkbenchType workbench) {
        if (minecraft.gameMode == null || minecraft.player == null || workbench == null || workbench == WorkbenchType.NONE) {
            return;
        }

        if (((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersDiverDown powers) {
            if (!powers.canExecuteMoveWithLevel(workbench.getRequiredLevel(powers))) {
                return;
            }

            powers.tryIntPower(PowersDiverDown.ACCESS_WORKBENCH, true, workbench.id);
            powers.tryIntPowerPacket(PowersDiverDown.ACCESS_WORKBENCH, workbench.id);
        }
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
     * WorkbenchType is what gets and places the icons for the UI. 
     * The first translatable is for the name, while the second
     * is for the description box.
     */
    public enum WorkbenchType {
        CRAFTING_TABLE_ID(Component.translatable("roundabout.diver_workbench.crafting"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/workbench_icons/crafting_table.png"),CRAFTING_TABLE,0,-11, Component.translatable("roundabout.diver_workbench.crafting.desc")),
        LOOM_ID(Component.translatable("roundabout.diver_workbench.loom"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/workbench_icons/loom.png"),LOOM,40,18, Component.translatable("roundabout.diver_workbench.loom.desc")),
        STONECUTTER_ID(Component.translatable("roundabout.diver_workbench.stonecutter"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/workbench_icons/stonecutter.png"),STONECUTTER,25,65, Component.translatable("roundabout.diver_workbench.stonecutter.desc")),
        SMITHING_TABLE_ID(Component.translatable("roundabout.diver_workbench.smithing"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/diver_down/workbench_icons/smithing_table.png"),SMITHING_TABLE,-25,65, Component.translatable("roundabout.diver_workbench.smithing.desc")),
        ANVIL_ID(Component.translatable("roundabout.diver_workbench.anvil"), new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/diver_down/workbench_icons/anvil.png"),ANVIL,-40,18, Component.translatable("roundabout.diver_workbench.anvil.desc")),

        NONE(Component.translatable("roundabout.diver_workbench.none"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/plunder_icons/main_stand.png"),(byte)0,0,75, Component.translatable("roundabout.stand_switch.main.desc"));

        protected static final WorkbenchType[] VALUES;
        protected static final int ICON_TOP_LEFT = 5;
        final Component name;
        final Component desc;
        final ResourceLocation rl;
        final byte id;

        final int xoff;
        final int yoff;

        public int getRequiredLevel(PowersDiverDown powers) {
            return switch (this) {
                case CRAFTING_TABLE_ID -> powers.getWorkbenchLevel(); // Or whatever minimum level you want for base crafting table
                case LOOM_ID -> powers.getLoomLevel();
                case STONECUTTER_ID -> powers.getStonecutterLevel();
                case SMITHING_TABLE_ID -> powers.getSmithingTableLevel();
                case ANVIL_ID -> powers.getAnvilLevel();
                default -> 0;
            };
        }

        private WorkbenchType(Component component, ResourceLocation rl, byte id, int xoff, int yoff, Component desc) {
            this.name = component;
            this.rl = rl;
            this.id = id;
            this.xoff = xoff;
            this.yoff = yoff;
            this.desc = desc;
        }

        void drawIcon(GuiGraphics guiGraphics, int i, int j, boolean isUnlocked) {
            ResourceLocation texture = isUnlocked ? this.rl : StandIcons.LOCKED;
            guiGraphics.blit(texture, i-1, j-1, 0, 0, 24, 24, 24, 24);
        }

        Component getName() {
            return this.name;
        }


        static {
            VALUES = new WorkbenchType[]{
                    CRAFTING_TABLE_ID,
                    LOOM_ID,
                    STONECUTTER_ID,
                    ANVIL_ID,
                    SMITHING_TABLE_ID
            };
        }
    }

    public class WorkbenchSlot
            extends AbstractWidget {
        final WorkbenchType icon;
        private boolean isSelected;

        public WorkbenchSlot(WorkbenchType workbench, int i, int j) {
            super(i, j, 36, 36, workbench.getName());
            this.icon = workbench;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            RenderSystem.enableBlend();
            if (!this.icon.equals(WorkbenchType.NONE)) {
                if (this.isSelected) {
                    this.drawSlot2(guiGraphics);
                } else {
                    this.drawSlot(guiGraphics);
                }
                boolean isUnlocked = true;
                Player player = Minecraft.getInstance().player;
                if (player != null && ((StandUser) player).roundabout$getStandPowers() instanceof PowersDiverDown powers) {
                    isUnlocked = powers.canExecuteMoveWithLevel(this.icon.getRequiredLevel(powers));
                }
                this.icon.drawIcon(guiGraphics, this.getX() + 7, this.getY() + 6, isUnlocked);
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
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX(), this.getY(), 36, 36, 202.0f, 52.0f, 22, 22, 256, 256);
        }
        private void drawSlot2(GuiGraphics guiGraphics) {
            guiGraphics.blit(WORKBENCH_SELECT_GUI, this.getX()-3, this.getY()-3, 42, 42, 200.0f, 76.0f, 26, 26, 256, 256);
        }
    }

    protected boolean isSurelyHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return p_97772_ >= (double)(p_97768_) && p_97772_ < (double)(p_97768_ + p_97770_) && p_97773_ >= (double)(p_97769_) && p_97773_ < (double)(p_97769_ + p_97771_);
    }

    /**
     * getter and setter methods. Much like how the selectedWorkbench
     * variable was simplified to a byte, the code here can also be
     * simplified, as there is no need to save anything.
     */
    public void setSelectedWorkbench(byte id){
        this.selectedWorkbench = id;
    }
}
