package interface_adapter.save_favourite;

/**
 * Represents the current state of the Save Favourite use case.
 * Stores information needed by the GUI to display
 * the result of saving a building.
 */
public class SaveFavouriteState {

    private String rsn = "";
    private String message = "";
    private boolean success;

    /**
     * Returns the registration number of the building.
     *
     * @return the building's RSN
     */
    public String getRsn() {
        return rsn;
    }

    /**
     * Updates the building's registration number.
     *
     * @param rsn the building's RSN
     */
    public void setRsn(String rsn) {
        this.rsn = rsn;
    }

    /**
     * Returns the message displayed to the user.
     *
     * @return the status message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the message displayed to the user.
     *
     * @param message the new status message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns whether saving the favourite succeeded.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Updates the success status.
     *
     * @param success whether saving succeeded
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
