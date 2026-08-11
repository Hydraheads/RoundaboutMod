package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
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

public class PowersD4C extends NewPunchingStand {
    public PowersD4C(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersD4C(entity);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableKingCrimson;
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_D4C_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public void tickPower() {
        super.tickPower();
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        return 25;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.D4C.create(this.getSelf().level());
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


    @Override public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.d4c."+getSkinString(skinId));
    }
    public static Component getSkinNameStatic(byte skinId) {
        return Component.translatable("skins.roundabout.d4c."+getSkinString(skinId));
    }
    public static String getSkinString(byte skinId) {

        return switch (skinId)
        {
            case D4CEntity.BASE -> "base";
            case D4CEntity.COVER -> "cover";
            case D4CEntity.SPINE -> "spine";
            case D4CEntity.VELLER -> "veller";
            case D4CEntity.ROA -> "roa";
            case D4CEntity.DEPARTURE -> "departure";
            case D4CEntity.PROMO -> "promo";
            case D4CEntity.FOUNTAIN_BOY -> "fountain_boy";
            case D4CEntity.CONCEPT_ART -> "concept_art";
            case D4CEntity.EOH_BLUE -> "eoh_blue";
            case D4CEntity.EOH_PINK -> "eoh_pink";
            case D4CEntity.EOH_PURPLE -> "eoh_purple";
            case D4CEntity.EOH_YELLOW -> "eoh_yellow";
            case D4CEntity.GOLDEN -> "golden";
            case D4CEntity.GOLDEN_V2 -> "golden_v2";
            case D4CEntity.CURSED_CARD -> "cursed_card";
            case D4CEntity.ULTRA_BRAND -> "ultra_brand";
            case D4CEntity.KEYHOLDER -> "keyholder";
            case D4CEntity.GRAY -> "gray";
            case D4CEntity.BLACK_WHITE -> "black_white";
            case D4CEntity.REVERSE -> "reverse";
            default -> "base";
        };
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(D4CEntity.BASE);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(D4CEntity.COVER);
                $$1.add(D4CEntity.SPINE);
            } if (Level > 2 || bypass){
                $$1.add(D4CEntity.VELLER);
                $$1.add(D4CEntity.ROA);
                $$1.add(D4CEntity.BLACK_WHITE);
                $$1.add(D4CEntity.REVERSE);
            } if (Level > 3 || bypass){
                $$1.add(D4CEntity.DEPARTURE);
                $$1.add(D4CEntity.PROMO);
                $$1.add(D4CEntity.KEYHOLDER);
                $$1.add(D4CEntity.GRAY);
            } if (Level > 4 || bypass){
                $$1.add(D4CEntity.EOH_BLUE);
                $$1.add(D4CEntity.EOH_PINK);
                $$1.add(D4CEntity.EOH_PURPLE);
                $$1.add(D4CEntity.EOH_YELLOW);
            } if (Level > 5 || bypass){
                $$1.add(D4CEntity.FOUNTAIN_BOY);
                $$1.add(D4CEntity.CONCEPT_ART);
                $$1.add(D4CEntity.CURSED_CARD);
                $$1.add(D4CEntity.ULTRA_BRAND);
            } if (Level > 6 || bypass){
                $$1.add(D4CEntity.GOLDEN);
                $$1.add(D4CEntity.GOLDEN_V2);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
            }
        }
        return $$1;
    }
    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }
}
