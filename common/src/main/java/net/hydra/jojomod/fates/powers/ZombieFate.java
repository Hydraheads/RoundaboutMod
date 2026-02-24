package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ZombieFate extends VampiricFate {

    public static final byte DISGUISE = 50;
    public static final byte ZOMBIE_SHOT = 51;
    public int spikeTimeDuring = 0;

    public ZombieFate() {
        super();
    }
    public ZombieFate(LivingEntity self) {
        super(self);
    }

    public FatePowers generateFatePowers(LivingEntity entity){
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
            case SKILL_1_NORMAL,SKILL_1_CROUCH -> {
                zombieShotClient();
            }
            case SKILL_2_NORMAL,SKILL_2_CROUCH -> {
                suckBlood();
            }
            case SKILL_3_NORMAL,SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL,SKILL_4_CROUCH -> {
                switchDisguiseClient();
            }
        }
    };

    public boolean isArrow(ItemStack stack){
        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ArrowItem){
            return true;
        }
        return false;
    }
    public boolean isKnife(ItemStack stack){
        if (stack != null && !stack.isEmpty() && stack.is(ModItems.KNIFE)){
            return true;
        }
        return false;
    }
    public boolean isKnifeBundle(ItemStack stack){
        if (stack != null && !stack.isEmpty() && stack.is(ModItems.KNIFE_BUNDLE)){
            return true;
        }
        return false;
    }
    public boolean canUseZombieShot(){
        ItemStack hand = self.getMainHandItem();
        ItemStack offHand = self.getOffhandItem();
        if (isKnife(hand) || isKnifeBundle(hand) || isArrow(hand)){
            return true;
        }
        if (isKnife(offHand) || isKnifeBundle(offHand) || isArrow(offHand)){
            return true;
        }

        return false;
    }

    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.FATE_1_SNEAK || num == PowerIndex.FATE_4){
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public void zombieShotClient(){
        if (getActivePower() == ZOMBIE_SHOT){
            tryPowerPacket(NONE);
            return;
        }
        if (canUseZombieShot()){
            if (!onCooldown(PowerIndex.FATE_1_SNEAK)) {
                tryPowerPacket(ZOMBIE_SHOT);
            }
        }
    }

    public void switchDisguiseClient(){
        tryPowerPacket(DISGUISE);
    }

    public void switchDisguiseServer(){
        if (!self.level().isClientSide()) {
            setActivePower(PowerIndex.NONE);
            if (self instanceof Player pl) {
                if (!onCooldown(PowerIndex.FATE_4)) {
                    IPlayerEntity ipe = ((IPlayerEntity) pl);
                    boolean isMorphed = ipe.roundabout$getShapeShift() != ShapeShifts.PLAYER.ordinal();
                    setCooldown(PowerIndex.FATE_4,10);
                    if (!isMorphed) {
                        boolean isDisguised = isDisguised();
                        Position pn = self.getEyePosition();
                        ((ServerLevel) self.level()).sendParticles(ParticleTypes.CLOUD,
                                pn.x(), pn.y(), pn.z(),
                                4, 0.2, 0.2, 0.2, 0.01);
                        if (isDisguised) {
                            ipe.roundabout$setShapeShiftExtraData((byte) 0);
                            ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.zombie_disguise_off").withStyle(ChatFormatting.DARK_PURPLE), true);
                        } else {
                            ipe.roundabout$setShapeShiftExtraData((byte) 1);
                            ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.zombie_disguise_on").withStyle(ChatFormatting.DARK_PURPLE), true);
                        }
                    }
                }
            }
        }
    }


    public void doZombieShot(){

    }

    @Override
    public void drawOtherGUIElements(Font font, GuiGraphics context, float delta, int mouseX, int mouseY, int i, int j, ResourceLocation rl){

    }

    public void zombieShotStart(){
        if (!self.level().isClientSide()) {
            if (canUseZombieShot() && !onCooldown(PowerIndex.FATE_1_SNEAK)){
                setActivePower(ZOMBIE_SHOT);
                setAttackTimeDuring(0);
                setCooldown(PowerIndex.FATE_1_SNEAK,60);
                self.level().playSound(null, self.blockPosition(), ModSounds.ZOMBIE_CHARGE_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);
            } else {
                setActivePower(PowerIndex.NONE);
            }
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move,forced);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == DISGUISE) {
            switchDisguiseServer();
        } else if (move == ZOMBIE_SHOT) {
            zombieShotStart();
        }
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

    public static int maxSpike= 50;
    public static int maxSpike2= 56;
    public boolean retract = false;
    public boolean extended = false;

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

            if (activePower == ZOMBIE_SHOT){
                if (!canUseZombieShot()) {
                    xTryPower(NONE,true);
                } else if (attackTimeDuring >= 24){
                    doZombieShot();
                    xTryPower(NONE,true);
                } else {
                    if(this.attackTimeDuring%4==0) {
                        Vec3 gravVec = this.getSelf().getPosition(1f).add(RotationUtil.vecPlayerToWorld(
                                new Vec3(0,0.3*self.getEyeHeight(),0),
                                ((IGravityEntity)self).roundabout$getGravityDirection()));
                        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                                gravVec.x, gravVec.y, gravVec.z,
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }

        } else {
            byte pos2 = getPlayerPos2();
            if (pos2 == PlayerPosIndex.BLOOD_SUCK) {
                if (spikeTimeDuring < maxSpike) {
                    spikeTimeDuring++;
                }
            } else {
                spikeTimeDuring=0;
                retract = false;
                extended = false;
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
        if (isDisguised()){
            return super.getDamageAdd(source,amt,target);
        }

        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            if (target instanceof Player pl){
                return 0.08F;
            } else {
                return 0.17F;
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
        if (activePower == ZOMBIE_SHOT){
            basis*=0.2F;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelSprintJump(){
        return getActivePower() == ZOMBIE_SHOT || super.cancelSprintJump();
    }
    @Override
    /**Cancel all sprinting*/
    public boolean cancelSprint(){
        return getActivePower() == ZOMBIE_SHOT || super.cancelSprint();
    }
    @Override
    public boolean cancelSprintParticles(){
        return getActivePower() == ZOMBIE_SHOT || super.cancelSprintJump();
    }


    private final TargetingConditions hypnosisTargeting = TargetingConditions.forCombat().range(11);
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (canUseZombieShot()) {
            setSkillIcon(context, x, y, 1, StandIcons.ZOMBIE_PROJECTILES, PowerIndex.FATE_1_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.ZOMBIE_WORM, PowerIndex.FATE_1);
        }
        setSkillIcon(context, x, y, 2, StandIcons.ZOMBIE_DRINK, PowerIndex.FATE_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        if (isDisguised()){
            setSkillIcon(context, x, y, 4, StandIcons.ZOMBIE_DISGUISE_ON, PowerIndex.FATE_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.ZOMBIE_DISGUISE_OFF, PowerIndex.FATE_4);
        }
    }


    @Override
    public boolean isVisionOn(){
        return !isDisguised();
    }

    public boolean isDisguised(){
        if (self instanceof Player PE && ((IPlayerEntity)PE).roundabout$getShapeShiftExtraData() == (byte)1
        && ((IPlayerEntity)PE).roundabout$getShapeShift() == ShapeShifts.PLAYER.ordinal()){
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
