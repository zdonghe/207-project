package use_case.save_favourite;

/**
 * Input data for the Save Favourite use case
 * Stores unique identifier of the building that
 * the user wants to save as a favourite
 */

public class SaveFavouriteInputData {
    private final String rsn;

    /**
     * Constructs input data for saving a favourite building
     * @param rsn the unique registration number of the building
     */

    public SaveFavouriteInputData(String rsn) {
        this.rsn = rsn;
    }

    /**
     * Returns the registration number of the building
     *
     * @return the building's RSN
     */

    public String getRsn() {
        return rsn;
    }
}
