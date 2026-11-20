package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A rental apartment building registered with the City of Toronto RentSafeTO program.
 *
 * <p>A building owns its evaluation history. Identity is the City registration number (RSN), and
 * the entity is otherwise treated as immutable once the profile fields are set.
 */
public final class Building {
  private final String rsn;
  private final String address;
  private final String ward;
  private final String wardName;
  private final Integer yearBuilt;
  private final Integer storeys;
  private final Integer units;
  private final String propertyType;
  private final String managementCompany;
  private final Double latitude;
  private final Double longitude;
  private final List<Evaluation> evaluations = new ArrayList<>();

  /** Creates a building from its registration profile fields. */
  public Building(
      String rsn,
      String address,
      String ward,
      String wardName,
      Integer yearBuilt,
      Integer storeys,
      Integer units,
      String propertyType,
      String managementCompany,
      Double latitude,
      Double longitude) {
    this.rsn = rsn == null ? "" : rsn.trim();
    this.address = address == null ? "" : address.trim();
    this.ward = ward == null ? "" : ward.trim();
    this.wardName = wardName == null ? "" : wardName.trim();
    this.yearBuilt = yearBuilt;
    this.storeys = storeys;
    this.units = units;
    this.propertyType = propertyType == null ? "" : propertyType.trim();
    this.managementCompany = managementCompany == null ? "" : managementCompany.trim();
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Adds an evaluation to the history.
   *
   * <p>An evaluation with the same year and completion date replaces the existing record, so a
   * corrected record is not dropped. An evaluation belonging to another building is rejected.
   */
  public void addEvaluation(Evaluation evaluation) {
    if (evaluation == null) {
      return;
    }
    if (!evaluation.getRsn().isBlank() && !evaluation.getRsn().equals(rsn)) {
      throw new IllegalArgumentException("Evaluation RSN does not match building RSN.");
    }
    for (int index = 0; index < evaluations.size(); index++) {
      Evaluation existing = evaluations.get(index);
      if (existing.getYear() == evaluation.getYear()
          && Objects.equals(existing.getCompletedOn(), evaluation.getCompletedOn())) {
        evaluations.set(index, evaluation);
        return;
      }
    }
    evaluations.add(evaluation);
  }

  /** Returns the most recent evaluation, when one is available. */
  public Optional<Evaluation> getLatestEvaluation() {
    return evaluations.stream()
        .max(Comparator.comparingInt(Evaluation::getYear)
            .thenComparing(
                Evaluation::getCompletedOn,
                Comparator.nullsFirst(Comparator.naturalOrder())));
  }

  /** Returns every evaluation ordered from oldest to newest. */
  public List<Evaluation> getEvaluationHistory() {
    List<Evaluation> history = new ArrayList<>(evaluations);
    history.sort(Comparator.comparingInt(Evaluation::getYear)
        .thenComparing(
            Evaluation::getCompletedOn,
            Comparator.nullsLast(Comparator.naturalOrder())));
    return history;
  }

  /** Returns an uppercase, punctuation-free address key used for simple matching. */
  public String getAddressKey() {
    String normalized = address.toUpperCase(Locale.ROOT);
    normalized = normalized.replaceAll("[^A-Z0-9 ]", " ");
    return normalized.replaceAll("\\s+", " ").trim();
  }

  /** Returns whether the building has enough registration data to be identified. */
  public boolean isRegistered() {
    return !rsn.isBlank() && !address.isBlank();
  }

  /** Returns the address and ward label shown in lists and window titles. */
  public String getDisplayName() {
    if (address.isBlank()) {
      return ward.isBlank() ? "" : "Ward " + ward;
    }
    if (ward.isBlank()) {
      return address;
    }
    return address + " - Ward " + ward;
  }

  public String getRsn() {
    return rsn;
  }

  public String getAddress() {
    return address;
  }

  public String getWard() {
    return ward;
  }

  public String getWardName() {
    return wardName;
  }

  public Integer getYearBuilt() {
    return yearBuilt;
  }

  public Integer getStoreys() {
    return storeys;
  }

  public Integer getUnits() {
    return units;
  }

  public String getPropertyType() {
    return propertyType;
  }

  public String getManagementCompany() {
    return managementCompany;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }
}
