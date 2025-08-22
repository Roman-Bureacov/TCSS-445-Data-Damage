# TCSS-445-Data-Damage
To run a script, copy and past one of the following example scripts into the simulation page
text editor and click run. 

To save a script, write a script in the editor, set the Script Name in the top, and hit save. 
Note: only valid runnable scripts can be saved.

To load a script, find its id in the scripts page, enter that id on the simulation page in the
Load ID text field and hit load.


# Sample Sim Scripts

## script1
```
kinetic Precision AutoRifle
energy RapidFire SniperRifle
power Precision RocketLauncher

startswith energy

kinetic.equip!
loop 2 {
    equipped.shootAtPrecision!
}

energy.equip!
equipped.shootAtPrecision!
```

## script2
```
kinetic Aggressive ScoutRifle
energy Aggressive Shotgun
power Precision LinearFusionRifle

startswith kinetic

loop 200 {
    equipped.shootAtPrecision!
    if equipped.magazine# < 0 {
        equipped.reload
    }
}
equipped.reload!
```

## script3
```
kinetic Aggressive HandCannon
energy RapidFire Shotgun
power RapidFire MachineGun

startswith kinetic

loop while equipped.magazine# > 0 {
    equipped.shootAtPrecision!
}

energy.equip!

loop while NOT equipped.isMagazineEmpty? {
    equipped.shootAtBody!
}
```

## script4
```
kinetic Precision AutoRifle
energy RapidFire SniperRifle
power Precision RocketLauncher

startswith energy
loop 20 {
    kinetic.equip!
    loop 5 {
        equipped.shootAtPrecision!
    }

    energy.equip!
    equipped.shootAtPrecision!

    power.equip!
    loop 5 {
        equipped.shootAtPrecision!
    }
}

energy.equip!
equipped.shootAtPrecision!
```

## script5
```
kinetic Precision AutoRifle
energy RapidFire SniperRifle
power Precision RocketLauncher

startswith energy

loop 2 {
    kinetic.equip!
    loop 2 {
        equipped.shootAtPrecision!
    }

    energy.equip!
    equipped.shootAtPrecision!

    power.equip!
    loop 2 {
        equipped.shootAtPrecision!
    }
}

energy.equip!
equipped.shootAtPrecision!
```