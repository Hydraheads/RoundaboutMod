package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
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

    @Unique
    public boolean roundabout$ModifyEntity;
    @Unique
    public @Nullable ItemStack roundabout$RenderMainHand;
    @Unique
    public @Nullable ItemStack roundabout$RenderOffHand;
    @Unique
    public boolean dominant$Hand;

    @Inject(method = "render", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$Render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci){

        IEntityAndData entityAndData = ((IEntityAndData) entity);
        StandUserClient userClient = ((StandUserClient) entity);
        if (entityAndData.roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung() && ClientNetworking.getAppropriateConfig() != null &&
        ClientNetworking.getAppropriateConfig().achtungSettings != null &&
                ClientNetworking.getAppropriateConfig().achtungSettings.hidesHeldItems){
            ci.cancel();
            return;
        }

        dominant$Hand = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity instanceof Player) {
            roundabout$ModifyEntity = ((TimeStop) entity.level()).CanTimeStopEntity(entity) || ClientUtil.getScreenFreeze();
            if (roundabout$ModifyEntity) {
                if (userClient.roundabout$getRoundaboutRenderMainHand() == null){
                    userClient.roundabout$setRoundaboutRenderMainHand(entity.getMainHandItem().copy());
                }
                if (userClient.roundabout$getRoundaboutRenderOffHand() == null){
                    userClient.roundabout$setRoundaboutRenderOffHand(entity.getOffhandItem().copy());
                }
                roundabout$RenderMainHand = userClient.roundabout$getRoundaboutRenderMainHand();
                roundabout$RenderOffHand = userClient.roundabout$getRoundaboutRenderOffHand();
            } else {
                if (userClient.roundabout$getRoundaboutRenderOffHand() != null){
                    userClient.roundabout$setRoundaboutRenderOffHand(null);
                } else if (userClient.roundabout$getRoundaboutRenderMainHand() != null){
                    userClient.roundabout$setRoundaboutRenderMainHand(null);
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
