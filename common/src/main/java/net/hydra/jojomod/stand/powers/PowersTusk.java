package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.models.layers.animations.TuskAnimations;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.pathfinding.TuskHoleEntity;
import net.hydra.jojomod.entity.projectile.ReturningObjectEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.projectile.TuskNailEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersTusk extends NewDashPreset {
    public PowersTusk(LivingEntity self) {
        super(self);
    }


    public static final byte
        SYNC_NAILS = PowerIndex.FALL_BRACE_FINISH,

        SHOOT_MODE = PowerIndex.ATTACK,
        CHARGE_NAILS = PowerIndex.SNEAK_ATTACK_CHARGE,
        FIRE_NAIL = PowerIndex.SNEAK_ATTACK,
        FIRE_BOTH_NAILS = PowerIndex.EXTRA_2,
        CHARGE_EXTRA = PowerIndex.RANGED_BARRAGE_CHARGE,
        FIRE_EXTRA_NAILS = PowerIndex.RANGED_BARRAGE,
        CHARGE_NAIL = 52,

        SPIN_LAUNCH = PowerIndex.POWER_1,
        SHOCKWAVE = PowerIndex.POWER_2,
        SLASH = PowerIndex.POWER_1_SNEAK,


        TARGET = PowerIndex.POWER_1_BONUS,
        DRILL = PowerIndex.POWER_2_SNEAK,
        DRILL_FINISH = PowerIndex.POWER_2_SNEAK_EXTRA,
        BRUSHING = PowerIndex.POWER_2_BLOCK,
        BRIDGE = 53,

        WARP = PowerIndex.BARRAGE_CHARGE_2,
        GRAB = PowerIndex.EXTRA_2_FINISH,
        FLATTEN = PowerIndex.RANGED_BARRAGE_2,
        PILOT = PowerIndex.BOUNCE,

        ADVANCE = 54,
        ADVANCE_BARRAGE = 55,
        REVERSE = 56,
        WALL_BREAK = 57,
        DEATH_NAIL = 58,
        DEATH_PUNCH = 59;

    public static final byte
        MINING = 2,
        SHOCKWAVE_SOUND = 3,
        CHARGE_SOUND_2 = 4;

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        switch (soundChoice) {
            case PowersTusk.MINING -> {return ModSounds.TUSK_SAW_MINE_EVENT;}
            case PowersTusk.SHOCKWAVE_SOUND -> {return ModSounds.TUSK_SHOCKWAVE_EVENT;}
            case PowersTusk.CHARGE_SOUND_2 -> {return ModSounds.TUSK_2_CHARGE_EVENT;}
            case SoundIndex.BARRAGE_CRY_SOUND -> {return ModSounds.TUSK_ORA_RUSH_EVENT;}
            case PowersTusk.ADVANCE -> {return ModSounds.TUSK_ADVANCE_EVENT;}
            case SoundIndex.SUMMON_SOUND -> {
                return switch (this.getAct()) {
                    case 2 ->  ModSounds.TUSK_2_SUMMON_EVENT;
                    case 3 ->  ModSounds.TUSK_3_SUMMON_EVENT;
                    case 4 ->  ModSounds.TUSK_4_SUMMON_EVENT;
                    default ->  ModSounds.TUSK_1_SUMMON_EVENT;

                };
            }
        }

        return super.getSoundFromByte(soundChoice);
    }

    public static final int ACT_ONE_CD = 5*20;
    public static final int ACT_TWO_CD = 60*20;
    public static final int CHARGE_MIN = 5;
    public static final int CHARGE_MAX = 35;

    @Override
    public StandEntity getNewStandEntity() {
        return switch (this.act) {
            case 2 -> ModEntities.TUSK_A2.create(this.getSelf().level());
            case 3 -> ModEntities.TUSK_A3.create(this.getSelf().level());
            case 4 -> ModEntities.TUSK_A4.create(this.getSelf().level());
            default -> ModEntities.TUSK_A1.create(this.getSelf().level());
        };
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return this.getSelf() instanceof Player P && ((IPlayerEntity)P).roundabout$getStandLevel() != 1 && this.getActivePower() != PowersTusk.DEATH_NAIL && this.getActivePower() != PowersTusk.DEATH_PUNCH && !(this.deathShot != null && !this.deathShot.isRemoved());
    }

    @Override
    public boolean rendersPlayer() {
        return !this.canSummonStandAsEntity();
    }

    @Override public int getDisplayPowerInventoryYOffset() {return this.getAct() < 3 ? 20 : 0;}
    @Override public int getDisplayPowerInventoryScale() {return this.getAct() == 4 ? 24 : 30;}
    @Override public boolean hasPassiveCombatMode() {return true;}
    @Override public boolean hasShootingModeVisually(HumanoidArm arm) {
        if (!isGunMode() || this.getActivePower() == PowersTusk.SHOOT_MODE || isCharging()) {
            if (renderBothArms()) {
                return true;
            }
            return arm == this.getSelf().getMainArm();
        }
        return false;
    }
    @Override public boolean interceptGuard() {return true;}
    @Override public boolean interceptAttack() {return true;}
    @Override public boolean clickRelease() {return this.getActivePower() == PowersTusk.SHOOT_MODE || isCharging();}
    @Override public byte getActivePowerPhaseMax() {return 2;}

    @Override public boolean isMiningStand() {return (this.getAct() < 3) && !this.isShooting() && this.hasNail() && !this.isInHole() && !this.isCharging() ;}
    @Override public float getAxeMiningSpeed() {return this.getAct() == 1 ? 25.0F : 10.0F;}
    @Override public float getSwordMiningSpeed() {return this.getAct() == 1 ? 30.0F : 10.0F;}
    @Override public float getShovelMiningSpeed() {return this.getAct() == 1 ? 4.0F : 15.0F;}
    @Override public float getPickMiningSpeed() {return this.getAct() == 1 ? 0.1F : 3F;}

    @Override
    public boolean canUseMiningStand() {
        return super.canUseMiningStand() && !this.isShooting() && this.hasNail();
    }



    public static final int flattenTime = 40;
    public float flattenTicks = 0;

    ArrayList<CooldownInstance> nailCooldowns = new ArrayList<>();
    public int getUsedNails() {return nailCooldowns.size();}
    public int getMaxActiveNails() {return 10-getUsedNails();}

    private int[] convertCDToArray() {
        int[] ret = new int[nailCooldowns.size()];
        for(int i=0;i<ret.length;i++) {
            ret[i] = nailCooldowns.get(i).time;
        }
        return ret;
    }
    private void convertArrayToCD(int[] array) {
        ArrayList<CooldownInstance> ret = new ArrayList<>();
        for(int i : array) {
            ret.add(new CooldownInstance(i,i > ACT_ONE_CD ? ACT_TWO_CD : ACT_ONE_CD ));
        }
        this.nailCooldowns = ret;
    }

    private int getMainHandNails() {return Math.min(5, getMaxActiveNails() );}
    private int getOffHandNails() {return Math.max(0,getMaxActiveNails()-5);}
    public int getLeftHandNails() {return this.getSelf().getMainArm() == HumanoidArm.RIGHT ? getOffHandNails() : getMainHandNails();}
    public int getRightHandNails() {return this.getSelf().getMainArm() == HumanoidArm.RIGHT ? getMainHandNails() : getOffHandNails();}


    public boolean hasNail() {return getMaxActiveNails() > 0;}


    private void addNailCooldown(int time) {
        if (this.nailCooldowns.size() < 10) {
            this.nailCooldowns.add(new CooldownInstance(time,time));
        }

    }

    private void expendNails() {
        if (this.nailCharge > 0) {
            if (this.getAct() == 1) {
                for (int i=0;i<this.nailCharge;i++) {
                  this.addNailCooldown(ACT_ONE_CD);
                }
            }
        } else {
            if (this.getAct() > 1) {
                this.addNailCooldown(ACT_TWO_CD);
            }
        }
        if (!isClient() && this.getSelf() instanceof Player) {
            this.saveDiscAndSync();
        }
    }

    public void tickNails() {tickNails(1);}
    public void tickNails(int ticks) {
        for (int i=0;i<this.nailCooldowns.size();i++) {
            CooldownInstance cd = this.nailCooldowns.get(i);
            if (cd.time <= ticks-1) {
                this.nailCooldowns.remove(cd);
            } else {
                cd.time -= ticks;
            }
        }
    }
    @Override
    public void refreshCooldowns() {
        this.nailCooldowns = new ArrayList<>();
        this.extraCharge = 0;
        super.refreshCooldowns();
    }


    public boolean renderBothArms() {
        return (this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.CHARGE_NAILS
                || this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.FIRE_BOTH_NAILS)
                && !this.getSelf().isUsingItem();
    }

    private int act = 1;
    public int getAct() {return this.act;}
    public void setAct(int a,boolean summon) {
        if (a != this.act) {
            this.setPowerNone();
            this.nailCharge = 0;
            this.extraCharge = 0;
            this.act = Mth.clamp(a, 1, 4);
            ConfigManager.saveClientConfig();

            if (summon) {
                this.getStandUserSelf().roundabout$removeStandOut();
                this.getStandUserSelf().roundabout$summonStand(this.getSelf().level(), true, true);
            }
        }
    }
    public boolean isFastSpin() {
        return this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.DRILL
                || this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.BRUSHING
                || this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.CHARGE_NAIL;
    }


    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 1) {
            if (this.getAct() == 1) {
                if (!hasNail() || this.isCharging() || this.getActivePower() == PowersTusk.SHOOT_MODE) {return true;}
                if (this.getSelf() instanceof Player P) {
                    IPlayerEntity IPE = (IPlayerEntity) P;
                    return (!canLaunchItem(IPE.roundabout$getForRealMainHand()) && !canLaunchItem(IPE.roundabout$getForRealOffHand())) || !hasNail();
                }
            } else if (this.getAct() == 2 ) {
                return getHoleTarget() == null;
            } else if (this.getAct() == 3 ) {
                return !canFlatten();
            }
        } else if (slot == 2) {
            if (this.getAct() == 3) {
                return !canGrab();
            }
            if (this.getActivePower() == PowersTusk.SHOOT_MODE) {return true;}
            if (!hasNail()) {return true;}

            if (this.getAct() == 2) {
                if (!isCharging()) {
                    return !isBrushable();
                } else {
                    return !canBridge();
                }
            }
        } else if (slot == 3) {
            if (this.getAct() == 3 && this.isInHole() && this.getPilotingStand() instanceof TuskHoleEntity) {
                return !this.canWarpHoles();
            } else {
                return !hasNail() || this.nailCharge > 5 || this.getActivePower() == PowersTusk.CHARGE_EXTRA || this.getActivePower() == PowersTusk.FLATTEN;
            }
        } else if (slot == 4) {
            return this.getActivePower() == PowersTusk.FLATTEN && !isInHole();
        }
        return super.isAttackIneptVisually(activeP, slot);
    }


    @Override
    public boolean setPowerAttack() {
        if (!isCharging()) {
            byte anim = this.getStandUserSelf().roundabout$getStandAnimation();
            if (anim != PowersTusk.FIRE_NAIL && anim != PowersTusk.FIRE_BOTH_NAILS) {
                this.setAnimation(PowersTusk.SHOOT_MODE, false);
            }
            this.setActivePower(PowersTusk.SHOOT_MODE);
        }
        return true;
    }


    @Override
    public boolean setPowerOther(int move, int lastMove) {

        switch (move) {

            case PowersTusk.CHARGE_NAILS -> {
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.TUSK_1_CHARGE_EVENT, SoundSource.PLAYERS, 1F, 0.8f + nailCharge * 0.15F);
                setAttackTimeDuring(0);
                this.setActivePower(PowersTusk.CHARGE_NAILS);
                this.nailCharge += 1;
                if (nailCharge == 6) {
                    this.setAnimation(PowersTusk.CHARGE_NAILS);
                }
            }
            case PowersTusk.CHARGE_EXTRA -> {
                setAttackTimeDuring(0);
                this.setActivePower(PowersTusk.CHARGE_EXTRA);
                this.extraCharge += 1;
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 2.0F - extraCharge * 0.1F);
            }
            case FIRE_NAIL -> {
                expendNails();

                if (this.getAct() == 1 && this.nailCharge > 5) {
                    this.setAnimation(PowersTusk.FIRE_BOTH_NAILS);
                } else {
                    this.setAnimation(PowersTusk.FIRE_NAIL);
                }

                if (!isClient()) {
                    if (this.getAct() > 1) {
                        this.shootNail(this.getAttackTimeDuring());
                    } else {
                        shootNailBurst();
                    }
                } else {
                    if (this.getAct() == 3 && isInHole()) {
                        this.shootNail(this.getAttackTimeDuring());
                    }
                }
                nailFireDelay = this.getAct() == 1 ? 10 : 6;
                this.setActivePower(PowersTusk.FIRE_NAIL);
                this.setAttackTimeDuring(-5);
            }
            case PowersTusk.FIRE_EXTRA_NAILS -> {
                if (!isClient()) {
                    shootNailBurst(true);
                } else {
                    Vec3 look = getSelf().getLookAngle().multiply(1, 0, 1).normalize();
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.5, look.x, -1, look.z);
                }

                this.setCooldown(PowerIndex.SKILL_EXTRA, this.extraCharge * 50);
                this.extraCharge = 0;
            }

            case PowersTusk.CHARGE_NAIL -> {
                this.setAnimation(PowersTusk.CHARGE_NAIL);
                if (this.getActivePower() != PowersTusk.CHARGE_NAIL) {
                    this.playStandUserOnlySoundsIfNearby(PowersTusk.CHARGE_SOUND_2, 30, false, false);
                    this.setAttackTimeDuring(1);
                }
                this.setActivePower(PowersTusk.CHARGE_NAIL);
            }

            case PowersTusk.SLASH -> setPowerSlash();

            case PowersTusk.SPIN_LAUNCH -> spinLaunch();

            case PowersTusk.SHOCKWAVE -> {
                this.setAnimation(PowersTusk.SHOCKWAVE);
                setAttackTimeDuring(0);
                this.setActivePower(PowersTusk.SHOCKWAVE);
                this.setCooldown(PowerIndex.SKILL_2, 140);
                this.playStandUserOnlySoundsIfNearby(PowersTusk.SHOCKWAVE_SOUND, 20, false, false);
            }

            case PowersTusk.DRILL -> startDrilling();
            case PowersTusk.DRILL_FINISH -> endDrilling();

            case PowersTusk.BRUSHING -> {
                this.setActivePower(PowersTusk.BRUSHING);
                this.setAnimation(PowersTusk.BRUSHING);
                this.setAttackTimeDuring(0);
            }

            case PowersTusk.BRIDGE -> bridgeLaunch();

            case PowersTusk.WARP -> {
                if (this.getPilotingStand() != null) {
                    this.setActivePower(PowersTusk.WARP);
                    this.setAttackTime(0);
                    this.setCooldown(PowerIndex.GLOBAL_DASH, 120);
                    this.setAnimation(PowersTusk.WARP);
                    if (!isClient()) {
                        Vec3 pos = this.getPilotingStand().getPosition(0);
                        this.getSelf().teleportTo(pos.x, pos.y, pos.z);
                    }
                    this.setPiloting(0);
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.TUSK_WARP_EVENT, SoundSource.PLAYERS, 1F, 1F);
                }
            }

            case PowersTusk.FLATTEN -> {
                if (this.getActivePower() != PowersTusk.FLATTEN) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.TUSK_FLATTEN_EVENT, SoundSource.PLAYERS, 1F, 1F);

                    if (isInHole()) {
                        this.setCooldown(PowerIndex.SKILL_1, 140);
                    } else {
                        addNailCooldown(ACT_TWO_CD);
                        if (!isClient()) {
                            TuskHoleEntity tuskHoleEntity = new TuskHoleEntity(this.getSelf().level(), this.getSelf());
                            tuskHoleEntity.setPos(this.getSelf().getPosition(0));
                            this.getSelf().level().addFreshEntity(tuskHoleEntity);
                        }
                    }

                    this.setActivePower(PowersTusk.FLATTEN);
                    this.setAnimation(PowersTusk.FLATTEN);
                    this.setAttackTime(0);
                    this.setAttackTimeDuring(0);
                } else {
                    this.setAttackTimeDuring(PowersTusk.flattenTime); // automatically triggers the end of flattening
                    this.updateUniqueMoves();
                }
            }

            case PowersTusk.PILOT -> {
                if (!isClient()) {
                    TuskHoleEntity tuskHoleEntity = new TuskHoleEntity(this.getSelf().level(), this.getSelf());
                    tuskHoleEntity.setPos(this.getSelf().getPosition(0));
                    this.getSelf().level().addFreshEntity(tuskHoleEntity);
                }
            }

            case PowersTusk.ADVANCE -> {
                StandEntity stand = getStandEntity(this.getSelf());
                if (stand != null) {
                    playSoundsIfNearby(ADVANCE, 27, false);
                    this.setAttackTimeDuring(0);
                    this.setAttackTime(0);
                    this.setActivePower(PowersTusk.ADVANCE);
                    this.poseStand(OffsetIndex.LOOSE);

                    Vec2 twoVec = new Vec2((this.getSelf().getYHeadRot() % 360),(this.getSelf().getXRot()));
                    Direction gdir = ((IGravityEntity)this.getSelf()).roundabout$getGravityDirection();
                    Vec2 twoVecGrav = RotationUtil.rotPlayerToWorld(twoVec,gdir);
                    Vec3 threeVec = new Vec3(0,0.25,0);
                    threeVec = RotationUtil.vecPlayerToWorld(threeVec,gdir);

                    stand.setPos(this.getSelf().getEyePosition(0));
                    Entity target = getTargetEntity(this.getSelf(),5F,40);
                    if (target != null) {
                        if (target instanceof Mob) {
                            ((StandUser)target).roundabout$setDazed((byte)20);
                        }
                        this.tryPower(PowersTusk.ADVANCE_BARRAGE);
                        this.tryPowerPacket(PowersTusk.ADVANCE_BARRAGE);
                        return true;
                    }
                    stand.setYRot(twoVec.x);
                    stand.setXRot(twoVec.y);
                    advanceVec = DamageHandler.getRotationVector(
                            twoVecGrav.y, (twoVecGrav.x)).scale(1.1F).add(threeVec.x,threeVec.y,threeVec.z);
                    stand.setPos(this.getSelf().position().add(advanceVec));

                }
            }

            case PowersTusk.DEATH_NAIL -> {
                this.setActivePower(PowersTusk.DEATH_NAIL);
                this.setAttackTime(0);
                this.setAttackTimeDuring(0);
                this.setAnimation(PowersTusk.CHARGE_NAIL);
                StandEntity stand = this.getStandEntity(this.getSelf());
                if (stand != null) {
                    stand.forceDespawn(true);
                }
            }
        }

        return super.setPowerOther(move,lastMove);
    }

    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        switch (move) {
            case PowersTusk.FIRE_NAIL -> {
                Roundabout.LOGGER.info(""+pos);
                if (!isClient()) {
                    TuskNailEntity tuskNailEntity = new TuskNailEntity(this.getSelf(), this.getSelf().level(), (byte) this.getAct());
                    float time = getChargeScale(this.getAttackTimeDuring());
                    tuskNailEntity.shootFromRotation(this.getPilotingStand(), (float) pos.x, (float) pos.y, -0.5F, Mth.lerp(time, 1.2F, 2F), 0.1F);
                    tuskNailEntity.setPos(this.getPilotingStand().getPosition(0).add(0, 0.2, 0));
                    this.getSelf().level().addFreshEntity(tuskNailEntity);
                } else {
                    Roundabout.LOGGER.error("TUSK HOLE NAIL NOT CLIENTSIDE");
                }
            }
        }
        return super.tryPosPower(move, forced, pos);
    }

    @Override
    public void tickPower() {
        super.tickPower();
        this.tickNails();

        if (this.getAct() == 4 && !isInfiniteSpinning() && !(this.getSelf() instanceof Player P && P.isCreative()) ) {
            this.setAct(3,true);
        }


        if (this.getSelf() instanceof Player P) {
            if (P.isCreative()) {
                this.nailCooldowns = new ArrayList<>();
            }


            if (this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.FLATTEN) {
                this.flattenTicks = this.getAttackTimeDuring();
            } else if (this.flattenTicks > 0) {
                flattenTicks--;
            }

            int id = ((IPlayerEntity) P).roundabout$getControlling();
            Entity getPilotEntity = this.self.level().getEntity(id);
            if (isPacketPlayer()) {

                if (getPilotEntity instanceof LivingEntity LE) {

                    if (LE.isRemoved() || !LE.isAlive() ||
                            MainUtil.cheapDistanceTo2(LE.getX(), LE.getZ(), P.getX(), P.getZ())
                                    > getMaxPilotRange()) {
                        IPlayerEntity ipe = ((IPlayerEntity) P);
                        ipe.roundabout$setIsControlling(0);
                        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
                        ClientUtil.setCameraEntity(null);
                    } else {
                        ClientUtil.setCameraEntity(getPilotingStand());
                    }
                } else {
                    ClientUtil.setCameraEntity(null);
                }

                this.targetHole = null;
                if (this.getAct() == 3 && PowerTypes.isUsingStand(this.getSelf())) {
                    Entity target = MainUtil.raytraceGroundThingsThroughWalls(this.getSelf().level(),this.getSelf(),40);
                    if (target != null) {
                        if (target instanceof TuskHoleEntity THE && !THE.isVortex()) {
                            this.targetHole = THE;
                        }
                    }
                }

            }
        }


        if (this.getAttackTime() > this.getAttackTimeMax()) {
            this.setAttackTimeMax(0);
        }
        if (nailFireDelay > 0) {
            nailFireDelay --;
        }

        if (this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.FIRE_BOTH_NAILS && this.getAttackTime() > 5) {
            this.setAnimation(PowerIndex.NONE);
        }

        StandEntity stand = this.getStandEntity(this.getSelf());
        if (stand != null) {
            if (this.getAct() == 4) {
                if (stand.getAnimation() == StandEntity.BARRAGE && this.getActivePower() != PowersTusk.ADVANCE_BARRAGE) {
                    animateStand(StandEntity.IDLE);
                }
            }
        }

    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();

        switch (this.getActivePower()) {
            case PowersTusk.SHOCKWAVE -> {
                if (this.getAttackTimeDuring() > 10) {
                    this.doShockwave();
                } else {
                    if (!this.getSelf().level().isClientSide()) {
                        if(this.getAttackTimeDuring()%3==0) {
                            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                                    this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                    1, 0.2, 0.2, 0.2, 0.05);
                        }
                    }
                }
            }
            case PowersTusk.SLASH -> updateSlash();
            case PowersTusk.DRILL -> tickDrilling();
            case PowersTusk.BRUSHING -> tickBrushing();
            case PowersTusk.FLATTEN -> {
                if (this.getAttackTimeDuring() > flattenTime) {
                    this.setPowerNone();
                    flattenTicks = 5;
                }
            }
            case PowersTusk.ADVANCE -> tickAdvance();
            case PowersTusk.ADVANCE_BARRAGE -> tickBarrage();

            case PowersTusk.DEATH_NAIL -> {
                if (!isClient()) {
                    if (this.getAttackTimeDuring() > 10) {
                        this.deathShot();
                    }
                }
            }
            case PowersTusk.DEATH_PUNCH -> {
                if (this.getAttackTimeDuring() > 36) {
                    deathTarget = null;
                    this.setPowerNone();
                } else if (this.getAttackTimeDuring() > 33) {
                    if (!isClient()) {
                        deathPunch();
                    }
                } else {
                    StandEntity stand = this.getStandEntity(this.getSelf());
                    if (stand != null && !isClient()) {
                        if (this.attackTimeDuring % 4 == 0) {
                            sendParticlesIfPossible(self.level(), ModParticles.MENACING,
                                    stand.getX(), stand.getY() + 0.3, stand.getZ(),
                                    1, 0.2, 0.2, 0.2, 0.05);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int value) {
        switch (move) {

            case PowersTusk.TARGET -> {
                this.setActivePower(PowersTusk.TARGET);
                this.setAttackTime(0);
                Entity target = this.getSelf().level().getEntity(value);
                if (target != null) {
                    this.getSelf().setLastHurtMob(target);
                }
            }

            case PowersTusk.GRAB -> {
                Entity target = this.getSelf().level().getEntity(value);
                if (target instanceof LivingEntity LE && target.isAttackable() && !MainUtil.isBossMob(target)) {
                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.TUSK_GRAB_EVENT,SoundSource.PLAYERS,1F,1F);
                    this.setPiloting(0);
                    if (!isClient()) {
                        if (StandDamageEntityAttack(target,LE.getHealth() <= 8 ? LE.getHealth()-1 : 8,0,this.getSelf())) {

                            LE.addEffect(new MobEffectInstance(ModEffects.UNBALANCED,200,2));
                            ((StandUser)LE).roundabout$setDazed((byte)10);
                            LE.setDeltaMovement(Vec3.ZERO);
                            Vec3 pos = this.getSelf().getPosition(0);
                            target.teleportTo(pos.x,pos.y,pos.z);
                        }
                    }
                }
            }

            case PowerIndex.POWER_4 -> {
                if (value != this.act) {
                    if (this.isInHole()) {
                        this.setPiloting(0);
                    }
                    this.setAct(value,true);
                    this.setCooldown(PowerIndex.SKILL_4, 50);
                }
            }
            case PowerIndex.EXTRA -> this.nailCharge = value;

            case PowersTusk.REVERSE -> {
                Entity entity = value == 0 ? this.getSelf() : this.getSelf().level().getEntity(value);
                if (entity instanceof LivingEntity LE && LE.hasEffect(ModEffects.INFINITE_SPIN)) {
                    ((StandUser)LE).roundabout$clearInfiniteSpin();
                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.TUSK_REVERSE_EVENT,SoundSource.PLAYERS,1F,1F);
                    this.setInfiniteSpin(0);
                }
            }
        }
        return super.tryIntPower(move, forced, value);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        switch (move) {
            case PowersTusk.WALL_BREAK -> {
                this.setCooldown(PowerIndex.SKILL_2,150);

                if (!isClient()) {
                    Vec3 vec3d = this.getSelf().getEyePosition(0);
                    Vec3 vec3d2 = blockPos.getCenter().subtract(this.getSelf().getEyePosition()).normalize();
                    Vec3 vec3d3 = vec3d.add(vec3d2.multiply(3,3,3));
                    BlockHitResult result = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
                    Direction dir = result.getDirection().getOpposite();


                    Pair<Direction,Direction> args = switch (dir) {
                        case NORTH,SOUTH -> NORTHSOUTH;
                        case EAST,WEST -> EASTWEST;
                        case UP,DOWN -> UPDOWN;
                    };
                    for(int i=0;i<3;i++) {
                        for (int x = -1; x < 2; x++) {
                            for (int y = -1; y < 2; y++) {
                                BlockPos delta = blockPos.relative(dir, i);

                                Direction dirX = switch (x) {
                                    case -1 -> args.getA();
                                    case 1 -> args.getA().getOpposite();
                                    default -> null;
                                };
                                if (dirX != null) {
                                    delta = delta.relative(dirX);
                                }

                                Direction dirY = switch (y) {
                                    case -1 -> args.getB();
                                    case 1 -> args.getB().getOpposite();
                                    default -> null;
                                };
                                if (dirY != null) {
                                    delta = delta.relative(dirY);
                                }
                                BlockState state = this.getSelf().level().getBlockState(delta);
                                if (!state.isAir()) {
                                    ReturningObjectEntity ret = new ReturningObjectEntity(ModEntities.RETURNING_OBJECT,this.getSelf().level());
                                    ret.setPos(delta.getCenter());
                                    MainUtil.takeKnockbackWithY(ret,1,0,-1,0);
                                    ret.setLifetime(40);
                                    ret.setReturnPos(delta);
                                    ret.setState(state);
                                    this.getSelf().level().addFreshEntity(ret);
                                    this.getSelf().level().destroyBlock(delta,false);

                                }
                            }
                        }
                    }
                }
            }
        }
        return super.tryBlockPosPower(move, forced, blockPos);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowersTusk.SYNC_NAILS -> {
                int x = data/1000000;
                int y = (data/1000)%1000;
                int z = data%1000;
                this.setNailColor(x/255.0F,y/255.0F,z/255.0F);
            }
            case PowersTusk.ADVANCE_BARRAGE -> {
                this.setActivePower(PowersTusk.ADVANCE_BARRAGE);
                this.setAttackTimeDuring(0);
                this.setAttackTime(0);
                this.setAttackTimeMax(80);
            }
            case PowersTusk.DEATH_PUNCH -> {
                this.deathTarget = this.getSelf().level().getEntity(data);
            }
        }
        super.updatePowerInt(activePower, data);
    }

    @Override
    public boolean interceptDamageEvent(DamageSource $$0, float $$1) {
        if (this.getAct() == 3 && this.getActivePower() == PowersTusk.FLATTEN ) { // MainUtil.isSpecialDamagE(DamageSource d)
            return !MainUtil.isSpecialDamage($$0);
        }
        return false;
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();

        byte isAct1 = this.getAct() == 1 || level >= 6 ? level : 0;

        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,level < 4 ? 1 : 6, "ability.roundabout.tusk_saw",
                "instruction.roundabout.press_attack", StandIcons.ANUBIS_SLASH,0,isAct1,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,level < 4 ? 1 : 6, "ability.roundabout.tusk_spinthrow",
                "instruction.roundabout.press_skill", StandIcons.TUSK_SPINTHROW,1,isAct1,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,level < 4 ? 1 : 6, "ability.roundabout.tusk_shockwave",
                "instruction.roundabout.press_skill", StandIcons.TUSK_SHOCKWAVE,2,isAct1,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,level < 4 ? 1 : 6, "ability.roundabout.tusk_blast",
                "instruction.roundabout.press_block", StandIcons.SOFT_SHOOTING_MODE,0,isAct1,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,level < 4 ? 3 : 6, "ability.roundabout.tusk_toe_blast",
                "instruction.roundabout.hold_block", StandIcons.ANUBIS_EMPOWER,3,isAct1,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,level < 4 ? 2 : 6, "ability.roundabout.tusk_leap",
                "instruction.roundabout.press_skill_crouch", StandIcons.TUSK_NAILLEAP,3,isAct1,bypass));


        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, 4, "ability.roundabout.tusk_drill",
                "instruction.roundabout.hold_attack", StandIcons.STAR_PLATINUM_PUNCH,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, 4, "ability.roundabout.tusk_target",
                "instruction.roundabout.press_skill", StandIcons.TUSK_TARGET,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118, 4, "ability.roundabout.tusk_brush",
                "instruction.roundabout.press_skill", StandIcons.TUSK_DRILL_BRUSH,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80, 5, "ability.roundabout.tusk_ball_bridge",
                "instruction.roundabout.press_skill_steel", StandIcons.TUSK_BALL_BRIDGE,2,level,bypass));


        $$1.add(drawSingleGUIIcon(context, 18, leftPos+96, topPos+80, 6, "ability.roundabout.tusk_focus",
                "instruction.roundabout.press_attack", StandIcons.RATT_SCOPE_IN,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,6, "ability.roundabout.tusk_flatten",
                "instruction.roundabout.press_skill", StandIcons.TUSK_FLATTEN,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,6, "ability.roundabout.tusk_grab",
                "instruction.roundabout.press_skill_hole", StandIcons.TUSK_GRASP,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,6, "ability.roundabout.tusk_warp",
                "instruction.roundabout.press_skill", StandIcons.TUSK_WORMHOLE,4,level,bypass));


        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80,7, "ability.roundabout.tusk_advance",
                "instruction.roundabout.press_attack", StandIcons.THE_WORLD_ASSAULT,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+99,7, "ability.roundabout.tusk_inverse",
                "instruction.roundabout.press_skill", StandIcons.SOFT_AND_WET_BUBBLE_ENCASEMENT,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+118,7, "ability.roundabout.tusk_wall_breaker",
                "instruction.roundabout.press_skill", StandIcons.MINING,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+153,topPos+80,4, "ability.roundabout.tusk_charge",
                "instruction.roundabout.hold_block", StandIcons.GO_BEYOND,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+153,topPos+99,6, "ability.roundabout.tusk_act_change",
                "instruction.roundabout.press_skill", StandIcons.TUSK_ICONS[this.getAct()],4,level,bypass));
      /*$$1.add(drawSingleGUIIcon(context,18,leftPos+153,topPos+99,0, "ability.roundabout.tusk_spin_growth",
                "instruction.roundabout.passive", StandIcons.STEEL_BALL,4,level,bypass)); */


        return $$1;
    }

    @Override
    public byte getMaxLevel() {
        return 7;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context,x,y,1,switch(act) {
            case 2 -> StandIcons.TUSK_TARGET;
            case 3 -> StandIcons.TUSK_FLATTEN;
            case 4 -> StandIcons.SELF_BURN; // anti-spin
            default -> StandIcons.TUSK_SPINTHROW;
        }, act == 2 ? PowerIndex.NO_CD : PowerIndex.SKILL_1);

        setSkillIcon(context,x,y,2,switch(act) {
            case 2 -> this.getActivePower() == PowersTusk.CHARGE_NAIL ? (canExecuteMoveWithLevel(5) ? StandIcons.TUSK_BALL_BRIDGE : StandIcons.LOCKED)  :  StandIcons.TUSK_DRILL_BRUSH;
            case 3 -> StandIcons.TUSK_GRASP;
            case 4 -> StandIcons.THE_WORLD_GRAB_BLOCK; // wall breaker
            default -> StandIcons.TUSK_SHOCKWAVE;
        }, PowerIndex.SKILL_2);

        if (act == 1 && this.isHoldingSneak()) {
            LockedOrNot(context,x,y,3,StandIcons.TUSK_NAILLEAP,PowerIndex.GLOBAL_DASH,2);
        } else if (act == 3 && this.isInHole()) {
            setSkillIcon(context,x,y,3,StandIcons.TUSK_WORMHOLE,PowerIndex.NO_CD);
        } else {
            setSkillIcon(context,x,y,3,StandIcons.DODGE,PowerIndex.GLOBAL_DASH);
        }

        LockedOrNot(context,x,y,4,StandIcons.TUSK_ICONS[this.getAct()],PowerIndex.SKILL_4,6);

        super.renderIcons(context, x, y);
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                switch (this.getAct()) {
                    case 1 -> clientSpinLaunch();
                    case 2 -> clientTarget();
                    case 3 -> clientFlatten();
           //         case 4 -> clientReverse();
                }
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                switch (this.getAct()) {
                    case 1 -> clientShockwave();
                    case 2 -> {
                        if (this.isCharging() && canBridge() && !onCooldown(PowerIndex.SKILL_2)) {
                            clientBridge();
                        } else {
                            clientBrushing();
                        }
                    }
                    case 3 -> clientGrabEntity();
             //       case 4 -> clientWallBreaker();
                }
            }
            case SKILL_3_NORMAL -> {
                if (this.getAct() == 3 && this.canWarpHoles()) {
                    clientWarpHoles();
                    return;
                }
            }
            case SKILL_3_CROUCH -> {
                if (this.getAct() == 1) {
                    clientLaunch();
                    return;
                } else if (canDash()) {
                    dash();
                }
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> clientChangeActs();
        }

        if (canDash()) {
            super.powerActivate(context);
        }
    }

    public boolean canDash() {
        return this.nailCharge <= 5 && this.getActivePower() != PowersTusk.CHARGE_EXTRA && !isInHole() && this.getActivePower() != PowersTusk.FLATTEN && !isInHole();
    }


    public boolean canLaunchItem(ItemStack itemStack) {
        return itemStack.is(Items.IRON_NUGGET)
                || itemStack.is(Items.GOLD_NUGGET)
                || itemStack.is(Items.FLINT)
                || itemStack.is(ModItems.SNIPER_AMMO)
                || itemStack.is(ModItems.TOMMY_AMMO)
                || itemStack.is(ModItems.SNUBNOSE_AMMO);
    }
    public void clientSpinLaunch() {
        if (!onCooldown(PowerIndex.SKILL_1) && hasNail() && !isCharging()) {
            if (this.getSelf() instanceof Player P) {
                IPlayerEntity IPE = (IPlayerEntity) P;
                if (canLaunchItem(IPE.roundabout$getForRealMainHand())
                    || canLaunchItem(IPE.roundabout$getForRealOffHand())) {
                    tryPower(PowersTusk.SPIN_LAUNCH);
                    tryPowerPacket(PowersTusk.SPIN_LAUNCH);
                }
            }
        }
    }
    public void spinLaunch() {
        this.setAnimation(PowersTusk.SPIN_LAUNCH);
        this.getSelf().level().playSound(null,getSelf().blockPosition(),ModSounds.TUSK_SPIN_THROW_EVENT,SoundSource.PLAYERS,1F,1F);


        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.BLOCK_THROW_EVENT,SoundSource.PLAYERS,0.5F,0.9F+(float)(Math.random()*0.2F));
        this.setCooldown(PowerIndex.SKILL_1,50);
        ItemStack itemStack = null;
        if (this.getSelf() instanceof Player P) {
            IPlayerEntity IPE = (IPlayerEntity) P;
            if (canLaunchItem(IPE.roundabout$getForRealMainHand())) {
                itemStack = IPE.roundabout$getForRealMainHand();
            } else if (canLaunchItem(IPE.roundabout$getForRealMainHand())) {
                itemStack = IPE.roundabout$getForRealOffHand();
            }

            if (itemStack != null) {
                if (!P.isCreative()) {itemStack.shrink(1);}

                ThrownObjectEntity.throwAnObject(this.getSelf(),false,new ItemStack(itemStack.getItem(),1),
                        0.05F,0.05F,0.5F,0.8F,-3F,
                        false, ThrownObjectEntity.SPINTHROW,
                        this.getSelf().getViewXRot(0),this.getSelf().getViewYRot(0),this.getSelf().getEyePosition(),
                        false,1,false);
            }
        }
    }

    public void clientShockwave() {
        if (!onCooldown(PowerIndex.SKILL_2) && hasNail() && !isCharging() && this.getActivePower() != PowersTusk.SHOOT_MODE )  {
            tryPower(PowersTusk.SHOCKWAVE);
            tryPowerPacket(PowersTusk.SHOCKWAVE);
        }
    }

    public List<Entity> ShockwaveHitbox(List<Entity> entities) {
        List<Entity> hitEntities = new ArrayList<>(entities){};
        Direction gravD = ((IGravityEntity) this.self).roundabout$getGravityDirection();

        for (Entity value : entities) {
            if (value.isInvulnerable() || ((!value.isAttackable() || !MainUtil.isStandPickable(value)) && !(value instanceof StandEntity)) || !value.isAlive() || (this.self.isPassenger() && this.self.getVehicle().getUUID() == value.getUUID())
                    || (value instanceof StandEntity SE && SE.getUser() != null && SE.getUser().getUUID() == this.self.getUUID())) {
                hitEntities.remove(value);
            } else {
                int angle = 100;
                Vec2 lookVec = new Vec2(getLookAtEntityYaw(self, value), getLookAtEntityPitch(self, value));
                if (gravD != Direction.DOWN) {
                    lookVec = RotationUtil.rotPlayerToWorld(lookVec.x, lookVec.y, gravD);
                }
                if (!(angleDistance(lookVec.x, self.getYHeadRot() % 360f) <= angle && angleDistance(lookVec.y, self.getXRot()) <= angle)) {
                    hitEntities.remove(value);
                }
            }
        }
        List<Entity> hitEntities2 = new ArrayList<>(hitEntities) {
        };
        for (Entity value : hitEntities) {
            if (value instanceof StandEntity SE && SE.getUser() != null) {
                for (Entity value2 : hitEntities) {
                    if (value2.is(SE.getUser())) {
                        hitEntities2.remove(value);
                    }
                }
            }
        }
        return hitEntities2;
    }

    public void doShockwave() {
        this.setPowerNone();
        this.setActivePowerPhase((byte)2);
        this.setAttackTime(0);
        this.setAttackTimeMax(50);
        if (!isClient()) {

            float reach = 5F;
            float half = reach/2;
            Vec3 start = this.getSelf().getEyePosition();
            Vec3 mid = start.add(this.getSelf().getLookAngle().multiply(half,half,half));
            Vec3 end =  start.add(this.getSelf().getLookAngle().multiply(reach,reach,reach));

            int max = 20;
            for (int i=0;i<max;i++) {
                Vec3 delta = start.lerp(end,(float)i/max);
                for(int k=0;k<2;k++) {
                    spawnParticles(delta.x, delta.y, delta.z);
                }
            }

            List<Entity> targets = ShockwaveHitbox(DamageHandler.genHitbox(self, mid.x, mid.y,
                    mid.z, half, half, half));

            for (Entity target :targets) {
                if (!StandDamageEntityAttack(target,getReducedDamage(target)? 2.0F : 4.0F,0F,this.getSelf())) {
                    if (target instanceof LivingEntity LE) {
                        if (LE.isBlocking()) {
                            MainUtil.knockShieldPlusStand(target,50);
                        }
                    }
                }
            }
        }
    }
    private void spawnParticles(double x, double y, double z) {
        BlockPos blockPos = new BlockPos((int)x,(int)y,(int)z);
        for(int i=0;i<4;i++) {
            if (!this.getSelf().level().getBlockState(blockPos).isSolid()) {

                blockPos = blockPos.below();
            } else {
                break;
            }
        }
        BlockState blockState = this.getSelf().level().getBlockState(blockPos);
        if (!blockState.isAir()) {
            ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), x, blockPos.getY() + 0.8, z,
                    0, 0, 0, 0, 0);

            if (Math.random() < 0.2) {
                ((ServerLevel) this.getSelf().level()).sendParticles(new DustParticleOptions(new Vector3f(1F, 1F, 1F
                        ), 1f),
                        x, blockPos.getY() + 1.1, z,
                        0, 1, 1, 1, 0);
            }
        }

    }


    public void clientLaunch() {
        if (!onCooldown(PowerIndex.GLOBAL_DASH) && this.getSelf().onGround() && canExecuteMoveWithLevel(2)
                && (this.hasNail() && this.nailCharge <= 5 || this.getActivePower() != PowersTusk.CHARGE_EXTRA)) {
            tryPower(PowerIndex.SNEAK_MOVEMENT);
            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
        }
    }
    @Override
    public boolean setPowerSneakMovement(int lastMove) {
        setAnimation(PowerIndex.SNEAK_MOVEMENT);
        this.getSelf().level().playSound(null,getSelf().blockPosition(),ModSounds.TUSK_LEAP_EVENT,SoundSource.PLAYERS,1F,1F);
        this.getStandUserSelf().roundabout$setLeapTicks(60);
        this.getStandUserSelf().roundabout$setLeapIntentionally(true);
        this.setCooldown(PowerIndex.GLOBAL_DASH,200);
        if (this.isClient()) {
            Vec3 look = this.getSelf().getLookAngle().multiply(1, 0, 1).normalize().reverse();
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1, look.x, -3, look.z);
        }
        return true;
    }

    private Entity getHoleTarget() {
        Entity target = MainUtil.getTargetEntity(this.getSelf(),15,10);
        if (target instanceof LivingEntity LE) {
            if (LE.isInvisible()) {
                return null;
            }
        }
        return target;
    }

    public void clientTarget() {
        Entity target = getHoleTarget();
        if (target != null) {
            int id = target.getId();
            tryIntPower(PowersTusk.TARGET,true,id);
            tryIntPowerPacket(PowersTusk.TARGET,id);
        }
    }

    private BlockHitResult getLookedBlock() {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 3, vec3d2.y * 3, vec3d2.z * 3);
        return this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
    }
    public boolean isBrushable() {
        return this.getSelf().level().getBlockState(getLookedBlock().getBlockPos()).getBlock() instanceof BrushableBlock;
    }

    public void clientBrushing() {
        if (!onCooldown(PowerIndex.SKILL_2) && !isAttackIneptVisually(this.getActivePower(),2) ) {
            tryPower(PowersTusk.BRUSHING);
            tryPowerPacket(PowersTusk.BRUSHING);
        }
    }
    public void tickBrushing() {
        if (this.isBrushable()) {
            BlockHitResult blockHitResult = this.getLookedBlock();
            BlockEntity blockEntity = this.getSelf().level().getBlockEntity(blockHitResult.getBlockPos());

            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = getLookedBlock().getDirection();
            BlockPos offset = blockPos.offset(direction.getStepX(),direction.getStepY(),direction.getStepZ());

            if (blockEntity instanceof BrushableBlockEntity BBE && this.getSelf().level().getBlockState(offset).isAir()) {

                if (this.getSelf().tickCount%3 == 0) {
                    spawnDustParticles(this.getSelf().level(), blockHitResult, blockEntity.getBlockState(), this.getSelf().getViewVector(0), this.getSelf().getMainArm());
                }

                if (BBE.brush(this.getSelf().level().getGameTime(),(Player)this.getSelf(),direction) ) {
                    this.setCooldown(PowerIndex.SKILL_2,60);
                    this.setPowerNone();
                }
                return;
            }
        }
        this.setCooldown(PowerIndex.SKILL_2,40);
        this.setPowerNone();
    }
    public void spawnDustParticles(Level level, BlockHitResult blockHitResult, BlockState blockState, Vec3 vec3, HumanoidArm humanoidArm) {
        int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
        int j = level.getRandom().nextInt(7, 12);
        BlockParticleOption blockParticleOption = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
        Direction direction = blockHitResult.getDirection();
        Vec3 delta = switch (direction) {
            case DOWN,UP -> new Vec3(vec3.z(), 0.0, -vec3.x);
            case NORTH -> new Vec3(1.0, 0.0, -0.1);
            case SOUTH -> new Vec3(-1.0, 0.0, 0.1);
            case WEST -> new Vec3(-0.1, 0.0, -1.0);
            case EAST -> new Vec3(0.1, 0.0, 1.0);
        };

        Vec3 vec32 = blockHitResult.getLocation();
        for (int k = 0; k < j; ++k) {
            level.addParticle(blockParticleOption, vec32.x - (double)(direction == Direction.WEST ? 1.0E-6f : 0.0f), vec32.y, vec32.z - (double)(direction == Direction.NORTH ? 1.0E-6f : 0.0f), delta.x() * (double)i * 3.0 * level.getRandom().nextDouble(), 0.0, delta.z() * (double)i * 3.0 * level.getRandom().nextDouble());
        }
    }

    public boolean isBridgeItem(Item i) {
        return i.equals(Items.IRON_INGOT)
               /* || i.equals(Items.GOLD_INGOT)
                || i.equals(Items.NETHERITE_INGOT)
                || i.equals(ModItems.METEORITE_INGOT)*/;
    }
    public boolean isBridgeItem(ItemStack i) {return isBridgeItem(i.getItem());}
    public boolean canBridge() {
        return isBridgeItem(this.getSelf().getMainHandItem()) || isBridgeItem(this.getSelf().getOffhandItem());
    }

    public void clientBridge() {
        tryPower(PowersTusk.BRIDGE);
        tryPowerPacket(PowersTusk.BRIDGE);
    }

    public void clientWarpHoles() {
        tryPower(PowersTusk.WARP);
        tryPowerPacket(PowersTusk.WARP);
    }

    public void clientGrabEntity() {
        Entity grabEntity = this.getGrabEntity();
        if (this.canGrab() && grabEntity != null) {
            tryIntPower(PowersTusk.GRAB,true,grabEntity.getId());
            tryIntPowerPacket(PowersTusk.GRAB,grabEntity.getId());
        }
    }

    public void clientFlatten() {
        if (canFlatten()) {
            if (!onCooldown(PowerIndex.SKILL_1)) {
                tryPower(PowersTusk.FLATTEN);
                tryPowerPacket(PowersTusk.FLATTEN);
            }
        }
    }

    public void clientChangeActs() {
        if (!onCooldown(PowerIndex.SKILL_4) && this.getActivePower() != PowersTusk.FLATTEN && this.canExecuteMoveWithLevel(6)) {
            ClientUtil.openTuskActScreen();
        }
    }

    public float getNailDamage(TuskNailEntity nail, Entity target, int act) {
        boolean bl = MainUtil.getReducedDamage(target);
        float damage = switch (act) {
            case 1 -> bl ? 1.35F : 3.0F;
            case 2,3 -> bl ? 3 : 5;
            case 4 -> bl ? 4 : 2;
            default -> 1.0F;
        };
        if (nail.getExtra() == TuskNailEntity.REDUCED) {
            damage *= 0.2F;
        } else if (nail.getExtra() == TuskNailEntity.GOLDEN) {
            damage *= 2F;
        }
        return damage;
    }
    public float getHoleDamage(Entity target) {
        return MainUtil.getReducedDamage(target) ? 1.5F : 4.0F;
    }


    @Override
    public boolean canCombatModeUse(Item item) {
        if (this.getActivePower() != PowerIndex.NONE) {
            return false;
        }
        return (this.isHoldingSneak() && item instanceof ShieldItem);
    }

    public boolean isGunMode() {
        ClientConfig cf = ConfigManager.getClientConfig();
        if (cf != null) {
            return cf.tuskUsesShootMode;
        }
        return true;
    }
    public boolean renderDrill() {
        return this.getAct() > 1 && this.hasNail();
    }
    public boolean isShooting() {
        return this.getActivePower() == PowersTusk.SHOOT_MODE;
    }
    public boolean canAttack(){
        if (this.getAttackTimeDuring() <= -1) {
            return this.getActivePowerPhase() < this.getActivePowerPhaseMax() || this.getAttackTime() >= this.getAttackTimeMax();
        }
        return false;
    }
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
      //  if (isPiloting()) {return;}
        if (this.isGunMode() && (options.keyUse.isDown())) {
            if (keyIsDown) {
                if (isPiloting()) {
                    pilotInputAttack();
                } else {
                    buttonInputShoot(true, options, this.getAct());
                }
            } else {
                shootRelease();
            }
        } else if (this.isPiloting() && keyIsDown) {
            pilotInputAttack();
        } else {
            switch (this.act) {
                case 1 -> buttonInputSlash(keyIsDown, options);
                case 2 -> buttonInputDrill(keyIsDown, options);
                case 3 -> buttonInputWarp(keyIsDown, options);
                case 4 -> buttonInputAdvance(keyIsDown, options);
            }
        }
    }


    public void buttonInputSlash(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (canAttack()) {
                this.tryPower(PowersTusk.SLASH);
                tryPowerPacket(PowersTusk.SLASH);
            }
        }
    }

    public void setPowerSlash() {
        if (this.activePowerPhase >= 2){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 2) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown;
            }
        }
        this.attackTimeDuring = 0;
        this.setActivePower(PowersTusk.SLASH);
        this.setAttackTime(0);
    }

    public void updateSlash() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                this.setPowerNone();
            } else {
                if ((this.attackTimeDuring == 5 && this.activePowerPhase == 1)
                        || this.attackTimeDuring == 8) {
                    beginSlashAttack();
                }
            }
        }
    }

    public void beginSlashAttack() {
        if (this.getSelf() instanceof Player){
            this.getSelf().swing(InteractionHand.MAIN_HAND);
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-10);
                C2SPacketUtil.standPunchPacket(getTargetEntityId2(3), this.activePowerPhase);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,3,5.0F);
            slashAttack(targetEntity);
        }
    }

    @Override public void punchImpact(Entity entity) {slashAttack(entity);}
    public void slashAttack(Entity target) {
        this.setAttackTimeDuring(-10);
        boolean lastHit = this.getActivePowerPhase() == this.getActivePowerPhaseMax();
        if (target != null) {
            this.getSelf().level().playSound(null,getSelf().blockPosition(),getBrawlPunchSound(),SoundSource.PLAYERS,1F,1.3F+((float)Math.random()*0.2F));

            float damage = lastHit ? 3.0F : 2.0F;
            if (!this.hasNail()) {damage = 0.1F;}
            if (this.getReducedDamage(target)) {damage *= 0.5F;}
            float knockback = lastHit ? 0.3F : 0.05F;

            if (!StandDamageEntityAttack(target,damage,knockback,this.getSelf())) {
                if (lastHit && this.hasNail()) {
                    MainUtil.knockShieldPlusStand(target,60);
                }
            }
        }
        this.setPowerNone();
    }
    private final int maxDrillTime = 50;
    public void buttonInputDrill(boolean keyIsDown, Options options) {
        if (this.getAttackTime() > this.getAttackTimeMax()) {
            if (keyIsDown) {
                if (this.getActivePower() != PowersTusk.DRILL) {
                    tryPower(PowersTusk.DRILL);
                    tryPowerPacket(PowersTusk.DRILL);
                }
            } else if (this.getActivePower() == PowersTusk.DRILL) {
                tryPower(PowersTusk.DRILL_FINISH);
                tryPowerPacket(PowersTusk.DRILL_FINISH);
            }
        } else if (!keyIsDown && this.getActivePower() == PowersTusk.DRILL) {
            tryPower(PowersTusk.DRILL_FINISH);
            tryPowerPacket(PowersTusk.DRILL_FINISH);
        }
    }

    public void startDrilling() {
        this.setAnimation(PowersTusk.DRILL);

        this.setActivePowerPhase((byte)2);
        this.setActivePower(PowersTusk.DRILL);
        this.setAttackTimeDuring(0);
        this.setAttackTime(0);
    }
    public void tickDrilling() {
        this.setAttackTime(0);
        this.setAttackTimeDuring(0);
        this.setAttackTimeMax(this.getAttackTimeMax() + 1);
        if (!this.isClient() && this.getSelf().tickCount % 4 == 0) {
            Entity target = this.getTargetEntity(this.getSelf(),3,5);
            if (target != null && this.hasNail()) {
                Vec3 kb = target.getDeltaMovement();
                StandDamageEntityAttack(target,0.3F,0,this.getSelf());
                target.setDeltaMovement(kb);
            }
        }
        if (this.getAttackTimeMax() > maxDrillTime) {
            endDrilling();
        }
    }
    public void endDrilling() {

        if (!this.isClient()) {
            Entity target = this.getTargetEntity(this.getSelf(),3,5);
            if (target != null && this.hasNail()) {
                float knocbackStrength = 0.5F+((float)this.getAttackTimeMax()/maxDrillTime)*1.5F;
                if (!StandDamageEntityAttack(target,2F,knocbackStrength,this.getSelf())) {
                    MainUtil.knockShieldPlusStand(target,80);
                }
            }
        }
        this.setPowerNone();
        this.setAttackTimeMax(Math.min(this.getAttackTimeMax() + 40,55));
        this.setAnimation(PowersTusk.DRILL_FINISH);
    }

    public void bridgeLaunch() {
        if (!isClient()) {
            ItemStack stack = null;
            if (canBridge()) {
                if (isBridgeItem(this.getSelf().getMainHandItem())) {
                    stack = this.getSelf().getMainHandItem();
                } else if (isBridgeItem(this.getSelf().getOffhandItem())) {
                    stack = this.getSelf().getOffhandItem();
                }
            }
            if (stack != null) {
                ThrownObjectEntity object = new ThrownObjectEntity(this.getSelf(), this.getSelf().level(),stack.copyWithCount(1),true);
                object.setPos(this.getSelf().getPosition(0).add(0,this.getSelf().getEyeHeight()*0.8F,0));
                object.setStyle(ThrownObjectEntity.BRIDGETHROW);
                float time = this.getChargeScale(this.getAttackTimeDuring());
                object.standDamagePlayer = Mth.lerp(time,2,4);
                object.standDamageMob = object.standDamagePlayer * 2;
                object.shootFromRotation(this.getSelf(),this.getSelf().getXRot(),this.getSelf().getYRot(),-0.5F,Mth.lerp(time,1F,3F),0.05F);
                this.getSelf().level().addFreshEntity(object);

                if (!(this.getSelf() instanceof Player P) || !P.isCreative()) {
                    stack.shrink(1);
                }
            }
        }
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.TUSK_2_CHARGE_EVENT,SoundSource.PLAYERS,1F,1F);
        this.setCooldown(PowerIndex.SKILL_2,100);
        this.setPowerNone();
        this.setAnimation(PowersTusk.BRIDGE);
        this.stopSoundsIfNearby(PowersTusk.CHARGE_SOUND_2,30,false);
        this.getSelf().level().playSound(null,getSelf().blockPosition(),ModSounds.TUSK_SPIN_THROW_EVENT,SoundSource.PLAYERS,1F,1F);
        this.nailFireDelay = 10;
        this.expendNails();
    }
    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        if (entity instanceof TuskHoleEntity) {
            entity.xxa = kpi.leftImpulse;
            entity.zza = kpi.forwardImpulse;
        }
    }


    public boolean canWarpHoles() {
        if (this.getAct() == 3) {
            if (isInHole() && this.getPilotingStand() instanceof TuskHoleEntity THE) {
                if (THE.getTimeInHole() > 10 + THE.distanceTo(this.getSelf())*1.5) {
                    return THE.level().getBlockState(THE.blockPosition()).isAir();
                }
            }
        }
        return false;
    }
    public boolean canGrab() {
        if (this.getAct() == 3) {
            if (isInHole() && this.getPilotingStand() instanceof TuskHoleEntity THE) {
                if (THE.getTimeInHole() > 20) {
                    return this.getGrabEntity() instanceof LivingEntity;
                }
            }
        }
        return false;
    }

    public boolean canFlatten() {
        return this.getSelf().onGround() && canUseHole() && !this.getSelf().isPassenger() || this.getActivePower() == PowersTusk.FLATTEN;
    }

    public Entity getGrabEntity() {
        if (this.isClient()) {
            if (isInHole()) {
                TuskHoleEntity THE = (TuskHoleEntity) getPilotingStand();
                Vec3 pos = THE.getPosition(0);
                List<Entity> targets = MainUtil.genHitbox(this.getSelf().level(),pos.x-1,pos.y-1,pos.z-1,pos.x+1,pos.y+1,pos.z+1);
                targets.remove(this.getSelf());
                return MainUtil.AttackHitboxNear(THE, targets, 2);

            }
        }
        return null;
    }

    public boolean canUseHole() {
        return this.hasNail() || this.isInHole() || this.getActivePower() == PowersTusk.FLATTEN;
    }

    public TuskHoleEntity targetHole = null;
    public void buttonInputWarp(boolean keyIsDown, Options options) {
        if (keyIsDown && canUseHole() && this.getAttackTime() > 5) {
            if (targetHole != null) {
                this.setAttackTime(0);
                this.setPiloting(targetHole.getId());
                tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, targetHole.getId());
            }

        }
    }

    @Override
    public void pilotInputAttack() {
        if ( (isGunMode() && this.getActivePower() == PowersTusk.SHOOT_MODE) || this.getActivePower() == PowersTusk.FIRE_NAIL || this.getActivePower() == PowersTusk.CHARGE_NAIL) {
            tryPower(PowersTusk.CHARGE_NAIL);
            tryPowerPacket(PowersTusk.CHARGE_NAIL);
        } else {
            if (this.getAttackTime() >= 5) {
                if (this.self instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    ipe.roundabout$setIsControlling(0);
                }
                this.setAttackTime(0);
                this.setPiloting(0);
                tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
            }
        }
    }


    @Override
    public boolean canPilotPlaceBlock(ItemStack stack) {
        return false;
    }

    @Override
    public int getPilotPlaceRange() {
        return this.getPilotingStand() != null ? 3 : 0;
    }

    @Override
    public boolean pilotInputInteract() {
        if (isGunMode() ) {
            tryPower(PowersTusk.SHOOT_MODE);
            tryPowerPacket(PowersTusk.SHOOT_MODE);
        } else if (this.hasNail() && this.nailFireDelay == 0) {
            tryPower(PowersTusk.CHARGE_NAIL);
            tryPowerPacket(PowersTusk.CHARGE_NAIL);
        }
        return true;
    }

    public void synchToCamera() {
        if (isPiloting()) {
            LivingEntity ent = getPilotingStand();
            if (ent != null) {
                ClientUtil.synchToCamera(ent);
            }
        }
    }

    @Override
    public void setPiloting(int ID) {
        if (this.getSelf() instanceof Player P) {
            IPlayerEntity ipe = (IPlayerEntity) P;
            ipe.roundabout$setIsControlling(ID);
        }
    }

    @Override
    public LivingEntity getPilotingStand() {
        if (this.getSelf() instanceof Player P) {
            IPlayerEntity ipe = (IPlayerEntity) P;
            Entity ent = this.getSelf().level().getEntity(ipe.roundabout$getControlling());
            if (ent instanceof LivingEntity LE) {
                return LE;
            }
        }
        return null;
    }

    @Override
    public boolean isPiloting() {
        return getPilotingStand() instanceof TuskHoleEntity;
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        if (PowerTypes.isUsingStand(player)) {
            if (this.getAct() == 2 && this.getSelf().getLastHurtMob() == ent && this.getActivePower() == PowersTusk.TARGET && this.getAttackTime() < 20 ) {
                return true;
            }

            if (this.getAct() == 3 && !(this.getPilotingStand() != null && this.getPilotingStand().equals(ent)) ) {
                return ent.equals(this.targetHole) || (this.isHoldingSneak() && ent instanceof TuskHoleEntity) && ent.distanceTo(player) < 25;
            }
            Entity targetEntity = this.getTargetEntity(this.getSelf(),2);
            if (this.getAct() == 4 && targetEntity instanceof LivingEntity LE && ((StandUser) LE).roundabout$hasInfiniteSpin()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        if (this.getAct() == 3) {
            if (ent.equals(this.targetHole)) {
                return 2676479;
            } else if (this.isHoldingSneak()) {
                return 16777215;
            }
        } else if (this.getAct() == 4) {
            if (ent.equals(this.getTargetEntity(this.getSelf(),2))) {
                return 16411584;
            }
        }
        return 2676479;
    }

    public TuskHoleEntity getNearbyHole() {
        float radius = 2.0F;
        Vec3 pos = this.getSelf().getPosition(0);
        List<Entity> targets = MainUtil.genHitbox(this.getSelf().level(),
                pos.x-radius,pos.y,pos.z-radius,
                pos.x+radius,pos.y,pos.z+radius);
        for (Entity entity : targets) {
            if (entity instanceof TuskHoleEntity THE && entity.distanceTo(this.getSelf()) < radius) {
                if (this.targetHole == null || targetHole != entity) {
                    return THE;
                }
            }
        }
        return null;
    }
    public boolean isInHole() {
        return isPiloting();
    }


    public Vec3 advanceVec;
    public void buttonInputAdvance(boolean keyIsDown, Options options) {
        if (!isCharging()) {
            if (keyIsDown && this.getAttackTime() > 10) {
                if (canAttack()) {
                    this.setAttackTime(0);
                    if (this.getActivePower() != PowersTusk.ADVANCE) {
                        tryPower(PowersTusk.ADVANCE);
                        tryPowerPacket(PowersTusk.ADVANCE);
                        return;
                    }
                }
                if (!canAttack() || this.getActivePower() == PowersTusk.ADVANCE) {
                    this.setAttackTime(0);
                    tryPower(PowerIndex.NONE);
                    tryPowerPacket(PowerIndex.NONE);
                }
            }
        }
    }

    public void tickAdvance() {
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (this.getActivePower() == PowersTusk.ADVANCE) {
                if (!this.getSelf().level().isClientSide()) {
                    if (this.attackTimeDuring == 108) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    } else if (attackTimeDuring >= 0) {
                        StandEntity stand = getStandEntity(this.self);
                        if (stand != null) {
                            AABB BB1 = stand.getBoundingBox();
                            Vec3 vec3d = this.getSelf().getEyePosition(0);
                            Vec3 vec3d2 = this.getSelf().getViewVector(0);
                            Vec3 vec3d3 = vec3d.add(vec3d2.x * 15, vec3d2.y * 15, vec3d2.z * 15);
                            double mag = 0.1F;// + (Math.pow(Math.max(attackTimeDuring,20)-20, 1.05F) / 1000);


                            BlockHitResult blockHit = this.getSelf().level().clip(
                                    new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                                            this.getSelf()));

                            Vec3 blockCenterPlus = blockHit.getBlockPos().getCenter();


                            advanceVec = advanceVec.add(
                                    blockCenterPlus.subtract(
                                            this.getSelf().position().add(
                                                    advanceVec))
                                            .normalize()
                                            .scale(mag)
                            );
                            Vec3 yes = this.getSelf().position().add(advanceVec);
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

                            if ((stand.isTechnicallyInWall()) ||
                                    stand.position().distanceTo(this.getSelf().position()) > 10){
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                            }

                            AABB BB2 = stand.getBoundingBox();
                            Entity target = checkAdvance(stand);
                            if (target != null) {
                                if (target instanceof Mob ) {
                                    ((StandUser)target).roundabout$setDazed((byte)20);
                                }
                                this.setActivePower(PowersTusk.ADVANCE_BARRAGE);
                                if (this.getSelf() instanceof Player P) {
                                    S2CPacketUtil.sendIntPowerDataPacket(P,PowersTusk.ADVANCE_BARRAGE,0);
                                }
                                this.setAttackTimeDuring(0);
                                this.playBarrageChargeSound();
                                this.animateStand(StandEntity.BARRAGE_CHARGE);
                            }
                        }
                    }
                }
            }
        }
    }


    public Entity checkAdvance(StandEntity stand){
        List<Entity> $$3 = getTargetEntityList(stand,5F,40);
        this.storeEnt = null;
        if (!$$3.isEmpty()) {
            boolean atd =this.attackTimeDuring > 11;
            for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                Entity target = $$3.get($$4);
                if (((atd && target instanceof LivingEntity && !target.is(this.getSelf()) && target.showVehicleHealth() &&
                        !target.isInvulnerable() && target.isAlive() && !(this.self.isPassenger() &&
                        this.self.getVehicle().getUUID() == target.getUUID()) && stand.getSensing().hasLineOfSight(target)
                        &&
                        !(target instanceof FollowingStandEntity SE && (OffsetIndex.OffsetStyle(SE.getOffsetType()) == OffsetIndex.FOLLOW_STYLE ||
                                OffsetIndex.OffsetStyle(SE.getOffsetType()) == OffsetIndex.FIXED_STYLE))
                ) || (target instanceof BlockWallEntity))
                        && (target.distanceTo(stand) < 3)
                ){
                    return target;
                }
            }
        }
        return null;
    }

    public void tickBarrage() {
        if (this.getAttackTimeDuring() == 20) {
            this.playBarrageCrySound();
        } if (this.getAttackTimeDuring() > 20) {
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_CHARGE_SOUND,30,false);

            StandEntity stand = this.getStandEntity(this.getSelf());
            if (stand != null) {
                if (stand.getAnimation() == StandEntity.BARRAGE_CHARGE) {
                    this.animateStand(StandEntity.BARRAGE);
                }

                if (this.self instanceof Player){
                    if (isPacketPlayer()){
                        List<Entity> listE = getTargetEntityList(stand,3,40);
                        int id = -1;
                        if (storeEnt != null){
                            id = storeEnt.getId();
                        }
                        C2SPacketUtil.standBarrageHitPacket(id, this.attackTimeDuring);
                        if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect){
                            for (Entity entity : listE) {
                                if (!(storeEnt != null && entity.is(storeEnt))) {
                                    if (!(entity instanceof StandEntity) && entity.distanceTo(stand) < 3.5) {
                                        C2SPacketUtil.standBarrageHitPacket(entity.getId(), this.attackTimeDuring + 1000);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    List<Entity> listE = getTargetEntityList(stand,3,40);
                    barrageImpact(storeEnt, this.attackTimeDuring);
                    if (!listE.isEmpty()){
                        for (Entity entity : listE) {
                            if (!(storeEnt != null && entity.is(storeEnt))) {
                                if (!(entity instanceof StandEntity) && entity.distanceTo(stand) < 3.5) {
                                    barrageImpact(entity, this.attackTimeDuring + 1000);
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void barrageImpact(Entity entity, int hitNumber){
        if (this.isBarrageAttacking()) {
            boolean sideHit = false;
            if (hitNumber > 1000){
                if (!(ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect)){
                    return;
                }
                hitNumber-=1000;
                sideHit = true;
            }
            boolean lastHit = (hitNumber >= this.getBarrageLength());

            if (entity != null) {
                hitParticles(entity);

                float pow;
                float knockbackStrength = 0;
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
                    pow/=4;
                    knockbackStrength/=6;
                }

                if (entity.hurt(ModDamageTypes.of(this.getSelf().level(),ModDamageTypes.INFINITE_SPIN,this.getSelf()),pow) ) {
                    if (entity instanceof LivingEntity LE) {
                        if (!LE.hasEffect(ModEffects.INFINITE_SPIN) && !MainUtil.isBossMob(LE)) {
                            LE.addEffect(new MobEffectInstance(ModEffects.INFINITE_SPIN,-1));
                        }
                        if (lastHit) {
                            setDazed((LivingEntity) entity, (byte) 0);

                            if (!sideHit) {
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
                }
            } else {
                if (!sideHit) {
                    playBarrageMissNoise(hitNumber);
                }
            }

            if (lastHit) {
                this.setPowerNone();
                this.setAttackTimeDuring(-10);
                this.setAttackTime(0);
                this.animateStand(StandEntity.IDLE);

            }
        }
    }

    @Override
    public byte chooseBarrageSound() {
        return SoundIndex.BARRAGE_CRY_SOUND;
    }

    @Override
    public float getRushDistance() {
        StandEntity stand = this.getStandEntity(this.getSelf());
        if (stand != null) {
            return  (float)this.getSelf().position().distanceTo(stand.position())+super.getRushDistance();
        }
        return super.getRushDistance();
    }
    @Override
    public boolean isBarrageAttacking() {return this.getActivePower() == PowersTusk.ADVANCE_BARRAGE;}
    @Override
    public int getBarrageLength() {return 70;}

    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.TUSK_FINAL_ORA_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1f);
        }
    }

    public void clientReverse() {
        Entity target = this.getTargetEntity(this.getSelf(),2);
        int id = -1;
        if (target instanceof LivingEntity LE) {
            id = target.getId();
        } else if (this.getStandUserSelf().roundabout$hasInfiniteSpin()) {
            id = 0;
        }
        if (id != -1) {
            tryIntPower(PowersTusk.REVERSE,true,id);
            tryIntPowerPacket(PowersTusk.REVERSE,id);
        }
    }

    private Pair<Direction,Direction> UPDOWN = new Pair<>(Direction.NORTH,Direction.EAST);
    private Pair<Direction,Direction> NORTHSOUTH = new Pair<>(Direction.UP,Direction.EAST);
    private Pair<Direction,Direction> EASTWEST = new Pair<>(Direction.NORTH,Direction.UP);

    public void clientWallBreaker() {
        BlockPos result = getTargetBlock().getBlockPos();
        if (!onCooldown(PowerIndex.SKILL_2) && !this.getSelf().level().getBlockState(result).isAir()) {
            tryBlockPosPower(PowersTusk.WALL_BREAK,true,result);
            tryBlockPosPowerPacket(PowersTusk.WALL_BREAK,result);
        }
    }

    private BlockHitResult getTargetBlock() {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 3, vec3d2.y * 3, vec3d2.z * 3);
        return this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
    }

    public Entity deathTarget = null;
    private TuskNailEntity deathShot = null;

    public void deathShot() {
        this.expendNails();
        this.deathShot = this.shootNail(0.75F,0.0F);
        this.setPowerNone();
    }

    public void punchOfDeath(TuskNailEntity nail, Entity target) {
        deathShot = null;
        if (!isClient()) {
            deathTarget = target;
            this.getStandUserSelf().roundabout$summonStand(this.getSelf().level(),true,false);
            StandEntity stand = this.getStandEntity(this.getSelf());
            if (stand != null) {
                if (stand instanceof FollowingStandEntity FSE) {
                    FSE.setOffsetType(OffsetIndex.LOOSE);
                }
                stand.setPos(nail.getPosition(0));
                stand.setAnimation(TuskEntity.DEATH_PUNCH);
            }
            this.setActivePower(PowersTusk.DEATH_PUNCH);
            if (this.getSelf() instanceof Player P) {
                S2CPacketUtil.sendActivePowerPacket(P,PowersTusk.DEATH_PUNCH);
                S2CPacketUtil.sendIntPowerDataPacket(P,PowersTusk.DEATH_PUNCH,deathTarget.getId());
            }
            this.setAttackTimeDuring(0);
            this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.IMPALE_CHARGE_EVENT,SoundSource.PLAYERS,1F,1F);
        }
    }

    public void deathPunch() {
        StandEntity stand = this.getStandEntity(this.getSelf());
        if (stand != null) {
            if (deathTarget != null) {
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.COMBAT_PUNCH_2_EVENT,SoundSource.PLAYERS,1F,0.7F);

                if (deathTarget.getPosition(0).add(new Vec3(0,deathTarget.getEyeHeight()*0.5F,0)).distanceTo(stand.getPosition(0)) < 3) {
                    if (deathTarget.hurt(ModDamageTypes.of(this.getSelf().level(), ModDamageTypes.INFINITE_SPIN, this.getSelf()), 1)) {
                        Vec3 dir = deathTarget.getPosition(0).subtract(stand.getPosition(0)).normalize().reverse();
                        MainUtil.takeKnockbackWithY(deathTarget,0.2F,dir.x,dir.y-0.1F,dir.z);
                        if (deathTarget instanceof LivingEntity LE) {
                            if (MainUtil.isBossMob(LE)) {
                                deathTarget.hurt(ModDamageTypes.of(this.getSelf().level(), ModDamageTypes.INFINITE_SPIN), 15);
                            } else {
                                LE.addEffect(new MobEffectInstance(ModEffects.INFINITE_SPIN, -1));
                            }
                        }
                    }
                }
            }
        }
    }

    private int nailFireDelay = 0;
    private int nailCharge = 0;
    public int getNailCharge() {return nailCharge;}
    private int extraCharge = 0;
    public int getExtraCharge() {return extraCharge;}
    public boolean isCharging() {
        return this.getActivePower() == PowersTusk.CHARGE_NAILS || this.getActivePower() == PowersTusk.CHARGE_EXTRA || this.getActivePower() == PowersTusk.CHARGE_NAIL || this.getActivePower() == PowersTusk.DEATH_NAIL;
    }

    @Override
    public boolean onClickRelease() {
        if (isCharging() || this.getActivePower() == PowersTusk.FIRE_NAIL) {
            this.shootRelease();
            return true;
        }
        if (this.getActivePower() == PowersTusk.SHOOT_MODE) {
            this.setPowerNone();
            tryPowerPacket(PowerIndex.NONE);
        }
        return false;
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (isGunMode() && this.getActivePower() != PowersTusk.DEATH_PUNCH ) {
                tryPower(PowersTusk.SHOOT_MODE);
                tryPowerPacket(PowersTusk.SHOOT_MODE);
            } else {
                buttonInputShoot(true,options,this.getAct());
            }
        }
        return false;
    }
    public void shootRelease() {
        if ( (getAct() == 1 && this.nailCharge > 0 && isCharging() )
                || (this.getActivePower() == PowersTusk.CHARGE_NAIL)) {
            if (nailFireDelay == 0) {
                tryPower(FIRE_NAIL);
                tryPowerPacket(FIRE_NAIL);
                this.nailCharge = 0;
            }
        }
    }
    public void buttonInputShoot(boolean keyIsDown, Options options, int act) {
        if (keyIsDown && this.getActivePower() != PowersTusk.FLATTEN) {
            if (this.getAct() == 1) {
                if (this.nailCharge == 0 && this.extraCharge != 0 && this.getActivePower() != PowersTusk.CHARGE_EXTRA && canExecuteMoveWithLevel(3)) {
                    tryPower(PowersTusk.FIRE_EXTRA_NAILS);
                    tryPowerPacket(PowersTusk.FIRE_EXTRA_NAILS);
                } else if (this.hasNail() && this.nailFireDelay == 0 ) {

                    int delay = 4;
                    if (this.getAttackTimeDuring() >= delay || this.getActivePower() != PowersTusk.CHARGE_NAILS) {

                        if (this.getActivePower() == PowersTusk.FIRE_NAIL) {this.setActivePower(PowerIndex.NONE);}

                        if (nailCharge < getMaxActiveNails()) {
                            tryPower(PowersTusk.CHARGE_NAILS);
                            tryPowerPacket(PowersTusk.CHARGE_NAILS);
                        } else if (this.getAct() == 1 && this.extraCharge < 10 && !onCooldown(PowerIndex.SKILL_EXTRA) && this.getSelf().tickCount%3==0 && canExecuteMoveWithLevel(3)) {
                            tryPower(PowersTusk.CHARGE_EXTRA);
                            tryPowerPacket(PowersTusk.CHARGE_EXTRA);
                        }
                    }
                }


            } else if (nailFireDelay == 0 && hasNail() && canSummonStandAsEntity()) {
                if (this.getAct() == 2 || this.getAct() == 3) {
                    tryPower(CHARGE_NAIL);
                    tryPowerPacket(CHARGE_NAIL);
                } else if (this.getAct() == 4) {
                    tryPower(DEATH_NAIL);
                    tryPowerPacket(DEATH_NAIL);

                }
            }


        }
    }


    @Override
    public float inputSpeedModifiers(float basis) {
        if (nailCharge > 0) {basis *= 0.6F;}
        if (this.getActivePower() == PowersTusk.SHOCKWAVE) {basis *= 0.5F;}
        if (this.getActivePower() == PowersTusk.BRUSHING) {basis *= 0.3F;}
        if (this.getActivePower() == PowersTusk.ADVANCE) {basis *= 0.1F;}

        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelJump() {return this.getActivePower() == PowersTusk.ADVANCE;}
    @Override public boolean cancelSprint() {return super.cancelSprint() || nailCharge > 0 || this.getActivePower() == PowersTusk.ADVANCE;}
    @Override public boolean cancelSprintJump() {return super.cancelSprintJump() || nailCharge > 0 || this.getActivePower() == PowersTusk.ADVANCE;}
    @Override public boolean cancelSprintParticles() {return super.cancelSprintJump() || nailCharge > 0 || this.getActivePower() == PowersTusk.ADVANCE;}



    public void shootNailBurst() {shootNailBurst(false);}
    public void shootNailBurst(boolean extra) {
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.TUSK_1_SHOOT_EVENT,SoundSource.PLAYERS,1F, extra ? 2 : 1);

        this.setActivePower(PowerIndex.NONE);
        int nails = extra ? extraCharge : nailCharge;
        for(int i = 0; i<nails; i++) {
            float accuracy = (float) Math.pow(i*0.35,2.3);
            float force = this.getAct() == 1 ? 1.0F : 1.2F;
            shootNail(force,accuracy,extra, !extra && nailCharge == 10 ? TuskNailEntity.GUARD_BREAK : TuskNailEntity.NONE);
        }
        if (!extra) {
            this.nailCharge = 0;
        }

    }

    public float getChargeScale(float atd) {
        return Mth.inverseLerp(Mth.clamp(atd,CHARGE_MIN,CHARGE_MAX),0,CHARGE_MAX);
    }
    public void shootNail(float charge) {
        float time = getChargeScale(charge);
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.TUSK_2_SHOOT_EVENT,SoundSource.PLAYERS,1F,0.9F+((float) Math.random()*0.2F));
        this.stopSoundsIfNearby(PowersTusk.CHARGE_SOUND_2,30,false);
        shootNail(Mth.lerp(time,1.2F,2F),0.2F,false, time == 1 ? TuskNailEntity.GOLDEN : TuskNailEntity.NONE);
    }
    public TuskNailEntity shootNail(float force, float accuracy) {return shootNail(force,accuracy,false,TuskNailEntity.NONE);}
    public TuskNailEntity shootNail(float force, float accuracy, boolean toes, byte extra) {


        if (isInHole()) {
            if (isClient()) {
                Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                tryPosPowerPacket(PowersTusk.FIRE_NAIL,new Vec3(camera.getXRot(),camera.getYRot(),0));
            }
            return null;
        } else {
            TuskNailEntity tuskNailEntity = new TuskNailEntity(this.getSelf(),this.getSelf().level(),(byte)this.getAct());
            tuskNailEntity.setExtra(extra);

            tuskNailEntity.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -0.5F, force, accuracy);
            Vec3 firingPos = this.getSelf().getPosition(0).add(new Vec3(0,this.getSelf().getEyeHeight()*0.75F,0));
            if (toes) {
                firingPos = this.getSelf().getPosition(0).add(0, 0.2, 0);
            }
            tuskNailEntity.setPos(firingPos);
            this.getSelf().level().addFreshEntity(tuskNailEntity);
            return tuskNailEntity;
        }
    }

    @Override
    public boolean ableToSpin() {
        return canExecuteMoveWithLevel(7);
    }

    public boolean canExecuteAct4() {
        if (this.getSelf() instanceof Player P) {
            return (((IPlayerEntity)P).roundabout$getStandLevel() == 7 && this.isInfiniteSpinning()) || P.isCreative();
        }
        return false;
    }

    @Override
    public ResourceLocation getIconYes(int slot) {
        if (this.isInfiniteSpinning() && slot == 4) {
            return StandIcons.SQUARE_GOLD;
        }
        return super.getIconYes(slot);
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        int barTexture = 12;
        Entity TE = getTargetEntity(playerEntity, 3, this.getBrawlPunchAngle());
        float attackTimeMax = standUser.roundabout$getAttackTimeMax();

        if (this.getAct() == 3 && isPiloting() && this.getPilotingStand() instanceof TuskHoleEntity THE) {
            float dist = this.getSelf().distanceTo(this.getPilotingStand());
            int count = (int) ( THE.getTimeInHole()/(20+3*dist) *15);

            if (count > 15) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 24, 15, 6);
            } else {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, count, 6);
            }
        } else if (attackTimeMax > 0) {
            float attackTime = standUser.roundabout$getAttackTime();
            float finalATime = attackTime / attackTimeMax;
            if (finalATime <= 1) {

                if (this.getActivePowerPhase() == this.getActivePowerPhaseMax()) {
                    barTexture = 24;
                } else {
                    if (TE != null) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }
                }

                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                int finalATimeInt;
                if (this.getActivePower() == PowersTusk.DRILL || this.getActivePower() == PowersTusk.DRILL_FINISH) {
                    finalATimeInt = Math.round(((float)maxDrillTime-this.getAttackTimeMax())/maxDrillTime*15);
                } else {
                    finalATimeInt = Math.round(finalATime * 15);
                }
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


            }
        } else if (TE != null && PowerTypes.isUsingStand(this.getSelf()) && this.getAct() < 3) {
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
        }

        super.renderAttackHud(context, playerEntity, scaledWidth, scaledHeight, ticks, vehicleHeartCount, flashAlpha, otherFlashAlpha);
    }

    @Override
    public boolean shouldRenderPilotingHud() {return false;}
    @Override
    public boolean replaceHudActively() {
        return (this.getUsedNails() > 0 || isCharging() || this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.SHOOT_MODE || this.getActivePower() == PowersTusk.CHARGE_NAIL);
    }

    @Override
    public void getReplacementHUD(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int x, boolean removeNum) {
        StandUser SU = (StandUser) playerEntity;
        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {

            renderNailHud(context,PT,scaledWidth,scaledHeight,x);
        }
    }

    private void renderNailHud(GuiGraphics context, PowersTusk PT, int scaledWidth, int scaledHeight, int x) {
        int l = scaledHeight - 32 + 3;

        context.blit(StandIcons.JOJO_ICONS_2, x, l, 0, 71, 182, 5);

        int deltaX = x;

        for (CooldownInstance cd : this.nailCooldowns) {
            int type = cd.maxTime > 10 * 20 ? 81 : 76;
            context.blit(StandIcons.JOJO_ICONS_2,deltaX,l,0,86,18,5);
            int size = (int) ( 18.2*cd.time/cd.maxTime);
            context.blit(StandIcons.JOJO_ICONS_2,deltaX,l,0,type,size,5);
            deltaX += 18;
        }

        if (PT.getNailCharge() > 0) {
            int k = (int) (18.2F * PT.getNailCharge());
            context.blit(StandIcons.JOJO_ICONS_2, deltaX, l, 0, 86, k, 5);
        }

        if (PT.getExtraCharge() > 0 || PT.onCooldown(PowerIndex.SKILL_EXTRA)) {
            int k = (int) (182 * (PT.getExtraCharge() / 10.0F));
            if (PT.onCooldown(PowerIndex.SKILL_EXTRA)) {
                CooldownInstance cd = this.getCooldown(PowerIndex.SKILL_EXTRA);
                k = (int) (182 * (float)cd.time/500);
            }
            context.blit(StandIcons.JOJO_ICONS_2, x, l+2, 0, 91, k, 5);
        }

        Font font = ClientUtil.getFont();
        String $$6 = PT.getMaxActiveNails() + "";
        int $$7 = (scaledWidth - font.width($$6)) / 2;
        int $$8 = scaledHeight - 31 - 4;
        context.drawString(font, $$6, $$7 + 1, $$8, 0, false);
        context.drawString(font, $$6, $$7 - 1, $$8, 0, false);
        context.drawString(font, $$6, $$7, $$8 + 1, 0, false);
        context.drawString(font, $$6, $$7, $$8 - 1, 0, false);
        context.drawString(font, $$6, $$7, $$8, 2867683, false);
    }

    public void setAnimation(byte b) {setAnimation(b,true);}
    public void setAnimation(byte b,boolean reset) {
        this.getStandUserSelf().roundabout$setStandAnimation(b);
        if (reset) {
            this.getStandUserSelf().roundabout$getWornStandAnimation().stop();
            this.getStandUserSelf().roundabout$getWornStandAnimation().startIfStopped(this.getSelf().tickCount);
        }
    }

    public AnimationDefinition getFirstPersonAnimation() {
        return switch (this.getStandUserSelf().roundabout$getStandAnimation()) {
            case PowersTusk.FIRE_NAIL -> TuskAnimations.Shoot;
            case PowersTusk.FIRE_BOTH_NAILS -> this.getSelf().getMainArm() == HumanoidArm.RIGHT ? TuskAnimations.FireBothLeft : TuskAnimations.FireBothRight;
            case PowersTusk.SHOCKWAVE -> TuskAnimations.Shockwave;
            case PowersTusk.CHARGE_NAILS -> this.getSelf().getMainArm() == HumanoidArm.RIGHT ? TuskAnimations.RaiseLeft : TuskAnimations.RaiseRight;
            case PowerIndex.SNEAK_MOVEMENT -> TuskAnimations.Launch;
            case PowersTusk.SPIN_LAUNCH,PowersTusk.BRIDGE -> TuskAnimations.SpinThrow;
            case PowersTusk.DRILL -> TuskAnimations.Extend;
            case PowersTusk.DRILL_FINISH -> TuskAnimations.ExtendPush;
            case PowersTusk.BRUSHING,PowerIndex.MINING -> TuskAnimations.Extend;
            default -> TuskAnimations.Shooting;
        };
    }

    @Override
    public boolean setPowerNone() {
        this.setAnimation(PowerIndex.NONE);
        return super.setPowerNone();
    }

    @Override
    public boolean setPowerMining(int lastMove) {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.MINING);
        this.setAnimation(PowerIndex.MINING);
        playStandUserOnlySoundsIfNearby(PowersTusk.MINING, 27, false,true);
        return true;
    }

    @Override
    public void setActivePower(byte activeMove) {
        byte activePower = this.getActivePower();
        if (activePower == PowerIndex.MINING && activeMove != activePower) {
            stopSoundsIfNearby(PowersTusk.MINING,20,false);
        }
        if (activePower == PowersTusk.ADVANCE && activeMove != activePower) {
            stopSoundsIfNearby(PowersTusk.ADVANCE,20,false);
        }
        super.setActivePower(activeMove);
        if (activePower == PowersTusk.CHARGE_NAILS && !(activeMove == PowersTusk.CHARGE_NAILS || activeMove == PowersTusk.CHARGE_EXTRA) ) {
            this.shootRelease();
        }

    }

    @Override
    public void onStandSummon(boolean desummon) {
        if (desummon && this.isCharging()) {
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_CRY_SOUND,30,false);
            this.stopSoundsIfNearby(PowersTusk.MINING,30,false);
            this.setPowerNone();
            this.nailCharge = 0;
        } else if (!desummon) {
            if (this.getSelf().isUsingItem()) {
                this.getSelf().stopUsingItem();
            }
            this.setNailColor(this.nailColor.x,this.nailColor.y,this.nailColor.z);
            if (this.getSelf() instanceof Player P) {
                int level = P.isCreative() || getStandUserSelf().roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem
                        ? this.getMaxLevel()
                        : ((IPlayerEntity)P).roundabout$getStandLevel();
                if (level < 4) {
                    this.setAct(1,true);
                } else if (level < 6) {
                    this.setAct(2,true);
                }
            }
        }
        super.onStandSummon(desummon);
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect) {}
    @Override
    public void tickMobAI(LivingEntity attackTarget){}
    public byte worthinessType(){return HUMANOID_WORTHY;}

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        PowersTusk powersTusk = new PowersTusk(entity);
        PowersTusk.buildSkins();
        return powersTusk;
    }

    private static final String location = "textures/stand/tusk/act_";
    public static void buildSkins() {
        for(int i=0;i<4;i++) {
            MANGA_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,location+(i+1)+ "/manga.png");
            BLUE_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,location+(i+1)+ "/blue.png");
            RESONANCE_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,location+(i+1)+ "/resonance.png");
            BREAKER_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,location+(i+1)+ "/breaker.png");
            FRIGID_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,location+(i+1)+ "/frigid.png");
        }
    }
    public static ResourceLocation getSkin(byte skin, int act) {
        if (MANGA_SKIN[0] == null) {buildSkins();}
        ResourceLocation[] skins = switch (skin) {
            case MANGA -> MANGA_SKIN;
            case BLUE -> BLUE_SKIN;
            case RESONANCE -> RESONANCE_SKIN;
            case FRIGID -> FRIGID_SKIN;
            case BREAKER -> BREAKER_SKIN;
            default -> MANGA_SKIN;
        };
        return skins[act-1];
    }


    @Override
    public List<Byte> getPosList() {
        return List.of((byte)0);
    }

    private static ResourceLocation[] MANGA_SKIN = new ResourceLocation[4];
    private static ResourceLocation[] BLUE_SKIN = new ResourceLocation[4];
    private static ResourceLocation[] RESONANCE_SKIN = new ResourceLocation[4];
    private static ResourceLocation[] BREAKER_SKIN = new ResourceLocation[4];
    private static ResourceLocation[] FRIGID_SKIN = new ResourceLocation[4];

    public static final byte
            MANGA = 1,
            BLUE = 2,
            RESONANCE = 3,
            BREAKER = 4,
            FRIGID = 5;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                BLUE,
                BREAKER,
                FRIGID
          //      RESONANCE
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case BLUE -> Component.translatable("skins.roundabout.tusk.blue");
            case RESONANCE -> Component.translatable("skins.roundabout.tusk.resonance");
            case BREAKER -> Component.translatable("skins.roundabout.tusk.breaker");
            case FRIGID -> Component.translatable("skins.roundabout.tusk.frigid");

            default -> Component.translatable("skins.roundabout.tusk.manga");
        };
    }


    @Override
    public Vector3f getLeapColor() {
        return getNailColor();
    }



    private Vector3f nailColor = new Vector3f(127/255F,194/255F,249/255F);
    public Vector3f getNailColor() {
        return nailColor;
    }
    public void setNailColor(float x, float y, float z) {
        this.nailColor = new Vector3f(x,y,z);
        if (this.getSelf() != null && !this.isClient() && this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersTusk.SYNC_NAILS,(int)(this.nailColor.x*255)*1000000 + (int)(this.nailColor.y*255)*1000 + (int)(this.nailColor.z*255) );
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putFloat("nailColorX",this.getNailColor().x);
        $$0.putFloat("nailColorY",this.getNailColor().y);
        $$0.putFloat("nailColorZ",this.getNailColor().z);

        $$0.putByte("act",(byte)this.getAct());

        $$0.putIntArray("nailCooldowns",this.convertCDToArray());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("nailColorX")) {
            this.setNailColor(
                    $$0.getFloat("nailColorX"),
                    $$0.getFloat("nailColorY"),
                    $$0.getFloat("nailColorZ")
            );
        }
        if ($$0.contains("nailCooldowns")) {
            this.convertArrayToCD($$0.getIntArray("nailCooldowns"));
        }
        if ($$0.contains("act")) {
            this.setAct((int)$$0.getByte("act"),true);
        }
    }

    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("Prisma");
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active");
    }

}