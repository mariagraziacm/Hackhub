package it.unicam.hackhub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    private String id; 
    
    private String hackathonId; 
    private String teamId; 
    
    private int score;
    
    @Lob
    private String comment;
    
    private boolean evaluated;
    private String title;
    
    @Lob
    private String description;

    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;

  
    public Submission() {
    }

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
    public int getScore() { return score; }
    public String getComment() { return comment; }
    public boolean isEvaluated() { return evaluated; }

    
    public void setId(String id) { this.id = id; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setScore(int score) { this.score = score; }
    public void setComment(String comment) { this.comment = comment; }
    public void setEvaluated(boolean evaluated) { this.evaluated = evaluated; }


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Submission)) return false;
        Submission that = (Submission) o;
        return id != null && id.equals(that.id);
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
            throw new IllegalStateException("Submission giÃ  valutata");
        }

        this.score = score;
        this.comment = comment;
        this.evaluated = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void ensureEvaluated() {
        if (!evaluated) {
            throw new IllegalStateException("Non valutata");
        }
    }
}