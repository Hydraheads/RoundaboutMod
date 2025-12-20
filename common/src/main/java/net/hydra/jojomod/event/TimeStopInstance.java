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

    public int duration;
    public int durationInterpolation;
    public int maxDuration;


    public TimeStopInstance(int id, double x, double y, double z, double range, int duration, int maxDuration){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
        this.duration = duration;
        this.durationInterpolation = duration;
        this.maxDuration = maxDuration;
    }
}
