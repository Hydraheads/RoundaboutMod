package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

import static net.hydra.jojomod.event.index.PowerIndex.POWER_2;

public class PowersCatchTheRainbow extends NewDashPreset {
    public PowersCatchTheRainbow(LivingEntity self) {
        super(self);
    }

    /**
     * general definition stuff
     **/
    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return false;
    }

    @Override
    public boolean rendersPlayer() {
        return true;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersCatchTheRainbow(entity);
    }

    @Override
    public boolean isStandEnabled() {
        // turn this into a config
        return true;
    }

    //cool summon vfx sfx
    @Override
    public void playSummonEffects(boolean forced) {
        if (!this.getSelf().level().isClientSide()) {
            for(int i = 0; i < 23; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-1, 1);
                double randY = Roundabout.RANDOM.nextDouble(-1, 2);
                double randZ = Roundabout.RANDOM.nextDouble(-1, 1);
                sendParticlesIfPossible(self.level(),new DustParticleOptions(new Vector3f(0.76F, 1.0F, 0.9F
                        ), 2f),
                        this.getSelf().getX() + randX,
                        this.getSelf().getY() + randY,
                        this.getSelf().getZ() + randZ,
                        0,0,0.2,0,0);

            }
        }
    }
    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                //change
                return ModSounds.GREEN_DAY_MOLD_SPREAD_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    //poses
    @Override
    public Component getPosName(byte posID) {
        switch (posID){
            case 1 -> {return Component.translatable("idle.roundabout.catch_the_rainbow2"); }
            case 2 -> {return Component.translatable("idle.roundabout.catch_the_rainbow3"); }
        }
        return Component.translatable("idle.roundabout.catch_the_rainbow1");
    }

    @Override
    public List<Byte> getPosList() {
        List<Byte> listy = Lists.newArrayList();
        listy.add((byte) 0);
        listy.add((byte) 1);
        listy.add((byte) 2);
        return listy;
    }

    //skins
    public static final byte
            BASE = 1,
            GHAST = 2,
            LALI_HO = 3,
            EYE = 4;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                BASE,
                GHAST,
                LALI_HO,
                EYE
        );
    }

    @Override public Component getSkinName(byte skinId) {
        if (this.self != null) {
            if (!isInRain()) {
                return switch (skinId) {
                    case GHAST -> Component.translatable("skins.roundabout.catch_the_rainbow.ghast_dry");
                    case LALI_HO -> Component.translatable("skins.roundabout.catch_the_rainbow.lali_ho");
                    case EYE -> Component.translatable("skins.roundabout.catch_the_rainbow.eye_white");
                    default -> Component.translatable("skins.roundabout.catch_the_rainbow.base");
                };
            } else {
                return switch (skinId) {
                    case GHAST -> Component.translatable("skins.roundabout.catch_the_rainbow.ghast_happy");
                    case LALI_HO -> Component.translatable("skins.roundabout.catch_the_rainbow.lali_ho");
                    case EYE -> Component.translatable("skins.roundabout.catch_the_rainbow.eye_red");
                    default -> Component.translatable("skins.roundabout.catch_the_rainbow.base");
                };
            }
        }
        else{
            return switch (skinId) {
                case GHAST -> Component.translatable("skins.roundabout.catch_the_rainbow.ghast_dry");
                case LALI_HO -> Component.translatable("skins.roundabout.catch_the_rainbow.lali_ho");
                case EYE -> Component.translatable("skins.roundabout.catch_the_rainbow.eye_white");
                default -> Component.translatable("skins.roundabout.catch_the_rainbow.base");
            };
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (!isInRain())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else if (isHoldingSneak() && !self.onGround())
            setSkillIcon(context, x, y, 3, StandIcons.CATCH_THE_RAINBOW_DROP_DOWN, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.CATCH_THE_RAINBOW_RAIN_DASH, PowerIndex.GLOBAL_DASH);
        if (isHoldingSneak())
            setSkillIcon(context, x, y, 1, StandIcons.CATCH_THE_RAINBOW_RAIN_MEND, PowerIndex.SKILL_1_SNEAK);
        setSkillIcon(context, x, y, 2, StandIcons.CATCH_THE_RAINBOW_CHOKE, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 4, StandIcons.CATCH_THE_RAINBOW_FULL_DODGE, PowerIndex.SKILL_4);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = com.google.common.collect.Lists.newArrayList();

        // manual scope
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.ratt_scope",
                "instruction.roundabout.press_skill", StandIcons.RATT_SCOPE_IN,1,level,bypas));
        // charge fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+118,0, "ability.roundabout.ratt_fire",
                "instruction.roundabout.hold_block", StandIcons.RATT_BURST,0,level,bypas));
        // burst fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+99,0, "ability.roundabout.ratt_mode_change",
                "instruction.roundabout.press_skill", StandIcons.RATT_SINGLE,2,level,bypas));
        // place ratt
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.ratt_place",
                "instruction.roundabout.press_skill", StandIcons.RATT_PLACE,2,level,bypas));
        // place burst
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.ratt_place_burst",
                "instruction.roundabout.press_skill", StandIcons.RATT_BURST,1,level,bypas));
        // place auto
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.ratt_auto",
                "instruction.roundabout.press_skill_crouch", StandIcons.RATT_AUTO,1,level,bypas));
        // dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        // passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99,0, "ability.roundabout.ratt_flesh",
                "instruction.roundabout.passive", StandIcons.RATT_BLOB,3,level,bypas));
        // bucket passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,0, "ability.roundabout.ratt_bucket",
                "instruction.roundabout.passive", StandIcons.RATT_BUCKET,3,level,bypas));

        return $$1;
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (!isInRain()) {
            if (slot == 1) {
                return true;
            }
            if (slot == 2) {
                return true;
            }
            if (slot == 4) {
                return true;
            }
        }
        if (slot == 1 && !canUseRainMend())
            return true;
        if (slot == 2 && HasOffHand && isHoldingSneak()){
                return true;
        } else if (slot == 2 && !HasOffHand && !isHoldingSneak())
                return true;

        return super.isAttackIneptVisually(activeP, slot);
    }


    private static final byte
    RAINMEND = 53,
    DROPDOWN = 54,
    OFF_HAND_THROW_SLIM = 55,
    RETURN = 56;

    @Override
    public void powerActivate(PowerContext context){
        switch (context){
            case SKILL_1_CROUCH -> rainMendClient();

            case SKILL_3_NORMAL -> {
                if (!isInRain())
                {dash();}
                else
                {rainDash();}
            }

            case SKILL_3_CROUCH -> {
                if  (!this.getSelf().onGround() && isInRain())
                {dropDownClient();}
            }

            case SKILL_2_NORMAL -> {
                if (isInRain())
                    {OffHandThrow();}
            }
            case SKILL_2_CROUCH -> {
                if (isInRain())
                {OffHandReturn();}
            }
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced){
        if (canChangePower(move, forced)) {
            if (move == PowerIndex.NONE) {
                this.setPowerNone();
            } else if (move == PowerIndex.MOVEMENT) {
                this.setPowerMovement(move);
            } else {
                this.setPowerOther(move,this.getActivePower());
            }
        }
        return super.tryPower(move,forced);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == RAINMEND) {
            useRainMend();
        }
        else if (move == DROPDOWN) {
            dropDown();
        }
        else if (move == POWER_2) {
            return OffHandThrowServer(ModEntities.LEFT_SEPERATED_ARM.create(this.self.level()));
        }
        else if (move == OFF_HAND_THROW_SLIM) {
            return OffHandThrowServer(ModEntities.LEFT_SEPERATED_ARM_SLIM.create(this.self.level()));
        }
        else if (move == RETURN) {
            return OffHandReturnServer();
        }
        return super.setPowerOther(move,lastMove);
    }
    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowerIndex.POWER_2 -> {
                LeftArmState = data;
            }
        }
        super.updatePowerInt(activePower,data);
    }
    public int LeftArmState = 0;

    public boolean isInRain() {
        BlockPos $$0 = this.self.blockPosition();
        return this.self.level().isRainingAt($$0)
                || this.self.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.self.getBoundingBox().maxY, (double)$$0.getZ()));
    }

    //rain speed
    @Override
    public float inputSpeedModifiers(float basis) {
        if (isInRain() && PowerTypes.hasStandActive(self)){
            if (!(this.getSelf() instanceof Mob)) {
                basis *= this.getSelf().isSprinting() ? 1.35F : 1F;
            }
        }
        return super.inputSpeedModifiers(basis);
    }

    //life clutch
    public int deathTimer = 0;

    @Override
    public void tickPower() {
        super.tickPower();
        if (deathTimer != 0) deathTimer --;

        if (isInRain() && hasStandActive(self)) {
            this.getSelf().resetFallDistance();
        }
        if(this.self instanceof Player) {
            if (!this.self.level().isClientSide) {
                if (HasOffHand) {
                    LeftArmState = 0;
                } else {
                    LeftArmState = 1;
                }
                this.updatePowerInt(PowerIndex.POWER_2, LeftArmState);
                S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(), PowerIndex.POWER_2, LeftArmState);

                if(Off_hand_entity == null){
                    HasOffHand = true;
                }
            }else{
                if(LeftArmState == 0){
                    HasOffHand = true;
                }else{
                    HasOffHand = false;
                }
            }
        }
        if(!(currentarm == null)) {
            if(!(armGoneTicks>0)) {
                if (!this.self.level().isClientSide()) {
                    currentarm.discard();
                }
            }else{
                if (!this.self.level().isClientSide()) {
                    if(MainUtil.cheapDistanceTo(this.self.getX(),this.self.getY(),this.self.getZ(),currentarm.getX(),currentarm.getY(),currentarm.getZ())<1 && currentarm.StartupTicks == 0) {
                        armGoneTicks = 0;
                        playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);

                        double Xangle = Math.toRadians(this.self.getLookAngle().x);
                        double Zangle = Math.toRadians(this.self.getLookAngle().z);
                        double diameter = 0.4d;
                        for (int i = 0; i < 11; i = i + 1) {
                            sendParticlesIfPossible(self.level(),ParticleTypes.SPLASH,
                                    this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                                    this.getSelf().getY() + 0.5,
                                    this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                                    0, 0, 0, 0, 0);
                        }
                    }
                }else{
                    if(MainUtil.cheapDistanceTo(this.self.getX(),this.self.getY(),this.self.getZ(),currentarm.getX(),currentarm.getY(),currentarm.getZ())<1.5 && currentarm.StartupTicks == 0 ) {
                        armGoneTicks = 0;
                    }
                }
            }

            if (currentarm.position() == this.getSelf().position()){
                OffHandReturn();
            }
        }
    }

    public SeperatedArmEntity currentarm;
    public int armGoneTicks = 0;

    @Override
    public boolean cheatDeath(DamageSource dsource){
        if (!dsource.is(ModDamageTypes.SUNLIGHT) && !dsource.is(DamageTypes.GENERIC_KILL)
                && self instanceof Player PE) {
            if (isInRain() && deathTimer == 0 && hasStandActive(self)) {
                deathTimer = 1200;
                PE.setHealth(1);
                PE.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1), PE);
                PE.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 10), PE);
                getStandUserSelf().roundabout$setDazed((byte)0);
                playSoundIfPossible(self.level(), null, self.blockPosition(), ModSounds.VAMPIRE_AWAKEN_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);
                return true;
            }
        }
        return false;
    }

    //the f###ing healing move that i hate

    public void rainMendClient(){
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.tryPower(RAINMEND, true);
            tryPowerPacket(RAINMEND);
        }
    }

    public LivingEntity getMendTarget(){
        Entity TE =getTargetEntity(this.self, 2);
        if (TE instanceof LivingEntity LV){
            return LV;
        }
        return null;
    }

    public boolean canUseRainMend(){
        if (isInRain()) {
            return (self.getHealth() < self.getMaxHealth() || self.hasEffect(ModEffects.BLEED)) ||
                    (self instanceof Player PE && PE.isCreative() || getMendTarget() != null);
        }
        else return false;
    }

    public void useRainMend() {
        if (/*!this.self.level().isClientSide() &&*/ !onCooldown(PowerIndex.SKILL_1_SNEAK) && canUseRainMend()){
            Entity ent = self;

            if (ent instanceof LivingEntity LV && ent.isAlive()) {
                this.setCooldown(PowerIndex.SKILL_1_SNEAK, 400);
                LV.heal(2f);

                MobEffectInstance bleed = LV.getEffect(ModEffects.BLEED);
                if (bleed != null) {
                    if (bleed.getAmplifier() > 0) {
                        int amp = bleed.getAmplifier() - 1;
                        LV.removeEffect(bleed.getEffect());
                        LV.addEffect(new MobEffectInstance(ModEffects.BLEED, bleed.getDuration(), amp));
                    } else {
                        LV.removeEffect(bleed.getEffect());
                    }
                }

                double Xangle = Math.toRadians(this.self.getLookAngle().x);
                double Pitch = Math.toRadians(this.self.getLookAngle().y);
                double Zangle = Math.toRadians(this.self.getLookAngle().z);
                double diameter = 0.6d;
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                for (int i = 0; i < 11; i = i + 1) {
                    sendParticlesIfPossible(self.level(), ParticleTypes.SPLASH,
                            this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                            this.getSelf().getY() + (this.getSelf().getEyeHeight() * 0.7),
                            this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                            0, 0, 0, 0, 0);
                }
            }
        }
    }

    //drop down (add damage)

    public void dropDownClient(){
        if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
            this.tryPower(DROPDOWN, true);
            tryPowerPacket(DROPDOWN);
        }
    }

    public void dropDown(){
        if (!this.self.level().isClientSide() && !this.onCooldown(PowerIndex.GLOBAL_DASH)) {
            this.setCooldown(PowerIndex.GLOBAL_DASH, 20);
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 2F,
                    0,
                    3,
                    0);
        }
    }

    //choke

    public Vec3 rayCastFromSelf(double dist){
        int steps = (int) (Math.round(dist * 6) + 1);
        Vec3 CurrentCheckPos = this.self.getEyePosition();
        double xstep = (this.self.getLookAngle().x)/6 ;
        double ystep = (self.getLookAngle().y)/6 ;
        double zstep = (self.getLookAngle().z)/6 ;

        for(int i = 0; i < steps ;i ++){
            CurrentCheckPos = CurrentCheckPos.add(xstep,ystep,zstep);

            // if(!this.self.level().isClientSide) {
            //     sendParticlesIfPossible(self.level(),ParticleTypes.END_ROD, CurrentCheckPos.x, CurrentCheckPos.y, CurrentCheckPos.z, 1, 0, 0, 0, 0);
            // }
            BlockPos bp = (BlockPos.containing(CurrentCheckPos));
            if(this.self.level().getBlockState(bp).getBlock() != Blocks.AIR
                    && this.self.level().getBlockState(bp).getBlock()!=Blocks.WATER
                    && this.self.level().getBlockState(bp).getBlock()!=Blocks.LAVA
                    && !this.self.level().getBlockState(bp).canBeReplaced()
            ) {
                // if(!this.self.level().isClientSide) {

                //     sendParticlesIfPossible(self.level(),ParticleTypes.END_ROD, bp.getCenter().x, bp.getCenter().y, bp.getCenter().z, 1, 0, 0, 0, 0);
                // }
                return CurrentCheckPos;
            }
        }
        return CurrentCheckPos;
    }

    public SeperatedArmEntity Off_hand_entity = null;
    public boolean HasOffHandCharge = true;
    public boolean HasOffHand = true;

    public ItemStack OffhandItemToReturn;

    public boolean OffHandThrowServer(SeperatedArmEntity SAE){

        if (Off_hand_entity == null) {
            if (SAE != null) {
                Off_hand_entity = SAE;
                SAE.setUser(this.self);
                SAE.setXRot(this.self.getXRot());
                PowerTypes.copyPlaneOfExisting(self, SAE);
                SAE.setYRot(this.self.getYRot());
                SAE.setPos(getRayBlock(this.self, 0.5f).add(0, -0.3, 0));
                SAE.setItemInHand(InteractionHand.MAIN_HAND, this.self.getItemInHand(InteractionHand.OFF_HAND).copy());
                this.self.level().addFreshEntity(SAE);
                SAE.jump(rayCastFromSelf(20));
                Off_hand_entity = SAE;
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GREEN_DAY_SPLIT_EVENT, SoundSource.PLAYERS, 1.0F, 2.0F);
            }
            HasOffHand = false;
            this.self.getItemInHand(InteractionHand.OFF_HAND).setCount(0);
            Vec3 location = getRayBlock(this.self, 1f);

        }
        return true;
    }

    public void OffHandThrow(){
        if (!this.onCooldown(PowerIndex.SKILL_2) && isInRain()) {

                if (HasOffHandCharge && !HasOffHand) {
                    HasOffHandCharge = false;
                } else {
                    this.setCooldown(PowerIndex.SKILL_2, 60);
                    HasOffHandCharge = true;
                }
                if (isClient()) {
                    AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) this.self;
                    if ((abstractClientPlayer).getModelName().equals("default")) {
                        tryPowerPacket(POWER_2);
                    } else {
                        tryPowerPacket(OFF_HAND_THROW_SLIM);
                    }
                }
                //HasOffHand = false;
        }
    }

    public void OffHandReturn(){
        if(!HasOffHand && !this.onCooldown(PowerIndex.SKILL_2) && isInRain()){
            this.setCooldown(PowerIndex.SKILL_2, 200);
            //HasOffHand = true;
            tryPowerPacket(RETURN);
        }
    }

    public boolean OffHandReturnServer() {
        if(!HasOffHand) {
            ItemEntity $$2 = new ItemEntity(this.self.level(), this.self.getX(), this.self.getY() + 1, this.self.getZ(), Off_hand_entity.getMainHandItem());
            //this.self.level().addFreshEntity($$2);
            Player player = (Player) this.self;
            if (this.self.getOffhandItem().getItem() instanceof AirItem) {
                OffhandItemToReturn = Off_hand_entity.getMainHandItem();
            } else {
                OffhandItemToReturn = Off_hand_entity.getMainHandItem();
                // ItemEntity item = new ItemEntity(this.self.level(), this.self.getX(), this.self.getY() + 2, this.self.getZ(), Off_hand_entity.getMainHandItem());
                //$$2.setPickUpDelay(1);
                // this.self.level().addFreshEntity($$2);
            }

            Off_hand_entity.setUser(null);
            Off_hand_entity.discard();
            Off_hand_entity = null;
            HasOffHand = true;
            double Xangle = Math.toRadians(this.self.getLookAngle().x);
            double Pitch = Math.toRadians(this.self.getLookAngle().y);
            double Zangle = Math.toRadians(this.self.getLookAngle().z);
            double diameter = 0.6d;
            playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            for (int i = 0; i < 11; i = i + 1) {
                sendParticlesIfPossible(self.level(), ParticleTypes.SPLASH,
                        this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                        this.getSelf().getY() + (this.getSelf().getEyeHeight() * 0.7),
                        this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                        0, 0, 0, 0, 0);
            }
        }
        return true;
    }

    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("hasoffhand",HasOffHand);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("hasoffhand")) {
            HasOffHand = $$0.getBoolean("hasoffhand");
        }
    }

    //rain dodge
    public void rainDash(){
        Options options = Minecraft.getInstance().options;

        inputDash = true;
        if (this.getSelf().level().isClientSide && !this.isClashing()) {
            if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                    byte forward = 0;
                    byte strafe = 0;
                    if (options.keyUp.isDown()) forward++;
                    if (options.keyDown.isDown()) forward--;
                    if (options.keyLeft.isDown()) strafe++;
                    if (options.keyRight.isDown()) strafe--;
                    int degrees = (int) (this.getSelf().getYRot() % 360);
                    int backwards = 0;

                    if (strafe > 0 && forward == 0) {
                        degrees -= 90;
                        degrees = degrees % 360;
                        backwards = 1;
                    } else if (strafe > 0 && forward > 0) {
                        degrees -= 45;
                        degrees = degrees % 360;
                        backwards = 2;
                    } else if (strafe > 0) {
                        degrees -= 135;
                        degrees = degrees % 360;
                        backwards = -1;
                    } else if (strafe < 0 && forward == 0) {
                        degrees += 90;
                        degrees = degrees % 360;
                        backwards = 3;
                    } else if (strafe < 0 && forward > 0) {
                        degrees += 45;
                        degrees = degrees % 360;
                        backwards = 4;
                    } else if (strafe < 0) {
                        degrees += 135;
                        degrees = degrees % 360;
                        backwards = -2;
                    } else if (forward < 0) {
                        degrees += 180;
                        degrees = degrees % 360;
                        backwards = -3;
                    }

                    int buffer = 0;
                    int cdTime = ClientNetworking.getAppropriateConfig().generalStandSettings.dashCooldown / 6;
                    if (this.getSelf() instanceof Player) {
                        ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(0);
                    }
                    this.setCooldown(PowerIndex.GLOBAL_DASH, cdTime);
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.91F,
                            Mth.sin(degrees * ((float) Math.PI / 180)),
                            Mth.sin(-20 * ((float) Math.PI / 180)),
                            -Mth.cos(degrees * ((float) Math.PI / 180)));

                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.MOVEMENT, true);
                    tryIntPowerPacket(PowerIndex.MOVEMENT, backwards+buffer);
                }
            }
        }
    }

    //wip description
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
        return Component.literal(  "truppo").withStyle(ChatFormatting.LIGHT_PURPLE);
    }
}
