package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.models.layers.animations.TuskAnimations;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.pathfinding.TuskHoleEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.projectile.TuskNailEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.DebugParticles;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersTusk extends NewDashPreset {
    public PowersTusk(LivingEntity self) {
        super(self);
    }

    // TODO: FIX MINING
    // TODO: RENDER TOE CD <- UNFINISHED
    // TODO: RENDER NAILS
    // TODO: SAVE COOLDOWNS
    // TODO: TUSKIFY THE MENU + TUSK ACT ICONS

    public static final byte
        SHOOT_MODE = PowerIndex.ATTACK,
        CHARGE_NAILS = PowerIndex.SNEAK_ATTACK_CHARGE,
        FIRE_NAIL = PowerIndex.SNEAK_ATTACK,
        FIRE_BOTH_NAILS = PowerIndex.EXTRA_2,
        CHARGE_EXTRA = PowerIndex.RANGED_BARRAGE_CHARGE,
        FIRE_EXTRA_NAILS = PowerIndex.RANGED_BARRAGE,

        SPIN_LAUNCH = PowerIndex.POWER_1,
        SHOCKWAVE = PowerIndex.POWER_2,
        SLASH = PowerIndex.POWER_1_SNEAK,
    
        DRILL = PowerIndex.POWER_2_SNEAK,
        DRILL_FINISH = PowerIndex.POWER_2_SNEAK_EXTRA,
        BRUSHING = PowerIndex.POWER_2_BLOCK;

    @Override
    public StandEntity getNewStandEntity() {
        return switch (this.act) {
            case 2 -> ModEntities.TUSK_A2.create(this.getSelf().level());
            case 3 -> ModEntities.TUSK_A3.create(this.getSelf().level());
            case 4 -> ModEntities.TUSK_A4.create(this.getSelf().level());
            default -> ModEntities.TUSK_A1.create(this.getSelf().level());
        };
    }

    @Override public int getDisplayPowerInventoryYOffset() {return this.getAct() < 3 ? 20 : 0;}
    @Override public int getDisplayPowerInventoryScale() {return this.getAct() == 4 ? 24 : 30;}
    @Override public boolean hasPassiveCombatMode() {return this.getActivePower() != PowerIndex.MINING ;}
    @Override public boolean hasShootingModeVisually(HumanoidArm arm) {
        if (!isGunMode() || this.getActivePower() == PowersTusk.SHOOT_MODE) {
            if (renderBothArms()) {
                return true;
            }
            return arm == this.getSelf().getMainArm();
        }
        return false;
    }
    @Override public boolean interceptGuard() {return true;}
    @Override public boolean interceptAttack() {return super.interceptAttack();}
    @Override public boolean clickRelease() {return this.nailCharge > 0 || this.getActivePower() == PowersTusk.SHOOT_MODE;}
    @Override public byte getActivePowerPhaseMax() {return 2;}

    @Override public boolean isMiningStand() {return this.getAct() == 1 || this.getAct() == 2;}
    @Override public float getAxeMiningSpeed() {return this.getAct() == 1 ? 10.0F : 2F;}
    @Override public float getSwordMiningSpeed() {return this.getAct() == 1 ? 20.0F : 2F;}
    @Override public float getShovelMiningSpeed() {return this.getAct() == 1 ? 2.0F : 10.0F;}
    @Override public float getPickMiningSpeed() {return this.getAct() == 2 ? 1.0F : 0.5F;}


    public int getMaxActiveNails() {return 10-(int)Math.ceil(getLightNails()+getHeavyNails());}

    private int getMainHandNails() {return Math.min(5, getMaxActiveNails() );}
    private int getOffHandNails() {return Math.max(0,getMaxActiveNails()-5);}
    public int getLeftHandNails() {return this.getSelf().getMainArm() == HumanoidArm.RIGHT ? getOffHandNails() : getMainHandNails();}
    public int getRightHandNails() {return this.getSelf().getMainArm() == HumanoidArm.RIGHT ? getMainHandNails() : getOffHandNails();}

    public boolean hasNail() {return getMaxActiveNails() > 0;}
    private float lightCooldown = 0;
    public float getLightNails() {return lightCooldown;}
    private float heavyCooldown = 0;
    public float getHeavyNails() {return heavyCooldown;}

    private void expendNails() {
        if (this.nailCharge > 0) {
            if (this.getAct() == 1) {
                this.lightCooldown += this.nailCharge;
            } else {
                this.heavyCooldown += this.nailCharge;
            }
        } else {
            if (this.getAct() > 2) {
                this.heavyCooldown += 1;
            }
        }
    }

    private void tickNails() {
        if (this.lightCooldown > 0) {
            float lightNailLoss = 1.0F / (5.0F * 20.0F);
            lightNailLoss *= Math.max(Math.min(10-this.getMaxActiveNails(),3) * 0.8F,1);
            this.lightCooldown = Mth.clamp(this.lightCooldown- lightNailLoss,0,10);
        }
        if (this.heavyCooldown > 0) {
            float heavyNailLoss = 1.0F / (60.0F * 20.0F);
            heavyNailLoss *= Math.max((Math.min(10-this.getMaxActiveNails(),5) * 0.8F),1);
            this.heavyCooldown = Mth.clamp(this.heavyCooldown- heavyNailLoss,0,10);
        }
        if (this.getSelf() instanceof Player P && P.isCreative()) {
            this.heavyCooldown = 0;
            this.lightCooldown = 0;
        }
    }
    @Override
    public void refreshCooldowns() {
        this.heavyCooldown = 0;
        this.lightCooldown = 0;
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
            this.nailCharge = 0;
            this.extraCharge = 0;
            this.act = Mth.clamp(a, 1, 4);
            ConfigManager.getClientConfig().dynamicSettings.TuskAct = act;
            ConfigManager.saveClientConfig();

            if (summon) {
                this.getStandUserSelf().roundabout$removeStandOut();
                this.getStandUserSelf().roundabout$summonStand(this.getSelf().level(), true, true);
            }
        }
    }
    public boolean isFastSpin() {
        return this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.DRILL || this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.BRUSHING;
    }


    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 1) {

            if (!hasNail() || this.isCharging() || this.getActivePower() == PowersTusk.SHOOT_MODE) {return true;}
            if (this.getAct() == 1) {

                if (this.getSelf() instanceof Player P) {
                    IPlayerEntity IPE = (IPlayerEntity) P;
                    return (!canLaunchItem(IPE.roundabout$getForRealMainHand()) && !canLaunchItem(IPE.roundabout$getForRealOffHand())) || !hasNail();
                }
            }
        }
        if (slot == 2) {
            if (this.getActivePower() == PowersTusk.SHOOT_MODE) {return true;}

            if (!hasNail() || this.isCharging()) {return true;}
            if (this.getAct() == 2) {
                return !isBrushable();
            }
        }
        if (slot == 3) {
            return !hasNail() || this.nailCharge > 5 || this.getActivePower() == PowersTusk.CHARGE_EXTRA;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {

            case PowersTusk.SHOOT_MODE -> {
                if (!isCharging()) {
                    byte anim = this.getStandUserSelf().roundabout$getStandAnimation();
                    if (anim != PowersTusk.FIRE_NAIL && anim != PowersTusk.FIRE_BOTH_NAILS) {
                        this.setAnimation(PowersTusk.SHOOT_MODE, false);
                    }
                    this.setActivePower(PowersTusk.SHOOT_MODE);
                }
            }

            case PowersTusk.CHARGE_NAILS -> {
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
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS,1.0F,1.0F);
            }
            case FIRE_NAIL -> {
                expendNails();

                if (this.getAct() < 3 && this.nailCharge > 5) {
                    this.setAnimation(PowersTusk.FIRE_BOTH_NAILS);
                } else {
                    this.setAnimation(PowersTusk.FIRE_NAIL);
                }

                this.setActivePower(PowersTusk.FIRE_NAIL);
                this.setAttackTimeDuring(0);
                this.setAttackTime(0);
                if (!isClient()) {
                    if (this.getAct() > 2) {
                        this.shootNail(this.getAct() == 3 ? 1.3F : 0.85F, 0.1F);
                    } else {
                        shootNailBurst();
                    }
                }
                nailFireDelay = this.getAct() == 3 ? 5 : 10;
            }
            case PowersTusk.FIRE_EXTRA_NAILS -> {
                if (!isClient()) {
                    shootNailBurst(true);
                } else {
                    Vec3 look = getSelf().getLookAngle().multiply(1, 0, 1).normalize();
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),0.5,look.x,-1,look.z);
                }

                this.setCooldown(PowerIndex.SKILL_EXTRA,this.extraCharge*50 );
                this.extraCharge = 0;
            }
            case PowersTusk.SLASH -> setPowerSlash();

            case PowersTusk.SPIN_LAUNCH -> spinLaunch();

            case PowersTusk.SHOCKWAVE -> {
                this.setAnimation(PowersTusk.SHOCKWAVE);
                setAttackTimeDuring(0);
                this.setActivePower(PowersTusk.SHOCKWAVE);
                this.setCooldown(PowerIndex.SKILL_2,140);
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.IMPALE_CHARGE_EVENT,SoundSource.PLAYERS,0.5F,1.0F);

            }

            case PowersTusk.DRILL -> startDrilling();
            case PowersTusk.DRILL_FINISH -> endDrilling();
            
            case PowersTusk.BRUSHING -> {
                this.setActivePower(PowersTusk.BRUSHING);
                this.setAnimation(PowersTusk.BRUSHING);
                this.setAttackTimeDuring(0);
            }
            
        }
        return super.tryPower(move, forced);
    }


    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
        if (this.getActivePower() == PowersTusk.SHOCKWAVE) {
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
        } else if (this.getActivePower() == PowersTusk.SLASH) {
            updateSlash();
        } else if (this.getActivePower() == PowersTusk.DRILL) {
            tickDrilling();
        } else if (this.getActivePower() == PowersTusk.BRUSHING) {
            tickBrushing();
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int value) {
        switch (move) {
            case PowerIndex.POWER_4 -> {
                if (value != this.act) {
                    this.setAct(value,true);
                    this.setCooldown(PowerIndex.SKILL_4, 120);
                }
            }
            case PowerIndex.EXTRA -> this.nailCharge = value;
        }
        return super.tryIntPower(move, forced, value);
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context,x,y,1,switch(act) {
            case 2 -> StandIcons.TUSK_BALL_BRIDGE;
            case 3 -> StandIcons.D4C_MELT_DODGE; // flatten dodge
            case 4 -> StandIcons.SELF_BURN; // anti-spin
            default -> StandIcons.TUSK_SPINTHROW;
        }, PowerIndex.SKILL_1);

        setSkillIcon(context,x,y,2,switch(act) {
            case 2 -> StandIcons.TUSK_DRILL_BRUSH;
            case 3 -> StandIcons.HIDDEN_HURRICANE; // drag
            case 4 -> StandIcons.THE_WORLD_GRAB_BLOCK; // wall breaker
            default -> StandIcons.TUSK_SHOCKWAVE;
        }, PowerIndex.SKILL_2);

        if (act == 1 && this.isHoldingSneak()) {
            setSkillIcon(context,x,y,3,StandIcons.TUSK_NAILLEAP,PowerIndex.GLOBAL_DASH);
        } else if (act == 3 && this.isHoldingSneak()) { // replace when onto act 3
            setSkillIcon(context,x,y,3,StandIcons.D4C_PARALLEL_RUNNING,PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context,x,y,3,StandIcons.DODGE,PowerIndex.GLOBAL_DASH);
        }

        setSkillIcon(context,x,y,4,StandIcons.TUSK_ICONS[this.getAct()],PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }


    @Override
    public void powerActivate(PowerContext context) {

        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                switch (this.getAct()) {
                    case 1 -> clientSpinLaunch();
                }
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                switch (this.getAct()) {
                    case 1 -> clientShockwave();
                    case 2-> clientBrushing();
                }
            }
            case SKILL_3_CROUCH -> {
                if (this.getAct() == 1) {
                    clientLaunch();
                    return;
                }
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> clientChangeActs();
        }

        if (this.nailCharge <= 5 && this.getActivePower() != PowersTusk.CHARGE_EXTRA) {
            super.powerActivate(context);
        }
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

    @Override
    public void tickPower() {
        super.tickPower();
        this.tickNails();


        if (this.getAttackTime() > this.getAttackTimeMax()) {
            this.setAttackTimeMax(0);
        }
        if (nailFireDelay > 0) {
            nailFireDelay --;
        }

        if (this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.FIRE_BOTH_NAILS && this.getAttackTime() > 5) {
            this.setAnimation(PowerIndex.NONE);
        }

        if (isClient()) {
            this.targetHole = null;
            if (this.getAct() == 3 && PowerTypes.isUsingStand(this.getSelf())) {
                Entity target = MainUtil.raytraceGroundThingsThroughWalls(this.getSelf().level(),this.getSelf(),25);
                if (target != null) {
                    if (target instanceof TuskHoleEntity THE) {
                        this.targetHole = THE;
                    }
                }
            }
        }
    }


    public List<Entity> ShockwaveHitbox(List<Entity> entities, float maxDistance) {
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        Direction gravD = ((IGravityEntity) this.self).roundabout$getGravityDirection();

        for (Entity value : entities) {
            if (value.isInvulnerable() || ((!value.isAttackable() || !MainUtil.isStandPickable(value)) && !(value instanceof StandEntity)) || !value.isAlive() || (this.self.isPassenger() && this.self.getVehicle().getUUID() == value.getUUID())
                    || (value instanceof StandEntity SE && SE.getUser() != null && SE.getUser().getUUID() == this.self.getUUID())) {
                hitEntities.remove(value);
            } else {
                int angle = 18;
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
            this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.IMPALE_HIT_EVENT, SoundSource.PLAYERS,0.5F,1.0F);



            Vec3 start = this.getSelf().getEyePosition();
            Vec3 end =  start.add(this.getSelf().getLookAngle().multiply(5,5,5));

            int max = 20;
            for (int i=0;i<max;i++) {
                Vec3 delta = start.lerp(end,(float)i/max);
                for(int k=0;k<2;k++) {
                    spawnParticles(delta.x, delta.y, delta.z);
                }
            }

            float halfReach = (float) 2.5;
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            List<Entity> targets = ShockwaveHitbox(DamageHandler.genHitbox(self, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), 5);

            for (Entity target :targets) {
                if (target instanceof LivingEntity LE) {
                    if (LE.isBlocking()) {
                        MainUtil.knockShieldPlusStand(target,50);
                    } else {
                        StandDamageEntityAttack(target,getReducedDamage(target)? 2.0F : 4.0F,0F,this.getSelf());
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
    public boolean canLaunchItem(ItemStack itemStack) {
        return itemStack.is(Items.IRON_NUGGET)
                || itemStack.is(Items.GOLD_NUGGET)
                || itemStack.is(Items.FLINT)
                || itemStack.is(ModItems.SNIPER_AMMO)
                || itemStack.is(ModItems.TOMMY_AMMO)
                || itemStack.is(ModItems.SNUBNOSE_AMMO);
    }

    public void clientLaunch() {
        if (!onCooldown(PowerIndex.GLOBAL_DASH) && this.getSelf().onGround()
                && (this.hasNail() && this.nailCharge <= 5 || this.getActivePower() != PowersTusk.CHARGE_EXTRA) ) {
            tryPower(PowerIndex.SNEAK_MOVEMENT);
            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
        }
    }
    @Override
    public boolean setPowerSneakMovement(int lastMove) {
        setAnimation(PowerIndex.SNEAK_MOVEMENT);
        this.getStandUserSelf().roundabout$setLeapTicks(60);
        this.getStandUserSelf().roundabout$setLeapIntentionally(true);
        this.setCooldown(PowerIndex.GLOBAL_DASH,200);
        if (this.isClient()) {
            Vec3 look = this.getSelf().getLookAngle().multiply(1, 0, 1).normalize().reverse();
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1, look.x, -3, look.z);
        }
        return true;
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

    public void clientChangeActs() {
        if (!onCooldown(PowerIndex.SKILL_4)) {
            ClientUtil.openTuskActScreen();
        }
    }

    public float getNailDamage(Entity target, int act) {
        boolean bl = MainUtil.getReducedDamage(target);
        return switch (act) {
            case 1 -> bl ? 1.35F : 3.0F;
            case 2,3 -> bl ? 2 : 4;
            case 4 -> bl ? 4.0F : 9.0F;
            default -> 1.0F;
        };
    }
    public float getHoleDamage(Entity target) {
        return MainUtil.getReducedDamage(target) ? 1.5F : 4.0F;
    }


    @Override
    public boolean canCombatModeUse(Item item) {
        if (this.getActivePower() != PowerIndex.NONE) {
            return false;
        }
        return false;
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
        if (this.isGunMode() && (this.getActivePower() == PowersTusk.SHOOT_MODE || isCharging())) {
            if (keyIsDown) {
                buttonInputShoot(keyIsDown, options, this.getAct());
            } else {
                shootRelease();
            }
        } else {
            switch (this.act) {
                case 1 -> buttonInputSlash(keyIsDown, options);
                case 2 -> buttonInputDrill(keyIsDown, options);
                case 3 -> buttonInputWarp(keyIsDown, options);
                case 4 -> buttonInputAdvance(keyIsDown, options);
            }
        }
        super.buttonInputAttack(keyIsDown, options);
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
                        || this.attackTimeDuring == 6) {
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




    public TuskHoleEntity targetHole = null;
    public void buttonInputWarp(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (targetHole != null) {
                if (isHoleNearby()) {
                    Roundabout.LOGGER.info("SNEAK: " + this.targetHole);
                } else {
                    if (this.getSelf() instanceof Player P) {
                        P.displayClientMessage(Component.translatable("message.tusk.nearby_hole").withStyle(ChatFormatting.RED), true);
                    }
                }
            }
        }
    }


    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        if (this.getAct() == 3) {
            return ent.equals(this.targetHole) || (this.isHoldingSneak() && ent instanceof TuskHoleEntity);
        }
        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        if (this.getAct() == 3) {
            if (ent.equals(this.targetHole)) {
                return isHoleNearby() ? 2676479 : 16755200;
            } else if (this.isHoldingSneak()) {
                return 16777215;
            }
        }
        return 16777215;
    }

    public boolean isHoleNearby() {
        float radius = 2.0F;
        Vec3 pos = this.getSelf().getPosition(0);
        List<Entity> targets = MainUtil.genHitbox(this.getSelf().level(),
                pos.x-radius,pos.y-radius,pos.z-radius,
                pos.x+radius,pos.y+radius,pos.z+radius);
        for (Entity entity : targets) {
            if (entity instanceof TuskHoleEntity && entity.distanceTo(this.getSelf()) < radius) {
                return this.targetHole == null || targetHole != entity;
            }
        }
        return false;
    }

    public void buttonInputAdvance(boolean keyIsDown, Options options) {}



    private int nailFireDelay = 0;
    private int nailCharge = 0;
    public int getNailCharge() {return nailCharge;}
    private int extraCharge = 0;
    public int getExtraCharge() {return extraCharge;}
    public boolean isCharging() {
        return this.getActivePower() == PowersTusk.CHARGE_NAILS || this.getActivePower() == PowersTusk.CHARGE_EXTRA;
    }

    @Override
    public boolean onClickRelease() {
        if (this.nailCharge > 0) {
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
            if (isGunMode() ) {
                tryPower(PowersTusk.SHOOT_MODE);
                tryPowerPacket(PowersTusk.SHOOT_MODE);
            } else {
                buttonInputShoot(keyIsDown,options,this.getAct());
            }
        }
        return false;
    }
    public void shootRelease() {
        if (this.nailCharge > 0) {
            if (getAct() == 1 || getAct() == 2) {
                if (nailFireDelay == 0) {
                    if (this.getActivePower() == PowersTusk.CHARGE_NAILS || this.getActivePower() == PowersTusk.CHARGE_EXTRA ) {
                        tryPower(FIRE_NAIL);
                        tryPowerPacket(FIRE_NAIL);
                        this.nailCharge = 0;
                    }
                }
            }
        }
    }
    public void buttonInputShoot(boolean keyIsDown, Options options, int act) {
        if (keyIsDown) {
            if (this.act == 1 || this.act == 2) {
                if (this.nailCharge == 0 && this.extraCharge != 0 && this.getActivePower() != PowersTusk.CHARGE_EXTRA) {
                    tryPower(PowersTusk.FIRE_EXTRA_NAILS);
                    tryPowerPacket(PowersTusk.FIRE_EXTRA_NAILS);
                } else if (this.hasNail() && this.nailFireDelay == 0 ) {

                    int delay = act == 1 ? 3 : 4;
                    if (this.getAttackTimeDuring() >= delay || this.getActivePower() != PowersTusk.CHARGE_NAILS) {

                        if (this.getActivePower() == PowersTusk.FIRE_NAIL) {this.setActivePower(PowerIndex.NONE);}

                        if (nailCharge < getMaxActiveNails()) {
                            tryPower(PowersTusk.CHARGE_NAILS);
                            tryPowerPacket(PowersTusk.CHARGE_NAILS);
                        } else if (this.getAct() == 1 && this.extraCharge < 10 && !onCooldown(PowerIndex.SKILL_EXTRA)) {
                            tryPower(PowersTusk.CHARGE_EXTRA);
                            tryPowerPacket(PowersTusk.CHARGE_EXTRA);
                        }
                    }
                }


            } else if ( this.getAct() == 3 && hasNail() && nailFireDelay == 0) {
                tryPower(FIRE_NAIL);
                tryPowerPacket(FIRE_NAIL);
            }


        }
    }


    @Override
    public float inputSpeedModifiers(float basis) {
        if (nailCharge > 0) {basis *= 0.6F;}
        if (this.getActivePower() == PowersTusk.SHOCKWAVE) {basis *= 0.5F;}
        if (this.getActivePower() == PowersTusk.BRUSHING) {basis *= 0.3F;}

        return super.inputSpeedModifiers(basis);
    }
    @Override public boolean cancelSprint() {return super.cancelSprint() || nailCharge > 0;}
    @Override public boolean cancelSprintJump() {return super.cancelSprintJump() || nailCharge > 0;}
    @Override public boolean cancelSprintParticles() {return super.cancelSprintJump() || nailCharge > 0;}



    public void shootNailBurst() {shootNailBurst(false);}
    public void shootNailBurst(boolean extra) {
        this.setActivePower(PowerIndex.NONE);
        int nails = extra ? extraCharge : nailCharge;
        for(int i = 0; i<nails; i++) {
            float accuracy = (float) (this.getAct() == 1 ?  Math.pow(i*0.35,2.3) : Math.pow(i*0.5,1.2));
            float force = this.getAct() == 1 ? 1.0F : 1.2F;
            shootNail(force,accuracy,extra, !extra && nailCharge == 10 );
        }
        if (!extra) {
            this.nailCharge = 0;
        }

    }

    public void shootNail(float force, float accuracy) {shootNail(force,accuracy,false,false);}
    public void shootNail(float force, float accuracy, boolean toes, boolean guardBreak) {
        TuskNailEntity tuskNailEntity = new TuskNailEntity(this.getSelf(),this.getSelf().level(),(byte)this.getAct());
        if (guardBreak) {
            tuskNailEntity.setExtra(TuskNailEntity.GUARD_BREAK);
        }


        tuskNailEntity.shootFromRotation(this.getSelf(),this.getSelf().getXRot(), this.getSelf().getYRot(), -0.5F, force, accuracy);
        Vec3 firingPos = this.getSelf().getEyePosition(0);
        if (toes) {
            firingPos = this.getSelf().getPosition(0).add(0,0.2,0);
        }
        tuskNailEntity.setPos(firingPos);
        this.getSelf().level().addFreshEntity(tuskNailEntity);

    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        int barTexture = 0;
        Entity TE = standUser.roundabout$getStandPowers().getTargetEntity(playerEntity, 3, this.getSelf().getYRot());
        float attackTimeMax = standUser.roundabout$getAttackTimeMax();
        if (attackTimeMax > 0) {
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
        }

        super.renderAttackHud(context, playerEntity, scaledWidth, scaledHeight, ticks, vehicleHeartCount, flashAlpha, otherFlashAlpha);
    }

    @Override
    public boolean replaceHudActively() {
        return this.getMaxActiveNails() != 10 || this.getNailCharge() > 0 || this.getStandUserSelf().roundabout$getStandAnimation() == PowersTusk.SHOOT_MODE;
    }

    @Override
    public void getReplacementHUD(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int x, boolean removeNum) {
        int l = scaledHeight - 32 + 3;
        StandUser SU = (StandUser) playerEntity;
        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
            context.blit(StandIcons.JOJO_ICONS_2, x, l, 0, 71, 182, 5);

            int deltaX = x;
            if (PT.getHeavyNails() > 0) {
                int k = (int) (18.2F * PT.getHeavyNails());
                context.blit(StandIcons.JOJO_ICONS_2, deltaX, l, 0, 81, k, 5);
                deltaX += k;
            }
            if (PT.getLightNails() > 0) {
                int k = (int) (18.2F * PT.getLightNails());
                context.blit(StandIcons.JOJO_ICONS_2, deltaX, l, 0, 76, k, 5);
                deltaX += k;
            }
            if (PT.getNailCharge() > 0) {
                int k = (int) (18.2F * PT.getNailCharge());
                context.blit(StandIcons.JOJO_ICONS_2, deltaX, l, 0, 86, k, 5);
                deltaX += k;
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
            case PowersTusk.SPIN_LAUNCH -> TuskAnimations.SpinThrow;
            case PowersTusk.DRILL -> TuskAnimations.Extend;
            case PowersTusk.DRILL_FINISH -> TuskAnimations.ExtendPush;
            case PowersTusk.BRUSHING -> TuskAnimations.Extend;
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
        return true;
    }

    @Override
    public void setActivePower(byte activeMove) {
        byte activePower = this.getActivePower();
        super.setActivePower(activeMove);
        if (activePower == PowersTusk.CHARGE_NAILS && !(activeMove == PowersTusk.CHARGE_NAILS || activeMove == PowersTusk.CHARGE_EXTRA) ) {
            this.shootRelease();
        }
    }

    @Override
    public void onStandSummon(boolean desummon) {
        if (desummon && this.getActivePower() == PowersTusk.CHARGE_NAILS) {
            this.setPowerNone();
            this.nailCharge = 0;
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
        powersTusk.setAct(ConfigManager.getClientConfig().dynamicSettings.TuskAct,false);
        PowersTusk.buildSkins();
        return powersTusk;
    }

    public static void buildSkins() {
        for(int i=0;i<4;i++) {
            MANGA_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/tusk/act_"+(i+1)+ "/manga.png");
            BLUE_SKIN[i] = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/tusk/act_"+(i+1)+ "/blue.png");
        }
    }
    public static ResourceLocation getSkin(byte skin, int act) {
        if (MANGA_SKIN[0] == null) {buildSkins();}
        ResourceLocation[] skins = switch (skin) {
            case MANGA -> MANGA_SKIN;
            case BLUE -> BLUE_SKIN;
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


    public static final byte
            MANGA = 1,
            BLUE = 2;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                BLUE
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case BLUE -> Component.translatable("skins.roundabout.tusk.blue");
            default -> Component.translatable("skins.roundabout.tusk.manga");
        };
    }


    @Override
    public Vector3f getLeapColor() {
        return new Vector3f(127/255F,194/255F,249/255F);
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