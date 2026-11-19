package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        if (originalDisc != null) {
            CompoundTag compoundtag = new CompoundTag();

            if (self instanceof Player PE && !(originalDisc.getItem() instanceof MaxStandDiscItem)) {
                IPlayerEntity IPE = ((IPlayerEntity) PE);
                if (originalDisc.getItem() instanceof StandDiscItem SD && !SD.standPowers.isSecondaryStand()) {
                    originalDisc.getOrCreateTagElement("Memory").putByte("Level",
                            (byte) Math.max(IPE.roundabout$getStandLevel() - 1, 0));
                    originalDisc.getOrCreateTagElement("Memory").putInt("Experience", IPE.roundabout$getStandExp());
                    originalDisc.getOrCreateTagElement("Memory").putBoolean("BonusSkin", IPE.roundabout$getUnlockedBonusSkin());
                }
            }

            tag.put("roundabout.originalDisc", originalDisc.save(compoundtag));
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