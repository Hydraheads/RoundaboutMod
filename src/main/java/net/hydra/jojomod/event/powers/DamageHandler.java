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

    //public Vec3d reachOut(){
    //}

    public static void genPointHitbox(double maxDistance, LivingEntity entity, float power, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ){

        Vec3d pointVec = getRayPoint(entity, maxDistance);
        genHitbox(entity, power, pointVec.x, pointVec.y, pointVec.z, radiusX, radiusY, radiusZ);
        if (!entity.getWorld().isClient()){
            ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z, 1,0.0, 0.0, 0.0,1);
        }
        entity.getWorld().addParticle(ParticleTypes.EXPLOSION, true, pointVec.x, pointVec.y, pointVec.z, 0.0, 0.0, 0.0);
    }

    public static Vec3d getRayPoint(LivingEntity entity, double maxDistance){
            MinecraftClient mc = MinecraftClient.getInstance();
            float tickDelta = mc.getLastFrameDuration();
            Vec3d vec3d = entity.getCameraPosVec(tickDelta);
            Vec3d vec3d2 = entity.getRotationVec(tickDelta);
            return vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
    }
    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
    public static void genHitbox(LivingEntity entity, float power, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ) {
        double k = MathHelper.floor(startX - radiusX);
        double l = MathHelper.floor(startX + radiusX);
        double r = (startY - radiusY);
        double s = (startY + radiusY);
        double t = (startZ - radiusZ);
        double u = (startZ + radiusZ);
        RoundaboutMod.LOGGER.info("1");
        List<Entity> list = entity.getWorld().getOtherEntities(entity, new Box(k, r, t, l, s, u));
        for (Entity value : list) {
            RoundaboutMod.LOGGER.info("2");
            value.damage(ModDamageTypes.of(entity.getWorld(), ModDamageTypes.STAND), power);
        }
    }
}
