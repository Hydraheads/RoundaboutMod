package net.hydra.jojomod.util;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Vector;

public class DebugParticles {


    public static void drawPoint(Level level, Vec3 vec3) {
        DebugParticles.drawPoint(level, vec3, new Vector3f(0, 1, 0));
    }
    public static void drawPoint(Level level, Vec3 vec3, Vector3f color) {
        level.addParticle(new DustParticleOptions(color,1),vec3.x,vec3.y,vec3.z,0,0,0);
    }

    public static void drawLine(Level level, Vec3 v1, Vec3 v2) {
        DebugParticles.drawLine(level, v1, v2, new Vector3f(0,1,0) );
    }
    public static void drawLine(Level level, Vec3 v1, Vec3 v2, Vector3f color) {
        for(float i=0;i<=1;i+=0.1F) {
            DebugParticles.drawPoint(level,v1.lerp(v2,i),color);
        }
    }

}
