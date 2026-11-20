package cs.toronto.edu.csc207.rentsafe.interface_adapter.compare;

import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareInputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareInputData;

/** Turns a view compare action into a compare use case request. */
public final class CompareController {
  private final CompareInputBoundary compareInputBoundary;

  /** Creates a compare controller. */
  public CompareController(CompareInputBoundary compareInputBoundary) {
    this.compareInputBoundary = compareInputBoundary;
  }

  /** Compares the two buildings with the supplied registration numbers. */
  public void compare(String leftRsn, String rightRsn) {
    compareInputBoundary.execute(new CompareInputData(leftRsn, rightRsn));
  }
}
