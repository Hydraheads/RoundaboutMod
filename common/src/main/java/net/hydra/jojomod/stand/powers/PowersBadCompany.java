package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.badCompanyTroops.*;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.List;

public class PowersBadCompany extends NewDashPreset {
    @Override public boolean isWip(){return true;}
    @Override public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.paused").withStyle(ChatFormatting.AQUA);}
    @Override public Component ifWipListDev(){ return Component.literal("DOGael Arts").withStyle(ChatFormatting.AQUA);}

    public PowersBadCompany(LivingEntity self) { super(self); }
    @Override public StandPowers generateStandPowers(LivingEntity entity) { return new PowersBadCompany(entity); }

    public boolean canSummonStandAsEntity(){
        return false;
    }

    public Component getPosName(byte posID){ return Component.empty(); }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }

    public static final byte
        ANIME_SKIN = 1,

        TANK_SUMMON = 50;

    @Override
    public List<Byte> getSkinList() {
        List<Byte> list = Lists.newArrayList();
        list.add(ANIME_SKIN);
        return list;
    }

    public static final int
        TROOP = 0,
        BUNDLE = 1,
        HELICOPTER = 2,
        TANK = 3;

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.BAD_COMPANY_TROOP_RECALL, PowerIndex.NO_CD);
        }else{
            setSkillIcon(context, x, y, 1, StandIcons.BAD_COMPANY_TROOP_DROP, PowerIndex.SKILL_1);
        }

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        setSkillIcon(context, x, y, 4, StandIcons.BAD_COMPANY_PILOT_MODE, PowerIndex.NO_CD);
        //setSkillIcon(context, x, y, 4, StandIcons.BAD_COMPANY_PILOT_MODE_EXIT, PowerIndex.NO_CD);

        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> {
                openTroopSelector();
            }
            case SKILL_3_NORMAL, SKILL_3_GUARD, SKILL_3_CROUCH, SKILL_3_CROUCH_GUARD -> {
                dash();
            }
        }
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.BC_TROOP_SUMMON_EVENT;
        }else if (soundChoice == SoundIndex.SUMMON_SOUND_2) {
            return ModSounds.BC_BUNDLE_SUMMON_EVENT;
        }else if (soundChoice == SoundIndex.SUMMON_SOUND_4) {
            return ModSounds.BC_HELICOPTER_SUMMON_EVENT;
        }else if (soundChoice == TANK_SUMMON) {
            return ModSounds.BC_TANK_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public float getSoundVolumeFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return 0.6f;
        }else if (soundChoice == SoundIndex.SUMMON_SOUND_2) {
            return 0.6f;
        }

        return super.getSoundVolumeFromByte(soundChoice);
    }

    HashSet<BCTroop> activeTroops = new HashSet<>();
    public void initActiveTroops() {
        if (activeTroops == null) {
            activeTroops = new HashSet<>();
        }
    }

    private void openTroopSelector() {
        ClientUtil.openTroopSelectionScreen();
    }

    public void selectedTroop(int id) {
        Roundabout.LOGGER.info("Selected Troop ID: {}", id);
        tryIntPowerPacket(PowerIndex.POWER_1, id);
        /*if (id == TROOP) {
           Roundabout.LOGGER.info("Selected Troop!");
        }else if (id == BUNDLE) {
           tryIntPowerPacket(PowerIndex.POWER_1, id);
           Roundabout.LOGGER.info("Selected Bundle Troop!");
        }else if (id == HELICOPTER) {
           Roundabout.LOGGER.info("Selected Helicopter!");
        }else if (id == TANK) {
           Roundabout.LOGGER.info("Selected Tank!");
        }else {
           Roundabout.LOGGER.info("Unknow ID: {}", id);
        }*/
    }

    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_1) {
            Roundabout.LOGGER.info("received package on " + isClient());
            if (chargeTime == TROOP) {
                spawnSoldier(false);
            }else if (chargeTime == BUNDLE) {
                spawnSoldier(true);
            }else if (chargeTime == HELICOPTER) {
                playStandUserOnlySoundsIfNearby(SoundIndex.SUMMON_SOUND_4, 8, false, false);
            }else if (chargeTime == TANK) {
                playStandUserOnlySoundsIfNearby(TANK_SUMMON, 8, false, false);
            }
        }

        return super.tryIntPower(move, forced, chargeTime);
    }

    public void spawnSoldier(boolean bundle) {
        if (isClient()) { return; }

        initActiveTroops();
        BCSoldier soldier = ModEntities.BAD_COMPANY_SOLDIER.create(self.level());
        if (soldier != null) {
            soldier.setUser(self);
            soldier.absMoveTo(
                    self.getPosition(1).x,
                    self.getPosition(1).y,
                    self.getPosition(1).z
            );
            soldier.setBundle(bundle);
            self.level().addFreshEntity(soldier);
            activeTroops.add(soldier);
            if (bundle) {
                playStandUserOnlySoundsIfNearby(SoundIndex.SUMMON_SOUND_2, 8, false, false);
            }else {
                playStandUserOnlySoundsIfNearby(this.getSummonSound(), 8, false, false);
            }
        }
    }

    public void troopRemoved(BCTroop soldier) {
        activeTroops.remove(soldier);
    }

    public boolean troopOnList(BCTroop soldier) {
        return activeTroops.contains(soldier);
    }
}
