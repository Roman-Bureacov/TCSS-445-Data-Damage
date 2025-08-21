package Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.script.TimeSheet;

/**
 * Controller for rendering the damage and DPS charts in the scripts editor page
 * @author Kaleb Agnostou
 * @author Roman Bureacov
 * @version August 2025
 */
public class GraphDisplayController {
    private static final int SIXTY_SECONDS = 60;
    private static final double SEC_PER_MS = 1 / 1000d;
    private static final double Y_WIDTH = 100;
    
    @FXML private LineChart<Number, Number> totalDamageChart;
    @FXML private NumberAxis totalDamageX;
    @FXML private NumberAxis totalDamageY;
    
    @FXML private LineChart<Number, Number> DPSChart;
    @FXML private NumberAxis DPSX;
    @FXML private NumberAxis DPSY;

    @FXML
    private void initialize() {
        totalDamageX.setAutoRanging(false);
        totalDamageX.setLowerBound(0);
        totalDamageX.setUpperBound(SIXTY_SECONDS);
        totalDamageY.setPrefWidth(Y_WIDTH);
        totalDamageChart.setCreateSymbols(false);

        DPSX.setAutoRanging(false);
        DPSX.setLowerBound(0);
        DPSX.setUpperBound(SIXTY_SECONDS);
        DPSY.setAutoRanging(false);
        DPSY.setPrefWidth(Y_WIDTH);
        DPSChart.setCreateSymbols(false);
    }

    public void renderDamage(final TimeSheet timesheet) {
        totalDamageChart.getData().clear();
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
                continue;
            }

            double t = (tMs * SEC_PER_MS);

            cumulative += dmg;
            cumulativeSeries.getData().add(new XYChart.Data<>(t, cumulative));

            instantSeries.getData().add(new XYChart.Data<>(t, 0));
            instantSeries.getData().add(new XYChart.Data<>(t, dmg));
            instantSeries.getData().add(new XYChart.Data<>(t, 0));
        }

        cumulativeSeries.getData().add(new XYChart.Data<>(SIXTY_SECONDS, cumulative));
        instantSeries.getData().add(new XYChart.Data<>(SIXTY_SECONDS, 0));

        totalDamageX.setLowerBound(0);
        totalDamageX.setUpperBound(SIXTY_SECONDS);

        totalDamageChart.setCreateSymbols(false); // optional: cleaner lines
        totalDamageChart.getData().addAll(cumulativeSeries, instantSeries);
    }


    /**
     * Renders the DPS chart based on the timesheet. This method
     * will convert the timesheet into its corresponding
     * DPS counterpart.
     * @param timesheet the timesheet to render the DPS on
     */
    public void renderDpsChart(final TimeSheet timesheet) {
        final TimeSheet DPSTimeSheet = timesheet.getDPSTimesheet(250);

        DPSChart.getData().clear();
        if (timesheet == null) {
            return;
        }

        XYChart.Series<Number, Number> DPSSeries = new XYChart.Series<>();
        DPSSeries.setName("DPS");

        XYChart.Series<Number, Number> instantSeries = new XYChart.Series<>();
        instantSeries.setName("Hit Damage");

        DPSSeries.getData().add(new XYChart.Data<>(0.0, 0.0));
        instantSeries.getData().add(new XYChart.Data<>(0.0, 0.0));

        double average = 0;
        int count = 0;
        for (Object[] row : DPSTimeSheet) {
            int tMs = (Integer) row[0];
            double DPS = (Double) row[1];
            if (DPS <= 0) {
                continue;
            }

            average += DPS;
            count++;

            final double t = (tMs * SEC_PER_MS);

            DPSSeries.getData().add(new XYChart.Data<>(t, DPS));

            instantSeries.getData().add(new XYChart.Data<>(t, 0));
            instantSeries.getData().add(new XYChart.Data<>(t, DPS));
        }

        average = average/count;

        DPSX.setLowerBound(0);
        DPSX.setUpperBound(SIXTY_SECONDS);
        DPSY.setLowerBound(0);
        DPSY.setUpperBound(average * 2);
        DPSY.setTickUnit(average / 4);

        DPSChart.setCreateSymbols(false); // optional: cleaner lines
        DPSChart.getData().addAll(DPSSeries, instantSeries);
    }

}