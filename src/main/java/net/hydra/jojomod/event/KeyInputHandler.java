package net.hydra.jojomod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.ModMessages;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_JOJO = "key.category.roundabout.jojo";
    public static final String KEY_ABILITY_1 = "key.roundabout.ability.one";
    public static final String KEY_ABILITY_2 = "key.roundabout.ability.two";
    public static final String KEY_ABILITY_3 = "key.roundabout.ability.three";
    public static final String KEY_ABILITY_4 = "key.roundabout.ability.four";
    public static final String KEY_SUMMON_STAND = "key.roundabout.summon.stand";

    public static KeyBinding summonKey;
    public static KeyBinding abilityOneKey;
    public static KeyBinding abilityTwoKey;
    public static KeyBinding abilityThreeKey;
    public static KeyBinding abilityFourKey;

    //This is what the keys do, what code they run
    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                //RoundaboutMod.LOGGER.info("px");
                if (client.player.isAlive()) {
                    //RoundaboutMod.LOGGER.info(""+client.options.forwardKey.isPressed());
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeBoolean(client.options.forwardKey.isPressed());
                    buffer.writeBoolean(client.options.backKey.isPressed());
                    buffer.writeBoolean(client.options.leftKey.isPressed());
                    buffer.writeBoolean(client.options.rightKey.isPressed());
                    ClientPlayNetworking.send(ModMessages.MOVE_SYNC_ID, buffer);
                }
                while (summonKey.wasPressed()) {
                    //client.player.sendMessage(Text.of("Summon Key"));
                    ClientPlayNetworking.send(ModMessages.SUMMON_ID, PacketByteBufs.create());
                }
                while (abilityOneKey.wasPressed()) {
                    //client.player.sendMessage(Text.of("Ability Key"));
                }
                while (abilityTwoKey.wasPressed()) {
                    //client.player.sendMessage(Text.of("Ability Key 2"));
                }
                while (abilityThreeKey.wasPressed()) {
                    //client.player.sendMessage(Text.of("Ability Key 3"));
                }
                while (abilityFourKey.wasPressed()) {
                    client.player.sendMessage(Text.of("Special Move"));
                }
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
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY_JOJO
        ));

        abilityTwoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ABILITY_2,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KEY_CATEGORY_JOJO
        ));

        abilityThreeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ABILITY_3,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY_JOJO
        ));

        abilityFourKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ABILITY_4,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KEY_CATEGORY_JOJO
        ));
        registerKeyInputs();
    }
}
