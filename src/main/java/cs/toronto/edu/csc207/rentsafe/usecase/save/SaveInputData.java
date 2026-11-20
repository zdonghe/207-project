package cs.toronto.edu.csc207.rentsafe.usecase.save;

/** Save request data for one shortlist action. */
public final class SaveInputData {
  private final SaveAction action;
  private final String rsn;
  private final String note;

  /** Creates a save request. */
  public SaveInputData(SaveAction action, String rsn, String note) {
    this.action = action == null ? SaveAction.LOAD : action;
    this.rsn = rsn == null ? "" : rsn.trim();
    this.note = note == null ? "" : note.trim();
  }

  public SaveAction getAction() {
    return action;
  }

  public String getRsn() {
    return rsn;
  }

  public String getNote() {
    return note;
  }
}
