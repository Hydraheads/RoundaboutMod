package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.index.Tactics;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class ZombieMinionScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation CORPSE_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/tactics_chooser.png");
    private static final int SPRITE_SHEET_WIDTH = 256;
    private static final int SPRITE_SHEET_HEIGHT = 256;
    private static final int SLOT_AREA = 26;
    private static final int SLOT_PADDING = 5;
    private static final int SLOT_AREA_PADDED = 31;
    private static final int HELP_TIPS_OFFSET_Y = 5;
    private static final int ALL_SLOTS_WIDTH = tacticIcon.VALUES.length * 31 - 5;
    private tacticIcon currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    public final int hostID;

    private final List<PoseSlot> slots = Lists.newArrayList();

    public ZombieMinionScreen(int endid) {
        super(GameNarrator.NO_TITLE);
        hostID = endid;
        this.currentlyHovered = null;
    }

    private ShapeShifts getDefaultSelected() {
        Player pl = Minecraft.getInstance().player;
        if (pl != null){
            return ShapeShifts.getShiftFromByte(((IPlayerEntity)pl).roundabout$getShapeShift());
        }
        return ShapeShifts.PLAYER;
    }

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;

        this.currentlyHovered = tacticIcon.NONE;
        for (int i = 0; i < tacticIcon.VALUES.length; ++i) {
            tacticIcon pIcon = tacticIcon.VALUES[i];
            this.slots.add(new PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
        }
    }


    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        this.switchToHoveredGameMode();
        if (this.currentlyHovered.id != Tactics.CHANGE_TEAM.id){
            this.minecraft.setScreen(null);
        }
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
        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 26.0f, 125, 63, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);
        if (this.currentlyHovered != null) {
            guiGraphics.drawCenteredString(this.font, this.currentlyHovered.getName(), this.width / 2, this.height / 2 - 31 - 32, -1);
        }
        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        caughtSomething = false;
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
        for (PoseSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon);

            Player p = ClientUtil.getPlayer();
            if (p != null) {
                Entity ent = p.level().getEntity(hostID);
                if (ent instanceof BaseMinion bm){
                    if (MobSlot.getID() == bm.getMovementTactic() ||
                            MobSlot.getID() == bm.getTargetTactic()
                    || (MobSlot.getID() == 3 && bm.getMovementTactic() == 0)
                            || (MobSlot.getID() == 6 && bm.getTargetTactic() == 0)){
                        MobSlot.setCorrect(true);
                    }
                }
            }

            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            caughtSomething = true;
            this.currentlyHovered = MobSlot.icon;
        }
        if (!caughtSomething){
            this.currentlyHovered = tacticIcon.NONE;
        }
    }

    boolean caughtSomething = false;

    private void switchToHoveredGameMode() {
       switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }

    private void switchToHoveredGameMode(Minecraft minecraft, tacticIcon pIcon) {
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


        if (pIcon.id != Tactics.NONE.id) {
            minecraft.player.playSound(ModSounds.JUSTICE_SELECT_EVENT, 200F, 1.0F);
            C2SPacketUtil.switchZombieTacticPacket(hostID,pIcon.id);
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


    public enum tacticIcon {

        NONE(Component.translatable("roundabout.minion.none"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/pose_icons/jonathan.png"),Tactics.NONE.id,0,31),
        EQUIP(Component.translatable("roundabout.minion.tactics.equip"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/equip.png"), Tactics.EQUIP.id,-30,4),
        SETHOME(Component.translatable("roundabout.minion.tactics.sethome"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/sethome.png"),Tactics.SETHOME.id,0,4),
        SENDHOME(Component.translatable("roundabout.minion.tactics.sendhome"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/sendhome.png"),Tactics.SENDHOME.id,30,4),
        STAY_PUT(Component.translatable("roundabout.corpse.tactics.still"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/stay_put.png"),Tactics.STAY_PUT.id,-31,40),
        ROAM(Component.translatable("roundabout.corpse.tactics.wander"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/roam.png"),Tactics.ROAM.id,-31,70),
        HOLD(Component.translatable("roundabout.corpse.tactics.hold"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/hold.png"),Tactics.HOLD.id,-61,70),
        FOLLOW(Component.translatable("roundabout.corpse.tactics.follow"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/follow.png"),Tactics.FOLLOW.id,-61,40),
        DEFEND(Component.translatable("roundabout.corpse.tactics.guard"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/defend.png"),Tactics.DEFEND.id,31,40),
        HUNT_MONSTERS(Component.translatable("roundabout.corpse.tactics.hunt_monster"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/hunt_monsters.png"),Tactics.HUNT_MONSTERS.id,61,40),
        HUNT_PLAYERS(Component.translatable("roundabout.corpse.tactics.hunt_player"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/hunt_players.png"),Tactics.HUNT_PLAYERS.id,61,70),
        PEACEFUL(Component.translatable("roundabout.corpse.tactics.peaceful"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/tactics_icons/peaceful.png"),Tactics.PEACEFUL.id,31,70);

        static tacticIcon getByte(Tactics tactics) {
            return switch (tactics) {
                default -> throw new IncompatibleClassChangeError();
                case EQUIP -> EQUIP;
                case SETHOME -> SETHOME;
                case SENDHOME -> SENDHOME;
                case STAY_PUT -> STAY_PUT;
                case HOLD -> HOLD;
                case ROAM -> ROAM;
                case FOLLOW -> FOLLOW;
                case DEFEND -> DEFEND;
                case HUNT_MONSTERS -> HUNT_MONSTERS;
                case HUNT_PLAYERS -> HUNT_PLAYERS;
                case PEACEFUL -> PEACEFUL;
            };
        }
        protected static final tacticIcon[] VALUES;
        private static final int ICON_AREA = 16;
        protected static final int ICON_TOP_LEFT = 5;
        final Component name;
        final ResourceLocation rl;
        final byte id;

        final int xoff;
        final int yoff;

        private tacticIcon(Component component, ResourceLocation rl, byte id, int xoff, int yoff) {
            this.name = component;
            this.rl = rl;
            this.id = id;
            this.xoff = xoff;
            this.yoff = yoff;
        }


        void drawIcon(GuiGraphics guiGraphics, int i, int j) {
            guiGraphics.blit(rl, i-1, j-1, 0, 0, 18, 18, 18, 18);
        }
        void drawIcon2(ResourceLocation rl, GuiGraphics guiGraphics, int i, int j) {
            guiGraphics.blit(rl, i-1, j-1, 0, 0, 18, 18, 18, 18);
        }

        Component getName() {
            return this.name;
        }

        static {
            VALUES = new tacticIcon[]{NONE,EQUIP,SENDHOME,SETHOME,STAY_PUT,HOLD,ROAM,FOLLOW,
            FOLLOW, DEFEND, HUNT_MONSTERS, HUNT_PLAYERS, PEACEFUL};
        }
    }

    public class PoseSlot
            extends AbstractWidget {
        final tacticIcon icon;
        private boolean isSelected;
        private boolean isCorrect = false;

        public PoseSlot(tacticIcon pIcon, int i, int j) {
            super(i, j, 26, 26, pIcon.getName());
            this.icon = pIcon;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            if (!this.icon.equals(tacticIcon.NONE)) {
                this.drawSlot(guiGraphics);
                this.icon.drawIcon(guiGraphics, this.getX() + 5, this.getY() + 5);
                if (this.isSelected) {
                    this.drawSelection(guiGraphics);
                } else if (isCorrect){
                    this.drawSelection2(guiGraphics);
                }
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }
        public void setCorrect(boolean bl) {
            this.isCorrect = bl;
        }

        public int getID(){
            return icon.id;
        }

        private void drawSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, this.getX(), this.getY(), 144.0f, 26.0f, 26, 26, 256, 256);
        }

        private void drawSelection(GuiGraphics guiGraphics) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, this.getX(), this.getY(), 170.0f, 26.0f, 26, 26, 256, 256);
        }
        private void drawSelection2(GuiGraphics guiGraphics) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, this.getX(), this.getY(), 198.0f, 26.0f, 26, 26, 256, 256);
        }
    }
}
