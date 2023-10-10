package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class InputMixin {

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    public int attackCooldown;

    /** This class is in part for detecting and canceling mouse inputs during stand attacks.
     * Please note this should
     * only apply to non-enhancer stands while their physical body is out.*/
        @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
        public void roundaboutAttack(CallbackInfoReturnable<Boolean> ci) {
            //handleInputEvents
            if (player != null) {
                StandUserComponent standComp = MyComponents.STAND_USER.get(player);
                if (standComp.getActive()){
                    ci.setReturnValue(true);
                }
                //while (this.options.attackKey.wasPressed()) {
                //}
            }
        }
        @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
        public void roundaboutBlockBreak(boolean breaking, CallbackInfo ci) {
            if (player != null) {
                StandUserComponent standComp = MyComponents.STAND_USER.get(player);
                if (standComp.getActive()) {
                    if (!breaking){
                        this.attackCooldown = 0;
                    }
                    this.interactionManager.cancelBlockBreaking();
                    ci.cancel();
                }
            }
        }
}
