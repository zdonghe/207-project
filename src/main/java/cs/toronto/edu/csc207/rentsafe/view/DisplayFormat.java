package cs.toronto.edu.csc207.rentsafe.view;

/** Small formatting helpers shared by the Swing views. */
final class DisplayFormat {
  private DisplayFormat() {
  }

  static String text(Object value) {
    if (value == null) {
      return "N/A";
    }
    String text = value.toString();
    return text.isBlank() ? "N/A" : text;
  }

  static String score(Integer value) {
    return value == null ? "N/A" : value + "/100";
  }
}
