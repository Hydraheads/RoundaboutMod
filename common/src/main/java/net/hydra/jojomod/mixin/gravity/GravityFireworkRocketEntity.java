package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FireworkRocketEntity.class)
public abstract class GravityFireworkRocketEntity extends Entity {

    @Shadow
    private @Nullable LivingEntity attachedToEntity;


    public GravityFireworkRocketEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @ModifyVariable(
            method = "tick()V",
            at = @At(
                    value = "STORE"
            )
            , ordinal = 0
    )
    public Vec3 tick(Vec3 value) {
        if (attachedToEntity != null) {
            value = RotationUtil.vecWorldToPlayer(value, GravityAPI.getGravityDirection(attachedToEntity));
        }
        return value;
    }

}