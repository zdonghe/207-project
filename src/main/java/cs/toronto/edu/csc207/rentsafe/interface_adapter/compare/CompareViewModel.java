package cs.toronto.edu.csc207.rentsafe.interface_adapter.compare;

import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareRow;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Observable state for the comparison screen. */
public final class CompareViewModel {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private String leftTitle = "";
  private String rightTitle = "";
  private List<CompareRow> rows = List.of();
  private String message = "";

  /** Registers a listener for comparison state changes. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  /** Replaces the left building title. */
  public void setLeftTitle(String newTitle) {
    String oldTitle = leftTitle;
    leftTitle = newTitle == null ? "" : newTitle;
    support.firePropertyChange("leftTitle", oldTitle, leftTitle);
  }

  /** Replaces the right building title. */
  public void setRightTitle(String newTitle) {
    String oldTitle = rightTitle;
    rightTitle = newTitle == null ? "" : newTitle;
    support.firePropertyChange("rightTitle", oldTitle, rightTitle);
  }

  /** Replaces the comparison rows. */
  public void setRows(List<CompareRow> newRows) {
    List<CompareRow> oldRows = rows;
    rows = newRows == null ? List.of() : List.copyOf(newRows);
    support.firePropertyChange("rows", oldRows, rows);
  }

  /** Replaces the status message. */
  public void setMessage(String newMessage) {
    String oldMessage = message;
    message = newMessage == null ? "" : newMessage;
    support.firePropertyChange("message", oldMessage, message);
  }

  public String getLeftTitle() {
    return leftTitle;
  }

  public String getRightTitle() {
    return rightTitle;
  }

  public List<CompareRow> getRows() {
    return rows;
  }

  public String getMessage() {
    return message;
  }
}
