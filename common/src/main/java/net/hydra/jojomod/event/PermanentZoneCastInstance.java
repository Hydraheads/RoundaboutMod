package net.hydra.jojomod.event;

public class PermanentZoneCastInstance {

    /**
     * The id component of the user.
     */
    public static byte
            FOG_FIELD = 1,
            FIRESTORM = 2,
            MOLD_FIELD = 3;
    /**
     * The id component of the user.
     */

    public int id;

    /**
     * The x component of the vector.
     */
    public double x;
    /**
     * The y component of the vector.
     */
    public double y;
    /**
     * The z component of the vector.
     */
    public double z;
    /**
     * The range component of the vector.
     */
    public double range;
    public byte context;


    public PermanentZoneCastInstance(int id, double x, double y, double z, double range,byte context){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
        this.context = context;
    }

}
