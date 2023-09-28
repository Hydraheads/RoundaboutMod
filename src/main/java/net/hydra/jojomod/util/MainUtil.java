package net.hydra.jojomod.util;

public class MainUtil {

    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        return start + (delta * (end - start))*multiplier;
    }
}
