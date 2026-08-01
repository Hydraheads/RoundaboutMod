package net.hydra.jojomod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddExperienceOrbPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class FakeExperienceOrb  extends Entity {
    private static final int LIFETIME = 6000;
    private static final int ENTITY_SCAN_PERIOD = 20;
    private static final int MAX_FOLLOW_DIST = 8;
    private static final int ORB_GROUPS_PER_AREA = 40;
    private static final double ORB_MERGE_DISTANCE = 0.5;
    private int age;
    private int health = 5;
    private int value;
    private int count = 1;
    private Player followingPlayer;

    public FakeExperienceOrb(Level $$0, double $$1, double $$2, double $$3, int $$4) {
        this(ModEntities.FAKE_EXP, $$0);
        this.setPos($$1, $$2, $$3);
        this.setYRot((float)(this.random.nextDouble() * 360.0));
        this.setDeltaMovement(
                (this.random.nextDouble() * 0.2F - 0.1F) * 2.0, this.random.nextDouble() * 0.2 * 2.0, (this.random.nextDouble() * 0.2F - 0.1F) * 2.0
        );
        this.value = $$4;
    }

    public FakeExperienceOrb(EntityType<? extends FakeExperienceOrb> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
        }

        if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement(
                    (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F),
                    0.2F,
                    (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F)
            );
        }

        if (!this.level().noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
        }

        if (this.tickCount % 20 == 1) {
            this.scanForEntities();
        }

        if (this.followingPlayer != null && (this.followingPlayer.isSpectator() || this.followingPlayer.isDeadOrDying())) {
            this.followingPlayer = null;
        }

        if (this.followingPlayer != null) {
            Vec3 $$0 = new Vec3(
                    this.followingPlayer.getX() - this.getX(),
                    this.followingPlayer.getY() + (double)this.followingPlayer.getEyeHeight() / 2.0 - this.getY(),
                    this.followingPlayer.getZ() - this.getZ()
            );
            double $$1 = $$0.lengthSqr();
            if ($$1 < 64.0) {
                double $$2 = 1.0 - Math.sqrt($$1) / 8.0;
                this.setDeltaMovement(this.getDeltaMovement().add($$0.normalize().scale($$2 * $$2 * 0.1)));
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float $$3 = 0.98F;
        if (this.onGround()) {
            $$3 = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double)$$3, 0.98, (double)$$3));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
        }

        this.age++;
        if (this.age >= 6000) {
            this.discard();
        }
    }

    @Override
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    private void scanForEntities() {
        if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 64.0) {
            this.followingPlayer = this.level().getNearestPlayer(this, 8.0);
        }

        if (this.level() instanceof ServerLevel) {
            for (FakeExperienceOrb $$1 : this.level().getEntities(EntityTypeTest.forClass(FakeExperienceOrb.class), this.getBoundingBox().inflate(0.5), this::canMerge)) {
                this.merge($$1);
            }
        }
    }

    public static void award(ServerLevel $$0, Vec3 $$1, int $$2) {
        while ($$2 > 0) {
            int $$3 = getExperienceValue($$2);
            $$2 -= $$3;
            if (!tryMergeToExisting($$0, $$1, $$3)) {
                $$0.addFreshEntity(new FakeExperienceOrb($$0, $$1.x(), $$1.y(), $$1.z(), $$3));
            }
        }
    }

    private static boolean tryMergeToExisting(ServerLevel $$0, Vec3 $$1, int $$2) {
        AABB $$3 = AABB.ofSize($$1, 1.0, 1.0, 1.0);
        int $$4 = $$0.getRandom().nextInt(40);
        List<FakeExperienceOrb> $$5 = $$0.getEntities(EntityTypeTest.forClass(FakeExperienceOrb.class), $$3, $$2x -> canMerge($$2x, $$4, $$2));
        if (!$$5.isEmpty()) {
            FakeExperienceOrb $$6 = $$5.get(0);
            $$6.count++;
            $$6.age = 0;
            return true;
        } else {
            return false;
        }
    }

    private boolean canMerge(FakeExperienceOrb $$0x) {
        return $$0x != this && canMerge($$0x, this.getId(), this.value);
    }

    private static boolean canMerge(FakeExperienceOrb $$0, int $$1, int $$2) {
        return !$$0.isRemoved() && ($$0.getId() - $$1) % 40 == 0 && $$0.value == $$2;
    }

    private void merge(FakeExperienceOrb $$0) {
        this.count = this.count + $$0.count;
        this.age = Math.min(this.age, $$0.age);
        $$0.discard();
    }

    private void setUnderwaterMovement() {
        Vec3 $$0 = this.getDeltaMovement();
        this.setDeltaMovement($$0.x * 0.99F, Math.min($$0.y + 5.0E-4F, 0.06F), $$0.z * 0.99F);
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else if (this.level().isClientSide) {
            return true;
        } else {
            this.markHurt();
            this.health = (int)((float)this.health - $$1);
            if (this.health <= 0) {
                this.discard();
            }

            return true;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putShort("Health", (short)this.health);
        $$0.putShort("Age", (short)this.age);
        $$0.putShort("Value", (short)this.value);
        $$0.putInt("Count", this.count);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        this.health = $$0.getShort("Health");
        this.age = $$0.getShort("Age");
        this.value = $$0.getShort("Value");
        this.count = Math.max($$0.getInt("Count"), 1);
    }

    @Override
    public void playerTouch(Player $$0) {
        if (!this.level().isClientSide) {
            if ($$0.takeXpDelay == 0) {
                $$0.takeXpDelay = 2;
                $$0.take(this, 1);
                int $$1 = this.repairPlayerItems($$0, this.value);
                if ($$1 > 0) {
                    $$0.giveExperiencePoints($$1);
                }

                this.count--;
                if (this.count == 0) {
                    this.discard();
                }
            }
        }
    }

    private int repairPlayerItems(Player $$0, int $$1) {
        Map.Entry<EquipmentSlot, ItemStack> $$2 = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, $$0, ItemStack::isDamaged);
        if ($$2 != null) {
            ItemStack $$3 = $$2.getValue();
            int $$4 = Math.min(this.xpToDurability(this.value), $$3.getDamageValue());
            $$3.setDamageValue($$3.getDamageValue() - $$4);
            int $$5 = $$1 - this.durabilityToXp($$4);
            return $$5 > 0 ? this.repairPlayerItems($$0, $$5) : 0;
        } else {
            return $$1;
        }
    }

    private int durabilityToXp(int $$0) {
        return $$0 / 2;
    }

    private int xpToDurability(int $$0) {
        return $$0 * 2;
    }

    public int getValue() {
        return this.value;
    }

    public int getIcon() {
        if (this.value >= 2477) {
            return 10;
        } else if (this.value >= 1237) {
            return 9;
        } else if (this.value >= 617) {
            return 8;
        } else if (this.value >= 307) {
            return 7;
        } else if (this.value >= 149) {
            return 6;
        } else if (this.value >= 73) {
            return 5;
        } else if (this.value >= 37) {
            return 4;
        } else if (this.value >= 17) {
            return 3;
        } else if (this.value >= 7) {
            return 2;
        } else {
            return this.value >= 3 ? 1 : 0;
        }
    }

    public static int getExperienceValue(int $$0) {
        if ($$0 >= 2477) {
            return 2477;
        } else if ($$0 >= 1237) {
            return 1237;
        } else if ($$0 >= 617) {
            return 617;
        } else if ($$0 >= 307) {
            return 307;
        } else if ($$0 >= 149) {
            return 149;
        } else if ($$0 >= 73) {
            return 73;
        } else if ($$0 >= 37) {
            return 37;
        } else if ($$0 >= 17) {
            return 17;
        } else if ($$0 >= 7) {
            return 7;
        } else {
            return $$0 >= 3 ? 3 : 1;
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }


    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }
}

