package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class FogTrapBlockEntity extends BlockEntity {
    private UUID owner;
    private String name;
    private int range = 20;

    public FogTrapBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlocks.FOG_TRAP_BLOCK_ENTITY, $$1, $$2);
    }

    public UUID getOwner(){
        return owner;
    }

    public String getName(){
        return name;
    }

    public int getRange(){return range;}

    public void setValues(Entity thisowner, String thisname){
        if(owner == null && name == null){
            owner = thisowner.getUUID();
            name = thisname;

            if(thisowner instanceof StandUser se && se.roundabout$getStandPowers() instanceof PowersJustice justice){
                range = justice.fogTrapRange;
            }

        }
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        if(owner != null) {
            tag.putUUID("owner", owner);
        }
        if(name != null) {
            tag.putString("name", name);
        }
        tag.putInt("range",range);

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("owner")) {
            owner = tag.getUUID("owner");
        }
        if(tag.contains("name")) {
            name = tag.getString("name");
        }
        if(tag.contains("range")){
            range = tag.getInt("range");
        }
    }
}
