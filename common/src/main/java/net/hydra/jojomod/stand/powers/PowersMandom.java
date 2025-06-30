package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.SavedSecond;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PowersMandom extends NewDashPreset {
    public PowersMandom(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersMandom(entity);
    }


    public boolean dangerYapping = false;
    public boolean dangerYappingOn(){
        return dangerYapping;
    }
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

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        super.renderIcons(context, x, y);
    }
    public void registerHUDIcons() {
        HashSet<GuiIcon> icons = new HashSet<>();

        // code for basic icons: the rest rely on criteria we have to manually implement

        GUI_ICON_REGISTRAR = icons;
    }

    @Override
    public void tick() {
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                return itsRewindTime();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public static final byte
            ROTATE = 1;
    /**Let the client brunt the task of mass scanning blocks so it doesn't lag server TPS
     * also instill block limits so the packet count is sane*/
    public boolean itsRewindTime(){
        //this.setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().heyYaSettings.oreDetectionCooldown);
        if (isClient()){
        }
        return true;
    }


    int ticksSinceLastYap = 0;
    int maxTicksSinceLastYap = 400;

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    public SimpleParticleType getParticle(Entity ent){
        if (ent instanceof Monster || (ent instanceof Mob mb && mb.isAggressive()))
            return ModParticles.RED_CLOCK;
        if (ent !=null && ent.is(this.self))
            return ModParticles.CLOCK;
        if (ent instanceof Player)
            return ModParticles.BLUE_CLOCK;
        if (ent instanceof Projectile)
            return ModParticles.ORANGE_CLOCK;
        return ModParticles.GREEN_CLOCK;
    }

    @Override
    public void tickPower() {
        super.tickPower();

        /**Grabs nearby entities pretty regularly to see if they can be rendered*/
        if (!this.self.level().isClientSide()) {
            if (this.self.tickCount % 3 == 0) {
                List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.self.level(), this.self.blockPosition(), 50);
                if (!mobsInRange.isEmpty()) {
                    for (Entity ent : mobsInRange) {
                        if (MainUtil.canRewindInTime(ent,this.self)) {
                            IEntityAndData iData = (IEntityAndData) ent;
                            SavedSecond lastSecond = iData.roundabout$getLastSavedSecond();
                            if (lastSecond != null){
                                if (!lastSecond.hasHadParticle){
                                    lastSecond.hasHadParticle = true;
                                    lastSecond.isTickingParticles = this.self;
                                    ((ServerLevel) this.self.level()).sendParticles(getParticle(ent),
                                            lastSecond.position.x, lastSecond.position.y+ent.getEyeHeight()*0.8, lastSecond.position.z,
                                            0, 0, 0, 0, 0.015);
                                }
                                if (!(ent instanceof Projectile)) {
                                    if (lastSecond.isTickingParticles != null && lastSecond.isTickingParticles.is(this.self)) {
                                        Vec3 forward = Vec3.directionFromRotation(lastSecond.rotationVec);
                                        ((ServerLevel) this.self.level()).sendParticles(ModParticles.TIME_EMBER,
                                                lastSecond.position.x, lastSecond.position.y + ent.getEyeHeight() * 0.8, lastSecond.position.z,
                                                0,
                                                forward.x,
                                                forward.y,
                                                forward.z,
                                                0.25);
                                    }
                                }
                            }
                        }
                    }
                    /**
                    ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_TRAIL,
                            this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                            0, 0, 0, 0, 0.015);**/
                }
            }
        }
    }


    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }


    public static final byte
            MANGA = 1,
            PURPLE = 2,
            SKY = 3,
            SQUID = 4,
            GLOW_SQUID = 5,
            ROSE = 6,
            NAUTILUS = 7,
            ALIEN = 8,
            DARK = 9,
            JELLYFISH = 10,
            HAPPY = 11;
    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                MANGA,
                PURPLE,
                SKY,
                ALIEN,
                DARK,
                SQUID,
                GLOW_SQUID,
                ROSE,
                NAUTILUS,
                JELLYFISH,
                HAPPY
        );
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case PowersMandom.PURPLE -> Component.translatable("skins.roundabout.mandom.purple");
            case PowersMandom.SKY -> Component.translatable("skins.roundabout.mandom.sky");
            case PowersMandom.SQUID -> Component.translatable("skins.roundabout.mandom.squid");
            case PowersMandom.ALIEN -> Component.translatable("skins.roundabout.mandom.alien");
            case PowersMandom.DARK -> Component.translatable("skins.roundabout.mandom.dark");
            case PowersMandom.GLOW_SQUID -> Component.translatable("skins.roundabout.mandom.glow_squid");
            case PowersMandom.ROSE -> Component.translatable("skins.roundabout.mandom.rose");
            case PowersMandom.NAUTILUS -> Component.translatable("skins.roundabout.mandom.nautilus");
            case PowersMandom.JELLYFISH -> Component.translatable("skins.roundabout.mandom.jellyfish");
            case PowersMandom.HAPPY -> Component.translatable("skins.roundabout.mandom.happy");
            default -> Component.translatable("skins.roundabout.mandom.manga");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_MANDOM_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    public byte worthinessType(){
        return HUMANOID_WORTHY;
    }
    public Component getPosName(byte posID) {
        if (posID == 1) {
            return Component.translatable("idle.roundabout.hey_ya_2");
        } else if (posID == 2) {
                return Component.translatable("idle.roundabout.mandom_back");
        } else {
            return Component.translatable("idle.roundabout.hey_ya_1");
        }
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        return $$1;
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        return $$1;
    }

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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }
}