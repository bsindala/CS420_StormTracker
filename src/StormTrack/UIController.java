package StormTrack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.geotools.feature.SchemaException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class UIController extends VBox{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="heatmap"
    private Button heatmap = new Button(); // Value injected by FXMLLoader

    @FXML // fx:id="stormtrack"
    private Button stormtrack = new Button(); // Value injected by FXMLLoader

    @FXML // fx:id="latitude"
    private TextField latitude = new TextField(); // Value injected by FXMLLoader

    @FXML // fx:id="longitude"
    private TextField longitude = new TextField(); // Value injected by FXMLLoader

    @FXML // fx:id="choicebox"
    private ChoiceBox<String> choicebox = new ChoiceBox<>(); // Value injected by FXMLLoader

    @FXML // fx:id="go"
    private Button go = new Button(); // Value injected by FXMLLoader

    @FXML // fx:id="listView"
    private ListView<String> listView = new ListView<>(); // Value injected by FXMLLoader

    @FXML // fx:id="info"
    private TextArea info = new TextArea(); // Value injected by FXMLLoader

    @FXML // fx:id="canvas"
    private Canvas canvas;// Value injected by FXMLLoader

    private final StormController stormController = new StormController();
    private final ArrayList storms = new ArrayList(stormController.getStormIDs());
    private final ObservableList<String> masterStormIDList = FXCollections.observableArrayList(storms);
    private final ArrayList years = new ArrayList(stormController.getYears());
    private final MapCanvas map = new MapCanvas(800, 600);

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert heatmap != null : "fx:id=\"heatmap\" was not injected: check your FXML file 'Window.fxml'.";
        assert stormtrack != null : "fx:id=\"stormtrack\" was not injected: check your FXML file 'Window.fxml'.";
        assert latitude != null : "fx:id=\"latitude\" was not injected: check your FXML file 'Window.fxml'.";
        assert longitude != null : "fx:id=\"longitude\" was not injected: check your FXML file 'Window.fxml'.";
        assert choicebox != null : "fx:id=\"choicebox\" was not injected: check your FXML file 'Window.fxml'.";
        assert go != null : "fx:id=\"go\" was not injected: check your FXML file 'Window.fxml'.";
        assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'Window.fxml'.";
        assert info != null : "fx:id=\"info\" was not injected: check your FXML file 'Window.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'Window.fxml'.";

        //Initial data setup/cleanup
        years.add(0,"All years");
        FXCollections.sort(masterStormIDList);

        //Set up and assign the map canvas to the Borderpane's center.
        Pane anchor = (Pane) canvas.getParent();
        ( (BorderPane) anchor ).setCenter( map.getCanvas());

        //Populate the years in pulldown menu from the StormData set.
        setupChoiceBox();

        //Populate the storms in the listView from the StormData set.
        listView.getItems().addAll(masterStormIDList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Configure "Go" button behavior
        setupGoButton();

        //Configure "Storm Track" button behavior
        setupStormTrackButton();

        //Configure list view
        setupListView();

        //Configure "Heat Map" button
        setupHeatView();
    }

    private void setupChoiceBox() {
        choicebox.getItems().addAll(FXCollections.observableArrayList(years));
        choicebox.getSelectionModel().selectFirst();
        choicebox.getSelectionModel().selectedIndexProperty()
                .addListener((ov, value, newValue) -> {
                    listView.getItems().clear();

                    if ( newValue.intValue() == 0 )
                        listView.getItems().addAll(masterStormIDList);
                    else {
                        int listValue = (int) years.get(newValue.intValue());
                        ArrayList currentStorms = (ArrayList) storms.stream().filter(
                                line -> ((String) line).substring(0,4).equals(listValue + "") )
                                .collect(Collectors.toList());
                        ObservableList current = FXCollections.observableArrayList(currentStorms);
                        FXCollections.sort(current);
                        listView.getItems().addAll(current);
                    }
                });
    }

    private void setupGoButton() {
        go.setDefaultButton(true);
        go.setOnAction(e -> {
            String latitudeData = latitude.getText();
            String longitudeData = longitude.getText();

            if ( latitudeData.isEmpty() && longitudeData.isEmpty())
            {
                try {
                    map.resetView();
                } catch (FactoryException e1) {
                    e1.printStackTrace();
                }
            }
            else if ( latitudeData.isEmpty())
            {
               popupWarning("Please provide a latitude value");
            }
            else if ( longitudeData.isEmpty()){
                popupWarning("Please provide a longitude value");
            }
            else {
                double latCoord;
                double longCoord;
                try {
                    latCoord = Double.parseDouble(latitudeData);
                    longCoord = Double.parseDouble(longitudeData);

                    if ( latCoord > 90 )
                        latCoord = 90;
                    if ( latCoord < -90 )
                        latCoord = -90;
                    if ( longCoord > 180 )
                        longCoord = 180;
                    if ( longCoord < -180 )
                        longCoord = -180;

                    try {
                        map.showArea(latCoord,longCoord);
                    } catch (FactoryException e2) {
                        e2.printStackTrace();
                    }
                }
                catch (NumberFormatException e1) {
                    popupWarning( "Invalid entry");
                }

            }
        });
    }

    private void setupStormTrackButton() {
        stormtrack.setOnAction( e -> {
            ObservableList<String> selected = listView.getSelectionModel().getSelectedItems();
            List<Storm> stormList = selected.stream()
                    .map(stormController::getStormByID)
                    .collect(Collectors.toList());

            try {
                map.drawLines( stormList );
            } catch (SchemaException e1) {
                e1.printStackTrace();
            }

        });
    }

    private void setupListView() {
        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            try {
                Storm storm = stormController.getStormByID(newValue);
                String data = storm + "\n"
                        + storm.getHistory()
                        .stream()
                        .map(DatePosition::toString)
                        .collect(Collectors.joining());

                info.setText(data);
            }
            catch (NullPointerException e1) {
                //Do nothing, multiple objects were selected.
            }
        });
    }

    private void setupHeatView() {
        heatmap.setOnAction( e -> {
            ObservableList<String> selected = listView.getSelectionModel().getSelectedItems();

//            if ( selected.size() < 2) {
            try {
                map.generateHeatMap(stormController.getAllStorms());
            } catch (TransformException e1) {
                e1.printStackTrace();
            } catch (SchemaException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
//            }
//            else {
//
//            }

        });
    }

    private void popupWarning(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText( s );

        alert.showAndWait();
    }

}
