package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.VisageStoreEntry;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public VisageStoreScreen(boolean costs) {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
        this.costsEmeralds = costs;
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
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int $$2) {

        //if (minecraft.gameMode == null || minecraft.player == null) {
        //   return;
        //}

        int k;
        int l;
        k = this.width / 2 - 79;
        l = this.height / 2 - 31 - 31;
        if (isSurelyHovering(k, l, 19, 24, mouseX, mouseY)) {
            turnPage(true);
        }

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 50, 60, 10, mouseX, mouseY)) {
            this.minecraft.setScreen(null);
            return true;
        }

        k = this.width / 2 + 55;
        l = this.height / 2 - 31 - 31;
        if (isSurelyHovering(k, l, 19, 24, mouseX, mouseY)) {
            turnPage(false);
        }
        //ModPacketHandler.PACKET_ACCESS.itemContextToServer(pIcon.id,
        //        stack, PacketDataIndex.USE_CORPSE_BAG, vc);

        List<VisageStoreEntry> list = getPage();
        for (VisageStoreEntry value : list) {
            if (value.page == page) {
                int index = list.indexOf(value);
                int kk = index % 5;
                kk *= 22;
                k = this.width / 2 - 54 + kk;
                int bb = 0;
                bb = Mth.floor((double) index / 5) * 22;
                l = this.height / 2 - 73 + bb;
                if (isSurelyHovering(k, l, 16, 16, mouseX, mouseY)) {
                    if (!canAfford(value)) {
                        SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
                        soundmanager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    } else {
                        if (costsEmeralds){
                            ModPacketHandler.PACKET_ACCESS.intToServerPacket(ModItems.getVisageStore().indexOf(value),PacketDataIndex.INT_INDEX_OF_VISAGE_EMERALDS);
                        } else {
                            ModPacketHandler.PACKET_ACCESS.intToServerPacket(ModItems.getVisageStore().indexOf(value),PacketDataIndex.INT_INDEX_OF_VISAGE_LEVEL);
                        }
                        this.minecraft.setScreen(null);
                    }
                }
            }
        }

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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);

        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 58;
        int l = this.height / 2 - 31 - 80;
        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 0.0f, 113, 26, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui"), this.width / 2 -2, this.height / 2 - 31 - 76, -1);

        guiGraphics.drawCenteredString(this.font, ""+(page+1), this.width / 2, this.height / 2 + 30, -1);

        if (isSurelyHovering( this.width / 2-30,  this.height / 2 + 50, 60, 10, mouseX, mouseY)) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.exit").
                            withStyle(ChatFormatting.GREEN),
                    this.width / 2, this.height / 2 + 50, -1);
        } else {
            guiGraphics.drawCenteredString(this.font, Component.translatable("roundabout.cinderella.gui.exit"),
                    this.width / 2, this.height / 2 + 50, -1);
        }


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



        List<VisageStoreEntry> list = getPage();
        for (VisageStoreEntry value : list) {
            if (value.page == page){
                int index = list.indexOf(value);
                int kk = index % 5;
                kk *= 22;
                k = this.width / 2 - 54 + kk;
                int bb =0;
                bb = Mth.floor((double) index / 5) * 22;
                l = this.height / 2 - 73 + bb;
                guiGraphics.renderItem(value.stack, k, l, k+l * this.imageWidth);
                if (isSurelyHovering(k,l,16,16,mouseX,mouseY)){
                    if (!canAfford(value)){
                        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k-2, l-2, 165, 27, 20, 20, 256, 256);
                    } else {
                        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k-2, l-2, 144, 27, 20, 20, 256, 256);
                    }

                    List<Component> compList = Lists.newArrayList();
                    compList.add(value.stack.getHoverName());
                    compList.add(Component.translatable(  value.stack.getDescriptionId()+".desc", value.costE).withStyle(ChatFormatting.ITALIC));

                    if (costsEmeralds){
                        compList.add(Component.translatable(  "roundabout.cinderella.gui.cost_emeralds", value.costE).withStyle(ChatFormatting.GREEN));
                    } else {
                        compList.add(Component.translatable(  "roundabout.cinderella.gui.cost_levels", value.costL).withStyle(ChatFormatting.GREEN));
                    }
                    if (!canAfford(value)){
                        compList.add(Component.translatable(  "roundabout.cinderella.gui.cannot_afford").withStyle(ChatFormatting.RED));
                    }

                    guiGraphics.renderTooltip(this.font, compList, Optional.empty(), mouseX, mouseY);
                }
            }
        }

        if (!this.setFirstMousePos) {
            this.firstMouseX = mouseX;
            this.firstMouseY = mouseY;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == mouseX && this.firstMouseY == mouseY;
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

