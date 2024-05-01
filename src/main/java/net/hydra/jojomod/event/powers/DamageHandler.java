package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

public class DamageHandler {


    /** Returns an offset away from a player's sight*/
    public static Vec3d getRayPoint(LivingEntity entity, double maxDistance){
            MinecraftClient mc = MinecraftClient.getInstance();
            float tickDelta = mc.getLastFrameDuration();
            Vec3d vec3d = entity.getCameraPosVec(tickDelta);
            Vec3d vec3d2 = entity.getRotationVec(tickDelta);
            return vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
    }

    /** Gets an offset one block away of the direction looked. Multiply to use any distance.*/
    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
    /**Generates a hitbox*/
    public static List<Entity> genHitbox(LivingEntity entity, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ) {
        double k = MathHelper.floor(startX - radiusX);
        double l = MathHelper.floor(startX + radiusX);
        double r = (startY - radiusY);
        double s = (startY + radiusY);
        double t = (startZ - radiusZ);
        double u = (startZ + radiusZ);
        return entity.getWorld().getOtherEntities(entity, new Box(k, r, t, l, s, u));
    }

    public static boolean StandDamageEntity(Entity entity, float power, Entity attacker){
        return entity.damage(ModDamageTypes.of(entity.getWorld(), ModDamageTypes.STAND, attacker), power);
    }
}
