package cs.toronto.edu.csc207.rentsafe.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests evaluation deficiencies, comparisons, and summaries. */
class EvaluationTest {

  @Test
  void deficienciesAreItemsScoredZeroOrOne() {
    Evaluation evaluation = evaluation(
        2025,
        85,
        List.of(
            new EvaluationItem("Pest control log", "Health", 0, 1.0, ""),
            new EvaluationItem("Balcony guards", "Safety", 1, 1.0, ""),
            new EvaluationItem("Windows", "Building", 3, 1.0, ""),
            new EvaluationItem("Pools", "Amenities", null, 1.0, "")));

    assertEquals(2, evaluation.getDeficiencies().size());
  }

  @Test
  void scoreForCategoryIsCaseInsensitive() {
    Evaluation evaluation = evaluation(
        2025, 85, List.of(new EvaluationItem("Pest control log", "Health", 0, 1.0, "")));

    assertTrue(evaluation.getScoreFor("PEST CONTROL LOG").isPresent());
    assertEquals(0, evaluation.getScoreFor("pest control log").getAsInt());
    assertTrue(evaluation.getScoreFor("Balcony guards").isEmpty());
  }

  @Test
  void improvementComparesCurrentScores() {
    Evaluation earlier = evaluation(2023, 70, List.of());
    Evaluation later = evaluation(2025, 85, List.of());

    assertTrue(later.isImprovementOver(earlier));
    assertFalse(earlier.isImprovementOver(later));
    assertFalse(later.isImprovementOver(later));
  }

  @Test
  void changedItemsReportRisesAndDeclines() {
    Evaluation earlier = evaluation(
        2023,
        70,
        List.of(
            new EvaluationItem("Pest control log", "Health", 1, 1.0, ""),
            new EvaluationItem("Balcony guards", "Safety", 3, 1.0, "")));
    Evaluation later = evaluation(
        2025,
        70,
        List.of(
            new EvaluationItem("Pest control log", "Health", 3, 1.0, ""),
            new EvaluationItem("Balcony guards", "Safety", 2, 1.0, "")));

    List<EvaluationChange> changes = later.getChangedItems(earlier);
    assertEquals(2, changes.size());
    assertTrue(changes.stream().anyMatch(EvaluationChange::isImprovement));
  }

  @Test
  void summaryMentionsYearAndScore() {
    Evaluation evaluation = evaluation(2025, 85, List.of());

    assertTrue(evaluation.getSummary().contains("2025"));
    assertTrue(evaluation.getSummary().contains("85"));
  }

  @Test
  void itemSeverityAndNormalizedScoreTrackTheScore() {
    EvaluationItem high = new EvaluationItem("Pest control log", "Health", 0, 1.0, "");
    EvaluationItem medium = new EvaluationItem("Balcony guards", "Safety", 1, 1.0, "");
    EvaluationItem low = new EvaluationItem("Windows", "Building", 3, 1.0, "");

    assertEquals(Severity.HIGH, high.getSeverity());
    assertEquals(Severity.MEDIUM, medium.getSeverity());
    assertEquals(Severity.LOW, low.getSeverity());
    assertEquals(1.0, low.getNormalizedScore());
  }

  private static Evaluation evaluation(int year, int score, List<EvaluationItem> items) {
    return new Evaluation(
        "4153770", year, LocalDate.of(year, 6, 1), score, score, 0, items);
  }
}
