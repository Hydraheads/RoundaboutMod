package net.hydra.jojomod.fates.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
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


    @Override
    public void drawOtherGUIElements(Font font, GuiGraphics context, float delta, int mouseX, int mouseY, int i, int j, ResourceLocation rl){
        context.blit(rl, i +80, j + 19, 192, 152, 20, 20);
        int bloodAmt = 100;
        int bloodLmt = 200;
        int blt = (int) Math.floor(((double) 20 / bloodLmt) * (bloodAmt));
        int blt2 = 20-blt;
        if (blt > 0){
            context.blit(rl, i +80, j + 19+blt2, 192, 173+blt, 20, blt);
        }

        context.blit(rl, i +102, j + 19, 214, 152, 20, 20);
        bloodAmt = 40;
        blt = (int) Math.floor(((double) 20 / bloodLmt) * (bloodAmt));
        blt2 = 20-blt;
        if (blt > 0){
            context.blit(rl, i +102, j + 19+blt2, 214, 173+blt2, 20, blt);
        }

        context.blit(rl, i +124, j + 19, 236, 152, 20, 20);
        bloodAmt = 130;
        blt = (int) Math.floor(((double) 20 / bloodLmt) * (bloodAmt));
        blt2 = 20-blt;
        if (blt > 0){
            context.blit(rl, i +124, j + 19+blt2, 236, 173+blt2, 20, blt);
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
                    0);
        //display = Component.translatable("leveling.roundabout.disc_maxed",
        //        0);
        context.drawString(font, display, i  +80, j+64, 4210752, false);

        int points = 2;
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
        int exp = 100;
        int maxXP = 200;
        blt = (int) Math.floor(((double) 92 / maxXP) * (exp));
        context.blit(rl, ss, sss, 10, 244, 92, 4);
        context.blit(rl, ss, sss, 10, 248, blt, 4);
    }


    @Override

    public void handleCustomGUIClick(int i, int j, double mouseX, double mouseY){
        if (isSurelyHovering(i +147, j + 20,19,18,mouseX,mouseY)){
            if (self.level().isClientSide()){
                ClientUtil.openHairspryUI();
            }
        }
    }

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
        if (!onCooldown(PowerIndex.FATE_1_SNEAK) || activePower == HAIR_EXTENDED) {
            if (activePower != HAIR_EXTENDED) {
                this.setCooldown(PowerIndex.FATE_1_SNEAK, 44);
            }
            tryPowerPacket(HAIR_EXTENDED);
        }
    }
    public void hypnosis(){
        if (isHearing()){
            stopHearingClient();
        }
        tryPowerPacket(HYPNOSIS);
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
        }
        return super.setPowerOther(move,lastMove);
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

    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (slot == 3 && isPlantedInWall() && !isHoldingSneak() && !canLatchOntoWall())
            return true;
        if (slot == 3 && isHoldingSneak() && !canUseBloodSpeed() && !canLatchOntoWall())
            return true;
        if (slot == 2 && isHoldingSneak() && !canUseRegen())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
    }
    @Override
    public float getJumpHeightAddon(){
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
        return 4;
    }
    public float getAddon(){
        if (activePower == BLOOD_REGEN)
            return 0;

        if (self.isCrouching() && rechargeJump){
            return 4;
        } else {
            if (jumpedOffWall)
                return 0;
            return 2;
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
            if (getPlayerPos2() == PlayerPosIndex.HAIR_EXTENSION){
                animationProgress++;
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

    @Override
    public float getDamageReduction(DamageSource source, float amt){
        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            return 0.10F + 0.01F*resilienceLevel;
        }
        if (source.is(DamageTypes.ARROW) || source.is(ModDamageTypes.BULLET)){
            return 0.1F + 0.02F*resilienceLevel;
        }
        return super.getDamageReduction(source,amt);
    }
    @Override
    public float getDamageAdd(DamageSource source, float amt, Entity target){
        if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
            if (target instanceof Player pl){
                return 0.1F + (strengthLevel*0.02F);
            } else {
                return 0.2F + (strengthLevel*0.04F);
            }
        }
        return super.getDamageAdd(source,amt,target);
    }

    /**For enhancement stands that adjust your normal player attack speed*/
    public float getBonusAttackSpeed() {
        return 1.1F+ (0.1F*dexterityLevel);
    }
    /**For enhancement stands that adjust your normal player mining speed*/
    public float getBonusPassiveMiningSpeed(){
        return 1.2F+ (0.4F*dexterityLevel);
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
        return 1.5F+(0.1F*bloodSpeedLevel);
    }
    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (hasHairExtended())
            basis*=0.8F;
        return super.inputSpeedModifiers(basis);
    }

    private final TargetingConditions hypnosisTargeting = TargetingConditions.forCombat().range(11);
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.FLESH_BUD, PowerIndex.FATE_1_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.HYPNOTISM, PowerIndex.FATE_1);
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.REGENERATE, PowerIndex.FATE_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
        }

        if ((canLatchOntoWall() || (isPlantedInWall() && !isHoldingSneak())) && canWallWalkConfig()) {
            setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
        } else if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.CHEETAH_SPEED, PowerIndex.FATE_3_SNEAK);
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
        return 15+(3*superHearingLevel);
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {

        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
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

    public int strengthLevel = 0;
    public static int strengthMaxLevel = 5;
    public int dexterityLevel = 0;
    public static int dexterityMaxLevel = 5;
    public int resilienceLevel = 0;
    public static int reslienceMaxLevel = 5;

    public int hypnotismLevel = 0;
    public static int hypnotismMaxLevel = 1;
    public int superHearingLevel = 0;
    public static int superHearingMaxLevel = 5;
    public int bloodSpeedLevel = 0;
    public static int bloodSpeedMaxLevel = 5;

    public int graftingLevel = 0;
    public static int graftingMaxLevel = 1;
    public int fleshBudLevel = 0;
    public static int fleshBudMaxLevel = 1;
    public int daggerSplatterLevel = 0;
    public static int daggerSplatterMaxLevel = 1;

    public int jumpLevel = 0;
    public static int jumpMaxLevel = 1;
    public int ripperEyesLevel = 0;
    public static int ripperEyesMaxLevel = 5;
    public int freezeLevel = 0;
    public static int freezeMaxLevel = 5;

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


        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+67,topPos+80,
                strengthLevel, strengthMaxLevel, "ability.roundabout.vamp_strength",
                "instruction.roundabout.passive", StandIcons.VAMPIRE_STRENGTH,0, 10+(strengthLevel*2), 20+(strengthLevel*4)));
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+67,topPos+99,
                dexterityLevel, dexterityMaxLevel, "ability.roundabout.vamp_dexterity",
                "instruction.roundabout.passive", StandIcons.VAMPIRE_DEXTERITY,0, 10+(dexterityLevel), 20+(resilienceLevel*4)));
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+67,topPos+118,
                resilienceLevel, reslienceMaxLevel, "ability.roundabout.vamp_resilience",
                "instruction.roundabout.passive", StandIcons.VAMPIRE_RESILIENCE,0, 10+(resilienceLevel), 10+(resilienceLevel*2)));

        String tring = "ability.roundabout.hypnotism.locked";
        if (hypnotismLevel > 0)
            tring = "ability.roundabout.hypnotism";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+86,topPos+80,
                hypnotismLevel, hypnotismMaxLevel, tring,
                "instruction.roundabout.press_skill", StandIcons.HYPNOTISM,1, 0,0));

        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+86,topPos+99,
                superHearingLevel, superHearingMaxLevel, "ability.roundabout.super_hearing",
                "instruction.roundabout.press_skill", StandIcons.HEARING_MODE,4, 15+(3*superHearingLevel),0));

        tring = "ability.roundabout.blood_speed.locked";
        if (bloodSpeedLevel > 0)
            tring = "ability.roundabout.blood_speed";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+86,topPos+118,
                bloodSpeedLevel, bloodSpeedMaxLevel, tring,
                "instruction.roundabout.press_skill_crouch", StandIcons.CHEETAH_SPEED,3, 50+(10*bloodSpeedLevel),0));

        tring = "ability.roundabout.grafting.locked";
        if (graftingLevel > 0)
            tring = "ability.roundabout.grafting";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+105,topPos+80,
                graftingLevel, graftingMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.GRAFTING,0, 0,0));

        tring = "ability.roundabout.flesh_bud.locked";
        if (fleshBudLevel > 0)
            tring = "ability.roundabout.flesh_bud";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+105,topPos+99,
                fleshBudLevel, fleshBudMaxLevel, tring,
                "instruction.roundabout.press_skill_crouch", StandIcons.FLESH_BUD,1, 0,0));

        tring = "ability.roundabout.sacrificial_dagger.locked";
        if (daggerSplatterLevel > 0)
            tring = "ability.roundabout.sacrificial_dagger";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+105,topPos+118,
                daggerSplatterLevel, daggerSplatterMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.DAGGER,0, 0,0));

        tring = "ability.roundabout.vamp_jump_boost";
        $$1.add(drawSingleGUIIconVamp(context,18,leftPos+128,topPos+80,
                jumpLevel, jumpMaxLevel, tring,
                "instruction.roundabout.passive", StandIcons.VAMP_JUMP_BOOST,0, 3+(2*jumpLevel),
                5+(3*jumpLevel)));


        return $$1;
    }
}
