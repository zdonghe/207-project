package cs.toronto.edu.csc207.rentsafe.view;

import cs.toronto.edu.csc207.rentsafe.entity.Favourite;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.save.SaveController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.save.SaveViewModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Saved screen: shortlist editor and CSV export action. */
public final class SaveView extends JPanel {
  private final SaveController controller;
  private final SaveViewModel viewModel;
  private final JTextField rsnField = new JTextField(12);
  private final JTextField noteField = new JTextField(22);
  private final JLabel statusLabel = new JLabel(" ");
  private final DefaultTableModel tableModel = new DefaultTableModel(
      new String[] {"Building ID", "Address", "Note", "Added"}, 0);
  private final JTable table = new JTable(tableModel);

  /** Creates the saved shortlist screen and loads existing favourites. */
  public SaveView(SaveController controller, SaveViewModel viewModel) {
    this.controller = controller;
    this.viewModel = viewModel;

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
    controls.add(new JLabel("Building ID:"));
    controls.add(rsnField);
    controls.add(new JLabel("Note:"));
    controls.add(noteField);
    JButton saveButton = new JButton("Save favourite");
    saveButton.addActionListener(event -> save());
    controls.add(saveButton);
    JButton removeButton = new JButton("Remove selected");
    removeButton.addActionListener(event -> removeSelected());
    controls.add(removeButton);
    JButton exportButton = new JButton("Export CSV");
    exportButton.addActionListener(event -> controller.export());
    controls.add(exportButton);
    add(controls, BorderLayout.NORTH);

    table.setFillsViewportHeight(true);
    table.setRowHeight(24);
    table.setDefaultEditor(Object.class, null);
    table.getTableHeader().setReorderingAllowed(false);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Your shortlist"));
    add(scrollPane, BorderLayout.CENTER);
    add(statusLabel, BorderLayout.SOUTH);

    viewModel.addPropertyChangeListener(event -> render());
    controller.load();
  }

  private void save() {
    controller.save(rsnField.getText(), noteField.getText());
    noteField.setText("");
  }

  private void removeSelected() {
    int selected = table.getSelectedRow();
    if (selected < 0) {
      statusLabel.setText("Select a saved building to remove.");
      return;
    }
    int modelRow = table.convertRowIndexToModel(selected);
    controller.remove(String.valueOf(tableModel.getValueAt(modelRow, 0)));
  }

  private void render() {
    statusLabel.setText(viewModel.getMessage());
    tableModel.setRowCount(0);
    for (Favourite favourite : viewModel.getFavourites()) {
      tableModel.addRow(new Object[] {
          favourite.getRsn(),
          favourite.getBuilding().getAddress(),
          favourite.getNote(),
          favourite.getAddedAt().toLocalDate().toString()});
    }
  }
}
