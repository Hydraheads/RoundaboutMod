package net.hydra.jojomod.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class FabricKeyInputs {


    //registers the keys
    public static void register(){
        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.summonKey);
        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.abilityOneKey);

        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.abilityTwoKey);
        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.abilityThreeKey);

        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.abilityFourKey);

        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.menuKey);
        
        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.guardKey);

        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.switchRow);

        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.showExp);

        KeyBindingHelper.registerKeyBinding(KeyInputRegistry.pose);

    }
}
