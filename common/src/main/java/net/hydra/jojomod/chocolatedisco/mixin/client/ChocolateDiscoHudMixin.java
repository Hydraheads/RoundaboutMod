package net.hydra.jojomod.chocolatedisco.mixin.client;

import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridTransform;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoQueueState;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelectionState;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.hydra.jojomod.chocolatedisco.PowersChocolateDisco;

@Mixin(AbilityScapeBasis.class)
public class ChocolateDiscoHudMixin {

    private static final ResourceLocation CHOCOLATE_DISCO_TELEPORT =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_teleport.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_HIGH_TELEPORT =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_highteleport.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_SELECT =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_select.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_BUILD =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_build.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_BUILDOFF =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_buildoff.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_GRID_LOCK =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_gridlock.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_GRID_UNLOCK =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_gridunlock.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_QUEUE =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_queue.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_UNQUEUE =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_unqueue.png"
            );

    private static final ResourceLocation CHOCOLATE_DISCO_SELF_DISCO =
            new ResourceLocation(
                    "roundabout",
                    "textures/gui/icons/chocolate_disco_selfdisco.png"
            );

    @Inject(
            method = "renderIcons",
            at = @At("HEAD"),
            remap = false
    )
    private void chocolateDisco$renderIcons(
            GuiGraphics context,
            int x,
            int y,
            CallbackInfo ci
    ) {

        if (!((Object) this instanceof PowersChocolateDisco powers)) {
            return;
        }

        boolean holdingSneak =
                powers.isHoldingSneak();


        /*
         * ============================================================
         * SHIFT
         *
         * 1 = High Teleport
         * 2 = Building Mode
         * 3 = Grid Lock / Unlock
         * 4 = Self Disco
         * ============================================================
         */

        if (holdingSneak) {

            /*
             * Shift+Z uses the SAME cooldown as normal Z.
             *
             * Both are tied to PowerIndex.SKILL_1.
             */
            powers.setSkillIcon(
                    context,
                    x,
                    y,
                    1,
                    CHOCOLATE_DISCO_HIGH_TELEPORT,
                    PowerIndex.SKILL_1
            );


            powers.setSkillIcon(
                    context,
                    x,
                    y,
                    2,
                    ChocolateDiscoSelectionState.isBuildingMode()
                            ? CHOCOLATE_DISCO_BUILDOFF
                            : CHOCOLATE_DISCO_BUILD,
                    PowerIndex.NO_CD
            );


            if (
                    powers.getSelf() instanceof Player player
                            && ChocolateDiscoGridTransform.isLocked(player)
            ) {

                powers.setSkillIcon(
                        context,
                        x,
                        y,
                        3,
                        CHOCOLATE_DISCO_GRID_UNLOCK,
                        PowerIndex.NO_CD
                );

            } else {

                powers.setSkillIcon(
                        context,
                        x,
                        y,
                        3,
                        CHOCOLATE_DISCO_GRID_LOCK,
                        PowerIndex.NO_CD
                );
            }


            powers.setSkillIcon(
                    context,
                    x,
                    y,
                    4,
                    CHOCOLATE_DISCO_SELF_DISCO,
                    PowerIndex.SKILL_4_SNEAK
            );

            return;
        }


        /*
         * ============================================================
         * NORMAL
         *
         * 1 = Teleport
         * 2 = Select
         * 3 = Dash
         * 4 = Queue / Unqueue
         * ============================================================
         */

        /*
         * Normal Z uses the SAME cooldown as Shift+Z.
         */
        powers.setSkillIcon(
                context,
                x,
                y,
                1,
                CHOCOLATE_DISCO_TELEPORT,
                PowerIndex.SKILL_1
        );


        powers.setSkillIcon(
                context,
                x,
                y,
                2,
                CHOCOLATE_DISCO_SELECT,
                PowerIndex.NO_CD
        );


        powers.setSkillIcon(
                context,
                x,
                y,
                3,
                StandIcons.DODGE,
                PowerIndex.GLOBAL_DASH
        );


        /*
         * ============================================================
         * QUEUE / UNQUEUE
         * ============================================================
         */

        ItemStack mainHand =
                powers.getSelf() instanceof Player player
                        ? player.getMainHandItem()
                        : ItemStack.EMPTY;

        ResourceLocation queueIcon;


        if (!ChocolateDiscoQueueState.isQueued()) {

            queueIcon =
                    CHOCOLATE_DISCO_QUEUE;

        } else if (mainHand.isEmpty()) {

            queueIcon =
                    CHOCOLATE_DISCO_UNQUEUE;

        } else if (
                ItemStack.isSameItemSameTags(
                        ChocolateDiscoQueueState.getQueuedItem(),
                        mainHand
                )
        ) {

            queueIcon =
                    CHOCOLATE_DISCO_QUEUE;

        } else {

            queueIcon =
                    CHOCOLATE_DISCO_UNQUEUE;
        }


        powers.setSkillIcon(
                context,
                x,
                y,
                4,
                queueIcon,
                PowerIndex.NO_CD
        );
    }


    @Inject(
            method = "isAttackIneptVisually",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void chocolateDisco$queueAvailability(
            byte activeP,
            int slot,
            CallbackInfoReturnable<Boolean> cir
    ) {

        if (!((Object) this instanceof PowersChocolateDisco powers)) {
            return;
        }


        /*
         * Slot 4 while sneaking is Self Disco.
         * Roundabout handles its normal cooldown.
         */
        if (
                slot != 4
                        || powers.isHoldingSneak()
        ) {
            return;
        }


        if (!(powers.getSelf() instanceof Player player)) {
            return;
        }


        boolean buildingMode =
                ChocolateDiscoSelectionState.isBuildingMode();

        ItemStack mainHand =
                player.getMainHandItem();

        boolean queued =
                ChocolateDiscoQueueState.isQueued();


        /*
         * ============================================================
         * NO QUEUE
         * ============================================================
         */

        if (!queued) {

            if (mainHand.isEmpty()) {

                cir.setReturnValue(true);
                return;
            }


            /*
             * Building Mode can only queue blocks.
             * TNT is not allowed.
             */
            if (buildingMode) {

                if (!(mainHand.getItem() instanceof BlockItem)) {

                    cir.setReturnValue(true);
                    return;
                }


                if (
                        Block.byItem(
                                mainHand.getItem()
                        ) == Blocks.TNT
                ) {

                    cir.setReturnValue(true);
                }

                return;
            }


            /*
             * Any non-empty item can be queued normally.
             */
            return;
        }


        /*
         * ============================================================
         * QUEUE EXISTS
         * ============================================================
         */

        ItemStack queuedItem =
                ChocolateDiscoQueueState.getQueuedItem();


        /*
         * Empty hand = V can return the queue.
         */
        if (mainHand.isEmpty()) {
            return;
        }


        /*
         * Different item = V cannot add it.
         */
        if (
                !ItemStack.isSameItemSameTags(
                        queuedItem,
                        mainHand
                )
        ) {

            cir.setReturnValue(true);
            return;
        }


        /*
         * Same item, but queue is full.
         */
        if (
                ChocolateDiscoQueueState.getQueuedCount()
                        >= 64
        ) {

            cir.setReturnValue(true);
        }
    }
}
