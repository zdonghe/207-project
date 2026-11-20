package cs.toronto.edu.csc207.rentsafe.usecase.search;

import cs.toronto.edu.csc207.rentsafe.data_access.BuildingDataSource;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Applies the search use case over a building data source. */
public final class SearchInteractor implements SearchInputBoundary {
  private static final int queryLengthLimit = 120;

  private final BuildingDataSource buildingDataSource;
  private final SearchOutputBoundary searchOutputBoundary;

  /** Creates a search interactor. */
  public SearchInteractor(
      BuildingDataSource buildingDataSource, SearchOutputBoundary searchOutputBoundary) {
    this.buildingDataSource = buildingDataSource;
    this.searchOutputBoundary = searchOutputBoundary;
  }

  @Override
  public void execute(SearchInputData inputData) {
    String query = inputData == null ? "" : inputData.getQuery().trim();
    if (query.length() > queryLengthLimit) {
      searchOutputBoundary.presentError(
          "The search term is too long. Use an address, ward, or building ID.");
      return;
    }

    List<Building> buildings;
    try {
      buildings = buildingDataSource.search(query);
    } catch (RuntimeException ex) {
      searchOutputBoundary.presentError(
          "Could not load building data. Check the local cache or try again.");
      return;
    }

    List<SearchResult> results = buildings.stream()
        .map(SearchResult::from)
        .sorted(Comparator.comparingInt((SearchResult result) -> rank(result, query))
            .thenComparing(SearchResult::getDisplayName, String.CASE_INSENSITIVE_ORDER))
        .toList();
    searchOutputBoundary.present(
        new SearchOutputData(results, buildMessage(query, results.size())));
  }

  private static int rank(SearchResult result, String query) {
    if (query.isBlank()) {
      return 0;
    }
    String needle = query.toUpperCase(Locale.ROOT);
    final String rsn = result.getRsn().toUpperCase(Locale.ROOT);
    final String address = result.getAddress().toUpperCase(Locale.ROOT);
    final String ward = result.getWard().toUpperCase(Locale.ROOT);
    final String wardName = result.getWardName().toUpperCase(Locale.ROOT);
    if (rsn.equals(needle)) {
      return 0;
    }
    if (address.startsWith(needle)) {
      return 1;
    }
    if (address.contains(needle)) {
      return 2;
    }
    if (ward.equals(needle)) {
      return 3;
    }
    if (wardName.toUpperCase(Locale.ROOT).contains(needle)) {
      return 4;
    }
    return 5;
  }

  private static String buildMessage(String query, int count) {
    if (count == 0) {
      if (query.isBlank()) {
        return "No buildings are available in the local cache.";
      }
      return "No buildings matched \"" + query
          + "\". Try removing filters or a shorter search term.";
    }
    if (query.isBlank()) {
      return "Showing all " + count + " cached building(s).";
    }
    return "Found " + count + " building(s) matching \"" + query + "\".";
  }
}
