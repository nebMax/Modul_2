package Modul_2;

import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class ChartController {

    private ScatterChart<Number, Number> scatterChart;

    public ChartController() {
        // Визначте вісі для діаграми
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        // Створіть ScatterChart з визначеними вісями
        scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Calories vs Age");

        // Додайте маркери для даних
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Results");
        scatterChart.getData().add(series);
    }

    public ScatterChart<Number, Number> getScatterChart() {
        return scatterChart;
    }

    public void updateChart(ObservableList<Calc.Person> personList) {
        scatterChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Results");

        for (Calc.Person person : personList) {
            series.getData().add(new XYChart.Data<>(person.getAge(), person.getCalories()));
        }

        scatterChart.getData().add(series);
    }
}