package interface_adapter.save_favourite;

import use_case.save_favourite.SaveFavouriteInputBoundary;
import use_case.save_favourite.SaveFavouriteInputData;

/**
 * The controller for the Save Favourite use case.
 * Receives the building's RSN from the view and passes
 * it to the use case interactor.
 */
public class SaveFavouriteController {

    private final SaveFavouriteInputBoundary saveFavouriteUseCase;

    /**
     * Constructs the controller for saving favourites.
     *
     * @param saveFavouriteUseCase the input boundary
     *                             for the Save Favourite use case
     */
    public SaveFavouriteController(
            SaveFavouriteInputBoundary saveFavouriteUseCase) {
        this.saveFavouriteUseCase = saveFavouriteUseCase;
    }

    /**
     * Executes the Save Favourite use case.
     *
     * @param rsn the registration number of the building
     *            that the user wants to save
     */
    public void execute(String rsn) {

        // Create the input data containing the selected building's RSN.
        SaveFavouriteInputData inputData =
                new SaveFavouriteInputData(rsn);

        // Pass the input data to the interactor through its interface.
        saveFavouriteUseCase.execute(inputData);
    }
}
