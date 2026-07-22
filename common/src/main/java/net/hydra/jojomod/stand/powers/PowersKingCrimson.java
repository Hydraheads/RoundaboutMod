package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.TimeSkipSnapshot;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.*;

public class PowersKingCrimson extends BlockGrabPreset {

    public PowersKingCrimson(LivingEntity self) {
        super(self);
    }
    public final Map<Integer, TimeSkipSnapshot> epitaph = new HashMap<>();
    public final Map<Integer, TimeSkipSnapshot> skip_dump = new HashMap<>();

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().theWorldSettings.enableTheWorld;
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKingCrimson(entity);
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_KING_CRIMSON_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == EPITAPH_NOISE) {
            return ModSounds.EPITAPH_ACTIVATE_EVENT;
        } else if (soundChoice == EPITAPH_FADE_NOISE) {
            return ModSounds.EPITAPH_FADE_EVENT;
        } else if (soundChoice == TIME_SKIP_1) {
            return ModSounds.SKIP_TIME_1_EVENT;
        } else if (soundChoice == TIME_SKIP_2) {
            return ModSounds.SKIP_TIME_2_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    public static final byte EPITAPH_NOISE = 106;
    public static final byte EPITAPH_FADE_NOISE = 107;
    public static final byte TIME_SKIP_1 = 108;
    public static final byte TIME_SKIP_2 = 109;
    @Override
    public SoundEvent getImpaleSound(){
        return ModSounds.KING_CRIMSON_IMPALE_EVENT;

    }

    public boolean isUsingEpitaph(){
        return !epitaph.isEmpty();
    }


    public static Vec3 getPredictedDirection() {
        return new Vec3(Math.random()*1-0.5F,0,Math.random()*1-0.5F);
    }
    public static Vec3 predictIdle(LivingEntity liv, int ticks) {
        //Mobs and Players that are still still need to move when idle
        Level level = liv.level();

        Vec3 predicted = liv.position();
        AABB box = liv.getBoundingBox();

        if (liv instanceof Creeper creeper && creeper.getSwelling(1) > 0){
            return predicted;
        }
        float speed = (float) (Math.random()*0.9F);
        if (liv instanceof WanderingTrader){
            speed = (float) (Math.random()*0.3F);
        }
        float sped = liv.getSpeed();
        Vec3 basevelocity = getPredictedDirection()
                .normalize()
                .scale(sped * speed);
        if (basevelocity.y > 0)
            basevelocity = basevelocity.multiply(1, 0, 1);
        for (int i = 0; i < ticks; i++) {

            Vec3 velocity = basevelocity;
            BlockPos ft = BlockPos.containing(predicted);
            if (!liv.isInWater() && !MainUtil.inWater(level.getBlockState(ft))) {
                velocity = velocity.add(0, -1, 0);
            } else {
                velocity.multiply(1,0,1);
            }

            // ----- Normal collision -----
            Vec3 collided = Entity.collideBoundingBox(
                    liv,
                    velocity,
                    box,
                    level,
                    List.of()
            );
            Vec3 nextPos = predicted.add(collided);
            BlockPos feet = BlockPos.containing(nextPos);
            BlockPos below = feet.below();
            BlockState ground = level.getBlockState(below);
            BlockState ground2 = level.getBlockState(feet);

            if (!ground.blocksMotion()) {
                // Don't move there
                break;
            }
            if (MainUtil.isDangerous(liv.level(), feet,ground2)){
                return predicted;
            }

            predicted = predicted.add(collided);
            box = box.move(collided);
        }

        return predicted;
    }

    public void releaseTimeSkip(){

    }

    public Vec3 predictPlayer(LivingEntity player, int ticks) {
        Level level = player.level();

        Vec3 predicted = player.position();
        AABB box = player.getBoundingBox();

        Deque<Vec3> history = null;
        if (player instanceof Player ye){
            history = ((IPlayerEntity) ye).rdbt$getMovementHistory();
        } else if (player.getControllingPassenger() instanceof Player ye){
            history = ((IPlayerEntity) ye).rdbt$getMovementHistory();
        }

        Vec3 oldPos = player.position();

        if (history != null && history.size() >= 2) {
            Iterator<Vec3> it = history.descendingIterator();

            Vec3 newest = it.next();
            Vec3 previous = it.hasNext() ? it.next() : newest;
            Vec3 third = it.hasNext() ? it.next() : previous;

            oldPos = third;
        }
        if (player.position().distanceTo(oldPos) < 0.1 && player.getId() != self.getId()){
            return predictIdle(player,ticks);
        }
        Vec3 baseVelocity = player.position()
                .subtract(oldPos)
                .normalize()
                .scale(player.getSpeed() * (2.5+(Math.random()*0.5)));
        if (baseVelocity.y > 0)
            baseVelocity = baseVelocity.multiply(1, 0, 1);

        for (int i = 0; i < ticks; i++) {

            // ----- Estimate movement direction -----



            Vec3 velocity = baseVelocity;

            BlockPos ft = BlockPos.containing(predicted);
            if (!player.isInWater() && !player.isFallFlying() && !MainUtil.inWater(level.getBlockState(ft))
            && !(player instanceof Player pl2 && pl2.getAbilities().flying)) {
                velocity = velocity.add(0, -1, 0);
            }  else {
                velocity.multiply(1,0,1);
            }

            // ----- Normal collision -----
            Vec3 collided = Entity.collideBoundingBox(
                    player,
                    velocity,
                    box,
                    level,
                    List.of()
            );

            // ----- Try stepping up -----
            boolean hitWall =
                    collided.x != velocity.x ||
                            collided.z != velocity.z;

            if (hitWall) {
                i+=3;
                double stepHeight = 1.0;

                // Move upward first
                Vec3 up = Entity.collideBoundingBox(
                        player,
                        new Vec3(0, stepHeight, 0),
                        box,
                        level,
                        List.of()
                );

                AABB steppedBox = box.move(up);

                // Move horizontally while elevated
                Vec3 forward = Entity.collideBoundingBox(
                        player,
                        new Vec3(velocity.x, 0, velocity.z),
                        steppedBox,
                        level,
                        List.of()
                );

                steppedBox = steppedBox.move(forward);

                // Move back down
                Vec3 down = Entity.collideBoundingBox(
                        player,
                        new Vec3(0, -stepHeight, 0),
                        steppedBox,
                        level,
                        List.of()
                );

                Vec3 steppedMove = up.add(forward).add(down);

                // Prefer whichever gives more horizontal travel
                if (forward.horizontalDistanceSqr() > collided.horizontalDistanceSqr()) {
                    collided = steppedMove;
                }
                if (collided.y <= 0){
                    hitWall2 = true;
                }
            }

            predicted = predicted.add(collided);
            box = box.move(collided);
        }

        boolean deviousStratBlocker = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindStopsDeviousStrategies;

        if (deviousStratBlocker) {
            // 2. Check for dangerous blocks inside target box
            boolean cancel = false;
            double width = player.getBbWidth();
            double height = player.getBbHeight();
            AABB targetBox = new AABB(
                    predicted.x - width / 2.0, predicted.y, predicted.z - width / 2.0,
                    predicted.x + width / 2.0, predicted.y + height, predicted.z + width / 2.0
            );
            targetBox = RotationUtil.boxPlayerToWorld(targetBox,((IGravityEntity)player).roundabout$getGravityDirection());

            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(targetBox.minX), Mth.floor(targetBox.minY), Mth.floor(targetBox.minZ),
                    Mth.floor(targetBox.maxX), Mth.floor(targetBox.maxY), Mth.floor(targetBox.maxZ))) {

                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                // List of bad blocks to avoid
                if (block == Blocks.COBWEB || block == Blocks.LAVA) {
                    cancel = true;
                    break;
                }

                // Optional: also avoid fire or cactus
                if (block == Blocks.FIRE || block == Blocks.CACTUS) {
                    cancel = true;
                    break;
                }
            }
            if (cancel){
                return player.position();
            }
        }

        return predicted;
    }
    public Vec3 predictPosition(Mob mob, int ticks) {
        Path path = mob.getNavigation().getPath();

        if (path == null) {
            return predictIdle(mob,ticks);
        }

        double remaining = mob.getSpeed() * ticks;
        Vec3 current = mob.position();

        int index = path.getNextNodeIndex();

        while (index < path.getNodeCount()) {
            Node node = path.getNode(index);

            Vec3 next = new Vec3(
                    node.x + 0.5,
                    node.y,
                    node.z + 0.5
            );

            double segment = current.distanceTo(next);

            if (remaining <= segment) {
                return current.lerp(next, remaining / segment);
            }

            remaining -= segment;
            current = next;
            index++;
        }

        if (current.distanceTo(mob.position()) < 0.01 && !MainUtil.isBossMob(mob)
        && !(mob instanceof FlyingMob)){
            return predictIdle(mob,ticks);
        }

        return current;
    }

    public void debugPlayer(){
        if (self instanceof Player pl) {
            int id = self.getId();
            float xRot = self.getXRot();
            float yRot = self.getYRot();
            Vec3 predicted = self.position();

            hitWall2 = false;
            predicted = predictPlayer(pl, 40);
            if (hitWall2){
                yRot = Mth.wrapDegrees(yRot + 180.0F);
            }
            epitaph.put(self.getId(), new TimeSkipSnapshot(
                    id,
                    predicted,
                    xRot,
                    yRot
            ));
            S2CPacketUtil.addEpitaph(pl, id, predicted, xRot, yRot);
        }
    }

    //This variable makes a player turn around when they hit a wall to sell a believable reaction
    public boolean hitWall2 = false;

    public void basicSkip(boolean skipSelf){
        AABB area = self.getBoundingBox().inflate(getSkipRange());

        for (Entity entity : self.level().getEntitiesOfClass(Entity.class, area)) {
            if (entity instanceof Projectile proj) {
                skip_dump.put(proj.getId(), new TimeSkipSnapshot(
                        proj.getId(),
                        predictProjectile(proj, 100),
                        proj.getXRot(),
                        proj.getYRot()
                ));
            } else if (entity instanceof PrimedTnt pt){
                pt.setFuse(1);
            } if (entity instanceof LivingEntity living) {
                if (living.getControllingPassenger() instanceof Player){
                    continue;
                }
                if (!skipSelf && living.getId() == self.getId()) {
                    continue;
                } else if (living instanceof StandEntity) {
                    continue;
                }
                StandEntity stand = getStandEntity(self);
                int id = living.getId();
                if (!(stand != null && stand.getId() == id)) {
                    if (!(living instanceof StandEntity) &&
                            !(living instanceof Player pk && pk.isCreative()
                                    && pk.getId() != self.getId())
                    ) {
                        Vec3 predicted = living.position();
                        float xRot = living.getXRot();
                        float yRot = living.getYRot();
                        if (!living.isSleeping()) {
                            if (living instanceof Mob mob) {
                                if (!mob.isLeashed()) {
                                    predicted = predictPosition(mob, 100);
                                }
                            } else if (living instanceof Player player) {
                                // Fallback for players, armor stands, etc.
                                hitWall2 = false;
                                predicted = predictPlayer(player, 40);
                                if (player.getId() == self.getId()) {
                                    if (predicted.distanceTo(self.getPosition(1)) < 0.1) {
                                        continue;
                                    }
                                }
                                if (hitWall2 && player.getId() != self.getId()) {
                                    yRot = Mth.wrapDegrees(yRot + 180.0F);
                                }
                            }
                        }


                        skip_dump.put(living.getId(), new TimeSkipSnapshot(
                                id,
                                predicted,
                                xRot,
                                yRot
                        ));
                    }
                }
            }
        }

        playStandUserOnlySoundsIfNearby(TIME_SKIP_2, 75, true, false);
        scatterPackets();
        if (skip_dump.isEmpty()){
            return;
        }
        for (TimeSkipSnapshot snapshot : skip_dump.values()) {
            skipSingle(snapshot);
        }
        skip_dump.clear();

    }

    public void skipSingle(TimeSkipSnapshot snapshot){

        if (snapshot.getEntityId() == -1) {
            return;
        }
        Level level = self.level();

        Entity entity = level.getEntity(snapshot.getEntityId());

        if (entity == null || !entity.isAlive()) {
            return;
        }
        if (entity instanceof StandEntity) {
            return;
        }
        if (entity.isPassenger()){
            return;
        }
        if (entity.getControllingPassenger() instanceof Player){
            return;
        }

        if (entity instanceof LivingEntity LE) {
            if (LE instanceof Creeper creeper && creeper.getSwelling(1) > 0){
                creeper.setSwellDir(30);
            }
            double width = entity.getBbWidth();
            double height = entity.getBbHeight();
            // Construct bounding box at the target position
            AABB targetBox = new AABB(
                    snapshot.position.x - width / 2.0, snapshot.position.y, snapshot.position.z - width / 2.0,
                    snapshot.position.x + width / 2.0, snapshot.position.y + height, snapshot.position.z + width / 2.0
            );
            targetBox = RotationUtil.boxPlayerToWorld(targetBox, ((IGravityEntity) entity).roundabout$getGravityDirection());

            if (!level.noCollision(entity, targetBox)) {
                return;
            }

            boolean deviousStratBlocker = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindStopsDeviousStrategies;

            if (deviousStratBlocker && entity instanceof Player) {
                // 2. Check for dangerous blocks inside target box
                boolean cancel = false;
                for (BlockPos pos : BlockPos.betweenClosed(
                        Mth.floor(targetBox.minX), Mth.floor(targetBox.minY), Mth.floor(targetBox.minZ),
                        Mth.floor(targetBox.maxX), Mth.floor(targetBox.maxY), Mth.floor(targetBox.maxZ))) {

                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();

                    // List of bad blocks to avoid
                    if (block == Blocks.COBWEB || block == Blocks.LAVA) {
                        cancel = true;
                        break;
                    }

                    // Optional: also avoid fire or cactus
                    if (block == Blocks.FIRE || block == Blocks.CACTUS) {
                        cancel = true;
                        break;
                    }
                }
                if (cancel) {
                    return;
                }
            }
        }

        packetNearby(new Vector3f((float) snapshot.position.x,
                        (float) snapshot.position.y,
                        (float) snapshot.position.z),
                entity.getId());

        entity.teleportTo(
                snapshot.position.x,
                snapshot.position.y,
                snapshot.position.z
        );
        entity.setYRot(snapshot.yRot);
        entity.setYHeadRot(snapshot.yRot);
        entity.teleportTo(((ServerLevel) entity.level()),snapshot.position.x,
                snapshot.position.y,
                snapshot.position.z,
                Set.of(
                        RelativeMovement.X,
                        RelativeMovement.Y,
                        RelativeMovement.Z),
                snapshot.yRot,entity.getXRot());
        if (entity instanceof Mob mb && !MainUtil.isBossMob(mb)){
            mb.getNavigation().stop();
            ((IMob)mb).roundabout$setConfusionTicks(7);
        }
        if (!entity.getPassengers().isEmpty()) {
            for (Entity passenger : entity.getPassengers()) {
                entity.positionRider(passenger);
            }
        }
    }



    public static Vec3 predictProjectile(Projectile projectile, int ticks) {
        Level level = projectile.level();

        Vec3 pos = projectile.position();
        Vec3 velocity = projectile.getDeltaMovement();

        for (int i = 0; i < ticks; i++) {

            Vec3 nextPos = pos.add(velocity);

            // Ignore entities, collide only with blocks.
            BlockHitResult hit = level.clip(new ClipContext(
                    pos,
                    nextPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    projectile
            ));

            if (hit.getType() == HitResult.Type.BLOCK) {
                return pos;
                //return hit.getLocation();
            }

            pos = nextPos;

            // Vanilla-style drag
            velocity = velocity.scale(0.99);

            // Vanilla gravity
            if (!projectile.isNoGravity()) {
                if (!(projectile instanceof AbstractArrow aa && ((ISuperThrownAbstractArrow)aa).roundabout$getSuperThrow())){
                    float gravity = -0.05F;
                    if (projectile instanceof ThrowableProjectile aa){
                        gravity =  -1*((AccessThrowableProjectile)aa).rdbt$getGravity();
                    }

                    velocity = velocity.add(0.0, gravity, 0.0);
                }
            }
        }

        return pos;
    }

    public void timeSkip(boolean skipSelf) {
        if (!(self instanceof ServerPlayer pl)) {
            return;
        }

        if (epitaph.isEmpty()) {
            basicSkip(skipSelf);
            return;
        }

        AABB area = self.getBoundingBox().inflate(getSkipRange());
        for (Entity entity : self.level().getEntitiesOfClass(Entity.class, area)) {
            if (entity instanceof Projectile proj){
                epitaph.put(proj.getId(), new TimeSkipSnapshot(
                        proj.getId(),
                        predictProjectile(proj,100),
                        proj.getXRot(),
                        proj.getYRot()
                ));
            } else if (entity instanceof PrimedTnt pt){
                pt.setFuse(1);
            }
        }
        for (TimeSkipSnapshot snapshot : epitaph.values()) {
            if (!skipSelf && snapshot.getEntityId() == self.getId()){
                continue;
            }
            skipSingle(snapshot);
        }


        S2CPacketUtil.sendCancelSoundPacket(pl,this.self.getId(),EPITAPH_NOISE);
        playStandUserOnlySoundsIfNearby(TIME_SKIP_1, 75, true, false);
        scatterPackets();
        epitaph.clear();
        S2CPacketUtil.clearEpitaph(pl);
    }

    public void scatterPackets(){
        packetNearby2();
    }
    public int getSkipRange(){
        return 50;
    }
    public final void packetNearby2() {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 75)) {
                    S2CPacketUtil.sendSimpleByteToClientPacket(serverPlayerEntity,PacketDataIndex.TIME_SKIP);
                }
            }
        }
    }
    public final void packetNearby(Vector3f blip, int entId) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    S2CPacketUtil.sendBlipPacket(serverPlayerEntity, (byte) 2, entId,blip);
                }
            }
        }
    }
    public void epitaph() {
        if (self instanceof ServerPlayer pl) {
            if (epitaph.isEmpty()) {
                //debugPlayer();
                AABB area = self.getBoundingBox().inflate(getSkipRange());

                for (LivingEntity living : self.level().getEntitiesOfClass(LivingEntity.class, area)) {
                    StandEntity stand = getStandEntity(self);
                    int id = living.getId();
                    if (!(stand != null && stand.getId() == id)){
                        if (!(living instanceof StandEntity) &&
                                !(living instanceof Player pk && pk.isCreative()
                                        && pk.getId() != self.getId())
                        ) {
                            Vec3 predicted = living.position();
                            float xRot = living.getXRot();
                            float yRot = living.getYRot();
                            if (!living.isSleeping()){
                                if (living instanceof Mob mob) {
                                    if (!mob.isLeashed()) {
                                        predicted = predictPosition(mob, 100);
                                    }
                                } else if (living instanceof Player player) {
                                    // Fallback for players, armor stands, etc.
                                    hitWall2 = false;
                                    predicted = predictPlayer(player, 40);
                                    if (hitWall2) {
                                        yRot = Mth.wrapDegrees(yRot + 180.0F);
                                    }
                                }
                            }


                            epitaph.put(living.getId(), new TimeSkipSnapshot(
                                    id,
                                    predicted,
                                    xRot,
                                    yRot
                            ));
                            S2CPacketUtil.addEpitaph(pl, id, predicted, xRot, yRot);
                        }
                    }

                }
                epitaph.put(-1, new TimeSkipSnapshot(
                        -1,
                        Vec3.ZERO,
                        0,
                        0
                ));
                S2CPacketUtil.addEpitaph(pl, -1,  Vec3.ZERO, 0, 0);
                S2CPacketUtil.sendPlaySoundPacket(pl,this.self.getId(),EPITAPH_NOISE);
                S2CPacketUtil.sendCancelSoundPacket(pl,this.self.getId(),EPITAPH_FADE_NOISE);
            } else {
                S2CPacketUtil.sendPlaySoundPacket(pl,this.self.getId(),EPITAPH_FADE_NOISE);
                S2CPacketUtil.sendCancelSoundPacket(pl,this.self.getId(),EPITAPH_NOISE);
                epitaph.clear();
                S2CPacketUtil.clearEpitaph(pl);
            }

            //Roundabout.LOGGER.info("Captured {} entities", epitaph.size());
        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.theWorldAttackMultOnPlayers *0.01));
    }
    @Override
    public float getImpalePunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (4F * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs((float) (20.1F * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        }
    }
        @Override
    public StandEntity getNewStandEntity() {
        byte sk = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        return ModEntities.KING_CRIMSON.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == KingCrimsonEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.king_crimson.manga");
        } if (skinId == KingCrimsonEntity.END){
            return Component.translatable(  "skins.roundabout.king_crimson.end");
        } if (skinId == KingCrimsonEntity.END_2){
            return Component.translatable(  "skins.roundabout.king_crimson.end_2");
        } if (skinId == KingCrimsonEntity.STARLESS){
            return Component.translatable(  "skins.roundabout.king_crimson.starless");
        } if (skinId == KingCrimsonEntity.HEAVEN){
            return Component.translatable(  "skins.roundabout.king_crimson.heaven");
        }if (skinId == KingCrimsonEntity.AGOGO){
            return Component.translatable(  "skins.roundabout.king_crimson.agogo");
        }if (skinId == KingCrimsonEntity.SPINE_ART){
            return Component.translatable(  "skins.roundabout.king_crimson.spine_art");
        }if (skinId == KingCrimsonEntity.GREEN){
            return Component.translatable(  "skins.roundabout.king_crimson.green");
        }if (skinId == KingCrimsonEntity.YELLOW){
            return Component.translatable(  "skins.roundabout.king_crimson.yellow");
        }if (skinId == KingCrimsonEntity.AQUA){
            return Component.translatable(  "skins.roundabout.king_crimson.aqua");
        }if (skinId == KingCrimsonEntity.BLACK){
            return Component.translatable(  "skins.roundabout.king_crimson.black");
        }if (skinId == KingCrimsonEntity.DARK){
            return Component.translatable(  "skins.roundabout.king_crimson.dark");
        }if (skinId == KingCrimsonEntity.BETA){
            return Component.translatable(  "skins.roundabout.king_crimson.beta");
        }if (skinId == KingCrimsonEntity.CONCEPT){
            return Component.translatable(  "skins.roundabout.king_crimson.concept");
        }if (skinId == KingCrimsonEntity.PART_5_SKIN){
            return Component.translatable(  "skins.roundabout.king_crimson.base");
        }if (skinId == KingCrimsonEntity.BLUE){
            return Component.translatable(  "skins.roundabout.king_crimson.blue");
        }if (skinId == KingCrimsonEntity.VISION){
            return Component.translatable(  "skins.roundabout.king_crimson.vision");
        }
        return Component.translatable(  "skins.roundabout.king_crimson.red");
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK
                || this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            return true;
        }
        return super.cancelSprintJump();
    }
    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            int cdr = ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, cdr);
            return true;
        }
        return super.canInterruptPower(sauce,interrupter);
    }

        @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(KingCrimsonEntity.RED);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);

            $$1.add(KingCrimsonEntity.PART_5_SKIN);
            $$1.add(KingCrimsonEntity.MANGA_SKIN);
            if (Level > 1 || bypass) {
                $$1.add(KingCrimsonEntity.VISION);
                $$1.add(KingCrimsonEntity.SPINE_ART);
                $$1.add(KingCrimsonEntity.AGOGO);
            } if (Level > 2 || bypass) {
                $$1.add(KingCrimsonEntity.BLUE);
                $$1.add(KingCrimsonEntity.BLACK);
                $$1.add(KingCrimsonEntity.DARK);
            } if (Level > 3 || bypass) {
                $$1.add(KingCrimsonEntity.HEAVEN);
                $$1.add(KingCrimsonEntity.AQUA);
                $$1.add(KingCrimsonEntity.YELLOW);
                $$1.add(KingCrimsonEntity.GREEN);
            } if (Level > 4 || bypass) {
                $$1.add(KingCrimsonEntity.STARLESS);
                $$1.add(KingCrimsonEntity.CONCEPT);
                $$1.add(KingCrimsonEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(KingCrimsonEntity.END);
                $$1.add(KingCrimsonEntity.END_2);
            }
        }
        return $$1;
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL-> {
                epitaphClient();
            }
            case SKILL_1_CROUCH -> {
                impaleClient();
            }

            case SKILL_2_NORMAL -> {
                timeSkipClient();
            }
            case SKILL_2_GUARD -> {
                timeSkipSelfClient();
            }
            case SKILL_2_CROUCH -> {
                itemGrabClient();
            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
        }
    }

    @Override
    public boolean isAppropriateToGrab(){
        if (!hasBlock()) {
            return true;
        }
        return false;
    }
    public void timeSkipSelfClient() {
        if (hasBlock()){
            return;
        }
        if (isUsingEpitaph()){
            tryPowerPacket(PowerIndex.EXTRA);
        }
    }
    public void timeSkipClient() {
        if (hasBlock()){
            itemGrabClient();
            return;
        }

        boolean isMoving = (Math.abs(self.getDeltaMovement().x) > 0.01 ||
                Math.abs(self.getDeltaMovement().z) > 0.01 ||
                !self.onGround());
        if (isMoving && !isUsingEpitaph()){
            tryPowerPacket(PowerIndex.EXTRA);
        } else {
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }


    public void epitaphClient(){

        if (hasBlock())
            return;
        tryPowerPacket(PowerIndex.POWER_1);
    }

    public void tryToDashClient(){
        if (hasBlock())
            return;
        if (!doVault()) {
            dash();
        }
    }


    public int getImpaleLevel(){
        return 1;
    }
    public void impaleClient(){
        if (!canImpale()){
            return;
        }

        if (hasBlock())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            if (canExecuteMoveWithLevel(getImpaleLevel())) {
                if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                } else {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                    tryPowerPacket(PowerIndex.POWER_1_SNEAK);
                }
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 1, StandIcons.KING_CRIMSON_EPITAPH, PowerIndex.SKILL_1, 0);
        } else {
            LockedOrNot(context, x, y, 1, StandIcons.KING_CRIMSON_IMAPLE, PowerIndex.SKILL_1_SNEAK,getImpaleLevel());
        }

        if (!isHoldingSneak()){
            if (hasBlock()){
                LockedOrNot(context, x, y, 2, StandIcons.KING_CRIMSON_ITEM_GRAB, PowerIndex.SKILL_2,getImpaleLevel());

            } else if (isUsingEpitaph()){
                if (isGuarding()){
                    LockedOrNot(context, x, y, 2, StandIcons.TIME_SKIP_3, PowerIndex.SKILL_2_SNEAK, 0);
                } else {
                    LockedOrNot(context, x, y, 2, StandIcons.TIME_SKIP_2, PowerIndex.SKILL_2_SNEAK, 0);
                }
            } else {
                LockedOrNot(context, x, y, 2, StandIcons.TIME_SKIP, PowerIndex.SKILL_2_SNEAK, 0);
            }
        } else {
            LockedOrNot(context, x, y, 2, StandIcons.KING_CRIMSON_ITEM_GRAB, PowerIndex.SKILL_2,getImpaleLevel());
        }

        if (canVault()){
            setSkillIcon(context, x, y, 3, StandIcons.KING_CRIMSON_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
        } else {
            if (!isHoldingSneak()){
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3);
            }
        }
        if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 4, StandIcons.TIME_ERASE, PowerIndex.SKILL_4, 0);
        } else {
            LockedOrNot(context, x, y, 4, StandIcons.TIME_ERASE, PowerIndex.SKILL_4_SNEAK,getImpaleLevel());
        }
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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, impaleRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            float zamn = ((float) attackTimeDuring / getMaxSuperHitTime());
            int ClashTime = Math.min(15,Math.round(zamn * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 111, 15, 6);
            if (zamn >= 1){
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 132, ClashTime, 6);
            } else if (crossedThreshold2(zamn)){
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 118, ClashTime, 6);
            } else {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 125, ClashTime, 6);
            }
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }
    public boolean crossedThreshold(){
        float zamn = ((float) attackTimeDuring / getMaxSuperHitTime());
        return crossedThreshold2(zamn);
    }
    public boolean crossedThreshold2(float zamn){
        return zamn >= 0.5F;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100,true);
        }
        return super.tryPower(move,forced);
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
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK,atd);
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
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            updateImpale();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateFinalAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateFinalAttackCharge();
        }
        super.updateUniqueMoves();
    }

    public int chargedFinal;

    public void updateImpale(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
                this.standImpale();
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

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.SNEAK_ATTACK) {
                this.chargedFinal = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_1_SNEAK){
            return this.impale();
        } else if (move == PowerIndex.POWER_1){
            this.epitaph();
        } else if (move == PowerIndex.POWER_2){
            this.timeSkip(false);
            return true;
        } else if (move == PowerIndex.EXTRA){
            this.timeSkip(true);
            return true;
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerSuperHit();
        }
        return super.setPowerOther(move,lastMove);
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
    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            impaleImpact(target);
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            finalAttackImpact(target);
        }
    }
    public void animateFinalAttack(){
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
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

    public boolean setPowerFinalAttack() {
        animateFinalAttack();
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        this.clashDone = false;
        return true;
    }
    public static final float impaleRange = 3.5F;
    public void standImpale(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(impaleRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,impaleRange);
            impaleImpact(targetEntity);
        }

    }

    public void updateFinalAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.standFinalAttack();
            }
        }
    }

    public void standFinalAttack(){

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown + chargedFinal);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId());
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            finalAttackImpact(targetEntity);
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
                        MainUtil.makeBleed(LE, 0, 200, this.self);
                        addEXP(2, LE);
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
        if (charged >= 1F){
            return ModSounds.KING_CRIMSON_PUNCH_5_EVENT;
        } else if (charged >= 0.5F){
            return ModSounds.KING_CRIMSON_PUNCH_4_EVENT;
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

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (hasBlock()){
            return true;
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public byte getThrowStyleType(){
        return ThrownObjectEntity.TWTHROW;
    }

    public float getFinalAttackKnockback(){
        float charge = getChargedPercent();
        if (charge >= 1){
            return (((float)this.chargedFinal /(float)getMaxSuperHitTime())*3);
        } else if (charge >= 0.5F){
            return 0.7F;
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
            float ret = (getChargedPercent()*punchD)+3;
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=2;
            }
            return ret;
        }
    }


    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.35F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.89F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }

    public float getChargedPercent(){
        return (((float)this.chargedFinal/(float)getMaxSuperHitTime()));
    }

    public int getMaxSuperHitTime(){
        return 30+(getMeltLevel()*2);
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
    public float getPunchLandPitch(){
        return 1.3F + 0.07F * activePowerPhase;
    }
    @Override
    public float getPunchLandLastPitch(){
        return 1F;
    }

    @Override
    public SoundEvent getPunchLandSound(){
        return ModSounds.KING_CRIMSON_PUNCH_EVENT;
    }
    @Override
    public SoundEvent getPunchLandLastSound(){
        return ModSounds.KING_CRIMSON_PUNCH_2_EVENT;
    }

}