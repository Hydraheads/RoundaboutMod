package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;

import java.util.Arrays;

public class PowersTheGratefulDead extends NewPunchingStand {
    public PowersTheGratefulDead(LivingEntity self){
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersTheGratefulDead(entity);
    }
    @Override
    public boolean canSummonStandAsEntity(){
        return true;
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.THE_GRATEFUL_DEAD.create(this.getSelf().level());
    }

    @Override
    public int getMaxGuardPoints(){
        return 15;
    }

    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 1.45F;
        } else {
            return 4.0F;
        }
    }

    //GUI things

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        if(!isMiasmaActive()){
            setSkillIcon(context, x, y ,1, StandIcons.MIASMA_TOGGLE_ACTIVE, PowerIndex.SKILL_1);
        }else{
            setSkillIcon(context, x, y, 1, StandIcons.MIASMA_TOGGLE_INACTIVE, PowerIndex.SKILL_1);
        }

        if(!isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.AGE_GRAB, PowerIndex.SKILL_2);
        }else{
            setSkillIcon(context, x, y, 2, StandIcons.SNEAK_X, PowerIndex.SKILL_2);
        }

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if(!isDisguised()){
            setSkillIcon(context, x, y, 4, StandIcons.AGE_DISGUISE_1, PowerIndex.SKILL_4);
        }else{
            setSkillIcon(context, x, y, 4, StandIcons.AGE_DISGUISE_2, PowerIndex.SKILL_4);
        }

        super.renderIcons(context, x, y);
    }

    public boolean isMiasmaActive() {return false;}

    public boolean isDisguised() {return false;}

    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal("K4traik").withStyle(ChatFormatting.DARK_PURPLE);
    }

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_GRATEFUL_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    public static final byte
            ANIME_THE_GRATEFUL_DEAD = 0,
            MANGA_THE_GRATEFUL_DEAD = 1;

    @Override
    public List<Byte> getSkinList(){
        return Arrays.asList(
                ANIME_THE_GRATEFUL_DEAD,
                MANGA_THE_GRATEFUL_DEAD
        );
    }

    @Override
    public Component getSkinName(byte skinId){
        if(skinId==MANGA_THE_GRATEFUL_DEAD){
            return Component.translatable("skins.roundabout.the_grateful_dead.manga");
        }else{
            return Component.translatable("skins.roundabout.the_grateful_dead.anime");
        }
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        return $$1;
    }

    @Override
    public Component getPosName(byte posID){
        if (posID == 1){
            return Component.translatable(   "idle.roundabout.floaty");
        }else{
            return Component.translatable(   "idle.roundabout.walky");
        }
    }
}
