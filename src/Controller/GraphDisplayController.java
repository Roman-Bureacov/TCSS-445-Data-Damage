package Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.script.TimeSheet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphDisplayController {
    @FXML private LineChart<Number, Number> chart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML
    private void initialize() {
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(60);
        chart.setCreateSymbols(false);
    }

    public void renderDamage(TimeSheet timesheet) {
        chart.getData().clear();
        if (timesheet == null) {
            return;
        }

        XYChart.Series<Number, Number> cumulativeSeries = new XYChart.Series<>();
        cumulativeSeries.setName("Cumulative");

        XYChart.Series<Number, Number> instantSeries = new XYChart.Series<>();
        instantSeries.setName("Hit Damage");

        double cumulative = 0.0;
        cumulativeSeries.getData().add(new XYChart.Data<>(0.0, 0.0));
        instantSeries.getData().add(new XYChart.Data<>(0.0, 0.0));

        for (Object[] row : timesheet) {
            int tMs = (Integer) row[0];
            int dmg = (Integer) row[1];
            if (dmg <= 0){
                continue; // ignore non-damage events
            }

            double t = (tMs / 1000.0);

            // cumulative line
            cumulative += dmg;
            cumulativeSeries.getData().add(new XYChart.Data<>(t, cumulative));

            // "stem" spike at time t: baseline -> spike -> baseline
            instantSeries.getData().add(new XYChart.Data<>(t, 0));
            instantSeries.getData().add(new XYChart.Data<>(t, dmg));
            instantSeries.getData().add(new XYChart.Data<>(t, 0));
        }

        // extend to right edge
        cumulativeSeries.getData().add(new XYChart.Data<>(60.0, cumulative));
        instantSeries.getData().add(new XYChart.Data<>(60.0, 0));

        xAxis.setLowerBound(0);
        xAxis.setUpperBound(60);

        chart.setCreateSymbols(false); // optional: cleaner lines
        chart.getData().addAll(cumulativeSeries, instantSeries);
    }
}