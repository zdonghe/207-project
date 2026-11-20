package cs.toronto.edu.csc207.rentsafe.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Main application window with navigation and a card layout for the four screens. */
public final class MainFrame extends JFrame {
  private final CardLayout cardLayout = new CardLayout();
  private final JPanel cards = new JPanel(cardLayout);
  private final Map<String, JButton> navButtons = new HashMap<>();

  /** Creates the main window and its navigation chrome. */
  public MainFrame() {
    super("RentSafeTO Explorer");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1100, 700);
    setMinimumSize(new Dimension(900, 560));
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    add(buildNavigation(), BorderLayout.WEST);
    add(cards, BorderLayout.CENTER);
    add(buildFooter(), BorderLayout.SOUTH);
  }

  /** Registers a screen under a card name. */
  public void addCard(String name, JComponent component) {
    cards.add(component, name);
  }

  /** Shows a registered card and marks its navigation button active. */
  public void showCard(String name) {
    cardLayout.show(cards, name);
    for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
      boolean active = entry.getKey().equals(name);
      Font font = entry.getValue().getFont();
      entry.getValue().setFont(font.deriveFont(active ? Font.BOLD : Font.PLAIN));
    }
  }

  private JPanel buildNavigation() {
    JPanel navigation = new JPanel();
    navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
    navigation.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));
    navigation.setPreferredSize(new Dimension(190, 0));
    navigation.setBackground(new Color(245, 247, 250));

    JLabel title = new JLabel("<html><b>RentSafeTO</b><br>Explorer</html>");
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    title.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
    navigation.add(title);

    navigation.add(navButton("Search", "search"));
    navigation.add(Box.createVerticalStrut(6));
    navigation.add(navButton("Detail", "detail"));
    navigation.add(Box.createVerticalStrut(6));
    navigation.add(navButton("Compare", "compare"));
    navigation.add(Box.createVerticalStrut(6));
    navigation.add(navButton("Saved", "saved"));
    navigation.add(Box.createVerticalGlue());

    JLabel source = new JLabel("<html>Data: City of<br>Toronto Open Data</html>");
    source.setAlignmentX(Component.LEFT_ALIGNMENT);
    source.setForeground(new Color(90, 96, 105));
    navigation.add(source);
    return navigation;
  }

  private JButton navButton(String label, String card) {
    JButton button = new JButton(label);
    button.setAlignmentX(Component.LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.addActionListener(event -> showCard(card));
    navButtons.put(card, button);
    return button;
  }

  private JPanel buildFooter() {
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
    footer.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    JLabel label =
        new JLabel("Independent student project. Not affiliated with the City of Toronto.");
    label.setForeground(new Color(100, 105, 112));
    footer.add(label);
    return footer;
  }
}
