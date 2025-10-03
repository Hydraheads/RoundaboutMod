package net.hydra.jojomod.entity.paintings;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.Validate;

import java.util.Optional;

public class BirthOfVenusPainting extends RoundaboutPainting {



    public BirthOfVenusPainting(EntityType<? extends BirthOfVenusPainting> $$0, Level $$1) {
        super($$0, $$1);
    }

    private BirthOfVenusPainting(Level $$0, BlockPos $$1, Direction $$2) {
        super(getPaintingType(),$$0, $$1);
        this.setDirection($$2);
    }



    public static EntityType<? extends RoundaboutPainting> getPaintingType(){
        return ModEntities.BIRTH_OF_VENUS_PAINTING;
    }

    public static Optional<RoundaboutPainting> create(Level $$0, BlockPos $$1, Direction $$2) {
        RoundaboutPainting $$3 = new BirthOfVenusPainting($$0, $$1,$$2);
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
        return 64;
    }

    @Override
    public int getHeight() {
        return 48;
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
        return ModItems.PAINTING_BIRTH_OF_VENUS;
    }
}
