package net.hydra.jojomod.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.PlayerLikeModel;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Map;

public class HumanoidLikeArmorLayer<T extends JojoNPC, M extends PlayerLikeModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final A innerModel;
    private final A outerModel;
    private final TextureAtlas armorTrimAtlas;

    @Unique
    public int roundabout$ArmorPhase;
    @Unique
    public boolean roundabout$ModifyEntity;
    @Unique
    public @org.jetbrains.annotations.Nullable ItemStack roundabout$RenderChest;
    @Unique
    public @org.jetbrains.annotations.Nullable ItemStack roundabout$RenderLegs;
    @Unique
    public @org.jetbrains.annotations.Nullable ItemStack roundabout$RenderBoots;
    @Unique
    public @org.jetbrains.annotations.Nullable ItemStack roundabout$RenderHead;

    public HumanoidLikeArmorLayer(RenderLayerParent<T, M> $$0, A $$1, A $$2, ModelManager $$3) {
        super($$0);
            this.innerModel = $$1;
            this.outerModel = $$2;
            this.armorTrimAtlas = $$3.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        roundabout$ArmorPhase = 0;
        if ($$3.host != null) {
            IPlayerEntity ipe = ((IPlayerEntity) $$3.host);
            if (ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift()) == ShapeShifts.EERIE){
                return;
            }

            roundabout$ModifyEntity = ((TimeStop) $$3.level()).CanTimeStopEntity($$3) || ClientUtil.getScreenFreeze();
            if (roundabout$ModifyEntity) {
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderChest() == null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderChest($$3.getItemBySlot(EquipmentSlot.CHEST).copy());
                }
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderLegs() == null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderLegs($$3.getItemBySlot(EquipmentSlot.LEGS).copy());
                }
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderBoots() == null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderBoots($$3.getItemBySlot(EquipmentSlot.FEET).copy());
                }
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderHead() == null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderHead($$3.getItemBySlot(EquipmentSlot.HEAD).copy());
                }
                roundabout$RenderChest = ((IEntityAndData) $$3).roundabout$getRoundaboutRenderChest();
                roundabout$RenderLegs = ((IEntityAndData) $$3).roundabout$getRoundaboutRenderLegs();
                roundabout$RenderBoots = ((IEntityAndData) $$3).roundabout$getRoundaboutRenderBoots();
                roundabout$RenderHead = ((IEntityAndData) $$3).roundabout$getRoundaboutRenderHead();
            } else {
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderChest() != null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderChest(null);
                }
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderLegs() != null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderLegs(null);
                }
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderBoots() != null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderBoots(null);
                }
                if (((IEntityAndData) $$3).roundabout$getRoundaboutRenderHead() != null){
                    ((IEntityAndData) $$3).roundabout$setRoundaboutRenderHead(null);
                }
            }

            if (!((IPlayerEntity)$$3.host).roundabout$getMaskSlot().isEmpty()
                    && ((IPlayerEntity)$$3.host).roundabout$getMaskSlot().getItem() instanceof MaskItem){
                return;
            }

        } else {
            roundabout$ModifyEntity = false;
        }
        if (ConfigManager.getClientConfig().renderArmorOnFogClones) {
            this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.CHEST, $$2, this.getArmorModel(EquipmentSlot.CHEST));
            this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.LEGS, $$2, this.getArmorModel(EquipmentSlot.LEGS));
            this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.FEET, $$2, this.getArmorModel(EquipmentSlot.FEET));
            this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.HEAD, $$2, this.getArmorModel(EquipmentSlot.HEAD));
        }
    }

    private void renderArmorPiece(PoseStack $$0, MultiBufferSource $$1, T $$2, EquipmentSlot $$3, int $$4, A $$5) {
        ItemStack $$6 = store($$2.getItemBySlot($$3));
        if ($$6.getItem() instanceof ArmorItem $$7) {
            if ($$7.getEquipmentSlot() == $$3) {
                this.getParentModel().copyPropertiesTo2($$5);
                this.setPartVisibility($$5, $$3);
                boolean $$9 = this.usesInnerModel($$3);
                if ($$7 instanceof DyeableArmorItem $$10) {
                    int $$11 = $$10.getColor($$6);
                    float $$12 = (float)($$11 >> 16 & 0xFF) / 255.0F;
                    float $$13 = (float)($$11 >> 8 & 0xFF) / 255.0F;
                    float $$14 = (float)($$11 & 0xFF) / 255.0F;
                    this.renderModel($$0, $$1, $$4, $$7, $$5, $$9, $$12, $$13, $$14, null);
                    this.renderModel($$0, $$1, $$4, $$7, $$5, $$9, 1.0F, 1.0F, 1.0F, "overlay");
                } else {
                    this.renderModel($$0, $$1, $$4, $$7, $$5, $$9, 1.0F, 1.0F, 1.0F, null);
                }

                ArmorTrim.getTrim($$2.level().registryAccess(), $$6).ifPresent($$6x -> this.renderTrim($$7.getMaterial(), $$0, $$1, $$4, $$6x, $$5, $$9));
                if ($$6.hasFoil()) {
                    this.renderGlint($$0, $$1, $$4, $$5);
                }
            }
        }
    }

    public ItemStack store(ItemStack stack){
        if (roundabout$ModifyEntity) {
            ItemStack rewrite;
            roundabout$ArmorPhase++;
            if (roundabout$ArmorPhase == 1){
                if (roundabout$RenderChest != null) {
                    return roundabout$RenderChest;
                }
            } else if (roundabout$ArmorPhase == 2){
                if (roundabout$RenderLegs != null) {
                    return roundabout$RenderLegs;
                }
            } else if (roundabout$ArmorPhase == 3){
                if (roundabout$RenderBoots != null) {
                    return roundabout$RenderBoots;
                }
            } else {
                if (roundabout$RenderHead != null) {
                    return roundabout$RenderHead;
                }
            }
            return stack;
        } else {
            return stack;
        }
    }

    protected void setPartVisibility(A $$0, EquipmentSlot $$1) {
        $$0.setAllVisible(false);
        switch ($$1) {
            case HEAD:
                $$0.head.visible = true;
                $$0.hat.visible = true;
                break;
            case CHEST:
                $$0.body.visible = true;
                $$0.rightArm.visible = true;
                $$0.leftArm.visible = true;
                break;
            case LEGS:
                $$0.body.visible = true;
                $$0.rightLeg.visible = true;
                $$0.leftLeg.visible = true;
                break;
            case FEET:
                $$0.rightLeg.visible = true;
                $$0.leftLeg.visible = true;
        }
    }

    private void renderModel(
            PoseStack $$0, MultiBufferSource $$1, int $$2, ArmorItem $$3, A $$4, boolean $$5, float $$6, float $$7, float $$8, @Nullable String $$9
    ) {
        VertexConsumer $$10 = $$1.getBuffer(RenderType.armorCutoutNoCull(this.getArmorLocation($$3, $$5, $$9)));
        $$4.renderToBuffer($$0, $$10, $$2, OverlayTexture.NO_OVERLAY, $$6, $$7, $$8, 1.0F);
    }

    private void renderTrim(ArmorMaterial $$0, PoseStack $$1, MultiBufferSource $$2, int $$3, ArmorTrim $$4, A $$5, boolean $$6) {
        TextureAtlasSprite $$7 = this.armorTrimAtlas.getSprite($$6 ? $$4.innerTexture($$0) : $$4.outerTexture($$0));
        VertexConsumer $$8 = $$7.wrap($$2.getBuffer(Sheets.armorTrimsSheet()));
        $$5.renderToBuffer($$1, $$8, $$3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGlint(PoseStack $$0, MultiBufferSource $$1, int $$2, A $$3) {
        $$3.renderToBuffer($$0, $$1.getBuffer(RenderType.armorEntityGlint()), $$2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private A getArmorModel(EquipmentSlot $$0) {
        return this.usesInnerModel($$0) ? this.innerModel : this.outerModel;
    }

    private boolean usesInnerModel(EquipmentSlot $$0) {
        return $$0 == EquipmentSlot.LEGS;
    }

    private ResourceLocation getArmorLocation(ArmorItem $$0, boolean $$1, @Nullable String $$2) {
        String $$3 = "textures/models/armor/" + $$0.getMaterial().getName() + "_layer_" + ($$1 ? 2 : 1) + ($$2 == null ? "" : "_" + $$2) + ".png";
        return ARMOR_LOCATION_CACHE.computeIfAbsent($$3, ResourceLocation::new);
    }
}
