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
        if (timesheet == null) return;

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        int maxMs = 0;
        for (Object[] row : timesheet) {
            if (row == null || row.length < 2) continue;
            if (!(row[0] instanceof Integer) || !(row[1] instanceof Integer)) continue;
            int tMs = (Integer) row[0];
            if (tMs > maxMs) maxMs = tMs;
        }
        double maxSec = maxMs / 1000.0;
        double scale = (maxSec > 0) ? (60.0 / maxSec) : 1.0;

        double cumulative = 0.0;
        series.getData().add(new XYChart.Data<>(0.0, 0.0));

        for (Object[] row : timesheet) {
            if (row == null || row.length < 2) continue;
            if (!(row[0] instanceof Integer) || !(row[1] instanceof Integer)) continue;
            int tMs = (Integer) row[0];
            int dmg = (Integer) row[1];
            if (dmg <= 0) continue;
            cumulative += dmg;
            double tSecScaled = (tMs / 1000.0) * scale;
            series.getData().add(new XYChart.Data<>(tSecScaled, cumulative));
        }

        xAxis.setLowerBound(0);
        xAxis.setUpperBound(60);
        chart.getData().add(series);
    }
}