package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SonicBoom.class)
public class SoftAndWetSonicBoom {

    /**This mixin is in relation to plundering sound.
     * The warden's Sonic Boom ability pops all sound bubble.
     * Much wow, very powerful.*/
    @Inject(method = "start(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/warden/Warden;J)V", at = @At(value = "HEAD"),cancellable = true)
    public void sba$start(ServerLevel $$0, Warden $$1, long $$2, CallbackInfo ci) {
        if ($$1 != null) {
            ILevelAccess access = ((ILevelAccess) $$1.level());
            if (access.sba$isSoundPlunderedEntity($$1)){
                SoftAndWetPlunderBubbleEntity bubs = access.sba$getSoundPlunderedBubbleEntity($$1);
                if (bubs != null) {
                    bubs.popBubble();
                }
            }
            access.sba$raptureBubbles($$1.blockPosition());
        }
    }
}
