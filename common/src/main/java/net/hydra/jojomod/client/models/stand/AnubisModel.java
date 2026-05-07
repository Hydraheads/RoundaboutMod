package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.anubis.AnubisFirstPersonAnimations;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class AnubisModel extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart sword;

    public AnubisModel() {
        super(RenderType::entityCutout);

        this.sword = createBodyLayer().bakeRoot();

        ModItemModels.ANUBIS_MODEL = this;
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create()
                .texOffs(-2, 0).addBox(-6.0F, -0.05F, -28.5F, 6.0F, 0.0F, 27.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-4.0F, -0.55F, 0.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(18, 27).addBox(-7.0F, -0.55F, -1.5F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(21, 31).addBox(-3.5F, 0.5F, -1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(21, 31).addBox(-3.5F, -0.6F, -1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-10, 35).addBox(-8.0F, 0.0F, -2.5F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                ,PartPose.offsetAndRotation(-2.0F, -4.75F, -1.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition sheath = stand.addOrReplaceChild("sheath", CubeListBuilder.create().texOffs(1, 34).addBox(-4.5F, -1.0F, -28.5F, 3.0F, 2.0F, 27.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.5F, 0.0F, -6.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    // x axis:
    // y axis: downwards
    // z axis:

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        sword.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return sword;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    public static ResourceLocation item = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/anime_item.png");
    public static ResourceLocation anime = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/anime.png");
    public static ResourceLocation raging = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/raging_katana.png");
    public static ResourceLocation alluring = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/alluring_katana.png");
    public static ResourceLocation evil = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/evil.png");
    public static ResourceLocation wooden = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/wooden.png");
    public static ResourceLocation stone = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/stone.png");
    public static ResourceLocation grass = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/grass.png");
    public static ResourceLocation aquamarine = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/aquamarine.png");
    public static ResourceLocation gray_wagon = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/gray_wagon.png");
    public static ResourceLocation timekeeper = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/timekeeper.png");
    public static ResourceLocation diamond = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/diamond.png");
    public static ResourceLocation chorus = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/ender.png");
    public static ResourceLocation ancient = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/ancient.png");
    public static ResourceLocation khopesh = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/khopesh.png");
    public static ResourceLocation cleaver = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/cleaver.png");
    public static ResourceLocation cleaver_sheathed = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/cleaver_sheathed.png");
    public static ResourceLocation illusory = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/illusory.png");
    public static ResourceLocation illusory_sheathed = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/illusory_sheathed.png");
    public static ResourceLocation bloodstained = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/bloodstained.png");
    public static ResourceLocation brilliance = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/brilliance.png");
    public static ResourceLocation chainblade_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/chainblade_1.png");
    public static ResourceLocation chainblade_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/chainblade_2.png");
    public static ResourceLocation chef = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/chef.png");
    public static ResourceLocation serpent = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/serpent.png");
    public static ResourceLocation[] soul = {new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/soul.png"),
                                            new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/soul_2.png"),
                                            new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/soul_3.png")};

    public static ResourceLocation emissive_chainblade_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/emissive/chainblade_1.png");
    public static ResourceLocation emissive_chainblade_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/emissive/chainblade_2.png");



    public ResourceLocation getTextureLocation(Entity context, byte skin){
        switch (skin)
        {
            case 0 -> {return item;}
            case 1 -> {return anime;}
            case 2 -> {return evil;}
            case 3 -> {return wooden;}
            case 4 -> {return stone;}
            case 5 -> {return aquamarine;}
            case 6 -> {return timekeeper;}
            case 7 -> {return diamond;}
            case 8 -> {return ancient;}
            case 9 -> {return grass;}
            case 10 -> {return gray_wagon;}
            case 11 -> {return chorus;}
            case 12 -> {return raging;}
            case 13 -> {return alluring;}
            case 14 -> {return khopesh;}
            case 19 -> {return bloodstained;}
            case 20 -> {return brilliance;}
            case 21 -> {return context.tickCount % 6 < 3 && PowerTypes.isUsingStand(context) ? chainblade_1 : chainblade_2;}
            case 22 -> {return chef;}
            case 23 -> {return serpent;}
            case 24 -> {return soul[context.tickCount/3 % 3 ];}

            case 15 -> {return cleaver;}
            case 16 -> {return illusory;}
            case 17 -> {return cleaver_sheathed;}
            case 18 -> {return illusory_sheathed;}

            default -> {return anime;}
        }
    }

    public ResourceLocation getEmissive(Entity context, byte skin) {
        if (skin == 21) {
            return context.tickCount % 6 < 3 && PowerTypes.isUsingStand(context) ? emissive_chainblade_1 : emissive_chainblade_2;
        } else if (skin == 20) {
            return brilliance;
        } else if (skin == 16) {
            return illusory;
        }
        return anime;
    }



    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context, (byte)0)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        render(context,partialTicks,poseStack,bufferSource,light,r,g,b,alpha,skin,true);
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin,boolean animations) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            if (LE.getUseItem().getItem() instanceof AnubisItem && animations && LE instanceof Player P) {
                this.animate(((IPlayerEntity)P).roundabout$getItemAnimation(),AnubisFirstPersonAnimations.ItemUnsheath,partialTicks,1F  );
            }

            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
            StandUser SU = (StandUser) context;
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.isUsingStand(context)) {
                if (skin == PowersAnubis.CHAINBLADE || skin == PowersAnubis.BRILLIANCE || skin == PowersAnubis.ILLUSORY ) {
                    VertexConsumer Consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getEmissive(context, skin)));
                    root().render(poseStack, Consumer, 15728880, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
                }
            }
        }
    }

    public void renderFirstPerson(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float partialTicks) {
        if (((IEntityAndData) entity).roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung()) return;
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (((TimeStop)entity.level()).CanTimeStopEntity(entity) || ClientUtil.checkIfGamePaused()){
            partialTicks = 0;
        }

        float alpha = 1F;
        byte skin = (byte) 0;

        StandUser user = (StandUser) entity;
        float ticks = user.roundabout$getAnubisVanishTicks();
        float alphaTicks = AnubisLayer.getTicks(entity,partialTicks);


        if (user.roundabout$isPossessed()) {
            skin = (byte)1;
           /* CompoundTag tag = entity.getMainHandItem().getTag();
            if (tag != null) {
                if (tag.getFloat("CustomModelData") == 2F) {
                    skin = (byte) 17;
                } else if (tag.getFloat("CustomModelData") == 3F) {
                    skin = (byte) 18;
                }
            } */
        } else if ( (user.roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.hasStandActive(entity) ) || (ticks != 0 && !entity.getMainHandItem().is(ModItems.ANUBIS_ITEM)   ) ) {
            alpha = alphaTicks;
            skin = user.roundabout$getStandSkin();
        } else if (entity.getMainHandItem().getItem() instanceof AnubisItem && !user.roundabout$getEffectiveCombatMode()) {
            CompoundTag tag = entity.getMainHandItem().getTag();
            if (tag != null) {
                if (tag.getFloat("CustomModelData") == 2F) {
                    skin = (byte) 17;
                } else if (tag.getFloat("CustomModelData") == 3F) {
                    skin = (byte) 18;
                }
            }
        }



        poseStack.translate(0,0,-1.27); // -forward
        poseStack.translate(0.85,0,0); // +left
        poseStack.translate(0,-0.3,0); //  +up
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,-15),0,0,0); // positive towards camera
        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,100),0,0,0); // around Y axis
        if (skin == (byte) 0 || skin == (byte)17 || skin == (byte)18) {
            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,180),0,0,0);
            poseStack.translate(0,0.35,0);
            poseStack.translate(0.1,0,0);
        }
        if (entity.getUseItem().getItem().equals(ModItems.ANUBIS_ITEM)) {
            poseStack.translate(0.1,0,0);
        } else {
            poseStack.scale(0.765F,0.765F,0.765F);
        }


        if (entity.getUseItem().getItem() instanceof AnubisItem) {
            if (entity instanceof Player P) {
                this.animate( ((IPlayerEntity)P).roundabout$getItemAnimation(),AnubisFirstPersonAnimations.Unsheath,partialTicks,1F);
            }
        } else if (((StandUser)entity).roundabout$getStandPowers() instanceof PowersAnubis PA) {
            AnimationDefinition anim = switch (user.roundabout$getStandAnimation()) {
                case PowerIndex.GUARD -> AnubisFirstPersonAnimations.Block;
                case PowerIndex.BARRAGE -> AnubisFirstPersonAnimations.BarrageDash;
                case PowerIndex.BARRAGE_CHARGE -> AnubisFirstPersonAnimations.BarrageCharge;
                case PowerIndex.BARRAGE_CHARGE_2 -> AnubisFirstPersonAnimations.Shieldbreak;
                case PowerIndex.BARRAGE_2 -> AnubisFirstPersonAnimations.ShieldbreakHit;
                case PowersAnubis.DOUBLE -> AnubisFirstPersonAnimations.DoubleCut;
                case PowersAnubis.UPPERCUT -> AnubisFirstPersonAnimations.Uppercut;
                case PowersAnubis.THRUST -> AnubisFirstPersonAnimations.Thrust;
                case PowerIndex.SNEAK_ATTACK_CHARGE -> AnubisFirstPersonAnimations.Pogo;

                default -> null;
            };
            if (anim == null) {
                switch (user.roundabout$getStandAnimation()) {
                    case PowerIndex.ATTACK -> {
                        if (PA.activePowerPhase == 1) {
                            anim = AnubisFirstPersonAnimations.Attack;
                        } else {
                            anim = AnubisFirstPersonAnimations.Attack2;
                        }
                    }
                    case PowerIndex.SNEAK_ATTACK -> {
                        if (PA.activePowerPhase == 1) {
                            anim = AnubisFirstPersonAnimations.SneakAttack;
                        } else {
                            anim = AnubisFirstPersonAnimations.SneakAttack2;
                        }
                    }
                }
            }

            if (anim != null) {
                user.roundabout$getWornStandAnimation().startIfStopped(entity.tickCount);
                this.animate(user.roundabout$getWornStandAnimation(),anim,partialTicks,1F);
            } else {
                user.roundabout$getWornStandAnimation().stop();
            }
        }


        if (entity.getMainArm() == HumanoidArm.LEFT) { // I need to rotate it slightly inwards in order to make it better
            poseStack.scale(1,1,-1);
            poseStack.translate(0.1,0,2);
            if (entity.getUseItem().getItem() instanceof AnubisItem) { // don't ask me why I need to do this
                poseStack.translate(0,0,-3.7);
            }
        }

        if (PowerTypes.hasStandActive(entity) && ((StandUser)entity).roundabout$getStandPowers() instanceof PowersAnubis PA) {
            ClientConfig.OpacitySettings opacitySettings = ConfigManager.getClientConfig().opacitySettings;
            alpha *= (PA.getActivePower() == PowerIndex.NONE ? opacitySettings.opacityOfStand : opacitySettings.opacityWhileAttacking)/100;

        }
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity, skin )));
        root().render(poseStack,consumer,packedLight,OverlayTexture.NO_OVERLAY,1,1,1,alpha);
        StandUser SU = (StandUser) entity;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.isUsingStand(entity)) {
            if (skin == PowersAnubis.CHAINBLADE || skin == PowersAnubis.BRILLIANCE || skin == PowersAnubis.ILLUSORY ) {
                VertexConsumer Consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getEmissive(entity, skin)));
                root().render(poseStack, Consumer, 15728880, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
            }
        }
    }
}

