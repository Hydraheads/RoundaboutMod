package net.hydra.jojomod.client.models.stand;


import net.hydra.jojomod.entity.stand.PearlJamEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersPearlJam;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;



public class PearlJamModel<T extends PearlJamEntity> extends StandModel<T>{

    private final ModelPart stand2;
    private final ModelPart body2;
    private final ModelPart rightHand2;
    private final ModelPart leftHand2;
    private final ModelPart head2;

    private final ModelPart stand3;
    private final ModelPart body3;
    private final ModelPart rightHand3;
    private final ModelPart leftHand3;
    private final ModelPart head3;


    public PearlJamModel(ModelPart root){
        this.stand = root.getChild("pj_1").getChild("stand_1");
        this.body = this.stand.getChild("body11");
        this.rightHand = this.stand.getChild("right_arm11");
        this.leftHand = this.stand.getChild("left_arm11");
        this.head = this.stand.getChild("stem11");

        this.stand2 = root.getChild("pj_2").getChild("stand_2");
        this.body2 = this.stand2.getChild("body21");
        this.rightHand2 = this.stand2.getChild("right_arm21");
        this.leftHand2 = this.stand2.getChild("left_arm21");
        this.head2 = this.stand2.getChild("stem21");

        this.stand3 = root.getChild("pj_3").getChild("stand_3");
        this.body3 = this.stand3.getChild("body31");
        this.rightHand3 = this.stand3.getChild("right_arm31");
        this.leftHand3 = this.stand3.getChild("left_arm31");
        this.head3 = this.stand3.getChild("stem31");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition pj_1 = partdefinition.addOrReplaceChild("pj_1", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -7.0F));

        PartDefinition stand_1 = pj_1.addOrReplaceChild("stand_1", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition body11 = stand_1.addOrReplaceChild("body11", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm11 = stand_1.addOrReplaceChild("right_arm11", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm11 = stand_1.addOrReplaceChild("left_arm11", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem11 = stand_1.addOrReplaceChild("stem11", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition poses = pj_1.addOrReplaceChild("poses", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition stand12 = poses.addOrReplaceChild("stand12", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body12 = stand12.addOrReplaceChild("body12", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm12 = stand12.addOrReplaceChild("right_arm12", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm12 = stand12.addOrReplaceChild("left_arm12", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem12 = stand12.addOrReplaceChild("stem12", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition stand13 = poses.addOrReplaceChild("stand13", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body13 = stand13.addOrReplaceChild("body13", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm13 = stand13.addOrReplaceChild("right_arm13", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm13 = stand13.addOrReplaceChild("left_arm13", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem13 = stand13.addOrReplaceChild("stem13", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition pj_2 = partdefinition.addOrReplaceChild("pj_2", CubeListBuilder.create(), PartPose.offset(-4.0F, 7.0F, 4.0F));

        PartDefinition stand_2 = pj_2.addOrReplaceChild("stand_2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition body21 = stand_2.addOrReplaceChild("body21", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm21 = stand_2.addOrReplaceChild("right_arm21", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm21 = stand_2.addOrReplaceChild("left_arm21", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem21 = stand_2.addOrReplaceChild("stem21", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition poses2 = pj_2.addOrReplaceChild("poses2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -7.0F));

        PartDefinition stand22 = poses2.addOrReplaceChild("stand22", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body22 = stand22.addOrReplaceChild("body22", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, 3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, 3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, 5.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 7.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm22 = stand22.addOrReplaceChild("right_arm22", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm22 = stand22.addOrReplaceChild("left_arm22", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem22 = stand22.addOrReplaceChild("stem22", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, 6.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, 5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition stand23 = poses2.addOrReplaceChild("stand23", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body23 = stand23.addOrReplaceChild("body23", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, 3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, 3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, 5.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 7.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm23 = stand23.addOrReplaceChild("right_arm23", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm23 = stand23.addOrReplaceChild("left_arm23", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem23 = stand23.addOrReplaceChild("stem23", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, 6.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, 5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition pj_3 = partdefinition.addOrReplaceChild("pj_3", CubeListBuilder.create(), PartPose.offset(-7.0F, 28.0F, -15.0F));

        PartDefinition stand_3 = pj_3.addOrReplaceChild("stand_3", CubeListBuilder.create(), PartPose.offset(16.0F, -17.0F, 15.0F));

        PartDefinition body31 = stand_3.addOrReplaceChild("body31", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm31 = stand_3.addOrReplaceChild("right_arm31", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm31 = stand_3.addOrReplaceChild("left_arm31", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem31 = stand_3.addOrReplaceChild("stem31", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition poses3 = pj_3.addOrReplaceChild("poses3", CubeListBuilder.create(), PartPose.offset(16.0F, -17.0F, 15.0F));

        PartDefinition stand32 = poses3.addOrReplaceChild("stand32", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body32 = stand32.addOrReplaceChild("body32", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm32 = stand32.addOrReplaceChild("right_arm32", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm32 = stand32.addOrReplaceChild("left_arm32", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem32 = stand32.addOrReplaceChild("stem32", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition stand33 = poses3.addOrReplaceChild("stand33", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body33 = stand33.addOrReplaceChild("body33", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm33 = stand33.addOrReplaceChild("right_arm33", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition left_arm33 = stand33.addOrReplaceChild("left_arm33", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition stem33 = stand33.addOrReplaceChild("stem33", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 48, 48);
    }

    StandPowers Power = new PowersPearlJam(null);

    @Override
    public ModelPart root() {return stand;}
}
