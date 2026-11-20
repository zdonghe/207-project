package cs.toronto.edu.csc207.rentsafe.usecase.detail;

import cs.toronto.edu.csc207.rentsafe.data_access.BuildingDataSource;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import java.util.Optional;

/** Loads one building and its ordered evaluation history. */
public final class DetailInteractor implements DetailInputBoundary {
  private final BuildingDataSource buildingDataSource;
  private final DetailOutputBoundary detailOutputBoundary;

  /** Creates a detail interactor. */
  public DetailInteractor(
      BuildingDataSource buildingDataSource, DetailOutputBoundary detailOutputBoundary) {
    this.buildingDataSource = buildingDataSource;
    this.detailOutputBoundary = detailOutputBoundary;
  }

  @Override
  public void execute(DetailInputData inputData) {
    String rsn = inputData == null ? "" : inputData.getRsn();
    if (rsn.isBlank()) {
      detailOutputBoundary.presentError("Enter a building ID (RSN) to view its details.");
      return;
    }

    try {
      Optional<Building> found = buildingDataSource.findByRsn(rsn);
      if (found.isEmpty()) {
        detailOutputBoundary.presentError("No building found for ID " + rsn + ".");
        return;
      }
      Building building = found.get();
      detailOutputBoundary.present(
          new DetailOutputData(
              building,
              building.getEvaluationHistory(),
              "Loaded " + building.getEvaluationHistory().size() + " evaluation(s) for "
                  + building.getDisplayName() + "."));
    } catch (RuntimeException ex) {
      detailOutputBoundary.presentError("Could not load building data: " + ex.getMessage());
    }
  }
}
