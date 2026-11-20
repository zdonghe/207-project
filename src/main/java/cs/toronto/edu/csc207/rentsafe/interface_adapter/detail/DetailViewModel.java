package cs.toronto.edu.csc207.rentsafe.interface_adapter.detail;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Observable state for the building detail screen. */
public final class DetailViewModel {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private Building building;
  private List<Evaluation> history = List.of();
  private String message = "";

  /** Registers a listener for detail state changes. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  /** Replaces the loaded building. */
  public void setBuilding(Building newBuilding) {
    Building oldBuilding = building;
    building = newBuilding;
    support.firePropertyChange("building", oldBuilding, building);
  }

  /** Replaces the evaluation history rows. */
  public void setHistory(List<Evaluation> newHistory) {
    List<Evaluation> oldHistory = history;
    history = newHistory == null ? List.of() : List.copyOf(newHistory);
    support.firePropertyChange("history", oldHistory, history);
  }

  /** Replaces the status message. */
  public void setMessage(String newMessage) {
    String oldMessage = message;
    message = newMessage == null ? "" : newMessage;
    support.firePropertyChange("message", oldMessage, message);
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
