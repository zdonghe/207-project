package cs.toronto.edu.csc207.rentsafe.data_access;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

/** Live City of Toronto Open Data adapter for apartment building evaluations. */
public final class CkanApiClient implements BuildingDataSource {
  private static final String evaluationResource = "244f7a02-da5c-425b-b55f-fbdd133dd732";
  private static final String defaultBaseUrl =
      "https://ckan0.cf.opendata.inter.prod-toronto.ca";
  private static final int resultLimit = 100;

  private final String baseUrl;
  private final HttpClient httpClient;
  private int lastRecordCount;

  /** Creates a live client using the public CKAN endpoint. */
  public CkanApiClient() {
    this(defaultBaseUrl, HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build());
  }

  /** Creates a live client with an explicit endpoint, useful for focused tests. */
  public CkanApiClient(String baseUrl, HttpClient httpClient) {
    this.baseUrl = baseUrl == null ? defaultBaseUrl : baseUrl;
    this.httpClient = httpClient;
  }

  @Override
  public List<Building> search(String query) {
    StringBuilder url = new StringBuilder(baseUrl)
        .append("/api/3/action/datastore_search?resource_id=")
        .append(evaluationResource)
        .append("&limit=")
        .append(resultLimit);
    if (query != null && !query.isBlank()) {
      url.append("&q=").append(URLEncoder.encode(query.trim(), StandardCharsets.UTF_8));
    }
    return fetchBuildings(URI.create(url.toString()));
  }

  @Override
  public Optional<Building> findByRsn(String rsn) {
    if (rsn == null || rsn.isBlank()) {
      return Optional.empty();
    }
    String wanted = rsn.trim();
    String filters = "{\"RSN\":\"" + wanted + "\"}";
    String url = baseUrl + "/api/3/action/datastore_search?resource_id=" + evaluationResource
        + "&limit=10&filters=" + URLEncoder.encode(filters, StandardCharsets.UTF_8);
    return fetchBuildings(URI.create(url)).stream()
        .filter(building -> building.getRsn().equals(wanted))
        .findFirst();
  }

  /** Returns the CKAN total record count reported by the most recent request. */
  public int getLastRecordCount() {
    return lastRecordCount;
  }

  private List<Building> fetchBuildings(URI uri) {
    HttpRequest request = HttpRequest.newBuilder(uri)
        .timeout(Duration.ofSeconds(20))
        .header("Accept", "application/json")
        .GET()
        .build();
    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        throw new IllegalStateException("CKAN returned HTTP " + response.statusCode() + ".");
      }
      JSONObject root = new JSONObject(response.body());
      if (!root.optBoolean("success")) {
        throw new IllegalStateException("CKAN reported an unsuccessful response.");
      }
      JSONObject result = root.optJSONObject("result");
      if (result == null) {
        return List.of();
      }
      JSONArray records = result.optJSONArray("records");
      lastRecordCount = result.optInt("total", records == null ? 0 : records.length());
      return CkanRecordMapper.map(records);
    } catch (IOException ex) {
      throw new IllegalStateException("Could not reach the City of Toronto API.", ex);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("The City of Toronto API request was interrupted.", ex);
    }
  }
}
