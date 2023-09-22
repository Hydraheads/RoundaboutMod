package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.access.IHudAccess;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class GameHudMixin implements IHudAccess {

    @Shadow
    @Final
    @Mutable
    private MinecraftClient client;
    @Shadow
    private int ticks;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    @Unique
    private float flashAlpha = 0f;
    @Unique
    private float otherFlashAlpha = 0f;

    //private void renderHotbar(float tickDelta, DrawContext context) {


    @Inject(method = "renderHotbar", at = @At(value = "TAIL"))
    private void renderStatusBarsMixin(float tickDelta, DrawContext context, CallbackInfo info) {
        StandHudRender.renderStandHud(context, client, this.getCameraPlayer(), scaledWidth, scaledHeight, ticks, this.getHeartCount(this.getRiddenEntity()), flashAlpha, otherFlashAlpha);
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void renderStatusBarsMixin(DrawContext context, CallbackInfo info) {
        StandHudRender.renderGuardHud(context, client, this.getCameraPlayer(), scaledWidth, scaledHeight, ticks, this.getHeartCount(this.getRiddenEntity()), flashAlpha, otherFlashAlpha);
    }

    @Shadow
    private PlayerEntity getCameraPlayer() {
        return null;
    }

    @Shadow
    private LivingEntity getRiddenEntity() {
        return null;
    }

    @Shadow
    private int getHeartCount(LivingEntity entity) {
        return 0;
    }

    @Override
    public void setFlashAlpha(float flashAlpha) {
        this.flashAlpha = flashAlpha;
    }

    @Override
    public void setOtherFlashAlpha(float otherFlashAlpha) {
        this.otherFlashAlpha = otherFlashAlpha;
    }
//    public static final String DEBUG_TEXT_1 = "hud.roundabout.standout";
}
