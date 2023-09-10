package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class StandModel extends GeoModel<StandEntity> {
    @Override
    public Identifier getModelResource(StandEntity animatable) {
        return new Identifier(RoundaboutMod.MOD_ID,"geo/star_platinum.json");
    }

    @Override
    public Identifier getTextureResource(StandEntity animatable) {
        return new Identifier(RoundaboutMod.MOD_ID,"textures/stand/star_platinum.png");
    }

    @Override
    public Identifier getAnimationResource(StandEntity animatable) {
        return new Identifier(RoundaboutMod.MOD_ID,"animations/star_platinum.json");
    }
}
