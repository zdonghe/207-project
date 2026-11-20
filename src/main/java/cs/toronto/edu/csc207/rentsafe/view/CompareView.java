package cs.toronto.edu.csc207.rentsafe.view;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.compare.CompareController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.compare.CompareViewModel;
import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareRow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Compare screen: two building selectors and a side-by-side metric table. */
public final class CompareView extends JPanel {
  private final CompareController controller;
  private final CompareViewModel viewModel;
  private final JComboBox<BuildingOption> leftBox = new JComboBox<>();
  private final JComboBox<BuildingOption> rightBox = new JComboBox<>();
  private final JLabel statusLabel = new JLabel(" ");
  private final DefaultTableModel tableModel = new DefaultTableModel(
      new String[] {"Metric", "Left", "Right"}, 0);
  private final JTable table = new JTable(tableModel);

  /** Creates the comparison screen from the buildings available in the cache. */
  public CompareView(
      CompareController controller, CompareViewModel viewModel, List<Building> buildings) {
    this.controller = controller;
    this.viewModel = viewModel;

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
    controls.add(new JLabel("Left building:"));
    controls.add(leftBox);
    controls.add(new JLabel("Right building:"));
    controls.add(rightBox);
    JButton compareButton = new JButton("Compare");
    compareButton.addActionListener(event -> compareSelected());
    controls.add(compareButton);
    add(controls, BorderLayout.NORTH);

    table.setFillsViewportHeight(true);
    table.setRowHeight(24);
    table.setDefaultEditor(Object.class, null);
    table.getTableHeader().setReorderingAllowed(false);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Comparison"));
    add(scrollPane, BorderLayout.CENTER);
    add(statusLabel, BorderLayout.SOUTH);

    for (Building building : buildings) {
      BuildingOption option = new BuildingOption(building.getRsn(), building.getDisplayName());
      leftBox.addItem(option);
      rightBox.addItem(option);
    }
    if (rightBox.getItemCount() > 1) {
      rightBox.setSelectedIndex(1);
    }
    viewModel.addPropertyChangeListener(event -> render());
    render();
  }

  private void compareSelected() {
    BuildingOption left = (BuildingOption) leftBox.getSelectedItem();
    BuildingOption right = (BuildingOption) rightBox.getSelectedItem();
    if (left == null || right == null) {
      statusLabel.setText("Add cached buildings before comparing.");
      return;
    }
    controller.compare(left.rsn(), right.rsn());
  }

  private void render() {
    statusLabel.setText(viewModel.getMessage());
    tableModel.setRowCount(0);
    for (CompareRow row : viewModel.getRows()) {
      tableModel.addRow(new Object[] {row.getLabel(), row.getLeftValue(), row.getRightValue()});
    }
    String leftTitle = viewModel.getLeftTitle().isBlank() ? "Left" : viewModel.getLeftTitle();
    String rightTitle = viewModel.getRightTitle().isBlank() ? "Right" : viewModel.getRightTitle();
    table.getColumnModel().getColumn(1).setHeaderValue(leftTitle);
    table.getColumnModel().getColumn(2).setHeaderValue(rightTitle);
    table.getTableHeader().repaint();
  }

  private record BuildingOption(String rsn, String label) {
    @Override
    public String toString() {
      return label;
    }
  }
}
