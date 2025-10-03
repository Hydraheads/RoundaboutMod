package net.hydra.jojomod.entity.paintings;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.Optional;

public class VanGoughPainting extends RoundaboutPainting {


    public VanGoughPainting(EntityType<? extends VanGoughPainting> $$0, Level $$1) {
        super($$0, $$1);
    }

    private VanGoughPainting(Level $$0, BlockPos $$1, Direction $$2) {
        super(getPaintingType(),$$0, $$1);
        this.setDirection($$2);
    }



    public static EntityType<? extends RoundaboutPainting> getPaintingType(){
        return ModEntities.VAN_GOUGH_PAINTING;
    }

    public static Optional<RoundaboutPainting> create(Level $$0, BlockPos $$1, Direction $$2) {
        RoundaboutPainting $$3 = new VanGoughPainting($$0, $$1,$$2);
        if (!$$3.survives())
            return Optional.empty();
        return Optional.of($$3);
    }
    public void setDirection(Direction $$0) {
        Validate.notNull($$0);
        Validate.isTrue($$0.getAxis().isHorizontal());
        this.direction = $$0;
        this.setYRot((float)(this.direction.get2DDataValue() * 90));
        this.yRotO = this.getYRot();
        this.recalculateBoundingBox();
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }


    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getPaintingItem());
    }

    @Override
    public Item getPaintingItem(){
        return ModItems.PAINTING_VAN_GOUGH;
    }
}
