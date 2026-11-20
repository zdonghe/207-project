package cs.toronto.edu.csc207.rentsafe.usecase.save;

import cs.toronto.edu.csc207.rentsafe.entity.Favourite;
import java.util.List;

/** Save response data ready for the presenter. */
public final class SaveOutputData {
  private final List<Favourite> favourites;
  private final String message;
  private final String reportPath;

  /** Creates save output with the refreshed shortlist and confirmation text. */
  public SaveOutputData(List<Favourite> favourites, String message, String reportPath) {
    this.favourites = favourites == null ? List.of() : List.copyOf(favourites);
    this.message = message == null ? "" : message;
    this.reportPath = reportPath == null ? "" : reportPath;
  }

  public List<Favourite> getFavourites() {
    return favourites;
  }

  public String getMessage() {
    return message;
  }

  public String getReportPath() {
    return reportPath;
  }
}
