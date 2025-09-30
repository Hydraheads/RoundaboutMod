package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.item.FogBlockItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class JusticeItemEntity extends Entity {

    /**This mixin makes fog blocks as items disappear fast and be foggy*/
    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (getItem().getItem() instanceof FogBlockItem){
            if (this.age < 5840){
                this.age = 5840;
            }

            if (!this.level().isClientSide()){
                if (this.getAge() % 3 == 0) {
                    ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, this.getRandomX(3),
                            this.getRandomY(), this.getRandomZ(3),
                            0, 0.0, 0.5, 0.0, 0.35);
                }
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    private int age;
    @Shadow
    public ItemStack getItem() {
        return null;
    }

    @Shadow public abstract int getAge();

    public JusticeItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

}
