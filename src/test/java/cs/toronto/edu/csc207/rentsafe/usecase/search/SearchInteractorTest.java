package cs.toronto.edu.csc207.rentsafe.usecase.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs.toronto.edu.csc207.rentsafe.data_access.BuildingDataSource;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Tests the search use case through a fake data source and a capturing presenter. */
class SearchInteractorTest {

  @Test
  void searchReturnsRankedMatches() {
    SearchInteractor interactor = new SearchInteractor(
        new FakeDataSource(List.of(
            building("4153770", "44 BALLIOL ST", 2024, 81),
            building("4244920", "118 BALLIOL ST", 2025, 99),
            building("4153000", "10 YONGE ST", 2025, 75))),
        presenter);
    interactor.execute(new SearchInputData("BALLIOL"));

    assertNotNull(output);
    assertEquals(2, output.getResults().size());
    assertEquals("4244920", output.getResults().get(0).getRsn());
    assertEquals(99, output.getResults().get(0).getLatestScore().intValue());
    assertEquals(2025, output.getResults().get(0).getEvaluationYear().intValue());
  }

  @Test
  void exactBuildingIdRanksFirst() {
    SearchInteractor interactor = new SearchInteractor(
        new FakeDataSource(List.of(
            building("4153770", "44 BALLIOL ST", 2024, 81),
            building("4153771", "44 BALLIOL ST", 2025, 90))),
        presenter);
    interactor.execute(new SearchInputData("4153770"));

    assertNotNull(output);
    assertEquals("4153770", output.getResults().get(0).getRsn());
  }

  @Test
  void blankQueryReturnsEveryCachedBuilding() {
    SearchInteractor interactor = new SearchInteractor(
        new FakeDataSource(List.of(
            building("1", "1 FIRST ST", 2024, 70),
            building("2", "2 SECOND ST", 2024, 80))),
        presenter);
    interactor.execute(new SearchInputData("  "));

    assertNotNull(output);
    assertEquals(2, output.getResults().size());
    assertTrue(output.getMessage().contains("all 2"));
  }

  @Test
  void emptyResultPresentsHelpfulMessage() {
    SearchInteractor interactor = new SearchInteractor(
        new FakeDataSource(List.of(building("1", "1 FIRST ST", 2024, 70))),
        presenter);
    interactor.execute(new SearchInputData("MARS"));

    assertNotNull(output);
    assertTrue(output.getResults().isEmpty());
    assertTrue(output.getMessage().contains("No buildings matched"));
  }

  @Test
  void dataSourceFailureIsPresentedAsError() {
    SearchInteractor interactor = new SearchInteractor(
        new FakeDataSource(List.of(), new IllegalStateException("cache unavailable")),
        presenter);
    interactor.execute(new SearchInputData("BALLIOL"));

    assertNull(output);
    assertNotNull(error);
    assertTrue(error.contains("Could not load building data"));
  }

  private final CapturingPresenter presenter = new CapturingPresenter();
  private SearchOutputData output;
  private String error;

  private static Building building(String rsn, String address, int year, int score) {
    Building building = new Building(
        rsn, address, "12", "Toronto-St. Paul's", 1960, 10, 100, "PRIVATE",
        "Test Management", 43.7, -79.4);
    building.addEvaluation(new Evaluation(
        rsn, year, LocalDate.of(year, 6, 1), score, score, 0, List.of()));
    return building;
  }

  private final class CapturingPresenter implements SearchOutputBoundary {
    @Override
    public void present(SearchOutputData outputData) {
      output = outputData;
    }

    @Override
    public void presentError(String message) {
      error = message;
    }
  }

  private static final class FakeDataSource implements BuildingDataSource {
    private final List<Building> buildings;
    private final RuntimeException failure;

    FakeDataSource(List<Building> buildings) {
      this(buildings, null);
    }

    FakeDataSource(List<Building> buildings, RuntimeException failure) {
      this.buildings = buildings;
      this.failure = failure;
    }

    @Override
    public List<Building> search(String query) {
      if (failure != null) {
        throw failure;
      }
      if (query == null || query.isBlank()) {
        return buildings;
      }
      String needle = query.trim().toUpperCase(Locale.ROOT);
      return buildings.stream()
          .filter(building -> building.getRsn().equals(query.trim())
              || building.getAddress().toUpperCase(Locale.ROOT).contains(needle))
          .toList();
    }

    @Override
    public Optional<Building> findByRsn(String rsn) {
      return buildings.stream().filter(building -> building.getRsn().equals(rsn)).findFirst();
    }
  }
}
