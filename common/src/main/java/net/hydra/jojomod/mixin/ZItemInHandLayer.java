package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
    public boolean sba$ModifyEntity;
    @Unique
    public @Nullable ItemStack sba$RenderMainHand;
    @Unique
    public @Nullable ItemStack sba$RenderOffHand;
    @Unique
    public boolean dominant$Hand;
    @Unique
    public Entity stored$Ent;

    @Inject(method = "render", at = @At(value = "HEAD"),cancellable = true)
    public void sba$Render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci){
        stored$Ent = entity;
        IEntityAndData entityAndData = ((IEntityAndData) entity);
        StandUserClient userClient = ((StandUserClient) entity);
        if (entityAndData.sba$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung() && ClientNetworking.getAppropriateConfig() != null &&
        ClientNetworking.getAppropriateConfig().achtungSettings != null &&
                ClientNetworking.getAppropriateConfig().achtungSettings.hidesHeldItems && !ClientUtil.checkIfClientCanSeeMobsForWindVision()){
            ci.cancel();
            return;
        }
        if (entityAndData.sba$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision()){
            ci.cancel();
            return;
        }
        if (((StandUser)entity).sba$getStandPowers() instanceof Powers20thCenturyBoy CB && CB.invincibleState && PowerTypes.isUsingStand(entity)) {
            ci.cancel();
            return;
        }
        if (((StandUser)entity).sba$getStandPowers() instanceof PowersCatchTheRainbow PCR && PCR.ctrDodgeTicks != 0 && PowerTypes.isUsingStand(entity)) {
            ci.cancel();
            return;
        }


        dominant$Hand = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity instanceof Player) {
            sba$ModifyEntity = ((TimeStop) entity.level()).CanTimeStopEntity(entity) || ClientUtil.getScreenFreeze();
            if (sba$ModifyEntity) {
                if (userClient.sba$getSBARenderMainHand() == null){
                    userClient.sba$setSBARenderMainHand(entity.getMainHandItem().copy());
                }
                if (userClient.sba$getSBARenderOffHand() == null){
                    userClient.sba$setSBARenderOffHand(entity.getOffhandItem().copy());
                }
                sba$RenderMainHand = userClient.sba$getSBARenderMainHand();
                sba$RenderOffHand = userClient.sba$getSBARenderOffHand();
            } else {
                if (userClient.sba$getSBARenderOffHand() != null){
                    userClient.sba$setSBARenderOffHand(null);
                } else if (userClient.sba$getSBARenderMainHand() != null){
                    userClient.sba$setSBARenderMainHand(null);
                }
            }
        } else {
            sba$ModifyEntity = false;
        }

        LivingEntity host = entity;
        if ( ((StandUser)host).sba$getStandPowers() instanceof PowersRatt) {
            if ( ((StandUser)host).sba$getStandPowers().scopeLevel != 0  ) {
                ci.cancel();
            }
        }
        if (entity instanceof JojoNPC jnpc && jnpc.host != null && ((StandUser)jnpc.host).sba$getEffectiveCombatMode()){
            host = jnpc.host;
        }



        if (host != null && ((StandUser)host).sba$getEffectiveCombatMode() && !host.isUsingItem()){
            boolean bl = true;
            if (((StandUser)host).sba$getStandPowers() instanceof PowersAnubis PA) {
                if (AnubisLayer.shouldShowItem(host)) {
                    bl = false;
                }
            }


            if (bl) {
                ci.cancel();
                return;
            }
        }

        if (entity instanceof Player pl && PlayerPosIndex.isHidingHeldItem(((IPlayerEntity)pl).sba$GetPos2())){
            ci.cancel();
            return;
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    public ItemStack sba$Render2(ItemStack it){
        if (stored$Ent instanceof Player) {
            EquipmentSlot es;
            if (!dominant$Hand) {
                es = EquipmentSlot.MAINHAND;
                ItemStack newStack = MainUtil.xHandCancelItem(es, stored$Ent, it);
                if (!newStack.equals(it)){
                    return newStack;
                }
            } else {
                es = EquipmentSlot.OFFHAND;
                ItemStack newStack = MainUtil.xHandCancelItem(es, stored$Ent, it);
                if (!newStack.equals(it)){
                    return newStack;
                }
            }
        }


        if (sba$ModifyEntity) {
            if (!dominant$Hand) {
                if (sba$RenderMainHand != null) {
                    return sba$RenderMainHand;
                }
            } else {
                if (sba$RenderOffHand != null) {
                    return sba$RenderOffHand;
                }
            }
        }
        return it;
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 1)
    public ItemStack sba$Render3(ItemStack it){

        if (stored$Ent instanceof Player) {
            EquipmentSlot es;
            if (dominant$Hand) {
                es = EquipmentSlot.MAINHAND;
                ItemStack newStack = MainUtil.xHandCancelItem(es, stored$Ent, it);
                if (!newStack.equals(it)){
                    return newStack;
                }
            } else {
                es = EquipmentSlot.OFFHAND;
                ItemStack newStack = MainUtil.xHandCancelItem(es, stored$Ent, it);
                if (!newStack.equals(it)){
                    return newStack;
                }
            }
        }

        if (sba$ModifyEntity) {
            if (dominant$Hand) {
                if (sba$RenderMainHand != null) {
                    return sba$RenderMainHand;
                }
            } else {
                if (sba$RenderOffHand != null) {
                    return sba$RenderOffHand;
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
