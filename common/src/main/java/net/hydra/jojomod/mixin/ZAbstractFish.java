package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFish.class)
public abstract class ZAbstractFish extends WaterAnimal {

    @Inject(method = "saveToBucketTag(Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "TAIL"))
    protected void roundabout$saveToBucketTag(ItemStack $$0, CallbackInfo ci) {
        CompoundTag $$1 = $$0.getOrCreateTag();
        MainUtil.saveEntityData($$1,((IEntityAndData) this));
        MainUtil.saveLivingData($$1,((StandUser) this),this);
        MainUtil.saveMobData($$1,((IMob) this));
    }
    @Inject(method = "loadFromBucketTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "TAIL"))
    protected void roundabout$loadFromBucketTag(CompoundTag $$0, CallbackInfo ci) {
        MainUtil.loadEntityData($$0,((IEntityAndData) this));
        MainUtil.loadLivingData($$0,((StandUser) this),this);
        MainUtil.loadMobData($$0,((IMob) this));
    }
    protected ZAbstractFish(EntityType<? extends WaterAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }
}
