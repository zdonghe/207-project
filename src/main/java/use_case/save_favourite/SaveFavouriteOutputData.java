package use_case.save_favourite;

/**
 * The output data for the Save Favourite use case.
 * Contains information about the building that
 * was successfully saved as a favourite.
 */
public class SaveFavouriteOutputData {

    private final String rsn;

    /**
     * Constructs the output data for a saved favourite.
     *
     * @param rsn the registration number of the saved building
     */
    public SaveFavouriteOutputData(String rsn) {
        this.rsn = rsn;
    }

    /**
     * Returns the registration number of the saved building.
     *
     * @return the building's RSN
     */
    public String getRsn() {
        return rsn;
    }
}

