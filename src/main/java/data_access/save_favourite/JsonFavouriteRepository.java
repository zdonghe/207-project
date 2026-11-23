package data_access.save_favourite;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import use_case.save_favourite.FavouriteRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores and retrieves favourite buildings using a JSON file.
 * Implements the FavouriteRepository interface.
 */
public class JsonFavouriteRepository implements FavouriteRepository {

    private final Path filePath;
    private final Gson gson = new Gson();

    /**
     * Constructs the repository.
     *
     * @param filePath the location of the favourites JSON file
     */
    public JsonFavouriteRepository(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Checks whether a building is already saved.
     *
     * @param rsn the building's registration number
     * @return true if the building is a favourite
     * @throws IOException if reading fails
     */
    @Override
    public boolean contains(String rsn) throws IOException {
        return loadFavourites().contains(rsn);
    }

    /**
     * Saves a building as a favourite, preventing duplicates.
     *
     * @param rsn the building's registration number
     * @throws IOException if saving fails
     */
    @Override
    public void save(String rsn) throws IOException {
        List<String> favourites = loadFavourites();

        if (!favourites.contains(rsn)) {
            favourites.add(rsn);
            writeFavourites(favourites);
        }
    }

    /**
     * Retrieves all saved favourite building identifiers.
     *
     * @return a list of saved RSNs
     * @throws IOException if reading fails
     */
    @Override
    public List<String> getAll() throws IOException {
        return loadFavourites();
    }

    /**
     * Removes a building from the favourites list.
     *
     * @param rsn the building's registration number
     * @throws IOException if removing fails
     */
    @Override
    public void remove(String rsn) throws IOException {
        List<String> favourites = loadFavourites();

        // Only update the file if the building was saved.
        if (favourites.remove(rsn)) {
            writeFavourites(favourites);
        }
    }

    /**
     * Reads the saved favourites from the JSON file.
     *
     * @return a modifiable list of saved RSNs
     * @throws IOException if reading or parsing fails
     */
    private List<String> loadFavourites() throws IOException {

        // Return an empty list if the file does not exist.
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(
                    filePath, StandardCharsets.UTF_8);

            String[] saved = gson.fromJson(
                    json, String[].class);

            if (saved == null) {
                return new ArrayList<>();
            }

            return new ArrayList<>(Arrays.asList(saved));

        } catch (JsonParseException exception) {
            throw new IOException(
                    "Invalid favourites JSON file.", exception);
        }
    }

    /**
     * Writes the updated list of favourites to the JSON file.
     *
     * @param favourites the building identifiers to store
     * @throws IOException if writing fails
     */
    private void writeFavourites(List<String> favourites)
            throws IOException {

        // Create the directory if necessary.
        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        // Convert the list to JSON and save it.
        Files.writeString(
                filePath,
                gson.toJson(favourites),
                StandardCharsets.UTF_8
        );
    }
}
