package cs.toronto.edu.csc207.rentsafe.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** A shortlist report assembled before it is exported. */
public final class Report {
  private final String id;
  private final String title;
  private final LocalDateTime createdAt;
  private final ReportFormat format;
  private final List<Building> buildings = new ArrayList<>();
  private final List<ReportSection> sections = new ArrayList<>();

  /** Creates an empty report with identity, title, creation time, and output format. */
  public Report(String id, String title, LocalDateTime createdAt, ReportFormat format) {
    this.id = id == null ? "" : id.trim();
    this.title = title == null ? "" : title.trim();
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    this.format = format == null ? ReportFormat.CSV : format;
  }

  /** Adds a building, ignoring duplicates with the same registration number. */
  public void addBuilding(Building building) {
    if (building == null) {
      return;
    }
    boolean present = buildings.stream()
        .anyMatch(existing -> existing.getRsn().equals(building.getRsn()));
    if (!present) {
      buildings.add(building);
    }
  }

  /** Adds a text section to the report. */
  public void addSection(ReportSection section) {
    if (section != null) {
      sections.add(section);
    }
  }

  /** Returns the buildings included in the report. */
  public List<Building> getBuildings() {
    return List.copyOf(buildings);
  }

  /** Returns the extra sections included in the report. */
  public List<ReportSection> getSections() {
    return List.copyOf(sections);
  }

  /** Returns a short summary of the report contents. */
  public String getSummary() {
    return title + ": " + buildings.size() + " building(s), " + format + " format.";
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public ReportFormat getFormat() {
    return format;
  }
}
