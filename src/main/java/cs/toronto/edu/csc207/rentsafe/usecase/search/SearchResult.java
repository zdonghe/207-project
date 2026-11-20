package cs.toronto.edu.csc207.rentsafe.usecase.search;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.entity.Evaluation;
import java.util.Optional;

/** One row in the search results table. */
public final class SearchResult {
  private final String rsn;
  private final String displayName;
  private final String address;
  private final String ward;
  private final String wardName;
  private final Integer latestScore;
  private final Integer evaluationYear;
  private final int evaluationCount;

  /** Creates a result row from a building and its latest evaluation. */
  public SearchResult(
      String rsn,
      String displayName,
      String address,
      String ward,
      String wardName,
      Integer latestScore,
      Integer evaluationYear,
      int evaluationCount) {
    this.rsn = rsn == null ? "" : rsn;
    this.displayName = displayName == null ? "" : displayName;
    this.address = address == null ? "" : address;
    this.ward = ward == null ? "" : ward;
    this.wardName = wardName == null ? "" : wardName;
    this.latestScore = latestScore;
    this.evaluationYear = evaluationYear;
    this.evaluationCount = evaluationCount;
  }

  /** Builds a result row from a building entity. */
  public static SearchResult from(Building building) {
    Optional<Evaluation> latest = building.getLatestEvaluation();
    Integer score = latest.map(Evaluation::getCurrentScore).orElse(null);
    Integer year = latest.map(Evaluation::getYear).orElse(null);
    return new SearchResult(
        building.getRsn(),
        building.getDisplayName(),
        building.getAddress(),
        building.getWard(),
        building.getWardName(),
        score,
        year,
        building.getEvaluationHistory().size());
  }

  public String getRsn() {
    return rsn;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getAddress() {
    return address;
  }

  public String getWard() {
    return ward;
  }

  public String getWardName() {
    return wardName;
  }

  public Integer getLatestScore() {
    return latestScore;
  }

  public Integer getEvaluationYear() {
    return evaluationYear;
  }

  public int getEvaluationCount() {
    return evaluationCount;
  }
}
