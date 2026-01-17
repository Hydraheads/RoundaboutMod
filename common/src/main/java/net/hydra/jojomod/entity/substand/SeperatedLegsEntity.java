package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.List;

public class SeperatedLegsEntity extends StandEntity {
    public final AnimationState floating = new AnimationState();

    public static final byte
        IDLE=11;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == IDLE) {
                this.floating.startIfStopped(this.tickCount);
            } else {
                this.floating.stop();
            }

        }
    }

    public SeperatedLegsEntity(EntityType<SeperatedLegsEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
    public LivingEntity lastcontact;
    public int StartupTicks = 10;


    @Override
    public void tick() {
        this.setFadeOut((byte)1);
        this.setAnimation(IDLE);
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            if(StartupTicks==0){
                if((!(this.getUser() == null) && (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersGreenDay))){
                    this.lookAt(EntityAnchorArgument.Anchor.EYES,User.getEyePosition());
                    this.setDeltaMovement(this.getLookAngle().multiply(0.2,0.2,0.2));
                }else{
                    this.discard();
                }
            }else{
                StartupTicks--;
            }

            for(int i = 0; i < 4; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
                double randY = Roundabout.RANDOM.nextDouble(-0.1, 0.1);
                double randZ = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
                ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD,
                        this.getX()+randX,
                        this.getY() + 1.2 +randY ,
                        this.getZ() + randZ,
                        1,0,0,0,0);
            }
            List<Entity> damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),0.6,1,0.6);
            for(int j = 0;j<damages.size();j++){
                Entity entity = damages.get(j);
                if(!(entity.equals((Object)this) ||entity.equals((Object)user))) {
                    entity.hurt(ModDamageTypes.of(level(), ModDamageTypes.KICKED, this, user), 5);
                }
            }

        }

        super.tick();
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

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }


}
