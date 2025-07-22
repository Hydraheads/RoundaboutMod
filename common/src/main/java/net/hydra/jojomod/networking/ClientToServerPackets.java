package net.hydra.jojomod.networking;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.index.Corpses;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.networking.message.impl.IMessageEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ClientToServerPackets {
    public static class StandPowerPackets implements IMessageEvent {
        public enum MESSAGES {
            TryPower("try_power"),
            TryPosPower("try_pos_power"),
            TryBlockPosPower("try_block_pos_power"),
            TryHitResultPosPower("try_hit_result_pos_power"),
            TryIntPower("try_int_power"),
            TryTripleIntPower("try_triple_int_power"),
            BodyBag("body_bag");

            public final String value;

            MESSAGES(String value)
            {
                this.value = value;
            }
        }

        public StandUser basicChecks(ServerPlayer sender){
            if (sender == null)
                throw new RuntimeException("\"sender\" was null on a C2S packet!");
            return ((StandUser)sender);
        }
        @Override
        public void INVOKE(String message, @Nullable ServerPlayer sender, Object... vargs) {
            /**Try Power Packet*/
            if (sender != null) {
                MinecraftServer server = sender.server;

                if (message.equals(MESSAGES.TryPower.value)) {
                    server.execute(() -> {
                        StandUser powers = basicChecks(sender);
                        byte b = (byte) vargs[0];
                        powers.roundabout$tryPower(b, true);
                    });
                }
                /**Try Power Packet*/
                if (message.equals(MESSAGES.TryPosPower.value)) {
                    server.execute(() -> {
                        StandUser powers = basicChecks(sender);
                        byte b = (byte) vargs[0];
                        Vector3f c = (Vector3f) vargs[1];
                        powers.roundabout$tryPosPower(b, true, new Vec3(c.x, c.y, c.z));
                    });
                }
                /**Try Block Pos Power Packet*/
                if (message.equals(MESSAGES.TryBlockPosPower.value)) {
                    server.execute(() -> {
                        StandUser powers = basicChecks(sender);
                        byte b = (byte) vargs[0];
                        BlockPos c = (BlockPos) vargs[1];
                        powers.roundabout$tryBlockPosPower(b, true, c);
                    });
                }
                /**Try Block Pos Power Packet*/
                if (message.equals(MESSAGES.TryHitResultPosPower.value)) {
                    server.execute(() -> {
                        StandUser powers = basicChecks(sender);
                        byte b = (byte) vargs[0];
                        BlockPos c = (BlockPos) vargs[1];
                        BlockHitResult d = (BlockHitResult) vargs[2];
                        powers.roundabout$tryBlockPosPower(b, true, c, d);
                    });
                }
                /**Try Power Packet*/
                if (message.equals(MESSAGES.TryIntPower.value)) {
                    server.execute(() -> {
                        StandUser powers = basicChecks(sender);
                        byte b = (byte) vargs[0];
                        int c = (int) vargs[1];
                        powers.roundabout$tryIntPower(b, true, c);
                    });
                }
                /**Try Triple Int Power Packet*/
                if (message.equals(MESSAGES.TryTripleIntPower.value)) {
                    server.execute(() -> {
                        StandUser powers = basicChecks(sender);
                        byte b = (byte) vargs[0];
                        int c = (int) vargs[1];
                        int d = (int) vargs[2];
                        int e = (int) vargs[3];
                        powers.roundabout$tryIntPower(b, true, c, d, e);
                    });
                }
                /**Justice Body Bag Usage Packet*/
                if (message.equals(MESSAGES.BodyBag.value)) {
                    server.execute(() -> {
                        ServerLevel world = (ServerLevel) sender.level();
                        byte context = (byte) vargs[0];
                        ItemStack stack = (ItemStack) vargs[1];
                        Vector3f vec = (Vector3f) vargs[2];
                        boolean offh = ItemStack.isSameItemSameTags(sender.getOffhandItem(),stack);
                        if (sender.getInventory().contains(stack) || offh || stack.is(ModItems.CREATIVE_BODY_BAG)) {
                            ItemStack item;
                            int zombies = 1;
                            int skeletons = 1;
                            int spiders = 1;
                            int villagers = 1;
                            int creepers = 1;
                            int phantoms = 1;
                            if (!stack.is(ModItems.CREATIVE_BODY_BAG)){
                                if (offh) {
                                    item = sender.getOffhandItem();
                                } else {
                                    item = sender.getInventory().getItem((sender.getInventory().findSlotMatchingItem(stack)));
                                }
                                CompoundTag $$1 = item.getOrCreateTagElement("bodies");
                                zombies = $$1.getInt("zombie");
                                skeletons = $$1.getInt("skeleton");
                                spiders = $$1.getInt("spider");
                                villagers = $$1.getInt("villager");
                                creepers = $$1.getInt("creeper");
                                phantoms = $$1.getInt("phantom");
                            }
                            FallenMob fm = null;
                            int yElevation = 0;
                            if (context == Corpses.ZOMBIE.id) {
                                zombies--;
                                if (zombies >= 0) {
                                    fm = ModEntities.FALLEN_ZOMBIE.create(sender.level());
                                }
                            } else if (context == Corpses.SKELETON.id) {
                                skeletons--;
                                if (skeletons >= 0) {
                                    fm = ModEntities.FALLEN_SKELETON.create(sender.level());
                                }
                            } else if (context == Corpses.SPIDER.id) {
                                spiders--;
                                if (spiders >= 0) {
                                    fm = ModEntities.FALLEN_SPIDER.create(sender.level());
                                }
                            } else if (context == Corpses.VILLAGER.id) {
                                villagers--;
                                if (villagers >= 0) {
                                    fm = ModEntities.FALLEN_VILLAGER.create(sender.level());
                                }
                            } else if (context == Corpses.CREEPER.id) {
                                creepers--;
                                if (creepers >= 0) {
                                    fm = ModEntities.FALLEN_CREEPER.create(sender.level());
                                }
                            } else if (context == Corpses.PHANTOM.id) {
                                phantoms--;
                                if (phantoms >= 0) {
                                    fm = ModEntities.FALLEN_PHANTOM.create(sender.level());
                                }
                            }

                            if (fm != null) {
                                fm.setPos(vec.x, vec.y + yElevation, vec.z);
                                fm.placer = sender;
                                fm.setPhasesFull(true);
                                fm.tickThroughPlacerStart();
                                fm.setForcedRotation(sender.getYRot() % 360);
                                fm.setYRot(sender.getYRot() % 360);
                                fm.setYBodyRot(sender.yBodyRot % 360);
                                sender.level().addFreshEntity(fm);
                                if (!stack.is(ModItems.CREATIVE_BODY_BAG)) {
                                    if (offh){
                                        item =sender.getOffhandItem();
                                    } else {
                                        item = sender.getInventory().getItem((sender.getInventory().findSlotMatchingItem(stack)));
                                    }
                                    if (!sender.isCreative()) {
                                        if (context == Corpses.ZOMBIE.id) {
                                            item.getOrCreateTagElement("bodies").putInt("zombie", zombies);
                                        } else if (context == Corpses.SKELETON.id) {
                                            item.getOrCreateTagElement("bodies").putInt("skeleton", skeletons);
                                        } else if (context == Corpses.SPIDER.id) {
                                            item.getOrCreateTagElement("bodies").putInt("spider", spiders);
                                        } else if (context == Corpses.VILLAGER.id) {
                                            item.getOrCreateTagElement("bodies").putInt("villager", villagers);
                                        } else if (context == Corpses.CREEPER.id) {
                                            item.getOrCreateTagElement("bodies").putInt("creeper", creepers);
                                        } else if (context == Corpses.PHANTOM.id) {
                                            item.getOrCreateTagElement("bodies").putInt("phantom", phantoms);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}
