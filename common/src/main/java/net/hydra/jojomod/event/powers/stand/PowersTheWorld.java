package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.event.powers.stand.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.item.HarpoonItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Shadow;

public class PowersTheWorld extends TWAndSPSharedPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.WORLD_SUMMON_SOUND_EVENT;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersTheWorld(entity);
    }

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.THE_WORLD.create(this.getSelf().level());
    }

    @Override
    public SoundEvent getLastHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }

    /**Assault Ability*/
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (!this.onCooldown(PowerIndex.SKILL_1)) {
                    if (keyIsDown) {
                        if (!options.keyShift.isDown()) {

                        } else {
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move,forced);
    }

    @Override
    public void setCurrentMaxTSTime(float chargedTSSeconds){
        if (chargedTSSeconds >= 100){
            this.maxChargeTSTime = 180;
            this.setChargedTSTicks(180);
        } else if (chargedTSSeconds == 20) {
            this.maxChargeTSTime = 20;
        } else {
            this.maxChargeTSTime = 100;
        }
    }




    @Override
    public boolean setPowerOther(int move, int lastMove) {

        return super.setPowerOther(move,lastMove);
    }


    @Override
    public void tickPower(){

        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {

        }
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
        if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_2){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    //public void setSkillIcon(GuiGraphics context, int x, int y, ResourceLocation rl, boolean dull, @Nullable CooldownInstance cooldownInstance){

    @Override
    public void renderIcons(GuiGraphics context, int x, int y){
        if (this.getSelf().isCrouching()){

            setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_ITEM, PowerIndex.SKILL_2);

            boolean done = false;
            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1){

                if (!this.getSelf().onGround() && canStandRebound()) {
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.SKILL_3_SNEAK);
                }

            } else {

                if (!this.getSelf().onGround()){
                    if (canVault()){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.SKILL_3);
                    } else if (this.getSelf().fallDistance > 3){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                    }
                }
            }
            if (!done){
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_WORLD, PowerIndex.SKILL_3_SNEAK);
            }
        } else {


            //setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);

            /*If it can find a mob to grab, it will*/
            Entity targetEntity = this.rayCastEntity(this.getSelf(),2F);
            if (targetEntity != null && canGrab(targetEntity)) {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_MOB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_BLOCK, PowerIndex.SKILL_2);
            }


            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.SKILL_3_SNEAK);
            } else {
                if (!(((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.SKILL_3);
                } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3){
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
                }
            }
        }

        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (this.getSelf().isCrouching()){
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP, PowerIndex.SKILL_4);
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

}
