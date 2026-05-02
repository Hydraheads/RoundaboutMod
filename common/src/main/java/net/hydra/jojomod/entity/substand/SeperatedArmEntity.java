package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.GoBeyondEntity;
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
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class SeperatedArmEntity extends StandEntity {

    String context = "left_hand";
    public final AnimationState floating = new AnimationState();
    public boolean Can_activate = true;
    public boolean Can_activate_special = false;
    public int FireworkLaunchTicks = 0;
    public Vec3 LaunchAngle = null;
    public int SpinTicks = 0;

    public void setSpinTicks(int val){SpinTicks = val;};


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

    public SeperatedArmEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
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
        if(this.getMainHandItem().getItem() instanceof FireworkRocketItem FRE){

            level().playSound(null, this.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.getMainHandItem().setCount(this.getMainHandItem().getCount() - 1);
            CompoundTag fireworks = getMainHandItem().getTag().getCompound("Fireworks");
            if(fireworks.contains("Flight")){
                FireworkLaunchTicks = (fireworks.getByte("Flight")) * 20;
            }
            this.setDeltaMovement((this.getLookAngle().multiply(1.5,1.5,1.5)).add(0,0,0));
        }else{
            this.setDeltaMovement((this.getLookAngle().multiply(1.5,1.5,1.5)).add(0,0.5,0));
        }

        LaunchAngle = this.getDeltaMovement();
        Can_activate = true;
        flyingTicks=0;
    }

    public void jump2(Vec3 jumpT0Pos){
        Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());
        ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD_DUST, location.x,
                location.y, location.z,
                24,
                0.005, 0.005, 0.005,
                0.1);
        //this.setDeltaMovement(jumpT0Pos);
        this.lookAt(EntityAnchorArgument.Anchor.EYES,jumpT0Pos);
        if(this.getMainHandItem().getItem() instanceof FireworkRocketItem FRE){

            level().playSound(null, this.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.getMainHandItem().setCount(this.getMainHandItem().getCount() - 1);
            CompoundTag fireworks = getMainHandItem().getTag().getCompound("Fireworks");
            if(fireworks.contains("Flight")){
                FireworkLaunchTicks = (fireworks.getByte("Flight")) * 20;
            }
            this.setDeltaMovement((this.getLookAngle().multiply(2,2,2)).add(0,0,0));
        }else{
            this.setDeltaMovement((this.getLookAngle().multiply(2,2,2)).add(0,0,0));
        }

        LaunchAngle = this.getDeltaMovement();
        Can_activate = true;
        flyingTicks=0;
    }

    public BlockPos IsArmContactingBlock(){
        
        BlockPos Checkpos = this.getOnPos();

        for(int i = 0; i < 4; i = i + 1){
            if(!(this.level().getBlockState(Checkpos.above()).getBlock()instanceof AirBlock ) ){
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

          //  if(isColliding(Checkpos.above(),(this.level().getBlockState(Checkpos.above())))){
          //      return Checkpos.above();
          //  }else if(isColliding(Checkpos.below(),(this.level().getBlockState(Checkpos.below())))){
          //      return Checkpos.below();
          //  }else if(isColliding(Checkpos.south(),(this.level().getBlockState(Checkpos.south())))){
          //      return Checkpos.south();
          //  }else if(isColliding(Checkpos.north(),(this.level().getBlockState(Checkpos.north())))){
          //      return Checkpos.north();
          //  }else if(isColliding(Checkpos.east(),(this.level().getBlockState(Checkpos.east())))){
          //      return Checkpos.east();
         //   }else if(isColliding(Checkpos.west(),(this.level().getBlockState(Checkpos.west())))){
          //      return Checkpos.west();
         //   }else{
         //       return null;
         //   }


        }
        //return hitResult.getBlockPos();
        return Checkpos;
    }
    public int flyingTicks=0;


    @Override
    public void tick() {
        this.entityData.set(HELD_ITEM,this.getMainHandItem());


        tickeffects();
    }

    public void tickeffects() {
        this.setFadeOut((byte)1);
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            if(user == null){
                spawnAtLocation(this.getMainHandItem());
                this.discard();
            }else if((!(((StandUser)user).roundabout$getStandPowers() instanceof PowersGreenDay)) || (!user.isAlive())){
                spawnAtLocation(this.getMainHandItem());
                this.discard();
            }
            else{
                if(!onGround()){
                    flyingTicks +=1;
                }else{
                    flyingTicks = 0;
                }
                if(Can_activate_special || (Can_activate  && flyingTicks > 2)) {
                    ItemStack item = (this.getMainHandItem());
                    if (!(this.IsArmContactingBlock() == null) && Can_activate) {
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

                        BlockState state = this.level().getBlockState(IsArmContactingBlock());
                        ServerLevel level = (ServerLevel) this.level();

                        BlockPos targetpos = IsArmContactingBlock();
                        
                        if (this.getMainHandItem().getItem() instanceof PickaxeItem) {
                            if (pickaxeable) {
                                level.destroyBlock(targetpos, true,this);
                                this.setDeltaMovement(0, 0, 0);
                                Can_activate = false;
                            }

                        }
                        else if (this.getMainHandItem().getItem() instanceof ShovelItem) {
                            if (shovelable) {
                                level.destroyBlock(targetpos, true, this);
                                this.setDeltaMovement(0, 0, 0);
                                Can_activate = false;
                            }

                        }
                        else if (this.getMainHandItem().getItem() instanceof AxeItem) {
                            if (axeable) {
                                level.destroyBlock(targetpos, true,this);

                                this.setDeltaMovement(0, 0, 0);
                                Can_activate = false;
                            }

                        }
                    }
                    doAttack();
                    Can_activate_special = false;
                }
                attractMobs();
                pickUpItems();
                doSpin();
            }

            if(FireworkLaunchTicks > 0){
                FireworkLaunchTicks --;
                this.setDeltaMovement(LaunchAngle);
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.FIREWORK,
                        this.getX(),
                        this.getY() + 0.15 ,
                        this.getZ(),
                        1,0,0,0,0);

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

    public boolean hasUsedItem = false;

    public void doSpin(){
        if(SpinTicks > 0){
            for(int i = 0; i < 1; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-0.3, 0.3);
                double randY = Roundabout.RANDOM.nextDouble(-0.3, 0.3);
                double randZ = Roundabout.RANDOM.nextDouble(-0.3, 0.3);
                ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0.76F, 1.0F, 0.9F
                        ), 2f),
                        this.getX() + randX,
                        this.getY() + randY,
                        this.getZ() + randZ,
                        0,0,0.2,0,0);

            }
            SpinTicks --;
            Can_activate_special = true;
            this.setYRot(this.getYRot() + 25);
            this.setYHeadRot(this.getYHeadRot() + 45);

            if(getMainHandItem().getItem() instanceof SplashPotionItem SPI || getMainHandItem().getItem() instanceof LingeringPotionItem){
                ThrownPotion pot = new ThrownPotion(level(),this.getX(),this.getY(),this.getZ());
                pot.setDeltaMovement(0,-1,0);
                pot.setItem(getMainHandItem());
                level().addFreshEntity(pot);
                this.getMainHandItem().setCount(0);

            }else if(getMainHandItem().getItem() instanceof BlockItem){
                Block block = ((BlockItem) getMainHandItem().getItem()).getBlock();
                if(block instanceof WebBlock && !hasUsedItem && level().getBlockState(this.getOnPos()).isAir()){
                    this.setDeltaMovement(0,0,0);
                    level().setBlockAndUpdate(this.getOnPos(),block.defaultBlockState());
                    this.getMainHandItem().setCount(this.getMainHandItem().getCount() - 1);
                    hasUsedItem = true;
                }

            }
            if (getMainHandItem().getItem() instanceof EnderpearlItem EPI && !hasUsedItem){
                for(int i =1; i<44; i++) {
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.PORTAL, this.getX() + Roundabout.RANDOM.nextDouble(-1, 1),
                            this.getY() + Roundabout.RANDOM.nextDouble(-1, 1), this.getZ() + Roundabout.RANDOM.nextDouble(-1, 1),
                            1,
                            0, 0, 0,
                            3);
                }
                if(!level().isClientSide()) {
                    User.teleportTo(this.getX(), this.getY(), this.getZ());
                    level().playSound(null, this.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                hasUsedItem = true;
               // this.getUser().moveTo(new Vec3(getX(),getY(),getZ()));
                this.getMainHandItem().setCount(this.getMainHandItem().getCount() - 1);
            }

        }else{
            hasUsedItem = false;
            this.getHeldItem();
        }
    }



    public void attractMobs(){
        List<Entity> damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),16,16,16);
        for(int j = 0;j<damages.size();j++) {
            Entity entity = damages.get(j);
            if(entity instanceof Mob mob){
                if(((entity instanceof Sheep || entity instanceof Cow || entity instanceof Goat) && (Ingredient.of(new ItemLike[]{Items.WHEAT})).test(getMainHandItem()))
                        ||(((entity instanceof Chicken || entity instanceof Parrot) && (Ingredient.of(new ItemLike[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD})).test(getMainHandItem())))
                        ||(entity instanceof Pig && (Ingredient.of(new ItemLike[]{Items.CARROT, Items.POTATO, Items.BEETROOT})).test(getMainHandItem()))
                ) {
                    PathNavigation nav = mob.getNavigation();
                    Path path = nav.createPath(this.getX(), this.getY(), this.getZ(), 1);
                    if (path != null) {
                        nav.moveTo(path, 1);
                    }
                }
            }
        }

    }

    public void pickUpItems(){

        Item item = (this.getMainHandItem().getItem());
        ItemStack itemStack = (this.getMainHandItem());
        List<Entity> damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),3,3,3);

        for(int j = 0;j<damages.size();j++) {
            Entity entity = damages.get(j);
            item = (this.getMainHandItem().getItem());
            itemStack = (this.getMainHandItem());

            if(entity instanceof ItemEntity IE){
                if (IE.getItem().getItem().equals(item) || item instanceof AirItem){

                    for(int i = 0; i < IE.getItem().getCount(); i++){

                        if(itemStack.getCount() < itemStack.getMaxStackSize()){

                            if(item instanceof AirItem){
                                this.setItemInHand(InteractionHand.MAIN_HAND,IE.getItem());
                                IE.discard();
                            }else{
                                itemStack.setCount(itemStack.getCount() + IE.getItem().getCount());
                                IE.discard();
                            }
                        }

                    }

                }
            }
        }
    }



    public void doAttack() {
        LivingEntity user = this.getUser();
        Item item = (this.getMainHandItem().getItem());
        List<Entity> damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),1,1,1);
        if(SpinTicks >0){
            damages = MainUtil.genHitbox(this.level(),this.getX(),this.getY(),this.getZ(),2,2,2);
        }


        for(int j = 0;j<damages.size();j++){

            Entity entity = damages.get(j);
            ((StandUser)user).roundabout$getStandPowers().addEXP(1);

            if(!((entity.equals(this) ||entity.equals((Object)user)) || entity instanceof StandEntity || entity instanceof ItemEntity)) {
                if (flyingTicks > 2 && SpinTicks >0) {
                    BlockPos pos = new BlockPos(new Vec3i((int) this.getX(), (int) (this.getY() - 0.2), (int) this.getZ()));
                    if ((level().getBlockState(new BlockPos(pos)).isAir())) {
                        this.level().addParticle(ParticleTypes.FLASH,this.getX(),this.getY(),this.getZ(),0,0,0);
                        entity.addDeltaMovement(new Vec3(0, 0.2, 0));
                    }
                }

                if(item instanceof KnifeItem){
                    Can_activate = false;
                    this.setDeltaMovement(0,0,0);
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
                    if(SpinTicks > 1){
                        $$2 = $$2 / 6;
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
                    Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT, location.x,
                            location.y, location.z,
                            16,
                            0.45, 0.45, 0.45,
                            0.1);

                }else if(this.getMainHandItem().getItem() instanceof ShieldItem){
                    Can_activate = true;
                    if(entity instanceof Projectile && !(entity instanceof GoBeyondEntity)){
                        entity.discard();

                        Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());
                        ((ServerLevel) this.level()).sendParticles(ModParticles.PUNCH_MISS, location.x,
                                location.y, location.z,
                                1,
                                0, 0, 0,
                                0.1);


                    }
                }

                else {
                    Can_activate = false;
                    this.setDeltaMovement(0, 0, 0);
                    Vec3 location = new Vec3(this.getX(), this.getY(), this.getZ());
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT, location.x,
                            location.y, location.z,
                            16,
                            0.45, 0.45, 0.45,
                            0.1);
                    if (entity instanceof Player) {
                        entity.hurt(ModDamageTypes.of(level(), DamageTypes.PLAYER_ATTACK, this.getUser(), user), (Double.valueOf(this.getAttributeValue(Attributes.ATTACK_DAMAGE)).floatValue()) * 1.75f);
                    } else {
                        entity.hurt(ModDamageTypes.of(level(), DamageTypes.PLAYER_ATTACK, this.getUser(), user), (Double.valueOf(this.getAttributeValue(Attributes.ATTACK_DAMAGE)).floatValue()) * 1f);
                    }
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

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}
