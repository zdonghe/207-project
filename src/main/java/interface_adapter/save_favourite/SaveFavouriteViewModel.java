package interface_adapter.save_favourite;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for the Save Favourite use case.
 * Stores the current state and notifies the view when
 * the state changes.
 */
public class SaveFavouriteViewModel {

    private SaveFavouriteState state = new SaveFavouriteState();

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    /**
     * Returns the current state.
     *
     * @return the Save Favourite state
     */
    public SaveFavouriteState getState() {
        return state;
    }

    /**
     * Updates the current state.
     *
     * @param state the new state
     */
    public void setState(SaveFavouriteState state) {
        this.state = state;
    }

    /**
     * Notifies registered listeners that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /**
     * Registers a listener for state changes.
     *
     * @param listener the listener to register
     */
    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a registered listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
