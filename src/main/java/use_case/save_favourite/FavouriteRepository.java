package use_case.save_favourite;

import java.io.IOException;
import java.util.List;

/**
 * The data access interface for storing and retrieving
 * favourite buildings.
 */
public interface FavouriteRepository {

    /**
     * Checks whether a building is already a favourite.
     *
     * @param rsn the building's registration number
     * @return true if the building is already saved
     * @throws IOException if the data cannot be accessed
     */
    boolean contains(String rsn) throws IOException;

    /**
     * Saves a building as a favourite.
     *
     * @param rsn the building's registration number
     * @throws IOException if saving fails
     */
    void save(String rsn) throws IOException;

    /**
     * Retrieves all saved favourite building RSNs.
     *
     * @return a list of saved building identifiers
     * @throws IOException if reading fails
     */
    List<String> getAll() throws IOException;

    /**
     * Removes a building from the favourites list.
     *
     * @param rsn the building's registration number
     * @throws IOException if removing fails
     */
    void remove(String rsn) throws IOException;
}

