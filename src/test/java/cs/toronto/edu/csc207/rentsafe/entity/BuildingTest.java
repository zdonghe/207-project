package cs.toronto.edu.csc207.rentsafe.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests building identity, history ordering, and display helpers. */
class BuildingTest {

  @Test
  void latestEvaluationUsesHighestYear() {
    Building building = building();
    building.addEvaluation(evaluation(2023, 80));
    building.addEvaluation(evaluation(2025, 90));

    assertEquals(2025, building.getLatestEvaluation().orElseThrow().getYear());
  }

  @Test
  void historyIsOrderedOldestFirst() {
    Building building = building();
    building.addEvaluation(evaluation(2025, 90));
    building.addEvaluation(evaluation(2023, 80));

    List<Evaluation> history = building.getEvaluationHistory();
    assertEquals(2023, history.get(0).getYear());
    assertEquals(2025, history.get(1).getYear());
  }

  @Test
  void duplicateEvaluationsAreIgnored() {
    Building building = building();
    building.addEvaluation(evaluation(2025, 90));
    building.addEvaluation(evaluation(2025, 90));

    assertEquals(1, building.getEvaluationHistory().size());
  }

  @Test
  void addressKeyNormalizesCaseAndPunctuation() {
    Building building = new Building(
        "1", "123  Main St. W.", "12", "Ward", 1970, 5, 50, "PRIVATE", "", 0.0, 0.0);

    assertEquals("123 MAIN ST W", building.getAddressKey());
  }

  @Test
  void displayNameAndRegistrationReflectProfile() {
    Building building = building();

    assertTrue(building.getDisplayName().contains("44 BALLIOL ST"));
    assertTrue(building.getDisplayName().contains("Ward 12"));
    assertTrue(building.isRegistered());
    assertFalse(new Building(
        "", "", "", "", null, null, null, "", "", null, null).isRegistered());
  }

  private static Building building() {
    return new Building(
        "4153770", "44 BALLIOL ST", "12", "Toronto-St. Paul's", 1957, 4, 30, "PRIVATE",
        "Test Management", 43.697956, -79.395094);
  }

  private static Evaluation evaluation(int year, int score) {
    return new Evaluation("4153770", year, LocalDate.of(year, 6, 1), score, score, 0, List.of());
  }
}
