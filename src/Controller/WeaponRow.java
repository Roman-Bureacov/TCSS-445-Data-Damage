package Controller;

import javafx.beans.property.*;

/**
 * A row containing weapon information that will be displayed in the weapons page table.
 *
 * @author Kaleb Anagnostou
 * @version 2025 August
 */
public class WeaponRow {
    /** The weapon type. */
    private final StringProperty myWeaponType = new SimpleStringProperty("");
    /** The weapon frame. */
    private final StringProperty myFrame = new SimpleStringProperty("");
    /** The weapon ammo type. */
    private final StringProperty myAmmo = new SimpleStringProperty("");
    /** The weapon reserves. */
    private final IntegerProperty myReserves = new SimpleIntegerProperty(0);
    /** The weapon magazine size. */
    private final IntegerProperty myMagazine = new SimpleIntegerProperty(0);
    /** The weapon fire rate. */
    private final IntegerProperty myFireRate = new SimpleIntegerProperty(0);
    /** The weapon reload speed. */
    private final DoubleProperty myReload = new SimpleDoubleProperty(0.0);
    /** The weapon body damage. */
    private final IntegerProperty myBody = new SimpleIntegerProperty(0);
    /** The weapon precision damage. */
    private final IntegerProperty myPrecision = new SimpleIntegerProperty(0);
    /** The weapon one mag damage. */
    private final IntegerProperty myOneMagDamage = new SimpleIntegerProperty(0);
    /** The weapon theoretical total damage. */
    private final IntegerProperty myTheoreticalTotalDamage = new SimpleIntegerProperty(0);
    /** The weapon sustained dps. */
    private final DoubleProperty mySustainedDps = new SimpleDoubleProperty(0.0);
    /** The weapon true dps. */
    private final DoubleProperty myTrueDps = new SimpleDoubleProperty(0.0);
    /** The weapon description. */
    private final StringProperty myWeaponDesc = new SimpleStringProperty("");

    public String getWeaponType(){
        return myWeaponType.get();
    }

    public String getFrame(){
        return myFrame.get();
    }

    public int getReserves(){
        return myReserves.get();
    }

    public int getMagazine(){
        return myMagazine.get();
    }

    public int getFireRate(){
        return myFireRate.get();
    }

    public double getReload(){
        return myReload.get();
    }

    public int getBody(){
        return myBody.get();
    }

    public int getPrecision(){
        return myPrecision.get();
    }

    public String getAmmo(){
        return myAmmo.get();
    }

    public int getOneMagDamage(){
        return myOneMagDamage.get();
    }

    public int getTheoreticalTotalDamage(){
        return myTheoreticalTotalDamage.get();
    }

    public double getSustainedDps(){
        return mySustainedDps.get();
    }

    public double getTrueDps(){
        return myTrueDps.get();
    }

    public String getWeapon_desc() {
        return myWeaponDesc.get();
    }

    public void setWeaponType(String theWeaponType){
        myWeaponType.set(theWeaponType);
    }

    public void setFrame(String theWeaponFrame){
        myFrame.set(theWeaponFrame);
    }

    public void setReserves(int theReserves){
        myReserves.set(theReserves);
    }

    public void setMagazine(int theMagazine){
        myMagazine.set(theMagazine);
    }

    public void setFireRate(int theFireRate){
        myFireRate.set(theFireRate);
    }

    public void setReload(double theReloadSpeed){
        myReload.set(theReloadSpeed);
    }

    public void setBody(int theBodyDamage){
        myBody.set(theBodyDamage);
    }

    public void setPrecision(int thePrecisionDamage){
        myPrecision.set(thePrecisionDamage);
    }

    public void setAmmo(String theAmmoType){
        myAmmo.set(theAmmoType);
    }

    public void setOneMagDamage(int theOneMagDamage){
        myOneMagDamage.set(theOneMagDamage);
    }

    public void setTheoreticalTotalDamage(int theTheoreticalTotalDamage){
        myTheoreticalTotalDamage.set(theTheoreticalTotalDamage);
    }

    public void setSustainedDps(double theSustainedDps){
        mySustainedDps.set(theSustainedDps);
    }

    public void setTrueDps(double theTrueDps){
        myTrueDps.set(theTrueDps);
    }

    public void setDescription(String theDescription) {
        myWeaponDesc.set(theDescription);
    }
}

