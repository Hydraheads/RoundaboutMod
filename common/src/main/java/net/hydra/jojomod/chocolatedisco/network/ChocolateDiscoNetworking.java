    package net.hydra.jojomod.chocolatedisco.network;

import net.hydra.jojomod.networking.ClientToServerPackets;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.zetalasis.networking.message.api.ModMessageEvents;
    
    import net.hydra.jojomod.chocolatedisco.ChocolateDiscoGrid;
    import net.hydra.jojomod.chocolatedisco.IChocolateDiscoProjectileAccess;
    import net.hydra.jojomod.item.ModItems;
    import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
    import net.hydra.jojomod.event.index.PowerIndex;
    import net.hydra.jojomod.event.powers.StandPowers;
    import net.hydra.jojomod.event.powers.StandUser;
    import net.minecraft.core.BlockPos;
    import net.minecraft.core.Direction;
    import net.minecraft.network.chat.Component;
        import net.minecraft.server.MinecraftServer;
    import net.minecraft.server.level.ServerLevel;
    import net.minecraft.server.level.ServerPlayer;
    import net.minecraft.world.InteractionHand;
    import net.minecraft.world.entity.EntityType;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.entity.Mob;
    import net.minecraft.world.entity.item.FallingBlockEntity;
    import net.minecraft.world.entity.item.PrimedTnt;
    import net.minecraft.world.entity.projectile.Projectile;
    import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
    import net.minecraft.world.entity.projectile.ThrownPotion;
    import net.minecraft.world.item.BlockItem;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.block.Block;
    import net.minecraft.world.level.block.Blocks;
    import net.minecraft.world.level.block.PointedDripstoneBlock;
    import net.minecraft.world.phys.AABB;
    import net.minecraft.world.phys.Vec3;
    import net.hydra.jojomod.entity.stand.FollowingStandEntity;
    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.Iterator;
    import java.util.Map;
    import java.util.Set;
    import java.util.UUID;
    
    
    public class ChocolateDiscoNetworking {
    
        public static final String TELEPORT_PROJECTILES = ClientToServerPackets.StandPowerPackets.MESSAGES.ChocolateDiscoTeleportProjectiles.value;
        public static final String QUEUE_ITEM = ClientToServerPackets.StandPowerPackets.MESSAGES.ChocolateDiscoQueueItem.value;
        public static final String REDIRECT_PROJECTILES = ClientToServerPackets.StandPowerPackets.MESSAGES.ChocolateDiscoRedirectProjectiles.value;
        public static final String GRID_LOCK_STATE = ClientToServerPackets.StandPowerPackets.MESSAGES.ChocolateDiscoGridLockState.value;
        public static final String BUILDING_MODE_STATE = ClientToServerPackets.StandPowerPackets.MESSAGES.ChocolateDiscoBuildingModeState.value;
        public static final String BUILDING_SELECTION = ClientToServerPackets.StandPowerPackets.MESSAGES.ChocolateDiscoBuildingSelection.value;

        public static final String QUEUE_STATE = ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoQueueState.value;
        public static final String BUILDING_SELECTION_CLEAR = ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoBuildingSelectionClear.value;
        public static final String BUILDING_MODE_RESET = ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoBuildingModeReset.value;
        public static final String DISCO_SUMMON_SOUND = ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoSummonSound.value;

        public static void playDiscoSummonSound(ServerPlayer player) {
            ModMessageEvents.sendToPlayer(
                    player,
                    ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoSummonSound.value
            );
        }

        public static boolean isRedirectActive(UUID playerUUID) {
            return REDIRECT_SESSIONS.containsKey(playerUUID);
        }
    
        private static final int DETECTION_TICKS = 40;
    
        private static final int BUILDING_MODE_QUEUE_MAX_ITEMS = 64 * 10;
    
        private static final double AIM_ASSIST_RADIUS = 2.5D;
    
        private static final double AIM_ASSIST_MAX_ANGLE =
                Math.toRadians(20.0D);
    
        private static final Map<UUID, ItemStack> QUEUED_ITEMS =
                new HashMap<>();
    
        private static final Map<UUID, DetectionSession> ACTIVE_SESSIONS =
                new HashMap<>();
    
        private static final Map<UUID, RedirectSession> REDIRECT_SESSIONS =
                new HashMap<>();
    
    
        private static final Map<UUID, Boolean> GRID_LOCK_STATES =
                new HashMap<>();
    
        private static final Map<UUID, Boolean> BUILDING_MODE_STATES =
                new HashMap<>();
    
        private static final Map<UUID, Set<Integer>> BUILDING_SELECTED_CELLS =
                new HashMap<>();
    
        private static final Set<UUID> BUILDING_PLACEMENT_CONFIRMED =
                new HashSet<>();
    
        private static final Map<UUID, LockedGridTransform>
                LOCKED_GRID_TRANSFORMS =
                new HashMap<>();
    
        private static final Set<Block> BUILDING_MODE_BLACKLIST =
                Set.of(
                        Blocks.TNT,
                        Blocks.OBSIDIAN,
                        Blocks.POINTED_DRIPSTONE,
                        Blocks.COBWEB,
                        Blocks.REDSTONE_BLOCK,
                        Blocks.REDSTONE_WIRE,
                        Blocks.REDSTONE_TORCH,
                        Blocks.REDSTONE_WALL_TORCH,
                        Blocks.REPEATER,
                        Blocks.COMPARATOR,
                        Blocks.LEVER,
                        Blocks.PISTON,
                        Blocks.STICKY_PISTON,
                        Blocks.OBSERVER,
                        Blocks.DISPENSER,
                        Blocks.DROPPER,
                        Blocks.HOPPER,
                        Blocks.TARGET,
                        Blocks.NOTE_BLOCK,
                        Blocks.DAYLIGHT_DETECTOR,
                        Blocks.TRAPPED_CHEST,
                        Blocks.TRIPWIRE_HOOK,
                        Blocks.TRIPWIRE,
                        Blocks.IRON_DOOR,
                        Blocks.IRON_TRAPDOOR,
                        Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
                        Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE
                );
    
        /*
         * ================================================================
         * LOCKED GRID TRANSFORM
         * ================================================================
         */
    
        private static class LockedGridTransform {
    
            final double x;
            final double y;
            final double z;
            final float yaw;
    
            LockedGridTransform(
                    double x,
                    double y,
                    double z,
                    float yaw
            ) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.yaw = yaw;
            }
        }
    
        /*
         * ================================================================
         * NORMAL Z DETECTION SESSION
         * ================================================================
         */
    
        private static class DetectionSession {
    
            final int column;
            final int row;
            final int gridSize;
            final boolean highTeleport;
    
            final double transformX;
            final double transformY;
            final double transformZ;
            final float transformYaw;
    
            int ticksRemaining;
    
            final Set<Projectile> teleportedProjectiles =
                    new HashSet<>();
    
            DetectionSession(
                    int column,
                    int row,
                    int gridSize,
                    int ticksRemaining,
                    boolean highTeleport,
                    double transformX,
                    double transformY,
                    double transformZ,
                    float transformYaw
            ) {
                this.column = column;
                this.row = row;
                this.gridSize = gridSize;
                this.ticksRemaining = ticksRemaining;
                this.highTeleport = highTeleport;
    
                this.transformX = transformX;
                this.transformY = transformY;
                this.transformZ = transformZ;
                this.transformYaw = transformYaw;
            }
        }
    
        /*
         * ================================================================
         * SHIFT+V REDIRECT SESSION
         * ================================================================
         */
    
        private static class RedirectSession {
    
            int startupTicksRemaining;
            int activeTicksRemaining;
    
            boolean queuedItemThrown = false;
    
            final Set<Projectile> teleportedProjectiles =
                    new HashSet<>();
    
            RedirectSession(
                    int startupTicksRemaining,
                    int activeTicksRemaining
            ) {
                this.startupTicksRemaining =
                        startupTicksRemaining;
    
                this.activeTicksRemaining =
                        activeTicksRemaining;
            }
        }
    
        /*
         * ================================================================
         * FROZEN PLAYER
         * ================================================================
         */
    
        private static class FrozenPlayer {
    
            final double x;
            final double y;
            final double z;
    
            int ticksRemaining;
    
            FrozenPlayer(
                    double x,
                    double y,
                    double z,
                    int ticksRemaining
            ) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.ticksRemaining = ticksRemaining;
            }
        }
    
        /*
         * ================================================================
         * PLAYER DETECTION AREA
         * ================================================================
         */
    
        private static AABB getChocolateDiscoPlayerDetectionArea(
                ServerPlayer player
        ) {
    
            double centerX =
                    player.getX();
    
            double centerZ =
                    player.getZ();
    
            double minX =
                    centerX - 4.5D;
    
            double maxX =
                    centerX + 4.5D;
    
            double minZ =
                    centerZ - 4.5D;
    
            double maxZ =
                    centerZ + 4.5D;
    
            double minY =
                    player.getY() - 1.0D;
    
            double maxY =
                    player.getY() + 3.0D;
    
            return new AABB(
                    minX,
                    minY,
                    minZ,
                    maxX,
                    maxY,
                    maxZ
            );
        }
    
        /*
         * ================================================================
         * GRID DETECTION AREA
         * ================================================================
         */
    
        private static AABB getChocolateDiscoGridDetectionArea(
                ServerPlayer player
        ) {
    
            double minX =
                    Double.MAX_VALUE;
    
            double minZ =
                    Double.MAX_VALUE;
    
            double maxX =
                    -Double.MAX_VALUE;
    
            double maxZ =
                    -Double.MAX_VALUE;
    
            int gridSize =
                    ChocolateDiscoGrid.GRID_SIZE;
    
            for (
                    int column = 0;
                    column < gridSize;
                    column++
            ) {
    
                for (
                        int row = 0;
                        row < gridSize;
                        row++
                ) {
    
                    BlockPos block =
                            ChocolateDiscoGrid.getBlock(
                                    player,
                                    column,
                                    row,
                                    gridSize
                            );
    
                    minX =
                            Math.min(
                                    minX,
                                    block.getX()
                            );
    
                    minZ =
                            Math.min(
                                    minZ,
                                    block.getZ()
                            );
    
                    maxX =
                            Math.max(
                                    maxX,
                                    block.getX() + 1.0D
                            );
    
                    maxZ =
                            Math.max(
                                    maxZ,
                                    block.getZ() + 1.0D
                            );
                }
            }
    
            double minY =
                    player.getY() - 1.0D;
    
            double maxY =
                    player.getY() + 3.0D;
    
            return new AABB(
                    minX,
                    minY,
                    minZ,
                    maxX,
                    maxY,
                    maxZ
            );
        }
    
        /*
         * ================================================================
         * QUEUE STATE SYNC
         * ================================================================
         */
    
        private static void syncQueueState(ServerPlayer player) {
            ItemStack queuedItem = QUEUED_ITEMS.get(player.getUUID());
            boolean queued = queuedItem != null && !queuedItem.isEmpty();
            ModMessageEvents.sendToPlayer(
                    player,
                    ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoQueueState.value,
                    queued,
                    queued ? queuedItem.copy() : ItemStack.EMPTY
            );
        }

        /*
         * ================================================================
         * BUILDING BLOCK POSITION
         * ================================================================
         */
    
        private static BlockPos getBuildingBlock(
                ServerPlayer player,
                int column,
                int row
        ) {
    
            boolean locked =
                    Boolean.TRUE.equals(
                            GRID_LOCK_STATES.get(
                                    player.getUUID()
                            )
                    );
    
            if (!locked) {
    
                return ChocolateDiscoGrid.getBlock(
                        player,
                        column,
                        row,
                        ChocolateDiscoGrid.GRID_SIZE
                );
            }
    
            LockedGridTransform transform =
                    LOCKED_GRID_TRANSFORMS.get(
                            player.getUUID()
                    );
    
            if (transform == null) {
    
                return ChocolateDiscoGrid.getBlock(
                        player,
                        column,
                        row,
                        ChocolateDiscoGrid.LARGE_GRID_SIZE
                );
            }
    
            int gridSize =
                    ChocolateDiscoGrid.LARGE_GRID_SIZE;
    
            Direction direction =
                    Direction.fromYRot(
                            transform.yaw
                    );
    
            int forwardX =
                    direction.getStepX();
    
            int forwardZ =
                    direction.getStepZ();
    
            int sideX =
                    -forwardZ;
    
            int sideZ =
                    forwardX;
    
            int center =
                    gridSize / 2;
    
            int columnOffset =
                    column - center;
    
            int forwardOffset =
                    3
                            + (
                            gridSize
                                    - 1
                                    - row
                    );
    
            int targetX =
                    (int) Math.floor(
                            transform.x
                                    + sideX * columnOffset
                                    + forwardX * forwardOffset
                    );
    
            int targetY =
                    (int) Math.floor(
                            transform.y
                    );
    
            int targetZ =
                    (int) Math.floor(
                            transform.z
                                    + sideZ * columnOffset
                                    + forwardZ * forwardOffset
                    );
    
            return new BlockPos(
                    targetX,
                    targetY,
                    targetZ
            );
        }
    
        /*
         * ================================================================
         * FIND BUILDING PLACEMENT POSITION
         * ================================================================
         */
    
        private static BlockPos findBuildingPlacementPosition(
                ServerLevel level,
                BlockPos target
        ) {
    
            int maxBuildHeight =
                    level.getMaxBuildHeight();
    
            while (
                    target.getY() < maxBuildHeight
                            && !level.getBlockState(
                            target
                    ).canBeReplaced()
            ) {
    
                target =
                        target.above();
            }
    
            if (
                    target.getY() >= maxBuildHeight
            ) {
    
                return null;
            }
    
            return target;
        }
    
        /*
         * ================================================================
         * PLACE BUILDING BLOCKS
         * ================================================================
         */
    
        private static void placeBuildingBlocks(
                ServerPlayer player
        ) {
    
            UUID playerUUID =
                    player.getUUID();
    
            if (
                    !Boolean.TRUE.equals(
                            BUILDING_MODE_STATES.get(
                                    playerUUID
                            )
                    )
            ) {
                return;
            }
    
            Set<Integer> selectedCells =
                    BUILDING_SELECTED_CELLS.get(
                            playerUUID
                    );
    
            if (
                    selectedCells == null
                            || selectedCells.isEmpty()
            ) {
    
                player.displayClientMessage(
                        Component.literal(
                                "No building tiles selected!"
                        ),
                        true
                );
    
                return;
            }
    
            ItemStack queuedItem =
                    QUEUED_ITEMS.get(
                            playerUUID
                    );
    
            if (
                    queuedItem == null
                            || queuedItem.isEmpty()
            ) {
    
                player.displayClientMessage(
                        Component.literal(
                                "No block is queued!"
                        ),
                        true
                );
    
                return;
            }
    
            if (
                    !(queuedItem.getItem()
                            instanceof BlockItem blockItem)
            ) {
    
                player.displayClientMessage(
                        Component.literal(
                                "The queued item is not a block!"
                        ),
                        true
                );
    
                return;
            }
    
            Block block =
                    blockItem.getBlock();
    
            if (
                    BUILDING_MODE_BLACKLIST.contains(
                            block
                    )
            ) {
    
                player.displayClientMessage(
                        Component.literal(
                                "This block cannot be used in Building Mode!"
                        ),
                        true
                );
    
                return;
            }
    
            int selectedCount =
                    selectedCells.size();
    
            int queuedCount =
                    queuedItem.getCount();
    
            if (
                    selectedCount > queuedCount
                            && !BUILDING_PLACEMENT_CONFIRMED.contains(
                            playerUUID
                    )
            ) {
    
                BUILDING_PLACEMENT_CONFIRMED.add(
                        playerUUID
                );
    
                player.displayClientMessage(
                        Component.literal(
                                "Only "
                                        + queuedCount
                                        + " blocks are queued for "
                                        + selectedCount
                                        + " selected tiles. Press Z again to place anyway."
                        ),
                        true
                );
    
                return;
            }
    
            BUILDING_PLACEMENT_CONFIRMED.remove(
                    playerUUID
            );
    
            ServerLevel level =
                    player.serverLevel();
    
            int placedCount =
                    0;
    
            Set<Integer> cellsToPlace =
                    new HashSet<>(
                            selectedCells
                    );
    
            for (
                    int cellKey :
                    cellsToPlace
            ) {
    
                if (
                        queuedItem.isEmpty()
                ) {
                    break;
                }
    
                int row =
                        cellKey / 100;
    
                int column =
                        cellKey % 100;
    
                boolean locked =
                        Boolean.TRUE.equals(
                                GRID_LOCK_STATES.get(
                                        playerUUID
                                )
                        );
    
                int gridSize =
                        locked
                                ? ChocolateDiscoGrid.LARGE_GRID_SIZE
                                : ChocolateDiscoGrid.GRID_SIZE;
    
                if (
                        column < 0
                                || column >= gridSize
                                || row < 0
                                || row >= gridSize
                ) {
                    continue;
                }
    
                BlockPos target =
                        getBuildingBlock(
                                player,
                                column,
                                row
                        );
    
                target =
                        findBuildingPlacementPosition(
                                level,
                                target
                        );
    
                if (target == null) {
                    continue;
                }
    
                level.setBlock(
                        target,
                        block.defaultBlockState(),
                        3
                );
    
                queuedItem.shrink(1);
    
                placedCount++;
            }
    
            if (
                    queuedItem.isEmpty()
            ) {
    
                QUEUED_ITEMS.remove(
                        playerUUID
                );
    
                syncQueueState(player);
            }
    
            player.displayClientMessage(
                    Component.literal(
                            "Placed "
                                    + placedCount
                                    + " blocks!"
                    ),
                    true
            );
        }
    
        /*
         * ================================================================
         * SPAWN QUEUED POTION FOR Z
         * ================================================================
         */
    
        private static void spawnQueuedPotion(
                ServerLevel level,
                ServerPlayer player,
                DetectionSession session,
                ItemStack queuedItem,
                double x,
                double y,
                double z
        ) {
    
            net.minecraft.world.item.Item potionItem =
                    queuedItem.getItem();
    
            if (
                    potionItem != net.minecraft.world.item.Items.POTION
                            && potionItem != net.minecraft.world.item.Items.SPLASH_POTION
                            && potionItem != net.minecraft.world.item.Items.LINGERING_POTION
            ) {
                return;
            }
    
            if (
                    potionItem == net.minecraft.world.item.Items.POTION
            ) {
    
                potionItem =
                        net.minecraft.world.item.Items.SPLASH_POTION;
            }
    
            ItemStack potionStack =
                    new ItemStack(
                            potionItem
                    );
    
            potionStack.setTag(
                    queuedItem.getTag()
            );
    
            ThrownPotion potion =
                    new ThrownPotion(
                            level,
                            player
                    );
    
            potion.setPos(
                    x,
                    y,
                    z
            );
    
            potion.setItem(
                    potionStack
            );
    
            double speed =
                    2.5D;
    
            ((IChocolateDiscoProjectileAccess) potion)
                    .roundabout$setStandDamage(
                            session.highTeleport
                                    ? 4.0F
                                    : 2.0F
                    );
    
            ((IChocolateDiscoProjectileAccess) potion)
                    .roundabout$setStandDamagePlayersOnly(
                            true
                    );
    
            if (
                    session.highTeleport
            ) {
    
                potion.setDeltaMovement(
                        0.0D,
                        -speed,
                        0.0D
                );
    
            } else {
    
                Vec3 aimDirection =
                        getAimAssistDirection(
                                player,
                                level,
                                x,
                                y,
                                z
                        );
    
                potion.setDeltaMovement(
                        aimDirection.x * speed,
                        aimDirection.y * speed,
                        aimDirection.z * speed
                );
            }
    
            session.teleportedProjectiles.add(
                    potion
            );
    
            potion.hurtMarked =
                    true;
    
            level.addFreshEntity(
                    potion
            );
        }
    
        /*
         * ================================================================
         * SPAWN QUEUED POTION FOR REDIRECT
         * ================================================================
         */
    
        private static ThrownPotion spawnQueuedPotionForRedirect(
                ServerLevel level,
                ServerPlayer player,
                ItemStack queuedItem,
                double x,
                double y,
                double z
        ) {
    
            net.minecraft.world.item.Item potionItem =
                    queuedItem.getItem();
    
            if (
                    potionItem == net.minecraft.world.item.Items.POTION
            ) {
    
                potionItem =
                        net.minecraft.world.item.Items.SPLASH_POTION;
            }
    
            ItemStack potionStack =
                    new ItemStack(
                            potionItem
                    );
    
            potionStack.setTag(
                    queuedItem.getTag()
            );
    
            ThrownPotion potion =
                    new ThrownPotion(
                            level,
                            player
                    );
    
            potion.setPos(
                    x,
                    y,
                    z
            );
    
            potion.setItem(
                    potionStack
            );
    
            level.addFreshEntity(
                    potion
            );
    
            return potion;
        }
    
        /*
         * ================================================================
         * REGISTER SERVER
         * ================================================================
         */
    
        private static final class PacketReader {
            private final Object[] args;
            private int index;

            PacketReader(Object[] args) {
                this.args = args == null ? new Object[0] : args;
            }

            boolean readBoolean() { return (boolean) args[index++]; }
            int readInt() { return (int) args[index++]; }
            double readDouble() { return (double) args[index++]; }
            float readFloat() { return (float) args[index++]; }
        }

        public static void handleC2SPacket(String message, ServerPlayer player, Object... vargs) {
            if (player == null) {
                return;
            }

            MinecraftServer server = player.getServer();
            if (server == null) {
                return;
            }

            PacketReader reader = new PacketReader(vargs);

            if (message.equals(QUEUE_ITEM)) {
                server.execute(() -> {
                    
                                            UUID playerUUID =
                                                    player.getUUID();
                    
                    
                                            ItemStack mainHand =
                                                    player.getMainHandItem();
                    
                                            if (
                                                    mainHand.isEmpty()
                                                            && QUEUED_ITEMS.containsKey(
                                                            playerUUID
                                                    )
                                            ) {
                    
                                                ItemStack queuedItem =
                                                        QUEUED_ITEMS.get(
                                                                playerUUID
                                                        );
                    
                                                ItemStack returned =
                                                        queuedItem.copy();
                    
                                                boolean inventoryAccepted =
                                                        player.getInventory().add(
                                                                returned
                                                        );
                    
                                                if (
                                                        !returned.isEmpty()
                                                ) {
                    
                                                    player.drop(
                                                            returned,
                                                            false
                                                    );
                                                }
                    
                                                QUEUED_ITEMS.remove(
                                                        playerUUID
                                                );
                    
                                                BUILDING_PLACEMENT_CONFIRMED.remove(
                                                        playerUUID
                                                );
                    
                                                syncQueueState(player);
                    
                                                if (inventoryAccepted) {
                    
                                                    player.displayClientMessage(
                                                            Component.literal(
                                                                    "Queue emptied!"
                                                            ),
                                                            true
                                                    );
                    
                                                } else {
                    
                                                    player.displayClientMessage(
                                                            Component.literal(
                                                                    "Queue emptied! Some items were dropped."
                                                            ),
                                                            true
                                                    );
                                                }
                    
                                                return;
                                            }
                    
                                            if (
                                                    QUEUED_ITEMS.containsKey(
                                                            playerUUID
                                                    )
                                            ) {
                    
                                                ItemStack queuedItem =
                                                        QUEUED_ITEMS.get(
                                                                playerUUID
                                                        );
                    
                                                if (
                                                        ItemStack.isSameItemSameTags(
                                                                queuedItem,
                                                                mainHand
                                                        )
                                                ) {
                    
                                                    int queueLimit =
                                                            Boolean.TRUE.equals(
                                                                    BUILDING_MODE_STATES.get(playerUUID)
                                                            )
                                                                    ? BUILDING_MODE_QUEUE_MAX_ITEMS
                                                                    : 64;
                    
                                                    int queueSpace =
                                                            queueLimit - queuedItem.getCount();
                    
                                                    if (queueSpace <= 0) {
                    
                                                        player.displayClientMessage(
                                                                Component.literal(
                                                                        "Queue is full!"
                                                                ),
                                                                true
                                                        );
                    
                                                        return;
                                                    }
                    
                                                    int amountToAdd =
                                                            Math.min(
                                                                    queueSpace,
                                                                    mainHand.getCount()
                                                            );
                    
                                                    queuedItem.grow(
                                                            amountToAdd
                                                    );
                    
                                                    mainHand.shrink(
                                                            amountToAdd
                                                    );
                    
                                                    syncQueueState(
                                                            player
                                                    );
                    
                                                    player.displayClientMessage(
                                                            Component.literal(
                                                                    "Added "
                                                                            + amountToAdd
                                                                            + " item"
                                                                            + (amountToAdd == 1 ? "" : "s")
                                                                            + " to queue!"
                                                            ),
                                                            true
                                                    );
                    
                                                    return;
                                                }
                    
                                                player.displayClientMessage(
                                                        Component.literal(
                                                                "An item is already queued!"
                                                        ),
                                                        true
                                                );
                    
                                                return;
                                            }
                    
                                            if (
                                                    mainHand.isEmpty()
                                            ) {
                    
                                                player.displayClientMessage(
                                                        Component.literal(
                                                                "Nothing is queued!"
                                                        ),
                                                        true
                                                );
                    
                                                return;
                                            }
                    
                                            if (
                                                    Boolean.TRUE.equals(
                                                            BUILDING_MODE_STATES.get(
                                                                    playerUUID
                                                            )
                                                    )
                                                            && !(mainHand.getItem()
                                                            instanceof BlockItem)
                                            ) {
                    
                                                player.displayClientMessage(
                                                        Component.literal(
                                                                "Building Mode can only use blocks!"
                                                        ),
                                                        true
                                                );
                    
                                                return;
                                            }
                    
                                            if (
                                                    Boolean.TRUE.equals(
                                                            BUILDING_MODE_STATES.get(
                                                                    playerUUID
                                                            )
                                                    )
                                                            && BUILDING_MODE_BLACKLIST.contains(
                                                            Block.byItem(
                                                                    mainHand.getItem()
                                                            )
                                                    )
                                            ) {
                    
                                                player.displayClientMessage(
                                                        Component.literal(
                                                                "This block cannot be used in Building Mode!"
                                                        ),
                                                        true
                                                );
                    
                                                return;
                                            }
                    
                                            ItemStack queued =
                                                    mainHand.copy();
                    
                                            QUEUED_ITEMS.put(
                                                    playerUUID,
                                                    queued
                                            );
                    
                                            syncQueueState(player);
                    
                                            player.setItemInHand(
                                                    InteractionHand.MAIN_HAND,
                                                    ItemStack.EMPTY
                                            );
                    
                                            player.displayClientMessage(
                                                    Component.literal(
                                                            "Item queued!"
                                                    ),
                                                    true
                                            );
                                        });
                return;
            }

            if (message.equals(TELEPORT_PROJECTILES)) {
                int column =
                                                reader.readInt();
                    
                                        int row =
                                                reader.readInt();
                    
                                        boolean highTeleport =
                                                reader.readBoolean();
                    
                                        double transformX =
                                                reader.readDouble();
                    
                                        double transformY =
                                                reader.readDouble();
                    
                                        double transformZ =
                                                reader.readDouble();
                    
                                        float transformYaw =
                                                reader.readFloat();
                    
                                        server.execute(() -> {
                    
                                            UUID playerUUID =
                                                    player.getUUID();
                    
                                            /*
                                             * ========================================================
                                             * SHARED Z / SHIFT+Z COOLDOWN
                                             *
                                             * Both normal Z and Shift+Z use this same cooldown.
                                             * 40 ticks = 2 seconds.
                                             * ========================================================
                                             */
                    
                                            if (player instanceof StandUser standUser) {
                    
                                                StandPowers powers =
                                                        standUser.roundabout$getStandPowers();
                    
                                                if (powers != null) {
                    
                                                    if (
                                                            powers.onCooldown(
                                                                    PowerIndex.SKILL_1
                                                            )
                                                    ) {
                    
                                                        return;
                                                    }
                    
                                                    powers.setCooldown(
                                                            PowerIndex.SKILL_1,
                                                            40
                                                    );
                                                }
                                            }
                    
                    
                                            /*
                                             * ========================================================
                                             * BUILDING MODE
                                             * ========================================================
                                             */
                    
                                            if (
                                                    Boolean.TRUE.equals(
                                                            BUILDING_MODE_STATES.get(
                                                                    playerUUID
                                                            )
                                                    )
                                            ) {
                    
                                                placeBuildingBlocks(
                                                        player
                                                );
                    
                                                return;
                                            }
                    
                    
                                            /*
                                             * ========================================================
                                             * DETERMINE GRID SIZE
                                             * ========================================================
                                             */
                    
                                            int gridSize =
                                                    Boolean.TRUE.equals(
                                                            GRID_LOCK_STATES.get(
                                                                    playerUUID
                                                            )
                                                    )
                                                            ? ChocolateDiscoGrid.LARGE_GRID_SIZE
                                                            : ChocolateDiscoGrid.GRID_SIZE;
                    
                    
                                            /*
                                             * ========================================================
                                             * VALIDATE SELECTED TILE
                                             * ========================================================
                                             */
                    
                                            if (
                                                    column < 0
                                                            || column >= gridSize
                                                            || row < 0
                                                            || row >= gridSize
                                            ) {
                    
                                                return;
                                            }
                    
                    
                                            /*
                                             * ========================================================
                                             * CREATE DETECTION SESSION
                                             * ========================================================
                                             */
                    
                                            DetectionSession session =
                                                    new DetectionSession(
                                                            column,
                                                            row,
                                                            gridSize,
                                                            DETECTION_TICKS,
                                                            highTeleport,
                                                            transformX,
                                                            transformY,
                                                            transformZ,
                                                            transformYaw
                                                    );
                    
                                            ACTIVE_SESSIONS.put(
                                                    playerUUID,
                                                    session
                                            );
                    
                    
                                            disableShieldForAbility(
                                                    player,
                                                    DETECTION_TICKS
                                            );
                    
                    
                                            /*
                                             * ========================================================
                                             * HANDLE QUEUED ITEM
                                             * ========================================================
                                             */
                    
                                            ItemStack queuedItem =
                                                    QUEUED_ITEMS.get(
                                                            playerUUID
                                                    );
                    
                                            if (
                                                    queuedItem != null
                                                            && !queuedItem.isEmpty()
                                            ) {
                    
                                                /*
                                                 * ----------------------------------------------------
                                                 * SHIFT+Z — HIGH TELEPORT
                                                 * ----------------------------------------------------
                                                 */
                    
                                                if (
                                                        highTeleport
                                                ) {
                    
                                                    boolean spawned =
                                                            spawnQueuedItemForHighTeleport(
                                                                    player,
                                                                    session
                                                            );
                    
                                                    if (
                                                            spawned
                                                    ) {
                                                        // High teleport item handling complete.
                                                    }
                    
                                                }
                    
                                                /*
                                                 * ----------------------------------------------------
                                                 * NORMAL Z
                                                 * ----------------------------------------------------
                                                 */
                    
                                                else {
                    
                                                    ItemStack itemToThrow =
                                                            queuedItem.copy();
                    
                                                    itemToThrow.setCount(1);
                    
                                                    ServerLevel level =
                                                            player.serverLevel();
                    
                                                    Direction direction =
                                                            Direction.fromYRot(
                                                                    session.transformYaw
                                                            );
                    
                                                    int forwardX =
                                                            direction.getStepX();
                    
                                                    int forwardZ =
                                                            direction.getStepZ();
                    
                                                    int sideX =
                                                            -forwardZ;
                    
                                                    int sideZ =
                                                            forwardX;
                    
                                                    int center =
                                                            session.gridSize / 2;
                    
                                                    int columnOffset =
                                                            session.column - center;
                    
                                                    int forwardOffset =
                                                            3
                                                                    + (
                                                                    session.gridSize
                                                                            - 1
                                                                            - session.row
                                                            );
                    
                                                    double spawnX =
                                                            session.transformX
                                                                    + sideX * columnOffset
                                                                    + forwardX * forwardOffset;
                    
                                                    double spawnY =
                                                            session.transformY
                                                                    + 1.0D;
                    
                                                    double spawnZ =
                                                            session.transformZ
                                                                    + sideZ * columnOffset
                                                                    + forwardZ * forwardOffset;
                    
                    
                                                    /*
                                                     * ------------------------------------------------
                                                     * POTIONS
                                                     * ------------------------------------------------
                                                     */
                    
                                                    if (
                                                            itemToThrow.getItem()
                                                                    == net.minecraft.world.item.Items.POTION
                                                                    || itemToThrow.getItem()
                                                                    == net.minecraft.world.item.Items.SPLASH_POTION
                                                                    || itemToThrow.getItem()
                                                                    == net.minecraft.world.item.Items.LINGERING_POTION
                                                    ) {
                    
                                                        spawnQueuedPotion(
                                                                level,
                                                                player,
                                                                session,
                                                                queuedItem,
                                                                spawnX,
                                                                spawnY,
                                                                spawnZ
                                                        );
                    
                                                        queuedItem.shrink(1);
                    
                                                        if (
                                                                queuedItem.isEmpty()
                                                        ) {
                    
                                                            QUEUED_ITEMS.remove(
                                                                    playerUUID
                                                            );
                    
                                                            syncQueueState(
                                                                    player
                                                            );
                                                        }
                                                    }
                    
                    
                                                    /*
                                                     * ------------------------------------------------
                                                     * NORMAL THROWN ITEMS
                                                     * ------------------------------------------------
                                                     */
                    
                                                    else {
                    
                                                        Set<UUID> projectilesBeforeThrow =
                                                                new HashSet<>();
                    
                                                        for (
                                                                Projectile projectile :
                                                                level.getEntitiesOfClass(
                                                                        Projectile.class,
                                                                        player.getBoundingBox()
                                                                                .inflate(3.0D)
                                                                )
                                                        ) {
                    
                                                            if (
                                                                    projectile.isAlive()
                                                            ) {
                    
                                                                projectilesBeforeThrow.add(
                                                                        projectile.getUUID()
                                                                );
                                                            }
                                                        }
                    
                    
                                                        boolean throwSuccessful =
                                                                ThrownObjectEntity.throwAnObject(
                                                                        player,
                                                                        false,
                                                                        itemToThrow,
                    
                                                                        0.05F,
                                                                        0.05F,
                    
                                                                        0.5F,
                                                                        0.8F,
                                                                        -3F,
                    
                                                                        true,
                    
                                                                        ThrownObjectEntity.SPINTHROW,
                    
                                                                        90.0F,
                                                                        player.getYRot(),
                    
                                                                        new Vec3(
                                                                                spawnX,
                                                                                spawnY,
                                                                                spawnZ
                                                                        ),
                    
                                                                        false,
                    
                                                                        1F,
                    
                                                                        false
                                                                );
                    
                    
                                                        if (
                                                                throwSuccessful
                                                        ) {
                    
                                                            queuedItem.shrink(1);
                    
                                                            if (
                                                                    queuedItem.isEmpty()
                                                            ) {
                    
                                                                QUEUED_ITEMS.remove(
                                                                        playerUUID
                                                                );
                    
                                                                syncQueueState(
                                                                        player
                                                                );
                                                            }
                                                        }
                    
                    
                                                        /*
                                                         * ------------------------------------------------
                                                         * REGISTER NEWLY CREATED PROJECTILE
                                                         * ------------------------------------------------
                                                         */
                    
                                                        for (
                                                                Projectile projectile :
                                                                level.getEntitiesOfClass(
                                                                        Projectile.class,
                                                                        new AABB(
                                                                                spawnX - 2.0D,
                                                                                spawnY - 2.0D,
                                                                                spawnZ - 2.0D,
                                                                                spawnX + 2.0D,
                                                                                spawnY + 2.0D,
                                                                                spawnZ + 2.0D
                                                                        )
                                                                )
                                                        ) {
                    
                                                            if (
                                                                    !projectile.isAlive()
                                                            ) {
                                                                continue;
                                                            }
                    
                                                            if (
                                                                    !projectilesBeforeThrow.contains(
                                                                            projectile.getUUID()
                                                                    )
                                                            ) {
                    
                                                                teleportProjectile(
                                                                        player,
                                                                        session,
                                                                        projectile,
                                                                        true
                                                                );
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                return;
            }

            if (message.equals(GRID_LOCK_STATE)) {
                boolean locked =
                                                reader.readBoolean();
                    
                                        double transformX =
                                                reader.readDouble();
                    
                                        double transformY =
                                                reader.readDouble();
                    
                                        double transformZ =
                                                reader.readDouble();
                    
                                        float transformYaw =
                                                reader.readFloat();
                    
                                        server.execute(() -> {
                    
                                            UUID playerUUID =
                                                    player.getUUID();
                    
                                            GRID_LOCK_STATES.put(
                                                    playerUUID,
                                                    locked
                                            );
                    
                                            if (
                                                    locked
                                            ) {
                    
                                                LOCKED_GRID_TRANSFORMS.put(
                                                        playerUUID,
                                                        new LockedGridTransform(
                                                                transformX,
                                                                transformY,
                                                                transformZ,
                                                                transformYaw
                                                        )
                                                );
                    
                                            } else {
                    
                                                LOCKED_GRID_TRANSFORMS.remove(
                                                        playerUUID
                                                );
                    
                                                BUILDING_PLACEMENT_CONFIRMED.remove(
                                                        playerUUID
                                                );
                    
                                                BUILDING_SELECTED_CELLS.remove(
                                                        playerUUID
                                                );
                                            }
                                        });
                return;
            }

            if (message.equals(BUILDING_MODE_STATE)) {
                boolean buildingMode =
                                                reader.readBoolean();
                    
                                        server.execute(() -> {
                    
                                            UUID playerUUID =
                                                    player.getUUID();
                    
                                            BUILDING_MODE_STATES.put(
                                                    playerUUID,
                                                    buildingMode
                                            );
                    
                                            if (
                                                    buildingMode
                                            ) {
                    
                                                /*
                                                 * Entering Building Mode completely empties and returns
                                                 * any queue from normal mode.
                                                 */
                    
                                                ItemStack queuedItem =
                                                        QUEUED_ITEMS.remove(
                                                                playerUUID
                                                        );
                    
                                                if (
                                                        queuedItem != null
                                                                && !queuedItem.isEmpty()
                                                ) {
                    
                                                    ItemStack returned =
                                                            queuedItem.copy();
                    
                                                    player.getInventory()
                                                            .placeItemBackInInventory(
                                                                    returned
                                                            );
                    
                                                    if (
                                                            !returned.isEmpty()
                                                    ) {
                    
                                                        player.drop(
                                                                returned,
                                                                false
                                                        );
                                                    }
                                                }
                    
                                                BUILDING_PLACEMENT_CONFIRMED.remove(
                                                        playerUUID
                                                );
                    
                                                BUILDING_SELECTED_CELLS.remove(
                                                        playerUUID
                                                );
                    
                                                syncQueueState(
                                                        player
                                                );
                    
                                            } else {
                    
                                                /*
                                                 * Leaving Building Mode completely empties and returns
                                                 * any remaining Building Mode queue.
                                                 */
                    
                                                ItemStack queuedItem =
                                                        QUEUED_ITEMS.remove(
                                                                playerUUID
                                                        );
                    
                                                if (
                                                        queuedItem != null
                                                                && !queuedItem.isEmpty()
                                                ) {
                    
                                                    ItemStack returned =
                                                            queuedItem.copy();
                    
                                                    player.getInventory()
                                                            .placeItemBackInInventory(
                                                                    returned
                                                            );
                    
                                                    if (
                                                            !returned.isEmpty()
                                                    ) {
                    
                                                        player.drop(
                                                                returned,
                                                                false
                                                        );
                                                    }
                                                }
                    
                                                BUILDING_PLACEMENT_CONFIRMED.remove(
                                                        playerUUID
                                                );
                    
                                                BUILDING_SELECTED_CELLS.remove(
                                                        playerUUID
                                                );
                    
                                                syncQueueState(
                                                        player
                                                );
                                            }
                                        });
                return;
            }

            if (message.equals(BUILDING_SELECTION)) {
                int selectedCount =
                                                reader.readInt();
                    
                                        if (
                                                selectedCount < 0
                                                        || selectedCount > 225
                                        ) {
                    
                                            return;
                                        }
                    
                                        Set<Integer> selectedCells =
                                                new HashSet<>();
                    
                                        for (
                                                int i = 0;
                                                i < selectedCount;
                                                i++
                                        ) {
                    
                                            int column =
                                                    reader.readInt();
                    
                                            int row =
                                                    reader.readInt();
                    
                                            int cellKey =
                                                    row * 100 + column;
                    
                                            selectedCells.add(
                                                    cellKey
                                            );
                                        }
                    
                                        server.execute(() -> {
                    
                                            UUID playerUUID =
                                                    player.getUUID();
                    
                                            if (
                                                    !Boolean.TRUE.equals(
                                                            BUILDING_MODE_STATES.get(
                                                                    playerUUID
                                                            )
                                                    )
                                            ) {
                    
                                                return;
                                            }
                    
                                            BUILDING_PLACEMENT_CONFIRMED.remove(
                                                    playerUUID
                                            );
                    
                                            BUILDING_SELECTED_CELLS.put(
                                                    playerUUID,
                                                    selectedCells
                                            );
                    
                                            player.displayClientMessage(
                                                    Component.literal(
                                                            "Selected "
                                                                    + selectedCells.size()
                                                                    + " building tiles!"
                                                    ),
                                                    true
                                            );
                                        });
                return;
            }

            if (message.equals(REDIRECT_PROJECTILES)) {
                server.execute(() -> {
                    
                                            UUID playerUUID =
                                                    player.getUUID();
                    
                                            if (!(player instanceof StandUser standUser)) {
                                                return;
                                            }
                    
                                            StandPowers powers =
                                                    standUser.roundabout$getStandPowers();
                    
                                            if (powers == null) {
                                                return;
                                            }
                    
                                            if (
                                                    powers.onCooldown(
                                                            PowerIndex.SKILL_4_SNEAK
                                                    )
                                            ) {
                    
                                                player.displayClientMessage(
                                                        Component.literal(
                                                                "Chocolate Disco is on cooldown!"
                                                        ),
                                                        true
                                                );
                    
                                                return;
                                            }
                    
                                            RedirectSession session =
                                                    new RedirectSession(
                                                            40,
                                                            20
                                                    );
                    
                                            REDIRECT_SESSIONS.put(
                                                    playerUUID,
                                                    session
                                            );
                    
                                            disableShieldForAbility(
                                                    player,
                                                    60
                                            );
                    
                                            powers.setCooldown(
                                                    PowerIndex.SKILL_4_SNEAK,
                                                    200
                                            );
                                        });
                return;
            }

        }


    
        /*
         * ================================================================
         * FIND Z AIM ASSIST TARGET
         * ================================================================
         */
    
        private static LivingEntity findAimAssistTarget(
                ServerPlayer player,
                ServerLevel level,
                double targetX,
                double targetY,
                double targetZ
        ) {
    
            AABB assistArea =
                    new AABB(
                            targetX - AIM_ASSIST_RADIUS,
                            targetY - AIM_ASSIST_RADIUS,
                            targetZ - AIM_ASSIST_RADIUS,
                            targetX + AIM_ASSIST_RADIUS,
                            targetY + AIM_ASSIST_RADIUS,
                            targetZ + AIM_ASSIST_RADIUS
                    );
    
            ServerPlayer closestPlayer =
                    level.getEntitiesOfClass(
                                    ServerPlayer.class,
                                    assistArea,
                                    target ->
                                            !target.getUUID().equals(player.getUUID())
                                                    && target != player
                                                    && target.isAlive()
                                                    && !target.isSpectator()
                            )
                            .stream()
                            .min(
                                    java.util.Comparator.comparingDouble(
                                            target ->
                                                    target.distanceToSqr(
                                                            targetX,
                                                            targetY,
                                                            targetZ
                                                    )
                                    )
                            )
                            .orElse(null);
    
            if (closestPlayer != null) {
                return closestPlayer;
            }
    
            return level.getEntitiesOfClass(
                            Mob.class,
                            assistArea,
                            mob ->
                                    mob.isAlive()
                                            && !(mob instanceof FollowingStandEntity)
                    )
                    .stream()
                    .min(
                            java.util.Comparator.comparingDouble(
                                    mob ->
                                            mob.distanceToSqr(
                                                    targetX,
                                                    targetY,
                                                    targetZ
                                            )
                            )
                    )
                    .orElse(null);
        }
    
        /*
         * ================================================================
         * GET Z AIM ASSIST DIRECTION
         * ================================================================
         */
    
        private static Vec3 getAimAssistDirection(
                ServerPlayer player,
                ServerLevel level,
                double targetX,
                double targetY,
                double targetZ
        ) {
    
            LivingEntity target =
                    findAimAssistTarget(
                            player,
                            level,
                            targetX,
                            targetY,
                            targetZ
                    );
    
            if (target == null) {
    
                return new Vec3(
                        0.0D,
                        -1.0D,
                        0.0D
                );
            }
    
            double directionX =
                    target.getX() - targetX;
    
            double directionY =
                    (
                            target.getY()
                                    + target.getBbHeight() * 0.5D
                    ) - targetY;
    
            double directionZ =
                    target.getZ() - targetZ;
    
            double horizontalDistance =
                    Math.sqrt(
                            directionX * directionX
                                    + directionZ * directionZ
                    );
    
            if (horizontalDistance < 0.001D) {
    
                return new Vec3(
                        0.0D,
                        -1.0D,
                        0.0D
                );
            }
    
            double horizontalX =
                    directionX / horizontalDistance;
    
            double horizontalZ =
                    directionZ / horizontalDistance;
    
            double horizontalAmount =
                    Math.sin(
                            AIM_ASSIST_MAX_ANGLE
                    );
    
            double verticalAmount =
                    -Math.cos(
                            AIM_ASSIST_MAX_ANGLE
                    );
    
            return new Vec3(
                    horizontalX * horizontalAmount,
                    verticalAmount,
                    horizontalZ * horizontalAmount
            );
        }
    
        /*
         * ================================================================
         * CHECK Z AIM ASSIST BLACKLIST
         * ================================================================
         */
    
        private static boolean isZAimAssistBlacklisted(
                Projectile projectile
        ) {
    
            if (
                    !(projectile instanceof ThrowableItemProjectile throwableProjectile)
            ) {
    
                return false;
            }
    
            ItemStack projectileItem =
                    throwableProjectile.getItem();
    
            if (
                    projectileItem.isEmpty()
            ) {
    
                return false;
            }
    
            return projectileItem.getItem() == ModItems.GASOLINE_CAN
                    || projectileItem.getItem() == ModItems.MATCH
                    || projectileItem.getItem() == ModItems.MATCH_BUNDLE;
        }
    
        /*
         * ================================================================
         * TELEPORT PROJECTILE
         * ================================================================
         */
    
        private static void teleportProjectile(
                ServerPlayer player,
                DetectionSession session,
                Projectile projectile,
                boolean queuedProjectile
        ) {
    
            if (
                    session.teleportedProjectiles.contains(
                            projectile
                    )
            ) {
    
                return;
            }
    
            Direction direction =
                    Direction.fromYRot(
                            session.transformYaw
                    );
    
            int forwardX =
                    direction.getStepX();
    
            int forwardZ =
                    direction.getStepZ();
    
            int sideX =
                    -forwardZ;
    
            int sideZ =
                    forwardX;
    
            int center =
                    session.gridSize / 2;
    
            int columnOffset =
                    session.column - center;
    
            int forwardOffset =
                    3
                            + (
                            session.gridSize
                                    - 1
                                    - session.row
                    );
    
            double targetX =
                    session.transformX
                            + sideX * columnOffset
                            + forwardX * forwardOffset;
    
            double targetZ =
                    session.transformZ
                            + sideZ * columnOffset
                            + forwardZ * forwardOffset;
    
            double targetY =
                    session.transformY
                            + (
                            session.highTeleport
                                    ? 30.0D
                                    : 3.0D
                    );
    
            double speed =
                    projectile.getDeltaMovement().length()
                            + 2.5D;
    
            if (speed <= 0.0D) {
                speed = 1.0D;
            }
    
            projectile.teleportTo(
                    targetX,
                    targetY,
                    targetZ
            );
    
            boolean aimAssistBlacklisted =
                    isZAimAssistBlacklisted(
                            projectile
                    );
    
            ((IChocolateDiscoProjectileAccess) projectile)
                    .roundabout$setStandDamage(
                            session.highTeleport
                                    ? 4.0F
                                    : 2.0F
                    );
    
            ((IChocolateDiscoProjectileAccess) projectile)
                    .roundabout$setStandDamagePlayersOnly(
                            true
                    );
    
            if (
                    aimAssistBlacklisted
            ) {
    
                projectile.setDeltaMovement(
                        0.0D,
                        -speed,
                        0.0D
                );
    
            } else {
    
                Vec3 aimDirection =
                        getAimAssistDirection(
                                player,
                                player.serverLevel(),
                                targetX,
                                targetY,
                                targetZ
                        );
    
                projectile.setDeltaMovement(
                        aimDirection.x * speed,
                        aimDirection.y * speed,
                        aimDirection.z * speed
                );
            }
    
            session.teleportedProjectiles.add(
                    projectile
            );
    
            projectile.hurtMarked =
                    true;
        }
    
        /*
         * ================================================================
         * SPAWN QUEUED ITEM FOR HIGH TELEPORT
         * ================================================================
         */
    
        private static boolean spawnQueuedItemForHighTeleport(
                ServerPlayer player,
                DetectionSession session
        ) {
    
            UUID playerUUID =
                    player.getUUID();
    
            ItemStack queuedItem =
                    QUEUED_ITEMS.get(
                            playerUUID
                    );
    
            if (
                    queuedItem == null
                            || queuedItem.isEmpty()
            ) {
    
                return false;
            }
    
            ServerLevel level =
                    player.serverLevel();
    
            Direction direction =
                    Direction.fromYRot(
                            session.transformYaw
                    );
    
            int forwardX =
                    direction.getStepX();
    
            int forwardZ =
                    direction.getStepZ();
    
            int sideX =
                    -forwardZ;
    
            int sideZ =
                    forwardX;
    
            int center =
                    session.gridSize / 2;
    
            int columnOffset =
                    session.column - center;
    
            int forwardOffset =
                    3
                            + (
                            session.gridSize
                                    - 1
                                    - session.row
                    );
    
            double targetX =
                    session.transformX
                            + sideX * columnOffset
                            + forwardX * forwardOffset;
    
            double targetZ =
                    session.transformZ
                            + sideZ * columnOffset
                            + forwardZ * forwardOffset;
    
            double targetY =
                    session.transformY
                            + 30.0D;
    
            /*
             * TNT
             */
    
            if (
                    queuedItem.getItem() == net.minecraft.world.item.Items.TNT
            ) {
    
                PrimedTnt tnt =
                        new PrimedTnt(
                                EntityType.TNT,
                                level
                        );
    
                tnt.setPos(
                        targetX + 0.5D,
                        targetY,
                        targetZ + 0.5D
                );
    
                tnt.setFuse(
                        80
                );
    
                level.addFreshEntity(
                        tnt
                );
    
                queuedItem.shrink(1);
    
                if (
                        queuedItem.isEmpty()
                ) {
    
                    QUEUED_ITEMS.remove(
                            playerUUID
                    );
    
                    syncQueueState(
                            player
                    );
                }
    
                return true;
            }
    
            if (
                    queuedItem.getItem()
                            instanceof BlockItem blockItem
            ) {
    
                Block block =
                        blockItem.getBlock();
    
                BlockPos target =
                        BlockPos.containing(
                                targetX,
                                targetY,
                                targetZ
                        );
    
                var fallingState =
                        block.defaultBlockState();
    
                if (
                        block instanceof PointedDripstoneBlock
                ) {
    
                    fallingState =
                            fallingState.setValue(
                                    PointedDripstoneBlock.TIP_DIRECTION,
                                    Direction.DOWN
                            );
                }
    
                FallingBlockEntity fallingBlock =
                        FallingBlockEntity.fall(
                                level,
                                target,
                                fallingState
                        );
    
                if (block instanceof PointedDripstoneBlock) {
    
                    Vec3 velocity =
                            fallingBlock.getDeltaMovement();
    
                    fallingBlock.setDeltaMovement(
                            velocity.x,
                            velocity.y - 0.3D,
                            velocity.z
                    );
                }
    
                fallingBlock.setHurtsEntities(
                        2.0F,
                        40
                );
    
                level.addFreshEntity(
                        fallingBlock
                );
    
                queuedItem.shrink(1);
    
                if (
                        queuedItem.isEmpty()
                ) {
    
                    QUEUED_ITEMS.remove(
                            playerUUID
                    );
    
                    syncQueueState(
                            player
                    );
                }
    
                return true;
            }
    
            /*
             * Queued potions are handled directly and are already
             * registered with this detection session.
             */
    
            if (
                    queuedItem.getItem()
                            == net.minecraft.world.item.Items.POTION
                            || queuedItem.getItem()
                            == net.minecraft.world.item.Items.SPLASH_POTION
                            || queuedItem.getItem()
                            == net.minecraft.world.item.Items.LINGERING_POTION
            ) {
    
                spawnQueuedPotion(
                        level,
                        player,
                        session,
                        queuedItem,
                        targetX,
                        targetY,
                        targetZ
                );
    
                queuedItem.shrink(1);
    
                if (
                        queuedItem.isEmpty()
                ) {
    
                    QUEUED_ITEMS.remove(
                            playerUUID
                    );
    
                    syncQueueState(
                            player
                    );
                }
    
                return true;
            }
    
            /*
             * Normal thrown/projectile items.
             */
    
            Set<UUID> projectilesBeforeThrow =
                    new HashSet<>();
    
            for (
                    Projectile projectile :
                    level.getEntitiesOfClass(
                            Projectile.class,
                            player.getBoundingBox()
                                    .inflate(3.0D)
                    )
            ) {
    
                if (
                        projectile.isAlive()
                ) {
    
                    projectilesBeforeThrow.add(
                            projectile.getUUID()
                    );
                }
            }
    
            ItemStack itemToThrow =
                    queuedItem.copy();
    
            itemToThrow.setCount(1);
    
            double spawnX =
                    targetX;
    
            double spawnY =
                    targetY;
    
            double spawnZ =
                    targetZ;
    
            boolean throwSuccessful =
                    ThrownObjectEntity.throwAnObject(
                            player,
                            false,
                            itemToThrow,
    
                            0.05F,
                            0.05F,
    
                            0.5F,
                            0.8F,
                            -3F,
    
                            true,
    
                            ThrownObjectEntity.SPINTHROW,
    
                            90.0F,
                            player.getYRot(),
    
                            new Vec3(
                                    spawnX,
                                    spawnY,
                                    spawnZ
                            ),
    
                            false,
    
                            1F,
    
                            false
                    );
    
            if (
                    !throwSuccessful
            ) {
    
                return false;
            }
    
            queuedItem.shrink(1);
    
            if (
                    queuedItem.isEmpty()
            ) {
    
                QUEUED_ITEMS.remove(
                        playerUUID
                );
    
                syncQueueState(
                        player
                );
            }
    
            for (
                    Projectile projectile :
                    level.getEntitiesOfClass(
                            Projectile.class,
                            new AABB(
                                    targetX - 2.0D,
                                    targetY - 2.0D,
                                    targetZ - 2.0D,
                                    targetX + 2.0D,
                                    targetY + 2.0D,
                                    targetZ + 2.0D
                            )
                    )
            ) {
    
                if (
                        !projectile.isAlive()
                ) {
                    continue;
                }
    
                if (
                        !projectilesBeforeThrow.contains(
                                projectile.getUUID()
                        )
                ) {
    
                    teleportProjectile(
                            player,
                            session,
                            projectile,
                            true
                    );
                }
            }
    
            return true;
        }
    
        /*
         * ================================================================
         * REDIRECT PROJECTILE
         * ================================================================
         */
    
        private static void redirectProjectile(
                ServerPlayer player,
                RedirectSession session,
                Projectile projectile
        ) {
    
            if (
                    session.teleportedProjectiles.contains(
                            projectile
                    )
            ) {
    
                return;
            }
    
            double speed =
                    projectile.getDeltaMovement().length()
                            + 5.0D;
    
            double yawRadians =
                    Math.toRadians(
                            -player.getYRot()
                    );
    
            double forwardX =
                    Math.sin(
                            yawRadians
                    );
    
            double forwardZ =
                    Math.cos(
                            yawRadians
                    );
    
            double targetX =
                    player.getX()
                            + forwardX * 10.0D;
    
            double targetY =
                    player.getY()
                            + 3.0D;
    
            double targetZ =
                    player.getZ()
                            + forwardZ * 10.0D;
    
            projectile.teleportTo(
                    targetX,
                    targetY,
                    targetZ
            );
    
            double directionX =
                    player.getX()
                            - targetX;
    
            double directionY =
                    player.getY()
                            - targetY;
    
            double directionZ =
                    player.getZ()
                            - targetZ;
    
            double distance =
                    Math.sqrt(
                            directionX * directionX
                                    + directionY * directionY
                                    + directionZ * directionZ
                    );
    
            if (
                    distance > 0.0D
            ) {
    
                directionX /=
                        distance;
    
                directionY /=
                        distance;
    
                directionZ /=
                        distance;
            }
    
            projectile.setDeltaMovement(
                    directionX * speed,
                    directionY * speed,
                    directionZ * speed
            );
    
            session.teleportedProjectiles.add(
                    projectile
            );
    
            ((IChocolateDiscoProjectileAccess) projectile)
                    .roundabout$setStandDamage(
                            6.0F
                    );
    
            ((IChocolateDiscoProjectileAccess) projectile)
                    .roundabout$setStandDamagePlayersOnly(
                            false
                    );
    
            projectile.hurtMarked =
                    true;
        }
    
        /*
         * ================================================================
         * THROW QUEUED ITEM FOR REDIRECT
         * ================================================================
         */
    
        private static void throwQueuedItemForRedirect(
                ServerPlayer player,
                RedirectSession session
        ) {
    
            UUID playerUUID =
                    player.getUUID();
    
            ItemStack queuedItem =
                    QUEUED_ITEMS.get(
                            playerUUID
                    );
    
            if (
                    queuedItem == null
                            || queuedItem.isEmpty()
            ) {
    
                return;
            }
    
            ServerLevel level =
                    player.serverLevel();
    
            Set<UUID> projectilesBeforeThrow =
                    new HashSet<>();
    
            for (
                    Projectile projectile :
                    level.getEntitiesOfClass(
                            Projectile.class,
                            player.getBoundingBox()
                                    .inflate(3.0D)
                    )
            ) {
    
                if (
                        projectile.isAlive()
                ) {
    
                    projectilesBeforeThrow.add(
                            projectile.getUUID()
                    );
                }
            }
    
            ItemStack itemToThrow =
                    queuedItem.copy();
    
            itemToThrow.setCount(1);
    
            double spawnX =
                    player.getX();
    
            double spawnY =
                    player.getY() + 3.0D;
    
            double spawnZ =
                    player.getZ();
    
            boolean throwSuccessful;
    
            if (
                    itemToThrow.getItem()
                            == net.minecraft.world.item.Items.POTION
                            || itemToThrow.getItem()
                            == net.minecraft.world.item.Items.SPLASH_POTION
                            || itemToThrow.getItem()
                            == net.minecraft.world.item.Items.LINGERING_POTION
            ) {
    
                spawnQueuedPotionForRedirect(
                        level,
                        player,
                        queuedItem,
                        spawnX,
                        spawnY,
                        spawnZ
                );
    
                throwSuccessful =
                        true;
    
            } else {
    
                throwSuccessful =
                        ThrownObjectEntity.throwAnObject(
                                player,
                                false,
                                itemToThrow,
    
                                0.05F,
                                0.05F,
    
                                0.5F,
                                0.8F,
                                -3F,
    
                                true,
    
                                ThrownObjectEntity.SPINTHROW,
    
                                90.0F,
                                player.getYRot(),
    
                                new Vec3(
                                        spawnX,
                                        spawnY,
                                        spawnZ
                                ),
    
                                false,
    
                                1F,
    
                                false
                        );
            }
    
            if (
                    throwSuccessful
            ) {
    
                queuedItem.shrink(1);
    
                if (
                        queuedItem.isEmpty()
                ) {
    
                    QUEUED_ITEMS.remove(
                            playerUUID
                    );
    
                    syncQueueState(
                            player
                    );
                }
            }
    
            for (
                    Projectile projectile :
                    level.getEntitiesOfClass(
                            Projectile.class,
                            player.getBoundingBox()
                                    .inflate(5.0D)
                    )
            ) {
    
                if (
                        !projectile.isAlive()
                ) {
                    continue;
                }
    
                if (
                        !projectilesBeforeThrow.contains(
                                projectile.getUUID()
                        )
                ) {
    
                    redirectProjectile(
                            player,
                            session,
                            projectile
                    );
                }
            }
        }
    
        private static void disableShieldForAbility(
                ServerPlayer player,
                int ticks
        ) {
            player.stopUsingItem();
            player.getCooldowns().addCooldown(
                    net.minecraft.world.item.Items.SHIELD,
                    ticks
            );
        }
    
        public static void onChocolateDiscoStandSwitch(
                ServerPlayer player
        ) {
    
            UUID playerUUID =
                    player.getUUID();
    
            ItemStack queuedItem =
                    QUEUED_ITEMS.remove(
                            playerUUID
                    );
    
            BUILDING_PLACEMENT_CONFIRMED.remove(
                    playerUUID
            );
    
            BUILDING_SELECTED_CELLS.remove(
                    playerUUID
            );
    
            if (
                    queuedItem != null
                            && !queuedItem.isEmpty()
            ) {
    
                ItemStack returned =
                        queuedItem.copy();
    
                player.getInventory()
                        .placeItemBackInInventory(
                                returned
                        );
    
                if (
                        !returned.isEmpty()
                ) {
    
                    player.drop(
                            returned,
                            false
                    );
                }
            }
    
            syncQueueState(player);
        }
    
        public static void onPlayerDeath(ServerPlayer player) {
            UUID playerUUID = player.getUUID();

            ItemStack queuedItem = QUEUED_ITEMS.remove(playerUUID);
            if (queuedItem != null && !queuedItem.isEmpty()) {
                player.drop(queuedItem.copy(), true);
                syncQueueState(player);
            }

            BUILDING_SELECTED_CELLS.remove(playerUUID);
            BUILDING_PLACEMENT_CONFIRMED.remove(playerUUID);
            LOCKED_GRID_TRANSFORMS.remove(playerUUID);
            GRID_LOCK_STATES.remove(playerUUID);
            BUILDING_MODE_STATES.remove(playerUUID);
            REDIRECT_SESSIONS.remove(playerUUID);
            ACTIVE_SESSIONS.remove(playerUUID);

            ModMessageEvents.sendToPlayer(
                    player,
                    ServerToClientPackets.S2CPackets.MESSAGES.ChocolateDiscoBuildingModeReset.value
            );
        }

        /*
         * ================================================================
         * SERVER TICK
         * ================================================================
         */
    
        public static void tick(
                MinecraftServer server
        ) {
    
            /*
             * ============================================================
             * SHIFT+V REDIRECT
             * ============================================================
             */
    
            Iterator<Map.Entry<UUID, RedirectSession>>
                    redirectIterator =
                    REDIRECT_SESSIONS.entrySet().iterator();
    
            while (
                    redirectIterator.hasNext()
            ) {
    
                Map.Entry<UUID, RedirectSession> entry =
                        redirectIterator.next();
    
                UUID playerUUID =
                        entry.getKey();
    
                RedirectSession session =
                        entry.getValue();
    
                ServerPlayer player =
                        server.getPlayerList()
                                .getPlayer(
                                        playerUUID
                                );
    
                if (
                        player == null
                                || !player.isAlive()
                ) {
    
                    redirectIterator.remove();
    
                    continue;
                }
    
                /*
                 * STARTUP
                 */
    
                if (
                        session.startupTicksRemaining > 0
                ) {
    
                    session.startupTicksRemaining--;
    
                    if (
                            session.startupTicksRemaining == 0
                                    && !session.queuedItemThrown
                    ) {
    
                        throwQueuedItemForRedirect(
                                player,
                                session
                        );
    
                        session.queuedItemThrown =
                                true;
                    }
    
                    continue;
                }
    
                /*
                 * ACTIVE REDIRECT
                 */
    
                if (
                        session.activeTicksRemaining > 0
                ) {
    
                    ServerLevel level =
                            player.serverLevel();
    
                    AABB detectionArea =
                            getChocolateDiscoPlayerDetectionArea(
                                    player
                            );
    
                    for (
                            Projectile projectile :
                            level.getEntitiesOfClass(
                                    Projectile.class,
                                    detectionArea
                            )
                    ) {
    
                        if (
                                !projectile.isAlive()
                        ) {
                            continue;
                        }
    
                        redirectProjectile(
                                player,
                                session,
                                projectile
                        );
                    }
    
                    session.activeTicksRemaining--;
    
                    continue;
                }
    
                /*
                 * FINISHED
                 */
    
                redirectIterator.remove();
            }
    
            /*
             * ============================================================
             * NORMAL Z PROJECTILE DETECTION
             * ============================================================
             */
    
            Iterator<Map.Entry<UUID, DetectionSession>>
                    sessionIterator =
                    ACTIVE_SESSIONS.entrySet().iterator();
    
            while (
                    sessionIterator.hasNext()
            ) {
    
                Map.Entry<UUID, DetectionSession> entry =
                        sessionIterator.next();
    
                UUID playerUUID =
                        entry.getKey();
    
                DetectionSession session =
                        entry.getValue();
    
                ServerPlayer player =
                        server.getPlayerList()
                                .getPlayer(
                                        playerUUID
                                );
    
                if (
                        player == null
                                || !player.isAlive()
                ) {
    
                    sessionIterator.remove();
    
                    continue;
                }
    
                ServerLevel level =
                        player.serverLevel();
    
                /*
                 * Player detection.
                 */
    
                AABB playerDetectionArea =
                        getChocolateDiscoPlayerDetectionArea(
                                player
                        );
    
                for (
                        Projectile projectile :
                        level.getEntitiesOfClass(
                                Projectile.class,
                                playerDetectionArea
                        )
                ) {
    
                    if (
                            !projectile.isAlive()
                    ) {
                        continue;
                    }
    
                    teleportProjectile(
                            player,
                            session,
                            projectile,
                            false
                    );
                }
    
                /*
                 * Grid detection.
                 */
    
                if (
                        !REDIRECT_SESSIONS.containsKey(
                                playerUUID
                        )
                                && !Boolean.TRUE.equals(
                                GRID_LOCK_STATES.get(
                                        playerUUID
                                )
                        )
                ) {
    
                    AABB gridDetectionArea =
                            getChocolateDiscoGridDetectionArea(
                                    player
                            );
    
                    for (
                            Projectile projectile :
                            level.getEntitiesOfClass(
                                    Projectile.class,
                                    gridDetectionArea
                            )
                    ) {
    
                        if (
                                !projectile.isAlive()
                        ) {
                            continue;
                        }
    
                        teleportProjectile(
                                player,
                                session,
                                projectile,
                                false
                        );
                    }
                }
    
                session.ticksRemaining--;
    
                if (
                        session.ticksRemaining <= 0
                ) {
    
                    sessionIterator.remove();
                }
            }
        }
    }
