package net.hydra.jojomod.event.index;

public class PowerIndex {
    /**Easy way to reference bytes as specific power types, for a stand
     * using one of its many abilities.*/
    public static final byte NONE = 0;
    public static final byte ATTACK = 1;
    public static final byte SNEAK_ATTACK = 2;
    public static final byte GUARD = 3;
    public static final byte BARRAGE_CHARGE = 4;
    public static final byte BARRAGE = 5;
    public static final byte BARRAGE_CLASH = 6;
    public static final byte SPECIAL = 7;
    public static final byte SPECIAL_FINISH = 8;
    public static final byte SPECIAL_CANCEL = 9;
    public static final byte SPECIAL_CHARGED = 10;
    public static final byte SPECIAL_TRACKER = 11;
    public static final byte MOVEMENT = 12;
    public static final byte SNEAK_MOVEMENT = 13;
    public static final byte CLASH_CANCEL = 14;
    public static final byte EXTRA = 15;
    public static final byte EXTRA_2 = 16;
    public static final byte FALL_BRACE_FINISH = 17;
    public static final byte EXTRA_2_FINISH = 18;
    public static final byte VAULT = 19;
    public static final byte BOUNCE = 20;
    public static final byte POWER_1 = 21;
    public static final byte POWER_1_SNEAK = 22;
    public static final byte POWER_2 = 23;
    public static final byte POWER_2_SNEAK = 24;
    public static final byte POWER_3 = 25;
    public static final byte POWER_3_SNEAK = 26;
    public static final byte POWER_4 = 27;
    public static final byte POWER_4_SNEAK = 28;
    public static final byte POWER_2_EXTRA = 29;
    public static final byte POWER_2_SNEAK_EXTRA = 30;
    public static final byte POWER_3_EXTRA = 31;
    public static final byte POWER_3_SNEAK_EXTRA = 32;
    public static final byte POWER_4_EXTRA = 33;
    public static final byte POWER_4_SNEAK_EXTRA= 52;
    public static final byte POWER_1_BONUS= 34;
    public static final byte POWER_2_BONUS= 35;
    public static final byte POWER_3_BONUS= 36;
    public static final byte POWER_4_BONUS= 37;
    public static final byte MINING = 39;
    public static final byte LEAD_IN = 40;
    public static final byte BARRAGE_CHARGE_2 = 41;
    public static final byte BARRAGE_2= 42;
    public static final byte SNEAK_ATTACK_CHARGE= 43;
    public static final byte POWER_1_BLOCK= 44;
    public static final byte POWER_2_BLOCK= 45;
    public static final byte POWER_3_BLOCK= 46;
    public static final byte POWER_4_BLOCK= 47;
    public static final byte RANGED_BARRAGE_CHARGE = 48;
    public static final byte RANGED_BARRAGE = 49;
    public static final byte RANGED_BARRAGE_CHARGE_2 = 50;
    public static final byte RANGED_BARRAGE_2 = 51;


    /**Even more basic, refers to the buttons for cooldowns*/
    public static final byte NO_CD = -1;
    public static final byte SKILL_1 = 0;
    public static final byte SKILL_2 = 1;
    public static final byte SKILL_3 = 2;
    public static final byte SKILL_4 = 3;
    public static final byte SKILL_1_SNEAK = 4;
    public static final byte SKILL_2_SNEAK = 5;
    public static final byte GLOBAL_DASH = 6;
    public static final byte SKILL_4_SNEAK = 7;
    public static final byte SKILL_EXTRA = 8;
    public static final byte SKILL_EXTRA_2 = 9;

    public static final byte FATE_1 = 10;
    public static final byte FATE_2 = 11;
    public static final byte FATE_3 = 12;
    public static final byte FATE_4 = 13;
    public static final byte FATE_1_SNEAK = 14;
    public static final byte FATE_2_SNEAK = 15;
    public static final byte FATE_3_SNEAK = 16;
    public static final byte FATE_4_SNEAK = 17;
    public static final byte FATE_EXTRA = 18;
    public static final byte FATE_EXTRA_2 = 19;

    public static final byte GENERAL_1 = 20;
    public static final byte GENERAL_2 = 21;
    public static final byte GENERAL_3 = 22;
    public static final byte GENERAL_4 = 23;
    public static final byte GENERAL_1_SNEAK = 24;
    public static final byte GENERAL_2_SNEAK = 25;
    public static final byte GENERAL_3_SNEAK = 26;
    public static final byte GENERAL_4_SNEAK = 27;
    public static final byte GENERAL_EXTRA = 28;
    public static final byte GENERAL_EXTRA_2 = 29;


    /**You can define any other number after 51 in your own stand's clas if you don't want to use
     * the old naming convention and it really doesn't matter that much as long as you are
     * keeping track of it internally**/
    public static final byte SKILL_1_GUARD = 60;
    public static final byte SKILL_2_GUARD = 61;
    public static final byte SKILL_3_GUARD = 62;
    public static final byte SKILL_4_GUARD = 63;
    public static final byte SKILL_1_CROUCH_GUARD = 70;
    public static final byte SKILL_2_CROUCH_GUARD = 71;
    public static final byte SKILL_3_CROUCH_GUARD = 72;
    public static final byte SKILL_4_CROUCH_GUARD = 73;

}
