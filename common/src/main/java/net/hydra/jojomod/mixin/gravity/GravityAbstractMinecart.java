package net.hydra.jojomod.mixin.gravity;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.access.AccessMinecart;
import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(AbstractMinecart.class)
public abstract class GravityAbstractMinecart extends Entity implements AccessMinecart {
    @Shadow
    @Final
    private static Map<RailShape, Pair<Vec3i, Vec3i>> EXITS;

    public GravityAbstractMinecart(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    @Nullable
    public abstract Vec3 getPos(double $$0, double $$1, double $$2);

    @Override
    @Unique
    public void rodbt$cleardata() {
    }
}
