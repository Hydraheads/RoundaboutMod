package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class ZSlime {

    @Inject(method = "playerTouch", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$playerTouch(Player $$0, CallbackInfo ci) {
        if (((TimeStop) $$0.level()).CanTimeStopEntity((Slime)(Object)this)){
            ci.cancel();
        }
    }
}
