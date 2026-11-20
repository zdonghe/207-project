package cs.toronto.edu.csc207.rentsafe.interface_adapter.save;

import cs.toronto.edu.csc207.rentsafe.usecase.save.SaveOutputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.save.SaveOutputData;

/** Formats save output into save view model state. */
public final class SavePresenter implements SaveOutputBoundary {
  private final SaveViewModel saveViewModel;

  /** Creates a save presenter. */
  public SavePresenter(SaveViewModel saveViewModel) {
    this.saveViewModel = saveViewModel;
  }

  @Override
  public void present(SaveOutputData outputData) {
    saveViewModel.setFavourites(outputData.getFavourites());
    saveViewModel.setReportPath(outputData.getReportPath());
    saveViewModel.setMessage(outputData.getMessage());
  }

  @Override
  public void presentError(String message) {
    saveViewModel.setReportPath("");
    saveViewModel.setMessage(message);
  }
}
