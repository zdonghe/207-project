package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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
    return items.stream().filter(EvaluationItem::isDeficiency).collect(Collectors.toList());
  }

  /** Returns the item with the given name, ignoring case. */
  public Optional<EvaluationItem> getItem(String name) {
    if (name == null || name.isBlank()) {
      return Optional.empty();
    }
    String needle = name.trim().toLowerCase(Locale.ROOT);
    return items.stream()
        .filter(item -> item.getName().toLowerCase(Locale.ROOT).equals(needle))
        .findFirst();
  }

  /** Returns every item in the given category, ignoring case. */
  public List<EvaluationItem> getItemsInCategory(String category) {
    if (category == null || category.isBlank()) {
      return List.of();
    }
    String needle = category.trim().toLowerCase(Locale.ROOT);
    return items.stream()
        .filter(item -> item.getCategory().toLowerCase(Locale.ROOT).equals(needle))
        .collect(Collectors.toList());
  }

  /** Returns whether the current score is higher than the previous evaluation's score. */
  public boolean isImprovementOver(Evaluation previous) {
    if (previous == null || currentScore == null || previous.currentScore == null) {
      return false;
    }
    return currentScore > previous.currentScore;
  }

  /**
   * Returns the items present in both evaluations whose scores changed.
   *
   * <p>Items added in this evaluation or missing from the previous one are ignored. Comparing
   * evaluations from different buildings is rejected.
   */
  public List<EvaluationChange> getChangedItems(Evaluation previous) {
    List<EvaluationChange> changes = new ArrayList<>();
    if (previous == null) {
      return changes;
    }
    if (!previous.getRsn().isBlank() && !previous.getRsn().equals(rsn)) {
      throw new IllegalArgumentException("Cannot compare evaluations for different buildings.");
    }
    for (EvaluationItem item : items) {
      if (!item.isApplicable()) {
        continue;
      }
      Optional<EvaluationItem> previousItem = previous.getItem(item.getName());
      if (previousItem.isPresent()
          && previousItem.get().isApplicable()
          && previousItem.get().getScore().intValue() != item.getScore().intValue()) {
        changes.add(new EvaluationChange(
            item.getName(), previousItem.get().getScore(), item.getScore()));
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
