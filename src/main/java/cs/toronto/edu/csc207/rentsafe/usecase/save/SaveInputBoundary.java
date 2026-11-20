package cs.toronto.edu.csc207.rentsafe.usecase.save;

/** Input boundary for favourites and report export. */
public interface SaveInputBoundary {
  /** Runs the requested save, remove, load, or export action. */
  void execute(SaveInputData inputData);
}
