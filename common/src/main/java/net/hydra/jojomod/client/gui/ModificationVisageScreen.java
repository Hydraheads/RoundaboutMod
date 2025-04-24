package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.mobs.PlayerModifiedNPC;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.VisageStoreEntry;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModificationVisageScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation CORPSE_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/cinderella_gui.png");

    public static final ResourceLocation POWER_INVENTORY_GEAR_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/stand_user_settings.png");
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

    public ItemStack visage;

    private final List<CorpseBagScreen.PoseSlot> slots = Lists.newArrayList();

    public ModificationVisageScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }
    public ModificationVisageScreen(ItemStack stack) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
        visage = stack;
    }


    public int page = 0;
    public boolean costsEmeralds = false;

    Player pl;

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        pl = Minecraft.getInstance().player;
        this.page = 0;

        //this.currentlyHovered = CorpseBagScreen.corpseIcon.NONE;
        //for (int i = 0; i < CorpseBagScreen.corpseIcon.VALUES.length; ++i) {
            //CorpseBagScreen.corpseIcon pIcon = CorpseBagScreen.corpseIcon.VALUES[i];
            //this.slots.add(new CorpseBagScreen.PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
        //}
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

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 50, 60, 8, mouseX, mouseY)) {
            this.minecraft.setScreen(null);
            return true;
        }

        //ModPacketHandler.PACKET_ACCESS.itemContextToServer(pIcon.id,
        //        stack, PacketDataIndex.USE_CORPSE_BAG, vc);


        //this.minecraft.setScreen(null);
        return true;
    }

    public boolean canAfford(VisageStoreEntry vs){
        if (pl != null) {
            if (costsEmeralds) {
                int i = 0;
                for(int $$5 = 0; $$5 < pl.getInventory().getContainerSize(); ++$$5) {
                    ItemStack $$6 = pl.getInventory().getItem($$5);
                    if ($$6.getItem().equals(Items.EMERALD)){
                        i+=$$6.getCount();
                    }
                }
                return i >= vs.costE;
            }

            if (pl.experienceLevel >= vs.costL){
                return true;
            }
        }
        return false;
    }

    protected int imageWidth = 176;

    public int visageHeight = 237;
    public int maxVisageHeight = 270;

    public int visageWidth = 135;
    public int maxVisageWidth = 270;

    public int visageHeadSize = 135;
    public int maxVisageHeadSize = 270;

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
        guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.mod_visage_gui"), this.width / 2 -2, this.height / 2 - 31 - 76, -1);



        int i = this.width / 2 - 30;
        int j = this.height / 2 - 70;
        guiGraphics.drawString(this.font, Component.translatable("roundabout.cinderella.mod_visage_gui.height").withStyle(ChatFormatting.GRAY), i +1, j - 9, 4210752, false);
        guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i, j, 11, 173, 118, 11);
        int renderSpot1 = (int) Math.floor(((double) 114 / maxVisageHeight) * (visageHeight));
        if (isSurelyHovering(i, j, 118, 11, mouseX, mouseY) || sliderHeld == 1) {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot1, j, 5, 185, 5, 11);
        } else {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot1, j, 5, 173, 5, 11);
        }

        guiGraphics.drawString(this.font, Component.translatable("roundabout.cinderella.mod_visage_gui.weight").withStyle(ChatFormatting.GRAY), i+1, j + 13, 4210752, false);
        guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i, j + 22, 11, 173, 118, 11);

        int renderSpot2 =(int) Math.floor(((double) 114 / maxVisageWidth) * (visageWidth));
        if (isSurelyHovering(i, j + 22, 118, 11, mouseX, mouseY) || sliderHeld == 2) {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot2, j + 22, 5, 185, 5, 11);
        } else {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i+ renderSpot2, j + 22, 5, 173, 5, 11);
        }

        guiGraphics.drawString(this.font, Component.translatable("roundabout.cinderella.mod_visage_gui.head_size").withStyle(ChatFormatting.GRAY), i +1, j + 35, 4210752, false);
        guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i, j + 44, 11, 173, 118, 11);
        int renderSpot3 = (int) Math.floor(((double) 114 / maxVisageHeadSize) * (visageHeadSize));
        if (isSurelyHovering(i , j + 44, 118, 11, mouseX, mouseY) || sliderHeld == 3) {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot3, j + 44, 5, 185, 5, 11);
        } else {
            guiGraphics.blit(POWER_INVENTORY_GEAR_LOCATION, i + renderSpot3, j + 44, 5, 173, 5, 11);
        }



        k = this.width / 2 - 75;
        l = this.height / 2 +17;
        renderEntityInInventoryFollowsMouse(guiGraphics, k, l, 30,
                (float) k - mouseX, (float) l - mouseY, pl);


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
        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 50, 60, 8, mouseX, mouseY)) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.exit").
                            withStyle(ChatFormatting.GREEN),
                    this.width / 2, this.height / 2 + 50, -1);
        } else {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.exit"),
                    this.width / 2, this.height / 2 + 50, -1);
        }

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 40, 60, 8, mouseX, mouseY)) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.reset").
                            withStyle(ChatFormatting.GREEN),
                    this.width / 2, this.height / 2 + 40, -1);
        } else {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.reset"),
                    this.width / 2, this.height / 2 + 40, -1);
        }





        if (!this.setFirstMousePos) {
            this.firstMouseX = mouseX;
            this.firstMouseY = mouseY;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == mouseX && this.firstMouseY == mouseY;
    }


    public static void renderEntityInInventoryFollowsMouse(GuiGraphics $$0, int $$1, int $$2, int $$3, float $$4, float $$5, Player user) {

        Entity E = ModEntities.MODIFIED_NPC.create(user.level());
        if (E instanceof PlayerModifiedNPC $$6) {
            $$6.host = user;
            $$6.faker = user;

            float $$7 = (float) Math.atan((double) ($$4 / 40.0F));
            float $$8 = (float) Math.atan((double) ($$5 / 40.0F));
            Quaternionf $$9 = new Quaternionf().rotateZ((float) Math.PI);
            Quaternionf $$10 = new Quaternionf().rotateX($$8 * 20.0F * (float) (Math.PI / 180.0));
            $$9.mul($$10);
            float $$11 = $$6.yBodyRot;
            float $$12 = $$6.getYRot();
            float $$13 = $$6.getXRot();
            float $$14 = $$6.yHeadRotO;
            float $$15 = $$6.yHeadRot;
            $$6.yBodyRot = 180.0F + $$7 * 20.0F;
            $$6.setYRot(180.0F + $$7 * 40.0F);
            $$6.setXRot(-$$8 * 20.0F);
            $$6.yHeadRot = $$6.getYRot();
            $$6.yHeadRotO = $$6.getYRot();
            renderEntityInInventory($$0, $$1, $$2, $$3, $$9, $$10, $$6);
            $$6.yBodyRot = $$11;
            $$6.setYRot($$12);
            $$6.setXRot($$13);
            $$6.yHeadRotO = $$14;
            $$6.yHeadRot = $$15;
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

