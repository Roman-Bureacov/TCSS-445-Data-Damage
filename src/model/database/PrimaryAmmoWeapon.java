package model.database;

/**
 * A class that represents a primary-ammo weapon, specifically with unlimited reserves.
 * @author Roman Bureacov
 * @version 2025 August
 */
public class PrimaryAmmoWeapon extends GenericWeapon {
    PrimaryAmmoWeapon(final WeaponSkeleton skeleton) {
        super(skeleton);
    }

    @Override
    void reload() { // primary-ammo weapons have infinite reserves
        if (getMagazineMax() != getMagazineCurrent())
            setMagazineCurrent(getMagazineMax());
    }
}
