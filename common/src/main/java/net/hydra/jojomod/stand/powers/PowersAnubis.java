package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.gui.MemoryRecordScreen;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.AnubisSlipstreamEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import oshi.util.tuples.Pair;

import java.util.*;

public class PowersAnubis extends NewDashPreset {
    public PowersAnubis(LivingEntity self) {
        super(self);
    }


    public static final int MaxPossesionTime = 100;
    public static final int MaxPlayTime = 100;
    public static final int PogoDelay = 8;
    public static final byte SWING = 50;


    public List<AnubisMemory> memories = new ArrayList<AnubisMemory>();
    public final List<KeyMapping> playKeys = new ArrayList<>();
    public final List<Byte> playBytes = new ArrayList<>();
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        Options o = Minecraft.getInstance().options;



        PowersAnubis PA = new PowersAnubis(entity);
        return PA;
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
        if (!isHoldingSneak() ) {
            setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_ALLURING_LIGHT, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_RAGING_LIGHT, PowerIndex.SKILL_1_SNEAK);
        }

        ResourceLocation icon2 = StandIcons.ANUBIS_REPLAY;
        if (this.playTime > 0) {
            icon2 = StandIcons.ANUBIS_CANCEL;
        } else if (Minecraft.getInstance().screen instanceof MemoryRecordScreen MA && MA.recording) {
            icon2 = StandIcons.ANUBIS_DMOUSE;
        }
        setSkillIcon(context, x, y, 2, icon2, PowerIndex.SKILL_2);

        if (!isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.ANUBIS_BACKFLIP, PowerIndex.GLOBAL_DASH);
        }

        ResourceLocation icon4 = StandIcons.ANUBIS_RECORD;
        if (this.playTime > 0) {
            if (this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
                icon4 = StandIcons.ANUBIS_CANCEL;
            } else {
                icon4 = StandIcons.ANUBIS_SAVE;
            }
        }
        setSkillIcon(context, x, y, 4, icon4, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        if ( ((StandUser)this.getSelf()).roundabout$getActive()
                && this.getActivePower() != PowerIndex.GUARD
                && this.getActivePower() != PowerIndex.BARRAGE_CHARGE) {
            basis *= this.getSelf().isSprinting() ? 1.6F : 1F;
        }
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE && this.getAttackTimeDuring() > this.getBarrageMinimum()) {
            int v = this.getBarrageWindup()-this.getBarrageMinimum();
            float scale = Math.min((this.getAttackTimeDuring()-v)/(float)v,1.0F);
            basis *= 1 - (float) 0.7*scale;
        }
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            basis *= 1.5;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public int getJumpHeightAddon() {
        if (this.getStandUserSelf().roundabout$getActive() && !(this.getActivePower() == PowerIndex.BARRAGE_CHARGE)) {
            return 1;
        }
        return super.getJumpHeightAddon();
    }

    @Override
    public boolean cancelSprintJump() {
        if (this.getActivePower() == PowerIndex.GUARD) {
            return true;
        }
        return super.cancelSprintJump();
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> {
                AlluringLightClient();
            }
            case SKILL_1_CROUCH, SKILL_1_CROUCH_GUARD -> {
                RagingLightClient();
            }

            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                if (this.playTime > 0) {
                    MemoryCancelClient();
                } else if (Minecraft.getInstance().screen instanceof MemoryRecordScreen MA && MA.recording) {
                    MemoryDeltaClient();
                } else {
                    MemoryPlayClient();
                }
            }

            case SKILL_3_NORMAL -> {
                this.dash();
            }

            case SKILL_3_CROUCH -> {
                BackflipClient();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                if (this.playTime > 0) {
                    if (this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
                        MemoryCancelClient();
                    } else {
                        MemoryCancelSaveClient();
                    }
                } else {
                    MemoryRecordClient();
                }
            }
        }
    }

    public void AlluringLightClient() {
        if (!onCooldown(PowerIndex.SKILL_1)) {
            this.setCooldown(PowerIndex.SKILL_1,200);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }
    public void AlluringLightServer() {
        this.setCooldown(PowerIndex.SKILL_1,200);
        int radius = 8;
        AABB box = this.getSelf().getBoundingBox().inflate(radius,2,radius);
        for (Mob M : this.getSelf().level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT,this.getSelf(),box)) {
            ((IMob)M).roundabout$setHypnotizedBy(this.getSelf(),200);
        }

        Vec3 pos = this.getSelf().getPosition(1);
        Vector3f[] colors = {
                new Vector3f(0.96F, 0.96F, 0.92F),
                new Vector3f(0.93F, 0.87F, 0.57F)
        };
        //TODO: PARTICLE SPRAY (with colors)
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.FIREWORK,
                pos.x,
                pos.y + this.getSelf().getEyeHeight(),
                pos.z,
                20, 0, 0, 0, 0.4);
    }

    public void RagingLightClient() {
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            setCooldown(PowerIndex.SKILL_1_SNEAK,200);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }
    public void RagingLightServer() {
        this.setCooldown(PowerIndex.SKILL_1_SNEAK,200);
        int radius = 13;
        AABB box = this.getSelf().getBoundingBox().inflate(radius,2,radius);
        for (Mob M : this.getSelf().level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT,this.getSelf(),box)) {
            M.setTarget(this.getSelf());
            M.setLastHurtByMob(this.getSelf());
        }

        Vec3 pos = this.getSelf().getPosition(1);
        Vector3f[] colors = {
                new Vector3f(0.85F, 0.31F, 0.15F),
                new Vector3f(0.31F, 0.22F, 0.20F )
        };
        //TODO: PARTICLE SPRAY (with colors)
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.FIREWORK,
                pos.x,
                pos.y + this.getSelf().getEyeHeight(),
                pos.z,
                20, 0, 0, 0, 0.4);
    }


    public void BackflipClient() {
        if (!onCooldown(PowerIndex.GLOBAL_DASH)) {
            tryPower(PowerIndex.SNEAK_MOVEMENT,true);
            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);

        }
    }

    public void MemoryRecordClient() {
        if(!this.getStandUserSelf().roundabout$getUniqueStandModeToggle() && Minecraft.getInstance().mouseHandler.isMouseGrabbed()) {
            ClientUtil.openMemoryRecordScreen(true);
        }
    }

    public void MemoryPlayClient() {
        if(!this.getStandUserSelf().roundabout$getUniqueStandModeToggle() && Minecraft.getInstance().mouseHandler.isMouseGrabbed()) {
            ClientUtil.openMemoryRecordScreen(false);
        }
    }

    public void MemoryDeltaClient() {
        if (!isAttackIneptVisually(PowerIndex.SKILL_2,2)) {
            MemoryRecordScreen MA = (MemoryRecordScreen) Minecraft.getInstance().screen;
            boolean b = this.memories.get(MA.currentlyHovered).delta_mouse;
            this.memories.get(MA.currentlyHovered).delta_mouse = !b;
        }
    }

    public void MemoryCancelSaveClient() {
       // Roundabout.LOGGER.info(""+this.memories.get(playSlot).moments.toString());
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);

        int time = PowersAnubis.MaxPlayTime-this.playTime;
        for (int i=0;i<playBytes.size();i++) {
            if (isPressed(playBytes.get(i),time)) {
                AnubisMemory memory = this.getUsedMemory();
                List<AnubisMoment> moments = memory.moments;
                moments.add(new AnubisMoment(this.playBytes.get(i),Math.min(PowersAnubis.MaxPlayTime,time),false));
            }
        }

        Roundabout.LOGGER.info(this.getUsedMemory().moments.toString());


        setPlayTime(-1);
        this.playSlot = (byte)-1;



        // visualValues = new ArrayList<>();

    }
    public void MemoryCancelClient() {
        if (!this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
            this.getUsedMemory().moments = new ArrayList<>();
        }
        this.visualValues = new ArrayList<>();
        setPlayTime(-1);
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP) {
            case PowerIndex.SKILL_4 -> {
                return !Minecraft.getInstance().mouseHandler.isMouseGrabbed();
            }
            case PowerIndex.SKILL_2 -> {
                return (Minecraft.getInstance().screen instanceof MemoryRecordScreen MA && (MA.currentlyHovered == -1 || MA.currentlyHovered == 8) );
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        StandUser SU = (StandUser) this.getSelf();
        switch (move) {
            case PowerIndex.POWER_1 ->  {
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_ALLURING_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
            }
            case PowerIndex.POWER_1_SNEAK -> {
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_RAGING_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
            }
            case PowerIndex.BARRAGE -> {
                this.setActivePower(PowerIndex.RANGED_BARRAGE);
                setPowerOther(PowerIndex.RANGED_BARRAGE,this.getActivePower());
            }
            case PowerIndex.SNEAK_MOVEMENT -> {
                ///  gives you another pogo
                canPogo = true;
                this.setAttackTime(0);
                this.setActivePower(PowerIndex.SNEAK_MOVEMENT);
                this.setCooldown(PowerIndex.GLOBAL_DASH,260);
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_BACKFLIP_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
                this.getStandUserSelf().roundabout$setStandAnimation(PowerIndex.SNEAK_MOVEMENT);
                if (this.getSelf() instanceof Player P) {
                    P.getAbilities().flying = false;
                }
                if (!isClient()) {
                    Vec3 look = getSelf().getLookAngle().multiply(1, 0, 1).normalize();
                    SU.roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                    SU.roundabout$setLeapIntentionally(true);

                    float strength = 1.25F;
                    if (Math.abs(look.x) + Math.abs(look.z) == 0) {
                        strength *= 0.7F;
                    } else if (!this.getSelf().onGround()) {
                        strength *= 0.8F;
                    }
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), strength, look.x * 1, -1 * (this.getSelf().onGround() ? 1 : 0.8), look.z * 1);
                }
            }
        }
        return super.tryPower(move, forced);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> {
                AlluringLightServer();
            }
            case PowerIndex.POWER_1_SNEAK -> {
                RagingLightServer();
            }

            case PowerIndex.ATTACK, PowerIndex.SNEAK_ATTACK -> {
                tryBasicAttack((byte)move);
            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                tryPogoAttack();
            }
            case PowerIndex.BARRAGE_CHARGE_2 -> {
                this.attackTimeDuring = 0;
                this.playBarrageChargeSound();
                this.setActivePower(PowerIndex.BARRAGE_CHARGE_2);
            }
            case PowerIndex.RANGED_BARRAGE -> {
                this.setActivePower(PowerIndex.RANGED_BARRAGE);
                this.setAttackTime(0);
            }

        }
        if (isVariant((byte)move)) {
            this.tryBasicAttack((byte)move);
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1) {
        ///  cancels the pogo during the windup
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE && getAttackTimeDuring() < PogoDelay) {
            this.setPowerNone();
        }
    }

    List<Integer> lasthits = new ArrayList<>();

    float slipstreamTimer = 3;

    boolean canPogo = true;
    int pogoCounter = 0;
    @Override
    public void tickPower() {

        // Roundabout.LOGGER.info(" CA: " + this.getActivePower() + " | " + this.getAttackTime() + " | "+ this.getAttackTimeDuring() + "/" + this.getAttackTimeMax());




        StandUser SU = this.getStandUserSelf();

        if (SU.roundabout$getStandSkin() == (byte) 0) {
            SU.roundabout$setStandSkin((byte)1);
        }

        if (this.getSelf().onGround()) {

            if (this.getActivePower() != PowerIndex.SNEAK_ATTACK_CHARGE || this.attackTime > PogoDelay + 3) {
                canPogo = true;
            }


            if (this.isClient()) {
                if (this.getSelf() instanceof Player P){
                    if (pogoCounter != 0 && ConfigManager.getClientConfig().standTweakSettings.anubisPogoCounter) {
                        P.displayClientMessage(Component.literal("" + pogoCounter).withStyle(ChatFormatting.RED), true);
                    }
                }
                pogoCounter = 0;
            }

        } else if (!canPogo) {
            if (this.getSelf() instanceof Player P) {
                if (P.isCreative()) { canPogo = true;}
            }
        }

        /** slipstream creation */
        if (!this.isClient()) {
            Level level = this.getSelf().level();
            boolean noSlip = this.getActivePower() == PowerIndex.SNEAK_MOVEMENT || this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE;
            if (this.getSelf().isSprinting() && SU.roundabout$getActive() && !noSlip) {

                float dif = this.getSelf().walkDist-this.getSelf().walkDistO;
                if (dif != 0) {
                    slipstreamTimer -= dif;
                }

                if (slipstreamTimer <= 0) {
                    slipstreamTimer = 3;
                    AnubisSlipstreamEntity AS = new AnubisSlipstreamEntity(ModEntities.ANUBIS_SLIPSTREAM,level);
                    AS.setPos(this.getSelf().getPosition(1));
                    level.addFreshEntity(AS);
                }
            }
        }

        SU.roundabout$setCombatMode(SU.roundabout$getActive());

        if (this.memories.size() != 8) {
            generateMemories(this);
        }
        if (this.playTime > 0 && isClient()) {



            if (!this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {

                List<Byte> value = new ArrayList<>();
                for (int i=0;i<this.playKeys.size();i++) {
                    KeyMapping key = playKeys.get(i);

                    /// visualData storing
                    if(key.isDown()) {value.add(playBytes.get(i));}

                    /// gets the last instance of a key being saved
                    int time = PowersAnubis.MaxPlayTime-this.playTime;
                    byte id = playBytes.get(i);
                    boolean vargs = key.isDown();
                    int lastMoment = this.getLastMoment(this.playSlot, id, time);

                    boolean bl = false;
                    if (lastMoment != -1) {
                        AnubisMoment m = getUsedMemory().moments.get(lastMoment);
                        if (!(m.vargs == vargs)) {
                            bl = true;
                        }
                    }
                    boolean bl2 = true;
                    if (id == AnubisMoment.ABILITY_2 || id == AnubisMoment.ABILITY_3) {

                        if(this.getStandUserSelf().roundabout$getActive()) {
                            bl2 = false;
                        }
                    }
                    ///  if either the key has not been saved or the values are different add a new moment
                    if ( bl2 && ( (lastMoment == -1 && vargs ) || bl) ) {
                        getUsedMemory().moments.add(new AnubisMoment(id, time,vargs ));
                    }
                }


                if (visualValues.isEmpty()) {
                    visualValues.add(new Pair<>(value, 0));
                } else {
                    Pair<List<Byte>, Integer> last = visualValues.get(visualValues.size() - 1);
                    if (last.getA().equals(value)) {
                        visualValues.set(visualValues.size() - 1, new Pair<>(last.getA(), last.getB() + 1));
                    } else {
                        visualValues.add(new Pair<>(value, 0));
                    }
                }
            }



            this.playTime--;
            boolean bl = false;
            AnubisMemory memory = this.getUsedMemory();
            if (this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
                if (memory != null) {
                    if (!memory.moments.isEmpty()) {
                        if (PowersAnubis.MaxPlayTime - this.playTime > memory.moments.get(memory.moments.size() - 1).time) {
                            bl = true;
                        }
                    }
                }
            }

            if (this.playTime <= 0 || bl) {
                this.MemoryCancelSaveClient();
            }
        }

        if (isGuarding()) {
            getStandUserSelf().roundabout$setStandAnimation(PowerIndex.GUARD);
        } else if (getStandUserSelf().roundabout$getStandAnimation() == PowerIndex.GUARD) {
            getStandUserSelf().roundabout$setStandAnimation(NONE);
        }

        if(SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_MOVEMENT ) {
            if (this.attackTime > 16) {
                SU.roundabout$setStandAnimation(PowerIndex.NONE);
            }
        }
        if(SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_ATTACK_CHARGE ) {
            if (this.getActivePower() == PowerIndex.NONE || this.getSelf().onGround()) {
                SU.roundabout$setStandAnimation(PowerIndex.NONE);
            }
        }
        if (this.getActivePower() == PowerIndex.SNEAK_MOVEMENT) {
            if (this.getAttackTime() > 10 && this.getAttackTime() < 20) {
                if(this.getSelf().isCrouching()) {
                    this.addMomentum(0,-0.075F,0);
                }
            } else if (this.getAttackTime() > 20) {
                this.setPowerNone();
            }
        }


/// WARNING: THIS WILL BREAK AT SOME POINT
        this.getSelf().setNoGravity(this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE && this.attackTimeDuring < PogoDelay);

        super.tickPower();
    }





    @Override
    public boolean interceptAttack(){
        return true;
    }
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (this.canAttack()) {

                byte index = PowerIndex.ATTACK;
                if (this.isHoldingSneak()) {
                    if (!this.getSelf().onGround() && canPogo) {
                        canPogo = false;
                        index = PowerIndex.SNEAK_ATTACK_CHARGE;
                    } else {
                        index = PowerIndex.SNEAK_ATTACK;
                    }
                }
                if (index != PowerIndex.SNEAK_ATTACK_CHARGE) {
                    lasthits.add((index == PowerIndex.SNEAK_ATTACK ? -1 : 1));
                }
                if (lasthits.size() == 3) {
                    index = determineThird(lasthits);
                }

                if (index != PowerIndex.SNEAK_ATTACK_CHARGE && index != PowersAnubis.DOUBLE && index != PowersAnubis.UPPERCUT) {
                    this.getSelf().swing(InteractionHand.MAIN_HAND);
                }
                this.tryPower(index);
                tryPowerPacket(index);
            }

        }
    }

    @Override
    public boolean setPowerAttack() {
        return setPowerOther(PowerIndex.ATTACK,this.getActivePower());
    }

    public void tryBasicAttack(byte move) {
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown;
            }

        }
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        if (move == PowersAnubis.UPPERCUT) {
            ((StandUser)this.getSelf()).roundabout$setMeleeImmunity(8);
        }
        setActivePower(move);
    }



    @Override
    public void updateAttack() {
        updateUniqueMoves();
    }

    private final int quickdrawDelay = 6;
    @Override
    public void updateUniqueMoves() {
        switch (getActivePower()) {
            case PowerIndex.ATTACK,PowerIndex.SNEAK_ATTACK -> {
                updateAttacks();
            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                updatePogoAttack();
            }
            case PowerIndex.BARRAGE_CHARGE_2 -> {
                if (this.attackTimeDuring >= this.getKickBarrageWindup()) {
                    setActivePower(PowerIndex.BARRAGE_2);
                }
            }
            case PowerIndex.BARRAGE_2 -> {
                BarrageSlash();
            }
            case PowerIndex.RANGED_BARRAGE -> {
                if (this.getAttackTime() == quickdrawDelay) {
                    StartQuickdraw(8);
                } else {
                    UpdateQuickdraw();
                }
                if (this.getAttackTime() < quickdrawDelay) {
                    scopeLevel = 1;
                } else {
                    scopeLevel = 0;
                }

            }
        }
        if (isVariant(this.getActivePower())) {
            updateAttacks();
        }
    }

    public void updateAttacks() {
        if (!isClient()) {

            if (this.attackTimeDuring > -1) {
                if (this.attackTimeDuring > this.attackTimeMax) {
                    this.attackTime = -1;
                    this.attackTimeMax = 0;
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                } else {
                    switch (this.getActivePower()) {
                        case PowersAnubis.UPPERCUT -> {
                            if (this.getAttackTimeDuring() < 4) {
                                if (this.getTargetEntity(this.getSelf(),2) == null) {
                                    Vec3 look = this.getSelf().getLookAngle();
                                    look = new Vec3(look.x,0, look.z).normalize().reverse();
                                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),1F,look.x,look.y,look.z);
                                } else {
                                    Uppercut();
                                }
                            } else {
                                Uppercut();
                            }
                        }

                        case PowersAnubis.DOUBLE ->  {
                            if (this.attackTimeDuring%4 == 0) {
                                DoubleCut(attackTimeDuring < 8);
                            }
                        }

                        case PowersAnubis.SPIN -> {
                            Options o = Minecraft.getInstance().options;
                            if (this.getAttackTimeDuring() > 5) {
                                ThrustCut();
                            } else if (this.getAttackTimeDuring() == 1 && !o.keyDown.isDown()) {
                                Vec3 look = this.getSelf().getLookAngle().reverse();
                                look = new Vec3(look.x,-0.2,look.z);
                                float strength = 1F + (o.keyUp.isDown() ? 0.2F : 0) + (o.keyDown.isDown() ? -0.2F : 0);
                                strength *= (this.getSelf().onGround() ? 1F : 0.6F);
                                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),strength,look.x,look.y,look.z);
                            }
                        }

                    }

                    ///  NORMAL AND SNEAK CLICK
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK ||
                            this.getActivePower() == PowerIndex.ATTACK) {
                        /// the actual attack
                        if (getActivePower() == PowerIndex.SNEAK_ATTACK) {
                            SAttack();
                        } else {
                            NAttack();
                        }

                    }

                    if (this.getSelf() instanceof Player) {
                        if (isPacketPlayer()) {
                            this.setAttackTimeDuring(-10);
                            //   C2SPacketUtil.standPunchPacket(getTargetEntityId(15), this.activePowerPhase);
                        }
                    }


                }
            }

        }
    }


    public void NAttack(){

        Entity targetEntity = getTargetEntity(this.self,-1,15);
        punchImpact(targetEntity);
    }
    public void SAttack(){

        Entity targetEntity = getTargetEntity(this.self,-1,15);
        punchImpact(targetEntity);
    }

    public void tryPogoAttack() {
        if (this.getSelf() instanceof Player P) {
            P.getAbilities().flying = false;
        }
        this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;
        this.attackTimeDuring = 0;

        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        //  this.getStandUserSelf().roundabout$setStandAnimation(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.setAttackTime(0);
    }

    public void updatePogoAttack() {
        this.getSelf().setNoGravity(this.attackTimeDuring < PogoDelay);
        if (this.attackTimeDuring > -1) {

            if (this.getSelf().onGround() && this.getAttackTime() < PogoDelay) {
                this.setPowerNone();
                this.attackTime += 5;
            }

            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
            } else {
                if (!isClient()) {
                    /**  Pogo is broken up into 4 stages: Hover, Launch, Attack, and Aftershock */
                    int windup = PogoDelay;
                    if (attackTimeDuring == windup) {
                        PogoLaunch();

                    } else if (attackTimeDuring < windup) {
                        MainUtil.slowTarget(this.getSelf(), 0.8F);

                    } else if (attackTimeDuring < windup + 6) {
                        Vec3 pos = this.getSelf().getEyePosition(0F).add(this.getSelf().getLookAngle().scale(1));
                        List<Entity> targets = MainUtil.genHitbox(this.getSelf().level(),
                                pos.x,pos.y,pos.z,
                                1.4,1.4,1.4);
                        targets.removeIf(entity -> entity.equals(this.getSelf()));
                        targets = doAttackChecks(targets);
                        Entity target = null;
                        if (!targets.isEmpty()) {target = targets.get(0);}

                        Options o = Minecraft.getInstance().options;
                        if (target != null) {
                            this.setAttackTimeDuring(this.getAttackTimeDuring()+15);

                            double strength = this.getSelf().isCrouching() ? 0.5 : 0.9;
                            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),strength,0,-1,0);
                            this.getStandUserSelf().roundabout$setLeapTicks(20);
                            this.getStandUserSelf().roundabout$setLeapIntentionally(true);

                            if (StandDamageEntityAttack(target,4,1,this.getSelf())) {
                                if (target instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers().interceptGuard()
                                        && LE.isBlocking() && !((StandUser) LE).roundabout$isGuarding()){
                                    knockShield2(target, 30);
                                }
                            }
                            this.setPowerNone();
                            if (this.getSelf() instanceof Player P) {
                                S2CPacketUtil.sendIntPowerDataPacket(P, PowerIndex.SNEAK_ATTACK_CHARGE, attackTime + 5);
                                if (target instanceof ArmorStand) {
                                    S2CPacketUtil.sendIntPowerDataPacket(P,PowerIndex.EXTRA,1);
                                }
                            }

                            this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_POGO_HIT_EVENT,SoundSource.PLAYERS,1F,0.9F+(float)Math.random()*0.2F);
                            ((StandUser)this.getSelf()).roundabout$setMeleeImmunity((byte) (this.getSelf().isCrouching() ? 10 : 5) );

                        }

                    } else if (attackTimeDuring < windup + 9) { /// Slows the user after a duration
                        MainUtil.slowTarget(this.getSelf(),0.7F);
                        this.getSelf().resetFallDistance();
                    }
                }

            }
        }

    }



    public void PogoLaunch(){
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }
        float power = 1.5F;
        Vec3 lookAngle = this.getSelf().getLookAngle().reverse();
        this.getSelf().resetFallDistance();
        if (lookAngle.y < -0.15) {
            power *= 0.5F;
        }
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_POGO_LAUNCH_EVENT,SoundSource.PLAYERS,1F,0.9F+(float)(Math.random()*0.2) );
        this.getStandUserSelf().roundabout$setMeleeImmunity((byte)6);
        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),power,lookAngle.x,lookAngle.y,lookAngle.z);
    }

    @Override
    public void punchImpact(Entity entity) {
        this.setAttackTimeDuring(-10);
        float knockbackStrength = 0.2F;
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {knockbackStrength = 0.4F;}
        if (this.getSelf().isSprinting()) {knockbackStrength += 0.05F;}

        List<Entity> entities = getBasicSwordHitBox(this.getActivePower() == PowerIndex.SNEAK_ATTACK);
        for (Entity e : entities ) {
            if (e != null) {
                float pow = getPunchStrength(e) * (this.getActivePower() == PowerIndex.SNEAK_ATTACK ? 1.3F : 1F);
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {
                    if (e instanceof LivingEntity LE) {
                        addEXP(1);
                    }
                    this.takeDeterminedKnockback(this.getSelf(), e, knockbackStrength);
                }

            }
        }
        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
                float pitch = 0.9F+(float)(Math.random()*0.2F);
                pitch += this.getActivePower() == PowerIndex.SNEAK_ATTACK ? -0.3F : 0;
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1F,pitch);
            }
        }

    }

    /// ---------------------------------
    /// VARIANTS
    /// ---------------------------------

    public static boolean isVariant(byte b) {
        return b >= PowersAnubis.DOUBLE && b <= PowersAnubis.SPIN;
    }
    public static final byte
            DOUBLE = 52,
            THRUST = 53,
            UPPERCUT = 54,
            SPIN = 55;




    public byte determineThird(List<Integer> list) {
        lasthits = new ArrayList<>();
        String id = list.get(0)+""+list.get(1);
        return switch (id) {
            case "11" -> DOUBLE;
            case "1-1" -> UPPERCUT;
            case "-11" -> UPPERCUT;
            case "-1-1" -> SPIN;
            default -> throw new IllegalStateException("How did you do this: " + id);
        };

    }




    public void DoubleCut(boolean first) {
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }
        if (!canPogo) {
            this.setAttackTime(0);
            this.setAttackTimeMax(this.getAttackTimeMax()+10);
        }
        if (!first) {
            this.setAttackTimeDuring(-10);
        }

        if (this.getSelf().onGround() && this.getTargetEntity(this.getSelf(),2,this.getSelf().getXRot()) == null ) {
            Vec3 look = this.getSelf().getLookAngle();
            look = new Vec3(look.x,0,look.z).normalize();
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),-0.5,look.x,look.y,look.z);
        }

        float knockbackStrength = 0.6F + (this.getSelf().isSprinting() ? 0.1F : 0F);
        if (first) {knockbackStrength = 0.2F;}

        List<Entity> entities =  defaultSwordHitbox(this.getSelf(),1.3, 3.4);
        entities = doAttackChecks(entities);
        for (Entity e : entities ) {
            if (e != null) {
                if (e.distanceTo(this.getSelf()) < 1.5F) {
                    knockbackStrength += 0.15F;
                    this.setAttackTime(0);
                    this.setAttackTimeMax(this.getAttackTimeMax()+5);
                }

                float pow = getHeavyPunchStrength(e) * 0.6F;
                if (e instanceof LivingEntity LE) {
                    if (pow > LE.getHealth()) {
                        pow = Math.max(LE.getHealth() - 0.1F,0.1F);
                    }
                }
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {

                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(),SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1F,0.4F + (float)(Math.random()*0.2) + (first ? 0.0F : 0.3F) );
                    if (e instanceof LivingEntity LE) {
                        addEXP(2);
                    }
                    this.takeDeterminedKnockback(this.getSelf(), e, knockbackStrength);

                } else if (!first) {
                    if (e instanceof LivingEntity LE) {
                        if (LE.isBlocking()) {
                            MainUtil.knockShieldPlusStand(e,40);
                        }
                    }
                }

            }
        }

        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
            }

        }
    }


    public void ThrustCut() {


        this.setAttackTimeDuring(-10);

        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_BARRAGE_1_EVENT,SoundSource.PLAYERS,0.8F,1.3F);
        List<Entity> entities = defaultSwordHitbox(this.getSelf(),1,3.6);
        for (Entity entity : entities) {
            float dist = entity.distanceTo(this.getSelf());
            boolean range = dist > 2.85F;
            float pow = this.getHeavyPunchStrength(entity);
            if (range) {
                pow *= 1.4F;
                ((StandUser)this.getSelf()).roundabout$setMeleeImmunity(10);
            }
            if (StandDamageEntityAttack(entity,pow,0.0F,this.getSelf())) {
                int dur = 100;
                if (range) {
                    MainUtil.takeUnresistableKnockbackWithY(entity,0.45,0,-1,0);
                    dur = 200;
                } else {
                }
                if (entity instanceof LivingEntity LE) {
                    LE.addEffect(new MobEffectInstance(ModEffects.BLEED,dur,0));
                }
            }
        }

        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
            }

        }
    }

    public void Uppercut() {
        this.getSelf().resetFallDistance();
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }
        this.setAttackTimeDuring(-10);


        List<Entity> entities =  defaultSwordHitbox(this.getSelf(),1.3, 3);
        for (Entity e : entities ) {
            if (e != null) {

                float pow = getHeavyPunchStrength(e);
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {
                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(),SoundEvents.PLAYER_ATTACK_KNOCKBACK,SoundSource.PLAYERS,1F,0.4F + (float)(Math.random()*0.2));
                    if (e instanceof LivingEntity LE) {
                        addEXP(2);
                    }
                    Vec3 look = this.getSelf().getLookAngle().normalize();
                    look = new Vec3(look.x,0,look.z).normalize().reverse().scale(this.getSelf().isSprinting() ? 1.3 : 1);
                    MainUtil.takeKnockbackWithY(e,0.8F,look.x,-3,look.z);
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),0.9F,0,-4,0);


                } else {
                    if (e instanceof LivingEntity LE) {
                        if (LE.isBlocking()) {
                            MainUtil.knockShieldPlusStand(e,30);
                        }
                    }
                }

            }
        }

        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
            }

        }
    }


    @Override
    public boolean setPowerBarrageCharge() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        playBarrageChargeSound();
        return true;
    }

    @Override
    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.RANGED_BARRAGE);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        playBarrageCrySound();
    }





    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            ///  basic swing, will probably be vanished at some point
            case PowersAnubis.SWING -> {
                this.getSelf().swing(InteractionHand.MAIN_HAND);
            }
            /// pogo counter synching
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                this.attackTime = data;
                this.setPowerNone();

                pogoCounter += 1;
                if (ConfigManager.getClientConfig().standTweakSettings.anubisPogoCounter) {
                    ((Player) this.getSelf()).displayClientMessage(Component.literal("" + pogoCounter).withStyle(ChatFormatting.WHITE), true);
                }
            }
            /// canPogo synching
            case PowerIndex.EXTRA -> {
                canPogo = data == 1;
            }
            case PowerIndex.BARRAGE -> {
                if (data == 1) {
                    setPowerNone();
                } else {
                    this.setAttackTime(quickdrawDelay+1);
                    this.setAttackTimeDuring(0);
                    this.setActivePowerPhase(this.getActivePowerPhaseMax());
                    this.setAttackTimeMax(data);
                }
            }

        }
        super.updatePowerInt(activePower,data);
    }


    @Override
    public boolean interceptGuard(){
        return true;
    }

    public boolean canGuard(){
        return !this.isBarraging() && !this.isClashing() && this.getActivePower() != PowerIndex.SNEAK_ATTACK_CHARGE;
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (this.isBarrageCharging() || this.isBarrageAttacking()) {return false;}
        if (!this.isGuarding() && canGuard()) {
            tryPower(PowerIndex.GUARD,true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }


    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if(keyIsDown) {
            if (isHoldingSneak() && (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax()))) {

                this.tryPower(PowerIndex.BARRAGE_CHARGE_2);
                this.tryPowerPacket(PowerIndex.BARRAGE_CHARGE_2);

            } else {
                super.buttonInputBarrage(keyIsDown, options);
            }
        }
    }


    List<Entity> targets = new ArrayList<Entity>();
    @Override
    public void updateBarrage() {}
    @Override
    public void updateBarrageCharge() {}
    @Override
    public boolean isBarrageAttacking() {return super.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2;}
    @Override
    public boolean isBarrageCharging() {return super.isBarrageCharging() || this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2;}
    public int getBarrageMinimum() {return getBarrageWindup();}
    @Override
    public int getBarrageWindup() {return super.getBarrageWindup()+10;}

    @Override
    public boolean clickRelease() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2);
    }
    @Override
    public boolean onClickRelease() {
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE && this.getAttackTimeDuring() > this.getBarrageMinimum()) {
            tryPower(PowerIndex.BARRAGE);
            tryPowerPacket(PowerIndex.BARRAGE);
            return true;
        }
        return super.onClickRelease();
    }

    @Override
    public boolean canScope() {
        return true;
    }

    public void StartQuickdraw(float dist) {
        Level level = this.getSelf().level();
        BlockHitResult bh = MainUtil.getAheadVec(this.getSelf(),dist);
        BlockPos bp = bh.getBlockPos();
        if (level.getBlockState(bp).isAir()) {
            for(int i=0;i<5;i++) {
                bp = bp.below();
                if (!level.getBlockState(bp).isAir()) {
                    break;
                }
            }
        }
        if (!level.getBlockState(bp).isAir() && level.getBlockState(bp.above().above()).isAir()) {
            if (bp.getY()-this.getSelf().getY() > 2) {return;}
            bp = bp.above();
            Vec3 pos = this.getSelf().getPosition(1F);
            Vec3 npos = new Vec3(bp.getX(),bp.getY(),bp.getZ());
            Vec3 dpos = npos.subtract(pos);
            List<Entity> entities = new ArrayList<Entity>();
            int intervals = 5;
            for(int i=0;i<intervals-1;i++) {
                float d = 1F/intervals*i;
                Vec3 spos = pos.add(dpos.scale(d));
                List<Entity> targets = MainUtil.genHitbox(level,spos.x,spos.y,spos.z,2,1.5,2);
                targets = doAttackChecks(targets);
                for (Entity entity : targets) {
                    if (!entities.contains(entity)) {entities.add(entity);}
                }
            }
            this.targets = entities;

            this.setAttackTimeMax(70);
            this.getSelf().teleportTo(bp.getX(),bp.getY(),bp.getZ());
            this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_BARRAGE_END_EVENT,SoundSource.PLAYERS,1.5F,0.9F);
        } else {
            this.setAttackTimeMax(15);
        }

        this.setAttackTime(quickdrawDelay+1);
        this.setAttackTimeDuring(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowerIndex.BARRAGE,this.getAttackTimeMax());
        }
    }

    public void UpdateQuickdraw() {
        ((StandUser)this.getSelf()).roundabout$setMeleeImmunity(3);
        int duration = 15;
        for (Entity entity : this.targets) {
            if (entity instanceof LivingEntity LE) {
                ((StandUser) entity).roundabout$setDazed((byte) 3);
            }
            if (this.getAttackTimeDuring() > duration) {
                if (StandRushDamageEntityAttack(entity, 3F, 0F, this.getSelf())) {
                    MainUtil.takeKnockbackWithY(entity, 0.9, 0, -1, 0);
                }
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_BARRAGE_1_EVENT,SoundSource.PLAYERS,1.5F,1.0F);
            } else if (this.getSelf().tickCount%2 == 1) {
                if (StandRushDamageEntityAttack(entity, getBarrageHitStrength(entity), 0F, this.getSelf())) {
                    MainUtil.takeUnresistableKnockbackWithY(entity, 0.01, 0, -1, 0);
                    this.hitParticles(entity);
                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_BARRAGE_1_HIT_EVENT,SoundSource.PLAYERS,1.0F,0.9F +(float)(Math.random()*0.2));
                }
            }
        }
        if (this.getAttackTimeDuring() > duration) {
            this.targets = new ArrayList<Entity>();
            setPowerNone();
            if (this.getSelf() instanceof Player P) {
                S2CPacketUtil.sendActivePowerPacket(P, PowerIndex.NONE);
            }
        }

    }

    public void BarrageSlash() {

        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }
        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown);
        this.setAttackTime(0);
        this.setAttackTimeDuring(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        this.setPowerNone();
        float knockbackStrength = 1.25F + (this.getSelf().isSprinting() ? 0.1F : 0F);

        List<Entity> entities =  defaultSwordHitbox(this.getSelf(),1.5, 3.3);
        for (Entity e : entities ) {
            if (e != null) {
                if (e.distanceTo(this.getSelf()) < 1.5F) {
                    knockbackStrength += 0.15F;
                    this.setAttackTime(0);
                    this.setAttackTimeMax(this.getAttackTimeMax()+5);
                }

                float pow = getHeavyPunchStrength(e);
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {
                    this.getSelf().level().playSound(null,this.getSelf().blockPosition(),SoundEvents.PLAYER_ATTACK_KNOCKBACK,SoundSource.PLAYERS,1F,0.4F + (float)(Math.random()*0.2));
                    if (e instanceof LivingEntity LE) {
                        addEXP(2);
                    }
                    this.takeDeterminedKnockback(this.getSelf(), e, knockbackStrength);

                    /// knocks you back slightly if you hit it
                    Options o = Minecraft.getInstance().options;
                    if (!o.keyUp.isDown()) {
                        Vec3 look = this.getSelf().getLookAngle();
                        look = new Vec3(look.x,-0.1,look.z).normalize();
                        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),0.15F,look.x,look.y,look.z);
                    }


                } else {
                    if (e instanceof LivingEntity LE) {
                        if (LE.isBlocking()) {
                            MainUtil.knockShieldPlusStand(e,200);
                        }
                    }
                }

            }
        }
        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
                if (this.getSelf() instanceof Player P) {
                    P.crit(e);
                }
            }

        }
    }

    @Override
    public boolean setPowerNone() {
        return super.setPowerNone();
    }

    public Component getPosName(byte posID){
        return switch (posID) {
            case (byte) 1 -> Component.translatable("idle.roundabout.anubis_2");
            default -> Component.translatable("idle.roundabout.anubis_1");
        };
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte)0);
        $$1.add((byte)1);
        return $$1;
    }

    public static final byte
            ANIME = 1,
            EVIL = 2;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME,
                EVIL
        );
    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case PowersAnubis.EVIL -> Component.translatable("skins.roundabout.anubis.evil");
            default -> Component.translatable("skins.roundabout.anubis.anime");
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public byte worthinessType(){
        return HUMANOID_WORTHY;
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
        return Component.literal(  "Prisma").withStyle(ChatFormatting.YELLOW);
    }

    public List<Entity> getBasicSwordHitBox(boolean crouching) {
        List<Entity> entities = defaultSwordHitbox(this.getSelf(),1.2, 3.5);
        if (crouching) {
            entities = defaultSwordHitbox(this.getSelf(),1.7,2.5);
        }
        return entities;
    }

    public List<Entity> defaultSwordHitbox(Entity e,double width, double forwards) {
        Vec3 pos = e.getEyePosition(0F).add(e.getLookAngle().scale(forwards));
        double yrot = Math.toRadians(this.getSelf().getViewYRot(0F));

        Vec3 forward = new Vec3(Math.cos(yrot+Math.PI/2),0,Math.sin(yrot+Math.PI/2));
        Vec3 left = new Vec3(forward.z,0,forward.x);


        Vec3 offset = Vec3.ZERO;
        offset = offset.add(left.scale(width));
        offset = offset.add(forward.scale(forwards));

        List<Entity> list = MainUtil.genHitbox(e.level(), pos.x, pos.y, pos.z,1+Math.abs(offset.x),2,1+Math.abs(offset.z));


        list = doAttackChecks(doAttackChecks(list));
        double size =(double) Math.max(width,forwards);
        list.removeIf(entity -> entity.getPosition(1F).distanceTo(e.getPosition(1F)) > size );
        list.remove(e);
        return list;
    }
    public List<Entity> doAttackChecks(List<Entity> list) {
        list.remove(this.getSelf());
        list.removeIf(Entity -> !Entity.isAttackable() );
        list.removeIf(entity -> (entity instanceof TamableAnimal TA && Objects.equals(TA.getOwner(), this.getSelf())) );
        return  list;
    }

    public void addMomentum(float x, float y, float z) {
        addMomentum(new Vec3(x,y,z));
    }
    public void addMomentum(Vec3 v) {
        this.getSelf().hasImpulse = true;
        this.getSelf().hurtMarked = true;
        this.getSelf().setDeltaMovement(this.getSelf().getDeltaMovement().add(v));
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    List<Pair<List<Byte>,Integer>> visualValues = new ArrayList<>();
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        boolean renderingSomething = false;

        float attackTimeDuring = this.getAttackTimeDuring();
        if (standOn && this.isClashing()) {
            renderingSomething = true;
            int ClashTime = 15 - Math.round((attackTimeDuring / 60) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && this.isBarrageAttacking() && attackTimeDuring > -1) {
            renderingSomething = true;
            int ClashTime = 15 - Math.round((attackTimeDuring / this.getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && this.isBarrageCharging()) {
            renderingSomething = true;
            int windup = this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 ? this.getKickBarrageWindup() : this.getBarrageWindup();
            int ClashTime = Math.round(( Math.min(attackTimeDuring,windup) / windup) * 15);
            int height = 30;
            if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE) {
                if (this.getAttackTimeDuring() > this.getBarrageMinimum()) {
                    height -= 6;
                }
            }
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, height, ClashTime, 6);

        } else {
            int barTexture = 0;

            List<Entity> TE = getBasicSwordHitBox(this.isHoldingSneak());
            float attackTimeMax = this.getAttackTimeMax();
            if (this.getAttackTime() > this.getAttackTimeMax()) {
                this.lasthits = new ArrayList<>();
            }
            if (attackTimeMax > 0) {
                float attackTime = this.getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {


                    if (this.getActivePowerPhase() == standUser.roundabout$getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else {
                        if (!TE.isEmpty()) {
                            barTexture = 12;
                        } else {
                            barTexture = 18;
                        }
                    }


                    renderingSomething = true;
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (standOn)  {
                if (!TE.isEmpty()) {
                    if (barTexture == 0) {
                        renderingSomething = true;
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
        if (canPogo && this.getAttackTimeDuring() == -1 && renderingSomething) {
            context.blit(StandIcons.JOJO_ICONS,k,j,193,60,15,6);
        }


        ///  memory rendering
        if (!visualValues.isEmpty()) {
            for (int i = 0; i < visualValues.size(); i++) {
                Pair<List<Byte>, Integer> pair = visualValues.get(i);
                renderMoment(context, pair.getA().toArray(new Byte[0]), (visualValues.size() - i) * 8, pair.getB());
            }
        }

    }
    public void renderMoment(GuiGraphics context, Byte[] moments,int offset, int time) {
        int Offset = offset + 4 + ( (this.getStandUserSelf().roundabout$getActive() || !FateTypes.isHuman(this.getSelf())) ? 24 : 0);
        int xoff = 1;
        for (int i=0;i<moments.length;i++) {
            int xIcon = 7 * switch (moments[i]) {
                case AnubisMoment.UP -> 0;
                case AnubisMoment.DOWN -> 1;
                case AnubisMoment.LEFT -> 2;
                case AnubisMoment.RIGHT -> 3;
                case AnubisMoment.JUMP -> 4;
                case AnubisMoment.SPRINT -> 5;
                case AnubisMoment.CROUCH -> 6;
                case AnubisMoment.SUMMON -> 7;
                case AnubisMoment.ABILITY_1 -> 8;
                case AnubisMoment.ABILITY_2 -> 9;
                case AnubisMoment.DASH -> 10;
                case AnubisMoment.ABILITY_3 -> 11;
                case AnubisMoment.ATTACK -> 12;
                case AnubisMoment.INTERACT -> 13;
                default -> 11;
            };
            if (moments[i] > 20 && moments[i] < 30) {
                xIcon = 7 * (10+moments[i]-20);
            }
            context.blit(StandIcons.ANUBIS_MEMORY,xoff,Offset,xIcon,55,7,7);
            xoff += 8;
        }
        if (moments.length == 0) {xoff +=8;}
        if (time != -1) {
            context.drawString(Minecraft.getInstance().font, "" + time, xoff, Offset, 16777215);
        }
    }


    public byte playSlot = (byte)-1;
    public int playTime = -1;
    public int maxPlayTime = -1;
    public void setPlayTime(int time) {
        playTime = time;
        this.maxPlayTime = time;
    }
    public void recordMemory(byte slot) {
        if (memories.isEmpty()) {return;}
        if (slot == (byte) -1 || slot == 8) {return;}



        playSlot = slot;
        setPlayTime(PowersAnubis.MaxPlayTime);

        visualValues = new ArrayList<>();

        AnubisMemory mem = this.memories.get(slot);
        this.memories.set(slot,new AnubisMemory(mem.item,new ArrayList<>() ) );
    }
    public void playbackMemory(byte slot) {
        if (memories.isEmpty()) {return;}
        if (slot == (byte) -1 || slot == 8) {return;}
        AnubisMemory memory = this.memories.get(slot);
        if (memory != null && !memory.moments.isEmpty()) {
            List<AnubisMoment> moments = this.memories.get(slot).moments;

            playSlot = slot;
            setPlayTime(PowersAnubis.MaxPlayTime-moments.get(0).time );
            Roundabout.LOGGER.info("{}/{}",moments.get(moments.size()-1).time,moments.get(0).time);
            this.getStandUserSelf().roundabout$setUniqueStandModeToggle(true);

        }


    }

    public static void generateMemories(PowersAnubis PA) {
        for (int i=0;i<8;i++) {
            List<AnubisMoment>  moment = new ArrayList<>();
            AnubisMemory AM = new AnubisMemory(ModItems.ANUBIS_ITEM, moment);
            PA.memories.add(AM);
        }

        Options o = Minecraft.getInstance().options;
        PA.playKeys.add(o.keyUp);PA.playBytes.add(AnubisMoment.UP);
        PA.playKeys.add(o.keyDown);PA.playBytes.add(AnubisMoment.DOWN);
        PA.playKeys.add(o.keyLeft);PA.playBytes.add(AnubisMoment.LEFT);
        PA.playKeys.add(o.keyRight);PA.playBytes.add(AnubisMoment.RIGHT);
        PA.playKeys.add(o.keyJump);PA.playBytes.add(AnubisMoment.JUMP);
        PA.playKeys.add(o.keySprint);PA.playBytes.add(AnubisMoment.SPRINT);
        PA.playKeys.add(o.keyShift);PA.playBytes.add(AnubisMoment.CROUCH);
        PA.playKeys.add(KeyInputRegistry.abilityThreeKey);PA.playBytes.add(AnubisMoment.DASH);
        PA.playKeys.add(KeyInputRegistry.summonKey);PA.playBytes.add(AnubisMoment.SUMMON);
        PA.playKeys.add(KeyInputRegistry.abilityOneKey);PA.playBytes.add(AnubisMoment.ABILITY_1);
        PA.playKeys.add(KeyInputRegistry.abilityTwoKey);PA.playBytes.add(AnubisMoment.ABILITY_2);
        PA.playKeys.add(KeyInputRegistry.abilityFourKey);PA.playBytes.add(AnubisMoment.ABILITY_3);
        PA.playKeys.add(o.keyAttack);PA.playBytes.add(AnubisMoment.ATTACK);
        PA.playKeys.add(o.keyUse);PA.playBytes.add(AnubisMoment.INTERACT);

        for(int i=0;i<AnubisMoment.HOTBAR.length;i++) {
            PA.playKeys.add(o.keyHotbarSlots[i]);PA.playBytes.add(AnubisMoment.HOTBAR[i]);
        }




    }
    public AnubisMemory getUsedMemory() {
        if (this.playSlot != -1) {
            if (!this.memories.isEmpty()) {
                return this.memories.get(this.playSlot);
            }
        }
        return null;
    }
    public int getLastMoment(int slot,byte type,int time) {
        AnubisMemory mem = this.memories.get(slot);
        List<AnubisMoment> moments = mem.moments;
        if (moments.isEmpty()) {return -1;}

        for(int i=moments.size()-1;i>=0;i--) {
            AnubisMoment moment = moments.get(i);
            if (moment.type == type) {
                if (time >= moment.time) {
                    return i;
                }
            }
        }

        return -1;
    }
    public boolean isPressed(byte id, int time) {
        return isPressed(this.playSlot,id,time);
    }
    public boolean isPressed(byte slot, byte id, int time) {
        int a = this.getLastMoment(slot,id,time);
        if (a != -1) {
            AnubisMoment moment = this.memories.get(slot).moments.get(a);
            return moment.vargs;
        }
        return false;
    }
    public void convertToVisual(byte slot,List<AnubisMoment> moments) {
        visualValues = new ArrayList<>();
        if (slot == (byte)-1) {return;}
        if (moments.isEmpty()) {return;}


        int maxTime = Math.min(PowersAnubis.MaxPlayTime,moments.get(moments.size()-1).time);

        for(int time = 0; time<maxTime; time++ ) {
            List<Byte> value = new ArrayList<>();
            for (int i = 0; i < this.playBytes.size(); i++) {
                if (isPressed(slot,playBytes.get(i), time)) {
                    value.add(playBytes.get(i));
                }
            }

            if (visualValues.isEmpty()) {
                visualValues.add(new Pair<>(value, 0));
            } else {
                Pair<List<Byte>, Integer> last = visualValues.get(visualValues.size() - 1);
                if (last.getA().equals(value)) {
                    visualValues.set(visualValues.size() - 1, new Pair<>(last.getA(), last.getB() + 1));
                } else {
                    visualValues.add(new Pair<>(value, 0));
                }
            }
        }
    }

}

