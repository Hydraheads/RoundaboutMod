package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class GravityEnderManMixin extends Monster implements NeutralMob {
    @Shadow protected abstract boolean teleport();

    @Shadow protected abstract boolean teleport(double d, double e, double f);

    protected GravityEnderManMixin(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(
            method = "isLookingAtMe(Lnet/minecraft/world/entity/player/Player;)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$isLookingAtMe(Player target, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN)
            return;
        ItemStack $$1 = target.getInventory().armor.get(3);
        if ($$1.is(Blocks.CARVED_PUMPKIN.asItem())) {
            cir.setReturnValue(false);
        } else {
            Vec3 $$2 = target.getViewVector(1.0F).normalize();
            Vec3 $$3 = new Vec3(this.getX() - target.getEyePosition().x, this.getEyeY() - target.getEyePosition().y, this.getZ() - target.getEyePosition().z);
            double $$4 = $$3.length();
            $$3 = $$3.normalize();
            double $$5 = $$2.dot($$3);
            cir.setReturnValue($$5 > 1.0 - 0.025 / $$4 ? target.hasLineOfSight(this) : false);
        }
    }


    @Inject(
            method = "teleportTowards(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$teleportTowards(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(entity);
        if (gravityDirection == Direction.DOWN)
            return;
        Vec3 $$1 = new Vec3(this.getX() - entity.getEyePosition().x, this.getY(0.5) - entity.getEyePosition().y, this.getZ() - entity.getEyePosition().z);
        $$1 = $$1.normalize();
        double $$2 = 16.0;
        double $$3 = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - $$1.x * 16.0;
        double $$4 = this.getY() + (double)(this.random.nextInt(16) - 8) - $$1.y * 16.0;
        double $$5 = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - $$1.z * 16.0;
        cir.setReturnValue(this.teleport($$3, $$4, $$5));
    }
}
