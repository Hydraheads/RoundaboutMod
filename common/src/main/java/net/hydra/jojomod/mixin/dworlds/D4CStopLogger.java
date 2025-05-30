package net.hydra.jojomod.mixin.dworlds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class D4CStopLogger {
    @Redirect(method = "saveAllChunks", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), require = 1)
    private void roundabout$stopLoggingD4CWorlds(Logger instance, String message, Object arg1, Object arg2)
    {
        if (arg2 instanceof ResourceLocation location && !location.toString().startsWith("roundabout:d4c-")) {
            instance.info(message, arg1, arg2);
        }
    }

    @Redirect(method = "saveAllChunks", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V"), require = 1)
    private void roundabout$stopLoggingD4CChunks(Logger instance, String message, Object arg1)
    {
        if (arg1 instanceof ResourceLocation location && !location.toString().startsWith("roundabout:d4c-")) {
            instance.info(message, arg1);
        }
    }
}
