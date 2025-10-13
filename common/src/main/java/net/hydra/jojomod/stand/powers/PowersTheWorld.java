package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.entity.visages.mobs.DIONPC;
import net.hydra.jojomod.entity.visages.mobs.DiegoNPC;
import net.hydra.jojomod.entity.visages.mobs.JotaroNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.event.powers.visagedata.voicedata.DIOVoice;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class PowersTheWorld extends TWAndSPSharedPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().theWorldSettings.enableTheWorld;
    }

    @Override
    public void playSummonSound() {
        if (this.self.isCrouching()){
            return;
        }

        if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof DIOVoice DV){
            DV.playSummon();
        }
        playStandUserOnlySoundsIfNearby(this.getSummonSound(), 10, false,false);
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersTheWorld(entity);
    }

    @Override
    public StandEntity getNewStandEntity(){
        byte sk = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (sk == TheWorldEntity.ULTIMATE_SKIN || sk == TheWorldEntity.ULTIMATE_KARS_SKIN){
            return ModEntities.THE_WORLD_ULTIMATE.create(this.getSelf().level());
        }
        return ModEntities.THE_WORLD.create(this.getSelf().level());
    }

    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().theWorldSettings.theWorldGuardPoints;
    }
    @Override
    public void playTheLastHitSound(){
        Byte LastHitSound = this.getLastHitSound();
        if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof DIOVoice DV){
            DV.playSoundIfPossible( getSoundFromByte(LastHitSound),20,1,2);
        } else {
            this.playStandUserOnlySoundsIfNearby(LastHitSound, 15, false,
                    true);
        }
    }

    @Override
    public void playImpaleConnectSoundExtra(){
        if (!this.self.level().isClientSide()) {
            if (this.self instanceof Player pe && ((IPlayerEntity) pe).roundabout$getVoiceData() instanceof DIOVoice DV) {
                DV.playSoundIfPossible(ModSounds.DIO_SHINE_EVENT,22,1,2);
            }
        }
    }
    @Override
    public void playBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof DIOVoice DV) {

                if (!DV.inTheMiddleOfTalking()) {
                    DV.forceTalkingTicks(70);
                    byte barrageCrySound = this.chooseBarrageSound();
                    if (barrageCrySound != SoundIndex.NO_SOUND) {
                        playSoundsIfNearby(barrageCrySound, 27, false, true);
                    }
                }
            } else {
                byte barrageCrySound = this.chooseBarrageSound();
                if (barrageCrySound != SoundIndex.NO_SOUND) {
                    playStandUserOnlySoundsIfNearby(barrageCrySound, 27, false, true);
                }
            }
        }
    }
    @Override
    public Byte getLastHitSound(){

        double rand = Math.random();
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == TheWorldEntity.OVA_SKIN) {
            if (rand > 0.5) {
                return LAST_HIT_7_NOISE;
            } else {
                return LAST_HIT_8_NOISE;
            }
        }
        if (skn == TheWorldEntity.ARCADE_SKIN || skn == TheWorldEntity.ARCADE_SKIN_2){
            if (rand > 0.66) {
                return LAST_HIT_10_NOISE;
            } else if (rand > 0.33) {
                return LAST_HIT_11_NOISE;
            } else {
                return LAST_HIT_12_NOISE;
            }
        }
        if (skn == TheWorldEntity.PART_7_SKIN || skn == TheWorldEntity.PART_7_BLUE){
            if (rand > 0.66) {
                return LAST_HIT_4_NOISE;
            } else if (rand > 0.33) {
                return LAST_HIT_5_NOISE;
            } else {
                return LAST_HIT_6_NOISE;
            }
        }
        if (rand > 0.66) {
            return LAST_HIT_1_NOISE;
        } else if (rand > 0.33) {
            return LAST_HIT_2_NOISE;
        } else {
            return LAST_HIT_3_NOISE;
        }
    }

    @Override
    public SoundEvent getLastRejectionHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }


    @Override
    public float inputSpeedModifiers(float basis){

        return super.inputSpeedModifiers(basis);
    }



    public boolean isUsingAssault(){
        return (this.activePower == PowerIndex.POWER_1);
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (this.getActivePower() == PowerIndex.POWER_1 && this.getAttackTimeDuring() >= 0 && slot != 2 && slot != 1){
            return true;
        } else if (this.getActivePower() == PowerIndex.POWER_1_BONUS && this.getAttackTimeDuring() >= 0 && slot != 1){
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }
    @Override
    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
            byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
            if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof DIOVoice DV) {
                DV.forceTalkingTicks(70);
                if (skn == TheWorldEntity.ARCADE_SKIN || skn == TheWorldEntity.ARCADE_SKIN_2){
                    playSoundsIfNearby(BARRAGE_NOISE_8, 27, false);
                    return;
                }
                if (skn == TheWorldEntity.OVA_SKIN){
                    playSoundsIfNearby(BARRAGE_NOISE_5, 27, false);
                    return;
                }
                if (skn == TheWorldEntity.PART_7_SKIN || skn == TheWorldEntity.PART_7_BLUE){
                    playSoundsIfNearby(BARRAGE_NOISE_3, 27, false);
                    return;
                }
                playSoundsIfNearby(BARRAGE_NOISE_2, 27, false);
            } else {

                if (skn == TheWorldEntity.ARCADE_SKIN || skn == TheWorldEntity.ARCADE_SKIN_2){
                    playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_8, 27, false, true);
                    return;
                }
                if (skn == TheWorldEntity.OVA_SKIN){
                    playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_5, 27, false,true);
                    return;
                }
                if (skn == TheWorldEntity.PART_7_SKIN || skn == TheWorldEntity.PART_7_BLUE){
                    playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_3, 27, false,true);
                    return;
                }
                playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_2, 27, false,true);
            }
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced) {

        if (this.getActivePower() == PowerIndex.POWER_1 || this.getActivePower() == PowerIndex.POWER_1_BONUS){
            if (move != PowerIndex.POWER_1_BONUS) {
                stopSoundsIfNearby(ASSAULT_NOISE, 100, false);
            }
        }
        return super.tryPower(move,forced);
    }

    @Override
    public int getImpulseTSLevel(){
        return 2;
    }
    @Override
    public int getTSLevel(){
        return 3;
    }
    public int getAssaultLevel(){
        return 0;
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(TheWorldEntity.PART_3_SKIN);
        $$1.add(TheWorldEntity.MANGA_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(TheWorldEntity.BLACK_SKIN);
                $$1.add(TheWorldEntity.OVA_SKIN);
            } if (Level > 2 || bypass){
                $$1.add(TheWorldEntity.HERITAGE_SKIN);
                $$1.add(TheWorldEntity.ARCADE_SKIN);
                $$1.add(TheWorldEntity.ARCADE_SKIN_2);
            } if (Level > 3 || bypass){
                $$1.add(TheWorldEntity.DARK_SKIN);
                $$1.add(TheWorldEntity.BRONZE);
                $$1.add(TheWorldEntity.FOUR_DEE_EXPERIENCE);
            } if (Level > 4 || bypass){
                $$1.add(TheWorldEntity.AGOGO_SKIN);
                $$1.add(TheWorldEntity.SCARLET);
            } if (Level > 5 || bypass){
                $$1.add(TheWorldEntity.PART_7_SKIN);
                $$1.add(TheWorldEntity.PART_7_BLUE);
                $$1.add(TheWorldEntity.THE_NETHER);
            } if (Level > 6 || bypass){
                $$1.add(TheWorldEntity.AQUA_SKIN);
                $$1.add(TheWorldEntity.BETA);
                $$1.add(TheWorldEntity.ULTIMATE_SKIN);
                $$1.add(TheWorldEntity.ULTIMATE_KARS_SKIN);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(TheWorldEntity.OVER_HEAVEN);
            }
        }
        return $$1;
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos,byte level,boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.THE_WORLD_PUNCH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.THE_WORLD_GUARD,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.final_kick",
                "instruction.roundabout.hold_attack_crouch", StandIcons.THE_WORLD_FINAL_KICK,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.THE_WORLD_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.kick_barrage",
                "instruction.roundabout.kick_barrage", StandIcons.THE_WORLD_KICK_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.forward_barrage",
                "instruction.roundabout.forward_barrage", StandIcons.THE_WORLD_TRAVEL_BARRAGE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,getAssaultLevel(), "ability.roundabout.assault",
                "instruction.roundabout.press_skill", StandIcons.THE_WORLD_ASSAULT,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, getImpaleLevel(), "ability.roundabout.impale",
                "instruction.roundabout.press_skill_crouch", StandIcons.THE_WORLD_IMPALE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,0, "ability.roundabout.air_tanks",
                "instruction.roundabout.passive", StandIcons.THE_WORLD_AIR_TANKS,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.block_grab",
                "instruction.roundabout.press_skill", StandIcons.THE_WORLD_GRAB_BLOCK,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.item_grab",
                "instruction.roundabout.press_skill_crouch", StandIcons.THE_WORLD_GRAB_ITEM,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,0, "ability.roundabout.mob_grab",
                "instruction.roundabout.press_skill_near_mob", StandIcons.THE_WORLD_GRAB_MOB,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.phase_grab",
                "instruction.roundabout.press_skill_block", StandIcons.THE_WORLD_PHASE_GRAB,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.THE_WORLD_FALL_CATCH,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.THE_WORLD_LEDGE_GRAB,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99,getLeapLevel(), "ability.roundabout.stand_leap",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAND_LEAP_WORLD,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+118,getLeapLevel(), "ability.roundabout.stand_leap_rebound",
                "instruction.roundabout.press_skill_rebound", StandIcons.STAND_LEAP_REBOUND_WORLD,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80,getTSLevel(), "ability.roundabout.time_stop",
                "instruction.roundabout.press_skill", StandIcons.THE_WORLD_TIME_STOP,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+99,getImpulseTSLevel(),"ability.roundabout.time_stop_impulse",
                "instruction.roundabout.press_skill_crouch", StandIcons.THE_WORLD_TIME_STOP_IMPULSE,4,level,bypas));
        return $$1;
    }

    @Override
    public void setChargeTicksMult(){
        this.setChargedTSTicks(this.getChargedTSTicks()*(1+
                (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld -100)/100));
    }

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().theWorldSettings.getMiningTierTheWorld;
    }
    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.miningSpeedMultiplierTheWorld *0.01));
    }


    public boolean fullTSChargeBonus(){
        if (canExecuteMoveWithLevel(getMaxTSFactorLevel()) && ClientNetworking.getAppropriateConfig().timeStopSettings.maxTWBypassesReduction){
            return this.maxChargedTSTicks >= ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld;
        } else {
            return false;
        }
    }

    @Override
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        if (chargedTSSeconds >= (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)){
            if (canExecuteMoveWithLevel(getMaxTSFactorLevel()) && this.getSelf() instanceof Player) {
                this.maxChargeTSTime = ClientNetworking.getAppropriateConfig().timeStopSettings.maxTWChargeBonusTicks +
                ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld;
                this.setChargedTSTicks(this.maxChargeTSTime);
                return ClientNetworking.getAppropriateConfig().timeStopSettings.maxTWChargeBonusTicks;
            } else {
                this.maxChargeTSTime = ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld;
                this.setChargedTSTicks(this.maxChargeTSTime);
            }
        } else if (chargedTSSeconds == ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength) {
            this.maxChargeTSTime = ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength;
        } else if (chargedTSSeconds == (Math.min(ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld *0.2,
                ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength))) {
            this.maxChargeTSTime = (int) (Math.min(ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld *0.2,
                    ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength));
        } else {
            this.maxChargeTSTime = (int) (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld);;
        }

        if (!canExecuteMoveWithLevel(4)){
            if (this.maxChargeTSTime > (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)*0.4){
                this.maxChargeTSTime = (int) (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld *0.4);;
            }
        } else if (!canExecuteMoveWithLevel(5)){
            if (this.maxChargeTSTime > (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)*0.6){
                this.maxChargeTSTime = (int) (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld *0.6);;
            }
        } else if (!canExecuteMoveWithLevel(6)){
            if (this.maxChargeTSTime > (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)*0.8){
                this.maxChargeTSTime = (int) (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld *0.8);
            }
        }
        return 0;
    }
    @Override
    public int getMobTSTime(){
        return ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld-1;
    }
    @Override
    public int getMaxTSTime(){
        if (canExecuteMoveWithLevel(6)){
            return (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld);
        } else if (canExecuteMoveWithLevel(5)){
            return (int) ((ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)*0.8);
        } else if (canExecuteMoveWithLevel(4)){
            return (int) ((ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)*0.6);
        }
        return (int) ((ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld)*0.4);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            return this.assault();
        } else if (move == PowerIndex.POWER_1_BONUS && this.getActivePower() == PowerIndex.POWER_1) {
            return this.assaultGrab();
        }
        return super.setPowerOther(move,lastMove);
    }


    @Override
    public boolean canSeeThroughFog(){
        return this.scopeLevel > 0;
    }

    public Vec3 assultVec = Vec3.ZERO;
    public boolean assaultGrab(){
        if (this.attackTimeDuring >= 0) {
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand)) {
                this.setActivePower(PowerIndex.POWER_1_BONUS);
                if (!this.getSelf().level().isClientSide) {
                    this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                            SoundSource.PLAYERS, 0.95F, 1.3F);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            stand.getX(), stand.getY() + 0.3, stand.getZ(),
                            30, 0.4, 0.4, 0.4, 0.4);
                }
                return true;
            }
        }
        return false;
    }
    public boolean assault(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1);
            playSoundsIfNearby(ASSAULT_NOISE, 27, false);
            this.animateStand(TheWorldEntity.ASSAULT);
            this.poseStand(OffsetIndex.LOOSE);

            Vec2 twoVec = new Vec2((this.getSelf().getYHeadRot() % 360),(this.getSelf().getXRot()));
            Direction gdir = ((IGravityEntity)this.self).roundabout$getGravityDirection();
            Vec2 twoVecGrav = RotationUtil.rotPlayerToWorld(twoVec,gdir);
            Vec3 threeVec = new Vec3(0,0.25,0);
            threeVec = RotationUtil.vecPlayerToWorld(threeVec,gdir);

            stand.setYRot(twoVec.x);
            stand.setXRot(twoVec.y);
            assultVec = DamageHandler.getRotationVector(
                    twoVecGrav.y, (float) (twoVecGrav.x)).scale(1.8).add(threeVec.x,threeVec.y,threeVec.z);
            stand.setPos(this.getSelf().position().add(assultVec));
            return true;
        }
        return false;
    }
    public static final byte ASSAULT_NOISE = 80;

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
            super.updateUniqueMoves();
    }
    @Override
    public float getSoundPitchFromByte(byte soundChoice){
            return super.getSoundPitchFromByte(soundChoice);
    }

    @Override
    public SoundEvent getFinalAttackSound(){
        return ModSounds.FINAL_KICK_EVENT;
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 7) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.skins").
                        withStyle(ChatFormatting.AQUA), true);
            } else if (level == 4){
                    ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.skins").
                            withStyle(ChatFormatting.AQUA), true);
            } else if (level == 2 || level == 3 || level == 6 || level == 5){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }


    @Override
    public void tickPowerEnd(){

        super.tickPowerEnd();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (this.getActivePower() == PowerIndex.POWER_1 || this.getActivePower() == PowerIndex.POWER_1_BONUS) {
                if (!this.getSelf().level().isClientSide()) {
                    if (this.attackTimeDuring == 108) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    } else if (this.attackTimeDuring >= 0) {
                        StandEntity stand = getStandEntity(this.self);
                        if (Objects.nonNull(stand)) {
                            AABB BB1 = stand.getBoundingBox();
                            Vec3 vec3d = this.getSelf().getEyePosition(0);
                            Vec3 vec3d2 = this.getSelf().getViewVector(0);
                            Vec3 vec3d3 = vec3d.add(vec3d2.x * 15, vec3d2.y * 15, vec3d2.z * 15);
                            double mag = 0.05F;

                            if (this.attackTimeDuring > 10) {
                                mag += Math.pow(attackTimeDuring-10, 1.4) / 1000;
                            }
                            BlockHitResult blockHit = this.getSelf().level().clip(
                                    new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                                            this.getSelf()));

                            Vec3 blockCenterPlus = blockHit.getBlockPos().getCenter();

                            assultVec = assultVec.add(
                                    blockCenterPlus.subtract(this.getSelf().position().add(assultVec)).normalize().scale(mag)
                            );
                            Vec3 yes = this.getSelf().position().add(assultVec);
                            double post = stand.position().distanceTo(blockHit.getBlockPos().getCenter());
                            if (post< 1.5){
                                stand.setYRot(this.getSelf().getYHeadRot() % 360);
                                stand.setXRot(this.getSelf().getXRot());
                            } else {

                                Direction gdir = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                                Vec2 grot = new Vec2(getLookAtPlaceYaw(stand,blockCenterPlus),
                                        getLookAtPlacePitch(stand,blockCenterPlus)
                                        );
                                grot =  RotationUtil.rotWorldToPlayer(grot,gdir);
                                stand.setYRot(grot.x);
                                stand.setXRot(grot.y);
                            }
                            if (post < 0.4){
                                stand.setPos(blockHit.getBlockPos().getCenter());
                            } else {
                                stand.setPos(yes);
                            }

                            if (this.getActivePower() == PowerIndex.POWER_1_BONUS) {
                                if (post <= 2){
                                    ((StandUser) this.getSelf()).roundabout$tryBlockPosPower(PowerIndex.POWER_2,
                                            true, blockHit.getBlockPos());
                                    return;
                                }
                            }

                            if ((stand.isTechnicallyInWall() && this.getActivePower() != PowerIndex.POWER_1_BONUS) ||
                                    stand.position().distanceTo(this.getSelf().position()) > 10){
                                stopSoundsIfNearby(ASSAULT_NOISE, 32, false);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                            }
                            AABB BB2 = stand.getBoundingBox();
                            if (this.attackTimeDuring > 11) {
                                if (this.getActivePower() != PowerIndex.POWER_1_BONUS){
                                    tryAssaultHit(stand, BB1, BB2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void rollSkin(){
        StandUser user = getUserData(this.self);
        if (this.self instanceof WitherSkeleton
        || this.self instanceof WitherBoss){
            user.roundabout$setStandSkin(TheWorldEntity.BLACK_SKIN);
        } else if (this.self instanceof Drowned ||
                this.self instanceof WaterAnimal ||
                this.self instanceof Guardian){
            user.roundabout$setStandSkin(TheWorldEntity.AQUA_SKIN);
        } else if (this.self instanceof IronGolem){
            user.roundabout$setStandSkin(TheWorldEntity.DARK_SKIN);
        } else if (this.self instanceof Zombie){
            user.roundabout$setStandSkin(TheWorldEntity.MANGA_SKIN);
        } else if (this.self instanceof WanderingTrader){
            user.roundabout$setStandSkin(TheWorldEntity.ARCADE_SKIN);
        } else if (this.self instanceof EnderMan ||
                this.self instanceof Endermite){
            user.roundabout$setStandSkin(TheWorldEntity.HERITAGE_SKIN);
        } else if (this.self instanceof EnderDragon ||
                this.self instanceof Warden){
            user.roundabout$setStandSkin(TheWorldEntity.OVER_HEAVEN);
        } else if (this.self instanceof Witch){
            user.roundabout$setStandSkin(TheWorldEntity.PART_7_BLUE);
        } else if (this.self instanceof Raider){
            user.roundabout$setStandSkin(TheWorldEntity.OVA_SKIN);
        } else if (this.self instanceof Skeleton){
            user.roundabout$setStandSkin(TheWorldEntity.PART_7_SKIN);
        }
    }
    @Override
    public float getFloatOutRange(){
        return 10F;
    }

    @Override
    public float getFinalAttackKnockback(){
        return (((float)this.chargedFinal/(float)maxSuperHitTime)*2.0F);
    }
    @Override
    public float getFinalPunchStrength(Entity entity){
        float ret;
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            ret = (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
            if (this.chargedFinal >= maxSuperHitTime){
                ret +=1;
            }
        } else {
            ret = (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+3;
            if (this.chargedFinal >= maxSuperHitTime){
                ret +=2;
            }
        }
        return ret;
    }

    @Override
    public double getGrabRange(){
        if (this.getActivePower() == PowerIndex.POWER_1_BONUS){
            return 121;
        }
        return super.getGrabRange();
    }

    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == ASSAULT_NOISE) {
            return ASSAULT_NOISE;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.theWorldAttackMultOnPlayers *0.01));
    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.theWorldAttackMultOnMobs *0.01));
    }
    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.75F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(2.5F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }
    @Override
    public float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(3));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(8));
        }
    }
    @Override
    public float getBarrageHitStrength(Entity entity){
        float str = super.getBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        theWorldSettings.theWorldAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        theWorldSettings.theWorldAttackMultOnMobs * 0.01)));
            }
        }

        if (entity instanceof LivingEntity){
            if (str >= ((LivingEntity) entity).getHealth() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit){
                if (entity instanceof Player) {
                    str = 0.00001F;
                } else {
                    str = 0F;
                }
            }
        }
        return str;
    }
    @Override
    public float getKickBarrageFinisherStrength(Entity entity){
        float str = super.getKickBarrageFinisherStrength(entity);
        if (this.getReducedDamage(entity)){
            return str*levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    theWorldSettings.theWorldAttackMultOnPlayers *0.01)));
        } else {
            return str*levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    theWorldSettings.theWorldAttackMultOnMobs *0.01)));
        }
    }
    @Override
    public float getKickBarrageHitStrength(Entity entity){
        float str = super.getKickBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        theWorldSettings.theWorldAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        theWorldSettings.theWorldAttackMultOnMobs * 0.01)));
            }
        }
        return str;
    }
    @Override
    public float getImpalePunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (3F * (ClientNetworking.getAppropriateConfig().
                                generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs((float) (17F * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        }
    }

    public boolean tryAssaultHit(StandEntity stand, AABB bb1, AABB bb2){
        bb1 = bb1.inflate(1.6F);
        bb2 = bb2.inflate(1.6F);

        AABB $$2 = bb1.minmax(bb2);
        List<Entity> $$3 = stand.level().getEntities(stand, $$2);
        if (!$$3.isEmpty()) {
            for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                Entity $$5 = $$3.get($$4);
                if ($$5 instanceof LivingEntity LE && !$$5.is(this.getSelf()) && $$5.showVehicleHealth() &&
                        !$$5.isInvulnerable() && $$5.isAlive() && !(this.self.isPassenger() &&
                        this.self.getVehicle().getUUID() == $$5.getUUID()) && stand.getSensing().hasLineOfSight($$5)){

                    hitParticlesCenter(LE);


                    if (this.StandDamageEntityAttack($$5,getAssaultStrength($$5), 0.4F, this.self)){
                        addEXP(3,LE);
                        if (!getAssaultEarlyTime()) {
                            MainUtil.makeBleed($$5, 0, 100, null);
                        } else {
                            MainUtil.makeBleed($$5, 0, 50, null);
                        }
                    } else if (((LivingEntity) $$5).isBlocking()) {
                        if (!getAssaultEarlyTime()) {
                            MainUtil.knockShieldPlusStand($$5,40);
                        } else {
                            MainUtil.knockShieldPlusStand($$5,30);
                        }
                    }

                    stopSoundsIfNearby(ASSAULT_NOISE, 100, false);
                    stand.setYRot(getLookAtEntityYaw(stand,$$5));
                    stand.setXRot(getLookAtEntityPitch(stand,$$5));
                    this.self.level().playSound(null, this.self.blockPosition(),  ModSounds.PUNCH_4_SOUND_EVENT,
                            SoundSource.PLAYERS, 0.95F, 1.3F);
                    int cdr = ClientNetworking.getAppropriateConfig().theWorldSettings.assaultCooldown;
                    if (this.getSelf() instanceof ServerPlayer) {
                        S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                                PowerIndex.SKILL_1, cdr);
                    }
                    this.setCooldown(PowerIndex.SKILL_1, cdr);
                    this.setAttackTimeDuring(-12);
                    animateStand(TheWorldEntity.ASSAULT_PUNCH);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getAssaultEarlyTime(){
        return getAttackTimeDuring() < 20;
    }
    public float getAssaultStrength(Entity entity){
        float mult = 1;
        boolean isReduced = this.getReducedDamage(entity);
        if (getAttackTimeDuring() > 95){
            mult = 1.7F;
        } else if (getAttackTimeDuring() > 70){
            mult = 1.4F;
        } else if (getAttackTimeDuring() > 45){
            mult = 1.2F;
        } else if (getAssaultEarlyTime() && isReduced){
            mult = 0.75F;
        }

        if (isReduced){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.7F*mult));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(7F*mult));
        }
    }
    public float getGrabThrowStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.1F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            case SKILL_1_NORMAL -> {
                assaultOrFBarrageClient();
            }
            case SKILL_1_CROUCH -> {
                impaleOrFBarrageClient();
            }


            case SKILL_2_NORMAL -> {
                blockAndEntityGrabClient();
            }
            case SKILL_2_GUARD, SKILL_2_CROUCH_GUARD -> {
                phaseGrabClient();
            }
            case SKILL_2_CROUCH -> {
                itemGrabClient();
            }

            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                tryToStandLeapClient();
            }

            case SKILL_4_NORMAL, SKILL_4_CROUCH, SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                doTSClient();
            }

        }
    }

    @Override
    public void tryToDashClient(){
        if (isUsingAssault())
            return;
        super.tryToDashClient();
    }
    @Override
    public void tryToStandLeapClient(){
        if (isUsingAssault())
            return;
        super.tryToStandLeapClient();
    }

    @Override
    public void blockAndEntityGrabClient(){
        if (doAssaultGrabClient())
            return;
        super.blockAndEntityGrabClient();
    }
    @Override
    public void itemGrabClient(){
        if (doAssaultGrabClient())
            return;
        super.itemGrabClient();
    }

    public void assaultOrFBarrageClient(){
        if (clientForwardBarrage())
            return;
        if (hasBlock() || hasEntity())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            if (!this.isGuarding()) {
                if (canExecuteMoveWithLevel(getAssaultLevel())) {
                    if (!this.isBarrageCharging() && this.getActivePower() != PowerIndex.BARRAGE_CHARGE_2) {
                        if (this.activePower == PowerIndex.POWER_1 || this.activePower == PowerIndex.POWER_1_BONUS) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                            tryPowerPacket(PowerIndex.NONE);
                        } else {
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                            tryPowerPacket(PowerIndex.POWER_1);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void tickPower(){

        //Roundabout.LOGGER.info("AT: "+this.attackTime+" ATD: "+this.attackTimeDuring+" kickstarted: "+this.kickStarted+" APP: "+this.getActivePowerPhase()+" MAX:"+this.getActivePowerPhaseMax());
        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (this.getSelf().getAirSupply() < this.getSelf().getMaxAirSupply() && ((StandUser) this.getSelf()).roundabout$getActive()){
            } else {
                if (this.getSelf().isEyeInFluid(FluidTags.WATER)
                        && !this.getSelf().level().getBlockState(BlockPos.containing(
                        this.getSelf().getX(), this.getSelf().getEyeY(),
                        this.getSelf().getZ())).is(Blocks.BUBBLE_COLUMN)) {
                    /***Removed weird code*/

                } else {
                    if (((StandUser) this.getSelf()).roundabout$getActive()) {
                            this.setAirAmount(Math.min(this.getAirAmount() + 4, this.getMaxAirAmount()));
                    }
                }
            }
        }


        if (((StandUser) this.getSelf()).roundabout$getActive()) {
            if (this.getAirAmount() > 0 && this.getSelf().getAirSupply() < this.getSelf().getMaxAirSupply()) {
                this.getSelf().setAirSupply(Math.max(0, Math.min(this.getSelf().getAirSupply() + 4, this.getSelf().getMaxAirSupply())));
                this.setAirAmount(Math.max(0, Math.min(this.getAirAmount() - 4, this.getMaxAirAmount())));
            }
        }
    }

    public boolean doAssaultGrabClient(){
        if (this.getActivePower() == PowerIndex.POWER_1 && attackTimeDuring >= 0){
            if (!this.onCooldown(PowerIndex.SKILL_2)) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BONUS, true);
                tryPowerPacket(PowerIndex.POWER_1_BONUS);
                return true;
            }
        }
        return false;
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == TheWorldEntity.PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.base");
        } else if (skinId == TheWorldEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.manga");
        } else if (skinId == TheWorldEntity.HERITAGE_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.heritage");
        } else if (skinId == TheWorldEntity.OVA_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.ova");
        } else if (skinId == TheWorldEntity.PART_7_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.part_7");
        } else if (skinId == TheWorldEntity.BLACK_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.black");
        } else if (skinId == TheWorldEntity.PART_7_BLUE){
            return Component.translatable(  "skins.roundabout.the_world.blue_part_7");
        } else if (skinId == TheWorldEntity.OVER_HEAVEN){
            return Component.translatable(  "skins.roundabout.the_world.over_heaven");
        } else if (skinId == TheWorldEntity.DARK_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.dark");
        } else if (skinId == TheWorldEntity.AQUA_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.aqua");
        } else if (skinId == TheWorldEntity.ARCADE_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.arcade");
        } else if (skinId == TheWorldEntity.AGOGO_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.agogo");
        } else if (skinId == TheWorldEntity.BETA){
            return Component.translatable(  "skins.roundabout.the_world.beta");
        } else if (skinId == TheWorldEntity.ARCADE_SKIN_2){
            return Component.translatable(  "skins.roundabout.the_world.arcade_2");
        } else if (skinId == TheWorldEntity.FOUR_DEE_EXPERIENCE){
            return Component.translatable(  "skins.roundabout.the_world.four_dee");
        } else if (skinId == TheWorldEntity.ULTIMATE_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.ultimate");
        } else if (skinId == TheWorldEntity.ULTIMATE_KARS_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.ultimate_kars");
        } else if (skinId == TheWorldEntity.SCARLET){
            return Component.translatable(  "skins.roundabout.the_world.scarlet");
        } else if (skinId == TheWorldEntity.THE_NETHER){
            return Component.translatable(  "skins.roundabout.the_world.the_nether");
        } else if (skinId == TheWorldEntity.BRONZE){
            return Component.translatable(  "skins.roundabout.the_world.bronze");
        }
        return Component.translatable(  "skins.roundabout.the_world.base");
    }
    @Override
    public boolean canInterruptPower(){
        if (this.getActivePower() == PowerIndex.POWER_1 || this.getActivePower() == PowerIndex.POWER_1_BONUS){
            int cdr = ClientNetworking.getAppropriateConfig().theWorldSettings.assaultInterruptCooldown;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1, cdr);
            return true;
        } else {
            return super.canInterruptPower();
        }
    }


    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1){
            amt = 100;
        } else {
            amt = (100+((currentLevel-1)*50));
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }

    /**Charge up Time Stop*/
    @Override
    public boolean canChangePower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if ((this.getActivePower() == PowerIndex.NONE || forced) &&
                    (!this.isDazed(this.getSelf()) || move == PowerIndex.BARRAGE_CLASH)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == TheWorldEntity.ARCADE_SKIN || skn == TheWorldEntity.ARCADE_SKIN_2){
            return BARRAGE_NOISE_8;
        }
        if (skn == TheWorldEntity.OVA_SKIN){
            if (rand > 0.5) {
                return BARRAGE_NOISE_5;
            } else {
                return BARRAGE_NOISE_6;
            }
        }
        if (skn == TheWorldEntity.PART_7_SKIN || skn == TheWorldEntity.PART_7_BLUE){
            if (rand > 0.5) {
                return BARRAGE_NOISE_3;
            } else {
                return BARRAGE_NOISE_4;
            }
        }

        if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }

    public static enum TPTYPE {
        GROUND,
        WATER,
        AIR
    }

    public int teleportTime = 0;
    public int postTPStall = 0;

    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (this.attackTimeDuring <= -1) {
            if (this.getSelf().fallDistance > 4 && !(this.getSelf() instanceof FlyingMob) && !this.getSelf().isNoGravity()
                    && !(this.getSelf().noPhysics) && !(this.self instanceof EnderDragon) && !(this.self instanceof WitherBoss)) {
                /**Fall Brace AI*/
                ((StandUser) this.getSelf()).roundabout$summonStand(this.getSelf().level(),true,false);
                if (this.getSelf() instanceof Mob MB){
                    ((IMob)MB).roundabout$setRetractTicks(140);
                }
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                return;
            }
        }
        boolean check = attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf());
        double dist = 0;
        if (check) {
            if (ClientNetworking.getAppropriateConfig().timeStopSettings.mobsTeleportInsteadOfStoppingTime) {
                dist = attackTarget.distanceTo(this.getSelf());
                    TPTYPE tptype = TPTYPE.GROUND;
                if (this.getSelf() instanceof WaterAnimal || this.getSelf() instanceof Guardian) {
                    tptype = TPTYPE.WATER;
                } else if (this.getSelf() instanceof FlyingMob) {
                    tptype = TPTYPE.AIR;
                }
                if (this.attackTimeDuring <= -1) {
                    if (!this.getSelf().isPassenger()) {
                        teleportTime = Math.max(0, teleportTime - 1);
                        if (teleportTime == 0 && !(this.getSelf() instanceof Creeper CREEP && CREEP.isIgnited())) {
                            if (dist <= 8 && !(this.getSelf() instanceof Creeper)) {
                                Vec3 pos = this.getSelf().position().add(0, this.getSelf().getEyeHeight(), 0);
                                float p = 0;
                                float y = 0;
                                if (this.getSelf() instanceof Villager || this.getSelf() instanceof Skeleton) {
                                    p = getLookAtEntityPitch(this.getSelf(), attackTarget);
                                    y = getLookAtEntityYaw(this.getSelf(), attackTarget);
                                }
                                if (this.teleport(tptype)) {
                                    if (this.getSelf() instanceof Villager) {
                                        for (int i = 0; i < 4; i++) {
                                            KnifeEntity $$7 = new KnifeEntity(this.getSelf().level(), this.getSelf(), ModItems.KNIFE.getDefaultInstance(), pos);
                                            $$7.pickup = AbstractArrow.Pickup.DISALLOWED;
                                            $$7.shootFromRotationWithVariance(this.getSelf(),
                                                    p,
                                                    y,
                                                    -0.5F, 1.5F, 1.0F);
                                            this.getSelf().level().addFreshEntity($$7);
                                        }
                                    } else if (this.getSelf() instanceof Skeleton) {
                                        Arrow $$7 = new Arrow(this.getSelf().level(), pos.x, pos.y, pos.z);
                                        $$7.pickup = AbstractArrow.Pickup.DISALLOWED;
                                        $$7.shootFromRotation(this.getSelf(),
                                                p,
                                                y,
                                                0F, 3.0F, 1.0F);
                                        $$7.setOwner(this.getSelf());
                                        this.getSelf().level().addFreshEntity($$7);
                                    }
                                    teleportTime = 200;
                                    postTPStall = 8;
                                }
                            } else if (dist < 40) {
                                if (this.teleportTowards(attackTarget, tptype)) {
                                    if (this.getSelf() instanceof Creeper) {
                                        this.teleportTime = 100;
                                    } else {
                                        this.teleportTime = 200;
                                    }
                                    postTPStall = 8;
                                }
                            }

                        }
                    }
                }
            } else {
                if (!onCooldown(PowerIndex.SKILL_4) && this.getActivePower() == PowerIndex.NONE && !this.isStoppingTime()){
                    this.setMaxChargeTSTime(ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld);
                    this.setChargedTSTicks(Math.min(ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksTheWorld,20));
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SPECIAL, true);
                }
            }
        }
        postTPStall = Math.max(0,postTPStall-1);
        if (postTPStall == 0) {
            if (!(this.getSelf() instanceof Creeper)) {
                if (check) {
                    if ((this.getActivePower() != PowerIndex.NONE)
                            || dist <= 5) {
                        this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                        float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                        this.getSelf().setYRot(yrot);
                        this.getSelf().setYHeadRot(yrot);
                    }

                    if (this.attackTimeDuring == -1 || (this.attackTimeDuring < -1 && this.activePower == PowerIndex.ATTACK)) {
                        Entity targetEntity = getTargetEntity(this.self, -1);
                        if (targetEntity != null && targetEntity.is(attackTarget)) {
                            double RNG = Math.random();
                            if (RNG < 0.4 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge) {
                                wentForCharge = true;
                                if (RNG < 0.1) {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE_2, true);
                                } else {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                                }
                            } else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                                if ((RNG < 0.85 && (this.getSelf() instanceof Hoglin || this.getSelf() instanceof Ravager))
                                || (this.self instanceof JotaroNPC && RNG < 0.47)) {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                    wentForCharge = false;
                                } else {
                                    if (!onCooldown(PowerIndex.SKILL_1_SNEAK) && RNG >= 0.85 && dist <= 3 && !wentForCharge) {
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                                        wentForCharge = true;
                                    } else {
                                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                                        wentForCharge = false;
                                    }
                                }
                            }
                        } else if ((this.getSelf().getHealth() > 20 || this.getSelf() instanceof Piglin
                                || this.getSelf() instanceof DIONPC || this.getSelf() instanceof DiegoNPC
                                || this.getSelf() instanceof AbstractVillager) && dist <= 8 && dist >= 5) {
                            if (!onCooldown(PowerIndex.SKILL_1)) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                            }
                        } else if ((this.getSelf() instanceof Spider || this.getSelf() instanceof Slime
                                || this.getSelf() instanceof DIONPC || this.getSelf() instanceof DiegoNPC
                                || this.getSelf() instanceof Rabbit || this.getSelf() instanceof AbstractVillager
                                || this.getSelf() instanceof Piglin || this.getSelf() instanceof Vindicator) &&
                                this.getSelf().onGround() && dist <= 19 && dist >= 5) {
                            if (!onCooldown(PowerIndex.GLOBAL_DASH)) {
                                if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                                    this.setCooldown(PowerIndex.GLOBAL_DASH, 300);
                                    bonusLeapCount = 3;
                                    this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                                    float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                                    this.getSelf().setYRot(yrot);
                                    this.getSelf().setYRot(yrot);
                                    this.getSelf().setYHeadRot(yrot);
                                    bigLeap(this.getSelf(), 20, 1);
                                    ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    protected boolean teleport(TPTYPE tptype) {
        if (!this.getSelf().level().isClientSide() && this.getSelf().isAlive()) {
            double $$0 = this.getSelf().getX() + (this.getSelf().getRandom().nextDouble() - 0.5) * 19.0;
            double $$1 = this.getSelf().getY() + (double)(this.getSelf().getRandom().nextInt(16) - 8);
            double $$2 = this.getSelf().getZ() + (this.getSelf().getRandom().nextDouble() - 0.5) * 19.0;
            return this.teleport($$0, $$1, $$2,tptype);
        } else {
            return false;
        }
    }

    boolean teleportTowards(Entity $$0,TPTYPE tptype) {
        Vec3 $$1 = new Vec3(this.getSelf().getX() - $$0.getX(), this.getSelf().getY(0.5) - $$0.getEyeY(), this.getSelf().getZ() - $$0.getZ());
        $$1 = $$1.normalize();
        double $$2 = 16.0;
        double $$3 = this.getSelf().getX() + (this.getSelf().getRandom().nextDouble() - 0.5) * 8.0 - $$1.x * 16.0;
        double $$4 = this.getSelf().getY() + (double)(this.getSelf().getRandom().nextInt(16) - 8) - $$1.y * 16.0;
        double $$5 = this.getSelf().getZ() + (this.getSelf().getRandom().nextDouble() - 0.5) * 8.0 - $$1.z * 16.0;
        return this.teleport($$3, $$4, $$5, tptype);
    }

    private boolean teleport(double $$0, double $$1, double $$2,TPTYPE tptype) {
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos($$0, $$1, $$2);

        while ($$3.getY() > this.getSelf().level().getMinBuildHeight() && !this.getSelf().level().getBlockState($$3).blocksMotion()) {
            $$3.move(Direction.DOWN);
        }

        BlockState $$4 = this.getSelf().level().getBlockState($$3);
        boolean $$5 = $$4.blocksMotion();
        boolean $$6 = $$4.getFluidState().is(FluidTags.WATER);
        if (($$5 || tptype.equals(TPTYPE.AIR)) && !$$6) {
            Vec3 $$7 = this.getSelf().position();
            boolean $$8 = randomTeleport($$0, $$1, $$2, true,tptype);
            if ($$8) {
                if (!this.getSelf().isSilent()) {
                    this.getSelf().level().playSound(null, this.getSelf().xo, this.getSelf().yo,
                            this.getSelf().zo, ModSounds.TIME_SNAP_EVENT, this.getSelf().getSoundSource(), 2.0F, 1.0F);
                    this.getSelf().playSound(ModSounds.TIME_SNAP_EVENT, 2.0F, 1.0F);
                }
            }

            return $$8;
        } else {
            return false;
        }
    }

    public boolean randomTeleport(double $$0, double $$1, double $$2, boolean $$3,TPTYPE tptype) {
        double $$4 = this.getSelf().getX();
        double $$5 = this.getSelf().getY();
        double $$6 = this.getSelf().getZ();
        double $$7 = $$1;
        boolean $$8 = false;
        BlockPos $$9 = BlockPos.containing($$0, $$1, $$2);
        Level $$10 = this.getSelf().level();
        if ($$10.hasChunkAt($$9)) {
            boolean $$11 = false;

            while (!$$11 && $$9.getY() > $$10.getMinBuildHeight()) {
                BlockPos $$12 = $$9.below();
                BlockState $$13 = $$10.getBlockState($$12);
                if ($$13.blocksMotion() || tptype.equals(TPTYPE.AIR)) {
                    $$11 = true;
                } else {
                    $$7--;
                    $$9 = $$12;
                }
            }

            if ($$11) {
                AABB bb2 = this.getSelf().getDimensions(this.getSelf().getPose()).makeBoundingBox($$0,$$7,$$2);
                if ($$10.noCollision(null,bb2) &&
                        ((!(tptype.equals(TPTYPE.WATER)) && !$$10.containsAnyLiquid(bb2)) ||
                                (tptype.equals(TPTYPE.WATER) && $$10.containsAnyLiquid(bb2)))) {
                    $$8 = true;
                    packetNearby(new Vector3f((float) $$0, (float) $$7, (float) $$2));
                    this.getSelf().teleportTo($$0, $$7, $$2);
                    packetNearby(new Vector3f((float) $$0, (float) $$7, (float) $$2));
                }
            }
        }
        if (!$$8) {
            this.getSelf().teleportTo($$4, $$5, $$6);
            return false;
        } else {

            if (this.getSelf() instanceof PathfinderMob) {
                ((PathfinderMob)this.getSelf()).getNavigation().stop();
            }

            return true;
        }
    }


    public final void packetNearby(Vector3f blip) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    S2CPacketUtil.sendBlipPacket(serverPlayerEntity, (byte) 2, this.getSelf().getId(),blip);
                }
            }
        }
    }
    @Override
    public void playSPandTWTSSounds(){

        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == TheWorldEntity.OVA_SKIN) {
            playSoundsIfNearby(TIME_STOP_NOISE_7, 100, true);
        } else if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2) {
                playSoundsIfNearby(TIME_STOP_NOISE_11, 100, true);
        } else if (bt == TheWorldEntity.PART_7_BLUE || bt == TheWorldEntity.PART_7_SKIN){
            playSoundsIfNearby(TIME_STOP_NOISE_5, 100, true);
        } else {
            playSoundsIfNearby(TIME_STOP_NOISE_4, 100, true);
        }
    }

    @Override
    public byte getTimeStopShortNoise(){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == TheWorldEntity.OVA_SKIN) {
            return TIME_STOP_NOISE_9;
        } else if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2) {
            return TIME_STOP_NOISE_10;
        }
        return TIME_STOP_NOISE_2;
    }
    @Override
    public byte getTimeResumeNoise(){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == TheWorldEntity.OVA_SKIN) {
            return TIME_RESUME_NOISE_2;
        } else if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2) {
            return TIME_RESUME_NOISE_3;
        }
        return TIME_RESUME_NOISE;
    }
    @Override
    public void playKickBarrageCrySound(){
        if (!this.self.level().isClientSide()) {

            if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof DIOVoice DV) {

                if (!DV.inTheMiddleOfTalking()) {
                    DV.forceTalkingTicks(70);
                    byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
                    if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2) {
                        playSoundsIfNearby(BARRAGE_NOISE_7, 32, false, true);
                        return;
                    }
                    if (bt == TheWorldEntity.OVA_SKIN) {
                        return;
                    }
                    if (bt == TheWorldEntity.PART_7_BLUE || bt == TheWorldEntity.PART_7_SKIN) {
                        playSoundsIfNearby(KICK_BARRAGE_NOISE_3, 32, false, true);
                    } else {
                        double rand = Math.random();
                        byte barrageCrySound;
                        if (rand > 0.5) {
                            barrageCrySound = KICK_BARRAGE_NOISE;
                        } else {
                            barrageCrySound = KICK_BARRAGE_NOISE_2;
                        }
                        playSoundsIfNearby(barrageCrySound, 32, false, true);
                    }
                }
            } else {
                byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
                if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2) {
                    playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_7, 32, false, true);
                    return;
                }
                if (bt == TheWorldEntity.OVA_SKIN) {
                    return;
                }
                if (bt == TheWorldEntity.PART_7_BLUE || bt == TheWorldEntity.PART_7_SKIN) {
                    playStandUserOnlySoundsIfNearby(KICK_BARRAGE_NOISE_3, 32, false, true);
                } else {
                    super.playKickBarrageCrySound();
                }
            }
        }
    }

    @Override
    public void playTSVoiceSound(){
        if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof DIOVoice DV) {
            if (!DV.inTheMiddleOfTalking()) {
                DV.forceTalkingTicks(40);
                playSoundsIfNearby(getTSVoice(), 100, false, true);
            }
        } else {

            playStandUserOnlySoundsIfNearby(getTSVoice(), 100, false, true);
        }
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_3) {
                return ModSounds.TWAU_USHA_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_4) {
            return ModSounds.TWAU_BARRAGE_2_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_5) {
            return ModSounds.OVA_BARRAGE_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_6) {
            return ModSounds.OVA_BARRAGE_2_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_7) {
            return ModSounds.ARCADE_URI_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_8) {
            return ModSounds.ARCADE_BARRAGE_EVENT;
        } else if (soundChoice == SoundIndex.SUMMON_SOUND) {
            if (bt == TheWorldEntity.OVA_SKIN){
                return ModSounds.OVA_SUMMON_THE_WORLD_EVENT;
            } else {
                return ModSounds.WORLD_SUMMON_SOUND_EVENT;
            }
        } else if (soundChoice == LAST_HIT_1_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
        } else if (soundChoice == LAST_HIT_2_NOISE) {
            return ModSounds.THE_WORLD_MUDA_EVENT;
        } else if (soundChoice == LAST_HIT_3_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA2_SOUND_EVENT;
        } else if (soundChoice == LAST_HIT_4_NOISE) {
            return ModSounds.TWAU_MUDA_EVENT;
        } else if (soundChoice == LAST_HIT_5_NOISE) {
            return ModSounds.TWAU_MUDA_2_EVENT;
        } else if (soundChoice == LAST_HIT_6_NOISE) {
            return ModSounds.TWAU_MUDA_3_EVENT;
        } else if (soundChoice == LAST_HIT_7_NOISE) {
            return ModSounds.OVA_MUDA_EVENT;
        } else if (soundChoice == LAST_HIT_8_NOISE) {
            return ModSounds.OVA_MUDA_2_EVENT;
        } else if (soundChoice == LAST_HIT_10_NOISE) {
            return ModSounds.ARCADE_MUDA_EVENT;
        } else if (soundChoice == LAST_HIT_11_NOISE) {
            return ModSounds.ARCADE_MUDA_2_EVENT;
        } else if (soundChoice == LAST_HIT_12_NOISE) {
            return ModSounds.ARCADE_MUDA_3_EVENT;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        } else if (soundChoice == KICK_BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA4_SOUND_EVENT;
        } else if (soundChoice == KICK_BARRAGE_NOISE_2) {
            return ModSounds.THE_WORLD_WRY_EVENT;
        } else if (soundChoice == KICK_BARRAGE_NOISE_3) {
            return ModSounds.TWAU_WRY_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else if (soundChoice == ASSAULT_NOISE){
            return ModSounds.THE_WORLD_ASSAULT_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            if (bt == TheWorldEntity.OVA_SKIN) {
                return ModSounds.OVA_THE_WORLD_EVENT;
            } else if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2){
                return ModSounds.ARCADE_TIMESTOP_2_EVENT;
            } else if (bt == TheWorldEntity.PART_7_BLUE || bt == TheWorldEntity.PART_7_SKIN){
                return ModSounds.TWAU_THE_WORLD_EVENT;
            } else {
                return ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT;
            }
        } else if (soundChoice == TIME_STOP_VOICE_2){
            if (bt == TheWorldEntity.OVA_SKIN) {
                return ModSounds.OVA_THE_WORLD_2_EVENT;
            } else if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2){
                return ModSounds.ARCADE_TIMESTOP_2_EVENT;
            } else if (bt == TheWorldEntity.OVER_HEAVEN){
                return ModSounds.THE_WORLD_OVER_HEAVEN_EVENT;
            } else if (bt == TheWorldEntity.PART_7_BLUE || bt == TheWorldEntity.PART_7_SKIN){
                return ModSounds.TWAU_TIMESTOP_2_EVENT;
            } else {
                return ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT;
            }
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public SoundEvent getImpaleSound(){

        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == TheWorldEntity.ARCADE_SKIN || bt == TheWorldEntity.ARCADE_SKIN_2){
            return ModSounds.ARCADE_IMPALE_EVENT;
        }
        return super.getImpaleSound();
    }

    //public void setSkillIcon(GuiGraphics context, int x, int y, ResourceLocation rl, boolean dull, @Nullable CooldownInstance cooldownInstance){
    public int currentAir = this.getMaxAirAmount();
    @Override
    public int getAirAmount(){
        return currentAir;
    }
    @Override
    public void setAirAmount(int airAmount){
        currentAir = airAmount;
        if (this.getSelf() instanceof ServerPlayer) {
            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) this.getSelf()),
                    PacketDataIndex.S2C_INT_OXYGEN_TANK, currentAir);
        }
    }

    @Override
    public int getLeapLevel(){
        return 6;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y){
        if (isHoldingSneak()){
            if (this.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2) {
                setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_TRAVEL_BARRAGE, PowerIndex.NO_CD);
            } else {
                if (canExecuteMoveWithLevel(getImpaleLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_IMPALE, PowerIndex.SKILL_1_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }

            if (this.isGuarding()) {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_PHASE_GRAB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_ITEM, PowerIndex.SKILL_2);
            }

            boolean done = false;
            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1){

                if (!this.getSelf().onGround() && canStandRebound()) {
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.NO_CD);
                }

            } else {

                if (!this.getSelf().onGround()){
                    if (canVault()){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
                    } else if (this.getSelf().fallDistance > 3){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                    }
                }
            }
            if (!done){
                if (canExecuteMoveWithLevel(getLeapLevel())) {
                    boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                    if (jojoveinLikeKeys){
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_WORLD, PowerIndex.SKILL_3);
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_WORLD, PowerIndex.GLOBAL_DASH);
                    }
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }
        } else {

            if (this.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2) {
                setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_TRAVEL_BARRAGE, PowerIndex.NO_CD);
            } else {
                if (canExecuteMoveWithLevel(getAssaultLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }

            if (this.isGuarding()) {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_PHASE_GRAB, PowerIndex.SKILL_2);
            } else {
                /*If it can find a mob to grab, it will*/
                Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(), 2.1F);
                if (targetEntity != null && canGrab(targetEntity)) {
                    setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_MOB, PowerIndex.SKILL_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_BLOCK, PowerIndex.SKILL_2);
                }
            }


            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.NO_CD);
            } else {
                if (!(((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
                } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3){
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
                }
            }
        }

        boolean exTS = canExecuteMoveWithLevel(getTSLevel());
        boolean exImpTS = canExecuteMoveWithLevel(getImpulseTSLevel());
        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (isHoldingSneak() || (!exTS && exImpTS)){
            if (exImpTS) {
                setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else {
            if (exTS) {
                setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        }
    }

    protected void clampRotation(Entity $$0) {
        $$0.setYBodyRot(this.getSelf().getYRot());
        float $$1 = Mth.wrapDegrees($$0.getYRot() - this.getSelf().getYRot());
        float $$2 = Mth.clamp($$1, -105.0F, 105.0F);
        $$0.yRotO += $$2 - $$1;
        $$0.setYRot($$0.getYRot() + $$2 - $$1);
        $$0.setYHeadRot($$0.getYRot());
    }

    public static final byte DODGE_NOISE = 19;

    public static final byte LAST_HIT_1_NOISE = 120;
    public static final byte LAST_HIT_2_NOISE = 121;
    public static final byte LAST_HIT_3_NOISE = 122;
    public static final byte LAST_HIT_4_NOISE = 123;
    public static final byte LAST_HIT_5_NOISE = 124;
    public static final byte LAST_HIT_6_NOISE = 125;
    public static final byte LAST_HIT_7_NOISE = 126;
    public static final byte LAST_HIT_8_NOISE = 127;

    public static final byte LAST_HIT_10_NOISE = 119;
    public static final byte LAST_HIT_11_NOISE = 118;
    public static final byte LAST_HIT_12_NOISE = 117;
}
