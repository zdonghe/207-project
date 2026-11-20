package cs.toronto.edu.csc207.rentsafe.entity;

import java.time.LocalDateTime;
import java.util.Optional;

/** A building saved to the tenant's shortlist, with an optional note. */
public final class Favourite {
  private final Building building;
  private final Integer savedEvaluationYear;
  private String note;
  private final LocalDateTime addedAt;

  /** Creates a favourite, recording the latest evaluation year at save time. */
  public Favourite(Building building, String note, LocalDateTime addedAt) {
    this(building, note, addedAt, latestYear(building));
  }

  /** Creates a favourite with an explicit evaluation snapshot year loaded from storage. */
  public Favourite(
      Building building, String note, LocalDateTime addedAt, Integer savedEvaluationYear) {
    if (building == null) {
      throw new IllegalArgumentException("A favourite requires a building.");
    }
    this.building = building;
    this.note = note == null ? "" : note.trim();
    this.addedAt = addedAt == null ? LocalDateTime.now() : addedAt;
    this.savedEvaluationYear = savedEvaluationYear;
  }

  /** Replaces the tenant's note. */
  public void updateNote(String newNote) {
    this.note = newNote == null ? "" : newNote.trim();
  }

  /** Returns whether a newer evaluation exists than the one saved with this favourite. */
  public boolean isStale() {
    Optional<Evaluation> latest = building.getLatestEvaluation();
    if (latest.isEmpty()) {
      return false;
    }
    return savedEvaluationYear == null || latest.get().getYear() > savedEvaluationYear;
  }

  public String getRsn() {
    return building.getRsn();
  }

  public String getNote() {
    return note;
  }

  public LocalDateTime getAddedAt() {
    return addedAt;
  }

  public Building getBuilding() {
    return building;
  }

  public Integer getSavedEvaluationYear() {
    return savedEvaluationYear;
  }

  private static Integer latestYear(Building building) {
    if (building == null) {
      return null;
    }
    return building.getLatestEvaluation().map(Evaluation::getYear).orElse(null);
  }
}
