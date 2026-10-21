package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.menu.LyinEyesInventoryMenu;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class PowersLyinEyes extends NewDashPreset {
    // for move ids accessed here. update public bytes every time this is edited.
    private static final byte
        READ_INVENTORY = 53,
        FINISH_READ_INVENTORY = 54,
        FINISH_OPEN_MAP = 55;
    // for mob ai, a new attribute is needed to increase the detection range
    private static final net.minecraft.world.entity.ai.attributes.AttributeModifier DETECTION_BOOST =
            new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                    java.util.UUID.fromString("d8f3b2a0-4e2a-4b9d-8f2c-1a2b3c4d5e6f"),
                    "Lyin Eyes Range Boost",
                    20.0D,
                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION
            );
    // stand summoning variable
    private int summonOverlayTicks = 0;
    private int gatherOverlayTicks = 0;
    private int idleOverlayTicks = 0;
    private boolean isSummon = false;
    private boolean isGather = false;
    private boolean desummonFromMove = false;
    private boolean hadInventoryMenuOpen = false;
    //inventory reading target;
    private Entity inventoryTarget = null;
    //highlight mode
    private boolean visionModeClient = false;
    //map mode
    private boolean mapModeClient = false;

    // basic stand registry stuff
    public PowersLyinEyes(LivingEntity self) {super(self);}
    @Override
    public boolean canSummonStandAsEntity() {return false;}
    @Override
    public boolean isSecondaryStand(){return true;}
    @Override
    public boolean rendersPlayer(){return true;}
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersLyinEyes(entity);
    }

    // stand summoning start

    @Override
    public void onStandSummon(boolean desummon) {
        if (!desummon && this.self.level().isClientSide()) {
            summonOverlayTicks = 1;
            if(!visionModeClient && !mapModeClient) {
                isSummon = true;
            }
        }
        if (desummon && this.self.level().isClientSide()) {
            isSummon = false;
            desummonFromMove = false;
        }
        super.onStandSummon(desummon);
    }

    // stand summoning end

    // tick power start

    @Override
    public void tickPower() {
        mapOpacity = Mth.approach(mapOpacity, mapModeClient ? 1.0F : 0.0F, 0.20F);
        if (this.self.level().isClientSide()) {
            //inventory tracking for animation oooooh
            boolean isMenuOpen = isInventoryMenuOpen();
            if (isMenuOpen) {
                hadInventoryMenuOpen = true;
                isGather = true;
            } else if (hadInventoryMenuOpen) {
                hadInventoryMenuOpen = false;
                if (!mapModeClient) {
                    isGather = false;
                }
                if (!visionModeClient || !mapModeClient) {
                    isSummon = true;
                }
            }
        }
        super.tickPower();

        if (isSummon) {
            summonOverlayTicks = Math.min(summonOverlayTicks + 1, 12);
            // once summoned, idle anim.
            if (summonOverlayTicks >= 12) {
                idleOverlayTicks++;
            } else {
                idleOverlayTicks = 0;
            }
        } else {
            // if desummoned through a move, update every tick instead of every other tick
            int decrement = desummonFromMove ? 2 : 1;
            summonOverlayTicks = Math.max(summonOverlayTicks - decrement, 0);
            idleOverlayTicks = 0;
        }
        if (isGather) {
            gatherOverlayTicks = Math.min(gatherOverlayTicks + 1, 7);
        } else {
            gatherOverlayTicks = Math.max(gatherOverlayTicks - 1, 0);
        }
    }

    // tick power end

    // moves start

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        // inventory reading
        if (slot == 4) {
            if (!isPlayerDetected()) {
                return true;
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    /**
     * Gets the input for the power based on the buttons pressed.
     * Note, this is client side only. DO NOT FORGET!!!!
     *
     * @param context What button combination was pressed.
     */
    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            // entity highlighter
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                switchVisionClient();
            }
            // minimap
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                tryOpenMap();
            }
            // dash
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            // inventory reader
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                tryReadInventory();
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int value) {
        // inventory open thingy
        if (move == FINISH_READ_INVENTORY) {
            return readInventory(value);
        }
        return super.tryIntPower(move, forced, value);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> {
                if (this.self.level().isClientSide()) {
                    switchVision();
                } else {
                    //need an extra toggle here so it gets updated on the server
                    //used for the villager passive thingy
                    visionModeClient = !visionModeClient;
                }
            }
            case PowerIndex.POWER_2 -> {
                if (this.self.level().isClientSide()) {
                    switchMap();
                }
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateIntMove(int in) {
        super.updateIntMove(in);
    }

    //used with attackTimeDuring to activate moves when it hits 0
    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == READ_INVENTORY) {
            completeReadInventory();
        }
        super.updateUniqueMoves();
    }

    // inventory reading start

    // used for visually inept
    private boolean isPlayerDetected() {
        Entity target = getTargetEntity(self, 20);
        return target instanceof Player;
    }

    // client side power only, since inventories are client side, probably.
    private void tryReadInventory() {
        this.inventoryTarget = getTargetEntity(self, 20);
        if (this.inventoryTarget instanceof Player p) {
            this.setAttackTimeDuring(0);
            this.setActivePower(READ_INVENTORY);
            isSummon = false;
            desummonFromMove = true;
            isGather = true;
            gatherOverlayTicks = 1;
            // play sound here
            this.self.playSound(ModSounds.LYIN_EYES_GATHER_EVENT, 0.9F, 1.2F);
        }
    }

    // complete read inventory
    private void completeReadInventory() {
        if (this.getAttackTimeDuring() > 5) {
            if (this.inventoryTarget instanceof Player p) {
                tryIntPowerPacket(FINISH_READ_INVENTORY, this.inventoryTarget.getId());
            }
        }
    }

    // gets the player id from the server and opens up the menu
    private boolean readInventory(int playerID) {
        if (!this.self.level().isClientSide() && self instanceof ServerPlayer serverPlayer) {
            //if (this.getSelf() instanceof Player self) {
            //    serverPlayer.displayClientMessage(Component.literal("AHHHH PLAYER DETECTED AHHHHH PANIC AHHH!!!!"), false);
            //}
            Entity target = serverPlayer.level().getEntity(playerID);
            if (target instanceof ServerPlayer targetPlayer) {
                LyinEyesInventoryMenu.open(serverPlayer, targetPlayer);
            }
        }
        return true;
    }

    // inventory reading stop

    // tension highlighter start

    private void switchVisionClient(){
        this.tryPower(PowerIndex.POWER_1, true);
        tryPowerPacket(PowerIndex.POWER_1);
        if (isClient() && visionModeClient) {
            // play sound here
            this.self.playSound(ModSounds.LYIN_EYES_VISION_EVENT, 0.85F, 1F);
        }
    }

    private void switchVision(){
        if (isClient() && this.self instanceof Player PE) {
            if (!visionModeClient) {
                visionModeClient = true;
                isSummon = false;
                desummonFromMove = true;
                PE.displayClientMessage(Component.translatable("text.roundabout.lyin_eyes.tension_highlight").withStyle(ChatFormatting.GOLD), true);
            }
            else{
                visionModeClient = false;
                if (!mapModeClient) {
                    isSummon = true;
                }
                PE.displayClientMessage(Component.translatable("text.roundabout.lyin_eyes.tension_highlight_off").withStyle(ChatFormatting.GOLD), true);
            }
        }
    }

    // tension highlighter end

    // map start

    private float mapOpacity = 0.0F;

    private void tryOpenMap() {
        this.tryPower(PowerIndex.POWER_2, true);
        tryPowerPacket(PowerIndex.POWER_2);
        if (isClient() && mapModeClient) {
            // play sound here
            this.self.playSound(ModSounds.LYIN_EYES_GATHER_EVENT, 0.9F, 1.2F);
        }
    }

    private void switchMap() {
        if (!mapModeClient) {
            mapModeClient = true;
            isSummon = false;
            mapModeClient = true;
            desummonFromMove = true;
            gatherOverlayTicks = 1;
            isGather = true;
        }
        else{
            mapModeClient = false;
            if (!visionModeClient) {
                isSummon = true;
            }
            isGather = false;
        }
    }

    // map end

    // for tracking open menus

    private boolean isInventoryMenuOpen() {
        if (this.self instanceof Player player) {
            return player.containerMenu instanceof LyinEyesInventoryMenu;
        }
        return false;
    }

    // moves end

    // stand icons and info screen start

    /**
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if(visionModeClient) {
            setSkillIcon(context, x, y, 1, StandIcons.LYIN_EYES_TENSION_SENSOR_ON, PowerIndex.SKILL_1);
        }
        else {
            setSkillIcon(context, x, y, 1, StandIcons.LYIN_EYES_TENSION_SENSOR, PowerIndex.SKILL_1);
        }
        if(mapModeClient) {
            setSkillIcon(context, x, y, 2, StandIcons.LYIN_EYES_MAP_ON, PowerIndex.SKILL_2);
        }
        else {
            setSkillIcon(context, x, y, 2, StandIcons.LYIN_EYES_MAP_OFF, PowerIndex.SKILL_2);
        }
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.LYIN_EYES_INVENTORY, PowerIndex.SKILL_4);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
                                                  int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.lyin_eyes_tension_sensor",
                "instruction.roundabout.press_skill", StandIcons.LYIN_EYES_TENSION_SENSOR, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.lyin_eyes_callout",
                "instruction.roundabout.passive", StandIcons.LYIN_EYES_PASSIVE, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.lyin_eyes_map",
                "instruction.roundabout.press_skill", StandIcons.LYIN_EYES_MAP_OFF, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.lyin_eyes_inventory",
                "instruction.roundabout.press_skill", StandIcons.LYIN_EYES_INVENTORY, 4, level, bypas));
        return $$1;
    }

    // stand icons and info screen end

    // sounds start

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.LYIN_EYES_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    // sounds end

    //skins and poses

    @Override
    public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.lyin_eyes.base");
    }

    @Override
    public Component getPosName(byte posID){
        return switch (posID) {
            case (byte) 1 -> Component.translatable("idle.roundabout.lyin_eyes_2");
            case (byte) 2 -> Component.translatable("idle.roundabout.lyin_eyes_3");
            case (byte) 3 -> Component.translatable("idle.roundabout.lyin_eyes_4");
            default -> Component.translatable("idle.roundabout.lyin_eyes_1");
        };
    }
    
    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte)0);
        $$1.add((byte)1);
        $$1.add((byte)2);
        $$1.add((byte)3);
        return $$1;
    }

    // mob and other stuff AI

    // wtf would lyin eyes even do for mobs lmao
    // i just made it so that their detection range is increased by 20 blocks if they have it.
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (!this.self.level().isClientSide()) {
            net.minecraft.world.entity.ai.attributes.AttributeInstance followRangeAttr =
                    this.self.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE);

            if (followRangeAttr != null && !followRangeAttr.hasModifier(DETECTION_BOOST)) {
                followRangeAttr.addTransientModifier(DETECTION_BOOST);
            }
        }
    }

    // basic setters and getters here

    public int getSummonOverlayTicks() {return summonOverlayTicks;}
    public int getGatherOverlayTicks() {return gatherOverlayTicks;}
    public int getIdleOverlayTicks() { return idleOverlayTicks; }
    public boolean getVisionModeClient() {return visionModeClient;}
    public boolean isActive() {return hasStandActive(this.self);}
    public boolean getMapModeClient() {return mapModeClient;}
    public float getMapOpacity() {return mapOpacity;}

    // WIP FEATURES BELOW HERE, DELETE WHEN DONE

    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("88superguy").withStyle(ChatFormatting.GOLD);
    }
}
