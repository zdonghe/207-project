package cs.toronto.edu.csc207.rentsafe.entity;

/** A named RentSafeTO score value. */
public final class Score implements Comparable<Score> {
  private final ScoreType type;
  private final Integer value;

  /** Creates a score. A null value means the score was not published. */
  public Score(ScoreType type, Integer value) {
    this.type = type == null ? ScoreType.CURRENT : type;
    this.value = value;
  }

  /** Returns whether a score value is present. */
  public boolean isValid() {
    return value != null;
  }

  /** Returns a risk bucket derived from the score value. */
  public RiskLevel getRiskLevel() {
    if (value == null) {
      return RiskLevel.UNKNOWN;
    }
    if (value >= 80) {
      return RiskLevel.LOW;
    }
    if (value >= 60) {
      return RiskLevel.MEDIUM;
    }
    return RiskLevel.HIGH;
  }

  @Override
  public int compareTo(Score other) {
    if (other == null) {
      return -1;
    }
    if (value == null && other.value == null) {
      return 0;
    }
    if (value == null) {
      return 1;
    }
    if (other.value == null) {
      return -1;
    }
    return Integer.compare(value, other.value);
  }

  public ScoreType getType() {
    return type;
  }

  public Integer getValue() {
    return value;
  }
}
