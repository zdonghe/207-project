package cs.toronto.edu.csc207.rentsafe.data_access;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Favourite;
import cs.toronto.edu.csc207.rentsafe.usecase.save.FavouriteRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Stores the shortlist as JSON under the user's home directory. */
public final class JsonFavouriteRepository implements FavouriteRepository {
  private final BuildingDataSource buildingDataSource;
  private final Path file;

  /** Creates a repository at the default user-home location. */
  public JsonFavouriteRepository(BuildingDataSource buildingDataSource) {
    this(buildingDataSource, defaultPath());
  }

  /** Creates a repository at an explicit file path, useful for tests. */
  public JsonFavouriteRepository(BuildingDataSource buildingDataSource, Path file) {
    this.buildingDataSource = buildingDataSource;
    this.file = file;
  }

  @Override
  public List<Favourite> findAll() {
    if (!Files.exists(file)) {
      return List.of();
    }
    try {
      String json = Files.readString(file, StandardCharsets.UTF_8);
      if (json.isBlank()) {
        return List.of();
      }
      JSONArray entries = new JSONArray(json);
      List<Favourite> favourites = new ArrayList<>();
      for (int index = 0; index < entries.length(); index++) {
        Favourite favourite = readFavourite(entries.optJSONObject(index));
        if (favourite != null) {
          favourites.add(favourite);
        }
      }
      return List.copyOf(favourites);
    } catch (IOException ex) {
      throw new UncheckedIOException("Could not read favourites from " + file + ".", ex);
    } catch (JSONException ex) {
      throw new IllegalStateException("The favourites file is not valid JSON: " + file + ".", ex);
    }
  }

  @Override
  public void save(Favourite favourite) {
    List<Favourite> favourites = new ArrayList<>(findAll());
    favourites.removeIf(existing -> existing.getRsn().equals(favourite.getRsn()));
    favourites.add(favourite);
    write(favourites);
  }

  @Override
  public void remove(String rsn) {
    List<Favourite> favourites = new ArrayList<>(findAll());
    boolean removed = favourites.removeIf(existing -> existing.getRsn().equals(rsn));
    if (removed) {
      write(favourites);
    }
  }

  private Favourite readFavourite(JSONObject entry) {
    if (entry == null) {
      return null;
    }
    String rsn = entry.optString("rsn", "").trim();
    Optional<Building> building = buildingDataSource.findByRsn(rsn);
    if (building.isEmpty()) {
      return null;
    }
    Integer savedYear = entry.has("savedEvaluationYear")
        ? entry.optInt("savedEvaluationYear")
        : null;
    return new Favourite(
        building.get(),
        entry.optString("note", ""),
        parseDate(entry.optString("addedAt", "")),
        savedYear);
  }

  private void write(List<Favourite> favourites) {
    JSONArray entries = new JSONArray();
    for (Favourite favourite : favourites) {
      JSONObject entry = new JSONObject();
      entry.put("rsn", favourite.getRsn());
      entry.put("note", favourite.getNote());
      entry.put("addedAt", favourite.getAddedAt().toString());
      if (favourite.getSavedEvaluationYear() != null) {
        entry.put("savedEvaluationYear", favourite.getSavedEvaluationYear());
      }
      entries.put(entry);
    }
    try {
      Path parent = file.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      Path temp = file.resolveSibling(file.getFileName() + ".tmp");
      Files.writeString(temp, entries.toString(2), StandardCharsets.UTF_8);
      Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      throw new UncheckedIOException("Could not write favourites to " + file + ".", ex);
    }
  }

  private static LocalDateTime parseDate(String value) {
    try {
      return LocalDateTime.parse(value);
    } catch (DateTimeParseException ex) {
      return null;
    }
  }

  private static Path defaultPath() {
    return Path.of(System.getProperty("user.home"), ".rentsafe-to-explorer", "favourites.json");
  }
}
