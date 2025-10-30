package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AbilityScapeBasis {
    /**Note that self refers to the entity with the power*/
    public final LivingEntity self;

    public AbilityScapeBasis(LivingEntity self) {
        this.self = self;
    }

    // -----------------------------------------------------------------------------------------
    // UNDERSTANDING THE MAIN VARIABLES
    // -----------------------------------------------------------------------------------------

    /**The time that passed since using the last attack. It counts up, so that a visual meter can display cooldowns.
     * It is also used to */
    public int attackTime = -1;

    /**The time within an attack. This matters, because if you desummon a stand the attack time doesnt reset */
    public int attackTimeDuring = -1;

    /**The time until the generic ability cooldown passes.
     This exists so you have downtime that non-stand users can get it and attack you during.*/
    public int attackTimeMax = -1;

    /**The id of the move being used. Ex: 1 = punch*/
    public byte activePower = 0;

    /**The phase of the move being used, primarily to keep track of which punch you are on in a punch string.*/
    public byte activePowerPhase = 0;

    /**This is when the punch combo goes on cooldown. Default is 3 hit combo.*/
    public final byte activePowerPhaseMax = 3;

    /**This variable exists so that a client can begin displaying your attack hud info without ticking through it.
     * Basically, stand attacks are clientside, but they need the server's confirmation to kickstart so you
     * can't hit targets in frozen tps*/
    public boolean kickStarted = true;

    /**This plays automatically when a power is changed on the server to sync it with the client*/
    public void kickStartClient(){
        this.kickStarted = true;
    }


    public void updateMovesFromPacket(byte activePower){

    }

    /**If the cooldown slot is to be controlled by the server, return true. Consider using this if
     * bad TPS makes a stand ability actually overpowered for the client to handle the recharging of.*/
    public boolean isServerControlledCooldown(CooldownInstance ci, byte num){
        return false;
    }
    /**If you stand still enough, abilities recharge faster. But this could be overpowered for some abilties, so
     * use discretion and override this to return false on abilities where this might be op.*/
    public boolean canUseStillStandingRecharge(byte bt){
        return true;
    }


    public boolean getReducedDamage(Entity entity){
        return (entity instanceof Player || entity instanceof StandEntity ||
                ((entity instanceof LivingEntity LE && !((StandUser)LE).roundabout$getStandDisc().isEmpty()) &&
                        ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.standUserMobsTakePlayerDamageMultipliers)
        );
    }

    /**If your character is in a position to change abilities. By default, you are locked into clashing while clashing
     * unless you forfeit the clash by desummoning your stand*/
    public boolean canChangePower(int move, boolean forced){
        if ((this.activePower == PowerIndex.NONE || forced) &&
                (!this.isDazed(this.self))) {
            return true;
        }
        return false;
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    /** There is no reason for the function to be a boolean, that goes unused, so gradually we can convert this to
     * a void function*/
    public void tryPower(int move){
        tryPower(move,true);
    }
    public boolean tryPower(int move, boolean forced){
        if (canChangePower(move, forced)) {
            if (move == PowerIndex.NONE) {
                this.setPowerNone();
            } else if (move == PowerIndex.MOVEMENT) {
                this.setPowerMovement(move);
            } else {
                this.setPowerOther(move,this.getActivePower());
            }
        }
        return false;
    }

    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        return false;
    }

    /**Sets your active power to nothing*/
    public boolean setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        return true;
    }


    public boolean tryPosPower(int move, boolean forced, Vec3 pos){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockhit){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }

    public int storedInt = 0;
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        tryPower(move, forced);
        if (this.canChangePower(move, forced)) {
            if (move == PowerIndex.MOVEMENT) {
                this.storedInt = chargeTime;
            }
        }
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryTripleIntPower(int move, boolean forced, int chargeTime, int move2, int move3){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }

    public void tryPowerPacket(byte packet){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPowerPacket(packet);
        }
    }
    public void tryIntPowerPacket(byte packet, int integer){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryIntPowerPacket(packet,integer);
        }
    }
    /**This is different than int power packet only by virtue of what functions it passes through, and is useful
     * for calling something even if you are in a barrage clash or other conditions would otherwise interrupt your
     * packet. Very niche, but it exists, and isn't always used in essential ways*/
    public void tryIntToServerPacket(byte packet, int integer){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.intToServerPacket(packet,integer);
        }
    }

    public void tryTripleIntPacket(byte packet, int in1, int in2, int in3){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryTripleIntPacket(packet, in1, in2, in3);
        }
    }
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerPacket(packet, pos);
        }
    }
    public void tryBlockPosPowerPacket(byte packet, BlockPos pos, HitResult hitResult){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryBlockPosPowerPacket(packet, pos, hitResult);
        }
    }
    public void tryPosPowerPacket(byte packet, Vec3 pos){
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.tryPosPowerPacket(packet, pos);
        }
    }
    public Vec3 savedPos;

    /**The most basic getters and setters*/
    public StandUser getStandUserSelf(){
        return ((StandUser)this.self);
    }

    public int getAttackTime(){
        return this.attackTime;
    }
    public int getAttackTimeDuring(){
        return this.attackTimeDuring;
    }
    public byte getActivePower(){
        return this.activePower;
    }
    public byte getActivePowerPhase(){
        return this.activePowerPhase;
    }
    public byte getActivePowerPhaseMax(){
        return this.activePowerPhaseMax;
    }

    public void setAttackTime(int attackTime){
        this.attackTime = attackTime;
    }
    public void setAttackTimeDuring(int attackTimeDuring){
        this.attackTimeDuring = attackTimeDuring;
    }
    public void setAttackTimeMax(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public int getAttackTimeMax(){
        return this.attackTimeMax;
    }

    public void setMaxAttackTime(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public void setActivePower(byte activeMove){
        this.activePower = activeMove;
    }
    public void setActivePowerPhase(byte activePowerPhase){
        this.activePowerPhase = activePowerPhase;
    }



    public void xTryPower(byte index, boolean forced){
    }
    public void tryPowerStuff(){
        syncActivePower();
        if (this.self.level().isClientSide) {
            kickStarted = false;
        }
    }

    /**Stuff that happens every tick while possessing the stand in general.
     * Remember to call the super when you override or some things might not function properly*/
    public void tickPower(){

        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (impactSlowdown >= -1){
                impactSlowdown--;
            }
            if (impactBrace) {
                if (((StandUser) this.getSelf()).roundabout$getActive()) {
                    if (this.getSelf().onGround()) {
                        impactBrace = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.FALL_BRACE_FINISH, true);
                        if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                            tryPowerPacket(PowerIndex.FALL_BRACE_FINISH);
                        }
                    } else if (this.getSelf().isInWater() || this.getSelf().hasEffect(MobEffects.LEVITATION)) {
                        impactSlowdown = -1;
                        impactBrace = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                        if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                            tryPowerPacket(PowerIndex.NONE);
                        }
                    } else {
                        if (impactAirTime > -1) {
                            impactAirTime--;
                        }
                        impactSlowdown = 15;
                        if (impactAirTime > -1 || this.getSelf().tickCount % 2 == 0) {
                            this.getSelf().fallDistance -= 1;
                            if (this.getSelf().fallDistance < 0) {
                                this.getSelf().fallDistance = 0;
                            }
                        }
                    }
                } else {
                    impactSlowdown = -1;
                    impactBrace = false;
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                        tryPowerPacket(PowerIndex.NONE);
                    }
                }
            }
        }
        baseTickPower();
    }


    /**This function ensures the client sending attack packets is ONLY the player using the attack, prevents double attacking*/
    public boolean isPacketPlayer(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return mc.player != null && mc.player.getId() == this.self.getId();
        }
        return false;
    }

    public void syncActivePower(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer SP){
            S2CPacketUtil.sendActivePowerPacket(SP,activePower);
        }
    }

    public void baseTickPower(){
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.self.level().isClientSide){
                if (!this.kickStarted && this.getAttackTimeDuring() <= -1){
                    this.kickStarted = true;
                }
            }
            if (!this.self.level().isClientSide || kickStarted) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    if (this.attackTimeDuring == -1) {
                        xTryPower(PowerIndex.NONE,true);
                    } else {
                        if (!this.isAttackInept(this.activePower)) {
                            if (this.activePower == PowerIndex.ATTACK) {
                                this.updateAttack();
                            } else {
                                this.updateUniqueMoves();
                            }
                        } else {
                            resetAttackState();
                        }
                    }
                }
                this.attackTime++;
                if (this.attackTime > this.attackTimeMax) {
                    this.setActivePowerPhase((byte) 0);
                }
                if (this.interruptCD > 0) {
                    this.interruptCD--;
                }
            }
            this.tickDash();
        }
        if (this.self.level().isClientSide) {
            tickSounds();
        }
        if (this.scopeLevel != 0 && !this.canScope()){
            setScopeLevel(0);
            this.scopeTime = -1;
        }
    }
    /**plays every tick that the active power is set to X move, the unique moves lets you do your own packets,
     * see examples*/
    public void updateUniqueMoves(){
    }

    /**same as above but for the standard attack packet*/
    public void updateAttack(){
    }

    /**Override this if you want to add or remove conditions that prevent moves from updating and shut
     * them down*/
    public boolean isAttackInept(byte activeP){
        return this.self.isUsingItem() || this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }

    /**If doing something like eating, cancels attack state*/
    public void resetAttackState(){
        if (shouldReset(this.getActivePower())){
            this.interruptCD = 3;
            
            xTryPower(PowerIndex.NONE,true);
        }
    }

    /**If eating or using items in general shouldn't cancel certain abilties, put them as exceptions here*/
    public boolean shouldReset(byte activeP){
        return (this.self.isUsingItem() &&
                !(this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))));
    }
    public int interruptCD = 0;
    public boolean getInterruptCD(){
        return this.interruptCD <= 0;
    }
    public void setInterruptCD(int interruptCD){
        this.interruptCD = interruptCD;
    }



    public static final byte
            NONE = 0;

    /**Sound updates that play every tick*/
    public void tickSounds(){
        if (this.self.level().isClientSide) {
            ((StandUserClient) this.self).roundabout$clientPlaySound();
            ((StandUserClient) this.self).roundabout$clientSoundCancel();
        }
    }

    /**Does your stand let you zoom in a lot? Override this if it does*/
    public boolean canScope(){
        return false;
    }
    public int scopeTime = -1;
    public int scopeLevel = 0;
    public void setScopeLevel(int level){
        if (scopeLevel <= 0 && level > 0){
            if (this.getSelf().level().isClientSide()){
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SCOPE);
            }
        } else if (scopeLevel > 0 && level <= 0){
            if (this.getSelf().level().isClientSide()){
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SCOPE_OFF);
            }
        }
        scopeLevel=level;
    }




    // -----------------------------------------------------------------------------------------
    // UI/CLIENT, + SKIN FUNCTIONS TO OVERRIDE
    // Be conscious of where you use client functions and arguments since this class can't accidentally
    // call client functions on a server without crashing
    // -----------------------------------------------------------------------------------------
    /**Override this to render stand icons, see examples from other stands*/
    public void renderIcons(GuiGraphics context, int x, int y) {
    }

    /**Use this to draw HUD elements, it is primarily for the middle HUD (attack cooldown bar that is
     * blue, white, and orange), see punchingstand for examples*/
    public void renderAttackHud(GuiGraphics context,  Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha){
    }

    /**A basic function called to draw stand icons*/
    public void setSkillIcon(GuiGraphics context, int x, int y, int slot, ResourceLocation rl, byte CDI){
        setSkillIcon(context,x,y,slot,rl,CDI,false);
    }


    public ResourceLocation getIconYes(int slot){
        return StandIcons.SQUARE_ICON;
    }

    public static final int squareHeight = 24;
    public static final int squareWidth = 24;
    public void setSkillIcon(GuiGraphics context, int x, int y, int slot, ResourceLocation rl, byte CDI, boolean locked){
        RenderSystem.enableBlend();
        context.setColor(1f, 1f, 1f, 1f);
        CooldownInstance cd = null;
        if (CDI >= 0 && !getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= CDI){
            cd = getPowerCooldowns().get(CDI);
        }
        x += slot * 25;
        y-=1;

        if (locked){
            RenderSystem.enableBlend();
            context.blit(StandIcons.LOCKED_SQUARE_ICON,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else {
            RenderSystem.enableBlend();
            context.blit(getIconYes(slot),x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
            Font renderer = Minecraft.getInstance().font;
            if (slot==4){
                Component special4Key = KeyInputRegistry.abilityFourKey.getTranslatedKeyMessage();
                special4Key = fixKey(special4Key);
                context.drawString(renderer, special4Key,x-1,y+11,0xffffff,true);
            }
            else if (slot==3){
                Component special3Key = KeyInputRegistry.abilityThreeKey.getTranslatedKeyMessage();
                special3Key = fixKey(special3Key);
                context.drawString(renderer, special3Key,x-1,y+11,0xffffff,true);
            }
            else if (slot==2){
                Component special2Key = KeyInputRegistry.abilityTwoKey.getTranslatedKeyMessage();
                special2Key = fixKey(special2Key);
                context.drawString(renderer, special2Key,x-1,y+11,0xffffff,true);
            }
            else if (slot==1){
                Component special1Key = KeyInputRegistry.abilityOneKey.getTranslatedKeyMessage();
                special1Key = fixKey(special1Key);
                context.drawString(renderer, special1Key,x-1,y+11,0xffffff,true);
            }
            Component special1Key = KeyInputRegistry.abilityOneKey.getTranslatedKeyMessage();
        }


        if ((cd != null && (cd.time >= 0)) || isAttackIneptVisually(CDI,slot)){
            RenderSystem.enableBlend();
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
            if ((cd != null && (cd.time >= 0))) {
                float blit = (20*(1-((float) (1+cd.time) /(1+cd.maxTime))));
                int b = (int) Math.round(blit);
                RenderSystem.enableBlend();
                context.setColor(1f, 1f, 1f, 1f);

                ResourceLocation COOLDOWN_TEX = StandIcons.COOLDOWN_ICON;

                if (cd.isFrozen())
                    COOLDOWN_TEX = StandIcons.FROZEN_COOLDOWN_ICON;

                context.blit(COOLDOWN_TEX, x - 1, y - 1 + b, 0, b, 20, 20-b, 20, 20);
                int num = ((int)(Math.floor((double) cd.time /20)+1));
                int offset = x+3;
                if (num <=9){
                    offset = x+7;
                }

                if (!cd.isFrozen())
                    context.drawString(Minecraft.getInstance().font, ""+num,offset,y,0xffffff,true);

            }
            context.setColor(1f, 1f, 1f, 0.9f);
        } else {
            RenderSystem.enableBlend();
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
        }
    }


    /**This function grays out icons for moves you can't currently use. Slot is the icon slot from 1-4,
     * activeP is your currently active power*/
    public boolean isAttackIneptVisually(byte activeP, int slot){
        return this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }




    // -----------------------------------------------------------------------------------------
    // Functions you will NOT need to override
    // And will never be needed by you
    // BASICALLY YOU CAN IGNORE EVERYTHING HERE
    // -----------------------------------------------------------------------------------------

    public AbilityIconInstance drawSingleGUIIcon(GuiGraphics context, int size, int startingLeft, int startingTop, int levelToUnlock,
                                                 String nameSTR, String instructionStr, ResourceLocation draw, int extra, byte level, boolean bypass){
        Component name;
        if (level < levelToUnlock && !bypass) {
            context.blit(StandIcons.LOCKED_SQUARE_ICON, startingLeft, startingTop, 0, 0,size, size, size, size);
            context.blit(StandIcons.LOCKED, startingLeft+2, startingTop+2, 0, 0,size-4, size-4, size-4, size-4);
            name = Component.translatable("ability.roundabout.locked").withStyle(ChatFormatting.BOLD).
                    withStyle(ChatFormatting.DARK_GRAY);
        } else {
            context.blit(StandIcons.SQUARE_ICON, startingLeft, startingTop, 0, 0,size, size, size, size);
            context.blit(draw, startingLeft+2, startingTop+2, 0, 0,size-4, size-4, size-4, size-4);
            name = Component.translatable(nameSTR).withStyle(ChatFormatting.BOLD).
                    withStyle(ChatFormatting.DARK_PURPLE);
        }
        Component instruction;
        if (level < levelToUnlock && !bypass){
            instruction = Component.translatable("ability.roundabout.locked.ctrl").
                    withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED);
        } else {
            if (extra <= 0) {
                instruction = Component.translatable(instructionStr).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.BLUE);
            } else {
                instruction = Component.translatable(instructionStr, "" + extra).withStyle(ChatFormatting.ITALIC).
                        withStyle(ChatFormatting.BLUE);

            }
        }
        Component description;
        if (level < levelToUnlock && !bypass){
            description = Component.translatable("ability.roundabout.locked.desc", "" + levelToUnlock).
                    withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED);
        } else {
            description = Component.translatable(nameSTR+".desc");
        }
        return new AbilityIconInstance(size,startingLeft,startingTop,levelToUnlock,
                name,instruction,description,extra);
    }

    /**Code for the button that switches your ability row*/
    public boolean heldDownSwitch = false;
    public void switchRowsKey(boolean keyIsDown, Options options) {
        if (!heldDownSwitch) {
            if (keyIsDown) {
                heldDownSwitch = true;
                if (isHoldingSneakToggle) {
                    isHoldingSneakToggle = false;
                } else {
                    isHoldingSneakToggle = true;
                }
            }
        } else {
            if (!keyIsDown) {
                heldDownSwitch = false;
            }
        }
    }
    /**Related code to the above*/
    public boolean isHoldingSneakToggle = false;
    public boolean isHoldingSneak(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return ((mc.options.keyShift.isDown() && !isHoldingSneakToggle) || (isHoldingSneakToggle && !mc.options.keyShift.isDown()));
        }
        return this.self.isCrouching();
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        return $$1;
    }


    public static Component fixKey(Component textIn){

        String X = textIn.getString();
        if (X.length() > 1){
            String[] split = X.split("\\s");
            if (split.length > 1){
                return Component.nullToEmpty(""+split[0].charAt(0)+split[1].charAt(0));
            } else {
                if (split[0].length() > 1){
                    return Component.nullToEmpty(""+split[0].charAt(0)+split[0].charAt(1));
                } else {
                    return Component.nullToEmpty(""+split[0].charAt(0));
                }
            }
        } else {
            return textIn;
        }
    }
    public LivingEntity getSelf(){
        return this.self;
    }

    /**Does the casting to stand user for you, will always work because this class needs a livingentity to exist*/
    public StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }

    /**What side are we on?*/
    public boolean isClient(){
        return this.getSelf().level().isClientSide();
    }

    /**Just time stop barrage canceling when the time stop expires*/
    public boolean bonusBarrageConditions(){
        return true;
    }

    /**Your stand's generalized cooldowns*/
    public List<CooldownInstance> getPowerCooldowns(){
        if (this.self != null){
            return getUserData(this.self).rdbt$getPowerCooldowns();
        }
        return new ArrayList<>();
    }

    public void setPowerCooldowns(List<CooldownInstance> powerCooldowns){
        if (this.self != null){
            getUserData(this.self).rdbt$setPowerCooldowns(powerCooldowns);
        }
    }

    public void syncAllCooldowns(){
        try {
            if (this.self instanceof ServerPlayer sp) {
                byte cin = -1;
                for (CooldownInstance ci : getPowerCooldowns()){
                    cin++;
                    S2CPacketUtil.sendMaxCooldownSyncPacket(sp, cin, ci.time, ci.maxTime);
                }
            }
        } catch (Exception e){
            //I very much doubt this will error
            Roundabout.LOGGER.info("???");
        }
    }



    /**Functions to check/set barrage daze*/
    public boolean isDazed(LivingEntity entity){
        return this.getUserData(entity).roundabout$isDazed();
    }
    public void setDazed(LivingEntity entity, byte dazeTime){
        if ((1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            /*Warden, iron golems, and anything else knockback immmune can't be dazed**/
            return;
        } else if (MainUtil.isBossMob(entity)){
            /*Bosses can't be dazed**/
            return;
        }

        /**Stand Drops item when user is dazed*/
        StandEntity stand = getStandEntity(entity);
        if (stand != null && !stand.getHeldItem().isEmpty()){
            double $$3 = stand.getEyeY() - 0.3F;
            ItemEntity $$4 = new ItemEntity(stand.level(), stand.getX(), $$3, stand.getZ(), stand.getHeldItem());
            $$4.setPickUpDelay(40);
            $$4.setThrower(stand.getUUID());
            stand.level().addFreshEntity($$4);
            stand.setHeldItem(ItemStack.EMPTY);
        }
        if (dazeTime > 0){
            ((StandUser) entity).roundabout$tryPower(PowerIndex.NONE,true);
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.HURT_BY_BARRAGE);
        } else {
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.IDLE);
        }
        this.getUserData(entity).roundabout$setDazed(dazeTime);
    }
    public void setDazedSafely(LivingEntity entity, byte dazeTime){
        if (dazeTime > 0){
            ((StandUser) entity).roundabout$tryPower(PowerIndex.NONE,true);
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.HURT_BY_BARRAGE);
        } else {
            ((StandUser) entity).roundabout$getStandPowers().animateStand(StandEntity.IDLE);
        }
        this.getUserData(entity).roundabout$setDazed(dazeTime);
    }


    /**If you have a stand entity summoned, get that*/
    public StandEntity getStandEntity(LivingEntity User){
        return this.getUserData(User).roundabout$getStand();
    } public boolean hasStandEntity(LivingEntity User){
        return this.getUserData(User).roundabout$hasStandOut();
    } public boolean hasStandActive(LivingEntity User){
        return this.getUserData(User).roundabout$getActive();
    }

    /**set an ability on cooldown*/
    public void setCooldown(byte power, int cooldown){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            getPowerCooldowns().get(power).time = cooldown;
            getPowerCooldowns().get(power).maxTime = cooldown;
        }
    }
    /**set an ability on cooldown, and change the max cooldown*/
    public void setCooldownMax(byte power, int cooldown, int maxCooldown){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            getPowerCooldowns().get(power).time = cooldown;
            getPowerCooldowns().get(power).maxTime = maxCooldown;
        }
    }

    /**Returns the cooldown for an ability*/
    public CooldownInstance getCooldown(byte power){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            return getPowerCooldowns().get(power);
        }
        return null;
    }

    /**Checks if an ability is currently on cooldown*/
    public boolean onCooldown(byte power){
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= power){
            return (getPowerCooldowns().get(power).time >= 0);
        }
        return false;
    }

    public boolean hasCooldowns(){
        List<CooldownInstance> CDCopy = new ArrayList<>(getPowerCooldowns()) {
        };
        for (byte i = 0; i < CDCopy.size(); i++){
            CooldownInstance ci = CDCopy.get(i);
            if (ci.time >= 0){
                return true;
            }
        }
        return false;
    }

    public void refreshCooldowns(){
        List<CooldownInstance> CDCopy = new ArrayList<>(getPowerCooldowns()) {
        };
        for (byte i = 0; i < CDCopy.size(); i++){
            CooldownInstance ci = CDCopy.get(i);
            ci.time = -1;
        }
        setPowerCooldowns(CDCopy);
    }


    public void tickDash(){
        if (this.getSelf() instanceof Player) {

            if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 0) {
                cancelConsumableItem(this.getSelf());
            }

            if (((IPlayerEntity)this.getSelf()).roundabout$getClientDodgeTime() >= 10){
                ((IPlayerEntity)this.getSelf()).roundabout$setClientDodgeTime(-1);
                if (!this.getSelf().level().isClientSide){
                    ((IPlayerEntity)this.getSelf()).roundabout$setDodgeTime(-1);
                    byte pos = ((IPlayerEntity)this.getSelf()).roundabout$GetPos();
                    if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD) {
                        ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.NONE);
                    }
                }
            } else if (((IPlayerEntity)this.getSelf()).roundabout$getClientDodgeTime() >= 0){
                ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(((IPlayerEntity) this.getSelf()).roundabout$getClientDodgeTime()+1);
            }

            if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 10){

                ((IPlayerEntity)this.getSelf()).roundabout$setDodgeTime(-1);
                byte pos = ((IPlayerEntity)this.getSelf()).roundabout$GetPos();
                if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD) {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.NONE);
                }
            } else if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 0){
                if (this.getSelf().level().isClientSide){
                    ((IPlayerEntity) this.getSelf()).roundabout$setDodgeTime(((IPlayerEntity) this.getSelf()).roundabout$getDodgeTime()+1);
                }
            }
        }
    }


    /**Call this to make yourself stop using an item*/
    public void cancelConsumableItem(LivingEntity entity){
        ItemStack itemStack = entity.getUseItem();
        Item item = itemStack.getItem();
        if (item.isEdible() || item instanceof PotionItem) {
            entity.releaseUsingItem();
            if (entity instanceof Player) {
                entity.stopUsingItem();
            }
        }
    }








    /**returns if you are using stand guard*/
    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
    }



    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    };

    private boolean held1 = false;
    private boolean held2 = false;
    private boolean held3 = false;
    private boolean held4 = false;

    public void buttonInput1(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held1)
                return;
            held1 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_1_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held1 = false;
        }
    }

    public void buttonInput2(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held2)
                return;
            held2 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_2_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held2 = false;
        }
    }

    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held3)
                return;
            held3 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_3_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held3 = false;
        }
    }

    public void buttonInput4(boolean keyIsDown, Options options) {
        if (keyIsDown)
        {
            if (held4)
                return;
            held4 = true;

            if (!isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_NORMAL);
                return;
            }
            if (isHoldingSneak() && !isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_CROUCH);
                return;
            }
            if (!isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_GUARD);
                return;
            }
            if (isHoldingSneak() && isGuarding())
            {
                powerActivate(PowerContext.SKILL_4_CROUCH_GUARD);
                return;
            }
        }
        else
        {
            held4 = false;
        }
    }


    public void preButtonInput4(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput4(keyIsDown, options);
            }
        }
    }
    public void preButtonInput3(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput3(keyIsDown, options);
            }
        }
    }

    public void preButtonInput2(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput2(keyIsDown, options);
            }
        }
    }

    public void preButtonInput1(boolean keyIsDown, Options options){
        if (!hasStandActive(this.getSelf())) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                ((StandUser) this.getSelf()).roundabout$setIdleTime(0);
                buttonInput1(keyIsDown, options);
            }
        }
    }


    @SuppressWarnings("deprecation")
    public boolean doVault(){
        if (!this.self.onGround()) {
            Vec3 vec3d = this.getSelf().getEyePosition(1);

            Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
            Vec3 vec3d2 = this.getSelf().getViewVector(1);
            Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
            BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));


            boolean logicCheck = blockHit.getBlockPos().getY()+1 > this.getSelf().getY();
            BlockPos aboveCheck = blockHit.getBlockPos().above();
            if (gravD != Direction.DOWN){
                BlockPos abv = blockHit.getBlockPos().relative(gravD.getOpposite());
                BlockPos att = blockHit.getBlockPos();

                Vec3 blockHitVec = RotationUtil.vecPlayerToWorld(new Vec3(abv.getX(),att.getY(),att.getZ()), gravD);
                Vec3 playerVec = RotationUtil.vecPlayerToWorld(this.getSelf().position(), gravD);
                aboveCheck = abv;
                logicCheck = blockHitVec.y+1 > playerVec.y;
            }

            if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && logicCheck
                    && !this.getSelf().level().getBlockState(aboveCheck).isSolid()) {
                if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                    /*Stand vaulting*/
                    this.setCooldown(PowerIndex.GLOBAL_DASH, ClientNetworking.getAppropriateConfig().generalStandSettings.vaultingCooldown);
                    double mag = this.getSelf().getPosition(0).distanceTo(
                            new Vec3(blockHit.getLocation().x, blockHit.getLocation().y, blockHit.getLocation().z)) * 1.68 + 1;
                    Vec3 vec3 = new Vec3(
                            (blockHit.getLocation().x - this.getSelf().getX()) / mag,
                            (blockHit.getLocation().y - this.getSelf().getY()) / mag,
                            (blockHit.getLocation().z - this.getSelf().getZ()) / mag
                    );
                    if (gravD != Direction.DOWN){
                        vec3 = RotationUtil.vecWorldToPlayer(vec3,gravD);
                    }

                    MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                            vec3.x,
                            0.35 + Math.max(vec3.y, 0),
                            vec3.z
                    );
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.VAULT, true);
                    tryPowerPacket(PowerIndex.VAULT);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean canVault(){
        if (this.self.onGround())
            return false;
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));

        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        boolean logicCheck = blockHit.getBlockPos().getY()+1 > this.getSelf().getY();
        BlockPos aboveCheck = blockHit.getBlockPos().above();
        if (gravD != Direction.DOWN){
            BlockPos abv = blockHit.getBlockPos().relative(gravD.getOpposite());
            BlockPos att = blockHit.getBlockPos();

            Vec3 blockHitVec = RotationUtil.vecPlayerToWorld(new Vec3(abv.getX(),att.getY(),att.getZ()), gravD);
            Vec3 playerVec = RotationUtil.vecPlayerToWorld(this.getSelf().position(), gravD);
            aboveCheck = abv;
            logicCheck = blockHitVec.y+1 > playerVec.y;
        }

        if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && logicCheck
                && !this.getSelf().level().getBlockState(aboveCheck).isSolid()){
            return true;
        } else {
            return false;
        }
    }

    public boolean fallBraceInit() {
        this.getSelf().fallDistance -= 20;
        if (this.getSelf().fallDistance < 0){
            this.getSelf().fallDistance = 0;
        }
        impactBrace = true;
        impactAirTime = 15;

        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.EXTRA);
        if (!this.getSelf().level().isClientSide()) {
            playFallBraceInitSound();
        }
        return true;
    }

    public void playFallBraceInitSound(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
    }

    public void playFallBraceImpactSounds(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.FALL_BRACE_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
    }
    public void playFallBraceImpactParticles(){
        ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                50, 1.1, 0.05, 1.1, 0.4);
        ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                30, 1, 0.05, 1, 0.4);
    }

    public boolean vaultOrFallBraceFails(){
        if (!doVault()){
            if (canFallBrace()) {
                doFallBraceClient();
            } else {
                return true;
            }
        }
        return false;
    }
    public void doFallBraceClient(){
        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
        tryPowerPacket(PowerIndex.EXTRA);
    }
    public boolean fallBrace() {
        impactBrace= false;
        if (this.getActivePower() == PowerIndex.EXTRA && this.attackTimeDuring >= 0) {

            cancelConsumableItem(this.getSelf());
            this.setAttackTimeDuring(-15);
            if (!this.getSelf().level().isClientSide()) {
                playFallBraceImpactParticles();
                playFallBraceImpactSounds();
                int degrees = (int) (this.getSelf().getYRot() % 360);
                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1.2F,
                        Mth.sin(degrees * ((float) Math.PI / 180)),
                        Mth.sin(-12 * ((float) Math.PI / 180)),
                        -Mth.cos(degrees * ((float) Math.PI / 180)));
            }
        }
        return true;
    }
    public boolean impactBrace = false;

    public int impactAirTime = -1;
    public int impactSlowdown = -1;
    public boolean canFallBrace(){
        return this.getSelf().fallDistance > 3 && impactSlowdown <= -1 && !((StandUser)this.self).roundabout$isBubbleEncased();
    }


    public boolean vault() {
        cancelConsumableItem(this.getSelf());
        this.setAttackTimeDuring(-7);
        this.setActivePower(PowerIndex.VAULT);
        this.getSelf().resetFallDistance();
        if (!this.getSelf().level().isClientSide()) {
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.8 + (Math.random() * 0.04)));
        }
        return true;
    }


    public boolean setPowerMovement(int lastMove) {
        if (this.getSelf() instanceof Player) {
            cancelConsumableItem(this.getSelf());
            this.setPowerNone();
            if (!this.getSelf().level().isClientSide()) {
                ((IPlayerEntity)this.getSelf()).roundabout$setClientDodgeTime(0);
                ((IPlayerEntity) this.getSelf()).roundabout$setDodgeTime(0);
                if (storedInt < 0) {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.DODGE_BACKWARD);
                } else {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.DODGE_FORWARD);
                }

                int degrees = (int) (this.getSelf().getYRot() % 360);
                int offset = switch (storedInt) { case 1 -> -90; case 2 -> -45; case -1 -> -135; case 3 -> 90; case 4 -> 45; case -2 -> 135; case -3 -> 180; default -> 0; };
                degrees = (degrees + offset) % 360;

                for (int i = 0; i < 3; i++){
                    float j = 0.1F;
                    if (i == 1){
                        degrees -= 20;
                    } else if (i == 2){
                        degrees += 40;
                    } else {
                        j = 0.2F;
                    }


                    Vec3 cvec = new Vec3(0,0.1,0);
                    Vec3 dvec = new Vec3(Mth.sin(degrees * ((float) Math.PI / 180))*0.3,
                            Mth.sin(-20 * ((float) Math.PI / 180))*-j,
                            -Mth.cos(degrees * ((float) Math.PI / 180))*0.3);
                    Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                    if (gravD != Direction.DOWN){
                        cvec = RotationUtil.vecPlayerToWorld(cvec,gravD);
                        dvec = RotationUtil.vecPlayerToWorld(dvec,gravD);
                    }

                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CLOUD,
                            this.getSelf().getX()+cvec.x, this.getSelf().getY()+cvec.y, this.getSelf().getZ()+cvec.z,
                            0,
                            dvec.x,
                            dvec.y,
                            dvec.z,
                            0.8);
                }
            }
        }
        if (!this.getSelf().level().isClientSide()) {
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }
    public boolean inputDash = false;

    public void dash(){
        Options options = Minecraft.getInstance().options;

        inputDash = true;
        if (this.getSelf().level().isClientSide) {
            if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (this.getSelf().onGround() && !this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                    byte forward = 0;
                    byte strafe = 0;
                    if (options.keyUp.isDown()) forward++;
                    if (options.keyDown.isDown()) forward--;
                    if (options.keyLeft.isDown()) strafe++;
                    if (options.keyRight.isDown()) strafe--;
                    int degrees = (int) (this.getSelf().getYRot() % 360);
                    int backwards = 0;

                    if (strafe > 0 && forward == 0) {
                        degrees -= 90;
                        degrees = degrees % 360;
                        backwards = 1;
                    } else if (strafe > 0 && forward > 0) {
                        degrees -= 45;
                        degrees = degrees % 360;
                        backwards = 2;
                    } else if (strafe > 0) {
                        degrees -= 135;
                        degrees = degrees % 360;
                        backwards = -1;
                    } else if (strafe < 0 && forward == 0) {
                        degrees += 90;
                        degrees = degrees % 360;
                        backwards = 3;
                    } else if (strafe < 0 && forward > 0) {
                        degrees += 45;
                        degrees = degrees % 360;
                        backwards = 4;
                    } else if (strafe < 0) {
                        degrees += 135;
                        degrees = degrees % 360;
                        backwards = -2;
                    } else if (forward < 0) {
                        degrees += 180;
                        degrees = degrees % 360;
                        backwards = -3;
                    }

                    int cdTime = ClientNetworking.getAppropriateConfig().generalStandSettings.dashCooldown;
                    if (this.getSelf() instanceof Player) {
                        ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(0);
                        if (options.keyJump.isDown()) {
                            cdTime = ClientNetworking.getAppropriateConfig().generalStandSettings.jumpingDashCooldown;
                        }
                    }
                    this.setCooldown(PowerIndex.GLOBAL_DASH, cdTime);
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.91F,
                            Mth.sin(degrees * ((float) Math.PI / 180)),
                            Mth.sin(-20 * ((float) Math.PI / 180)),
                            -Mth.cos(degrees * ((float) Math.PI / 180)));

                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.MOVEMENT, true);
                    tryIntPowerPacket(PowerIndex.MOVEMENT, backwards);
                }
            }
        }
    }
}
