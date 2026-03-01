package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.KnifeItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SeperatedArmEntity extends StandEntity {

    String context = "left_hand";
    public final AnimationState floating = new AnimationState();
    public boolean Can_activate = true;

    public static final byte
            IDLE=11;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == IDLE) {
                this.floating.startIfStopped(this.tickCount);
            } else {
                this.floating.stop();
            }

        }
    }

    public SeperatedArmEntity(EntityType<SeperatedArmEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public void jump(Vec3 jumpT0Pos){
        Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());
        ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD_DUST, location.x,
                location.y, location.z,
                24,
                0.005, 0.005, 0.005,
                0.1);
        //this.setDeltaMovement(jumpT0Pos);
        this.lookAt(EntityAnchorArgument.Anchor.EYES,jumpT0Pos);
        this.setDeltaMovement((this.getLookAngle().multiply(1.5,1.5,1.5)).add(0,0.5,0));
        Can_activate = true;
        flyingTicks=0;
    }

    public BlockPos IsArmContactingBlock(){
        BlockPos Checkpos = this.getOnPos();
        for(int i = 0; i < 4; i = i + 1){
            if(!(this.level().getBlockState(Checkpos.above()).getBlock()instanceof AirBlock )){
                return Checkpos.above();
            }else if(!(this.level().getBlockState(Checkpos.below()).getBlock()instanceof AirBlock)){
                return Checkpos.below();
            }else if(!(this.level().getBlockState(Checkpos.south()).getBlock()instanceof AirBlock)){
                return Checkpos.south();
            }else if(!(this.level().getBlockState(Checkpos.north()).getBlock()instanceof AirBlock)){
                return Checkpos.north();
            }else if(!(this.level().getBlockState(Checkpos.east()).getBlock()instanceof AirBlock)){
                return Checkpos.east();
            }else if(!(this.level().getBlockState(Checkpos.west()).getBlock()instanceof AirBlock)){
                return Checkpos.west();
            }else{
                return null;
            }

        }
        return Checkpos;
    }
    public int flyingTicks=0;

    @Override
    public void tick() {
        this.setFadeOut((byte)1);
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            if(user == null){
                this.discard();
            }else{
                if(!onGround()){
                    flyingTicks +=1;
                }
                if(Can_activate  && flyingTicks > 2) {
                    ItemStack item = (this.getMainHandItem());
                    if (!(this.IsArmContactingBlock() == null)) {
                        BlockState block = (this.level().getBlockState(this.IsArmContactingBlock()));



                        boolean RightTier = false;

                        if (item.getItem() instanceof DiggerItem DI) {
                            RightTier = (
                                    (block.is(BlockTags.NEEDS_STONE_TOOL) && !(DI.getTier().equals(Tiers.WOOD)))
                                            ||
                                            (block.is(BlockTags.NEEDS_IRON_TOOL) && (DI.getTier().equals(Tiers.IRON) || DI.getTier().equals(Tiers.DIAMOND) || DI.getTier().equals(Tiers.NETHERITE)))
                                            ||
                                            (block.is(BlockTags.NEEDS_DIAMOND_TOOL) && DI.getTier().equals(Tiers.NETHERITE) || DI.getTier().equals(Tiers.DIAMOND))
                                            ||
                                            !(block.is(BlockTags.NEEDS_DIAMOND_TOOL) || block.is(BlockTags.NEEDS_STONE_TOOL) || block.is(BlockTags.NEEDS_IRON_TOOL))
                            );
                        }

                        boolean pickaxeable = block.is(BlockTags.MINEABLE_WITH_PICKAXE) && RightTier;
                        boolean axeable = block.is(BlockTags.MINEABLE_WITH_AXE) && RightTier;
                        boolean shovelable = block.is(BlockTags.MINEABLE_WITH_SHOVEL) && RightTier;


                        if (this.getMainHandItem().getItem() instanceof PickaxeItem) {
                            if (pickaxeable) {
                                this.level().destroyBlock(IsArmContactingBlock(), true);
                                this.setDeltaMovement(0, 0, 0);
                                Can_activate = false;
                            }

                        }
                        if (this.getMainHandItem().getItem() instanceof ShovelItem) {
                            if (shovelable) {
                                this.level().destroyBlock(IsArmContactingBlock(), true);
                                this.setDeltaMovement(0, 0, 0);
                                Can_activate = false;
                            }

                        }
                        if (this.getMainHandItem().getItem() instanceof AxeItem) {
                            if (axeable) {
                                this.level().destroyBlock(IsArmContactingBlock(), true);
                                this.setDeltaMovement(0, 0, 0);
                                Can_activate = false;
                            }

                        }
                    }
                    doAttack();

                }
            }

            for(int i = 0; i < 2; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
                double randY = Roundabout.RANDOM.nextDouble(-0.1, 0.1);
                double randZ = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
                ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD,
                        this.getX()+randX,
                        this.getY()+randY + 0.15 ,
                        this.getZ() + randZ,
                        1,0,0,0,0);
            }
            if(Can_activate && !onGround()) {
                ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD_DUST,
                        this.getX(),
                        this.getY() + 0.15,
                        this.getZ(),
                        1, 0, 0, 0, 0);
            }

        }

        super.tick();
    }

    public void doAttack() {
        LivingEntity user = this.getUser();
        Item item = (this.getMainHandItem().getItem());
        List<Entity> damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),1,1,1);
        for(int j = 0;j<damages.size();j++){

            Entity entity = damages.get(j);

            if(!((entity.equals((Object)this) ||entity.equals((Object)user)) || entity instanceof StandEntity)) {
                if(item instanceof KnifeItem){
                    float $$2;
                    Entity $$1 = entity;

                    if (entity instanceof Player) {
                        $$2 = (float) (2.29F * (ClientNetworking.getAppropriateConfig().itemSettings.knifeDamageOnPlayers *0.01));
                    } else {
                        $$2 = (float) (4.0F * (ClientNetworking.getAppropriateConfig().itemSettings.knifeDamageOnMobs *0.01));;
                    }
                    if ($$1 instanceof LivingEntity $$3) {
                        int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, $$3);
                        $$2 = (float) ($$2 * (1-(f*0.03)));

                    }

                    Entity $$4 = this.getUser();
                    DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.KNIFE, $$4);
                    SoundEvent $$6 = ModSounds.KNIFE_IMPACT_EVENT;
                    Vec3 DM = $$1.getDeltaMovement();
                    if ($$1.hurt($$5, $$2)) {

                        if ($$4 instanceof LivingEntity LE) {
                            LE.setLastHurtMob($$1);
                        }
                        if (MainUtil.getMobBleed($$1)){
                            ((StandUser)$$1).roundabout$setBleedLevel(0);
                            ((LivingEntity)$$1).addEffect(new MobEffectInstance(ModEffects.BLEED, 400, 0), this);
                        }
                        if ($$1.getType() == EntityType.ENDERMAN) {
                            return;
                        }

                        if ($$1 instanceof LivingEntity $$7) {
                            $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
                            if ($$4 instanceof LivingEntity) {
                                EnchantmentHelper.doPostHurtEffects($$7, $$4);
                                EnchantmentHelper.doPostDamageEffects((LivingEntity) $$4, $$7);
                            }

                        }
                        this.playSound($$6, 1.0F, (this.random.nextFloat() * 0.2F + 0.9F));
                        this.getMainHandItem().setCount(this.getMainHandItem().getCount() - 1);
                    }
                }else{
                    entity.hurt(ModDamageTypes.of(level(), DamageTypes.PLAYER_ATTACK, this.getUser(), user),(Double.valueOf(this.getAttributeValue(Attributes.ATTACK_DAMAGE)).floatValue())*1.5f);
                }
                Can_activate = false;
                this.setDeltaMovement(0,0,0);
                Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT, location.x,
                        location.y, location.z,
                        10,
                        0.005, 0.005, 0.005,
                        0.1);
                if(item instanceof KnifeItem){

                }
            }
        }
    }




    @Override
    public boolean isInvulnerableTo(DamageSource $$0) {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }


    /**USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.*/
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SeperatedLegsEntity.class,
            EntityDataSerializers.INT);

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity $$0) {
        return true;
    }

    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox();
    }

    @Override
    public boolean hasNoPhysics() {
        return false;
    }

    @Override
    protected boolean isHorizontalCollisionMinor(Vec3 $$0) {
        return super.isHorizontalCollisionMinor($$0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return true;
    }
}
