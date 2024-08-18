package net.hydra.jojomod.util;


import com.google.common.collect.Sets;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainUtil {
    /**Additional math functions for the mod.*/

    /** This version of interpolation accommodates speed multipliers so you can control how
     * fast something moves from point A to point B.
     * Ex: the speed a stand tilts in*/
    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * (end - start))*multiplier;
    }

    public static float controlledLerpAngleDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * Mth.wrapDegrees(end - start))*multiplier;
    }public static float controlledLerpRadianDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * wrapRadians(end - start))*multiplier;
    }


    public static List<Entity> genHitbox(Level level, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ) {
        double k = Mth.floor(startX - radiusX);
        double l = Mth.floor(startX + radiusX);
        double r = (startY - radiusY);
        double s = (startY + radiusY);
        double t = (startZ - radiusZ);
        double u = (startZ + radiusZ);
        return level.getEntities(null, new AABB(k, r, t, l, s, u));
    }

    public static List<net.minecraft.world.entity.Entity> hitbox(List<net.minecraft.world.entity.Entity> entities){

        List<net.minecraft.world.entity.Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.showVehicleHealth() || value.isInvulnerable() || !value.isAlive()){
                hitEntities.remove(value);
            }
        }
        return hitEntities;
    }
    public static List<net.minecraft.world.entity.Entity> hitboxGas(List<net.minecraft.world.entity.Entity> entities){

        List<net.minecraft.world.entity.Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if ((!value.showVehicleHealth() && !(value instanceof GasolineCanEntity)) || value.isInvulnerable() || !value.isAlive()){
                hitEntities.remove(value);
            }
        }
        return hitEntities;
    }

    public static float wrapRadians(float radians) {
        radians *= Mth.RAD_TO_DEG;
        float f = radians % 360.0f;
        if (f >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return (f*Mth.DEG_TO_RAD);
    }

    public static Vec3 getMoveRelative(float $$0, Vec3 $$1, Entity ett) {
        return getInputVector($$1, $$0, ett.getYRot());
    }

    private static Vec3 getInputVector(Vec3 $$0, float $$1, float $$2) {
        double $$3 = $$0.lengthSqr();
        if ($$3 < 1.0E-7) {
            return Vec3.ZERO;
        } else {
            Vec3 $$4 = ($$3 > 1.0 ? $$0.normalize() : $$0).scale((double)$$1);
            float $$5 = Mth.sin($$2 * (float) (Math.PI / 180.0));
            float $$6 = Mth.cos($$2 * (float) (Math.PI / 180.0));
            return new Vec3($$4.x * (double)$$6 - $$4.z * (double)$$5, $$4.y, $$4.z * (double)$$6 + $$4.x * (double)$$5);
        }
    }

    public static double fixAngle(float angle){
       return (Math.abs(angle) % 360);
    }

    public static void takeUnresistableKnockbackWithY(Entity entity, double strength, double x, double y, double z) {
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        entity.setDeltaMovement(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.hasImpulse = true;
    }
    public static void takeUnresistableKnockbackWithY2(Entity entity,double x, double y, double z) {
        entity.hurtMarked = true;
        entity.setDeltaMovement( x,
                y,
                z);
        entity.hasImpulse = true;
    }

    public static double lengthdir_x(double length, double angle) {
        return length * (Math.cos(toRadians(angle))) * -1;
    }

    public static double lengthdir_z(double length, double angle) {
        return length * (Math.sin(toRadians(angle)));
    }

    public static double toRadians(double angle) {
        return angle * (Math.PI / 180);
    }

    public static double cheapDistanceTo(double x,double y,double z,double x2,double y2,double z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(y-y2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }
    public static double cheapDistanceTo2(double x,double z,double x2,double z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }
    public static int cheapDistanceTo2(int x,int z,int x2,int z2){
        int mdist = 0;
        int cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }

    public static boolean isPlayerNearby(Vec3 pos, Level level, double range, int exemptID) {
        if (level instanceof ServerLevel) {
            ServerLevel serverWorld = ((ServerLevel) level);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = serverWorld.players().get(j);

                if (serverPlayerEntity.level() != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (serverPlayerEntity.getId() != exemptID && blockPos.closerToCenterThan(pos, range)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void gasExplode(BlockState blk, ServerLevel level, BlockPos blkPos, int iteration, int hitRadius, int blockRadius, float power){
        if (!level.isClientSide){
            level.sendParticles(ParticleTypes.LAVA, blkPos.getX() + 0.5, blkPos.getY(), blkPos.getZ() + 0.5,
                    2, 0.0, 0.0, 0.0, 0.4);
            if (iteration == 0){
                SoundEvent $$6 = ModSounds.GASOLINE_EXPLOSION_EVENT;
                level.playSound(null, blkPos, $$6, SoundSource.BLOCKS, 10F, 1F);
                level.sendParticles(ParticleTypes.EXPLOSION, blkPos.getX()+0.5F, blkPos.getY()+0.5F, blkPos.getZ()+0.5F,
                        1, 0.1, 0.1, 0.1, 0.2);
            }

            List<Entity> entities = MainUtil.hitboxGas(MainUtil.genHitbox(level, blkPos.getX(), blkPos.getY(),
                    blkPos.getZ(), hitRadius, hitRadius+1, hitRadius));
            if (!entities.isEmpty()) {
                DamageSource $$5 = ModDamageTypes.of(level,ModDamageTypes.GASOLINE_EXPLOSION);
                for (Entity value : entities) {
                    if (value instanceof GasolineCanEntity){
                        value.remove(Entity.RemovalReason.DISCARDED);
                        gasExplode(null, level, value.getOnPos(), iteration + 1, 1, blockRadius, power);
                        break;
                    }
                    if (!value.fireImmune()) {
                        value.setSecondsOnFire(15);
                        float np = power;
                        if (value instanceof LivingEntity){
                            ((StandUser)value).roundabout$setGasolineTime(-1);
                            if (value instanceof Player){
                                np *= 0.31F;
                            }
                            int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, (LivingEntity) value);
                            np = (float) (np * (1-(f*0.045)));
                        }
                        if (value instanceof LivingEntity && ((LivingEntity)value).hasEffect(MobEffects.FIRE_RESISTANCE)){
                            MobEffectInstance instance = ((LivingEntity)value).getEffect(MobEffects.FIRE_RESISTANCE);
                            ((LivingEntity)value).removeEffect(MobEffects.FIRE_RESISTANCE);
                            value.hurt($$5,np);
                            ((LivingEntity)value).addEffect(instance);
                        } else {
                            value.hurt($$5,np);
                        }
                    }
                }
            }
        }

        if (blk != null) {
            level.removeBlock(blkPos, false);
        }

        Set<BlockPos> gasList = Sets.newHashSet();
        if (!level.isClientSide && iteration < 8) {
            for (int x = -blockRadius; x < blockRadius; x++) {
                for (int y = -blockRadius; y < blockRadius; y++) {
                    for (int z = -blockRadius; z < blockRadius; z++) {
                        BlockPos blkPo2 = new BlockPos(blkPos.getX() + x, blkPos.getY()+y, blkPos.getZ() + z);
                        BlockState state = level.getBlockState(blkPo2);
                        Block block = state.getBlock();

                        if (block instanceof GasolineBlock) {
                            boolean ignited = state.getValue(GasolineBlock.IGNITED);
                            if (!ignited){
                                state = state.setValue(GasolineBlock.IGNITED, Boolean.valueOf(true));
                                level.setBlock(blkPo2, state, 1);
                                gasList.add(blkPo2);
                            }
                        }
                    }
                }
            }
            if (!gasList.isEmpty()) {
                for (BlockPos gasPuddle : gasList) {
                    BlockState state = level.getBlockState(gasPuddle);
                    gasExplode(state, level, gasPuddle, iteration + 1, hitRadius, blockRadius, power);
                }
            }
        }


    }


    /**For new Locacaca mechanics and rendering*/
    public static LivingEntity getStoneTarget(Level $$0, LivingEntity $$1){
        List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox($$0, $$1.getX(), $$1.getY(),
                $$1.getZ(), 5, 5, 5));
        double maxDistance = 5;
        LivingEntity target = null;
        if (!entities.isEmpty()) {
            for (Entity value : entities) {
                if (value instanceof LivingEntity && value.getUUID() != $$1.getUUID() && !(value instanceof StandEntity)) {
                    double distance = value.position().distanceTo($$1.position());
                    if (distance <= maxDistance && ((StandUser)value).roundabout$getLocacacaCurse() < 0){
                        target = (LivingEntity) value;
                        maxDistance = distance;
                    }
                }
            }
        }

        return target;
    }

    /**A generalized packet for sending ints to the client. Context is what to do with the data int*/
    public static void handleIntPacketS2C(LocalPlayer player, int data, byte context){
        if (context == 1) {
            ((StandUser) player).roundabout$setGasolineTime(data);
        }
    }

    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/
    public static void handleBytePacketC2S(Player player, byte data, byte context){
        if (context == PacketDataIndex.PLAY_SOUND_C2S_CONTEXT) {
            ((StandUser) player).getStandPowers().playSoundsIfNearby(data, 100, true);
        }
    }

    /**A generalized packet for sending floats to the server. Context is what to do with the data byte*/
    public static void handleFloatPacketC2S(Player player, float data, byte context){
        if (context == PacketDataIndex.FLOAT_VELOCITY_BARBED_WIRE) {
            if (player.getVehicle() != null){
                player.getVehicle().hurt(ModDamageTypes.of(player.level(), ModDamageTypes.BARBED_WIRE), data);
            } else {
                player.hurt(ModDamageTypes.of(player.level(), ModDamageTypes.BARBED_WIRE), data);
            }
        }
    }
}
