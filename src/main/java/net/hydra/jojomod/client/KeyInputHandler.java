package net.hydra.jojomod.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModMessages;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
@Environment(EnvType.CLIENT)
public class KeyInputHandler {
    public static final String KEY_CATEGORY_JOJO = "key.category.roundabout.jojo";
    public static final String KEY_ABILITY_1 = "key.roundabout.ability.one";
    public static final String KEY_ABILITY_2 = "key.roundabout.ability.two";
    public static final String KEY_ABILITY_3 = "key.roundabout.ability.three";
    public static final String KEY_ABILITY_4 = "key.roundabout.ability.four";
    public static final String KEY_SUMMON_STAND = "key.roundabout.summon.stand";
    public static final String KEY_JOJO_MENU = "key.roundabout.menu";

    public static KeyBinding summonKey;
    public static KeyBinding abilityOneKey;
    public static KeyBinding abilityTwoKey;
    public static KeyBinding abilityThreeKey;
    public static KeyBinding abilityFourKey;
    public static KeyBinding menuKey;

    //This is what the keys do, what code they run
    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                //RoundaboutMod.LOGGER.info("px");
                if (client.player.isAlive()) {
                    //RoundaboutMod.LOGGER.info(""+client.options.forwardKey.isPressed());

                    /*If you have a stand out, update the stand leaning attributes.
                    * Currently, strafe is reported, but unused.*/
                    if (((StandUser) client.player).getActive()) {
                        StandEntity stand = ((StandUser) client.player).getStand();
                        if (stand != null) {
                            var mf = stand.getMoveForward();
                            byte forward = 0;
                            byte strafe = 0;
                            if (client.options.forwardKey.isPressed()) forward++;
                            if (client.options.backKey.isPressed()) forward--;
                            if (client.options.leftKey.isPressed()) strafe++;
                            if (client.options.rightKey.isPressed()) strafe--;

                            if (mf != forward) {
                                PacketByteBuf buffer = PacketByteBufs.create();

                                buffer.writeByte(forward);
                                buffer.writeByte(strafe);
                                ClientPlayNetworking.send(ModMessages.MOVE_SYNC_ID, buffer);
                            }
                        }
                    }
                }
                while (summonKey.wasPressed()) {
                    //client.player.sendMessage(Text.of("Summon Key"));
                    if (((StandUser) client.player).getSummonCD()) {
                        if (((StandUser) client.player).getActive()){
                            ((StandUser) client.player).setSummonCD(8);
                        } else {
                            ((StandUser) client.player).setSummonCD(2);
                        }
                        ClientPlayNetworking.send(ModMessages.STAND_SUMMON_PACKET, PacketByteBufs.create());
                    }
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
                while (menuKey.wasPressed()) {
                  client.setScreen(new PowerInventoryScreen(client.player));
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

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_JOJO_MENU,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                KEY_CATEGORY_JOJO
        ));
        registerKeyInputs();
    }
}
