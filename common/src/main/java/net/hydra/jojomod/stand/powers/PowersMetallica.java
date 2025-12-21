package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.projectile.MetallicaKnifeEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.hydra.jojomod.event.ModParticles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowersMetallica extends NewDashPreset {

    public static final byte NAILS = 0;
    public static final byte RAZORS = 1;
    public static final byte SCISSORS = 2;

    private static final int HARVEST_CODE = 999;

    public List<MetallicaKnifeEntity> summonedKnives = new ArrayList<>();
    public List<ItemEntity> magnetItems = new ArrayList<>();

    public PowersMetallica(LivingEntity self) {
        super(self);
        if (self != null && !self.level().isClientSide) {
            ((IEntityAndData)self).roundabout$setMagneticField(false);
        }
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) { return new PowersMetallica(entity); }
    @Override
    public boolean isStandEnabled() { return true; }
    @Override
    public boolean canSummonStandAsEntity() { return false; }
    @Override
    public boolean rendersPlayer() { return true; }

    @Override
    public void onStandSummon(boolean active) {
        super.onStandSummon(active);
        if (active && self != null && !self.level().isClientSide) {
            ((IEntityAndData)self).roundabout$setMagneticField(false);
        }
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player){
        if (!player.hasLineOfSight(ent)) return false;
        Entity target = MainUtil.getTargetEntity(player, 40.0f, 1);
        return target != null && target.is(ent);
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player){ return 0xFF0000; }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (isHoldingSneak()) {
            byte mode = getMetalMode();

            if (mode == NAILS) {
                setSkillIcon(context, x, y, 1, StandIcons.METALLICA_NAIL, PowerIndex.NO_CD);
            }
            else if (mode == RAZORS) {
                setSkillIcon(context, x, y, 1, StandIcons.METALLICA_RAZOR, PowerIndex.NO_CD);
            }
            else {
                setSkillIcon(context, x, y, 1, StandIcons.METALLICA_SCISSORS, PowerIndex.NO_CD);
            }
        } else {
            if (isMagneticFieldActive())
                setSkillIcon(context, x, y, 1, StandIcons.METALLICA_FIELD_ON, PowerIndex.SKILL_1);
            else
                setSkillIcon(context, x, y, 1, StandIcons.METALLICA_FIELD_OFF, PowerIndex.SKILL_1);
        }

        if (!isHoldingSneak()) {
            if (hasKnivesReady())
                setSkillIcon(context, x, y, 2, StandIcons.METALLICA_KNIFE_FIRE, PowerIndex.SKILL_2);
            else
                setSkillIcon(context, x, y, 2, StandIcons.METALLICA_KNIFE_RAISE, PowerIndex.SKILL_2);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.METALLICA_HARVEST, PowerIndex.SKILL_2);
        }

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.METALLICA_HEAL, PowerIndex.SKILL_EXTRA);

        if (!isHoldingSneak())
            setSkillIcon(context, x, y, 4, StandIcons.METALLICA_INVIS, PowerIndex.POWER_3);
        else
            setSkillIcon(context, x, y, 4, StandIcons.METALLICA_REPAIR, PowerIndex.NO_CD);

        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> tryPowerPacket(PowerIndex.POWER_1);
            case SKILL_1_CROUCH -> {
                tryPowerPacket(PowerIndex.POWER_2);
                cycleMetalMode();
            }
            case SKILL_2_NORMAL -> handleKnivesClient();
            case SKILL_2_CROUCH -> tryIntPowerPacket(PowerIndex.POWER_2_EXTRA, HARVEST_CODE);
            case SKILL_3_NORMAL -> dash();
            case SKILL_3_CROUCH -> { if(!onCooldown(PowerIndex.SKILL_EXTRA)) tryPowerPacket(PowerIndex.SKILL_EXTRA); }
            case SKILL_4_NORMAL -> tryPowerPacket(PowerIndex.POWER_3);
            case SKILL_4_CROUCH -> tryPowerPacket(PowerIndex.POWER_4);
        }
    }

    public void handleKnivesClient() {
        Entity target = MainUtil.getTargetEntity(self, 40.0f, 2);
        int targetId = (target != null) ? target.getId() : -1;
        tryIntPowerPacket(PowerIndex.POWER_2_EXTRA, targetId);
    }

    private boolean hasKnivesReady() {
        if (summonedKnives == null) return false;
        summonedKnives.removeIf(k -> k == null || !k.isAlive());
        return !summonedKnives.isEmpty();
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int data) {
        if (move == PowerIndex.POWER_2_EXTRA) {
            if (data == HARVEST_CODE) return executeIronHarvest();
            if (hasKnivesReady()) return fireKnivesAt(data);
            else return spawnKnives();
        }
        return super.tryIntPower(move, forced, data);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> { return toggleMagneticField(); }
            case PowerIndex.POWER_2 -> { return cycleMetalMode(); }
            case PowerIndex.SKILL_2_SNEAK -> { return executeIronHarvest(); }
            case PowerIndex.POWER_3 -> { return toggleInvis(); }
            case PowerIndex.POWER_4 -> { return executeRepair(); }
            case PowerIndex.SKILL_EXTRA -> { return executeHeal(); }
        }
        return super.setPowerOther(move, lastMove);
    }

    public boolean spawnKnives() {
        if (onCooldown(PowerIndex.SKILL_2)) return false;
        BlockPos centerPos = self.getOnPos();
        if (self.level().getBlockState(centerPos).isAir()) centerPos = centerPos.below();
        int randomLimit = 1 + self.getRandom().nextInt(9);
        List<BlockPos> validPositions = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos pos = centerPos.offset(x, 0, z);
                BlockState state = self.level().getBlockState(pos);
                if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.SAND) || state.is(Blocks.GRAVEL) || state.is(Blocks.STONE)) {
                    validPositions.add(pos);
                }
            }
        }
        Collections.shuffle(validPositions);
        summonedKnives.clear();
        int spawned = 0;

        for (BlockPos pos : validPositions) {
            if (spawned >= randomLimit) break;
            BlockState state = self.level().getBlockState(pos);
            boolean consumed = false;

            if (MainUtil.getIsGamemodeApproriateForGrief(self)) {
                if (state.is(Blocks.GRASS_BLOCK)) { self.level().setBlock(pos, Blocks.DIRT.defaultBlockState(), 3); consumed = true; }
                else if (state.is(Blocks.SAND)) { self.level().setBlock(pos, Blocks.SANDSTONE.defaultBlockState(), 3); consumed = true; }
                else if (state.is(Blocks.GRAVEL)) { self.level().setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3); consumed = true; }
                else if (state.is(Blocks.STONE)) { self.level().setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3); consumed = true; }
            } else { consumed = true; }

            if (consumed) {
                createKnifeEntity(pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5);
                if (Math.random() < 0.2) self.level().playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.PLAYERS, 0.5f, 1.0f);
                spawned++;
            }
        }

        if (spawned == 0 && self instanceof Player p && !p.isCreative()) {
            int nuggetsFound = 0;
            for(int i = 0; i < p.getInventory().getContainerSize(); ++i) {
                ItemStack stack = p.getInventory().getItem(i);
                if (stack.is(Items.IRON_NUGGET)) nuggetsFound += stack.getCount();
            }
            if (nuggetsFound > 0) {
                int toSpawn = Math.min(nuggetsFound, 1 + self.getRandom().nextInt(3));
                int consumedCount = 0;
                for (int i = 0; i < p.getInventory().getContainerSize(); ++i) {
                    if (consumedCount >= toSpawn) break;
                    ItemStack stack = p.getInventory().getItem(i);
                    if (stack.is(Items.IRON_NUGGET)) {
                        while (stack.getCount() > 0 && consumedCount < toSpawn) {
                            stack.shrink(1);
                            consumedCount++;
                        }
                    }
                }
                for (int i = 0; i < consumedCount; i++) spawnKnifeAroundHead();
                spawned = consumedCount;
            }
        } else if (spawned == 0 && self instanceof Player p && p.isCreative()) {
            for(int i=0; i<3; i++) spawnKnifeAroundHead();
            spawned = 3;
        }

        if (spawned > 0) { setCooldown(PowerIndex.SKILL_2, 10); return true; }
        return false;
    }

    private void spawnKnifeAroundHead() {
        double offsetX = (self.getRandom().nextDouble() - 0.5) * 1.5;
        double offsetZ = (self.getRandom().nextDouble() - 0.5) * 1.5;
        createKnifeEntity(self.getX() + offsetX, self.getEyeY(), self.getZ() + offsetZ);
    }

    private void createKnifeEntity(double x, double y, double z) {
        MetallicaKnifeEntity knife = new MetallicaKnifeEntity(self.level(), self);
        knife.setPos(x, y, z);
        knife.setWaiting(true);
        knife.setInvisible(false);
        self.level().addFreshEntity(knife);
        summonedKnives.add(knife);
    }

    public boolean fireKnivesAt(int targetId) {
        Vec3 targetPos;
        Entity target = (targetId != -1) ? self.level().getEntity(targetId) : null;
        if (target != null) targetPos = target.getBoundingBox().getCenter();
        else targetPos = self.pick(60.0D, 0.0F, false).getLocation();

        for (MetallicaKnifeEntity knife : summonedKnives) {
            knife.setWaiting(false);
            knife.setInvisible(false);
            double dx = targetPos.x - knife.getX(); double dy = targetPos.y - knife.getY(); double dz = targetPos.z - knife.getZ();
            knife.shoot(dx, dy, dz, 3.5F, 0.2F);
            self.level().playSound(null, knife.getX(), knife.getY(), knife.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0f, 1.5f);
        }
        summonedKnives.clear();
        setCooldown(PowerIndex.SKILL_2, 50);
        return true;
    }

    public boolean executeIronHarvest() {
        if(onCooldown(PowerIndex.SKILL_2)) return false;
        Vec3 eye = self.getEyePosition();
        Vec3 reach = eye.add(self.getViewVector(1.0f).scale(4.0));
        BlockPos center = BlockPos.containing(reach);
        boolean anyHarvested = false;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
            BlockState bs = self.level().getBlockState(pos);
            boolean isIronOre = bs.is(Blocks.IRON_ORE) || bs.is(Blocks.DEEPSLATE_IRON_ORE);
            boolean isFlower = bs.is(BlockTags.FLOWERS) || bs.is(BlockTags.SMALL_FLOWERS) ||
                    bs.getBlock() instanceof net.minecraft.world.level.block.FlowerBlock ||
                    bs.getBlock() instanceof net.minecraft.world.level.block.TallFlowerBlock;
            if ((isFlower || isIronOre) && MainUtil.getIsGamemodeApproriateForGrief(self)) {
                if (isIronOre) {
                    Block replacement = bs.is(Blocks.IRON_ORE) ? Blocks.STONE : Blocks.DEEPSLATE;
                    self.level().setBlock(pos, replacement.defaultBlockState(), 3);
                    int count = 1 + self.getRandom().nextInt(3);
                    spawnMagnetItem(pos, new ItemStack(Items.RAW_IRON, count));
                    self.level().playSound(null, pos, SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 0.5f, 1.5f);
                    if (self.level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.WAX_OFF, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 5, 0.3, 0.3, 0.3, 0.1);
                    anyHarvested = true;
                } else {
                    self.level().destroyBlock(pos, false);
                    int count = 1 + self.getRandom().nextInt(3);
                    spawnMagnetItem(pos, new ItemStack(Items.IRON_NUGGET, count));
                    self.level().playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                    if (self.level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 3, 0.2, 0.2, 0.2, 0);
                    anyHarvested = true;
                }
            }
        }
        if (anyHarvested) { setCooldown(PowerIndex.SKILL_2, 10); return true; }
        return false;
    }

    private void spawnMagnetItem(BlockPos pos, ItemStack stack) {
        ItemEntity item = new ItemEntity(self.level(), pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stack);
        Vec3 direction = self.position().add(0, 1.0, 0).subtract(item.position()).normalize().scale(0.8);
        item.setDeltaMovement(direction);
        item.setPickUpDelay(0);
        item.setNoGravity(true);
        item.setInvisible(false);
        self.level().addFreshEntity(item);
        magnetItems.add(item);
    }

    public boolean executeRepair() {
        if (!(self instanceof Player p)) return false;

        List<ItemStack> damagedItems = new ArrayList<>();
        for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
            ItemStack stack = p.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.isDamaged() && stack.getItem() instanceof TieredItem tiered && tiered.getTier() == Tiers.IRON) {
                damagedItems.add(stack);
            }
        }

        if (damagedItems.isEmpty()) {
            return false;
        }

        int nuggetsSlot = -1;
        for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
            if (p.getInventory().getItem(i).is(Items.IRON_NUGGET)) { nuggetsSlot = i; break; }
        }

        if (nuggetsSlot == -1 && !p.isCreative()) {
            return false;
        }

        ItemStack target = damagedItems.get(self.getRandom().nextInt(damagedItems.size()));
        int repairAmount = target.getMaxDamage() / 4;
        target.setDamageValue(Math.max(0, target.getDamageValue() - repairAmount));

        if (!p.isCreative()) p.getInventory().getItem(nuggetsSlot).shrink(1);

        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 1.0f, 1.5f);

        if (self.level() instanceof ServerLevel sl) {
            sl.sendParticles(new net.minecraft.core.particles.ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.IRON_INGOT)),
                    self.getX(), self.getEyeY(), self.getZ(), 15, 0.4, 0.4, 0.4, 0.1);
        }
        return true;
    }

    private boolean magneticFieldActive = false;
    private byte metalMode = NAILS;
    public boolean isMagneticFieldActive() { return ((IEntityAndData)self).roundabout$isMagneticField(); }
    public byte getMetalMode() { return ((IEntityAndData)self).roundabout$getMetalMode(); }
    public boolean toggleMagneticField() { boolean newState = !isMagneticFieldActive(); ((IEntityAndData)self).roundabout$setMagneticField(newState); return true; }

    public boolean cycleMetalMode() {
        byte newMode = (byte) (getMetalMode() + 1);
        if (newMode > SCISSORS) newMode = NAILS;
        ((IEntityAndData)self).roundabout$setMetalMode(newMode);

        if (!self.level().isClientSide) {
            List<Entity> nearby = self.level().getEntitiesOfClass(Entity.class, self.getBoundingBox().inflate(20));
            for (Entity e : nearby) {
                if (e instanceof LivingEntity && e != self) {
                    ((IEntityAndData)e).roundabout$setMetalMeter(0);
                }
            }
            if (self instanceof Player p) {
                String modeName = switch(newMode) {
                    case RAZORS -> "Mode: Razors";
                    case SCISSORS -> "Mode: Scissors";
                    default -> "Mode: Nails";
                };
                p.displayClientMessage(Component.literal(modeName).withStyle(ChatFormatting.GOLD), true);
            }
        }
        return true;
    }

    @Override
    public void tickPower() {
        super.tickPower();
        if (!magnetItems.isEmpty()) {
            magnetItems.removeIf(item -> {
                if (item == null || item.isRemoved()) return true;
                if (item.tickCount >= 60) { item.setNoGravity(false); item.setDeltaMovement(item.getDeltaMovement().multiply(0.2, 0.2, 0.2)); return true; }
                return false;
            });
        }
        if (isInvisible() && !self.level().isClientSide && self.tickCount % 5 == 0) {
            AABB searchArea = self.getBoundingBox().inflate(24.0);
            List<Mob> mobs = self.level().getEntitiesOfClass(Mob.class, searchArea);
            for (Mob mob : mobs) {
                if (mob.getTarget() == self) {
                    if (mob.distanceTo(self) > 5.0) { mob.setTarget(null); mob.setLastHurtByMob(null); }
                }
            }
        }
        if (isMagneticFieldActive() && !self.level().isClientSide()) {
            float radius = 10.0f;
            List<Entity> targets = MainUtil.getEntitiesInRange(self.level(), self.blockPosition(), radius);
            for (Entity ent : targets) {
                if (ent instanceof LivingEntity victim && !ent.is(self) && MainUtil.canActuallyHitInvolved(self, ent)) {
                    if (!self.hasLineOfSight(victim)) continue;
                    IEntityAndData data = (IEntityAndData) victim;
                    float dist = victim.distanceTo(self);
                    float distFactor = 1.0f - (dist / 10.0f); if (distFactor < 0) distFactor = 0; float distMult = 0.5f + distFactor;
                    float speedMult = switch(getMetalMode()) { case NAILS -> 2.0f; case RAZORS -> 1.0f; case SCISSORS -> 0.66f; default -> 1.0f; };
                    float buildup = speedMult * distMult;
                    float current = data.roundabout$getMetalMeter() + buildup;
                    if (current >= 100f) { triggerMetalEffect(victim, data); current = 0; }
                    data.roundabout$setMetalMeter(current);
                    data.roundabout$setMetalMode(this.metalMode);
                }
            }
        }
    }

    private void triggerMetalEffect(LivingEntity victim, IEntityAndData data) {
        float dmg = 0; int bleedTicks = 0; int bleedLvl = 0; byte mode = getMetalMode();

        victim.hurt(self.damageSources().magic(), dmg);

        if (bleedTicks > 0 && BuiltInRegistries.MOB_EFFECT.containsKey(new ResourceLocation(Roundabout.MOD_ID, "bleed"))) {
            victim.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(Roundabout.MOD_ID, "bleed")), bleedTicks, bleedLvl, false, false));
        }

        ParticleOptions pType = switch(getMetalMode()) {
            case NAILS -> ModParticles.METALLICA_NAIL;
            case RAZORS -> ModParticles.METALLICA_RAZOR;
            case SCISSORS -> ModParticles.METALLICA_SCISSORS;
            default -> ParticleTypes.CRIT;
        };



        if (self.level() instanceof ServerLevel sl) {
            sl.sendParticles(pType, victim.getX(), victim.getY() + victim.getBbHeight()/2, victim.getZ(), 30, 0.4, 0.4, 0.4, 0.1);
        }
    }
    public boolean executeHeal() {
        setCooldown(PowerIndex.SKILL_EXTRA, 600);
        float dmg = ((IEntityAndData)self).roundabout$getLastDamageTaken();
        if (dmg <= 0) dmg = 6.0f; if (dmg > 40.0f) dmg = 40.0f;
        self.heal(dmg);
        ((IEntityAndData)self).roundabout$setLastDamageTaken(0);
        if (BuiltInRegistries.MOB_EFFECT.containsKey(new ResourceLocation(Roundabout.MOD_ID, "bleed"))) { self.removeEffect(BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(Roundabout.MOD_ID, "bleed"))); }
        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 1.0f, 1.5f);
        return true;
    }
    public boolean onKilledEntity(ServerLevel level, LivingEntity target) {
        if (((StandUser)self).roundabout$getActive() && target.distanceTo(this.self) <= 10) {
            int nuggets = (int) Math.min(target.getMaxHealth() / 4, 5);
            if (nuggets > 0) { spawnMagnetItem(target.blockPosition(), new ItemStack(Items.IRON_NUGGET, nuggets)); }
        }
        return super.onKilledEntity(level, target);
    }
    public boolean toggleInvis() {
        IEntityAndData data = (IEntityAndData) self;
        if (data.roundabout$getTrueInvisibility() > 0) {
            data.roundabout$setTrueInvisibility(0);
            self.removeEffect(MobEffects.INVISIBILITY);
        } else {
            data.roundabout$setTrueInvisibility(10000);
        }
        return true;
    }
    public boolean isInvisible() { return ((IEntityAndData)self).roundabout$getTrueInvisibility() > 0; }
}