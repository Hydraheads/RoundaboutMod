package net.hydra.jojomod.entity;


import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ModEntities {
    /** Entities are referenced in these files, but forge and fabric need to update
     * these with their registration data separately.*/
    public static EntityType<TerrierEntity> TERRIER_DOG;
    public static EntityType<TheWorldEntity> THE_WORLD;
    public static EntityType<StarPlatinumEntity> STAR_PLATINUM;
    public static EntityType<JusticeEntity> JUSTICE;
    public static EntityType<JusticePirateEntity> JUSTICE_PIRATE;
    public static EntityType<StarPlatinumBaseballEntity> STAR_PLATINUM_BASEBALL;
    public static EntityType<HarpoonEntity> THROWN_HARPOON;
    public static EntityType<KnifeEntity> THROWN_KNIFE;
    public static EntityType<MatchEntity> THROWN_MATCH;

    public static EntityType<GasolineCanEntity> GASOLINE_CAN;

    public static EntityType<GasolineSplatterEntity> GASOLINE_SPLATTER;

    public static EntityType<StandArrowEntity> STAND_ARROW;

    public static EntityType<ThrownObjectEntity> THROWN_OBJECT;
    public static final ResourceLocation HARPOON_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_harpoon.png");
    public static final ResourceLocation KNIFE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_knife.png");
    public static final ResourceLocation GASOLINE_CAN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/thrown_gasoline_can.png");

}
