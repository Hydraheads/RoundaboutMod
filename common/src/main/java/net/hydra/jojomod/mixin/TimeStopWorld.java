package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(Level.class)
public class TimeStopWorld implements TimeStop {
    /**Stropping time- uses a list of time stopping players.
     * Use that list to determine if a block or entity SHOULD be ticked.*/
    private ImmutableList<LivingEntity> timeStoppingEntities = ImmutableList.of();

    /**Adds an entity to the list of time stopping entities*/
    @Override
    public void addTimeStoppingEntity(LivingEntity $$0) {
        if (this.timeStoppingEntities.isEmpty()) {
            this.timeStoppingEntities = ImmutableList.of($$0);
        } else {
            List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
            $$1.add($$0);
            this.timeStoppingEntities = ImmutableList.copyOf($$1);
        }
    }

    /**Adds an entity to the list of time stopping entities*/
    @Override
    public void removeTimeStoppingEntity(LivingEntity $$0) {
        if (!this.timeStoppingEntities.isEmpty()) {
            List<LivingEntity> $$1 = Lists.newArrayList(this.timeStoppingEntities);
            for (int i = this.timeStoppingEntities.size() - 1; i >= 0; --i) {
                if (this.timeStoppingEntities.get(i).getId() == $$0.getId()){
                    $$1.remove(i);
                }
            }
            this.timeStoppingEntities = ImmutableList.copyOf($$1);
        }
    }

    @Override
    public ImmutableList<LivingEntity> getTimeStoppingEntities() {
        return timeStoppingEntities;
    }

    @Override
    public boolean inTimeStopRange(Vec3i pos){
        return false;
    }
    @Override
    public boolean inTimeStopRange(Entity entity){
        return inTimeStopRange(new Vec3i((int) entity.getX(),
                (int) entity.getY(),
                (int) entity.getZ()));
    }

    /**Code this to check if they are able to use powers mid TS*/
    @Override
    public boolean CanTimeStopEntity(Entity entity){
        if (entity instanceof Player && ((Player) entity).isCreative() || entity.isSpectator()) {
            return false;
        } else if (entity instanceof Warden){
            return false;
        } else if (entity instanceof LivingEntity){
            if (isTimeStoppingEntity((LivingEntity) entity)){
                return false;
            } else {
                return inTimeStopRange(entity);
            }
        } else {
            return inTimeStopRange(entity);
        }
    }

    /**Code this to check if they are in the list*/
    @Override
    public boolean isTimeStoppingEntity(LivingEntity entity){
        if (!this.timeStoppingEntities.isEmpty()) {
            for (int i = this.timeStoppingEntities.size() - 1; i >= 0; --i) {
               if (this.timeStoppingEntities.get(i).getId() == entity.getId()){
                   return true;
               }
            }
        }
        return false;
    }

    @ModifyVariable(method = "tickBlockEntities()V", at = @At(value = "STORE"))
    private Iterator<TickingBlockEntity> roundaboutTickTS(
            Iterator<TickingBlockEntity> $$1) {
        while ($$1.hasNext()) {
            TickingBlockEntity $$2 = $$1.next();
            if ($$2.isRemoved()) {
                $$1.remove();
            } else if (inTimeStopRange($$2.getPos())){
                $$1.remove();
            }
        }
        return $$1;
    }
}
