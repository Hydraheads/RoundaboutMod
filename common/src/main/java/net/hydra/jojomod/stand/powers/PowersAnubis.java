package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.gui.MemoryRecordScreen;
import net.hydra.jojomod.client.models.layers.anubis.AnubisAnimations;
import net.hydra.jojomod.client.models.layers.anubis.AnubisFirstPersonAnimations;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.entity.projectile.AnubisSlipstreamEntity;
import net.hydra.jojomod.entity.projectile.ThrownAnubisEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.AnubisEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.List;

public class PowersAnubis extends NewDashPreset {
    public PowersAnubis(LivingEntity self) {
        super(self);
    }

    public static boolean shouldDash(Mob M) {
        if (M == null) {return false;}
        if (M instanceof AnubisGuardian AG && !AG.hasTotem() ) {return false;}
        return ( M.isBaby() || !(MainUtil.isHumanoid(M)) ) && !(M instanceof AbstractIllager) ;
    }



    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.anubis_attack",
                "instruction.roundabout.press_attack", StandIcons.ANUBIS_SLASH,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.anubis_sneak_attack",
                "instruction.roundabout.press_attack_crouch", StandIcons.ANUBIS_UPPERCUT,0,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.anubis_pogo",
                "instruction.roundabout.press_attack_crouch", StandIcons.ANUBIS_DIVE_ATTACK,0,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.anubis_empower",
                "instruction.roundabout.press_skill", StandIcons.ANUBIS_EMPOWER,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.anubis_alluring_light",
                "instruction.roundabout.press_skill_crouch", StandIcons.ANUBIS_ALLURING_LIGHT,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.anubis_raging_light",
                "instruction.roundabout.press_skill_crouch", StandIcons.ANUBIS_RAGING_LIGHT,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, 0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.ANUBIS_GUARD,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, 0, "ability.roundabout.anubis_quickdraw",
                "instruction.roundabout.barrage", StandIcons.ANUBIS_BARRAGE,1,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118, 0, "ability.roundabout.anubis_shieldbreak",
                "instruction.roundabout.kick_barrage", StandIcons.ANUBIS_SHIELDBREAK,1,level,bypass));


        $$1.add(drawSingleGUIIcon(context, 18, leftPos+77, topPos+80, 0, "ability.roundabout.anubis_dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,4, "ability.roundabout.anubis_backflip",
                "instruction.roundabout.press_skill_crouch", StandIcons.ANUBIS_BACKFLIP,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,7, "ability.roundabout.anubis_sword_throw",
                "instruction.roundabout.press_skill_crouch", StandIcons.ANUBIS_SWORD_THROW,4,level,bypass));


        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.anubis_record",
                "instruction.roundabout.press_skill", StandIcons.ANUBIS_RECORD,4,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.anubis_replay",
                "instruction.roundabout.press_skill", StandIcons.ANUBIS_REPLAY,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.anubis_mouse",
                "instruction.roundabout.press_skill_memory", StandIcons.ANUBIS_DMOUSE,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.anubis_exp",
                "instruction.roundabout.passive", StandIcons.ANUBIS_EXP,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99,0, "ability.roundabout.anubis_speed",
                "instruction.roundabout.passive", StandIcons.ANUBIS_SPEED,3,level,bypass));

        return $$1;
    }

    @Override
    public byte getMaxLevel() {
        return 7;
    }

    @Override
    public void levelUp() {
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowerIndex.EXTRA_2,0);
        }
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 4) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.memory_and_ability").
                        withStyle(ChatFormatting.AQUA), true);
            } else if (level == 7) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.memory").
                        withStyle(ChatFormatting.AQUA), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.memory").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    public static final int MaxPossessionTime = 200;
    public int  getMaxPlayTime() {
        if (this.getSelf() instanceof Player P) {
            boolean bypass = (P.isCreative()) || (this.getStandUserSelf().roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem);
            IPlayerEntity IPE = (IPlayerEntity) P;

            return (int) (ConfigManager.getConfig().anubisSettings.anubisMaxMemory * (bypass ? 1 : 0.5 + (0.5 * ((float) IPE.roundabout$getStandLevel() / this.getMaxLevel()  )) ) );
        }
        return -1;
    }
    private static final int PogoDelay = 6;
    public int getPogoDelay(boolean stab) {
        return stab ? 11 : 6;
    }
    public int getPogoDelay() {
        return getPogoDelay(this.getActivePower() == PowersAnubis.STAB);
    }
    public boolean isPogoing() {
        return this.getActivePower() == PowersAnubis.POGO || this.getActivePower() == PowersAnubis.STAB;
    }


    public List<AnubisMemory> memories = new ArrayList<>();
    public final List<KeyMapping> playKeys = new ArrayList<>();
    public final List<Byte> playBytes = new ArrayList<>();
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        if (standSkin == (byte)0 ) {
            standSkin = 1;
        }
        return new PowersAnubis(entity);
    }


    public boolean canSummonStandAsEntity(){
        return isEmpowered() || this.getActivePower() >= CLEAVE || this.getActivePower() == PowerIndex.POWER_4_SNEAK;
    }
    @Override
    public StandEntity getNewStandEntity() {
        return new AnubisEntity(ModEntities.ANUBIS,this.getSelf().level());
    }
    @Override
    public void animateStand(byte r) {}
    @Override
    public boolean rendersPlayer(){return true;}
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_ALLURING_LIGHT, PowerIndex.SKILL_1_SNEAK);
            setSkillIcon(context, x, y, 2, StandIcons.ANUBIS_RAGING_LIGHT, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_EMPOWER, PowerIndex.SKILL_1);

            ResourceLocation icon2 = StandIcons.ANUBIS_REPLAY;
            if (this.playTime > 0) {
                icon2 = StandIcons.ANUBIS_CANCEL;
            } else if (Minecraft.getInstance().screen instanceof MemoryRecordScreen MA && MA.recording) {
                icon2 = StandIcons.ANUBIS_DMOUSE;
            }
            setSkillIcon(context, x, y, 2, icon2, PowerIndex.SKILL_2);
        }

        if (!isHoldingSneak()  || isGuarding()) {
            setSkillIcon(context, x, y, 3,isEmpowered() ? StandIcons.ANUBIS_SPEED : StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            ResourceLocation icon = StandIcons.LOCKED;
            if ( canExecuteMoveWithLevel(4) ) {icon = StandIcons.ANUBIS_BACKFLIP;}

            setSkillIcon(context, x, y, 3, icon, PowerIndex.GLOBAL_DASH,icon != StandIcons.ANUBIS_BACKFLIP);
        }

        ResourceLocation icon4 = StandIcons.ANUBIS_RECORD;
        if (this.isHoldingSneak()) {
            boolean bl = this.canExecuteMoveWithLevel(7);
            setSkillIcon(context, x, y, 4, bl ? StandIcons.ANUBIS_SWORD_THROW : StandIcons.LOCKED, PowerIndex.SKILL_4_SNEAK,!bl);

        } else {
            if (this.playTime > 0) {
                if (this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
                    icon4 = StandIcons.ANUBIS_CANCEL;
                } else {
                    icon4 = StandIcons.ANUBIS_SAVE;
                }
            }
            setSkillIcon(context, x, y, 4, icon4, PowerIndex.SKILL_4);

        }

        super.renderIcons(context, x, y);
    }

    private boolean shouldSlow() {
        return this.getActivePower() == PowerIndex.GUARD
                || this.getActivePower() == PowersAnubis.CLEAVE
                || this.getActivePower() == PowersAnubis.SPIN
                || this.getActivePower() == PowersAnubis.FLURRY
                || this.isEmpowered();
    }
    @Override
    public float inputSpeedModifiers(float basis) {
        if ( PowerTypes.hasStandActive(self)
                && this.getActivePower() != PowerIndex.GUARD
                && this.getActivePower() != PowerIndex.BARRAGE_CHARGE
                && !shouldSlow()) {
            if (!(this.getSelf() instanceof Mob)) {
                basis *= this.getSelf().isSprinting() ? 1.6F : 1F;
            }
        }
        switch (this.getActivePower()) {
            case PowerIndex.RANGED_BARRAGE -> basis *= 0.2F;
            case PowersAnubis.CLEAVE ->  basis *= 0.7F;
            case PowersAnubis.SPIN -> basis *= 0.4F;
            case PowersAnubis.FLURRY -> basis *= 0.3F;
        }
        if (this.isEmpowered()) {basis *= 0.85F;}

        return super.inputSpeedModifiers(basis);
    }

    @Override
    public int getJumpHeightAddon() {
        if (PowerTypes.hasStandActive(self)
                && this.getActivePower() != PowerIndex.BARRAGE_CHARGE
                && this.getActivePower() != PowerIndex.BARRAGE_2
                && !(shouldSlow() && !isEmpowered())  ) {
            return 1;
        }
        return super.getJumpHeightAddon();
    }

    @Override
    public boolean cancelSprintJump() {
        if (shouldSlow()) {
            return true;
        }
        return super.cancelSprintJump();
    }

    private boolean empower = false;
    public boolean isEmpowered() {return empower;}
    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> EmpowerClient();
            case SKILL_1_CROUCH -> AlluringLightClient();
            case SKILL_2_CROUCH -> RagingLightClient();
            case SKILL_2_NORMAL -> {
                if (this.playTime > 0) {
                    MemoryCancelClient();
                } else if (Minecraft.getInstance().screen instanceof MemoryRecordScreen MA && MA.recording) {
                    MemoryChangeClient();
                } else {
                    MemoryPlayClient();
                }
            }
            case SKILL_3_NORMAL -> {
                if(isEmpowered() && this.getSelf().onGround() && !this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                    tryPowerPackets(PowersAnubis.WEAVE);
                }
                dash();
            }
            case SKILL_3_CROUCH -> BackflipClient();


            case SKILL_4_NORMAL -> {
                if (this.playTime > 0) {
                    if (this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
                        MemoryCancelClient();
                    } else {
                        MemorySaveClient(true);
                    }
                } else {
                    MemoryRecordClient();
                }
            }
            case SKILL_4_CROUCH -> SwordThrowClient();
        }
    }

    public void EmpowerClient() {
        if (!onCooldown(PowerIndex.SKILL_1) && !isAttackIneptVisually(this.getActivePower(),1)) {
            tryPowerPackets(PowerIndex.POWER_1_BONUS);
        }
    }

    public void AlluringLightClient() {
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.setCooldown(PowerIndex.SKILL_1_SNEAK,200);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }
    public void AlluringLightServer() {
        this.setCooldown(PowerIndex.SKILL_1_SNEAK,200);
        int radius = 8;
        AABB box = this.getSelf().getBoundingBox().inflate(radius,2,radius);

        boolean bypass = this.getStandUserSelf().roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem || (this.getSelf() instanceof Player P && P.isCreative());
        int time = 160;
        if (!bypass && this.getSelf() instanceof Player P) {
            IPlayerEntity IPE = (IPlayerEntity) P;
            time =(int) ( time * Math.min(1,(double) IPE.roundabout$getStandLevel() / this.getMaxLevel()+0.2F) );
        }

        for (Mob M : this.getSelf().level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT,this.getSelf(),box)) {
            if (!(M instanceof FallenMob)) {
                ((IMob) M).roundabout$setHypnotizedBy(this.getSelf(), time);
            }
        }

        Vec3 pos = this.getSelf().getPosition(1);

        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.ALLURING_LIGHT,
                pos.x,
                pos.y + this.getSelf().getEyeHeight(),
                pos.z,
                30, 0, 0, 0, 0.4);
    }

    public void RagingLightClient() {
        if (!onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            setCooldown(PowerIndex.SKILL_2_SNEAK,200);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }
    public void RagingLightServer() {
        this.setCooldown(PowerIndex.SKILL_2_SNEAK,200);
        this.addEXP(AnubisItem.aggroOnto(this.getSelf()) );
    }


    public void BackflipClient() {
        if (!onCooldown(PowerIndex.GLOBAL_DASH) && canExecuteMoveWithLevel(4) ) {
            tryPowerPackets(PowerIndex.SNEAK_MOVEMENT);

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

    public void MemoryChangeClient() {
        if (!isAttackIneptVisually(PowerIndex.SKILL_2,2)) {
            MemoryRecordScreen MA = (MemoryRecordScreen) Minecraft.getInstance().screen;
            if (MA != null) {
                byte type = this.memories.get(MA.currentlyHovered).memory_type;
                this.memories.get(MA.currentlyHovered).memory_type = (byte) ((type + 1) % 2);
                SaveMemories();
            }
        }
    }


    public void MemorySaveClient(boolean rotSave) {
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);

        AnubisMemory memory = this.getUsedMemory();
        List<AnubisMoment> moments = memory.moments;
        int time = (ConfigManager.getConfig().anubisSettings.anubisMaxMemory-this.playTime)+1;
        for (Byte playByte : playBytes) {
            if (isPressed(playByte, time)) {
                moments.add(new AnubisMoment(playByte, Math.min(this.getMaxPlayTime(), time), false));
            }
        }

        if (rotSave) {
            List<Vec3> newRots = new ArrayList<>();
            for (int i = 1; i < memory.rots.size(); i++) {
                Vec3 rot = memory.rots.get(i);
                Vec3 pRot = memory.rots.get(i - 1);
                Vec3 fRot = new Vec3(rot.x, rot.y - pRot.y, rot.z - pRot.z);
                newRots.add(fRot);
            }
            memory.rots = newRots;
            newRots = new ArrayList<>();
            for (Vec3 vec3 : memory.rots ) {
                if (newRots.isEmpty() || newRots.get(newRots.size()-1).x != vec3.x) {
                    newRots.add(vec3);
                } else {
                    Vec3 rot = newRots.get(newRots.size()-1);
                    newRots.set(newRots.size()-1,new Vec3(rot.x,rot.y+vec3.y,rot.z+vec3.z) );
                }
            }
            memory.rots = newRots;

        }


        SaveMemories();

        setPlayTime(-1);
        this.playSlot = (byte)-1;
    }
    public void SaveMemories() {
        for(int i=0;i<8;i++) {
            String cf = convertToConfig(i);
            if (cf != null) {
                ConfigManager.getClientConfig().anubisMemories.saveToMemory(i+1,cf);
                ConfigManager.saveClientConfig();
            }
        }
    }


    public void MemoryCancelClient() {
        if (!this.getStandUserSelf().roundabout$getUniqueStandModeToggle() && !this.memories.isEmpty() && this.playSlot != -1) {
            this.memories.set(this.playSlot,lastMemory);
            lastMemory = null;
        }
        this.visualValues = new ArrayList<>();
        setPlayTime(-1);
        this.getStandUserSelf().roundabout$setUniqueStandModeToggle(false);
    }

    public void SwordThrowClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK) && canExecuteMoveWithLevel(7)) {
            tryPowerPackets(PowerIndex.POWER_4_SNEAK);
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 1) {
            return this.isBarrageAttacking() || this.isBarrageCharging() || this.getActivePower() == PowersAnubis.POGO;
        }
        switch (activeP) {
            case PowerIndex.SKILL_4 -> {
                if (!Minecraft.getInstance().mouseHandler.isMouseGrabbed()) {
                    return true;
                }
            }
            case PowerIndex.SKILL_2 -> {
                if ( (Minecraft.getInstance().screen instanceof MemoryRecordScreen MA && (MA.currentlyHovered == -1 || MA.currentlyHovered == 8)  ) ) {
                    return true;
                }
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {

        if (!isClient() && this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP,100,false);
            this.stopSoundsIfNearby(SoundIndex.ALT_CHARGE_SOUND_1,100,false);
        }

        StandUser SU = (StandUser) this.getSelf();
        switch (move) {
            case PowersAnubis.POGO -> disablePogo();

            case PowerIndex.POWER_1 ->  this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_ALLURING_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
            case PowerIndex.POWER_1_SNEAK -> this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_RAGING_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
            case PowerIndex.BARRAGE -> {
                this.setActivePower(PowerIndex.RANGED_BARRAGE);
                setPowerOther(PowerIndex.RANGED_BARRAGE,this.getActivePower());
            }

            case PowerIndex.BARRAGE_CHARGE_2 -> this.setAnimation(PowerIndex.BARRAGE_CHARGE_2);

            case PowerIndex.SNEAK_MOVEMENT -> {
                ///  gives you another pogo
                enablePogo();
                this.setAttackTimeDuring(0);
                this.setActivePower(PowerIndex.SNEAK_MOVEMENT);
                this.setCooldown(PowerIndex.GLOBAL_DASH, ConfigManager.getConfig().anubisSettings.anubisBackflipCooldown);
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ANUBIS_BACKFLIP_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);

                if (!isClient()) {
                    setAnimation(PowerIndex.SNEAK_MOVEMENT);
                    sendDoubleIntPacketIfNearby(PacketDataIndex.S2C_SNYC_ACTIVE_POWER,self.getId(),PowerIndex.SNEAK_MOVEMENT,100,false);
                }

                if (this.getSelf() instanceof Player P) {
                    P.getAbilities().flying = false;
                }

                Vec3 look = getSelf().getLookAngle().multiply(1, 0, 1).normalize();
                SU.roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                SU.roundabout$setLeapIntentionally(true);

                if (isPacketPlayer()) {
                    float strength = 1F;
                    if (this.getSelf().onGround()) {
                        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), strength, look.x, -1, look.z);
                    } else {
                        if (Math.abs(look.x) + Math.abs(look.z) == 0) {
                            strength *= 0.6F;
                        }
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
            case PowerIndex.POWER_1_BONUS -> {
                empower = !empower;
                if (empower) {
                    this.setAttackTime(0);
                    this.setAttackTimeDuring(0);
                    this.setAttackTimeMax(40);
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ANUBIS_EMPOWER_EVENT, SoundSource.PLAYERS, 1F, 1F);
                }
            }
            case PowerIndex.POWER_1 -> AlluringLightServer();
            case PowerIndex.POWER_1_SNEAK -> RagingLightServer();
            case PowerIndex.ATTACK -> tryBasicAttack((byte)move);
            case PowersAnubis.POGO -> tryPogoAttack(false);
            case PowerIndex.BARRAGE_CHARGE_2 -> {
                this.attackTimeDuring = 0;
                this.playSneakBarrageCharge();
                this.setActivePower(PowerIndex.BARRAGE_CHARGE_2);
            }
            case PowerIndex.RANGED_BARRAGE -> {
                this.setAttackTime(0);
                this.setActivePower(PowerIndex.RANGED_BARRAGE);
                this.setAnimation(PowerIndex.BARRAGE);
            }
            case PowerIndex.BARRAGE_2 -> BarrageSlash();

            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                this.attackTimeDuring = 0;
                this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
                this.setAnimation(PowerIndex.SNEAK_ATTACK_CHARGE);
            }
            case PowerIndex.SNEAK_ATTACK -> {
                this.chargedFinal = this.attackTimeDuring;

                this.setAttackTimeDuring(-10);
                this.setAttackTime(0);
                this.setAttackTimeMax((int) (30 + chargedFinal * 1.5));
                this.setActivePowerPhase(this.getActivePowerPhaseMax());

                this.setAnimation(PowerIndex.SNEAK_ATTACK);
                this.setActivePower(PowerIndex.SNEAK_ATTACK);
            }

            case PowerIndex.POWER_4_SNEAK -> {
                this.setAnimation(PowerIndex.POWER_4_SNEAK);
                this.setAttackTimeDuring(0);
                this.setCooldown(PowerIndex.SKILL_4_SNEAK,320);
                this.setActivePower(PowerIndex.POWER_4_SNEAK);
            }

            case PowersAnubis.CLEAVE -> setPowerVariant(PowersAnubis.CLEAVE,120);
            case PowersAnubis.SPIN -> {
                setPowerVariant(PowersAnubis.SPIN,140);
            }

            case PowersAnubis.FLURRY -> {
                setPowerVariant(PowersAnubis.FLURRY,140);
                this.setAttackTimeMax(50);
                this.setActivePowerPhase(this.getActivePowerPhaseMax());
            }
            case PowersAnubis.STAB -> {
                this.empower = false;
                disablePogo();
                this.setCooldown(PowerIndex.SKILL_1,120);
                tryPogoAttack(true);
            }
            case PowersAnubis.WEAVE -> {
                this.empower = false;
                this.iframeTicks = 15;
                this.setCooldown(PowerIndex.SKILL_1,80);
                this.setActivePower(PowersAnubis.WEAVE);
            }
            case PowersAnubis.LAUNCH -> setPowerVariant(PowersAnubis.LAUNCH,100);

        }
        return super.setPowerOther(move, lastMove);
    }

    private void setPowerVariant(byte move, int cooldown) {
        this.setAttackTimeDuring(0);
        this.setAttackTime(0);
        this.setActivePower(move);
        this.setCooldown(PowerIndex.SKILL_1,cooldown);
        this.setAnimation(move);
        this.empower = false;
    }

    @Override
    public ResourceLocation getIconYes(int slot) {
        if (slot == 1 && this.isEmpowered()) {
            return StandIcons.SQUARE_GOLD;
        }
        return super.getIconYes(slot);
    }

    public void playSneakBarrageCharge() {
        if (!this.self.level().isClientSide()) {
            playSoundsIfNearby(SoundIndex.ALT_CHARGE_SOUND_1, 27, false);
        }
    }

    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1) {
        ///  cancels the pogo during the windup
        if ((isPogoing())
                && getAttackTimeDuring() < getPogoDelay() ) {
            this.setPowerNone();
        }
    }

    List<Integer> lasthits = new ArrayList<>();


    int fallTime = 0;

    int pogoTime = 0;
    public boolean canPogo() {return pogoTime == 0;}
    public void enablePogo() {pogoTime = 0;}
    public void disablePogo() {pogoTime = -1;}
    public void setPogo(int i) {pogoTime = i;}
    int pogoCounter = 0;
    @Override
    public void tickPower() {

       // Roundabout.LOGGER.info("CA: " + this.getActivePower() + " | " + this.getAttackTime() + " | "+ this.getAttackTimeDuring() + "/" + this.getAttackTimeMax() + " PHASE: " + this.getActivePowerPhase());

        StandUser SU = this.getStandUserSelf();

        if (SU.roundabout$isSealed()) {MemoryCancelClient();}


        if (pogoTime > 0) {pogoTime -= 1;}
        if (this.getSelf().onGround() && this.isClient()) {
            if ((this.getActivePower() != PowersAnubis.POGO && this.getActivePower() != PowersAnubis.STAB) || this.attackTime <= getPogoDelay()) {
                if (isPogoing()) {
                    this.setPowerNone();
                }
                if (pogoTime == -1) {
                    setPogo(60);
                }
            }
        } else if (!canPogo()) {
            if (this.getSelf() instanceof Player P) {
                if (P.isCreative()) { enablePogo();}
            }
        }

        tickSlipStream();

        tickMemories();

        tickExtras();

        this.getSelf().setNoGravity(this.isPogoing());

        super.tickPower();
    }

    AnubisSlipstreamEntity lastSlipstream = null;
    float slipstreamTimer = 3;
    public void tickSlipStream() {
        if (!this.isClient()) {
            boolean noSlip = this.getActivePower() == PowerIndex.SNEAK_MOVEMENT || this.getActivePower() == PowersAnubis.POGO;
            if (this.getSelf().isSprinting() && PowerTypes.hasStandActive(self) && !noSlip) {

                float dif = this.getSelf().walkDist-this.getSelf().walkDistO;
                if (dif != 0) {
                    slipstreamTimer -= dif;
                }

                if (slipstreamTimer <= 0) {
                    slipstreamTimer = 3;
                    AnubisSlipstreamEntity ASE = new AnubisSlipstreamEntity(ModEntities.ANUBIS_SLIPSTREAM,this.getSelf().level(),60,lastSlipstream);
                    ASE.setPos(this.getSelf().getPosition(1F));
                    this.getSelf().level().addFreshEntity(ASE);
                    lastSlipstream = ASE;
                }
            }
        }
    }

    @Override
    public boolean hasPassiveCombatMode(){
        return true;
    }


    public void tickMemories() {
        if (this.memories.size() != 8 && isClient()) {
            generateMemories(this);
        }

        AnubisMemory memory = this.getUsedMemory();
        if (this.playTime > 0 && isClient()) {



            if (!this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {

                List<Byte> value = new ArrayList<>();
                for (int i=0;i<this.playKeys.size();i++) {
                    KeyMapping key = playKeys.get(i);

                    /// visualData storing
                    if(key.isDown()) {value.add(playBytes.get(i));}

                    /// gets the last instance of a key being saved
                    int time = this.getMaxPlayTime()-this.playTime;
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

                        if(PowerTypes.hasStandActive(self)) {
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
                visualDuration = 40;
            }

            /// playback section
            boolean bl = false;
            if (this.getStandUserSelf().roundabout$getUniqueStandModeToggle()) {
                if (memory != null) {
                    int time = this.getMaxPlayTime()-this.playTime;
                    for (int i=0;i<playBytes.size();i++) {
                        if (isPressed(playBytes.get(i),time) && !isPressed(playBytes.get(i),time-1)) {
                            ((IKeyMapping)this.playKeys.get(i)).roundabout$addClick();
                        }
                    }



                    if (memory.canPlayback()) {
                        if (this.getMaxPlayTime() - this.playTime > memory.getLastTime() ) {
                            bl = true;
                        }
                    }
                }
            }
            this.playTime--;

            if (this.playTime <= 0 || bl) {
                this.MemorySaveClient(!this.getStandUserSelf().roundabout$getUniqueStandModeToggle());
            }
        }
    }

    int visualDuration = 0;
    public void tickExtras() {
        StandUser SU = this.getStandUserSelf();

        if (!this.canSummonStandAsEntity() && this.getStandEntity(this.getSelf()) != null) {
            this.getStandEntity(this.getSelf()).forceDespawn(true);
        }

        if (iframeTicks > 0) {iframeTicks -= 1;}

        if (!this.isClient()) {
            // guard
            if (isGuarding()) {
                setAnimation(PowerIndex.GUARD);
            } else if (getStandUserSelf().roundabout$getStandAnimation() == PowerIndex.GUARD) {
                setAnimation(NONE);
            }

            // backflip go away
            if (SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_MOVEMENT) {
                if (this.getAttackTimeDuring() > 16 || this.getActivePower() != PowerIndex.SNEAK_MOVEMENT) {
                    setAnimation(PowerIndex.NONE);
                }
            }
            // pogo
            else if(SU.roundabout$getStandAnimation() == PowersAnubis.POGO || SU.roundabout$getStandAnimation() == PowersAnubis.STAB ) {
                if (this.getSelf().onGround()) {
                    setAnimation(PowerIndex.NONE);
                    this.getStandUserSelf().roundabout$getWornStandAnimation().stop();
                }
            }
        }
        if (isClient()) {
            if (SU.roundabout$getStandAnimation() != PowerIndex.NONE
                    && SU.roundabout$getStandAnimation() != PowerIndex.BARRAGE_CHARGE_2
                    && SU.roundabout$getStandAnimation() != PowerIndex.BARRAGE_CHARGE
                    && SU.roundabout$getStandAnimation() != PowerIndex.GUARD
                    && PowersAnubis.getAnimation(SU) != null) {
                float current = SU.roundabout$getWornStandAnimation().getAccumulatedTime() / 1000F;
                float max = PowersAnubis.getAnimation(SU).lengthInSeconds();
                if (current > max) {
                    this.setAnimation(PowerIndex.NONE);
                }
            }
        }



        /// fastfalling
        if (this.getActivePower() == PowerIndex.SNEAK_MOVEMENT) {
            if (this.getAttackTimeDuring() > 8 && this.getAttackTimeDuring() < 20) {
                if(this.getSelf().isCrouching()) {
                    this.addMomentum(0,-0.045F,0);
                }
            } else if (this.getAttackTimeDuring() > 20) {
                this.setPowerNone();
            }
        }
        /// visual banishing
        visualDuration -= 1;
        if (visualDuration <= 0) {
            visualDuration = 0;
            visualValues = new ArrayList<>();
        }

        if (this.getSelf().onGround()) {
            this.fallTime = 0;
        } else {
            this.fallTime += 1;
        }
    }

    public static AnimationDefinition getAnimation(StandUser SU) {
        AnimationDefinition anim = null;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
            boolean leftArm = ((LivingEntity)SU).getMainArm() == HumanoidArm.LEFT;
            switch (SU.roundabout$getStandAnimation()) {
                case PowerIndex.SNEAK_MOVEMENT -> {
                    if (PA.getAttackTime() < 16) {
                        anim = AnubisAnimations.Backflip;
                    }
                }
                case PowerIndex.GUARD -> anim = leftArm ? AnubisAnimations.L_Block : AnubisAnimations.Block;
                case PowersAnubis.POGO -> anim = AnubisAnimations.PogoReady;
                case PowerIndex.BARRAGE_CHARGE_2 -> anim = leftArm ? AnubisAnimations.L_ShieldbreakCharge : AnubisAnimations.ShieldbreakCharge;
                case PowerIndex.BARRAGE_2 -> anim = leftArm ? AnubisAnimations.L_ShieldbreakHit : AnubisAnimations.ShieldbreakHit;
                case PowerIndex.BARRAGE_CHARGE-> anim = leftArm ? AnubisAnimations.L_BarrageCharge : AnubisAnimations.BarrageCharge;
                case PowerIndex.BARRAGE -> anim = leftArm ? AnubisAnimations.L_BarrageDash : AnubisAnimations.BarrageDash;
                case PowerIndex.SNEAK_ATTACK_CHARGE -> anim = leftArm ? AnubisAnimations.L_UppercutCharge : AnubisAnimations.UppercutCharge;
                case PowerIndex.SNEAK_ATTACK -> anim = leftArm ? AnubisAnimations.L_UppercutRelease : AnubisAnimations.UppercutRelease;
                case PowerIndex.ATTACK -> {
                    if (PA.activePowerPhase == 1) {
                        anim = leftArm ? AnubisAnimations.L_Attack : AnubisAnimations.Attack;
                    }  else if (PA.activePowerPhase == 2) {
                        anim = leftArm ? AnubisAnimations.L_Attack2 : AnubisAnimations.Attack2;
                    } else {
                        anim = leftArm ? AnubisAnimations.L_Attack3 : AnubisAnimations.Attack3;
                    }
                }
                case PowersAnubis.CLEAVE -> anim = leftArm ? AnubisAnimations.L_Cleave : AnubisAnimations.Cleave;
                case PowersAnubis.SPIN -> anim = leftArm ? AnubisAnimations.L_Spin : AnubisAnimations.Spin;
                case PowersAnubis.LAUNCH -> anim = leftArm ? AnubisAnimations.L_Launch : AnubisAnimations.Launch;
                case PowersAnubis.FLURRY -> anim = leftArm ? AnubisAnimations.L_Flurry : AnubisAnimations.Flurry;
                case PowerIndex.POWER_4_SNEAK -> anim = leftArm ? AnubisAnimations.L_SwordThrow : AnubisAnimations.SwordThrow;
                case PowersAnubis.STAB -> anim = leftArm ? AnubisAnimations.L_Stab : AnubisAnimations.Stab;
            }
        }
        return anim;
    }

    public static AnimationDefinition getFirstPersonAnimation(StandUser SU) {
        AnimationDefinition anim = null;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
            anim = switch (SU.roundabout$getStandAnimation()) {
                case PowerIndex.GUARD -> AnubisFirstPersonAnimations.Block;
                case PowerIndex.BARRAGE -> AnubisFirstPersonAnimations.BarrageDash;
                case PowerIndex.BARRAGE_CHARGE -> AnubisFirstPersonAnimations.BarrageCharge;
                case PowerIndex.BARRAGE_CHARGE_2 -> AnubisFirstPersonAnimations.Shieldbreak;
                case PowerIndex.BARRAGE_2 -> AnubisFirstPersonAnimations.ShieldbreakHit;
                case PowerIndex.SNEAK_ATTACK_CHARGE -> AnubisFirstPersonAnimations.UppercutCharge;
                case PowerIndex.SNEAK_ATTACK -> AnubisFirstPersonAnimations.UppercutRelease;
                case PowersAnubis.POGO -> AnubisFirstPersonAnimations.Pogo;
                case PowersAnubis.FLURRY -> AnubisFirstPersonAnimations.Flurry;
                case PowersAnubis.CLEAVE -> AnubisFirstPersonAnimations.Cleave;
                case PowersAnubis.SPIN -> AnubisFirstPersonAnimations.SwordSpin;
                case PowersAnubis.STAB -> AnubisFirstPersonAnimations.Stab;
                case PowerIndex.POWER_4_SNEAK -> AnubisFirstPersonAnimations.SwordThrow;
                case PowersAnubis.LAUNCH -> AnubisFirstPersonAnimations.Launch;

                default -> null;
            };
            if (anim == null) {
                switch (SU.roundabout$getStandAnimation()) {
                    case PowerIndex.ATTACK -> {
                        if (PA.activePowerPhase == 1) {
                            anim = AnubisFirstPersonAnimations.Attack;
                        } else if (PA.activePowerPhase == 2) {
                            anim = AnubisFirstPersonAnimations.Attack2;
                        } else if (PA.activePowerPhase == 3) {
                            anim = AnubisFirstPersonAnimations.Attack3;
                        }
                    }
                }
            }
        }
        return anim;
    }

    public void setAnimation(byte b) {
        if (this.getSelf() instanceof Player P && this.isClient()) {
            ((IPlayerEntity)P).roundabout$SetPoseEmote(Poses.NONE.id);
        }
        this.getStandUserSelf().roundabout$setStandAnimation(b);
        this.getStandUserSelf().roundabout$getWornStandAnimation().stop();
        this.getStandUserSelf().roundabout$getWornStandAnimation().startIfStopped(this.getSelf().tickCount);
    }

    private int iframeTicks = 0;
    @Override
    public boolean interceptDamageEvent(DamageSource $$0, float $$1) {
        if ( ($$0.is(DamageTypes.MOB_ATTACK)
                || $$0.is(DamageTypes.PLAYER_ATTACK)
                || $$0.is(ModDamageTypes.STAND)) && $$0.getEntity() != null ) {

            if (iframeTicks > 0) {
                playSoundIfPossible(self.level(),null,this.getSelf().blockPosition(),ModSounds.DODGE_EVENT,SoundSource.PLAYERS,1F,1F);
                return true;
            }
            return false;

        }
        if (!$$0.is(ModDamageTypes.TIME) && !$$0.is(ModDamageTypes.GO_BEYOND) && $$0.getEntity() != null ) {
            if (iframeTicks > 0 && this.getActivePower() == PowersAnubis.WEAVE) {
                playSoundIfPossible(self.level(),null,this.getSelf().blockPosition(),ModSounds.DODGE_EVENT,SoundSource.PLAYERS,1F,0.1f+((float) Math.random()*0.2F));
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean canAttack() {
        return super.canAttack() && this.activePowerPhase < this.activePowerPhaseMax && this.getActivePower() < CLEAVE;
    }


    /// CLIENT CHECK BEWARE
    public boolean pogoChecks() {
        return this.isHoldingSneak()
                && !this.getSelf().onGround()
                && canPogo()
                && this.getAttackTime() > 5
                && (this.fallTime > 3 || Minecraft.getInstance().options.keyJump.isDown())
                && this.getActivePower() != PowersAnubis.POGO
                && this.getActivePower() != PowersAnubis.FLURRY
                && this.getActivePower() != PowersAnubis.SPIN
                && this.getActivePower() != PowerIndex.SNEAK_ATTACK
                && this.getActivePower() != PowerIndex.SNEAK_ATTACK_CHARGE;
    }

    @Override
    public boolean interceptAttack(){return true;}
    public static final int maxSuperHitTime = 25;
    private int chargedFinal = 0;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (this.getStandUserSelf().roundabout$isDazed()) {return;}
        if ( (!keyIsDown || this.attackTimeDuring > maxSuperHitTime) && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            this.chargedFinal = this.attackTimeDuring;
            tryPowerPackets(PowerIndex.SNEAK_ATTACK);
        } else {
            if (keyIsDown) {
                if (pogoChecks() && !this.isBarrageCharging()) {
                    if (this.isEmpowered()) {
                        tryPowerPackets(PowersAnubis.STAB);
                    } else {
                        tryPowerPackets(PowersAnubis.POGO);
                    }

                } else {
                    if (isEmpowered()) {
                        if (this.isHoldingSneak()) {
                            tryPowerPackets(PowersAnubis.LAUNCH);
                        } else {
                            tryPowerPackets(PowersAnubis.CLEAVE);
                        }
                    } else if (this.canAttack()) {

                        if (this.isHoldingSneak()) {
                            tryPowerPackets(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            tryPowerPackets(PowerIndex.ATTACK);
                        }
                    }

                }
            }
        }
    }

    @Override
    public boolean setPowerAttack() {
        return setPowerOther(PowerIndex.ATTACK,this.getActivePower());
    }

    public void tryBasicAttack(byte move) {
        this.activePowerPhase++;
        if (this.activePowerPhase == 3) {
            this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown-7;
        } else {
            this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown;
        }

        this.attackTimeDuring = 0;
        this.setAttackTime(0);

        this.setAnimation(move);
        setActivePower(move);
    }



    public void updateAttack() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                setPowerNone();
            } else if (this.activePowerPhase == 1 && this.attackTimeDuring == 4 || this.attackTimeDuring == 5) {
                this.standPunch();
            }
        }
    }

    private final int quickdrawDelay = 4;
    @Override
    public void updateUniqueMoves() {
        switch (getActivePower()) {
            case PowerIndex.SNEAK_ATTACK -> updateUppercut();
            case PowersAnubis.POGO, PowersAnubis.STAB -> updatePogoAttack();
            case PowerIndex.RANGED_BARRAGE -> updateQuickdraw();
            case PowerIndex.POWER_4_SNEAK -> updateSwordThrow();
            case PowersAnubis.CLEAVE -> updateCleave();
            case PowersAnubis.SPIN -> updateSpin();
            case PowersAnubis.FLURRY -> updateFlurry();
            case PowersAnubis.LAUNCH -> updateLaunch();
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        switch (move) {
            case PowerIndex.BOUNCE -> {
                if (chargeTime != 0) {
                    pogoAttack(chargeTime);
                }
            }
            case PowerIndex.POWER_1_BONUS -> MainUtil.slowTarget(this.getSelf(),chargeTime/100F);

        }
        return super.tryIntPower(move, forced, chargeTime);
    }



    public void updateUppercut() {
        if (this.getAttackTimeDuring() < 5) {
            if (this.isPacketPlayer()) {
                if (self.onGround()) {
                    self.setDeltaMovement(self.getLookAngle().scale(0.4f));
                } else {
                    self.setDeltaMovement(self.getLookAngle().scale(0.3f));
                }
            }
            Entity TE2 = getTargetEntity(self, 1.4F, 40);
            if (TE2 != null) {
                this.uppercutAttack();
            }
        } else {
            this.playMissSound(this.getSelf().level());
            this.setPowerNone();
        }
    }

    public void uppercutAttack() {
        ((StandUser)this.getSelf()).roundabout$setBubbleEncased((byte)(0));
        this.getSelf().resetFallDistance();

        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.6F, 0, -1, 0);
        }
        super.setPowerNone();

        List<Entity> entities = getUppercutHitbox();
        for (Entity e : entities) {
            if (isPacketPlayer()){
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,e.getId());
            } else {
                uppercutHit(e);
            }
        }

        if (!isClient()) {
            if (!entities.isEmpty()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0, e.getEyeHeight() / 2, 0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ANUBIS_UPPERCUT_EVENT, SoundSource.PLAYERS, 1F, 0.9F + (float) (Math.random() * 0.2));
            } else {
                playMissSound(this.getSelf().level());
            }
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        switch (this.getActivePower()) {
            case PowerIndex.SNEAK_ATTACK -> uppercutHit(target);
            case PowersAnubis.CLEAVE -> cleaveHit(target);
            case PowersAnubis.SPIN -> spinHit(target);
            case PowersAnubis.LAUNCH -> launchHit(target);
        }
    }

    public void standPunch(){
        List<Entity> entities = getBasicSwordHitBox();
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;
                if (!entities.isEmpty()){
                    for (Entity entity : entities) {
                        if (!(entity instanceof StandEntity)) {
                            C2SPacketUtil.standPunchPacket(entity.getId(),this.getActivePowerPhase());
                        }
                    }
                }
            }
        } else {
            /*Caps how far out the punch goes*/
            if (!entities.isEmpty()){
                for (Entity entity : entities) {
                    if (!(entity instanceof StandEntity)) {
                        punchImpact(entity);
                    }
                }
            }
        }

        if (entities.isEmpty() && !isClient()) {
            playMissSound(this.getSelf().level());
        }

    }


    @Override
    public void punchImpact(Entity entity) {

        Vec3 pos = entity.getPosition(0F).add(0,entity.getEyeHeight()/2,0);
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
        float pitch = 0.9F+(float)(Math.random()*0.2F);
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_SWING_EVENT,SoundSource.PLAYERS,1F,pitch);

        this.setAttackTimeDuring(-10);
        float knockbackStrength = 0.2F;
        if (this.getSelf().isSprinting()) {knockbackStrength += 0.15F;}
        if (this.activePowerPhase == this.activePowerPhaseMax) {knockbackStrength += 0.3F;}

        float pow = this.activePowerPhase == activePowerPhaseMax ? getHeavyPunchStrength(entity) : getPunchStrength(entity);
        if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
            if (entity instanceof LivingEntity) {
                addEXP(1);
            }
            takeDeterminedKnockback(this.getSelf(), entity, knockbackStrength);
        } else if (entity instanceof LivingEntity LE && LE.isBlocking() && this.activePowerPhase == this.activePowerPhaseMax) {
            MainUtil.knockShieldPlusStand(LE, 50);
        }

    }

    public void updateQuickdraw() {
        if (this.getAttackTime() < quickdrawDelay) {
            scopeLevel = 1;
            this.getSelf().resetFallDistance();
            MainUtil.slowTarget(this.getSelf(),0.6F);
        } else if (this.getAttackTime() == quickdrawDelay) {
            if(!isClient()) {
                StartQuickdraw(8);
            }
        } else {
            UpdateQuickdraw();
            scopeLevel = 0;
        }

    }


    @Override
    public boolean canScope() {
        return this.getActivePower() == PowerIndex.RANGED_BARRAGE;
    }

    public void StartQuickdraw(float dist) {
        addEXP(2);
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
            List<Entity> entities = new ArrayList<>();
            int intervals = 5;
            for(int i=0;i<intervals;i++) {
                float d = 1F/intervals*i;
                Vec3 spos = pos.add(dpos.scale(d));
                List<Entity> targets = MainUtil.genHitbox(level,spos.x,spos.y,spos.z,2,1.5,2);
                targets = doAttackChecks(targets);
                for (Entity entity : targets) {
                    if (!entities.contains(entity)) {entities.add(entity);}
                }
            }
            this.targets = entities;

            this.setAttackTimeMax(55);
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
        ((StandUser)this.getSelf()).roundabout$setBubbleEncased((byte)(0));
        int duration = 15;
        if (!this.isClient()) {
            for (Entity entity : this.targets) {
                if (entity instanceof LivingEntity LE) {
                    setDazed(LE, (byte)3);
                }
                if (this.getAttackTimeDuring() > duration) {
                    if (StandRushDamageEntityAttack(entity, 3F, 0F, this.getSelf())) {
                        MainUtil.takeKnockbackWithY(entity, 0.9, 0, -1, 0);
                    }
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ANUBIS_BARRAGE_1_EVENT, SoundSource.PLAYERS, 1.5F, 1.0F);
                } else if (this.getSelf().tickCount % 2 == 1) {
                    if (StandRushDamageEntityAttack(entity, getBarrageHitStrength(entity)+0.5F, 0F, this.getSelf())) {
                        MainUtil.takeUnresistableKnockbackWithY(entity, 0.01, 0, -1, 0);
                        this.hitParticles(entity);
                        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ANUBIS_BARRAGE_1_HIT_EVENT, SoundSource.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.2));
                    }
                }
            }
            if (this.getAttackTimeDuring() > duration) {
                this.targets = new ArrayList<>();
                setPowerNone();
                if (this.getSelf() instanceof Player P) {
                    S2CPacketUtil.sendActivePowerPacket(P, PowerIndex.NONE);
                }
            }
        }

    }

    @Override
    public float getRushDistance() {
        return 10;
    }

    public void BarrageSlash() {
        ((StandUser)this.getSelf()).roundabout$setBubbleEncased((byte)(0));

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown);
        this.setAttackTime(0);
        this.setAttackTimeDuring(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        this.setPowerNone();
        this.setAnimation(PowerIndex.BARRAGE_2);
        float knockbackStrength = 1.25F + (this.getSelf().isSprinting() ? 0.1F : 0F);

        List<Entity> entities = defaultSwordHitbox(this.getSelf(),4, 45,0.05);
        if (!entities.isEmpty()) {
            this.getSelf().level().playSound(null,this.getSelf().blockPosition(),SoundEvents.PLAYER_ATTACK_KNOCKBACK,SoundSource.PLAYERS,1F,0.4F + (float)(Math.random()*0.2));
        } else {
            playMissSound(this.getSelf().level());
        }
        for (Entity e : entities ) {
            if (e != null) {
                if (e.distanceTo(this.getSelf()) < 1.5F) {
                    knockbackStrength += 0.15F;
                    this.setAttackTime(0);
                    this.setAttackTimeMax(this.getAttackTimeMax()+5);
                }

                float pow = getHeavyPunchStrength(e)*1.5F;
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {
                    if (e instanceof LivingEntity) {
                        addEXP(1);
                    }
                    takeDeterminedKnockback(this.getSelf(), e, knockbackStrength);
/*
                    /// knocks you back slightly if you hit it
                    Options o = Minecraft.getInstance().options;
                    if (!o.keyUp.isDown()) {
                        Vec3 look = this.getSelf().getLookAngle();
                        look = new Vec3(look.x,-0.1,look.z).normalize();
                        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),0.15F,look.x,look.y,look.z);
                    } */


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

    public void updateSwordThrow() {
        if (this.attackTimeDuring < 16) {
            if (!this.getSelf().level().isClientSide()) {
                if(this.attackTimeDuring%4==0) {
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                            this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                            1, 0.2, 0.2, 0.2, 0.05);
                }
            }
        } else if (this.attackTimeDuring == 16) {
            playSoundIfPossible(self.level(),null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1F, 0.4F + ((float) Math.random() * 0.2F));
            if (!isClient()) {
                ThrownAnubisEntity thrownAnubis = new ThrownAnubisEntity(this.getSelf(), this.getSelf().level());
                thrownAnubis.setOwner(this.getSelf());
                thrownAnubis.setPos(this.getSelf().getEyePosition(0));
                thrownAnubis.shootFromRotation(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), -0.5F, 2F, 0);
                thrownAnubis.setYRot(this.getSelf().getYRot());

                this.getSelf().level().addFreshEntity(thrownAnubis);
            }
        } else if (this.attackTimeDuring > 16) {
            this.getStandUserSelf().roundabout$setAnubisVanishTicks(0);
            super.setPowerNone();
            if (!this.self.level().isClientSide()){
                this.getStandUserSelf().roundabout$sealStand(400);
            }
       //     this.getStandUserSelf().roundabout$setActive(false);
        }
    }

    public void updateCleave() {
        if (this.attackTimeDuring > this.getCleaveWindup()) {
            super.setPowerNone();
            this.setAttackTime(0);
            this.setAttackTimeMax(15);
            this.setActivePowerPhase(this.getActivePowerPhaseMax());
            if (this.getSelf() instanceof Player){
                if (isPacketPlayer()){
                    this.attackTimeDuring = -20;

                    List<Entity> entities = getCleaveHitbox();
                    for (Entity e : entities) {
                        if (isPacketPlayer()){
                            tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,e.getId());
                        } else {
                            cleaveHit(e);
                        }
                    }
                }
                if (this.getCleaveHitbox().isEmpty()) {
                    this.playMissSound(this.getSelf().level());
                }
            }
        }
    }

    public void cleaveHit(Entity e) {

        if (StandDamageEntityAttack(e,this.getHeavyPunchStrength(e),0,this.getSelf())) {
            if (e instanceof LivingEntity) {
                addEXP(2);
            }
            Vec3 vec3 = e.getPosition(0).subtract(this.getSelf().getPosition(0)).multiply(1,0,1).normalize().reverse();
            MainUtil.takeKnockbackWithY(e,1.0F,vec3.x,-0.15,vec3.z);
        } else if (e instanceof LivingEntity LE && LE.isBlocking()) {
            MainUtil.knockShieldPlusStand(LE,100);
        }

        Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
        float pitch = 0.9F+(float)(Math.random()*0.2F);
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_UPPERCUT_EVENT,SoundSource.PLAYERS,1F,pitch);
    }

    public void updateSpin() {
        if (this.attackTimeDuring > this.getSpinWindup() && this.attackTimeDuring < this.getSpinWindup()+getSpinDuration()) {
            if (this.getSelf() instanceof Player) {
                if (this.getSelf().tickCount%2 == 0) {
                    if (isPacketPlayer()) {

                        List<Entity> entities = getSpinHitbox();
                        for (Entity e : entities) {
                            if (isPacketPlayer()) {
                                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, e.getId());
                            } else {
                                spinHit(e);
                            }
                        }
                    }
                    if (!isClient() && this.getSpinHitbox().isEmpty()) {
                        this.playMissSound(this.getSelf().level());
                    }
                    findDeflectables();
                }
            }
        } else if (this.attackTimeDuring > this.getSpinWindup()+getSpinDuration()) {
            this.setPowerNone();
            this.setAttackTime(0);
            this.setAttackTimeMax(40);
            this.setActivePowerPhase(this.getActivePowerPhaseMax());
        }
    }

    public void spinHit(Entity e) {

        if (e != null) {
            if (e.hurt(ModDamageTypes.of(this.getSelf().level(), ModDamageTypes.ANUBIS_SPIN, this.getSelf()), this.getHeavyPunchStrength(e))) {
                if (e instanceof LivingEntity LE) {
                    addEXP(1);
                    if (MainUtil.getMobBleed(LE)) {
                        MainUtil.makeBleed(LE, 0, 200, this.getSelf());
                    }
                }
                Vec3 vec3 = e.getPosition(0).subtract(this.getSelf().getPosition(0)).multiply(1, 0, 1).normalize().reverse();
                MainUtil.takeKnockbackWithY(e, 1.5F, vec3.x, -0.15, vec3.z);

                Vec3 pos = e.getPosition(0F).add(0, e.getEyeHeight() / 2, 0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);

                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ANUBIS_EXTRA_EVENT, SoundSource.PLAYERS, 1F, 0.9F + ((float) Math.random() * 0.2F));
            }
        }

    }

    @Override
    public float getReach() {return 3;}

    public void updateFlurry() {
        if (this.attackTimeDuring >= this.getFlurryWindup() && this.attackTimeDuring < this.getFlurryWindup()+getFlurryDuration()) {
            if (this.getSelf() instanceof Player) {
                this.setAttackTime((35 - 1) -
                        Math.round(((float) this.attackTimeDuring / this.getFlurryDuration())
                                * (35 - 1)));

                standBarrageHit();
            }
        } else if (this.attackTimeDuring > this.getFlurryWindup()+getFlurryDuration()) {
            super.setPowerNone();
            this.setAttackTime(0);
            this.setAttackTimeMax(40);
            this.setActivePowerPhase(this.getActivePowerPhaseMax());
        }
    }

    public void standBarrageHit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                List<Entity> listE = getTargetEntityList(this.self,-1);

                if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect){
                    for (Entity entity : listE) {
                        C2SPacketUtil.standBarrageHitPacket(entity.getId(), this.attackTimeDuring);
                    }
                }
            }
        }
        if (getTargetEntityList(this.getSelf(),-1).isEmpty()) {
            this.playBarrageMissNoise(this.getSelf().tickCount);
        }
        findDeflectables();
    }

    @Override
    public void barrageImpact(Entity entity, int hitNumber) {
        boolean sideHit = false;
        if (hitNumber > 1000) {
            if (!(ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect)) {
                return;
            }
            hitNumber -= 1000;
            sideHit = true;
        }
        boolean lastHit = (hitNumber >= this.getFlurryWindup()+this.getFlurryDuration()-1);
        if (entity != null) {
            hitParticles(entity);

            float pow;
            float knockbackStrength;
            Vec3 prevVelocity = entity.getDeltaMovement();
            if (lastHit) {
                pow = this.getFlurryFinisherStrength(entity);
                knockbackStrength = 1.4F;
            } else {
                pow = this.getFlurryStrength(entity);
                float mn = this.getFlurryDuration() - hitNumber;
                if (mn == 0) {
                    mn = 0.015F;
                } else {
                    mn = ((0.015F / (mn)));
                }
                knockbackStrength = 0.014F - mn;
            }

            if (sideHit) {
                pow /= 3;
                knockbackStrength /= 3;
            }

            if (StandRushDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (lastHit) {
                        if (entity instanceof Player PE) {
                            ((IPlayerEntity) PE).roundabout$setCameraHits(-1);
                        }
                        if (!sideHit) {
                            ((StandUser) LE).roundabout$setDestructionTrailTicks(80);
                            playBarrageEndNoise(0, entity);
                        }
                    } else {
                        if (entity instanceof Player PE) {
                            ((IPlayerEntity) PE).roundabout$setCameraHits(2);
                        }
                        if (!sideHit) {
                            this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_BARRAGE_1_HIT_EVENT,SoundSource.PLAYERS,1F,0.9F+((float)Math.random()*0.2F));
                            //    playKickBarrageNoise(hitNumber, entity);
                        }
                    }
                }
                kickBarrageImpact2(entity, lastHit, knockbackStrength);
            } else {
                if (lastHit) {

                    if (!sideHit) {
                        playBarrageBlockEndNoise(0, entity);
                    }
                } else {
                    playBarrageBlockNoise();
                    entity.setDeltaMovement(prevVelocity);
                }
            }
        } else {

            if (!sideHit) {
                playBarrageMissNoise(hitNumber);
            }
        }

        if (lastHit) {
            this.attackTimeDuring = -20;
        }
    }

    private void kickBarrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {

                takeKnockbackWithY(entity, knockbackStrength,
                        Mth.sin(this.getSelf().getYRot() * ((float) Math.PI / 180)),
                        Mth.sin(-15 * ((float) Math.PI / 180)),
                        -Mth.cos(this.getSelf().getYRot() * ((float) Math.PI / 180)));
            }
        }
    }

    private float getFlurryFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2F;
        } else {
            return 7;
        }
    }

    private float getFlurryStrength(Entity entity){
        float barrageLength = this.getFlurryDuration();
        float power;
        if (this.getReducedDamage(entity)){
            power = 7/barrageLength;
        } else {
            power = 15/barrageLength;
        }
        /*Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit){
                if (entity instanceof Player) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }

    public void updateLaunch() {
        if (this.getAttackTimeDuring() > 3) {
            super.setPowerNone();
            this.setAttackTime(0);
            this.setAttackTimeMax(15);
            this.setActivePowerPhase(this.getActivePowerPhaseMax());

            boolean hit = !this.getLaunchHitbox().isEmpty();
            boolean launched = false;
            if (this.getSelf() instanceof Player) {
                if (isPacketPlayer()) {
                    this.attackTimeDuring = -20;
                    List<Entity> entities = getLaunchHitbox();
                    for (Entity e : entities) {
                        if (isPacketPlayer()) {
                            tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, e.getId());
                        } else {
                            launchHit(e);
                        }
                    }
                }
                if (!hit) {
                    this.playMissSound(this.getSelf().level());
                }
            }

            for (int i=-1;i<2;i++) {
                Vec3 eyePos = this.getSelf().getEyePosition(0);
                Vec3 view = this.getSelf().getViewVector(0).multiply(1, 0, 1).normalize();
                view = view.yRot((float) (i * Math.toRadians(50)) );
                float range = i == 0 ? 1.5F : 2F;
                BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(eyePos, eyePos.add(view.scale(range)),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));

                BlockState state = this.getSelf().level().getBlockState(blockHit.getBlockPos());
                if (MainUtil.canBlockGrab(this.getSelf(),blockHit.getBlockPos())) {
                    launched = true;

                    if (!isClient()) {
                        if (this.getSelf().level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING)) {
                            this.getSelf().level().destroyBlock(blockHit.getBlockPos(), false, this.getSelf());
                        } else {
                            this.getSelf().level().playSound(null,this.getSelf().blockPosition(),state.getSoundType().getBreakSound(),SoundSource.PLAYERS,1F,1F);
                        }

                        ThrownObjectEntity toe = new ThrownObjectEntity(this.getSelf(), this.getSelf().level(), state.getBlock().asItem().getDefaultInstance(), false);
                        toe.setStyle(ThrownObjectEntity.STAND_DAMAGE);
                        toe.standDamageMob = 6;
                        toe.standDamagePlayer = 3;
                        toe.setSuperThrowTicks(5);


                        Vec3 viewVector = this.getSelf().getViewVector(0);
                        toe.setPos(blockHit.getBlockPos().getCenter().add(viewVector.scale(1.0F)));
                        MainUtil.takeUnresistableKnockbackWithY(toe, -1F + (Math.random() * 0.1F - 0.05F), viewVector.x, viewVector.y, viewVector.z);
                        this.getSelf().level().addFreshEntity(toe);
                    }
                }
            }
            if (hit) {
                this.setCooldown(PowerIndex.SKILL_1,launched ? 150 : 200);
            }

        }
    }

    public void launchHit(Entity e) {

        if (StandDamageEntityAttack(e,this.getPunchStrength(e),0.3F,this.getSelf())) {
            if (e instanceof LivingEntity) {
                addEXP(1);
                if (MainUtil.getMobBleed(e)) {
                    MainUtil.makeBleed(e,1,100,this.getSelf());
                }
            }
        } else if (e instanceof LivingEntity LE && LE.isBlocking()) {
            MainUtil.knockShieldPlusStand(LE,60);
        }

        Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
        float pitch = 0.9F+(float)(Math.random()*0.2F);
        this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_UPPERCUT_EVENT,SoundSource.PLAYERS,1F,pitch);
    }

    public void tryPogoAttack(boolean stab) {
        if (this.getSelf() instanceof Player P) {
            P.getAbilities().flying = false;
        }
        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown);
        this.setAttackTimeDuring(0);
        this.setAttackTime(0);

        byte index = stab ? PowersAnubis.STAB : PowersAnubis.POGO;
        this.setActivePower(index);
        setAnimation(index);
        if (!isClient()) {
            sendDoubleIntPacketIfNearby(PacketDataIndex.S2C_SNYC_ACTIVE_POWER, self.getId(), index, 100, false);
        }
    }

    public void updatePogoAttack() {
        this.getSelf().setNoGravity(this.attackTimeDuring < getPogoDelay());
        if (this.attackTimeDuring > -1) {

            if (this.getSelf().onGround() && this.getAttackTime() < getPogoDelay()) {
                this.attackTime += 5;
            }

            if (this.attackTimeDuring > this.attackTimeMax && this.isPacketPlayer()) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
            } else {
                final int windup = getPogoDelay();
                /*  Pogo is broken up into 4 stages: Hover, Launch, Attack, and Aftershock */
                if (attackTimeDuring == windup) {
                    if ((isClient() && isPacketPlayer())) {
                        this.iframeTicks = 7;
                        PogoLaunch();
                    } else if (!isClient()) {
                        this.iframeTicks = 7;
                        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_POGO_LAUNCH_EVENT,SoundSource.PLAYERS,1F,0.9F+(float)(Math.random()*0.2) );
                    }
                } else if (attackTimeDuring < windup) {
                    if ((isClient() && isPacketPlayer())) {
                        MainUtil.slowTarget(this.getSelf(), 0.8F);
                    } else {
                        this.getSelf().resetFallDistance();
                    }
                } else if (attackTimeDuring < windup + 6) {
                    if ((isClient() && isPacketPlayer())) {
                        pogoCheck();
                    }
                } else if (attackTimeDuring < windup + 9) { /// Slows the user after a duration
                    MainUtil.slowTarget(this.getSelf(), 0.7F);
                    this.getSelf().resetFallDistance();
                } else {
                    super.setPowerNone();
                }

            }
        }

    }

    public void PogoLaunch() {
        this.getSelf().resetFallDistance();
        float power = this.getActivePower() == PowersAnubis.STAB ? 1.5F : 1.2F;
        Vec3 lookAngle = this.getSelf().getLookAngle().reverse();
        this.getSelf().resetFallDistance();
        if (lookAngle.y < -0) {
            power *= 0.5F;
        }
        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),power,lookAngle.x,lookAngle.y,lookAngle.z);

    }

    public void pogoCheck() {


        Vec3 pos = this.getSelf().getEyePosition(0F).add(this.getSelf().getLookAngle().scale(1));
        List<Entity> targets = MainUtil.genHitbox(this.getSelf().level(),
                pos.x,pos.y,pos.z,
                0.75,0.75,0.75);
        targets.removeIf(entity -> entity.equals(this.getSelf()));
        targets = doAttackChecks(targets);
        Entity target = null;
        if (!targets.isEmpty()) {target = targets.get(0);}

        if (target != null) {
            tryIntPowerPacket(PowerIndex.BOUNCE,target.getId());

            super.setPowerNone();
        }
    }

    public void pogoAttack(int id) {
        ((StandUser)this.getSelf()).roundabout$setBubbleEncased((byte)(0));

        Entity target = this.getSelf().level().getEntity(id);

        if (target != null) {
            if (target instanceof LivingEntity LE) {
                if (StandDamageEntityAttack(target, this.getPunchStrength(target), -0.35F, this.getSelf())) {
                    addEXP(1);

                    if (this.getActivePower() == PowersAnubis.STAB) {
                        if (MainUtil.getMobBleed(LE)) {
                            MainUtil.makeBleed(LE,1,160,this.getSelf());
                        }
                        Vec3 dir = this.getSelf().getLookAngle().multiply(1,0,1).normalize();
                        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),1.0,dir.x,0F,dir.z);
                    }
                } else {
                    if (LE.isBlocking()) {
                        MainUtil.knockShieldPlusStand(LE, this.getActivePower() == PowersAnubis.STAB ? 80 : 40);
                    }
                }
            }
        }
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P, PowersAnubis.POGO, attackTime + 5);
        }



        this.getSelf().level().playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_POGO_HIT_EVENT,SoundSource.PLAYERS,1F,0.9F+(float)Math.random()*0.2F);
        super.setPowerNone();
    }

    public static final byte
            POGO = 56,
            CLEAVE = 57,
            SPIN = 58,
            FLURRY = 59,
            STAB = 60,
            WEAVE = 61,
            LAUNCH = 62;


    public void uppercutHit(Entity target) {
        super.setPowerNone();
        this.iframeTicks = 5;

        float pow = getUppercutStrength(target);
        if (StandDamageEntityAttack(target, pow, 0, this.self)) {
            if (target instanceof LivingEntity) {
                addEXP(2);
            }
            Vec3 look = this.getSelf().getLookAngle().normalize();
            look = new Vec3(look.x, 0, look.z).normalize().reverse().scale(this.getSelf().isSprinting() ? 1.3 : 1);
            MainUtil.takeUnresistableKnockbackWithY(target,Math.max(0.3,(this.chargedFinal/(float)maxSuperHitTime)*0.8F), look.x, -3, look.z);
        } else {
            if (target instanceof LivingEntity LE) {
                if (LE.isBlocking()) {
                    MainUtil.knockShieldPlusStand(target, this.chargedFinal > maxSuperHitTime - 1 ? 60 : 30);
                }
            }
        }
    }


    @Override
    public boolean setPowerBarrageCharge() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        this.setAnimation(PowerIndex.BARRAGE_CHARGE);
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

            /// pogo counter syncing
            case PowersAnubis.POGO -> {
                this.attackTime = data;
                super.setPowerNone();

                pogoCounter += 1;
                if (ConfigManager.getClientConfig().anubisSettings.anubisPogoCounter) {
                    ((Player) this.getSelf()).displayClientMessage(Component.literal("" + pogoCounter).withStyle(ChatFormatting.WHITE), true);
                }
            }
            /// canPogo syncing
            case PowerIndex.EXTRA -> {
                if (data == 1) {
                    enablePogo();
                } else {
                    disablePogo();
                }
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
            case PowerIndex.EXTRA_2 -> MemoryCancelClient();

        }
        super.updatePowerInt(activePower,data);
    }


    @Override
    public boolean interceptGuard(){
        return true;
    }

    public boolean canGuard(){
        return !this.isBarraging()
                && !this.isClashing()
                && this.getActivePower() != PowersAnubis.POGO
                && this.getActivePower() != PowerIndex.RANGED_BARRAGE
                && this.getActivePower() != PowerIndex.SNEAK_ATTACK
                && this.getActivePower() != PowersAnubis.SPIN
                && this.getActivePower() != PowersAnubis.FLURRY;
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (this.isBarrageCharging() || this.isBarrageAttacking()) {return false;}
        if (!this.isGuarding() && canGuard()) {
            tryPowerPackets(PowerIndex.GUARD);
            return true;
        }
        return false;
    }

    public boolean isRecording() {
        return this.playTime > 0 && !this.getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }


    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        if (this.getActivePower() == PowerIndex.POWER_4_SNEAK
                || (this.getActivePower() == PowersAnubis.FLURRY && this.attackTimeDuring < this.getFlurryWindup() )
                || ( (isPogoing()) && this.getAttackTimeDuring() < this.getPogoDelay()  )  ) {
            return true;
        }

        return super.canInterruptPower(sauce,interrupter);
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if(keyIsDown) {
            if (this.getActivePower() != PowersAnubis.POGO) {
                if (isHoldingSneak()) {
                    if (isEmpowered()) {
                        tryPowerPackets(PowersAnubis.SPIN);
                    } else if ((this.getAttackTime() >= this.getAttackTimeMax() ||
                            (this.getActivePowerPhase() != this.getActivePowerPhaseMax()))) {
                        tryPowerPackets(PowerIndex.BARRAGE_CHARGE_2);
                    }
                } else if (getSelf().getVehicle() == null) {
                    if (isEmpowered()) {
                        tryPowerPackets(PowersAnubis.FLURRY);
                    } else {
                        super.buttonInputBarrage(true, options);
                    }
                }
            }
        }
    }

    @Override
    public boolean canCombatModeUse(Item item) {
        if (this.getActivePower() != PowerIndex.NONE && this.getActivePower() != PowerIndex.ATTACK ) {
            return false;
        }
        return !item.equals(ModItems.ANUBIS_ITEM)
                && !item.equals(Items.SHIELD)
                && !item.equals(Items.FISHING_ROD);
    }

    List<Entity> targets = new ArrayList<>();
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
    public int getBarrageWindup() {return super.getBarrageWindup()+5;}

    public int getCleaveWindup() {return 20;}
    public int getSpinWindup() {return 7;}
    public int getSpinDuration() {return 20;}
    public int getFlurryWindup() {return 15;}
    public int getFlurryDuration() {return 15;}

    @Override
    public boolean clickRelease() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2);
    }
    @Override
    public boolean onClickRelease() {
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE) {
            if (this.getAttackTimeDuring() > this.getBarrageMinimum()) {
                tryPowerPackets(PowerIndex.BARRAGE);
                return true;
            }

        } else if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            if (this.getAttackTimeDuring() >= this.getKickBarrageWindup()) {
                tryPowerPackets(PowerIndex.BARRAGE_2);
                return true;
            }
        }
        return super.onClickRelease();
    }

    @Override
    public boolean setPowerNone() {
        setAnimation(PowerIndex.NONE);
        return super.setPowerNone();
    }

    public Component getPosName(byte posID){
        return switch (posID) {
            case (byte) 1 -> Component.translatable("idle.roundabout.anubis_2");
            case (byte) 2 -> Component.translatable("idle.roundabout.anubis_3");
            case (byte) 3 -> Component.translatable("idle.roundabout.anubis_4");
            default -> Component.translatable("idle.roundabout.anubis_1");
        };
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte)0);
        $$1.add((byte)1);
        $$1.add((byte)2);
        $$1.add((byte)3);
        return $$1;
    }

    public static final byte
            ANIME = 1, //
            EVIL = 2, //
            WOODEN = 3, //
            STONE = 4, //
            AQUAMARINE = 5,
            TIMEKEEPER = 6,
            DIAMOND = 7,
            ANCIENT = 8,
            GRASS = 9, //
            GRAY_WAGON = 10,
            CHORUS = 11,
            RAGING = 12,
            ALLURING = 13,
            KHOPESPH = 14,
            CLEAVER = 15,
            ILLUSORY = 16,
    // illusory_sheathed -> 17
    // cleaver_sheathed -> 18
    BLOODSTAINED = 19,
            BRILLIANCE = 20,
            CHAINBLADE = 21,
            CHEF = 22,
            SERPENT = 23,
            SOULBORN = 24,
            BONE_BLADE = 25;


    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(ANIME);
        $$1.add(EVIL);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(WOODEN);
                $$1.add(STONE);
                $$1.add(BONE_BLADE);
                //        $$1.add(CHEF);
            } if (Level > 2 || bypass){
                $$1.add(ALLURING);
                $$1.add(RAGING);
                $$1.add(SERPENT);
                //     $$1.add(SOULBORN);
            } if (Level > 3 || bypass){
                $$1.add(GRASS);
                $$1.add(AQUAMARINE);
                $$1.add(KHOPESPH);
                $$1.add(CHAINBLADE);
            } if (Level > 4 || bypass){
                $$1.add(GRAY_WAGON);
                $$1.add(TIMEKEEPER);
                $$1.add(BLOODSTAINED);
            } if (Level > 5 || bypass){
                $$1.add(DIAMOND);
                $$1.add(CHORUS);
                $$1.add(BRILLIANCE);
            } if (Level > 6 || bypass){
                $$1.add(ANCIENT);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(CLEAVER);
                $$1.add(ILLUSORY);
            }
        }
        return $$1;

    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case PowersAnubis.EVIL -> Component.translatable("skins.roundabout.anubis.evil");
            case PowersAnubis.ALLURING -> Component.translatable("skins.roundabout.anubis.alluring");
            case PowersAnubis.RAGING -> Component.translatable("skins.roundabout.anubis.raging");
            case PowersAnubis.WOODEN -> Component.translatable("skins.roundabout.anubis.wooden");
            case PowersAnubis.STONE -> Component.translatable("skins.roundabout.anubis.stone");
            case PowersAnubis.GRASS -> Component.translatable("skins.roundabout.anubis.grass");
            case PowersAnubis.KHOPESPH -> Component.translatable("skins.roundabout.anubis.khopesh");
            case PowersAnubis.AQUAMARINE -> Component.translatable("skins.roundabout.anubis.aquamarine");
            case PowersAnubis.GRAY_WAGON -> Component.translatable("skins.roundabout.anubis.gray_wagon");
            case PowersAnubis.TIMEKEEPER -> Component.translatable("skins.roundabout.anubis.timekeeper");
            case PowersAnubis.DIAMOND -> Component.translatable("skins.roundabout.anubis.diamond");
            case PowersAnubis.CHORUS -> Component.translatable("skins.roundabout.anubis.chorus");
            case PowersAnubis.ANCIENT -> Component.translatable("skins.roundabout.anubis.ancient");
            case PowersAnubis.CLEAVER -> Component.translatable("skins.roundabout.anubis.cleaver");
            case PowersAnubis.ILLUSORY -> Component.translatable("skins.roundabout.anubis.illusory");
            case PowersAnubis.BLOODSTAINED -> Component.translatable("skins.roundabout.anubis.bloodstained");
            case PowersAnubis.BRILLIANCE -> Component.translatable("skins.roundabout.anubis.brilliance");
            case PowersAnubis.CHAINBLADE -> Component.translatable("skins.roundabout.anubis.chainblade");
            case PowersAnubis.CHEF -> Component.translatable("skins.roundabout.anubis.chef");
            case PowersAnubis.SERPENT -> Component.translatable("skins.roundabout.anubis.serpent");
            case PowersAnubis.BONE_BLADE -> Component.translatable("skins.roundabout.anubis.boneblade");
            case PowersAnubis.SOULBORN -> Component.translatable("skins.roundabout.anubis.soulborn");

            default -> Component.translatable("skins.roundabout.anubis.anime");
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public void playMissSound(Level l) {
        l.playSound(null,this.getSelf().blockPosition(),ModSounds.ANUBIS_POGO_LAUNCH_EVENT,SoundSource.PLAYERS,1F,(float)(1.0F+Math.random()*0.2F));
    }

    @Override
    public void onStandSummon(boolean desummon) {
        if (!desummon) {

            if (this.getStandUserSelf().roundabout$getStandSkin() == (byte)0) {
                this.getStandUserSelf().roundabout$setStandSkin((byte) 1);
            }

            if (this.getSelf() instanceof Player PE && !isClient()) {

                Level lv = PE.level();
                ItemStack disc = this.getStandUserSelf().roundabout$getStandDisc();
                CompoundTag tag = disc.getTagElement("Memory");
                if (tag != null) {
                    if (tag.contains("AnubisSkin")) {

                        this.getStandUserSelf().roundabout$setStandSkin(tag.getByte("AnubisSkin"));
                        lv.playSound(null, PE.getX(), PE.getY(),
                                PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                        ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                PE.getY() + PE.getEyeHeight(), PE.getZ(),
                                10, 0.5, 0.5, 0.5, 0.2);
                        PE.displayClientMessage(
                                Component.translatable("unlock_skin.roundabout.anubis.traitor"), true);

                        tag.remove("AnubisSkin");
                    }
                }
            }

        } else {
            if (isEmpowered()) {
                this.empower = false;
            }
        }
        super.onStandSummon(desummon);
    }

    @Override
    public void onStandSwitchInto() {
        super.onStandSwitchInto();
        if (!(this.getSelf() instanceof Player && (((Player)this.getSelf()).isCreative()))) {
            if (this.getSelf() instanceof Player) {
                if (!isClient()) {
                    S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1, 100);
                    S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_4_SNEAK, 320);
                }
            }
            this.setCooldown(PowerIndex.SKILL_1, 100);
            this.setCooldown(PowerIndex.SKILL_4_SNEAK, 320);
        }
    }

    public List<Entity> getBasicSwordHitBox() {
        List<Entity> entities = defaultSwordHitbox(this.getSelf(),3, 45,0.01);
        return entities;
    }
    public List<Entity> getUppercutHitbox() {
        return defaultSwordHitbox(this.getSelf(),2.5F, 45,0.015);
    }
    public List<Entity> getCleaveHitbox() {
        return defaultSwordHitbox(this.getSelf(),4.2F, 60,0.025);
    }
    public List<Entity> getSpinHitbox() {
        return this.getSelf().level().getEntities(this.getSelf(),this.getSelf().getBoundingBox().inflate(5))
                .stream()
                .filter(entity -> !entity.is(this.getSelf()) && !(entity instanceof StandEntity SE && SE.getUser().is(this.getSelf()) ))
                .filter(entity -> entity.distanceTo(this.getSelf()) < 2.5F).toList();
    }
    public List<Entity> getLaunchHitbox() {
        return defaultSwordHitbox(this.getSelf(),1.8, 60,0.025);
    }

    public List<Entity> defaultSwordHitbox(Entity e,double radius, double angle, double factor) {
        final Vec3 Eyepos = new Vec3(e.getEyePosition(0F).x,0,e.getEyePosition().z);//.add(e.getLookAngle());
        Vec3 pos = e.getEyePosition().add(0,e.getLookAngle().y,0);

        List<Entity> list = MainUtil.genHitbox(this.getSelf().level(),pos.x,pos.y,pos.z,8,1.5,8);
        list = doAttackChecks(list);
        list.remove(e);

        final Vec3 forward = new Vec3(e.getLookAngle().x,0,e.getLookAngle().z).normalize();
        final Vec3 backward = new Vec3(e.getLookAngle().x,0,e.getLookAngle().z).normalize().reverse();


        list.removeIf(entity -> {
            Vec3 ePos = entity.getPosition(0F);
            ePos = new Vec3(ePos.x,0,ePos.z);

            Vec3 vector = ePos.subtract(Eyepos);
            double dist = vector.length();
            vector = vector.normalize();

            double b = Math.toRadians(e.getYRot());
            final Vec3 Lookvec = new Vec3(Math.cos(b),0,Math.sin(b) );

            double dungle = Math.abs(Math.toDegrees(vector.dot(Lookvec)));


            final Vec3 fVec = Eyepos.add(forward);
            final Vec3 bVec = Eyepos.add(backward);


            if (ePos.distanceTo(fVec) > ePos.distanceTo(bVec)) {return true;}
            AABB box = entity.getBoundingBox();
            if (dist > radius-(dungle*factor*0.35)+(box.getXsize()+box.getZsize())/4) {return true;}
            return (dungle > angle );
        });




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
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.ANUBIS_SHIELDBREAK_EVENT;
        } else if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_ANUBIS_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.4F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5F));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(2F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }

    public float getUppercutStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
        } else {
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+1;
        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                anubisSettings.anubisAttackMultOnPlayers*0.01));
    }
    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                anubisSettings.anubisAttackMultOnMobs*0.01));
    }

    @Override
    public int getMaxGuardPoints() {
        return ConfigManager.getConfig().anubisSettings.anubisGuardPoints;
    }

    private boolean shouldShowBarrage() {
        return this.isBarrageCharging()
                || this.getActivePower() == PowersAnubis.CLEAVE
                || this.getActivePower() == PowersAnubis.SPIN
                || this.getActivePower() == PowersAnubis.FLURRY;
    }

    public boolean visualMouse = false;
    public List<Pair<List<Byte>,Integer>> visualValues = new ArrayList<>();
    AnubisMemory lastMemory = null;
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;


        float attackTimeDuring = this.getAttackTimeDuring();
        if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            int ClashTime = Math.min(15, Math.round((attackTimeDuring / maxSuperHitTime) * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && this.isClashing()) {
            int ClashTime = 15 - Math.round((attackTimeDuring / 60) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && this.isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / this.getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && this.shouldShowBarrage()) {
            int windup = switch(this.getActivePower()) {
                case PowerIndex.BARRAGE_CHARGE_2 -> this.getKickBarrageWindup();
                case PowersAnubis.SPIN -> this.getSpinWindup();
                case PowersAnubis.CLEAVE -> this.getCleaveWindup();
                case PowersAnubis.FLURRY -> getFlurryWindup();
                default -> this.getBarrageWindup();
            };

            int ClashTime = Math.round(( Math.min(attackTimeDuring,windup) / windup) * 15);
            int duration = switch(this.getActivePower()) {
                case PowersAnubis.SPIN -> this.getSpinDuration();
                case PowersAnubis.FLURRY -> this.getFlurryDuration();
                default -> 0;
            };

            if (duration != 0 && this.getAttackTimeDuring() > windup) {
                ClashTime = Math.round(( (duration-(attackTimeDuring-windup)) / duration) * 15);
            }

            int height = 30;
            if (this.isBarrageCharging()) {
                if (this.getAttackTimeDuring() > ( (this.getActivePower() == PowerIndex.BARRAGE_CHARGE) ? this.getBarrageWindup() : this.getKickBarrageWindup() ) ) {
                    height -= 6;
                }
            }
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, height, ClashTime, 6);

        } else {
            int barTexture = 0;

            List<Entity> TE = getBasicSwordHitBox();
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


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (standOn)  {
                if (!TE.isEmpty()) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
        if (canPogo() && PowerTypes.isUsingStand(this.getSelf())) {
            context.blit(StandIcons.JOJO_ICONS,k,j,193,60,15,7);
        }


        ///  memory rendering
        if (!visualValues.isEmpty()) {
            for (int i = 0; i < visualValues.size(); i++) {
                Pair<List<Byte>, Integer> pair = visualValues.get(i);
                renderMoment(context, pair.getA().toArray(new Byte[0]), (visualValues.size() - i) * 8, pair.getB(),visualMouse);
            }
        }

    }
    public void renderMoment(GuiGraphics context, Byte[] moments,int offset, int time, boolean bl) {
        List<Byte> Moments = Arrays.stream(moments).toList();
        int Offset = offset + 4 + ( (PowerTypes.hasStandActive(self) || !FateTypes.isHuman(this.getSelf())) ? 24 : 0);
        int xoff = 1;

        if (bl) {
            context.blit(StandIcons.ANUBIS_MEMORY,xoff,Offset,0,72,3,8);
            xoff += 3;
        }

        Vec3 dir = new Vec3(0,0,0);
        if (Moments.contains((byte)1)) {dir = dir.add(0,1,0);}
        if (Moments.contains((byte)2)) {dir = dir.add(0,-1,0);}
        if (Moments.contains((byte)3)) {dir = dir.add(-1,0,0);}
        if (Moments.contains((byte)4)) {dir = dir.add(1,0,0);}
        if (!dir.equals(Vec3.ZERO)) {
            List<Vec3> icons = new ArrayList<>();
            icons.add(new Vec3(0,1,0));
            icons.add(new Vec3(0,-1,0));
            icons.add(new Vec3(-1,0,0));
            icons.add(new Vec3(1,0,0));
            icons.add(new Vec3(-1,1,0));
            icons.add(new Vec3(1,1,0));
            icons.add(new Vec3(1,-1,0));
            icons.add(new Vec3(-1,-1,0));

            context.blit(StandIcons.ANUBIS_MEMORY,xoff,Offset,7*icons.indexOf(dir),80,7,7);
            xoff += 8;
        }


        for (Byte moment : moments) {
            if (moment > 4) {
                int xIcon = getIcon(moment);
                context.blit(StandIcons.ANUBIS_MEMORY, xoff, Offset, xIcon, 87, 7, 7);
                xoff += 8;
            }
        }
        if (time != -1) {
            context.drawString(Minecraft.getInstance().font, "" + time, xoff, Offset, 16777215);
        }
    }

    private static int getIcon(Byte moment) {
        int xIcon = 7 * switch (moment) {
            case AnubisMoment.JUMP -> 0;
            case AnubisMoment.SPRINT -> 1;
            case AnubisMoment.CROUCH -> 2;
            case AnubisMoment.CROUCH_TOGGLE -> 3;
            case AnubisMoment.SUMMON -> 4;
            case AnubisMoment.ABILITY_1 -> 5;
            case AnubisMoment.ABILITY_2 -> 6;
            case AnubisMoment.DASH -> 7;
            case AnubisMoment.ABILITY_3 -> 8;
            case AnubisMoment.ATTACK -> 9;
            case AnubisMoment.INTERACT -> 10;
            case AnubisMoment.SWAP_OFFHAND -> 11;
            case AnubisMoment.USE_OFFHAND -> 12;
            default -> 90;
        };
        if (moment > 20 && moment < 30) {
            xIcon = 1 + 7 * (12 + (moment - 20));
        }
        return xIcon;
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
        setPlayTime(this.getMaxPlayTime());

        visualValues = new ArrayList<>();
        visualMouse = this.getUsedMemory().canMouse();

        lastMemory = new AnubisMemory(this.memories.get(slot));
        this.memories.get(slot).moments = new ArrayList<>();
        this.memories.get(slot).rots = new ArrayList<>();

        this.memories.get(slot).rots.add(AnubisMoment.convertVec(new Vec3(0,this.getSelf().getXRot(),this.getSelf().getYRot())));
    }
    public void playbackMemory(byte slot) {
        if (memories.isEmpty()) {return;}
        if (slot == (byte) -1 || slot == 8) {return;}
        AnubisMemory memory = this.memories.get(slot);

        int lastTime = 0;
        if ( !memory.moments.isEmpty() ){
            lastTime = memory.moments.get(memory.moments.size()-1).time;
        }
        if (memory.canMouse()) {
            if (!memory.rots.isEmpty()) {
                lastTime = Math.max(lastTime, (int)memory.rots.get(memory.rots.size()-1).x );
            }
        }

        if (lastTime > this.getMaxPlayTime()) {
            if (this.getSelf() instanceof Player P) {
                P.displayClientMessage(Component.translatable("roundabout.anubis.memory_fail").withStyle(ChatFormatting.RED),true);
                return;
            }
        }

        if (memory.canPlayback()) {
            int time = -1;
            if (memory.canPlayback()) {
                time = this.getMaxPlayTime() - memory.getFirstTime();
            }

            playSlot = slot;
            setPlayTime(time);
            lastPartialTick = -1;
            lastTick = -1;
            //Roundabout.LOGGER.info("{}/{}",moments.get(moments.size()-1).time,moments.get(0).time);
            this.getStandUserSelf().roundabout$setUniqueStandModeToggle(true);


        }


    }

    public String convertToConfig(int i) {
        AnubisMemory AM = this.memories.get(i);
        String ret = "";
        if (AM != null) {

            Item item = AM.item;
            if (item != null) {
                ret = ret + BuiltInRegistries.ITEM.getKey(item).toString();

                ret = ret + "/" + AM.memory_type;

                ret = ret + "/{";
                List<AnubisMoment> moments = AM.moments;
                for(AnubisMoment am : moments) {
                    ret = ret + am.toConfig();
                }

                ret = ret + "}";


                ret = ret + "/[";
                for (Vec3 v3 : AM.rots) {
                    String v3String = v3.x + "_" + AnubisMoment.convertToShorter(v3.y) + "_" + AnubisMoment.convertToShorter(v3.z) + "_";
                    ret = ret + v3String;
                }

                ret = ret + "]";
            } else {/*Roundabout.LOGGER.warn("Null Memory Item: " + i);*/}
        } else {/*Roundabout.LOGGER.warn("Null Memory: " + i);*/}
        return ret;
    }


    public static AnubisMemory convertToMemory(int slot) {
        String cf = ConfigManager.getClientConfig().anubisMemories.getFromMemory(slot);
        if (cf.equals("nothing yet :P")) {cf = "";ConfigManager.getClientConfig().anubisMemories.saveToMemory(slot,"");}

        Item item = ModItems.ANUBIS_ITEM;
        byte Mode = (byte)-1;
        List<AnubisMoment> moments = new ArrayList<>();
        List<Vec3> rots = new ArrayList<>();


        if (!cf.isEmpty()) {
            Scanner s = new Scanner(cf);
            s.useDelimiter("/");
            if (s.hasNext()) {
                String itemName = s.next();
                try {
                    item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemName));
                    if (item.equals(Items.AIR)) {
                        item = ModItems.ANUBIS_ITEM;
                    }
                } catch (ResourceLocationException e) {}


                if (s.hasNext()) {
                    try {
                        Mode = Byte.parseByte(s.next());
                    } catch (NumberFormatException e) {}

                    if (s.hasNext()) {
                        if (cf.contains("{") && cf.contains("}")) {

                            String mems = cf.substring(cf.indexOf("{") + 1, cf.indexOf("}"));
                            Scanner memScanner = new Scanner(mems);
                            memScanner.useDelimiter("_");
                            List<Byte> memBytes = new ArrayList<>();
                            while (memScanner.hasNext()) {
                                String o = memScanner.next();
                                try {
                                    if(o.equals("}")) {break;}
                                    memBytes.add((byte)Integer.parseInt(o));
                                } catch (NumberFormatException e) {
                                    Roundabout.LOGGER.warn("Invalid Memory Token??  " + o);
                                }
                            }

                            if (memBytes.size() % 3 == 0) {
                                for (int i = 0; i < memBytes.size(); i += 3) {
                                    moments.add(new AnubisMoment(memBytes.get(i), memBytes.get(i + 1), memBytes.get(i + 2) == 1));
                                }
                            } else {
                                Roundabout.LOGGER.warn("Invalid Memory Length");
                            }

                            if (cf.contains("[") && cf.contains("]")) {
                                List<Float> memrots = new ArrayList<>();
                                String rotString = cf.substring(cf.indexOf("[")+1,cf.indexOf("]"));
                                Scanner rotScanner = new Scanner(rotString);
                                rotScanner.useDelimiter("_");
                                while(rotScanner.hasNext()) {
                                    String o = rotScanner.next();
                                    if (o.equals("]")) {break;}
                                    try {
                                        memrots.add(Float.parseFloat(o));
                                    } catch (NumberFormatException e) {
                                        Roundabout.LOGGER.warn("Invalid Rotation Token??  " + o);
                                    }
                                }

                                if (memrots.size() % 3 == 0) {
                                    for (int i = 0; i < memrots.size(); i += 3) {
                                        rots.add(new Vec3(memrots.get(i),memrots.get(i+1),memrots.get(i+2)));
                                    }
                                }

                            }

                        } else {
                            // Roundabout.LOGGER.warn("Invalid Memory");
                        }

                    }
                }
            }
        } else {/*Roundabout.LOGGER.warn("Received Empty String from anubisMemories");*/}
        if (Mode != (byte)-1) {
            AnubisMemory AM = new AnubisMemory(item, new ArrayList<>());
            AM.memory_type = Mode;
            AM.moments = moments;
            AM.rots = rots;
            return AM;
        } else {
            //       Roundabout.LOGGER.warn("Invalid Memory Item");
        }
        return null;
    }

    public static void generateMemories(PowersAnubis PA) {
        for (int i=0;i<8;i++) {

            List<AnubisMoment> moment = new ArrayList<>();
            AnubisMemory AM =new AnubisMemory(ModItems.ANUBIS_ITEM, moment);

            AnubisMemory ret = convertToMemory(i+1);
            if (ret != null) {
                AM = ret;
            }
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

        PA.playKeys.add(KeyInputRegistry.switchRow);PA.playBytes.add(AnubisMoment.CROUCH_TOGGLE);
        PA.playKeys.add(KeyInputRegistry.abilityThreeKey);PA.playBytes.add(AnubisMoment.DASH);
        PA.playKeys.add(KeyInputRegistry.summonKey);PA.playBytes.add(AnubisMoment.SUMMON);
        PA.playKeys.add(KeyInputRegistry.abilityOneKey);PA.playBytes.add(AnubisMoment.ABILITY_1);
        PA.playKeys.add(KeyInputRegistry.abilityTwoKey);PA.playBytes.add(AnubisMoment.ABILITY_2);
        PA.playKeys.add(KeyInputRegistry.abilityFourKey);PA.playBytes.add(AnubisMoment.ABILITY_3);

        PA.playKeys.add(o.keyAttack);PA.playBytes.add(AnubisMoment.ATTACK);
        PA.playKeys.add(o.keyUse);PA.playBytes.add(AnubisMoment.INTERACT);

        PA.playKeys.add(o.keySwapOffhand);PA.playBytes.add(AnubisMoment.SWAP_OFFHAND);
        PA.playKeys.add(KeyInputRegistry.guardKey);PA.playBytes.add(AnubisMoment.USE_OFFHAND);
        for(int i=0;i<AnubisMoment.HOTBAR.length;i++) {
            PA.playKeys.add(o.keyHotbarSlots[i]);PA.playBytes.add(AnubisMoment.HOTBAR[i]);
        }
    }
    public float lastPartialTick = 0;
    public float lastTick = 0;
    public AnubisMemory getUsedMemory() {
        if (this.playSlot != -1) {
            if (!this.memories.isEmpty()) {
                return this.memories.get(this.playSlot);
            }
        }
        return null;
    }
    public int getLastMoment(int slot,byte type,int time) {
        if (slot != -1) {
            AnubisMemory mem = this.memories.get(slot);
            if (mem != null) {

                List<AnubisMoment> moments = mem.moments;
                if (moments.isEmpty()) {
                    return -1;
                }

                for (int i = moments.size() - 1; i >= 0; i--) {
                    AnubisMoment moment = moments.get(i);
                    if (moment.type == type) {
                        if (time >= moment.time) {
                            return i;
                        }
                    }
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
        visualMouse = this.memories.get(slot).canMouse();

        int maxTime = Math.min(ConfigManager.getConfig().anubisSettings.anubisMaxMemory,moments.get(moments.size()-1).time);

        for(int time = 0; time<maxTime; time++ ) {
            List<Byte> value = new ArrayList<>();
            for (Byte playByte : this.playBytes) {
                if (isPressed(slot, playByte, time)) {
                    value.add(playByte);
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
            visualDuration = 40;
        }
    }


    public float getRange(){
        if (self instanceof AnubisGuardian)
            return 2F;
        return 3.3F;
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {

        if (this.getSelf() instanceof AnubisGuardian AG && AG.hasTotem() ) {return;}

        if (attackTarget != null && !this.getStandUserSelf().roundabout$isDazed()) {


            if (this.getSelf().distanceTo(attackTarget) < getRange() && !PowersAnubis.shouldDash((Mob)this.getSelf()) ) {
                if (canAttack() && this.attackTime > 10) {
                    StandUser SU = (StandUser) this.getSelf();
                    SU.roundabout$tryPower(PowerIndex.ATTACK, true);
                    this.getSelf().swing(InteractionHand.MAIN_HAND);
                    this.setAttackTimeDuring(0);
                }
            } else if (PowersAnubis.shouldDash((Mob)this.getSelf())) {
                tickDashing(attackTarget);
            }
        }
        super.tickMobAI(attackTarget);
    }

    private void tickDashing(LivingEntity attackTarget) {
        if (!onCooldown(PowerIndex.GLOBAL_DASH)) {
            if (attackTarget != null && this.getSelf().onGround()) {
                float dist = attackTarget.distanceTo(this.getSelf());
                if (dist < 15) {
                    Vec3 dir = attackTarget.getPosition(0).subtract(this.getSelf().getPosition(0));
                    dir = new Vec3(dir.x, 0, dir.z).normalize().reverse();

                    if (dir.length() == 1) {
                        double yOff = attackTarget.getY() - this.getSelf().getY();
                        if (yOff > 2.4) {
                            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1, dir.x, -2F, dir.z);
                        } else if (dist > 3.5) {
                            float strength = 1;
                            if (dist > 7) {
                                strength = 1.4F;
                            }
                            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), strength, dir.x, -0.33F, dir.z);
                        }

                        Vec3 cvec = new Vec3(0, 0.1, 0);
                        Vec3 rDir = dir.scale(0.2F);

                        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CLOUD,
                                this.getSelf().getX() + cvec.x, this.getSelf().getY() + cvec.y, this.getSelf().getZ() + cvec.z,
                                0,
                                rDir.x,
                                rDir.y,
                                rDir.z,
                                0.8);
                        playSoundIfPossible(self.level(),null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.98 + (Math.random() * 0.04)));

                        this.getSelf().setYHeadRot(MainUtil.getLookAtEntityYaw(this.getSelf(), attackTarget));
                        this.getSelf().setYRot(MainUtil.getLookAtEntityYaw(this.getSelf(), attackTarget));
                    }
                    this.setCooldown(PowerIndex.GLOBAL_DASH, 40);
                }
            }
        }
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().anubisSettings.enableAnubis;
    }


    public void unlockSkin(byte b){
        Level lv = this.getSelf().level();
        if ((this.getSelf()) instanceof Player PE){
            StandUser user = ((StandUser)PE);
            ItemStack stack = user.roundabout$getStandDisc();
            if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_ANUBIS)){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                if (!ipe.roundabout$getUnlockedBonusSkin()){
                    if (!lv.isClientSide()) {
                        ipe.roundabout$setUnlockedBonusSkin(true);
                        lv.playSound(null, PE.getX(), PE.getY(),
                                PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                        ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                10, 0.5, 0.5, 0.5, 0.2);
                        user.roundabout$setStandSkin(b);
                        user.roundabout$summonStand(this.getSelf().level(), true, false);
                        ((ServerPlayer) ipe).displayClientMessage(
                                Component.translatable("unlock_skin.roundabout.anubis.traitor"), true);
                    }
                }
            }
        }
    }

    @Override
    public Vector3f getLeapColor() {
        return new Vector3f(171F/255F,141F/255F,230F/255F);
    }

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1){
            amt = 25;
        } else {
            amt = (100+((currentLevel-1)*75));
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }

    public void tryPowerPackets(byte move) {
        tryPower(move);
        tryPowerPacket(move);
    }
}

