package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.D4CLightBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.shader.RPostShaderRegistry;
import net.hydra.jojomod.client.shader.RRenderUtil;
import net.hydra.jojomod.client.shader.callback.IRendererCallback;
import net.hydra.jojomod.client.shader.callback.RenderCallbackRegistry;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PunchingStand;
import net.hydra.jojomod.item.InterdimensionalKeyItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        if (isGuarding())
            setSkillIcon(context, x, y, 1, StandIcons.D4C_GENERATE_KEY, PowerIndex.SKILL_1_SNEAK);
        else
            if (!isHoldingSneak())
                setSkillIcon(context, x, y, 1, StandIcons.D4C_BETWEEN_VISION, PowerIndex.SKILL_1);
            else
                setSkillIcon(context, x, y, 1, StandIcons.D4C_DIMENSION_KIDNAP, PowerIndex.SKILL_1);

        if (!isHoldingSneak() && !isGuarding())
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_SUMMON, PowerIndex.SKILL_2);
        else if (!isGuarding()) {
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_SWAP, PowerIndex.SKILL_2_SNEAK);
        }
        else
        {
            setSkillIcon(context, x, y, 2, StandIcons.D4C_CLONE_MINING, PowerIndex.SKILL_1);
        }

        if (isGuarding())
            setSkillIcon(context, x, y, 3, StandIcons.D4C_PARALLEL_RUNNING, PowerIndex.SKILL_EXTRA);
        else
            if (!isHoldingSneak())
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
            else
                setSkillIcon(context, x, y, 3, StandIcons.D4C_MELT_DODGE, PowerIndex.SKILL_3);

        if (!InterdimensionalKeyItem.isLinked(this.getSelf().getOffhandItem()))
            if (!isInD4CWorld())
                if (!isHoldingSneak())
                    setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4);
                else
                    setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP, PowerIndex.SKILL_4);
            else
                if (!isHoldingSneak())
                    setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_RETURN, PowerIndex.SKILL_4);
                else
                    setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_RETURN, PowerIndex.SKILL_4);
        else
            if (!isHoldingSneak())
                setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP_KEY, PowerIndex.SKILL_EXTRA_2);
            else
                setSkillIcon(context, x, y, 4, StandIcons.D4C_DIMENSION_HOP_KEY, PowerIndex.SKILL_EXTRA_2);

        fx.roundabout$onGUI(context);
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

            if (isGuarding())
            {
                if (isPRunning)
                    return;

                if (this.onCooldown(PowerIndex.SKILL_1))
                    return;

                if (!isInD4CWorld())
                    return;

                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BLOCK, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1_BLOCK);
                this.setCooldown(PowerIndex.SKILL_1, 20);
            }
            else
            {
                if (!isHoldingSneak())
                {
                    if (this.onCooldown(PowerIndex.SKILL_1))
                        return;

                    betweenVision = !betweenVision;
                    this.setCooldown(PowerIndex.SKILL_1, 20);
                }
            }
        } else if (!keyIsDown) {
            held1 = false;
        }
    }

    private boolean generateKey()
    {
        if (this.getSelf().level().isClientSide)
            return true;

        if (!isInD4CWorld())
            return false;

        Level level = this.getSelf().level();
        StandEntity entity = getStandEntity(this.getSelf());

        if (entity == null)
            return false;

        ((ServerLevel)level).sendParticles(
                ParticleTypes.SONIC_BOOM,
                entity.getX(),
                entity.getY()+1.0f,
                entity.getZ(),
                1,
                0.0f,
                0.0f,
                0.0f,
                1.0f
        );

        ((ServerLevel)level).sendParticles(
                ParticleTypes.END_ROD,
                entity.getX(),
                entity.getY()+1.0f,
                entity.getZ(),
                25,
                0.2f,
                0.4f,
                0.2f,
                0.3f
        );

        ItemStack stack = ModItems.INTERDIMENSIONAL_KEY.getDefaultInstance();
        InterdimensionalKeyItem.setLinkedDimension(stack, this.getSelf().level().dimension().location());
        ((ServerPlayer)this.getSelf()).addItem(stack);

        return true;
    }

    private int betweenVisionTicks = 0;
    private void highlightBlocksInFrustum(int angle, int offset)
    {
        betweenVisionTicks++;

        if (betweenVisionTicks < 10)
            return;

        betweenVisionTicks = 0;

        Set<BlockPos> highlighted = new HashSet<>();

        for (int pitch = -angle; pitch < angle; pitch += offset)
        {
            for (int yaw = -angle; yaw < angle; yaw += offset)
            {
                BlockPos state = ClientUtil.raycastForBlockGivenAngle(Math.toRadians(pitch), Math.toRadians(yaw), 100);
                if (state == null)
                    continue;

                if (!highlighted.add(state))
                    continue;

                if (!isBetweenTwoThings(state.above()))
                    continue;

                this.getSelf().level().addParticle(
                        ModParticles.D4C_LINES,
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
        if (isPRunning)
            return;

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

    public static int pRunningTimeLimit = 10;
    private volatile boolean isPRunning = false;
    public int pRunningFrames = 0;
    private boolean held3 = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!inputDash) {
                if (isHoldingSneak()) {
                    if (!this.onCooldown(PowerIndex.SKILL_3) && isBetweenTwoThings()) {
                        if (isPRunning)
                            return;

                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3);

                        this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().cooldownsInTicks.D4CMeltDodgeCooldown);
                    }
                    inputDash = true;
                } else {
                    if (isGuarding())
                    {
                        if (!this.onCooldown(PowerIndex.SKILL_EXTRA))
                            if (isPRunning || isBetweenTwoThings(this.getSelf().blockPosition()))
                            {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_3_BLOCK);
                                this.setCooldown(PowerIndex.SKILL_EXTRA, 20);
                            }
                    }
                    else
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
        if (isPRunning)
            return;

        if (keyIsDown && !held4 && !(this.onCooldown(PowerIndex.SKILL_4)))
        {
            held4 = true;

            if (isBetweenTwoThings())
            {
                if (!isHoldingSneak())
                {
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

    private static class ShaderFx implements IRendererCallback {
        public boolean isDimensionTraveling = false;
        public boolean shouldShowDimensionFx = false;

        @Override
        public void roundabout$LEVEL_RENDER_FINISH(float partialTick) {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer player = client.player;

            if (player == null) return;
            if (!(((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C))
                return;

            if (((StandUser)player).roundabout$isParallelRunning())
            {
                if (RPostShaderRegistry.DESATURATE != null)
                    RPostShaderRegistry.DESATURATE.roundabout$process(partialTick);

                if (RPostShaderRegistry.DECONVERGE != null)
                    RPostShaderRegistry.DECONVERGE.roundabout$process(partialTick);

                if (RPostShaderRegistry.PHOSPHOR != null)
                    RPostShaderRegistry.PHOSPHOR.roundabout$process(partialTick);
            }

            Level level = player.level();

            shouldShowDimensionFx = (level.dimension().location().getNamespace().equals("roundabout")) && ((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C;

            if (RRenderUtil.isUsingFabulous())
                return;

            ClientConfig clientConfig = ConfigManager.getClientConfig();
            if (shouldShowDimensionFx && clientConfig.experiments.d4cShouldUseColorShader)
            {
                if (RPostShaderRegistry.D4C_ALT_DIMENSION != null)
                {
                    RPostShaderRegistry.D4C_ALT_DIMENSION.roundabout$process(partialTick);
                }
            }
        }

        @Override
        public void roundabout$GAME_RENDERER_FINISH(float tickDelta) {
            if (Minecraft.getInstance().player == null)
                return;

            if (((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers() instanceof PowersD4C d4c)
            {
                if (((StandUser)Minecraft.getInstance().player).roundabout$isParallelRunning())
                    d4c.pRunningFrames++;
                else
                    d4c.pRunningFrames = 0;
            }
        }

        public void roundabout$onGUI(GuiGraphics graphics) {
            if (Minecraft.getInstance().player == null)
                return;

            if (((StandUser) Minecraft.getInstance().player).roundabout$getStandPowers() instanceof PowersD4C d4c) {
                if (((StandUser) Minecraft.getInstance().player).roundabout$isParallelRunning()) {
                    int whiteFlashFrameCount = 8;
                    if (d4c.pRunningFrames < whiteFlashFrameCount) {
                        float alpha = 1.0f - (d4c.pRunningFrames / (float) whiteFlashFrameCount);
                        int alphaInt = (int) (alpha * 255);
                        int color = (alphaInt << 24) | 0xFFFFFF;

                        graphics.fill(0, 0,
                                Minecraft.getInstance().getWindow().getWidth(),
                                Minecraft.getInstance().getWindow().getHeight(),
                                color
                        );
                    }
                }
            }
        }

        @Override
        public void roundabout$bootstrap() {

        }

        public ShaderFx()
        {}
    }

    private static ShaderFx fx;

    static {
        fx = new ShaderFx();
        RenderCallbackRegistry.register(fx);
    }

    private void yoinkCurrency() {
        if (this.getSelf() instanceof Player pe) {
            int count = 0;
            var inventory = pe.getInventory();

            for (int i = 0; i < inventory.items.size(); i++) {
                ItemStack stack = inventory.items.get(i);
                if (stack.is(Items.EMERALD) || stack.is(Items.EMERALD_BLOCK)) {
                    count += stack.getCount();
                    pe.drop(stack.copy(), false);
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }

            if (count >= 1) {
                pe.displayClientMessage(Component.translatable("item.roundabout.d4c.lost_emeralds", count), true);
            }
        }
    }

    /** Store player entity ids here while we wait for them to recieve the packet and load the dimension. */
    public static HashMap<Integer, DynamicWorld> queuedWorldTransports = new HashMap<>();
    private boolean teleportToD4CWorld()
    {
        if (this.getSelf().getServer() == null)
            return false;

        StandEntity thisEntity = getStandEntity(this.getSelf());
        if (thisEntity == null)
            return false;

        if (InterdimensionalKeyItem.isLinked(this.getSelf().getOffhandItem()))
        {
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_4_EXTRA);
            this.animateStand((byte) 30);
            this.poseStand(OffsetIndex.GUARD);
        }
        else
        {
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_4);
            this.animateStand((byte) 30);
            this.poseStand(OffsetIndex.GUARD);
        }

        return true;
    }

    private void updateTeleport()
    {
        if (InterdimensionalKeyItem.isLinked(this.getSelf().getOffhandItem()))
        {
            if (this.getSelf().level().isClientSide)
                if (!this.getSelf().level().dimension().location().getPath().equals("overworld"))
                    this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().cooldownsInTicks.d4cDimensionHopToOldDimension);

            if (this.getSelf().getServer() == null)
                return;

            if (attackTimeDuring > 39)
            {
                attackTimeDuring = -1;

                this.animateStand((byte)0);

                MinecraftServer server = this.getSelf().getServer();

                ServerLevel oldLevel = server.getLevel(
                        ResourceKey.create(Registries.DIMENSION, InterdimensionalKeyItem.getLinkedDimension(this.getSelf().getOffhandItem()))
                );
                if (oldLevel == null)
                {
                    Roundabout.LOGGER.error("Exception while teleporting to linked level: oldLevel was null!");
                    return;
                }

                yoinkCurrency();

                ServerPlayer player = (ServerPlayer)this.getSelf();

                player.teleportTo(oldLevel, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                ((StandUser)player).roundabout$summonStand(oldLevel, true, false);
                return;
            }
        }
        else
        {
            if (this.getSelf().level().isClientSide)
                if (!this.getSelf().level().dimension().location().getPath().equals("overworld"))
                    this.setCooldown(PowerIndex.SKILL_4, ClientNetworking.getAppropriateConfig().cooldownsInTicks.d4cDimensionHopToNewDimension);

            if (attackTimeDuring > 39)
            {
                attackTimeDuring = -1;

                this.animateStand((byte)0);

                MinecraftServer server = this.getSelf().getServer();
                if (server == null)
                    return;

                ServerLevel overworld = server.overworld();
                ServerPlayer player = (ServerPlayer)this.getSelf();
                if (player.level() != player.getServer().overworld() && player.level().dimension().location().getNamespace().equals("roundabout"))
                {
                    yoinkCurrency();
                    player.teleportTo(overworld, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                    ((StandUser)player).roundabout$summonStand(overworld, true, false);
                    DynamicWorld.deregisterWorld(player.getServer(), player.level().dimension().location().getPath());

                    return;
                }

                DynamicWorld world = DynamicWorld.generateD4CWorld(player.getServer());
                if (world.getLevel() != null)
                {
                    yoinkCurrency();
                    queuedWorldTransports.put(player.getId(), world);
                    world.broadcastPacketsToPlayers(player.getServer());
                    return;
                }
            }
        }
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
            yoinkCurrency();
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

    public void ejectParallelRunning()
    {
        if (parallelThread != null)
        {
            parallelThread.interrupt();
        }
    }

    private Thread parallelThread = null;
    // TODO: add a owner system for D4C light blocks, more polish
    private boolean parallelRunning() {
        if (isPRunning) {
            if (parallelThread != null && parallelThread.isAlive()) {
                parallelThread.interrupt();
                Roundabout.LOGGER.info("Parallel running interrupted");
            }
            return false;
        }

        LivingEntity self = this.getSelf();
        Level level = self.level();
        BlockPos bottom = self.blockPosition();
        BlockPos top = bottom.above();

        double oldX = self.getX();
        double oldY = self.getY();
        double oldZ = self.getZ();

        float yaw = self.getYHeadRot();
        float pitch = self.getXRot();
        float yRot = self.getYRot();

        isPRunning = true;
        ((StandUser) self).roundabout$setParallelRunning(true);
        this.scopeLevel = 3;

        parallelThread = new Thread(() -> {
            Runnable cleanup = () -> {
                isPRunning = false;
                ((StandUser) self).roundabout$setParallelRunning(false);

                if (!level.isClientSide) {
                    if (level.getBlockState(bottom).is(ModBlocks.D4C_LIGHT_BLOCK)) {
                        level.setBlock(bottom, Blocks.AIR.defaultBlockState(), 3);
                    }
                    if (level.getBlockState(top).is(ModBlocks.D4C_LIGHT_BLOCK)) {
                        level.setBlock(top, Blocks.AIR.defaultBlockState(), 3);
                    }
                }

                if (!isBetweenTwoThings(self.blockPosition())) {
                    self.teleportTo(oldX, oldY, oldZ);
                    self.setXRot(pitch);
                    self.setYHeadRot(yaw);
                    self.setYBodyRot(yRot);
                }

                this.scopeLevel = 0;

                Roundabout.LOGGER.info("Stopped P Running");

                if (!this.getSelf().level().isClientSide)
                    ModPacketHandler.PACKET_ACCESS.ejectPRunning((ServerPlayer) this.getSelf());

                setCooldown(PowerIndex.SKILL_EXTRA, pRunningTimeLimit + 5);
            };

            try {
                Thread.sleep(pRunningTimeLimit * 1000L);
            } catch (InterruptedException e) {
            }
            finally
            {
                if (level.isClientSide) {
                    Minecraft.getInstance().execute(cleanup);
                } else {
                    self.getServer().execute(cleanup);
                }
            }
        });

        parallelThread.start();

        UUID uuid = this.getSelf().getUUID();

        if (!level.isClientSide && self.getServer() != null) {
            if (level.getBlockState(bottom).isAir()) {
                level.setBlock(bottom, ModBlocks.D4C_LIGHT_BLOCK.defaultBlockState(), 3);
                BlockEntity be = level.getBlockEntity(bottom);
                if (be instanceof D4CLightBlockEntity d4cBe) {
                    d4cBe.setOwner(uuid);
                }
            }

            if (level.getBlockState(top).isAir()) {
                level.setBlock(top, ModBlocks.D4C_LIGHT_BLOCK.defaultBlockState(), 3);
                BlockEntity be = level.getBlockEntity(top);
                if (be instanceof D4CLightBlockEntity d4cBe) {
                    d4cBe.setOwner(uuid);
                }
            }
        }

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
            case PowerIndex.SKILL_1_SNEAK -> {
                return (!isInD4CWorld());
            }
            case PowerIndex.SKILL_EXTRA -> {
                return !(isBetweenTwoThings());
            }
            case PowerIndex.SKILL_4, PowerIndex.SKILL_4_SNEAK, PowerIndex.SKILL_2, PowerIndex.SKILL_3 -> {
                return !(isBetweenTwoThings()) && (!isPRunning);
            }
        }

        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    @Override
    public void tickPower() {
        super.tickPower();
    }

    public void meltDodge(AbstractArrow arrow)
    {

    }

    /** Is the entity inbetween two things? */
    public boolean isBetweenTwoThings()
    {
        return (isBetweenTwoThings(this.getSelf().blockPosition()));
    };

    @SuppressWarnings("deprecation") // isSolid()
    public boolean isBlockSolid(BlockPos pos)
    {
        BlockState state = this.getSelf().level().getBlockState(pos);

        return (
                state.isSolid() &&
                !state.isAir() &&
                !state.is(ModBlocks.D4C_LIGHT_BLOCK)
        );
    }

    // TODO: replace this with a more advanced predicate
    public boolean isBetweenTwoThings(BlockPos pos)
    {
        return (
                isBlockSolid(pos.subtract(new Vec3i(0, 1, 0))) &&
                        isBlockSolid(pos.subtract(new Vec3i(0, -2, 0))) &&
                        !isBlockSolid(pos) &&
                        !isBlockSolid(pos.subtract(new Vec3i(0, -1, 0)))
                );
    };

    public boolean isInD4CWorld()
    {
        return (this.getSelf().level().dimension().location().toString().startsWith("roundabout:d4c-"));
    }

    @Override
    public boolean isWip(){
        return true;
    }

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                D4CEntity.MANGA_SKIN,
                D4CEntity.WONDER_FESTIVAL,
                D4CEntity.PROMO,
                D4CEntity.PROMO_L,
                D4CEntity.SPECIAL,
                D4CEntity.GRAND
        );
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1_BLOCK -> {
                return this.generateKey();
            }
            case PowerIndex.POWER_2 -> {
                return this.spawnClone();
            }
            case PowerIndex.POWER_3 -> {
                return this.meltDodge();
            }
            case PowerIndex.POWER_3_BLOCK -> {
                return this.parallelRunning();
            }
            case PowerIndex.POWER_4, PowerIndex.POWER_4_EXTRA -> {
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

    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == PowerIndex.POWER_4 || this.getActivePower() == PowerIndex.POWER_4_EXTRA)
            updateTeleport();

        super.updateUniqueMoves();
    }
}