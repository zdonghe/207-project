package cs.toronto.edu.csc207.rentsafe.usecase.save;

import cs.toronto.edu.csc207.rentsafe.data_access.BuildingDataSource;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Favourite;
import cs.toronto.edu.csc207.rentsafe.entity.Report;
import cs.toronto.edu.csc207.rentsafe.entity.ReportFormat;
import cs.toronto.edu.csc207.rentsafe.entity.ReportSection;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Manages the shortlist and exports it as a CSV report. */
public final class SaveInteractor implements SaveInputBoundary {
  private final BuildingDataSource buildingDataSource;
  private final FavouriteRepository favouriteRepository;
  private final ReportWriter reportWriter;
  private final SaveOutputBoundary saveOutputBoundary;

  /** Creates a save and export interactor. */
  public SaveInteractor(
      BuildingDataSource buildingDataSource,
      FavouriteRepository favouriteRepository,
      ReportWriter reportWriter,
      SaveOutputBoundary saveOutputBoundary) {
    this.buildingDataSource = buildingDataSource;
    this.favouriteRepository = favouriteRepository;
    this.reportWriter = reportWriter;
    this.saveOutputBoundary = saveOutputBoundary;
  }

  @Override
  public void execute(SaveInputData inputData) {
    try {
      switch (inputData.getAction()) {
        case LOAD -> load();
        case SAVE -> save(inputData);
        case REMOVE -> remove(inputData);
        case EXPORT -> export();
        default -> saveOutputBoundary.presentError("Unsupported shortlist action.");
      }
    } catch (IOException ex) {
      saveOutputBoundary.presentError("Could not write the report: " + ex.getMessage());
    } catch (RuntimeException ex) {
      saveOutputBoundary.presentError("Shortlist operation failed: " + ex.getMessage());
    }
  }

  private void load() {
    List<Favourite> favourites = favouriteRepository.findAll();
    if (favourites.isEmpty()) {
      presentFavourites("No saved buildings yet. Add one by building ID.", null);
      return;
    }
    presentFavourites(favourites.size() + " saved building(s).", null);
  }

  private void save(SaveInputData inputData) {
    if (inputData.getRsn().isBlank()) {
      saveOutputBoundary.presentError("Enter a building ID (RSN) before saving.");
      return;
    }
    Optional<Building> found = buildingDataSource.findByRsn(inputData.getRsn());
    if (found.isEmpty()) {
      saveOutputBoundary.presentError("No building found for ID " + inputData.getRsn() + ".");
      return;
    }
    Building building = found.get();
    favouriteRepository.save(new Favourite(building, inputData.getNote(), LocalDateTime.now()));
    presentFavourites("Saved " + building.getDisplayName() + " to your shortlist.", null);
  }

  private void remove(SaveInputData inputData) {
    if (inputData.getRsn().isBlank()) {
      saveOutputBoundary.presentError("Select a saved building to remove.");
      return;
    }
    favouriteRepository.remove(inputData.getRsn());
    presentFavourites("Removed building " + inputData.getRsn() + " from your shortlist.", null);
  }

  private void export() throws IOException {
    List<Favourite> favourites = favouriteRepository.findAll();
    if (favourites.isEmpty()) {
      saveOutputBoundary.presentError("Save at least one building before exporting a report.");
      return;
    }

    Report report = new Report(
        UUID.randomUUID().toString(),
        "RentSafeTO shortlist",
        LocalDateTime.now(),
        ReportFormat.CSV);
    for (Favourite favourite : favourites) {
      report.addBuilding(favourite.getBuilding());
    }
    report.addSection(new ReportSection("Notes", notes(favourites)));

    Path path = reportWriter.write(report);
    presentFavourites(
        "Exported " + favourites.size() + " building(s) to " + path + ".", path.toString());
  }

  private void presentFavourites(String message, String reportPath) {
    saveOutputBoundary.present(
        new SaveOutputData(favouriteRepository.findAll(), message, reportPath));
  }

  private static String notes(List<Favourite> favourites) {
    StringBuilder notes = new StringBuilder();
    for (Favourite favourite : favourites) {
      if (!favourite.getNote().isBlank()) {
        if (!notes.isEmpty()) {
          notes.append(" | ");
        }
        notes.append(favourite.getRsn()).append(": ").append(favourite.getNote());
      }
    }
    return notes.toString();
  }
}
