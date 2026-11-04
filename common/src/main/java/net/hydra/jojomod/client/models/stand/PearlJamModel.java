package net.hydra.jojomod.client.models.stand;


import net.hydra.jojomod.client.models.substand.zsabbathsouls.animations.MobSoulAnimation;
import net.hydra.jojomod.entity.stand.PearlJamEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersPearlJam;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;



public class PearlJamModel<T extends PearlJamEntity> extends StandModel<T>{

    private final ModelPart stand2;
    private final ModelPart pearlone;
    private final ModelPart body;
    private final ModelPart rightHand;
    private final ModelPart leftHand;
    private final ModelPart head;
    private final ModelPart pearltwo;
    private final ModelPart body2;
    private final ModelPart rightHand2;
    private final ModelPart leftHand2;
    private final ModelPart head2;
    private final ModelPart pearlthree;
    private final ModelPart body3;
    private final ModelPart rightHand3;
    private final ModelPart leftHand3;
    private final ModelPart head3;


    public PearlJamModel(ModelPart root){
        this.stand = root.getChild("stand");
        this.stand2 = this.stand.getChild("stand2");

        this.pearlone = this.stand2.getChild("pearlone");
        this.body = this.pearlone.getChild("body");
        this.rightHand = this.pearlone.getChild("rightHand");
        this.leftHand = this.pearlone.getChild("leftHand");
        this.head = this.pearlone.getChild("head");

        this.pearltwo = this.stand2.getChild("pearltwo");
        this.body2 = this.pearltwo.getChild("body2");
        this.rightHand2 = this.pearltwo.getChild("rightHand2");
        this.leftHand2 = this.pearltwo.getChild("leftHand2");
        this.head2 = this.pearltwo.getChild("head2");

        this.pearlthree = this.stand2.getChild("pearlthree");
        this.body3 = this.pearlthree.getChild("body3");
        this.rightHand3 = this.pearlthree.getChild("rightHand3");
        this.leftHand3 = this.pearlthree.getChild("leftHand3");
        this.head3 = this.pearlthree.getChild("head3");

    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pearlone = stand2.addOrReplaceChild("pearlone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = pearlone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightHand = pearlone.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition leftHand = pearlone.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition head = pearlone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition pearltwo = stand2.addOrReplaceChild("pearltwo", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2 = pearltwo.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightHand2 = pearltwo.addOrReplaceChild("rightHand2", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition leftHand2 = pearltwo.addOrReplaceChild("leftHand2", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition head2 = pearltwo.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition pearlthree = stand2.addOrReplaceChild("pearlthree", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body3 = pearlthree.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightHand3 = pearlthree.addOrReplaceChild("rightHand3", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition leftHand3 = pearlthree.addOrReplaceChild("leftHand3", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition head3 = pearlthree.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 48, 48);

    }

    StandPowers Power = new PowersPearlJam(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.animate(pEntity.grabbedStandByBlackSabbath, MobSoulAnimation.grabbed_pearljam, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {return stand;}
}
