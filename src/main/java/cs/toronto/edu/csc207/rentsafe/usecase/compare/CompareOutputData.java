package cs.toronto.edu.csc207.rentsafe.usecase.compare;

import java.util.List;

/** Compare response data ready for the presenter. */
public final class CompareOutputData {
  private final String leftTitle;
  private final String rightTitle;
  private final List<CompareRow> rows;
  private final String message;

  /** Creates comparison output for two buildings. */
  public CompareOutputData(
      String leftTitle, String rightTitle, List<CompareRow> rows, String message) {
    this.leftTitle = leftTitle == null ? "" : leftTitle;
    this.rightTitle = rightTitle == null ? "" : rightTitle;
    this.rows = rows == null ? List.of() : List.copyOf(rows);
    this.message = message == null ? "" : message;
  }

  public String getLeftTitle() {
    return leftTitle;
  }

  public String getRightTitle() {
    return rightTitle;
  }

  public List<CompareRow> getRows() {
    return rows;
  }

  public String getMessage() {
    return message;
  }
}
