package cs.toronto.edu.csc207.rentsafe.interface_adapter.search;

import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchResult;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Observable state for the search screen. */
public final class SearchViewModel {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private List<SearchResult> results = List.of();
  private String message = "";

  /** Registers a listener for results and message changes. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  /** Replaces the result rows. */
  public void setResults(List<SearchResult> newResults) {
    List<SearchResult> oldResults = results;
    results = newResults == null ? List.of() : List.copyOf(newResults);
    support.firePropertyChange("results", oldResults, results);
  }

  /** Replaces the status message. */
  public void setMessage(String newMessage) {
    String oldMessage = message;
    message = newMessage == null ? "" : newMessage;
    support.firePropertyChange("message", oldMessage, message);
  }

  public List<SearchResult> getResults() {
    return results;
  }

  public String getMessage() {
    return message;
  }
}
