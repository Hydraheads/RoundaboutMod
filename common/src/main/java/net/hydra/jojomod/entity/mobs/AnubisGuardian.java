package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AnubisGuardian extends PathfinderMob implements CrossbowAttackMob {

    private static final EntityDataAccessor<Integer> SUMMONER_ID = SynchedEntityData.defineId(AnubisGuardian.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> CHOSEN_ACTION = SynchedEntityData.defineId(AnubisGuardian.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> CROSSBOW_STATE = SynchedEntityData.defineId(AnubisGuardian.class, EntityDataSerializers.BYTE);


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONER_ID, -1);
        this.entityData.define(CHOSEN_ACTION, (byte)0);
        this.entityData.define(CROSSBOW_STATE, (byte)0);
    }

    public void setAction(AnubisAttacks AA) {this.entityData.set(CHOSEN_ACTION,AA.id);
 //       Roundabout.LOGGER.info(""+AA);
    }
    public AnubisAttacks getAction() {return AnubisAttacks.getFromByte(this.entityData.get(CHOSEN_ACTION));}

    public void setSummoner(LivingEntity LE) {this.entityData.set(SUMMONER_ID,LE.getId());}
    public LivingEntity getSummoner(Level l) {return (LivingEntity) l.getEntity(this.entityData.get(SUMMONER_ID));}

    public void setCrossbow(byte i) {this.entityData.set(CROSSBOW_STATE,i);}
    public boolean isCrossbowCharged() {return this.entityData.get(CROSSBOW_STATE) ==CrossbowState.CHARGED.id;}





    public AnubisGuardian(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
                //  StandArrowItem.grantStand(ModItems.MAX_STAND_DISC_ANUBIS.getDefaultInstance(),this);
        this.setMaxUpStep(1.5F);
    }
    public static AttributeSupplier.Builder createAttributes() {return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MOVEMENT_SPEED,1.0);}


    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ChooseAttack(this));

        this.goalSelector.addGoal(3, new ChargeCrossbow(this));
        this.goalSelector.addGoal(3, new FireCrossbow(this));
        // rush with axe
        // rush with sword, faster attack time, pots if they're low
        // bow shot, does it mainly to targets above them
        // fire res if in lava

        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 0.2));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));


        this.targetSelector.addGoal(1, new PrioritizeSummoner(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        super.registerGoals();
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        return switch (this.getAction()) {
            case CROSSBOW_FIRE ->  AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
            case CROSSBOW_CHARGE -> AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
            case RUSH -> AbstractIllager.IllagerArmPose.ATTACKING;
            default -> AbstractIllager.IllagerArmPose.CROSSED;
        };
    }



    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.getSummoner(this.level()) == null && $$0.getEntity() != null && $$0.getEntity() instanceof LivingEntity LE) {
            this.setSummoner(LE);
        }
        return super.hurt($$0, $$1);
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {this.performCrossbowAttack(livingEntity,v);}

    @Override
    public void setChargingCrossbow(boolean b) {
        if (b) {
            this.setCrossbow(CrossbowState.CHARGED.id);
        } else {
            this.setCrossbow(CrossbowState.UNCHARGED.id);
        }
    }
    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack itemStack, Projectile projectile, float v) {this.shootCrossbowProjectile(this, livingEntity, projectile, v, 1.6f);}
    @Override
    public void onCrossbowAttackPerformed() {this.noActionTime = 0;}



    /// Crossbow Enum
public enum CrossbowState {
        UNCHARGED((byte)0),
        CHARGING((byte)1),
        CHARGED((byte)2);

        public final byte id;
        CrossbowState(byte $$0) {
        this.id = $$0;
    }
}

    /// AnubisAttack Enum
public enum AnubisAttacks {
    NONE((byte) 0),
    RUSH((byte) 1),
    CROSSBOW_CHARGE((byte) 2),
    CROSSBOW_FIRE((byte) 3);

    public final byte id;
    AnubisAttacks(byte $$0) {
        this.id = $$0;
    }

    public static AnubisAttacks getFromByte(byte b) {
        if (b == NONE.id) {
            return NONE;
        } else if (b == RUSH.id) {
            return RUSH;
        } else if (b == CROSSBOW_CHARGE.id) {
            return CROSSBOW_CHARGE;
        } else if (b == CROSSBOW_FIRE.id) {
            return CROSSBOW_FIRE;
        }
        return NONE;
    }
    public static AnubisAttacks getRandom(Mob m) {
        return getFromByte((byte)m.getRandom().nextInt(0,4));
    }
}

/// targets and goals
static class ChooseAttack extends Goal {
    protected final AnubisGuardian mob;

    public ChooseAttack(AnubisGuardian mob) {this.mob = mob;}
    @Override
    public void start() {
        super.start();
        AnubisAttacks attack;
        while (true) {
            attack = AnubisAttacks.getRandom(this.mob);
            if (attack.equals(AnubisAttacks.RUSH)) {
                break;
            } else if (attack.equals(AnubisAttacks.CROSSBOW_CHARGE)) {
                if (!this.mob.isCrossbowCharged()) {
                    break;
                }
            } else if (attack.equals(AnubisAttacks.CROSSBOW_FIRE)) {
                if (this.mob.isCrossbowCharged()) {
                    break;
                }
            }
        }
        this.mob.setAction(attack);
        stop();

    }
    @Override
    public boolean canUse() {return this.mob.getAction().equals(AnubisAttacks.NONE) && this.mob.getTarget() != null;}
}
static class ChargeCrossbow extends Goal {
    protected final AnubisGuardian mob;
    public ChargeCrossbow(AnubisGuardian mob) {
        this.mob = mob;
    }

    @Override
    public void start() {
        super.start();
        this.mob.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));
        this.mob.startUsingItem(InteractionHand.MAIN_HAND);
        this.mob.setCrossbow(CrossbowState.CHARGING.id);
    }

    @Override
    public void tick() {
        super.tick();
        int dur = CrossbowItem.getChargeDuration(Items.CROSSBOW.getDefaultInstance());
        if (this.mob.getTicksUsingItem() > dur+10) {
            this.mob.setCrossbow(CrossbowState.CHARGED.id);
            this.mob.setAction(AnubisAttacks.NONE);
            this.stop();
        } else if (this.mob.getTicksUsingItem() > dur) {
            CrossbowItem.setCharged(this.mob.getItemBySlot(EquipmentSlot.MAINHAND), true);
        }
    }

    @Override
    public void stop() {
        this.mob.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.AIR));
        super.stop();
    }

    @Override
    public boolean canUse() {
        return !this.mob.isCrossbowCharged() && this.mob.getAction() == AnubisAttacks.CROSSBOW_CHARGE;
    }
}

static class FireCrossbow extends Goal {
    protected final AnubisGuardian mob;
    public FireCrossbow(AnubisGuardian mob) {this.mob = mob;}

    @Override
    public void start() {
        super.start();
        this.mob.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));
        CrossbowItem.setCharged(this.mob.getItemBySlot(EquipmentSlot.MAINHAND),true);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mob.getTarget() != null) {
            this.mob.performRangedAttack(this.mob.getTarget(), 1.0f);
            ItemStack itemStack2 = this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.CROSSBOW));
            CrossbowItem.setCharged(itemStack2, false);

            this.mob.setCrossbow(CrossbowState.UNCHARGED.id);
            this.mob.setAction(AnubisAttacks.NONE);
            this.stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.AIR));
    }

    @Override
    public boolean canUse() {
        return this.mob.isCrossbowCharged();
    }
}


static class PrioritizeSummoner extends TargetGoal {
    public PrioritizeSummoner(PathfinderMob $$0) {
        super($$0,false,false);
    }
    @Override
    public void start() {
        super.start();
        this.mob.setTarget(((AnubisGuardian)this.mob).getSummoner(this.mob.level()));
    }
    @Override
    public boolean canUse() {
        return ((AnubisGuardian)this.mob).getSummoner(this.mob.level()) != null;
    }
}


}


