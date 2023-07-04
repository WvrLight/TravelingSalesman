import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConvergencePlot extends Application {
    GeneticTSPSolver TSP = new GeneticTSPSolver();

    @Override public void start(Stage stage) {
        // Initialize chart variables
        final NumberAxis xAxis_path = new NumberAxis();
        final NumberAxis yAxis_path = new NumberAxis();

        final NumberAxis xAxis_cost = new NumberAxis();
        final NumberAxis yAxis_cost = new NumberAxis(500, 1500, 50);

        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis_path, yAxis_path);
        final LineChart<Number, Number> costChart = new LineChart<>(xAxis_cost, yAxis_cost);

        XYChart.Series<Number, Number> series_path = new XYChart.Series<>();
        XYChart.Series<Number, Number> series_cost = new XYChart.Series<>();

        // Initialize container for elements
        AnchorPane root = new AnchorPane();
        HBox charts = new HBox();

        // Add TSP point data to chart
        for (int i = 0; i < TSP.getFittestParent().getList().size(); i++) {
            series_path.getData().add(new XYChart.Data<>(TSP.getFittestParent().getPoint(i).getX(), TSP.getFittestParent().getPoint(i).getY()));
        }
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        // Add TSP cost data to chart
        for (int i = 0; i < TSP.MAX_GENERATIONS; i++) {
            series_cost.getData().add(new XYChart.Data<>(i, TSP.costPerGen[i]));
        }
        costChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        // Display chart
        lineChart.setTitle("Path of Fittest Generation");
        costChart.setTitle("Cost per Generation");

        charts.getChildren().add(lineChart);
        charts.getChildren().add(costChart);
        root.getChildren().add(charts);
        Scene scene  = new Scene(root, 1600, 900);
        lineChart.getData().add(series_path);
        costChart.getData().add(series_cost);
        lineChart.setPrefSize(800, 900);
        costChart.setPrefSize(800, 900);
        lineChart.setLegendVisible(false);
        costChart.setLegendVisible(false);

        stage.setScene(scene);
        stage.show();

        // Adding of labels to each point
        for (int i = 0; i < TSP.getFittestParent().getList().size(); i++) {
            Text text = new Text();
            text.setText(TSP.getFittestParent().getPoint(i).getName());
            text.setFont(Font.font("Segoe UI", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setX(xAxis_path.getDisplayPosition(TSP.getFittestParent().getPoint(i).getX()) + 35);
            text.setY(yAxis_path.getDisplayPosition(TSP.getFittestParent().getPoint(i).getY()) + 25);
            root.getChildren().add(text);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}