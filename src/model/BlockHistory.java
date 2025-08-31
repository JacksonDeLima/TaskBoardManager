package model;

import java.time.LocalDateTime;

public class BlockHistory {
    private int id;
    private int cardId;
    private String action;
    private String reason;
    private LocalDateTime actionTime;

    public BlockHistory() {}

    public BlockHistory(int id, int cardId, String action, String reason, LocalDateTime actionTime) {
        this.id = id;
        this.cardId = cardId;
        this.action = action;
        this.reason = reason;
        this.actionTime = actionTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }
}
