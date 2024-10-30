package net.hydra.jojomod.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ForgeKeyInputs {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
         event.register(KeyInputRegistry.summonKey);
         event.register(KeyInputRegistry.abilityOneKey);
         event.register(KeyInputRegistry.abilityTwoKey);
         event.register(KeyInputRegistry.abilityThreeKey);
         event.register(KeyInputRegistry.abilityFourKey);
         event.register(KeyInputRegistry.menuKey);
        event.register(KeyInputRegistry.guardKey);
        event.register(KeyInputRegistry.switchRow);
        event.register(KeyInputRegistry.showExp);
        }
}
