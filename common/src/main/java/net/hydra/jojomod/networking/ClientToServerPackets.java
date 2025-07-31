package net.hydra.jojomod.networking;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.index.Corpses;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
            IntToServer("int_to_server"),
            ByteToServer("byte_to_server"),
            SingleByteToServer("single_byte_to_server"),
            TryTripleIntPower("try_triple_int_power"),
            BodyBag("body_bag"),
            ModVisageConfigure("mod_visage"),
            TimeStopHovering("time_stop_hovering"),
            GlaiveHit("glaive_hit"),
            StandSummon("stand_summon"),
            UpdatePilot("update_pilot"),
            MoveSync("moving_sync"),
            StandPunch("stand_punch"),
            StandBarrageHit("stand_barrage_hit"),
            BarrageClashUpdate("barrage_clash_update"),
            Handshake("handshake");

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
                /**Generic int to server packet*/
                if (message.equals(MESSAGES.IntToServer.value)) {
                    server.execute(() -> {
                        basicChecks(sender);
                        byte b = (byte) vargs[0];
                        int c = (int) vargs[1];
                        MainUtil.handleIntPacketC2S(sender,c,b);
                    });
                }
                /**Generic byte to server packet*/
                if (message.equals(MESSAGES.ByteToServer.value)) {
                    server.execute(() -> {
                        basicChecks(sender);
                        byte b = (byte) vargs[0];
                        byte c = (byte) vargs[1];
                        MainUtil.handleBytePacketC2S(sender, c, b);
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

                /**Modification visage saving after client configuration*/
                if (message.equals(MESSAGES.ModVisageConfigure.value)) {
                    server.execute(() -> {
                        byte chest = (byte) vargs[0];
                        ItemStack stack = (ItemStack) vargs[1];
                        Vector3f vec = (Vector3f) vargs[2];

                        boolean offh = ItemStack.isSameItemSameTags(sender.getOffhandItem(),stack);
                        if (stack.getItem() instanceof ModificationMaskItem || sender.getInventory().contains(stack) || offh) {
                            ItemStack item;
                            if (offh) {
                                item = sender.getOffhandItem();
                            } else {
                                int yes = sender.getInventory().findSlotMatchingItem(stack);
                                item = sender.getInventory().getItem(yes);
                            }
                            item.getOrCreateTagElement("modifications").putInt("height", (int) vec.x);
                            item.getOrCreateTagElement("modifications").putInt("width", (int) vec.y);
                            item.getOrCreateTagElement("modifications").putInt("head", (int) vec.z);
                            item.getOrCreateTagElement("modifications").putInt("chest", chest);
                        }
                    });
                }


                /**A single byte message to the server*/
                if (message.equals(MESSAGES.SingleByteToServer.value)) {
                    server.execute(() -> {
                        byte context = (byte) vargs[0];
                        MainUtil.handleSingleBytePacketC2S(sender, context);
                    });
                }

                /**Allows you to hover ins topped time*/
                if (message.equals(MESSAGES.TimeStopHovering.value)) {
                    server.execute(() -> {
                        boolean tsJump = (boolean) vargs[0];
                        ServerLevel level = (ServerLevel) sender.level();
                        ((StandUser) sender).roundabout$setTSJump(tsJump);
                    });
                }
                /**The glaive you use and the entity you hit with it*/
                if (message.equals(MESSAGES.GlaiveHit.value)) {
                    server.execute(() -> {
                        int target = (int) vargs[0];
                        ItemStack glaive = (ItemStack) vargs[1];
                        ServerLevel world = (ServerLevel) sender.level();

                        Entity entity = world.getEntity(target);
                        if (glaive.getItem() instanceof GlaiveItem) {
                            ((GlaiveItem)glaive.getItem()).glaiveAttack(glaive,world,sender,entity);
                        }
                    });
                }
                /**Summoning stands*/
                if (message.equals(MESSAGES.StandSummon.value)) {
                    ServerLevel world = (ServerLevel) sender.level();
                    ((StandUser) sender).roundabout$summonStand(world, false, true);
                }
                /**Update Piloting Stand*/
                if (message.equals(MESSAGES.UpdatePilot.value)) {
                    float x = (float)vargs[0];
                    float y = (float)vargs[1];
                    float z = (float)vargs[2];
                    float xrot = (float)vargs[3];
                    float zrot = (float)vargs[4];
                    int ent = (int)vargs[5];
                    MainUtil.handleMovePilot(x,y,z,xrot,zrot,sender,ent);
                }
                /**Sync movement for stand leaning animation as you walk*/
                if (message.equals(MESSAGES.MoveSync.value)) {
                    byte forward = (byte)vargs[0];
                    byte strafe = (byte)vargs[1];
                    ((StandUser) sender).roundabout$setDI(forward, strafe);
                }
                /**Basic stand punch packet*/
                if (message.equals(MESSAGES.StandPunch.value)) {
                    int targetID = (int)vargs[0];
                    byte APP = (byte)vargs[1];
                    Entity TE = sender.level().getEntity(targetID);
                    ((StandUser) sender).roundabout$getStandPowers().setActivePowerPhase(APP);
                    ((StandUser) sender).roundabout$getStandPowers().punchImpact(TE);
                }
                /**Basic stand barrage hit packet*/
                if (message.equals(MESSAGES.StandBarrageHit.value)) {
                    int targetID = (int)vargs[0];
                    int hitNumber = (int)vargs[1];
                    Entity TE = sender.level().getEntity(targetID);
                    ((StandUser) sender).roundabout$getStandPowers().barrageImpact(TE, hitNumber);
                }
                /**Barrage Clash packet*/
                if (message.equals(MESSAGES.BarrageClashUpdate.value)) {
                    float clashProg = (float)vargs[0];
                    boolean clashDone = (boolean)vargs[1];

                    if (((StandUser) sender).roundabout$isClashing()){
                        ((StandUser) sender).roundabout$getStandPowers().setClashProgress(clashProg);
                        ((StandUser) sender).roundabout$getStandPowers().setClashDone(clashDone);
                    }
                }
                /**Handshake packet*/
                if (message.equals(MESSAGES.Handshake.value)) {
                    MainUtil.handShake(sender);
                }
            }
        }
    }
}
