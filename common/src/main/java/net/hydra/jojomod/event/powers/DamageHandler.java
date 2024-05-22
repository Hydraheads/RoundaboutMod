package net.hydra.jojomod.event.powers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class DamageHandler {


    /** Returns an offset away from a player's sight*/
    public static Vec3 getRayPoint(LivingEntity entity, double maxDistance){
            float tickDelta = 0;
            if (entity.level().isClientSide()) {
                Minecraft mc = Minecraft.getInstance();
                tickDelta = mc.getDeltaFrameTime();
            }
            Vec3 vec3d = entity.getEyePosition(tickDelta);
            Vec3 vec3d2 = entity.getViewVector(tickDelta);
            return vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
    }

    /** Gets an offset one block away of the direction looked. Multiply to use any distance.*/
    public static Vec3 getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = Mth.cos(g);
        float i = Mth.sin(g);
        float j = Mth.cos(f);
        float k = Mth.sin(f);
        return new Vec3(i * j, -k, h * j);
    }
    /**Generates a hitbox*/
    public static List<Entity> genHitbox(LivingEntity entity, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ) {
        double k = Mth.floor(startX - radiusX);
        double l = Mth.floor(startX + radiusX);
        double r = (startY - radiusY);
        double s = (startY + radiusY);
        double t = (startZ - radiusZ);
        double u = (startZ + radiusZ);
        return entity.level().getEntities(entity, new AABB(k, r, t, l, s, u));
    }

    public static boolean StandDamageEntity(Entity entity, float power, Entity attacker){
        return entity.hurt(ModDamageTypes.of(entity.level(), ModDamageTypes.STAND, attacker), power);
    }
}