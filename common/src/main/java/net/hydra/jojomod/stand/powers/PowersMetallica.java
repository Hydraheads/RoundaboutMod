package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.projectile.MetallicaKnifeEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

import java.util.ArrayList;
import java.util.List;

public class PowersMetallica extends NewDashPreset {

    public static final byte NAILS = 0;
    public static final byte RAZORS = 1;
    public static final byte SCISSORS = 2;

    private static final int HARVEST_CODE = 999;

    private float clientFieldExpansion = 0.0f;

    public float camoAlpha = 0.0f;
    public float prevCamoAlpha = 0.0f;
    private static final float FADE_SPEED = 0.1f;

    public int attackRevealTicks = 0;
    private int lastHurtMobTimestampCheck = 0;

    public List<MetallicaKnifeEntity> summonedKnives = new ArrayList<>();
    public List<ItemEntity> magnetItems = new ArrayList<>();

    public PowersMetallica(LivingEntity self) {
        super(self);
        if (self != null && !self.level().isClientSide) {
            setMagneticField(false);
        }
    }

    public void setMagneticField(boolean field){
        getStandUserSelf().roundabout$setUniqueStandModeToggle(field);
    }

    public static boolean isImmuneToMetallica(LivingEntity entity) {
        if (entity instanceof IronGolem) return false;
        return entity instanceof AbstractSkeleton || entity instanceof Slime || entity instanceof Blaze ||
                entity instanceof Phantom || entity instanceof Vex || entity instanceof Allay ||
                entity instanceof SnowGolem || entity instanceof ArmorStand || entity instanceof WitherBoss;
    }

    @Override
    public void onStandSummon(boolean active) {
        super.onStandSummon(active);

        if (self != null) {
            setMagneticField(false);

            ((StandUser)self).roundabout$setMetallicaInvisibility(-1);
            self.removeEffect(MobEffects.INVISIBILITY);
            this.camoAlpha = 0.0f;
            this.prevCamoAlpha = 0.0f;
            this.attackRevealTicks = 0;

            if (self.isInvisible()) {
                self.setInvisible(false);
            }
            if (self.level().isClientSide() && self == ClientUtil.getPlayer()) {
                ClientUtil.setThrowFadeToTheEther(1.0f);
            }
        }
    }

    public static float getMetallicaInvisibilityAlpha(LivingEntity entity, double distanceToCamera) {
        return getMetallicaInvisibilityAlpha(entity, distanceToCamera, 0.0f);
    }

    public static float getMetallicaInvisibilityAlpha(LivingEntity entity, double distanceToCamera, float partialTicks) {
        if (entity.level().isClientSide()) {
            if (ClientUtil.getInvisibilityVision()) return 0.4f;

            float activationAlpha = 0.0f;
            float revealFactor = 0.0f;

            if (entity instanceof StandUser user && user.roundabout$getStandPowers() instanceof PowersMetallica metallica) {
                activationAlpha = Mth.lerp(partialTicks, metallica.prevCamoAlpha, metallica.camoAlpha);

                if (metallica.attackRevealTicks > 0) {
                    float progress = (metallica.attackRevealTicks - partialTicks) / 10.0f;
                    revealFactor = Mth.clamp(progress, 0.0f, 0.6f);
                }
            } else {
                if (((StandUser)entity).roundabout$getMetallicaInvisibility() > -1) {
                    activationAlpha = 1.0f;
                }
            }

            if (activationAlpha <= 0.01f) return 1.0f;

            float distanceAlpha = 1.0f;

            boolean isSelf = entity.level().isClientSide && entity == ClientUtil.getPlayer();

            if (isSelf) {
                distanceAlpha = 0.4f;
            } else {

                if (distanceToCamera <= 6.0) {
                    distanceAlpha = 1.0f - (float) (distanceToCamera / 6.0);
                } else if (distanceToCamera <= 12.0) {
                    distanceAlpha = 0.0f;
                } else {
                    if (distanceToCamera < 14.0) {
                        float factor = (float) ((distanceToCamera - 12.0) / 2.0);
                        distanceAlpha = Mth.lerp(factor, 0.0f, 0.3f);
                    } else {
                        distanceAlpha = 0.3f;
                    }
                }
            }

            float finalAlpha = Mth.lerp(activationAlpha, 1.0f, distanceAlpha);
            finalAlpha += revealFactor;

            return Mth.clamp(finalAlpha, 0.0f, 1.0f);
        }
        return 0;
    }

    public static void handleMobDrop(LivingEntity mob, Player player) {
        if (mob == null || player == null) return;
        if (!MainUtil.getMobBleed(mob) && !(mob instanceof IronGolem)) return;
        int maxHp = (int) mob.getMaxHealth();
        int nuggetsCount = Math.min(9, maxHp);
        if (mob instanceof AbstractFish) nuggetsCount = 1;
        if (nuggetsCount > 0) {
            ItemStack drop = new ItemStack(Items.IRON_NUGGET, nuggetsCount);
            if (!player.addItem(drop)) player.drop(drop, false);
            else player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }
    @Override public boolean isWip(){ return true; }
    @Override public Component ifWipListDevStatus(){ return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA); }
    @Override public Component ifWipListDev(){ return Component.literal("Lucio").withStyle(ChatFormatting.YELLOW); }
    @Override public StandPowers generateStandPowers(LivingEntity entity) { return new PowersMetallica(entity); }
    @Override public boolean isStandEnabled() { return true; }
    @Override public boolean canSummonStandAsEntity() { return false; }
    @Override public boolean rendersPlayer() { return true; }
    @Override public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            byte mode = getMetalMode();
            if (mode == NAILS) setSkillIcon(context, x, y, 1, StandIcons.METALLICA_NAIL, PowerIndex.NO_CD);
            else if (mode == RAZORS) setSkillIcon(context, x, y, 1, StandIcons.METALLICA_RAZOR, PowerIndex.NO_CD);
            else setSkillIcon(context, x, y, 1, StandIcons.METALLICA_SCISSORS, PowerIndex.NO_CD);
        } else {
            if (isMagneticFieldActive()) setSkillIcon(context, x, y, 1, StandIcons.METALLICA_FIELD_ON, PowerIndex.SKILL_1);
            else setSkillIcon(context, x, y, 1, StandIcons.METALLICA_FIELD_OFF, PowerIndex.SKILL_1);
        }
        if (!isHoldingSneak()) {
            if (hasKnivesReady()) setSkillIcon(context, x, y, 2, StandIcons.METALLICA_KNIFE_FIRE, PowerIndex.SKILL_2);
            else setSkillIcon(context, x, y, 2, StandIcons.METALLICA_KNIFE_RAISE, PowerIndex.SKILL_2);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.METALLICA_HARVEST, PowerIndex.SKILL_2);
        }
        if (!isHoldingSneak()) setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else setSkillIcon(context, x, y, 3, StandIcons.METALLICA_HEAL, PowerIndex.SKILL_EXTRA);
        if (!isHoldingSneak()) setSkillIcon(context, x, y, 4, StandIcons.METALLICA_INVIS, PowerIndex.POWER_3);
        else setSkillIcon(context, x, y, 4, StandIcons.METALLICA_REPAIR, PowerIndex.NO_CD);
        super.renderIcons(context, x, y);
    }
    @Override public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> tryPowerPacket(PowerIndex.POWER_1);
            case SKILL_1_CROUCH -> { tryPowerPacket(PowerIndex.POWER_2); cycleMetalMode(); }
            case SKILL_2_NORMAL -> handleKnivesClient();
            case SKILL_2_CROUCH -> tryIntPowerPacket(PowerIndex.POWER_2_EXTRA, HARVEST_CODE);
            case SKILL_3_NORMAL -> dash();
            case SKILL_3_CROUCH -> { if(!onCooldown(PowerIndex.SKILL_EXTRA)) tryPowerPacket(PowerIndex.SKILL_EXTRA); }
            case SKILL_4_NORMAL -> tryPowerPacket(PowerIndex.POWER_3);
            case SKILL_4_CROUCH -> tryPowerPacket(PowerIndex.POWER_4);
        }
    }
    public void handleKnivesClient() { Entity target = MainUtil.getTargetEntity(self, 40.0f, 2); int targetId = (target != null) ? target.getId() : -1; tryIntPowerPacket(PowerIndex.POWER_2_EXTRA, targetId); }
    private boolean hasKnivesReady() { if (summonedKnives == null) return false; summonedKnives.removeIf(k -> k == null || !k.isAlive()); return !summonedKnives.isEmpty(); }
    @Override public boolean tryIntPower(int move, boolean forced, int data) {
        if (move == PowerIndex.POWER_2_EXTRA) {
            if (data == HARVEST_CODE) return executeIronHarvest();
            return spawnKnives();
        }
        return super.tryIntPower(move, forced, data);
    }
    @Override public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> { return toggleMagneticField();
            } case PowerIndex.POWER_2 -> { return cycleMetalMode();
            } case PowerIndex.SKILL_2_SNEAK -> { return executeIronHarvest();
            } case PowerIndex.POWER_3 -> { return toggleInvis();
            } case PowerIndex.POWER_4 -> { return executeRepair();
            } case PowerIndex.SKILL_EXTRA -> { return executeHeal();
            } } return super.setPowerOther(move, lastMove);
    }
    public boolean spawnKnives() {
        if (onCooldown(PowerIndex.SKILL_2)) return false;
        self.swing(InteractionHand.MAIN_HAND, true);
        if (!self.level().isClientSide) {
            HitResult hit = self.pick(10.0D, 0.0F, false);
            boolean spawned = false;
            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                BlockState state = self.level().getBlockState(pos);
                BlockState newState = null;
                if (state.is(Blocks.GRASS_BLOCK)) newState = Blocks.DIRT.defaultBlockState();
                else if (state.is(Blocks.GRAVEL)) newState = Blocks.COBBLESTONE.defaultBlockState();
                else if (state.is(Blocks.STONE)) newState = Blocks.COBBLESTONE.defaultBlockState();
                else if (state.is(Blocks.SAND)) newState = Blocks.SANDSTONE.defaultBlockState();
                else if (state.is(Blocks.DEEPSLATE)) newState = Blocks.COBBLED_DEEPSLATE.defaultBlockState();
                if (newState != null) {
                    if (MainUtil.getIsGamemodeApproriateForGrief(self)) {
                        self.level().setBlock(pos, newState, 3);
                    }
                    createKnifeEntity(pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5);
                    self.level().playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.PLAYERS, 0.5f, 1.0f);
                    spawned = true;
                }
            }
            if (!spawned && self instanceof Player p) {
                boolean hasNugget = p.isCreative();
                int slot = -1;
                if (!hasNugget) {
                    for(int i = 0; i < p.getInventory().getContainerSize(); ++i) {
                        if (p.getInventory().getItem(i).is(Items.IRON_NUGGET)) {
                            slot = i;
                            hasNugget = true;
                            break;
                        }
                    }
                }
                if (hasNugget) {
                    if (slot != -1) p.getInventory().getItem(slot).shrink(1);
                    Vec3 spawnPos;
                    if (hit.getType() != HitResult.Type.MISS) {
                        spawnPos = hit.getLocation().add(0, 0.5, 0);
                    } else {
                        spawnPos = self.getEyePosition().add(self.getViewVector(1.0f).scale(2.0));
                    }
                    createKnifeEntity(spawnPos.x, spawnPos.y, spawnPos.z);
                    spawned = true;
                }
            }
            if (spawned) {
                setCooldown(PowerIndex.SKILL_2, 10);
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
    private void createKnifeEntity(double x, double y, double z) {
        MetallicaKnifeEntity knife = new MetallicaKnifeEntity(self.level(), self);
        knife.setPos(x, y, z);
        knife.setWaiting(true);
        knife.setInvisible(false);
        self.level().addFreshEntity(knife);
        summonedKnives.add(knife);
    }
    public boolean fireKnivesAt(int targetId) { return true; }
    public boolean executeIronHarvest() {
        if(onCooldown(PowerIndex.SKILL_2)) return false;
        self.swing(InteractionHand.MAIN_HAND, true);
        if (!self.level().isClientSide) {
            Vec3 eye = self.getEyePosition();
            Vec3 reach = eye.add(self.getViewVector(1.0f).scale(4.0));
            BlockPos center = BlockPos.containing(reach);
            boolean anyHarvested = false;
            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
                BlockState bs = self.level().getBlockState(pos);
                boolean isIronOre = bs.is(Blocks.IRON_ORE) || bs.is(Blocks.DEEPSLATE_IRON_ORE);
                boolean isFlower = bs.is(BlockTags.FLOWERS) || bs.is(BlockTags.SMALL_FLOWERS) || bs.getBlock() instanceof net.minecraft.world.level.block.FlowerBlock || bs.getBlock() instanceof net.minecraft.world.level.block.TallFlowerBlock;
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
        }
        return false;
    }
    private void spawnMagnetItem(BlockPos pos, ItemStack stack) {
        ItemEntity item = new ItemEntity(self.level(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        Vec3 targetPos = self.getBoundingBox().getCenter();
        Vec3 itemPos = item.position();
        Vec3 direction = targetPos.subtract(itemPos).normalize().scale(0.5);
        item.setDeltaMovement(direction);
        item.setPickUpDelay(0);
        item.setNoGravity(true);
        item.setInvisible(false);
        self.level().addFreshEntity(item);
        magnetItems.add(item);
    }
    public boolean executeRepair() {
        if (!(self instanceof Player p)) return false;
        net.minecraft.world.phys.HitResult hit = self.pick(5.0D, 0.0F, false);
        if (!self.level().isClientSide) {
            if (hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                net.minecraft.world.phys.BlockHitResult blockHit = (net.minecraft.world.phys.BlockHitResult) hit;
                BlockPos pos = blockHit.getBlockPos();
                BlockState state = self.level().getBlockState(pos);
                Block block = state.getBlock();
                boolean isDamagedAnvil = block == Blocks.DAMAGED_ANVIL;
                boolean isChippedAnvil = block == Blocks.CHIPPED_ANVIL;
                if (isDamagedAnvil || isChippedAnvil) {
                    ItemStack mainHand = p.getMainHandItem();
                    ItemStack offHand = p.getOffhandItem();
                    ItemStack resource = ItemStack.EMPTY;
                    if (mainHand.is(Items.IRON_INGOT) || offHand.is(Items.IRON_INGOT)) {
                        resource = mainHand.is(Items.IRON_INGOT) ? mainHand : offHand;
                        BlockState newState;
                        if (isDamagedAnvil) newState = Blocks.CHIPPED_ANVIL.defaultBlockState();
                        else newState = Blocks.ANVIL.defaultBlockState();
                        if (!p.isCreative()) resource.shrink(1);
                        net.minecraft.core.Direction facing = state.getValue(AnvilBlock.FACING);
                        self.level().setBlock(pos, newState.setValue(AnvilBlock.FACING, facing), 3);
                        self.level().playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        self.level().addParticle(ParticleTypes.WAX_OFF, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0,0,0);
                        return true;
                    }
                }
            }
            List<ItemStack> damagedItems = new ArrayList<>();
            for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
                ItemStack stack = p.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.isDamaged()) {
                    boolean valid = false;
                    if (stack.getItem() instanceof TieredItem tiered && tiered.getTier() == Tiers.IRON) valid = true;
                    if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == ArmorMaterials.IRON) valid = true;
                    if (valid) damagedItems.add(stack);
                }
            }
            if (damagedItems.isEmpty()) return false;
            int nuggetsSlot = -1;
            for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
                if (p.getInventory().getItem(i).is(Items.IRON_NUGGET)) { nuggetsSlot = i; break; }
            }
            if (nuggetsSlot == -1 && !p.isCreative()) return false;
            ItemStack target = damagedItems.get(self.getRandom().nextInt(damagedItems.size()));
            int repairAmount = target.getMaxDamage() / 4;
            target.setDamageValue(Math.max(0, target.getDamageValue() - repairAmount));
            if (!p.isCreative()) p.getInventory().getItem(nuggetsSlot).shrink(1);
            self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.5f, 1.2f);
            if (self.level() instanceof ServerLevel sl) {
                sl.sendParticles(new net.minecraft.core.particles.ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.IRON_INGOT)),
                        self.getX(), self.getEyeY(), self.getZ(), 15, 0.4, 0.4, 0.4, 0.1);
            }
            return true;
        }
        return false;
    }
    private boolean magneticFieldActive = false;
    private byte metalMode = NAILS;
    public boolean isMagneticFieldActive(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }

    private byte roundabout$metalMode = 0;
    public byte getMetalMode() { return roundabout$metalMode; }
    public boolean toggleMagneticField() {
        self.swing(InteractionHand.MAIN_HAND, true);
        boolean newState = !isMagneticFieldActive();
        setMagneticField(newState);
        return true;
    }
    public boolean cycleMetalMode() {
        byte newMode = (byte) (getMetalMode() + 1);
        if (newMode > SCISSORS) newMode = NAILS;
        metalMode = newMode;
        if (!self.level().isClientSide) {
            List<Entity> nearby = self.level().getEntitiesOfClass(Entity.class, self.getBoundingBox().inflate(20));
            for (Entity e : nearby) {
                if (e instanceof LivingEntity && e != self) ((StandUser)e).roundabout$setMetalMeter(0);
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

        this.prevCamoAlpha = this.camoAlpha;
        boolean wantsInvis = ((StandUser)self).roundabout$getMetallicaInvisibility() > -1;

        if (wantsInvis) {
            if (camoAlpha < 1.0f) {
                camoAlpha += FADE_SPEED;
                if (camoAlpha > 1.0f) camoAlpha = 1.0f;
            }
        } else {
            if (camoAlpha > 0.0f) {
                camoAlpha -= FADE_SPEED;
                if (camoAlpha < 0.0f) camoAlpha = 0.0f;
            }
        }

        if (self.swinging && attackRevealTicks == 0) {
            attackRevealTicks = 10;
        }
        if (self.getLastHurtMobTimestamp() != lastHurtMobTimestampCheck) {
            attackRevealTicks = 10;
            lastHurtMobTimestampCheck = self.getLastHurtMobTimestamp();
        }
        if (attackRevealTicks > 0) {
            attackRevealTicks--;
        }

        if (self.isInvisible()) {
            if (!self.hasEffect(MobEffects.INVISIBILITY)) {
                self.setInvisible(false);
            }
        }

        if (self.level().isClientSide() && self == ClientUtil.getPlayer()) {
            float handAlpha = 1.0f;
            if (wantsInvis || camoAlpha > 0) {
                handAlpha = 1.0f - camoAlpha;
                if (handAlpha < 0.4f) handAlpha = 0.4f;
            }
            ClientUtil.setThrowFadeToTheEther(handAlpha);
        }

        ResourceLocation bleedLoc = new ResourceLocation(Roundabout.MOD_ID, "bleed");
        if (wantsInvis && BuiltInRegistries.MOB_EFFECT.containsKey(bleedLoc)) {
            if (self.hasEffect(BuiltInRegistries.MOB_EFFECT.get(bleedLoc))) {
                toggleInvis();
            }
        }

        if (wantsInvis || camoAlpha > 0) {
            List<ItemEntity> nearbyDrops = self.level().getEntitiesOfClass(ItemEntity.class, self.getBoundingBox().inflate(10.0));
            for (ItemEntity drop : nearbyDrops) {
                if (drop.getOwner() != null && drop.getOwner().is(self)) {
                    drop.setInvisible(false);
                }
            }
        }

        if (!magnetItems.isEmpty()) {
            magnetItems.removeIf(item -> {
                if (item == null || item.isRemoved()) return true;
                if (item.isInvisible()) item.setInvisible(false);

                if (item.tickCount < 5) return false;
                Vec3 targetPos = self.getBoundingBox().getCenter();
                Vec3 itemPos = item.position();
                Vec3 direction = targetPos.subtract(itemPos);
                double distance = direction.length();
                if (distance < 1.5) {
                    item.setDeltaMovement(direction.normalize().scale(0.2));
                    return false;
                }
                double speed = 0.75;
                item.setDeltaMovement(direction.normalize().scale(speed));
                if (item.tickCount >= 100) {
                    item.setNoGravity(false);
                    return true;
                }
                return false;
            });
        }

        if (!summonedKnives.isEmpty()) {
            summonedKnives.removeIf(k -> k == null || !k.isAlive() || k.isRemoved() || !k.isWaiting());
        }

        if (wantsInvis && !self.level().isClientSide) {
            if (self.tickCount % 5 == 0) {
                AABB searchArea = self.getBoundingBox().inflate(20.0);
                List<Mob> mobs = self.level().getEntitiesOfClass(Mob.class, searchArea);
                for (Mob mob : mobs) {
                    if (mob.getTarget() == self && mob.distanceTo(self) > 5.0) {
                        mob.setTarget(null);
                        mob.setLastHurtByMob(null);
                    }
                }
            }
        }

        if (self.level().isClientSide()) {
            Player clientPlayer = ClientUtil.getPlayer();
            boolean canSeeStand = false;
            if (clientPlayer != null) {
                if (clientPlayer == self) canSeeStand = true;
                else if (clientPlayer instanceof StandUser su && su.roundabout$hasAStand()) canSeeStand = true;
            }

            if (canSeeStand) {
                long time = self.tickCount;

                if (((StandUser)self).roundabout$getActive() && !wantsInvis && time % 2 == 0) {
                    net.minecraft.core.particles.SimpleParticleType[] bodyVariants = {
                            ModParticles.METALLICA_A, ModParticles.METALLICA_B,
                            ModParticles.METALLICA_C, ModParticles.METALLICA_D
                    };
                    int pIdx = (int)(time % bodyVariants.length);
                    self.level().addParticle(bodyVariants[pIdx],
                            self.getX() + (self.getRandom().nextDouble() - 0.5),
                            self.getY() + (self.getRandom().nextDouble() * self.getBbHeight()),
                            self.getZ() + (self.getRandom().nextDouble() - 0.5),
                            (double)self.getId(), 1.0d, 0d);
                }

                boolean active = isMagneticFieldActive();
                float transitionSpeed = 0.05f;
                if (active) clientFieldExpansion = Math.min(clientFieldExpansion + transitionSpeed, 1.0f);
                else clientFieldExpansion = Math.max(clientFieldExpansion - transitionSpeed, 0.0f);

                if (clientFieldExpansion > 0.0f) {
                    boolean shouldRenderParticles = false;
                    if (clientPlayer == self) shouldRenderParticles = true;
                    else if (clientPlayer.distanceTo(self) < 6.0) shouldRenderParticles = true;

                    if (shouldRenderParticles) {
                        double[] maxRadii = {4.0, 10.0};
                        for (double maxRadius : maxRadii) {
                            double currentRadius = maxRadius * clientFieldExpansion;
                            int points = 40;
                            for (int i = 0; i < points; i++) {
                                double angle = (i * 2.0 * Math.PI) / points;
                                angle += (time * 0.02);
                                double dx = Math.cos(angle) * currentRadius;
                                double dz = Math.sin(angle) * currentRadius;
                                double dy = 0.5 + (0.5 * clientFieldExpansion);
                                self.level().addParticle(ModParticles.METALLICA_FIELD_PNG,
                                        self.getX() + dx, self.getY() + dy, self.getZ() + dz,
                                        (double)self.getId(), 1.0d, 0d);
                            }
                        }
                    }
                }
            }
        }

        if (!self.level().isClientSide()) {
            float attackRange = 10.0f;
            float checkRange = 30.0f;
            boolean active = isMagneticFieldActive();

            List<Entity> targets = MainUtil.getEntitiesInRange(self.level(), self.blockPosition(), checkRange);

            for (Entity ent : targets) {
                if (ent instanceof LivingEntity victim && !ent.is(self) && MainUtil.canActuallyHitInvolved(self, ent)) {
                    StandUser data = (StandUser) victim;
                    float dist = victim.distanceTo(self);

                    if (!active) {
                        float current = data.roundabout$getMetalMeter();
                        if (current > 0) {
                            current = Math.max(0, current - 1.0f);
                            data.roundabout$setMetalMeter(current);
                        }
                    }
                    else {
                        if (dist <= attackRange && self.hasLineOfSight(victim)) {
                            if (isImmuneToMetallica(victim)) continue;

                            float distFactor = 1.0f - (dist / attackRange);
                            if (distFactor < 0) distFactor = 0;
                            float distMult = 0.5f + distFactor;

                            float speedMult = switch(getMetalMode()) {
                                case NAILS -> 2.0f;
                                case RAZORS -> 1.0f;
                                case SCISSORS -> 0.66f;
                                default -> 1.0f;
                            };

                            float buildup = speedMult * distMult;
                            float current = data.roundabout$getMetalMeter() + buildup;

                            if (current >= 100f) {
                                triggerMetalEffect(victim, data);
                                current = 0;
                            }
                            data.roundabout$setMetalMeter(current);
                        }
                        else {
                            float current = data.roundabout$getMetalMeter();
                            if (current > 0) {
                                current = Math.max(0, current - 0.5f);
                                data.roundabout$setMetalMeter(current);
                            }
                        }
                    }
                }
            }
        }
    }


    private void triggerMetalEffect(LivingEntity victim, StandUser data) {
        float dmg = (getMetalMode() == SCISSORS) ? 12.0f : (getMetalMode() == RAZORS) ? 6.0f : 3.0f;

        ResourceKey<DamageType> ironKey = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "iron_deprivation"));
        DamageSource source = null;

        if (self.level() instanceof ServerLevel sl) {
            if (sl.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).containsKey(ironKey)) {
                source = new DamageSource(sl.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ironKey), self, self);
            } else {
                source = new DamageSource(sl.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.MAGIC), self, self) {
                    @Override
                    public Component getLocalizedDeathMessage(LivingEntity killed) {
                        return Component.translatable("death.attack.iron_deprivation", killed.getDisplayName(), self.getDisplayName());
                    }
                };
            }
        } else {
            source = self.damageSources().indirectMagic(self, self);
        }

        victim.hurt(source, dmg);

        ResourceLocation bleedLoc = new ResourceLocation(Roundabout.MOD_ID, "bleed");
        if (BuiltInRegistries.MOB_EFFECT.containsKey(bleedLoc)) {
            net.minecraft.world.effect.MobEffect bleedEffect = BuiltInRegistries.MOB_EFFECT.get(bleedLoc);

            if (getMetalMode() != NAILS) {
                int amp = 0;
                if (getMetalMode() == SCISSORS) amp = 1;
                victim.addEffect(new MobEffectInstance(bleedEffect, 100, amp));
            }
        }

        switch (getMetalMode()) {
            case NAILS:
                victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                break;
            case RAZORS:
                victim.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 0));
                break;
            case SCISSORS:
                victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                break;
        }

        net.minecraft.core.particles.ParticleOptions pType = switch(getMetalMode()) {
            case NAILS -> net.hydra.jojomod.event.ModParticles.METALLICA_NAIL;
            case RAZORS -> net.hydra.jojomod.event.ModParticles.METALLICA_RAZOR;
            case SCISSORS -> net.hydra.jojomod.event.ModParticles.METALLICA_SCISSORS;
            default -> net.minecraft.core.particles.ParticleTypes.CRIT;
        };

        if (self.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            sl.sendParticles(pType,
                    victim.getX(), victim.getY() + victim.getBbHeight()/2, victim.getZ(),
                    120, 0.5, 0.5, 0.5, 0.1);
        }
    }

    public boolean executeHeal() {
        setCooldown(net.hydra.jojomod.event.index.PowerIndex.SKILL_EXTRA, 600);
        float dmg = ((IEntityAndData)self).roundabout$getLastDamageTaken();
        if (dmg <= 0) dmg = 6.0f; if (dmg > 40.0f) dmg = 40.0f;
        self.heal(dmg);
        ((IEntityAndData)self).roundabout$setLastDamageTaken(0);
        if (net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.containsKey(new net.minecraft.resources.ResourceLocation(net.hydra.jojomod.Roundabout.MOD_ID, "bleed"))) {
            self.removeEffect(net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.get(new net.minecraft.resources.ResourceLocation(net.hydra.jojomod.Roundabout.MOD_ID, "bleed")));
        }
        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), net.minecraft.sounds.SoundEvents.IRON_GOLEM_REPAIR, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.5f);
        return true;
    }

    public int roundabout$metallicaInvisibility = -1;

    public boolean toggleInvis() {
        StandUser data = (StandUser) self;
        if (data.roundabout$getMetallicaInvisibility() > -1) {
            data.roundabout$setMetallicaInvisibility(-1);
            self.removeEffect(net.minecraft.world.effect.MobEffects.INVISIBILITY);
        } else {
            ResourceLocation bleedLoc = new ResourceLocation(Roundabout.MOD_ID, "bleed");
            if (BuiltInRegistries.MOB_EFFECT.containsKey(bleedLoc)) {
                if (self.hasEffect(BuiltInRegistries.MOB_EFFECT.get(bleedLoc))) {
                    return false;
                }
            }
            data.roundabout$setMetallicaInvisibility(10000);
        }
        return true;
    }

    public boolean isInvisible() { return ((StandUser)self).roundabout$getMetallicaInvisibility() > -1; }
}