package prisongame.prisongame.gangs;

public enum GangRole {
    NONE,
    MEMBER,
    OFFICIAL,
    OWNER;

    public boolean isAtLeast(GangRole role) {
        return ordinal() >= role.ordinal();
    }
}
