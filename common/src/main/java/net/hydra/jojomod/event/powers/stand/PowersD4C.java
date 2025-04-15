package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PowersD4C extends PunchingStand {
    public PowersD4C(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersD4C(entity);
    }


    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().guardPoints.d4cDefend;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.D4C.create(this.getSelf().level());
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 1, StandIcons.D4C_BETWEEN_VISION, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.D4C_DIMENSION_KIDNAP, PowerIndex.SKILL_1_SNEAK);

        if (!isHoldingSneak() && !isGuarding())
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_SUMMON, PowerIndex.SKILL_2);
        else if (!isGuarding()) {
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_SWAP, PowerIndex.SKILL_2_SNEAK);
        }
        else
        {
            setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_EXTRA_2);
        }

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        else
            setSkillIcon(context, x, y, 3, StandIcons.D4C_MELT_DODGE, PowerIndex.SKILL_3);

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4);
        else
            setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4_SNEAK);
    }

    private boolean held1 = false;
    public boolean betweenVision = false;
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (betweenVision)
            highlightBlocksInFrustum(1500, 100);

        if (keyIsDown && !held1)
        {
            held1 = true;

            if (this.onCooldown(PowerIndex.SKILL_1))
                return;

            betweenVision = !betweenVision;
            this.setCooldown(PowerIndex.SKILL_1, 20);
        } else if (!keyIsDown) {
            held1 = false;
        }
    }

    private int betweenVisionTicks = 0;
    private void highlightBlocksInFrustum(int angle, int offset)
    {
        betweenVisionTicks++;

        if (betweenVisionTicks < 10)
            return;

        betweenVisionTicks = 0;

        for (int pitch = -angle; pitch < angle; pitch += offset)
        {
            for (int yaw = -angle; yaw < angle; yaw += offset)
            {
                BlockPos state = ClientUtil.raycastForBlockGivenAngle(Math.toRadians(pitch), Math.toRadians(yaw), 100);
                if (state == null)
                    continue;

                if (!isBetweenTwoThings(state.above()))
                    continue;

                this.getSelf().level().addParticle(
                        ModParticles.MENACING,
                        true,
                        state.getX()+0.5f,
                        state.getY()+1.0f,
                        state.getZ()+0.5f,
                        0.0f,
                        (Math.random()*0.04f)-0.02f,
                        0.0f
                );
            }
        }
    }

    private boolean held2 = false;
    @Nullable
    public D4CCloneEntity targetingClone = null;
    @Nullable public AABB miningBox = null;

    @Nullable private BlockPos pos1 = null;

    @Override
    public void buttonInput2(boolean keyIsDown, Options options) {
        if (isHoldingSneak() && !isGuarding())
        {
            if (MainUtil.getTargetEntity(this.getSelf(), 100, 2) instanceof D4CCloneEntity clone)
            {
                targetingClone = clone;
            }
        }
        else { targetingClone = null; }

        if (keyIsDown && !held2)
        {
            held2 = true;

            if (isGuarding())
            {
                if (pos1 == null && miningBox != null)
                {
                    miningBox = null;
                    return;
                }

                if (miningBox == null)
                {
                    BlockHitResult result = getGrabBlock();
                    if (pos1 == null)
                    {
                        if (result != null) {
                            pos1 = result.getBlockPos();
                        }
                    }
                    else
                    {
                        if (result != null)
                        {
                            miningBox = new AABB(pos1, result.getBlockPos());
                            pos1 = null;
                        }
                    }
                }

                return;
            }

            if (!isHoldingSneak() && !(this.onCooldown(PowerIndex.SKILL_2)))
            {
                if (isBetweenTwoThings())
                {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2);
                    this.setCooldown(PowerIndex.SKILL_2, 80);
                }
            }
            else
            {
                if (!(this.onCooldown(PowerIndex.SKILL_2_SNEAK)))
                {
                    //Entity TE = MainUtil.getTargetEntity(this.getSelf(), 100, 10);
                    if (targetingClone != null)
                    {
                        if (targetingClone.player != this.getSelf())
                            return;

                        // there isnt a data index for what i want and im too lazy to add one so here, update move, it probably fits
                        ModPacketHandler.PACKET_ACCESS.intToServerPacket(targetingClone.getId(), PacketDataIndex.INT_UPDATE_MOVE);

                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_2_SNEAK);

                        this.setCooldown(PowerIndex.SKILL_2_SNEAK, 40);
                        this.targetingClone = null;
                    }
                }
            }
        }
        else if (!keyIsDown)
        {
            held2 = false;
        }
    }

    private BlockHitResult getGrabBlock(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 5, vec3d2.y * 5, vec3d2.z * 5);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (blockHit.getType() == HitResult.Type.BLOCK){
            return blockHit;
        }
        return null;
    }

    private boolean spawnClone()
    {
        if (!(this.getSelf() instanceof Player PE))
            return false;

        D4CCloneEntity entity = ModEntities.D4C_CLONE.create(this.getSelf().level());
        if (entity == null) {Roundabout.LOGGER.error("Error in PowersD4C: clone entity was null after attempting to create it!"); return false; }

        entity.absMoveTo(
                this.getSelf().getX(),
                this.getSelf().getY(),
                this.getSelf().getZ()
        );

        entity.setPlayer(PE);
        entity.setYRot(this.getSelf().getYRot());
        entity.setXRot(this.getSelf().getXRot());

        this.getSelf().level().addFreshEntity(entity);

        return true;
    }

    private void swapClone(D4CCloneEntity clone)
    {
        if (!(this.getSelf() instanceof Player PE))
            return;

        if (clone.player != PE)
            return;

        Vec3 clonePos = clone.position();
        float cloneXRot = clone.getXRot();
        float cloneYRot = clone.getYRot();
        float cloneHealth = clone.getHealth();

        List<MobEffectInstance> cloneEffects = new ArrayList<>();
        for (MobEffectInstance effect : clone.getActiveEffects()) {
            cloneEffects.add(new MobEffectInstance(effect));
        }

        List<MobEffectInstance> playerEffects = new ArrayList<>();
        for (MobEffectInstance effect : PE.getActiveEffects()) {
            playerEffects.add(new MobEffectInstance(effect));
        }

        clone.teleportTo(PE.getX(), PE.getY(), PE.getZ());
        clone.setYRot(PE.getYRot());
        clone.setXRot(PE.getXRot());
        clone.setHealth(PE.getHealth());

        PE.teleportTo(clonePos.x, clonePos.y, clonePos.z);
        PE.moveTo(clonePos.x, clonePos.y, clonePos.z, cloneXRot, cloneYRot);
        PE.setHealth(cloneHealth);

        for (MobEffectInstance effect : clone.getActiveEffects()) {
            clone.removeEffect(effect.getEffect());
        }
        for (MobEffectInstance effect : PE.getActiveEffects()) {
            PE.removeEffect(effect.getEffect());
        }

        for (MobEffectInstance effect : cloneEffects) {
            PE.addEffect(new MobEffectInstance(effect));
        }
        for (MobEffectInstance effect : playerEffects) {
            clone.addEffect(new MobEffectInstance(effect));
        }

        StandUser SU = (StandUser) PE;
        StandUser CSU = (StandUser) clone;

        byte playerLocacacaCurse = SU.roundabout$getLocacacaCurse();
        byte cloneLocacacaCurse = CSU.roundabout$getLocacacaCurse();

        SU.roundabout$setLocacacaCurse(cloneLocacacaCurse);
        CSU.roundabout$setLocacacaCurse(playerLocacacaCurse);

        byte playerOnStandFire = SU.roundabout$getOnStandFire();
        byte cloneOnStandFire = CSU.roundabout$getOnStandFire();

        SU.roundabout$setOnStandFire(cloneOnStandFire);
        CSU.roundabout$setOnStandFire(playerOnStandFire);
    }

    private boolean held3 = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!inputDash) {
                if (isHoldingSneak()) {
                    if (!this.onCooldown(PowerIndex.SKILL_3) && isBetweenTwoThings()) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);

                        this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.D4CMeltDodgeCooldown);
                    }
                    inputDash = true;
                } else {
                    super.buttonInput3(keyIsDown, options);
                }
            }
        } else {
            inputDash = false;
        }
    }

    private boolean held4 = false;
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (keyIsDown && !held4 && !(this.onCooldown(PowerIndex.SKILL_4)))
        {
            held4 = true;

            if (isBetweenTwoThings())
            {
                if (!isHoldingSneak())
                {
                    this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().cooldownsInTicks.d4cDimensionHopToNewDimension);
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_4);
                }
                else {
                    this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().cooldownsInTicks.d4cDimensionKidnap);
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_4_SNEAK);
                }

                //ModPacketHandler.PACKET_ACCESS.registerNewWorld();
            }
        }
        else if (!keyIsDown)
            held4 = false;
    }

    /** Store player entity ids here while we wait for them to recieve the packet and load the dimension. */
    public static HashMap<Integer, DynamicWorld> queuedWorldTransports = new HashMap<>();
    private boolean teleportToD4CWorld()
    {
        if (this.getSelf().getServer() == null)
            return false;

        DynamicWorld world = DynamicWorld.generateD4CWorld(this.getSelf().getServer());
        if (world.getLevel() != null)
        {
            queuedWorldTransports.put(this.getSelf().getId(), world);
            world.broadcastPacketsToPlayers(this.getSelf().getServer());
            return true;
        }

        return false;
    }

    private boolean teleportToD4CWorldKidnap()
    {
        if (this.getSelf().getServer() == null)
            return false;

        BlockPos thisPosition = this.getSelf().blockPosition();
        int radius = ClientNetworking.getAppropriateConfig().d4cDimensionKidnapRadius;

        List<LivingEntity> entities = this.getSelf().getServer().overworld().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this.getSelf(),
                new AABB(thisPosition).inflate(radius));

        DynamicWorld world = DynamicWorld.generateD4CWorld(this.getSelf().getServer());
        if (world.getLevel() != null)
        {
            queuedWorldTransports.put(this.getSelf().getId(), world);
        }
        else
            return false;

        for (LivingEntity entity : entities)
        {
            if (!entity.equals(this.getSelf()))
            {
                if (entity instanceof StandEntity)
                    continue;

                if (entity instanceof Player)
                    queuedWorldTransports.put(entity.getId(), world);
                else
                    entity.teleportTo(world.getLevel(), entity.getX(), entity.getY(), entity.getZ(), Set.of(
                            RelativeMovement.X,
                            RelativeMovement.Y,
                            RelativeMovement.Z), entity.getYRot(), entity.getXRot());
            }
        }

        world.broadcastPacketsToPlayers(this.getSelf().getServer());

        return true;
    }

    private boolean meltDodge()
    {
        if (this.getSelf().level().isClientSide)
            return false;

        // serverside
        meltDodgeTicks = 0;

        return true;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        switch (activeP)
        {
            case PowerIndex.SKILL_4, PowerIndex.SKILL_4_SNEAK, PowerIndex.SKILL_2, PowerIndex.SKILL_3 -> {
                return !(isBetweenTwoThings());
            }
        }

        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not meld dodging */
    public int meltDodgeTicks = -1;

    @Override
    public void tickPower() {
        super.tickPower();
    }

    public void meltDodge(AbstractArrow arrow)
    {

    }

    /** Is the entity inbetween two tings? */
    @SuppressWarnings("deprecation") // isSolid()
    boolean isBetweenTwoThings()
    {
        return (isBetweenTwoThings(this.getSelf().blockPosition()));
    };

    // TODO: replace this with a more advanced predicate
    @SuppressWarnings("deprecation") // isSolid()
    boolean isBetweenTwoThings(BlockPos pos)
    {
        BlockState bottom = this.getSelf().level().getBlockState(pos.subtract(new Vec3i(0, 1, 0)));
        BlockState top = this.getSelf().level().getBlockState(pos.subtract(new Vec3i(0, -2, 0)));

        return (!bottom.isAir() && !top.isAir()) && (bottom.isSolid() && top.isSolid());
    };


    @Override
    public boolean isWip(){
        return true;
    }

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                D4CEntity.MANGA_SKIN,
                D4CEntity.WONDER_FESTIVAL
        );
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                return this.spawnClone();
            }
            case PowerIndex.POWER_3 -> {
                return this.meltDodge();
            }
            case PowerIndex.POWER_4 -> {
                return this.teleportToD4CWorld();
            }
            case PowerIndex.POWER_4_SNEAK -> {
                return this.teleportToD4CWorldKidnap();
            }
        }

        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateIntMove(int in) {
        Entity targeted = this.getSelf().level().getEntity(in);
        if (targeted instanceof D4CCloneEntity clone)
        {
            this.swapClone(clone);
        }

        super.updateIntMove(in);
    }
}