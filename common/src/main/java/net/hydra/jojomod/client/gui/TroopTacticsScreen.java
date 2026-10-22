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
        STOP = 0,
        ADVANCE = 1,
        FIRE = 2,
        HUNT = 3,
        WARD = 4,
        TANK = 5,
        HELICOPTER = 6,
        MISSILE = 7,
        SOLDIER = 8;



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

        int offsetCenter = 30;
        int offsetY = 10;
        int centerX = this.width / 2-10;
        int centerY = this.height / 2-10;

        this.slots.add(new SwitchSelect(STOP, centerX, centerY-25,
                Component.translatable("roundabout.troop_tactics.stop")));

        this.slots.add(new SwitchSelect(ADVANCE, centerX-(offsetCenter*2), centerY-12,
                Component.translatable("roundabout.troop_tactics.advance")));
        this.slots.add(new SwitchSelect(FIRE, centerX-offsetCenter, centerY-18,
                Component.translatable("roundabout.troop_tactics.fire")));
        this.slots.add(new SwitchSelect(HUNT, centerX-(offsetCenter*2)+offsetY, centerY+16,
                Component.translatable("roundabout.troop_tactics.hunt")));
        this.slots.add(new SwitchSelect(WARD, centerX-offsetCenter+offsetY, centerY+10,
                Component.translatable("roundabout.troop_tactics.ward")));

        this.slots.add(new SwitchSelect(TANK, centerX+(offsetCenter*2), centerY-12,
                Component.translatable("roundabout.troop_tactics.tank")));
        this.slots.add(new SwitchSelect(HELICOPTER, centerX+offsetCenter, centerY-18,
                Component.translatable("roundabout.troop_tactics.helicopter")));
        this.slots.add(new SwitchSelect(MISSILE, centerX+(offsetCenter*2)-offsetY, centerY+16,
                Component.translatable("roundabout.troop_tactics.missile")));
        this.slots.add(new SwitchSelect(SOLDIER, centerX+offsetCenter-offsetY, centerY+10,
                Component.translatable("roundabout.troop_tactics.soldier")));

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

        Component str = Component.translatable("roundabout.bad_company.troop_tactics");

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
        if (this.currentlyHovered != -1) {
            Component text = slots.get(currentlyHovered).name;

            guiGraphics.drawCenteredString(this.font, text, this.width / 2, this.height / 2 - 31 + 90, -1);
        }
    }


    private void selectCurrentHoveredTroop(boolean safeSelect) {
        if (currentlyHovered != -1) {
            if (safeSelect && lastChangedConfig == currentlyHovered) {
                return;
            }
            if (Minecraft.getInstance().player != null && ((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers() instanceof PowersBadCompany PBC) {
                //PBC.selectedTroop(currentlyHovered);
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
            if (isSelected) { renderHover(guiGraphics); }
            renderNumber(guiGraphics);
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }


        private void renderSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX(), this.getY(), isHoveredOrFocused() ? 92 : 72, 0, 20, 20, 192, 192);
        }

        private void renderIcon(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX()+2, this.getY()+2, context*16, 48, 16, 16, 192, 192);
        }

        private void renderHover(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX()-4, this.getY()-4, 132, 0, 24, 24, 192, 192);
        }

        private void renderNumber(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX()-2, this.getY()+15, 7*context, 80, 7, 7, 192, 192);
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
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityTwoKey)) {
            this.exitTroopSelect();

            this.minecraft.options.keyUse.setDown(false);
        }
        return false;
    }
}
