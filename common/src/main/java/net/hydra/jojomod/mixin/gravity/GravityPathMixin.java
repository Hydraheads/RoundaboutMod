package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Path.class)
public class GravityPathMixin {
    @Shadow @Final private List<Node> nodes;

    @Inject(
            method = "getEntityPosAtNode(Lnet/minecraft/world/entity/Entity;I)Lnet/minecraft/world/phys/Vec3;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$tickTrue(Entity $$0, int $$1, CallbackInfoReturnable<Vec3> cir) {
        Direction dir = ((IGravityEntity)$$0).roundabout$getGravityDirection();
        if (dir == Direction.DOWN)
            return;

        Vec3 transVec = new Vec3((double)(double)((int)($$0.getBbWidth() + 1.0F)) * 0.5,
                0,
                (double)((int)($$0.getBbWidth() + 1.0F)) * 0.5);
        transVec = RotationUtil.vecPlayerToWorld(transVec,dir);

        Node $$2 = this.nodes.get($$1);
        double $$3 = $$2.x + transVec.x;
        double $$4 = (double)$$2.y + transVec.y;
        double $$5 = (double)$$2.z + transVec.z;
        cir.setReturnValue(new Vec3($$3, $$4, $$5));
    }
}
