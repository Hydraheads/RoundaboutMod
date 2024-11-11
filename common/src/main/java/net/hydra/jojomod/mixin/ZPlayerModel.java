package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerModel;
import net.minecraft.client.model.PlayerModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerModel.class)
public class ZPlayerModel implements IPlayerModel {

    @Shadow
    @Final
    private boolean slim;
    @Override
    @Unique
    public boolean roundabout$getSlim(){
        return this.slim;
    }

}
