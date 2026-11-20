package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.SavedSecond;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PowersGER extends PowersGoldExperience {
    public PowersGER(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGER(entity);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GOLD_EXPERIENCE_REQUIEM.create(this.getSelf().level());
    }

    public boolean canUseRequiem() {
        return false;
    }

    public boolean isRequiemStand() {
        return true;
    }

    ItemStack originalDisc;

    public boolean hasNormalGEDisc() {
        return originalDisc != null && !originalDisc.isEmpty();
    }

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

    @Override
    public boolean replaceHudActively() {
        return hasNormalGEDisc();
    }

    public void goBackToGE(){
        setPowerNone();

        ((StandUser) self).roundabout$setStandDisc(originalDisc);
        ((StandUser) self).roundabout$getStandPowers().onStandSwitchInto();
        if (((StandUser) self).roundabout$getStandPowers() instanceof PowersGoldExperience GER) {
            GER.idlePos = idlePos;
            GER.standSkin = standSkin;
            GER.resolvePoints = resolvePoints;

            GER.saveDiscAndSync();
        }

        if (((StandUser) self).roundabout$getStand() != null) {
            ((StandUser) self).roundabout$getStand().discard();
            ((StandUser) self).roundabout$setStand(null);
        }
        ((StandUser) self).roundabout$summonStand(self.level(), true, true);
    }

    public void tickPower() {
        super.tickPower();
        if (hasNormalGEDisc()) {
            resolvePoints -= 0.25f;
            if (resolvePoints <= 0f) {
                goBackToGE();
            }
        }
    }

    @Override
    public boolean interceptIncomingHarm(DamageSource source, float amount) {
        Entity culprit = source.getDirectEntity();
        if (culprit == null) {
            source.getEntity();
        }
        if (culprit != null) {
            IEntityAndData iData = (IEntityAndData) culprit;
            SavedSecond lastSecond = iData.roundabout$getLastSavedSecond();
            if (lastSecond != null) {
                lastSecond.loadTime(culprit);
            }
            if (culprit instanceof LivingEntity LE && LE.isUsingItem()){
                LE.stopUsingItem();
            }
            if (culprit instanceof Mob mb && !MainUtil.isBossMob(mb)){
                mb.getNavigation().stop();
                if (!MainUtil.blockConfusionTicks(mb)) {
                    ((IMob) mb).roundabout$setConfusionTicks(7);
                }
            }

            return true;
        }

        return super.interceptIncomingHarm(source, amount);
    }
}