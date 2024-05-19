package net.hydra.jojomod.util;


import net.minecraft.util.math.MathHelper;

public class MainUtil {
    /** This version of interpolation accommodates speed multipliers so you can control how
     * fast something moves from point A to point B.
     * Ex: the speed a stand tilts in
     * @see  */
    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,2);
        return start + (delta * (end - start))*multiplier;
    }

    public static float controlledLerpAngleDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,2);
        return start + (delta * MathHelper.wrapDegrees(end - start))*multiplier;
    }public static float controlledLerpRadianDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,2);
        return start + (delta * wrapRadians(end - start))*multiplier;
    }

    public static float wrapRadians(float radians) {
        radians *= MathHelper.DEGREES_PER_RADIAN;
        float f = radians % 360.0f;
        if (f >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return (f*MathHelper.RADIANS_PER_DEGREE);
    }
}
