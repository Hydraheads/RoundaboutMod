package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class AchtungGameRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;

    /***
     * Just being extra careful that the transparency achtung baby creates is getting reset in potential
     * spill-over areas in case mods are canceling things. This is the tail end of the renderItemInHand
     * function.
     */
    @Inject(method = "renderItemInHand", at = @At(value = "TAIL"))
    private void roundabout$renderItemInHand(PoseStack $$0, Camera $$1, float $$2, CallbackInfo ci)
    {
        ClientUtil.setThrowFadeToTheEther(1);
        ClientUtil.hideInvis = false;
    }


    @Inject(method = "pick", at = @At(value = "TAIL"))
    private void roundabout$pick(float $$0, CallbackInfo ci)
    {
        if (this.minecraft.hitResult instanceof EntityHitResult ehr){
            if (ehr != null && ehr.getEntity() != null){
                if (PowerTypes.isInADifferentExistence(ehr.getEntity(),this.minecraft.player)){
                    Entity $$1 = this.minecraft.getCameraEntity();
                    if ($$1 != null) {
                        if (this.minecraft.level != null) {
                            this.minecraft.getProfiler().push("pick");
                            this.minecraft.crosshairPickEntity = null;
                            double $$2 = (double)this.minecraft.gameMode.getPickRange();
                            this.minecraft.hitResult = $$1.pick($$2, $$0, false);
                            Vec3 $$3 = $$1.getEyePosition($$0);
                            boolean $$4 = false;
                            int $$5 = 3;
                            double $$6 = $$2;
                            if (this.minecraft.gameMode.hasFarPickRange()) {
                                $$6 = 6.0;
                                $$2 = $$6;
                            } else {
                                if ($$2 > 3.0) {
                                    $$4 = true;
                                }

                                $$2 = $$2;
                            }

                            $$6 *= $$6;
                            if (this.minecraft.hitResult != null) {
                                $$6 = this.minecraft.hitResult.getLocation().distanceToSqr($$3);
                            }

                            Vec3 $$7 = $$1.getViewVector(1.0F);
                            Vec3 $$8 = $$3.add($$7.x * $$2, $$7.y * $$2, $$7.z * $$2);
                            float $$9 = 1.0F;
                            AABB $$10 = $$1.getBoundingBox().expandTowards($$7.scale($$2)).inflate(1.0, 1.0, 1.0);
                            EntityHitResult $$11 = ProjectileUtil.getEntityHitResult($$1, $$3, $$8, $$10, $$0x -> !$$0x.isSpectator() && $$0x.isPickable()
                                    && !PowerTypes.isInADifferentExistence($$0x,this.minecraft.player), $$6);
                            if ($$11 != null) {
                                Entity $$12 = $$11.getEntity();
                                Vec3 $$13 = $$11.getLocation();
                                double $$14 = $$3.distanceToSqr($$13);
                                if ($$4 && $$14 > 9.0) {
                                    this.minecraft.hitResult = BlockHitResult.miss($$13, Direction.getNearest($$7.x, $$7.y, $$7.z), BlockPos.containing($$13));
                                } else if ($$14 < $$6 || this.minecraft.hitResult == null) {
                                    this.minecraft.hitResult = $$11;
                                    if ($$12 instanceof LivingEntity || $$12 instanceof ItemFrame) {
                                        this.minecraft.crosshairPickEntity = $$12;
                                    }
                                }
                            }

                            this.minecraft.getProfiler().pop();
                        }
                    }

                }
            }

        }
    }
}
