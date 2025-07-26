package net.hydra.jojomod.mixin.achtung;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class AchtungItemEntity extends Entity {

    /***
     * Support for thrown item gaining invisibility
     */
    @Inject(method = "setThrower", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$setThrower(@Nullable UUID $$0, CallbackInfo ci) {
        if ($$0 != null) {
            Player pl =  this.level().getPlayerByUUID($$0);
            if (pl != null){
                if (MainUtil.getEntityIsTrulyInvisible(pl) && ClientNetworking.getAppropriateConfig().achtungSettings.hidesShotProjectiles){
                    ((IEntityAndData)this).roundabout$setTrueInvisibility(MainUtil.getEntityTrulyInvisibleTicks(pl));
                }
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public AchtungItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}
