package cs.toronto.edu.csc207.rentsafe.usecase.save;

import cs.toronto.edu.csc207.rentsafe.entity.Report;
import java.io.IOException;
import java.nio.file.Path;

/** Output port for writing a report to disk. */
public interface ReportWriter {
  /** Writes the report and returns the path of the created file. */
  Path write(Report report) throws IOException;
}
