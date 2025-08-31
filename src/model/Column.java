package model;

public class Column {
    private int id;
    private int boardId;
    private String name;
    private int order;
    private ColumnType type;

    public Column() {}

    public Column(int id, int boardId, String name, int order, ColumnType type) {
        this.id = id;
        this.boardId = boardId;
        this.name = name;
        this.order = order;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBoardId() { return boardId; }
    public void setBoardId(int boardId) { this.boardId = boardId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }

    public ColumnType getType() { return type; }
    public void setType(ColumnType type) { this.type = type; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
