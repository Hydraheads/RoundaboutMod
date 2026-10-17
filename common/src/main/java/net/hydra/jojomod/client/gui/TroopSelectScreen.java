package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.Corpses;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ClientToServerPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.networking.message.api.ModMessageEvents;
import org.joml.Vector3f;

import java.util.List;

public class TroopSelectScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation TROOP_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/troop_chooser.png");

    private static final int
            TROOP = 0,
            BUNDLE = 1,
            HELICOPTER = 2,
            TANK = 3;



    public TroopSelectScreen() {
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

        int offsetCenter = 10;
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int Yoffset = 20;

        this.slots.add(new SwitchSelect(TROOP, centerX-offsetCenter, centerY-offsetCenter + Yoffset,
                Component.translatable("roundabout.troop_select.troop")));
        this.slots.add(new SwitchSelect(BUNDLE, centerX+offsetCenter, centerY-offsetCenter + Yoffset,
                Component.translatable("roundabout.troop_select.bundle")));
        this.slots.add(new SwitchSelect(HELICOPTER, centerX-offsetCenter, centerY+offsetCenter + Yoffset,
                Component.translatable("roundabout.troop_select.helicopter")));
        this.slots.add(new SwitchSelect(TANK, centerX+offsetCenter, centerY+offsetCenter + Yoffset,
                Component.translatable("roundabout.troop_select.tank")));

    }



    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        this.switchToHoveredGameMode();
        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (this.checkToClose()) {
            return;
        }

        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 52;
        int l = this.height / 2 - 31 - 22 - 10;
        guiGraphics.blit(TROOP_CHOOSER_LOCATION, k, l, 0.0f, 104.0f, 105, 24, 128, 128);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);

        Component str = Component.translatable("roundabout.bad_company.troop_select");

        guiGraphics.drawCenteredString(this.font, str , this.width / 2, this.height / 2 - 31-10-10, -1);

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
            soundmanager.play(SimpleSoundInstance.forUI(ModSounds.KILLER_QUEEN_DETONATE_EVENT, (float) (0.95 + (Math.random() * 0.1F))));
        }


    }

    private boolean checkToClose() {
        if (minecraft != null) {
            if (sameKeyOneX(KeyInputRegistry.abilityTwoKey, this.minecraft.options)) {
                this.switchToHoveredGameMode();
                this.minecraft.setScreen(null);
                return true;
            }
        }
        return false;
    }

    private void switchToHoveredGameMode() {}

    public class SwitchSelect extends AbstractWidget {
        public int context;
        public int xoff;
        public int yoff;
        private boolean isSelected;
        final Component name;

        public SwitchSelect(int context, int xoff, int yoff, Component name) {
            super(xoff, yoff, 26, 26, name);
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
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX()-12, this.getY()-12, isHoveredOrFocused() ? 24 : 0, 0, 24, 24, 128, 128);
        }

        private void renderIcon(GuiGraphics guiGraphics) {
            guiGraphics.blit(TROOP_CHOOSER_LOCATION, this.getX()+4-12, this.getY()+4-12, context*18, 31, 18, 18, 128, 128);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    public boolean sameKeyOneX(KeyMapping key1, Options options){
        return (InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)key1).roundabout$justTellMeTheKey().getValue())
                || (key1.same(options.keyLoadHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keyLoadHotbarActivator).roundabout$justTellMeTheKey().getValue()))
                || (key1.same(options.keySaveHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keySaveHotbarActivator).roundabout$justTellMeTheKey().getValue()))
        );
    }
}
