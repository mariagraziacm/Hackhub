package it.unicam.hackhub.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Submission {
    private final String id;
    private final String hackathonId;
    private final String teamId;
    private int score;
    private String comment;
    private boolean evaluated;

    private String title;
    private String description;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Submission(String id,
                      String hackathonId,
                      String teamId,
                      String title,
                      String description) {
        this.id = requireNotBlank(id, "id obbligatorio");
        this.hackathonId = requireNotBlank(hackathonId, "hackathonId obbligatorio");
        this.teamId = requireNotBlank(teamId, "teamId obbligatorio");

        this.title = requireNotBlank(title, "title obbligatorio");
        this.description = requireNotBlank(description, "description obbligatoria");

        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public String getId() { return id; }
    public String getHackathonId() { return hackathonId; }
    public String getTeamId() { return teamId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void update(String title, String description) {
        this.title = requireNotBlank(title, "title obbligatorio");
        this.description = requireNotBlank(description, "description obbligatoria");
        this.updatedAt = LocalDateTime.now();
    }

    private String requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
    public void setScore(int score) {
    this.score = score;
}

public void setComment(String comment) {
    this.comment = comment;
}

public void setEvaluated(boolean evaluated) {
    this.evaluated = evaluated;
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Submission)) return false;
        Submission that = (Submission) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void rate(int score, String comment) {
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score non valido");
        }

        if (this.evaluated) {
            throw new IllegalStateException("Submission già valutata");
        }

        this.score = score;
        this.comment = comment;
        this.evaluated = true;
        this.updatedAt = LocalDateTime.now();
    }
    public boolean isEvaluated() {
        return evaluated;
    }
    public void ensureEvaluated() {
        if (!evaluated) {
            throw new IllegalStateException("Non valutata");
        }
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}