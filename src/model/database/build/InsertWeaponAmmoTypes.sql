INSERT INTO weapon_ammo (weapon_id, ammo_id)
SELECT w.weapon_id, s.ammo_id
FROM weapons w
JOIN (
    -- ===========================
    -- Primaries
    -- ===========================
    SELECT 'AutoRifle' AS weapon_type, 'RapidFire' AS weapon_frame, 1 AS ammo_id
    UNION ALL SELECT 'AutoRifle','Adaptive',1
    UNION ALL SELECT 'AutoRifle','Support',1
    UNION ALL SELECT 'AutoRifle','Precision',1
    UNION ALL SELECT 'AutoRifle','HighImpact',1

    UNION ALL SELECT 'SMG','Precision',1
    UNION ALL SELECT 'SMG','Aggressive',1
    UNION ALL SELECT 'SMG','Adaptive',1
    UNION ALL SELECT 'SMG','Lightweight',1

    UNION ALL SELECT 'PulseRifle','RapidFire',1
    UNION ALL SELECT 'PulseRifle','Lightweight',1
    UNION ALL SELECT 'PulseRifle','LegacyPR55',1
    UNION ALL SELECT 'PulseRifle','Adaptive',1
    UNION ALL SELECT 'PulseRifle','HighImpact',1
    UNION ALL SELECT 'PulseRifle','AggressiveBurst',1
    UNION ALL SELECT 'PulseRifle','HeavyBurst',1

    UNION ALL SELECT 'ScoutRifle','RapidFire',1
    UNION ALL SELECT 'ScoutRifle','Lightweight',1
    UNION ALL SELECT 'ScoutRifle','Precision',1
    UNION ALL SELECT 'ScoutRifle','HighImpact',1
    UNION ALL SELECT 'ScoutRifle','Aggressive',1

    UNION ALL SELECT 'HandCannon','Aggressive',1
    UNION ALL SELECT 'HandCannon','Precision',1
    UNION ALL SELECT 'HandCannon','HeavyBurst',1
    UNION ALL SELECT 'HandCannon','Lightweight',1
    UNION ALL SELECT 'HandCannon','Adaptive',1

    UNION ALL SELECT 'Sidearm','AdaptiveBurst',1
    UNION ALL SELECT 'Sidearm','RapidFire',1
    UNION ALL SELECT 'Sidearm','HeavyBurst',1
    UNION ALL SELECT 'Sidearm','Precision',1
    UNION ALL SELECT 'Sidearm','Adaptive',1
    UNION ALL SELECT 'Sidearm','Lightweight',1

    UNION ALL SELECT 'Bow','Lightweight',1
    UNION ALL SELECT 'Bow','Precision',1

    -- ===========================
    -- Specials
    -- ===========================
    UNION ALL SELECT 'SniperRifle','RapidFire',2
    UNION ALL SELECT 'SniperRifle','Adaptive',2
    UNION ALL SELECT 'SniperRifle','Aggressive',2

    UNION ALL SELECT 'TraceRifle','Adaptive',2

    UNION ALL SELECT 'FusionRifle','RapidFire',2
    UNION ALL SELECT 'FusionRifle','Adaptive',2
    UNION ALL SELECT 'FusionRifle','Aggressive',2
    UNION ALL SELECT 'FusionRifle','Precision',2
    UNION ALL SELECT 'FusionRifle','HighImpact',2

    UNION ALL SELECT 'Shotgun','RapidFire',2
    UNION ALL SELECT 'Shotgun','Lightweight',2
    UNION ALL SELECT 'Shotgun','Precision',2
    UNION ALL SELECT 'Shotgun','PinpointSlug',2
    UNION ALL SELECT 'Shotgun','Aggressive',2
    UNION ALL SELECT 'Shotgun','HeavyBurst',2

    UNION ALL SELECT 'BreechGrenadeLauncher','DoubleFire',2
    UNION ALL SELECT 'BreechGrenadeLauncher','Lightweight',2
    UNION ALL SELECT 'BreechGrenadeLauncher','Wave',2
    UNION ALL SELECT 'BreechGrenadeLauncher','MicroMissile',2

    UNION ALL SELECT 'Glaive','RapidFire',2
    UNION ALL SELECT 'Glaive','Adaptive',2
    UNION ALL SELECT 'Glaive','Aggressive',2

    UNION ALL SELECT 'Sidearm','RocketAssisted',2

    -- ===========================
    -- Heavies / Power
    -- ===========================
    UNION ALL SELECT 'MachineGun','RapidFire',3
    UNION ALL SELECT 'MachineGun','Aggressive',3
    UNION ALL SELECT 'MachineGun','Adaptive',3
    UNION ALL SELECT 'MachineGun','HighImpact',3

    UNION ALL SELECT 'GrenadeLauncher','RapidFire',3
    UNION ALL SELECT 'GrenadeLauncher','Adaptive',3
    UNION ALL SELECT 'GrenadeLauncher','CompressedWave',3

    UNION ALL SELECT 'RocketLauncher','Aggressive',3
    UNION ALL SELECT 'RocketLauncher','Adaptive',3
    UNION ALL SELECT 'RocketLauncher','HighImpact',3
    UNION ALL SELECT 'RocketLauncher','Precision',3

    UNION ALL SELECT 'LinearFusionRifle','Precision',3
    UNION ALL SELECT 'LinearFusionRifle','AdaptiveBurst',3
) AS s
ON w.weapon_type = s.weapon_type
AND w.weapon_frame = s.weapon_frame;
