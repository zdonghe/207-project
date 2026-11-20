package cs.toronto.edu.csc207.rentsafe.usecase.detail;

/** Detail request data containing a building registration number. */
public final class DetailInputData {
  private final String rsn;

  /** Creates a detail request for one building ID. */
  public DetailInputData(String rsn) {
    this.rsn = rsn == null ? "" : rsn.trim();
  }

  public String getRsn() {
    return rsn;
  }
}
