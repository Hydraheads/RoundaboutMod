package net.hydra.jojomod.entity.visages;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class StandUsingNPC extends JojoNPC{
    public StandUsingNPC(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }

    public StandDiscItem getDisc(){
        return null;
    }

    public boolean isInitialized = false;

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("isInitialized",isInitialized);
    }
    public void applySkin(){
    }
    public void rollStand(){
        if (!isInitialized){
            ItemStack stack = ((StandUser)this).roundabout$getStandDisc();
            if (stack == null || stack.isEmpty()){
                if (getDisc() != null){
                    ((StandUser)this).roundabout$setStandDisc(getDisc().getDefaultInstance().copy());
                    getDisc().generateStandPowers(this);
                    applySkin();
                }
            }
            isInitialized = true;
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        super.readAdditionalSaveData($$0);
        isInitialized = $$0.getBoolean("isInitialized");
        rollStand();
    }

    @Override
    public boolean canSummonStandThroughFightOrFlightActive(){
        return true;
    }

}
