package cs.toronto.edu.csc207.rentsafe.usecase.save;

import cs.toronto.edu.csc207.rentsafe.entity.Favourite;
import java.util.List;

/** Persistence port for the tenant's shortlist. */
public interface FavouriteRepository {
  /** Returns every saved favourite. */
  List<Favourite> findAll();

  /** Saves a favourite, replacing any earlier entry for the same building. */
  void save(Favourite favourite);

  /** Removes the favourite for a building ID, if present. */
  void remove(String rsn);
}
