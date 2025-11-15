package net.hydra.jojomod.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Floats;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.gui.FogInventoryMenu;
import net.hydra.jojomod.client.gui.PowerInventoryMenu;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.corpses.FallenPhantom;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.paintings.RoundaboutPainting;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.substand.EncasementBubbleEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.zetalasis.networking.message.api.ModMessageEvents;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class MainUtil {

    public static boolean isClient = false;

    public static boolean isClient(){
        return isClient;
    }
    public static void setClient(){
        isClient = true;
    }


    public static boolean isMeleeDamage(DamageSource di){
        if (di.is(DamageTypes.PLAYER_ATTACK) || di.is(DamageTypes.MOB_ATTACK))
            return true;
        return false;
    }

    public static boolean isZapper(LivingEntity ent1, Entity ent2){
        if (ent1 != null && ent2 != null) {
            StandUser standComp = ((StandUser) ent1);
            int zappedId = standComp.roundabout$getZappedToID();
            if (zappedId > -1){
                Entity ent = ent2.level().getEntity(zappedId);
                if (ent != null && ent.is(ent2)){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isPlayerInFireBlock(Entity player) {
        // Get the position of the block the player is standing in
        BlockPos pos = BlockPos.containing(player.getX(), player.getY()+0.01F, player.getZ());

        // Get the block at that position
        Block block = player.level().getBlockState(pos).getBlock();

        // Check if it’s a subclass of AbstractFireBlock
        return block instanceof BaseFireBlock;
    }

    public static ArrayList<String> walkableBlocks = Lists.newArrayList();
    public static ArrayList<String> standBlockGrabBlacklist = Lists.newArrayList();
    public static ArrayList<String> naturalStandUserMobBlacklist = Lists.newArrayList();
    public static ArrayList<String> hypnotismMobBlackList = Lists.newArrayList();
    public static Set<String> foodThatGivesBloodList = Set.of();
    Map<String, FoodBloodStats> foodThatGivesBloodMap;

    public record FoodBloodStats(String id, int hunger, float saturation) {}

    public static Map<String, FoodBloodStats> parseFoodList(Set<String> entries) {
        Map<String, FoodBloodStats> map = new HashMap<>();

        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length < 4) continue; // safety check

            // Recombine the namespaced ID ("minecraft:beef" = parts[0] + ":" + parts[1])
            String id = parts[0] + ":" + parts[1];
            int hunger = Integer.parseInt(parts[2]);
            float saturation = Float.parseFloat(parts[3].replace("F", "")); // remove trailing F

            map.put(id, new FoodBloodStats(id, hunger, saturation));
        }

        return map;
    }

    public static Map<String, FoodBloodStats> foodMap;


    public static int getBloodAmount(ItemStack stack) {
        if (foodMap != null && !foodMap.isEmpty()) {
            // Get the registry name of the item
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id != null) {
                FoodBloodStats stats = foodMap.get(id.toString());
                if (stats != null) {
                    // You can use either hunger, saturation, or a combination
                    return stats.hunger(); // or stats.saturation(), depending on what you need
                }
            }
        }
        return 0;
    }

    public static float getSaturationAmount(ItemStack stack) {
        if (foodMap != null && !foodMap.isEmpty()) {
            // Get the registry name of the item
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id != null) {
                FoodBloodStats stats = foodMap.get(id.toString());
                if (stats != null) {
                    // You can use either hunger, saturation, or a combination
                    return stats.saturation(); // or stats.saturation(), depending on what you need
                }
            }
        }
        return 0;
    }

    public static boolean isBlockBlacklisted(BlockState bs){
        ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
        if (standBlockGrabBlacklist != null && !standBlockGrabBlacklist.isEmpty() && rl != null && standBlockGrabBlacklist.contains(rl.toString())){
            return true;
        }
        return false;
    }
    public static boolean isItemGrabBlacklisted(ItemStack stack){
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (standBlockGrabBlacklist != null && !standBlockGrabBlacklist.isEmpty() && rl != null && standBlockGrabBlacklist.contains(rl.toString())){
            return true;
        }
        return false;
    }
    public static boolean isMobStandUserBlacklisted(Entity ent){
        ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(ent.getType());
        if (naturalStandUserMobBlacklist != null && !naturalStandUserMobBlacklist.isEmpty() && rl != null && naturalStandUserMobBlacklist.contains(rl.toString())){
            return true;
        }
        return false;
    }
    public static boolean isHypnotismTargetBlacklisted(Entity ent){
        ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(ent.getType());
        if (hypnotismMobBlackList != null && !hypnotismMobBlackList.isEmpty() && rl != null && hypnotismMobBlackList.contains(rl.toString())){
            return true;
        }
        return false;
    }
    public static boolean isBlockWalkable(BlockState bs){
        if (!bs.isSolid()){
            return false;
        }
        ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
        if (walkableBlocks != null && !walkableBlocks.isEmpty() && rl != null && walkableBlocks.contains(rl.toString())){
            return false;
        }
        return true;
    }
    public static boolean isBlockWalkableSimplified(BlockState bs){
        ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
        if (walkableBlocks != null && !walkableBlocks.isEmpty() && rl != null && walkableBlocks.contains(rl.toString())){
            return false;
        }
        return true;
    }

    public static void clearCooldowns(Entity ent){
        if (ent instanceof LivingEntity LE){
            StandUser SU = ((StandUser) LE);
            StandPowers powers = SU.roundabout$getStandPowers();
            powers.refreshCooldowns();
            if (ent instanceof ServerPlayer SP){
                S2CPacketUtil.refreshCooldowns(SP);
            }
        }
    }

    public static boolean isKnockbackImmune(Entity ent){
        if (ent instanceof LivingEntity LE){
            StandUser SU = ((StandUser) LE);
            if (SU.roundabout$getStandPowers() instanceof PowersWalkingHeart PW && (PW.hasExtendedHeelsForWalking()
            || (SU.roundabout$getActive() && ent instanceof Mob mb && mb.onGround()))){
                return true;
            }

        }
        return false;
    }

    public static Direction getDirectionFromByte(byte bt){
        switch (bt) {
            case 1:
                return Direction.NORTH;
            case 2:
                return Direction.SOUTH;
            case 3:
                return Direction.EAST;
            case 4:
                return Direction.WEST;
            case 5:
                return Direction.UP;
            default:
                return Direction.DOWN;
        }
    }
    public static byte getByteFromDirection(Direction direction){
        switch (direction) {
            case NORTH:
                return 1;
            case SOUTH:
                return 2;
            case EAST:
                return 3;
            case WEST:
                return 4;
            case UP:
                return 5;
            default:
                return 0;
        }
    }


    public static int maxGasTicks(){
        return 200;
    }
    public static int maxBucketGasTicks(){
        return 200;
    }
    public static int maxLeapTicks(){
        return 60;
    }


    /**Additional math functions for the mod.*/

    /** This version of interpolation accommodates speed multipliers so you can control how
     * fast something moves from point A to point B.
     * Ex: the speed a stand tilts in*/
    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * (end - start))*multiplier;
    }

    /**The most basic hitbox grabber in the world*/

    public static List<Entity> getEntitiesInRange(Level level, BlockPos center, double range) {
        return getEntitiesInRange(level,center,range,null);
    }
    public static List<Entity> getEntitiesInRange(Level level, BlockPos center, double range, @Nullable Entity exception) {
        AABB area = new AABB(
                center.getX() - range, center.getY() - range, center.getZ() - range,
                center.getX() + range, center.getY() + range, center.getZ() + range
        );

        return level.getEntities(exception, area);
    }

    public static final void spreadRadialClientPacket(Entity entity,double range, boolean skipSelf, String packet, Object... vargs) {
        if (!entity.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) entity.level());
            Vec3 userLocation = new Vec3(entity.getX(),  entity.getY(), entity.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) entity.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }
                if (skipSelf && entity.is(serverPlayerEntity)) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    ModMessageEvents.sendToPlayer((ServerPlayer)serverPlayerEntity, packet,vargs);
                }
            }
        }
    }
    public static final void spreadRadialClientPacket(Level level,BlockPos pos,double range, String packet, Object... vargs) {
        if (!level.isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) level);
            Vec3 userLocation = new Vec3(pos.getX(),  pos.getY(), pos.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) level).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    ModMessageEvents.sendToPlayer((ServerPlayer)serverPlayerEntity, packet,vargs);
                }
            }
        }
    }
    public static boolean isCreativeOrInvincible(Entity ent){
        if (ent != null && (ent.isInvulnerable() || (ent instanceof Player PL && PL.isCreative()))){
            return true;
        }
        return false;
    }
    public static boolean getEntityIsTrulyInvisible(Entity ent){
        if (ent != null){
            IEntityAndData entityAndData = ((IEntityAndData) ent);
            return entityAndData.roundabout$getTrueInvisibility() > -1;
        }
        return false;
    }
    public static int getEntityTrulyInvisibleTicks(Entity ent){
        if (ent != null){
            IEntityAndData entityAndData = ((IEntityAndData) ent);
            return entityAndData.roundabout$getTrueInvisibility();
        }
        return -1;
    }
    public static boolean isMobOrItsMounts(Entity ent, Entity checkAgaist){
        if (ent != null && checkAgaist != null){
            if (ent.is(checkAgaist)){
                return true;
            }
            if (ent.hasPassenger(checkAgaist)){
                return true;
            }
            if (checkAgaist.hasPassenger(ent)){
                return true;
            }
            if (checkAgaist instanceof LivingEntity LE){
                StandUser user = ((StandUser)LE);
                StandEntity st = user.roundabout$getStand();
                if (st != null && st.is(ent)){
                    return true;
                }
            }
            if (ent instanceof LivingEntity LE){
                StandUser user = ((StandUser)LE);
                StandEntity st = user.roundabout$getStand();
                if (st != null && st.is(checkAgaist)){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isDreadBook(ItemStack stack) {
        if (stack != null && !stack.isEmpty() && stack.is(Items.BOOK)){
            Component name = stack.getHoverName();
            String str = name.getString().toLowerCase();
            if ("cha'garoth".equals(str) || "chagaroth".equals(str) || "dreadbeast".equals(str)){
                return true;
            }
        }
        return false;
    }
    public static float controlledLerpAngleDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * Mth.wrapDegrees(end - start))*multiplier;
    }public static float controlledLerpRadianDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * wrapRadians(end - start))*multiplier;
    }
    public static final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(20.0);
    public static final TargetingConditions followTargetting = TargetingConditions.forCombat().range(50.0);
    public static final TargetingConditions plsWorkTargetting = TargetingConditions.forCombat().range(20.0).ignoreInvisibilityTesting();

    public static double getWorthyOdds(Mob mob) {
        if ((isBossMob(mob) && !ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.bossMobsCanNaturallyHaveStands)
        || mob instanceof JojoNPC || isMobStandUserBlacklisted(mob)){
            return 0;
        }
        return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.worthyMobOdds;
    }
    public static boolean getIfMobIsAttacking(Mob mb){
        for (WrappedGoal wrappedGoal : ((IMob)mb).roundabout$getGoalSelector().getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();

            // Check if it's a melee or ranged attack goal
            if (goal instanceof PanicGoal) {
                // Make sure the goal is running
                if (wrappedGoal.isRunning()) {
                    return false; // Mob is actively trying to attack the player
                }
            }
        }
        return true;
    }
    public static double getStandUserOdds(Mob mob) {
        if ((isBossMob(mob) && !ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.bossMobsCanNaturallyHaveStands)
                || mob instanceof JojoNPC
        || isMobStandUserBlacklisted(mob)){
            return 0;
        } else if (mob instanceof AbstractVillager){
            return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.standUserVillagerOdds;
        }
        return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.standUserOdds;
    }
    public static double getWorthyBreedBonus(Mob mob) {
        if (mob instanceof AbstractVillager){
            return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.userAndWorthyBreedingOddsBonus *ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.multiplyAboveForVillagerBreeding;
        }
        return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.userAndWorthyBreedingOddsBonus;
    }
    public static double getStandUserBreedBonus(Mob mob) {
        if (mob instanceof AbstractVillager){
            return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.userAndWorthyBreedingOddsBonus *ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.multiplyAboveForVillagerBreeding;
        }
        return ClientNetworking.getAppropriateConfig().generalStandUserMobSettings.userAndWorthyBreedingOddsBonus;
    }
    public static boolean isHumanoid(LivingEntity LE){
        return (LE instanceof Zombie || LE instanceof AbstractSkeleton
        || LE instanceof Player || LE instanceof Piglin
                || LE instanceof JojoNPC);

    }
    public static LivingEntity homeOnWorthy(Level level, Vec3 vec3, double range) {
        List<Entity> EntitiesInRange = genHitbox(level, vec3.x, vec3.y,
                vec3.z, range, range, range);
        List<Entity> hitEntities = new ArrayList<>(EntitiesInRange) {
        };
        LivingEntity mm = null;
        double distance = -1;
        for (Entity value : hitEntities) {
            if (value instanceof LivingEntity mb){
                if (canGrantStand(mb) && (distance == -1 || mb.distanceToSqr(vec3) < distance)){
                    mm = mb;
                    distance = mb.distanceToSqr(vec3);
                }
            }
        }
        return mm;
    }

    public static ItemStack saveToDiscData(LivingEntity ent, ItemStack stack){
        if (ent instanceof Player PE && !(stack.getItem() instanceof MaxStandDiscItem)) {
            IPlayerEntity IPE = ((IPlayerEntity) PE);
            if (stack.getItem() instanceof StandDiscItem SD && !SD.standPowers.isSecondaryStand()) {
                stack.getOrCreateTagElement("Memory").putByte("Level", (byte) Math.max(IPE.roundabout$getStandLevel() - 1, 0));
                stack.getOrCreateTagElement("Memory").putInt("Experience", IPE.roundabout$getStandExp());
                stack.getOrCreateTagElement("Memory").putBoolean("BonusSkin",IPE.roundabout$getUnlockedBonusSkin());
            }
        }
        stack.getOrCreateTagElement("Memory").putByte("Skin",((StandUser)ent).roundabout$getStandSkin());
        stack.getOrCreateTagElement("Memory").putByte("Pose",((StandUser)ent).roundabout$getIdlePos());
        return stack;
    }

    public static void handleChangeItem(Player player, byte context, ItemStack stack) {
        if (context == PacketDataIndex.ITEM_SWITCH_MAIN || context == PacketDataIndex.ITEM_SWITCH_SECONDARY) {
            boolean offh = ItemStack.isSameItemSameTags(player.getOffhandItem(),stack);
            if (player.getInventory().contains(stack) || offh){
                if (stack.getItem() instanceof StandArrowItem){
                    StandArrowItem.rerollStand(player,offh,stack,context);
                }
            }
        }
    }

    public static void handleSetCreativeModeSlot(Player player, int integer, ItemStack stack, byte context) {
        if (context == PacketDataIndex.ADD_FOG_ITEM) {
            if (ClientNetworking.getAppropriateConfig().justiceSettings.enableFogBlockInventory) {
                StandUser user = ((StandUser) player);
                ServerPlayer sp = ((ServerPlayer) player);

                boolean flag = integer < 0;
                ItemStack itemstack = stack;
                if (!itemstack.isItemEnabled(player.level().enabledFeatures())) {
                    return;
                }


                if (stack == null || stack.isEmpty() || stack.getItem() instanceof AirItem)
                    return;

                if (!(stack.getItem() instanceof FogBlockItem)) {
                    Roundabout.LOGGER.warn("Attempted to give player {} item {}, but they failed the check! Justice User: {}",
                            player.getName().getString(),
                            BuiltInRegistries.ITEM.getKey(stack.getItem()),
                            (user.roundabout$getStandPowers() instanceof PowersJustice)
                    );
                    //sp.connection.disconnect(Component.literal("Exploit Detected"));
                    return;
                }

                CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack);
                if (!itemstack.isEmpty() && compoundtag != null && compoundtag.contains("x") && compoundtag.contains("y") && compoundtag.contains("z")) {
                    BlockPos blockpos = BlockEntity.getPosFromTag(compoundtag);
                    if (player.level().isLoaded(blockpos)) {
                        BlockEntity blockentity = player.level().getBlockEntity(blockpos);
                        if (blockentity != null) {
                            blockentity.saveToItem(itemstack);
                        }
                    }
                }

                boolean flag1 = integer >= 1 && integer <= 45;
                boolean flag2 = itemstack.isEmpty() || itemstack.getDamageValue() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();
                if (flag1 && flag2) {
                    player.inventoryMenu.getSlot(integer).setByPlayer(itemstack);
                    player.inventoryMenu.broadcastChanges();
                } else if (flag && flag2) {
                    if (canAddItem(itemstack,player.getInventory())){
                        addItem(player,itemstack);
                    } else {
                        player.drop(itemstack, true);
                    }
                }

            }
        }
    }




    public static boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack) || itemStack2.getCount() >= itemStack2.getMaxStackSize())) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public static boolean isWearingEitherStoneMask(Entity ent){
        return isWearingStoneMask(ent) || isWearingBloodyStoneMask(ent);
    }

    public static void activateStoneMask(Entity ent){
        if (ent instanceof LivingEntity LE && !ent.isInWater() && !ent.level().isClientSide()){
            ItemStack stack = LE.getItemBySlot(EquipmentSlot.HEAD);
            if (stack != null && !stack.isEmpty() && stack.is(ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem())){

                if (ent instanceof Player PE){
                    IFatePlayer ifp = (IFatePlayer) PE;
                    if (FateTypes.isHuman(PE)){
                        ifp.rdbt$startVampireTransformation();
                    }
                }
                ItemStack stack2 = ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem().getDefaultInstance();
                stack2.setTag(stack.getTag());
                LE.setItemSlot(EquipmentSlot.HEAD,stack2);
            }
        }
    }
    //only called at end of transformation
    public static void popOffStoneMask(Entity ent){
        if (ent instanceof LivingEntity LE){
            ItemStack helmet = LE.getItemBySlot(EquipmentSlot.HEAD);
            if (helmet != null && !helmet.isEmpty() && helmet.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem())){

                    Level level = ent.level();

                    // Do nothing if no helmet
                    if (helmet.isEmpty()) return;


                    if (!level.isClientSide) {
                        // Remove the helmet
                        ent.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        // Drop item slightly above the player’s head
                        Vec3 pos = ent.position().add(0, ent.getBbHeight() * 0.8, 0);
                        ItemEntity drop = new ItemEntity(level, pos.x, pos.y, pos.z, helmet.copy());

                        // Give it some small random velocity like a pop effect
                        drop.setDeltaMovement(
                                (level.random.nextDouble() - 0.5) * 0.2,
                                0.3 + level.random.nextDouble() * 0.2,
                                (level.random.nextDouble() - 0.5) * 0.2
                        );

                        // Shrink the player's inventory copy only after spawning the entity
                        helmet.shrink(helmet.getCount());
                        level.addFreshEntity(drop);
                    }

            }
        }
    }
    public static void clearStoneMask(Entity ent){
        if (ent instanceof LivingEntity LE){
            if (FateTypes.isTransforming(LE))
                return;

            ItemStack stack = LE.getItemBySlot(EquipmentSlot.HEAD);
            if (stack != null && !stack.isEmpty() && stack.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem())){
                ItemStack stack2 = ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem().getDefaultInstance();
                stack2.setTag(stack.getTag());
                LE.setItemSlot(EquipmentSlot.HEAD,stack2);
            }

            stack = LE.getItemBySlot(EquipmentSlot.MAINHAND);
            if (stack != null && !stack.isEmpty() && stack.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem())){
                ItemStack stack2 = ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem().getDefaultInstance();
                stack2.setTag(stack.getTag());
                LE.setItemSlot(EquipmentSlot.MAINHAND,stack2);
            }
            stack = LE.getItemBySlot(EquipmentSlot.OFFHAND);
            if (stack != null && !stack.isEmpty() && stack.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem())){
                ItemStack stack2 = ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem().getDefaultInstance();
                stack2.setTag(stack.getTag());
                LE.setItemSlot(EquipmentSlot.OFFHAND,stack2);
            }
        }
    }
    public static boolean isWearingStoneMask(Entity ent){
        if (ent instanceof LivingEntity LE){
            ItemStack stack = LE.getItemBySlot(EquipmentSlot.HEAD);
            if (stack != null && !stack.isEmpty()){
                return stack.is(ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem());
            }
        }
        return false;
    }
    public static boolean isWearingBloodyStoneMask(Entity ent){
        if (ent instanceof LivingEntity LE){
            ItemStack stack = LE.getItemBySlot(EquipmentSlot.HEAD);
            if (stack != null && !stack.isEmpty()){
                return stack.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem());
            }
        }
        return false;
    }

    public static boolean confirmIsOre(BlockState state){
        return (state.is(ModPacketHandler.PLATFORM_ACCESS.getOreTag()) || state.is(Blocks.ANCIENT_DEBRIS));
    }
    public static void extractDiscData(LivingEntity ent, StandDiscItem SD, ItemStack stack){
        StandUser user = ((StandUser)ent);
        CompoundTag $$4 = stack.getTagElement("Memory");
        if ($$4 != null) {
            if (SD instanceof MaxStandDiscItem) {
                if (ent instanceof Player PE) {
                    IPlayerEntity IPE = ((IPlayerEntity) PE);
                    IPE.roundabout$setStandLevel(SD.standPowers.getMaxLevel());
                }
            } else {
                if (ent instanceof Player PE) {
                    IPlayerEntity IPE = ((IPlayerEntity) PE);
                    if ($$4.contains("Level")) {
                        byte lvl = (byte) ($$4.getByte("Level") + 1);
                        lvl = (byte) Mth.clamp(lvl, 1, SD.standPowers.getMaxLevel());
                        IPE.roundabout$setStandLevel(lvl);

                        if ($$4.contains("Experience")) {
                            int exp = ($$4.getInt("Experience"));
                            exp = Mth.clamp(exp, 0, SD.standPowers.getExpForLevelUp(lvl));
                            IPE.roundabout$setStandExp(exp);
                        } else {
                            IPE.roundabout$setStandExp(0);
                        }
                    } else {
                        IPE.roundabout$setStandLevel((byte)1);
                        IPE.roundabout$setStandExp(0);
                    }
                    if ($$4.contains("BonusSkin")) {
                        IPE.roundabout$setUnlockedBonusSkin($$4.getBoolean("BonusSkin"));
                    } else {
                        IPE.roundabout$setUnlockedBonusSkin(false);
                    }
                }
            }

            if ($$4.contains("Skin")) {
                byte skn = ($$4.getByte("Skin"));
                user.roundabout$setStandSkin(skn);
            } else {
                user.roundabout$setStandSkin((byte) 0);
            }
            if ($$4.contains("Pose")) {
                byte skn = ($$4.getByte("Pose"));
                user.roundabout$setIdlePosX(skn);
            } else {
                user.roundabout$setIdlePosX((byte) 0);
            }
        } else {
            if (ent instanceof Player PE) {
                IPlayerEntity IPE = ((IPlayerEntity) PE);
                IPE.roundabout$setStandLevel((byte) 1);
                IPE.roundabout$setStandExp(0);
                IPE.roundabout$setUnlockedBonusSkin(false);
            };
            user.roundabout$setStandSkin((byte) 0);
            user.roundabout$setIdlePosX((byte) 0);
        }

    }
    public static LivingEntity homeOnFlier(Level level, Vec3 vec3, double range, Entity owner) {
        List<Entity> EntitiesInRange = genHitbox(level, vec3.x, vec3.y,
                vec3.z, range, range, range);
        List<Entity> hitEntities = new ArrayList<>(EntitiesInRange) {
        };
        for (Entity value : hitEntities) {
            if (value instanceof LivingEntity mb){
                if ((mb.isFallFlying() || mb instanceof Phantom|| mb instanceof FallenPhantom) && !(mb instanceof StandEntity)){
                    if (owner != null && owner.getUUID() == mb.getUUID()) {
                    } else {
                        return mb;
                    }
                }
            }
        }
        return null;
    }
    public static boolean getMobBleed(Entity Mob) {
        if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.disableBleedingAndBloodSplatters){
            return false;
        }

        if (Mob instanceof LivingEntity){
            return Mob instanceof Zombie || (Mob instanceof Animal && !(Mob instanceof SkeletonHorse) && !(Mob instanceof ZombieHorse))
                    || Mob instanceof Villager || Mob instanceof WaterAnimal || Mob instanceof WanderingTrader || Mob instanceof Witch
                    || Mob instanceof AbstractIllager || Mob instanceof Creeper || Mob instanceof Player || Mob instanceof AbstractPiglin
                    || Mob instanceof JojoNPC || Mob instanceof Zoglin || Mob instanceof Ravager
                    || Mob instanceof Spider || Mob instanceof EnderDragon || Mob instanceof EnderMan;
        }
        return false;
    }

    public static boolean canDrinkBlood(Entity mob){
        return (getMobBleed(mob) && !hasEnderBlood(mob) && mob.isAlive() && !mob.isRemoved());
    }

    public static boolean canDrinkBloodFair(Entity ent,Entity drinker){
        return canDrinkBlood(ent) && !(ent instanceof Player);
    }
    public static boolean canDrinkBloodCrit(Entity ent,Entity drinker){
        return !(ent instanceof Mob mb && mb.getTarget() != null && mb.getTarget().is(drinker));
    }


    public static void makeFaceless(Entity entity, int ticks, int power, Entity user){
        if (entity instanceof LivingEntity LE){
            if (((LivingEntity)entity).hasEffect(ModEffects.FACELESS)){
                MobEffectInstance mf = ((LivingEntity)entity).getEffect(ModEffects.FACELESS);
                if (mf != null && mf.getAmplifier() <= power && mf.getDuration() > ticks){
                    return;
                }
            }
            ((StandUser)entity).roundabout$setGlow((byte) 1);
            ((LivingEntity)entity).addEffect(new MobEffectInstance(ModEffects.FACELESS, ticks, power), user);
        }
    }

    public static void makeMobBleed(Entity target) {
        if (!(target instanceof LivingEntity LE && FateTypes.hasBloodHunger(LE))) {
            int variety = (int) Math.round(Math.random() * 4);
            Block modBlock = ModBlocks.BLOOD_SPLATTER;
            if (MainUtil.hasBlueBlood(target)) {
                modBlock = ModBlocks.BLUE_BLOOD_SPLATTER;
            } else if (MainUtil.hasEnderBlood(target)) {
                modBlock = ModBlocks.ENDER_BLOOD_SPLATTER;
            }

            if (variety != 1) {
                MainUtil.setSplatter(target.level(), target.getOnPos(), (int) Math.floor(Math.random() * 3) - 1, 0, modBlock.defaultBlockState().
                        setValue(ModBlocks.BLOOD_LEVEL, Integer.valueOf((int) Math.round(Math.random() * 3))));
            }
            if (variety != 2) {
                MainUtil.setSplatter(target.level(), target.getOnPos(), (int) Math.floor(Math.random() * 3) - 1, -1, modBlock.defaultBlockState().
                        setValue(ModBlocks.BLOOD_LEVEL, Integer.valueOf((int) Math.round(Math.random() * 3))));
            }
            if (variety != 3) {
                MainUtil.setSplatter(target.level(), target.getOnPos(), (int) Math.floor(Math.random() * 3) - 1, 1, modBlock.defaultBlockState().
                        setValue(ModBlocks.BLOOD_LEVEL, Integer.valueOf((int) Math.round(Math.random() * 3))));
            }
        }
    }
    public static boolean hasBlueBlood(Entity target){
        if (target instanceof Spider || target instanceof Bee || target instanceof Silverfish  || target instanceof Squid){
            return true;
        } else {
            return false;
        }
    }
    public static boolean hasEnderBlood(Entity target){
        if (target instanceof EnderMan || target instanceof Endermite || target instanceof EnderDragon){
            return true;
        } else {
            return false;
        }
    }

    private static boolean containsInvalidValues(double $$0, double $$1, double $$2, float $$3, float $$4) {
        return Double.isNaN($$0) || Double.isNaN($$1) || Double.isNaN($$2) || !Floats.isFinite($$4) || !Floats.isFinite($$3);
    }

    private static double clampHorizontal(double $$0) {
        return Mth.clamp($$0, -3.0E7, 3.0E7);
    }

    private static double clampVertical(double $$0) {
        return Mth.clamp($$0, -2.0E7, 2.0E7);
    }
    public static void handleMovePilot(double getX, double getY, double getZ, float getYRot, float getXRot,
                                       Player player, int entityInt) {
        Entity entity = player.level().getEntity(entityInt);
        if (entity != null) {
            if (containsInvalidValues(getX, getY, getZ, getYRot, getXRot)) {
            } else {
                if (entity != player) {
                    ServerLevel serverlevel = (ServerLevel) player.level();
                    double d0 = entity.getX();
                    double d1 = entity.getY();
                    double d2 = entity.getZ();
                    double d3 = clampHorizontal(getX);
                    double d4 = clampVertical(getY);
                    double d5 = clampHorizontal(getZ);
                    float f = Mth.wrapDegrees(getYRot);
                    float f1 = Mth.wrapDegrees(getXRot);

                    boolean flag = serverlevel.noCollision(entity, entity.getBoundingBox().deflate(0.0625D));
                    boolean flag1 = entity.verticalCollisionBelow;
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingentity = (LivingEntity) entity;
                        if (livingentity.onClimbable()) {
                            livingentity.resetFallDistance();
                        }
                    }

                    entity.absMoveTo(d3, d4, d5, f, f1);
                    entity.moveTo(d3, d4, d5, f, f1);
                }

            }
        }
    }
    public static boolean canCauseRejection(Entity ent){
        if (ent instanceof Mob ME){
            if (!(ME instanceof WitherBoss) && !(ME instanceof EnderDragon) && !(ME instanceof Warden)){
                if (((StandUser)ME).roundabout$getStandDisc().isEmpty()){
                    return true;
                }
            }
        } else if (ent instanceof Player PE){
            if (PE.experienceLevel < ClientNetworking.getAppropriateConfig().itemSettings.levelsToGetStand && ((StandUser) PE).roundabout$getStandDisc().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public static boolean canGrantStand(Entity ent){
        if (ent instanceof Mob ME){
            if (!(ME instanceof StandEntity)){
                if (((StandUser)ME).roundabout$getStandDisc().isEmpty()){
                    return ((IMob)ME).roundabout$isWorthy();
                }
            }
        } else if (ent instanceof Player PL){
            if (ClientNetworking.getAppropriateConfig().itemSettings.canAwakenOtherPlayersWithArrows){
                if (((StandUser)PL).roundabout$getStandDisc().isEmpty()){
                    return PL.experienceLevel >= ClientNetworking.getAppropriateConfig().itemSettings.levelsToGetStand;
                }
            }
        }
        return false;
    }

    public static float gasDamageMultiplier(){
        return (float) (0.78F * (ClientNetworking.getAppropriateConfig().itemSettings.gasolineExplosionDamage *0.01));
    }

    public static void makeBleed(Entity entity, int level, int ticks, Entity source){
        if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.disableBleedingAndBloodSplatters){
            return;
        }
        if (getMobBleed(entity)){
            if (!hasEnderBlood(entity) && !hasBlueBlood(entity)) {
                if (source != null && isWearingEitherStoneMask(source) && source.distanceTo(entity) < 5) {
                    activateStoneMask(source);
                } else if (isWearingStoneMask(entity)) {
                    activateStoneMask(entity);
                }
            }

            ((StandUser)entity).roundabout$setBleedLevel(level);
            ((LivingEntity)entity).addEffect(new MobEffectInstance(ModEffects.BLEED, ticks, level), source);
        }
    }

    /**Imported Chrosu fruit TP code for ender blood*/
    public static void randomChorusTeleport(LivingEntity entity){
        Level $$1 = entity.level();
        if (!$$1.isClientSide) {
            double $$4 = entity.getX();
            double $$5 = entity.getY();
            double $$6 = entity.getZ();

            for (int $$7 = 0; $$7 < 16; $$7++) {
                double $$8 = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
                double $$9 = Mth.clamp(
                        entity.getY() + (double) (entity.getRandom().nextInt(16) - 8),
                        (double) $$1.getMinBuildHeight(),
                        (double) ($$1.getMinBuildHeight() + ((ServerLevel) $$1).getLogicalHeight() - 1)
                );
                double $$10 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                Vec3 $$11 = entity.position();
                if (randomTeleport(entity,$$8, $$9, $$10, true)) {
                    entity.hurtMarked = true;
                    entity.hasImpulse = true;
                    $$1.gameEvent(GameEvent.TELEPORT, $$11, GameEvent.Context.of(entity));
                    SoundEvent $$12 = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    $$1.playSound(null, $$4, $$5, $$6, $$12, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.playSound($$12, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    public static boolean randomTeleport(LivingEntity ent, double $$0, double $$1, double $$2, boolean $$3) {
        double $$4 = ent.getX();
        double $$5 = ent.getY();
        double $$6 = ent.getZ();
        double $$7 = $$1;
        boolean $$8 = false;
        BlockPos $$9 = BlockPos.containing($$0, $$1, $$2);
        Level $$10 = ent.level();
        if ($$10.hasChunkAt($$9)) {
            boolean $$11 = false;

            while (!$$11 && $$9.getY() > $$10.getMinBuildHeight()) {
                BlockPos $$12 = $$9.below();
                BlockState $$13 = $$10.getBlockState($$12);
                if ($$13.blocksMotion()) {
                    $$11 = true;
                } else {
                    $$7--;
                    $$9 = $$12;
                }
            }

            if ($$11) {
                ent.teleportTo($$0, $$7, $$2);
                if (ent instanceof Player){
                    ((IEntityAndData)ent).roundabout$setQVec2Params(new Vec3($$0, $$7, $$2));
                }
                if ($$10.noCollision(ent) && !$$10.containsAnyLiquid(ent.getBoundingBox())) {
                    $$8 = true;
                }
            }
        }

        if (!$$8) {
            ent.teleportTo($$4, $$5, $$6);
            if (ent instanceof Player){
                ((IEntityAndData)ent).roundabout$setQVec2Params(new Vec3($$0, $$7, $$2));
            }
            return false;
        } else {
            if ($$3) {
                $$10.broadcastEntityEvent(ent, (byte)46);
            }

            if (ent instanceof PathfinderMob) {
                ((PathfinderMob)ent).getNavigation().stop();
            }

            return true;
        }
    }

    public static List<Entity> genHitbox(Level level, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ) {
        double k = Mth.floor(startX - radiusX);
        double l = Mth.floor(startX + radiusX);
        double r = (startY - radiusY);
        double s = (startY + radiusY);
        double t = (startZ - radiusZ);
        double u = (startZ + radiusZ);
        return level.getEntities(null, new AABB(k, r, t, l, s, u));
    }

    public static List<net.minecraft.world.entity.Entity> hitbox(List<net.minecraft.world.entity.Entity> entities){

        List<net.minecraft.world.entity.Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.showVehicleHealth() || value.isInvulnerable() || !value.isAlive() || value instanceof StandEntity){
                hitEntities.remove(value);
            }
        }
        return hitEntities;
    }
    public static List<net.minecraft.world.entity.Entity> hitboxGas(List<net.minecraft.world.entity.Entity> entities){

        List<net.minecraft.world.entity.Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if ((!value.showVehicleHealth() && !(value instanceof GasolineCanEntity)) || value.isInvulnerable() || !value.isAlive()){
                hitEntities.remove(value);
            }
        }
        return hitEntities;
    }

    public static float wrapRadians(float radians) {
        radians *= Mth.RAD_TO_DEG;
        float f = radians % 360.0f;
        if (f >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return (f*Mth.DEG_TO_RAD);
    }

    public static Vec3 getMoveRelative(float $$0, Vec3 $$1, Entity ett) {
        return getInputVector($$1, $$0, ett.getYRot());
    }

    private static Vec3 getInputVector(Vec3 $$0, float $$1, float $$2) {
        double $$3 = $$0.lengthSqr();
        if ($$3 < 1.0E-7) {
            return Vec3.ZERO;
        } else {
            Vec3 $$4 = ($$3 > 1.0 ? $$0.normalize() : $$0).scale((double)$$1);
            float $$5 = Mth.sin($$2 * (float) (Math.PI / 180.0));
            float $$6 = Mth.cos($$2 * (float) (Math.PI / 180.0));
            return new Vec3($$4.x * (double)$$6 - $$4.z * (double)$$5, $$4.y, $$4.z * (double)$$6 + $$4.x * (double)$$5);
        }
    }

    public static double fixAngle(float angle){
       return (Math.abs(angle) % 360);
    }

    public static void slowTarget(Entity e, float p) {
        e.hurtMarked = true;
        e.setDeltaMovement(e.getDeltaMovement().scale(p));
        e.hasImpulse = true;
    }

    public static void takeKnockbackWithY(Entity entity, double strength, double x, double y, double z) {

        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }

        if (MainUtil.isKnockbackImmune(entity)){
            return;
        }
        takeUnresistableKnockbackWithY(entity,strength,x,y,z);
    }
    public static void takeUnresistableKnockbackWithY(Entity entity, double strength, double x, double y, double z) {
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        entity.setDeltaMovement(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.hasImpulse = true;
    }
    public static void takeLiteralUnresistableKnockbackWithY(Entity entity,  double x, double y, double z) {
        entity.hurtMarked = true;
        entity.setDeltaMovement(x,
                y,
               z);
        entity.hasImpulse = true;
    }
    public static void takeUnresistableKnockbackWithYBias(Entity entity, double strength, double x, double y, double z, float yBias) {
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        Vec3 vec3d3 = vec3d2.multiply(yBias,1,yBias);
        entity.setDeltaMovement(- vec3d3.x,
                -vec3d3.y,
                - vec3d3.z);
        entity.hasImpulse = true;
    }
    public static void takeUnresistableKnockbackWithY2(Entity entity,double x, double y, double z) {
        entity.hurtMarked = true;
        entity.setDeltaMovement( x,
                y,
                z);
        entity.hasImpulse = true;
    }

    public static void ejectInFront(StandEntity stand){
        if (stand.getFirstPassenger() != null && stand.getUser() != null){
            Entity entity = stand.getFirstPassenger();
            stand.ejectPassengers();
            if (entity.level().dimensionTypeId() == stand.getUser().level().dimensionTypeId()) {
                if (entity instanceof Player ent) {
                    ((IEntityAndData) ent).roundabout$setQVec2Params(new Vec3(stand.getUser().getX(), stand.getUser().getY(), stand.getUser().getZ()));
                } else {
                    entity.dismountTo(stand.getUser().getX(), stand.getUser().getY(), stand.getUser().getZ());
                }
            }
        }
    }

    public static void knockback(Entity entity, double d, double e, double f) {
        if (entity instanceof LivingEntity le && (d *= 1.0 - le.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        if (MainUtil.isKnockbackImmune(entity)){
            return;
        }

        entity.hasImpulse = true;
        Vec3 vec3 = entity.getDeltaMovement();
        Vec3 vec32 = new Vec3(e, 0.0, f).normalize().scale(d);
        entity.setDeltaMovement(vec3.x / 2.0 - vec32.x, entity.onGround() ? Math.min(0.4, vec3.y / 2.0 + d) : vec3.y, vec3.z / 2.0 - vec32.z);
        entity.hurtMarked = true;
    }
    public static void knockbackWithoutBumpUp(Entity entity, double d, double e, double f) {
        if (entity instanceof LivingEntity le && (d *= 1.0 - le.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        if (MainUtil.isKnockbackImmune(entity)){
            return;
        }
        entity.hasImpulse = true;
        Vec3 vec3 = entity.getDeltaMovement();
        Vec3 vec32 = new Vec3(e, 0.0, f).normalize().scale(d);
        entity.setDeltaMovement(vec3.x / 2.0 - vec32.x, vec3.y, vec3.z / 2.0 - vec32.z);
        entity.hurtMarked = true;
    }

    public static double lengthdir_x(double length, double angle) {
        return length * (Math.cos(toRadians(angle))) * -1;
    }

    public static double lengthdir_z(double length, double angle) {
        return length * (Math.sin(toRadians(angle)));
    }

    public static double toRadians(double angle) {
        return angle * (Math.PI / 180);
    }

    public static double cheapDistanceTo(double x,double y,double z,double x2,double y2,double z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(y-y2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }
    public static double cheapDistanceTo2(double x,double z,double x2,double z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }
    public static int cheapDistanceTo2(int x,int z,int x2,int z2){
        int mdist = 0;
        int cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }

    public static double cheapDistanceTo(int x,int y,int z,int x2,int y2,int z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(y-y2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }

    public static boolean isPlayerNearby(Vec3 pos, Level level, double range, int exemptID) {
        if (level instanceof ServerLevel) {
            ServerLevel serverWorld = ((ServerLevel) level);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = serverWorld.players().get(j);

                if (serverPlayerEntity.level() != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (serverPlayerEntity.getId() != exemptID && blockPos.closerToCenterThan(pos, range)) {
                    return true;
                }
            }
        }
        return false;
    }
    //Couldn't find better wording but this tests if you're on claimed land that isn't yours
    //It doesn't need the block hit to be on a claim
    public static boolean canPlaceOnClaim(Player player,BlockHitResult blockHit){
        //Seems counterintuitive but most abilities have their own ways of handling this, so I'll just make it return True.

        if(!ClientNetworking.getAppropriateConfig().griefSettings.doExtraGriefChecksForClaims || !MainUtil.getIsGamemodeApproriateForGrief(player)){
            return true;

        }
        boolean isLiquid = (!player.level().getBlockState(blockHit.getBlockPos()).isSolid() && !player.level().getBlockState(blockHit.getBlockPos()).isAir());
        BlockState replace = player.level().getBlockState(blockHit.getBlockPos());
        //Always correct, but for some reason I need to put it as a conditional
        if(Blocks.BARRIER.asItem() instanceof  BlockItem barrier){
            barrier.place(new BlockPlaceContext(player,player.getUsedItemHand(),barrier.getDefaultInstance(),blockHit));
            BlockPos placedBPos = blockHit.getBlockPos().relative(blockHit.getDirection());
            player.level().destroyBlock(placedBPos,false,player);
            if(!player.level().getBlockState(placedBPos).isAir()){
                player.level().removeBlock(placedBPos,false);
                if(isLiquid) {
                    player.level().setBlock(blockHit.getBlockPos(), replace, 0);
                }
                return false;
            }
            if(isLiquid) {
                player.level().setBlock(blockHit.getBlockPos(), replace, 0);
            }
        }
        return true;
    }

    public static void gasExplode(BlockState blk, ServerLevel level, BlockPos blkPos, int iteration, int hitRadius, int blockRadius, float power){
        if (!level.isClientSide){
            level.sendParticles(ParticleTypes.LAVA, blkPos.getX() + 0.5, blkPos.getY(), blkPos.getZ() + 0.5,
                    2, 0.0, 0.0, 0.0, 0.4);
            if (iteration == 0){
                SoundEvent $$6 = ModSounds.GASOLINE_EXPLOSION_EVENT;
                level.playSound(null, blkPos, $$6, SoundSource.BLOCKS, 10F, 1F);
                level.sendParticles(ParticleTypes.EXPLOSION, blkPos.getX()+0.5F, blkPos.getY()+0.5F, blkPos.getZ()+0.5F,
                        1, 0.1, 0.1, 0.1, 0.2);
            }

            List<Entity> entities = MainUtil.hitboxGas(MainUtil.genHitbox(level, blkPos.getX(), blkPos.getY(),
                    blkPos.getZ(), hitRadius, hitRadius+1, hitRadius));
            if (!entities.isEmpty()) {
                DamageSource $$5 = ModDamageTypes.of(level,ModDamageTypes.GASOLINE_EXPLOSION);
                for (Entity value : entities) {
                    if (value instanceof GasolineCanEntity){
                        value.remove(Entity.RemovalReason.DISCARDED);
                        gasExplode(null, level, value.getOnPos(), iteration + 1, 1, blockRadius, power);
                        break;
                    }
                    if (!value.fireImmune()) {
                        value.setSecondsOnFire(15);
                        float np = power;
                        if (value instanceof LivingEntity){
                            ((StandUser)value).roundabout$setGasolineTime(-1);
                            if (value instanceof Player){
                                np *= 0.31F;
                            }
                            int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, (LivingEntity) value);
                            np = (float) (np * (1-(f*0.045)));
                        }
                        if (value instanceof LivingEntity && ((LivingEntity)value).hasEffect(MobEffects.FIRE_RESISTANCE)){
                            MobEffectInstance instance = ((LivingEntity)value).getEffect(MobEffects.FIRE_RESISTANCE);
                            ((LivingEntity)value).removeEffect(MobEffects.FIRE_RESISTANCE);
                            value.hurt($$5,np);
                            ((LivingEntity)value).addEffect(instance);
                        } else {
                            value.hurt($$5,np);
                        }
                    }
                }
            }
        }

        if (blk != null) {
            level.removeBlock(blkPos, false);
        }

        Set<BlockPos> gasList = Sets.newHashSet();
        if (!level.isClientSide && iteration < 8) {
            for (int x = -blockRadius; x < blockRadius; x++) {
                for (int y = -blockRadius; y < blockRadius; y++) {
                    for (int z = -blockRadius; z < blockRadius; z++) {
                        BlockPos blkPo2 = new BlockPos(blkPos.getX() + x, blkPos.getY()+y, blkPos.getZ() + z);
                        BlockState state = level.getBlockState(blkPo2);
                        Block block = state.getBlock();

                        if (block instanceof GasolineBlock) {
                            boolean ignited = state.getValue(GasolineBlock.IGNITED);
                            if (!ignited){
                                state = state.setValue(GasolineBlock.IGNITED, Boolean.valueOf(true));
                                level.setBlock(blkPo2, state, 1);
                                gasList.add(blkPo2);
                            }
                        }
                    }
                }
            }
            if (!gasList.isEmpty()) {
                for (BlockPos gasPuddle : gasList) {
                    BlockState state = level.getBlockState(gasPuddle);
                    gasExplode(state, level, gasPuddle, iteration + 1, hitRadius, blockRadius, power);
                }
            }
        }


    }


    /**For new Locacaca mechanics and rendering*/
    public static LivingEntity getStoneTarget(Level $$0, LivingEntity $$1){
        List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox($$0, $$1.getX(), $$1.getY(),
                $$1.getZ(), 5, 5, 5));
        double maxDistance = 5;
        LivingEntity target = null;
        if (!entities.isEmpty()) {
            for (Entity value : entities) {
                if (value instanceof LivingEntity && value.getUUID() != $$1.getUUID() && !(value instanceof StandEntity)
                        && !(value instanceof FallenMob)) {
                    double distance = value.position().distanceTo($$1.position());
                    if (distance <= maxDistance && ((StandUser)value).roundabout$getLocacacaCurse() < 0){
                        target = (LivingEntity) value;
                        maxDistance = distance;
                    }
                }
            }
        }

        return target;
    }

    public static Entity getTargetEntity(LivingEntity User, float distance){
        return getTargetEntity(User,distance,25);
    }
    public static Entity getTargetEntity(LivingEntity User, float distance, int angle){
        /*First, attempts to hit what you are looking at*/
            getDistanceOut(User, distance, false);
        Entity targetEntity = rayCastEntity(User,distance);
        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        if (targetEntity == null) {
            float halfReach = (float) (distance*0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(User, halfReach);
            targetEntity = AttackHitboxNear(User, GrabHitbox(User, DamageHandler.genHitbox(User, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distance, angle),distance);
        }

        if (targetEntity instanceof EnderDragonPart EDP){
            targetEntity = EDP.parentMob;
        }
        return targetEntity;
    }

    public static float getDistanceOut(LivingEntity entity, float range, boolean offset){
        float distanceFront = getRayDistance(entity, range);
        if (offset) {
            Entity targetEntity = rayCastEntity(entity,range);
            if (targetEntity != null && targetEntity.distanceTo(entity) < distanceFront) {
                distanceFront = targetEntity.distanceTo(entity);
            }
            distanceFront -= 1;
            distanceFront = Math.max(Math.min(distanceFront, 1.7F), 0.4F);
        }
        return distanceFront;
    }

    public static float getRayDistance(LivingEntity entity, float range){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (blockHit.getType() != HitResult.Type.MISS){
            return Mth.sqrt((float) entity.distanceToSqr(blockHit.getLocation()));
        }
        return range;
    }


    public static boolean isThrownBlockItem(Item item){
        if (item instanceof BlockItem){
            Block blk = ((BlockItem)item).getBlock();
            if (item instanceof ItemNameBlockItem || blk instanceof BushBlock
                    || blk instanceof WebBlock  || blk instanceof BarbedWireBundleBlock
                    || blk instanceof TorchBlock
                    || blk instanceof FrogspawnBlock
                    || blk instanceof CauldronBlock
                    || blk instanceof BellBlock
                    || blk instanceof SnowLayerBlock
                    || blk instanceof TurtleEggBlock
                    || blk instanceof CarpetBlock
                    || blk instanceof SugarCaneBlock
                    || blk instanceof GrowingPlantHeadBlock
                    || blk instanceof StereoBlock
                    || blk instanceof FlowerPotBlock
                    || blk instanceof DaylightDetectorBlock
                    || blk instanceof VineBlock
                    || blk instanceof SnifferEggBlock
                    || blk instanceof BasePressurePlateBlock
                    || blk instanceof PowderSnowBlock
                    || blk instanceof SkullBlock
                    || blk instanceof RodBlock
                    || blk instanceof HorizontalDirectionalBlock
                    || blk instanceof FleshBlock
                    || (blk instanceof SimpleWaterloggedBlock && !(blk instanceof LeavesBlock))){
                return false;
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static boolean tryPlaceBlock(Entity ent, BlockPos pos){
        return tryPlaceBlock(ent, pos, false);
    }

    @SuppressWarnings("deprecation")
    public static boolean tryPlaceBlock(Entity ent, BlockPos pos, boolean liquid){
        if (ent != null && !ent.level().isClientSide()) {
            BlockState state = ent.level().getBlockState(pos);
            if (state.isAir() || (state.canBeReplaced() && getIsGamemodeApproriateForGrief(ent) && (!state.liquid() || liquid) &&
                    ent.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING) &&
                    !((ent instanceof Player PL &&
                            (PL.blockActionRestricted(ent.level(), pos, ((ServerPlayer)
                                    ent).gameMode.getGameModeForPlayer()))) ||
                            (ent instanceof Player && !ent.level().mayInteract(((Player) ent), pos))))) {
                return true;
            }
        }
        return false;
    }

    public static List<Entity> GrabHitbox(LivingEntity User, List<Entity> entities, float maxDistance, int angle){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.showVehicleHealth() || value.isInvulnerable() || !value.isAlive() || (User.isPassenger() && User.getVehicle().getUUID() == value.getUUID())
            || (User instanceof StandEntity SE && SE.getUser() != null &&  SE.getUser().isPassenger() && SE.getUser().getVehicle().getUUID() == value.getUUID())){
                hitEntities.remove(value);
            } else {
                if (!(angleDistance(getLookAtEntityYaw(User, value), (User.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(User, value), User.getXRot()) <= angle)){
                    hitEntities.remove(value);
                }
            }
        }
        return hitEntities;
    }

    public static int getTargetEntityId(LivingEntity User, float distance){
        return getTargetEntityId(User,distance,25);
    }

    public static int getTargetEntityId(LivingEntity User, float distance, int angle){
        Entity targetEntity = getTargetEntity(User, distance, angle);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }
    public static boolean isStandDamage(DamageSource sauce){
        if (sauce.is(ModDamageTypes.STAND) || sauce.is(ModDamageTypes.PENETRATING_STAND) || sauce.is(ModDamageTypes.STAR_FINGER)
                || sauce.is(ModDamageTypes.STAND_RUSH)|| sauce.is(ModDamageTypes.CROSSFIRE)|| sauce.is(ModDamageTypes.EXPLOSIVE_STAND)
                || sauce.is(ModDamageTypes.GO_BEYOND)
                || sauce.is(ModDamageTypes.CORPSE) || sauce.is(ModDamageTypes.CORPSE_EXPLOSION) || sauce.is(ModDamageTypes.CORPSE_ARROW)
                || sauce.is(ModDamageTypes.MELTING)
                || sauce.is(ModDamageTypes.HEEL_SPIKE)
                || sauce.is(ModDamageTypes.CREAM_VOID_BALL)
                || sauce.is(ModDamageTypes.ANUBIS_POSSESS)
                || sauce.is(ModDamageTypes.ANUBIS_SPIN) ){
            return true;
        }
        return false;
    }
    public static boolean isCorpseDamage(DamageSource sauce){
        if (sauce.is(ModDamageTypes.CORPSE) || sauce.is(ModDamageTypes.CORPSE_EXPLOSION) || sauce.is(ModDamageTypes.CORPSE_ARROW)){
            return true;
        }
        return false;
    }
    public static boolean isArmorBypassingButNotShieldBypassing(DamageSource sauce){
        if (sauce.is(ModDamageTypes.STAND) || sauce.is(ModDamageTypes.CORPSE) || sauce.is(ModDamageTypes.EXPLOSIVE_STAND)  || sauce.is(ModDamageTypes.HEEL_SPIKE)  ||
                sauce.is(ModDamageTypes.CORPSE_ARROW) ||  sauce.is(ModDamageTypes.STAND_RUSH) ||  sauce.is(ModDamageTypes.CROSSFIRE) ||
                sauce.is(ModDamageTypes.CORPSE_EXPLOSION)) {
            return true;
        }
        return false;
    }


    /**Returns the vertical angle between two mobs*/
    public static float getLookAtEntityPitch(Entity user, Entity targetEntity) {
        double f;
        double d = targetEntity.getEyePosition().x - user.getEyePosition().x;
        double e = targetEntity.getEyePosition().z - user.getEyePosition().z;
        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)targetEntity;
            f = livingEntity.getEyePosition().y - user.getEyePosition().y;
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - user.getEyePosition().y;
        }
        double g = Math.sqrt(d * d + e * e);
        return (float)(-(Mth.atan2(f, g) * 57.2957763671875));
    }
    public static Vec2 getRotationsBetween(Vec3 from, Vec3 to) {
        Vec3 delta = to.subtract(from);
        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        float yRot = (float) (Math.toDegrees(Math.atan2(-dx, dz)));
        float xRot = (float) (Math.toDegrees(-Math.atan2(dy, horizontalDistance)));

        return new Vec2(xRot, yRot); // Vec2 is (xRot, yRot)
    }

    /**Returns the horizontal angle between two mobs*/
    public static float getLookAtEntityYaw(Entity user, Entity targetEntity) {
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }
    public static float getLookAtEntityYawWithAngle(Vec3 user, Entity targetEntity) {
        double d = targetEntity.getX() - user.x();
        double e = targetEntity.getZ() - user.z();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }
    public static float getLookAtEntityYawWithAngles(Vec3 user, Vec3 target) {
        double d = target.x() - user.x();
        double e = target.z() - user.z();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }
    public static float getLookAtEntityEyeYawWithAngle(Vec3 user, Entity targetEntity) {
        double d = targetEntity.getEyePosition().x() - user.x();
        double e = targetEntity.getEyePosition().z() - user.z();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        float distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }

    /**Creative players should only be rewound by themselves*/
    public static boolean canRewindInTime(Entity ent, Entity rewinder){
        if (!ent.isRemoved() && ent.isAlive()) {
            if ((ent instanceof Player PE && PE.isCreative()) && rewinder != null && !rewinder.is(ent)){
                return false;
            }
            if (ent instanceof ArmorStand || ent instanceof Painting || ent instanceof RoundaboutPainting)
                return false;
            return true;
        }
        return false;
    }

    public static boolean canHaveSightTaken(LivingEntity LE){
        if (LE instanceof Sniffer || LE instanceof Bat || LE instanceof Dolphin){
            return false;
        }

        if (LE instanceof Mob mb && ((IMob)mb).roundabout$getSightProtectionTicks() > 0){
            return false;
        }

        return !(isBossMob(LE) && ClientNetworking.getAppropriateConfig().softAndWetSettings.bossesCannotLoseSight);
    }

    /**Is it a potion effect that should not be easily discarded or plundered, or has special particles*/
    public static boolean isSpecialEffect(MobEffectInstance value){
        return isSpecialEffect(value.getEffect());
    }
    public static boolean isSpecialEffect(MobEffect value){
        return value.equals(ModEffects.BLEED) || value.equals(ModEffects.FACELESS) ||
                value.equals(ModEffects.CAPTURING_LOVE) || value.equals(ModEffects.MELTING);
    }
    public static boolean canHaveFrictionTaken(LivingEntity LE){
        if (LE.onClimbable()){
            return false;
        }
        if (((StandUser)LE).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.hasExtendedHeelsForWalking()){
            return false;
        }
        return !(isBossMob(LE) && ClientNetworking.getAppropriateConfig().softAndWetSettings.bossesCannotLoseFriction);
    }
    public static boolean canBeScoopedUpInBubble(LivingEntity LE){
        return !(isBossMob(LE)) && LE.getBbWidth() < ClientNetworking.getAppropriateConfig().softAndWetSettings.widthOfMobBubbleMobsStolen && LE.getBbHeight() < ClientNetworking.getAppropriateConfig().softAndWetSettings.sizeOfMobBubbleMobsStolen && !(LE.getVehicle() instanceof SoftAndWetPlunderBubbleEntity);
    }

    /**No Armor Stands*/
    public static boolean isActuallyALivingEntityNoCap(Entity LE){
        return LE instanceof Mob || LE instanceof Player;
    }
    public static HitResult getHitResultOnMoveVector(Entity $$0, Predicate<Entity> $$1) {
        Vec3 $$2 = $$0.getDeltaMovement();
        Level $$3 = $$0.level();
        Vec3 $$4 = $$0.position();
        return getHitResult($$4, $$0, $$1, $$2, $$3);
    }

    public static BlockHitResult getAheadVec(Entity entity,float distOut){
        Vec3 vec3d = entity.getEyePosition(1f);
        Vec3 vec3d2 = entity.getViewVector(1f);
        return entity.level().clip(new ClipContext(vec3d, vec3d.add(vec3d2.x * distOut,
                vec3d2.y * distOut, vec3d2.z * distOut), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                entity));
    }
    public static BlockHitResult getAheadVecRender(Entity entity,float distOut, float rendertick){
        Vec3 vec3d = entity.getEyePosition(rendertick);
        Vec3 vec3d2 = entity.getViewVector(1f);
        return entity.level().clip(new ClipContext(vec3d, vec3d.add(vec3d2.x * distOut,
                vec3d2.y * distOut, vec3d2.z * distOut), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                entity));
    }

    /**Generate pointer on block or entity position*/
    public static Vec3 getRaytracePointOnMobOrBlock(Entity source, float range){
        EntityHitResult targetEntity = rayCastEntityHitResult(source,range);
        if (targetEntity != null && targetEntity.getEntity() != null && canActuallyHit(source,targetEntity.getEntity())){
            return targetEntity.getLocation();
        }

        Vec3 vec3d = source.getEyePosition(0);
        Vec3 vec3d2 = source.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = source.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,source));
        return blockHit.getLocation();

    }
    public static Vec3 getRaytracePointOnMobOrBlock(Entity source, float range, float distance){
        EntityHitResult targetEntity = rayCastEntityHitResult(source,range);
        if (targetEntity != null && targetEntity.getEntity() != null && canActuallyHit(source,targetEntity.getEntity())){
            return targetEntity.getLocation();
        }

        Vec3 vec3d = source.getEyePosition(0);
        Vec3 vec3d2 = source.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = source.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,source));
        return blockHit.getLocation().relative(blockHit.getDirection(),distance);

    }
    public static Vec3 getRaytracePointOnMobOrBlockIfNotUp(Entity source, float range, float distance){
        EntityHitResult targetEntity = rayCastEntityHitResult(source,range);
        if (targetEntity != null && targetEntity.getEntity() != null && canActuallyHit(source,targetEntity.getEntity())){
            return targetEntity.getLocation();
        }

        Vec3 vec3d = source.getEyePosition(0);
        Vec3 vec3d2 = source.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = source.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,source));

        if (blockHit.getType().equals(HitResult.Type.MISS))
            return null;

        if (blockHit.getDirection().equals(Direction.UP))
            return blockHit.getLocation();
        Vec3 vec= new Vec3(blockHit.getBlockPos().getX(),blockHit.getBlockPos().getY(),blockHit.getBlockPos().getZ());
        vec = vec.add(0.5f,0.5f,0.5f).relative(blockHit.getDirection(),0.5).relative(blockHit.getDirection(),distance);
        return vec;

    }

    public static float hasModifiedPartialVisibility(Entity entity){
        float basis = 1F;
        if (getEntityIsTrulyInvisible(entity)){
            basis *=0.4F;
        }
        return basis;
    }
    public static boolean canActuallyHitInvolved(Entity self, Entity entity){
        if (entity instanceof SoftAndWetPlunderBubbleEntity){
            return false;
        }
        if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.generalDetectionGoThroughDoorsAndCorners){
            return true;
        }
        Vec3 from = new Vec3(self.getX(), self.getY(), self.getZ()); // your position
        Vec3 to = entity.getEyePosition(1.0F); // where the entity's eyes are

        BlockHitResult result = self.level().clip(new ClipContext(
                from,
                to,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                self
        ));
        boolean isBlocked = result.getType() != HitResult.Type.MISS &&
                result.getLocation().distanceTo(from) < to.distanceTo(from);

        Vec3 from2 = new Vec3(entity.getX(), entity.getY(), entity.getZ()); // your position
        Vec3 to2 = self.getEyePosition(1.0F); // where the entity's eyes are

        BlockHitResult result2 = self.level().clip(new ClipContext(
                from2,
                to2,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                self
        ));
        boolean isBlocked2 = result2.getType() != HitResult.Type.MISS &&
                result2.getLocation().distanceTo(from2) < to2.distanceTo(from2);


        Vec3 from3 = new Vec3(entity.getX(), entity.getY(), entity.getZ()); // your position
        Vec3 to3 = self.getEyePosition(1.0F); // where the entity's eyes are

        BlockHitResult result3 = self.level().clip(new ClipContext(
                from3,
                to2,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                self
        ));
        boolean isBlocked3 = result3.getType() != HitResult.Type.MISS &&
                result3.getLocation().distanceTo(from3) < to3.distanceTo(from3);

        return !isBlocked || !isBlocked2 || !isBlocked3;
    }

    public static boolean canActuallyHit(Entity self, Entity entity){
//        if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.generalDetectionGoThroughDoorsAndCorners){
//            return true;
//        }
//        Vec3 from = new Vec3(self.getX(), self.getY(), self.getZ()); // your position
//        Vec3 to = entity.getEyePosition(1.0F); // where the entity's eyes are
//
//        BlockHitResult result = self.level().clip(new ClipContext(
//                from,
//                to,
//                ClipContext.Block.COLLIDER,
//                ClipContext.Fluid.NONE,
//                self
//        ));
//        boolean isBlocked = result.getType() != HitResult.Type.MISS &&
//                result.getLocation().distanceTo(from) < to.distanceTo(from);
        // return !isBlocked
        return canActuallyHitInvolved(self,entity);
    }
    public static boolean isStandPickable(Entity entity){
        if (entity instanceof SoftAndWetPlunderBubbleEntity sbe){
            if (entity.level().isClientSide() && ClientUtil.getPlayer() != null && ClientUtil.getPlayer().getId() == sbe.getUserID()) {
                return false;
            } else {
                return false;
                //return sbe.getActivated() && !sbe.isPopPlunderBubbble();
            }
        } else if (entity instanceof EncasementBubbleEntity sbe){
            return false;
        }
        return entity.isPickable();
    }

    private static HitResult getHitResult(Vec3 $$0, Entity $$1, Predicate<Entity> $$2, Vec3 $$3, Level $$4) {
        Vec3 $$5 = $$0.add($$3);
        HitResult $$6 = $$4.clip(new ClipContext($$0, $$5, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, $$1));
        if ($$6.getType() != HitResult.Type.MISS) {
            $$5 = $$6.getLocation();
        }

        HitResult $$7 = getEntityHitResult($$4, $$1, $$0, $$5, $$1.getBoundingBox().expandTowards($$3).inflate(1.5), $$2);
        if ($$7 != null) {
            $$6 = $$7;
        }

        return $$6;
    }

    @Nullable
    public static EntityHitResult getEntityHitResult(Level $$0, Entity $$1, Vec3 $$2, Vec3 $$3, AABB $$4, Predicate<Entity> $$5, float $$6) {
        double $$7 = Double.MAX_VALUE;
        Entity $$8 = null;

        for (Entity $$9 : $$0.getEntities($$1, $$4, $$5)) {
            AABB $$10 = $$9.getBoundingBox().inflate((double)$$6);
            Optional<Vec3> $$11 = $$10.clip($$2, $$3);
            if ($$11.isPresent()) {
                double $$12 = $$2.distanceToSqr($$11.get());
                if ($$12 < $$7) {
                    $$8 = $$9;
                    $$7 = $$12;
                }
            }
        }

        return $$8 == null ? null : new EntityHitResult($$8);
    }
    @Nullable
    public static EntityHitResult getEntityHitResult(Level $$0, Entity $$1, Vec3 $$2, Vec3 $$3, AABB $$4, Predicate<Entity> $$5) {
        return getEntityHitResult($$0, $$1, $$2, $$3, $$4, $$5, 0.3F);
    }
    @Nullable
    public static EntityHitResult getEntityHitResult(Entity $$0, Vec3 $$1, Vec3 $$2, AABB $$3, Predicate<Entity> $$4, double $$5) {
        Level $$6 = $$0.level();
        double $$7 = $$5;
        Entity $$8 = null;
        Vec3 $$9 = null;

        for (Entity $$10 : $$6.getEntities($$0, $$3, $$4)) {
            AABB $$11 = $$10.getBoundingBox().inflate((double)$$10.getPickRadius());
            Optional<Vec3> $$12 = $$11.clip($$1, $$2);
            if ($$11.contains($$1)) {
                if ($$7 >= 0.0) {
                    $$8 = $$10;
                    $$9 = $$12.orElse($$1);
                    $$7 = 0.0;
                }
            } else if ($$12.isPresent()) {
                Vec3 $$13 = $$12.get();
                double $$14 = $$1.distanceToSqr($$13);
                if ($$14 < $$7 || $$7 == 0.0) {
                    if ($$10.getRootVehicle() == $$0.getRootVehicle()) {
                        if ($$7 == 0.0) {
                            $$8 = $$10;
                            $$9 = $$13;
                        }
                    } else {
                        $$8 = $$10;
                        $$9 = $$13;
                        $$7 = $$14;
                    }
                }
            }
        }

        return $$8 == null ? null : new EntityHitResult($$8, $$9);
    }
    public static boolean isBossMob(LivingEntity LE){
        if (LE instanceof Warden || LE instanceof EnderDragon || LE instanceof WitherBoss){
            return true;
        }
        return false;
    }

    public static Entity AttackHitboxNear(Entity User, List<Entity> entities, float distance) {
        float nearestDistance = -1;
        Entity nearestMob = null;

        if (entities != null) {
            for (Entity value : entities) {
                if (!value.isInvulnerable() && value.isAlive() && value.getUUID() != User.getUUID()) {
                    float distanceTo = value.distanceTo(User);
                    if ((nearestDistance < 0 || distanceTo < nearestDistance) && distanceTo <= distance) {
                        if (canActuallyHit(value,User)) {
                            nearestDistance = distanceTo;
                            nearestMob = value;
                        }
                    }
                }
            }
        }
        return nearestMob;
    }
    public static Entity rayCastEntity(Entity entityX, float reach){
        float tickDelta = 0;
        if (entityX.level().isClientSide()) {
            tickDelta = ClientUtil.getDelta();
        }
        Vec3 vec3d = entityX.getEyePosition(tickDelta);

        Vec3 vec3d2 = entityX.getViewVector(1.0f);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        AABB box = new AABB(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entityX, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.isPickable() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null){
            Entity hitResult = entityHitResult.getEntity();
            if (hitResult.isAlive() && !hitResult.isRemoved() && canActuallyHit(entityX,hitResult)) {
                return hitResult;
            }
        }
        return null;
    }
    public static EntityHitResult rayCastEntityHitResult(Entity entityX, float reach){
        float tickDelta = 0;
        if (entityX.level().isClientSide()) {
            tickDelta = ClientUtil.getDelta();
        }
        Vec3 vec3d = entityX.getEyePosition(tickDelta);

        Vec3 vec3d2 = entityX.getViewVector(1.0f);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        AABB box = new AABB(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entityX, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.isPickable() && !entity.isInvulnerable() && !(entityX instanceof SoftAndWetBubbleEntity), reach*reach);
        if (entityHitResult != null){
            Entity hitResult = entityHitResult.getEntity();
            if (hitResult.isAlive() && !hitResult.isRemoved()) {
                return entityHitResult;
            }
        }
        return null;
    }

    /**Guard breaking*/
    public static StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }
    public static boolean knockShield(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= getUserData((LivingEntity) entity);
                    if (!standUser.roundabout$isGuarding()) {
                        if (entity instanceof Player){
                            ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                            Item item = itemStack.getItem();
                            if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                                ((LivingEntity) entity).releaseUsingItem();
                                ((Player) entity).stopUsingItem();
                            }
                            ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                            entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean forceAggression(LivingEntity LE){
        if (LE != null){
            StandUser user = ((StandUser) LE);
            return (user.roundabout$hasAStand() || user.roundabout$getZappedToID() > -1);
        }
        return false;
    }

    public static boolean knockShieldPlusStand(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= getUserData((LivingEntity) entity);
                    if (standUser.roundabout$isGuarding()) {
                        if (!standUser.roundabout$getGuardBroken()){
                            standUser.roundabout$breakGuard();
                        }
                    }
                    if (entity instanceof Player){
                        ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                        Item item = itemStack.getItem();
                        if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                            ((LivingEntity) entity).releaseUsingItem();
                            ((Player) entity).stopUsingItem();
                        }
                        ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                        entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**Code for determining if it is appropriate to place a splatter down*/
    public static boolean canPlaceSplatter(Level level,BlockPos pos, int offsetX, int offsetY, int offsetZ){
        BlockPos blk =  new BlockPos(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

        if (level.isEmptyBlock(blk)) {
            BlockPos $$8 = blk.below();
            if (level.getBlockState($$8).isFaceSturdy(level, $$8, Direction.UP)) {
                return true;
            }
        }

        return false;
    }

    public static void setSplatter(Level level,BlockPos pos, int offsetX, int offsetZ, BlockState state){
        BlockPos blockPos = null;
        if (canPlaceSplatter(level, pos, offsetX, +1, offsetZ)) {
            blockPos = new BlockPos(pos.getX() + offsetX, pos.getY() + 1, pos.getZ() + offsetZ);
        } else if (canPlaceSplatter(level, pos, offsetX, +2, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY() + 2,pos.getZ()+offsetZ);
        } else if (canPlaceSplatter(level, pos, offsetX, 0, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY(),pos.getZ()+offsetZ);
        } else if (canPlaceSplatter(level, pos, offsetX, -1, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY()-1,pos.getZ()+offsetZ);
        }
        //if (this.level().getBlockState(pos).getBlock())
        if (blockPos != null) {
            level.setBlockAndUpdate(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), state);
        }
    }
    public static final TargetingConditions OFFER_TARGER_CONTEXT = TargetingConditions.forNonCombat().range(64.0D);

    public static void handShake(ServerPlayer player){
        Networking.sendConfigToPlayer(player);
    }

    public static void handShakeCooldowns(ServerPlayer player){
        ((StandUser)player).roundabout$getStandPowers().syncAllCooldowns();
        S2CPacketUtil.affirmCooldownsS2C(player);
    }


    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/

    public static void handleBytePacketC2S(Player player, byte data, byte context){
        if (context == PacketDataIndex.BYTE_UPDATE_COOLDOWN) {
            ((StandUser) player).roundabout$getStandPowers().setCooldown(data, -1);
        } else if (context == PacketDataIndex.BYTE_STRIKE_POSE) {
            ((IPlayerEntity) player).roundabout$SetPoseEmote(data);
        } else if (context == PacketDataIndex.BYTE_CORPSE_TACTICS) {
            if (((StandUser) player).roundabout$getStandPowers() instanceof PowersJustice PJ){
                PJ.justiceTacticsUse(data);
            }
        } else if (context == PacketDataIndex.BYTE_CHANGE_MORPH) {
            StandPowers sp = ((StandUser) player).roundabout$getStandPowers();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(data);
            if (sp instanceof PowersJustice pj){

                if (ShapeShifts.isVillager(shift) && !sp.canExecuteMoveWithLevel(pj.getVillagerMorphLevel())){
                    return;
                }if (ShapeShifts.isZombie(shift) && !sp.canExecuteMoveWithLevel(pj.getZombieMorphLevel())){
                    return;
                }if (ShapeShifts.isSkeleton(shift) && !sp.canExecuteMoveWithLevel(pj.getSkeletonMorphLevel())){
                    return;
                }
                if (shift == ShapeShifts.VILLAGER){
                    byte totalMorph = 0;
                    Villager ent = player.level().getNearestEntity(Villager.class, OFFER_TARGER_CONTEXT, player, player.getX(), player.getY(), player.getZ(),player.getBoundingBox().inflate(12.0D, 2.0D, 12.0D));
                    if (ent != null){
                        VillagerType VT = ent.getVillagerData().getType();
                        VillagerProfession VP = ent.getVillagerData().getProfession();
                        totalMorph = (byte) (ShapeShifts.getByteFromType(VT) + ShapeShifts.getByteFromProfession(VP));
                    } else {
                        VillagerType VT = VillagerType.byBiome(player.level().getBiome(player.blockPosition()));
                        totalMorph = (byte) (ShapeShifts.getByteFromType(VT) + 1);
                    }


                    ((IPlayerEntity) player).roundabout$setShapeShiftExtraData(totalMorph);
                }
                ((IPlayerEntity) player).roundabout$shapeShift();
                ((IPlayerEntity) player).roundabout$setShapeShift(data);
            }

        }
    }
    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/
    public static void handleSingleBytePacketC2S(Player player, byte context){
        if (context == PacketDataIndex.SINGLE_BYTE_SILENT_SUMMON) {
            ((StandUser) player).roundabout$summonStand(player.level(), false, false);

        } else if (context == PacketDataIndex.SINGLE_BYTE_SKIN_LEFT) {
                StandUser user = ((StandUser) player);
                user.roundabout$getStandPowers().getSkinInDirection(false,false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_SKIN_RIGHT) {
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getSkinInDirection(true,false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_SKIN_LEFT_SEALED) {
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getSkinInDirection(false,true);
        } else if (context == PacketDataIndex.SINGLE_BYTE_SKIN_RIGHT_SEALED) {
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getSkinInDirection(true,true);
        } else if (context == PacketDataIndex.SINGLE_BYTE_IDLE_LEFT) {
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getPoseInDirection(false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_IDLE_RIGHT){
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getPoseInDirection(true);
        } else if (context == PacketDataIndex.SINGLE_BYTE_OPEN_POWER_INVENTORY) {
            StandUser standUser = ((StandUser) player);
            standUser.roundabout$getStandPowers().setCooldown(context,-1);
            IPlayerEntity iplay = ((IPlayerEntity) player);
            byte unlocked = 0;
            if (iplay.roundabout$getUnlockedBonusSkin()){
                unlocked = 1;
            }

            S2CPacketUtil.sendByteBundleToClientPacket(((ServerPlayer) player), PacketDataIndex.S2C_BUNDLE_POWER_INV,
                    standUser.roundabout$getStandSkin(), unlocked);

            if (player.containerMenu != player.inventoryMenu) {
                player.containerMenu = player.inventoryMenu;
            }

            ((IPlayerEntityServer)player).roundabout$nextContainerCounter();
            int cid = ((IPlayerEntityServer)player).roundabout$getCounter();
            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) player), PacketDataIndex.S2C_POWER_INVENTORY,
                    cid);
            player.containerMenu = new PowerInventoryMenu(player.getInventory(), true, player,cid);
            ((IPlayerEntityServer)player).roundabout$initMenu(player.containerMenu);
        } else if (context == PacketDataIndex.SINGLE_BYTE_OPEN_FOG_INVENTORY) {
            player.containerMenu = new FogInventoryMenu(player.getInventory(), !player.level().isClientSide, player);
            ((IPlayerEntityServer)player).roundabout$initMenu(player.containerMenu);
        } else if (context == PacketDataIndex.SINGLE_BYTE_GLAIVE_START_SOUND) {
            ((StandUser) player).roundabout$getStandPowers().playSoundsIfNearby(SoundIndex.GLAIVE_CHARGE, 10, false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_ITEM_STOP_SOUND) {
            ((StandUser) player).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.ITEM_GROUP, 30,false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_STAND_ARROW_START_SOUND) {
            ((StandUser) player).roundabout$getStandPowers().playSoundsIfNearby(SoundIndex.STAND_ARROW_CHARGE, 10, false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_SCOPE) {
            if (player != null && ((StandUser)player).roundabout$getStand() instanceof StarPlatinumEntity SE){
                SE.setScoping(true);
                if (ClientNetworking.getAppropriateConfig().starPlatinumSettings.starPlatinumScopeUsesPotionEffectForNightVision) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000000, 21, false, false), null);
                }
            }
        } else if (context == PacketDataIndex.SINGLE_BYTE_SCOPE_OFF) {
            if (player != null && ((StandUser)player).roundabout$getStand() instanceof StarPlatinumEntity SE){
                SE.setScoping(false);
                if (ClientNetworking.getAppropriateConfig().starPlatinumSettings.starPlatinumScopeUsesPotionEffectForNightVision) {
                    MobEffectInstance ME = player.getEffect(MobEffects.NIGHT_VISION);
                    if (ME != null && ME.getDuration() >= 100000 && ME.getDuration() >= 100000 && ME.getAmplifier() > 20) {
                        player.removeEffect(MobEffects.NIGHT_VISION);
                    }
                }
            }
        } else if (context == PacketDataIndex.SINGLE_BYTE_FORWARD_BARRAGE) {
            if (player != null && ((StandUser)player).roundabout$getStandPowers().attackTimeDuring >= 0){
                ((StandUser)player).roundabout$getStandPowers().forwardBarrage = true;
                ((StandUser)player).roundabout$getStandPowers().moveStarted = true;
                ((StandUser)player).roundabout$getStandPowers().poseStand(OffsetIndex.LOOSE);
                StandEntity SE = ((StandUser)player).roundabout$getStand();
                if (SE != null){
                    SE.setPos(player.getPosition(1).add(player.getForward().scale(2)).add(0,0.2F,0));
                    SE.setYRot(player.getYHeadRot() % 360);
                }
            }
        } else if (context == PacketDataIndex.SINGLE_BYTE_SMELT) {
            if (player != null) {

                 if (player.containerMenu instanceof AbstractFurnaceMenu fm) {
                    Container ct = ((IAbstractFurnaceMenu)fm).roundabout$getContainer();
                    if (ct instanceof AbstractFurnaceBlockEntity fbe){

                        int it = ClientNetworking.getAppropriateConfig().magiciansRedSettings.magiciansRedFurnaceTicks;
                        if (it > 0) {
                            ((IAbstractFurnaceBlockEntity) fbe).roundabout$setFurnaceHeatingTime(it);
                        }
                    }
                 }
            }
        } else if (context == PacketDataIndex.SINGLE_BYTE_DESUMMON) {
            if (player != null) {
                StandUser user = ((StandUser) player);
                if (user.roundabout$getActive()){
                    user.roundabout$summonStand(player.level(), false, false);
                }
            }
        } else if (context == PacketDataIndex.END_BLOOD_SPEED) {
            if (player != null) {
                if (((IFatePlayer)player).rdbt$getFatePowers() instanceof VampiricFate vp){
                    vp.setSpeedActivated(0);
                }
            }
        }
    }

    public static boolean getIsGamemodeApproriateForGrief(Entity Li){
        if (Li != null && !Li.level().isClientSide()) {
            if ((!(Li instanceof Player) || (((ServerPlayer) Li).gameMode.getGameModeForPlayer() != GameType.SPECTATOR
                    && ((ServerPlayer) Li).gameMode.getGameModeForPlayer() != GameType.ADVENTURE))
                    && Li.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVampireBurnTick(LivingEntity ent) {
        if (ent.level().isDay() && !ent.level().isClientSide) {
            float $$0 = ent.getLightLevelDependentMagicValue();
            BlockPos $$1 = BlockPos.containing(ent.getEyePosition().x, ent.getEyePosition().y, ent.getEyePosition().z);
            if ($$0 > 0.5F && ent.level().random.nextFloat() * 30.0F < ($$0 - 0.4F) * 2.0F && ent.level().canSeeSky($$1)) {
                return true;
            }
        }

        return false;
    }

    /**A generalized packet for sending floats to the server. Context is what to do with the data byte*/
    public static void handleFloatPacketC2S(Player player, float data, byte context){
        if (context == PacketDataIndex.FLOAT_VELOCITY_BARBED_WIRE) {
            if (player.getVehicle() != null){
                if (player.getVehicle().hurt(ModDamageTypes.of(player.level(), ModDamageTypes.BARBED_WIRE), data)){
                    MainUtil.makeBleed(player.getVehicle(),0,200,null);
                }
            } else {
                if (player.hurt(ModDamageTypes.of(player.level(), ModDamageTypes.BARBED_WIRE), data)){
                    MainUtil.makeBleed(player,0,200,null);
                }
            }
        } else if (context == PacketDataIndex.FLOAT_STAR_FINGER_SIZE) {
            if (((StandUser)player).roundabout$getStand() instanceof StarPlatinumEntity SP){
                SP.setFingerLength(data);
            }
        } else if (context == PacketDataIndex.FLOAT_DISTANCE_OUT) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setDistanceOut(data);
        } else if (context == PacketDataIndex.FLOAT_SIZE_PERCENT) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setSizePercent(data);
        } else if (context == PacketDataIndex.FLOAT_IDLE_ROTATION) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setIdleRotation(data);
        } else if (context == PacketDataIndex.FLOAT_IDLE_Y_OFFSET) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setIdleYOffset(data);
        } else if (context == PacketDataIndex.FLOAT_UPDATE_STAND_MOVE) {
            ((StandUser)player).roundabout$getStandPowers().updateMove(data);
        } else if (context == PacketDataIndex.FLOAT_BIG_JUMP) {
            ((StandUser)player).roundabout$setBigJump(true);
            ((StandUser)player).roundabout$setBigJumpCurrentProgress(data);
        } else if (context == PacketDataIndex.FLOAT_BIG_JUMP_CANCEL) {
            ((StandUser)player).roundabout$setBigJump(false);
            ((StandUser)player).roundabout$setBigJumpCurrentProgress(data);
        }
    }
    @SuppressWarnings("deprecation")
    public static void handleIntPacketC2S(Player player, int data, byte context){
        if (context == PacketDataIndex.INT_GLAIVE_TARGET){
            Entity target = player.level().getEntity(data);

            target.hurt(ModDamageTypes.of(player.level(), ModDamageTypes.GLAIVE), data);
        } else if (context == PacketDataIndex.INT_TS_TIME){
            if (((StandUser)player).roundabout$getStandPowers().getChargedTSTicks() > data) {
                ((StandUser)player).roundabout$getStandPowers().setChargedTSTicks(data);
            }
        } else if (context == PacketDataIndex.INT_RIDE_TICKS){
            ((StandUser)player).roundabout$setRestrainedTicks(data);
        } else if (context == PacketDataIndex.INT_STAND_ATTACK){
            Entity target = player.level().getEntity(data);
            ((StandUser)player).roundabout$getStandPowers().handleStandAttack(player,target);
        } else if (context == PacketDataIndex.INT_STAND_ATTACK_2){
            Entity target = player.level().getEntity(data);
            ((StandUser)player).roundabout$getStandPowers().handleStandAttack2(player,target);
        } else if (context == PacketDataIndex.INT_ANCHOR_PLACE){
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setAnchorPlace(data);
        } else if (context == PacketDataIndex.INT_ANCHOR_PLACE_ATTACK){
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setAnchorPlaceAttack(data);
        } else if (context == PacketDataIndex.INT_UPDATE_MOVE){
            ((StandUser)player).roundabout$getStandPowers().updateIntMove(data);
        } else if (context == PacketDataIndex.INT_UPDATE_PILOT){
            StandEntity SE = ((StandUser)player).roundabout$getStand();
            if (SE != null){
                BlockPos veci3 = BlockPos.containing(new Vec3(SE.getEyePosition().x, SE.getEyePosition().y, SE.getEyePosition().z));
                BlockState bl3 = SE.level().getBlockState(veci3);
                if (!(bl3.isSolid() && (bl3.getBlock().isCollisionShapeFullBlock(bl3,player.level(),veci3) ||
                        (bl3.getBlock() instanceof SlabBlock ||
                        bl3.getBlock() instanceof StairBlock)))){
                    ((StandUser)player).roundabout$getStandPowers().setPiloting(data);
                }
            } else {
                ((StandUser)player).roundabout$getStandPowers().setPiloting(data);
            }
        } else if (context == PacketDataIndex.INT_INDEX_OF_VISAGE_LEVEL){
            if (ModItems.getVisageStore().size() >= data){
                VisageStoreEntry entry = ModItems.getVisageStore().get(data);
                if (player.experienceLevel >= entry.costL){
                    addItem(player,entry.stack.copy());
                    player.giveExperienceLevels(-entry.costL);

                    SoundEvent $$6 = ModSounds.CINDERELLA_SPARKLE_EVENT;
                    player.level().playSound(null, BlockPos.containing(player.position()), $$6, SoundSource.BLOCKS, 1F, 1F);
                }
            }
        } else if (context == PacketDataIndex.INT_INDEX_OF_VISAGE_EMERALDS){
            if (ModItems.getVisageStore().size() >= data){
                VisageStoreEntry entry = ModItems.getVisageStore().get(data);
                    int i = 0;
                    for(int $$5 = 0; $$5 < player.getInventory().getContainerSize(); ++$$5) {
                        ItemStack $$6 = player.getInventory().getItem($$5);
                        if ($$6.getItem().equals(Items.EMERALD)){
                            i+=$$6.getCount();
                        }
                    }
                    if (i >= entry.costE){
                        addItem(player,entry.stack.copy());
                        int j = 0;
                        for(int $$5 = 0; $$5 < player.getInventory().getContainerSize(); ++$$5) {
                            ItemStack $$6 = player.getInventory().getItem($$5);
                            if ($$6.getItem().equals(Items.EMERALD)){
                                int ct = $$6.getCount();
                                if ((j + ct) > entry.costE){
                                    $$6.shrink(entry.costE);
                                    j+= entry.costE;
                                } else {
                                    $$6.shrink(ct);
                                    j+= ct;
                                }
                            }
                            if (j >= entry.costE){
                                break;
                            }
                        }

                        SoundEvent $$6 = ModSounds.CINDERELLA_SPARKLE_EVENT;
                        player.level().playSound(null, BlockPos.containing(player.position()), $$6, SoundSource.BLOCKS, 1F, 1F);
                    }
            }
        } else if (context == PacketDataIndex.INT_RELLA_START){
            Entity target = player.level().getEntity(data);
            if (target instanceof Aesthetician aes){
                aes.addPlayerToList(player);
            }
        } else if (context == PacketDataIndex.INT_RELLA_CANCEL){
            Entity target = player.level().getEntity(data);
            if (target instanceof Aesthetician aes){
                aes.removePlayerFromList(player);
            }
        }
    }
    public static void addItem(Player player, ItemStack stack){
        ItemEntity $$4 = new ItemEntity(player.level(), player.getEyePosition().x,
                player.getEyePosition().y, player.getEyePosition().z,
                stack);
        $$4.setPickUpDelay(0);
        $$4.setThrower(player.getUUID());
        player.level().addFreshEntity($$4);
    }

    public static void syncActivePower(Player pl, byte activePower){

        StandPowers powers = ((StandUser) pl).roundabout$getStandPowers();

        if (powers.activePower != activePower){
            if (activePower == PowerIndex.NONE){
                powers.setAttackTimeDuring(-1);
            } else {
                powers.setAttackTimeDuring(0);
            }
        }
        powers.updateMovesFromPacket(activePower);
        powers.setActivePower(activePower);
        powers.kickStartClient();

    }

    public static void syncActivePowerFate(Player pl, byte activePower){

        FatePowers powers = ((IFatePlayer) pl).rdbt$getFatePowers();

        if (powers.activePower != activePower){
            if (activePower == PowerIndex.NONE){
                powers.setAttackTimeDuring(-1);
            } else {
                powers.setAttackTimeDuring(0);
            }
        }
        powers.updateMovesFromPacket(activePower);
        powers.setActivePower(activePower);
        powers.kickStartClient();

    }
    public static void syncActivePowerPowers(Player pl, byte activePower){

        //FILL THIS IN FOR POWERS

    }
    public static void syncCooldownsForAttacks(int attackTime, int attackTimeMax, int attackTimeDuring,
                                               byte activePower, byte activePowerPhase, Player pl){

        StandPowers powers = ((StandUser) pl).roundabout$getStandPowers();

        if (powers.activePower != activePower){
            if (activePower == PowerIndex.NONE){
                powers.setAttackTimeDuring(-1);
            } else {
                powers.setAttackTimeDuring(0);
            }
        }
        powers.updateMovesFromPacket(activePower);
        powers.setActivePower(activePower);
        powers.kickStartClient();
    }

    public static Entity raytraceEntity(Level world, LivingEntity player, double maxDistance) {
        Vec3 eyePos = player.getEyePosition(1.0F); // player.getEyePosition(float)
        Vec3 lookVec = player.getViewVector(1.0F); // player.getViewVector(float)
        Vec3 reachVec = eyePos.add(lookVec.scale(maxDistance)); // end point of the ray

        // Raytrace blocks first
        ClipContext blockContext = new ClipContext(
                eyePos,
                reachVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult blockHit = world.clip(blockContext);
        double blockHitDistance = blockHit != null
                ? blockHit.getLocation().distanceTo(eyePos)
                : maxDistance;

        // Search for potential target entities in bounding box
        AABB box = player.getBoundingBox().expandTowards(lookVec.scale(maxDistance)).inflate(1.0);
        List<Entity> candidates = world.getEntities(player, box,
                (e) -> e instanceof LivingEntity && e.isPickable() && e.isAlive());

        Entity closest = null;
        double closestDistance = blockHitDistance;

        for (Entity entity : candidates) {
            AABB aabb = entity.getBoundingBox().inflate(0.3); // widen the target hit box a bit
            Optional<Vec3> hitOptional = aabb.clip(eyePos, reachVec);

            if (hitOptional.isPresent()) {
                double hitDistance = eyePos.distanceTo(hitOptional.get());
                if (hitDistance < closestDistance) {
                    closestDistance = hitDistance;
                    closest = entity;
                }
            }
        }

        return closest; // null if no valid hit
    }


    public static Entity raytraceEntityStand(Level world, LivingEntity player, double maxDistance) {

        Vec3 eyePos = player.getEyePosition(1.0F); // player.getEyePosition(float)
        Vec3 lookVec = player.getViewVector(1.0F); // player.getViewVector(float)
        Vec3 reachVec = eyePos.add(lookVec.scale(maxDistance)); // end point of the ray

        // Raytrace blocks first
        ClipContext blockContext = new ClipContext(
                eyePos,
                reachVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult blockHit = world.clip(blockContext);
        double blockHitDistance = blockHit != null
                ? blockHit.getLocation().distanceTo(eyePos)
                : maxDistance;

        // Search for potential target entities in bounding box
        AABB box = player.getBoundingBox().expandTowards(lookVec.scale(maxDistance)).inflate(1.0);
        List<Entity> candidates = world.getEntities(player, box,
                (e) -> e instanceof Entity && e.isPickable() && e.isAlive());

        Entity closest = null;
        double closestDistance = blockHitDistance;

        for (Entity entity : candidates) {
            AABB aabb = entity.getBoundingBox().inflate(0.3); // widen the target hit box a bit
            Optional<Vec3> hitOptional = aabb.clip(eyePos, reachVec);

            if (hitOptional.isPresent()) {
                double hitDistance = eyePos.distanceTo(hitOptional.get());
                if (hitDistance < closestDistance && !entity.isSpectator() && MainUtil.isStandPickable(entity) && !entity.isInvulnerable()
                && !entity.hasPassenger(player)) {
                    closestDistance = hitDistance;
                    closest = entity;
                }
            }
        }

        return closest; // null if no valid hit
    }


    public static boolean isHoldingRoadRoller(Entity ent){
        if (ent instanceof LivingEntity LE){
            ItemStack hand = LE.getMainHandItem();
            ItemStack offHand = LE.getOffhandItem();
            if ((!hand.isEmpty() && hand.getItem() instanceof RoadRollerItem) || (!offHand.isEmpty() && offHand.getItem() instanceof RoadRollerItem)){
                return true;
            }
        }
        return false;
    }


    public static Entity raytraceEntityStandThroughWalls(Level world, LivingEntity player, double maxDistance) {

        Vec3 eyePos = player.getEyePosition(1.0F); // player.getEyePosition(float)
        Vec3 lookVec = player.getViewVector(1.0F); // player.getViewVector(float)
        Vec3 reachVec = eyePos.add(lookVec.scale(maxDistance)); // end point of the ray

        // Raytrace blocks first
        ClipContext blockContext = new ClipContext(
                eyePos,
                reachVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult blockHit = world.clip(blockContext);
        double blockHitDistance = maxDistance;

        //
        // Search for potential target entities in bounding box
        AABB box = player.getBoundingBox().expandTowards(lookVec.scale(maxDistance)).inflate(1.0);
        List<Entity> candidates = world.getEntities(player, box,
                (e) -> e instanceof Entity && e.isPickable() && e.isAlive());

        Entity closest = null;
        double closestDistance = blockHitDistance;

        for (Entity entity : candidates) {
            AABB aabb = entity.getBoundingBox().inflate(0.3); // widen the target hit box a bit
            Optional<Vec3> hitOptional = aabb.clip(eyePos, reachVec);

            if (hitOptional.isPresent()) {
                double hitDistance = eyePos.distanceTo(hitOptional.get());
                if (hitDistance < closestDistance && !entity.isSpectator() && MainUtil.isStandPickable(entity) && !entity.isInvulnerable()
                        && !entity.hasPassenger(player)) {
                    closestDistance = hitDistance;
                    closest = entity;
                }
            }
        }

        return closest; // null if no valid hit
    }

}
