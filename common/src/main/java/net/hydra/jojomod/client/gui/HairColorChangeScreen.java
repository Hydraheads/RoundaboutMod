package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.VisageStoreEntry;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.networking.ClientToServerPackets;
import net.minecraft.ChatFormatting;
import net.minecraft.client.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zetalasis.networking.message.api.ModMessageEvents;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HairColorChangeScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation CORPSE_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/cinderella_gui.png");

    public static final ResourceLocation POWER_INVENTORY_GEAR_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/stand_user_settings.png");
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;


    public HairColorChangeScreen() {
        super(GameNarrator.NO_TITLE);
    }


    public int page = 0;
    public boolean costsEmeralds = false;

    Player pl;

    public float hairColorX = 1;
    public float hairColorY = 1;
    public float hairColorZ = 1;
    public float baseHairColorX = 1;
    public float baseHairColorY = 1;
    public float baseHairColorZ = 1;

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        pl = Minecraft.getInstance().player;
        if (pl != null){
            hairColorX = ((IPlayerEntity)pl).rdbt$getHairColorX();
            baseHairColorX = hairColorX;
            hairColorY = ((IPlayerEntity)pl).rdbt$getHairColorY();
            baseHairColorY = hairColorY;
            hairColorZ = ((IPlayerEntity)pl).rdbt$getHairColorZ();
            baseHairColorZ = hairColorZ;
        }
        this.page = 0;

    }

    public int getLastPageNumber() {
        List<VisageStoreEntry> list = ModItems.getVisageStore();
        return list.get(list.size()-1).page;
    }


    public void turnPage(boolean left){
        List<VisageStoreEntry> list = ModItems.getVisageStore();
        if (left){
            this.page--;
            if (page < 0){
                page = getLastPageNumber();
            }
        } else {
            this.page++;
            if (page > getLastPageNumber()){
                page = 0;
            }
        }

        SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
        soundmanager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
    public List<VisageStoreEntry> getPage(){
        List<VisageStoreEntry> list = ModItems.getVisageStore();
        List<VisageStoreEntry> list2 = new ArrayList<>();
        for (VisageStoreEntry value : list) {
            if (value.page == page){
                list2.add(value);
            }
        }
        return list2;
    }

    public int sliderHeld = 0;
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int $$2) {

        //if (minecraft.gameMode == null || minecraft.player == null) {
        //   return;
        //}


        sliderHeld = 0;
        int k;
        int l;

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 40, 60, 8, mouseX, mouseY)) {

            this.minecraft.setScreen(null);

            ModMessageEvents.sendToServer(
                    ClientToServerPackets.StandPowerPackets.MESSAGES.HairColor.value,
                    hairColorX,hairColorY,hairColorZ
            );
            //SAVE
            return true;
        }
        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 55, 60, 8, mouseX, mouseY)) {
            this.minecraft.setScreen(null);
            return true;
        }
        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 70, 60, 8, mouseX, mouseY)) {
            hairColorX = baseHairColorX;
            hairColorY = baseHairColorY;
            hairColorZ = baseHairColorZ;
            ModMessageEvents.sendToServer(
                    ClientToServerPackets.StandPowerPackets.MESSAGES.HairColor.value,
                    hairColorX,hairColorY,hairColorZ
            );
            return true;
        }

        //ModPacketHandler.PACKET_ACCESS.itemContextToServer(pIcon.id,
        //        stack, PacketDataIndex.USE_CORPSE_BAG, vc);


        //this.minecraft.setScreen(null);
        return true;
    }


    protected int imageWidth = 176;

    public float maxRed = 1;

    public float maxGreen = 1;

    public float maxBlue = 1;

    @Override
    public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
        if (updateClickDragged($$0,$$1,$$2)){
            return true;
        }
        return super.mouseDragged($$0,$$1,$$2,$$3,$$4);
    }

    public boolean updateClickDragged(double $$0, double $$1, int $$2){
        Player pl = Minecraft.getInstance().player;
        if (pl != null) {
            int i = this.width / 2 - 30;
            int j = this.height / 2 - 70;
            IPlayerEntity ipe = ((IPlayerEntity) pl);
            $$0 = Math.min($$0,i+118);
            $$0 = Math.max($$0,i);
            if (sliderHeld == 1) {
                float initialX = (float) ($$0 - i);
                initialX =  (((float) maxRed / 118) * initialX);
                hairColorX = initialX;
                return true;
            }
            if (sliderHeld == 2) {
                float initialX = (float) ($$0 - i);
                initialX = (((float) maxGreen / 118) * initialX);
                hairColorY = initialX;
                return true;
            }
            if (sliderHeld == 3) {
                float initialX = (float) ($$0 - i);
                initialX = (((float) maxBlue / 118) * initialX);
                hairColorZ = initialX;
                sliderHeld = 3;
                return true;
            }
        }
        return false;
    }

    public boolean updateClicked(double $$0, double $$1, int $$2){
        Player pl = Minecraft.getInstance().player;
        if (pl != null) {
            int i = this.width / 2 - 30;
            int j = this.height / 2 - 70;
            IPlayerEntity ipe = ((IPlayerEntity) pl);
            if (isSurelyHovering(i, j, 118, 11, $$0, $$1)) {
                float initialX = (float) ($$0 - i);
                initialX = (((float) maxRed / 118) * initialX);
                hairColorX = initialX;
                sliderHeld = 1;
                return true;
            }
            if (isSurelyHovering(i, j + 22, 118, 11, $$0, $$1)) {
                float initialX = (float) ($$0 - i);
                initialX = (((float) maxGreen / 118) * initialX);
                hairColorY = initialX;
                sliderHeld = 2;
                return true;
            }
            if (isSurelyHovering(i, j + 44, 118, 11, $$0, $$1)) {
                float initialX = (float) ($$0 - i);
                initialX = (((float) maxBlue / 118) * initialX);
                hairColorZ = initialX;
                sliderHeld = 3;
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int $$2) {
        Player pl = Minecraft.getInstance().player;
        if (pl != null) {
            if (updateClicked(mouseX, mouseY, $$2)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, $$2);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);

        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 58;
        int l = this.height / 2 - 111;
        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 0.0f, 113, 26, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.hairspray.base"), this.width / 2 -2, this.height / 2 - 31 - 76, -1);



        int i = this.width / 2 - 30;
        int j = this.height / 2 - 70;
        guiGraphics.drawString(this.font, Component.translatable("roundabout.hairspray.red").withStyle(ChatFormatting.GRAY), i +1, j - 9, 4210752, false);
        guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i, j, 11, 173, 118, 11);
        int renderSpot1 = (int) Math.floor(((double) 114 / maxRed) * (hairColorX));
        if (isSurelyHovering(i, j, 118, 11, mouseX, mouseY) || sliderHeld == 1) {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot1, j, 5, 185, 5, 11);
        } else {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot1, j, 5, 173, 5, 11);
        }

        guiGraphics.drawString(this.font, Component.translatable("roundabout.hairspray.green").withStyle(ChatFormatting.GRAY), i+1, j + 13, 4210752, false);
        guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i, j + 22, 11, 173, 118, 11);

        int renderSpot2 =(int) Math.floor(((double) 114 / maxGreen) * (hairColorY));
        if (isSurelyHovering(i, j + 22, 118, 11, mouseX, mouseY) || sliderHeld == 2) {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot2, j + 22, 5, 185, 5, 11);
        } else {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i+ renderSpot2, j + 22, 5, 173, 5, 11);
        }

        guiGraphics.drawString(this.font, Component.translatable("roundabout.hairspray.blue").withStyle(ChatFormatting.GRAY), i +1, j + 35, 4210752, false);
        guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i, j + 44, 11, 173, 118, 11);
        int renderSpot3 = (int) Math.floor(((double) 114 / maxBlue) * (hairColorZ));
        if (isSurelyHovering(i , j + 44, 118, 11, mouseX, mouseY) || sliderHeld == 3) {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot3, j + 44, 5, 185, 5, 11);
        } else {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot3, j + 44, 5, 173, 5, 11);
        }

        k = this.width / 2 - 75;
        l = this.height / 2 +17;
        ((IPlayerEntity)pl).rdbt$setHairColorX(hairColorX);
        ((IPlayerEntity)pl).rdbt$setHairColorY(hairColorY);
        ((IPlayerEntity)pl).rdbt$setHairColorZ(hairColorZ);
        byte pos2 = ((IPlayerEntity)pl).roundabout$GetPos2();
        ((IPlayerEntity)pl).roundabout$SetPos2(PlayerPosIndex.HAIR_EXTENSION);
        renderEntityInInventoryFollowsMouse(guiGraphics, k, l, 30,
                (float) k - mouseX, (float) l - 50 - mouseY, pl);
        ((IPlayerEntity)pl).roundabout$SetPos2(pos2);
        ((IPlayerEntity)pl).rdbt$setHairColorX(baseHairColorX);
        ((IPlayerEntity)pl).rdbt$setHairColorY(baseHairColorY);
        ((IPlayerEntity)pl).rdbt$setHairColorZ(baseHairColorZ);

        k = this.width / 2 - 110;
        l = this.height / 2 - 75;
        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 109.0f, 71, 107, 256, 256);


        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 40, 60, 8, mouseX, mouseY)) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.save").
                            withStyle(ChatFormatting.GREEN),
                    this.width / 2, this.height / 2 + 40, -1);
        } else {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.save"),
                    this.width / 2, this.height / 2 + 40, -1);
        }

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 55, 60, 8, mouseX, mouseY)) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.exit").
                            withStyle(ChatFormatting.GREEN),
                    this.width / 2, this.height / 2 + 55, -1);
        } else {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.exit"),
                    this.width / 2, this.height / 2 + 55, -1);
        }

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 70, 70, 8, mouseX, mouseY)) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.reset").
                            withStyle(ChatFormatting.GREEN),
                    this.width / 2, this.height / 2 + 70, -1);
        } else {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.reset"),
                    this.width / 2, this.height / 2 + 70, -1);
        }






        if (!this.setFirstMousePos) {
            this.firstMouseX = mouseX;
            this.firstMouseY = mouseY;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == mouseX && this.firstMouseY == mouseY;
    }


    public void renderEntityInInventoryFollowsMouse(GuiGraphics $$0, int $$1, int $$2, int $$3, float $$4, float $$5, Player user) {

        if (user != null) {
            IPlayerEntity ipe = ((IPlayerEntity)user);

            float $$7 = (float) Math.atan((double) ($$4 / 40.0F));
            float $$8 = (float) Math.atan((double) ($$5 / 40.0F));
            Quaternionf $$9 = new Quaternionf().rotateZ((float) Math.PI);
            Quaternionf $$10 = new Quaternionf().rotateX($$8 * 20.0F * (float) (Math.PI / 180.0));
            $$9.mul($$10);
            float $$11 = user.yBodyRot;
            float $$12 = user.getYRot();
            float $$13 = user.getXRot();
            float $$14 = user.yHeadRotO;
            float $$15 = user.yHeadRot;
            user.yBodyRot = 180.0F + $$7 * 20.0F;
            user.setYRot(180.0F + $$7 * 40.0F);
            user.setXRot(-$$8 * 20.0F);
            user.yHeadRot = user.getYRot();
            user.yHeadRotO = user.getYRot();
            renderEntityInInventory($$0, $$1, $$2, $$3, $$9, $$10, user);
            user.yBodyRot = $$11;
            user.setYRot($$12);
            user.setXRot($$13);
            user.yHeadRotO = $$14;
            user.yHeadRot = $$15;
        }
    }
    public static void renderEntityInInventory(GuiGraphics $$0, int $$1, int $$2, int $$3, Quaternionf $$4, @Nullable Quaternionf $$5, LivingEntity $$6) {
        $$0.pose().pushPose();
        $$0.pose().translate((double)$$1, (double)$$2, 50.0);
        $$0.pose().mulPoseMatrix(new Matrix4f().scaling((float)$$3, (float)$$3, (float)(-$$3)));
        $$0.pose().mulPose($$4);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        if ($$5 != null) {
            $$5.conjugate();
            $$7.overrideCameraOrientation($$5);
        }

        $$7.setRenderShadow(false);
        OptionInstance<GraphicsStatus> gr = Minecraft.getInstance().options.graphicsMode();
        GraphicsStatus grg = (GraphicsStatus)gr.get();
        gr.set(GraphicsStatus.FANCY);
        $$7.render($$6, 0.0, 0.0, 0.0, 0.0F, 1.0F, $$0.pose(), $$0.bufferSource(), 15728880);
        gr.set(grg);
        $$0.flush();
        $$7.setRenderShadow(true);
        $$0.pose().popPose();
        Lighting.setupFor3DItems();
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

