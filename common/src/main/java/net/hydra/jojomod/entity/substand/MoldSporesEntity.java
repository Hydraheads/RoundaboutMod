package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.justice.JusticeCreeper;
import net.hydra.jojomod.mixin.justice.JusticeZombie;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class MoldSporesEntity extends StandEntity {
    public float range = ClientNetworking.getAppropriateConfig().greenDaySettings.moldDefaultRange;
    public int lifetime = 600;
    public MoldSporesEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public void tick() {
        if (lifetime <1){
            this.discard();
        }else{
            lifetime--;
        }
        this.setFadeOut((byte) 1);
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            tickeffect();
            if (user == null) {
                spawnAtLocation(this.getMainHandItem());
                this.discard();
            }
            this.setDeltaMovement(0,-0.2,0);
            if (!onGround()) {
                range += 0.07 * (ClientNetworking.getAppropriateConfig().greenDaySettings.moldGrowthRate / 100);
                //this.setDeltaMovement(0,-0.4,0);
            }
                ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD_DUST,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (((int) range ^ 3) * 1) + 1, range, range, range, 0.005);

            ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.76F, 1.0F, 0.9F), 2f),
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    (int) (((int) range ^ 3) * 0.25) +1, range, range, range, 0.005);



            }
            super.tick();


    }

    public void tickeffect(){
        List<Entity> damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),range,range,range);
        for(int j = 0;j<damages.size();j++) {
            Entity entity = damages.get(j);

            //boolean down = previousYpos > entity.getY() + 0.1;

            boolean isStand = (entity instanceof StandEntity);
            if(entity instanceof LivingEntity) {
                if (!((StandUser) entity).roundabout$getStandPowers().isStoppingTime()
                        && !((StandUser) entity).roundabout$isBubbleEncased()
                        && !isStand
                        && ((StandUser) entity).GoingDown()
                        && !(entity instanceof FallenMob)
                        && ((StandUser) entity).getJumpImmunityTicks() < 1
                        && !entity.equals(User)) {
                    if(!((PowersGreenDay)((StandUser)User).roundabout$getStandPowers()).allies.contains(entity.getStringUUID())) {

                        double width = entity.getBbWidth() / 2;
                        double height = entity.getBbHeight() / 2;
                        ((ServerLevel) level()).sendParticles(ModParticles.MOLD
                                , entity.getX(),
                                (entity.getY() + height / 2),
                                entity.getZ(),
                                13, width, height, width, 0)
                        ;


                        ((StandUser) entity).DoMoldTick();
                        ((StandUser)User).roundabout$getStandPowers().addEXP(1);
                    }
                }
            }







        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource $$0) {
        return true;
    }


    @Override
    public boolean fireImmune() {
        return true;
    }


    /**USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.*/
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SeperatedLegsEntity.class,
            EntityDataSerializers.INT);


    @Override
    public boolean hasNoPhysics() {
        return false;
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }


    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return false;
    }

    @Override
    protected boolean isHorizontalCollisionMinor(Vec3 $$0) {
        return super.isHorizontalCollisionMinor($$0);
    }
}
