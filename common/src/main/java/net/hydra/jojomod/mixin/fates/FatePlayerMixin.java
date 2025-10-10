package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class FatePlayerMixin extends LivingEntity implements IFatePlayer {

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$Tick(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            rdbt$tickThroughVampireChange();
        }
    }
    /**tick through transformation sequences*/

    @Unique
    public int rdbt$vampireTransformation = -1;

    @Unique
    public void rdbt$startVampireTransformation(){
        //starts the animation
        rdbt$vampireTransformation = 0;
        ((StandUser)this).roundabout$setDazed((byte) 120);
        level().playSound(null, this, ModSounds.STONE_MASK_ACTIVATE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    @Unique
    public void rdbt$tickThroughVampireChange(){
        if (rdbt$vampireTransformation >= 0){
            rdbt$vampireTransformation++;
            if (rdbt$vampireTransformation > 120){
                rdbt$vampireTransformation = -1;
                if (FateTypes.isHuman(this)){
                    MainUtil.popOffStoneMask(this);
                    FateTypes.setVampire(this);
                }
                //12 is the id of vampire stone mask transformation animation in Poses.java
                if (((IPlayerEntity)this).roundabout$GetPoseEmote() == 12){
                    ((IPlayerEntity)this).roundabout$SetPoseEmote((byte)0);
                }
            } else if (rdbt$vampireTransformation > 13){
                if (rdbt$vampireTransformation == 14){
                    //12 is the id of vampire stone mask transformation animation in Poses.java
                    ((IPlayerEntity)this).roundabout$SetPoseEmote((byte)12);
                }
                rdbt$scatterTransformationParticles();
            }
        }
    }
    @Unique
    public void rdbt$scatterTransformationParticles(){
        for (int i = 0; i < 7; ++i) {
            double randomX = (Math.random() * 0.5) - 0.25;
            double randomY = (Math.random() * 0.5) - 0.25;
            double randomZ = (Math.random() * 0.5) - 0.25;

            Vec3 vecpos = getEyePosition();
            ((ServerLevel) this.level()).sendParticles(ModParticles.BLUE_SPARKLE,
                    vecpos.x,vecpos.y,vecpos.z,
                    0, randomX, randomY, randomZ, 3.5);
        }
    }

    protected FatePlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
}
