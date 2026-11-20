package cs.toronto.edu.csc207.rentsafe.entity;

/** A titled block of text inside a report. */
public final class ReportSection {
  private final String title;
  private final String content;

  /** Creates a report section with a heading and body text. */
  public ReportSection(String title, String content) {
    this.title = title == null ? "" : title.trim();
    this.content = content == null ? "" : content.trim();
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }
}
