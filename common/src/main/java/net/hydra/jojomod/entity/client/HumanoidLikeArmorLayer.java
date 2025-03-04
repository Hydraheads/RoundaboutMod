package net.hydra.jojomod.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.PlayerLikeModel;
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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

import javax.annotation.Nullable;
import java.util.Map;

public class HumanoidLikeArmorLayer<T extends JojoNPC, M extends PlayerLikeModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final A innerModel;
    private final A outerModel;
    private final TextureAtlas armorTrimAtlas;

    public HumanoidLikeArmorLayer(RenderLayerParent<T, M> $$0, A $$1, A $$2, ModelManager $$3) {
        super($$0);
        this.innerModel = $$1;
        this.outerModel = $$2;
        this.armorTrimAtlas = $$3.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.CHEST, $$2, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.LEGS, $$2, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.FEET, $$2, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece($$0, $$1, $$3, EquipmentSlot.HEAD, $$2, this.getArmorModel(EquipmentSlot.HEAD));
    }

    private void renderArmorPiece(PoseStack $$0, MultiBufferSource $$1, T $$2, EquipmentSlot $$3, int $$4, A $$5) {
        ItemStack $$6 = $$2.getItemBySlot($$3);
        if ($$6.getItem() instanceof ArmorItem $$7) {
            if ($$7.getEquipmentSlot() == $$3) {
                this.getParentModel().copyPropertiesTo($$5);
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
