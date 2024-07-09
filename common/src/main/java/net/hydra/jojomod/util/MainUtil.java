package net.hydra.jojomod.util;


import com.google.common.collect.Sets;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
            }

            List<Entity> entities = MainUtil.hitboxGas(MainUtil.genHitbox(level, blkPos.getX(), blkPos.getY(),
                    blkPos.getZ(), hitRadius, hitRadius+1, hitRadius));
            if (!entities.isEmpty()) {
                for (Entity value : entities) {
                    if (value instanceof GasolineCanEntity){
                        value.remove(Entity.RemovalReason.DISCARDED);
                        gasExplode(null, level, value.getOnPos(), iteration + 1, 1, blockRadius, 16);
                        break;
                    }
                    if (!value.fireImmune()) {
                        value.setSecondsOnFire(15);
                        value.hurt(level.damageSources().onFire(),power);
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
                    gasExplode(state, level, gasPuddle, iteration + 1, hitRadius, blockRadius, 16);
                }
            }
        }


    }

    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/
    public static void handleBytePacketC2S(Player player, byte data, byte context){
        if (context == PacketDataIndex.PLAY_SOUND_C2S_CONTEXT) {
            ((StandUser) player).getStandPowers().playSoundsIfNearby(data, 100, true);
        }
    }
}
