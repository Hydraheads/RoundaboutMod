package net.hydra.jojomod.client.shader;

import org.joml.Vector3f;

/* Static class to handle data for fog as Mixins cannot directly store data */
public class FogDataHolder {
    public static boolean shouldRenderFog = false;

    public static float fogDensity = 1.f;
    public static float fogNear = 0.05f;
    public static float fogFar = 12.f;

    /* solid white */
    public static Vector3f fogColor = new Vector3f(1.f, 1.f, 1.f);
}
