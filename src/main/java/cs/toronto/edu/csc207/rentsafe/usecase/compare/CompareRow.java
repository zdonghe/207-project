package cs.toronto.edu.csc207.rentsafe.usecase.compare;

/** One metric row in a side-by-side comparison. */
public final class CompareRow {
  private final String label;
  private final String leftValue;
  private final String rightValue;

  /** Creates a comparison row. */
  public CompareRow(String label, String leftValue, String rightValue) {
    this.label = label == null ? "" : label;
    this.leftValue = leftValue == null ? "N/A" : leftValue;
    this.rightValue = rightValue == null ? "N/A" : rightValue;
  }

  public String getLabel() {
    return label;
  }

  public String getLeftValue() {
    return leftValue;
  }

  public String getRightValue() {
    return rightValue;
  }
}
