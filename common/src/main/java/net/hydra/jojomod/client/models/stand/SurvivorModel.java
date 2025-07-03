package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.SoftAndWetAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SurvivorModel<T extends SoftAndWetEntity> extends StandModel<T> {
    public SurvivorModel(ModelPart root) {
        this.stand = root.getChild("stand");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create().texOffs(-12, 0).addBox(-8.0F, -1.025F, -4.0F, 11.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, -1.0F));

        PartDefinition bone = stand2.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(-2, 12).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
        }


        StandPowers Power = new PowersSoftAndWet(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
        this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
        this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);
        this.animate(pEntity.kick, SoftAndWetAnimations.Kick, pAgeInTicks, 1f);
        this.animate(pEntity.kick_charge, SoftAndWetAnimations.ChargeKick, pAgeInTicks, 1f);
        this.animate(pEntity.encasement_punch, SoftAndWetAnimations.ChargedPunch, pAgeInTicks, 1f);
        }

    @Override
    public ModelPart root() {
        return stand;
        }
}

