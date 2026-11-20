package cs.toronto.edu.csc207.rentsafe.usecase.search;

/** Search request data. A blank query means "show every cached building". */
public final class SearchInputData {
  private final String query;

  /** Creates a search request from the tenant's query. */
  public SearchInputData(String query) {
    this.query = query == null ? "" : query.trim();
  }

  public String getQuery() {
    return query;
  }
}
