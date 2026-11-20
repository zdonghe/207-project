package cs.toronto.edu.csc207.rentsafe.usecase.compare;

import cs.toronto.edu.csc207.rentsafe.data_access.BuildingDataSource;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Builds a side-by-side comparison of two buildings from cached data. */
public final class CompareInteractor implements CompareInputBoundary {
  private final BuildingDataSource buildingDataSource;
  private final CompareOutputBoundary compareOutputBoundary;

  /** Creates a compare interactor. */
  public CompareInteractor(
      BuildingDataSource buildingDataSource, CompareOutputBoundary compareOutputBoundary) {
    this.buildingDataSource = buildingDataSource;
    this.compareOutputBoundary = compareOutputBoundary;
  }

  @Override
  public void execute(CompareInputData inputData) {
    if (inputData == null || inputData.getLeftRsn().isBlank()
        || inputData.getRightRsn().isBlank()) {
      compareOutputBoundary.presentError("Choose two buildings to compare.");
      return;
    }

    try {
      Optional<Building> left = buildingDataSource.findByRsn(inputData.getLeftRsn());
      Optional<Building> right = buildingDataSource.findByRsn(inputData.getRightRsn());
      if (left.isEmpty() || right.isEmpty()) {
        compareOutputBoundary.presentError("One of the selected buildings is not in the cache.");
        return;
      }
      Building leftBuilding = left.get();
      Building rightBuilding = right.get();
      List<CompareRow> rows = buildRows(leftBuilding, rightBuilding);
      String message = leftBuilding.getRsn().equals(rightBuilding.getRsn())
          ? "Comparing one building with itself. Review the evaluation years to track change."
          : "Comparison based on the latest cached evaluation for each building.";
      compareOutputBoundary.present(
          new CompareOutputData(
              leftBuilding.getDisplayName(),
              rightBuilding.getDisplayName(),
              rows,
              message));
    } catch (RuntimeException ex) {
      compareOutputBoundary.presentError("Could not load comparison data: " + ex.getMessage());
    }
  }

  private static List<CompareRow> buildRows(Building left, Building right) {
    List<CompareRow> rows = new ArrayList<>();
    rows.add(new CompareRow("Address", left.getAddress(), right.getAddress()));
    rows.add(new CompareRow("Ward", left.getWard(), right.getWard()));
    rows.add(new CompareRow("Ward name", left.getWardName(), right.getWardName()));
    rows.add(new CompareRow(
        "Year built", format(left.getYearBuilt()), format(right.getYearBuilt())));
    rows.add(new CompareRow("Storeys", format(left.getStoreys()), format(right.getStoreys())));
    rows.add(new CompareRow("Units", format(left.getUnits()), format(right.getUnits())));
    rows.add(new CompareRow(
        "Property type", left.getPropertyType(), right.getPropertyType()));
    rows.add(new CompareRow(
        "Management company", left.getManagementCompany(), right.getManagementCompany()));
    rows.add(new CompareRow(
        "Evaluations on file",
        String.valueOf(left.getEvaluationHistory().size()),
        String.valueOf(right.getEvaluationHistory().size())));
    rows.add(new CompareRow(
        "Latest evaluation year", latestYear(left), latestYear(right)));
    rows.add(new CompareRow("Latest score", latestScore(left), latestScore(right)));
    rows.add(new CompareRow("Proactive score", latestProactive(left), latestProactive(right)));
    rows.add(new CompareRow("Reactive score", latestReactive(left), latestReactive(right)));
    rows.add(new CompareRow("Score difference", scoreDifference(left, right), ""));
    return rows;
  }

  private static String latestYear(Building building) {
    return building.getLatestEvaluation().map(evaluation -> String.valueOf(evaluation.getYear()))
        .orElse("N/A");
  }

  private static String latestScore(Building building) {
    return building.getLatestEvaluation().map(Evaluation::getCurrentScore).map(String::valueOf)
        .orElse("N/A");
  }

  private static String latestProactive(Building building) {
    return building.getLatestEvaluation().map(Evaluation::getProactiveScore).map(String::valueOf)
        .orElse("N/A");
  }

  private static String latestReactive(Building building) {
    return building.getLatestEvaluation().map(Evaluation::getReactiveScore).map(String::valueOf)
        .orElse("N/A");
  }

  private static String scoreDifference(Building left, Building right) {
    Optional<Integer> leftScore = left.getLatestEvaluation().map(Evaluation::getCurrentScore);
    Optional<Integer> rightScore = right.getLatestEvaluation().map(Evaluation::getCurrentScore);
    if (leftScore.isEmpty() || rightScore.isEmpty()) {
      return "N/A";
    }
    int difference = leftScore.get() - rightScore.get();
    return difference > 0 ? "+" + difference : String.valueOf(difference);
  }

  private static String format(Integer value) {
    return value == null ? "N/A" : String.valueOf(value);
  }

  private static String format(String value) {
    return value == null || value.isBlank() ? "N/A" : value;
  }
}
