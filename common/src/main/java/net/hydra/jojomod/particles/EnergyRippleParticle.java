package net.hydra.jojomod.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EnergyRippleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private boolean inverted;
    private boolean pullCam;

    public EnergyRippleParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, 0.0, 0.0, 0.0);
        this.sprites = spriteSet;
        this.age = 0;
        this.quadSize = 0.7F;
        this.lifetime = 7;
        //set count to 0 if you want to toggle these
        this.inverted = (g == 1);
        this.pullCam = (h == 1);

        this.setSprite(this.sprites.get(0, 7));
    }

    @Override
    public void render(VertexConsumer $$0, Camera $$1, float $$2) {
        Vec3 $$3 = $$1.getPosition();
        float $$4 = (float)(Mth.lerp((double)$$2, this.xo, this.x) - $$3.x());
        float $$5 = (float)(Mth.lerp((double)$$2, this.yo, this.y) - $$3.y());
        float $$6 = (float)(Mth.lerp((double)$$2, this.zo, this.z) - $$3.z());
        // need to offset the particle so that it renders over entities and doesn't sink into them
        if(this.pullCam) {
            float dist = Mth.sqrt($$4 * $$4 + $$5 * $$5 + $$6 * $$6);
            if (dist > 0.4F) {
                float bias = 0.35F;
                $$4 -= ($$4 / dist) * bias;
                $$5 -= ($$5 / dist) * bias;
                $$6 -= ($$6 / dist) * bias;
            }
        }
        Quaternionf $$7;
        if (this.roll == 0.0F) {
            $$7 = $$1.rotation();
        } else {
            $$7 = new Quaternionf($$1.rotation());
            $$7.rotateZ(Mth.lerp($$2, this.oRoll, this.roll));
        }

        Vector3f[] $$9 = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float $$10 = this.getQuadSize($$2);

        for (int $$11 = 0; $$11 < 4; $$11++) {
            Vector3f $$12 = $$9[$$11];
            $$12.rotate($$7);
            $$12.mul($$10);
            $$12.add($$4, $$5, $$6);
        }

        float $$13 = this.getU0();
        float $$14 = this.getU1();
        float $$15 = this.getV0();
        float $$16 = this.getV1();
        int $$17 = this.getLightColor($$2);

        $$0.vertex((double)$$9[0].x(), (double)$$9[0].y(), (double)$$9[0].z())
                .uv($$14, $$16)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();

        $$0.vertex((double)$$9[1].x(), (double)$$9[1].y(), (double)$$9[1].z())
                .uv($$14, $$15)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();

        $$0.vertex((double)$$9[2].x(), (double)$$9[2].y(), (double)$$9[2].z())
                .uv($$13, $$15)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();

        $$0.vertex((double)$$9[3].x(), (double)$$9[3].y(), (double)$$9[3].z())
                .uv($$13, $$16)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2($$17)
                .endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU0();
    }

    @Override
    protected float getU1() {
        return this.sprite.getU1();
    }

    @Override
    protected float getV0() {
        return this.sprite.getV0();
    }

    @Override
    protected float getV1() {
        return this.sprite.getV1();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        if (this.inverted) {
            int frame = Math.max(0, 6 - (this.age * 7 / this.lifetime));
            this.setSprite(this.sprites.get(frame, 6));
        } else {
            int frame = Math.min(6, (this.age * 7 / this.lifetime));
            this.setSprite(this.sprites.get(frame, 6));
        }
        this.alpha = Math.max(0.10F, this.alpha - 0.15F);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new EnergyRippleParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}
