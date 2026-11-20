package use_case.save_favourite;

/**
 * The input boundary for the Save Favourite use case.
 * Defines the operations that the interactor must implement.
 */
public interface SaveFavouriteInputBoundary {

    /**
     * Executes the Save Favourite use case.
     *
     * @param inputData the data containing the RSN
     *                  of the building to save
     */
    void execute(SaveFavouriteInputData inputData);
}
