package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class AnubisGuardian extends Raider implements RangedAttackMob {


    @Override
    protected void dropCustomDeathLoot(DamageSource $$0, int $$1, boolean $$2) {
        this.spawnAtLocation(ModItems.ANUBIS_ITEM.getDefaultInstance());
        super.dropCustomDeathLoot($$0, $$1, $$2);
    }

    private static final EntityDataAccessor<Integer> SUMMONER_ID = SynchedEntityData.defineId(AnubisGuardian.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> CHOSEN_ACTION = SynchedEntityData.defineId(AnubisGuardian.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> HAS_POPPED = SynchedEntityData.defineId(AnubisGuardian.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONER_ID, -1);
        this.entityData.define(CHOSEN_ACTION, (byte)0);
        this.entityData.define(HAS_POPPED, false);
    }

    public void setAction(AnubisAttacks AA) {
        this.entityData.set(CHOSEN_ACTION,AA.id);

        switch (this.getAction()) {
            case NONE -> this.setItemInHand(InteractionHand.MAIN_HAND,Items.AIR.getDefaultInstance());
            case AXE_RUSH -> this.setItemInHand(InteractionHand.MAIN_HAND,Items.IRON_AXE.getDefaultInstance());
            case SWORD_RUSH -> {
                ItemStack a = new ItemStack(Items.IRON_SWORD);
                a.enchant(Enchantments.KNOCKBACK,2);
                this.setItemInHand(InteractionHand.MAIN_HAND,a);
            }
            case BOW_ATTACK -> this.setItemInHand(InteractionHand.MAIN_HAND,Items.BOW.getDefaultInstance());
        }
    }
    public AnubisAttacks getAction() {return AnubisAttacks.getFromByte(this.entityData.get(CHOSEN_ACTION));}

    public void setSummoner(LivingEntity LE) {this.entityData.set(SUMMONER_ID,LE.getId());}
    public LivingEntity getSummoner(Level l) {return (LivingEntity) l.getEntity(this.entityData.get(SUMMONER_ID));}

    public void setPopped(boolean b) {this.entityData.set(HAS_POPPED,b);}
    public boolean hasTotem() {return !this.entityData.get(HAS_POPPED);}

    @Override
    public void tick() {

        if (this.getAction().equals(AnubisAttacks.NONE)) {
            this.setItemInHand(InteractionHand.MAIN_HAND,Items.AIR.getDefaultInstance());
        }


        super.tick();
    }

    public AnubisGuardian(EntityType<? extends Raider> $$0, Level $$1) {
        super($$0, $$1);
        this.setMaxUpStep(1.5F);
    }
    public static AttributeSupplier.Builder createAttributes() {return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.ARMOR,6).add(Attributes.ATTACK_DAMAGE, 5.0);}


    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ChooseNormalAttack(this));
        this.goalSelector.addGoal(2, new RageGoal(this));

        this.goalSelector.addGoal(3, new AxeRush(this));
        this.goalSelector.addGoal(3, new SwordRush(this));
        this.goalSelector.addGoal(3, new BowAttack(this));
        // bow shot, does it mainly to targets above them
        // fire res if in lava

        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 0.3F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));


        this.targetSelector.addGoal(1, new PrioritizeSummoner(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        return switch (this.getAction()) {
            case AXE_RUSH,SWORD_RUSH -> AbstractIllager.IllagerArmPose.ATTACKING;
            case BOW_ATTACK -> AbstractIllager.IllagerArmPose.BOW_AND_ARROW;
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
    public SoundEvent getCelebrateSound() {return null;}
    @Override
    public void applyRaidBuffs(int i, boolean b) {}

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        ItemStack itemStack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemStack, f);
        double d = livingEntity.getX() - this.getX();
        double e = livingEntity.getY(0.3333333333333333) - abstractArrow.getY();
        double g = livingEntity.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        abstractArrow.shoot(d, e + h * (double)0.2f, g, 1.6f, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(abstractArrow);
    }


    /// AnubisAttack Enum
public enum AnubisAttacks {
    NONE((byte) 0),
    AXE_RUSH((byte) 1),
    SWORD_RUSH((byte) 2),
    BOW_ATTACK((byte)3);

    public final byte id;
    AnubisAttacks(byte $$0) {
        this.id = $$0;
    }

    public static AnubisAttacks getFromByte(byte b) {
        if (b == NONE.id) {
            return NONE;
        } else if (b == AXE_RUSH.id) {
            return AXE_RUSH;
        } else if (b == SWORD_RUSH.id) {
            return SWORD_RUSH;
        } else if (b == BOW_ATTACK.id) {
            return BOW_ATTACK;
        }
        return NONE;
    }
    public static AnubisAttacks getRandom(Mob m) {
        return getFromByte((byte)m.getRandom().nextInt(0,4));
    }
}

/// targets and goals
static class ChooseNormalAttack extends RangedBowAttackGoal<AnubisGuardian> {
    protected final AnubisGuardian mob;

    public ChooseNormalAttack(AnubisGuardian mob) {
        super(mob,0.2F,30,8);
        this.mob = mob;
        this.time = 0;
    }

    private int time = 0;

    @Override
    public void tick() {
        super.tick();
        time++;
        if (time > 20) {
            this.mob.setAction(AnubisAttacks.getRandom(this.mob));
            stop();
        }
    }

    @Override
    public boolean canUse() {return this.mob.hasTotem() && this.mob.getAction().equals(AnubisAttacks.NONE) && this.mob.getTarget() != null;}
    @Override
    public boolean canContinueToUse() {return super.canContinueToUse() && canUse();}
}

static class AxeRush extends MeleeAttackGoal {
    protected final AnubisGuardian mob;

    private int tick = 100;
    public AxeRush(AnubisGuardian $$0) {
        super($$0, 0.43F, true);
        this.mob = $$0;
    }

    @Override
    public void start() {
        super.start();
        tick = 100;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAction(AnubisAttacks.NONE);
    }

    @Override
    public void tick() {
        tick -= 1;
        if (tick <= 0) {
            stop();
        }
        super.tick();
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.mob.getAction().equals(AnubisAttacks.AXE_RUSH);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.mob.getAction().equals(AnubisAttacks.AXE_RUSH);
    }
}

static class SwordRush extends MeleeAttackGoal {
    protected final AnubisGuardian mob;

    private int tick = 100;
    private int stunTicks = 0;
    public SwordRush(AnubisGuardian $$0) {
        super($$0, 0.45F, true);
        this.mob = $$0;
    }

    @Override
    public void start() {
        super.start();
        tick = 100;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAction(AnubisAttacks.NONE);
    }


    @Override
    protected void checkAndPerformAttack(LivingEntity $$0, double $$1) {
        double $$2 = this.getAttackReachSqr($$0);
        if ($$1 <= $$2 && $$0.hurtTime == 0 && this.getTicksUntilNextAttack() <= 15 && stunTicks == 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget($$0);

            if ($$0.isBlocking()) {
                this.stunTicks = 20;
            }
        }
    }

    @Override
    public void tick() {
        tick -= 1;
        if (tick <= 0) {
            stop();
        }
        if (stunTicks > 0) {
            stunTicks--;
        }
        super.tick();
    }
    @Override
    public boolean canUse() {
        return super.canUse() && this.mob.getAction().equals(AnubisAttacks.SWORD_RUSH);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.mob.getAction().equals(AnubisAttacks.SWORD_RUSH);
    }
}

static class BowAttack extends RangedBowAttackGoal<AnubisGuardian> {

    private final AnubisGuardian mob;
    private int ticks = 100;
    public BowAttack(AnubisGuardian $$0) {
        super($$0, 0.2, 20, 20.0f);
        mob = $$0;
    }

    @Override
    public void start() {
        super.start();
        ticks = 100;
    }

    @Override
    public void tick() {
        super.tick();
        ticks -= 1;
        if (ticks <= 0) {
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAction(AnubisAttacks.NONE);
    }

    @Override
    public boolean canUse() {return super.canUse() && mob.getAction().equals(AnubisAttacks.BOW_ATTACK);}
    @Override
    public boolean canContinueToUse() {return super.canContinueToUse() && mob.getAction().equals(AnubisAttacks.BOW_ATTACK);}
}

static class RageGoal extends MeleeAttackGoal {

    private AnubisGuardian mob;
    public RageGoal(AnubisGuardian $$0) {
        super($$0, 0.35, true);
        this.mob = $$0;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !mob.hasTotem();
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
        return ((AnubisGuardian)this.mob).getSummoner(this.mob.level()) != null
                && ( ((AnubisGuardian)this.mob).getSummoner(this.mob.level()) instanceof Player P && !P.isCreative()) ;
    }
}

}


