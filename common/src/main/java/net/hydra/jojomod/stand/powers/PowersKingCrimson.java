package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.stand.WalkingHeartEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PowersKingCrimson extends NewPunchingStand {

    public PowersKingCrimson(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().theWorldSettings.enableTheWorld;
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKingCrimson(entity);
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.STAR_SUMMON_SOUND_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
        @Override
    public StandEntity getNewStandEntity() {
        byte sk = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        return ModEntities.KING_CRIMSON.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == KingCrimsonEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.king_crimson.manga");
        } if (skinId == KingCrimsonEntity.END){
            return Component.translatable(  "skins.roundabout.king_crimson.end");
        } if (skinId == KingCrimsonEntity.END_2){
            return Component.translatable(  "skins.roundabout.king_crimson.end_2");
        } if (skinId == KingCrimsonEntity.STARLESS){
            return Component.translatable(  "skins.roundabout.king_crimson.starless");
        } if (skinId == KingCrimsonEntity.HEAVEN){
            return Component.translatable(  "skins.roundabout.king_crimson.heaven");
        }if (skinId == KingCrimsonEntity.AGOGO){
            return Component.translatable(  "skins.roundabout.king_crimson.agogo");
        }if (skinId == KingCrimsonEntity.SPINE_ART){
            return Component.translatable(  "skins.roundabout.king_crimson.spine_art");
        }if (skinId == KingCrimsonEntity.GREEN){
            return Component.translatable(  "skins.roundabout.king_crimson.green");
        }if (skinId == KingCrimsonEntity.YELLOW){
            return Component.translatable(  "skins.roundabout.king_crimson.yellow");
        }if (skinId == KingCrimsonEntity.AQUA){
            return Component.translatable(  "skins.roundabout.king_crimson.aqua");
        }if (skinId == KingCrimsonEntity.BLACK){
            return Component.translatable(  "skins.roundabout.king_crimson.black");
        }if (skinId == KingCrimsonEntity.DARK){
            return Component.translatable(  "skins.roundabout.king_crimson.dark");
        }if (skinId == KingCrimsonEntity.BETA){
            return Component.translatable(  "skins.roundabout.king_crimson.beta");
        }if (skinId == KingCrimsonEntity.CONCEPT){
            return Component.translatable(  "skins.roundabout.king_crimson.concept");
        }if (skinId == KingCrimsonEntity.RED){
            return Component.translatable(  "skins.roundabout.king_crimson.red");
        }
        return Component.translatable(  "skins.roundabout.king_crimson.base");
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(KingCrimsonEntity.RED);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);

            $$1.add(KingCrimsonEntity.PART_5_SKIN);
            $$1.add(KingCrimsonEntity.MANGA_SKIN);
            if (Level > 1 || bypass) {
                $$1.add(KingCrimsonEntity.SPINE_ART);
                $$1.add(KingCrimsonEntity.AGOGO);
            } if (Level > 2 || bypass) {
                $$1.add(KingCrimsonEntity.BLACK);
                $$1.add(KingCrimsonEntity.DARK);
            } if (Level > 3 || bypass) {
                $$1.add(KingCrimsonEntity.HEAVEN);
                $$1.add(KingCrimsonEntity.AQUA);
                $$1.add(KingCrimsonEntity.YELLOW);
                $$1.add(KingCrimsonEntity.GREEN);
            } if (Level > 4 || bypass) {
                $$1.add(KingCrimsonEntity.STARLESS);
                $$1.add(KingCrimsonEntity.CONCEPT);
                $$1.add(KingCrimsonEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(KingCrimsonEntity.END);
                $$1.add(KingCrimsonEntity.END_2);
            }
        }
        return $$1;
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
    }

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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }

}