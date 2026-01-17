package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.index.AnubisMemory;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MemoryRecordScreen extends Screen implements NoCancelInputScreen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation MEMORY_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/anubis_memory.png");

    public byte currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    public final ItemStack stack;

    private final List<PoseSlot> slots = Lists.newArrayList();

    public boolean recording = false;
    public MemoryRecordScreen(boolean recording) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = (byte)-1;
        stack = null;
        this.recording = recording;
    }


    public final int[][] positions = {
            {0,31},
            {31,31},
            {31,0},
            {31,-31},
            {0,-31},
            {-31,-31},
            {-31,0},
            {-31,31}
    };



    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;

        this.currentlyHovered = (byte)-1;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
            List<AnubisMemory> memories = PA.memories;
            boolean bypass = pl.isCreative() || (!SU.roundabout$getStandDisc().isEmpty() && SU.roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem);
            final int count = bypass ? 8 : ((IPlayerEntity)pl).roundabout$getStandLevel();
            for (int i = 0; i < count; ++i) {
                AnubisMemory memory = memories.get(i);
                corpseIcon pIcon = new corpseIcon(memory.item,(byte)i,positions[i][0], positions[i][1]+31 );
                this.slots.add(new PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
            }
            corpseIcon pIcon = new corpseIcon(ModItems.ANUBIS_ITEM,(byte)8,0, 31 );
            this.slots.add(new PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));

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

        Component str = Component.translatable("roundabout.anubis.playback_title");
        if (this.recording) {str = Component.translatable("roundabout.anubis.memory_title");}
        guiGraphics.drawCenteredString(this.font, str , this.width / 2, this.height / 2 - 31 - 32, -1);

        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
        for (PoseSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon.id);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            this.currentlyHovered = MobSlot.icon.id;

            Player player = Minecraft.getInstance().player;
            StandUser SU = (StandUser) player;
            if (SU.roundabout$getStandPowers() != null) {
                if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
                    PA.convertToVisual(currentlyHovered == 8 ? -1 : currentlyHovered,    PA.memories.get( (currentlyHovered == 8 || currentlyHovered == -1) ? 0 : currentlyHovered).moments);
                }
            }


        }

    }

    private void switchToHoveredGameMode() {
        if (this.currentlyHovered != -1 && this.currentlyHovered != 8) {
            switchToHoveredGameMode(this.minecraft,slots.get(this.currentlyHovered).icon);
        }
    }

    private void switchToHoveredGameMode(Minecraft minecraft, corpseIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
            if (this.recording) {
                PA.recordMemory(pIcon.id);
            } else {
                PA.playbackMemory(pIcon.id);
            }
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
            KeyMapping key = KeyInputRegistry.abilityTwoKey;
            if (this.recording) {key = KeyInputRegistry.abilityFourKey;}
            if (!sameKeyOneX(key, this.minecraft.options)) {
                this.switchToHoveredGameMode();
                this.minecraft.setScreen(null);
                return true;
            }
        }
        Options options = Minecraft.getInstance().options;
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft mc = Minecraft.getInstance();
        Player pl = mc.player;
        StandUser SU = ((StandUser) pl);
        // Prevent the screen from handling WASD, space, shift, etc.
        if (mc.options.keyUp.matches(keyCode, scanCode) ||
                mc.options.keyDown.matches(keyCode, scanCode) ||
                mc.options.keyLeft.matches(keyCode, scanCode) ||
                mc.options.keyRight.matches(keyCode, scanCode) ||
                mc.options.keyJump.matches(keyCode, scanCode) ||
                mc.options.keyShift.matches(keyCode, scanCode)) {
            return false; // Let these go through to the player
        }

        if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA && this.recording) {
            List<AnubisMemory> memories = PA.memories;
            if (this.currentlyHovered == -1 || this.currentlyHovered == 8) {return false;}
            for (int i=0;i<mc.options.keyHotbarSlots.length;i++) {
                KeyMapping key = mc.options.keyHotbarSlots[i];
                if (key.isDown()) {
                    ItemStack item = pl.getInventory().getItem(i);
                    if (!item.equals(ItemStack.EMPTY)) {
                        memories.get(currentlyHovered).item = item.getItem();
                        this.slots.get(currentlyHovered).icon.item = memories.get(currentlyHovered).item;
                        PA.SaveMemories();
                        return true;
                    }
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    class corpseIcon {
        public Item item;
        public byte id;
        public int xoff;
        public int yoff;
        public corpseIcon(Item item, byte id, int xoff, int yoff) {
            this.item = item;
            this.id = id;
            this.xoff = xoff;
            this.yoff = yoff;
        }
        public int getMode() {
            Player p = Minecraft.getInstance().player;
            StandUser SU = (StandUser) p;
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
                if(!PA.memories.isEmpty()) {
                    AnubisMemory memory = PA.memories.get(this.id);
                    if (memory != null) {
                        return memory.memory_type;
                    }
                }
            }
            return 0;
        }
    }

    public class PoseSlot
            extends AbstractWidget {
        final corpseIcon icon;
        private boolean isSelected;

        public PoseSlot(corpseIcon icon, int i, int j) {
            super(i, j, 26, 26, Component.literal(""));
            this.icon = icon;
        }


        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            if (!(this.icon.id == (byte)8) ) {
                guiGraphics.setColor(1f, 1f, 1f, 1f);

                this.drawSlot(guiGraphics,this.icon.getMode());
                guiGraphics.setColor(1f, 1f, 1f, 1f);
                guiGraphics.renderItem(this.icon.item.getDefaultInstance(),this.getX() + 5, this.getY() + 5);
                if (this.isSelected) {
                    this.drawSelection(guiGraphics,this.icon.getMode());
                }
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

        private void drawSlot(GuiGraphics guiGraphics, int i) {
            guiGraphics.blit(MEMORY_LOCATION, this.getX(), this.getY(), 144, i*26, 26, 26, 256, 256);
        }

        private void drawSelection(GuiGraphics guiGraphics, int i) {
            guiGraphics.blit(MEMORY_LOCATION, this.getX(), this.getY(), 170, i*26, 26, 26, 256, 256);
        }
    }
}
