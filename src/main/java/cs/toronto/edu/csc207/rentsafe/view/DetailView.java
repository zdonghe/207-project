package cs.toronto.edu.csc207.rentsafe.view;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.detail.DetailController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.detail.DetailViewModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Detail screen: building profile and ordered evaluation history. */
public final class DetailView extends JPanel {
  private final DetailController controller;
  private final DetailViewModel viewModel;
  private final JTextField rsnField = new JTextField(16);
  private final JLabel titleLabel = new JLabel("Select a building from Search.");
  private final JLabel statusLabel = new JLabel(" ");
  private final JTextArea profileArea = new JTextArea(9, 36);
  private final DefaultTableModel historyModel = new DefaultTableModel(
      new String[] {"Year", "Completed", "Current", "Proactive", "Reactive", "Deficiencies"}, 0);
  private final JTable historyTable = new JTable(historyModel);

  /** Creates the detail screen. */
  public DetailView(DetailController controller, DetailViewModel viewModel) {
    this.controller = controller;
    this.viewModel = viewModel;

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    final JPanel top = new JPanel(new BorderLayout(8, 8));
    JPanel lookup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
    JButton loadButton = new JButton("Load building");
    loadButton.addActionListener(event -> load());
    rsnField.addActionListener(event -> load());
    lookup.add(new JLabel("Building ID (RSN):"));
    lookup.add(rsnField);
    lookup.add(loadButton);
    top.add(lookup, BorderLayout.NORTH);
    top.add(titleLabel, BorderLayout.SOUTH);
    add(top, BorderLayout.NORTH);

    JPanel content = new JPanel(new GridLayout(2, 1, 8, 8));
    content.add(buildProfilePanel());
    content.add(buildHistoryPanel());
    add(content, BorderLayout.CENTER);
    add(statusLabel, BorderLayout.SOUTH);

    viewModel.addPropertyChangeListener(event -> render());
    render();
  }

  /** Loads a building by registration number and switches this view to its data. */
  public void showBuilding(String rsn) {
    rsnField.setText(rsn);
    controller.load(rsn);
  }

  private JPanel buildProfilePanel() {
    profileArea.setEditable(false);
    profileArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
    profileArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Building profile"));
    panel.add(new JScrollPane(profileArea), BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildHistoryPanel() {
    historyTable.setFillsViewportHeight(true);
    historyTable.setRowHeight(24);
    historyTable.setDefaultEditor(Object.class, null);
    historyTable.getTableHeader().setReorderingAllowed(false);
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Evaluation history"));
    panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
    return panel;
  }

  private void load() {
    controller.load(rsnField.getText());
  }

  private void render() {
    statusLabel.setText(viewModel.getMessage());
    historyModel.setRowCount(0);
    Building building = viewModel.getBuilding();
    if (building == null) {
      titleLabel.setText("No building loaded.");
      profileArea.setText(viewModel.getMessage());
      return;
    }
    titleLabel.setText(building.getDisplayName());
    profileArea.setText(profileText(building));
    profileArea.setCaretPosition(0);
    for (Evaluation evaluation : viewModel.getHistory()) {
      historyModel.addRow(new Object[] {
          evaluation.getYear(),
          evaluation.getCompletedOn() == null
              ? "N/A" : evaluation.getCompletedOn().toString(),
          DisplayFormat.score(evaluation.getCurrentScore()),
          DisplayFormat.text(evaluation.getProactiveScore()),
          DisplayFormat.text(evaluation.getReactiveScore()),
          evaluation.getDeficiencies().size()});
    }
  }

  private static String profileText(Building building) {
    StringBuilder text = new StringBuilder();
    text.append("Building ID: ").append(building.getRsn()).append('\n');
    text.append("Address: ").append(building.getAddress()).append('\n');
    text.append("Ward: ").append(DisplayFormat.text(building.getWard()));
    if (!building.getWardName().isBlank()) {
      text.append(" - ").append(building.getWardName());
    }
    text.append('\n');
    text.append("Year built: ").append(DisplayFormat.text(building.getYearBuilt())).append('\n');
    text.append("Storeys: ").append(DisplayFormat.text(building.getStoreys())).append('\n');
    text.append("Units: ").append(DisplayFormat.text(building.getUnits())).append('\n');
    text.append("Property type: ").append(DisplayFormat.text(building.getPropertyType()))
        .append('\n');
    text.append("Management: ").append(DisplayFormat.text(building.getManagementCompany()))
        .append('\n');
    text.append("Coordinates: ").append(DisplayFormat.text(building.getLatitude())).append(", ")
        .append(DisplayFormat.text(building.getLongitude())).append('\n');
    text.append("Registered: ").append(building.isRegistered() ? "yes" : "no").append('\n');
    return text.toString();
  }
}
