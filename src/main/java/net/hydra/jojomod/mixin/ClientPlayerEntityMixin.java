package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    public Input input;

    @Shadow
    protected int ticksLeftToDoubleTapSprint;

    /**This code mirrors item usage code, and it's why you are slower while eating.
     * The purpose of this mixin is to make stand blocking slow you down.*/
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onMovement(Lnet/minecraft/client/input/Input;)V", shift = At.Shift.AFTER))
    private void RoundaboutTickMovement(CallbackInfo ci) {
        if (((StandUser) this).isDazed()) {
            this.input.movementSideways = 0;
            this.input.movementForward = 0;
            this.ticksLeftToDoubleTapSprint = 0;
        } else if (((StandUser) this).isGuarding() || ((StandUser) this).isBarraging()) {
            this.input.movementSideways *= 0.2f;
            this.input.movementForward *= 0.2f;
            this.ticksLeftToDoubleTapSprint = 0;
        }
    }
}
