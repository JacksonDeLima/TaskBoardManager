package model;

import java.time.LocalDateTime;

public class Card {
    private int id;
    private int columnId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private boolean isBlocked;
    private String blockReason;

    public Card() {}

    public Card(int id, int columnId, String title, String description, LocalDateTime createdAt, boolean isBlocked, String blockReason) {
        this.id = id;
        this.columnId = columnId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.isBlocked = isBlocked;
        this.blockReason = blockReason;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getColumnId() { return columnId; }
    public void setColumnId(int columnId) { this.columnId = columnId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public String getBlockReason() { return blockReason; }
    public void setBlockReason(String blockReason) { this.blockReason = blockReason; }

    @Override
    public String toString() {
        return title + (isBlocked ? " (BLOQUEADO)" : "");
    }
}
