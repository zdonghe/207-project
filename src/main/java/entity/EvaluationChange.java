package entity;

/** A change to one evaluation item between two evaluations. */
public final class EvaluationChange {
  private final String itemName;
  private final int previousScore;
  private final int currentScore;

  /** Creates a change record for one category item. */
  public EvaluationChange(String itemName, int previousScore, int currentScore) {
    this.itemName = itemName == null ? "" : itemName.trim();
    this.previousScore = previousScore;
    this.currentScore = currentScore;
  }

  /** Returns the signed difference between the current and previous scores. */
  public int getDelta() {
    return currentScore - previousScore;
  }

  /** Returns whether the item score increased. */
  public boolean isImprovement() {
    return currentScore > previousScore;
  }

  /** Returns a short description such as "Pest Control Log: 1 -> 3". */
  public String getDescription() {
    return itemName + ": " + previousScore + " -> " + currentScore;
  }

  public String getItemName() {
    return itemName;
  }

  public int getPreviousScore() {
    return previousScore;
  }

  public int getCurrentScore() {
    return currentScore;
  }
}
