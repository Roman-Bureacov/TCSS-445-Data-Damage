INSERT INTO weapon_stats (weapon_id, reserves, fire_rate, reload_speed, magazine, stow_speed, ready_speed, body_damage, precision_damage)
SELECT w.weapon_id, s.reserves, s.fire_rate, s.reload_speed, s.magazine, s.stow_speed, s.ready_speed, s.body_damage, s.precision_damage
FROM weapons w
JOIN (
    -- ===========================
    -- Primaries: Auto Rifles
    -- ===========================
    SELECT 'AutoRifle' AS weapon_type, 'RapidFire' AS weapon_frame, 70 AS reserves, 720 AS fire_rate, 900 AS reload_speed, 70 AS magazine, 500 AS stow_speed, 500 AS ready_speed, 386 AS body_damage, 654 AS precision_damage
    UNION ALL SELECT 'AutoRifle','Adaptive',57,600,1120,57,500,500,426,746
    UNION ALL SELECT 'AutoRifle','Support',60,600,1120,60,500,500,530,743
    UNION ALL SELECT 'AutoRifle','Precision',45,450,1120,45,500,500,567,961
    UNION ALL SELECT 'AutoRifle','HighImpact',45,360,1120,45,500,500,680,1220

    -- ===========================
    -- Primaries: SMGs
    -- ===========================
    UNION ALL SELECT 'SMG','Precision',30,600,1190,30,500,500,663,1060
    UNION ALL SELECT 'SMG','Aggressive',30,720,1190,30,500,500,540,948
    UNION ALL SELECT 'SMG','Adaptive',41,900,1190,41,500,500,457,757
    UNION ALL SELECT 'SMG','Lightweight',40,900,1190,40,500,500,421,796

    -- ===========================
    -- Primaries: Pulse Rifles
    -- ===========================
    UNION ALL SELECT 'PulseRifle','RapidFire',48,540,930,48,500,500,447,827
    UNION ALL SELECT 'PulseRifle','Lightweight',48,450,1160,48,500,500,587,937
    UNION ALL SELECT 'PulseRifle','LegacyPR55',45,450,1160,45,500,500,587,937
    UNION ALL SELECT 'PulseRifle','Adaptive',48,390,1160,48,500,500,655,1084
    UNION ALL SELECT 'PulseRifle','HighImpact',46,340,1160,46,500,500,640,1147
    UNION ALL SELECT 'PulseRifle','AggressiveBurst',64,450,1160,64,500,500,461,900
    UNION ALL SELECT 'PulseRifle','HeavyBurst',30,324,1160,30,500,500,780,1446

    -- ===========================
    -- Primaries: Scout Rifles
    -- ===========================
    UNION ALL SELECT 'ScoutRifle','RapidFire',20,260,990,20,500,500,897,1750
    UNION ALL SELECT 'ScoutRifle','Lightweight',20,200,1240,20,500,500,1218,2065
    UNION ALL SELECT 'ScoutRifle','Precision',20,180,1240,20,500,500,1234,2213
    UNION ALL SELECT 'ScoutRifle','HighImpact',20,150,1240,20,500,500,1346,2704
    UNION ALL SELECT 'ScoutRifle','Aggressive',20,120,380,20,500,500,2256,3825

    -- ===========================
    -- Primaries: Hand Cannons
    -- ===========================
    UNION ALL SELECT 'HandCannon','Aggressive',14,120,1640,14,500,500,1653,3063
    UNION ALL SELECT 'HandCannon','Precision',19,180,1640,19,500,500,1528,2382
    UNION ALL SELECT 'HandCannon','HeavyBurst',22,257,1640,22,500,500,810,1781
    UNION ALL SELECT 'HandCannon','Lightweight',14,140,1640,14,500,500,1501,2693
    UNION ALL SELECT 'HandCannon','Adaptive',14,140,1640,14,500,500,1501,2693

    -- ===========================
    -- Primaries: Sidearms
    -- ===========================
    UNION ALL SELECT 'Sidearm','AdaptiveBurst',42,491,1030,42,500,500,764,1415
    UNION ALL SELECT 'Sidearm','RapidFire',20,450,820,20,500,500,955,1526
    UNION ALL SELECT 'Sidearm','HeavyBurst',36,325,1030,36,500,500,1260,2013
    UNION ALL SELECT 'Sidearm','Precision',18,260,1030,18,500,500,1527,2441
    UNION ALL SELECT 'Sidearm','Adaptive',18,300,1030,18,500,500,1375,2197
    UNION ALL SELECT 'Sidearm','Lightweight',18,360,1030,18,500,500,1184,1892

    -- ===========================
    -- Primaries: Bows
    -- ===========================
    UNION ALL SELECT 'Bow','Lightweight',1,55,600,1,500,500,3654,5839
    UNION ALL SELECT 'Bow','Precision',1,51,600,1,500,500,4589,5983

    -- ===========================
    -- Specials: Sniper Rifles
    -- ===========================
    UNION ALL SELECT 'SniperRifle','RapidFire',39,140,1580,7,500,500,3655,12578
    UNION ALL SELECT 'SniperRifle','Adaptive',31,90,1980,7,500,500,5117,16605
    UNION ALL SELECT 'SniperRifle','Aggressive',27,72,1980,5,500,500,5806,20319

    -- ===========================
    -- Specials: Trace Rifles
    -- ===========================
    UNION ALL SELECT 'TraceRifle','Adaptive',645,900,1440,104,500,500,596,801

    -- ===========================
    -- Specials: Fusion Rifles
    -- ===========================
    UNION ALL SELECT 'FusionRifle','RapidFire',24,53,1060,8,500,500,21681,21681
    UNION ALL SELECT 'FusionRifle','Adaptive',22,50,1320,8,500,500,22953,22953
    UNION ALL SELECT 'FusionRifle','Aggressive',22,52,1320,8,500,500,24000,24000
    UNION ALL SELECT 'FusionRifle','Precision',23,45,1320,8,500,500,23800,23800
    UNION ALL SELECT 'FusionRifle','HighImpact',21,40,1320,8,500,500,28545,28545

    -- ===========================
    -- Specials: Shotguns
    -- ===========================
    UNION ALL SELECT 'Shotgun','RapidFire',31,140,320,8,500,500,14616,16187
    UNION ALL SELECT 'Shotgun','Lightweight',21,80,430,8,500,500,18120,20074
    UNION ALL SELECT 'Shotgun','Precision',23,65,430,7,500,500,18948,20993
    UNION ALL SELECT 'Shotgun','PinpointSlug',23,65,430,8,500,500,11927,20931
    UNION ALL SELECT 'Shotgun','Aggressive',24,55,430,6,500,500,22008,24377
    UNION ALL SELECT 'Shotgun','HeavyBurst',44,147,430,16,500,500,6560,11511

    -- ===========================
    -- Specials: Breech Grenade Launchers
    -- ===========================
    UNION ALL SELECT 'BreechGrenadeLauncher','DoubleFire',23,100,1620,1,500,500,22268,22268
    UNION ALL SELECT 'BreechGrenadeLauncher','Lightweight',25,90,1620,1,500,500,17607,17607
    UNION ALL SELECT 'BreechGrenadeLauncher','Wave',25,90,1620,1,500,500,12906,12906
    UNION ALL SELECT 'BreechGrenadeLauncher','MicroMissile',25,90,1620,1,500,500,21199,21199

    -- ===========================
    -- Specials: Glaives
    -- ===========================
    UNION ALL SELECT 'Glaive','RapidFire',31,80,1570,7,500,500,10635,10635
    UNION ALL SELECT 'Glaive','Adaptive',29,55,1570,6,500,500,13506,13506
    UNION ALL SELECT 'Glaive','Aggressive',30,45,1570,6,500,500,15500,15500

    -- ===========================
    -- Specials: Sidearms (Special Variant)
    -- ===========================
    UNION ALL SELECT 'Sidearm','RocketAssisted',48,100,880,13,500,500,7493,8312

    -- ===========================
    -- Heavies: Machine Guns
    -- ===========================
    UNION ALL SELECT 'MachineGun','RapidFire',687,900,2860,115,500,500,1229,1603
    UNION ALL SELECT 'MachineGun','Aggressive',587,600,3570,91,500,500,1735,2262
    UNION ALL SELECT 'MachineGun','Adaptive',495,450,3570,75,500,500,2000,2803
    UNION ALL SELECT 'MachineGun','HighImpact',483,360,3570,75,500,500,2289,3298

    -- ===========================
    -- Heavies: Grenade Launchers
    -- ===========================
    UNION ALL SELECT 'GrenadeLauncher','RapidFire',35,150,1920,8,500,500,18031,18031
    UNION ALL SELECT 'GrenadeLauncher','Adaptive',30,120,2080,8,500,500,21202,21202
    UNION ALL SELECT 'GrenadeLauncher','CompressedWave',29,120,2160,7,500,500,21443,21443

    -- ===========================
    -- Heavies: Rocket Launchers
    -- ===========================
    UNION ALL SELECT 'RocketLauncher','Aggressive',11,75,2350,1,500,500,53949,53949
    UNION ALL SELECT 'RocketLauncher','Adaptive',10,68,2350,1,500,500,53949,53949
    UNION ALL SELECT 'RocketLauncher','HighImpact',12,62,2350,1,500,500,49061,49061
    UNION ALL SELECT 'RocketLauncher','Precision',12,62,2270,1,500,500,45579,45579

    -- ===========================
    -- Heavies: Linear Fusion Rifles
    -- ===========================
    UNION ALL SELECT 'LinearFusionRifle','Precision',25,56,1320,7,500,500,8525,29333
    UNION ALL SELECT 'LinearFusionRifle','AdaptiveBurst',24,42,1320,7,500,500,12141,41778
) AS s
ON w.weapon_type = s.weapon_type
AND w.weapon_frame = s.weapon_frame;
