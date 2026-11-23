package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IBucketItem;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.block.FancyLighterBlock;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.LifePunchCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.KingNothingEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.TrackingCloudEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.particles.ColorParticleOptions;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import oshi.util.tuples.Pair;

import java.util.*;

public class PowersKingNothing extends NewDashPreset {
    // skins
    public static final byte
            MANGA = 1,
            SPINE = 2,
            NOTE = 3,
            BETA = 4,
            WARDEN = 5,
            SNIFFER = 6,
            SOMETHING = 7,
            RAM = 8,
            CHRISTMAS = 9;

    // sounds
    public static final byte
            DISINTEGRATE = 32,
            PHASE = 33,
            FOUND_ITEM = 34;

    public static final byte SYNC_TRACKING = 50;

    // tags
    public static final String
            MEMORIZED_BLOCK_TAG = "memorizedBlock",
            MEMORIZED_ENTITY_TYPE_TAG = "memorizedEntityType",
            MEMORIZED_UUID_TAG = "memorizedUUID";

    public static final Map<Byte, VisualData> VISUAL_INFO = new HashMap<>();
    private static final float SMELL_RANGE = 5f;

    static {
        VISUAL_INFO.put(MANGA, new VisualData(0xb982df, 0xe959b1, 0xb982df, 0.2f));
        VISUAL_INFO.put(SPINE, new VisualData(0xff7abc, 0xececec, 0xff7abc, 0.15f));
        VISUAL_INFO.put(NOTE, new VisualData(0x78b898, 0xe4806d, 0x78b898, 0.5f));
        VISUAL_INFO.put(BETA, new VisualData(0xae41ff, 0x82ff00, 0xae41ff, 0.15f));
        VISUAL_INFO.put(WARDEN, new VisualData(0x022d38, 0x1d9ca4, 0x1d9ca4, 0.65f));
        VISUAL_INFO.put(SNIFFER, new VisualData(0x93311d, 0x459d8a, 0x459d8a, 0.25f));
        VISUAL_INFO.put(SOMETHING, new VisualData(0xA8DF81, 0x58E990, 0xA8DF81, 0.2f));
        VISUAL_INFO.put(RAM, new VisualData(0xde9c64, 0xd77f5b, 0xde9c64, 0.2f));
        VISUAL_INFO.put(CHRISTMAS, new VisualData(0xec4040, 0xe6e1e1, 0xec4040, 0.2f));
    }

    // item tracking sets
    public final Set<Entity> itemHoldingEntities = new HashSet<>();
    public final Set<BlockPos> itemHoldingBlocks = new HashSet<>();
    public final Set<Pair<BlockPos, BlockPos>> itemHoldingBlockPairs = new HashSet<>();
    private boolean foundItemYet = false;

    // idk what this is exactly but its used in the phase grab stuff
    public Vec3 moveVec = Vec3.ZERO;

    // memorized scent cloud stuff
    TrackingCloudEntity trackingCloud = null;
    private boolean isTrackingMemorizedScent = false;

    // memorized scents
    private Block memorizedBlock = null;
    private EntityType<?> memorizedEntityType = null;
    private UUID memorizedPlayer = null;

    private Item trackedItem = null;

    // for dimension change and such certain moves
    private ResourceKey<Level> dimension = null;

    // anim stuff
    private boolean isOut = false;
    private int returnAnimTicks = 0;
    private int summonAnimTicks = 0;
    private int disintegrateAnimTicks = 0;

    private boolean startPhaseTickAfter = false;

    public PowersKingNothing(LivingEntity self) {
        super(self);
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(MANGA);
        $$1.add(SPINE);
        $$1.add(SOMETHING);
        $$1.add(CHRISTMAS);
        $$1.add(NOTE);
        $$1.add(WARDEN);
        $$1.add(SNIFFER);
        $$1.add(RAM);
        $$1.add(BETA);
        return $$1;
    }

    public static String getSkinString(byte id) {
        return switch (id) {
            case SPINE -> "spine";
            case NOTE -> "note_block";
            case BETA -> "beta";
            case WARDEN -> "warden";
            case SNIFFER -> "sniffer";
            case SOMETHING -> "something";
            case RAM -> "ram";
            case CHRISTMAS -> "christmas";
            default -> "manga";
        };
    }

    private static boolean isGrabbable(ItemStack stack, Player self) {
        return !stack.isEmpty() && !(MainUtil.isItemGrabBlacklisted(stack)) && !(stack.getItem() instanceof BlockItem
                && (MainUtil.isBlockBlacklisted(((BlockItem) stack.getItem()).getBlock().defaultBlockState())
                || ((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock
                || ((BlockItem) stack.getItem()).getBlock() instanceof FancyLighterBlock))
                && !(stack.getItem() instanceof BoatItem) && !(stack.getItem() instanceof MinecartItem)
                && !(stack.getItem() instanceof RoadRollerItem) && !self.getCooldowns().isOnCooldown(stack.getItem());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKingNothing(entity);
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().kingNothingSettings.enableKingNothing;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.KING_NOTHING.create(this.getSelf().level());
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return !isTrackingItem() && !isTrackingMemorizedScent();
    }

    @Override
    public boolean returnFakeStandForHud() {
        return super.returnFakeStandForHud() || !canSummonStandAsEntity();
    }

    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        switch (soundChoice) {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.KING_NOTHING_SUMMON_EVENT;
            }
            case DISINTEGRATE -> {
                return ModSounds.KING_NOTHING_DESUMMON_EVENT;
            }
            case PHASE -> {
                return ModSounds.KING_NOTHING_PHASE_EVENT;
            }
            case FOUND_ITEM -> {
                return SoundEvents.EXPERIENCE_ORB_PICKUP;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == PHASE) {
            return PHASE;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (memorizedBlock != null) {
            $$0.putString(MEMORIZED_BLOCK_TAG, BuiltInRegistries.BLOCK.getKey(memorizedBlock).toString());
        }
        else {
            $$0.remove(MEMORIZED_BLOCK_TAG);
        }
        if (memorizedEntityType != null) {
            $$0.putString(MEMORIZED_ENTITY_TYPE_TAG,
                    BuiltInRegistries.ENTITY_TYPE.getKey(memorizedEntityType).toString());
        }
        else {
            $$0.remove(MEMORIZED_ENTITY_TYPE_TAG);
        }
        if (memorizedPlayer != null) {
            $$0.putUUID(MEMORIZED_UUID_TAG, memorizedPlayer);
        }
        else {
            $$0.remove(MEMORIZED_UUID_TAG);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains(MEMORIZED_BLOCK_TAG)) {
            memorizedBlock = BuiltInRegistries.BLOCK.get(new ResourceLocation($$0.getString(MEMORIZED_BLOCK_TAG)));
            memorizedEntityType = null;
            memorizedPlayer = null;
        }
        else if ($$0.contains(MEMORIZED_ENTITY_TYPE_TAG)) {
            memorizedEntityType =
                    BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation($$0.getString(MEMORIZED_ENTITY_TYPE_TAG)));
            memorizedBlock = null;
            memorizedPlayer = null;
        }
        else if ($$0.contains(MEMORIZED_UUID_TAG)) {
            memorizedPlayer = $$0.getUUID(MEMORIZED_UUID_TAG);
            memorizedEntityType = null;
            memorizedBlock = null;
        }
        else {
            resetMemorizedScentData();
        }
    }

    private void resetMemorizedScentData() {
        this.memorizedBlock = Blocks.IRON_ORE;
    }

    @Override
    public List<Byte> getPosList() {
        return humanoidPosList();
    }

    public VisualData getVisualInfo() {
        return VISUAL_INFO.getOrDefault(standSkin, VISUAL_INFO.get(MANGA));
    }

    public boolean spawnsParticlesThisTick() {
        return getSelf().tickCount
                % ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingParticleSpawnInterval == 0;
    }

    public ItemStack getMemorizedScentIcon() {
        if (memorizedBlock != null) {
            return memorizedBlock.asItem().getDefaultInstance();
        }
        else if (memorizedEntityType != null) {
            return Items.ZOMBIE_SPAWN_EGG.getDefaultInstance();
        }
        else if (memorizedPlayer != null) {
            return new ItemStack(Items.PLAYER_HEAD, 1);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        boolean normalZIcon = true;
        boolean normalXIcon = true;
        if (getActivePower() == PowerIndex.POWER_2_SNEAK) {
            setSkillIcon(context, x, y, 1, StandIcons.KING_NOTHING_DROP_ITEM, PowerIndex.NO_CD);
            setSkillIcon(context, x, y, 2, StandIcons.KING_NOTHING_PHASE_ITEM, PowerIndex.SKILL_2_SNEAK);
            normalZIcon = false;
            normalXIcon = false;
        }
        else if (getActivePower() == PowerIndex.POWER_2) {
            setSkillIcon(context, x, y, 2, StandIcons.KING_NOTHING_PHASE_GRAB, PowerIndex.SKILL_2);
            normalXIcon = false;
        }
        else if (isTrackingItem() || isTrackingMemorizedScent()) {
            setSkillIcon(context, x, y, 1, StandIcons.KING_NOTHING_REFORM, PowerIndex.SKILL_1_SNEAK);
            normalZIcon = false;
        }
        if (isHoldingSneak()) {
            if (normalZIcon) {
                setSkillIcon(context, x, y, 1, StandIcons.KING_NOTHING_SNIFF_ITEM, PowerIndex.SKILL_1_SNEAK);
            }
            if (normalXIcon) {
                setSkillIcon(context, x, y, 2, StandIcons.KING_NOTHING_PHASE_ITEM, PowerIndex.SKILL_2_SNEAK);
            }
            setSkillIcon(context, x, y, 3, StandIcons.KING_NOTHING_FORM_PREDICT, PowerIndex.SKILL_3);
        }
        else {
            if (normalZIcon) {
                setSkillIcon(context, x, y, 1, StandIcons.KING_NOTHING_TRACKING, PowerIndex.SKILL_1);
            }
            if (normalXIcon) {
                setSkillIcon(context, x, y, 2, StandIcons.KING_NOTHING_PHASE_GRAB, PowerIndex.SKILL_2);
            }
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        renderScentIcon(context, x, y);
        super.renderIcons(context, x, y);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        LivingEntity self = getSelf();
        switch (slot) {
            case 1 -> {
                if (getActivePower() == PowerIndex.POWER_2 || isHoldingSneak() && !isGrabbable(self.getMainHandItem(),
                        (Player) self) && getActivePower() == PowerIndex.NONE) {
                    return true;
                }
            }
            case 2 -> {
                if (isTrackingItem() || isTrackingMemorizedScent() || isHoldingSneak() && getActivePower() == PowerIndex.NONE
                        && self.getMainHandItem().isEmpty()) {
                    return true;
                }
            }
            case 3 -> {
                if ((isHoldingSneak() && (isTrackingItem()
                        || !hasFormPrediction(getSelf().getMainHandItem().getItem())))) {
                    return true;
                }
            }
            case 4 -> {
                if (isPhasing() || isTrackingItem() || isTrackingMemorizedScent()) {
                    return true;
                }
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
            int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        boolean canContinue = true;
        for (int i = 0; canContinue; i++) {
            int leftOffset = 20 + (19 * (i / 3));
            int topOffset = 80 + (19 * (i % 3));
            switch (i) {
                case 0 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.kn_smell_track", "instruction.roundabout.press_skill",
                        StandIcons.KING_NOTHING_TRACKING, 1, level, bypass));
                case 1 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.kn_track_item", "instruction.roundabout.press_skill_crouch",
                        StandIcons.KING_NOTHING_SNIFF_ITEM, 1, level, bypass));
                case 2 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.phase_grab", "instruction.roundabout.press_skill",
                        StandIcons.KING_NOTHING_PHASE_GRAB, 2, level, bypass));
                case 3 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.kn_phase_item", "instruction.roundabout.press_skill_crouch",
                        StandIcons.KING_NOTHING_PHASE_ITEM, 2, level, bypass));
                case 4 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.kn_drop_item", "instruction.roundabout.press_skill_phase_item",
                        StandIcons.KING_NOTHING_DROP_ITEM, 1, level, bypass));
                case 5 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.dodge", "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level,
                        bypass));
                case 6 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.kn_form_predict", "instruction.roundabout.press_skill_crouch",
                        StandIcons.KING_NOTHING_FORM_PREDICT, 3, level, bypass));
                case 7 -> $$1.add(drawSingleGUIIcon(context, 18, leftPos + leftOffset, topPos + topOffset, 0,
                        "ability.roundabout.kn_sniff", "instruction.roundabout.press_skill", StandIcons.STEAL_MEMORIES,
                        4, level, bypass));
                default -> canContinue = false;
            }
        }
        return $$1;
    }

    public void renderScentIcon(GuiGraphics context, int x, int y) {
        RenderSystem.enableBlend();
        context.setColor(1f, 1f, 1f, 1f);
        CooldownInstance cd = null;
        if (!getPowerCooldowns().isEmpty() && getPowerCooldowns().size() >= PowerIndex.SKILL_4) {
            cd = getPowerCooldowns().get(PowerIndex.SKILL_4);
        }
        x += 100;
        y -= 1;

        RenderSystem.enableBlend();
        context.blit(getIconYes(4), x - 3, y - 3, 0, 0, squareWidth, squareHeight, squareWidth, squareHeight);

        ItemStack block = getMemorizedScentIcon();
        RenderSystem.enableBlend();
        Font renderer = Minecraft.getInstance().font;
        Component special4Key = KeyInputRegistry.abilityFourKey.getTranslatedKeyMessage();
        special4Key = fixKey(special4Key);
        //true? false? it doesn't matter, for this text will render below the item no matter what i try :(
        context.drawString(renderer, special4Key, x - 1, y + 11, 0xffffff, true);
        RenderSystem.enableBlend();
        if ((cd != null && (cd.time >= 0)) || isAttackIneptVisually(PowerIndex.SKILL_4, 4)) {
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
            context.renderItem(block, x + 1, y + 1);
            if ((cd != null && (cd.time >= 0))) {
                float blit = (20 * (1 - ((float) (1 + cd.time) / (1 + cd.maxTime))));
                int b = Math.round(blit);
                RenderSystem.enableBlend();
                context.setColor(1f, 1f, 1f, 1f);

                ResourceLocation COOLDOWN_TEX = StandIcons.COOLDOWN_ICON;

                if (cd.isFrozen()) {
                    COOLDOWN_TEX = StandIcons.FROZEN_COOLDOWN_ICON;
                }

                context.blit(COOLDOWN_TEX, x - 1, y - 1 + b, 0, b, 20, 20 - b, 20, 20);
                int num = ((int) (Math.floor((double) cd.time / 20) + 1));
                int offset = x + 3;
                if (num <= 9) {
                    offset = x + 7;
                }
                //If 100 or more seconds long shift to the left
                if (num >= 100) {
                    offset = offset - 3;
                }

                if (!cd.isFrozen()) {
                    context.drawString(Minecraft.getInstance().font, "" + num, offset, y, 0xffffff, true);
                }

            }
            context.setColor(1f, 1f, 1f, 0.9f);
        }
        else {
            context.renderItem(block, x + 1, y + 1);
        }
    }

    public void addItemEntity(int id) {
        if (isClient()
                && ConfigManager.getClientConfig().kingNothingSettings.highlightInItemTrack) {
            Entity ent = getSelf().level().getEntity(id);
            itemHoldingEntities.add(ent);
        }
    }

    public void removeItemEntity(int id) {
        if (isClient()
                && ConfigManager.getClientConfig().kingNothingSettings.highlightInItemTrack) {
            Entity ent = getSelf().level().getEntity(id);
            itemHoldingEntities.remove(ent);
        }
    }

    public void addItemBlockPos(BlockPos pos1, BlockPos pos2) {
        if (isClient()
                && ConfigManager.getClientConfig().kingNothingSettings.highlightInItemTrack) {
            this.itemHoldingBlocks.add(pos1);
            this.itemHoldingBlocks.add(pos2);
            if (!pos1.equals(pos2)) {
                this.itemHoldingBlockPairs.add(new Pair<>(pos1, pos2));
            }
        }
    }

    public void removeItemBlockPos(BlockPos pos) {
        if (isClient()
                && ConfigManager.getClientConfig().kingNothingSettings.highlightInItemTrack) {
            this.itemHoldingBlocks.remove(pos);
            itemHoldingBlockPairs.removeIf(pair -> pair.getA().equals(pos) || pair.getB().equals(pos));
        }
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        return itemHoldingEntities.contains(ent) && player.hasLineOfSight(ent)
                && ConfigManager.getClientConfig().kingNothingSettings.highlightInItemTrack;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        return getVisualInfo().highlightColor();
    }

    public int getParticleColor(VisualData pdata) {
        return Roundabout.RANDOM.nextFloat() > pdata.percentOther() ? pdata.color1() : pdata.color2();
    }

    @Override
    public void tickPower() {
        super.tickPower();
        // handle idle stuffs
        if (getSelf().isAlive() && !getSelf().isRemoved() && getStandEntity(getSelf()) != null) {
            StandEntity stand = getStandEntity(getSelf());
            if (stand.getAnimation() == KingNothingEntity.SUMMON) {
                if (summonAnimTicks < 17) {
                    summonAnimTicks++;
                }
                else {
                    animateStand(StandEntity.IDLE);
                    summonAnimTicks = 0;
                }
            }
            else {
                summonAnimTicks = 0;
            }
            if (stand.getAnimation() == KingNothingEntity.RETURN) {
                if (returnAnimTicks < 26) {
                    returnAnimTicks++;
                }
                else {
                    animateStand(StandEntity.IDLE);
                    returnAnimTicks = 0;
                }
            }
            else {
                returnAnimTicks = 0;
            }
            if (stand.getAnimation() == KingNothingEntity.DISINTEGRATE) {
                if (disintegrateAnimTicks < 27) {
                    disintegrateAnimTicks++;
                }
                else {
                    disintegrateAnimTicks = 0;
                }
                if (disintegrateAnimTicks > 14) {
                    isOut = false;
                    getStandUserSelf().roundabout$removeStandOut();
                }
            }
            else {
                disintegrateAnimTicks = 0;
            }
        }
        if (getSelf().isAlive() && !getSelf().isRemoved() && isTrackingItem()) {
            Level level = getSelf().level();
            float checkRangeEnt = 22;
            int checkRangeChest = 16;
            boolean foundTem = itemTrackEnt(checkRangeEnt, level);
            // chest code!!
            foundTem = itemTrackBlock(checkRangeChest, level) || foundTem;
            if (foundTem && !foundItemYet) {
                foundItemYet = true;
                // FIND BETTER SOUND
                if (this.self instanceof ServerPlayer pl) {
                    S2CPacketUtil.sendPlaySoundPacket(pl, this.self.getId(), FOUND_ITEM);
                }
            }
            // passive particles
            if (!foundTem) {
                foundItemYet = false;
                int particleInterval = ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingParticleSpawnInterval;
                if (particleInterval <= 5 ? this.getSelf().tickCount % 5 == 0 : spawnsParticlesThisTick()) {
                    VisualData pdata = getVisualInfo();
                    for (int i = 0; i < 2; i++) {
                        int color = getParticleColor(pdata);
                        Vec3 offset = getSelf().getEyePosition().offsetRandom(level.getRandom(), 5);
                        MainUtil.sendParticlesIfPossible(getSelf(), getSelf().level(),
                                ColorParticleOptions.fromPackedColor(ModParticles.PUZZLE_PARTICLE, color),
                                offset.x(), offset.y(), offset.z(), 1, 0.5, 0.5, 0.5, 0.05);
                    }
                }
            }
        }
        if (isTrackingMemorizedScent && (trackingCloud != null && (trackingCloud.isRemoved() || trackingCloud.distanceToSqr(getSelf()) > 4096))) {
            if (trackingCloud != null && !trackingCloud.isRemoved()) {
                trackingCloud.discard();
            }
            syncTracking(false);
            trackingCloud = null;
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
        }
        if (getSelf().isAlive() && !getSelf().isRemoved() && trackingCloud != null && spawnsParticlesThisTick()) {
            VisualData pdata = getVisualInfo();
            for (int i = 0; i < 2; i++) {
                int color = getParticleColor(pdata);
                MainUtil.sendParticlesIfPossible(getSelf(), getSelf().level(),
                        ColorParticleOptions.fromPackedColor(ModParticles.PUZZLE_PARTICLE, color),
                        trackingCloud.getX(), trackingCloud.getY(), trackingCloud.getZ(), 1, 0.1, 0.1, 0.1, 0.01);
            }
        }
    }

    private void syncTracking(boolean isOut) {
        int status = (isOut ? 1 : 0);
        this.updatePowerInt(SYNC_TRACKING, status);
        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendIntPowerDataPacket((Player) this.getSelf(), SYNC_TRACKING, status);
        }
    }

    public boolean itemTrackBlock(int checkRangeChest, Level level) {
        int checked = 0;
        boolean foundTem = false;
        BlockPos selfPos = getSelf().blockPosition();
        Iterable<BlockPos> itter =
                BlockPos.betweenClosed(selfPos.offset(-checkRangeChest / 2, -2, -checkRangeChest / 2),
                        selfPos.offset(checkRangeChest / 2, checkRangeChest / 2, checkRangeChest / 2));
        if (!level.isClientSide()) {
            Set<BlockPos> locatedChests = new HashSet<>();
            for (BlockPos itpos : itter) {
                // this iterable in particular is stupid
                BlockPos pos = new BlockPos(itpos);
                boolean chestHasItem = false;
                if (checked > 16) {
                    break;
                }
                if (locatedChests.contains(pos)) {
                    chestHasItem = true;
                }
                BlockState bstate = level.getBlockState(pos);
                BlockEntity bentity = level.getBlockEntity(pos);
                float xSpreadBonus = 0;
                float zSpreadBonus = 0;
                BlockPos otherChestPos = null;
                if (bentity instanceof RandomizableContainerBlockEntity CO && !chestHasItem) {
                    checked++;
                    if (bstate.hasProperty(ChestBlock.TYPE) && bstate.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
                        boolean isLeft = bstate.getValue(ChestBlock.TYPE) == ChestType.LEFT;
                        switch (bstate.getValue(ChestBlock.FACING)) {
                            case NORTH -> {
                                otherChestPos = isLeft ? pos.east() : pos.west();
                                xSpreadBonus += 0.2f;
                            }
                            case SOUTH -> {
                                otherChestPos = isLeft ? pos.west() : pos.east();
                                xSpreadBonus += 0.2f;
                            }
                            case WEST -> {
                                otherChestPos = isLeft ? pos.north() : pos.south();
                                zSpreadBonus += 0.2f;
                            }
                            case EAST -> {
                                otherChestPos = isLeft ? pos.south() : pos.north();
                                zSpreadBonus += 0.2f;
                            }
                        }
                    }
                    for (int i = 0; i < CO.getContainerSize(); i++) {
                        if (CO.getItem(i).is(trackedItem)) {
                            chestHasItem = true;
                            break;
                        }
                    }
                    if (otherChestPos != null &&
                            level.getBlockEntity(otherChestPos) instanceof RandomizableContainerBlockEntity OCO) {
                        for (int i = 0; i < OCO.getContainerSize(); i++) {
                            if (OCO.getItem(i).is(trackedItem)) {
                                chestHasItem = true;
                                break;
                            }
                        }
                    }
                }
                if (chestHasItem) {
                    foundTem = true;
                    if (!this.itemHoldingBlocks.contains(pos) || (otherChestPos != null && this.itemHoldingBlocks.contains(otherChestPos))) {
                        this.itemHoldingBlocks.add(pos);
                        if (otherChestPos != null) {
                            this.itemHoldingBlocks.add(otherChestPos);
                        }
                        S2CPacketUtil.knAddItemContainer(((Player) getSelf()), pos, otherChestPos != null ? otherChestPos : pos);
                    }
                    if (otherChestPos != null) {
                        locatedChests.add(otherChestPos);
                    }
                    locatedChests.add(pos);
                    VisualData pdata = getVisualInfo();
                    Vec3 spawnPos = otherChestPos == null ? pos.getCenter() :
                            pos.getCenter().lerp(otherChestPos.getCenter(), 0.5);
                    int yOffset = 1 + (level.getBlockState(pos.above()).isAir() || (level.getBlockEntity(
                            pos.above()) instanceof RandomizableContainerBlockEntity) || otherChestPos != null && (
                            level.getBlockState(otherChestPos).isAir() || (level.getBlockEntity(
                                    otherChestPos.above()) instanceof RandomizableContainerBlockEntity)) ? 0 : 1);
                    if (spawnsParticlesThisTick()) {
                        for (int i = 0; i < 2 + (otherChestPos == null ? 0 : 2); i++) {
                            int color = getParticleColor(pdata);
                            MainUtil.sendParticlesIfPossible(getSelf(), getSelf().level(),
                                    ColorParticleOptions.fromPackedColor(ModParticles.PUZZLE_PARTICLE, color),
                                    spawnPos.x(), spawnPos.y() + yOffset, spawnPos.z(),
                                    1 + (otherChestPos == null ? 0 : 1),
                                    0.1 + xSpreadBonus, 0.1, 0.1 + zSpreadBonus, 0.01);
                        }
                    }
                }
                else {
                    if (this.itemHoldingBlocks.contains(pos)) {
                        this.itemHoldingBlocks.remove(pos);
                        S2CPacketUtil.knRemoveItemContainer(((Player) getSelf()), pos);
                    }
                    if (otherChestPos != null) {
                        if (this.itemHoldingBlocks.contains(otherChestPos)) {
                            this.itemHoldingBlocks.remove(otherChestPos);
                            S2CPacketUtil.knRemoveItemContainer(((Player) getSelf()), otherChestPos);
                        }
                    }
                }
            }
            for (BlockPos pos : itemHoldingBlocks) {
                if (!locatedChests.contains(pos)) {
                    S2CPacketUtil.knRemoveItemContainer(((Player) getSelf()), pos);
                }
            }
            itemHoldingBlocks.removeIf(pos -> !locatedChests.contains(pos));
        }
        return foundTem;
    }

    public boolean itemTrackEnt(float checkRangeEnt, Level level) {
        List<Entity> targets =
                MainUtil.getEntitiesInRange(getSelf().level(), getSelf().blockPosition(), checkRangeEnt, getSelf());
        boolean renderHighlights = ConfigManager.getClientConfig().kingNothingSettings.highlightInItemTrack;
        boolean foundTem = false;
        if (level.isClientSide() && renderHighlights) {
            itemHoldingEntities.removeIf((entity) -> !targets.contains(entity));
        }
        for (int i = 0; i < Math.min(64, targets.size()); i++) {
            Entity ent = targets.get(i);
            Entity checkedEnt = ent;
            if (!PowerTypes.isInADifferentExistence(getSelf(), ent)) {
                boolean foundItem = checkedEnt instanceof ItemEntity IE && IE.getItem().getItem().equals(trackedItem);
                if (foundItem && level.isClientSide() && renderHighlights) {
                    itemHoldingEntities.add(ent);
                }
                if (ent instanceof ItemFrame IF && IF.getItem().is(trackedItem)) {
                    foundItem = true;
                    if (level.isClientSide() && renderHighlights) {
                        itemHoldingEntities.add(ent);
                    }
                }
                if (!foundItem && checkedEnt instanceof LivingEntity LE && (LE.getMainHandItem().is(trackedItem)
                        || LE.getOffhandItem().getItem().equals(trackedItem) || LE.getItemBySlot(
                        LivingEntity.getEquipmentSlotForItem(trackedItem.getDefaultInstance())).is(trackedItem))) {
                    if (level.isClientSide() && renderHighlights) {
                        itemHoldingEntities.add(ent);
                    }
                    foundItem = true;
                }
                if (!foundItem && checkedEnt instanceof D4CCloneEntity D4C) {
                    if (D4C.inventory.countItem(trackedItem) > 0) {
                        if (!level.isClientSide() && !itemHoldingEntities.contains(ent)) {
                            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) getSelf()),
                                    PacketDataIndex.S2C_KN_ADD_HIGHLIGHT_ENTITY, ent.getId());
                        }
                        foundItem = true;
                    }
                    else if (!level.isClientSide() && itemHoldingEntities.contains(ent)) {
                        S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) getSelf()),
                                PacketDataIndex.S2C_KN_REMOVE_HIGHLIGHT_ENTITY, ent.getId());
                    }
                }
                if (!foundItem && checkedEnt instanceof CloneEntity CE && (CE instanceof KingCrimsonCloneEntity || (
                        CE instanceof LifePunchCloneEntity && !CE.getPlayer().is(getSelf())))) {
                    checkedEnt = CE.getPlayer();
                }
                if (!foundItem && checkedEnt instanceof Player PE) {
                    if (PE.getInventory().countItem(trackedItem) > 0) {
                        if (!level.isClientSide() && !itemHoldingEntities.contains(ent)) {
                            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) getSelf()),
                                    PacketDataIndex.S2C_KN_ADD_HIGHLIGHT_ENTITY, ent.getId());
                        }
                        foundItem = true;
                    }
                    else if (!level.isClientSide() && itemHoldingEntities.contains(ent)) {
                        S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) getSelf()),
                                PacketDataIndex.S2C_KN_REMOVE_HIGHLIGHT_ENTITY, ent.getId());
                    }
                }
                if (foundItem) {
                    foundTem = true;
                    if (!level.isClientSide() && spawnsParticlesThisTick()) {
                        VisualData pdata = getVisualInfo();
                        for (int j = 0; j < 2; j++) {
                            int color = getParticleColor(pdata);
                            MainUtil.sendParticlesIfPossible(getSelf(), getSelf().level(),
                                    ColorParticleOptions.fromPackedColor(ModParticles.PUZZLE_PARTICLE, color),
                                    ent.getX(), ent.getY() + ent.getEyeHeight() + 0.5, ent.getZ(), 1, 0.1, 0.05, 0.1,
                                    0.01);
                        }
                    }
                }
                else if (level.isClientSide() && renderHighlights) {
                    itemHoldingEntities.remove(ent);
                }
            }
            else if (level.isClientSide() && renderHighlights) {
                itemHoldingEntities.remove(ent);
            }
        }
        return foundTem;
    }

    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
        if (getSelf().isAlive() && !getSelf().isRemoved()) {
            // Tick Phase
            if (isPhasing() && !this.isClient()) {
                if (startPhaseTickAfter) {
                    startPhaseTickAfter = false;
                    startPhaseMovement(getStandEntity(getSelf()));
                }
                else if (this.attackTimeDuring == 108) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
                    animateStand(KingNothingEntity.RETURN);
                }
                else if (this.attackTimeDuring >= 0) {
                    StandEntity stand = getStandEntity(this.self);
                    if (Objects.nonNull(stand)) {
                        VisualData pdata = getVisualInfo();
                        AABB BB1 = stand.getBoundingBox();
                        if (spawnsParticlesThisTick() && ClientNetworking.getAppropriateConfig().kingNothingSettings.spawnPhaseGrabParticles) {
                            double x = BB1.getCenter().x();
                            double y = BB1.getCenter().y() + (((FollowingStandEntity) stand).getSizePercent() - 1) / 2;
                            double z = BB1.getCenter().z();
                            for (int i = 0; i < 2; i++) {
                                int color = getParticleColor(pdata);
                                MainUtil.sendParticlesIfPossible(getSelf(), getSelf().level(),
                                        ColorParticleOptions.fromPackedColor(ModParticles.PUZZLE_PARTICLE, color),
                                        x, y,
                                        z, 1, 0.1, 0.1, 0.1, 0.01);
                            }
                        }
                        Vec3 vec3d = this.getSelf().getEyePosition(0);
                        Vec3 vec3d2 = this.getSelf().getViewVector(0);
                        Vec3 vec3d3 = vec3d.add(vec3d2.x * 20, vec3d2.y * 20, vec3d2.z * 20);
                        double mag = 0.18F;

                        moveVec = moveVec.add(
                                vec3d3.subtract(this.getSelf().position().add(moveVec)).normalize().scale(mag));
                        Vec3 yes = this.getSelf().position().add(moveVec);
                        double post = stand.position().distanceTo(vec3d3);
                        if (post < 1.5) {
                            stand.setYRot(this.getSelf().getYHeadRot() % 360);
                            stand.setXRot(this.getSelf().getXRot());
                        }
                        else {
                            Direction gdir = ((IGravityEntity) this.self).roundabout$getGravityDirection();
                            Vec2 grot = new Vec2(getLookAtPlaceYaw(stand, vec3d3), getLookAtPlacePitch(stand, vec3d3));
                            grot = RotationUtil.rotWorldToPlayer(grot, gdir);
                            stand.setYRot(grot.x);
                            stand.setXRot(grot.y);
                        }
                        if (post < 0.4) {
                            stand.setPos(vec3d3);
                        }
                        else {
                            stand.setPos(yes);
                        }

                        ItemStack held = stand.getHeldItem();
                        boolean isHoldingItem = !held.isEmpty();
                        Vec3 vec3d2ST = stand.getViewVector(0);
                        Vec3 vec3daST = stand.getPosition(0);
                        Vec3 vec3DbST = stand.getEyePosition(0);
                        Vec3 vec3dST = vec3daST.lerp(vec3DbST, 0.5);
                        Vec3 vec3d3ST = vec3dST.add(vec3d2ST.x * 3, vec3d2ST.y * 3, vec3d2ST.z * 3);
                        BlockHitResult blockHit = stand.level().clip(
                                new ClipContext(vec3dST, vec3d3ST, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE,
                                        stand));
                        BlockPos bpos = blockHit.getBlockPos();
                        BlockState state = stand.level().getBlockState(bpos);
                        boolean didThings = false;
                        if (!state.isAir()) {
                            if (this.getSelf().level().getGameRules().getBoolean(
                                    ModGamerules.ROUNDABOUT_STAND_REDSTONE_INTERFERENCE) && !isHoldingItem) {
                                Block blk = state.getBlock();
                                if (((blk instanceof LeverBlock) || (blk instanceof ButtonBlock)
                                        || (blk instanceof DoorBlock) || (blk instanceof TrapDoorBlock)
                                        || (blk instanceof FenceGateBlock)) && this.getSelf() instanceof Player PE) {
                                    blk.use(state, PE.level(), bpos, PE, PE.getUsedItemHand(), blockHit);
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                    playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
                                    animateStand(KingNothingEntity.RETURN);
                                    didThings = true;
                                }
                            }
                            // bucket pickup
                            else if (held.is(Items.BUCKET) && tryBucketPickup(state, blockHit, bpos, stand)) {
                                didThings = true;
                            }
                            // block place
                            else if (held.getItem() instanceof BlockItem BI && tryPlaceBlock(BI, blockHit, bpos, state,
                                    stand, held)) {
                                didThings = true;
                            }
                            // bucket place
                            else if (held.getItem() instanceof BucketItem BU && !held.is(Items.LAVA_BUCKET) && !held.is(
                                    Items.BUCKET) && tryBucketPlace(BU, stand, bpos, blockHit)) {
                                didThings = true;
                            }
                            // mining !!
                            else if (isHoldingItem && tryMineBlock(held, state, bpos, stand)) {
                                didThings = true;
                            }
                            if (didThings) {
                                this.stopSoundsIfNearby(PHASE, 32, false);
                            }
                        }

                        AABB BB2 = stand.getBoundingBox();
                        if (this.attackTimeDuring > 2) {
                            if (!isHoldingItem) {
                                tryPhaseItemGrab(stand, BB1, BB2);
                            }
                            else {
                                tryPhaseInteractMob(stand, BB1, BB2);
                            }
                        }

                        if (stand.isTechnicallyInImpassableWall() || (isHoldingItem && stand.isTechnicallyInWall())
                                || stand.position().distanceTo(this.getSelf().position()) > 15) {
                            this.stopSoundsIfNearby(PHASE, 32, false);
                            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                                    7);
                            this.setCooldown(PowerIndex.SKILL_2, 5);
                            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                                    PowerIndex.SKILL_2_SNEAK, 7);
                            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 5);
                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                            setPowerNone();
                            animateStand(KingNothingEntity.RETURN);
                            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
                        }
                    }
                }
            }
            else {
               startPhaseTickAfter = false;
            }
        }
    }

    private boolean tryBucketPlace(BucketItem bucket, StandEntity stand, BlockPos bpos, BlockHitResult blockHit) {
        BlockPos placePos = bpos.relative(blockHit.getDirection());
        boolean placesNotfr = getSelf().level().isFluidAtPosition(placePos,
                fluidState -> (!fluidState.isSource() || bucket instanceof MobBucketItem) && (
                        fluidState.getType().isSame(((IBucketItem) bucket).roundabout$getContents())
                                || fluidState.isEmpty())) && canModifyPos(placePos);
        if (placesNotfr && bucket.emptyContents(null, stand.level(), placePos, null)) {
            bucket.checkExtraContent(null, stand.level(), stand.getHeldItem(), placePos);
            if (!getCreative()) {
                stand.setHeldItem(Items.BUCKET.getDefaultInstance());
            }
            getStandUserSelf().roundabout$tryPower(PowerIndex.NONE, true);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            this.setCooldown(PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhasePlaceCd);
            this.setCooldown(PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhasePlaceCd);
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
            animateStand(KingNothingEntity.RETURN);
        }
        return placesNotfr;
    }

    public boolean tryBucketPickup(BlockState state, BlockHitResult blockHit, BlockPos pos, StandEntity stand) {
        BlockState bstate = state;
        BlockPos bpos = pos;
        BucketPickup pickup = null;
        if (!(bstate.getBlock() instanceof BucketPickup)
                || bstate.getBlock() instanceof SimpleWaterloggedBlock && bstate.getFluidState().isEmpty()) {
            Direction dir = blockHit.getDirection();
            bpos = bpos.relative(dir);
            bstate = stand.level().getBlockState(bpos);
        }
        if (bstate.getBlock() instanceof BucketPickup BU) {
            pickup = BU;
        }
        boolean pickupfrfrfr = pickup != null && !(pickup instanceof SimpleWaterloggedBlock && !bstate.getValue(
                BlockStateProperties.WATERLOGGED)) && !(pickup instanceof LiquidBlock
                && !bstate.getFluidState().isSource()) && canModifyPos(pos);
        if (pickupfrfrfr) {
            if (pickup.getPickupSound().isPresent()) {
                getSelf().playSound(pickup.getPickupSound().get(), 1.0f, 1.0f);
            }
            ItemStack stacko = pickup.pickupBlock(stand.level(), bpos, bstate);
            if (!stacko.isEmpty()) {
                stand.setHeldItem(stacko);
            }
            getStandUserSelf().roundabout$tryPower(PowerIndex.NONE, true);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            this.setCooldown(PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhaseMineCd);
            this.setCooldown(PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhaseMineCd);
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
            animateStand(KingNothingEntity.RETURN);
        }
        return pickupfrfrfr;
    }

    public boolean canModifyPos(BlockPos pos) {
        return getSelf() instanceof ServerPlayer SP && SP.level().getGameRules().getBoolean(
                ModGamerules.ROUNDABOUT_STAND_GRIEFING) && !SP.blockActionRestricted(SP.level(), pos,
                SP.gameMode.getGameModeForPlayer()) && SP.level().mayInteract(SP, pos) && MainUtil.canInteractWithBlock(
                SP, pos);
    }

    public boolean tryPlaceBlock(BlockItem blockItem, BlockHitResult blockHit, BlockPos bpos, BlockState bstate,
            StandEntity stand, ItemStack held) {
        Direction dir = blockHit.getDirection();
        Direction placeDir = dir;
        if (dir.getAxis() == Direction.Axis.X || dir.getAxis() == Direction.Axis.Z) {
            placeDir = dir.getOpposite();
        }
        BlockPos placePos = bpos.relative(dir);
        BlockState placeState = stand.level().getBlockState(placePos);
        ItemStack returnStack = stand.getHeldItem().copy();
        boolean doubleSlabbinItFr = false;
        if (bstate.is(blockItem.getBlock()) && (bstate.getBlock() instanceof SlabBlock)
                && bstate.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE && (
                bstate.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.TOP ? (dir == Direction.DOWN) :
                        (dir == Direction.UP)) && canModifyPos(bpos)) {
            stand.level().setBlockAndUpdate(bpos, bstate.setValue(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE));
            stand.setHeldItem(ItemStack.EMPTY);
            doubleSlabbinItFr = true;
        }
        if (stand.level().isFluidAtPosition(bpos, fluidState -> !fluidState.isEmpty()) && !(
                bstate.hasProperty(BlockStateProperties.WATERLOGGED) && bstate.getValue(
                        BlockStateProperties.WATERLOGGED))) {
            return false;
        }
        // STOLEN FROM GOLD EXPERIENCE BECAUSE IM EVIL
        Vec3 oldPos = stand.getPosition(1);
        stand.setPos(0, 0, 0);
        boolean placesfrfr = doubleSlabbinItFr || ((bstate.isAir() || bstate.canBeReplaced()) && canModifyPos(bpos)
                && stand.level().isFluidAtPosition(bpos, FluidState::isEmpty)
                && blockItem.place(new DirectionalPlaceContext(stand.level(), bpos, placeDir, held, placeDir))
                != InteractionResult.FAIL) || ((placeState.isAir() || placeState.canBeReplaced()) && canModifyPos(
                placePos) && blockItem.place(new DirectionalPlaceContext(stand.level(), placePos, placeDir, held, placeDir))
                != InteractionResult.FAIL);
        if (placesfrfr) {
            if (getCreative()) {
                stand.setHeldItem(returnStack);
            }
            else if (blockItem instanceof SolidBucketItem) {
                stand.setHeldItem(Items.BUCKET.getDefaultInstance());
            }
            setPowerNone();
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            this.setCooldown(PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhasePlaceCd);
            this.setCooldown(PowerIndex.SKILL_2_SNEAK,
                    ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhasePlaceCd);
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
            animateStand(KingNothingEntity.RETURN);
        }
        else {
            stand.setPos(oldPos);
        }
        return placesfrfr;
    }

    public boolean tryMineBlock(ItemStack held, BlockState state, BlockPos bpos, StandEntity stand) {
        boolean minesfr = held.isCorrectToolForDrops(state) && canModifyPos(bpos);
        if (minesfr) {
            this.getSelf().level().destroyBlock(bpos, false);
            if (!getCreative()) {
                BlockEntity $$6 = state.hasBlockEntity() ? getSelf().level().getBlockEntity(bpos) : null;
                Block.dropResources(state, getSelf().level(), bpos, $$6, null, held);
            }
            damageHeld(held, stand, this.getSelf());
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
            int cooldown = ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhaseMineCd;
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            this.setCooldown(PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK, cooldown);
            this.setCooldown(PowerIndex.SKILL_2_SNEAK, cooldown);
            animateStand(KingNothingEntity.RETURN);
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
        }
        return minesfr;
    }

    @Override
    public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.king_nothing." + getSkinString(skinId));
    }

    private boolean isPhasing() {
        return getActivePower() == PowerIndex.POWER_2 || getActivePower() == PowerIndex.POWER_2_SNEAK;
    }

    public boolean isTrackingItem() {
        return trackedItem != null;
    }

    public boolean isTrackingMemorizedScent() {
        return isTrackingMemorizedScent;
    }

    public boolean isStandHere() {
        return !isPhasing() && !isTrackingItem() && !isTrackingMemorizedScent();
    }

    @Override
    public void powerActivate(PowerContext context) {
        if ((context == PowerContext.SKILL_1_NORMAL || context == PowerContext.SKILL_1_CROUCH)) {
            if (getActivePower() == PowerIndex.POWER_2_SNEAK && getStandEntity(getSelf()) != null) {
                tryPhaseDropItem();
                return;
            }
            else if (isTrackingItem()) {
                tryEndItemTrack();
                return;
            }
            else if (isTrackingMemorizedScent()) {
                tryEndMemorizedScentTrack();
                return;
            }
        }
        switch (context) {
            case SKILL_1_NORMAL -> tryTrackMemorizedScent();
            case SKILL_1_CROUCH -> tryItemTrack();
            case SKILL_2_NORMAL -> phaseGrabClient();
            case SKILL_2_CROUCH -> phaseItemClient();
            case SKILL_3_NORMAL -> dash();
            case SKILL_3_CROUCH -> this.tryFormPredict();
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> this.tryMemorize();
        }
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        if (move == PowerIndex.POWER_4) {
            memorizeBlock(blockPos);
        }
        return super.tryBlockPosPower(move, forced, blockPos);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.isClient() && this.isPhasing()) {
            this.stopSoundsIfNearby(PHASE, 120, false);
        }
        return super.tryPower(move, forced);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> {
                return startMemorizedScentTrack();
            }
            case PowerIndex.POWER_1_SNEAK -> {
                return startItemTracking();
            }
            case PowerIndex.POWER_2 -> {
                return phaseGrab();
            }
            case PowerIndex.POWER_2_SNEAK -> {
                return phaseItem();
            }
            case PowerIndex.POWER_3_SNEAK -> {
                return this.formPredict();
            }
            case PowerIndex.EXTRA -> {
                return this.phaseDropItem(true);
            }
            case PowerIndex.EXTRA_2_FINISH -> {
                return this.phaseDropItem(false);
            }
            case PowerIndex.EXTRA_2 -> {
                return this.endItemTrack();
            }
            case PowerIndex.POWER_1_BONUS -> {
                return this.endMemorizedScentTrack();
            }
            case PowerIndex.POWER_4_BONUS -> {
                return this.memorizeEntity();
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case SYNC_TRACKING -> {
                if (data == 0) {
                    this.isTrackingMemorizedScent = false;
                }
                else if (data == 1) {
                    isTrackingMemorizedScent = true;
                }
            }
        }
        super.updatePowerInt(activePower, data);
    }

    private boolean startMemorizedScentTrack() {
        animateStand(KingNothingEntity.DISINTEGRATE);
        playSoundsIfNearby(DISINTEGRATE, 27, false);
        isTrackingMemorizedScent = true;
        disintegrateAnimTicks = 0;
        if (!isClient()) {
            TrackingCloudEntity tc = ModEntities.TRACKING_CLOUD.create(this.getSelf().level());
            if (tc != null) {
                tc.setUser(this.getSelf());
                tc.setPos(getStandEntity(getSelf()).getBoundingBox().getCenter());
                this.getSelf().level().addFreshEntity(tc);
                this.trackingCloud = tc;
            }
        }
        return true;
    }

    @Override
    public boolean setPowerNone() {
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        StandEntity stand = getStandEntity(getSelf());
        if (stand != null && getAnimation() == KingNothingEntity.REACH) {
            animateStand(KingNothingEntity.RETURN);
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
        }
        if (stand != null && getSelf() instanceof Player) {
            addItem(stand);
        }
        // server-client desync fix
        if (!getSelf().level().dimension().equals(dimension)) {
            if (isTrackingItem()) {
                playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
            }
            trackedItem = null;
            foundItemYet = false;
            dimension = getSelf().level().dimension();
        }
        return true;
    }

    private void tryItemTrack() {
        if (!isPhasing() && !isTrackingItem() && !onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }

    public void tryEndItemTrack() {
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.getStandUserSelf().roundabout$tryPower(PowerIndex.EXTRA_2, true);
            tryPowerPacket(PowerIndex.EXTRA_2);
        }
    }

    public void tryTrackMemorizedScent() {
        if (!isPhasing()) {
            this.getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }

    private void tryEndMemorizedScentTrack() {
        if (!onCooldown(PowerIndex.POWER_1)) {
            this.getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_1_BONUS, true);
            tryPowerPacket(PowerIndex.POWER_1_BONUS);
        }
    }

    public boolean endItemTrack() {
        if (isTrackingItem() && !onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.trackedItem = null;
            this.foundItemYet = false;
            this.itemHoldingEntities.clear();
            this.itemHoldingBlocks.clear();
            disintegrateAnimTicks = 0;
            if (getSelf() instanceof ServerPlayer) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, 20);
                this.setCooldown(PowerIndex.SKILL_1_SNEAK, 20);
            }
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
            animateStand(KingNothingEntity.RETURN);
            setPowerNone();
        }
        return true;
    }

    private boolean endMemorizedScentTrack() {
        if (isTrackingMemorizedScent() && !onCooldown(PowerIndex.SKILL_1)) {
            this.isTrackingMemorizedScent = false;
            if (!this.isClient()) {
                this.trackingCloud.discard();
                this.trackingCloud = null;
            }
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
            animateStand(KingNothingEntity.RETURN);
            setPowerNone();
        }
        return true;
    }

    public void tryFormPredict() {
        if (isStandHere()) {
            getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_3_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_3_SNEAK);
        }
    }

    public void tryPhaseDropItem() {
        if (isHoldingSneak()) {
            getStandUserSelf().roundabout$tryPower(PowerIndex.EXTRA_2_FINISH, true);
            tryPowerPacket(PowerIndex.EXTRA_2_FINISH);
        }
        else {
            getStandUserSelf().roundabout$tryPower(PowerIndex.EXTRA, true);
            tryPowerPacket(PowerIndex.EXTRA);
        }
    }

    public void phaseGrabClient() {
        if (!isTrackingItem()) {
            if (!isPhasing()) {
                if (!this.onCooldown(PowerIndex.SKILL_2)) {
                    this.getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_2, true);
                    tryPowerPacket(PowerIndex.POWER_2);
                }
            }
            else {
                setPowerNone();
                tryPowerPacket(PowerIndex.NONE);
            }
        }
    }

    public boolean phaseGrab() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setActivePower(PowerIndex.POWER_2);
            startPhaseMovement(stand);
            return true;
        }
        return false;
    }

    public void phaseItemClient() {
        if (!isTrackingItem()) {
            if (!isPhasing()) {
                if (!this.onCooldown(PowerIndex.SKILL_2_SNEAK)) {
                    getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_2_SNEAK, true);
                    tryPowerPacket(PowerIndex.POWER_2_SNEAK);
                }
            }
            else {
                setPowerNone();
                tryPowerPacket(PowerIndex.NONE);
            }
        }
    }

    public boolean phaseItem() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            if (this.inventoryGrab()) {
                this.setActivePower(PowerIndex.POWER_2_SNEAK);
                startPhaseMovement(stand);
                return true;
            }
        }
        return false;
    }

    public void startPhaseMovement(StandEntity stand) {
        if (!startPhaseTickAfter) {
            this.poseStand(OffsetIndex.LOOSE);
            animateStand(KingNothingEntity.REACH);
            this.attackTimeDuring = 0;
            playSoundsIfNearby(PHASE, 27, false);

            Vec2 twoVec = new Vec2((this.getSelf().getYHeadRot() % 360), (this.getSelf().getXRot()));
            Direction gdir = ((IGravityEntity) this.self).roundabout$getGravityDirection();
            Vec2 twoVecGrav = RotationUtil.rotPlayerToWorld(twoVec, gdir);
            Vec3 threeVec = new Vec3(0, 0.25, 0);
            threeVec = RotationUtil.vecPlayerToWorld(threeVec, gdir);

            stand.setYRot(twoVec.x);
            stand.setXRot(twoVec.y);
            moveVec = DamageHandler.getRotationVector(twoVecGrav.y, twoVecGrav.x).scale(1.8).add(threeVec.x,
                    threeVec.y, threeVec.z);
            stand.setPos(this.getSelf().getPosition(1).add(moveVec));
        }
    }

    public boolean inventoryGrab() {
        if (!this.isClient()) {
            StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()
                    && this.getSelf() instanceof Player PE) {
                ItemStack stack = PE.getMainHandItem();
                if (isGrabbable(stack, PE)) {
                    standEntity.canAcquireHeldItem = true;
                    standEntity.setHeldItem(stack.copyWithCount(1));
                    playSoundIfPossible(self.level(), null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT,
                            SoundSource.PLAYERS, 1.7F, 1.3F);
                    stack.shrink(1);
                    return true;
                }
            }
        }
        return false;
    }

    public void tryPhaseItemGrab(StandEntity stand, AABB bb1, AABB bb2) {
        bb1 = bb1.inflate(1.6F);
        bb2 = bb2.inflate(1.6F);

        AABB $$2 = bb1.minmax(bb2);
        List<Entity> $$3 = stand.level().getEntities(stand, $$2);
        if (!$$3.isEmpty()) {
            for (Entity $$4 : $$3) {
                if (stand.getSensing().hasLineOfSight($$4)) {
                    boolean gotItem = false;
                    if ($$4 instanceof ItemEntity IE) {
                        if (!(IE.getItem().getItem() instanceof BlockItem BI && (
                                BI.getBlock() instanceof ShulkerBoxBlock
                                        || BI.getBlock() instanceof FancyLighterBlock))) {
                            stand.canAcquireHeldItem = true;
                            stand.setHeldItem(IE.getItem().copyWithCount(1));

                            IE.getItem().shrink(1);
                            itemNearby(IE.getId());
                            gotItem = true;
                        }
                    }
                    else if ($$4 instanceof StrayCatEntity SC) {
                        if (Objects.equals(SC.getOwner(), getSelf()) && SC.getPotted()) {
                            ItemStack catto;
                            if (SC.getBreed() == (byte) 1) {
                                catto = new ItemStack(ModItems.STRAY_CAT_MANGA);
                            }
                            else {
                                catto = new ItemStack(ModItems.STRAY_CAT_ANIME);
                            }
                            StrayCatItem.saveStrayCatEntityInfo(catto, SC);
                            SC.discard();
                            stand.canAcquireHeldItem = true;
                            stand.setHeldItem(catto);
                            gotItem = true;
                        }
                    }
                    if (gotItem) {
                        this.getStandUserSelf().roundabout$tryPower(PowerIndex.NONE, true);
                        S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                                ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
                        this.setCooldown(PowerIndex.SKILL_2,
                                ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
                        S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK,
                                ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
                        this.setCooldown(PowerIndex.SKILL_2_SNEAK,
                                ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
                        this.setAttackTimeDuring(0);
                        poseStand(OffsetIndex.FOLLOW_NOLEAN);
                        animateStand(KingNothingEntity.RETURN);
                        playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
                        return;
                    }
                }
            }
        }
    }

    public void tryPhaseInteractMob(StandEntity stand, AABB bb1, AABB bb2) {
        double yShrink = 0.6;
        bb1 = bb1.contract(0, yShrink, 0).contract(0, -yShrink, 0);
        bb2 = bb2.contract(0, yShrink, 0).contract(0, -yShrink, 0);

        AABB $$2 = bb1.minmax(bb2);
        List<Entity> $$3 = stand.level().getEntities(stand, $$2);
        if (!$$3.isEmpty()) {
            for (Entity $$4 : $$3) {
                LivingEntity self = getSelf();
                if (!$$4.equals(stand) && !$$4.equals(self) && stand.getSensing().hasLineOfSight($$4) && !(
                        $$4 instanceof LivingEntity LE && LE.isDeadOrDying())) {
                    ItemStack held = stand.getHeldItem();
                    int cooldown = ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown;
                    boolean didThing = false;
                    if ($$4 instanceof Bucketable BE && held.is(Items.WATER_BUCKET)) {
                        ItemStack bucketItem = BE.getBucketItemStack();
                        BE.saveToBucketTag(bucketItem);
                        stand.setHeldItem(bucketItem);
                        getSelf().level().playSound(null, getSelf().blockPosition(), BE.getPickupSound(),
                                SoundSource.PLAYERS);
                        $$4.discard();
                        didThing = true;
                    }
                    else if ($$4 instanceof Cow COW && !COW.isBaby() && held.is(Items.BUCKET)) {
                        stand.setHeldItem(Items.MILK_BUCKET.getDefaultInstance());
                        getSelf().playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                        didThing = true;
                    }
                    else if ($$4 instanceof Mob MB && held.is(Items.LEAD) && getSelf() instanceof Player PE
                            && MB.canBeLeashed(PE)) {
                        MB.setLeashedTo(PE, true);
                        stand.setHeldItem(ItemStack.EMPTY);
                        didThing = true;
                    }
                    else if ($$4.isAttackable() && $$4.isPickable() && !($$4 instanceof Player PE && (PE.isCreative() || PE.isSpectator()))
                            && !($$4 instanceof OwnableEntity OE && Objects.equals(OE.getOwner(), getSelf())) && !(
                            $$4 instanceof Boat || $$4 instanceof Minecart)) {
                        cooldown = ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingPhaseAttackCd;
                        if (ClientNetworking.getAppropriateConfig().kingNothingSettings.kingNothingDynamicPhaseAttackCd) {
                            cooldown += (int) (1F / stand.getAttributeValue(Attributes.ATTACK_SPEED) * 20);
                        }
                        doPhaseAttack(held, stand, self, $$4);
                        didThing = true;
                    }
                    if (didThing) {
                        this.getStandUserSelf().roundabout$tryPower(PowerIndex.NONE, true);
                        S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2,
                                ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
                        this.setCooldown(PowerIndex.SKILL_2_SNEAK,
                                ClientNetworking.getAppropriateConfig().generalStandSettings.objectPocketCooldown);
                        S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_2_SNEAK,
                                cooldown);
                        this.setCooldown(PowerIndex.SKILL_2_SNEAK, cooldown);
                        this.setAttackTimeDuring(0);
                        poseStand(OffsetIndex.FOLLOW_NOLEAN);
                        animateStand(KingNothingEntity.RETURN);
                        playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
                        return;
                    }
                }
            }
        }
    }

    public void doPhaseAttack(ItemStack weapon, StandEntity stand, LivingEntity self, Entity target) {
        if (target.hurt(ModDamageTypes.of(stand.level(), DamageTypes.PLAYER_ATTACK, self, self),
                getDamage(weapon, target, stand))) {
            doHitEffects(weapon, target, stand, self);
        }
        if (stand.canDisableShield()) {
            if (target instanceof LivingEntity LE && LE.isBlocking()) {
                MainUtil.knockShield(target, 60);
            }
        }
        playSoundIfPossible(self.level(), null, self.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.PLAYERS, 1.7F, 1.3F);
        damageHeld(weapon, stand, self);
    }

    private void damageHeld(ItemStack held, StandEntity stand, LivingEntity self) {
        if (held.isDamageableItem() && !getCreative()) {
            if (held.hurt(1, stand.level().getRandom(), null)) {
                stand.setHeldItem(ItemStack.EMPTY);
                playSoundIfPossible(self.level(), null, self.blockPosition(), SoundEvents.ITEM_BREAK,
                        SoundSource.PLAYERS, 1.7f, 1.3f);
            }
        }
    }

    public float getDamage(ItemStack stack, Entity ent, StandEntity stand) {
        float damage = 1;
        if (stand != null) {
            damage = (float) stand.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1;
        }
        if (ent instanceof LivingEntity) {
            damage += EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) ent).getMobType());
        }
        else {
            damage += EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
        }
        return damage;
    }

    public void doHitEffects(ItemStack weapon, Entity target, StandEntity stand, LivingEntity self) {
        int bleedTicks = 0;
        int bleedLevel = 0;
        if (weapon.is(ModItems.SCISSORS) || weapon.is(ModItems.SACRIFICIAL_DAGGER)) {
            bleedTicks = 400;
        }
        else if (weapon.getItem() instanceof SignBlockItem) {
            bleedTicks = 100;
            bleedLevel = 1;
            CompoundTag ct = weapon.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            ctd++;
            if (ctd > 2) {
                self.level().playSound(null, self.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1);
                weapon.shrink(1);
            }
            else {
                self.level().playSound(null, self.blockPosition(), ModSounds.SIGN_HIT_EVENT, SoundSource.PLAYERS, 1F,
                        1);
                ct.putInt("damaged", ctd);
            }
        }
        if (bleedTicks > 0) {
            MainUtil.makeBleed(target, bleedLevel, bleedTicks, stand);
        }
        int ench = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, weapon);
        if (ench >= 1) {
            target.setSecondsOnFire(ench * 4);
        }
    }

    public void addItem(StandEntity standEntity) {
        if (!this.isClient()) {
            addItemLight(standEntity);
        }
        standEntity.setHeldItem(ItemStack.EMPTY);
    }

    public void addItemLight(StandEntity standEntity) {
        if (canAddItem(standEntity.getHeldItem(), ((Player) this.getSelf()).getInventory())) {
            if (!standEntity.getHeldItem().isEmpty()) {
                playSoundIfPossible(getSelf().level(), null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT,
                        SoundSource.PLAYERS, 1.7F, 1.3F);
            }
            ItemStack playerHeld = this.getSelf().getMainHandItem();
            if (playerHeld.isEmpty()) {
                this.getSelf().setItemSlot(EquipmentSlot.MAINHAND, standEntity.getHeldItem());
            }
            else if ((ItemStack.isSameItemSameTags(standEntity.getHeldItem(), playerHeld)
                    && playerHeld.getCount() < playerHeld.getMaxStackSize())) {
                this.getSelf().setItemSlot(EquipmentSlot.MAINHAND, playerHeld.copyWithCount(playerHeld.getCount() + 1));
            }
            else {
                ((Player) this.getSelf()).addItem(standEntity.getHeldItem());
            }
        }
        else {
            ItemEntity $$4 = new ItemEntity(this.getSelf().level(), this.getSelf().getX(),
                    this.getSelf().getY() + this.getSelf().getEyeHeight(), this.getSelf().getZ(),
                    standEntity.getHeldItem());
            $$4.setPickUpDelay(40);
            $$4.setThrower(this.getSelf().getUUID());
            PowerTypes.copyPlaneOfExisting(self, $$4);
            this.getSelf().level().addFreshEntity($$4);
        }
    }

    public boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack)
                    || itemStack2.getCount() >= itemStack2.getMaxStackSize())) {
                continue;
            }
            bl = true;
            break;
        }
        return bl;
    }

    public final void itemNearby(int id) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(), this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (serverPlayerEntity.level() != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    S2CPacketUtil.sendGenericIntToClientPacket(serverPlayerEntity, PacketDataIndex.S2C_INT_GRAB_ITEM,
                            id);
                }
            }
        }
    }

    public void tryMemorize() {
        if (isStandHere()) {
            Entity targEnt = MainUtil.getTargetEntity(this.getSelf(), SMELL_RANGE, 0);
            if (targEnt instanceof LivingEntity && !MainUtil.isMobSniffBlacklisted(targEnt)) {
                this.getStandUserSelf().roundabout$tryPower(PowerIndex.POWER_4_BONUS, true);
                tryPowerPacket(PowerIndex.POWER_4_BONUS);
                return;
            }
            Vec3 vec3d = this.getSelf().getEyePosition(0);
            Vec3 vec3d2 = this.getSelf().getViewVector(0);
            Vec3 vec3d3 = vec3d.add(vec3d2.x * SMELL_RANGE, vec3d2.y * SMELL_RANGE, vec3d2.z * SMELL_RANGE);
            BlockHitResult blockHit = this.getSelf().level().clip(
                    new ClipContext(vec3d, vec3d3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.getSelf()));
            if (blockHit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = blockHit.getBlockPos();
                getStandUserSelf().roundabout$tryBlockPosPower(PowerIndex.POWER_4, true, pos);
                tryBlockPosPowerPacket(PowerIndex.POWER_4, pos);
            }
        }
    }

    public boolean phaseDropItem(boolean doInteractions) {
        StandEntity stand = getStandEntity(getSelf());
        if (!isClient()) {
            ItemStack held = stand.getHeldItem();
            Vec3 standPos = stand.getBoundingBox().getCenter();
            boolean specialInteraction = false;
            if (doInteractions) {
                Entity projectile = null;
                if (held.getItem() instanceof ThrowablePotionItem) {
                    projectile = new ThrownPotion(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z());
                }
                else if (held.getItem() instanceof EggItem) {
                    projectile = new ThrownEgg(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z());
                }
                else if (held.getItem() instanceof ExperienceBottleItem) {
                    projectile = new ThrownExperienceBottle(this.getSelf().level(), standPos.x(), standPos.y(),
                            standPos.z());
                }
                else if (held.getItem() instanceof EnderpearlItem) {
                    projectile = new ThrownEnderpearl(this.getSelf().level(), getSelf());
                    projectile.setPos(standPos);
                    if (getSelf() instanceof Player PE) {
                        PE.getCooldowns().addCooldown(held.getItem(), 20);
                    }
                }
                else if (held.getItem() instanceof SnowballItem) {
                    projectile = new Snowball(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z());
                }
                else if (held.getItem() instanceof GasolineCanItem) {
                    projectile =
                            new GasolineCanEntity(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z());
                    if (getSelf() instanceof Player PE) {
                        PE.getCooldowns().addCooldown(held.getItem(), 40);
                        Inventory inv = PE.getInventory();
                        for (ItemStack stack : inv.items) {
                            if (stack.getItem() instanceof BowItem) {
                                PE.getCooldowns().addCooldown(stack.getItem(), 30);
                            }
                        }
                        for (ItemStack stack : inv.offhand) {
                            if (stack.getItem() instanceof CrossbowItem) {
                                PE.getCooldowns().addCooldown(stack.getItem(), 30);
                            }
                        }

                        PE.getCooldowns().addCooldown(Items.FLINT_AND_STEEL, 30);
                        PE.getCooldowns().addCooldown(Items.FIRE_CHARGE, 30);
                        PE.getCooldowns().addCooldown(Items.BOW, 30);
                        PE.getCooldowns().addCooldown(ModItems.MATCH, 30);
                        PE.getCooldowns().addCooldown(ModItems.GASOLINE_BUCKET, 30);
                        PE.getCooldowns().addCooldown(ModItems.IRON_BALL_CROSSBOW, 30);
                        PE.getCooldowns().addCooldown(Items.LAVA_BUCKET, 30);
                        PE.getCooldowns().addCooldown(Items.CROSSBOW, 30);
                        PE.getCooldowns().addCooldown(ModItems.MATCH_BUNDLE, 30);
                    }
                }
                else if (held.is(ModItems.MATCH)) {
                    projectile = new MatchEntity(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z());
                }
                else if (held.is(ModItems.KNIFE)) {
                    projectile = new KnifeEntity(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z());
                }
                else if (held.getItem() instanceof FireworkRocketItem) {
                    projectile =
                            new FireworkRocketEntity(this.getSelf().level(), standPos.x(), standPos.y(), standPos.z(),
                                    held);
                }
                else if (held.getItem() instanceof StrayCatItem SC) {
                    StrayCatEntity stray = ModEntities.STRAY_CAT.create(this.getSelf().level());
                    if (stray != null) {
                        CompoundTag tag = held.getOrCreateTag();

                        if (tag.contains(StrayCatItem.OWNER_UUID_TAG)) {
                            stray.setOwnerUUID(tag.getUUID(StrayCatItem.OWNER_UUID_TAG));
                            stray.setTame(true);
                        }
                        if (tag.contains(StrayCatItem.HEALTH_TAG)) {
                            stray.setHealth(tag.getFloat(StrayCatItem.HEALTH_TAG));
                        }
                        stray.setBreed(SC.breed);
                        if (held.hasCustomHoverName()) {
                            stray.setCustomName(held.getHoverName());
                        }
                        stray.setPotted(true);
                        stray.setPos(standPos);
                        stray.setYRot(stand.getYRot());
                        projectile = stray;
                    }
                }
                if (projectile != null) {
                    if (projectile instanceof ThrowableItemProjectile TP) {
                        TP.setItem(held);
                    }
                    if (!(projectile instanceof FireworkRocketEntity)) {
                        projectile.setDeltaMovement(0, 0.2, 0);
                    }
                    if (projectile instanceof Projectile PE) {
                        PE.setOwner(this.getSelf());
                    }
                    PowerTypes.copyPlaneOfExisting(self, projectile);
                    this.getSelf().level().addFreshEntity(projectile);
                    specialInteraction = true;
                }
            }
            if (!specialInteraction) {
                ItemEntity $$4 =
                        new ItemEntity(this.getSelf().level(), standPos.x(),
                                standPos.y(),
                                standPos.z(), stand.getHeldItem(), 0, 0.2, 0);
                $$4.setPickUpDelay(40);
                $$4.setThrower(this.getSelf().getUUID());
                PowerTypes.copyPlaneOfExisting(self, $$4);
                this.getSelf().level().addFreshEntity($$4);
            }
            if (!(specialInteraction && getSelf() instanceof Player PE && PE.isCreative())) {
                stand.setHeldItem(ItemStack.EMPTY);
            }
            setPowerNone();
            animateStand(KingNothingEntity.RETURN);
            playSoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false);
        }

        return true;
    }

    @Override
    public void onStandSummon(boolean desummon) {
        super.onStandSummon(desummon);
        this.dimension = getSelf().level().dimension();
        if (desummon) {
            if (isPhasing()) {
                animateStand(StandEntity.IDLE);
            }
            this.isOut = false;
            this.trackedItem = null;
            this.foundItemYet = false;
            this.itemHoldingEntities.clear();
            this.itemHoldingBlocks.clear();
            this.isTrackingMemorizedScent = false;
            if (trackingCloud != null) {
                this.trackingCloud.discard();
                this.trackingCloud = null;
            }
        }
        else if (!isOut && canSummonStandAsEntity()) {
            isOut = true;
            summonAnimTicks = 0;
            returnAnimTicks = 0;
            animateStand(KingNothingEntity.SUMMON);
            startPhaseTickAfter = true;
        }
    }

    public boolean formPredict() {
        if (!isClient()) {
            Item held = this.getSelf().getMainHandItem().getItem();
            Block potentialBlock = getBlockFormPrediction(held);
            if (potentialBlock != null) {
                this.setMemorizedScent(potentialBlock);
                if (getSelf() instanceof Player PE) {
                    int id = BuiltInRegistries.BLOCK.getId(potentialBlock);
                    S2CPacketUtil.sendGenericIntToClientPacket(PE, PacketDataIndex.S2C_KN_SYNC_FORM_PREDICT_BLOCK, id);
                    PE.displayClientMessage(potentialBlock.getName().withStyle(ChatFormatting.DARK_PURPLE), true);
                }
                return true;
            }
            EntityType<?> potentialEntity = getEntityFormPrediction(held);
            if (potentialEntity != null) {
                this.setMemorizedScent(potentialEntity);
                if (getSelf() instanceof Player PE) {
                    int id = BuiltInRegistries.ENTITY_TYPE.getId(potentialEntity);
                    S2CPacketUtil.sendGenericIntToClientPacket(PE, PacketDataIndex.S2C_KN_SYNC_FORM_PREDICT_ENTITY, id);
                }
            }
        }
        return true;
    }

    public boolean hasFormPrediction(Item item) {
        return getEntityFormPrediction(item) != null || getBlockFormPrediction(item) != null;
    }

    public EntityType<?> getEntityFormPrediction(Item item) {
        return getEntityFormPrediction(item, true);
    }

    public EntityType<?> getEntityFormPrediction(Item item, boolean doRecursion) {
        if (MainUtil.KING_NOTHING_ENTITY_FORM_PREDICT.get(item) != null) {
            return MainUtil.KING_NOTHING_ENTITY_FORM_PREDICT.get(item);
        }
        else if ((item instanceof TieredItem || item instanceof ArmorItem) && doRecursion) {
            Set<Item> ingredients = new HashSet<>();
            ItemStack[] ingredientStacks = new ItemStack[0];
            EntityType<?> toReturn = null;
            if (item instanceof TieredItem TI) {
                ingredientStacks = TI.getTier().getRepairIngredient().getItems();
            }
            else if (item instanceof ArmorItem AI) {
                ingredientStacks = AI.getMaterial().getRepairIngredient().getItems();
            }
            for (ItemStack stack : ingredientStacks) {
                ingredients.add(stack.getItem());
            }
            for (Item ingItem : ingredients) {
                EntityType<?> current = getEntityFormPrediction(ingItem, false);
                if (toReturn == null) {
                    toReturn = current;
                }
                else if (!current.equals(toReturn)) {
                    toReturn = null;
                    break;
                }
            }
            return toReturn;
        }
        return null;
    }

    public Block getBlockFormPrediction(Item item) {
        return getBlockFormPrediction(item, true);
    }

    public Block getBlockFormPrediction(Item item, boolean doRecursion) {
        if (MainUtil.KING_NOTHING_BLOCK_FORM_PREDICT.get(item) != null) {
            return MainUtil.KING_NOTHING_BLOCK_FORM_PREDICT.get(item);
        }
        else if ((item instanceof TieredItem || item instanceof ArmorItem) && doRecursion) {
            Set<Item> ingredients = new HashSet<>();
            ItemStack[] ingredientStacks = new ItemStack[0];
            Block toReturn = null;
            if (item instanceof TieredItem TI) {
                ingredientStacks = TI.getTier().getRepairIngredient().getItems();
            }
            else if (item instanceof ArmorItem AI) {
                ingredientStacks = AI.getMaterial().getRepairIngredient().getItems();
            }
            for (ItemStack stack : ingredientStacks) {
                ingredients.add(stack.getItem());
            }
            for (Item ingItem : ingredients) {
                Block current = getBlockFormPrediction(ingItem, false);
                if (toReturn == null) {
                    toReturn = current;
                }
                else if (!current.equals(toReturn)) {
                    toReturn = null;
                    break;
                }
            }
            return toReturn;
        }
        return null;
    }

    private void memorizeBlock(BlockPos blockPos) {
        Block block = this.getSelf().level().getBlockState(blockPos).getBlock();
        if (isMemorizableBlock(block)) {
            if (getSelf() instanceof Player PE) {
                PE.displayClientMessage(block.getName().withStyle(ChatFormatting.DARK_PURPLE), true);
            }
            setMemorizedScent(block);
        }
    }

    private boolean startItemTracking() {
        if (!getSelf().getMainHandItem().isEmpty() && !onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.trackedItem = getSelf().getMainHandItem().getItem();
            if (getSelf() instanceof ServerPlayer) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, 20);
                this.setCooldown(PowerIndex.SKILL_1_SNEAK, 20);
            }
            playSoundsIfNearby(DISINTEGRATE, 27, true);
            animateStand(KingNothingEntity.DISINTEGRATE);
            disintegrateAnimTicks = 0;
        }
        return true;
    }

    public boolean memorizeEntity() {
        Entity ent = MainUtil.getTargetEntity(this.getSelf(), SMELL_RANGE, 0);
        if (ent instanceof LivingEntity LE && !MainUtil.isMobSniffBlacklisted(ent)) {
            setMemorizedScent(LE);
            if (this.getSelf() instanceof Player PE) {
                PE.displayClientMessage(LE.getName().copy().withStyle(ChatFormatting.DARK_PURPLE), true);
            }
        }
        return true;
    }

    private boolean isMemorizableBlock(Block block) {
        return !block.asItem().equals(Items.AIR) && !(block instanceof FogBlock) && block.defaultDestroyTime() >= 0
                && !MainUtil.isBlockSniffBlacklisted(block);
    }

    public void setMemorizedScent(Block memorizedBlock) {
        this.memorizedBlock = memorizedBlock;
        this.memorizedEntityType = null;
        this.memorizedPlayer = null;
        saveDiscAndSync();
    }

    public void setMemorizedScent(LivingEntity entity) {
        if (entity instanceof Player PE) {
            this.memorizedPlayer = PE.getUUID();
            this.memorizedEntityType = null;
        }
        else if (entity instanceof CloneEntity CE && CE.getPlayerUUID().isPresent()) {
            this.memorizedPlayer = CE.getPlayerUUID().get();
            this.memorizedEntityType = null;
        }
        else {
            this.memorizedEntityType = entity.getType();
            this.memorizedPlayer = null;
        }
        this.memorizedBlock = null;
        saveDiscAndSync();
    }

    public void setMemorizedScent(EntityType<?> entityType) {
        this.memorizedEntityType = entityType;
        this.memorizedBlock = null;
        this.memorizedPlayer = null;
        if (getSelf() instanceof Player PE && isClient()) {
            Entity tity = entityType.create(this.getSelf().level());
            if (tity != null) {
                PE.displayClientMessage(tity.getName().copy().withStyle(ChatFormatting.DARK_PURPLE), true);
                tity.discard();
            }
        }
    }

    public byte getMemorizedScentType() {
        if (memorizedPlayer != null) {
            return TrackingCloudEntity.PLAYER;
        }
        else if (memorizedEntityType != null) {
            return TrackingCloudEntity.MOB;
        }
        return TrackingCloudEntity.BLOCK;
    }

    public Block getMemorizedBlock() {
        return memorizedBlock;
    }

    public EntityType<?> getMemorizedEntityType() {
        return memorizedEntityType;
    }

    public UUID getMemorizedPlayer() {
        return memorizedPlayer;
    }

    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("JacobBoxCEO").withStyle(ChatFormatting.DARK_GREEN);
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.DARK_PURPLE);
    }

    // color 1 = ~~primary~~ main particle color
    // color 2 = ~~secondary~~ specialized particle color
    // highlight color = take a guess
    // percent other = the percent of particles that will be color 2
    public record VisualData(int color1, int color2, int highlightColor, float percentOther) {
    }
}
