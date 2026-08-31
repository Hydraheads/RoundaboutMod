package net.hydra.jojomod.mixin.whitesnake.inventory;

import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.hydra.jojomod.access.WhitesnakeInventoryAccess;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class WhitesnakeDropItemMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$dropSelected(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        ServerboundPlayerActionPacket.Action action = packet.getAction();
        if (!WhitesnakeControlInventory.isActive(player)
                || (action != ServerboundPlayerActionPacket.Action.DROP_ITEM
                && action != ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS)) return;
        int slot = player.getInventory().selected;
        ItemStack selected = WhitesnakeControlInventory.get(player).get(slot);
        if (!selected.isEmpty()
                && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers) {
            LivingEntity stand = powers.getPilotingStand();
            if (stand != null) {
                int count = action == ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS ? selected.getCount() : 1;
                ItemStack dropped = selected.split(count);
                if (selected.isEmpty()) WhitesnakeControlInventory.get(player).set(slot, ItemStack.EMPTY);
                ItemEntity item = new ItemEntity(player.level(), stand.getX(), stand.getEyeY() - 0.25D,
                        stand.getZ(), dropped);
                item.setDeltaMovement(stand.getLookAngle().scale(0.3D).add(0.0D, 0.1D, 0.0D));
                item.setPickUpDelay(40);
                item.setThrower(player.getUUID());
                player.level().addFreshEntity(item);
                ((WhitesnakeInventoryAccess) player).roundaboutWhitesnake$syncInventory();
            }
        }
        ci.cancel();
    }
}
