package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityAbstractClient;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;

import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.WalkingHeartEntity;
import net.hydra.jojomod.entity.substand.MoldSporesEntity;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.mixin.PlayerEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.*;

public class PowersGreenDay extends NewPunchingStand {
    public PowersGreenDay(LivingEntity self) {super(self);}




    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }


    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GREEN_DAY.create(this.getSelf().level());
    }



    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGreenDay(entity);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {

        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.GREEN_DAY_PUNCH,1,level,bypas));
        // charge fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+118,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.GREEN_DAY_GUARD,0,level,bypas));
        // burst fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+99,0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.GREEN_DAY_BARRAGE,2,level,bypas));
        // manual scope
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,4, "ability.roundabout.gd_punch_left",
            "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_PUNCH_RIGHT,1,level,bypas));
        // charge fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39, topPos+118,4, "ability.roundabout.gd_return_left",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_ARM_RETURN_RIGHT,1,level,bypas));
        // burst fire
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,5, "ability.roundabout.gd_spin_left",
                "instruction.roundabout.press_skill_block", StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT,1,level,bypas));
        // place ratt
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,3, "ability.roundabout.gd_punch_right",
                "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_PUNCH_LEFT,2,level,bypas));
        // place burst
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,3, "ability.roundabout.gd_return_right",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_ARM_RETURN_LEFT,2,level,bypas));
        // place auto
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99,5, "ability.roundabout.gd_spin_right",
                "instruction.roundabout.press_skill_block", StandIcons.GREEN_DAY_MOLD_SPIN_LEFT,2,level,bypas));
        // dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        // passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.GREEN_DAY_VAULT,3,level,bypas));
        // bucket passive
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,2, "ability.roundabout.gd_mold_leap",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_MOLD_LEAP,3,level,bypas));
        // ratt leap
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.gd_mold_spread",
                "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_SPREAD,4,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,2, "ability.roundabout.gd_stitch",
                "instruction.roundabout.press_skill_crouch", StandIcons.GREEN_DAY_STITCH,4,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.gd_pardon",
                "instruction.roundabout.press_skill_block", StandIcons.GREEN_DAY_PARDON,4,level,bypas));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.gd_mold_field",
                "instruction.roundabout.passive", StandIcons.GREEN_DAY_MOLD_FIELD,4,level,bypas));

        return $$1;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        boolean isSculk = false;
        if (this.getSelf() instanceof Player PE) {
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (!((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin()) {
                if ((this.self.level().getBiome(this.self.getOnPos()).is(Biomes.DEEP_DARK)) && !((IPlayerEntity)this.self).roundabout$getUnlockedBonusSkin()){
                    setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_SCULK_ABSORB, PowerIndex.NONE);
                    isSculk= true;
                }
            }
        }


        if (!isSculk) {
            if (isHoldingSneak())
                if (canExecuteMoveWithLevel(2)) {
                    setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_STITCH, PowerIndex.SKILL_4_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            else if (isGuarding())
                setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_PARDON, PowerIndex.SKILL_4_GUARD);
            else
                setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_MOLD_SPREAD, PowerIndex.SKILL_4);
        }

        if (isHoldingSneak())
            if (canExecuteMoveWithLevel(3)) {
                setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_ARM_RETURN_LEFT, PowerIndex.SKILL_2_SNEAK);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }

        else if(isGuarding() || isBarrageAttacking()) {
            if (isGuarding() || isBarrageAttacking())
                if (canExecuteMoveWithLevel(5)) {
                    setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_MOLD_SPIN_LEFT, PowerIndex.SKILL_2_GUARD);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
        }

        else if (canExecuteMoveWithLevel(3)) {
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_MOLD_PUNCH_LEFT, PowerIndex.SKILL_2);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
        }


        if (isHoldingSneak())
            if (canExecuteMoveWithLevel(2)) {
                setSkillIcon(context, x, y, 3, StandIcons.GREEN_DAY_MOLD_LEAP, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }

        else
        if(isGuarding())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        if (canVault() ) {
            setSkillIcon(context, x, y, 3, StandIcons.GREEN_DAY_VAULT, PowerIndex.GLOBAL_DASH);
        }
        if (isHoldingSneak())
            if (canExecuteMoveWithLevel(4)) {
                setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_ARM_RETURN_RIGHT, PowerIndex.SKILL_1);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }

        else
        if(isGuarding() || isBarrageAttacking())
            if (canExecuteMoveWithLevel(5)) {
                setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
        }

        else
        if (canExecuteMoveWithLevel(4)) {
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_PUNCH_RIGHT, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
        }




        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                attemptMainArmThrow();
            }

            case SKILL_1_CROUCH -> {
                attemptMainArmReturn();
            }

            case SKILL_1_GUARD -> {
                attemptMainArmSpin();
            }
            case SKILL_2_NORMAL -> {
                attemptOffHandThrow();
            }

            case SKILL_2_CROUCH -> {
                attemptOffHandReturn();
            }

            case SKILL_2_GUARD -> {
                attemptOffHandSpin();
            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {

                tryToStandLeapClient();



            }
            case SKILL_4_CROUCH, SKILL_4_CROUCH_GUARD -> {
                Stitch();
            }
            case SKILL_4_NORMAL -> {
                //toggleMold();
                MoldSpreadStart();
            }
            case SKILL_4_GUARD -> {
                selectAllyClient();
            }

        }
    }
    public boolean IsMoldFieldActive = false;
    public static final byte MAIN_ARM_THROW_SLIM = 52;
    public static final byte OFF_HAND_THROW_SLIM = 53;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_4_SNEAK -> {
                return StitchHeal(1.0f, this.self);

            }
            case PowerIndex.POWER_4 -> {
                return MoldSpread();
            }
            case PowerIndex.POWER_4_BLOCK -> {
                return selectAllyServer();
            }
            case PowerIndex.POWER_3_EXTRA -> {
                return moldLeapServer();
            }
            case PowerIndex.POWER_2 -> {
                return OffHandThrowServer(ModEntities.SEPERATED_ARM.create(this.self.level()));
            }

            case PowerIndex.POWER_2_SNEAK -> {
                return OffHandReturnServer();
            }

            case PowerIndex.POWER_2_BLOCK -> {
                return OffHandSpinServer();
            }

            case OFF_HAND_THROW_SLIM -> {
                return OffHandThrowServer(ModEntities.SEPERATED_ARM_SLIM.create(this.self.level()));
            }
            case PowerIndex.POWER_1 -> {
                return MainArmThrowServer(ModEntities.SEPERATED_ARM.create(this.self.level()));
            }

            case PowerIndex.POWER_1_SNEAK -> {
                return MainArmReturnServer();
            }

            case PowerIndex.POWER_1_BLOCK -> {
                return MainArmSpinServer();
            }

            case MAIN_ARM_THROW_SLIM -> {
                return MainArmThrowServer(ModEntities.SEPERATED_ARM_SLIM.create(this.self.level()));
            }

            case PowerIndex.VAULT -> {
                return this.vault();
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    public boolean hasCheckedAllies = false;


    @Override
    public void tickPower() {


        handleSecretSkinThinking();
        moldShenanigans();
        if(Objects.nonNull(this.getStandEntity(this.self))) {
            if (this.getStandEntity(this.self).getAnimation() == GreenDayEntity.MOLD_SPREAD && hmm == 0) {
                this.animateStand(StandEntity.IDLE);
                this.poseStand(OffsetIndex.FOLLOW);
                hmm = -1;
            }
        }
        if(hmm > -1){
            hmm--;
        }
        if(legGoneTicks>0) {
            if (!this.self.level().isClientSide()) {
                for (int i = 0; i < 2; i = i + 1) {
                    double randX = Roundabout.RANDOM.nextDouble(-0.4, 0.4);
                    double randY = Roundabout.RANDOM.nextDouble(-0.4, 0.4);
                    double randZ = Roundabout.RANDOM.nextDouble(-0.4, 0.4);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD,
                            this.getSelf().getX() + randX,
                            this.getSelf().getY()+0.2 + randY,
                            this.getSelf().getZ() + randZ,
                            0, 0, 0, 0, 0);
                }

            }
            legGoneTicks = legGoneTicks - 1;
        }
        if(!(currentlegs == null)) {

            if(!(legGoneTicks>0)) {
                if (!this.self.level().isClientSide()) {
                    currentlegs.discard();
                }
            }else{
                if (!this.self.level().isClientSide()) {
                    if(MainUtil.cheapDistanceTo(this.self.getX(),this.self.getY(),this.self.getZ(),currentlegs.getX(),currentlegs.getY(),currentlegs.getZ())<1 && currentlegs.StartupTicks == 0) {
                        legGoneTicks = 0;
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        ((StandUser) this.self).rdbt$SetCrawlTicks(0);
                        setActivePower(PowerIndex.POWER_3_BONUS);
                        this.updatePowerInt(PowerIndex.POWER_3_BONUS,0);
                        S2CPacketUtil.sendIntPowerDataPacket((Player)this.getSelf(),PowerIndex.POWER_3_BONUS,0);

                        double Xangle = Math.toRadians(this.self.getLookAngle().x);
                        double Pitch = Math.toRadians(this.self.getLookAngle().y);
                        double Zangle = Math.toRadians(this.self.getLookAngle().z);
                        double diameter = 0.4d;
                        for (int i = 0; i < 11; i = i + 1) {
                            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                                    this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                                    this.getSelf().getY() + 0.5,
                                    this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                                    0, 0, 0, 0, 0);
                        }
                    }
                }else{
                    if(MainUtil.cheapDistanceTo(this.self.getX(),this.self.getY(),this.self.getZ(),currentlegs.getX(),currentlegs.getY(),currentlegs.getZ())<1.5 && currentlegs.StartupTicks == 0 ) {
                        legGoneTicks = 0;
                        ((StandUser) this.self).rdbt$SetCrawlTicks(0);
                    }
                }

            }
        }else{
        }
        if(!hasCheckedAllies) {
            generateAllyList();
            hasCheckedAllies=true;
        }
        super.tickPower();



    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowerIndex.POWER_3_BONUS -> {
                ((StandUser)this.self).rdbt$SetCrawlTicks(data);
                legGoneTicks = data;
            }

        }
        super.updatePowerInt(activePower,data);
    }

    /**
      Secret skin
     */


    public int secretSkinObtainmentTimer = 0;
    public void handleSecretSkinThinking(){
        if(secretSkinObtainmentTimer>0){
            secretSkinObtainmentTimer --;
            if(secretSkinObtainmentTimer == 40){
                sculkBurst(2);
            }else if(secretSkinObtainmentTimer == 27){
                sculkBurst(1);
            }else if(secretSkinObtainmentTimer == 14){
                sculkBurst(0.5);
            }else if (secretSkinObtainmentTimer == 1){
                sculkFinish();
            }
        }
    }
    public void sculkFinish() {

        if (this.getSelf() instanceof Player PE) {
            StandEntity stand = this.getStandEntity(this.self);
            Level lv = this.self.level();
            StandUser user = ((StandUser) PE);
            if (Objects.nonNull(stand)) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SCULK_SOUL, stand.getX(),
                        stand.getY() + 1, stand.getZ(),
                        63,
                        0.5, 1, 0.5,
                        0);

                if (!lv.isClientSide()) {
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    ipe.roundabout$setUnlockedBonusSkin(true);
                    lv.playSound(null, PE.getX(), PE.getY(),
                            PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                    ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                            PE.getY() + PE.getEyeHeight(), PE.getZ(),
                            10, 0.5, 0.5, 0.5, 0.2);
                    user.roundabout$setStandSkin(GreenDayEntity.SILENCE);
                    ((ServerPlayer) ipe).displayClientMessage(
                            Component.translatable("unlock_skin.roundabout.green_day.silence"), true);
                    user.roundabout$summonStand(lv, true, false);
                }
            }
        }
    }

    public void sculkBurst(double range){
        StandEntity stand = this.getStandEntity(this.self);
        if(Objects.nonNull(stand)){
            ((ServerLevel) this.self.level()).sendParticles(new SculkChargeParticleOptions(3), stand.getX(),
                    stand.getY() + 1, stand.getZ(),
                    84,
                    (double) range, (double) range, (double) range ,
                    0);

        }
    }
    /**
     * Mold Spread Work
     */
    public void MoldSpreadStart(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
                tryPowerPacket(PowerIndex.POWER_4);
                this.setCooldown(PowerIndex.SKILL_4,ClientNetworking.getAppropriateConfig().greenDaySettings.moldSpreadCooldown);
        }
    }

    private int hmm = 0;



    public boolean MoldSpread() {
        if((this.self.level().getBiome(this.self.getOnPos()).is(Biomes.DEEP_DARK)) && !((IPlayerEntity)this.self).roundabout$getUnlockedBonusSkin()){
            secretSkinObtainmentTimer = 41;
        } else if (!isClient() && !this.isBarraging()) {

            StandEntity stand = getStandEntity(this.self);
            if(Objects.nonNull(stand)) {
                animateStand(GreenDayEntity.MOLD_SPREAD);
                this.poseStand(OffsetIndex.ATTACK);
                hmm = 20;
                MoldSporesEntity SLE = ModEntities.MOLD_SPORES.create(this.self.level());
                if (SLE != null) {
                    SLE.setUser(this.self);
                    SLE.setXRot(this.self.getXRot());
                    SLE.setYRot(this.self.getYRot());
                    SLE.setPos(this.getRayBlock(this.self,2).add(0,1,0));

                    SLE.setDeltaMovement(0, 0.7, 0);
                    this.self.level().addFreshEntity(SLE);
                }
            }
            //moldBurst(this.self.getOnPos().getCenter(),3);
            //moldBurst(this.self.getOnPos().getCenter().add(0,-10,0),7);
            //moldBurst(this.self.getOnPos().getCenter().add(0,-27,0),10);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_MOLD_SPREAD_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);

        }
        return true;
    }


    /**
     * getting the right hand for left handed mobs = players n stuff idk look man
     */

    public void attemptMainArmThrow(){
        if(canExecuteMoveWithLevel(4)) {
            if(!this.self.isUsingItem()){
                MainArmThrow();
            }
        }

    }
    public void attemptMainArmReturn(){
        if(canExecuteMoveWithLevel(4)) {
            MainArmReturn();
        }

    }
    public void attemptMainArmSpin(){
        if(canExecuteMoveWithLevel(3)) {
            MainArmSpin();
        }

    }

    public void attemptOffHandThrow(){
        if(!this.self.isUsingItem()) {
            if (canExecuteMoveWithLevel(3)) {
                OffHandThrow();
            }
        }
    }
    public void attemptOffHandReturn(){
        if(canExecuteMoveWithLevel(3)) {
            OffHandReturn();
        }
    }
    public void attemptOffHandSpin(){
        if(canExecuteMoveWithLevel(5)) {

            OffHandSpin();
        }

    }


    /**
     * Off Hand stuff
     */


    public SeperatedArmEntity Off_hand_entity = null;
    public boolean HasOffHandCharge = true;
    public boolean HasOffHand = true;

    public boolean OffHandThrowServer(SeperatedArmEntity SAE){
        if (Off_hand_entity == null) {
            if (SAE != null) {
                Off_hand_entity = SAE;
                SAE.setUser(this.self);
                SAE.setXRot(this.self.getXRot());
                SAE.setYRot(this.self.getYRot());
                SAE.setPos(getRayBlock(this.self,0.5f).add(0,-0.3,0));
                SAE.setItemInHand(InteractionHand.MAIN_HAND,this.self.getItemInHand(InteractionHand.OFF_HAND).copy());
                this.self.level().addFreshEntity(SAE);
                SAE.jump(this.getRayBlock(this.self, 20F));
                Off_hand_entity= SAE;
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_SPLIT_EVENT, SoundSource.PLAYERS, 1.0F, 2.0F);
            }
            this.self.getOffhandItem().setCount(0);
            Vec3 location = getRayBlock(this.self, 1f);

            // ((ServerLevel) this.self.level()).sendParticles(ModParticles.MOLD_DUST, location.x,
            //       location.y, location.z,
            //     24,
            //   0.005, 0.005, 0.005,
            // 0.1);
        }else{
            //if(!(((StandUser) this.self).roundabout$getTargetEntity(this.self,20)==null)) {
            //    SAE.jump(((StandUser) this.self).roundabout$getTargetEntity(this.self,20F).getOnPos().);
            //}else{

            Off_hand_entity.jump(this.getRayBlock(this.self, 20F));
            Off_hand_entity.jump2(((StandUser)this.self).roundabout$getTargetEntity(this.self,20).getEyePosition());




            //}
        }
        return true;
    }


    public void OffHandThrow(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            if(isBarrageAttacking() && !HasOffHand){
                OffHandSpin();
            }else {
                if (HasOffHandCharge) {
                    HasOffHandCharge = false;
                } else {
                    this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().greenDaySettings.armThrowCooldown);
                    HasOffHandCharge = true;
                }
                if (isClient()) {
                    AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) this.self;
                    if ((abstractClientPlayer).getModelName().equals("default")) {
                        tryPowerPacket(PowerIndex.POWER_2);
                    } else {
                        tryPowerPacket(OFF_HAND_THROW_SLIM);
                        // tryPowerPacket(PowerIndex.POWER_1);
                    }

                }
                HasOffHand = false;
            }
        }

    }
    public void OffHandReturn(){
        if(!HasOffHand){
            this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().greenDaySettings.armThrowCooldown * 2);
            HasOffHand = true;
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }

    public boolean OffHandReturnServer() {
        ItemEntity $$2 = new ItemEntity(this.self.level(), this.self.getX(), this.self.getY() + 1, this.self.getZ(),Off_hand_entity.getMainHandItem());
        //this.self.level().addFreshEntity($$2);
        Player player = (Player)this.self;
        if(this.self.getOffhandItem().getItem() instanceof AirItem){
            this.self.setItemInHand(InteractionHand.OFF_HAND,Off_hand_entity.getMainHandItem());
        }else {
            this.self.spawnAtLocation(Off_hand_entity.getMainHandItem());
        }

        Off_hand_entity.setUser(null);
        Off_hand_entity.discard();
        Off_hand_entity = null;
        double Xangle = Math.toRadians(this.self.getLookAngle().x);
        double Pitch = Math.toRadians(this.self.getLookAngle().y);
        double Zangle = Math.toRadians(this.self.getLookAngle().z);
        double diameter = 0.6d;
        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        for (int i = 0; i < 11; i = i + 1) {
            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                    this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                    this.getSelf().getY() + (this.getSelf().getEyeHeight() * 0.7),
                    this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                    0, 0, 0, 0, 0);
        }
        return true;
    }

    public void OffHandSpin(){
        if(!this.onCooldown(PowerIndex.SKILL_2)) {
            if(HasOffHand){
                OffHandThrow();
            }
            tryPowerPacket(PowerIndex.POWER_2_BLOCK);
            setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig().greenDaySettings.armSpinCooldown);
        }


    }

    public boolean OffHandSpinServer(){
        this.self.level().playSound(null, Off_hand_entity.blockPosition(), ModSounds.GREEN_DAY_ARM_SPIN_EVENT, SoundSource.PLAYERS, 1.0F, 2.0F);
        Off_hand_entity.setSpinTicks(ClientNetworking.getAppropriateConfig().greenDaySettings.armSpinDuration);
        Off_hand_entity.flyingTicks = 0;
        return true;
    }



    /**
     * Main Arm Stuff
     */

    public SeperatedArmEntity Main_arm = null;

    public void MainArmSpin(){
        if(!this.onCooldown(PowerIndex.SKILL_1)) {
            if(HasMainArm){
                MainArmThrow();
            }
            tryPowerPacket(PowerIndex.POWER_1_BLOCK);
            setCooldown(PowerIndex.SKILL_1,ClientNetworking.getAppropriateConfig().greenDaySettings.armSpinCooldown );
        }


    }

    public boolean MainArmSpinServer(){
            this.self.level().playSound(null, Main_arm.blockPosition(), ModSounds.GREEN_DAY_ARM_SPIN_EVENT, SoundSource.PLAYERS, 1.0F, 2.0F);
            Main_arm.setSpinTicks(ClientNetworking.getAppropriateConfig().greenDaySettings.armSpinDuration);
            Main_arm.flyingTicks = 0;
        return true;
    }

    public boolean MainArmThrowServer(SeperatedArmEntity SAE){
        if (Main_arm == null) {
            if (SAE != null) {
                Main_arm = SAE;
                SAE.setUser(this.self);
                SAE.setXRot(this.self.getXRot());
                SAE.setYRot(this.self.getYRot());
                SAE.setPos(getRayBlock(this.self,0.5f).add(0,-0.3,0));
                SAE.setItemInHand(InteractionHand.MAIN_HAND,this.self.getItemInHand(InteractionHand.MAIN_HAND).copy());
                this.self.level().addFreshEntity(SAE);
                SAE.jump(this.getRayBlock(this.self,20F));
                Main_arm = SAE;
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_SPLIT_EVENT, SoundSource.PLAYERS, 1.0F, 2.0F);
            }
            this.self.getMainHandItem().setCount(0);
            Vec3 location = getRayBlock(this.self, 1f);

           // ((ServerLevel) this.self.level()).sendParticles(ModParticles.MOLD_DUST, location.x,
             //       location.y, location.z,
               //     24,
                 //   0.005, 0.005, 0.005,
                   // 0.1);
        }else{

            Main_arm.jump(this.getRayBlock(this.self, 20F));
            Main_arm.jump2(((StandUser)this.self).roundabout$getTargetEntity(this.self,20).getEyePosition());
        }
        return true;
    }

    public boolean HasMainArmCharge = true;

    public void MainArmThrow(){
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            if(isBarrageAttacking() && !HasMainArm){
                MainArmSpin();
            }else {
                if (HasMainArmCharge) {
                    HasMainArmCharge = false;
                } else {
                    this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().greenDaySettings.armThrowCooldown);
                    HasMainArmCharge = true;
                }
                if (isClient()) {
                    AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) this.self;
                    if ((abstractClientPlayer).getModelName().equals("default")) {
                        tryPowerPacket(PowerIndex.POWER_1);
                    } else {
                        tryPowerPacket(MAIN_ARM_THROW_SLIM);
                       // tryPowerPacket(PowerIndex.POWER_1);
                    }

                }
                HasMainArm = false;
            }
        }

    }
    public boolean HasMainArm = true;
    public void MainArmReturn(){
        if(!HasMainArm){
            this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig().greenDaySettings.armThrowCooldown * 2);
            HasMainArm = true;
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }

    public boolean MainArmReturnServer() {
        ItemEntity $$2 = new ItemEntity(this.self.level(), this.self.getX(), this.self.getY() + 1, this.self.getZ(),Main_arm.getMainHandItem());
        $$2.setDefaultPickUpDelay();
        //this.self.level().addFreshEntity($$2);
        Player player = (Player)this.self;
        if(this.self.getMainHandItem().getItem() instanceof AirItem){
            this.self.setItemInHand(InteractionHand.MAIN_HAND,Main_arm.getMainHandItem());
        }else {
            this.self.spawnAtLocation(Main_arm.getMainHandItem());
        }
        //this.self.spawnAtLocation(Main_arm.getMainHandItem());
        Main_arm.setUser(null);
        Main_arm.discard();
        Main_arm = null;
        double Xangle = Math.toRadians(this.self.getLookAngle().x);
        double Pitch = Math.toRadians(this.self.getLookAngle().y);
        double Zangle = Math.toRadians(this.self.getLookAngle().z);
        double diameter = 0.6d;
        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        for (int i = 0; i < 11; i = i + 1) {
            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                    this.getSelf().getX() + (diameter * Math.sin(i * 4)) * Math.cos(Xangle),
                    this.getSelf().getY() + (this.getSelf().getEyeHeight() * 0.7),
                    this.getSelf().getZ() + (diameter * Math.cos(i * 4)) * Math.cos(Zangle),
                    0, 0, 0, 0, 0);
        }
        return true;
    }

    /**
     * Ally List Stuff
     */



    public boolean selectAllyServer(){
        listInit();
        if(!(((StandUser) this.self).roundabout$getTargetEntity(this.self,16)==null)) {
            Entity targetEnt = ((StandUser) this.self).roundabout$getTargetEntity(this.self, 16);
            if(!allies.contains(targetEnt.getStringUUID())) {
                allies.add(targetEnt.getStringUUID());
            }
            else{
                allies.remove(targetEnt.getStringUUID());
            }


        }
        allies = cleanseAllyList(allies);
        S2CPacketUtil.sync_allies(((Player)this.self),listToString(allies));
        return true;

    }

    public void selectAllyClient(){
        listInit();
        if(!(((StandUser) this.self).roundabout$getTargetEntity(this.self,16)==null)) {
            Entity targetEnt = ((StandUser) this.self).roundabout$getTargetEntity(this.self, 16);
            if(!allies.contains(targetEnt.getStringUUID())) {
                allies.add(targetEnt.getStringUUID());
            }
            else{
                allies.remove(targetEnt.getStringUUID());
            }

            tryPowerPacket(PowerIndex.POWER_4_BLOCK);
        }

        saveAllies();


    }

    public List<String> allies = new ArrayList<>();

    public  List<String> ConvertToString(){
        String cf = ConfigManager.getClientConfig().greenDayAllyList.getFromMemory();
        return allyListParser(cf);
    }

    public void generateAllyList(){
        listInit();
        allies = ConvertToString();
    }

    public void saveAllies(){
        String cf = listToString(allies);
        ConfigManager.getClientConfig().greenDayAllyList.saveToMemory(cf);
        ConfigManager.saveClientConfig();
    }

    public List<String> cleanseAllyList(List<String>  allies){
        List<String> newAllies = new ArrayList<>();
        Iterable<ServerLevel> serverLevels = this.self.getServer().getAllLevels();
        Iterator<ServerLevel> levels = serverLevels.iterator();
        while (levels.hasNext()) {
            ServerLevel level = levels.next();
            for (int i = 0; i < allies.size(); i++) {
                String current = allies.get(i);
                if(level.getEntity(UUID.fromString(current)) != null){
                    newAllies.add(current);
                }
            }

        }
        return newAllies;

    }



    public List<String> allyListParser(String string){
        List<String> allies = new ArrayList<>();
        String temporary = "";

        for (int i = 0; i < string.length(); i++) {
            char current = string.charAt(i);
            if (!(current == ',')){
                temporary += current;
            }else{
                allies.add(temporary);
                temporary = "";
            }
        }

        return allies;
    }




    public String listToString(List<String> allies){
        String allyString = "";

        for (int i = 0; i < allies.size(); i++) {
            String current = allies.get(i);
            allyString += current;
            allyString += ",";
        }

        return allyString;
    }


    public void listInit(){
        if (allies == null) {
            allies = new ArrayList<>();
        }
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        if(((StandUser)player).roundabout$isGuarding()) {
            if (allies.contains(ent.getStringUUID()) && player.hasLineOfSight(ent)){
                return true;
            } else if (!(((StandUser) player).roundabout$getTargetEntity(player, 16) == null)) {
                if (((StandUser) player).roundabout$getTargetEntity(player, 16).equals(ent)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        listInit();
        if(allies.contains(ent.getStringUUID())){
            if(!(((StandUser) player).roundabout$getTargetEntity(player,16)== null)) {
                if (((StandUser) player).roundabout$getTargetEntity(player, 16).equals(ent)) {
                    return 12379456;
                }
            }
            return 10349135;
        }
        return 15526430;
    }

    public void Stitch() {
        if (canExecuteMoveWithLevel(2)) {
            if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK)) {
                this.setCooldown(PowerIndex.SKILL_4_SNEAK, ClientNetworking.getAppropriateConfig().greenDaySettings.gDStitchcooldown);

                this.tryPower(PowerIndex.POWER_4_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_4_SNEAK);
            }
        }
    }

    public void toggleMold(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.setCooldown(PowerIndex.SKILL_4, 0);
            this.setCooldown(PowerIndex.SKILL_4_GUARD, 0);
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }

    }
    public int getLeapLevel(){
        return 3;
    }
    public int bonusLeapCount = -1;
    public void bigLeap(LivingEntity entity,float range, float mult){

        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(1).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                ((blockHit.getLocation().x - this.getSelf().getX())/mag)*mult*2.2,
                (0.3+ (Math.max((blockHit.getLocation().y - this.getSelf().getY())/mag,0))*1) * mult,
                ((blockHit.getLocation().z - this.getSelf().getZ())/mag)*mult*2.2
        );

    }

    public void tryToStandLeapClient() {
            if (vaultOrFallBraceFails()) {
                if (this.getSelf().onGround()) {
                    boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                    if ((jojoveinLikeKeys && !this.onCooldown(PowerIndex.SKILL_3)) ||
                            (!jojoveinLikeKeys && !this.onCooldown(PowerIndex.GLOBAL_DASH))) {
                        if (canExecuteMoveWithLevel(2)) {
                            if (jojoveinLikeKeys) {
                                this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                            } else {
                                this.setCooldown(PowerIndex.GLOBAL_DASH, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                                //this.setCooldown(PowerIndex.SNEAK_MOVEMENT, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                            }
                            setcrawlserver(this.self);
                            legGoneTicks = 240;
                            ((StandUser) this.self).rdbt$SetCrawlTicks(240);
                            getBarrageWindup();
                            addEXP(3);


                            tryPowerPacket(PowerIndex.POWER_3_EXTRA);

                            bonusLeapCount = 3;
                            bigLeap(this.getSelf(), 20, 1);
                            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                            ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
                        }
                    }
                }
            }
    }
    public int legGoneTicks = 0;
    public SeperatedLegsEntity currentlegs;
    public boolean moldLeapServer() {
        legGoneTicks = 240;
        for(int i = 0; i < 11; i = i + 1) {
            double randX = Roundabout.RANDOM.nextDouble(-0.5, 0.5);
            double randY = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
            double randZ = Roundabout.RANDOM.nextDouble(-0.5, 0.5);
            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD,
                    this.getSelf().getX(),
                    this.getSelf().getY() + 1 ,
                    this.getSelf().getZ(),
                    1,randX,randY,randZ,0.12);
        }
        if(!(currentlegs == null)){
            currentlegs.discard();
        }

        ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
        ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(false);
        setcrawlserver(this.self);
        SpawnLegs();
        return true;
    }

    public boolean setcrawlserver(LivingEntity entity) {
        if(!this.self.level().isClientSide()) {
            ((StandUser) entity).rdbt$SetCrawlTicks(240);
        }
        return true;
    }

    public void SpawnLegs(){
        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_SPLIT_EVENT, SoundSource.PLAYERS, 1.0F, 1.5F);
        SeperatedLegsEntity SLE = ModEntities.SEPERATED_LEGS.create(this.self.level());
        if(SLE != null) {
            SLE.setUser(this.self);
            SLE.setXRot(this.self.getXRot());
            SLE.setYRot(this.self.getYRot());
            SLE.setPos(this.self.getPosition(1).add(0,0.2,0));
            this.self.level().addFreshEntity(SLE);
            currentlegs = SLE;
        }
    }
    public boolean StitchHeal(float hp, LivingEntity entity) {

        float HealVal = ClientNetworking.getAppropriateConfig().greenDaySettings.greenDayStitchHeal;
        if(!isClient()) {
            addEXP(3);
            float maxhp = entity.getMaxHealth();
            float currenthp = entity.getHealth();

            if (currenthp < maxhp - (HealVal)) {
                entity.setHealth(currenthp + HealVal);
            }else{
                entity.setHealth(maxhp);
            }
            if (entity.hasEffect(ModEffects.BLEED)) {
                int level = entity.getEffect(ModEffects.BLEED).getAmplifier();
                int duration = entity.getEffect(ModEffects.BLEED).getDuration();
                entity.removeEffect(entity.getEffect(ModEffects.BLEED).getEffect());
                if (level > 0) {
                    entity.addEffect(new MobEffectInstance(ModEffects.BLEED, duration, level - 1));
                }
            }

            double Xangle = Math.toRadians(this.self.getLookAngle().x);
            double Pitch = Math.toRadians(this.self.getLookAngle().y);
            double Zangle = Math.toRadians(this.self.getLookAngle().z);
            double diameter = 0.6d;
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.GREEN_DAY_STITCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            for(int i = 0; i < 11; i = i + 1) {
                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                        this.getSelf().getX() + (diameter * Math.sin(i*4)) * Math.cos(Xangle),
                        this.getSelf().getY() + (this.getSelf().getEyeHeight()*0.7),
                        this.getSelf().getZ() + (diameter * Math.cos(i*4)) * Math.cos(Zangle),
                        0,0,0,0,0);
            }


        }
        return true;
    }

    @Override
    public byte getPermaCastContext() {
        return PermanentZoneCastInstance.MOLD_FIELD;
    }

        public boolean toggleMoldField() {
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                icast.roundabout$addPermaCaster(this.getSelf());

            } else {
                removeMold();
            }
        }
        return true;
    }

    public void removeMold() {
        IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
        icast.roundabout$removePermaCastingEntity(this.getSelf());
    }

    public void moldShenanigans() {
        if (!this.getSelf().level().isClientSide()) {
            if(isMoldFieldOn()) {
                for(int i = 0; i < 84; i = i + 1) {
                    double randX = Roundabout.RANDOM.nextDouble(-50, 50);
                    double randY = Roundabout.RANDOM.nextDouble(-50, 50);
                    double randZ = Roundabout.RANDOM.nextDouble(-50, 50);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD_DUST,
                            this.getSelf().getX() + randX,
                            this.getSelf().getY() + randY,
                            this.getSelf().getZ() + randZ,
                            0,1,-1,1,0.12);

                }
            }
        }
    }

    public boolean isMoldFieldOn() {
        return((IPermaCasting) this.getSelf().level()).roundabout$isPermaCastingEntity(this.self);
    };

    @Override
    public void playSummonEffects(boolean forced) {
        if (!this.getSelf().level().isClientSide()) {
            for(int i = 0; i < 23; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-1, 1);
                double randY = Roundabout.RANDOM.nextDouble(-1, 2);
                double randZ = Roundabout.RANDOM.nextDouble(-1, 1);
                ((ServerLevel) this.getSelf().level()).sendParticles(new DustParticleOptions(new Vector3f(0.76F, 1.0F, 0.9F
                ), 2f),
                        this.getSelf().getX() + randX,
                        this.getSelf().getY() + randY,
                        this.getSelf().getZ() + randZ,
                        0,0,0.2,0,0);

            }
        }
    }

    public void tryToDashClient(){
        if (!doVault()){
            dash();
        }
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (attackTarget != null && attackTarget.isAlive()){
            if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.BARRAGE)
                    || attackTarget.distanceTo(this.getSelf()) <= 5){
                rotateMobHead(attackTarget);
            }
            double RNG = Math.random();
            if((this.self.getHealth() < this.self.getMaxHealth()) && (RNG > 0.99)){
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
            }
            if(this.self instanceof FlyingMob || this.self instanceof Phantom){
                if( (RNG > 0.99)){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
                }
            }else
            if(!(this.self.onGround()) && (RNG > 0.9)){
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
            }

            Entity targetEntity = getTargetEntity(this.self, -1);
            if (targetEntity != null && targetEntity.is(attackTarget)) {
                if (this.attackTimeDuring <= -1) {
                    if (RNG < 0.35 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge){
                        wentForCharge = true;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                    } else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                        wentForCharge = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                    }
                }
            }
        }
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 5) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    @Override
    public byte getMaxLevel(){
        return 5;
    }

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_GREEN_DAY_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.45F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4.6F));
        }
    }


    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.GOLD);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "Fish").withStyle(ChatFormatting.GREEN);
    }


    public static final byte
            PART_FIVE_GREEN_DAY = 1,
            RED_DAY = 2,
            TEAL_DAY = 3,
            BROCCOLLI = 4,
            RED_NIGHT = 5,
            GORGONZOLA = 6,
            SILENCE = 7,
            TF_CENTURY = 8,
            AMERICAN_IDIOT = 9,
            NIMROD = 10,
            SAVIOURS = 11,
            MOUTH = 12;


    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId) {
        if (skinId == GreenDayEntity.PART_FIVE_GREEN_DAY) {
            return Component.translatable("skins.roundabout.green_day.part_five_green_day");
        } else if (skinId == GreenDayEntity.RED_DAY) {
            return Component.translatable("skins.roundabout.green_day.red_day");
        } else if (skinId == GreenDayEntity.TEAL_DAY) {
            return Component.translatable("skins.roundabout.green_day.teal_day");
        } else if (skinId == GreenDayEntity.BROCOLLI) {
            return Component.translatable("skins.roundabout.green_day.broccoli");
        } else if (skinId == GreenDayEntity.RED_NIGHT) {
            return Component.translatable("skins.roundabout.green_day.red_night");
        } else if (skinId == GreenDayEntity.GORGONZOLA) {
            return Component.translatable("skins.roundabout.green_day.gorgonzola");
        } else if (skinId == GreenDayEntity.SILENCE ){
            return Component.translatable("skins.roundabout.green_day.silence");
        } else if (skinId == GreenDayEntity.TF_CENTURY ){
        return Component.translatable("skins.roundabout.green_day.21_century");
        } else if (skinId == GreenDayEntity.AMERICAN_IDIOT ){
        return Component.translatable("skins.roundabout.green_day.american_idiot");
        } else if (skinId == GreenDayEntity.NIMROD ){
        return Component.translatable("skins.roundabout.green_day.nimrod");
        } else if (skinId == GreenDayEntity.SAVIOURS ){
        return Component.translatable("skins.roundabout.green_day.saviours");
        } else if (skinId == GreenDayEntity.MOUTH ){
        return Component.translatable("skins.roundabout.green_day.mouth");
        }

        return Component.translatable(  "skins.roundabout.green_day.part_five_green_day");
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(PART_FIVE_GREEN_DAY);
        //$$1.add(TEAL_DAY);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(SAVIOURS);

            } if (Level > 2 || bypass){
                $$1.add(NIMROD);

            } if (Level > 3 || bypass){
                $$1.add(TF_CENTURY);

            } if (Level > 4 || bypass){
                $$1.add(AMERICAN_IDIOT);
                $$1.add(MOUTH);

            } if (Level > 5 || bypass){


            } if (Level > 6 || bypass){

            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(SILENCE);
                ;
            }
        }
        return $$1;

    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().greenDaySettings.enableGreenDay;

    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                greenDaySettings.greenDayAttackMultOnMobs*0.01));
    }
    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                greenDaySettings.greenDayAttackMultOnPlayers*0.01));
    }
    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().greenDaySettings.greenDayGuardPoints;
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                greenDaySettings.miningSpeedMultiplierGreenDay *0.01));
    }

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().greenDaySettings.getMiningTierGreenDay;
    }
}
