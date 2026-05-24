package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.powers.StandUser;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class BombConfigScreen extends Screen implements NoCancelInputScreen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation KILLER_QUEEN_BOMB_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/killer_queen_bomb.png");
    
    private static final int
    	DISABLED=0,
    	ENABLED=1,
    	BLOCK_DESTRUCTION=0,
    	ON_CONTACT=1;
    
    public BombConfigScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    public boolean isPauseScreen() { return false;}

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
        	
        	ToggableIcon leftIcon = new ToggableIcon((byte)BLOCK_DESTRUCTION, this.width / 2 - 13 - offsetCenter, this.height / 2 + 31 - 44);
        	ToggableIcon rightIcon = new ToggableIcon((byte)ON_CONTACT, this.width / 2 - 13 + offsetCenter, this.height / 2 + 31 - 44);
        	
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
        
        public int getMode() {return getMode(false);}
        
        public int getMode(boolean invert) {
        	ClientConfig clientConfig = ConfigManager.getClientConfig();
            Player p = Minecraft.getInstance().player;
            StandUser SU = (StandUser) p;
            if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen PA) {
            	int conf = clientConfig.dynamicSettings.KillerQueenCurrentBombConfig;
            	if (this.context == BLOCK_DESTRUCTION) {
            		if (conf == 1 || conf == 3) {
	                	return (invert) ? DISABLED : ENABLED;
	                }
            	}else {
            		if (conf == 2 || conf == 3) {
	                	return (invert) ? DISABLED : ENABLED;
	                }
            	}
            }
            return (invert) ? ENABLED : DISABLED;
        }

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
			guiGraphics.setColor(1f, 1f, 1f, 1f);
			int status = this.getMode()*2;
			if (this.isSelected) {status = 1;}
			
			if (this.context == 0 && !ClientNetworking.getAppropriateConfig().killerQueenSettings.blocksDestruction) {status = 3;}
			
	        this.drawSlot(guiGraphics, status);
	        this.drawIcon(guiGraphics);
		    
		}
		
        
        @Override
		public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		    this.defaultButtonNarrationText(narrationElementOutput);
		}
		
		@Override
		public boolean isHoveredOrFocused() {
		    return super.isHoveredOrFocused();
		}
		
		public void setSelected(boolean bl) {
		    this.isSelected = bl;
		}
		
		private void drawSlot(GuiGraphics guiGraphics, int i) {
		    guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX(), this.getY()-7, i*26, 0, 26, 26+7, 192, 192);
		}
		
		private void drawIcon(GuiGraphics guiGraphics) {
			int status = getMode();
			if (this.context == BLOCK_DESTRUCTION && !ClientNetworking.getAppropriateConfig().killerQueenSettings.blocksDestruction) {status = 2;}
			guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX() + 4, this.getY()+4, status*18, 26+7 + this.context*18, 18, 18, 192, 192);

		}
		
    }
    

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        this.switchToHoveredGameMode();
        this.minecraft.setScreen(null);
        return super.mouseReleased($$0, $$1, $$2);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
       /* if (this.checkToClose()) {
            return;
        }*/


        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 62;
        int l = this.height / 2 - 31 - 39;
        guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, k, l, 0.0f, 88.0f, 125, 37, 192, 192);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);


       Component str = Component.translatable("roundabout.killer_queen.bomb_config");
       
       guiGraphics.drawCenteredString(this.font, str , this.width / 2, this.height / 2 - 31 - 32+8, -1);

        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
       
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
       
        this.currentlyHovered = -1;
        if (this.slots.get(0).isHoveredOrFocused()) {this.currentlyHovered = 0;}
        if (this.slots.get(1).isHoveredOrFocused()) {this.currentlyHovered = 1;}
        
        for (ToggableIcon MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.context);

        }
        
    }

    private void switchToHoveredGameMode() {
    	if (this.currentlyHovered != -1) {
    		switchToHoveredGameMode(this.minecraft,slots.get(this.currentlyHovered));
       }
    }

    private void switchToHoveredGameMode(Minecraft minecraft, ToggableIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }
        Player pl = Minecraft.getInstance().player;
        StandUser SU = (StandUser) pl;
        
        if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen PA) {
            
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            int conf = this.slots.get(0).getMode(this.currentlyHovered == 0);
            conf += this.slots.get(1).getMode(this.currentlyHovered == 1)*2;
            
            clientConfig.dynamicSettings.KillerQueenCurrentBombConfig = (int) conf;
            ConfigManager.saveClientConfig();
            
        }
    }
    
    public boolean roundabout$sameKeyOne(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.minecraft.options.keyLoadHotbarActivator) && this.minecraft.options.keyLoadHotbarActivator.isDown())
                || (key1.same(this.minecraft.options.keySaveHotbarActivator) && this.minecraft.options.keySaveHotbarActivator.isDown())
        );
    }
    
    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityOneKey)) {
            this.switchToHoveredGameMode();
            this.minecraft.setScreen(null);
            if (this.minecraft.player != null){
                StandUser SU = ((StandUser) this.minecraft.player);
                if (SU.roundabout$getStandPowers().isBarraging()){
                    //This prevents barrage canceling
                } else {
                    this.minecraft.options.keyUse.setDown(false);
                }
            }
        }
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Prevent the screen from handling WASD, space, shift, etc.
        if (Minecraft.getInstance().options.keyUp.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyDown.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyLeft.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyRight.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyJump.matches(keyCode, scanCode) ||
                Minecraft.getInstance().options.keyShift.matches(keyCode, scanCode)) {
            return false; // Let these go through to the player
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}