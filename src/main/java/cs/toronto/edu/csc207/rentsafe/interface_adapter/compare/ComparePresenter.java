package cs.toronto.edu.csc207.rentsafe.interface_adapter.compare;

import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareOutputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareOutputData;
import java.util.List;

/** Formats compare output into compare view model state. */
public final class ComparePresenter implements CompareOutputBoundary {
  private final CompareViewModel compareViewModel;

  /** Creates a compare presenter. */
  public ComparePresenter(CompareViewModel compareViewModel) {
    this.compareViewModel = compareViewModel;
  }

  @Override
  public void present(CompareOutputData outputData) {
    compareViewModel.setLeftTitle(outputData.getLeftTitle());
    compareViewModel.setRightTitle(outputData.getRightTitle());
    compareViewModel.setRows(outputData.getRows());
    compareViewModel.setMessage(outputData.getMessage());
  }

  @Override
  public void presentError(String message) {
    compareViewModel.setLeftTitle("");
    compareViewModel.setRightTitle("");
    compareViewModel.setRows(List.of());
    compareViewModel.setMessage(message);
  }
}
