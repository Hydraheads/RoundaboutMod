package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.index.Corpses;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class VisageStoreScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation CORPSE_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/cinderella_gui.png");
    private static final int SPRITE_SHEET_WIDTH = 256;
    private static final int SPRITE_SHEET_HEIGHT = 256;
    private static final int SLOT_AREA = 26;
    private static final int SLOT_PADDING = 5;
    private static final int SLOT_AREA_PADDED = 31;
    private static final int HELP_TIPS_OFFSET_Y = 5;
    private static final int ALL_SLOTS_WIDTH = CorpseBagScreen.corpseIcon.VALUES.length * 31 - 5;
    private CorpseBagScreen.corpseIcon currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;


    private final List<CorpseBagScreen.PoseSlot> slots = Lists.newArrayList();

    public VisageStoreScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }
    public VisageStoreScreen(ItemStack stack) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }

    private ShapeShifts getDefaultSelected() {
        Player pl = Minecraft.getInstance().player;
        if (pl != null){
            return ShapeShifts.getShiftFromByte(((IPlayerEntity)pl).roundabout$getShapeShift());
        }
        return ShapeShifts.PLAYER;
    }



    public int zombies = 0;
    public int skeletons = 0;
    public int spiders = 0;
    public int villagers = 0;
    public int creepers = 0;

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;


        //this.currentlyHovered = CorpseBagScreen.corpseIcon.NONE;
        //for (int i = 0; i < CorpseBagScreen.corpseIcon.VALUES.length; ++i) {
            //CorpseBagScreen.corpseIcon pIcon = CorpseBagScreen.corpseIcon.VALUES[i];
            //this.slots.add(new CorpseBagScreen.PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
        //}
    }


    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        this.switchToHoveredGameMode();
        this.minecraft.setScreen(null);
        return true;
    }
    protected int imageWidth = 176;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        if (this.checkToClose()) {
            return;
        }
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 58;
        int l = this.height / 2 - 31 - 80;
        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 0.0f, 113, 26, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui"), this.width / 2 -2, this.height / 2 - 31 - 76, -1);

        k = this.width / 2 - 79;
        l = this.height / 2 - 31 - 31;
        if (isSurelyHovering(k, l, 19, 24, mouseX, mouseY)) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 65.0f, 19, 24, 256, 256);
        } else {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 40.0f, 19, 24, 256, 256);
        }

        k = this.width / 2 + 55;
        l = this.height / 2 - 31 - 31;
        if (isSurelyHovering(k, l, 19, 24, mouseX, mouseY)) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 22.0f, 65.0f, 19, 24, 256, 256);
        } else {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 22.0f, 40.0f, 19, 24, 256, 256);
        }

        k = this.width / 2;
        l = this.height / 2 - 31 - 31;
        guiGraphics.renderItem(ModItems.JOTARO_MASK.getDefaultInstance(), k, l, k+l * this.imageWidth);

        if (!this.setFirstMousePos) {
            this.firstMouseX = mouseX;
            this.firstMouseY = mouseY;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == mouseX && this.firstMouseY == mouseY;
    }

    private void switchToHoveredGameMode() {
        switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }

    private void switchToHoveredGameMode(Minecraft minecraft, CorpseBagScreen.corpseIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }


            Vec3 vec3d = minecraft.player.getEyePosition(0);
            Vec3 vec3d2 = minecraft.player.getViewVector(0);
            Vec3 vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
            BlockHitResult blockHit = minecraft.player.level().clip(new ClipContext(vec3d, vec3d3,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, minecraft.player));
            Vector3f vc = minecraft.player.position().toVector3f();
            if (blockHit.getType() == HitResult.Type.BLOCK){
                vc = blockHit.getBlockPos().getCenter().toVector3f().add(0,1,0);
            }
            //ModPacketHandler.PACKET_ACCESS.itemContextToServer(pIcon.id,
            //        stack, PacketDataIndex.USE_CORPSE_BAG, vc);
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
    @Override
    public boolean keyPressed(int i, int j, int k) {
        return super.keyPressed(i, j, k);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    protected boolean isSurelyHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return p_97772_ >= (double)(p_97768_) && p_97772_ < (double)(p_97768_ + p_97770_) && p_97773_ >= (double)(p_97769_) && p_97773_ < (double)(p_97769_ + p_97771_);
    }

}

