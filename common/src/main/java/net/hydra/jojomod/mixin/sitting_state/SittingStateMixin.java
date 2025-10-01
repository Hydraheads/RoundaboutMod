package net.hydra.jojomod.mixin.sitting_state;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class SittingStateMixin {

    @Unique
    private boolean isSitting = false;

    @Unique
    public boolean isSitting() {
        return isSitting;
    }

    @Unique
    public void isSitting(boolean sitting) {
        this.isSitting = sitting;
    }
}
