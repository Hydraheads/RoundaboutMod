package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;


public class IceTwisterEntity extends WhiteAlbumFreezingEntity {
    public static final float height = 3f;
    public static final float width = 3f;


    public IceTwisterEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    public IceTwisterEntity(Level $$2, Vec3 pos) {
        this(ModEntities.COLD_TWISTER, pos.x(), pos.y(), pos.z(), $$2);
    }

    protected IceTwisterEntity(EntityType<? extends Entity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    public void tick(){
        if (!level().isClientSide()){
            if (lifeSpan > -1){
                lifeSpan--;
                if (lifeSpan == 0){
                    if (level() instanceof ServerLevel sl) {
                        sl.sendParticles(ModParticles.VACUUM,
                                this.getX(),
                                this.getY()+1,
                                this.getZ(),
                                15, 0.3, 0.4, 0.3, 0.1F);

                    }
                    discard();
                }
            } else {
                discard();
            }

            AABB wallBox = this.getBoundingBox();

            for (LivingEntity mob : level().getEntitiesOfClass(
                    LivingEntity.class,
                    wallBox.inflate(0.1))) {

                if (mob.getBoundingBox().intersects(wallBox)) {
                    if (MainUtil.canFreeze(mob)) {
                        if (mob instanceof Player pl){
                            if (this.tickCount%2==0){
                                HeatUtil.addHeat(mob,-1);
                            }
                        } else {
                            if (this.tickCount%2==0 || HeatUtil.getHeat(mob) > -33) {
                                HeatUtil.addHeat(mob, -1);
                            }
                        }
                    }
                    if (this.tickCount <5){
                        if (!mob.onGround()){
                            MainUtil.takeLiteralUnresistableKnockbackWithY(mob,0,-0.5F,0);
                        }
                    }
                }
            }

            if (tickCount > 6) {
                int range = 0;
                if (tickCount > 10) {
                    range = 1;
                }
                for (int y = 0; y < 3; y++) {
                    for (int x = -range; x <= range; x++) {
                        for (int z = -range; z <= range; z++) {
                            BlockPos targetPos = getOnPos().offset(x, y, z);
                            BlockState iceState = ModBlocks.STICKY_ICE.defaultBlockState();

                            if (canFreeze(targetPos)
                                    && iceState.canSurvive(level(), targetPos)) {
                                level().setBlockAndUpdate(targetPos, iceState);
                                level().scheduleTick(targetPos, ModBlocks.STICKY_ICE, Mth.nextInt(level().getRandom(), 141, 145));
                            }
                            // placement logic
                        }
                    }
                }
            }

        } else {
            if (renderCold < 10) {
                renderCold++;
            }
            if (!started && !((TimeStop) level()).inTimeStopRange(this)) {
                started = true;
                ClientUtil.handleTwisterSound(this);
            }
        }
        super.tick();
    }

}
