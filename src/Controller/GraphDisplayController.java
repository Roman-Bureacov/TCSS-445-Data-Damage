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
    /** Time the simulator runs for.*/
    private static final int SIXTY_SECONDS = 60;
    /** Seconds per Millisecond. */
    private static final double SEC_PER_MS = 1 / 1000d;
    /** Width of the graph.*/
    private static final double Y_WIDTH = 100;

    /** Graph for cumulative damage.*/
    @FXML private LineChart<Number, Number> myTotalDamageChart;
    /** X-axis for total damage.*/
    @FXML private NumberAxis myTotalDamageX;
    /** y-axis for total damage.*/
    @FXML private NumberAxis myTotalDamageY;
    /** Graph for average dps over time.*/
    @FXML private LineChart<Number, Number> myDPSChart;
    /** X-axis for average dps.*/
    @FXML private NumberAxis myDPSX;
    /** Y-axis for average dps.*/
    @FXML private NumberAxis myDPSY;

    /**
     * Initializes the graphs in the display window.
     */
    @FXML private void initialize() {
        myTotalDamageX.setAutoRanging(false);
        myTotalDamageX.setLowerBound(0);
        myTotalDamageX.setUpperBound(SIXTY_SECONDS);
        myTotalDamageY.setPrefWidth(Y_WIDTH);
        myTotalDamageChart.setCreateSymbols(false);

        myDPSX.setAutoRanging(false);
        myDPSX.setLowerBound(0);
        myDPSX.setUpperBound(SIXTY_SECONDS);
        myDPSY.setAutoRanging(false);
        myDPSY.setPrefWidth(Y_WIDTH);
        myDPSChart.setCreateSymbols(false);
    }

    /**
     * Takes a TimeSheet and adds the time-damage data to the total damage graph.
     *
     * @param theTimeSheet The theTimeSheet that has damage instance data that needs graphed.
     */
    public void renderDamage(final TimeSheet theTimeSheet) {
        myTotalDamageChart.getData().clear();
        if (theTimeSheet == null) {
            return;
        }

        XYChart.Series<Number, Number> cumulativeSeries = new XYChart.Series<>();
        cumulativeSeries.setName("Cumulative");

        XYChart.Series<Number, Number> instantSeries = new XYChart.Series<>();
        instantSeries.setName("Hit Damage");

        double cumulative = 0.0;
        cumulativeSeries.getData().add(new XYChart.Data<>(0.0, 0.0));
        instantSeries.getData().add(new XYChart.Data<>(0.0, 0.0));

        for (Object[] row : theTimeSheet) {
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

        myTotalDamageX.setLowerBound(0);
        myTotalDamageX.setUpperBound(SIXTY_SECONDS);

        myTotalDamageChart.setCreateSymbols(false);
        myTotalDamageChart.getData().addAll(cumulativeSeries, instantSeries);
    }


    /**
     * Renders the DPS chart based on the theTimesheet. This method
     * will convert the theTimesheet into its corresponding
     * DPS counterpart.
     * @param theTimesheet the theTimesheet to render the DPS on
     */
    public void renderDpsChart(final TimeSheet theTimesheet) {
        final TimeSheet DPSTimeSheet = theTimesheet.getDPSTimesheet(250);

        myDPSChart.getData().clear();
        if (theTimesheet == null) {
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
        double maxAvgDPSAfter1Sec = 0;
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

            if(tMs > 1000 && DPS > maxAvgDPSAfter1Sec) {
                maxAvgDPSAfter1Sec = DPS;
            }

            instantSeries.getData().add(new XYChart.Data<>(t, 0));
            instantSeries.getData().add(new XYChart.Data<>(t, DPS));
        }

        average = average/count;

        myDPSX.setLowerBound(0);
        myDPSX.setUpperBound(SIXTY_SECONDS);
        myDPSY.setLowerBound(0);
        myDPSY.setUpperBound(maxAvgDPSAfter1Sec);
        myDPSY.setTickUnit(average / 4);

        myDPSChart.setCreateSymbols(false);
        myDPSChart.getData().addAll(DPSSeries, instantSeries);
    }
}