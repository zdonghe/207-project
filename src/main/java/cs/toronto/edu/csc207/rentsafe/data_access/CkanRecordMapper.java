package cs.toronto.edu.csc207.rentsafe.data_access;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import cs.toronto.edu.csc207.rentsafe.entity.EvaluationItem;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/** Maps raw CKAN datastore records to domain entities. */
public final class CkanRecordMapper {
  private static final Set<String> metadataKeys = Set.of(
      "_id", "RSN", "YEAR REGISTERED", "YEAR_REGISTERED", "YEAR BUILT", "YEAR_EVALUATED",
      "YEAR EVALUATED", "PROPERTY TYPE", "PROPERTY_TYPE", "WARD", "WARDNAME", "WARD_NAME",
      "SITE ADDRESS", "SITE_ADDRESS", "ADDRESS", "CONFIRMED STOREYS", "NO_OF_STOREYS",
      "CONFIRMED UNITS", "NO_OF_UNITS", "EVALUATION COMPLETED ON", "CURRENT BUILDING EVAL SCORE",
      "PROACTIVE BUILDING SCORE", "CURRENT REACTIVE SCORE", "NO OF AREAS EVALUATED", "LATITUDE",
      "LONGITUDE", "X", "Y", "rank", "PROP_MANAGEMENT_COMPANY_NAME", "MANAGEMENT_COMPANY",
      "PCODE");

  private CkanRecordMapper() {
  }

  /** Maps records without registration metadata. */
  public static List<Building> map(JSONArray records) {
    return map(records, Map.of());
  }

  /** Maps records, filling management company names from a registration lookup. */
  public static List<Building> map(JSONArray records, Map<String, String> managementByRsn) {
    if (records == null) {
      return List.of();
    }
    Map<String, String> management = managementByRsn == null ? Map.of() : managementByRsn;
    Map<String, Building> buildings = new LinkedHashMap<>();
    for (int index = 0; index < records.length(); index++) {
      JSONObject record = records.optJSONObject(index);
      if (record == null) {
        continue;
      }
      String rsn = text(record.opt("RSN"));
      if (rsn == null) {
        continue;
      }
      Building building = buildings.computeIfAbsent(
          rsn,
          key -> buildBuilding(key, record, management.getOrDefault(key, "")));
      Evaluation evaluation = buildEvaluation(rsn, record);
      if (evaluation != null) {
        building.addEvaluation(evaluation);
      }
    }
    return List.copyOf(buildings.values());
  }

  private static Building buildBuilding(
      String rsn, JSONObject record, String registeredManagement) {
    String address = firstText(record, "SITE ADDRESS", "SITE_ADDRESS", "ADDRESS");
    String propertyType = firstText(record, "PROPERTY TYPE", "PROPERTY_TYPE");
    String management = firstText(record, "PROP_MANAGEMENT_COMPANY_NAME", "MANAGEMENT_COMPANY");
    return new Building(
        rsn,
        address,
        firstText(record, "WARD"),
        firstText(record, "WARDNAME", "WARD_NAME"),
        integer(record.opt("YEAR BUILT")),
        integer(record.opt("CONFIRMED STOREYS")) != null
            ? integer(record.opt("CONFIRMED STOREYS"))
            : integer(record.opt("NO_OF_STOREYS")),
        integer(record.opt("CONFIRMED UNITS")) != null
            ? integer(record.opt("CONFIRMED UNITS"))
            : integer(record.opt("NO_OF_UNITS")),
        propertyType,
        management == null ? registeredManagement : management,
        decimal(record.opt("LATITUDE")),
        decimal(record.opt("LONGITUDE")));
  }

  private static Evaluation buildEvaluation(String rsn, JSONObject record) {
    LocalDate completedOn = date(text(record.opt("EVALUATION COMPLETED ON")));
    Integer year = integer(record.opt("YEAR EVALUATED"));
    if (year == null && completedOn != null) {
      year = completedOn.getYear();
    }
    if (year == null) {
      return null;
    }
    return new Evaluation(
        rsn,
        year,
        completedOn,
        integer(record.opt("CURRENT BUILDING EVAL SCORE")),
        integer(record.opt("PROACTIVE BUILDING SCORE")),
        integer(record.opt("CURRENT REACTIVE SCORE")),
        items(record));
  }

  private static List<EvaluationItem> items(JSONObject record) {
    List<EvaluationItem> items = new ArrayList<>();
    for (String key : record.keySet()) {
      if (metadataKeys.contains(key)) {
        continue;
      }
      Integer score = integer(record.opt(key));
      if (score == null || score < 0 || score > 3) {
        continue;
      }
      items.add(new EvaluationItem(titleCase(key), "Building condition", score, 1.0, ""));
    }
    items.sort(Comparator.comparing(EvaluationItem::getName));
    return items;
  }

  private static String firstText(JSONObject record, String... keys) {
    for (String key : keys) {
      String value = text(record.opt(key));
      if (value != null) {
        return value;
      }
    }
    return null;
  }

  private static String text(Object value) {
    if (value == null || JSONObject.NULL.equals(value)) {
      return null;
    }
    String text = value.toString().trim();
    if (text.isEmpty() || "N/A".equalsIgnoreCase(text) || "NA".equalsIgnoreCase(text)
        || "-".equals(text)) {
      return null;
    }
    return text;
  }

  private static Integer integer(Object value) {
    if (value == null || JSONObject.NULL.equals(value)) {
      return null;
    }
    if (value instanceof Number number) {
      return number.intValue();
    }
    String text = value.toString().trim();
    if (text.isEmpty() || "N/A".equalsIgnoreCase(text)) {
      return null;
    }
    try {
      return Integer.valueOf(text);
    } catch (NumberFormatException ex) {
      try {
        return (int) Math.round(Double.parseDouble(text));
      } catch (NumberFormatException ignored) {
        return null;
      }
    }
  }

  private static Double decimal(Object value) {
    if (value == null || JSONObject.NULL.equals(value)) {
      return null;
    }
    if (value instanceof Number number) {
      return number.doubleValue();
    }
    String text = value.toString().trim();
    if (text.isEmpty() || "N/A".equalsIgnoreCase(text)) {
      return null;
    }
    try {
      return Double.valueOf(text);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private static LocalDate date(String value) {
    if (value == null) {
      return null;
    }
    String candidate = value.length() >= 10 ? value.substring(0, 10) : value;
    try {
      return LocalDate.parse(candidate);
    } catch (DateTimeParseException ex) {
      return null;
    }
  }

  private static String titleCase(String key) {
    String[] words = key.toLowerCase(Locale.ROOT).split("\\s+");
    StringBuilder title = new StringBuilder();
    for (String word : words) {
      if (word.isEmpty()) {
        continue;
      }
      if (!title.isEmpty()) {
        title.append(' ');
      }
      title.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
    }
    return title.toString();
  }
}
