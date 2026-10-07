package net.hydra.jojomod.event.powers.khnum;

import net.minecraft.util.Mth;

/** Shared interpolation for Khnum's model and collision dimensions. */
public final class KhnumFormScale {
    private KhnumFormScale() { }

    public static float interpolate(byte fromForm, byte toForm, float progress, boolean width, boolean playerModel) {
        return Mth.lerp(Mth.clamp(progress, 0.0F, 1.0F), scale(fromForm, width, playerModel),
                scale(toForm, width, playerModel));
    }

    private static float scale(byte form, boolean width, boolean playerModel) {
        return switch (form) {
            case 1 -> width ? 0.85F : 1.3F;
            case 2 -> width ? 1.65F : 1.0F;
            case 3 -> width ? 0.75F : playerModel ? 0.75F : 0.62F;
            default -> 1.0F;
        };
    }
}
