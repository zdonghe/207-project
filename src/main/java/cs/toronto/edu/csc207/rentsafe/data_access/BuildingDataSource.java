package cs.toronto.edu.csc207.rentsafe.data_access;

import cs.toronto.edu.csc207.rentsafe.entity.Building;
import java.util.List;
import java.util.Optional;

/** Read-only access to building records, whether live or cached. */
public interface BuildingDataSource {
  /** Returns buildings matching a free-text address, ward, or building ID query. */
  List<Building> search(String query);

  /** Returns the building with the exact registration number, when present. */
  Optional<Building> findByRsn(String rsn);
}
