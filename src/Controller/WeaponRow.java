package Controller;

import javafx.beans.property.*;

public class WeaponRow {
    private final StringProperty weaponType = new SimpleStringProperty("");
    private final StringProperty frame = new SimpleStringProperty("");
    private final StringProperty ammo = new SimpleStringProperty("");
    private final IntegerProperty reserves = new SimpleIntegerProperty(0);
    private final IntegerProperty magazine = new SimpleIntegerProperty(0);
    private final IntegerProperty fireRate = new SimpleIntegerProperty(0);
    private final DoubleProperty reload = new SimpleDoubleProperty(0.0);
    private final IntegerProperty body = new SimpleIntegerProperty(0);
    private final IntegerProperty precision = new SimpleIntegerProperty(0);
    private final IntegerProperty oneMagDamage = new SimpleIntegerProperty(0);
    private final IntegerProperty theoreticalTotalDamage = new SimpleIntegerProperty(0);
    private final DoubleProperty sustainedDps = new SimpleDoubleProperty(0.0);
    private final DoubleProperty trueDps = new SimpleDoubleProperty(0.0);
    private final StringProperty weapon_desc = new SimpleStringProperty("");

    public String getWeaponType(){
        return weaponType.get();
    }

    public void setWeaponType(String v){
        weaponType.set(v);
    }

    public String getFrame(){
        return frame.get();
    }

    public void setFrame(String v){
        frame.set(v);
    }

    public int getReserves(){
        return reserves.get();
    }

    public void setReserves(int v){
        reserves.set(v);
    }

    public int getMagazine(){
        return magazine.get();
    }

    public void setMagazine(int v){
        magazine.set(v);
    }

    public int getFireRate(){
        return fireRate.get();
    }

    public void setFireRate(int v){
        fireRate.set(v);
    }

    public double getReload(){
        return reload.get();
    }

    public void setReload(double v){
        reload.set(v);
    }

    public int getBody(){
        return body.get();
    }

    public void setBody(int v){
        body.set(v);
    }

    public int getPrecision(){
        return precision.get();
    }

    public void setPrecision(int v){
        precision.set(v);
    }

    public String getAmmo(){
        return ammo.get();
    }

    public void setAmmo(String v){
        ammo.set(v);
    }

    public int getOneMagDamage(){
        return oneMagDamage.get();
    }

    public void setOneMagDamage(int v){
        oneMagDamage.set(v);
    }

    public int getTheoreticalTotalDamage(){
        return theoreticalTotalDamage.get();
    }

    public void setTheoreticalTotalDamage(int v){
        theoreticalTotalDamage.set(v);
    }

    public double getSustainedDps(){
        return sustainedDps.get();
    }

    public void setSustainedDps(double v){
        sustainedDps.set(v);
    }

    public double getTrueDps(){
        return trueDps.get();
    }

    public void setTrueDps(double v){
        trueDps.set(v);
    }

    public String getWeapon_desc() {
        return weapon_desc.get();
    }

    public void setDescription(String v) {
        weapon_desc.set(v);
    }
}

