package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class VampireFate extends VampiricFate {


    public VampireFate() {
        super();
    }
    public VampireFate(LivingEntity self) {
        super(self);
    }

    public FatePowers generateFatePowers(LivingEntity entity){
        return new VampireFate(entity);
    }
    @Override
    /**The text name of the fate*/
    public Component getFateName(){
        return Component.translatable("text.roundabout.fate.vampire");
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                hypnosis();
            }
            case SKILL_1_CROUCH -> {
                hairExtendClient();
            }
            case SKILL_2_NORMAL -> {
                suckBlood();
            }
            case SKILL_2_CROUCH -> {
                regenClient();
            }
            case SKILL_3_CROUCH -> {
                bloodSpeedClient();
            }
            case SKILL_3_NORMAL -> {
                dashOrWallWalk();
            }
            case SKILL_4_NORMAL -> {
                setSuperHearingClient();
            }
            case SKILL_4_CROUCH -> {
                clientChangeVision();
            }
        }
    };
    public static final byte HYPNOSIS = 50;
    public static final byte HAIR_EXTENDED = 51;


    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.FATE_EXTRA){
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public static String formatTicks(int ticks) {
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
    @Override
    public void drawOtherGUIElements(Font font, GuiGraphics context, float delta, int mouseX, int mouseY, int i, int j, ResourceLocation rl){

        VampireData vdata = getVampireData();

        int blt = 0;
        if (vdata.vampireLevel >= 40){
            context.blit(rl, i +80, j + 19, 126, 194, 20, 20);
            context.blit(rl, i + 102, j + 19, 148, 194, 20, 20);
            context.blit(rl, i + 124, j + 19, 170, 194, 20, 20);
        } else {
            context.blit(rl, i +80, j + 19, 192, 152, 20, 20);
            int bloodAmt = vdata.animalExp;
            int bloodLmt = getEXPcap();
            blt = (int) Math.floor(((double) 20 / bloodLmt) * (bloodAmt));
            int blt2 = 20-blt;
            if (blt > 0) {
                if (bloodAmt >= bloodLmt) {
                    context.blit(rl, i + 80, j + 19 + blt2, 192, 194 + blt2, 20, blt);
                } else {
                    context.blit(rl, i + 80, j + 19 + blt2, 192, 173 + blt2, 20, blt);
                }
            }
            if (vdata.timeSinceAnimal > 0) {
                if (isSurelyHovering(i + 80, j + 19, 20, 20, mouseX, mouseY)) {
                    List<Component> compList = Lists.newArrayList();
                    compList.add(Component.literal("" + formatTicks(vdata.timeSinceAnimal)).withStyle(ChatFormatting.WHITE));
                    context.renderTooltip(Minecraft.getInstance().font, compList, Optional.empty(), mouseX, mouseY);
                }
            }

            context.blit(rl, i + 102, j + 19, 214, 152, 20, 20);
            bloodAmt = vdata.monsterEXP;
            blt = (int) Math.floor(((double) 20 / bloodLmt) * (bloodAmt));
            blt2 = 20 - blt;
            if (blt > 0) {
                if (bloodAmt >= bloodLmt) {
                    context.blit(rl, i + 102, j + 19 + blt2, 214, 194 + blt2, 20, blt);
                } else {
                    context.blit(rl, i + 102, j + 19 + blt2, 214, 173 + blt2, 20, blt);
                }
            }
            if (vdata.timeSinceMonster > 0) {
                if (isSurelyHovering(i + 102, j + 19, 20, 20, mouseX, mouseY)) {
                    List<Component> compList = Lists.newArrayList();
                    compList.add(Component.literal("" + formatTicks(vdata.timeSinceMonster)).withStyle(ChatFormatting.WHITE));
                    context.renderTooltip(Minecraft.getInstance().font, compList, Optional.empty(), mouseX, mouseY);
                }
            }

            context.blit(rl, i + 124, j + 19, 236, 152, 20, 20);
            bloodAmt = vdata.npcExp;
            blt = (int) Math.floor(((double) 20 / bloodLmt) * (bloodAmt));
            blt2 = 20 - blt;
            if (blt > 0) {
                if (bloodAmt >= bloodLmt) {
                    context.blit(rl, i + 124, j + 19 + blt2, 236, 194 + blt2, 20, blt);
                } else {
                    context.blit(rl, i + 124, j + 19 + blt2, 236, 173 + blt2, 20, blt);
                }
            }
            if (vdata.timeSinceNpc > 0) {
                if (isSurelyHovering(i + 124, j + 19, 20, 20, mouseX, mouseY)) {
                    List<Component> compList = Lists.newArrayList();
                    compList.add(Component.literal("" + formatTicks(vdata.timeSinceNpc)).withStyle(ChatFormatting.WHITE));
                    context.renderTooltip(Minecraft.getInstance().font, compList, Optional.empty(), mouseX, mouseY);
                }
            }
        }

        if (isSurelyHovering(i +147, j + 20,19,18,mouseX,mouseY)){
            context.blit(rl, i +147, j + 20, 236, 131, 19, 18);
            List<Component> compList = Lists.newArrayList();
            compList.add(Component.translatable("text.roundabout.fate.vampire_hair").withStyle(ChatFormatting.WHITE));
            context.renderTooltip(Minecraft.getInstance().font, compList, Optional.empty(), mouseX, mouseY);
        } else {
            context.blit(rl, i +147, j + 20, 236, 112, 19, 18);
        }


        Component display = Component.translatable("leveling.roundabout.fate_development_potential_level",
                vdata.vampireLevel+1);
        if (vdata.vampireLevel >= 40){
            display = Component.translatable("leveling.roundabout.fate_maxed");
        }
        //display = Component.translatable("leveling.roundabout.disc_maxed",
        //        0);
        context.drawString(font, display, i  +80, j+64, 4210752, false);

        int points = vdata.getPoints();
        if (points > 0){
            if (points == 1){
                display = Component.translatable("leveling.roundabout.fate_development_point",
                        points);
            } else {
                display = Component.translatable("leveling.roundabout.fate_development_points",
                        points);
            }
            context.drawString(font, display, i  +80, j+46, 10170412, false);
        }

        int ss = i + 78;
        int sss = j + 57;
        int exp = vdata.bloodExp;
        int maxXP = getLevelUpExpCost();
        blt = (int) Math.floor(((double) 92 / maxXP) * (exp));
        context.blit(rl, ss, sss, 10, 244, 92, 4);
        context.blit(rl, ss, sss, 10, 248, blt, 4);
    }

    @Override
    /**An easy way to replace the EXP bar with a stand bar, see the function below this one*/
    public boolean replaceHudActively(){
        if (self.level().isClientSide()){
            if (ClientUtil.renderBloodMeter()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x,
                                  boolean removeNum){
        StandHudRender.renderBloodExp(context,cameraPlayer,screenWidth,screenHeight,x);
    }

    @Override

    public void handleCustomGUIClick(int i, int j, double mouseX, double mouseY){
        if (self.level().isClientSide()){
            if (isSurelyHovering(i +147, j + 20,19,18,mouseX,mouseY)){
                    ClientUtil.openHairspryUI();
            }

            VampireData vdata = getVampireData();
            if (vdata.getPoints() > 0) {
                int level = vdata.vampireLevel;


                //Strength
                if (isSurelyHovering(i + 67, j + 80, 19, 19, mouseX, mouseY)) {
                    if (vdata.strengthLevel < VampireData.strengthMaxLevel){
                        ClientUtil.clickVampireSlot(1);
                    }
                }
                //Dexterity
                if (isSurelyHovering(i + 67, j + 99, 19, 19, mouseX, mouseY)) {
                    if (vdata.dexterityLevel < VampireData.dexterityMaxLevel){
                        ClientUtil.clickVampireSlot(2);
                    }
                }
                //Resilience
                if (isSurelyHovering(i + 67, j + 118, 19, 19, mouseX, mouseY)) {
                    if (vdata.resilienceLevel < VampireData.reslienceMaxLevel){
                        ClientUtil.clickVampireSlot(3);
                    }
                }


                //Hypnotism
                if (isSurelyHovering(i + 86, j + 80, 19, 19, mouseX, mouseY)) {
                    if (vdata.hypnotismLevel < VampireData.hypnotismMaxLevel){
                        ClientUtil.clickVampireSlot(4);
                    }
                }
                //Hearing
                if (isSurelyHovering(i + 86, j + 99, 19, 19, mouseX, mouseY)) {
                    if (vdata.superHearingLevel < VampireData.superHearingMaxLevel){
                        ClientUtil.clickVampireSlot(5);
                    }
                }
                //Blood Speed
                if (isSurelyHovering(i + 86, j + 118, 19, 19, mouseX, mouseY)) {
                    if (vdata.bloodSpeedLevel < VampireData.bloodSpeedMaxLevel){
                        ClientUtil.clickVampireSlot(6);
                    }
                }

                //Grafting
                if (isSurelyHovering(i + 105, j + 80, 19, 19, mouseX, mouseY)) {
                    if (vdata.graftingLevel < VampireData.graftingMaxLevel){
                        ClientUtil.clickVampireSlot(7);
                    }
                }
                //Flesh Bud
                if (isSurelyHovering(i + 105, j + 99, 19, 19, mouseX, mouseY)) {
                    if (vdata.fleshBudLevel < VampireData.fleshBudMaxLevel){
                        if (level >= 4){
                            ClientUtil.clickVampireSlot(8);
                        }
                    }
                }
                //Dagger
                if (isSurelyHovering(i + 105, j + 118, 19, 19, mouseX, mouseY)) {

                    if (vdata.daggerSplatterLevel < VampireData.daggerSplatterMaxLevel){
                        if (level >= 14){
                            ClientUtil.clickVampireSlot(9);
                        }
                    }
                }

                //Jump Boost
                if (isSurelyHovering(i + 129, j + 80, 19, 19, mouseX, mouseY)) {
                    if (vdata.jumpLevel < VampireData.jumpMaxLevel){
                        ClientUtil.clickVampireSlot(10);
                    }
                }
                //Eye
                if (isSurelyHovering(i + 129, j + 99, 19, 19, mouseX, mouseY)) {
                    if (vdata.ripperEyesLevel < VampireData.ripperEyesMaxLevel){
                        if (level >= ripperEyeLevel){
                            ClientUtil.clickVampireSlot(11);
                        }
                    }
                }
                //Cold
                if (isSurelyHovering(i + 129, j + 118, 19, 19, mouseX, mouseY)) {
                    if (vdata.freezeLevel < VampireData.freezeMaxLevel){
                        if (level >= 19){
                            ClientUtil.clickVampireSlot(12);
                        }
                    }
                }
            }
        }
    }
    public static final int ripperEyeLevel = 2;

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (move != HAIR_EXTENDED && !self.level().isClientSide() &&
                getPlayerPos2() == PlayerPosIndex.HAIR_EXTENSION) {
            super.setPlayerPos2(PlayerPosIndex.NONE_2);
        }

        switch (move) {
            case WALL_WALK -> {
                wallLatch();
            }
            case HAIR_EXTENDED -> {
                hairExtendServer();
            }
        }
        return super.tryPower(move,forced);
    }
    public void hairExtendClient(){
        if (isHearing()){
            stopHearingClient();
        }
        if (canUseFleshBud()) {
            if (!onCooldown(PowerIndex.FATE_1_SNEAK) || activePower == HAIR_EXTENDED) {
                if (activePower != HAIR_EXTENDED) {
                    this.setCooldown(PowerIndex.FATE_1_SNEAK, 44);
                }
                tryPowerPacket(HAIR_EXTENDED);
            }
        }
    }
    public void hypnosis(){
        if (isHearing()){
            stopHearingClient();
        }
        if (canUseHypnosis()) {
            tryPowerPacket(HYPNOSIS);
        }
    }
    public int animationProgress = 0;
    public int getProgressIntoAnimation(){
        return animationProgress;
    }
    public boolean hasHairExtended(){
        return getActivePower() == HAIR_EXTENDED;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == HYPNOSIS) {
            hypnosisServer();
        }if (move == FLOWER_DRINK) {
            drinkPlantServer();
        }
        return super.setPowerOther(move,lastMove);
    }

    public void drinkPlantServer(){
        if (!self.level().isClientSide()) {
            if (!onCooldown(PowerIndex.GENERAL_EXTRA)) {
                if (isHoldingPlant()) {
                    if (isPlant(self.getMainHandItem())){
                        ItemStack stack = self.getMainHandItem();
                        stack.shrink(1);
                    } else if (isPlant(self.getOffhandItem())){
                        ItemStack stack = self.getOffhandItem();
                        stack.shrink(1);
                    }
                    self.heal(1f);
                    setCooldown(PowerIndex.GENERAL_EXTRA, 200);
                    self.level().playSound(null, self.blockPosition(), ModSounds.VAMPIRE_CAMO_EVENT,
                            SoundSource.PLAYERS, 1F, 1.8F);
                    setPowerNone();
                }
            }
        }
    }
    public void hypnosisServer(){
        if (isHypnotizing){
            isHypnotizing = false;
            hypnoTicks = 0;
        } else {
            isHypnotizing = true;
            hypnoTicks = 0;
        }
    }
    public void hairExtendServer() {
        if (getActivePower() != BLOOD_REGEN) {
            if (hasHairExtended()) {
                xTryPower(PowerIndex.NONE, true);
            } else {
                setActivePower(HAIR_EXTENDED);
                setPlayerPos2(PlayerPosIndex.HAIR_EXTENSION);
                this.attackTimeDuring = 0;

                self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.HAIR_TOGGLE_EVENT, SoundSource.PLAYERS, 1F, 1F);

            }
        }
    }
    public static final int hairChargeLength = 20;
    public boolean isHypnotizing = false;
    public int hypnoTicks = 0;

    @Override
    public boolean negateDrink(){
        return isHoldingPlant();
    }

    public boolean isAttackIneptVisually(byte activeP, int slot){

        if (slot == 3 && isPlantedInWall() && !isHoldingSneak() && !canLatchOntoWall())
            return true;
        if (slot == 3 && isHoldingSneak() && !canUseBloodSpeed() && !canLatchOntoWall() && canUseBloodSpeedUnlock())
            return true;
        if (slot == 2 && isHoldingSneak() && !canUseRegen())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
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
        if (self.level().isClientSide() && !isVisionOn()){
            return super.getJumpHeightAddon();
        }
        return super.getJumpHeightAddon()+getAddon();
    }
    @Override
    public float getJumpHeightAddonMax(){
        return 5;
    }
    public float getAddon(){
        if (activePower == BLOOD_REGEN)
            return 0;



        if (self instanceof Player pl && ((StandUser)pl).roundabout$getActive() &&
                PowerTypes.getPowerType(pl) == PowerTypes.VAMPIRE.ordinal() &&
        getVampireData().jumpLevel > 0){
            if (self.isCrouching() && rechargeJump){
                return 7;
            } else {
                if (jumpedOffWall)
                    return 0;
                return 4;
            }
        } else {

            if (self.isCrouching() && rechargeJump){
                return 4;
            } else {
                if (jumpedOffWall)
                    return 0;
                return 2;
            }
        }

    }
    public boolean rechargeJump = false;


    @Override
    public void tickPower(){
        if (self.onGround()){
            rechargeJump = true;
        } else if (!self.isCrouching()){
            rechargeJump = false;
        }
        tickHypnosis();
        tickHair();
        super.tickPower();
    }

    public void tickHair(){
        if (self.level().isClientSide()){
            if (getPlayerPos2() == PlayerPosIndex.HAIR_EXTENSION) {
                animationProgress++;
            } else if (getPlayerPos2() == PlayerPosIndex.HAIR_EXTENSION_2){
                animationProgress = Math.max(animationProgress+1,16);
            } else {
                animationProgress = 0;
            }
        }

        if (activePower == HAIR_EXTENDED){
            if (attackTimeDuring >= getMaxAttackTimeDuringHair() && !isClient()) {
                Entity TE = getTargetEntity(self, 7, 15);
                if (TE != null){
                    if (MainUtil.canPlantBud(TE, self)){
                        if (((StandUser)TE).rdbt$getFleshBud() == null) {
                            if (canPlantDrink(TE) || canPlantHealth(TE)) {
                                fleshBudIfNearby(100, TE.getId());
                                ((StandUser) TE).rdbt$setFleshBud(self.getUUID());
                                if (TE instanceof Mob mb){
                                    ((StandUser)mb).roundabout$deeplyRemoveAttackTarget();
                                }
                            } else {
                                if (!canPlantHealth(TE)) {
                                    if (self instanceof Player PE) {
                                        PE.displayClientMessage(Component.translatable("text.roundabout.vampire.flesh_bud_fail").withStyle(ChatFormatting.RED), true);
                                    }
                                }
                            }
                        } else {
                            if (self instanceof Player PE) {
                                PE.displayClientMessage(Component.translatable("text.roundabout.vampire.flesh_bud_fail_already").withStyle(ChatFormatting.RED), true);
                            }
                        }
                    }
                }

                setAttackTimeDuring(-20);
            }
        }
    }


    @Override
    /**Cancel death, make sure to set player health if you do this*/
    public boolean cheatDeath(DamageSource dsource){
        if (!dsource.is(ModDamageTypes.SUNLIGHT) && !dsource.is(DamageTypes.GENERIC_KILL)
                && !MainUtil.isStandDamage(dsource) && self instanceof Player PE) {
            if (canUseRegen()) {
                if (!onCooldown(PowerIndex.FATE_2_SNEAK)) {
                    PE.setHealth(1);
                    PE.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1), PE);
                    PE.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0), PE);
                    getStandUserSelf().roundabout$setDazed((byte)0);
                    this.setCooldown(PowerIndex.FATE_2_SNEAK, 600);
                    xTryPower(BLOOD_REGEN,true);
                    self.level().playSound(null, self.blockPosition(), ModSounds.VAMPIRE_AWAKEN_EVENT,
                            SoundSource.PLAYERS, 1F, 1F);
                    ((ServerLevel) self.level()).sendParticles(ModParticles.MENACING,
                            self.getX(), self.getY(), self.getZ(),
                            10, 0, 0, 0, 0.1);
                }
                return true;
            }
        }
        return false;
    }

    public static int getLevelUpExpCost(){
        return ClientNetworking.getAppropriateConfig().vampireSettings.expPerVampLevelUp;
    }
    public static int getEXPcap(){
        return ClientNetworking.getAppropriateConfig().vampireSettings.expTypeCapPerDay;
    }
    @Override
    public void addBloodExp(int amt, Entity target){
        VampireData vdata = getVampireData();
        if (vdata.vampireLevel < 40 && self instanceof Player pl){
            int exp = vdata.bloodExp;

            if (target instanceof Npc) {
                int npcExp = vdata.npcExp;
                if (npcExp < getEXPcap()){
                    npcExp = Mth.clamp(npcExp+amt,0,getEXPcap());
                    int change = npcExp - vdata.npcExp;
                    if (change > 0){
                        vdata.bloodExp+=change;
                        vdata.npcExp = npcExp;
                        if (vdata.timeSinceNpc == 0){
                            vdata.timeSinceNpc = 24000;
                        }
                    }
                }
            } else if (target instanceof Animal || target instanceof WaterAnimal || target instanceof AmbientCreature){
                int animalExp = vdata.animalExp;
                if (animalExp < getEXPcap()){
                    animalExp = Mth.clamp(animalExp+amt,0,getEXPcap());
                    int change = animalExp - vdata.animalExp;
                    if (change > 0){
                        vdata.bloodExp+=change;
                        vdata.animalExp = animalExp;
                        if (vdata.timeSinceAnimal == 0){
                            vdata.timeSinceAnimal = 24000;
                        }
                    }
                }
            } else {
                int monsterEXP = vdata.monsterEXP;
                if (monsterEXP < getEXPcap()){
                    monsterEXP = Mth.clamp(monsterEXP+amt,0,getEXPcap());
                    int change = monsterEXP - vdata.monsterEXP;
                    if (change > 0){
                        vdata.bloodExp+=change;
                        vdata.monsterEXP = monsterEXP;
                        if (vdata.timeSinceMonster == 0){
                            vdata.timeSinceMonster = 24000;
                        }
                    }
                }
            }

            if (vdata.bloodExp >= getLevelUpExpCost() && vdata.vampireLevel < 40){
                vdata.bloodExp = 0;
                vdata.vampireLevel+=1;
                if (vdata.vampireLevel == 40){
                    ((ServerPlayer) this.self).displayClientMessage(Component.
                            translatable("leveling.roundabout.max_levelup.vampire").withStyle(ChatFormatting.DARK_RED)
                            .withStyle(ChatFormatting.BOLD), true);
                } else {
                    ((ServerPlayer) this.self).displayClientMessage(Component.
                            translatable("leveling.roundabout.levelup.vampire").withStyle(ChatFormatting.DARK_RED).
                            withStyle(ChatFormatting.BOLD), true);
                }
                S2CPacketUtil.vampireMessage(pl);

            }
            S2CPacketUtil.beamVampireData2(pl);
        }
    }

    @Override
    public float getDamageReduction(DamageSource source, float amt){
        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            return 0.10F + 0.01F*getVampireData().resilienceLevel;
        }
        if (source.is(DamageTypes.ARROW) || source.is(ModDamageTypes.BULLET)){
            return 0.1F + 0.02F*getVampireData().resilienceLevel;
        }
        return super.getDamageReduction(source,amt);
    }
    @Override
    public float getDamageAdd(DamageSource source, float amt, Entity target){
        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            if (target instanceof Player pl){
                return 0.1F + (getVampireData().strengthLevel*0.02F);
            } else {
                return 0.2F + (getVampireData().strengthLevel*0.04F);
            }
        }
        return super.getDamageAdd(source,amt,target);
    }

    /**For enhancement stands that adjust your normal player attack speed*/
    public float getBonusAttackSpeed() {
        return 1.1F+ (0.1F*getVampireData().dexterityLevel);
    }
    /**For enhancement stands that adjust your normal player mining speed*/
    public float getBonusPassiveMiningSpeed(){
        return 1.2F+ (0.4F*getVampireData().dexterityLevel);
    }

    public boolean canPlantDrink(Entity ent) {
        if (MainUtil.canDrinkBloodCritAggro(ent,self) && !(ent instanceof Monster)
                && !(ent instanceof Mob mb && ((IMob)mb).roundabout$isVampire())) {
            return true;
        }
        return false;
    }
    public boolean canPlantHealth(Entity ent) {
        if (ent instanceof LivingEntity LE && LE.getHealth()-getSuckDamage() <= 0){
            return true;
        }
        return false;
    }

    /***/


    public final void fleshBudIfNearby(double range, int entid) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                        S2CPacketUtil.sendGenericIntToClientPacket(serverPlayerEntity,
                                PacketDataIndex.S2C_INT_FLESH_BUD,entid);
                }
            }
        }
    }

    public void tickHypnosis() {
        if (!self.level().isClientSide())
            if (isHypnotizing) {
                if (hypnoTicks % 9 == 0){
                    Vec3 lvec = self.getLookAngle();
                    Position pn = self.getEyePosition().add(lvec.scale(3));
                    Vec3 rev = lvec.reverse();
                    if (hypnoTicks % 18 == 0) {
                        ((ServerLevel) this.self.level()).sendParticles(ModParticles.HYPNO_SWIRL, pn.x(),
                                pn.y(), pn.z(),
                                0,
                                rev.x, rev.y, rev.z,
                                0.08);
                        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.HYPNOSIS_EVENT, SoundSource.PLAYERS, 1F, 1F);

                    }
                    AABB aab = this.getSelf().getBoundingBox().inflate(12.0, 8.0, 12.0);
                    List<? extends LivingEntity> le = this.self.level().getNearbyEntities(Mob.class, hypnosisTargeting, ((LivingEntity)(Object)self), aab);
                    Iterator var4 = le.iterator();
                    while(var4.hasNext()) {
                        Mob nle = (Mob) var4.next();
                        if (!nle.isRemoved() && nle.isAlive() && !nle.isSleeping() &&
                                !(MainUtil.isHypnotismTargetBlacklisted(nle))){
                            if (nle.getTarget() == null || !nle.getTarget().isAlive()
                            || nle.getTarget().isRemoved()){
                                ((IMob) nle).roundabout$setHypnotizedBy(self);
                            }
                        }
                    }

                }
                hypnoTicks++;
            }
    }

    @Override
    public float getSpeedMod(){
        return 1.7F+(0.1F*getVampireData().bloodSpeedLevel);
    }
    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (hasHairExtended())
            basis*=0.8F;
        return super.inputSpeedModifiers(basis);
    }

    public boolean canUseHypnosis(){
        return getVampireData().hypnotismLevel > 0;
    }
    public boolean canUseFleshBud(){
        return getVampireData().fleshBudLevel > 0;
    }
    public boolean canUseBloodSpeedUnlock(){
        return getVampireData().bloodSpeedLevel > 0;
    }

    public boolean isPlant(ItemStack stack){
        if (stack != null && !stack.isEmpty()){
            return (stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof FlowerBlock);
        }
        return false;
    }
    @Override
    public boolean isHoldingPlant(){
        if (isPlant(self.getMainHandItem()) || isPlant(self.getOffhandItem())){
            return true;
        }
        return false;
    }

    private final TargetingConditions hypnosisTargeting = TargetingConditions.forCombat().range(11);
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            if (canUseFleshBud()){
                setSkillIcon(context, x, y, 1, StandIcons.FLESH_BUD, PowerIndex.FATE_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.FATE_1_SNEAK,true);
            }
        } else {
            if (canUseHypnosis()){
                setSkillIcon(context, x, y, 1, StandIcons.HYPNOTISM, PowerIndex.FATE_1);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.FATE_1, true);
            }
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.REGENERATE, PowerIndex.FATE_2_SNEAK);
        } else {
            if (isHoldingPlant()){
                setSkillIcon(context, x, y, 2, StandIcons.FLOWER_DRINK, PowerIndex.FATE_EXTRA);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
            }
        }

        if ((canLatchOntoWall() || (isPlantedInWall() && !isHoldingSneak())) && canWallWalkConfig()) {
            setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
        } else if (isHoldingSneak()) {
            if (canUseBloodSpeedUnlock()){
                setSkillIcon(context, x, y, 3, StandIcons.CHEETAH_SPEED, PowerIndex.FATE_3_SNEAK);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.FATE_3_SNEAK, true);
            }
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        if (!isHoldingSneak() || isHearing()) {
            setSkillIcon(context, x, y, 4, StandIcons.HEARING_MODE, PowerIndex.FATE_4_SNEAK);
        } else {
            if (isVisionOn()){
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_ON, PowerIndex.FATE_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_OFF, PowerIndex.FATE_4);
            }
        }
    }

    public int getMaxAttackTimeDuringHair(){
        return hairChargeLength;
    }

    @Override
    public float hearingDistance(){
        return 15+(3*getVampireData().superHearingLevel);
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {

        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (!standOn){
            Entity TE = getTargetEntity(playerEntity, 7, 15);

            if (getActivePower() == HAIR_EXTENDED){
                float finalATime = (Math.min((float)attackTimeDuring,(float)getMaxAttackTimeDuringHair())) /
                        getMaxAttackTimeDuringHair();
                int barTexture = 0;
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)){
                    barTexture = 68;
                }
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                int finalATimeInt = Math.round(finalATime * 15);
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);
                return;
            }
        }
        super.renderAttackHud(context,playerEntity,scaledWidth,scaledHeight,ticks,vehicleHeartCount,flashAlpha,otherFlashAlpha);
    }

    public AbilityIconInstance drawSingleGUIIconVamp(GuiGraphics context, int size, int startingLeft, int startingTop, int currentLevel, int maxLevel,
                                                     String nameSTR, String instructionStr, ResourceLocation draw, int extra,
                                                     int insert1, int insert2){
        return drawSingleGUIIconVamp(context,size,startingLeft,startingTop,currentLevel,maxLevel,nameSTR,instructionStr,draw,extra,insert1,insert2,-1);
    }
    public AbilityIconInstance drawSingleGUIIconVamp(GuiGraphics context, int size, int startingLeft, int startingTop, int currentLevel, int maxLevel,
                                                     String nameSTR, String instructionStr, ResourceLocation draw, int extra,
                                                     int insert1, int insert2, int levelReq){
        Component name;
        if (currentLevel < maxLevel) {
            context.blit(StandIcons.UNLOCK_SQUARE_ICON, startingLeft, startingTop, 0, 0,size, size, size, size);
        } else {
            context.blit(StandIcons.SQUARE_ICON, startingLeft, startingTop, 0, 0,size, size, size, size);
        }
        context.blit(draw, startingLeft+2, startingTop+2, 0, 0,size-4, size-4, size-4, size-4);
        name = Component.translatable(nameSTR).withStyle(ChatFormatting.BOLD).
                withStyle(ChatFormatting.DARK_PURPLE);
        Component instruction;
        if (extra <= 0) {
            instruction = Component.translatable(instructionStr).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.BLUE);
        } else {
            instruction = Component.translatable(instructionStr, "" + extra).withStyle(ChatFormatting.ITALIC).
                    withStyle(ChatFormatting.BLUE);

        }
        Component description;
        description = Component.translatable(nameSTR+".desc",insert1,insert2);

        Component desc2 = null;
        Component desc3 = null;
        if (currentLevel < maxLevel) {
            desc2 = Component.translatable("ability.roundabout.vampire_growth_room", maxLevel-currentLevel)
                    .withStyle(ChatFormatting.LIGHT_PURPLE);

            if (levelReq > getVampireData().vampireLevel){
                desc3 = Component.translatable(
                        "ability.roundabout.vampire_level_gate", levelReq+1)
                        .withStyle(ChatFormatting.RED);
            } else if (getVampireData().getPoints() >= 1){
                desc3 = Component.translatable(
                                "ability.roundabout.vampire_level_ready", levelReq+1)
                        .withStyle(ChatFormatting.GREEN);
            }
        }

        return new AbilityIconInstance(size,startingLeft,startingTop,currentLevel, maxLevel,
                name,instruction,description,extra,desc2,desc3);
    }


    public VampireData getVampireData(){
        if (self instanceof Player pl){
            return ((IPlayerEntity)pl).rdbt$getVampireData();
        } else {
            return new VampireData(self.level());
        }
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.vampire_passive",
                "instruction.roundabout.passive", StandIcons.VAMPIRE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.blood_drink",
                "instruction.roundabout.press_skill", StandIcons.BLOOD_DRINK,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.mass_heal",
                "instruction.roundabout.press_skill_crouch", StandIcons.REGENERATE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.wall_walk_vamp",
                "instruction.roundabout.press_skill_air", StandIcons.WALL_WALK_VAMP,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.vampire_vision",
                "instruction.roundabout.press_skill_crouch", StandIcons.VAMP_VISION_ON,4,level,bypas));

        VampireData data = getVampireData();

        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+67,topPos+80,
                data.strengthLevel, VampireData.strengthMaxLevel, "ability.roundabout.vamp_strength",
                "instruction.roundabout.passive", StandIcons.VAMPIRE_STRENGTH,0, 20+(data.strengthLevel*4), 10+(data.strengthLevel*2)));
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+67,topPos+99,
                data.dexterityLevel, VampireData.dexterityMaxLevel, "ability.roundabout.vamp_dexterity",
                "instruction.roundabout.passive", StandIcons.VAMPIRE_DEXTERITY,0, 20+(data.dexterityLevel*4), 10+(data.dexterityLevel)));
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+67,topPos+118,
                data.resilienceLevel, VampireData.reslienceMaxLevel, "ability.roundabout.vamp_resilience",
                "instruction.roundabout.passive", StandIcons.VAMPIRE_RESILIENCE,0, 10+(data.resilienceLevel), 10+(data.resilienceLevel*2)));

        String tring = "ability.roundabout.hypnotism.locked";
        if (data.hypnotismLevel > 0)
            tring = "ability.roundabout.hypnotism";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+86,topPos+80,
                data.hypnotismLevel, VampireData.hypnotismMaxLevel, tring,
                "instruction.roundabout.press_skill", StandIcons.HYPNOTISM,1, 0,0));

        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+86,topPos+99,
                data.superHearingLevel, VampireData.superHearingMaxLevel, "ability.roundabout.super_hearing",
                "instruction.roundabout.press_skill", StandIcons.HEARING_MODE,4, 15+(3*data.superHearingLevel),0));

        tring = "ability.roundabout.blood_speed.locked";
        if (data.bloodSpeedLevel > 0)
            tring = "ability.roundabout.blood_speed";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+86,topPos+118,
                data.bloodSpeedLevel, VampireData.bloodSpeedMaxLevel, tring,
                "instruction.roundabout.press_skill_crouch", StandIcons.CHEETAH_SPEED,3, 70+(10*data.bloodSpeedLevel),0));

        tring = "ability.roundabout.grafting.locked";
        if (data.graftingLevel > 0)
            tring = "ability.roundabout.grafting";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+105,topPos+80,
                data.graftingLevel, VampireData.graftingMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.GRAFTING,0, 0,0));

        tring = "ability.roundabout.flesh_bud.locked";
        if (data.fleshBudLevel > 0)
            tring = "ability.roundabout.flesh_bud";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+105,topPos+99,
                data.fleshBudLevel, VampireData.fleshBudMaxLevel, tring,
                "instruction.roundabout.press_skill_crouch", StandIcons.FLESH_BUD,1, 0,0, 4));

        tring = "ability.roundabout.sacrificial_dagger.locked";
        if (data.daggerSplatterLevel > 0)
            tring = "ability.roundabout.sacrificial_dagger";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+105,topPos+118,
                data.daggerSplatterLevel, VampireData.daggerSplatterMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.DAGGER,0, 0,0,14));

        tring = "ability.roundabout.vamp_jump_boost";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+129,topPos+80,
                data.jumpLevel, VampireData.jumpMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.VAMP_JUMP_BOOST,0, 3+(2*data.jumpLevel),
                5+(3*data.jumpLevel)));

        tring = "ability.roundabout.eye_manipulation.locked";
        if (data.ripperEyesLevel > 0)
            tring = "ability.roundabout.eye_manipulation";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+129,topPos+99,
                data.ripperEyesLevel, VampireData.ripperEyesMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.RIPPER_EYES,0, 0,
                0,ripperEyeLevel));

        tring = "ability.roundabout.unleash_the_cold.locked";
        if (data.freezeLevel > 0)
            tring = "ability.roundabout.unleash_the_cold";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+129,topPos+118,
                data.freezeLevel, VampireData.freezeMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.ICE_CLUTCH,0, 0,
                0,19));



        return $$1;
    }
}
