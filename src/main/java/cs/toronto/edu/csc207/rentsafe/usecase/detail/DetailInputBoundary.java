package cs.toronto.edu.csc207.rentsafe.usecase.detail;

/** Input boundary for loading one building's evaluation history. */
public interface DetailInputBoundary {
  /** Loads the building identified by the request. */
  void execute(DetailInputData inputData);
}
