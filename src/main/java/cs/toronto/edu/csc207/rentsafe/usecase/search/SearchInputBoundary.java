package cs.toronto.edu.csc207.rentsafe.usecase.search;

/** Input boundary for searching buildings. */
public interface SearchInputBoundary {
  /** Runs a search for the supplied request. */
  void execute(SearchInputData inputData);
}
