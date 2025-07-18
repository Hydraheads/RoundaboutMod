package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.block.InvisiBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersAchtungBaby extends NewDashPreset {
    public PowersAchtungBaby(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersAchtungBaby(entity);
    }


    public boolean invisibleVisionOn(){
        return !getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        if (invisibleVisionOn())
            setSkillIcon(context, x, y, 1, StandIcons.BABY_VISION_ON, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.BABY_VISION_OFF, PowerIndex.SKILL_1);


        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.SELF_INVIS, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.BURST_INVIS, PowerIndex.SKILL_2);

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        super.renderIcons(context, x, y);
    }


    @Override
    public boolean rendersPlayer(){
        return true;
    }
    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }


    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                switchModeClient();
            }
            case SKILL_2_NORMAL-> {
                invisiburstClient();
            }
            case SKILL_2_CROUCH-> {
                invisiburstSimpleClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    public void invisiburstClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }
    public void invisiburstSimpleClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
        }
    }

    public void switchModeClient(){
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
    }



    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return invisibleVisionSwitch();
            }
            case PowerIndex.POWER_2 -> {
                return invisibleBurst();
            }
            case PowerIndex.POWER_2_SNEAK -> {
                return invisibleBurstSimple();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    @SuppressWarnings("deprecation")
    public boolean invisibleBurst(){
        setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCooldown);
        if (this.self.level() instanceof ServerLevel sl){
            burstParticles(sl);
            float range = 5;
            burstEntities(range);
            int radius = 4;

            BlockPos baseCenter = this.self.getOnPos();

            if (MainUtil.getIsGamemodeApproriateForGrief(this.self)) {
                for (int x = -radius; x <= radius; x++) {
                    for (int y = 0; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + y * y + z * z <= radius * radius) {
                                BlockPos targetPos = baseCenter.offset(x, y, z);
                                BlockState oldState = this.self.level().getBlockState(targetPos);

                                // Example: Replace dirt with glowstone
                                if (!oldState.isAir() && oldState.getBlock().isCollisionShapeFullBlock(oldState, this.self.level(), targetPos)
                                        && this.self.level().getBlockEntity(targetPos) == null && !oldState.is(ModPacketHandler.PLATFORM_ACCESS.getOreTag())) {
                                    BlockState replaced = sl.getBlockState(targetPos);
                                    BlockEntity replacedEntity = sl.getBlockEntity(targetPos);
                                    CompoundTag replacedTag = replacedEntity != null ? replacedEntity.saveWithFullMetadata() : null;

                                    sl.setBlock(targetPos, ModBlocks.INVISIBLOCK.defaultBlockState(), 3);

                                    BlockEntity maybeEntity = sl.getBlockEntity(targetPos);
                                    if (maybeEntity instanceof InvisiBlockEntity entity) {
                                        entity.setOriginal(replaced, replacedTag, this.self.level());
                                        entity.ticksUntilRestore = ((IEntityAndData)this.self).roundabout$getTrueInvisibility();
                                    }
                                    this.self.level().setBlock(targetPos, ModBlocks.INVISIBLOCK.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void burstParticles(ServerLevel sl){

        Vec3 pos = new Vec3(this.self.getX(),
                this.self.getY() +(this.self.getBbHeight()*0.5),
                this.self.getZ());
        sl.sendParticles(ModParticles.BABY_CRACKLE,
                pos.x(),
                pos.y(),
                pos.z(),
                0,0, 0, 0, 0);
        playStandUserOnlySoundsIfNearby(BURST, 27, false,false);
        spawnExplosionParticles(this.self.level(), pos, 100, 0.5);
    }

    public void burstEntities(float range){
        List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.self.level(), this.getSelf().blockPosition(), range+1);
        if (!mobsInRange.isEmpty()) {
            for (Entity ent : mobsInRange) {
                if (ent.distanceTo(this.self) <= range){
                    IEntityAndData entityAndData = ((IEntityAndData) ent);
                    entityAndData.roundabout$setTrueInvisibility(ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstDuration);
                }
            }
        }
    }
    public boolean invisibleBurstSimple(){
        if (this.self.level() instanceof ServerLevel sl){
            burstParticles(sl);
            float range = 3;
            burstEntities(range);

        }

        return true;
    }

    public static void spawnExplosionParticles(Level level, Vec3 center, int particleCount, double speed) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        RandomSource random = level.random;

        for (int i = 0; i < particleCount; i++) {
            // Random direction on the unit sphere
            double x = random.nextFloat()-0.5F;
            double y = random.nextFloat()-0.5F;
            double z = random.nextFloat()-0.5F;

            serverLevel.sendParticles(
                    ModParticles.MAGIC_DUST, // Use another ParticleOptions if desired
                    center.x, center.y, center.z,
                    0, // count (we send 1 at a time in a loop)
                    x, y, z, speed
            );
        }
    }

    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        /**
        if (ent.isInvisibleTo())
        if (invisibleVisionOn() && MainUtil.getEntityIsTrulyInvisible(ent) && MainUtil.canActuallyHitInvolved(this.self,ent)){
            return true;
        }
         **/
        return false;
    }
    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        return 14806268;
    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }


    /** if = -1, not melt dodging */



    @Override
    public void tickPower() {
        if (this.self.level().isClientSide()){
        }
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



    @Override
    public int getDisplayPowerInventoryScale(){
        return 60;
    }
    @Override
    public int getDisplayPowerInventoryYOffset(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == JusticeEntity.DARK_MIRAGE){
            return super.getDisplayPowerInventoryYOffset();
        }
        return 7;
    }
    @Override public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.achtung_baby.base");
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
                return ModSounds.SUMMON_ACHTUNG_EVENT;
            }
            case BURST -> {
                return ModSounds.ACHTUNG_BURST_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }


    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP,slot);
    }

    public static final byte
            BURST = 61;
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        return $$1;
    }

    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }


    public boolean invisibleVisionSwitch(){
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(invisibleVisionOn());
            if (!isClient() && this.self instanceof ServerPlayer PE) {
                if (invisibleVisionOn()) {
                    PE.displayClientMessage(Component.translatable("text.roundabout.achtung.vision_on").withStyle(ChatFormatting.AQUA), true);
                } else {
                    PE.displayClientMessage(Component.translatable("text.roundabout.achtung.vision_off").withStyle(ChatFormatting.AQUA), true);
                }
            }
        }
        return true;
    }

}