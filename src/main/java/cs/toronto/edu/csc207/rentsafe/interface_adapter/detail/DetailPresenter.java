package cs.toronto.edu.csc207.rentsafe.interface_adapter.detail;

import cs.toronto.edu.csc207.rentsafe.usecase.detail.DetailOutputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.detail.DetailOutputData;
import java.util.List;

/** Formats detail output into detail view model state. */
public final class DetailPresenter implements DetailOutputBoundary {
  private final DetailViewModel detailViewModel;

  /** Creates a detail presenter. */
  public DetailPresenter(DetailViewModel detailViewModel) {
    this.detailViewModel = detailViewModel;
  }

  @Override
  public void present(DetailOutputData outputData) {
    detailViewModel.setBuilding(outputData.getBuilding());
    detailViewModel.setHistory(outputData.getHistory());
    detailViewModel.setMessage(outputData.getMessage());
  }

  @Override
  public void presentError(String message) {
    detailViewModel.setBuilding(null);
    detailViewModel.setHistory(List.of());
    detailViewModel.setMessage(message);
  }
}
