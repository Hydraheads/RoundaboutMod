package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class D4CEntity extends StandEntity {
    public D4CEntity(EntityType<? extends Mob> entityType, Level world) { super(entityType, world); }

    @Override public Component getSkinName(byte skinId) { return Component.translatable("skins.roundabout.d4c.base"); }
}
