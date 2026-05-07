package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.block.InvisiBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.List;

public class PowersAchtungBaby extends NewDashPreset {
    public PowersAchtungBaby(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersAchtungBaby(entity);
    }
    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().achtungSettings.enableAchtungBaby;
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

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.SELF_INVIS, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.BURST_INVIS, PowerIndex.SKILL_2);

        if (isHoldingSneak() && ClientNetworking.getAppropriateConfig().achtungSettings.hidesPlacedBlocks)
            setSkillIcon(context, x, y, 3, StandIcons.INVIS_BLOCK, PowerIndex.SKILL_3);
        else
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
            case SKILL_3_NORMAL -> {
                dash();
            }
            case SKILL_3_CROUCH -> {
                placeOrDashClient();
            }
        }
    }

    public void placeOrDashClient(){
        if (ClientNetworking.getAppropriateConfig().achtungSettings.hidesPlacedBlocks) {
            placeClient();
        } else {
            dash();
        }
    }
    public void placeClient(){
        ItemStack stack = this.getSelf().getMainHandItem();
        if (!onCooldown(PowerIndex.SKILL_3)) {
            this.setCooldown(PowerIndex.SKILL_3, 2);
            if (!stack.isEmpty()) {
                if (((IEntityAndData) this.self).roundabout$getTrueInvisibility() > -1) {
                    ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.POWER_3, true,
                            ((Player) this.getSelf()).getInventory().selected);
                    tryIntPowerPacket(PowerIndex.POWER_3,
                            ((Player) this.getSelf()).getInventory().selected);
                }
            }
        }
    }
    public int grabInventorySlot=1;
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_3){
            this.grabInventorySlot = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    public int burstTicks = -1;
    public boolean inBurstState(){
        return burstTicks > 1;
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
            case PowerIndex.POWER_3 -> {
                return inventoryGrab();
            }
        }
        return super.setPowerOther(move,lastMove);
    }


    public boolean inventoryGrab() {
        if (!this.getSelf().level().isClientSide()) {
            if (this.getSelf() instanceof Player) {
                if (((IEntityAndData) this.self).roundabout$getTrueInvisibility() > -1) {
                    ItemStack stack = ((Player) this.getSelf()).getInventory().getItem(this.grabInventorySlot);
                    if (!stack.isEmpty() && stack.getItem() instanceof BlockItem BE && !(
                            BE.getBlock() instanceof ShulkerBoxBlock)) {


                        ItemStack stack2 = stack.copy();

                        if (placeBlock(stack2))
                            stack.shrink(1);


                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean placeBlock(ItemStack stack) {
        if (!this.getSelf().level().isClientSide()) {
                Vec3 vec3d = this.getSelf().getEyePosition(0);
                Vec3 vec3d2 = this.getSelf().getViewVector(0);
                Vec3 vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
                BlockHitResult $$0 = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
                BlockPos pos = null;
                if (this.getSelf().level().getBlockState($$0.getBlockPos()).isSolid() ||
                        this.getSelf().level().getBlockState($$0.getBlockPos()).is(ModBlocks.INVISIBLOCK)){
                    pos = $$0.getBlockPos().relative($$0.getDirection());
                }
                if (pos != null) {
                    BlockState state = this.getSelf().level().getBlockState(pos);
                    if (stack.getItem() instanceof BlockItem blockItem) {
                        if (MainUtil.getIsGamemodeApproriateForGrief(this.self)) {
                            if (!blockItem.getBlock().isCollisionShapeFullBlock(
                                    blockItem.getBlock().defaultBlockState(),this.self.level(),pos)){
                                return false;
                            }
                            if(this.getSelf() instanceof Player plr) {
                                if(!MainUtil.canPlaceOnClaim(plr,$$0)){
                                    return false;
                                }
                            }

                            if (tryHitBlockAndConvert($$0, pos, state, stack)) {
                                this.setCooldown(PowerIndex.SKILL_3, 3);
                                return true;
                            }
                        }
                    }
                }
        }
        return false;
    }

    protected boolean canPlace(BlockPlaceContext $$0, BlockState $$1, BlockItem stack) {
        Player $$2 = $$0.getPlayer();
        CollisionContext $$3 = $$2 == null ? CollisionContext.empty() : CollisionContext.of($$2);
        return ($$1.canSurvive($$0.getLevel(), $$0.getClickedPos())) && $$0.getLevel().isUnobstructed($$1, $$0.getClickedPos(), $$3);
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext $$0, BlockItem stack) {
        BlockState $$1 = stack.getBlock().getStateForPlacement($$0);
        return $$1 != null && this.canPlace($$0, $$1,stack) ? $$1 : null;
    }
    public boolean tryHitBlockAndConvert(BlockHitResult $$0, BlockPos pos, BlockState state, ItemStack stack){

        if ((state.isAir() || state.canBeReplaced()) && !((this.getSelf() instanceof Player &&
                (((Player) this.getSelf()).blockActionRestricted(this.getSelf().level(), pos, ((ServerPlayer)
                        this.getSelf()).gameMode.getGameModeForPlayer()))) ||
                !this.getSelf().level().mayInteract(((Player) this.getSelf()), pos))){

            if (stack.getItem() instanceof BlockItem BI) {
                Direction direction = $$0.getDirection();
                if (direction.getAxis() == Direction.Axis.X){
                    direction = direction.getOpposite();
                }
                if (BI.getBlock() instanceof RotatedPillarBlock){
                    direction = $$0.getDirection();
                }

                DirectionalPlaceContext dpc = new DirectionalPlaceContext(this.getSelf().level(),
                        pos,
                        direction, stack,
                        direction);
                if (dpc.canPlace()) {

                    BlockState $$2 = this.getPlacementState(dpc, BI);
                    if ($$2 != null) {
                        BlockPlaceContext $$1 = this.updatePlacementContext(dpc);
                        if (this.self.level().setBlock($$1.getClickedPos(), ModBlocks.INVISIBLOCK.defaultBlockState(), 11)) {
                            BlockEntity BE = this.self.level().getBlockEntity($$1.getClickedPos());
                            if (BE instanceof InvisiBlockEntity IBE) {


                                BlockPos $$3 = $$1.getClickedPos();
                                Level $$4 = $$1.getLevel();
                                Player $$5 = $$1.getPlayer();
                                BlockState $$7 = $$4.getBlockState($$3);
                                SoundType $$8 = $$7.getSoundType();
                                $$4.playSound($$5, $$3, this.getPlaceSound(BI.getBlock().defaultBlockState()), SoundSource.BLOCKS, ($$8.getVolume() + 1.0F) / 2.0F, $$8.getPitch() * 0.8F);
                                $$4.gameEvent(GameEvent.BLOCK_PLACE, $$3, GameEvent.Context.of($$5, $$7));

                                IBE.setOriginal2(BI.getBlock().getStateForPlacement($$1));
                                IBE.ticksUntilRestore = ((IEntityAndData) this.self).roundabout$getTrueInvisibility();
                                PowersAchtungBaby.spawnExplosionParticles(this.self.level(), pos.getCenter(), 10, 0.2);
                                return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext $$0) {
        return $$0;
    }
    protected SoundEvent getPlaceSound(BlockState $$0) {
        return $$0.getSoundType().getPlaceSound();
    }
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_2 && ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCooldownUsesServerLatency) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }


    public boolean isPlacable(ItemStack stack){
        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof BlockItem BE)
            return true;
        return false;
    }

    @SuppressWarnings("deprecation")
    public void burstBlocks(ServerLevel sl){
        int radius = ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstBlockRange;
        BlockPos baseCenter = this.self.getOnPos();
        if (radius > 0) {
            if (MainUtil.getIsGamemodeApproriateForGrief(this.self)) {
                for (int x = -radius; x <= radius; x++) {
                    for (int y = 0; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + y * y + z * z <= radius * radius) {
                                BlockPos targetPos = baseCenter.offset(x, y, z);
                                BlockState oldState = this.self.level().getBlockState(targetPos);

                                // Example: Replace dirt with glowstone
                                if (!oldState.isAir() && !oldState.is(Blocks.OBSIDIAN) && oldState.getBlock().isCollisionShapeFullBlock(oldState, this.self.level(), targetPos)
                                        && this.self.level().getBlockEntity(targetPos) == null && !MainUtil.confirmIsOre(oldState)) {
                                    BlockState replaced = sl.getBlockState(targetPos);
                                    BlockEntity replacedEntity = sl.getBlockEntity(targetPos);
                                    CompoundTag replacedTag = replacedEntity != null ? replacedEntity.saveWithFullMetadata() : null;

                                    sl.setBlock(targetPos, ModBlocks.INVISIBLOCK.defaultBlockState(), 3);

                                    BlockEntity maybeEntity = sl.getBlockEntity(targetPos);
                                    if (maybeEntity instanceof InvisiBlockEntity entity) {
                                        entity.setOriginal(replaced, replacedTag, this.self.level());
                                        entity.ticksUntilRestore = ((IEntityAndData) this.self).roundabout$getTrueInvisibility();
                                    }
                                    this.self.level().setBlock(targetPos, ModBlocks.INVISIBLOCK.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public boolean invisibleBurst(){
        if (isClient() || (!this.onCooldown(PowerIndex.SKILL_2) || !ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCooldownUsesServerLatency)) {
            setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCooldown);
            if (this.self.level() instanceof ServerLevel sl) {
                burstTicks = 22;
                burstParticles(sl);
                float range = ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstRange;
                burstEntities(range);
                burstBlocks(sl);
            }
        }
        return true;
    }

    public void burstRejection(){
        if (this.self.level() instanceof ServerLevel sl) {
            burstTicks = 22;
            burstParticlesRejection(sl);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.ACHTUNG_BURST_EVENT, SoundSource.PLAYERS, 0.95F, 1f);
            float range = ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstRange;
            burstEntities(range);
            burstBlocks(sl);
        }
    }

    public void burstParticlesRejection(ServerLevel sl){

        Vec3 pos = new Vec3(this.self.getX(),
                this.self.getY() +(this.self.getBbHeight()*0.5),
                this.self.getZ());
        sl.sendParticles(ModParticles.BABY_CRACKLE,
                pos.x(),
                pos.y(),
                pos.z(),
                0,0, 0, 0, 0);
        spawnExplosionParticles(this.self.level(), pos, 100, 0.5);
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
        burstEntitiesAggro();
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

    public void burstEntitiesAggro(){
        float range = 100;
        float range2 = 13;
        if (range2 > -1) {
            List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.self.level(), this.getSelf().blockPosition(), range + 1);
            if (!mobsInRange.isEmpty()) {
                for (Entity ent : mobsInRange) {
                    if (ent instanceof Mob mb && mb.getTarget() != null && mb.getTarget().is(this.self)) {
                        if (mb.distanceTo(this.self) >= range2) {
                            ((StandUser)mb).roundabout$aggressivelyEnforceAggro(null);
                        }
                    }
                }
            }
        }
    }
    public boolean invisibleBurstSimple(){
        if (isClient() || (!this.onCooldown(PowerIndex.SKILL_2) || !ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCooldownUsesServerLatency)) {
            setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCooldown);
            if (this.self.level() instanceof ServerLevel sl) {
                burstTicks = 22;
                burstParticles(sl);
                float range = ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstCrouchRange;
                burstEntities(range);

            }

        }
        return true;
    }

    public boolean canUseStillStandingRecharge(byte bt){
        if (bt == PowerIndex.SKILL_2)
            return false;
        return super.canUseStillStandingRecharge(bt);
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


    int timebetweenbursts=10;
    @Override
    public void tickStandRejection(MobEffectInstance effect) {
        if (!this.getSelf().level().isClientSide()) {
            timebetweenbursts--;
            if (timebetweenbursts <= -1){
                burstRejection();
                timebetweenbursts = 30;
            }
        }
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (this.self.level() instanceof ServerLevel sl) {
            if (attackTarget != null && !onCooldown(PowerIndex.SKILL_2)) {
                invisibleBurst();
            }
        }
    }

    @Override
    public void tickPower() {
        if (!this.self.level().isClientSide()){
            if (burstTicks > -1){
                burstTicks--;
            }

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
        if (ClientNetworking.getAppropriateConfig().achtungSettings.hidesPlacedBlocks) {
            if (slot == 3 && isHoldingSneak() && ((IEntityAndData) this.self).roundabout$getTrueInvisibility() <= -1)
                return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    public static final byte
            BURST = 61;
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.invisivision",
                "instruction.roundabout.press_skill", StandIcons.BABY_VISION_ON, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.invisiburst",
                "instruction.roundabout.press_skill", StandIcons.BURST_INVIS,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.controlled_burst",
                "instruction.roundabout.press_skill_crouch", StandIcons.SELF_INVIS,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        if (ClientNetworking.getAppropriateConfig().achtungSettings.hidesPlacedBlocks) {
            $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.place_invis",
                    "instruction.roundabout.press_skill_crouch", StandIcons.INVIS_BLOCK, 3, level, bypass));
        }
        return $$1;
    }

    /**
    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }
     **/


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