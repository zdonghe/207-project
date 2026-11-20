package cs.toronto.edu.csc207.rentsafe.usecase.compare;

/** Input boundary for comparing two buildings. */
public interface CompareInputBoundary {
  /** Compares the two buildings identified by the request. */
  void execute(CompareInputData inputData);
}
