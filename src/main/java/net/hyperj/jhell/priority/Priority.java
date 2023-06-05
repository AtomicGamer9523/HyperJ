package net.hyperj.jhell.priority;

public enum Priority {
    HIGHEST,
    HIGH,
    MEDIUM,
    LOW,
    LOWEST;
    public static final Priority DEFAULT = Priority.LOWEST;
    public int anInt() {
        if(this == HIGHEST) return 100;
        if(this == HIGH) return 50;
        if(this == MEDIUM) return 25;
        if(this == LOW) return 10;
        return 5;
    }
}
