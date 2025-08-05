package net.hydra.jojomod.mixin.cinderella;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.ICreeper;
import net.hydra.jojomod.entity.goals.AvoidEntityWhenFacelessGoal;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value= Creeper.class)
public abstract class CinderellaCreeper extends Monster {

    /**Modded effects such as faceless marked as special in mainutil (dont emit standard particles)
     * will not spawn from creeper explosions if the creeper has the effects*/
    @ModifyVariable(method = "spawnLingeringCloud()V", at = @At("STORE"), ordinal = 0)
    protected Collection<MobEffectInstance> roundabout$SpawnLingeringCloud(Collection<MobEffectInstance> col) {
        if (!col.isEmpty()) {
            Collection<MobEffectInstance> col2 = Lists.newArrayList();
            for (MobEffectInstance $$2 : col) {
                if (!MainUtil.isSpecialEffect($$2.getEffect())){
                    col2.add($$2);
                }
            }
            return col2;
        }
        return col;
    }

    /**Creepers start becoming shy when faceless*/
    @Inject(method = "registerGoals()V", at = @At(value = "HEAD"))
    protected void roundabout$registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new AvoidEntityWhenFacelessGoal<>(this, Player.class, 6.0F, 1.0, 1.2));
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    protected CinderellaCreeper(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

}
