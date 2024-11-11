package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;

public class JusticeMobSwitcherScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation MOB_SWITCHER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/mob_switcher.png");
    private static final int SPRITE_SHEET_WIDTH = 128;
    private static final int SPRITE_SHEET_HEIGHT = 128;
    private static final int SLOT_AREA = 26;
    private static final int SLOT_PADDING = 5;
    private static final int SLOT_AREA_PADDED = 31;
    private static final int HELP_TIPS_OFFSET_Y = 5;
    private static final int ALL_SLOTS_WIDTH = MobIcon.VALUES.length * 31 - 5;
    private static final int ALL_SLOTS2_WIDTH = JusticeMobSwitcherScreen.MobIcon.VALUES2.length * 31 - 5;
    private static final Component SELECT_KEY = Component.translatable("justice.morph.select_next",
            KeyInputRegistry.abilityOneKey.getTranslatedKeyMessage(),
            KeyInputRegistry.abilityTwoKey.getTranslatedKeyMessage());
    private final JusticeMobSwitcherScreen.MobIcon previousHovered;
    private JusticeMobSwitcherScreen.MobIcon currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    public static boolean hasOVASkin = false;
    public static boolean hasWitherSkin = false;
    public static boolean hasStraySkin = false;
    private final List<JusticeMobSwitcherScreen.MobSlot> slots = Lists.newArrayList();

    public JusticeMobSwitcherScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = this.previousHovered = JusticeMobSwitcherScreen.MobIcon.getFromGameType(this.getDefaultSelected());
    }

    private ShapeShifts getDefaultSelected() {
        Player pl = Minecraft.getInstance().player;
        if (pl != null){
            return ShapeShifts.getShiftFromByte(((IPlayerEntity)pl).roundabout$getShapeShift());
        }
        return ShapeShifts.PLAYER;
    }

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;
        if (pl != null){
            byte standSkin = ((IPlayerEntity)pl).roundabout$getStandSkin();
            hasOVASkin = standSkin == JusticeEntity.OVA_SKIN;
            hasWitherSkin = standSkin == JusticeEntity.WITHER;
            hasStraySkin = standSkin == JusticeEntity.STRAY_SKIN;
        } else {
            hasOVASkin = false;
            hasWitherSkin = false;
            hasStraySkin = false;
        }


        this.currentlyHovered = this.previousHovered;
        if (hasOVASkin) {
            for (int i = 0; i < JusticeMobSwitcherScreen.MobIcon.VALUES2.length; ++i) {
                JusticeMobSwitcherScreen.MobIcon MobIcon = JusticeMobSwitcherScreen.MobIcon.VALUES2[i];
                this.slots.add(new JusticeMobSwitcherScreen.MobSlot(MobIcon, this.width / 2 - ALL_SLOTS2_WIDTH / 2 + i * 31, this.height / 2 - 31));
            }
        } else if (hasWitherSkin){
            for (int i = 0; i < JusticeMobSwitcherScreen.MobIcon.VALUES3.length; ++i) {
                JusticeMobSwitcherScreen.MobIcon MobIcon = JusticeMobSwitcherScreen.MobIcon.VALUES3[i];
                this.slots.add(new JusticeMobSwitcherScreen.MobSlot(MobIcon, this.width / 2 - ALL_SLOTS2_WIDTH / 2 + i * 31, this.height / 2 - 31));
            }
        } else if (hasStraySkin){
            for (int i = 0; i < JusticeMobSwitcherScreen.MobIcon.VALUES4.length; ++i) {
                JusticeMobSwitcherScreen.MobIcon MobIcon = JusticeMobSwitcherScreen.MobIcon.VALUES4[i];
                this.slots.add(new JusticeMobSwitcherScreen.MobSlot(MobIcon, this.width / 2 - ALL_SLOTS2_WIDTH / 2 + i * 31, this.height / 2 - 31));
            }
        } else {
            for (int i = 0; i < JusticeMobSwitcherScreen.MobIcon.VALUES.length; ++i) {
                JusticeMobSwitcherScreen.MobIcon MobIcon = JusticeMobSwitcherScreen.MobIcon.VALUES[i];
                this.slots.add(new JusticeMobSwitcherScreen.MobSlot(MobIcon, this.width / 2 - ALL_SLOTS_WIDTH / 2 + i * 31, this.height / 2 - 31));
            }
        }
    }


    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        this.switchToHoveredGameMode();
        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (this.checkToClose()) {
            return;
        }
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 62;
        int l = this.height / 2 - 31 - 27;
        guiGraphics.blit(MOB_SWITCHER_LOCATION, k, l, 0.0f, 0.0f, 125, 75, 128, 128);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.currentlyHovered.getName(), this.width / 2, this.height / 2 - 31 - 20, -1);
        guiGraphics.drawCenteredString(this.font, SELECT_KEY, this.width / 2, this.height / 2 + 5, 0xFFFFFF);
        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
        for (JusticeMobSwitcherScreen.MobSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            this.currentlyHovered = MobSlot.icon;
        }
    }

    private void switchToHoveredGameMode() {
        JusticeMobSwitcherScreen.switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }

    private static void switchToHoveredGameMode(Minecraft minecraft, JusticeMobSwitcherScreen.MobIcon MobIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }
        JusticeMobSwitcherScreen.MobIcon MobIcon2 = JusticeMobSwitcherScreen.MobIcon.
                getFromGameType(
                        ShapeShifts.getShiftFromByte(((IPlayerEntity)minecraft.player).roundabout$getShapeShift()));
        JusticeMobSwitcherScreen.MobIcon MobIcon3 = MobIcon;
        if (MobIcon3 != MobIcon2) {
            ModPacketHandler.PACKET_ACCESS.byteToServerPacket(MobIcon3.id, PacketDataIndex.BYTE_CHANGE_MORPH);
           // minecraft.player.connection.sendUnsignedCommand(MobIcon3.getCommand());
        }
    }
    public boolean sameKeyOne(KeyMapping key1, Options options){
        return (key1.isDown() || (key1.same(options.keyLoadHotbarActivator) && options.keyLoadHotbarActivator.isDown())
                || (key1.same(options.keySaveHotbarActivator) && options.keySaveHotbarActivator.isDown())
        );
    }
    public boolean sameKeyOneX(KeyMapping key1, Options options){
        return (InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)key1).roundabout$justTellMeTheKey().getValue())
                || (key1.same(options.keyLoadHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keyLoadHotbarActivator).roundabout$justTellMeTheKey().getValue()))
                || (key1.same(options.keySaveHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keySaveHotbarActivator).roundabout$justTellMeTheKey().getValue()))
        );
    }
    private boolean checkToClose() {
        if (minecraft != null) {
            if (!sameKeyOneX(KeyInputRegistry.abilityOneKey, this.minecraft.options)) {
                zHeld = false;
            } else {
                if (!zHeld) {
                    this.setFirstMousePos = false;
                    this.currentlyHovered = this.currentlyHovered.getNext();
                    zHeld = true;
                }
            }
            if (sameKeyOneX(KeyInputRegistry.abilityTwoKey, this.minecraft.options)) {
                this.switchToHoveredGameMode();
                this.minecraft.setScreen(null);
                return true;
            }
        }
        Options options = Minecraft.getInstance().options;
        return false;
    }
    @Override
    public boolean keyPressed(int i, int j, int k) {
        return super.keyPressed(i, j, k);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public enum MobIcon {
        PLAYER(Component.translatable("justice.morph.player"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_1.png"),ShapeShifts.PLAYER.id),
        VILLAGER(Component.translatable("justice.morph.villager"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_2.png"),ShapeShifts.VILLAGER.id),

        OVA(Component.translatable("justice.morph.ova"), new ResourceLocation(Roundabout.MOD_ID,
                        "textures/gui/icons/justice/disguise_ova.png"),ShapeShifts.OVA.id),
        ZOMBIE(Component.translatable("justice.morph.zombie"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_3.png"),ShapeShifts.ZOMBIE.id),

        SKELETON(Component.translatable("justice.morph.skeleton"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_4.png"),ShapeShifts.SKELETON.id),
        WITHER_SKELETON(Component.translatable("justice.morph.wither_skeleton"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_wither.png"),ShapeShifts.WITHER_SKELETON.id),
        STRAY(Component.translatable("justice.morph.stray"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_stray.png"),ShapeShifts.STRAY.id),
        EERIE(Component.translatable("justice.morph.eerie"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/icons/justice/disguise_eerie.png"),ShapeShifts.EERIE.id);

        protected static final JusticeMobSwitcherScreen.MobIcon[] VALUES;
        protected static final JusticeMobSwitcherScreen.MobIcon[] VALUES2;
        protected static final JusticeMobSwitcherScreen.MobIcon[] VALUES3;
        protected static final JusticeMobSwitcherScreen.MobIcon[] VALUES4;
        private static final int ICON_AREA = 16;
        protected static final int ICON_TOP_LEFT = 5;
        final Component name;
        final ResourceLocation rl;
        final byte id;

        private MobIcon(Component component, ResourceLocation rl,byte id) {
            this.name = component;
            this.rl = rl;
            this.id = id;
        }

        void drawIcon(GuiGraphics guiGraphics, int i, int j) {
            guiGraphics.blit(rl, i-1, j-1, 0, 0, 18, 18, 18, 18);
        }

        Component getName() {
            return this.name;
        }


        JusticeMobSwitcherScreen.MobIcon getNext() {
            if (JusticeMobSwitcherScreen.hasOVASkin){
                return switch (this) {
                    default -> throw new IncompatibleClassChangeError();
                    case PLAYER -> OVA;
                    case VILLAGER, OVA, EERIE -> ZOMBIE;
                    case ZOMBIE -> SKELETON;
                    case SKELETON, WITHER_SKELETON, STRAY  -> PLAYER;
                };
            } else if (JusticeMobSwitcherScreen.hasWitherSkin){
                return switch (this) {
                    default -> throw new IncompatibleClassChangeError();
                    case PLAYER -> VILLAGER;
                    case VILLAGER, OVA, EERIE  -> ZOMBIE;
                    case ZOMBIE -> WITHER_SKELETON;
                    case SKELETON, WITHER_SKELETON, STRAY  -> PLAYER;
                };
            } else if (JusticeMobSwitcherScreen.hasStraySkin){
                return switch (this) {
                    default -> throw new IncompatibleClassChangeError();
                    case PLAYER -> VILLAGER;
                    case VILLAGER, OVA, EERIE  -> ZOMBIE;
                    case ZOMBIE -> STRAY;
                    case SKELETON, WITHER_SKELETON, STRAY  -> PLAYER;
                };
            }
                return switch (this) {
                default -> throw new IncompatibleClassChangeError();
                case PLAYER -> VILLAGER;
                case OVA, VILLAGER, EERIE  -> ZOMBIE;
                case ZOMBIE -> SKELETON;
                case SKELETON, WITHER_SKELETON, STRAY -> PLAYER;
                };
        }
        static JusticeMobSwitcherScreen.MobIcon getFromGameType(ShapeShifts shift) {
            return switch (shift) {
                default -> throw new IncompatibleClassChangeError();
                case PLAYER -> PLAYER;
                case VILLAGER -> VILLAGER;
                case OVA -> OVA;
                case ZOMBIE -> ZOMBIE;
                case SKELETON -> SKELETON;
                case WITHER_SKELETON -> WITHER_SKELETON;
                case STRAY -> STRAY;
                case EERIE -> EERIE;
            };
        }

        static {
            VALUES = new MobIcon[]{PLAYER,VILLAGER,ZOMBIE,SKELETON};
            VALUES2 = new MobIcon[]{PLAYER,OVA,ZOMBIE,SKELETON};
            VALUES3 = new MobIcon[]{PLAYER,VILLAGER,ZOMBIE, WITHER_SKELETON};
            VALUES4 = new MobIcon[]{PLAYER,EERIE,ZOMBIE, STRAY};
        }
    }

    public class MobSlot
            extends AbstractWidget {
        final JusticeMobSwitcherScreen.MobIcon icon;
        private boolean isSelected;

        public MobSlot(JusticeMobSwitcherScreen.MobIcon MobIcon, int i, int j) {
            super(i, j, 26, 26, MobIcon.getName());
            this.icon = MobIcon;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            this.drawSlot(guiGraphics);
            this.icon.drawIcon(guiGraphics, this.getX() + 5, this.getY() + 5);
            if (this.isSelected) {
                this.drawSelection(guiGraphics);
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return super.isHoveredOrFocused() || this.isSelected;
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }

        private void drawSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX(), this.getY(), 0.0f, 75.0f, 26, 26, 128, 128);
        }

        private void drawSelection(GuiGraphics guiGraphics) {
            guiGraphics.blit(MOB_SWITCHER_LOCATION, this.getX(), this.getY(), 26.0f, 75.0f, 26, 26, 128, 128);
        }
    }
}