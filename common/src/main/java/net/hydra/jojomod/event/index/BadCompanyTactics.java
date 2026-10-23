package net.hydra.jojomod.event.index;


public enum BadCompanyTactics {
    NONE((byte) 0),
    RETURN((byte) 1),
    STOP((byte) 2),
    ADVANCE((byte) 3),
    FIRE((byte) 4),
    WARD((byte) 5);


    public final byte id;

    private BadCompanyTactics(byte $$0) {
        this.id = $$0;
    }


}
