package data_access.save_favourite;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JsonFavouriteRepository.
 * Verifies that favourite buildings are saved,
 * persisted, and not duplicated.
 */
public class JsonFavouriteRepositoryTest {

    @TempDir
    Path tempDir;

    /**
     * Tests whether a building can be saved successfully.
     *
     * @throws IOException if file access fails
     */
    @Test
    public void testSaveFavourite() throws IOException {

        // Create a temporary file for storing favourites.
        Path file = tempDir.resolve("favourites.json");

        // Create the repository.
        JsonFavouriteRepository repository =
                new JsonFavouriteRepository(file);

        // Save a building.
        repository.save("12345");

        // Verify that the building was saved.
        assertTrue(repository.contains("12345"));
        assertTrue(Files.exists(file));
    }

    /**
     * Tests whether duplicate favourites are prevented.
     *
     * @throws IOException if file access fails
     */
    @Test
    public void testDuplicateFavourite() throws IOException {

        Path file = tempDir.resolve("favourites.json");

        JsonFavouriteRepository repository =
                new JsonFavouriteRepository(file);

        // Attempt to save the same building twice.
        repository.save("12345");
        repository.save("12345");

        // Read the saved JSON data.
        String json = Files.readString(file);
        String[] saved = new Gson().fromJson(json, String[].class);

        // Verify only one building was saved.
        assertEquals(1, saved.length);
        assertEquals("12345", saved[0]);
    }

    /**
     * Tests whether favourites persist across repository instances.
     *
     * @throws IOException if file access fails
     */
    @Test
    public void testPersistence() throws IOException {

        Path file = tempDir.resolve("favourites.json");

        // First repository saves a building.
        JsonFavouriteRepository firstRepository =
                new JsonFavouriteRepository(file);

        firstRepository.save("12345");

        // Simulate reopening the application.
        JsonFavouriteRepository secondRepository =
                new JsonFavouriteRepository(file);

        // The saved building should still exist.
        assertTrue(secondRepository.contains("12345"));
    }

    /**
     * Tests whether an unsaved building is correctly identified.
     *
     * @throws IOException if file access fails
     */
    @Test
    public void testBuildingNotSaved() throws IOException {

        Path file = tempDir.resolve("favourites.json");

        JsonFavouriteRepository repository =
                new JsonFavouriteRepository(file);

        // No building has been saved yet.
        assertFalse(repository.contains("99999"));
    }

    /**
     * Tests whether invalid JSON is reported as an error.
     *
     * @throws IOException if writing the test file fails
     */
    @Test
    public void testInvalidJson() throws IOException {

        Path file = tempDir.resolve("favourites.json");

        // Write malformed JSON.
        Files.writeString(file, "{invalid json");

        JsonFavouriteRepository repository =
                new JsonFavouriteRepository(file);

        // Reading malformed JSON should throw an IOException.
        assertThrows(IOException.class,
                () -> repository.contains("12345"));
    }

    /**
     * Tests retrieving all saved favourites.
     *
     * @throws IOException if file access fails
     */
    @Test
    public void testGetAllFavourites() throws IOException {
        Path file = tempDir.resolve("favourites.json");

        JsonFavouriteRepository repository =
                new JsonFavouriteRepository(file);

        repository.save("12345");
        repository.save("67890");

        assertEquals(2, repository.getAll().size());
        assertTrue(repository.getAll().contains("12345"));
        assertTrue(repository.getAll().contains("67890"));
    }

    /**
     * Tests removing a saved favourite.
     *
     * @throws IOException if file access fails
     */
    @Test
    public void testRemoveFavourite() throws IOException {
        Path file = tempDir.resolve("favourites.json");

        JsonFavouriteRepository repository =
                new JsonFavouriteRepository(file);

        repository.save("12345");
        repository.save("67890");

        repository.remove("12345");

        assertFalse(repository.contains("12345"));
        assertTrue(repository.contains("67890"));
        assertEquals(1, repository.getAll().size());
    }

}

