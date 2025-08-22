package Controller;

import javafx.beans.property.*;

/**
 * A row containing scripts information that will be displayed in the scripts page.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class ScriptRow {
    /** The id of the sim script. */
    private final IntegerProperty myScriptId = new SimpleIntegerProperty(0);
    /** The name of the sim. */
    private final StringProperty mySaveName = new SimpleStringProperty("");
    /** The type of kinetic weapon used in the sim. */
    private final StringProperty myKineticWeaponType = new SimpleStringProperty("");
    /** The frame of the kinetic weapon used in the sim. */
    private final StringProperty myKineticWeaponFrame = new SimpleStringProperty("");
    /** The type of energy weapon used in the sim. */
    private final StringProperty myEnergyWeaponType = new SimpleStringProperty("");
    /** The frame of the energy weapon used in the sim. */
    private final StringProperty myEnergyWeaponFrame = new SimpleStringProperty("");
    /** The type of power weapon used in the sim. */
    private final StringProperty myPowerWeaponType = new SimpleStringProperty("");
    /** The frame of the power weapon used in the sim. */
    private final StringProperty myPowerWeaponFrame = new SimpleStringProperty("");
    /** The average DPS in the sim. */
    private final DoubleProperty myAvgDPS = new SimpleDoubleProperty(0.0);
    /** The total cumulative damage in the sim. */
    private final IntegerProperty myTotalDPS = new SimpleIntegerProperty(0);
    /** The date the sim was saved. */
    private final StringProperty mySaveDate = new SimpleStringProperty("");

    public int getScriptId() {
        return myScriptId.get();
    }

    public String getSaveName() {
        return mySaveName.get();
    }

    public StringProperty mySaveNameProperty() {
        return mySaveName;
    }

    public String getKineticWeaponType() {
        return myKineticWeaponType.get();
    }

    public StringProperty myKineticWeaponTypeProperty() {
        return myKineticWeaponType;
    }

    public String getKineticWeaponFrame() {
        return myKineticWeaponFrame.get();
    }

    public StringProperty myKineticWeaponFrameProperty() {
        return myKineticWeaponFrame;
    }

    public String getEnergyWeaponType() {
        return myEnergyWeaponType.get();
    }

    public StringProperty myEnergyWeaponTypeProperty() {
        return myEnergyWeaponType;
    }

    public String getEnergyWeaponFrame() {
        return myEnergyWeaponFrame.get();
    }

    public StringProperty myEnergyWeaponFrameProperty() {
        return myEnergyWeaponFrame;
    }

    public String getPowerWeaponType() {
        return myPowerWeaponType.get();
    }

    public StringProperty myPowerWeaponTypeProperty() {
        return myPowerWeaponType;
    }

    public String getPowerWeaponFrame() {
        return myPowerWeaponFrame.get();
    }

    public StringProperty myPowerWeaponFrameProperty() {
        return myPowerWeaponFrame;
    }

    public double getMyAvgDPS() {
        return myAvgDPS.get();
    }

    public DoubleProperty avgDPSProperty() {
        return myAvgDPS;
    }

    public int getTotalDPS() {
        return myTotalDPS.get();
    }

    public IntegerProperty totalDPSProperty() {
        return myTotalDPS;
    }

    public String getSaveDate() {
        return mySaveDate.get();
    }

    public StringProperty mySaveDateProperty() {
        return mySaveDate;
    }

    public void setScriptId(int theScriptId) {
        myScriptId.set(theScriptId);
    }

    public void setSaveName(String theSaveName) {
        mySaveName.set(theSaveName);
    }

    public void setKineticWeaponType(String theKineticWeaponType){
        myKineticWeaponType.set(theKineticWeaponType);
    }

    public void setKineticWeaponFrame(String theKineticWeaponFrame){
        myKineticWeaponFrame.set(theKineticWeaponFrame);
    }

    public void setEnergyWeaponType(String theEnergyWeaponType){
        myEnergyWeaponType.set(theEnergyWeaponType);
    }

    public void setMyEnergyWeaponFrame(String theEnergyWeaponFrame){
        myEnergyWeaponFrame.set(theEnergyWeaponFrame);
    }

    public void setPowerWeaponType(String thePowerWeaponType){
        myPowerWeaponType.set(thePowerWeaponType);
    }

    public void setPowerWeaponFrame(String thePowerWeaponFrame){
        myPowerWeaponFrame.set(thePowerWeaponFrame);
    }

    public void setAvgDPS(double theAvgDPS){
        myAvgDPS.set(theAvgDPS);
    }

    public void setMyTotalDPS(int theTotalDPS){
        myTotalDPS.set(theTotalDPS);
    }

    public void setSaveDate(String theSaveDate){
        mySaveDate.set(theSaveDate);
    }
}
