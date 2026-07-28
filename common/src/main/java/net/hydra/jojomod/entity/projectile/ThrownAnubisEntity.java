package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownAnubisEntity extends AbstractHurtingProjectile {

    public ThrownAnubisEntity(LivingEntity living, Level $$1) {
        super(ModEntities.THROWN_ANUBIS,$$1);
    }
    public ThrownAnubisEntity(EntityType<ThrownAnubisEntity> thrownAnubisEntityEntityType, Level level) {
        super(thrownAnubisEntityEntityType,level);
    }


    @Override
    protected void defineSynchedData() {}

    private int lifetime = 200;

    @Override
    public void tick() {
        this.noPhysics = true;
        Vec3 delta = this.getDeltaMovement();
        this.setDeltaMovement(delta.x, delta.y - 0.05, delta.z);
        super.tick();

        if (!this.level().isClientSide()) {
            if ((this.tickCount + 2) % 3 == 0) {
                ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                        this.getX(), this.getY(), this.getZ(),
                        0, 0, 0, 0, 0);
            }
        }
        this.lifetime--;
        if (this.lifetime <= 0) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {}

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        this.noPhysics = false;
        Entity ent = $$0.getEntity();

        if (this.getOwner() != null) {
            if (ent.equals(this.getOwner()) || ent instanceof TamableAnimal TA && ent.equals(TA.getOwner())) {
                return;
            }
        }

        if (ent instanceof Cow C && !((StandUser)C).roundabout$hasAStand() ) {

            if (!this.level().isClientSide()) {
                this.level().playSound(null,this.blockPosition(), ModSounds.ANUBIS_EXTRA_EVENT, SoundSource.PLAYERS,10F,1F);

                StandArrowItem.grantStand(ModItems.STAND_DISC_ANUBIS.getDefaultInstance(), C);
                C.hurt(ModDamageTypes.of(C.level(), ModDamageTypes.STAND), 1);
                this.discard();
                return;
            }
        } else {
            if (ent.hurt(ModDamageTypes.of(this.level(),ModDamageTypes.ANUBIS_SPIN,this,this.getOwner()),MainUtil.getReducedDamage(ent) ? 2 : 25)) {


                if (MainUtil.getMobBleed(ent)) {
                    MainUtil.makeBleed(ent, 1, 300, this.getOwner());
                }
                if (ent instanceof LivingEntity LE) {
                    if (!LE.isBlocking()) {
                        ent.hurt(ModDamageTypes.of(this.level(),ModDamageTypes.ANUBIS_SPIN,this,this.getOwner()),5);
                    }
                    LE.addEffect(new MobEffectInstance(ModEffects.CRIPPLED,200,1));
                }


                if (this.getOwner() != null && ((StandUser)this.getOwner()).roundabout$getStandPowers() instanceof PowersAnubis PA) {
                    PA.addEXP(3);
                }
            } else if (ent instanceof LivingEntity LE && LE.isBlocking()) {
                MainUtil.knockShieldPlusStand(LE,200);
            }
        }
        super.onHitEntity($$0);
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {return false;}
    @Override
    protected boolean shouldBurn() {return false;}
}
