package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.D4CPortalBlock;
import net.hydra.jojomod.block.D4CPortalBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.objects.FallingBannerEntity;
import net.hydra.jojomod.entity.objects.IceTwisterEntity;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
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
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.*;

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
        return ClientNetworking.getAppropriateConfig().d4cSettings.enableD4c;
    }
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public static final byte WORLD_MERGE = 106;
    public static final byte PORTAL = 107;
    public static final byte FUSE = 108;
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
        } else if (soundChoice == FUSE) {
            return ModSounds.D4C_FUSE_EVENT;
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
    public boolean isEligableForExit(){
        return isEligable() || isCollidingWithD4CPortal(self);
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
        if (self.getPose() == Pose.CROUCHING){
            box = ((IEntityAndData)self).rdbt$getPoseBox(Pose.STANDING);
        }
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
                                box.minY - thickness,
                                box.maxZ
                        ),

                        // Eye / head
                        new AABB(
                                box.minX,
                                box.maxY + thickness,
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
                                box.maxY + thickness,
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
                                box.minY - thickness,
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
    public static boolean isCollidingWithD4CPortal(Entity entity) {
        Level level = entity.level();
        AABB box = entity.getBoundingBox();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int minX = Mth.floor(box.minX);
        int minY = Mth.floor(box.minY);
        int minZ = Mth.floor(box.minZ);

        int maxX = Mth.floor(box.maxX);
        int maxY = Mth.floor(box.maxY);
        int maxZ = Mth.floor(box.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    pos.set(x, y, z);

                    if (level.getBlockState(pos).getBlock()
                            instanceof D4CPortalBlock) {

                        if (entity.level().isClientSide()){
                            return true;
                        } else {
                            BlockEntity blockEntity = level.getBlockEntity(pos);

                            if (blockEntity instanceof D4CPortalBlockEntity portal) {
                                if (portal.worldId ==
                                        PowerTypes.getPlaneOfExisting2(entity)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
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

    public boolean placeOne(BlockPos pos, int worldId){
        BlockState state = self.level().getBlockState(pos);
        boolean water = state.getBlock().equals(Blocks.WATER);
        if (state.isAir() || water){
            BlockState state2 = ModBlocks.D4C_PORTAL.defaultBlockState();
            if (water) {
                state2.trySetValue(D4CPortalBlock.WATERLOGGED, true);
            }
            self.level().setBlock(pos, state2,3);
            if (self.level().getBlockEntity(pos) instanceof D4CPortalBlockEntity portal){
                portal.ticksUntilRestore = PowerTypes.d4cWorldUptime();
                portal.initialized = true;
                portal.worldId = worldId;
                portal.creator = self.getUUID();
            }
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
            int worldId = ((int) (Math.random()*4))+1;
            if (placeOne(basePos,worldId)) {
                placeOne(basePos.relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.north(),worldId)){
                placeOne(basePos.north().relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.south(),worldId)){
                placeOne(basePos.south().relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.east(),worldId)){
                placeOne(basePos.east().relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.west(),worldId)) {
                placeOne(basePos.west().relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.above(),worldId)){
                    placeOne(basePos.above().relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.below(),worldId)){
                    placeOne(basePos.below().relative(dir.getOpposite()),worldId);
            } else if (placeOne(basePos.relative(dir.getOpposite()),worldId)){
                placeOne(basePos.relative(dir.getOpposite()).relative(dir.getOpposite()),worldId);
           } else {
                return;
            }

//            for (LivingEntity target : self.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(),self,self.getBoundingBox().inflate(20))) {
//                if (!target.equals(self) && target.isAlive()) {
//                    PowerTypes.forcePlaneOfExisting(target,(byte)1);
//                }
//            }
            populateWorld((byte) worldId);
            enactEligability2();
            PowerTypes.setPlaneOfExisting(self,(byte)worldId);
            playStandUserOnlySoundsIfNearby(WORLD_MERGE, 50, false, false);
        }
    }
    public void enactEligability2(){
        if (!isInBetweenSpace() && !self.isUnderWater()){
            if (hasBanner()){

                FallingBannerEntity twister = new FallingBannerEntity(
                        this.self.level(), self.getEyePosition());
                PowerTypes.copyPlaneOfExisting(self,twister);
                twister.user = self;
                twister.setBanner(self.getMainHandItem());
                twister.lifeSpan = 60;
                this.getSelf().level().addFreshEntity(twister);
                useUpBanner(self.getMainHandItem());
            }
        }
    }
    public void populateWorld(byte worldId) {
        if (!(self.level() instanceof ServerLevel sl)) {
            return;
        }

        int maxCopies = 8;
        double scanRadius = 15.0D;
        double scanRadius2 = 70.0D;
        double spawnRadius = 10.0D;

        AABB scanBox = self.getBoundingBox().inflate(scanRadius);
        AABB scanBox2 = self.getBoundingBox().inflate(scanRadius2);

        // -------------------------------------------------
        // FIRST: Check if this alternate world is already
        // populated in this area.
        // -------------------------------------------------

        List<LivingEntity> existingParallelEntities =
                sl.getEntitiesOfClass(
                        LivingEntity.class,
                        scanBox2,
                        entity ->
                                entity.isAlive()
                                        && entity != self
                                        && PowerTypes.getPlaneOfExisting(entity) == worldId
                                        && !PowerTypes.isNativeToOurWorld(entity)
                );

        // If this universe already has its population here,
        // refresh all of their lifetimes and do not create
        // any new copies.
        if (!existingParallelEntities.isEmpty()) {

            for (LivingEntity entity : existingParallelEntities) {
                PowerTypes.setTicksUntilGone(
                        entity,
                        PowerTypes.getForeignWorldMaxTime(worldId),
                        worldId
                );
            }

            return;
        }

        // -------------------------------------------------
        // OTHERWISE: Populate it normally.
        // -------------------------------------------------

        List<LivingEntity> possibleTargets = sl.getEntitiesOfClass(
                LivingEntity.class,
                scanBox,
                entity ->
                        entity.isAlive()
                                && !(entity instanceof StandEntity)
                                && !(entity instanceof CloneEntity)
                                && MainUtil.canCopyMob(entity)
                                && PowerTypes.originatedFromOurWorld(entity)
                                && PowerTypes.getPlaneOfExisting(entity) == 0
        );

        Collections.shuffle(possibleTargets);

        int copied = 0;
        int copiedPlayers = 0;

        for (LivingEntity target : possibleTargets) {
            if (target instanceof Player pl && self instanceof Player pl2) {
                if (pl.isSpectator() || (pl.isCreative() && !pl2.isCreative())) {
                    continue;
                }
                if (copiedPlayers >= maxCopies) {
                    continue;
                }

                Vec3 spawnPos = findWorldMergeSpawnPosition(
                        sl,
                        target,
                        spawnRadius
                );

                if (spawnPos == null) {
                    continue;
                }
                if (createParallelPlayerCopy(sl, pl, spawnPos, worldId)) {
                    copied++;
                }
                copiedPlayers++;
                continue;
            }
            if (copied >= maxCopies) {
                continue;
            }

            Vec3 spawnPos = findWorldMergeSpawnPosition(
                    sl,
                    target,
                    spawnRadius
            );

            if (spawnPos == null) {
                continue;
            }

            if (createParallelCopy(sl, target, spawnPos, worldId)) {
                copied++;
            }
        }


        if (this.getSelf().level().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            float chanceX = ClientNetworking.getAppropriateConfig().d4cSettings.chanceToEncounterNewAnimal;
            if (chanceX > 0 && (Math.random() * 1 < chanceX)) {

                Vec3 spawnPos = findWorldMergeSpawnPosition(
                        sl,
                        spawnRadius
                );

                if (spawnPos != null) {

                    double rand = Math.random() * 1;
                    Entity copyEntity;

                    if (self.level().canSeeSky(self.getOnPos()) || self.level().canSeeSky(self.getOnPos().north()) ||
                            self.level().canSeeSky(self.getOnPos().west()) ||
                            self.level().canSeeSky(self.getOnPos().east()) || self.level().canSeeSky(self.getOnPos().south())
                     || self.level().canSeeSky(self.getOnPos())) {
                        if (rand <= 0.1F) {
                            if (self.level().getBiome(this.getSelf().getOnPos()).is(Biomes.DESERT)) {
                                copyEntity = EntityType.CAMEL.create(this.getSelf().level());
                            } else {
                                copyEntity = EntityType.HORSE.create(this.getSelf().level());
                            }
                        } else if (rand <= 0.3F) {
                            Holder<Biome> biome = self.level().getBiome(this.getSelf().getOnPos());
                            if (biome.is(Biomes.BAMBOO_JUNGLE)) {
                                copyEntity = EntityType.PANDA.create(this.getSelf().level());
                            } else if (biome.is(Biomes.DESERT)) {
                                copyEntity = ModEntities.TERRIER_DOG.create(this.getSelf().level());
                            } else if (biome.is(Biomes.TAIGA)) {
                                copyEntity = EntityType.WOLF.create(this.getSelf().level());
                            } else if (biome.is(Biomes.JUNGLE) ||
                                    biome.is(Biomes.SPARSE_JUNGLE)) {
                                copyEntity = EntityType.PARROT.create(this.getSelf().level());
                            } else {
                                copyEntity = EntityType.COW.create(this.getSelf().level());
                            }
                        } else if (rand <= 0.4F) {
                            copyEntity = EntityType.COW.create(this.getSelf().level());
                        } else if (rand <= 0.45F) {
                            copyEntity = EntityType.CAT.create(this.getSelf().level());
                        } else if (rand <= 0.7F) {
                            copyEntity = EntityType.SHEEP.create(this.getSelf().level());
                        } else {
                            copyEntity = EntityType.PIG.create(this.getSelf().level());
                        }

                        if (copyEntity != null) {
                            PowerTypes.setPlaneOfExisting(copyEntity, worldId);
                            PowerTypes.setTicksUntilGone(
                                    copyEntity,
                                    PowerTypes.getForeignWorldMaxTime(worldId),
                                    worldId
                            );
                            self.level().addFreshEntity(copyEntity);
                        }
                    } else {
                        if (self.level().getBiome(this.getSelf().getOnPos()).is(Biomes.DRIPSTONE_CAVES)) {
                            copyEntity = EntityType.AXOLOTL.create(this.getSelf().level());

                            if (copyEntity != null) {
                                PowerTypes.setPlaneOfExisting(copyEntity, worldId);
                                PowerTypes.setTicksUntilGone(
                                        copyEntity,
                                        PowerTypes.getForeignWorldMaxTime(worldId),
                                        worldId
                                );
                                self.level().addFreshEntity(copyEntity);
                            }
                        }
                    }
                }
            }
        }

        // Spawn a random listed player
        if (copiedPlayers <= 1) {
            float chance = ClientNetworking.getAppropriateConfig().d4cSettings.chanceToEncounterPlayer;
            if (chance > 0 && (Math.random()*1 < chance)) {
                Vec3 spawnPos = findWorldMergeSpawnPosition(
                        sl,
                        spawnRadius
                );

                if (spawnPos == null) {
                    return;
                }

                Entity copyEntity = ModEntities.D4C_CLONE.create(this.getSelf().level());

                if (!(copyEntity instanceof D4CCloneEntity copy)) {
                    return;
                }

                if (MainUtil.playerNames == null || MainUtil.playerNames.isEmpty()) {
                    return;
                }

                List<Map.Entry<String, UUID>> players =
                        new ArrayList<>(MainUtil.playerNames.entrySet());

                Map.Entry<String, UUID> randomPlayer =
                        players.get(this.getSelf().getRandom().nextInt(players.size()));
                if (randomPlayer == null) {
                    return;
                }

                String playerName = randomPlayer.getKey();
                UUID playerUUID = randomPlayer.getValue();
                if (playerUUID != null && playerUUID.equals(self.getUUID())) {
                    //I don't want to spawn 2 of myself lol
                    return;
                }

                copy.setDisguiseName(playerName);
                copy.setCustomName(Component.literal(playerName));
                copy.setCustomNameVisible(true);
                copy.setPlayerUUID(playerUUID);

                copy.moveTo(
                        spawnPos.x,
                        spawnPos.y,
                        spawnPos.z,
                        self.getYRot(),
                        self.getXRot()
                );

                // Alternate universe
                ((IEntityAndData) copy).rdbt$setNativeCopy(playerUUID);

                PowerTypes.setPlaneOfExisting(copy, worldId);
                PowerTypes.setTicksUntilGone(
                        copy,
                        PowerTypes.getForeignWorldMaxTime(worldId),
                        worldId
                );

                self.level().addFreshEntity(copy);
            }
        }
    }

    @Nullable
    public Vec3 findWorldMergeSpawnPositionAnimal(
            ServerLevel level,
            double radius
    ) {
        int attempts = 40;
        double minDistance = 2.5D+ (0.5);

        for (int i = 0; i < attempts; i++) {

            double angle = Math.random() * Math.PI * 2.0D;

            // Random distance between minDistance and radius
            double distance = minDistance
                    + Math.sqrt(Math.random()) * (radius - minDistance);

            double x = self.getX() + Math.cos(angle) * distance;
            double z = self.getZ() + Math.sin(angle) * distance;

            int baseY = Mth.floor(self.getY());

            for (int yOffset = -4; yOffset <= 4; yOffset++) {

                double y = baseY + yOffset;

                Vec3 candidate = new Vec3(x, y, z);

                AABB yesbox = EntityType.HORSE.getAABB(self.getX(),self.getY(),self.getZ());

                AABB testBox = yesbox.move(
                        candidate.x - self.getX(),
                        candidate.y - self.getY(),
                        candidate.z - self.getZ()
                );

                if (level.noCollision(self, testBox)) {
                    return candidate;
                }
            }
        }

        return null;
    }
    @Nullable
    public Vec3 findWorldMergeSpawnPosition(
            ServerLevel level,
            double radius
    ) {
        int attempts = 40;
        double minDistance = 2.5D+ (0.5);

        for (int i = 0; i < attempts; i++) {

            double angle = Math.random() * Math.PI * 2.0D;

            // Random distance between minDistance and radius
            double distance = minDistance
                    + Math.sqrt(Math.random()) * (radius - minDistance);

            double x = self.getX() + Math.cos(angle) * distance;
            double z = self.getZ() + Math.sin(angle) * distance;

            int baseY = Mth.floor(self.getY());

            for (int yOffset = -4; yOffset <= 4; yOffset++) {

                double y = baseY + yOffset;

                Vec3 candidate = new Vec3(x, y, z);

                AABB yesbox = EntityType.PLAYER.getAABB(self.getX(),self.getY(),self.getZ());

                AABB testBox = yesbox.move(
                        candidate.x - self.getX(),
                        candidate.y - self.getY(),
                        candidate.z - self.getZ()
                );

                if (level.noCollision(self, testBox)) {
                    return candidate;
                }
            }
        }

        return null;
    }

    public static final boolean debugCollision = false;

    public boolean createParallelPlayerCopy(
            ServerLevel level,
            Player original,
            Vec3 spawnPos,
            byte worldId
    ) {
        Entity copyEntity = ModEntities.D4C_CLONE.create(this.getSelf().level());

        if (!(copyEntity instanceof D4CCloneEntity copy)) {
            return false;
        }

        // -------------------------------------------------
        // UNIVERSAL STUFF YOU ACTUALLY WANT TO PRESERVE
        // -------------------------------------------------

        // Name
        if (original.hasCustomName()) {
            copy.setCustomName(original.getCustomName());
            copy.setCustomNameVisible(original.isCustomNameVisible());
        }

        if (original.getUUID().equals(self.getUUID())){
            copy.safeCopy = true;
        }
        copy.setPlayer(original);
        copy.setItemSlot(EquipmentSlot.HEAD, original.getItemBySlot(EquipmentSlot.HEAD).copy());
        copy.setItemSlot(EquipmentSlot.CHEST, original.getItemBySlot(EquipmentSlot.CHEST).copy());
        copy.setItemSlot(EquipmentSlot.LEGS, original.getItemBySlot(EquipmentSlot.LEGS).copy());
        copy.setItemSlot(EquipmentSlot.FEET, original.getItemBySlot(EquipmentSlot.FEET).copy());
        copy.setItemSlot(EquipmentSlot.MAINHAND, original.getMainHandItem().copy());
        copy.setItemSlot(EquipmentSlot.OFFHAND, original.getOffhandItem().copy());
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            copy.setDropChance(slot, 0.0F);
        }

        ItemStack standDisc = ((StandUser)original).roundabout$getStandDisc();
        if (standDisc != null && !standDisc.isEmpty() && standDisc.getItem() instanceof StandDiscItem sdi){
            if (!(sdi.standPowers instanceof PowersD4C)){
                float randomChance = ClientNetworking.getAppropriateConfig().d4cSettings.chanceForAltStands;
                if (Math.random() <= randomChance && !ModItems.getPoolForMob(copy).isEmpty()) {
                    int index = (int) (Math.floor(Math.random() * ModItems.getPoolForMob(copy).size()));
                    ItemStack stack = ModItems.getPoolForMob(copy).get(index).getDefaultInstance();
                    if (!stack.isEmpty() && stack.getItem() instanceof StandDiscItem SD) {
                        ((StandUser) copy).roundabout$setStandDisc(stack);
                    }
                } else {
                    ((StandUser) copy).roundabout$setStandDisc(standDisc);
                }
            }
        }

        // Rotation
        copy.setYRot(original.getYRot());
        copy.setXRot(original.getXRot());

        // -------------------------------------------------
        // ENTITY-SPECIFIC APPEARANCE
        // -------------------------------------------------


        // Position
        copy.moveTo(
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                original.getYRot(),
                original.getXRot()
        );

        // Alternate universe
        ((IEntityAndData)copy).rdbt$setNativeCopy(original.getUUID());
        PowerTypes.setPlaneOfExisting(copy, worldId);
        PowerTypes.setTicksUntilGone(copy, PowerTypes.getForeignWorldMaxTime(worldId),worldId);

        level.addFreshEntity(copy);
        return true;
    }
    public boolean createParallelCopy(
            ServerLevel level,
            LivingEntity original,
            Vec3 spawnPos,
            byte worldId
    ) {
        Entity copyEntity = original.getType().create(level);

        if (!(copyEntity instanceof LivingEntity copy)) {
            return false;
        }

        // -------------------------------------------------
        // UNIVERSAL STUFF YOU ACTUALLY WANT TO PRESERVE
        // -------------------------------------------------

        // Name
        if (original.hasCustomName()) {
            copy.setCustomName(original.getCustomName());
            copy.setCustomNameVisible(original.isCustomNameVisible());
        }

        // Rotation
        copy.setYRot(original.getYRot());
        copy.setXRot(original.getXRot());

        // -------------------------------------------------
        // ENTITY-SPECIFIC APPEARANCE
        // -------------------------------------------------

        copyMobAppearance(original, copy);

        // Position
        copy.moveTo(
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                original.getYRot(),
                original.getXRot()
        );

        // Alternate universe
        PowerTypes.setPlaneOfExisting(copy, worldId);
        PowerTypes.setTicksUntilGone(copy, PowerTypes.getForeignWorldMaxTime(worldId),worldId);

        level.addFreshEntity(copy);

        return true;
    }
    public void copyMobAppearance(LivingEntity original, LivingEntity copy) {

        // Horses
        if (original instanceof Horse originalHorse
                && copy instanceof Horse copyHorse) {

            copyHorse.setVariant(originalHorse.getVariant());
        }

        ((IEntityAndData)copy).rdbt$setNativeCopy(original.getUUID());

        if (original instanceof Mob mb && copy instanceof Mob mb2){
            if (original instanceof AgeableMob ag && copy instanceof AgeableMob ag2){
                ag2.setAge(ag.getAge());
            }
            mb2.setBaby(mb.isBaby());

            mb2.setItemSlot(EquipmentSlot.HEAD, mb.getItemBySlot(EquipmentSlot.HEAD).copy());
            mb2.setItemSlot(EquipmentSlot.CHEST, mb.getItemBySlot(EquipmentSlot.CHEST).copy());
            mb2.setItemSlot(EquipmentSlot.LEGS, mb.getItemBySlot(EquipmentSlot.LEGS).copy());
            mb2.setItemSlot(EquipmentSlot.FEET, mb.getItemBySlot(EquipmentSlot.FEET).copy());
            mb2.setItemSlot(EquipmentSlot.MAINHAND, mb.getMainHandItem().copy());
            mb2.setItemSlot(EquipmentSlot.OFFHAND, mb.getOffhandItem().copy());
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                mb2.setDropChance(slot, 0.0F);
            }

        }
        // Cats
        if (original instanceof Cat originalCat
                && copy instanceof Cat copyCat) {

            copyCat.setVariant(originalCat.getVariant());
        }
        if (original instanceof Phantom originalPhantom
                && copy instanceof Phantom copyPhantom) {

            copyPhantom.setPhantomSize(originalPhantom.getPhantomSize());
        }
        if (original instanceof Slime originalSlime
                && copy instanceof Slime copySlime) {

            copySlime.setSize(originalSlime.getSize(), true);
        }
        // Sheep
        if (original instanceof Sheep originalSheep
                && copy instanceof Sheep copySheep) {

            copySheep.setColor(originalSheep.getColor());
        }

        // Parrots
        if (original instanceof Parrot originalParrot
                && copy instanceof Parrot copyParrot) {

            copyParrot.setVariant(originalParrot.getVariant());
        }
        // Aestheticians
        if (original instanceof Aesthetician originalAya
                && copy instanceof Aesthetician copyAya) {

            copyAya.setSkinNumber(originalAya.getSkinNumber());
        }
        // Rabbits
        if (original instanceof Rabbit originalRabbit
                && copy instanceof Rabbit copyRabbit) {

            copyRabbit.setVariant(originalRabbit.getVariant());
        }

        // Foxes
        if (original instanceof Fox originalFox
                && copy instanceof Fox copyFox) {

            copyFox.setVariant(originalFox.getVariant());
        }

        // Axolotls
        if (original instanceof Axolotl originalAxolotl
                && copy instanceof Axolotl copyAxolotl) {

            copyAxolotl.setVariant(originalAxolotl.getVariant());
        }

        // Frogs
        if (original instanceof Frog originalFrog
                && copy instanceof Frog copyFrog) {

            copyFrog.setVariant(originalFrog.getVariant());
        }

        // Tropical fish
        if (original instanceof TropicalFish originalFish
                && copy instanceof TropicalFish copyFish) {

            copyFish.setVariant(originalFish.getVariant());
        }
    }

    @Nullable
    public Vec3 findWorldMergeSpawnPosition(
            ServerLevel level,
            LivingEntity entity,
            double radius
    ) {
        int attempts = 40;
        double minDistance = 2.5D+ (entity.getBbWidth()/2);

        for (int i = 0; i < attempts; i++) {

            double angle = Math.random() * Math.PI * 2.0D;

            // Random distance between minDistance and radius
            double distance = minDistance
                    + Math.sqrt(Math.random()) * (radius - minDistance);

            double x = self.getX() + Math.cos(angle) * distance;
            double z = self.getZ() + Math.sin(angle) * distance;

            int baseY = Mth.floor(self.getY());

            for (int yOffset = -4; yOffset <= 4; yOffset++) {

                double y = baseY + yOffset;

                Vec3 candidate = new Vec3(x, y, z);

                AABB testBox = entity.getBoundingBox().move(
                        candidate.x - entity.getX(),
                        candidate.y - entity.getY(),
                        candidate.z - entity.getZ()
                );

                if (level.noCollision(entity, testBox)) {
                    return candidate;
                }
            }
        }

        return null;
    }
    public static int getDeductionTicks(Entity target, double distance){
        if (PowerTypes.isInD4CWorldWithRender(target)){
            if (distance >= 15) {
                return 6;
            } else if (distance >= 14){
                return 3;
            } else if (distance >= 12){
                return 2;
            }
        }
        return 1;
    }
    public void isekaiTarget(Entity target){
        if (isEligable()) {
            this.setAttackTimeDuring(-7);
            if (target != null && !target.isRemoved()){
                if (target.distanceTo(self) > grabRange+0.75F ||
                MainUtil.isBossMob(target)) {
                    target = null;
                }
                int worldId = ((int) (Math.random() * 2)) + 6;
                PowerTypes.setPlaneOfExisting(target, (byte) worldId);
                playStandUserOnlySoundsIfNearby(WORLD_MERGE, 50, false, false);
            }
            if (target == null){
                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.PUNCH_1_SOUND_EVENT,
                        SoundSource.PLAYERS, 0.95F, 1F);
            }
            enactEligability();
        } else {
            xTryPower(PowerIndex.NONE,true);
            animateStand(StandEntity.IDLE);
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
            playSoundIfPossible(self.level(),null, pl.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER,
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
            case SKILL_1_CROUCH -> {
                worldTakingClient();
            }
            case SKILL_2_NORMAL -> {
                makeCloneClient();
            }
            case SKILL_2_GUARD -> {
                switchCloneClient();
            }
            case SKILL_2_CROUCH_GUARD -> {
                replaceBodyClient();
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

    public void pullIntoRealityClient(){
        Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(), getReach());
        if (targetEntity !=null && targetEntity.isAlive()){
            tryIntPowerPacket(PowerIndex.POWER_2_BONUS,targetEntity.getId());
        }
    }

    public void exitD4CClient(){
        if (isEligableForExit()){
            tryPowerPacket(PowerIndex.POWER_1_BONUS);
        }
    }
    public void worldTakingClient(){
        if (PowerTypes.isInD4CWorld(self)){
            exitD4CClient();
        } else if (!this.onCooldown(PowerIndex.SKILL_EXTRA) && isEligable()) {
            if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_1_SNEAK);
            }
        }
    }
    public void worldMergingClient(){
        if (PowerTypes.isInD4CWorld(self)){
            exitD4CClient();
        } else if (!this.onCooldown(PowerIndex.SKILL_1) && isEligable()) {
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }
    public boolean seesBetween = false;
    public int seesBetweenTicks = 0;
    public void switchCloneClient(){
        if (PowerTypes.isInD4CWorld(self)){
            return;
        }

    }
    public void replaceBodyClient(){
        if (PowerTypes.isInD4CWorld(self)){
            pullIntoRealityClient();
            return;
        }

    }
    public void makeCloneClient(){
        if (PowerTypes.isInD4CWorld(self)){
            pullIntoRealityClient();
            return;
        }
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
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            if (this.activePower == PowerIndex.POWER_3_SNEAK) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_3_SNEAK);
            }
        }
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.D4C_MELT_DODGE, PowerIndex.SKILL_EXTRA);
        } else if (PowerTypes.isInD4CWorld(self)){
            LockedOrNot(context, x, y, 1, StandIcons.MERGING_RETURN, PowerIndex.NONE,0);
        } else if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 1, StandIcons.D4C_PARALLEL_RUNNING, PowerIndex.SKILL_1,0);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.D4C_PARALLEL_GRAB, PowerIndex.SKILL_1_SNEAK);
        }

        if (PowerTypes.isInD4CWorld(self)){
            LockedOrNot(context, x, y, 2, StandIcons.D4C_PARALLEL_GRAB_2, PowerIndex.SKILL_EXTRA_2,0);
        } else if (!isHoldingSneak()){
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
            LockedOrNot(context, x, y, 3, StandIcons.D4C_CHOP, PowerIndex.SKILL_3,0);
        }
        if (!isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP_2, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4);
        }
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        boolean dworld = PowerTypes.isInD4CWorld(self);
        if (!(slot == 2 && dworld)){
            if (slot == 1 || slot == 2 || slot == 4){
                if (slot == 1 && !isGuarding() && dworld){
                    return !isEligableForExit() || super.isAttackIneptVisually(activeP,slot);
                }
                return !isEligable() || super.isAttackIneptVisually(activeP,slot);
            }
        } else {
            Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(), getReach());
            if (targetEntity == null){
                return true;
            }
        }
        return super.isAttackIneptVisually(activeP,slot);
    }
    @Override
    public boolean interceptGuard(){
        return true;
    }
    @Override
    public boolean cancelSprintJump(){
        byte ap = this.getActivePower();
        if (ap == PowerIndex.SNEAK_ATTACK_CHARGE ||
                ap == PowerIndex.POWER_3_SNEAK ||
                ap == PowerIndex.POWER_1_SNEAK
        ){
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

    public void standDrag(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(grabRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,grabRange);
            isekaiTarget(targetEntity);
        }

    }
    public void updateDrag(){
        if (this.attackTimeDuring > -1) {
            if (!isEligable()) {
                if (!this.getSelf().level().isClientSide()) {
                    xTryPower(PowerIndex.NONE,true);
                    animateStand(StandEntity.IDLE);
                }
            }
            if (this.attackTimeDuring > 24) {
                this.standDrag();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    if(this.attackTimeDuring%4==0) {
                        sendParticlesIfPossible(self.level(),ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    public static boolean hasNoBlockCollision(
            Entity entity,
            AABB box
    ) {
        Level level = entity.level();

        // Check block collisions
        for (VoxelShape shape : level.getBlockCollisions(entity, box)) {
            if (!shape.isEmpty()) {
                return false;
            }
        }

        // Preserve vanilla world border collision
        VoxelShape border = borderCollision(level,entity, box);

        return border == null
                || !Shapes.joinIsNotEmpty(
                border,
                Shapes.create(box),
                BooleanOp.AND
        );
    }
    private static VoxelShape borderCollision(Level lvl, Entity $$0, AABB $$1) {
        WorldBorder $$2 = lvl.getWorldBorder();
        return $$2.isInsideCloseToBorder($$0, $$1) ? $$2.getCollisionShape() : null;
    }
    @Unique
    public static boolean ejectFromOGSpot(Entity self) {
        if (self == null){
            return false;
        }
        Vec3 fallback =
                ((IGravityEntity) self).rdbt$getExistPlaneStartPoint();
        if (fallback != null) {

            AABB fallbackBox = self.getBoundingBox()
                    .move(fallback.subtract(self.position()));

            if (hasNoBlockCollision(self, fallbackBox)) {

                if (self instanceof ServerPlayer player && player.level() instanceof ServerLevel sl) {
                    player.teleportTo(
                            sl,
                            fallback.x,
                            fallback.y,
                            fallback.z,
                            player.getYRot(),
                            player.getXRot()
                    );
                } else {
                    self.teleportTo(
                            fallback.x,
                            fallback.y,
                            fallback.z
                    );
                }

                return true;
            }
        }
        return false;
    }

    @Unique
    public static boolean ejectFromNearestEntity(Entity self) {
        if (self == null || !MainUtil.isActuallyALivingEntityNoCap(self)){
            return false;
        }
        Level level = self.level();

        double range = 10.0D;

        AABB searchBox = self.getBoundingBox().inflate(range);

        Entity target = level.getEntities(
                        self,
                        searchBox,
                        entity ->
                                ((entity instanceof Mob
                                        || entity instanceof Player) && PowerTypes.getPlaneOfExisting2(entity) == 0
                                && !(entity instanceof StandEntity))
                ).stream()
                .filter(entity -> entity != self)
                .filter(Entity::isAlive)
                .min(Comparator.comparingDouble(self::distanceToSqr))
                .orElse(null);

        if (target == null) {
            return false;
        }

        // Horizontal direction the target is facing.
        Vec3 forward = target.getLookAngle()
                .multiply(1.0D, 0.0D, 1.0D);

        if (forward.lengthSqr() < 0.001D) {
            return false;
        }

        forward = forward.normalize();

        // "Back" of the target.
        Vec3 backward = forward.scale(-1.0D);

        // Try several positions behind them.
        for (double distance = 0.8D; distance <= 2.5D; distance += 0.2D) {

            Vec3 candidatePos = new Vec3(
                    target.getX() + backward.x * distance,
                    target.getY(),
                    target.getZ() + backward.z * distance
            );

            // Keep the ejecting entity's current dimensions.
            AABB candidateBox = self.getBoundingBox()
                    .move(candidatePos.subtract(self.position()));

            // Check whether the entity can physically fit there.
            if (!hasNoBlockCollision(self, candidateBox)) {
                continue;
            }

            float yaw = target.getYRot() + 180.0F;

            if (self instanceof ServerPlayer player && player.level() instanceof ServerLevel sl) {

                player.teleportTo(sl,
                        candidatePos.x,
                        candidatePos.y,
                        candidatePos.z,
                        yaw,
                        player.getXRot()
                );

            } else {

                self.teleportTo(
                        candidatePos.x,
                        candidatePos.y,
                        candidatePos.z
                );

                self.setYRot(yaw);
                self.setYHeadRot(yaw);
                self.setYBodyRot(yaw);

                self.yRotO = yaw;
            }

            // Small push outward from the target.
            Vec3 push = backward.scale(0.15D);

            self.setDeltaMovement(
                    push.x,
                    0.08D,
                    push.z
            );
            self.hurtMarked = true;
            self.hasImpulse = true;

            // A few cloud particles.
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.CLOUD,
                        candidatePos.x,
                        candidatePos.y + self.getBbHeight() * 0.5D,
                        candidatePos.z,
                        4,
                        0.15D,
                        0.2D,
                        0.15D,
                        0.02D
                );
            }

            return true;
        }

        return false;
    }

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_3_SNEAK) {
            updateChop();
        } else if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            updateDrag();
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
                        sendParticlesIfPossible(self.level(),ModParticles.MENACING,
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
        } else if (this.getActivePower()==PowerIndex.POWER_1_SNEAK){
            if (this.getSelf().isCrouching()){
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1/f;
                basis *= g;
            }
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
        if (this.getActivePower() == PowerIndex.POWER_3_SNEAK){
            chopImpact(target);
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            finalAttackImpact(target);
        } else if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            isekaiTarget(target);
        }
    }
    public int dragTarget = 0;
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        }
        if (move == PowerIndex.SNEAK_ATTACK) {
            this.dragTarget = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    public void grabMobIntoWorld(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            Entity targetEntity = getTargetEntity(this.self,-1);
            if (targetEntity != null && !PowerTypes.isNativeToOurWorld(targetEntity)) {
                PowerTypes.setPlaneOfExisting(targetEntity, (byte) 0);
                this.setAttackTimeDuring(-5);
                this.setActivePower(PowerIndex.POWER_2_BONUS);
                playSoundsIfNearby(FUSE, 27, false);
                this.animateStand(D4CEntity.DRAG_2);
                this.poseStand(OffsetIndex.GUARD);
                if (self.level() instanceof ServerLevel sl){
                    Vector3f color = new Vector3f(0.97F, 1F, 0.3F);
                    sl.sendParticles(new DustParticleOptions(
                                    color,
                                    1.0F
                            ), targetEntity.getX(),
                            targetEntity.getY()+targetEntity.getEyeHeight(), targetEntity.getZ(),
                            20, 0.3, 0.3, 0.3, 0.3);
                }
            }
        }
    }
    @Override
    public SoundEvent getPunchLandSound(){
        return ModSounds.D4C_PUNCH_EVENT;
    }
    @Override
    public SoundEvent getPunchLandLastSound(){
        return ModSounds.D4C_PUNCH_2_EVENT;
    }
    @Override
    public float getPunchLandPitch(){
        return 1.0F - 0.05F * activePowerPhase;
    }
    @Override
    public float getPunchLandLastPitch(){
        return 1F;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.SNEAK_ATTACK_CHARGE) {
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.POWER_2_BONUS) {
            this.grabMobIntoWorld();
            return false;
        } else if (move == PowerIndex.POWER_3_SNEAK){
            return this.chopAttack();
        } else if (move == PowerIndex.POWER_2) {
            spawnCloneServer();
            return false;
        } else if (move == PowerIndex.POWER_1_SNEAK){
            standDragServer();
            return false;
        }else if (move == PowerIndex.POWER_1){
            worldMergingServer();
            return false;
        } else if (move == PowerIndex.POWER_1_BONUS){

            exitWorldServer();
            return false;
        }
        return super.setPowerOther(move,lastMove);
    }

    public void standDragServer(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            if (isEligable()) {
                this.setAttackTimeDuring(0);
                this.setActivePower(PowerIndex.POWER_1_SNEAK);
                this.animateStand(D4CEntity.DRAG);
                this.poseStand(OffsetIndex.GUARD);
            }
        }
    }
    public void exitWorldServer(){
        if (isEligableForExit()){
            PowerTypes.setPlaneOfExisting(self,(byte)0);
        }
    }


    public boolean chopAttack(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){

            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_3_SNEAK);
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
        } else if (this.getActivePower() == PowerIndex.POWER_3_SNEAK){
            int cdr = 25;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_3, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_3, cdr);
            return true;
        }
        return super.canInterruptPower(sauce,interrupter);
    }
    public float getChopKnockback(){
        return 1F;
    }

    public void chopImpact(Entity entity){
        if (activePower == PowerIndex.POWER_3_SNEAK){
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
                }
            }

            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_3, 40);
            }
            this.setCooldown(PowerIndex.SKILL_3, 40);
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
                playSoundIfPossible(self.level(),null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
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
                sendParticlesIfPossible(self.level(),ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
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
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
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
    public static final float grabRange = 3.5F;

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        byte active = this.getActivePower();
        if (standOn && active == PowerIndex.POWER_3_SNEAK) {
            Entity TE = this.getTargetEntity(playerEntity, chopRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && active == PowerIndex.POWER_1_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, grabRange);
            if (TE != null && !MainUtil.isBossMob(TE)) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && active == PowerIndex.SNEAK_ATTACK_CHARGE){
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
