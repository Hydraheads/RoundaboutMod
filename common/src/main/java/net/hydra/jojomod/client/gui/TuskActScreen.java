package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class TuskActScreen extends Screen implements NoCancelInputScreen {
    static final ResourceLocation MEMORY_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/tusk_acts.png");

    public int currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;


    private final List<PoseSlot> slots = Lists.newArrayList();

    public TuskActScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = (byte)-1;
    }


    public final int[][] positions = {
            {-1,0},
            {0,-31},
            {31,0},
            {-31,0},
            {0,31},
    };




    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;

        this.currentlyHovered = 0;
        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT ) {
            this.currentlyHovered = PT.getAct();
            for (int i = 0; i < 5; ++i) {
                actIcon pIcon = new actIcon(i == 0 ? PT.getAct() : i,positions[i][0], positions[i][1]+31 );
                this.slots.add(new PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
            }

        }

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
        int k = this.width / 2 - 62;
        int l = this.height / 2 - 31 - 39;
        guiGraphics.blit(MEMORY_LOCATION, k, l, 0.0f, 0.0f, 125, 24 , 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);


        Component str = switch(this.currentlyHovered) {
            case 1 -> Component.translatable("roundabout.tusk.act_1");
            case 2 -> Component.translatable("roundabout.tusk.act_2");
            case 3 -> Component.translatable("roundabout.tusk.act_3");
            case 4 -> Component.translatable("roundabout.tusk.act_4");
            default -> Component.translatable("roundabout.tusk.change_acts");
        };
        guiGraphics.drawCenteredString(this.font, str , this.width / 2, this.height / 2 - 31 - 32, -1);

        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;

        for (PoseSlot MobSlot : this.slots) {
            if (true) {MobSlot.render(guiGraphics, i, j, f);}
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon.id);
            if (!bl && MobSlot.isHoveredOrFocused()) {
                this.currentlyHovered = MobSlot.icon.id;
            }
        }

    }

    private void switchToHoveredGameMode() {
        if (this.currentlyHovered > 0) {
            switchToHoveredGameMode(this.minecraft,slots.get(this.currentlyHovered).icon);
        }
    }

    private void switchToHoveredGameMode(Minecraft minecraft, actIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;
        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
            PT.tryIntPower(PowerIndex.POWER_4,true,pIcon.id);
            PT.tryIntPowerPacket(PowerIndex.POWER_4,pIcon.id);
        }


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
            KeyMapping key = KeyInputRegistry.abilityFourKey;
            if (!sameKeyOneX(key, this.minecraft.options)) {
                this.switchToHoveredGameMode();
                this.minecraft.setScreen(null);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft mc = Minecraft.getInstance();
        Player pl = mc.player;
        // Prevent the screen from handling WASD, space, shift, etc.
        if (mc.options.keyUp.matches(keyCode, scanCode) ||
                mc.options.keyDown.matches(keyCode, scanCode) ||
                mc.options.keyLeft.matches(keyCode, scanCode) ||
                mc.options.keyRight.matches(keyCode, scanCode) ||
                mc.options.keyJump.matches(keyCode, scanCode) ||
                mc.options.keyShift.matches(keyCode, scanCode)) {
            return false; // Let these go through to the player
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    class actIcon {
        public int id;
        public int xoff;
        public int yoff;
        public actIcon(int id, int xoff, int yoff) {
            this.id = id;
            this.xoff = xoff;
            this.yoff = yoff;
        }
    }

    public class PoseSlot
            extends AbstractWidget {
        final actIcon icon;
        private boolean isSelected;

        public PoseSlot(actIcon icon, int i, int j) {
            super(i, j, 26, 26, Component.literal(""));
            this.icon = icon;
        }


        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            if (this.icon.xoff != -1) { // hides the middle icon
                guiGraphics.setColor(1f, 1f, 1f, 1f);

                this.drawSlot(guiGraphics);
                guiGraphics.setColor(1f, 1f, 1f, 1f);
                if (this.isSelected) {
                    this.drawSelection(guiGraphics);
                }

                guiGraphics.blit(StandIcons.TUSK_ICONS[this.icon.id],this.getX()+4,this.getY()+4,0,0,18,18,18,18);
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return super.isHoveredOrFocused() || this.isSelected;
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }

        private void drawSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(MEMORY_LOCATION, this.getX(), this.getY(), 144, 26, 26, 26, 256, 256);
        }

        private void drawSelection(GuiGraphics guiGraphics) {
            guiGraphics.blit(MEMORY_LOCATION, this.getX(), this.getY(), 170, 26, 26, 26, 256, 256);
        }
    }
}
