package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersBadCompany;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TroopTacticsScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation TROOP_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/troop_chooser.png");

    private static final int
            TROOP = 0,
            BUNDLE = 1,
            HELICOPTER = 2,
            TANK = 3,
            MINE = 4;



    public TroopTacticsScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    public boolean isPauseScreen() { return false;}

    public int currentlyHovered;
    public int lastChangedConfig = -1;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;
    private final List<SwitchSelect> slots = Lists.newArrayList();

    @Override
    protected void init() {
        super.init();
        zHeld = true;

        currentlyHovered = -1;
        lastChangedConfig = -1;

        int offsetCenter = 20;
        int centerX = this.width / 2-10;
        int centerY = this.height / 2-10;


        /*this.slots.add(new SwitchSelect(TROOP, centerX-offsetCenter, centerY-offsetCenter + Yoffset,
                Component.translatable("roundabout.troop_select.troop")));
        this.slots.add(new SwitchSelect(BUNDLE, centerX+offsetCenter, centerY-offsetCenter + Yoffset,
                Component.translatable("roundabout.troop_select.bundle")));
        this.slots.add(new SwitchSelect(HELICOPTER, centerX-offsetCenter-12, centerY+offsetCenter + Yoffset-6,
                Component.translatable("roundabout.troop_select.helicopter")));
        this.slots.add(new SwitchSelect(TANK, centerX+offsetCenter+12, centerY+offsetCenter + Yoffset-6,
                Component.translatable("roundabout.troop_select.tank")));
        this.slots.add(new SwitchSelect(MINE, centerX, centerY+offsetCenter + Yoffset+12,
                Component.translatable("roundabout.troop_select.mine")));
        */
    }



    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if ($$2 == 0) {
            selectCurrentHoveredTroop(false);
        }else {
            this.exitTroopSelect();
        }

        return super.mouseReleased($$0, $$1, $$2);
    }

    public void exitTroopSelect() {
        selectCurrentHoveredTroop(true);
        this.minecraft.setScreen(null);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 52;
        int l = this.height / 2 - 65;
        guiGraphics.blit(TROOP_CHOOSER_LOCATION, k, l-6, 0.0f, 108.0f, 105, 20, 192, 192);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);

        Component str = Component.translatable("roundabout.bad_company.troop_select");

        guiGraphics.drawCenteredString(this.font, str , this.width / 2, l, -1);

        int lastState = currentlyHovered;

        currentlyHovered = -1;
        for (SwitchSelect slot : slots) {
            slot.render(guiGraphics, i, j, f);
            if (slot.isHoveredOrFocused()) {
                currentlyHovered = slot.context;
            }
        }

        if (currentlyHovered != -1 && lastState != currentlyHovered) {
            SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
            soundmanager.play(SimpleSoundInstance.forUI(ModSounds.BC_UI_SELECT_EVENT, (float) (0.95 + (Math.random() * 0.1F))));
        }


    }



    private void switchToHoveredGameMode() {
        selectCurrentHoveredTroop(true);
    }

    private void selectCurrentHoveredTroop(boolean safeSelect) {
        if (currentlyHovered != -1) {
            if (safeSelect && lastChangedConfig == currentlyHovered) {
                return;
            }
            if (Minecraft.getInstance().player != null && ((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers() instanceof PowersBadCompany PBC) {
                PBC.selectedTroop(currentlyHovered);
                lastChangedConfig = currentlyHovered;
            }
        }
    }

    public class SwitchSelect extends AbstractWidget {
        public int context;
        public int xoff;
        public int yoff;
        private boolean isSelected;
        final Component name;

        public SwitchSelect(int context, int xoff, int yoff, Component name) {
            super(xoff, yoff, 20, 20, name);
            this.context = context;
            this.xoff = xoff;
            this.yoff = yoff;
            this.name = name;
        }

        public Component getName() {return name; }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
            renderSlot(guiGraphics);
            renderIcon(guiGraphics);
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }


        private void renderSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX(), this.getY(), isHoveredOrFocused() ? 96 : 72, 0, 20, 20, 192, 192);
        }

        private void renderIcon(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX()+2, this.getY()+2, context*18, 24, 18, 18, 192, 192);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    /*public boolean sameKeyOneX(KeyMapping key1, Options options){
        return (InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)key1).roundabout$justTellMeTheKey().getValue())
                || (key1.same(options.keyLoadHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keyLoadHotbarActivator).roundabout$justTellMeTheKey().getValue()))
                || (key1.same(options.keySaveHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keySaveHotbarActivator).roundabout$justTellMeTheKey().getValue()))
        );
    }*/


    public boolean roundabout$sameKeyOne(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.minecraft.options.keyLoadHotbarActivator) && this.minecraft.options.keyLoadHotbarActivator.isDown())
                || (key1.same(this.minecraft.options.keySaveHotbarActivator) && this.minecraft.options.keySaveHotbarActivator.isDown())
        );
    }

    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityOneKey)) {
            this.exitTroopSelect();

            this.minecraft.options.keyUse.setDown(false);
        }
        return false;
    }
}
