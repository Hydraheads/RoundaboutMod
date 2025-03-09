package net.hydra.jojomod.client.shader;

import org.joml.Vector3f;

/* Static class to handle data for fog as Mixins cannot directly store interchangable data */
public class FogDataHolder {
    public static boolean shouldRenderFog = false;

    public static float fogDensity = 0.f; // default 1
    public static float fogNear = 0.f; // default 0.05
    public static float fogFar = 0.f; // default 12

    /* solid white */
    public static Vector3f fogColor = new Vector3f(1.f, 1.f, 1.f);
}
