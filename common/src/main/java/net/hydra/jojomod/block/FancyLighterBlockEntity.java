package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.UUID;

public class FancyLighterBlockEntity extends BlockEntity {
    public FancyLighterBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlocks.FANCY_LIGHTER_BLOCK_ENTITY, $$1, $$2);
    }

    /*This is the integer in which the owner's id gets saved*/
    private UUID owner;
    private String name;

    public UUID getOwner(){
        return owner;
    }
    public String getName(){
        return name;
    }


    public void setValues(LivingEntity thisowner, String thisname){
        if(owner == null && name == null){
            owner = thisowner.getUUID();
            name = thisname;

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
    }


    public ItemStack getItem() {
        ItemStack $$0 = new ItemStack(ModItems.FANCY_LIGHTER);
        /*if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
            CompoundTag $$1 = new CompoundTag();
            $$1.put("Patterns", this.itemPatterns.copy());
            BlockItem.setBlockEntityData($$0, this.getType(), $$1);
        }

        if (this.name != null) {
            $$0.setHoverName(this.name);
        }*/

        return $$0;
    }

}
