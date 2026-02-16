package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ZombieFate extends VampiricFate {


    public ZombieFate() {
        super();
    }
    public ZombieFate(LivingEntity self) {
        super(self);
    }

    public FatePowers generateFatePowers(LivingEntity entity){
        Roundabout.LOGGER.info("test");
        return new ZombieFate(entity);
    }
    @Override
    /**The text name of the fate*/
    public Component getFateName(){
        return Component.translatable("text.roundabout.fate.zombie");
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL -> {
            }
            case SKILL_1_CROUCH -> {
            }
            case SKILL_2_NORMAL,SKILL_2_CROUCH -> {
                suckBlood();
            }
            case SKILL_3_NORMAL,SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL,SKILL_4_CROUCH -> {
            }
        }
    };


    @Override
    public void drawOtherGUIElements(Font font, GuiGraphics context, float delta, int mouseX, int mouseY, int i, int j, ResourceLocation rl){

    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move,forced);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        return super.setPowerOther(move,lastMove);
    }

    @Override
    public float getJumpHeightAddon(){
        if (getStandUserSelf().roundabout$getStandPowers().bigJumpBlocker() ||
        self instanceof Player pl && (((IPowersPlayer)pl).rdbt$getPowers().bigJumpBlocker()))
            return super.getJumpHeightAddon();
        //if (self.isCrouching() || isFast()){
        //    return super.getJumpHeightAddon()+4;
        //} else {
        //    return super.getJumpHeightAddon();
        //}
        if (isDisguised()){
            return super.getJumpHeightAddon();
        }
        return super.getJumpHeightAddon()+getAddon();
    }
    @Override
    public float getJumpHeightAddonMax(){
        return 5;
    }

    /**This function grays out icons for moves you can't currently use. Slot is the icon slot from 1-4,
     * activeP is your currently active power*/
    public boolean isAttackIneptVisually(byte activeP, int slot){
        Entity TE = getUserData(self).roundabout$getStandPowers().getTargetEntity(this.self, 3, 15);
        if (slot == 2 && !MainUtil.canDrinkBloodFair(TE, self) &&
                !negateDrink())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
    }

    public float getAddon(){
        if (self.isCrouching() && rechargeJump){
            return 4;
        } else {
            return 2;
        }
    }
    public boolean rechargeJump = false;


    @Override
    public void tickPower(){
        if (!self.level().isClientSide()){
            if (self instanceof Player player){
                IPlayerEntity ple = ((IPlayerEntity) player);
                byte shape = ple.roundabout$getShapeShift();
                ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
                if (shift != ShapeShifts.PLAYER){

                }
            }
        }
        if (self.onGround()){
            rechargeJump = true;
        } else if (!self.isCrouching()){
            rechargeJump = false;
        }
        super.tickPower();
    }


    @Override
    public float getDamageReduction(DamageSource source, float amt){
        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            return 0.08F;
        }
        if (source.is(DamageTypes.ARROW) || source.is(ModDamageTypes.BULLET)){
            return 0.1F;
        }
        return super.getDamageReduction(source,amt);
    }
    @Override
    public float getDamageAdd(DamageSource source, float amt, Entity target){
        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            if (target instanceof Player pl){
                return 0.05F;
            } else {
                return 0.1F;
            }
        }
        return super.getDamageAdd(source,amt,target);
    }

    /**For enhancement stands that adjust your normal player attack speed*/
    public float getBonusAttackSpeed() {
        return 1.05F;
    }
    /**For enhancement stands that adjust your normal player mining speed*/
    public float getBonusPassiveMiningSpeed(){
        return 1.1F;
    }

    public float inputSpeedModifiers(float basis){
        return super.inputSpeedModifiers(basis);
    }


    private final TargetingConditions hypnosisTargeting = TargetingConditions.forCombat().range(11);
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.ZOMBIE_WORM, PowerIndex.FATE_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.ZOMBIE_PROJECTILES, PowerIndex.FATE_1_SNEAK);
        }
        setSkillIcon(context, x, y, 2, StandIcons.ZOMBIE_DRINK, PowerIndex.FATE_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        if (isDisguised()){
            setSkillIcon(context, x, y, 4, StandIcons.ZOMBIE_DISGUISE_ON, PowerIndex.FATE_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.ZOMBIE_DISGUISE_OFF, PowerIndex.FATE_4);
        }
    }

    public boolean isDisguised(){
        if (self instanceof Player PE && ((IPlayerEntity)PE).roundabout$getShapeShiftExtraData() == (byte)1
        && ((IPlayerEntity)PE).roundabout$getShapeShiftExtraData() == ShapeShifts.PLAYER.ordinal()){
            return true;
        }
        return false;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        super.renderAttackHud(context,playerEntity,scaledWidth,scaledHeight,ticks,vehicleHeartCount,flashAlpha,otherFlashAlpha);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();


        return $$1;
    }
}
