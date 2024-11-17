package net.hydra.jojomod.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;


public class PowerInventoryScreen
        extends EffectRenderingInventoryScreen<PowerInventoryMenu> {
    /**Currently unfinished, this is when you press the stand power inventory key.
     * It should render your current stand, as well as its moves and stuff.*/
    public static final ResourceLocation POWER_INVENTORY_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/power_inventory.png");
    public static final ResourceLocation POWER_INVENTORY_GEAR_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/stand_user_settings.png");

    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private float xMouse;
    private float yMouse;
    //private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();
    private boolean widthTooNarrow;
    private boolean buttonClicked;

    public boolean isOptionsOut;
    private StandEntity stand = null;
    public List<AbilityIconInstance> abilityList = ImmutableList.of();

    public PowerInventoryScreen(Player player, PowerInventoryMenu pim) {
        super(pim, player.getInventory(), ((StandUser)player).roundabout$getStandPowers().getStandName());
        this.titleLabelX = 80;
    }


    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        Player pl = Minecraft.getInstance().player;
        int i = this.leftPos;
        int j = this.topPos;
        context.blit(POWER_INVENTORY_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (pl != null) {
        StandUser standUser = ((StandUser)pl);

            stand = standUser.roundabout$getStand();
            if (stand != null) {
                StandPowers sp = standUser.roundabout$getStandPowers();
                renderStandEntityInInventoryFollowsMouse(context, i + 51, j + 75 - sp.getDisplayPowerInventoryYOffset(), sp.getDisplayPowerInventoryScale(),
                        (float) (i + 51) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, stand,pl);
                  context.drawString(this.font, stand.getSkinName(((IPlayerEntity)pl).roundabout$getStandSkin()), this.titleLabelX+11+leftPos, this.titleLabelY+18+topPos, 16777215, false);
                context.drawString(this.font, stand.getPosName(stand.getIdleAnimation()), this.titleLabelX+11+leftPos, this.titleLabelY+36+topPos, 16777215, false);
                int lefXPos = leftPos+77;
                int rightXPos = leftPos+164;
                int topYPos = topPos+22;
                int bottomYPos = topPos+40;

                if (sp.hasMoreThanOneSkin()){
                    if (isSurelyHovering(rightXPos, topYPos, 7, 13, mouseX, mouseY)) {
                        context.blit(POWER_INVENTORY_LOCATION, rightXPos, topYPos, 177, 31, 7, 11);
                    } else {
                        context.blit(POWER_INVENTORY_LOCATION, rightXPos, topYPos, 177, 19, 7, 11);
                    }

                    if (isSurelyHovering(lefXPos, topYPos, 7, 13, mouseX, mouseY)) {
                        context.blit(POWER_INVENTORY_LOCATION, lefXPos, topYPos, 185, 31, 7, 11);
                    } else {
                        context.blit(POWER_INVENTORY_LOCATION, lefXPos, topYPos, 185, 19, 7, 11);
                    }
                }

                if (isSurelyHovering(rightXPos, bottomYPos, 7, 13, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_LOCATION, rightXPos, bottomYPos, 177, 31, 7, 11);
                } else {
                    context.blit(POWER_INVENTORY_LOCATION, rightXPos, bottomYPos, 177, 19, 7, 11);
                }

                if (isSurelyHovering(lefXPos, bottomYPos, 7, 13, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_LOCATION, lefXPos, bottomYPos, 185, 31, 7, 11);
                } else {
                    context.blit(POWER_INVENTORY_LOCATION, lefXPos, bottomYPos, 185, 19, 7, 11);
                }

                int leftGearPos = leftPos+5;
                int topGearPos = topPos+60;
                if (isSurelyHovering(leftGearPos, topGearPos, 19, 18, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_LOCATION, leftGearPos, topGearPos, 198, 0, 19, 18);
                } else {
                    context.blit(POWER_INVENTORY_LOCATION, leftGearPos, topGearPos, 178, 0, 19, 18);
                }

                int ss = this.leftPos+78;
                int sss = this.topPos+57;
                byte level = ((IPlayerEntity)pl).roundabout$getStandLevel();
                int exp = ((IPlayerEntity)pl).roundabout$getStandExp();
                int maxXP = standUser.roundabout$getStandPowers().getExpForLevelUp(level);
                Component display;
                if (level == standUser.roundabout$getStandPowers().getMaxLevel() ||
                        (!standUser.roundabout$getStandDisc().isEmpty() && standUser.roundabout$getStandDisc().getItem()
                        instanceof MaxStandDiscItem)){
                    exp = maxXP;
                    display = Component.translatable(  "leveling.roundabout.disc_maxed");
                } else {
                    display = Component.translatable(  "leveling.roundabout.disc_development_potential_level",
                            level);
                }
                context.drawString(this.font, display, this.titleLabelX+ss-78, this.titleLabelY+sss+2, 4210752, false);
                int blt = (int) Math.floor(((double) 92 /maxXP)*(exp));
                context.blit(POWER_INVENTORY_LOCATION, ss, sss, 10, 244, 92, 4);
                context.blit(POWER_INVENTORY_LOCATION, ss, sss, 10, 240, blt, 4);
            }
        }
    }

    public String[] splitIntoLine(String input, int maxCharInLine){

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while(word.length() > maxCharInLine){
                output.append(word.substring(0, maxCharInLine-lineLen) + "\n");
                word = word.substring(maxCharInLine-lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }
    protected boolean isSurelyHovering(int p_97768_, int p_97769_, int p_97770_, int p_97771_, double p_97772_, double p_97773_) {
        return p_97772_ >= (double)(p_97768_) && p_97772_ < (double)(p_97768_ + p_97770_) && p_97773_ >= (double)(p_97769_) && p_97773_ < (double)(p_97769_ + p_97771_);
    }

    public static void renderStandEntityInInventoryFollowsMouse(GuiGraphics $$0, int $$1, int $$2, int $$3, float $$4, float $$5, StandEntity $$6, Player user) {
        float $$7 = (float)Math.atan((double)($$4 / 40.0F));
        float $$8 = (float)Math.atan((double)($$5 / 40.0F));
        Quaternionf $$9 = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf $$10 = new Quaternionf().rotateX($$8 * 20.0F * (float) (Math.PI / 180.0));
        $$9.mul($$10);
        float $$11 = $$6.yBodyRot;
        float $$12 = $$6.getYRot();
        float $$13 = $$6.getXRot();
        float $$14 = $$6.yHeadRotO;
        float $$15 = $$6.yHeadRot;
        byte OT = $$6.getOffsetType();
        $$6.yBodyRot = 180.0F + $$7 * 20.0F;
        $$6.setYRot(180.0F + $$7 * 40.0F);
        $$6.setXRot(-$$8 * 20.0F);
        $$6.yHeadRot = user.getYRot();
        $$6.yHeadRotO = user.getYRot();
        $$6.setOffsetType(OffsetIndex.LOOSE);
        $$6.setDisplay(true);
        renderEntityInInventory($$0, $$1, $$2, $$3, $$9, $$10, $$6);
        $$6.setDisplay(false);
        $$6.yBodyRot = $$11;
        $$6.setYRot($$12);
        $$6.setXRot($$13);
        $$6.yHeadRotO = $$14;
        $$6.yHeadRot = $$15;
        $$6.setOffsetType(OT);
    }

    public int leftposModifier = 50;

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {


        //leftPos+=leftposModifier;

        this.renderBackground(context);

         super.render(context, mouseX, mouseY, delta);

        this.renderTooltip(context, mouseX, mouseY);

        Player pl = Minecraft.getInstance().player;
        int i = this.leftPos;
        int j = this.topPos;
        if (pl != null) {

            if (isOptionsOut){
                IPlayerEntity ipe = ((IPlayerEntity) pl);
                int anchorPlace = ipe.roundabout$getAnchorPlace();
                float distanceOut = ipe.roundabout$getDistanceOut();
                float idleOpacity = ipe.roundabout$getIdleOpacity();
                float combatOpacity = ipe.roundabout$getCombatOpacity();
                float enemyOpacity = ipe.roundabout$getEnemyOpacity();

                context.blit(POWER_INVENTORY_GEAR_LOCATION, i-150, j, 0, 0, 148, 167);
                context.drawString(this.font, Component.translatable(  "power_inventory.roundabout.settings.general").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.WHITE), i- 135, j+24, 4210752, false);

                context.drawString(this.font, Component.translatable(  "power_inventory.roundabout.settings.offset").withStyle(ChatFormatting.GRAY), i- 135, j+36, 4210752, false);
                context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136, j+46, 11, 173, 118, 11);
                int renderSpot1 = (int) Math.floor(((double) 114/359)*(anchorPlace));
                if (isSurelyHovering(i-136, j+45, 118, 11, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot1, j+45, 5, 185, 5, 11);
                } else {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot1, j+45, 5, 173, 5, 11);
                }

                context.drawString(this.font, Component.translatable(  "power_inventory.roundabout.settings.distance").withStyle(ChatFormatting.GRAY), i- 135, j+58, 4210752, false);
                context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136, j+67, 11, 173, 118, 11);

                int renderSpot2 = (int) Math.floor(((double) 114/2)*(distanceOut));
                if (isSurelyHovering(i-136, j+67, 118, 11, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot2, j+67, 5, 185, 5, 11);
                } else {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot2, j+67, 5, 173, 5, 11);
                }

                context.drawString(this.font, Component.translatable(  "power_inventory.roundabout.settings.opacity").withStyle(ChatFormatting.GRAY), i- 135, j+80, 4210752, false);
                context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136, j+89, 11, 173, 118, 11);
                int renderSpot3 = (int) Math.floor(((double) 114/100)*(idleOpacity));
                if (isSurelyHovering(i-136, j+89, 118, 11, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot3, j+89, 5, 185, 5, 11);
                } else {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot3, j+89, 5, 173, 5, 11);
                }

                context.drawString(this.font, Component.translatable(  "power_inventory.roundabout.settings.attack_opacity").withStyle(ChatFormatting.GRAY), i- 135, j+102, 4210752, false);
                context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136, j+111, 11, 173, 118, 11);
                int renderSpot4 = (int) Math.floor(((double) 114/100)*(combatOpacity));
                if (isSurelyHovering(i-136, j+111, 118, 11, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot4, j+111, 5, 185, 5, 11);
                } else {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot4, j+111, 5, 173, 5, 11);
                }

                context.drawString(this.font, Component.translatable(  "power_inventory.roundabout.settings.enemy_opacity").withStyle(ChatFormatting.GRAY), i- 135, j+124, 4210752, false);
                context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136, j+134, 11, 173, 118, 11);
                int renderSpot5 = (int) Math.floor(((double) 114/100)*(enemyOpacity));
                if (isSurelyHovering(i-136, j+133, 118, 11, mouseX, mouseY)) {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot5, j+133, 5, 185, 5, 11);
                } else {
                    context.blit(POWER_INVENTORY_GEAR_LOCATION, i-136+renderSpot5, j+133, 5, 173, 5, 11);
                }

                if (isSurelyHovering(i-136, j+146, 118, 11, mouseX, mouseY)) {
                    context.drawString(this.font, Component.translatable("power_inventory.roundabout.settings.reset")
                            .withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC), i- 135, j+148, 4210752, false);
                } else {
                    context.drawString(this.font, Component.translatable("power_inventory.roundabout.settings.reset")
                            .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC), i- 135, j+148, 4210752, false);
                }
            }
            StandUser standUser = ((StandUser) pl);
            boolean bypass = false;
            if ((!((StandUser) pl).roundabout$getStandDisc().isEmpty() &&
                    ((StandUser) pl).roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem) ||
            pl.isCreative()){
                bypass = true;
            }
            if (!ClientNetworking.getAppropriateConfig().enableStandLeveling) {
                bypass=true;
            }
            abilityList = standUser.roundabout$getStandPowers().drawGUIIcons(context, delta, mouseX, mouseY, i, j,
                    ((IPlayerEntity)pl).roundabout$getStandLevel(),bypass);

            if (!this.abilityList.isEmpty()) {
                AbilityIconInstance aii;
                for (int g = abilityList.size() - 1; g >= 0; --g) {
                    aii = abilityList.get(g);
                    if (isSurelyHovering(aii.startingLeft, aii.startingTop, aii.size, aii.size, mouseX, mouseY)) {
                        List<Component> compList = Lists.newArrayList();
                        compList.add(aii.name);
                        compList.add(aii.instruction);
                        String[] strung2 = splitIntoLine(aii.description.getString(), 30);
                        for (String s : strung2) {
                            compList.add(Component.literal(s));
                        }
                        context.renderTooltip(this.font, compList, Optional.empty(), mouseX, mouseY);
                    }
                }
            }
        }

        //this.recipeBookComponent.renderTooltip(context, this.leftPos, this.topPos, mouseX, mouseY);
        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;
        //leftPos-=leftposModifier;
    }



    @Override
    public void containerTick() {
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        isOptionsOut = false;
            //this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
            //this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            //this.addRenderableWidget(new ImageButton(this.leftPos + 104, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, $$0 -> {
                //this.recipeBookComponent.toggleVisibility();
                //this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                //$$0.setPosition(this.leftPos + 104, this.height / 2 - 22);
                //this.buttonClicked = true;
            //}));
            //this.addWidget(this.recipeBookComponent);
            //this.setInitialFocus(this.recipeBookComponent);
    }

    @Override
    protected void renderLabels(GuiGraphics $$0, int $$1, int $$2) {
        $$0.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }


    public static void renderEntityInInventory(GuiGraphics $$0, int $$1, int $$2, int $$3, Quaternionf $$4, @Nullable Quaternionf $$5, LivingEntity $$6) {
        $$0.pose().pushPose();
        $$0.pose().translate((double)$$1, (double)$$2, 50.0);
        $$0.pose().mulPoseMatrix(new Matrix4f().scaling((float)$$3, (float)$$3, (float)(-$$3)));
        $$0.pose().mulPose($$4);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        if ($$5 != null) {
            $$5.conjugate();
            $$7.overrideCameraOrientation($$5);
        }

        $$7.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> $$7.render($$6, 0.0, 0.0, 0.0, 0.0F, 1.0F, $$0.pose(), $$0.bufferSource(), 15728880));
        $$0.flush();
        $$7.setRenderShadow(true);
        $$0.pose().popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    protected boolean isHovering(int $$0, int $$1, int $$2, int $$3, double $$4, double $$5) {
        return (!this.widthTooNarrow) && super.isHovering($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @Override
    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        Player pl = Minecraft.getInstance().player;
        if (pl != null) {


            if (isOptionsOut){
                int i = this.leftPos;
                int j = this.topPos;
                IPlayerEntity ipe = ((IPlayerEntity) pl);
                int anchorPlace = ipe.roundabout$getAnchorPlace();
                float distanceOut = ipe.roundabout$getDistanceOut();
                float idleOpacity = ipe.roundabout$getIdleOpacity();
                float combatOpacity = ipe.roundabout$getCombatOpacity();
                float enemyOpacity = ipe.roundabout$getEnemyOpacity();
                int renderSpot1 = (int) Math.floor(((double) 114/359)*(anchorPlace));
                int jump = i-136;
                if (isSurelyHovering(jump, j+45, 118, 11, $$0, $$1)) {
                    int initialX = ((int)$$0) - jump;
                    initialX = (int)((float) 359 /118)*initialX;
                    ipe.roundabout$setAnchorPlace(initialX);
                    ModPacketHandler.PACKET_ACCESS.intToServerPacket(initialX, PacketDataIndex.INT_ANCHOR_PLACE);
                    return true;
                }
                if (isSurelyHovering(jump, j+67, 118, 11, $$0, $$1)) {
                    float initialX = (float) ($$0 - jump);
                    initialX = ((float) 2 /118)*initialX;
                    ipe.roundabout$setDistanceOut(initialX);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(initialX, PacketDataIndex.FLOAT_DISTANCE_OUT);
                    return true;
                }
                if (isSurelyHovering(jump, j+89, 118, 11, $$0, $$1)) {
                    float initialX = (float) ($$0 - jump);
                    initialX = ((float) 100 /118)*initialX;
                    ipe.roundabout$setIdleOpacity(initialX);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(initialX, PacketDataIndex.FLOAT_IDLE_OPACITY);
                    return true;
                }
                if (isSurelyHovering(jump, j+111, 118, 11, $$0, $$1)) {
                    float initialX = (float) ($$0 - jump);
                    initialX = ((float) 100 /118)*initialX;
                    ipe.roundabout$setCombatOpacity(initialX);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(initialX, PacketDataIndex.FLOAT_COMBAT_OPACITY);
                    return true;
                }
                if (isSurelyHovering(i-136, j+133, 118, 11, $$0, $$1)) {
                    float initialX = (float) ($$0 - jump);
                    initialX = ((float) 100 /118)*initialX;
                    ipe.roundabout$setEnemyOpacity(initialX);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(initialX, PacketDataIndex.FLOAT_ENEMY_OPACITY);
                    return true;
                }
                if (isSurelyHovering(i-136, j+146, 118, 11, $$0, $$1)) {
                    ipe.roundabout$setAnchorPlace(55);
                    ipe.roundabout$setDistanceOut(1.07F);
                    ipe.roundabout$setIdleOpacity(100);
                    ipe.roundabout$setCombatOpacity(100);
                    ipe.roundabout$setEnemyOpacity(100);
                    ModPacketHandler.PACKET_ACCESS.intToServerPacket(55, PacketDataIndex.INT_ANCHOR_PLACE);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(1.07F, PacketDataIndex.FLOAT_DISTANCE_OUT);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(100, PacketDataIndex.FLOAT_IDLE_OPACITY);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(100, PacketDataIndex.FLOAT_COMBAT_OPACITY);
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(100, PacketDataIndex.FLOAT_ENEMY_OPACITY);
                    return true;
                }
            }

            int leftGearPos = leftPos+6;
            int topGearPos = topPos+60;
            if (isSurelyHovering(leftGearPos, topGearPos, 18, 18, $$0, $$1)) {
                gearChange();
                SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
                soundmanager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }

            int lefXPos = leftPos + 77;
            int rightXPos = leftPos + 164;
            int topYPos = topPos + 22;
            int bottomYPos = topPos + 40;
            StandUser standUser = ((StandUser) pl);
            StandPowers sp = standUser.roundabout$getStandPowers();
            StandUserClientPlayer scp = ((StandUserClientPlayer)pl);
            int menuTicks = scp.roundabout$getMenuTicks();
            stand = standUser.roundabout$getStand();
                if (sp.hasMoreThanOneSkin()) {
                    if (isSurelyHovering(rightXPos, topYPos, 7, 13, $$0, $$1)) {
                        if (menuTicks <= -1) {
                            scp.roundabout$setMenuTicks(5);
                            ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_SKIN_RIGHT);
                        }
                        return true;
                    }

                    if (isSurelyHovering(lefXPos, topYPos, 7, 13, $$0, $$1)) {
                        if (menuTicks <= -1) {
                            scp.roundabout$setMenuTicks(5);
                            ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_SKIN_LEFT);
                        }
                        return true;
                    }
                }




            if (isSurelyHovering(rightXPos, bottomYPos, 7, 13, $$0, $$1)) {
                        if (menuTicks <= -1) {
                            scp.roundabout$setMenuTicks(5);
                            ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_IDLE_RIGHT);
                        }
                        return true;
                    }

                    if (isSurelyHovering(lefXPos, bottomYPos, 7, 13, $$0, $$1)) {
                        if (menuTicks <= -1) {
                            scp.roundabout$setMenuTicks(5);
                            ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_IDLE_LEFT);
                        }
                        return true;
                    }
        }
        /**
        if (this.recipeBookComponent.mouseClicked($$0, $$1, $$2)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
         **/
            return this.widthTooNarrow ? false : super.mouseClicked($$0, $$1, $$2);
        //}
    }

    public void gearChange(){
        if (this.isOptionsOut){
            this.isOptionsOut = false;
            this.leftPos -= leftposModifier;
        } else {
            this.isOptionsOut = true;
            this.leftPos += leftposModifier;
        }
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased($$0, $$1, $$2);
        }
    }

    @Override
    protected void slotClicked(Slot $$0, int $$1, int $$2, ClickType $$3) {
        super.slotClicked($$0,$$1,$$2,$$3);
    }

}
