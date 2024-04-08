package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DamageHandler {

    //public Vec3d reachOut(){
    //}

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
            value.damage(entity.getWorld().getDamageSources().magic(), power);
        }
    }
}
