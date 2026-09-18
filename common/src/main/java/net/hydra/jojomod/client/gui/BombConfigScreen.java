package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
    private final List<SwitchSelect> sizes = Lists.newArrayList();

    @Override
    protected void init() {
        super.init();
        zHeld = true;

        this.currentlyHovered = (byte)-1;

        int offsetCenter = 32;

        this.slots.add(new ToggableIcon((byte)BLOCK_DESTRUCTION, this.width / 2 - 13 - offsetCenter, this.height / 2 + 31 - 44, Component.translatable("roundabout.bomb_config.block_destruction")));
        this.slots.add(new ToggableIcon((byte)ON_CONTACT, this.width / 2 - 13 + offsetCenter, this.height / 2 + 31 - 44, Component.translatable("roundabout.bomb_config.contact_explosion")));

        this.sizes.add(new SwitchSelect(0, this.width / 2 - 33, this.height / 2 - 32 + 48, Component.translatable("roundabout.bomb_config.explosion_size_0")));
        this.sizes.add(new SwitchSelect(1, this.width / 2 - 33 + 20, this.height / 2 - 32 + 48, Component.translatable("roundabout.bomb_config.explosion_size_1")));
        this.sizes.add(new SwitchSelect(2, this.width / 2 - 33 + 40, this.height / 2 - 32 + 48, Component.translatable("roundabout.bomb_config.explosion_size_2")));


    }

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
            if (isSelected) {
                guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX() + 4, this.getY()+4, 54, 51, 18, 18, 192, 192);
            }

            guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX() + 4, this.getY()+4, this.context*18, 69, 18, 18, 192, 192);
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }

        public int getMode() {
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            Player p = Minecraft.getInstance().player;
            if (p != null) {
                StandUser SU = (StandUser) p;
                if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen) {
                    int conf = clientConfig.dynamicSettings.killerQueenCurrentBombSize;

                    return conf == context ? ENABLED : DISABLED;

                }
            }
            return DISABLED;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    public class ToggableIcon extends AbstractWidget {
    	public byte context;
        public int xoff;
        public int yoff;
        private boolean isSelected;
        final Component name;
        
        public ToggableIcon(byte context, int xoff, int yoff, Component name) {
        	super(xoff, yoff, 26, 26, name);
            this.context = context;
            this.xoff = xoff;
            this.yoff = yoff;
            this.name = name;
        }

        public Component getName() {return name; }

        public int getMode(boolean invert) {
        	ClientConfig clientConfig = ConfigManager.getClientConfig();

            int conf = clientConfig.dynamicSettings.killerQueenCurrentBombConfig;
            if (this.context == BLOCK_DESTRUCTION) {
                if (conf == 1 || conf == 3) {
                    return (invert && isHoveredOrFocused()) ? DISABLED : ENABLED;
                }
            } else {
                if (conf == 2 || conf == 3) {
                    return (invert && isHoveredOrFocused()) ? DISABLED : ENABLED;
                }
            }

            return (invert && isHoveredOrFocused()) ? ENABLED : DISABLED;
        }

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
			guiGraphics.setColor(1f, 1f, 1f, 1f);
			int status = this.getMode(false)*2;
			if (this.isSelected) {status = 1;}

            Level level = Minecraft.getInstance().player.level();

			if (this.context == 0 && (!ClientNetworking.getAppropriateConfig().killerQueenSettings.blocksDestruction
                    || !level.getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING))) {status = 3;}
			
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
			int status = getMode(false);
			if (this.context == BLOCK_DESTRUCTION && !ClientNetworking.getAppropriateConfig().killerQueenSettings.blocksDestruction) {status = 2;}
			guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.getX() + 4, this.getY()+4, status*18, 26+7 + this.context*18, 18, 18, 192, 192);

		}
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if ($$2 == 0) {
            updateConfigs();
        }else {
            this.exitBombConfig();
        }

        return super.mouseReleased($$0, $$1, $$2);
    }

    public void exitBombConfig() {
        this.switchToHoveredGameMode();
        this.minecraft.setScreen(null);

        Player pl = Minecraft.getInstance().player;
        if (pl != null) {
            StandUser SU = (StandUser) pl;

            if (SU.roundabout$getStandPowers() instanceof PowersKillerQueen PK) {
                PK.bombConfigPacket();
            }
        }
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

        if (Minecraft.getInstance().player != null) {
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            int context = 1;
            if (clientConfig != null) {
                context = clientConfig.dynamicSettings.killerQueenCurrentBombSize;
            }
            guiGraphics.blit(KILLER_QUEEN_BOMB_LOCATION, this.width / 2 - 32, this.height / 2 - 31 + 48, 64f * context, 125f, 64, 24, 192, 192);
        }
        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
       
        //boolean bl = this.firstMouseX == i && this.firstMouseY == j;

        int lastState = currentlyHovered;


        this.currentlyHovered = -1;
        if (this.slots.get(0).isHoveredOrFocused()) {this.currentlyHovered = 0;}
        if (this.slots.get(1).isHoveredOrFocused()) {this.currentlyHovered = 1;}
        if (this.sizes.get(0).isHoveredOrFocused()) {this.currentlyHovered = 2;}
        if (this.sizes.get(1).isHoveredOrFocused()) {this.currentlyHovered = 3;}
        if (this.sizes.get(2).isHoveredOrFocused()) {this.currentlyHovered = 4;}


        for (ToggableIcon MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.context);
        }
        for (SwitchSelect MobSlot : this.sizes) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected((this.currentlyHovered-2) == MobSlot.context);
        }

        if (currentlyHovered != -1 && lastState != currentlyHovered) {
            SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
            soundmanager.play(SimpleSoundInstance.forUI(ModSounds.KILLER_QUEEN_DETONATE_EVENT, (float) (0.95 + (Math.random() * 0.1F))));
        }

        if (this.currentlyHovered != -1) {
            Component text;
            if (currentlyHovered < 2) {
                text = slots.get(currentlyHovered).getName();
            }else {
                text = sizes.get(currentlyHovered - 2).getName();
            }

            guiGraphics.drawCenteredString(this.font, text, this.width / 2, this.height / 2 - 31 + 80, -1);
        }
        
    }

    private void updateConfigs() {
        if (currentlyHovered != -1) {
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            int value = this.slots.get(0).getMode(true) + (this.slots.get(1).getMode(true) * 2);

            clientConfig.dynamicSettings.killerQueenCurrentBombConfig = value;

            if (currentlyHovered >= 2) {
                clientConfig.dynamicSettings.killerQueenCurrentBombSize = currentlyHovered - 2;
            }

            ConfigManager.saveClientConfig();
        }
    }

    private void switchToHoveredGameMode() {
    	updateConfigs();
    }
    
    public boolean roundabout$sameKeyOne(KeyMapping key1){
        return (key1.isDown() || (key1.same(this.minecraft.options.keyLoadHotbarActivator) && this.minecraft.options.keyLoadHotbarActivator.isDown())
                || (key1.same(this.minecraft.options.keySaveHotbarActivator) && this.minecraft.options.keySaveHotbarActivator.isDown())
        );
    }
    
    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        if (this.minecraft != null && !roundabout$sameKeyOne(KeyInputRegistry.abilityOneKey)) {
            this.exitBombConfig();

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