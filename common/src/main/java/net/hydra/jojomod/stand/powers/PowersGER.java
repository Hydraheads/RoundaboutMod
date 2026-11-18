package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PowersGER extends PowersGoldExperience{
    public PowersGER(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersGER(entity);}

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GOLD_EXPERIENCE_REQUIEM.create(this.getSelf().level());
    }

    public boolean canUseRequiem() { return false; }
    public boolean isRequiemStand() {return true;}

    ItemStack originalDisc;

    public boolean hasNormalGEDisc() { return originalDisc != null && !originalDisc.isEmpty();}

    public void setOriginalDisc(ItemStack disc) {
        originalDisc = disc;
        saveDiscAndSync();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        CompoundTag compoundtag = new CompoundTag();
        if (originalDisc != null) {
            tag.put("roundabout.originalDisc",
                    MainUtil.saveToDiscData(self, originalDisc).save(compoundtag));
        }
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("roundabout.originalDisc", 10)) {
            CompoundTag compoundtag = tag.getCompound("roundabout.originalDisc");
            originalDisc = ItemStack.of(compoundtag);
        }

        super.readAdditionalSaveData(tag);
    }

}