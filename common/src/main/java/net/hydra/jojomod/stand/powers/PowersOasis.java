package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PowersOasis extends NewDashPreset {
    public PowersOasis(LivingEntity self) {
        super(self);
    }


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersOasis(entity);
    }

    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }

    @Override
    public boolean rendersPlayer(){
        return true;
    }

    @Override
    public boolean isBrawling(){
        return fistsOut;
    }

    public boolean fistsOut = false;



    public boolean renderSuit(){
        return (self instanceof Player pl || MainUtil.isHumanoid2(self)) && PowerTypes.hasStandActive(self);
    }

    // stand fading for first person
    public static float getOasisAmt(Entity entity, float partialTicks){
        float heyFull = 0;
        if (entity instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            boolean hasOasisOut = user.roundabout$getStandPowers() instanceof PowersOasis po && po.renderSuit();
            int oasisTicks = user.roundabout$getOasisVanishTicks();
            if (hasOasisOut || oasisTicks > 0) {
                byte skin = user.roundabout$getStandSkin();
                if (user.roundabout$getLastStandSkin() != skin) {
                    user.roundabout$setLastStandSkin(skin);
                    oasisTicks = 0;
                    user.roundabout$setOasisVanishTicks(0);
                }

                float partialTicks2 = partialTicks % 1;
                if (hasOasisOut) {
                    heyFull = oasisTicks + partialTicks2;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = oasisTicks - partialTicks2;
                    heyFull = Math.max(heyFull / 10, 0);
                }
            }
        }
        return heyFull;
    }


    public void toggleFistsClient() {
        tryPowerPacket(PowerIndex.POWER_1);
    }

    public void toggleFists() {
        if (!this.self.level().isClientSide()){
            fistsOut = !fistsOut;
            saveDiscAndSync();
        }
    }



    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            case SKILL_1_NORMAL -> {
                toggleFistsClient();
            }

        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {

            case PowerIndex.POWER_1 -> {
                toggleFists();
            }

        }
        return super.setPowerOther(move,lastMove);
    }



    @Override
    public void tickPower() {

    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("fistsOut",fistsOut);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("fistsOut")) {
            fistsOut = $$0.getBoolean("fistsOut");
        }
    }

    @Override
    public boolean setPowerAttack(){
        setAttack();
        return false;
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (fistsOut) {
            setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT_2, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT, PowerIndex.SKILL_1);
        }

    }





    public static String getSkinString(byte skinId) {
        return switch (skinId)
        {
            default -> "base";
        };
    }

}
