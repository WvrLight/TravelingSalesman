import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class ScatterPlot extends Application {
    Path coords = null;

    @Override public void start(Stage stage) {
        // Initialize chart variables
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // Initialize container for elements
        AnchorPane root = new AnchorPane();


        // Add TSP point data to chart
        for (int i = 0; i < coords.getList().size(); i++) {
            series.getData().add(new XYChart.Data<>(coords.getPoint(i).getX(), coords.getPoint(i).getY()));
        }
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        // Display chart
        root.getChildren().add(lineChart);
        lineChart.setPrefSize(1024, 768);
        Scene scene  = new Scene(root, 1024, 768);
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);

        stage.setScene(scene);
        stage.show();

        // Adding of labels to each point
        for (int i = 0; i < coords.getList().size(); i++) {
            Text text = new Text();
            text.setText(coords.getPoint(i).getName());
            text.setFont(Font.font("Segoe UI", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setX(xAxis.getDisplayPosition(coords.getPoint(i).getX()) + 40);
            text.setY(yAxis.getDisplayPosition(coords.getPoint(i).getY()));
            root.getChildren().add(text);
        }
    }

    ScatterPlot(Path TSP, String[] args) {
        coords = TSP;

        launch(args);
    }
 
    // public static void main(String[] args) {
    //     launch(args);
    // }
}