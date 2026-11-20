package cs.toronto.edu.csc207.rentsafe.usecase.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs.toronto.edu.csc207.rentsafe.data_access.CachedBuildingRepository;
import cs.toronto.edu.csc207.rentsafe.data_access.CsvReportWriter;
import cs.toronto.edu.csc207.rentsafe.data_access.JsonFavouriteRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests the shortlist persistence and CSV export path. */
class SaveInteractorTest {
  @TempDir
  Path tempDir;

  @Test
  void saveAndExportRoundTrip() throws IOException {
    CachedBuildingRepository cache = new CachedBuildingRepository();
    Path favouritesFile = tempDir.resolve("favourites.json");
    SaveInteractor interactor = new SaveInteractor(
        cache,
        new JsonFavouriteRepository(cache, favouritesFile),
        new CsvReportWriter(tempDir.resolve("reports")),
        presenter);

    interactor.execute(new SaveInputData(SaveAction.SAVE, "4153770", "Bring a tape measure"));
    assertNotNull(output);
    assertEquals(1, output.getFavourites().size());
    assertEquals("Bring a tape measure", output.getFavourites().get(0).getNote());

    interactor.execute(new SaveInputData(SaveAction.EXPORT, "", ""));
    Path report = Path.of(output.getReportPath());
    assertTrue(Files.exists(report));
    assertTrue(Files.readString(report).contains("4153770"));

    JsonFavouriteRepository reloaded = new JsonFavouriteRepository(cache, favouritesFile);
    assertEquals(1, reloaded.findAll().size());
  }

  @Test
  void exportWithoutFavouritesPresentsError() {
    CachedBuildingRepository cache = new CachedBuildingRepository();
    SaveInteractor interactor = new SaveInteractor(
        cache,
        new JsonFavouriteRepository(cache, tempDir.resolve("empty.json")),
        new CsvReportWriter(tempDir.resolve("reports")),
        presenter);

    interactor.execute(new SaveInputData(SaveAction.EXPORT, "", ""));

    assertNotNull(error);
    assertTrue(error.contains("Save at least one building"));
  }

  private final CapturingSavePresenter presenter = new CapturingSavePresenter();
  private SaveOutputData output;
  private String error;

  private final class CapturingSavePresenter implements SaveOutputBoundary {
    @Override
    public void present(SaveOutputData outputData) {
      output = outputData;
    }

    @Override
    public void presentError(String message) {
      error = message;
    }
  }
}
