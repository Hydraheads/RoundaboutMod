package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ZItemInHandLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    public ZItemInHandLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    public boolean roundabout$ModifyEntity;
    public @Nullable ItemStack roundabout$RenderMainHand;
    public @Nullable ItemStack roundabout$RenderOffHand;
    public boolean dominant$Hand;


    @Unique
    private static final ResourceLocation roundabout$TEXTURE_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_1.png");
    @Unique
    private static final ResourceLocation roundabout$TEXTURE_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_2.png");
    @Unique
    private static final ResourceLocation roundabout$TEXTURE_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_3.png");
    @Unique
    private static final ResourceLocation roundabout$TEXTURE_4 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_4.png");
    @Inject(method = "render", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$Render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci){
        dominant$Hand = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity instanceof Player) {
            roundabout$ModifyEntity = ((TimeStop) entity.level()).CanTimeStopEntity(entity) || ClientUtil.getScreenFreeze();
            if (roundabout$ModifyEntity) {
                if (((IEntityAndData) entity).roundabout$getRoundaboutRenderMainHand() == null){
                    ((IEntityAndData) entity).roundabout$setRoundaboutRenderMainHand(entity.getMainHandItem().copy());
                }
                if (((IEntityAndData) entity).roundabout$getRoundaboutRenderOffHand() == null){
                    ((IEntityAndData) entity).roundabout$setRoundaboutRenderOffHand(entity.getOffhandItem().copy());
                }
                roundabout$RenderMainHand = ((IEntityAndData) entity).roundabout$getRoundaboutRenderMainHand();
                roundabout$RenderOffHand = ((IEntityAndData) entity).roundabout$getRoundaboutRenderOffHand();
            } else {
                if (((IEntityAndData) entity).roundabout$getRoundaboutRenderOffHand() != null){
                    ((IEntityAndData) entity).roundabout$setRoundaboutRenderOffHand(null);
                } else if (((IEntityAndData) entity).roundabout$getRoundaboutRenderMainHand() != null){
                    ((IEntityAndData) entity).roundabout$setRoundaboutRenderMainHand(null);
                }
            }
        } else {
            roundabout$ModifyEntity = false;
        }

        LivingEntity host = entity;
        if ( ((StandUser)host).roundabout$getStandPowers() instanceof PowersRatt) {
            if ( ((StandUser)host).roundabout$getStandPowers().scopeLevel != 0  ) {
                ci.cancel();
            }
        }

            if (entity instanceof JojoNPC jnpc && jnpc.host != null && ((StandUser)jnpc.host).roundabout$getEffectiveCombatMode()){
            host = jnpc.host;
        }
        if (host != null && ((StandUser)host).roundabout$getEffectiveCombatMode() && !host.isUsingItem()){
            ci.cancel();
            return;
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    public ItemStack roundabout$Render2(ItemStack it){
        if (roundabout$ModifyEntity) {
            if (!dominant$Hand) {
                if (roundabout$RenderMainHand != null) {
                    return roundabout$RenderMainHand;
                }
            } else {
                if (roundabout$RenderOffHand != null) {
                    return roundabout$RenderOffHand;
                }
            }
        }
        return it;
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 1)
    public ItemStack roundabout$Render3(ItemStack it){
        if (roundabout$ModifyEntity) {
            if (dominant$Hand) {
                if (roundabout$RenderMainHand != null) {
                    return roundabout$RenderMainHand;
                }
            } else {
                if (roundabout$RenderOffHand != null) {
                    return roundabout$RenderOffHand;
                }
            }
        }
        return it;
    }

    @Shadow
    protected void renderArmWithItem(LivingEntity $$0, ItemStack $$1, ItemDisplayContext $$2, HumanoidArm $$3, PoseStack $$4, MultiBufferSource $$5, int $$6){
    }
    @Shadow
    public void render(PoseStack var1, MultiBufferSource var2, int var3, T var4, float var5, float var6, float var7, float var8, float var9, float var10) {
    }
}
