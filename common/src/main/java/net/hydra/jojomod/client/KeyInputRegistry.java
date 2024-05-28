package net.hydra.jojomod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyInputRegistry {
    public static final String KEY_CATEGORY_JOJO = "key.category.roundabout.jojo";
    public static final String KEY_ABILITY_1 = "key.roundabout.ability.one";
    public static final String KEY_ABILITY_2 = "key.roundabout.ability.two";
    public static final String KEY_ABILITY_3 = "key.roundabout.ability.three";
    public static final String KEY_ABILITY_4 = "key.roundabout.ability.four";
    public static final String KEY_SUMMON_STAND = "key.roundabout.summon.stand";
    public static final String KEY_JOJO_MENU = "key.roundabout.menu";

    public static final KeyMapping summonKey = new KeyMapping(
            KeyInputRegistry.KEY_SUMMON_STAND,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );

    public static  KeyMapping abilityOneKey = new KeyMapping(
            KeyInputRegistry.KEY_ABILITY_1,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );

    public static final KeyMapping abilityTwoKey = new KeyMapping(
            KeyInputRegistry.KEY_ABILITY_2,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static final KeyMapping abilityThreeKey = new KeyMapping(
            KeyInputRegistry.KEY_ABILITY_3,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static KeyMapping abilityFourKey = new KeyMapping(
            KeyInputRegistry.KEY_ABILITY_4,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
    public static KeyMapping menuKey = new KeyMapping(
            KeyInputRegistry.KEY_JOJO_MENU,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            KeyInputRegistry.KEY_CATEGORY_JOJO
    );
}
