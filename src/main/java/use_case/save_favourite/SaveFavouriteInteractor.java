package use_case.save_favourite;

import java.io.IOException;

/**
 * The interactor for the Save Favourite use case.
 * Handles the business logic for saving a building
 * to the user's favourites.
 */
public class SaveFavouriteInteractor
        implements SaveFavouriteInputBoundary {

    private final FavouriteRepository favouriteRepository;
    private final SaveFavouriteOutputBoundary outputBoundary;

    /**
     * Constructs the Save Favourite interactor.
     *
     * @param favouriteRepository stores favourite buildings
     * @param outputBoundary presents the result of the use case
     */
    public SaveFavouriteInteractor(
            FavouriteRepository favouriteRepository,
            SaveFavouriteOutputBoundary outputBoundary) {

        this.favouriteRepository = favouriteRepository;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the Save Favourite use case.
     *
     * @param inputData contains the selected building's RSN
     */
    @Override
    public void execute(SaveFavouriteInputData inputData) {

        // Check that a building has been selected.
        if (inputData == null || inputData.getRsn() == null
                || inputData.getRsn().isBlank()) {
            outputBoundary.prepareFailView("No building selected.");
            return;
        }

        String rsn = inputData.getRsn().trim();

        try {
            // Prevent the same building from being saved twice.
            if (favouriteRepository.contains(rsn)) {
                outputBoundary.prepareFailView(
                        "Building is already a favourite.");
                return;
            }

            // Save the building using the repository.
            favouriteRepository.save(rsn);

            // Notify the presenter that saving succeeded.
            SaveFavouriteOutputData outputData =
                    new SaveFavouriteOutputData(rsn);

            outputBoundary.prepareSuccessView(outputData);

        } catch (IOException exception) {
            // Notify the presenter if the file cannot be accessed.
            outputBoundary.prepareFailView(
                    "Unable to save favourite.");
        }
    }
}
