package use_case.save_favourite;

import java.io.IOException;

/**
 * The data access interface for storing favourite buildings.
 * Defines the operations needed by the Save Favourite use case
 * without specifying how the data is stored.
 */
public interface FavouriteRepository {

    /**
     * Checks whether a building is already saved as a favourite.
     *
     * @param rsn the unique registration number of the building
     * @return true if the building is already a favourite,
     *         false otherwise
     * @throws IOException if the stored data cannot be accessed
     */
    boolean contains(String rsn) throws IOException;

    /**
     * Saves a building as a favourite.
     *
     * @param rsn the unique registration number of the building
     * @throws IOException if the building cannot be saved
     */
    void save(String rsn) throws IOException;
}
