package model;

import java.time.LocalDateTime;

public class CardMovement {
    private int id;
    private int cardId;
    private int fromColumnId;
    private int toColumnId;
    private LocalDateTime movedAt;

    public CardMovement() {}

    public CardMovement(int id, int cardId, int fromColumnId, int toColumnId, LocalDateTime movedAt) {
        this.id = id;
        this.cardId = cardId;
        this.fromColumnId = fromColumnId;
        this.toColumnId = toColumnId;
        this.movedAt = movedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }

    public int getFromColumnId() { return fromColumnId; }
    public void setFromColumnId(int fromColumnId) { this.fromColumnId = fromColumnId; }

    public int getToColumnId() { return toColumnId; }
    public void setToColumnId(int toColumnId) { this.toColumnId = toColumnId; }

    public LocalDateTime getMovedAt() { return movedAt; }
    public void setMovedAt(LocalDateTime movedAt) { this.movedAt = movedAt; }
}
