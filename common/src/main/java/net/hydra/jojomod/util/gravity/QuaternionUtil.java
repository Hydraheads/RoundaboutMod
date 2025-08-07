package net.hydra.jojomod.util.gravity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

//https://github.com/qouteall/GravityChanger/tree/1.20.1-Fabric/src/main/java/gravity_changer/util
//credit to quoteall

public abstract class QuaternionUtil {
    public static Quaternionf getViewRotation(float pitch, float yaw) {
        Quaternionf r1 = new Quaternionf().fromAxisAngleDeg(new Vector3f(1, 0, 0), pitch);
        Quaternionf r2 = new Quaternionf().fromAxisAngleDeg(new Vector3f(0, 1, 0), yaw + 180);
        r1.mul(r2);
        return r1;
    }

    // NOTE the "from" and "to" cannot be opposite
    public static Quaternionf getRotationBetween(Vec3 from, Vec3 to) {
        from = from.normalize();
        to = to.normalize();
        Vec3 axis = from.cross(to).normalize();
        double cos = from.dot(to);
        double angle = Math.acos(cos);
        return new Quaternionf().fromAxisAngleRad(
                new Vector3f((float) axis.x, (float) axis.y, (float) axis.z),
                (float) angle
        );
    }

    // using mutable objects could easily cause bugs if forget to copy
    public static Vec3 rotate(Vec3 vec, Quaternionf quaternionf) {
        Vector3f vector3f = vec.toVector3f();
        vector3f.rotate(quaternionf);
        return new Vec3(vector3f);
    }
}