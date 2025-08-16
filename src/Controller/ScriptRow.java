package Controller;

import javafx.beans.property.*;

public class ScriptRow {
    private final StringProperty saveName = new SimpleStringProperty("");
    private final StringProperty kineticWeaponType = new SimpleStringProperty("");
    private final StringProperty kineticWeaponFrame = new SimpleStringProperty("");
    private final StringProperty energyWeaponType = new SimpleStringProperty("");
    private final StringProperty energyWeaponFrame = new SimpleStringProperty("");
    private final StringProperty powerWeaponType = new SimpleStringProperty("");
    private final StringProperty powerWeaponFrame = new SimpleStringProperty("");
    private final DoubleProperty avgDPS = new SimpleDoubleProperty(0.0);
    private final IntegerProperty totalDPS = new SimpleIntegerProperty(0);
    private final StringProperty saveDate = new SimpleStringProperty("");

    public String getSaveName() {
        return saveName.get();
    }

    public StringProperty saveNameProperty() {
        return saveName;
    }

    public String getKineticWeaponType() {
        return kineticWeaponType.get();
    }

    public StringProperty kineticWeaponTypeProperty() {
        return kineticWeaponType;
    }

    public String getKineticWeaponFrame() {
        return kineticWeaponFrame.get();
    }

    public StringProperty kineticWeaponFrameProperty() {
        return kineticWeaponFrame;
    }

    public String getEnergyWeaponType() {
        return energyWeaponType.get();
    }

    public StringProperty energyWeaponTypeProperty() {
        return energyWeaponType;
    }

    public String getEnergyWeaponFrame() {
        return energyWeaponFrame.get();
    }

    public StringProperty energyWeaponFrameProperty() {
        return energyWeaponFrame;
    }

    public String getPowerWeaponType() {
        return powerWeaponType.get();
    }

    public StringProperty powerWeaponTypeProperty() {
        return powerWeaponType;
    }

    public String getPowerWeaponFrame() {
        return powerWeaponFrame.get();
    }

    public StringProperty powerWeaponFrameProperty() {
        return powerWeaponFrame;
    }

    public double getAvgDPS() {
        return avgDPS.get();
    }

    public DoubleProperty avgDPSProperty() {
        return avgDPS;
    }

    public int getTotalDPS() {
        return totalDPS.get();
    }

    public IntegerProperty totalDPSProperty() {
        return totalDPS;
    }

    public String getSaveDate() {
        return saveDate.get();
    }

    public StringProperty saveDateProperty() {
        return saveDate;
    }

    public void setSaveName(String theSaveName) {
        saveName.set(theSaveName);
    }

    public void setKineticWeaponType(String theKineticWeaponType){
        kineticWeaponType.set(theKineticWeaponType);
    }

    public void setKineticWeaponFrame(String theKineticWeaponFrame){
        kineticWeaponFrame.set(theKineticWeaponFrame);
    }

    public void setEnergyWeaponType(String theEnergyWeaponType){
        energyWeaponType.set(theEnergyWeaponType);
    }

    public void setEnergyWeaponFrame(String theEnergyWeaponFrame){
        energyWeaponFrame.set(theEnergyWeaponFrame);
    }

    public void setPowerWeaponType(String thePowerWeaponType){
        powerWeaponType.set(thePowerWeaponType);
    }

    public void setPowerWeaponFrame(String thePowerWeaponFrame){
        powerWeaponFrame.set(thePowerWeaponFrame);
    }

    public void setAvgDPS(double theAvgDPS){
        avgDPS.set(theAvgDPS);
    }

    public void setTotalDPS(int theTotalDPS){
        totalDPS.set(theTotalDPS);
    }

    public void setSaveDate(String theSaveDate){
        saveDate.set(theSaveDate);
    }
}
