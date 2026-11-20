package use_case.save_favourite;

/**
 * The output boundary for the Save Favourite use case.
 * Defines how the interactor communicates the result
 * of saving a favourite building to the presenter.
 */
public interface SaveFavouriteOutputBoundary {

    /**
     * Prepares the view when a building is successfully
     * saved as a favourite.
     *
     * @param outputData the data for the saved building
     */
    void prepareSuccessView(SaveFavouriteOutputData outputData);

    /**
     * Prepares the view when saving a favourite fails.
     *
     * @param errorMessage a description of why saving failed
     */
    void prepareFailView(String errorMessage);
}
