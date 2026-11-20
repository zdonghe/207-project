package cs.toronto.edu.csc207.rentsafe.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;

/** One RentSafeTO evaluation of a building, with its category-level results. */
public final class Evaluation {
  private final String rsn;
  private final int year;
  private final LocalDate completedOn;
  private final Integer currentScore;
  private final Integer proactiveScore;
  private final Integer reactiveScore;
  private final List<EvaluationItem> items;

  /** Creates an evaluation. Missing scores are represented by null. */
  public Evaluation(
      String rsn,
      int year,
      LocalDate completedOn,
      Integer currentScore,
      Integer proactiveScore,
      Integer reactiveScore,
      List<EvaluationItem> items) {
    this.rsn = rsn == null ? "" : rsn.trim();
    this.year = year;
    this.completedOn = completedOn;
    this.currentScore = currentScore;
    this.proactiveScore = proactiveScore;
    this.reactiveScore = reactiveScore;
    this.items = items == null ? List.of() : List.copyOf(items);
  }

  /** Returns the category items scored 0 or 1. */
  public List<EvaluationItem> getDeficiencies() {
    return items.stream().filter(EvaluationItem::isDeficiency).toList();
  }

  /** Returns the score for an item or category name, ignoring case. */
  public OptionalInt getScoreFor(String category) {
    if (category == null || category.isBlank()) {
      return OptionalInt.empty();
    }
    String needle = category.trim().toLowerCase(Locale.ROOT);
    return items.stream()
        .filter(item -> item.isApplicable())
        .filter(item -> item.getName().toLowerCase(Locale.ROOT).equals(needle)
            || item.getCategory().toLowerCase(Locale.ROOT).equals(needle))
        .mapToInt(EvaluationItem::getScore)
        .findFirst();
  }

  /** Returns whether the current score is higher than the previous evaluation's score. */
  public boolean isImprovementOver(Evaluation previous) {
    if (previous == null || currentScore == null || previous.currentScore == null) {
      return false;
    }
    return currentScore > previous.currentScore;
  }

  /** Returns the category results that changed compared with the previous evaluation. */
  public List<EvaluationChange> getChangedItems(Evaluation previous) {
    List<EvaluationChange> changes = new ArrayList<>();
    if (previous == null) {
      return changes;
    }
    for (EvaluationItem item : items) {
      if (!item.isApplicable()) {
        continue;
      }
      OptionalInt previousScore = previous.getScoreFor(item.getName());
      if (previousScore.isPresent() && previousScore.getAsInt() != item.getScore()) {
        changes.add(
            new EvaluationChange(item.getName(), previousScore.getAsInt(), item.getScore()));
      }
    }
    return changes;
  }

  /** Returns a short human-readable summary of the evaluation. */
  public String getSummary() {
    String completed = completedOn == null ? "date not recorded" : completedOn.toString();
    return "Evaluation " + year + " (" + completed + "): current score "
        + display(currentScore) + ", " + getDeficiencies().size()
        + " deficiency item(s).";
  }

  public String getRsn() {
    return rsn;
  }

  public int getYear() {
    return year;
  }

  public LocalDate getCompletedOn() {
    return completedOn;
  }

  public Integer getCurrentScore() {
    return currentScore;
  }

  public Integer getProactiveScore() {
    return proactiveScore;
  }

  public Integer getReactiveScore() {
    return reactiveScore;
  }

  public List<EvaluationItem> getItems() {
    return items;
  }

  private static String display(Integer value) {
    return value == null ? "N/A" : value + "/100";
  }
}
