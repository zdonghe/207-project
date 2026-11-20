package cs.toronto.edu.csc207.rentsafe.usecase.compare;

/** Output boundary for the comparison view. */
public interface CompareOutputBoundary {
  /** Presents a completed comparison. */
  void present(CompareOutputData outputData);

  /** Presents a comparison failure. */
  void presentError(String message);
}
