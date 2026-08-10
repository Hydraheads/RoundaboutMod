package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class DiscSealHudMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow private int screenWidth;
    @Shadow private int screenHeight;

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$renderDiscSeal(GuiGraphics context, int x, CallbackInfo ci) {
        if (roundaboutWhitesnake$renderDiscSealBar(context, x)) ci.cancel();
    }

    @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$renderMountedDiscSeal(PlayerRideableJumping mount, GuiGraphics context,
                                                             int x, CallbackInfo ci) {
        if (roundaboutWhitesnake$renderDiscSealBar(context, x)) ci.cancel();
    }

    @Unique
    private boolean roundaboutWhitesnake$renderDiscSealBar(GuiGraphics context, int x) {
        if (minecraft.player == null) return false;
        DiscBearer bearer = (DiscBearer) minecraft.player;
        int remaining = 0;
        int maximum = 0;
        for (byte type = WhitesnakeDiscUtil.SIGHT; type <= WhitesnakeDiscUtil.HEARING; type++) {
            int candidate = bearer.roundabout$getDiscSealTicks(type);
            if (candidate > remaining) {
                remaining = candidate;
                maximum = bearer.roundabout$getDiscSealMaxTicks(type);
            }
        }
        if (remaining <= 0) return false;

        StandUser standUser = (StandUser) minecraft.player;
        if (standUser.roundabout$getSealedTicks() > remaining) {
            remaining = standUser.roundabout$getSealedTicks();
            maximum = standUser.roundabout$getMaxSealedTicks();
        }
        maximum = Math.max(1, maximum);
        int filled = (int) Math.floor((182.0D / maximum) * (maximum - remaining));
        int y = screenHeight - 29;
        context.blit(StandIcons.JOJO_ICONS, x, y, 0, 90, 182, 5);
        if (filled > 0) context.blit(StandIcons.JOJO_ICONS, x, y, 0, 95, filled, 5);
        context.blit(StandIcons.JOJO_ICONS, screenWidth / 2 - 5, screenHeight - 36, 183, 60, 9, 9);
        return true;
    }
}
