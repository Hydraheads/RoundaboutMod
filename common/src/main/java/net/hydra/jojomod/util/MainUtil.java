package net.hydra.jojomod.util;


import net.minecraft.util.Mth;

public class MainUtil {
    /**Additional math functions for the mod.*/

    /** This version of interpolation accommodates speed multipliers so you can control how
     * fast something moves from point A to point B.
     * Ex: the speed a stand tilts in*/
    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,2);
        return start + (delta * (end - start))*multiplier;
    }

    public static float controlledLerpAngleDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * Mth.wrapDegrees(end - start))*multiplier;
    }public static float controlledLerpRadianDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * wrapRadians(end - start))*multiplier;
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
}
