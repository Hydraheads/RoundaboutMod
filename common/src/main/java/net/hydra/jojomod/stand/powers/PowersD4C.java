package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.D4CPortalBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowersD4C extends NewPunchingStand {
    public PowersD4C(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersD4C(entity);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableKingCrimson;
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public static final byte WORLD_MERGE = 106;
    public static final byte PORTAL = 107;
    @Override
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == IMPALE_NOISE) {
            return 1.6F;
        } else {
            return super.getSoundPitchFromByte(soundChoice);
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_D4C_EVENT;
        } else if (soundChoice == WORLD_MERGE) {
            return ModSounds.WORLD_MERGE_EVENT;
        } else if (soundChoice == PORTAL) {
            return ModSounds.D4C_PORTAL_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public void enactEligability(){
        if (!isInBetweenSpace() && !self.isUnderWater()){
            if (hasBanner()){
                useUpBanner(self.getMainHandItem());
            }
        }
    }

    public boolean isEligable(){
        return hasBanner() || isInBetweenSpace() || self.isUnderWater();
    }
    public boolean isCollidingWithDoor() {
        AABB box = self.getBoundingBox();
        Level level = self.level();

        BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof DoorBlock)) {
                continue;
            }

            VoxelShape shape = state.getCollisionShape(level, pos);

            if (!shape.isEmpty()
                    && Shapes.joinIsNotEmpty(
                    shape.move(pos.getX(), pos.getY(), pos.getZ()),
                    Shapes.create(box),
                    BooleanOp.AND
            )) {
                return true;
            }
        }

        return false;
    }
    public boolean isInBetweenSpace(){
        if (isCollidingWithDoor()){
            return false;
        }

        return isBetweenSpace(BlockPos.containing(
                ((self.getEyePosition().subtract(self.getPosition(1f))).scale(0.2)).add(self.getPosition(1f))
        ), false);
    }
    public boolean isBetweenSpace(BlockPos pos, boolean move) {
        Level level = self.level();

        if (level.getBlockState(pos.below()).isSolid()
                && level.isRainingAt(pos)) {
            return true;
        }

        Direction gravity = RotationUtil.getGravityDirection(self);
        AABB box = self.getBoundingBox();
        if (move){
            box = box.move(Vec3.atCenterOf(pos).subtract(box.getCenter().subtract(
                    RotationUtil.vecPlayerToWorld(new Vec3(0,0.49,0),gravity)

            )));
        }

        AABB[] slices = getFootAndEyeSlices(box, gravity);

        for (Direction direction : Direction.values()) {

            // Along the gravity axis, use the entire player AABB.
            if (direction.getAxis() == gravity.getAxis()) {
                if (isBlockedInDirection(box, direction)
                        && isBlockedInDirection(box, direction.getOpposite())) {
                    return true;
                }
            }

            // Perpendicular to gravity, BOTH thin slices must be enclosed.
            else {
                if (isBlockedInDirection(slices[0], direction)
                        && isBlockedInDirection(slices[0], direction.getOpposite())
                        && isBlockedInDirection(slices[1], direction)
                        && isBlockedInDirection(slices[1], direction.getOpposite())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void spawnGravitySpiral(Level level, BlockPos pos, Direction gravity) {
        Direction upDirection = gravity.getOpposite();

        Vec3 start = Vec3.atCenterOf(pos).subtract(RotationUtil.vecPlayerToWorld(new Vec3(0,0.49,0),gravity));

        double height = 2.0D;
        int particles = 40;
        double radius = 0.35D;
        double rotations = 2.0D;

        for (int i = 0; i < particles; i++) {
            double progress = (double) i / (particles - 1);

            // Position along the axis opposite gravity.
            Vec3 axisOffset = new Vec3(
                    upDirection.getStepX() * height * progress,
                    upDirection.getStepY() * height * progress,
                    upDirection.getStepZ() * height * progress
            );

            // Spiral angle.
            double angle = progress * Math.PI * 2.0D * rotations;

            double offset1 = Math.cos(angle) * radius;
            double offset2 = Math.sin(angle) * radius;

            double x = start.x + axisOffset.x;
            double y = start.y + axisOffset.y;
            double z = start.z + axisOffset.z;

            // The two perpendicular axes depend on gravity.
            switch (gravity.getAxis()) {
                case Y -> {
                    x += offset1;
                    z += offset2;
                }

                case X -> {
                    y += offset1;
                    z += offset2;
                }

                case Z -> {
                    x += offset1;
                    y += offset2;
                }
            }
            Vector3f color;
            if (i % 2 == 0) {
                // Light blue
                color = new Vector3f(0.4F, 0.8F, 1.0F);
            } else {
                // Light purple
                color = new Vector3f(0.8F, 0.5F, 1.0F);
            }
            level.addParticle(
                    new DustParticleOptions(
                            color,
                            1.0F
                    ),
                    x,
                    y,
                    z,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }
    private AABB[] getFootAndEyeSlices(AABB box, Direction gravity) {
        double thickness = 0.05D;

        switch (gravity) {
            case DOWN -> {
                return new AABB[]{
                        // Foot
                        new AABB(
                                box.minX,
                                box.minY,
                                box.minZ,
                                box.maxX,
                                box.minY + thickness,
                                box.maxZ
                        ),

                        // Eye / head
                        new AABB(
                                box.minX,
                                box.maxY - thickness,
                                box.minZ,
                                box.maxX,
                                box.maxY,
                                box.maxZ
                        )
                };
            }

            case UP -> {
                return new AABB[]{
                        // Foot (upper end because gravity points UP)
                        new AABB(
                                box.minX,
                                box.maxY - thickness,
                                box.minZ,
                                box.maxX,
                                box.maxY,
                                box.maxZ
                        ),

                        // Eye / head
                        new AABB(
                                box.minX,
                                box.minY,
                                box.minZ,
                                box.maxX,
                                box.minY + thickness,
                                box.maxZ
                        )
                };
            }

            case NORTH -> {
                return new AABB[]{
                        // Foot
                        new AABB(
                                box.minX,
                                box.minY,
                                box.minZ,
                                box.maxX,
                                box.maxY,
                                box.minZ + thickness
                        ),

                        // Eye
                        new AABB(
                                box.minX,
                                box.minY,
                                box.maxZ - thickness,
                                box.maxX,
                                box.maxY,
                                box.maxZ
                        )
                };
            }

            case SOUTH -> {
                return new AABB[]{
                        // Foot
                        new AABB(
                                box.minX,
                                box.minY,
                                box.maxZ - thickness,
                                box.maxX,
                                box.maxY,
                                box.maxZ
                        ),

                        // Eye
                        new AABB(
                                box.minX,
                                box.minY,
                                box.minZ,
                                box.maxX,
                                box.maxY,
                                box.minZ + thickness
                        )
                };
            }

            case WEST -> {
                return new AABB[]{
                        // Foot
                        new AABB(
                                box.minX,
                                box.minY,
                                box.minZ,
                                box.minX + thickness,
                                box.maxY,
                                box.maxZ
                        ),

                        // Eye
                        new AABB(
                                box.maxX - thickness,
                                box.minY,
                                box.minZ,
                                box.maxX,
                                box.maxY,
                                box.maxZ
                        )
                };
            }

            case EAST -> {
                return new AABB[]{
                        // Foot
                        new AABB(
                                box.maxX - thickness,
                                box.minY,
                                box.minZ,
                                box.maxX,
                                box.maxY,
                                box.maxZ
                        ),

                        // Eye
                        new AABB(
                                box.minX,
                                box.minY,
                                box.minZ,
                                box.minX + thickness,
                                box.maxY,
                                box.maxZ
                        )
                };
            }

            default -> throw new IllegalStateException("Unexpected gravity: " + gravity);
        }
    }
    private boolean isBlockedInDirection(AABB box, Direction direction) {
        double checkDistance = 0.4D;

        AABB checkBox;

        switch (direction) {
            case EAST -> checkBox = new AABB(
                    box.maxX, box.minY+0.3, box.minZ,
                    box.maxX + checkDistance, box.maxY, box.maxZ
            );
            case WEST -> checkBox = new AABB(
                    box.minX - checkDistance, box.minY, box.minZ,
                    box.minX, box.maxY, box.maxZ
            );
            case UP -> checkBox = new AABB(
                    box.minX, box.maxY, box.minZ,
                    box.maxX, box.maxY + checkDistance, box.maxZ
            );
            case DOWN -> checkBox = new AABB(
                    box.minX, box.minY - checkDistance, box.minZ,
                    box.maxX, box.minY, box.maxZ
            );
            case SOUTH -> checkBox = new AABB(
                    box.minX, box.minY, box.maxZ,
                    box.maxX, box.maxY, box.maxZ + checkDistance
            );
            case NORTH -> checkBox = new AABB(
                    box.minX, box.minY, box.minZ - checkDistance,
                    box.maxX, box.maxY, box.minZ
            );
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        }

        return self.level()
                .getBlockCollisions(self, checkBox)
                .iterator()
                .hasNext();
    }
    public boolean hasBanner(){
        return MainUtil.isHoldingBanner(self);
    }

    public int getRechargeTime(){
        return 100;
    }
    public int rech = 0;
    @Override
    public void tickPower() {
        super.tickPower();
        if (self.level().isClientSide() && seesBetween){
            tickBetween();
        }
        if (!this.self.level().isClientSide() && self instanceof ServerPlayer sp){
            if (!canHoldBanner){
                rech++;
                if (rech >= getRechargeTime()){
                    canHoldBanner = true;
                    rech = 0;
                    saveDiscAndSync();
                }
            } else {
                rech = 0;
            }
        }
    }

    public boolean placeOne(BlockPos pos){
        BlockState state = self.level().getBlockState(pos);
        boolean water = state.getBlock().equals(Blocks.WATER);
        if (state.isAir() || water){
            BlockState state2 = ModBlocks.D4C_PORTAL.defaultBlockState();
            if (water) {
                state2.trySetValue(D4CPortalBlock.WATERLOGGED, true);
            }
            self.level().setBlock(pos, state2,3);
            return true;
        }
        return false;
    }
    public void worldMergingServer(){
        if (isEligable()){
            Direction dir = RotationUtil.getGravityDirection(self);
            BlockPos basePos = self.getOnPos().relative(dir.getOpposite());
            if (dir != Direction.DOWN){
                basePos = self.getOnPos();
            }
            if (placeOne(basePos)) {
                placeOne(basePos.relative(dir.getOpposite()));
            } else if (placeOne(basePos.north())){
                placeOne(basePos.north().relative(dir.getOpposite()));
            } else if (placeOne(basePos.south())){
                placeOne(basePos.south().relative(dir.getOpposite()));
            } else if (placeOne(basePos.east())){
                placeOne(basePos.east().relative(dir.getOpposite()));
            } else if (placeOne(basePos.west())) {
                placeOne(basePos.west().relative(dir.getOpposite()));
            } else if (placeOne(basePos.above())){
                    placeOne(basePos.above().relative(dir.getOpposite()));
            } else if (placeOne(basePos.below())){
                    placeOne(basePos.below().relative(dir.getOpposite()));
            } else if (placeOne(basePos.relative(dir.getOpposite()))){
                placeOne(basePos.relative(dir.getOpposite()).relative(dir.getOpposite()));
           } else {
                return;
            }
            playStandUserOnlySoundsIfNearby(WORLD_MERGE, 50, false, false);
            enactEligability();
        }
    }
    public void spawnCloneServer(){
        if (isEligable()){
            enactEligability();
        }
    }
    public void useUpBanner(ItemStack banner){
        if (self instanceof ServerPlayer pl) {
            pl.getCooldowns().addCooldown(banner.getItem(),getRechargeTime());
            pl.level().playSound(null, pl.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER,
                    SoundSource.PLAYERS, 1F, 1);
            canHoldBanner = false;
            saveDiscAndSync();
            pl.swing(InteractionHand.MAIN_HAND,true);
        }
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        return 25;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.D4C.create(this.getSelf().level());
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                worldMergingClient();
            }
            case SKILL_2_NORMAL -> {
                makeCloneClient();
            }
            case SKILL_3_GUARD -> {
                betweenVisionClient();
            }
            case SKILL_3_NORMAL -> {
                dashOrBlockSwitchClient();
            }
            case SKILL_3_CROUCH -> {
                chopClient();
            }
        }
    }
    public void worldMergingClient(){
        if (!this.onCooldown(PowerIndex.SKILL_1) && isEligable()) {
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }
    public boolean seesBetween = false;
    public int seesBetweenTicks = 0;
    public void makeCloneClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2) && isEligable()) {
                tryPowerPacket(PowerIndex.POWER_2);
        }
    }
    private BlockPos getForwardPosition(
            BlockPos origin,
            Direction facing,
            int forward,
            int side,
            int vertical
    ) {
        Direction sideways = facing.getClockWise();

        return origin
                .relative(facing, forward)
                .relative(sideways, side)
                .above(vertical);
    }
    public void tickBetween(){
        if (seesBetween){
            if (!isGuarding()){
                seesBetween = false;
                return;
            }
            seesBetweenTicks++;

            if (seesBetweenTicks >= 10){
                seesBetweenTicks =0;

                Direction dir = RotationUtil.getGravityDirection(self);


                Vec3 originVec = self.getEyePosition(1.0F);
                Vec3 look = self.getLookAngle().normalize();

                int maxDistance = 14;
                int maxSpots = 15;

                double coneAngle = Math.cos(Math.toRadians(30.0D));

                int found = 0;

                BlockPos origin = self.blockPosition();

                for (int x = -maxDistance; x <= maxDistance; x++) {
                    for (int y = -maxDistance; y <= maxDistance; y++) {
                        for (int z = -maxDistance; z <= maxDistance; z++) {

                            BlockPos pos = origin.offset(x, y, z);

                            Vec3 target = Vec3.atCenterOf(pos);

                            // Vector from the player's eyes toward this position.
                            Vec3 toTarget = target.subtract(originVec);

                            double distance = toTarget.length();

                            if (distance < 1.0D || distance > maxDistance) {
                                continue;
                            }

                            Vec3 directionToTarget = toTarget.normalize();

                            // Dot product determines whether this point is inside the cone.
                            if (look.dot(directionToTarget) < coneAngle) {
                                continue;
                            }

                            // Your existing checks.
                            if (!self.level().getBlockState(pos).isSolid()
                                    && !self.level().getBlockState(pos).liquid()
                                    && !self.level().getBlockState(
                                    pos.relative(dir.getOpposite())
                            ).isSolid()
                                    && !self.level().getBlockState(
                                    pos.relative(dir.getOpposite())
                            ).liquid()
                                    && isBetweenSpace(pos, true)) {

                                spawnGravitySpiral(
                                        self.level(),
                                        pos,
                                        RotationUtil.getGravityDirection(self)
                                );

                                if (++found >= maxSpots) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void betweenVisionClient(){
        seesBetween = !seesBetween;
        seesBetweenTicks = 10;
        if (seesBetween){
            this.getSelf().playSound(ModSounds.STAR_PLATINUM_SCOPE_EVENT, 1.0F, (float) (1.5F + (Math.random() * 0.04F)));
        }
        tickBetween();
    }
    public void dashOrBlockSwitchClient(){
            dash();
    }
    public void chopClient(){
        if (!canImpale()){
            return;
        }
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_1_SNEAK);
            }
        }
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.D4C_MELT_DODGE, PowerIndex.SKILL_3);
        } else if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 1, StandIcons.D4C_PARALLEL_RUNNING, PowerIndex.SKILL_1,0);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.D4C_PARALLEL_GRAB, PowerIndex.SKILL_3);
        }

        if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 2, StandIcons.D4C_CLONE_SUMMON, PowerIndex.SKILL_2,0);
        } else {
            LockedOrNot(context, x, y, 2, StandIcons.D4C_CLONE_SWAP, PowerIndex.SKILL_2_SNEAK,0);
        }

        if (!isHoldingSneak()) {
            if (isGuarding()){
                setSkillIcon(context, x, y, 3, StandIcons.D4C_BETWEEN_VISION, PowerIndex.NONE);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        } else {
            LockedOrNot(context, x, y, 3, StandIcons.D4C_CHOP, PowerIndex.SKILL_1_SNEAK,0);
        }
        if (!isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP_2, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4);
        }
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (slot == 1 || slot == 2 || slot == 4){
            return !isEligable();
        }
        return super.isAttackIneptVisually(activeP,slot);
    }
    @Override
    public boolean interceptGuard(){
        return true;
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            return true;
        }
        return super.cancelSprintJump();
    }

    public boolean setPowerFinalAttack() {
        animateFinalAttack();
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        this.clashDone = false;
        return true;
    }
    public void animateFinalAttack(){
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
    }

    public int chargedFinal = 0;
    public float getChargedPercent(){
        return (((float)this.chargedFinal/(float)getMaxSuperHitTime()));
    }

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            updateChop();
        } if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            updateFinalAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            updateFinalAttackCharge();
        }
        super.updateUniqueMoves();
    }

    public void updateChop(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 13) {
                this.standChop();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    if(this.attackTimeDuring%4==0) {
                        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    public void standChop(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(chopRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,chopRange);
            impaleImpact(targetEntity);
        }

    }

    public void updateFinalAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= 60) {
                if (this.getSelf() instanceof Player && this.getSelf().level().isClientSide && this.isPacketPlayer()){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring >= getMaxSuperHitTime() && !(this.getSelf() instanceof Player)){
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true,getMaxSuperHitTime());
            }
        }
    }
    @Override
    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.3f;
        }
        return super.inputSpeedModifiers(basis);
    }

    //hold input
    public boolean holdDownClick = false;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (impaleTicks > 0){
                return;
            }
            if (holdDownClick) {
                if (keyIsDown) {

                } else {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            super.buttonInputAttack(keyIsDown, options);
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }
    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            chopImpact(target);
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            finalAttackImpact(target);
        }
    }
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.SNEAK_ATTACK_CHARGE) {
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.POWER_1_SNEAK){
            return this.chopAttack();
        } else if (move == PowerIndex.POWER_2){
            spawnCloneServer();
            return false;
        }else if (move == PowerIndex.POWER_1){
            worldMergingServer();
            return false;
        }
        return super.setPowerOther(move,lastMove);
    }


    public boolean chopAttack(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){

            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1_SNEAK);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand(D4CEntity.IMPALE_2);
            this.poseStand(OffsetIndex.GUARD);

            return true;
        }
        return false;
    }
    public SoundEvent getChopSound(){
        return ModSounds.KING_CRIMSON_PUNCH_4_EVENT;

    }
    public boolean setPowerSuperHit() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,getMaxSuperHitTime());
        animateFinalAttackHit();
        //playBarrageCrySound();
        return true;
    }
    public void updateFinalAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.standFinalAttack();
            }
        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.theWorldAttackMultOnPlayers *0.01));
    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.theWorldAttackMultOnMobs *0.01));
    }
    public float getChopStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers((1F )));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs((6F )));
        }
    }

    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            int cdr = 25;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, cdr);
            return true;
        }
        return super.canInterruptPower(sauce,interrupter);
    }
    public float getChopKnockback(){
        return 1F;
    }

    public void chopImpact(Entity entity){
        if (activePower == PowerIndex.POWER_1_SNEAK){
            this.animateStand(D4CEntity.CHOP);
            this.setAttackTimeDuring(-20);
            if (entity != null && entity.distanceTo(self) > chopRange+0.75F) {
                entity = null;
            }
            if (entity != null) {
                hitParticlesCenter(entity);

                float pow;
                float knockbackStrength;
                pow = getChopStrength(entity);
                knockbackStrength = getChopKnockback();
                if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                    if (entity instanceof LivingEntity LE) {
                        addEXP(5, LE);
                        if (MainUtil.getMobBleed(entity)) {
                            MainUtil.makeBleed(entity, 1, 300, this.getSelf());
                        }
                    }
                    takeDeterminedKnockback(this.self, entity, knockbackStrength);
                } else {
                    knockShield2(entity, 100);
                }
            }

            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, 40);
            }
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 40);
            SoundEvent SE;
            float pitch = 1F;
            if (entity != null) {
                playImpaleConnectSoundExtra();
                if (airTriggered){
                    SE = ModSounds.PUNCH_4_SOUND_EVENT;
                } else {
                    SE = getChopSound();
                }
                pitch = 1.2F;
            } else {
                SE = getImpaleMissSound();
            }

            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            }
        }
    }

    public void standFinalAttack(){

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown + chargedFinal);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId());
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            finalAttackImpact(targetEntity);
        }
    }

    public float getFinalAttackKnockback(){
        float charge = getChargedPercent();
        if (charge >= 1){
            return (((float)this.chargedFinal /(float)getMaxSuperHitTime())*3.0F);
        } else if (charge >= 0.5F){
            return (((float)this.chargedFinal /(float)getMaxSuperHitTime())*2.8F);
        }
        return 0.1F;
    }
    public float getFinalPunchStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            float ret = (getChargedPercent()*punchD);
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=0.5F;
            }
            return ret;
        } else {
            float ret = (getChargedPercent()*punchD)+1;
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=1;
            }
            return ret;
        }
    }

    public void finalAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);

        if (entity != null && entity.distanceTo(self) > 5.5F) {
            entity = null;
        }
        if (entity != null) {
            float charged = getChargedPercent();
            hitParticlesCenter(entity);
            float pow;
            float knockbackStrength;
            pow = getFinalPunchStrength(entity);
            knockbackStrength = getFinalAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (charged >= 1) {
                        addEXP(5, LE);
                    } else if (charged > 0.5F){
                        addEXP(3, LE);
                    }
                }
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= getMaxSuperHitTime()) {
                    if (charged >= 1) {
                        knockShield2(entity, 70);
                    } else if (charged > 0.5F){
                        knockShield2(entity, 50);
                    }
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = getFinalAttackSound();
            pitch = getFinalAttackPitch();
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }
    public SoundEvent getFinalAttackSound(){
        float charged = getChargedPercent();
        if (charged >= 0.5F){
            return ModSounds.KING_CRIMSON_PUNCH_5_EVENT;
        }
        return ModSounds.KING_CRIMSON_PUNCH_3_EVENT;
    }
    public float getFinalAttackPitch(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            return 1;
        } else if (charged >= 0.5F){
            return 1;
        }
        return 1.2F;
    }

    public void animateFinalAttackHit(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            animateStand(KingCrimsonEntity.FINAL_2);
            return;
        } else if (charged >= 0.5F){
            animateStand(KingCrimsonEntity.FINAL_1);
            return;
        }
        animateStand((byte) 86);
    }
    public static final float chopRange = 4F;

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, chopRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            float zamn = ((float) attackTimeDuring / getMaxSuperHitTime());
            int ClashTime = Math.min(15,Math.round(zamn * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 213, 68, 15, 6);
            if (zamn >= 1){
                context.blit(StandIcons.JOJO_ICONS, k, j, 213, 89, ClashTime, 6);
            } else if (crossedThreshold2(zamn)){
                context.blit(StandIcons.JOJO_ICONS, k, j, 213, 75, ClashTime, 6);
            } else {
                context.blit(StandIcons.JOJO_ICONS, k, j, 213, 82, ClashTime, 6);
            }
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }
    public boolean crossedThreshold2(float zamn){
        return zamn >= 0.5F;
    }

    public int getMaxSuperHitTime(){
        return 30+(getMeltLevel()*2);
    }
    @Override public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.d4c."+getSkinString(skinId));
    }
    public static Component getSkinNameStatic(byte skinId) {
        return Component.translatable("skins.roundabout.d4c."+getSkinString(skinId));
    }
    public static String getSkinString(byte skinId) {

        return switch (skinId)
        {
            case D4CEntity.BASE -> "base";
            case D4CEntity.COVER -> "cover";
            case D4CEntity.SPINE -> "spine";
            case D4CEntity.VELLER -> "veller";
            case D4CEntity.ROA -> "roa";
            case D4CEntity.DEPARTURE -> "departure";
            case D4CEntity.PROMO -> "promo";
            case D4CEntity.FOUNTAIN_BOY -> "fountain_boy";
            case D4CEntity.CONCEPT_ART -> "concept_art";
            case D4CEntity.EOH_BLUE -> "eoh_blue";
            case D4CEntity.EOH_PINK -> "eoh_pink";
            case D4CEntity.EOH_PURPLE -> "eoh_purple";
            case D4CEntity.EOH_YELLOW -> "eoh_yellow";
            case D4CEntity.GOLDEN -> "golden";
            case D4CEntity.GOLDEN_V2 -> "golden_v2";
            case D4CEntity.CURSED_CARD -> "cursed_card";
            case D4CEntity.ULTRA_BRAND -> "ultra_brand";
            case D4CEntity.KEYHOLDER -> "keyholder";
            case D4CEntity.GRAY -> "gray";
            case D4CEntity.BLACK_WHITE -> "black_white";
            case D4CEntity.REVERSE -> "reverse";
            case D4CEntity.CHOCOLATE -> "chocolate";
            case D4CEntity.INVERSE -> "inverse";
            default -> "base";
        };
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(D4CEntity.BASE);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(D4CEntity.COVER);
                $$1.add(D4CEntity.SPINE);
                $$1.add(D4CEntity.CHOCOLATE);
            } if (Level > 2 || bypass){
                $$1.add(D4CEntity.VELLER);
                $$1.add(D4CEntity.ROA);
                $$1.add(D4CEntity.BLACK_WHITE);
                $$1.add(D4CEntity.REVERSE);
            } if (Level > 3 || bypass){
                $$1.add(D4CEntity.DEPARTURE);
                $$1.add(D4CEntity.PROMO);
                $$1.add(D4CEntity.KEYHOLDER);
                $$1.add(D4CEntity.GRAY);
            } if (Level > 4 || bypass){
                $$1.add(D4CEntity.EOH_BLUE);
                $$1.add(D4CEntity.EOH_PINK);
                $$1.add(D4CEntity.EOH_PURPLE);
                $$1.add(D4CEntity.EOH_YELLOW);
            } if (Level > 5 || bypass){
                $$1.add(D4CEntity.FOUNTAIN_BOY);
                $$1.add(D4CEntity.CONCEPT_ART);
                $$1.add(D4CEntity.CURSED_CARD);
                $$1.add(D4CEntity.ULTRA_BRAND);
            } if (Level > 6 || bypass){
                $$1.add(D4CEntity.INVERSE);
                $$1.add(D4CEntity.GOLDEN);
                $$1.add(D4CEntity.GOLDEN_V2);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
            }
        }
        return $$1;
    }


    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.31F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4.5f));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.87F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5.5F));
        }
    }

    @Override
    public float getBarrageDamagePlayer(){
        return 8;
    }

    @Override
    public float getBarrageDamageMob(){
        return 18;
    }

    public boolean canHoldBanner = true;
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("canHoldBanner",canHoldBanner);

    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);

        if ($$0.contains("canHoldBanner")) {
            canHoldBanner = $$0.getBoolean("canHoldBanner");
        }

    }
    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }
}
