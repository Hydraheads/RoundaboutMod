package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.client.models.layers.PreRenderEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class SurvivorEntity extends MultipleTypeStand implements PreRenderEntity {
    /**
     * Initialize Stands
     *
     * @param entityType
     * @param world
     */
    public SurvivorEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override

    public boolean validatePowers(LivingEntity user){
        return (((StandUser)user).roundabout$getStandPowers() instanceof PowersSurvivor);
    }
    public boolean hasNoPhysics(){
        return false;
    }



    @Override
    public boolean needsActive(){
        return false;
    }

    /**it instantly rotates towards its server rotation instead of awkwardly spinning for a few seconds*/
    @Override
    public boolean preRender(Entity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6) {
        float lerpYRot = (float) ((ILivingEntityAccess)ent).roundabout$getLerpYRot();
        ent.yRotO = lerpYRot;
        ent.setYRot(lerpYRot);
        ent.setYBodyRot(lerpYRot);
        ent.setYHeadRot(lerpYRot);
        return false;
    }
}
