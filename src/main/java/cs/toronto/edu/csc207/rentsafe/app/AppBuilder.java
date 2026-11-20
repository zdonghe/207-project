package cs.toronto.edu.csc207.rentsafe.app;

import cs.toronto.edu.csc207.rentsafe.data_access.BuildingDataSource;
import cs.toronto.edu.csc207.rentsafe.data_access.CachedBuildingRepository;
import cs.toronto.edu.csc207.rentsafe.data_access.CkanApiClient;
import cs.toronto.edu.csc207.rentsafe.data_access.CsvReportWriter;
import cs.toronto.edu.csc207.rentsafe.data_access.JsonFavouriteRepository;
import cs.toronto.edu.csc207.rentsafe.entity.Building;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.compare.CompareController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.compare.ComparePresenter;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.compare.CompareViewModel;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.detail.DetailController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.detail.DetailPresenter;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.detail.DetailViewModel;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.save.SaveController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.save.SavePresenter;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.save.SaveViewModel;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.search.SearchController;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.search.SearchPresenter;
import cs.toronto.edu.csc207.rentsafe.interface_adapter.search.SearchViewModel;
import cs.toronto.edu.csc207.rentsafe.usecase.compare.CompareInteractor;
import cs.toronto.edu.csc207.rentsafe.usecase.detail.DetailInteractor;
import cs.toronto.edu.csc207.rentsafe.usecase.save.FavouriteRepository;
import cs.toronto.edu.csc207.rentsafe.usecase.save.ReportWriter;
import cs.toronto.edu.csc207.rentsafe.usecase.save.SaveInteractor;
import cs.toronto.edu.csc207.rentsafe.usecase.search.SearchInteractor;
import cs.toronto.edu.csc207.rentsafe.view.CompareView;
import cs.toronto.edu.csc207.rentsafe.view.DetailView;
import cs.toronto.edu.csc207.rentsafe.view.MainFrame;
import cs.toronto.edu.csc207.rentsafe.view.SaveView;
import cs.toronto.edu.csc207.rentsafe.view.SearchView;
import java.util.List;

/** Composition root that wires data sources, use cases, presenters, and views. */
public final class AppBuilder {
  private final MainFrame frame = new MainFrame();
  private final CkanApiClient liveApi = new CkanApiClient();

  /** Builds every screen and connects it to the use cases and data layer. */
  public AppBuilder() {
    BuildingDataSource cached = new CachedBuildingRepository();
    FavouriteRepository favourites = new JsonFavouriteRepository(cached);
    ReportWriter reportWriter = new CsvReportWriter();

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchInteractor searchInteractor =
        new SearchInteractor(cached, new SearchPresenter(searchViewModel));
    SearchController searchController = new SearchController(searchInteractor);

    DetailViewModel detailViewModel = new DetailViewModel();
    DetailInteractor detailInteractor =
        new DetailInteractor(cached, new DetailPresenter(detailViewModel));
    DetailController detailController = new DetailController(detailInteractor);
    DetailView detailView = new DetailView(detailController, detailViewModel);

    CompareViewModel compareViewModel = new CompareViewModel();
    CompareInteractor compareInteractor =
        new CompareInteractor(cached, new ComparePresenter(compareViewModel));
    CompareController compareController = new CompareController(compareInteractor);
    CompareView compareView =
        new CompareView(compareController, compareViewModel, cached.search(""));

    SaveViewModel saveViewModel = new SaveViewModel();
    SaveInteractor saveInteractor =
        new SaveInteractor(cached, favourites, reportWriter, new SavePresenter(saveViewModel));
    SaveController saveController = new SaveController(saveInteractor);
    SaveView saveView = new SaveView(saveController, saveViewModel);

    SearchView searchView = new SearchView(
        searchController,
        searchViewModel,
        this::liveCheck,
        rsn -> {
          detailView.showBuilding(rsn);
          frame.showCard("detail");
        });

    frame.addCard("search", searchView);
    frame.addCard("detail", detailView);
    frame.addCard("compare", compareView);
    frame.addCard("saved", saveView);
  }

  /** Shows the main window and returns it. */
  public MainFrame build() {
    frame.showCard("search");
    frame.setVisible(true);
    return frame;
  }

  private String liveCheck() {
    List<Building> buildings = liveApi.search("BALLIOL");
    String message = "Live API returned " + liveApi.getLastRecordCount() + " record(s) for "
        + buildings.size() + " building(s).";
    System.out.println(message);
    return message;
  }
}
