package net.hydra.jojomod.event.index;

public class SoundIndex {
    /**A good way to keep track of what sound a stand is playing should its barrage
     * contain multiple noises is to assign the sounds to a byte on a per stand
     * basis. Mostly exists to cancel barrage sounds. Packets transfer bytes easily.*/
    public static final byte NO_SOUND = -1;
    public static final byte ALL_SOUNDS = 0;
    public static final byte BARRAGE_CHARGE_SOUND = 1;
    public static final byte ALT_CHARGE_SOUND_1 = 2;
    public static final byte ALT_CHARGE_SOUND_2 = 3;
    public static final byte ALT_CHARGE_SOUND_3 = 4;
    public static final byte ALT_CHARGE_SOUND_4 = 5;
    public static final byte BARRAGE_CRY_SOUND = 6;
    public static final byte BARRAGE_CRY_SOUND_2 = 7;
    public static final byte BARRAGE_CRY_SOUND_3 = 8;
    public static final byte BARRAGE_CRY_SOUND_4 = 9;
    public static final byte BARRAGE_CRY_SOUND_5 = 10;
    public static final byte BARRAGE_CRY_SOUND_6 = 11;
    public static final byte BARRAGE_CRY_SOUND_7 = 12;
    public static final byte SPECIAL_MOVE_SOUND = 13;
    public static final byte SPECIAL_MOVE_SOUND_2 = 14;
    public static final byte SPECIAL_MOVE_SOUND_3 = 15;
    public static final byte SPECIAL_MOVE_SOUND_4 = 16;
    public static final byte SPECIAL_MOVE_SOUND_5 = 17;

}
