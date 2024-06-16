package net.hydra.jojomod.event;

public class TimeStopInstance {

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

    public float duration;
    public float maxDuration;


    public TimeStopInstance(int id, double x, double y, double z, double range, float duration, float maxDuration){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
        this.duration = duration;
        this.maxDuration = maxDuration;
    }
}
