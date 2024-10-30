package net.hydra.jojomod.event.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandPowers {
    /**StandPowers is a class that every stand has a variation of, override it
     * to define and tick through stand abilities and cooldowns.
     * Note that most generic STAND USER code is in a mixin to the livingentity class.*/

    /**Note that self refers to the stand user, and not the stand itself.*/
    public final LivingEntity self;

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


    public StandPowers(LivingEntity self) {
        this.self = self;
    }

    public StandPowers generateStandPowers(LivingEntity entity){
        return null;
    }

    public LivingEntity getSelf(){
        return this.self;
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
    public float getStandReach(){
        return this.standReach;
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
    public float getTimestopRange(){
        return 100;
    }
    public Component getStandName(){
        ItemStack disc = ((StandUser)this.getSelf()).roundabout$getStandDisc();
        if (!disc.isEmpty() && disc.getItem() instanceof StandDiscItem SDI){
            return SDI.getDisplayName2();
        }
        return Component.empty();
    }

    /**The cooldown for summoning. It is mostly clientside and doesn't have to be synced*/
    public int summonCD = 0;

    /**This updates when a punch is thrown, to stop the stand from throwing the same punch twice if the game lags*/
    private byte activePowerPhaseCheck = -1;

    private int chargedTSTicks = 0;
    public boolean hasActedInTS = false;

    public int storedInt = 0;

    public int getChargedTSTicks(){
        return this.chargedTSTicks;
    }
    public void setChargedTSTicks(int chargedTSSeconds){
        this.chargedTSTicks = chargedTSSeconds;
    }
    public int getMaxTSTime (){
        return 0;
    }
    public int getMaxChargeTSTime(){
        return 0;
    }
    public boolean getIsTsCharging(){
        return false;
    }
    public boolean getSummonCD(){
        return this.summonCD <= 0;
    } public void setSummonCD(int summonCD){
        this.summonCD = summonCD;
    } public int getSummonCD2(){
        return this.summonCD;
    }

    /**This value prevents you from resummoning/blocking to cheese the 3 hit combo's last hit faster*/
    public int interruptCD = 0;
    public boolean getInterruptCD(){
        return this.interruptCD <= 0;
    }
    public void setInterruptCD(int interruptCD){
        this.interruptCD = interruptCD;
    }

    public boolean canBeTimeStopped(){
        return ((TimeStop) this.self.level()).inTimeStopRange(this.self);
    }

    /**Override this to set the special move key press conditions*/
    public void buttonInput4(boolean keyIsDown, Options options){
    }

    public void buttonInput3(boolean keyIsDown, Options options){
    }

    public void buttonInput2(boolean keyIsDown, Options options){
    }

    public void buttonInput1(boolean keyIsDown, Options options){
    }

    public void preButtonInput4(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                buttonInput4(keyIsDown, options);
            }
        }
    }



    public int getMobRecoilTime(){
        return -30;
    }

    public boolean forwardBarrage = false;
    public void preButtonInput3(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                buttonInput3(keyIsDown, options);
            }
        }
    }

    public void preButtonInput2(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                buttonInput2(keyIsDown, options);
            }
        }
    }

    public void preButtonInput1(boolean keyIsDown, Options options){
        if (hasStandActive(this.getSelf()) && !this.isClashing()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                buttonInput1(keyIsDown, options);
            }
        }
    }

    public void updateGuard(boolean yeet){
        if (suspendGuard) {
            if (!yeet) {
                suspendGuard = false;
            }
        }
    }
    public boolean suspendGuard = false;
    public boolean cancelItemUse() {
        return false;
    }
    public boolean cancelCollision(Entity et) {
        return false;
    }

    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && !this.isBarraging() && !this.isClashing()) {
            ((StandUser)this.getSelf()).roundabout$tryPower(PowerIndex.GUARD,true);
            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }
    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf())) {
            buttonInputAttack(keyIsDown, options);
        }
    }
    public void preCheckButtonInputBarrage(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf())) {
            buttonInputBarrage(keyIsDown, options);
        }
    }
    public boolean preCheckButtonInputGuard(boolean keyIsDown, Options options) {
        if (hasStandActive(this.getSelf())) {
            return buttonInputGuard(keyIsDown, options);
        }
        return false;
    }
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (this.canAttack()) {
                this.tryPower(PowerIndex.ATTACK, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.ATTACK);
            }
        }
    }

    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (keyIsDown) {
            if (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax())) {
                this.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BARRAGE_CHARGE);
            }
        }
    }
    public void addEXP(int amt, LivingEntity ent){
        if (!((StandUser)ent).roundabout$getStandDisc().isEmpty() && (ent instanceof Monster ||
                ent instanceof NeutralMob)){
            addEXP(amt*5);
        } else {
            addEXP(amt);
        }
    }
    public void addEXP(int amt){
        if (this.getSelf() instanceof Player PE){
            StandUser user = ((StandUser) PE);
            ItemStack stack = ((StandUser) PE).roundabout$getStandDisc();
            if (!stack.isEmpty() && !(stack.getItem() instanceof MaxStandDiscItem)){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$addStandExp(amt);
            }
        }
    }

    public void levelUp(){
        if (!this.getSelf().level().isClientSide()){
            ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.END_ROD,
                    this.getSelf().getX(), this.getSelf().getY() + this.getSelf().getEyeHeight(), this.getSelf().getZ(),
                    20, 0.4, 0.4, 0.4, 0.4);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.LEVELUP_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
        }
    }

    public int scopeLevel = 0;

    public void setScopeLevel(int level){
        if (scopeLevel <= 0 && level > 0){
            if (this.getSelf().level().isClientSide()){
                ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_SCOPE);
            }
        } else if (scopeLevel > 0 && level <= 0){
            if (this.getSelf().level().isClientSide()){
                ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_SCOPE_OFF);
            }
        }
        scopeLevel=level;
    }
    public int scopeTime = -1;

    public boolean canScope(){
        return false;
    }

        public List<CooldownInstance> StandCooldowns = initStandCooldowns();

    public List<CooldownInstance> initStandCooldowns(){
        List<CooldownInstance> Cooldowns = new ArrayList<>();
        for (byte i = 0; i < 10; i++) {
            Cooldowns.add(new CooldownInstance(-1, -1));
        }
        return Cooldowns;
    }

    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        return $$1;
    }

    public Component getSkinName(byte skinId){
        return null;
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        return $$1;
    }

    public void setCooldown(byte power, int cooldown){
        if (!StandCooldowns.isEmpty() && StandCooldowns.size() >= power){
            StandCooldowns.get(power).time = cooldown;
            StandCooldowns.get(power).maxTime = cooldown;
        }
    }

    public CooldownInstance getCooldown(byte power){
        if (!StandCooldowns.isEmpty() && StandCooldowns.size() >= power){
            return StandCooldowns.get(power);
        }
        return null;
    }

    public boolean onCooldown(byte power){
        if (!StandCooldowns.isEmpty() && StandCooldowns.size() >= power){
            return (StandCooldowns.get(power).time >= 0);
        }
        return false;
    }

    public int iconSize = 18;
    public int iconSize2 = 16;
    /**Override this to render stand icons*/
    public void renderIcons(GuiGraphics context, int x, int y){
    }
    public void renderAttackHud(GuiGraphics context,  Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha){

    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos,byte level,boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        return $$1;
    }

    public boolean canExecuteMoveWithLevel(int minLevel){
        if (this.getSelf() instanceof Player pl){
            if (((IPlayerEntity)pl).roundabout$getStandLevel() >= minLevel || (!((StandUser) pl).roundabout$getStandDisc().isEmpty() &&
                    ((StandUser) pl).roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem) ||
                    pl.isCreative()){
                return true;
            }
            return false;
        }
        return true;
    }

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

    int squareHeight = 24;
    int squareWidth = 24;
    public void setSkillIcon(GuiGraphics context, int x, int y, int slot, ResourceLocation rl, byte CDI){
        setSkillIcon(context,x,y,slot,rl,CDI,false);
    }
    public void setSkillIcon(GuiGraphics context, int x, int y, int slot, ResourceLocation rl, byte CDI, boolean locked){
        CooldownInstance cd = null;
        if (CDI >= 0 && !StandCooldowns.isEmpty() && StandCooldowns.size() >= CDI){
            cd = StandCooldowns.get(CDI);
        }
        if (slot==4){x+=100;}
        else if (slot==3){x+=75;}
        else if (slot==2){x+=50;}
        else if (slot==1){x+=25;}
        y-=1;

        if (locked){
            context.blit(StandIcons.LOCKED_SQUARE_ICON,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else {
            context.blit(StandIcons.SQUARE_ICON,x-3,y-3,0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        }


        if ((cd != null && (cd.time >= 0)) || isAttackIneptVisually(CDI,slot)){
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
            if ((cd != null && (cd.time >= 0))) {
                float blit = (20*(1-((float) (1+cd.time) /(1+cd.maxTime))));
                int b = (int) Math.round(blit);
                RenderSystem.enableBlend();
                context.setColor(1f, 1f, 1f, 1f);
                context.blit(StandIcons.COOLDOWN_ICON, x - 1, y - 1 + b, 0, b, 20, 20-b, 20, 20);
                int num = ((int)(Math.floor((double) cd.time /20)+1));
                int offset = x+3;
                if (num <=9){
                    offset = x+7;
                }
                context.drawString(Minecraft.getInstance().font, ""+num,offset,y,0xffffff,true);
                RenderSystem.disableBlend();
            }
            context.setColor(1f, 1f, 1f, 0.9f);
        } else {
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
        }
    }


    /**Barrage sound playing and canceling involve sending a byte in a packet, then reading it from here on
     * the client level. */
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return this.getBarrageChargeSound();
        } else if (soundChoice == SoundIndex.GLAIVE_CHARGE) {
            return ModSounds.GLAIVE_CHARGE_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_STAR_PLATINUM_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_4) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_5) {
            return ModSounds.TWAU_TIMESTOP_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_7) {
            return ModSounds.OVA_LONG_TS_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_8) {
            return ModSounds.OVA_SP_TS_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_9) {
            return ModSounds.OVA_SHORT_TS_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE_2) {
            return ModSounds.OVA_TIME_RESUME_EVENT;
        } else if (soundChoice == SoundIndex.STAND_ARROW_CHARGE) {
            return ModSounds.STAND_ARROW_CHARGE_EVENT;
        }
        return null;
    }
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND){
            return this.getBarrageChargePitch();
        } else {
            return 1F;
        }
    }

    public float getSoundVolumeFromByte(byte soundChoice){
        if (soundChoice == TIME_STOP_NOISE) {
            return 0.7f;
        } else if (soundChoice == TIME_STOP_NOISE_4 || soundChoice == TIME_STOP_NOISE_5
                || soundChoice == TIME_STOP_NOISE_7
                || soundChoice == TIME_STOP_NOISE_8
                || soundChoice == TIME_STOP_NOISE_9) {
            return 0.7f;
        }
        return 1F;
    }
    protected Byte getSummonSound() {
        return -1;
    }

    public void playSummonSound() {
        if (this.self.isCrouching()){
            return;
        }
        playStandUserOnlySoundsIfNearby(this.getSummonSound(), 10, false,false);
    } //Plays the Summon sound. Happens when stand is summoned with summon key.



    /**Override this function for alternate rush noises*/
    public byte chooseBarrageSound(){
        return 0;
    }
    private float getBarrageChargePitch(){
        return 1/((float) this.getBarrageWindup() /20);
    }
    /**Realistically, you only need to override this if you're canceling sounds*/

    public ResourceLocation getBarrageCryID(){
        return ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID;
    }
    public SoundEvent getBarrageChargeSound(){
        return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
    }

    public static final byte TIME_STOP_NOISE = 40;
    public static final byte TIME_STOP_NOISE_2 = TIME_STOP_NOISE+1;
    public static final byte TIME_STOP_NOISE_3 = TIME_STOP_NOISE+2;
    public static final byte TIME_STOP_NOISE_4 = TIME_STOP_NOISE+3;
    public static final byte TIME_STOP_NOISE_5 = TIME_STOP_NOISE+4;
    public static final byte TIME_STOP_NOISE_6 = TIME_STOP_NOISE+5;
    public static final byte TIME_STOP_NOISE_7 = TIME_STOP_NOISE+6;
    public static final byte TIME_STOP_NOISE_8 = TIME_STOP_NOISE+7;
    public static final byte TIME_STOP_NOISE_9 = TIME_STOP_NOISE+8;
    public static final byte TIME_STOP_TICKING = TIME_STOP_NOISE+13;
    public static final byte TIME_RESUME_NOISE = 60;
    public static final byte TIME_RESUME_NOISE_2 = 61;
    public boolean glowingEyes(){
        return false;
    }
    public boolean fullTSChargeBonus(){
        return false;
    }
    public ResourceLocation getBarrageChargeID(){
        return ModSounds.STAND_BARRAGE_WINDUP_ID;
    }
    public Byte getLastHitSound(){
        return SoundIndex.NO_SOUND;
    }

    public ResourceLocation getLastHitID(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID;
    }

    public ResourceLocation getSoundID(byte soundNumber){
        if (soundNumber == SoundIndex.BARRAGE_CRY_SOUND) {
            return getBarrageCryID();
        } else if (soundNumber == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return getBarrageChargeID();
        }
        return null;
    }

    public boolean isAttackInept(byte activeP){
        return this.self.isUsingItem() || this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }
    public boolean isAttackIneptVisually(byte activeP, int slot){
        return this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }
    public void tickPowerEnd(){

    }


    public void setAirAmount(int airAmount){
    }
    public int getAirAmount(){
        return -1;
    }
    public int getMaxAirAmount(){
        return 300;
    }

    public void tickPower(){
        if (this.self instanceof Player PE && PE.isSpectator()) {
            ((StandUser) this.getSelf()).roundabout$setActive(false);
        }
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.self.level().isClientSide && !this.kickStarted && this.getAttackTimeDuring() <= -1){
                this.kickStarted = true;
            }
            if (this.isClashing()) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    this.updateClashing();
                }
            } else if (!this.self.level().isClientSide || kickStarted) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    if (this.attackTimeDuring == -1) {
                        ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE,true);
                    } else {
                        if (!this.isAttackInept(this.activePower)) {
                            if (this.activePower == PowerIndex.ATTACK) {
                                this.updateAttack();
                            } else if (this.isBarraging()) {

                                if (bonusBarrageConditions()) {
                                    if (this.isBarrageCharging()) {
                                        this.updateBarrageCharge();
                                    } else {
                                        this.updateBarrage();
                                    }
                                } else {
                                    ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
                                }
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
            this.tickCooldowns();
        }
        if (this.self.level().isClientSide) {
            tickSounds();
        }
        if (this.scopeLevel != 0 && !this.canScope()){
            setScopeLevel(0);
            this.scopeTime = -1;
        }
    }

    public void tickMobAi(){

    }
    public void tickDash(){

    }
    public void tickCooldowns(){
        int amt = 1;
        if (this.self instanceof Player) {
            int idle = ((StandUser) this.getSelf()).roundabout$getIdleTime();
            if (idle > 300) {
                amt *= 4;
            } else if (idle > 200) {
                amt *= 3;
            } else if (idle > 40) {
                amt *= 2;
            }
        }
        for (byte i = 0; i < StandCooldowns.size(); i++){
            CooldownInstance ci = StandCooldowns.get(i);
            if (ci.time >= 0){
                ci.time-=amt;
                if (ci.time < -1){
                    ci.time=-1;
                }
                if (this.self instanceof Player) {
                    if (((Player)this.self).isCreative() && ci.time > 2){
                        ci.time=2;
                    }
                }
            }
        }
    }


    public boolean hasCooldowns(){
        List<CooldownInstance> CDCopy = new ArrayList<>(StandCooldowns) {
        };
        for (byte i = 0; i < CDCopy.size(); i++){
            CooldownInstance ci = CDCopy.get(i);
            if (ci.time >= 0){
                return true;
            }
        }
        return false;
    }

    /**The manner in which your powers tick when you are being timestopped. Override this if the stand acts differently.
     * By technicality, you should still tick sounds.*/
    public void timeTick(){
        if (this.getSelf().level().isClientSide) {
            this.tickSounds();
        }
    }

    /**Ticks through your own timestop. This value exists in the general stand powers in case you switch stands.*/
    public void timeTickStopPower(){
    }

    /**A generic function which sends a float corresponding with an active power via packets to the client from the
     *  server or vice versa. An example of its usage is sending the time left on TS in the world stand via
     *  overriding this method and sending a packet*/
    public void updatePowerFloat(byte activePower, float data){
    }

    /**A generic function which sends an int corresponding with an active power via packets to the client from the
     *  server or vice versa. An example of its usage is sending the time left on TS in the world stand via
     *  overriding this method and sending a packet*/
    public void updatePowerInt(byte activePower, int data){
    }


    protected void tickSounds(){
        if (this.self.level().isClientSide) {
            ((StandUserClient) this.self).roundabout$clientPlaySound();
             ((StandUserClient) this.self).roundabout$clientSoundCancel();
        }
    }

    private int clashIncrement =0;
    private int clashMod =0;

    private void RoundaboutEnemyClash(){
        if (this.isClashing()) {
            if (this.clashIncrement < 0) {
                ++this.clashIncrement;
                if (this.clashIncrement == 0) {
                    this.setClashProgress(0.0f);
                }
            }
            ++this.clashIncrement;
            if (this.clashIncrement < (6 + this.clashMod)){
                this.setClashProgress(this.clashIncrement < 10 ?
                        (float) this.clashIncrement * 0.1f : 0.8f + 2.0f / (float) (this.clashIncrement - 9) * 0.1f);
            } else {
                this.setClashDone(true);
            }

        }
    }

    public void breakClash(LivingEntity winner, LivingEntity loser){
        if (StandDamageEntityAttack(loser, this.getClashBreakStrength(loser), 0.0001F, winner)) {
            ((StandUser)winner).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
            ((StandUser)loser).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
            ((StandUser)winner).roundabout$getStandPowers().playBarrageEndNoise(0, loser);
            this.takeDeterminedKnockbackWithY(winner, loser, this.getBarrageFinisherKnockback());
            ((StandUser)winner).roundabout$getStandPowers().animateStand((byte) 13);
            ((StandUser)loser).roundabout$tryPower(PowerIndex.NONE,true);
        }
    }
    public void TieClash(LivingEntity user1, LivingEntity user2){
        ((StandUser)user1).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        ((StandUser)user2).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        ((StandUser)user1).roundabout$getStandPowers().playBarrageEndNoise(0F,user2);
        ((StandUser)user2).roundabout$getStandPowers().playBarrageEndNoise(-0.05F,user1);

        user1.hurtMarked = true;
        user2.hurtMarked = true;
        user1.knockback(0.55f,user2.getX()-user1.getX(), user2.getZ()-user1.getZ());
        user2.knockback(0.55f,user1.getX()-user2.getX(), user1.getZ()-user2.getZ());
    }

    public boolean canInterruptPower(){
        return false;
    }


    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
            StandUser standUser = ((StandUser) this.getSelf());
            if (standUser.roundabout$isDazed()) {
                basis = 0;
            } else if (!(this.getSelf().getVehicle() != null && this.getSelf().getControlledVehicle() == null) &&
                    (standUser.roundabout$isGuarding() && this.getSelf().getVehicle() == null)) {
                basis*=0.3f;
            } else if (this.isBarrageAttacking() || standUser.roundabout$isClashing()) {
                    basis*=0.2f;
            } else if (this.isBarrageCharging()) {
                basis*=0.5f;
            }
        return basis;
    }

    public void updateClashing(){
        if (this.getStandEntity(this.self) != null) {
            //Roundabout.LOGGER.info("3 " + this.getStandEntity(this.self).getPitch() + " " + this.getStandEntity(this.self).getYaw());
        }
        if (this.getClashOp() != null) {
            if (this.attackTimeDuring <= 60) {
                LivingEntity entity = this.getClashOp();

                /*Rotation has to be set actively by both client and server,
                 * because serverPitch and serverYaw are inconsistent, client overwrites stand stuff sometimes*/
                LivingEntity standEntity = ((StandUser) entity).roundabout$getStand();
                LivingEntity standSelf = ((StandUser) self).roundabout$getStand();
                if (standSelf != null && standEntity != null) {
                    if (!this.self.level().isClientSide) {
                        standSelf.setXRot(getLookAtEntityPitch(standSelf, standEntity));
                        standSelf.setYRot(getLookAtEntityYaw(standSelf, standEntity));
                        standEntity.setXRot(getLookAtEntityPitch(standEntity, standSelf));
                        standEntity.setYRot(getLookAtEntityYaw(standEntity, standSelf));
                    }
                }


                if (!(this.self instanceof Player)) {
                    this.RoundaboutEnemyClash();
                }
                if (!this.self.level().isClientSide) {

                    if ((this.getClashDone() && ((StandUser) entity).roundabout$getStandPowers().getClashDone())
                    || !((StandUser) this.self).roundabout$getActive() || !((StandUser) entity).roundabout$getActive()) {
                        this.updateClashing2();
                    } else {
                        playBarrageNoise(this.attackTimeDuring+ clashStarter, entity);
                    }
                }
            } else {
                if (!this.self.level().isClientSide) {
                    this.updateClashing2();
                }
            }
        } else {
            if (!this.self.level().isClientSide) {
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.CLASH_CANCEL, true);
            }
        }
    }
    private void updateClashing2(){
        if (this.getClashOp() != null) {
            boolean thisActive = ((StandUser) this.self).roundabout$getActive();
            boolean opActive = ((StandUser) this.getClashOp()).roundabout$getActive();
            if (thisActive && !opActive){
                breakClash(this.self, this.getClashOp());
            } else if (!thisActive && opActive){
                breakClash(this.getClashOp(), this.self);
            } else if (thisActive && opActive){
                if ((this.getClashProgress() == ((StandUser) this.getClashOp()).roundabout$getStandPowers().getClashProgress())) {
                    TieClash(this.self, this.getClashOp());
                } else if (this.getClashProgress() > ((StandUser) this.getClashOp()).roundabout$getStandPowers().getClashProgress()) {
                    breakClash(this.self, this.getClashOp());
                } else {
                    breakClash(this.getClashOp(), this.self);
                }
            }
            ((StandUser) this.self).roundabout$setAttackTimeDuring(-10);
            ((StandUser) this.getClashOp()).roundabout$setAttackTimeDuring(-10);
            ((StandUser) this.self).roundabout$getStandPowers().syncCooldowns();
            ((StandUser) this.getClashOp()).roundabout$getStandPowers().syncCooldowns();
        }
    }
    public void updateBarrageCharge(){
        if (this.attackTimeDuring >= this.getBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.BARRAGE, true);
        }
    }
    public void updateBarrage(){
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getBarrageLength())
                                    * (getBarrageRecoilTime() - 1)));

                    standBarrageHit();
                }
            }
        }
    }
    public void updateAttack(){
    }

    public void poseStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setOffsetType(r);
        }
    }
    public void animateStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setAnimation(r);
        }
    }
    public byte getAnimation(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            return stand.getAnimation();
        }
        return -1;
    }

    public StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }
    public StandEntity getStandEntity(LivingEntity User){
        return this.getUserData(User).roundabout$getStand();
    } public boolean hasStandEntity(LivingEntity User){
        return this.getUserData(User).roundabout$hasStandOut();
    } public boolean hasStandActive(LivingEntity User){
        return this.getUserData(User).roundabout$getActive();
    }

    /**Edit this to apply special effect when stand virus is ravaging a mob with this stand.
     * Use the instance to time the effect appropriately*/
    public void tickStandRejection(MobEffectInstance effect){
    }
    public float standReach = 5;

    public int getTargetEntityId(){
        Entity targetEntity = getTargetEntity(this.self, -1);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }

    public int getTargetEntityId2(float distance){
        Entity targetEntity = getTargetEntity(this.self, distance);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }
    public int getTargetEntityId2(float distance,LivingEntity userr,float angle){
        Entity targetEntity = getTargetEntityGenerous(userr, distance,angle);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }
    public void standBarrageHit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                List<Entity> listE = getTargetEntityList(this.self,-1);
                int id = -1;
                if (storeEnt != null){
                    id = storeEnt.getId();
                }
                    ModPacketHandler.PACKET_ACCESS.StandBarrageHitPacket(id, this.attackTimeDuring);
                if (!listE.isEmpty()){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                            if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                ModPacketHandler.PACKET_ACCESS.StandBarrageHitPacket(listE.get(i).getId(), this.attackTimeDuring + 1000);
                            }
                        }
                    }
                }

                if (this.attackTimeDuring == this.getBarrageLength()){
                    this.attackTimeDuring = -10;
                }
            }
        } else {
            /*Caps how far out the barrage hit goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);

            List<Entity> listE = getTargetEntityList(this.self,-1);
                barrageImpact(storeEnt, this.attackTimeDuring);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                        if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                            barrageImpact(listE.get(i), this.attackTimeDuring + 1000);
                        }
                    }
                }
            }
        }
    }

    /**This function ensures the client sending attack packets is ONLY the player using the attack, prevents double attacking*/
    public boolean isPacketPlayer(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return mc.player != null && mc.player.getId() == this.self.getId();
        }
        return false;
    }
    //((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z,
    //        1,0.0, 0.0, 0.0,1);

    public boolean isDazed(LivingEntity entity){
        return this.getUserData(entity).roundabout$isDazed();
    }
    private void setDazed(LivingEntity entity, byte dazeTime){
        if ((1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            /*Warden, iron golems, and anything else knockback immmune can't be dazed**/
            return;
        } else if (entity instanceof EnderDragon || entity instanceof WitherBoss){
            /*Bosses can't be dazed**/
            return;
        }
        if (dazeTime > 0){
            ((StandUser) entity).roundabout$tryPower(PowerIndex.NONE,true);
            ((StandUser) entity).roundabout$getStandPowers().animateStand((byte) 14);
        } else {
            ((StandUser) entity).roundabout$getStandPowers().animateStand((byte) 0);
        }
        this.getUserData(entity).roundabout$setDazed(dazeTime);
    }

    public boolean knockShield(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= this.getUserData((LivingEntity) entity);
                    if (standUser.roundabout$isGuarding()) {
                        if (!standUser.roundabout$getGuardBroken()){
                            standUser.roundabout$breakGuard();
                        }
                    }
                    if (entity instanceof Player){
                         ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                         Item item = itemStack.getItem();
                         if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                             ((LivingEntity) entity).releaseUsingItem();
                             ((Player) entity).stopUsingItem();
                         }
                        ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                        entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                    }
                    return true;
                }
            }
        }
        return false;
    }

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

    public boolean knockShield2(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    if (entity instanceof Player){
                        ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                        Item item = itemStack.getItem();
                        if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                            ((LivingEntity) entity).releaseUsingItem();
                            ((Player) entity).stopUsingItem();
                        }
                        ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                        entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**Override these methods to fine tune the attack strength of the stand*/
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 1.75F;
        } else {
            return 5;
        }
    } public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2.5F;
        } else {
            return 7;
        }
    } public float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 8;
        }
    }
    public float getBarrageFinisherKnockback(){
        return 2.8F;
    }

    private float getClashBreakStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 4;
        } else {
            return 10;
        }
    }

    public float getBarrageHitStrength(Entity entity){
        float barrageLength = this.getBarrageLength();
        float power;
        if (this.getReducedDamage(entity)){
            power = 9/barrageLength;
        } else {
            power = 20/barrageLength;
        }
        /*Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth()){
                if (entity instanceof Player) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }
    public boolean getReducedDamage(Entity entity){
        return (entity instanceof Player || entity instanceof StandEntity ||
                (entity instanceof LivingEntity LE && !((StandUser)LE).roundabout$getStandDisc().isEmpty())
        );
    }

    /**Initiates a stand barrage clash. This code should probably not be overridden, it is a very mutual event*/
    public void initiateClash(Entity entity){
        ((StandUser) entity).roundabout$getStandPowers().setClashOp(this.self);
        ((StandUser) this.self).roundabout$getStandPowers().setClashOp((LivingEntity) entity);
        this.clashStarter = 0;
        ((StandUser) entity).roundabout$getStandPowers().clashStarter = 1;

        ((StandUser) entity).roundabout$tryPower(PowerIndex.BARRAGE_CLASH, true);
        ((StandUser) self).roundabout$tryPower(PowerIndex.BARRAGE_CLASH, true);

        LivingEntity standEntity = ((StandUser) entity).roundabout$getStand();
        LivingEntity standSelf = ((StandUser) self).roundabout$getStand();

        if (standEntity != null && standSelf != null){
            ((StandUser) entity).roundabout$getStandPowers().playBarrageClashSound();
            ((StandUser) this.self).roundabout$getStandPowers().playBarrageClashSound();
            Vec3 CenterPoint = entity.position().add(self.position()).scale(0.5);

            Vec3 entityPoint = offsetBarrageVector(
                    CenterPoint.subtract(((CenterPoint.subtract(entity.position())).normalize()).scale(0.95)),
                    getLookAtEntityYaw(entity,self));


            Vec3 selfPoint = offsetBarrageVector(
                    CenterPoint.subtract(((CenterPoint.subtract(self.position())).normalize()).scale(0.95)),
                    getLookAtEntityYaw(self,entity));

            standEntity.setPosRaw(entityPoint.x(),entityPoint.y()+getYOffSet(standEntity),entityPoint.z());
            standEntity.setXRot(getLookAtEntityPitch(standEntity,standSelf));
            standEntity.setYRot(getLookAtEntityYaw(standEntity,standSelf));

            standSelf.setPosRaw(selfPoint.x(),selfPoint.y()+getYOffSet(standSelf),selfPoint.z());
            standSelf.setXRot(getLookAtEntityPitch(standSelf,standEntity));
            standSelf.setYRot(getLookAtEntityYaw(standSelf,standEntity));

        }
    }

    private Vec3 offsetBarrageVector(Vec3 vec3d, float yaw){
        Vec3 vec3d2 = DamageHandler.getRotationVector(0, yaw+ 90);
        return vec3d.add(vec3d2.x*0.3, 0, vec3d2.z*0.3);
    }

    private float getYOffSet(LivingEntity stand){
        float yy = 0.1F;
        if (stand.isSwimming() || stand.isVisuallyCrawling() || stand.isFallFlying()) {
            yy -= 1;
        }
        return yy;
    }

    public boolean clickRelease(){
        return false;
    }

    public void barrageImpact(Entity entity, int hitNumber){
        if (this.isBarrageAttacking()) {
            if (bonusBarrageConditions()) {
                boolean sideHit = false;
                if (hitNumber > 1000){
                    if (!(entity.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_AOE_BARRAGE))){
                        return;
                    }
                    hitNumber-=1000;
                    sideHit = true;
                }
                boolean lastHit = (hitNumber >= this.getBarrageLength());
                if (entity != null) {
                    if (entity instanceof LivingEntity && ((StandUser) entity).roundabout$isBarraging()
                            && ((StandUser) entity).roundabout$getAttackTimeDuring() > -1 && !(((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                        initiateClash(entity);
                    } else {
                        float pow;
                        float knockbackStrength = 0;
                        /**By saving the velocity before hitting, we can let people approach barraging foes
                         * through shields.*/
                        Vec3 prevVelocity = entity.getDeltaMovement();
                        if (lastHit) {
                            pow = this.getBarrageFinisherStrength(entity);
                            knockbackStrength = this.getBarrageFinisherKnockback();
                        } else {
                            pow = this.getBarrageHitStrength(entity);
                            float mn = this.getBarrageLength() - hitNumber;
                            if (mn == 0) {
                                mn = 0.015F;
                            } else {
                                mn = ((0.015F / (mn)));
                            }
                            knockbackStrength = 0.014F - mn;
                        }

                        if (sideHit){
                            pow/=3;
                            knockbackStrength/=3;
                        }

                        if (StandDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                            if (entity instanceof LivingEntity LE) {
                                if (lastHit) {
                                    setDazed((LivingEntity) entity, (byte) 0);

                                    if (!sideHit) {
                                        addEXP(8,LE);
                                        playBarrageEndNoise(0, entity);
                                    }
                                } else {
                                    setDazed((LivingEntity) entity, (byte) 3);
                                    if (!sideHit) {
                                        playBarrageNoise(hitNumber, entity);
                                    }
                                }
                            }
                            barrageImpact2(entity, lastHit, knockbackStrength);
                        } else {
                            if (lastHit) {
                                knockShield2(entity, 200);
                                if (!sideHit) {
                                    playBarrageBlockEndNoise(0, entity);
                                }
                            } else {
                                entity.setDeltaMovement(prevVelocity);
                            }
                        }
                    }
                } else {
                    if (!sideHit) {
                        playBarrageMissNoise(hitNumber);
                    }
                }

                if (lastHit) {
                    animateStand((byte) 13);
                    this.attackTimeDuring = -10;
                }
            } else {
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
            }
        } else {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }

    public boolean cancelSprintJump(){
        return this.isBarraging();
    }

    public void barrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                this.takeKnockbackUp(entity,knockbackStrength);
            }
        }
    }


    public boolean bonusBarrageConditions(){
        return true;
    }

    public void takeDeterminedKnockbackWithY(LivingEntity user, Entity target, float knockbackStrength){
        float xRot; if (!target.onGround()){xRot=user.getXRot();} else {xRot = -15;}
        this.takeKnockbackWithY(target, knockbackStrength,
                Mth.sin(user.getYRot() * ((float) Math.PI / 180)),
                Mth.sin(xRot * ((float) Math.PI / 180)),
                -Mth.cos(user.getYRot() * ((float) Math.PI / 180)));

    }
    public void takeDeterminedKnockback(LivingEntity user, Entity target, float knockbackStrength){

        if (target instanceof LivingEntity && (knockbackStrength *= (float) (1.0 - ((LivingEntity)target).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        target.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(Mth.sin(
                user.getYRot() * ((float) Math.PI / 180)),
                0,
                -Mth.cos(user.getYRot() * ((float) Math.PI / 180))).normalize().scale(knockbackStrength);
        target.setDeltaMovement(- vec3d2.x,
                target.onGround() ? 0.28 : 0,
                - vec3d2.z);
        target.hasImpulse = true;
    }
    public void playBarrageMissNoise(int hitNumber){
        if (!this.self.level().isClientSide()) {
            if (hitNumber%2==0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_MISS_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
            }
        }
    }
    public void playBarrageNoise(int hitNumber, Entity entity){
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 2 == 0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
            }
        }
    }
    public void playBarrageNoise2(int hitNumber, Entity entity){
        if (!this.self.level().isClientSide()) {
            if (hitNumber%2==0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_HIT2_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.9 + (Math.random() * 0.25)));
            }
        }
    }
    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
          this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1f);
        }
    }
    public void playBarrageBlockEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_BLOCK_EVENT, SoundSource.PLAYERS, 0.88F+mod, 1.7f);
        }
    }
    public void playBarrageBlockNoise(){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_BLOCK_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
        }
    }

    /**ClashDone is a value that makes you lock in your barrage when you are done barraging**/
    public boolean clashDone = false;
    public boolean getClashDone(){
        return this.clashDone;
    } public void setClashDone(boolean clashDone){
        this.clashDone = clashDone;
    }
    public float clashProgress = 0.0f;
    private float clashOpProgress = 0.0f;

    /**Clash Op is the opponent you are clashing with*/
    @Nullable
    private LivingEntity clashOp;
    public @Nullable LivingEntity getClashOp() {
        return this.clashOp;
    }
    public void setClashOp(@Nullable LivingEntity clashOp) {
        this.clashOp = clashOp;
    }
    public float getClashOpProgress(){
        return this.clashOpProgress;
    }
    public void setClashOpProgress(float clashOpProgress1) {
        this.clashOpProgress = clashOpProgress1;
    }
    public float getClashProgress(){
        return this.clashProgress;
    }
    public void setClashProgress(float clashProgress1){
        this.clashProgress = clashProgress1;
        if (!this.self.level().isClientSide && this.clashOp != null && this.clashOp instanceof ServerPlayer){
            ModPacketHandler.PACKET_ACCESS.updateClashPacket((ServerPlayer) this.clashOp,
                    this.self.getId(), this.clashProgress);
        }
    }

    public void punchImpact(Entity entity){
    }

    public void damage(Entity entity){

    }

    public boolean moveStarted = false;

    public Entity storeEnt = null;
    public List<Entity> getTargetEntityList(LivingEntity User, float distMax){
        /*First, attempts to hit what you are looking at*/
        if (!(distMax >= 0)) {
            distMax = this.getDistanceOut(User, this.standReach, false);
        }
        Entity targetEntity = this.rayCastEntity(User,distMax);

        if (targetEntity != null && User instanceof StandEntity SE && SE.getUser() != null && SE.getUser().is(targetEntity)){
            targetEntity = null;
        }

        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
            float halfReach = (float) (distMax*0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(User, halfReach);
            List<Entity> listE = StandGrabHitbox(User,DamageHandler.genHitbox(User, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distMax);
        if (targetEntity == null) {
            targetEntity = StandAttackHitboxNear(User,listE,25);
        }
        if (targetEntity instanceof StandEntity SE){

            if (SE.getUser() != null){
                targetEntity = SE.getUser();
            }
        }

        storeEnt = targetEntity;

        return listE;
    }
    public Entity getTargetEntity(LivingEntity User, float distMax){
        /*First, attempts to hit what you are looking at*/
        if (!(distMax >= 0)) {
            distMax = this.getDistanceOut(User, this.standReach, false);
        }
        Entity targetEntity = this.rayCastEntity(User,distMax);

        if (targetEntity != null && User instanceof StandEntity SE && SE.getUser() != null && SE.getUser().is(targetEntity)){
            targetEntity = null;
        }

        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        if (targetEntity == null) {
            float halfReach = (float) (distMax*0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(User, halfReach);
            targetEntity = StandAttackHitboxNear(User,StandGrabHitbox(User,DamageHandler.genHitbox(User, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distMax),25);
        }
        if (targetEntity instanceof StandEntity SE){

            if (SE.getUser() != null){
                targetEntity = SE.getUser();
            }
        }

        return targetEntity;
    }
    public boolean hasMoreThanOneSkin(){
        return this.getSelf() instanceof Player PE && ((((IPlayerEntity) PE).roundabout$getStandLevel() > 1 ||
                ((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin()) ||
                PE.isCreative() || hasGoldenDisc());
    }

    public boolean hasGoldenDisc(){
        ItemStack stack = ((StandUser)this.getSelf()).roundabout$getStandDisc();
        return !stack.isEmpty() && stack.getItem() instanceof MaxStandDiscItem;
    }

    public void getSkinInDirection(boolean right){
        StandUser SE = ((StandUser)this.getSelf());
        byte currentSkin = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        List<Byte> skins = getSkinList();
        if (!skins.isEmpty() && skins.size() > 1) {
            int skinind = 0;
            for (int i = 0; i<skins.size(); i++){
                if (skins.get(i) == currentSkin){
                    skinind = i;
                }
            }
            if (right) {
                skinind+=1;
                if (skinind >= skins.size()){
                    skinind =0;
                }
                SE.roundabout$setStandSkin((skins.get(skinind)));
            } else {
                skinind-=1;
                if (skinind < 0){
                    skinind =skins.size()-1;
                }
                SE.roundabout$setStandSkin((skins.get(skinind)));
            }
            SE.roundabout$summonStand(this.getSelf().level(), true, false);
        }
    }


    public void getPoseInDirection(boolean right){
        Roundabout.LOGGER.info("3");
        StandUser SE = ((StandUser)this.getSelf());
        byte currentSkin = ((StandUser)this.getSelf()).roundabout$getIdlePos();
        List<Byte> poses = getPosList();
        if (!poses.isEmpty() && poses.size() > 1) {
            Roundabout.LOGGER.info("4");
            int skinind = 0;
            for (int i = 0; i<poses.size(); i++){
                if (poses.get(i) == currentSkin){
                    skinind = i;
                }
            }
            Roundabout.LOGGER.info("5");
            if (right) {
                skinind+=1;
                if (skinind >= poses.size()){
                    skinind =0;
                }
                SE.roundabout$setIdlePosX(poses.get(skinind));
                Roundabout.LOGGER.info(""+poses.get(skinind));
            } else {
                skinind-=1;
                if (skinind < 0){
                    skinind =poses.size()-1;
                }
                SE.roundabout$setIdlePosX(poses.get(skinind));
                Roundabout.LOGGER.info(""+poses.get(skinind));
            }
        }
    }

    public Entity getTargetEntityGenerous(LivingEntity User, float distMax, float angle){
        /*First, attempts to hit what you are looking at*/
        if (!(distMax >= 0)) {
            distMax = this.getDistanceOut(User, this.standReach, false);
        }
        Entity targetEntity = this.rayCastEntity(User,distMax);

        if ((targetEntity != null && User instanceof StandEntity SE && SE.getUser() != null && SE.getUser().is(targetEntity))
         || (targetEntity != null && targetEntity.is(User))){
            targetEntity = null;
        }

        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        if (targetEntity == null) {
            float halfReach = (float) (distMax*0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(User, halfReach);
            targetEntity = StandAttackHitboxNear(User,StandGrabHitbox(User,DamageHandler.genHitbox(User, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distMax),angle);
        }
        if (targetEntity instanceof StandEntity SE){

            if (SE.getUser() != null){
                targetEntity = SE.getUser();
            }
        }

        return targetEntity;
    }

    public float getDistanceOut(LivingEntity entity, float range, boolean offset){
        float distanceFront = this.getRayDistance(entity, range);
        if (offset) {
            Entity targetEntity = this.rayCastEntity(entity,this.standReach);
            if (targetEntity != null && targetEntity.distanceTo(entity) < distanceFront) {
                distanceFront = targetEntity.distanceTo(entity);
            }
            distanceFront -= 1;
            distanceFront = Math.max(Math.min(distanceFront, 1.7F), 0.4F);
        }
        return distanceFront;
    }

    public float getDistanceOutAccurate(Entity entity, float range, boolean offset){
        float distanceFront = this.getRayDistance(entity, range);
        if (offset) {
            Entity targetEntity = this.getTargetEntity(this.self,this.standReach);
            if (targetEntity != null && targetEntity.distanceTo(entity) < distanceFront) {
                distanceFront = targetEntity.distanceTo(entity);
            }
            distanceFront -= 1;
            distanceFront = Math.max(Math.min(distanceFront, 1.7F), 0.4F);
        }
        return distanceFront;
    }

    public float getRayDistance(Entity entity, float range){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (blockHit.getType() != HitResult.Type.MISS){
            return Mth.sqrt((float) entity.distanceToSqr(blockHit.getLocation()));
        }
        return range;
    } public Vec3 getRayBlock(Entity entity, float range){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        return blockHit.getLocation();
    }

    public float getPivotPoint(Vector3d pointToRotate, Vector3d axisStart, Vector3d axisEnd) {
        Vector3d d = new Vector3d(axisEnd.x-axisStart.x,axisEnd.y-axisStart.y,axisEnd.z-axisStart.z).normalize();
        Vector3d v = new Vector3d(pointToRotate.x-axisStart.x,pointToRotate.y-axisStart.y,pointToRotate.z-axisStart.z).normalize();
        double t = v.dot(d);
        return (float) pointToRotate.distance(axisStart.add(d.mul(t)));
    }

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        float distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }

    /**Returns the vertical angle between two mobs*/
    public float getLookAtEntityPitch(Entity user, Entity targetEntity) {
        double f;
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)targetEntity;
            f = livingEntity.getEyeY() - user.getEyeY();
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - user.getEyeY();
        }
        double g = Math.sqrt(d * d + e * e);
        return (float)(-(Mth.atan2(f, g) * 57.2957763671875));
    }
    /**Returns the horizontal angle between two mobs*/
    public float getLookAtEntityYaw(Entity user, Entity targetEntity) {
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }



    /**Returns the vertical angle between a mob and a position*/
    public float getLookAtPlacePitch(Entity user, Vec3 vec) {
        double f;
        double d = vec.x() - user.getX();
        double e = vec.z() - user.getZ();
        f = vec.y() - user.getEyeY();
        double g = Math.sqrt(d * d + e * e);
        return (float)(-(Mth.atan2(f, g) * 57.2957763671875));
    }
    /**Returns the horizontal angle between a mob and a position*/
    public float getLookAtPlaceYaw(Entity user, Vec3 vec) {
        double d = vec.x() - user.getX();
        double e = vec.z() - user.getZ();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }


    public BlockHitResult getAheadVec(float distOut){
        Vec3 vec3d = this.self.getEyePosition(0);
        Vec3 vec3d2 = this.self.getViewVector(0);
        return this.getSelf().level().clip(new ClipContext(vec3d, vec3d.add(vec3d2.x * distOut,
                vec3d2.y * distOut, vec3d2.z * distOut), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                this.getSelf()));
    }
    /** This code grabs an entity in front of you at the specified range, raycasting is used*/
    public Entity rayCastEntity(LivingEntity User, float reach){
        float tickDelta = 0;
        if (this.self.level().isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            tickDelta = mc.getDeltaFrameTime();
        }
        Vec3 vec3d = User.getEyePosition(tickDelta);

        Vec3 vec3d2 = User.getViewVector(1.0f);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        AABB box = new AABB(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(User, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.isPickable() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null){
            Entity hitResult = entityHitResult.getEntity();
            if (hitResult.isAlive() && !hitResult.isRemoved() && !hitResult.is(User)) {
                return hitResult;
            }
        }
        return null;
    }

    public List<Entity> StandGrabHitbox(LivingEntity User, List<Entity> entities, float maxDistance){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
            for (Entity value : entities) {
                if (!value.showVehicleHealth() || (!value.isAttackable() && !(value instanceof StandEntity)) || value.isInvulnerable() || !value.isAlive() || (User.isPassenger() && User.getVehicle().getUUID() == value.getUUID())
                || value.is(User) || (((StandUser)User).roundabout$getStand() != null &&
                        ((StandUser)User).roundabout$getStand().is(User)) || (User instanceof StandEntity SE && SE.getUser() !=null && SE.getUser().is(value))){
                    hitEntities.remove(value);
                } else {
                    int angle = 25;
                    if (!(angleDistance(getLookAtEntityYaw(User, value), (User.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(User, value), User.getXRot()) <= angle)){
                        hitEntities.remove(value);
                    }
                }
            }
        return hitEntities;
    }
    public boolean StandAttackHitbox(List<Entity> entities, float pow, float knockbackStrength){
        boolean hitSomething = false;
        float nearestDistance = -1;
        Entity nearestMob;
        if (entities != null){
            for (Entity value : entities) {
                if (this.StandDamageEntityAttack(value,pow, knockbackStrength, this.self)){
                    hitSomething = true;
                }
            }
        }
        return hitSomething;
    }

    public int getExpForLevelUp(int currentLevel){
        return 100;
    }
    public byte getMaxLevel(){
        return 1;
    }

    public boolean StandDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker){
        if (attacker instanceof TamableAnimal TA){
            if (target instanceof TamableAnimal TT && TT.getOwner() != null
                    && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())){
                return false;
            }
        } else if (attacker instanceof AbstractVillager){
            if (target instanceof AbstractVillager){
                return false;
            }
        }
        if (DamageHandler.StandDamageEntity(target,pow, attacker)){
            if (attacker instanceof LivingEntity LE){
                LE.setLastHurtMob(target);
            }
            if (target instanceof LivingEntity && knockbackStrength > 0) {
                ((LivingEntity) target).knockback(knockbackStrength * 0.5f, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180)), -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }

    public void takeKnockbackWithY(Entity entity, double strength, double x, double y, double z) {

        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        entity.setDeltaMovement(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.hasImpulse = true;
    }


    public void takeKnockbackUp(Entity entity, double strength) {
        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        entity.hasImpulse = true;

        Vec3 vec3d2 = new Vec3(0, strength, 0).normalize().scale(strength);
        entity.setDeltaMovement(vec3d2.x,
                vec3d2.y,
                vec3d2.z);
    }

    public Entity StandAttackHitboxNear(LivingEntity User,List<Entity> entities, float angle){
        float nearestDistance = -1;
        Entity nearestMob = null;

        if (entities != null){
            for (Entity value : entities) {
                if (!value.isInvulnerable() && value.isAlive() && value.getUUID() != User.getUUID()){
                    if (!(value instanceof StandEntity SE1 && SE1.getUser() != null && SE1.getUser().is(User))) {
                        float distanceTo = value.distanceTo(User);
                        float range = this.standReach;
                        if (value instanceof StandEntity SE && OffsetIndex.OffsetStyle(SE.getOffsetType()) == OffsetIndex.FOLLOW_STYLE) {
                            range /= 2;
                        }
                        if ((nearestDistance < 0 || distanceTo < nearestDistance)
                                && distanceTo <= range) {
                            nearestDistance = distanceTo;
                            nearestMob = value;
                        }
                    }
                }
            }
        }

        return nearestMob;
    }

    public void updateUniqueMoves(){
    }

    public void kickStartClient(){
        this.kickStarted = true;
    }


    public boolean interceptAttack(){
        return false;
    }
    public boolean interceptGuard(){
        return false;
    }
    public boolean canChangePower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if ((this.activePower == PowerIndex.NONE || forced) &&
                    (!this.isDazed(this.self) || move == PowerIndex.BARRAGE_CLASH)) {
                return true;
            }
        }
        return false;
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public boolean tryPower(int move, boolean forced){
        if (!this.self.level().isClientSide && (this.isBarraging() || this.isClashing()) && (move != PowerIndex.BARRAGE && move != PowerIndex.BARRAGE_CLASH) && this.attackTimeDuring  > -1){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }

        if (canChangePower(move, forced)) {
                if (move == PowerIndex.NONE || move == PowerIndex.CLASH_CANCEL) {
                    return this.setPowerNone();
                } else if (move == PowerIndex.ATTACK) {
                    return this.setPowerAttack();
                } else if (move == PowerIndex.GUARD) {
                    return this.setPowerGuard();
                } else if (move == PowerIndex.BARRAGE_CHARGE) {
                    return this.setPowerBarrageCharge();
                } else if (move == PowerIndex.BARRAGE) {
                    return this.setPowerBarrage();
                } else if (move == PowerIndex.BARRAGE_CLASH) {
                    return this.setPowerClash();
                } else if (move == PowerIndex.SPECIAL) {
                    return this.setPowerSpecial(move);
                } else if (move == PowerIndex.MOVEMENT) {
                    return this.setPowerMovement(move);
                } else if (move == PowerIndex.SNEAK_MOVEMENT) {
                    return this.setPowerSneakMovement(move);
                } else if (move == PowerIndex.MINING) {
                    return this.setPowerMining(move);
                } else {
                    return this.setPowerOther(move, this.getActivePower());
                }

        }
        return false;
    }

    public boolean tryPosPower(int move, boolean forced, BlockPos blockPos){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        tryPower(move, forced);
        /*Return false in an override if you don't want to sync cooldowns, if for example you want a simple data update*/
        return true;
    }


    public boolean canSummonStand(){
        return false;
    }
    public StandEntity getNewStandEntity(){
        return null;
    }
    public void playSummonEffects(boolean forced){
    }
    /**The Sound Event to cancel when your barrage is canceled*/

    public final void playStandUserOnlySoundsIfNearby(byte soundNo, double range, boolean onSelf, boolean isVoice) {
        if (isVoice && this.getSelf() instanceof Player PE &&
                ((IPlayerEntity)PE).roundabout$getMaskInventory().getItem(1).is(ModItems.BLANK_MASK)){
            return;
        }
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range) && !((StandUser)serverPlayerEntity).roundabout$getStandDisc().isEmpty()) {
                    if (onSelf) {
                        ModPacketHandler.PACKET_ACCESS.startSoundPacket(serverPlayerEntity, serverPlayerEntity.getId(), soundNo);
                    } else {
                        ModPacketHandler.PACKET_ACCESS.startSoundPacket(serverPlayerEntity, this.self.getId(), soundNo);
                    }
                }
            }
        }
    }

    /**The Sound Event to cancel when your barrage is canceled*/

    public final void playSoundsIfNearby(byte soundNo, double range, boolean onSelf) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    if (onSelf) {
                        ModPacketHandler.PACKET_ACCESS.startSoundPacket(serverPlayerEntity, serverPlayerEntity.getId(), soundNo);
                    } else {
                        ModPacketHandler.PACKET_ACCESS.startSoundPacket(serverPlayerEntity, this.self.getId(), soundNo);
                    }
                }
            }
        }
    }
    /**This is called first by the server, it chooses the sfx and sends packets to nearby players*/
    public void playBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            byte barrageCrySound = this.chooseBarrageSound();
            if (barrageCrySound != SoundIndex.NO_SOUND) {
                playStandUserOnlySoundsIfNearby(barrageCrySound, 27, false,true);
            }
        }
    }

    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
        }
    }
    public void playBarrageChargeSound(){
        if (!this.self.level().isClientSide()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                playSoundsIfNearby(SoundIndex.BARRAGE_CHARGE_SOUND, 27, false);
            }
        }
    }

    public void handleStandAttack(Player player, Entity target){
    }

    public void handleStandAttack2(Player player, Entity target){
    }

    /**This is called fourth by the server, it sends a packet to cancel the sound.*/
    public final void stopSoundsIfNearby(byte soundNumber, double range, boolean onSelf) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    if (!onSelf){
                        ModPacketHandler.PACKET_ACCESS.stopSoundPacket(serverPlayerEntity,this.self.getId(),soundNumber);
                    } else {
                        ModPacketHandler.PACKET_ACCESS.stopSoundPacket(serverPlayerEntity,serverPlayerEntity.getId(),soundNumber);
                    }
                }
            }
        }
    }


    public boolean setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        animateStand((byte) 0);
        return true;
    }

    public void resetAttackState(){
        if (shouldReset(this.getActivePower())){
            this.interruptCD = 3;
            ((StandUser)this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
        }
    }


    /**If eating or using items in general shouldn't cancel certain abilties, put them as exceptions here*/
    public boolean shouldReset(byte activeP){
        return ((this.self.isUsingItem() &&
                !(this.getActivePower() == PowerIndex.BARRAGE_CLASH)) || this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())));
    }


    public void updateMovesFromPacket(byte activePower){

    }
    public boolean canAttack(){
        if (this.attackTimeDuring <= -1) {
            return this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax;
        }
        return false;
    }
    public boolean setPowerGuard() {
        if (((StandUser)this.self).roundabout$getGuardBroken()) {
            animateStand((byte) 15);
        } else {
            animateStand((byte) 10);
        }
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.GUARD);
        this.poseStand(OffsetIndex.GUARD);
        return true;
    }


    public boolean setPowerBarrageCharge() {
        return true;
    }

    public boolean setPowerBarrage() {
        return true;
    }

    public int clashStarter = 0;

    /**Override this to set the special move*/
    public boolean setPowerSpecial(int lastMove) {
        return false;
    }
    public boolean setPowerMovement(int lastMove) {
        return false;
    }
    public boolean setPowerSneakMovement(int lastMove) {
        return false;
    }
    public boolean setPowerOther(int move, int lastMove) {
        return false;
    }
    public boolean setPowerMining(int lastMove) {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.MINING);
        this.poseStand(OffsetIndex.FIXED_STYLE);
        animateStand((byte) 16);
        return true;
    }

    public boolean isMiningStand() {
        return false;
    }

    public boolean canUseMiningStand() {
        return (isMiningStand() && (!(this.getSelf().getMainHandItem().getItem() instanceof DiggerItem) || this.getActivePower() == PowerIndex.MINING));
    }


    public float getMiningSpeed() {
        return 3F;
    }

    public boolean setPowerClash() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CLASH);
        this.poseStand(OffsetIndex.LOOSE);
        this.setClashProgress(0f);
        this.clashIncrement = 0;
        this.clashMod = (int) (Math.round(Math.random()*8));
        animateStand((byte) 12);

        if (this.self instanceof Player && !this.self.level().isClientSide) {
            ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.barrage_clash"), true);
        }
        return true;
        //playBarrageGuardSound();
    }

    public int getBarrageRecoilTime(){
        return 35;
    }

    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
    }

    public boolean isBarrageCharging(){
        return (this.activePower == PowerIndex.BARRAGE_CHARGE);
    }
    public boolean isBarraging(){
        return (this.activePower == PowerIndex.BARRAGE || this.activePower == PowerIndex.BARRAGE_CHARGE);
    }
    public boolean isBarrageAttacking(){
        return this.activePower == PowerIndex.BARRAGE;
    }
    public boolean isClashing(){
        return this.activePower == PowerIndex.BARRAGE_CLASH && this.attackTimeDuring > -1;
    }
    public boolean disableMobAiAttack(){
        return ((this.activePower == PowerIndex.BARRAGE_CLASH && this.attackTimeDuring > -1) || this.isBarraging());
    }

    /**The AI for a stand User Mob, runs every tick. AttackTarget may be null*/
    public void tickMobAI(LivingEntity attackTarget){
    }
    public int getKickBarrageWindup(){
        return 29;
    }
    public int getBarrageWindup(){
        return 29;
    }
    public int getBarrageLength(){
        return 60;
    }

    public boolean setPowerAttack(){
        return false;
    }

    public void syncCooldowns(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer){
            ModPacketHandler.PACKET_ACCESS.syncCooldownPacket((ServerPlayer) this.self,
                    attackTime,attackTimeMax,attackTimeDuring,
                    activePower,activePowerPhase);
        }
    }

    public boolean isStoppingTime(){
        return false;
    }

    public void runExtraSoundCode(byte soundChoice) {
    }

    public boolean isHoldingSneakToggle = false;
    public boolean isHoldingSneak(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return ((mc.options.keyShift.isDown() && !isHoldingSneakToggle) || (isHoldingSneakToggle && !mc.options.keyShift.isDown()));
        }
        return this.getSelf().isCrouching();
    }
    public boolean dealWithProjectile(Entity ent){
        return false;
    }

    public boolean heldDownSwitch = false;
    public void switchRowsKey(boolean keyIsDown, Options options){
        if (!heldDownSwitch){
            if (keyIsDown){
                heldDownSwitch = true;
                if (isHoldingSneakToggle){
                    isHoldingSneakToggle=false;
                } else {
                    isHoldingSneakToggle=true;
                }
            }
        } else {
            if (!keyIsDown){
                heldDownSwitch = false;
            }
        }
    }
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND){
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else if (soundChoice <= SoundIndex.GLAIVE_CHARGE) {
            return SoundIndex.ITEM_GROUP;
        }

        return soundChoice;
    }
}
