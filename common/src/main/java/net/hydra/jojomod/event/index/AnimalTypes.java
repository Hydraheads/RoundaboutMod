package net.hydra.jojomod.event.index;

public enum AnimalTypes {
    NONE((byte) 0),
    BUTTERFLY((byte) 1),
    BEE((byte) 2),
    BAT((byte) 3),
    FISH((byte) 4),
    PIRAHNA((byte) 5),
    SNAKE((byte) 6),
    FROG((byte) 7),
    FOX((byte) 8);

    public final byte id;

    private AnimalTypes(byte $$0) {
        this.id = $$0;
    }

    public static net.hydra.jojomod.event.index.AnimalTypes getAnimalTypeFromByte(byte bt){
        if (bt== BUTTERFLY.id){
            return BUTTERFLY;
        } if (bt== BEE.id){
            return BEE;
        } if (bt== BAT.id){
            return BAT;
        } if (bt== FISH.id){
            return FISH;
        } if (bt== PIRAHNA.id) {
            return PIRAHNA;
        } if (bt== SNAKE.id) {
            return SNAKE;
        } if (bt== FROG.id) {
            return FROG;
        } if (bt== FOX.id) {
            return FOX;
        }
        return NONE;
    }
}
