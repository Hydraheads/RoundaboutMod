package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

public class BlackSabbathEntity extends StandEntity implements HasCustomInventoryScreen{

    public BlackSabbathEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            PART_5_ANIME = 1,
            PART_5_MANGA = 2,
            BURNING = 3,
            GIO_GIO = 4,
            VERDANT = 5,
            NIGHT = 6,
            DEPARTURE = 7,
            PHANTOM = 8,
            SWEET = 9;

    public final AnimationState coat_open = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {

        }
    }

    @Override
    public boolean forceVisualRotation(){
        return true;
    }

    @Override
    public boolean lockPos(){
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return true;
    }

    @Override
    public void tick(){
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
            super.defineSynchedData();
    }

    @Override
    public void openCustomInventoryScreen(Player var1) {
        if (!this.level().isClientSide) {

        }
    }
}
