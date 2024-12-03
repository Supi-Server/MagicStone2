package net.supiserver.play.magicStone.types;

public enum Rank {
    BEGINNER("beginner"),
    BEGINNER_PLUS("beginner+"),
    NORMAL("normal"),
    NORMAL_PLUS("normal+"),
    EXPERT("expert"),
    EXPERT_PLUS("expert+"),
    MASTER("master"),
    MASTER_PLUS("master+"),
    SUPICIALIST("supicialist");

    private final String value;
    Rank(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
