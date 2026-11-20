package cs.toronto.edu.csc207.rentsafe.usecase.detail;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import java.util.List;

/** Detail response data ready for the presenter. */
public final class DetailOutputData {
  private final Building building;
  private final List<Evaluation> history;
  private final String message;

  /** Creates detail output for one building. */
  public DetailOutputData(Building building, List<Evaluation> history, String message) {
    this.building = building;
    this.history = history == null ? List.of() : List.copyOf(history);
    this.message = message == null ? "" : message;
  }

  public Building getBuilding() {
    return building;
  }

  public List<Evaluation> getHistory() {
    return history;
  }

  public String getMessage() {
    return message;
  }
}
