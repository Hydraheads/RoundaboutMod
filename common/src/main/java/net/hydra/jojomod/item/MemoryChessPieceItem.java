package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ChessPieceBlockEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.IVillagerAccess;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemoryChessPieceItem extends BlockItem implements Vanishable {
    public MemoryChessPieceItem(Block $$0, Properties $$1) {
        super($$0,$$1.defaultDurability(3));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }
    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level,
                                                 @Nullable Player player, ItemStack stack, BlockState state) {

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof ChessPieceBlockEntity chess) {
            chess.setStoredStack(stack);
        }

        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return this.getDescriptionId();
    }
    /**Default 72000*/
    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return;
        }

        int usedTime = this.getUseDuration(stack) - timeLeft;
        int itemTime = 7;

        if (usedTime < itemTime) {
            return;
        }

        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return;
        }


        if (!tag.contains("vicName")) {
            return;
        }

        String name = tag.getString("vicName");
        if (name.isBlank()) {
            return;
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.CINDERELLA_SPARKLE_EVENT,
                SoundSource.NEUTRAL,
                1F,
                1F
        );

        player.displayClientMessage(
                Component.translatable(
                        "text.roundabout.memory." + tag.getInt("stealType")
                ).withStyle(ChatFormatting.AQUA),
                true
        );

        int stealType = tag.getInt("stealType");

        if (stealType == 10) {
            if (tag.contains("experience", Tag.TAG_INT)) {
                int xp = tag.getInt("experience");

                if (xp > 0) {
                    player.giveExperiencePoints(xp);

                    // Destroy the memory piece
                    stack.shrink(1);
                    return;
                }
            }
        }
        if (stealType != 12 && stealType != 13 && stealType != 14) {
            return;
        }

        // Check stored position exists
        if (!tag.contains("Pos") || !tag.contains("Dimension")) {
            return;
        }

        String dimensionString = tag.getString("Dimension");
        if (dimensionString.isBlank()) {
            return;
        }

        ResourceLocation dimensionLocation;
        try {
            dimensionLocation = new ResourceLocation(dimensionString);
        } catch (Exception e) {
            return;
        }

        ResourceKey<Level> storedDimension = ResourceKey.create(
                Registries.DIMENSION,
                dimensionLocation
        );

        if (!level.dimension().equals(storedDimension)) {
            return;
        }

        BlockPos targetPos = BlockPos.of(tag.getLong("Pos"));

        Vec3 start = player.position()
                .add(0, player.getEyeHeight(), 0);

        Vec3 target = Vec3.atCenterOf(targetPos);

        Vec3 direction = target.subtract(start);

        // Avoid normalize() on zero-length vectors
        if (direction.lengthSqr() == 0) {
            return;
        }

        direction = direction.normalize();

        serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                start.x,
                start.y,
                start.z,
                0,
                direction.x * 0.5,
                direction.y * 0.5,
                direction.z * 0.5,
                1.8
        );
        serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                start.x,
                start.y,
                start.z,
                0,
                direction.x * 0.5,
                direction.y * 0.5,
                direction.z * 0.5,
                1.5
        );
        serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                start.x,
                start.y,
                start.z,
                0,
                direction.x * 0.5,
                direction.y * 0.5,
                direction.z * 0.5,
                1.2
        );
    }
    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    public static ItemStack initializePiece(ItemStack stack, Entity victim, int stealType){
        if (victim instanceof LivingEntity livingEntity){
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("victim",victim.getUUID());
            tag.putInt("stealType",stealType);
            tag.putInt("swings",0);
            tag.putBoolean("activated",true);
            tag.putString("vicName", victim.getName().getString());
            if (stealType == 14) {
                tag.putLong("Pos", victim.getOnPos().above().asLong());
                tag.putString("Dimension", victim.level().dimension().location().toString());
            } else if (stealType == 10) {
                    tag.putInt("experience",livingEntity.getExperienceReward());
                } else if (stealType == 11 && victim instanceof Villager vg){
                    GossipContainer gossips = vg.getGossips();
                    Tag gossipTag = gossips.store(NbtOps.INSTANCE);
                    tag.put("StoredGossip", gossipTag);
                    ((IVillagerAccess)vg).roundabout$clearGossips();
                } else if (stealType == 12 && victim instanceof Villager vg){
                    Brain<Villager> brain = vg.getBrain();
                    if (brain.hasMemoryValue(MemoryModuleType.JOB_SITE)) {
                        Optional<GlobalPos> jobSite = brain.getMemory(MemoryModuleType.JOB_SITE);

                        if (jobSite.isPresent()) {
                            // Save the block position
                            tag.putLong("Pos", jobSite.get().pos().asLong());

                            // Save the dimension
                            tag.putString("Dimension",
                                    jobSite.get().dimension().location().toString());

                        }
                    }

                } else if (stealType == 13 && victim instanceof ServerPlayer pl){
                    BlockPos spawnPos;
                    ResourceKey<Level> spawnDimension;

                    if (pl.getRespawnPosition() != null) {
                        // Player has a bed/respawn anchor
                        spawnPos = pl.getRespawnPosition();
                        spawnDimension = pl.getRespawnDimension();
                    } else {
                        // Fall back to world spawn
                        ServerLevel world = pl.serverLevel();
                        spawnPos = world.getSharedSpawnPos();
                        spawnDimension = Level.OVERWORLD;
                    }

                    tag.putLong("Pos", spawnPos.asLong());
                    tag.putString("Dimension", spawnDimension.location().toString());
                }

        }
        return stack;
    }
    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        String comp = $$0.getOrCreateTag().getString("vicName");
        boolean comp2 = $$0.getOrCreateTag().hasUUID("victim");
        if (comp != null && !comp.isBlank()){
            ChatFormatting formatting = ChatFormatting.LIGHT_PURPLE;
             $$2.add(Component.literal(comp).withStyle(formatting));
            //$$2.add(Component.translatable("text.roundabout.memory."+
            //        $$0.getOrCreateTag().getInt("stealType")).withStyle(ChatFormatting.BLUE));
            if (!$$0.getOrCreateTag().getBoolean("activated")){
                $$2.add(Component.translatable("text.roundabout.inactive_piece").withStyle(ChatFormatting.AQUA));
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack $$0, ItemStack $$1) {
        return false;
    }

    public static void attackThePerson(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack != null && !(stack.getItem() instanceof MemoryChessPieceItem)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null){
            return;
        }
        boolean activated = tag.getBoolean("activated");
        if (!tag.hasUUID("victim") || !activated) {
            return;
        }

        int stealType = tag.getInt("stealType");

        if (stealType == 14 || stealType == 15) {
            return;
        }

        UUID uuid = tag.getUUID("victim");

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity entity = serverLevel.getEntity(uuid);

        if (entity != null) {
            if (!entity.isAlive()) {
                if (tag.hasUUID("victim") &&
                        entity.getUUID().equals(tag.getUUID("victim"))) {

                    tag.putBoolean("activated",false);
                    stack.setDamageValue(0);
                    player.setItemSlot(EquipmentSlot.MAINHAND, stack);
                }
            } else if (entity instanceof LivingEntity living && living.hurtTime <= 7) {
                float dmg;
                if (living instanceof Player pl){
                    dmg = multiplyPowerByStandConfigPlayers(1.5F);
                } else {
                    dmg = multiplyPowerByStandConfigMobs(3);
                }
                if (living.hurt(ModDamageTypes.of(living.level(), ModDamageTypes.CHESS_STRIKE, player), dmg) && !living.isAlive()) {
                    player.getMainHandItem().hurtAndBreak(4, player, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof MemoryChessPieceItem){
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                } else {
                    int destroy = tag.getInt("swings");
                    if (!player.getAbilities().instabuild) {
                        destroy++;
                    }
                    MainUtil.makeBleed(living,0,200,player);
                    player.getMainHandItem().hurtAndBreak(1, player, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));

                    if (destroy >= 3 && !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof MemoryChessPieceItem){
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        if (!player.getAbilities().instabuild) {
                            tag.putInt("swings", destroy);
                        }
                    }
                }
                if (!player.getMainHandItem().isEmpty()){
                    if (player.level() instanceof ServerLevel sl){
                        sl.playSound(null, player.blockPosition(),
                                ModSounds.CKB_ATTACK_EVENT, SoundSource.PLAYERS, 1F,
                                (float) (0.99f + Math.random() * 0.02f));
                    }
                }

            }
        } else {
            tag.putBoolean("activated",false);
            stack.setDamageValue(0);
            player.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }

    public static float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                californiaKingBedSettings.chessMultOnPlayers *0.01));
    }

    public static float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                californiaKingBedSettings.chessMultOnMobs *0.01));
    }
}
