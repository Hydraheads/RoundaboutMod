package net.hydra.jojomod.event.index;

public class SoundIndex {
    /**A good way to keep track of what sound a stand is playing should its barrage
     * contain multiple noises is to assign the sounds to a byte on a per stand
     * basis. Mostly exists to cancel barrage sounds. Packets transfer bytes easily.*/
    public static final byte NO_SOUND = -1;
    public static final byte ALL_SOUNDS = 0;
    public static final byte GLAIVE_CHARGE = -2;
    public static final byte BOWLER_HAT_AIM_SOUND = -5;
    public static final byte STAND_ARROW_CHARGE = -3;
    public static final byte CACKLE = -4;
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
    public static final byte SUMMON_SOUND = 18;
    public static final byte SUMMON_SOUND_2 = 19;
    public static final byte SUMMON_SOUND_4 = 20;

    /**Sound group cancel ids, used to cancel sound events like barrages uniformly*/
    public static final byte BARRAGE_SOUND_GROUP = 100;
    public static final byte TIME_CHARGE_SOUND_GROUP = 101;
    public static final byte TIME_SOUND_GROUP = 102;
    public static final byte TIME_END_GROUP = 103;
    public static final byte ITEM_GROUP = 104;

}
