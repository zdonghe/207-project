package cs.toronto.edu.csc207.rentsafe.usecase.save;

/** Output boundary for favourites and report export. */
public interface SaveOutputBoundary {
  /** Presents the current shortlist and a confirmation message. */
  void present(SaveOutputData outputData);

  /** Presents a save or export failure. */
  void presentError(String message);
}
