package cs.toronto.edu.csc207.rentsafe.interface_adapter.save;

import cs.toronto.edu.csc207.rentsafe.entity.Favourite;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Observable state for the saved shortlist screen. */
public final class SaveViewModel {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private List<Favourite> favourites = List.of();
  private String message = "";
  private String reportPath = "";

  /** Registers a listener for shortlist state changes. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  /** Replaces the shortlist rows. */
  public void setFavourites(List<Favourite> newFavourites) {
    List<Favourite> oldFavourites = favourites;
    favourites = newFavourites == null ? List.of() : List.copyOf(newFavourites);
    support.firePropertyChange("favourites", oldFavourites, favourites);
  }

  /** Replaces the status message. */
  public void setMessage(String newMessage) {
    String oldMessage = message;
    message = newMessage == null ? "" : newMessage;
    support.firePropertyChange("message", oldMessage, message);
  }

  /** Replaces the last exported report path. */
  public void setReportPath(String newPath) {
    String oldPath = reportPath;
    reportPath = newPath == null ? "" : newPath;
    support.firePropertyChange("reportPath", oldPath, reportPath);
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
