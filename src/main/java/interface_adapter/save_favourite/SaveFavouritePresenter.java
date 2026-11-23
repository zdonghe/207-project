package interface_adapter.save_favourite;

import use_case.save_favourite.SaveFavouriteOutputBoundary;
import use_case.save_favourite.SaveFavouriteOutputData;

/**
 * The presenter for the Save Favourite use case.
 * Updates the ViewModel based on the result of saving
 * a building as a favourite.
 */
public class SaveFavouritePresenter
        implements SaveFavouriteOutputBoundary {

    private final SaveFavouriteViewModel viewModel;

    /**
     * Constructs the Save Favourite presenter.
     *
     * @param viewModel the ViewModel to update
     */
    public SaveFavouritePresenter(
            SaveFavouriteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Updates the ViewModel when a building is successfully saved.
     *
     * @param outputData the information about the saved building
     */
    @Override
    public void prepareSuccessView(SaveFavouriteOutputData outputData) {

        // Get the current state of the ViewModel.
        SaveFavouriteState state = viewModel.getState();

        // Update the state to reflect a successful save.
        state.setRsn(outputData.getRsn());
        state.setMessage("Building saved successfully!");
        state.setSuccess(true);

        // Notify the GUI that the state has changed.
        viewModel.firePropertyChanged();
    }

    /**
     * Updates the ViewModel when saving a favourite fails.
     *
     * @param errorMessage the reason the save operation failed
     */
    @Override
    public void prepareFailView(String errorMessage) {

        // Get the current state of the ViewModel.
        SaveFavouriteState state = viewModel.getState();

        // Update the state to reflect the failure.
        state.setRsn("");
        state.setMessage(errorMessage);
        state.setSuccess(false);

        // Notify the GUI that the state has changed.
        viewModel.firePropertyChanged();
    }
}
