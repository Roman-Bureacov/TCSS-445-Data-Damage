package Controller;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class WeaponRow {
    private final StringProperty weaponType = new SimpleStringProperty("");
    private final StringProperty frame = new SimpleStringProperty("");
    private final IntegerProperty reserve = new SimpleIntegerProperty(0);
    private final IntegerProperty magazine = new SimpleIntegerProperty(0);
    private final IntegerProperty fireRate = new SimpleIntegerProperty(0);
    private final DoubleProperty reload = new SimpleDoubleProperty(0.0);
    private final IntegerProperty body = new SimpleIntegerProperty(0);
    private final IntegerProperty precision = new SimpleIntegerProperty(0);
    private final BooleanProperty kinetic = new SimpleBooleanProperty(false);
    private final BooleanProperty energy = new SimpleBooleanProperty(false);
    private final BooleanProperty power = new SimpleBooleanProperty(false);
    private final IntegerProperty oneMagDamage = new SimpleIntegerProperty(0);
    private final IntegerProperty theoreticalTotalDamage = new SimpleIntegerProperty(0);
    private final DoubleProperty sustainedDps = new SimpleDoubleProperty(0.0);
    private final DoubleProperty trueDps = new SimpleDoubleProperty(0.0);
    private final StringProperty weapon_disc = new SimpleStringProperty("");
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

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

    public int getReserve(){
        return reserve.get();
    }

    public void setReserve(int v){
        reserve.set(v);
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

    public boolean isKinetic(){
        return kinetic.get();
    }

    public void setKinetic(boolean v){
        kinetic.set(v);
    }

    public boolean isEnergy(){
        return energy.get();
    }

    public void setEnergy(boolean v){
        energy.set(v);
    }

    public boolean isPower(){
        return power.get();
    }

    public void setPower(boolean v){
        power.set(v);
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

    public String getWeapon_disc() {
        return weapon_disc.get();
    }

    public void setDescription(String v) {
        weapon_disc.set(v);
    }

    public Image getImage() {
        return image.get();
    }

    public void setImage(Image v) {
        image.set(v);
    }
}

