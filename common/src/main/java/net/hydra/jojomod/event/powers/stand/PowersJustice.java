package net.hydra.jojomod.event.powers.stand;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.presets.DashPreset;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PowersJustice extends DashPreset {
    public PowersJustice(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersJustice(entity);
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public boolean interceptAttack(){
        return false;
    }
    @Override
    public boolean interceptGuard(){
        return false;
    }

    @Override
    public boolean isMiningStand() {
        return false;
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.JUSTICE.create(this.getSelf().level());
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        byte bt = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            if (bt == JusticeEntity.FLAMED){
                return ModSounds.SUMMON_JUSTICE_2_EVENT;
            }
            return ModSounds.SUMMON_JUSTICE_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_1, PowerIndex.SKILL_EXTRA_2);

        setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_PHASE_GRAB, PowerIndex.SKILL_2);

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.NONE);

        setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
    }
    @Override
    public float getBonusAttackSpeed() {
        return 1.3F;
    }

    @Override
    public float getBonusPassiveMiningSpeed(){
        return 1.3F;
    }

    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(TheWorldEntity.PART_3_SKIN);
        $$1.add(JusticeEntity.SKELETON_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(JusticeEntity.MANGA_SKIN);
            } if (Level > 2 || bypass){
                $$1.add(JusticeEntity.OVA_SKIN);
            } if (Level > 3 || bypass){
                $$1.add(JusticeEntity.STRAY_SKIN);
                $$1.add(JusticeEntity.BOGGED);
            } if (Level > 4 || bypass){
                $$1.add(JusticeEntity.TWILIGHT);
            } if (Level > 5 || bypass){
                $$1.add(JusticeEntity.WITHER);
            } if (Level > 6 || bypass){
                $$1.add(JusticeEntity.TAROT);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(JusticeEntity.FLAMED);
            }
        }
        return $$1;
    }
}
