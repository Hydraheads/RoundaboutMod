package net.hydra.jojomod.event.index;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

public enum ShapeShifts {
    PLAYER((byte) 0),
    VILLAGER((byte) 1),
    OVA((byte) 2),
    ZOMBIE((byte) 3),
    SKELETON((byte) 4),
    WITHER_SKELETON((byte) 5),
    STRAY((byte) 6);

    public final byte id;
    private ShapeShifts(byte $$0) {
        this.id = $$0;
    }

    public static ShapeShifts getShiftFromByte(byte bt){
        if (bt== VILLAGER.id){
            return VILLAGER;
        } if (bt== OVA.id){
            return OVA;
        } if (bt== ZOMBIE.id){
            return ZOMBIE;
        } if (bt== SKELETON.id){
            return SKELETON;
        } if (bt== WITHER_SKELETON.id){
            return WITHER_SKELETON;
        } if (bt== STRAY.id){
            return STRAY;
        }
        return PLAYER;
    }

    public static boolean isSkeleton(ShapeShifts shape){
        return shape == ShapeShifts.SKELETON || shape == ShapeShifts.WITHER_SKELETON || shape == ShapeShifts.STRAY;
    }

    public static byte getByteFromType(VillagerType VT){
        if (VT==VillagerType.PLAINS){
            return 0;
        } else if (VT==VillagerType.SWAMP){
            return 15;
        } else if (VT==VillagerType.DESERT){
            return 30;
        } else if (VT==VillagerType.SNOW){
            return 45;
        } else if (VT==VillagerType.JUNGLE){
            return 60;
        } else if (VT==VillagerType.SAVANNA){
            return 75;
        } else if (VT==VillagerType.TAIGA){
            return 90;
        }
        return 0;
    }

    public static VillagerType getTypeFromByte(byte bt){
        if (bt >= 90){
            return VillagerType.TAIGA;
        } else if (bt >= 75){
            return VillagerType.SAVANNA;
        } else if (bt >= 60){
            return VillagerType.JUNGLE;
        } else if (bt >= 45){
            return VillagerType.SNOW;
        } else if (bt >= 30){
            return VillagerType.DESERT;
        } else if (bt >= 15){
            return VillagerType.SWAMP;
        }
        return VillagerType.PLAINS;
    }
    public static byte getByteFromProfession(VillagerProfession VP){
        if (VP==VillagerProfession.NONE){
            return 0;
        } else if (VP==VillagerProfession.NITWIT){
            return 1;
        } else if (VP==VillagerProfession.ARMORER){
            return 2;
        } else if (VP==VillagerProfession.BUTCHER){
            return 3;
        } else if (VP==VillagerProfession.CLERIC){
            return 4;
        } else if (VP==VillagerProfession.CARTOGRAPHER){
            return 5;
        } else if (VP==VillagerProfession.FARMER){
            return 6;
        } else if (VP==VillagerProfession.FISHERMAN){
            return 7;
        } else if (VP==VillagerProfession.FLETCHER){
            return 8;
        } else if (VP==VillagerProfession.LEATHERWORKER){
            return 9;
        } else if (VP==VillagerProfession.LIBRARIAN){
            return 10;
        } else if (VP==VillagerProfession.MASON){
            return 11;
        } else if (VP==VillagerProfession.SHEPHERD){
            return 12;
        } else if (VP==VillagerProfession.TOOLSMITH){
            return 12;
        } else if (VP==VillagerProfession.WEAPONSMITH){
            return 12;
        }
        return 1;
    }
    public static VillagerProfession getProfessionFromByte(byte bt){
        int profnum = bt %15;
        if (profnum == 0){
            return VillagerProfession.NONE;
        } else if (profnum == 1){
            return VillagerProfession.NITWIT;
        } else if (profnum == 2){
            return VillagerProfession.ARMORER;
        } else if (profnum == 3){
            return VillagerProfession.BUTCHER;
        } else if (profnum == 4){
            return VillagerProfession.CLERIC;
        } else if (profnum == 5){
            return VillagerProfession.CARTOGRAPHER;
        } else if (profnum == 6){
            return VillagerProfession.FARMER;
        } else if (profnum == 7){
            return VillagerProfession.FISHERMAN;
        } else if (profnum == 8){
            return VillagerProfession.FLETCHER;
        } else if (profnum == 9){
            return VillagerProfession.LEATHERWORKER;
        } else if (profnum == 10){
            return VillagerProfession.LIBRARIAN;
        } else if (profnum == 11){
            return VillagerProfession.MASON;
        } else if (profnum == 12){
            return VillagerProfession.SHEPHERD;
        } else if (profnum == 13){
            return VillagerProfession.TOOLSMITH;
        } else if (profnum == 14){
            return VillagerProfession.WEAPONSMITH;
        } else {
            return VillagerProfession.NITWIT;
        }
    }
}
