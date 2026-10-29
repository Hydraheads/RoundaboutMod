package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IBlockState;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.projectile.MetallicaKnifeEntity;
import net.hydra.jojomod.entity.stand.LonesomeRopeEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.hydra.jojomod.util.MainUtil.raytraceEntity;

public class PowersLonesome extends NewDashPreset {
    public PowersLonesome(LivingEntity self) {
        super(self);
    }

    boolean isPhaseable(String blockName){
        System.out.println(Objects.equals(blockName, "minecraft:iron_bars"));
        return (Objects.equals(blockName, "minecraft:iron_bars"));
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().ohLonesomeMeSettings.enableOhLonesomeMe;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersLonesome(entity);
    }
    public boolean ropeExists = false;



    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        setSkillIcon(context, x, y, 1, StandIcons.LONESOME_ARM_LAUNCH, PowerIndex.SKILL_1);

        /**It is sneak because all stands share this cooldown and SP/TW
         * shared it between dash and stand leap*/
        setSkillIcon(context, x, y, 2, StandIcons.LONESOME_LASSO, PowerIndex.SKILL_2);
        if(!isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.LONESOME_ZIPLINE, PowerIndex.SKILL_3);
        }
        setSkillIcon(context, x, y, 4, StandIcons.LONESOME_CRAWL, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                armLaunchClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                lassoClient();
            }
            case SKILL_3_NORMAL -> {
                dash();
            }
            case SKILL_3_CROUCH -> {
                ziplineClient();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                switchCrawlModeClient();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return armLaunch();
            }

            case PowerIndex.POWER_2 -> {
                //return scoutForOresOnClient();
            }

            case PowerIndex.POWER_3_SNEAK -> {
                //return scoutForOresOnClient();
            }

            case PowerIndex.POWER_4 -> {
                return switchCrawlMode();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public void armLaunchClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            this.tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }

    public void lassoClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            this.tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }

    public void ziplineClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            this.tryPower(PowerIndex.POWER_2_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }

    public void switchCrawlModeClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }

    public boolean crawlingOn(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }

    public boolean switchCrawlMode(){
        LivingEntity player = this.self;
        if (isPhaseable(getLookingAt(player))) {
            Vec3 blockPos = getRayBlockHit(player, 2f).getBlockPos().getCenter();
            player.setPos(blockPos);
            System.out.println(getRayBlockHit(player, 2f));
        } else {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(!crawlingOn());
        }
        return true;
    }

    public boolean armLaunch(){
        if (onCooldown(PowerIndex.SKILL_1)) return false;
        self.swing(InteractionHand.MAIN_HAND, true);

        if(!self.level().isClientSide){
            if (!ropeExists) {
                Vec3 eyePos = self.getEyePosition();

                createRopeEntity(eyePos.x, eyePos.y, eyePos.z);

                //playSoundIfPossible(self.level(),null, pos, SoundEvents.HOE_TILL, SoundSource.PLAYERS, 0.5f, 1.0f);
                ropeExists = true;
            }
        }




        return true;
    }
    private void createRopeEntity(double x, double y, double z) {
        LonesomeRopeEntity rope = new LonesomeRopeEntity(self.level(), self);
        rope.setPos(x, y, z);
        rope.setStopped(false);
        rope.setInvisible(false);
        self.level().addFreshEntity(rope);
    }

    public String getLookingAt(LivingEntity player){
        Level level = this.self.level();
        BlockPos blockPos = getRayBlockHit(player, 2f).getBlockPos();
        String blockName = level.getBlockState(blockPos).getBlock().getName().toString();
        return blockName;
    }

    public boolean crawl(){
        LivingEntity player = this.self;
        Level level = this.self.level();
        if (crawlingOn()){
            ((StandUser) player).rdbt$SetCrawlTicks(1);
        }
        return true;
    }

    public boolean cheatDeath(DamageSource dsource){
        if (dsource.is(DamageTypes.EXPLOSION) || dsource.is(DamageTypes.FALL) || dsource.is(DamageTypes.PLAYER_EXPLOSION)) {
            if (!onCooldown(PowerIndex.EXTRA)) {
                LivingEntity player = this.self;
                player.setHealth(1);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2), player);
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0), player);
                getStandUserSelf().roundabout$setUniqueStandModeToggle(true);
                getStandUserSelf().roundabout$setDazed((byte)0);
                this.setCooldown(PowerIndex.EXTRA, ClientNetworking.getAppropriateConfig().ohLonesomeMeSettings.endOfYourRopeCooldown);
                System.out.println(PowerIndex.EXTRA);
                xTryPower(PowerIndex.EXTRA,true);
                //playSoundIfPossible(self.level(), null, self.blockPosition(), ModSounds.ROPE_SOUND_EFFECT, // [NOT YET IMPLEMENTED]
                //SoundSource.PLAYERS, 1F, 1F);
                }
                return true;
        }
        return false;
    }


    @Override
    public void tickStandRejection(MobEffectInstance effect) {
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget){
    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP, slot);
    }


    @Override
    public void tickPower() {
        crawl();
        super.tickPower();
    }


    @Override
    public void updateIntMove(int in) {
        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }

    public static final byte
            YAP = 1;

    public static final byte
            MANGA = 1;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA
        );
    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            //case GOTHIC -> Component.translatable("skins.roundabout.hey_ya.gothic");
            default -> Component.translatable("skins.roundabout.Lonesome.manga");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    } // </3
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.HEY_YA_SUMMON_EVENT;
            }
            //case YAP_1 -> {
            //    return ModSounds.HEY_YA_1_EVENT;
            //}
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public boolean isWip(){
        return true;
    }

    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.GRAY);
    }

    @Override
    public Component ifWipListDev(){
        return Component.literal(  "BinaryCell").withStyle(ChatFormatting.DARK_GRAY);
    }



    public byte worthinessType(){
        return HUMANOID_WORTHY;
    }
    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.hey_ya_2");
        } else {
            return Component.translatable("idle.roundabout.hey_ya_1");
        }
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.arm_launch",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_ARM_LAUNCH, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.lasso",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_LASSO,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.zipline",
                "instruction.roundabout.press_skill_air", StandIcons.LONESOME_ZIPLINE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.crawl",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_CRAWL,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0, "ability.roundabout.going_through",
                "instruction.roundabout.press_skill", StandIcons.LONESOME_GOING_THROUGH,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 0, "ability.roundabout.end_of_your_rope",
                "instruction.roundabout.passive", StandIcons.LONESOME_END_OF_YOUR_ROPE,0,level,bypass));
        return $$1;
    }
}