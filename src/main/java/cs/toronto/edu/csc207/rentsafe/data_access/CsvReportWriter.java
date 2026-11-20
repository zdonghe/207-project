package cs.toronto.edu.csc207.rentsafe.data_access;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import cs.toronto.edu.csc207.rentsafe.entity.Report;
import cs.toronto.edu.csc207.rentsafe.usecase.save.ReportWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/** Writes a shortlist report as a CSV file under the user's home directory. */
public final class CsvReportWriter implements ReportWriter {
  private final Path directory;

  /** Creates a writer that stores reports in the default user-home folder. */
  public CsvReportWriter() {
    this(Path.of(System.getProperty("user.home"), "rentsafe-reports"));
  }

  /** Creates a writer that stores reports in an explicit folder, useful for tests. */
  public CsvReportWriter(Path directory) {
    this.directory = directory;
  }

  @Override
  public Path write(Report report) throws IOException {
    Files.createDirectories(directory);
    String stamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS"));
    Path file = directory.resolve("rentsafe-report-" + stamp + ".csv");
    try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
      writer.write("Report,Building ID,Address,Ward,Ward name,Year built,Storeys,Units,"
          + "Property type,Latest score,Latest year,Evaluations");
      writer.newLine();
      for (Building building : report.getBuildings()) {
        Optional<Evaluation> latest = building.getLatestEvaluation();
        writer.write(String.join(",",
            csv(report.getTitle()),
            csv(building.getRsn()),
            csv(building.getAddress()),
            csv(building.getWard()),
            csv(building.getWardName()),
            csv(value(building.getYearBuilt())),
            csv(value(building.getStoreys())),
            csv(value(building.getUnits())),
            csv(building.getPropertyType()),
            csv(value(latest.map(Evaluation::getCurrentScore).orElse(null))),
            csv(value(latest.map(Evaluation::getYear).orElse(null))),
            csv(String.valueOf(building.getEvaluationHistory().size()))));
        writer.newLine();
      }
    }
    return file;
  }

  private static String value(Integer number) {
    return number == null ? "N/A" : String.valueOf(number);
  }

  private static String csv(String value) {
    if (value == null) {
      return "";
    }
    return "\"" + value.replace("\"", "\"\"") + "\"";
  }
}
