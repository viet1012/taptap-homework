package mapping;

public enum ColumnMapping {
    DIRECTION(0),
    CARD_ID(1),
    TIMESTAMP(2),
    BUS_ID(3),
    TERMINAL_ID(4);

    private int prefix;

    ColumnMapping(int prefix) {
        this.prefix = prefix;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }
}
