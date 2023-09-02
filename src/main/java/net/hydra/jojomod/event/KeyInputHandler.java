package net.hydra.jojomod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_JOJO = "key.category.roundabout.jojo";
    public static final String KEY_ABILITY_1 = "key.roundabout.ability.one";
    public static final String KEY_ABILITY_2 = "key.roundabout.ability.two";
    public static final String KEY_ABILITY_3 = "key.roundabout.ability.three";
    public static final String KEY_SUMMON_STAND = "key.roundabout.summon.stand";

    public static KeyBinding summonKey;
    public static KeyBinding abilityOneKey;
    public static KeyBinding abilityTwoKey;
    public static KeyBinding abilityThreeKey;

    //This is what the keys do, what code they run
    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            assert client.player != null;
            if (summonKey.wasPressed()){
                client.player.sendMessage(Text.of("Summon Key"));
            } else if (abilityOneKey.wasPressed()){
                client.player.sendMessage(Text.of("Ability Key"));
            } else if (abilityTwoKey.wasPressed()){
                client.player.sendMessage(Text.of("Ability Key 2"));
            } else if (abilityThreeKey.wasPressed()){
                client.player.sendMessage(Text.of("Ability Key 3"));
            }
        });
    }

    //registers the keys
    public static void register(){
        summonKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SUMMON_STAND,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_JOJO
        ));

        abilityOneKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ABILITY_1,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KEY_CATEGORY_JOJO
        ));

        abilityTwoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ABILITY_2,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY_JOJO
        ));

        abilityThreeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ABILITY_3,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY_JOJO
        ));
        registerKeyInputs();
    }
}
