package cs.toronto.edu.csc207.rentsafe.data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies the bundled CKAN fixture loads and maps into usable buildings. */
class CachedBuildingRepositoryTest {

  private final CachedBuildingRepository repository = new CachedBuildingRepository();

  @Test
  void balliolFixtureLoadsBuildings() {
    List<Building> buildings = repository.search("BALLIOL");

    assertEquals(10, buildings.size());
    assertTrue(buildings.stream().allMatch(building -> building.getAddress().contains("BALLIOL")));
    assertTrue(buildings.stream().allMatch(Building::isRegistered));
  }

  @Test
  void cachedBuildingHasEvaluationHistoryAndScores() {
    Building building = repository.findByRsn("4153770").orElseThrow();

    assertEquals(2, building.getEvaluationHistory().size());
    assertTrue(building.getLatestEvaluation().isPresent());
    assertEquals(2026, building.getLatestEvaluation().orElseThrow().getYear());
    assertNotNull(building.getLatestEvaluation().orElseThrow().getCurrentScore());
    assertFalse(building.getLatestEvaluation().orElseThrow().getItems().isEmpty());
  }

  @Test
  void blankSearchReturnsEveryCachedBuilding() {
    assertFalse(repository.search("").isEmpty());
  }
}
