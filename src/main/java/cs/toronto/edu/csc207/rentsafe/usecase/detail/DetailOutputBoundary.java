package cs.toronto.edu.csc207.rentsafe.usecase.detail;

/** Output boundary for the building detail view. */
public interface DetailOutputBoundary {
  /** Presents a loaded building and its evaluation history. */
  void present(DetailOutputData outputData);

  /** Presents a detail loading failure. */
  void presentError(String message);
}
