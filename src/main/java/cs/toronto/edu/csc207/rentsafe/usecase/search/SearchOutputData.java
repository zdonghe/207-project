package cs.toronto.edu.csc207.rentsafe.usecase.search;

import java.util.List;

/** Search response data ready for the presenter. */
public final class SearchOutputData {
  private final List<SearchResult> results;
  private final String message;

  /** Creates search output from ranked results and a status message. */
  public SearchOutputData(List<SearchResult> results, String message) {
    this.results = results == null ? List.of() : List.copyOf(results);
    this.message = message == null ? "" : message;
  }

  public List<SearchResult> getResults() {
    return results;
  }

  public String getMessage() {
    return message;
  }
}
