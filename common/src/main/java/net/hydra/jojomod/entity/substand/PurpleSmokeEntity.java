package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.RoundaboutLoadServer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.PlayerEntity;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.hydra.jojomod.mixin.justice.JusticeCreeper;
import net.hydra.jojomod.mixin.justice.JusticeZombie;
import net.hydra.jojomod.stand.powers.PowersPurpleHaze;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PurpleSmokeEntity extends StandEntity {
    public float range = ClientNetworking.getAppropriateConfig().greenDaySettings.moldDefaultRange;
    public int lifetime = 600;
    public int lifetime_add = 150;
    public int totalDuration = 0;

    public PurpleSmokeEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public void tick() {


        this.setFadeOut((byte) 1);
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        StandUser StandUU = (StandUser) user;
        if (StandUU != null) {
            if (!(StandUU.roundabout$getStandPowers() instanceof PowersPurpleHaze)) {
                this.discard();
            } else if (((PowersPurpleHaze) StandUU.roundabout$getStandPowers()).purpleHazeFieldGone) {
                this.discard();
            }
        }
        lifetime--;
        if (lifetime < 1) {
            this.discard();

        }

        if (user == null) {
            this.discard();
        } else {
            if (user.isUsingItem() && user.getMainHandItem().getItem().getFoodProperties() != null) {
                if (user.isUsingItem() && user.getMainHandItem().getItem().getFoodProperties().getNutrition() > 0) {
                    this.discard();
                }
            }
            if (!getUser().isAlive()) {
                this.discard();
            }
            ;
        }
        if (this.getDeltaMovement().y > 0.2) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -00.06, 0));
        } else {
            this.setDeltaMovement(0, -0.2, 0);
        }
        if (!client) {
            int elapsedTicks = totalDuration - lifetime;
            float expansionProgress = Math.min(1.0F, (float) elapsedTicks / 60);
            range = 1.0F + (8.0F - 1.0F) * expansionProgress;
        }
        List<Entity> damages = MainUtil.genHitbox(
                this.level(),
                this.getX(),
                this.getY(),
                this.getZ(),
                range,
                range,
                range
        );

        /*if (client) {
            Roundabout.LOGGER.info(
                    "[PURPLE DEBUG] CLIENT smoke tick | range=" + range
                            + " | entities found=" + damages.size()
                            + " | user=" + this.getUser()
            );
        }*/

        for (int j = 0; j < damages.size(); j++) {
            if (Objects.nonNull(this.getUser())) {
                Entity entity = damages.get(j);
                if (entity instanceof LivingEntity) {
                    if (isDistortionMode()) {
                        ((StandUser) entity).SetInDistortionHazeTicks(5);
                    } else {
                        ((StandUser) entity).SetInPurpleHazeTicks(5);
                    }
                }
            }
        }
        if (!client) {
            tickeffect();
            tickBlockDecay();

            byte skin = 0;
            if (user != null && StandUU.roundabout$getStandPowers() instanceof PowersPurpleHaze ph) {
                skin = ph.getStandSkin();
            }
            spawnFieldParticles(skin);

            S2CPacketUtil.sync_mold_duration(lifetime, this.getId());
            S2CPacketUtil.sync_mold_range(range, this.getId());
        }

        /*if(client){
            Roundabout.LOGGER.info("ClientRange= " + range);
        }else{
            Roundabout.LOGGER.info("ServerRange= " + range);
        }*/
        super.tick();
    }
    private void tickBlockDecay() {
        if (!(this.level() instanceof ServerLevel sl)) return;

        int r = Mth.ceil(range);
        BlockPos center = this.blockPosition();

        int attempts = Mth.clamp((int) (range * 4F), 15, 120);

        for (int i = 0; i < attempts; i++) {
            int dx = Roundabout.RANDOM.nextInt(r * 2 + 1) - r;
            int dy = Roundabout.RANDOM.nextInt(r * 2 + 1) - r;
            int dz = Roundabout.RANDOM.nextInt(r * 2 + 1) - r;

            BlockPos pos = center.offset(dx, dy, dz);

            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > range) continue;

            BlockState state = sl.getBlockState(pos);
            BlockState decayed = getDecayedState(state);

            if (decayed != null) {
                sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        20, 0.3, 0.3, 0.3, 0.08);

                sl.setBlockAndUpdate(pos, decayed);
            }
        }
    }

    @Nullable
    private BlockState getDecayedState(BlockState state) {
        Block block = state.getBlock();

        if (block == Blocks.GRASS_BLOCK || block == Blocks.MYCELIUM || block == Blocks.PODZOL) {
            if (Roundabout.RANDOM.nextFloat() < 0.45F) {
                return Blocks.DIRT.defaultBlockState();
            }
            return null;
        }

        if (block instanceof FlowerBlock
                || block instanceof TallGrassBlock
                || block instanceof DoublePlantBlock
                || block instanceof SaplingBlock
                || block instanceof MushroomBlock
                || block == Blocks.FERN
                || block == Blocks.LARGE_FERN
                || block == Blocks.DEAD_BUSH) {
            if (Roundabout.RANDOM.nextFloat() < 0.6F) {
                return Blocks.AIR.defaultBlockState();
            }
            return null;
        }

        if (block instanceof LeavesBlock) {
            if (Roundabout.RANDOM.nextFloat() < 0.3F) {
                return Blocks.AIR.defaultBlockState();
            }
            return null;
        }

        if (block == Blocks.FARMLAND) {
            if (Roundabout.RANDOM.nextFloat() < 0.45F) {
                return Blocks.DIRT.defaultBlockState();
            }
            return null;
        }

        return null;
    }
    private void spawnFieldParticles(byte skin) {
        if (!(this.level() instanceof ServerLevel sl)) return;
        double x = this.getX();
        double y = this.getY() + 1.0;
        double z = this.getZ();

        switch (skin) {
            case PurpleHazeEntity.BLAZING_HAZE -> {
                if (isDistortionMode()) {
                    sl.sendParticles(ModParticles.DISTORTION_SMOKE, x, y, z, 30, range / 2, 1.5, range / 2, 0.01);
                } else {
                    sl.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 30, range / 2, 1.5, range / 2, 0.01);
                }
            }
            case PurpleHazeEntity.GREEN -> {
                sl.sendParticles(ParticleTypes.SNEEZE, x, y, z, 30, range / 2, 1.5, range / 2, 0.01);
                if (isDistortionMode()) {
                    sl.sendParticles(new DustParticleOptions(new Vector3f(0.0F, 0.0F, 0.0F), 1.5F),
                            x, y, z, 45, range / 2, 1.5, range / 2, 0.02);
                }
            }
            case PurpleHazeEntity.NETHERITE -> {
                sl.sendParticles(ParticleTypes.SMOKE, x, y, z, 30, range / 2, 1.5, range / 2, 0.01);
                if (isDistortionMode()) {
                    sl.sendParticles(new DustParticleOptions(new Vector3f(0.0F, 0.0F, 0.0F), 1.5F),
                            x, y, z, 45, range / 2, 1.5, range / 2, 0.02);
                }
            }
            default -> {
                if (isDistortionMode()) {
                    sl.sendParticles(ModParticles.DISTORTION_SMOKE, x, y, z, 30, range / 2, 1.5, range / 2, 0.01);
                } else {
                    sl.sendParticles(ModParticles.PURPLE_HAZE_SMOKE, x, y, z, 30, range / 2, 1.5, range / 2, 0.01);
                }
            }
        }
    }

    public void tickeffect() {
        List<Entity> damages = MainUtil.genHitbox(this.level(), this.getX(), this.getY(), this.getZ(), range, range, range);
        LivingEntity user = this.getUser();
        if (user == null) return;

        int elapsedTicks = totalDuration - lifetime;
        if (elapsedTicks < 40) return;

        for (Entity entity : damages) {
            if (!(entity instanceof LivingEntity living)) continue;

            int effectDuration = living instanceof Player ? 200 : 300;
            if (isDistortionMode()) {
                boolean already = living.hasEffect(ModEffects.DISTORTION_VIRUS);
                living.addEffect(new MobEffectInstance(ModEffects.DISTORTION_VIRUS, effectDuration));
                ((StandUser) living).SetInDistortionHazeTicks(5);
                if (living != user && !already) {
                    ((StandUser) user).roundabout$getStandPowers().addEXP(2);
                }
            } else {
                boolean already = living.hasEffect(ModEffects.HAZE_VIRUS);
                ((StandUser) living).SetInPurpleHazeTicks(5);
                living.addEffect(new MobEffectInstance(ModEffects.HAZE_VIRUS, 300));
                if (living != user && !already) {
                    ((StandUser) user).roundabout$getStandPowers().addEXP(3);
                }
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource $$0) {
        return true;
    }


    @Override
    public boolean fireImmune() {
        return true;
    }


    /**
     * USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.
     */
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SeperatedLegsEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> DISTORTION_MODE =
            SynchedEntityData.defineId(PurpleSmokeEntity.class, EntityDataSerializers.BOOLEAN);
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DISTORTION_MODE, false);
    }
    public boolean isDistortionMode() {
        return this.entityData.get(DISTORTION_MODE);
    }

    public void setDistortionMode(boolean value) {
        this.entityData.set(DISTORTION_MODE, value);
    }
    @Override
    public boolean hasNoPhysics() {
        return false;
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.GENERIC_KILL) || source.is(DamageTypes.FELL_OUT_OF_WORLD)){
            discard();
            return false;
        }
        return false;
    }


    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return false;
    }

    @Override
    protected boolean isHorizontalCollisionMinor(Vec3 $$0) {
        return super.isHorizontalCollisionMinor($$0);
    }
}
