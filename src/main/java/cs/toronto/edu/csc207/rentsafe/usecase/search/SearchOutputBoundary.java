package cs.toronto.edu.csc207.rentsafe.usecase.search;

/** Output boundary for search results. */
public interface SearchOutputBoundary {
  /** Presents a completed search. */
  void present(SearchOutputData outputData);

  /** Presents a search failure in language a tenant can act on. */
  void presentError(String message);
}
