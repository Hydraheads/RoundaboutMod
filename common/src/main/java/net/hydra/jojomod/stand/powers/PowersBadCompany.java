package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.badCompanyTroops.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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

        SOLDIER_SUMMON = 50,
        BUNDLE_SUMMON = 51,
        HELICOPTER_SUMMON = 52,
        TANK_SUMMON = 53;

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

    public BCSoldier getSoldierEntity(boolean bundle) {
        BCSoldier soldier = ModEntities.BAD_COMPANY_SOLDIER.create(self.level());
        if (soldier != null) {
            soldier.setSkin(((StandUser)self).roundabout$getStandSkin());
            soldier.setBundle(bundle);
        }
        return soldier;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.BAD_COMPANY_TROOP_RECALL, PowerIndex.NO_CD);
        }else{
            setSkillIcon(context, x, y, 1, StandIcons.BAD_COMPANY_TROOP_DROP, PowerIndex.NO_CD);
        }
        
        setSkillIcon(context, x, y, 2, StandIcons.BAD_COMPANY_TACTICAL_OPTIONS, PowerIndex.NO_CD);

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
            return ModSounds.BC_SUMMON_EVENT;
        }else if (soundChoice == SOLDIER_SUMMON) {
            return ModSounds.BC_TROOP_SUMMON_EVENT;
        }else if (soundChoice == BUNDLE_SUMMON) {
            return ModSounds.BC_BUNDLE_SUMMON_EVENT;
        }else if (soundChoice == HELICOPTER_SUMMON) {
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
        tryIntPowerPacket(PowerIndex.POWER_1, id);
    }

    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_1) {
            if (chargeTime == TROOP) {
                spawnSoldier(false);
            }else if (chargeTime == BUNDLE) {
                spawnSoldier(true);
            }else if (chargeTime == HELICOPTER) {
                playStandUserOnlySoundsIfNearby(HELICOPTER_SUMMON, 8, false, false);
            }else if (chargeTime == TANK) {
                playStandUserOnlySoundsIfNearby(TANK_SUMMON, 8, false, false);
            }
        }

        return super.tryIntPower(move, forced, chargeTime);
    }

    public void spawnSoldier(boolean bundle) {
        if (isClient()) { return; }

        initActiveTroops();
        BCSoldier soldier = getSoldierEntity(bundle);
        if (soldier != null) {
            soldier.setUser(self);
            soldier.absMoveTo(
                    self.getPosition(1).x,
                    self.getPosition(1).y,
                    self.getPosition(1).z
            );
            self.level().addFreshEntity(soldier);
            activeTroops.add(soldier);

            if (bundle) {
                playStandUserOnlySoundsIfNearby(BUNDLE_SUMMON, 8, false, false);
            }else {
                playStandUserOnlySoundsIfNearby(SOLDIER_SUMMON, 8, false, false);
            }
        }
    }

    public void troopRemoved(BCTroop soldier) {
        initActiveTroops();
        activeTroops.remove(soldier);
    }

    public boolean troopOnList(BCTroop soldier) {
        initActiveTroops();
        return activeTroops.contains(soldier);
    }
    @Override
    public boolean returnFakeStandForHud(){
        if(this.self != null) {
            return true;
        }
        return false;
    }

    @Override
    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = getSoldierEntity(false);
        }
        if (this.self instanceof Player PL && ((IPlayerEntity) PL).roundabout$getStandSkin() != displayStand.getSkin()) {
            displayStand = getSoldierEntity(false);
            displayStand.setSkin(((IPlayerEntity) PL).roundabout$getStandSkin());
        }

        // All of this is code to have the idle animation appear.
        if (displayStand != null && this.self != null) {
            if (displayStand instanceof BCSoldier S) {
                S.isDisplay = true;
            }
            StandUser su = (StandUser) this.self;
            displayStand.setUser(this.self);
            displayStand.tickCount = this.self.tickCount;

            displayStand.setAnimation(su.roundabout$getStandAnimation());
            displayStand.setIdleAnimation(su.roundabout$getIdlePos());

            StandEntity realStand = this.getStandEntity(this.self);
            if (realStand != null) {
                displayStand.setPose(realStand.getPose());
                displayStand.setIdleAnimation(realStand.getIdleAnimation());
            }

            displayStand.setupAnimationStates();
        }

        return displayStand;
    }
}
