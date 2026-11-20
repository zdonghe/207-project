package cs.toronto.edu.csc207.rentsafe.interface_adapter.save;

import cs.toronto.edu.csc207.rentsafe.usecase.save.SaveAction;
import cs.toronto.edu.csc207.rentsafe.usecase.save.SaveInputBoundary;
import cs.toronto.edu.csc207.rentsafe.usecase.save.SaveInputData;

/** Turns shortlist view actions into save use case requests. */
public final class SaveController {
  private final SaveInputBoundary saveInputBoundary;

  /** Creates a save controller. */
  public SaveController(SaveInputBoundary saveInputBoundary) {
    this.saveInputBoundary = saveInputBoundary;
  }

  /** Loads the saved shortlist. */
  public void load() {
    saveInputBoundary.execute(new SaveInputData(SaveAction.LOAD, "", ""));
  }

  /** Saves a building to the shortlist with an optional note. */
  public void save(String rsn, String note) {
    saveInputBoundary.execute(new SaveInputData(SaveAction.SAVE, rsn, note));
  }

  /** Removes a building from the shortlist. */
  public void remove(String rsn) {
    saveInputBoundary.execute(new SaveInputData(SaveAction.REMOVE, rsn, ""));
  }

  /** Exports the shortlist as a CSV report. */
  public void export() {
    saveInputBoundary.execute(new SaveInputData(SaveAction.EXPORT, "", ""));
  }
}
