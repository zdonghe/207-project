package cs.toronto.edu.csc207.rentsafe.usecase.compare;

/** Compare request data containing two building registration numbers. */
public final class CompareInputData {
  private final String leftRsn;
  private final String rightRsn;

  /** Creates a comparison request for two building IDs. */
  public CompareInputData(String leftRsn, String rightRsn) {
    this.leftRsn = leftRsn == null ? "" : leftRsn.trim();
    this.rightRsn = rightRsn == null ? "" : rightRsn.trim();
  }

  public String getLeftRsn() {
    return leftRsn;
  }

  public String getRightRsn() {
    return rightRsn;
  }
}
