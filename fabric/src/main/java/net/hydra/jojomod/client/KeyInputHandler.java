package net.hydra.jojomod.client;

import com.mojang.blaze3d.platform.InputConstants;
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
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
@Environment(EnvType.CLIENT)
public class KeyInputHandler {

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
                            if (client.options.keyUp.isDown()) forward++;
                            if (client.options.keyDown.isDown()) forward--;
                            if (client.options.keyLeft.isDown()) strafe++;
                            if (client.options.keyRight.isDown()) strafe--;

                            if (mf != forward) {
                                FriendlyByteBuf buffer = PacketByteBufs.create();

                                buffer.writeByte(forward);
                                buffer.writeByte(strafe);
                                ClientPlayNetworking.send(ModMessages.MOVE_SYNC_ID, buffer);
                            }
                        }
                    }
                }
                while (KeyInputRegistry.summonKey.consumeClick()) {
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
                while (KeyInputRegistry.abilityOneKey.consumeClick()) {
                    //client.player.sendMessage(Text.of("Ability Key"));
                }
                while (KeyInputRegistry.abilityTwoKey.consumeClick()) {
                    //client.player.sendMessage(Text.of("Ability Key 2"));
                }
                while (KeyInputRegistry.abilityThreeKey.consumeClick()) {
                    //client.player.sendMessage(Text.of("Ability Key 3"));
                }
                while (KeyInputRegistry.abilityFourKey.consumeClick()) {
                    client.player.sendSystemMessage(Component.nullToEmpty("Special Move"));
                }
                while (KeyInputRegistry.menuKey.consumeClick()) {
                  client.setScreen(new PowerInventoryScreen(client.player));
                }
            }
        });
    }

    //registers the keys
    public static void register(){
        KeyInputRegistry.summonKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KeyInputRegistry.KEY_SUMMON_STAND,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KeyInputRegistry.KEY_CATEGORY_JOJO
        ));

        KeyInputRegistry.abilityOneKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KeyInputRegistry.KEY_ABILITY_1,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeyInputRegistry.KEY_CATEGORY_JOJO
        ));

        KeyInputRegistry.abilityTwoKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KeyInputRegistry.KEY_ABILITY_2,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KeyInputRegistry.KEY_CATEGORY_JOJO
        ));

        KeyInputRegistry.abilityThreeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KeyInputRegistry.KEY_ABILITY_3,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KeyInputRegistry.KEY_CATEGORY_JOJO
        ));

        KeyInputRegistry.abilityFourKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KeyInputRegistry.KEY_ABILITY_4,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KeyInputRegistry.KEY_CATEGORY_JOJO
        ));

        KeyInputRegistry.menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KeyInputRegistry.KEY_JOJO_MENU,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                KeyInputRegistry.KEY_CATEGORY_JOJO
        ));
        registerKeyInputs();
    }
}
