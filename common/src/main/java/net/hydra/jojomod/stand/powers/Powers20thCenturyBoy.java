package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.models.layers.animations.CenturyBoyAnimations;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.Arrays;
import java.util.List;

/*
things to do before release:
take him off wip tab

 */
public class Powers20thCenturyBoy extends NewDashPreset {
    public Powers20thCenturyBoy(LivingEntity self){super(self);}

    public boolean invincibleState = false;
    public int staticMode = 0;
    public int mode = 1;

    /** general definition stuff **/
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
        return new Powers20thCenturyBoy(entity);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        switch (mode) {
            case 1 ->{
                setSkillIcon(context, x, y, 1, StandIcons.GROUND_STANCE, PowerIndex.SKILL_1);
            }
            case 2 ->{
                setSkillIcon(context, x, y, 1, StandIcons.NEUTRAL_STANCE, PowerIndex.SKILL_1);
            }
            case 3 ->{
                setSkillIcon(context, x, y, 1, StandIcons.KNOCKBACK_STANCE, PowerIndex.SKILL_1);
            }
            case 4 ->{
                setSkillIcon(context, x, y, 1, StandIcons.REDSTONE_STANCE, PowerIndex.SKILL_1);
            }
        }
        if (!invincibleState){
            setSkillIcon(context,x,y,2, StandIcons.TOGGLE_INVINCIBILITY, PowerIndex.SKILL_2);
        } else{
            setSkillIcon(context,x,y,2, StandIcons.DETOGGLE_INVINCIBILITY, PowerIndex.SKILL_2);
        }

        setSkillIcon(context,x,y,3,StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        super.renderIcons(context, x, y);
    }

    /** wip stuffz **/
    public boolean isWip(){return true;}
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.DARK_PURPLE);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Waiter").withStyle(ChatFormatting.WHITE);
    }

    @Override
    public boolean isStandEnabled() {return ClientNetworking.getAppropriateConfig().centuryBoySettings.enableCenturyBoy;}

    /** skins stuff **/
    public static final byte
        MANGA = 1,
        BEDROCK = 2,
        BEE = 3,
        BIKER = 4,
        RETRO = 5,
        GOLD = 6,
        OLD_CENTURY_BOY = 7,
        SHULKER = 8,
        SULFUR = 9,
        LEMON = 10,
        BLUE = 11,
        GRAPE = 12,
        STRAWBERRY = 13,
        CHICKEN = 14,
        OLDER_CENTURY_BOY = 15,
        OLDEST_CENTURY_BOY = 16,
        SALMONBERRY = 17,
        BETA = 18;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                BEDROCK,
                BEE,
                BIKER,
                RETRO,
                GOLD,
                OLD_CENTURY_BOY,
                SHULKER,
                SULFUR,
                LEMON,
                BLUE,
                GRAPE,
                STRAWBERRY,
                SALMONBERRY,
                CHICKEN,
                OLDER_CENTURY_BOY,
                OLDEST_CENTURY_BOY,
                BETA

        );
    }

    @Override
    public Component getSkinName(byte skinId) {
        return switch (skinId){
            case Powers20thCenturyBoy.BEDROCK -> Component.translatable("skins.roundabout.20_centuryboy.bedrock");
            case Powers20thCenturyBoy.BEE -> Component.translatable("skins.roundabout.20_centuryboy.bee");
            case Powers20thCenturyBoy.BIKER -> Component.translatable("skins.roundabout.20_centuryboy.biker");
            case Powers20thCenturyBoy.RETRO -> Component.translatable("skins.roundabout.20_centuryboy.retro");
            case Powers20thCenturyBoy.GOLD -> Component.translatable("skins.roundabout.20_centuryboy.gold");
            case Powers20thCenturyBoy.OLD_CENTURY_BOY -> Component.translatable("skins.roundabout.20_centuryboy.19th_century_boy");
            case Powers20thCenturyBoy.SHULKER -> Component.translatable("skins.roundabout.20_centuryboy.shulker");
            case Powers20thCenturyBoy.SULFUR -> Component.translatable("skins.roundabout.20_centuryboy.sulfur");
            case Powers20thCenturyBoy.LEMON -> Component.translatable("skins.roundabout.20_centuryboy.lemon");
            case Powers20thCenturyBoy.BLUE -> Component.translatable("skins.roundabout.20_centuryboy.blue");
            case Powers20thCenturyBoy.GRAPE -> Component.translatable("skins.roundabout.20_centuryboy.grape");
            case Powers20thCenturyBoy.STRAWBERRY -> Component.translatable("skins.roundabout.20_centuryboy.strawberry");
            case Powers20thCenturyBoy.CHICKEN -> Component.translatable("skins.roundabout.20_centuryboy.chicken");
            case Powers20thCenturyBoy.OLDER_CENTURY_BOY -> Component.translatable("skins.roundabout.20_centuryboy.10th_century_boy");
            case Powers20thCenturyBoy.OLDEST_CENTURY_BOY -> Component.translatable("skins.roundabout.20_centuryboy.11th_century_boy");
            case Powers20thCenturyBoy.SALMONBERRY -> Component.translatable("skins.roundabout.20_centuryboy.salmonberry");
            case Powers20thCenturyBoy.BETA -> Component.translatable("skins.roundabout.20_centuryboy.beta");
            default -> Component.translatable("skins.roundabout.20_centuryboy.manga");
        };
    }

    /** now to the fun stuff**/

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move){

            case PowerIndex.POWER_1, PowerIndex.POWER_1_SNEAK -> {
                switchMode();
            }
            case PowerIndex.POWER_2, PowerIndex.POWER_2_SNEAK -> {
                toggleInvincibility();
            }

        }

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void powerActivate(PowerContext context) {

        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                tryPowerPacket(PowerIndex.POWER_1);
            }
            case SKILL_2_NORMAL,SKILL_2_CROUCH -> {
                if (!this.onCooldown(PowerIndex.SKILL_2)) {
                    tryPower(PowerIndex.POWER_2, true);
                    this.tryPowerPacket(PowerIndex.POWER_2);
                    if (!this.invincibleState){
                        this.setCooldown(PowerIndex.SKILL_2, 80);
                    }
                }
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                if (!invincibleState){dash();}
            }

        }

    }


    public void switchMode(){
        if (mode < 4){
            mode += 1;
        } else{
            mode = 1;
        }
    }

    public void toggleInvincibility(){

        if (!invincibleState) {
            invincibleState = true;
            switch (mode) {
                case 1 -> {
                    staticMode = 1;
                    this.self.level().playSound(null, this.getSelf().blockPosition(), ModSounds.CENTURY_BOY_GROUND_STANCE_EVENT, SoundSource.PLAYERS, 3.0F, 1.0F);
                }
                case 2 -> {
                    staticMode = 2;
                    this.self.level().playSound(null, this.getSelf().blockPosition(), ModSounds.CENTURY_BOY_NORMAL_STANCE_EVENT, SoundSource.PLAYERS, 3.0F, 1.0F);
                }
                case 3 -> {
                    staticMode = 3;
                    this.self.level().playSound(null, this.getSelf().blockPosition(), ModSounds.CENTURY_BOY_PROPEL_STANCE_EVENT, SoundSource.PLAYERS, 3.0F, 1.0F);
                }
                case 4 -> {
                    staticMode = 4;
                    this.self.level().playSound(null, this.getSelf().blockPosition(), ModSounds.CENTURY_BOY_OUTPUT_STANCE_EVENT, SoundSource.PLAYERS, 3.0F, 1.0F);
                }
            }
            this.self.stopUsingItem();
            if(this.isClient()){ClientUtil.stopDestroyingBlock();}
        } else {
            invincibleState = false;
            staticMode = 0;
        }
    }



    @Override
    public boolean interceptDamageEvent(DamageSource damageSource, float amount) {
        if(staticMode == 3){
            if (ClientNetworking.getAppropriateConfig().centuryBoySettings.oldKnockbackStance){
                if(damageSource.getEntity() != null){
                    deflect();
                    this.self.setDeltaMovement(
                            this.self.position().subtract(
                                    damageSource.getSourcePosition()).multiply(new Vec3(amount/7.5, amount/7.5, amount/7.5)));
                }
            }else{
                deflect();
                return false;
            }


        }
        if (staticMode == 4){
            deflect();
            return false;
        }
        if (staticMode == 1){
            deflect();

            if (!ClientNetworking.getAppropriateConfig().centuryBoySettings.buffedGroundStance){
                if (damageSource.is(DamageTypes.STARVE) || damageSource.is(DamageTypes.IN_FIRE) ||
                        damageSource.is(DamageTypes.LAVA) || damageSource.is(DamageTypes.DROWN) ||
                        damageSource.is(ModDamageTypes.SUNLIGHT)){
                    return true;
                }
            }
            return false;
        }
        if(invincibleState){
            /** ps: don't forget to put TA4 shot when it gets added **/
            if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                    damageSource.is(DamageTypes.WITHER) ||
                    damageSource.is(DamageTypes.DRAGON_BREATH) ||
                    damageSource.is(ModDamageTypes.GO_BEYOND) ||
                    damageSource.is(DamageTypes.GENERIC_KILL)
            ) {
                return false;
            } else {
                deflect();
                return true;
            }
        } else {
            return super.interceptDamageEvent(damageSource, amount);
        }
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        if (invincibleState){
         return 0;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override public boolean cancelSprintParticles() {return invincibleState;}


    @Override
    public void tickPower() {
        if (!hasStandActive(this.getSelf())){
            invincibleState = false;
            staticMode = 0;
        }
    }

    /** animation thingy **/
    public static AnimationDefinition getAnimation(StandUser SU, boolean fp){
        AnimationDefinition anim = null;
        if (SU.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB && PCB.invincibleState){
            if (!fp){
                switch (PCB.staticMode){
                    case 1 -> anim = CenturyBoyAnimations.ground;
                    case 2 -> anim = CenturyBoyAnimations.neutral;
                    case 3 -> anim = CenturyBoyAnimations.knockback;
                    case 4 -> anim = CenturyBoyAnimations.redstone;
                }
            } else{
                switch (PCB.staticMode){
                    case 1 -> anim = CenturyBoyAnimations.FPground;
                    case 2 -> anim = CenturyBoyAnimations.FPneutral;
                    case 3 -> anim = CenturyBoyAnimations.FPknockback;
                    case 4 -> anim = CenturyBoyAnimations.FPredstone;
                }
            }
        }
        return anim;
    }
    public void setAnimation(byte b) {
        if (this.getSelf() instanceof Player P && this.isClient()) {
            ((IPlayerEntity) P).roundabout$SetPoseEmote(Poses.NONE.id);
        }
        this.getStandUserSelf().roundabout$setStandAnimation(b);
        this.getStandUserSelf().roundabout$getWornStandAnimation().stop();
        this.getStandUserSelf().roundabout$getWornStandAnimation().startIfStopped(this.getSelf().tickCount);

    }

    @Override
    public Component getPosName(byte posID) {
        switch (posID){
            case 1 -> {return Component.translatable("idle.roundabout.century_boy2"); }
            case 2 -> {return Component.translatable("idle.roundabout.century_boy3"); }
        }
        return Component.translatable("idle.roundabout.century_boy1");
    }

    @Override
    public List<Byte> getPosList() {
        List<Byte> listy = Lists.newArrayList();
        listy.add((byte) 0);
        listy.add((byte) 1);
        listy.add((byte) 2);
        return listy;
    }

    /** sounds **/
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        switch (soundChoice){
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.CENTURY_BOY_SUMMON_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    /** particles **/
    private void deflect(){
        Level level = this.self.level();
        BlockPos playerpos = this.self.getOnPos();
        BlockState state = level.getBlockState(playerpos);

        if (!state.isAir()){
            double x = this.self.getX();
            double y = this.self.getY();
            double z = this.self.getZ();

            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), x, y, z,10,.2,.3, 0.3, .3);
            }
            level.playSound(null, this.getSelf().blockPosition(), ModSounds.CENTURY_BOY_HIT_EVENT, SoundSource.PLAYERS, 3F, 1.0F);
        }
    }
}
