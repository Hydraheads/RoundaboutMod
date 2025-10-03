package net.hydra.jojomod.entity.paintings;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class RoundaboutPainting extends HangingEntity {

    public RoundaboutPainting(EntityType<? extends RoundaboutPainting> $$0, Level $$1) {
        super($$0, $$1);
    }

    public RoundaboutPainting(EntityType<? extends RoundaboutPainting> $$0,Level $$1, BlockPos $$2) {
        super($$0, $$1, $$2);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putByte("facing", (byte)this.direction.get2DDataValue());
        super.addAdditionalSaveData($$0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        this.direction = Direction.from2DDataValue($$0.getByte("facing"));
        super.readAdditionalSaveData($$0);
        this.setDirection(this.direction);
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public void dropItem(@Nullable Entity $$0) {
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
            if ($$0 instanceof Player $$1 && $$1.getAbilities().instabuild) {
                return;
            }

            this.spawnAtLocation(getPaintingItem());
        }
    }
    public Item getPaintingItem(){
        return Items.PAINTING;
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    public void moveTo(double $$0, double $$1, double $$2, float $$3, float $$4) {
        this.setPos($$0, $$1, $$2);
    }

    @Override
    public void lerpTo(double $$0, double $$1, double $$2, float $$3, float $$4, int $$5, boolean $$6) {
        this.setPos($$0, $$1, $$2);
    }

    @Override
    public Vec3 trackingPosition() {
        return Vec3.atLowerCornerOf(this.pos);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket $$0) {
        super.recreateFromPacket($$0);
        this.setDirection(Direction.from3DDataValue($$0.getData()));
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getPaintingItem());
    }
}
