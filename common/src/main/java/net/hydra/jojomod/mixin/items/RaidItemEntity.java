package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.access.IItemEntityAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class RaidItemEntity extends Entity implements IItemEntityAccess {


    /**This mixin makes designated reward items sparkle when drop, the executioner axe's template sparkles
     *  on the ground after winning a raid*/
    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (roundabout$raidSparkle){
            if (!this.level().isClientSide()){
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(3),
                        this.getRandomY(), this.getRandomZ(3),
                        4, 0.0, 0.5, 0.0, 0.35);
            }
        }
    }

    @Unique
    public boolean roundabout$raidSparkle = false;
    @Override
    @Unique
    public void roundabout$setRaidSparkle(boolean sparkle){
        roundabout$raidSparkle = sparkle;
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public RaidItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    public ItemStack getItem() {
        return null;
    }

    @Shadow public abstract int getAge();

}
