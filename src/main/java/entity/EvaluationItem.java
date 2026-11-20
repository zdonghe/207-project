package entity;

/** A single category result inside an evaluation. */
public final class EvaluationItem {
  private final String name;
  private final String category;
  private final Integer score;
  private final double weight;
  private final String description;

  /** Creates an evaluation item. A null score means the category was not applicable. */
  public EvaluationItem(
      String name, String category, Integer score, double weight, String description) {
    this.name = name == null ? "" : name.trim();
    this.category = category == null ? "" : category.trim();
    this.score = score;
    this.weight = weight;
    this.description = description == null ? "" : description.trim();
  }

  /** Returns whether this item has a numeric score from 0 to 3. */
  public boolean isApplicable() {
    return score != null && score >= 0 && score <= 3;
  }

  /** Returns whether the item is a deficiency, meaning a score of 0 or 1. */
  public boolean isDeficiency() {
    return isApplicable() && score <= 1;
  }

  /** Returns a severity bucket derived from the score, or UNKNOWN when not applicable. */
  public Severity getSeverity() {
    if (!isApplicable()) {
      return Severity.UNKNOWN;
    }
    if (score == 0) {
      return Severity.HIGH;
    }
    if (score == 1) {
      return Severity.MEDIUM;
    }
    return Severity.LOW;
  }

  /**
   * Returns the score normalized to the range 0.0 to 1.0.
   *
   * <p>Returns {@link Double#NaN} when the item is not applicable. Callers should check
   * {@link #isApplicable()} before using the result in arithmetic.
   */
  public double getNormalizedScore() {
    if (!isApplicable()) {
      return Double.NaN;
    }
    return score / 3.0;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public Integer getScore() {
    return score;
  }

  public double getWeight() {
    return weight;
  }

  public String getDescription() {
    return description;
  }
}
