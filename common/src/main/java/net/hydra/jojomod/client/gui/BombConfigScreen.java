package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.gui.MemoryRecordScreen.PoseSlot;
import net.hydra.jojomod.client.gui.MemoryRecordScreen.memoryIcon;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.index.AnubisMemory;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
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

public class BombConfigScreen extends Screen implements NoCancelInputScreen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation KILLER_QUEEN_BOMB_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/killer_queen_bomb.png");
    
    public BombConfigScreen(boolean recording) {
        super(GameNarrator.NO_TITLE);
    }
    

    public byte currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;
    private final List<ToggableIcon> slots = Lists.newArrayList();
    
    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;

        this.currentlyHovered = (byte)-1;
        if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen PA) {
            int offsetCenter = 32;
        	
        	ToggableIcon leftIcon = new ToggableIcon((byte)0, this.width / 2 - 13 - offsetCenter, this.height / 2 + 31 - 44);
        	ToggableIcon rightIcon = new ToggableIcon((byte)1, this.width / 2 - 13 + offsetCenter, this.height / 2 + 31 - 44);
        	
            this.slots.add(leftIcon);
            this.slots.add(rightIcon);

        }

    }

    public class ToggableIcon extends AbstractWidget {
    	public byte context;
        public int xoff;
        public int yoff;
        private boolean isSelected;
        
        public ToggableIcon(byte context, int xoff, int yoff) {
        	super(xoff, yoff, 26, 26, Component.literal(""));
            this.context = context;
            this.xoff = xoff;
            this.yoff = yoff;
        }
        
        public int getMode() {
            Player p = Minecraft.getInstance().player;
            StandUser SU = (StandUser) p;
            if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen PA) {
            	if (this.context == 0) {
	                if (PA.getExplodeOnContact()) {
	                	return 1;
	                }
            	}else {
            		if (PA.getDestroyTerrain()) {
	                	return 1;
	                }
            	}
            }
            return 0;
        }

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
			guiGraphics.setColor(1f, 1f, 1f, 1f);
			int status = this.getMode()*2;
			if (this.isSelected) {status = 1;}
			
	        this.drawSlot(guiGraphics, status);
	        this.drawIcon(guiGraphics);
	        //guiGraphics.setColor(1f, 1f, 1f, 1f);
	        //guiGraphics.renderItem(this.icon.item.getDefaultInstance(),this.getX() + 5, this.getY() + 5);
	        
		    
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
		    guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX(), this.getY()-7, i*26, 0, 26, 26+7, 192, 192);
		}
		
		private void drawIcon(GuiGraphics guiGraphics) {
			int status = getMode();
			guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX() + 4, this.getY()+4, status*18, 26+7 + this.context*18, 18, 18, 192, 192);

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
       /* if (this.checkToClose()) {
            return;
        }*/


        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
       /* int k = this.width / 2 - 62;
        int l = this.height / 2 - 31 - 39;
        guiGraphics.blit(MEMORY_LOCATION, k, l, 0.0f, 0.0f, 125, 24 , 256, 256);*/
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);


       //Component str = Component.translatable("roundabout.anubis.playback_title");
       //if (this.recording) {str = Component.translatable("roundabout.anubis.memory_title");}
       //guiGraphics.drawCenteredString(this.font, str , this.width / 2, this.height / 2 - 31 - 32, -1);

        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
        /*
        for (int[] pos : this.positions) {
            int x = this.width/2 - 5 + pos[0];
            int y = this.height/2 - 5 + pos[1];
            guiGraphics.blit(MEMORY_LOCATION,x,y,196,0,12,12,256,256);
        }*/
        
        for (ToggableIcon MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.context);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            this.currentlyHovered = MobSlot.context;
            /*
            Player player = Minecraft.getInstance().player;
            StandUser SU = (StandUser) player;
           */

        }
        
    }

    private void switchToHoveredGameMode() {
       switchToHoveredGameMode(this.minecraft,slots.get(this.currentlyHovered));
    }

    private void switchToHoveredGameMode(Minecraft minecraft, ToggableIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;
        
        if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen PA) {
            if (pIcon.context == 0) {
            	PA.setExplodeOnContact(!PA.getExplodeOnContact());
            }else {
            	PA.setDestroyTerrain(!PA.getDestroyTerrain());
            }
        }
    }
    
}