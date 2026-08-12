package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.models.FakeCapeLayer;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.RoadRollerItem;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

public class FogCloneRenderer<T extends FogCloneEntity> extends CloneRenderer<T> {

    public FogCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public void renderExtra(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight,
                            PlayerRenderer PR, Player pl){

        entity.setItemSlot(EquipmentSlot.HEAD, pl.getItemBySlot(EquipmentSlot.HEAD));
        entity.setItemSlot(EquipmentSlot.CHEST, pl.getItemBySlot(EquipmentSlot.CHEST));
        entity.setItemSlot(EquipmentSlot.LEGS, pl.getItemBySlot(EquipmentSlot.LEGS));
        entity.setItemSlot(EquipmentSlot.FEET, pl.getItemBySlot(EquipmentSlot.FEET));

        if (!(pl.getMainHandItem().getItem() instanceof FirearmItem) &&
                !(pl.getMainHandItem().getItem() instanceof RoadRollerItem)) {
            entity.setItemInHand(InteractionHand.MAIN_HAND, pl.getMainHandItem());
        }
        if (!(pl.getOffhandItem().getItem() instanceof FirearmItem) &&
                !(pl.getOffhandItem().getItem() instanceof RoadRollerItem)) {
            entity.setItemInHand(InteractionHand.OFF_HAND, pl.getOffhandItem());
        }
        entity.setPose(pl.getPose());
        entity.setSwimming(pl.isSwimming());
        ILivingEntityAccess ila = ((ILivingEntityAccess) pl);
        ILivingEntityAccess ila2 = ((ILivingEntityAccess) entity);
        ila2.roundabout$setSwimAmount(ila.roundabout$getSwimAmount());
        ila2.roundabout$setSwimAmountO(ila.roundabout$getSwimAmountO());
        ila2.roundabout$setWasTouchingWater(ila.roundabout$getWasTouchingWater());
        ila2.roundabout$setFallFlyingTicks(pl.getFallFlyingTicks());
        ila2.roundabout$setSharedFlag(1, ila.roundabout$getSharedFlag(1));
        ila2.roundabout$setSharedFlag(2, ila.roundabout$getSharedFlag(2));
        ila2.roundabout$setSharedFlag(3, ila.roundabout$getSharedFlag(3));
        ila2.roundabout$setSharedFlag(4, ila.roundabout$getSharedFlag(4));
        ila2.roundabout$setSharedFlag(5, ila.roundabout$getSharedFlag(5));
        ila2.roundabout$setSharedFlag(6, ila.roundabout$getSharedFlag(6));
        entity.deathTime = pl.deathTime;
        entity.setHealth(pl.getHealth());
        this.model.crouching = entity.isCrouching();
        ila2.roundabout$setUseItem(pl.getUseItem());
        ila2.roundabout$setUseItemTicks(pl.getUseItemRemainingTicks());


        if (pl.isFallFlying()) {
            if (!ila2.roundabout$getSharedFlag(7)) {
                ila2.roundabout$setSharedFlag(7, true);
            }
        } else {
            if (ila2.roundabout$getSharedFlag(7)) {
                ila2.roundabout$setSharedFlag(7, false);
            }
        }


    }

    @Override
    protected boolean shouldShowName(T $$0) {
        return false;
    }

}

