package cs.toronto.edu.csc207.rentsafe.data_access;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

/** Offline building source backed by the bundled CKAN response. */
public final class CachedBuildingRepository implements BuildingDataSource {
  private final List<Building> buildings;

  /** Loads the default cache bundled with the application. */
  public CachedBuildingRepository() {
    this("/cache/buildings.json", "/cache/registrations.json");
  }

  /** Loads alternative classpath resources, which keeps tests and demos flexible. */
  public CachedBuildingRepository(String buildingsResource, String registrationsResource) {
    JSONArray records = readRecords(buildingsResource);
    Map<String, String> management = readManagement(registrationsResource);
    this.buildings = List.copyOf(CkanRecordMapper.map(records, management));
  }

  @Override
  public List<Building> search(String query) {
    if (query == null || query.isBlank()) {
      return buildings;
    }
    String needle = query.trim().toUpperCase(Locale.ROOT);
    return buildings.stream()
        .filter(building -> matches(building, needle))
        .sorted(Comparator.comparing(Building::getAddress, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  @Override
  public Optional<Building> findByRsn(String rsn) {
    if (rsn == null || rsn.isBlank()) {
      return Optional.empty();
    }
    String wanted = rsn.trim();
    return buildings.stream().filter(building -> building.getRsn().equals(wanted)).findFirst();
  }

  private static boolean matches(Building building, String needle) {
    return building.getRsn().toUpperCase(Locale.ROOT).contains(needle)
        || building.getAddress().toUpperCase(Locale.ROOT).contains(needle)
        || building.getAddressKey().contains(needle)
        || building.getWard().toUpperCase(Locale.ROOT).contains(needle)
        || building.getWardName().toUpperCase(Locale.ROOT).contains(needle);
  }

  private static JSONArray readRecords(String resource) {
    try (InputStream stream = CachedBuildingRepository.class.getResourceAsStream(resource)) {
      if (stream == null) {
        throw new IllegalStateException("Cache resource not found: " + resource);
      }
      String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
      JSONObject result = new JSONObject(json).optJSONObject("result");
      JSONArray records = result == null ? null : result.optJSONArray("records");
      if (records == null) {
        throw new IllegalStateException("Cache resource has no result.records: " + resource);
      }
      return records;
    } catch (IOException ex) {
      throw new IllegalStateException("Could not read cache resource " + resource + ".", ex);
    }
  }

  private static Map<String, String> readManagement(String resource) {
    InputStream stream = CachedBuildingRepository.class.getResourceAsStream(resource);
    if (stream == null) {
      return Map.of();
    }
    try (stream) {
      String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
      JSONObject result = new JSONObject(json).optJSONObject("result");
      JSONArray records = result == null ? null : result.optJSONArray("records");
      if (records == null) {
        return Map.of();
      }
      Map<String, String> management = new HashMap<>();
      for (int index = 0; index < records.length(); index++) {
        JSONObject record = records.optJSONObject(index);
        if (record == null) {
          continue;
        }
        String rsn = record.optString("RSN", "").trim();
        String company = record.optString("PROP_MANAGEMENT_COMPANY_NAME", "").trim();
        if (!rsn.isEmpty() && !company.isEmpty() && !"N/A".equalsIgnoreCase(company)) {
          management.put(rsn, company);
        }
      }
      return management;
    } catch (IOException ex) {
      throw new IllegalStateException("Could not read cache resource " + resource + ".", ex);
    }
  }
}
