package net.hydra.jojomod.chocolatedisco.client.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.hydra.jojomod.access.IPlayerEntity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;

import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoModel;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoSlimModel;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity;

public class ChocolateDiscoFeatureRenderer
        extends RenderLayer<
        AbstractClientPlayer,
        PlayerModel<AbstractClientPlayer>
        > {

    private static final ResourceLocation[] WIDE_TEXTURES = {
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_default.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_black_white.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_p_cubed.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_battleship.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_ball.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_checkers.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_tablet.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_neopolitan.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_chocolate.png"
            )
    };

    private static final ResourceLocation[] SLIM_TEXTURES = {
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_default_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_black_white_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_p_cubed_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_battleship_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_ball_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_checkers_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_tablet_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_neopolitan_slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/chocolate_disco_chocolate_slim.png"
            )
    };

    /*
     * Self Disco textures.
     */
    private static final ResourceLocation GRID_WIDE_TEXTURE =
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide.png"
            );

    private static final ResourceLocation GRID_SLIM_TEXTURE =
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim.png"
            );

    /*
     * Normal Chocolate Disco fade.
     */
    private static final float MAX_FADE = 8.0F;

    /*
     * Self Disco grid forms over exactly 2 seconds.
     */
    private static final float GRID_FORMATION_LENGTH = 2.0F;

    /*
     * 40 frames = 2 seconds at 20 ticks per second.
     *
     * Each frame is a progressively revealed copy of the
     * Self Disco texture.
     */
    private static final int REVEAL_FRAMES = 40;

    /*
     * Generated textures are cached so we only create them
     * once for each wide/slim source texture.
     */
    private static final Map<
            ResourceLocation,
            ResourceLocation[]
            > REVEAL_TEXTURES = new HashMap<>();

    public static ChocolateDiscoModel model;

    public static ChocolateDiscoSlimModel slimModel;

    /*
     * Existing Chocolate Disco animation state.
     */
    private final Map<UUID, Float> animationTimers =
            new HashMap<>();

    private final Map<UUID, Byte> lastAnimations =
            new HashMap<>();

    /*
     * Self Disco animation state.
     */
    private final Map<UUID, Float> gridAnimationTimers =
            new HashMap<>();

    private final Map<UUID, Boolean> lastGridStates =
            new HashMap<>();

    private static final ResourceLocation[] WIDE_GRID_TEXTURES = {
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide1.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide2.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide3.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide4.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide5.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide6.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide7.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-wide8.png"
            )
    };

    private static final ResourceLocation[] SLIM_GRID_TEXTURES = {
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim1.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim2.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim3.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim4.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim5.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim6.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim7.png"
            ),
            new ResourceLocation(
                    "roundabout",
                    "textures/entity/self-disco-grid-slim8.png"
            )
    };

    public ChocolateDiscoFeatureRenderer(
            LivingEntityRenderer<
                    AbstractClientPlayer,
                    PlayerModel<AbstractClientPlayer>
                    > renderer,
            EntityRendererProvider.Context context
    ) {
        super(renderer);

        model =
                new ChocolateDiscoModel(
                        context.bakeLayer(
                                net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient
                                        .CHOCOLATE_DISCO_LAYER
                        )
                );

        slimModel =
                new ChocolateDiscoSlimModel(
                        context.bakeLayer(
                                net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient
                                        .CHOCOLATE_DISCO_SLIM_LAYER
                        )
                );
    }

    public ChocolateDiscoFeatureRenderer(
            LivingEntityRenderer<
                    AbstractClientPlayer,
                    PlayerModel<AbstractClientPlayer>
                    > renderer,
            EntityModelSet modelSet
    ) {
        super(renderer);
        model = new ChocolateDiscoModel(
                modelSet.bakeLayer(ChocolateDiscoClient.CHOCOLATE_DISCO_LAYER)
        );
        slimModel = new ChocolateDiscoSlimModel(
                modelSet.bakeLayer(ChocolateDiscoClient.CHOCOLATE_DISCO_SLIM_LAYER)
        );
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        ChocolateDiscoEntity stand =
                findChocolateDisco(player);

        UUID playerId =
                player.getUUID();

        /*
         * ============================================================
         * SELF DISCO
         * ============================================================
         */

        boolean gridActive =
                net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState
                        .isSelfDiscoActive(playerId);

        Boolean lastGridState =
                lastGridStates.get(playerId);

        if (gridActive) {

            /*
             * Start the grid timer when Self Disco first activates.
             */
            if (
                    lastGridState == null
                            || !lastGridState
            ) {

                gridAnimationTimers.put(
                        playerId,
                        0.0F
                );
            }

            float gridTimer =
                    gridAnimationTimers.getOrDefault(
                            playerId,
                            0.0F
                    );

            /*
             * Advance at exactly 20 ticks per second.
             */
            gridTimer +=
                    1.0F / 20.0F;

            gridTimer =
                    Math.min(
                            GRID_FORMATION_LENGTH,
                            gridTimer
                    );

            gridAnimationTimers.put(
                    playerId,
                    gridTimer
            );

            lastGridStates.put(
                    playerId,
                    true
            );

        } else {

            /*
             * Remove the timer once Self Disco ends.
             */
            gridAnimationTimers.remove(
                    playerId
            );

            lastGridStates.remove(
                    playerId
            );
        }

        /*
         * Render the Self Disco grid.
         */
        if (gridActive) {

            boolean slim =
                    player.getModelName()
                            .equals("slim");

            int skin = stand != null ? stand.getSkin() : 0;

            if (skin < 0 || skin >= 9) {
                skin = 0;
            }

            ResourceLocation originalGridTexture =
                    slim
                            ? SLIM_GRID_TEXTURES[skin]
                            : WIDE_GRID_TEXTURES[skin];

            float gridTimer =
                    gridAnimationTimers.getOrDefault(
                            playerId,
                            0.0F
                    );

            float revealProgress =
                    Math.min(
                            1.0F,
                            gridTimer
                                    / GRID_FORMATION_LENGTH
                    );

            /*
             * Convert the 0 -> 1 animation progress into one
             * of our 40 generated texture frames.
             */
            int revealFrame =
                    (int) (
                            revealProgress
                                    * REVEAL_FRAMES
                    );

            revealFrame =
                    Math.max(
                            0,
                            Math.min(
                                    REVEAL_FRAMES,
                                    revealFrame
                            )
                    );

            ResourceLocation gridTexture =
                    getRevealTexture(
                            originalGridTexture,
                            revealFrame
                    );

            poseStack.pushPose();

            /*
             * Render using the actual PlayerModel.
             *
             * This is important because the texture is mapped
             * using the exact same model geometry as the player.
             */
            this.getParentModel().renderToBuffer(
                    poseStack,
                    buffer.getBuffer(
                            RenderType.entityTranslucent(
                                    gridTexture
                            )
                    ),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                    1.0F
            );

            poseStack.popPose();
        }

        /*
         * ============================================================
         * NORMAL CHOCOLATE DISCO WRIST RENDERING
         * ============================================================
         */

        if (stand == null) {
            return;
        }

        byte animation =
                stand.getAnimation();

        if (animation == 1) {

            Byte lastAnimation =
                    lastAnimations.get(
                            playerId
                    );

            if (
                    lastAnimation == null
                            || lastAnimation != 1
            ) {

                animationTimers.put(
                        playerId,
                        0.0F
                );
            }

            float timer =
                    animationTimers.getOrDefault(
                            playerId,
                            0.0F
                    );

            timer +=
                    1.0F / 20.0F;

            animationTimers.put(
                    playerId,
                    timer
            );

            lastAnimations.put(
                    playerId,
                    animation
            );

        } else {

            animationTimers.remove(
                    playerId
            );

            lastAnimations.remove(
                    playerId
            );
        }

        float fadeOut =
                stand.getFadeOut();

        float alpha =
                (
                        (
                                (fadeOut + partialTick)
                                        / MAX_FADE
                        )
                                * 1.3F
                )
                        - 0.3F;

        alpha =
                Math.max(
                        0.0F,
                        Math.min(
                                1.0F,
                                alpha
                        )
                );

        alpha =
                smoothStep(
                        alpha
                );

        if (alpha <= 0.0F) {
            return;
        }

        boolean slim =
                player.getModelName()
                        .equals("slim");

        byte skinId =
                ((IPlayerEntity) player)
                        .roundabout$getStandSkin();

        int skinIndex =
                Math.max(
                        0,
                        Math.min(
                                skinId,
                                (byte) 8
                        )
                );

        ResourceLocation activeTexture =
                slim
                        ? SLIM_TEXTURES[skinIndex]
                        : WIDE_TEXTURES[skinIndex];

        float openProgress =
                net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState
                        .getProgress(playerId);

        poseStack.pushPose();

        this.getParentModel()
                .leftArm
                .translateAndRotate(
                        poseStack
                );

        poseStack.translate(
                0.15,
                0.6F,
                0.15F
        );

        if (slim) {

            slimModel.setAlpha(alpha);

            slimModel.setOpenProgress(
                    openProgress
            );

            slimModel.renderToBuffer(
                    poseStack,
                    buffer.getBuffer(
                            RenderType.entityTranslucent(
                                    activeTexture
                            )
                    ),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                    alpha
            );

        } else {

            model.setAlpha(alpha);

            model.setOpenProgress(
                    openProgress
            );

            model.renderToBuffer(
                    poseStack,
                    buffer.getBuffer(
                            RenderType.entityTranslucent(
                                    activeTexture
                            )
                    ),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                    alpha
            );
        }

        poseStack.popPose();
    }

    /*
     * ================================================================
     * SELF DISCO REVEAL TEXTURE
     * ================================================================
     */

    private static ResourceLocation getRevealTexture(
            ResourceLocation original,
            int frame
    ) {

        ResourceLocation[] textures =
                REVEAL_TEXTURES.get(
                        original
                );

        if (textures == null) {

            textures =
                    createRevealTextures(
                            original
                    );

            REVEAL_TEXTURES.put(
                    original,
                    textures
            );
        }

        frame =
                Math.max(
                        0,
                        Math.min(
                                REVEAL_FRAMES,
                                frame
                        )
                );

        return textures[frame];
    }

    /*
     * Creates the 40 progressively revealed versions
     * of the original Self Disco texture.
     *
     * IMPORTANT:
     *
     * Minecraft texture Y coordinates start at the top.
     *
     * We therefore reveal pixels starting from the bottom
     * of the texture and move upward toward the head.
     */
    private static ResourceLocation[] createRevealTextures(
            ResourceLocation original
    ) {

        ResourceLocation[] textures =
                new ResourceLocation[
                        REVEAL_FRAMES + 1
                        ];

        try {

            net.minecraft.server.packs.resources.Resource resource =
                    Minecraft.getInstance()
                            .getResourceManager()
                            .getResource(
                                    original
                            )
                            .orElseThrow();

            NativeImage source =
                    NativeImage.read(
                            resource.open()
                    );

            for (
                    int frame = 0;
                    frame <= REVEAL_FRAMES;
                    frame++
            ) {

                NativeImage reveal =
                        new NativeImage(
                                source.getWidth(),
                                source.getHeight(),
                                true
                        );

                float progress =
                        (float) frame
                                / (float) REVEAL_FRAMES;

                /*
                 * At progress 0:
                 *
                 * revealY = bottom of texture
                 *
                 * At progress 1:
                 *
                 * revealY = top of texture
                 */
                int revealY =
                        (int) (
                                (source.getHeight() - 1)
                                        * (1.0F - progress)
                        );

                for (
                        int y = 0;
                        y < source.getHeight();
                        y++
                ) {

                    for (
                            int x = 0;
                            x < source.getWidth();
                            x++
                    ) {

                        /*
                         * Keep pixels below the reveal line.
                         */
                        if (y >= revealY) {

                            reveal.setPixelRGBA(
                                    x,
                                    y,
                                    source.getPixelRGBA(
                                            x,
                                            y
                                    )
                            );

                        } else {

                            /*
                             * Everything above the reveal
                             * line is transparent.
                             */
                            reveal.setPixelRGBA(
                                    x,
                                    y,
                                    0
                            );
                        }
                    }
                }

                ResourceLocation revealLocation =
                        new ResourceLocation(
                                "roundabout",
                                "self_disco_reveal/"
                                        + original.getPath()
                                        .replace(
                                                "textures/entity/",
                                                ""
                                        )
                                        + "_"
                                        + frame
                                        + ".png"
                        );

                Minecraft.getInstance()
                        .getTextureManager()
                        .register(
                                revealLocation,
                                new DynamicTexture(
                                        reveal
                                )
                        );

                textures[frame] =
                        revealLocation;
            }

            source.close();

        } catch (Exception exception) {

            exception.printStackTrace();

            /*
             * If texture generation fails, fall back to
             * the original texture instead of crashing.
             */
            for (
                    int frame = 0;
                    frame <= REVEAL_FRAMES;
                    frame++
            ) {

                textures[frame] =
                        original;
            }
        }

        return textures;
    }

    /*
     * ================================================================
     * SMOOTH STEP
     * ================================================================
     */

    private static float smoothStep(
            float progress
    ) {

        progress =
                Math.max(
                        0.0F,
                        Math.min(
                                1.0F,
                                progress
                        )
                );

        return progress
                * progress
                * (
                3.0F
                        - 2.0F * progress
        );
    }

    /*
     * ================================================================
     * FIND CHOCOLATE DISCO
     * ================================================================
     */

    private ChocolateDiscoEntity findChocolateDisco(
            AbstractClientPlayer player
    ) {

        for (
                net.minecraft.world.entity.Entity entity :
                player.level().getEntities(
                        player,
                        player.getBoundingBox()
                                .inflate(2.0D)
                )
        ) {

            if (
                    entity instanceof ChocolateDiscoEntity chocolateDisco
                            && chocolateDisco.getUser() == player
            ) {

                return chocolateDisco;
            }
        }

        return null;
    }
}