package cs.toronto.edu.csc207.rentsafe.interface_adapter.detail;

import cs.toronto.edu.csc207.rentsafe.usecase.detail.DetailInputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.detail.DetailInputData;

/** Turns a view detail action into a detail use case request. */
public final class DetailController {
  private final DetailInputBoundary detailInputBoundary;

  /** Creates a detail controller. */
  public DetailController(DetailInputBoundary detailInputBoundary) {
    this.detailInputBoundary = detailInputBoundary;
  }

  /** Loads the building with the supplied registration number. */
  public void load(String rsn) {
    detailInputBoundary.execute(new DetailInputData(rsn));
  }
}
