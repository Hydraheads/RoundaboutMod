package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MagiciansRedEntity extends StandEntity {
    public MagiciansRedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        return Component.translatable(  "skins.roundabout.magicians_red.base");
    }
}
