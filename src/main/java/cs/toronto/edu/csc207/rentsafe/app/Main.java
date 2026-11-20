package cs.toronto.edu.csc207.rentsafe.app;

import cs.toronto.edu.csc207.rentsafe.data_access.CkanApiClient;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.view.MainFrame;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;

/** Application entry point. */
public final class Main {
  private Main() {
  }

  /** Starts the Swing application, optionally checking the live API first. */
  public static void main(String[] args) {
    if (Arrays.asList(args).contains("--live")) {
      runLiveCheck();
    }
    SwingUtilities.invokeLater(() -> {
      MainFrame frame = new AppBuilder().build();
      frame.showCard("search");
    });
  }

  private static void runLiveCheck() {
    CkanApiClient client = new CkanApiClient();
    try {
      List<Building> buildings = client.search("BALLIOL");
      System.out.println("Live CKAN check succeeded: " + client.getLastRecordCount()
          + " record(s), " + buildings.size() + " building(s).");
    } catch (RuntimeException ex) {
      System.err.println("Live CKAN check failed: " + ex.getMessage());
      System.err.println("The application will continue with the local cache.");
    }
  }
}
