package cs.toronto.edu.csc207.rentsafe.view;

import cs.toronto.edu.csc207.rentsafe.interface_adapter.search.SearchController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.search.SearchViewModel;
import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchResult;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/** Search screen: query field, result table, live API smoke test, and detail shortcut. */
public final class SearchView extends JPanel {
  private final SearchController controller;
  private final SearchViewModel viewModel;
  private final Supplier<String> liveCheck;
  private final Consumer<String> detailsHandler;
  private final JTextField queryField = new JTextField(26);
  private final JButton searchButton = new JButton("Search");
  private final JButton refreshButton = new JButton("Refresh from API");
  private final JButton detailsButton = new JButton("View details");
  private final JLabel statusLabel = new JLabel(" ");
  private final DefaultTableModel tableModel = new DefaultTableModel(
      new String[] {"Building ID", "Address", "Ward", "Latest score", "Year"}, 0);
  private final JTable table = new JTable(tableModel);

  /** Creates the search screen and performs the initial cached search. */
  public SearchView(
      SearchController controller,
      SearchViewModel viewModel,
      Supplier<String> liveCheck,
      Consumer<String> detailsHandler) {
    this.controller = controller;
    this.viewModel = viewModel;
    this.liveCheck = liveCheck;
    this.detailsHandler = detailsHandler;

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    add(buildQueryBar(), BorderLayout.NORTH);
    add(buildTable(), BorderLayout.CENTER);
    add(statusLabel, BorderLayout.SOUTH);

    searchButton.addActionListener(event -> runSearch());
    queryField.addActionListener(event -> runSearch());
    detailsButton.addActionListener(event -> openDetails());
    refreshButton.addActionListener(event -> refreshFromApi());
    viewModel.addPropertyChangeListener(event -> render());
    controller.search("");
  }

  private JPanel buildQueryBar() {
    JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
    bar.add(new JLabel("Address, ward, or building ID:"));
    bar.add(queryField);
    bar.add(searchButton);
    bar.add(detailsButton);
    bar.add(refreshButton);
    return bar;
  }

  private JScrollPane buildTable() {
    table.setAutoCreateRowSorter(true);
    table.setFillsViewportHeight(true);
    table.setRowHeight(24);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setDefaultEditor(Object.class, null);
    table.getTableHeader().setReorderingAllowed(false);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
          openDetails();
        }
      }
    });
    return new JScrollPane(table);
  }

  private void runSearch() {
    controller.search(queryField.getText());
  }

  private void openDetails() {
    int selected = table.getSelectedRow();
    if (selected < 0) {
      statusLabel.setText("Select a building in the table first.");
      return;
    }
    int modelRow = table.convertRowIndexToModel(selected);
    String rsn = String.valueOf(tableModel.getValueAt(modelRow, 0));
    if (detailsHandler != null) {
      detailsHandler.accept(rsn);
    }
  }

  private void refreshFromApi() {
    if (liveCheck == null) {
      return;
    }
    refreshButton.setEnabled(false);
    statusLabel.setText("Contacting the City of Toronto API...");
    new SwingWorker<String, Void>() {
      @Override
      protected String doInBackground() {
        return liveCheck.get();
      }

      @Override
      protected void done() {
        refreshButton.setEnabled(true);
        try {
          statusLabel.setText(get());
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          statusLabel.setText("Live API check was interrupted.");
        } catch (ExecutionException ex) {
          Throwable cause = ex.getCause() == null ? ex : ex.getCause();
          statusLabel.setText("Live API check failed: " + cause.getMessage());
        }
      }
    }.execute();
  }

  private void render() {
    statusLabel.setText(viewModel.getMessage());
    tableModel.setRowCount(0);
    for (SearchResult result : viewModel.getResults()) {
      tableModel.addRow(new Object[] {
          result.getRsn(),
          result.getAddress(),
          wardLabel(result),
          DisplayFormat.score(result.getLatestScore()),
          DisplayFormat.text(result.getEvaluationYear())});
    }
  }

  private static String wardLabel(SearchResult result) {
    if (result.getWardName().isBlank()) {
      return result.getWard();
    }
    return result.getWard() + " - " + result.getWardName();
  }
}
