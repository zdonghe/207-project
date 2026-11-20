package cs.toronto.edu.csc207.rentsafe.interface_adapter.search;

import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchOutputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchOutputData;
import java.util.List;

/** Formats search output into search view model state. */
public final class SearchPresenter implements SearchOutputBoundary {
  private final SearchViewModel searchViewModel;

  /** Creates a search presenter. */
  public SearchPresenter(SearchViewModel searchViewModel) {
    this.searchViewModel = searchViewModel;
  }

  @Override
  public void present(SearchOutputData outputData) {
    searchViewModel.setResults(outputData.getResults());
    searchViewModel.setMessage(outputData.getMessage());
  }

  @Override
  public void presentError(String message) {
    searchViewModel.setResults(List.of());
    searchViewModel.setMessage(message);
  }
}
