package cs.toronto.edu.csc207.rentsafe.interface_adapter.search;

import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchInputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchInputData;

/** Turns a view search action into a search use case request. */
public final class SearchController {
  private final SearchInputBoundary searchInputBoundary;

  /** Creates a search controller. */
  public SearchController(SearchInputBoundary searchInputBoundary) {
    this.searchInputBoundary = searchInputBoundary;
  }

  /** Runs a search for the supplied query. */
  public void search(String query) {
    searchInputBoundary.execute(new SearchInputData(query));
  }
}
