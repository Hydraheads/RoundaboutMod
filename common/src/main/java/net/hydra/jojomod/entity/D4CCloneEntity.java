package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class D4CCloneEntity extends CloneEntity {
    public D4CCloneEntity(EntityType<? extends PathfinderMob> entity, Level world) {
        super(entity, world);
    }

    private static final EntityDataAccessor<Boolean> SELECTED =
            SynchedEntityData.defineId(D4CCloneEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {

        }
        else {
            if (getMainHandItem().is(ItemTags.PICKAXES))
            {

            }

            Minecraft client = Minecraft.getInstance();

            if (client.player == null)
                return;

            StandUser localPlayer = ((StandUser)client.player);
            if (localPlayer.roundabout$getStand() instanceof D4CEntity)
            {
                if (this.player == client.player)
                {
                    PowersD4C powers = (PowersD4C) localPlayer.roundabout$getStandPowers();
                    this.setSelected(powers.targetingClone == this);
                }
            }
        }

        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SELECTED, false);
    }

    private Vector3f sizeOffset = new Vector3f(0.f, 0.f, 0.f);
    public void setSizeOffset(Vector3f value) { this.sizeOffset = value; }
    public Vector3f getSizeOffset() { return this.sizeOffset; }

    public void setSelected(boolean value) { this.entityData.set(SELECTED, value); }
    public boolean isSelected() { return this.getEntityData().get(SELECTED); }
}