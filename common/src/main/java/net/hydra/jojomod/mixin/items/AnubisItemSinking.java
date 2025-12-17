package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class AnubisItemSinking extends Entity {

    public AnubisItemSinking(EntityType<?> $$0, Level $$1) {super($$0, $$1);}

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "setUnderwaterMovement",at = @At(value="HEAD"),cancellable = true)
    public void roundabout$anubisSinksInWater(CallbackInfo ci) {
        if (this.getItem().is(ModItems.ANUBIS_ITEM) && !this.level().isClientSide()) {

            Vec3 v = this.getDeltaMovement();
            this.setDeltaMovement(v.x*0.8,Math.max(v.y-0.1,-0.2),v.z*0.8);

            if (this.tickCount %16 == 0) {
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.BUBBLE,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        6, 0, 1, 0, 0.4);
            }
            ci.cancel();
        }
    }

}
