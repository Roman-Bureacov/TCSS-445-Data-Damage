--- ==== DELIVERABLE SUBMISSION, DO NOT USE ===

--- ******************************************************************************************
--- DDL: CREATE THE DATABASE SCHEMA

CREATE TABLE weapons (
    weapon_id INTEGER PRIMARY KEY AUTOINCREMENT,
    weapon_type TEXT NOT NULL,
    weapon_frame TEXT NOT NULL
);

CREATE TABLE weapon_stats (
    weapon_id INTEGER,
    reserves INTEGER DEFAULT 0 CHECK (reserves >= 0),
    fire_rate INTEGER DEFAULT 0 CHECK (fire_rate >= 0),
    reload_speed INTEGER DEFAULT 0 CHECK (reload_speed >= 0),
    magazine INTEGER DEFAULT 0 CHECK (magazine >= 0),
    stow_speed INTEGER DEFAULT 0,
    ready_speed INTEGER DEFAULT 0,
    body_damage INTEGER DEFAULT 0 CHECK (body_damage >= 0),
    precision_damage INTEGER DEFAULT 0 CHECK (precision_damage >= 0),
    FOREIGN KEY (weapon_id)
      REFERENCES weapons (weapon_id)
      ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (weapon_id)
);

CREATE TABLE weapon_ammo (
     weapon_id INTEGER,
     ammo_id INTEGER,
     FOREIGN KEY (weapon_id)
         REFERENCES weapons (weapon_id)
         ON DELETE CASCADE ON UPDATE CASCADE,
     FOREIGN KEY (ammo_id)
         REFERENCES ammo_types (ammo_id)
         ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ammo_types (
    ammo_id INTEGER PRIMARY KEY,
    ammo TEXT
);

CREATE TABLE weapon_info (
     weapon_id INTEGER PRIMARY KEY,
     weapon_desc TEXT,
     weapon_image BLOB,
     FOREIGN KEY (weapon_id)
         REFERENCES weapons (weapon_id)
         ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sims (
    sim_id INTEGER PRIMARY KEY AUTOINCREMENT,
    sim_name TEXT,
    save_date TEXT DEFAULT CURRENT_TIMESTAMP,
    script_id INTEGER,
    FOREIGN KEY (script_id)
      REFERENCES sims_scripts (script_id)
      ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sims_meta (
   script_id INTEGER PRIMARY KEY,
   kinetic INTEGER,
   energy INTEGER,
   power INTEGER,
   average_dps REAL,
   total_damage INTEGER,
   FOREIGN KEY (script_id)
       REFERENCES sims (script_id)
       ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (kinetic)
       REFERENCES weapons (weapon_id)
       ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (energy)
       REFERENCES weapons (weapon_id)
       ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (power)
       REFERENCES weapons (weapon_id)
       ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sims_scripts (
  script_id INTEGER PRIMARY KEY AUTOINCREMENT,
  script TEXT
);

CREATE TABLE sim_events (
    event_id INTEGER PRIMARY KEY,
    script_id INTEGER,
    timestamps INTEGER,
    damage_instance INTEGER,
    event_desc TEXT,
    FOREIGN KEY (script_id)
        REFERENCES sims (script_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE pulse_rifle_specifics (
    weapon_id INTEGER PRIMARY KEY,
    burst_count INTEGER DEFAULT 3 CHECK (burst_count > 0),
    burst_recovery INTEGER DEFAULT 300 CHECK (burst_recovery > 0),
    FOREIGN KEY (weapon_id)
       REFERENCES weapons (weapon_id)
       ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE fusion_rifle_specifics (
    weapon_id INTEGER PRIMARY KEY,
    charge_time INTEGER DEFAULT 1000 CHECK (charge_time > 0),
    bolt_count INTEGER DEFAULT 5 CHECK (bolt_count > 0),
    charge_recovery INTEGER DEFAULT 500 CHECK (charge_recovery > 0),
    FOREIGN KEY (weapon_id)
        REFERENCES weapons (weapon_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

--- ******************************************************************************************
--- DDL: INSERT AMMO TYPES DATA

INSERT INTO ammo_types (ammo_id, ammo)
VALUES
    (1, "Primary"),
    (2, "Special"),
    (3, "Heavy")


--- ******************************************************************************************
--- DDL: INSERT FUSION RIFLE SPECIFIC DATA

INSERT INTO fusion_rifle_specifics (weapon_id, charge_time, bolt_count, charge_recovery)
VALUES
    (39, 500, 9, 630),
    (40, 660, 7, 550),
    (41, 660, 12, 500),
    (42, 780, 7, 560),
    (43, 960, 5, 550);

--- ******************************************************************************************
--- DDL: INSERT PULSE RIFLE SPECIFIC DATA

INSERT INTO pulse_rifle_specifics (weapon_id, burst_count, burst_recovery)
VALUES
    (10, 3, 200),
    (11, 3, 300),
    (12, 3, 300),
    (13, 3, 300),
    (14, 3, 400),
    (15, 4, 350),
    (16, 2, 200);


--- ******************************************************************************************
--- DDL: INSERT WEAPON AMMO TYPES DATA

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


--- ******************************************************************************************
--- DDL: INSERT WEAPON INFO DATA

INSERT INTO weapon_info (weapon_id, weapon_desc, weapon_image) VALUES
(1, 'A powerful hand cannon with a high-impact frame.', NULL),
(2, 'A reliable auto rifle with a balanced recoil pattern.', NULL),
(3, 'A precision sniper rifle capable of devastating long-range shots.', NULL),
(4, 'A shotgun with a tight spread and high damage at close range.', NULL),
(5, 'A fusion rifle that charges quickly and delivers a powerful energy burst.', NULL),
(6, 'A lightweight grenade launcher that excels at area-of-effect damage.', NULL),
(7, 'A rocket launcher with a large blast radius and high damage output.', NULL),
(8, 'A well-balanced sword with a versatile moveset.', NULL),
(9, 'A trace rifle that continuously fires a concentrated beam of energy.', NULL),
(10, 'A sidearm with a high rate of fire and decent damage output.', NULL);

--- ******************************************************************************************
--- DDL: INSERT WEAPON ID DATA

INSERT INTO weapons (weapon_type, weapon_frame)
VALUES

-- primaries
('AutoRifle', 'RapidFire'),
('AutoRifle', 'Adaptive'),
('AutoRifle', 'Support'),
('AutoRifle', 'Precision'),
('AutoRifle', 'HighImpact'),

('SMG', 'Precision'),
('SMG', 'Aggressive'),
('SMG', 'Adaptive'),
('SMG', 'Lightweight'),

('PulseRifle', 'RapidFire'),
('PulseRifle', 'Lightweight'),
('PulseRifle', 'LegacyPR55'),
('PulseRifle', 'Adaptive'),
('PulseRifle', 'HighImpact'),
('PulseRifle', 'AggressiveBurst'),
('PulseRifle', 'HeavyBurst'),

('ScoutRifle', 'RapidFire'),
('ScoutRifle', 'Lightweight'),
('ScoutRifle', 'Precision'),
('ScoutRifle', 'HighImpact'),
('ScoutRifle', 'Aggressive'),

('HandCannon', 'Aggressive'),
('HandCannon', 'Precision'),
('HandCannon', 'HeavyBurst'),
('HandCannon', 'Lightweight'),
('HandCannon', 'Adaptive'),

('Sidearm', 'AdaptiveBurst'),
('Sidearm', 'RapidFire'),
('Sidearm', 'HeavyBurst'),
('Sidearm', 'Precision'),
('Sidearm', 'Adaptive'),
('Sidearm', 'Lightweight'),

('Bow', 'Lightweight'),
('Bow', 'Precision'),

-- specials
('SniperRifle', 'RapidFire'),
('SniperRifle', 'Adaptive'),
('SniperRifle', 'Aggressive'),

('TraceRifle', 'Adaptive'),

('FusionRifle', 'RapidFire'),
('FusionRifle', 'Adaptive'),
('FusionRifle', 'Aggressive'),
('FusionRifle', 'Precision'),
('FusionRifle', 'HighImpact'),

('Shotgun', 'RapidFire'),
('Shotgun', 'Lightweight'),
('Shotgun', 'Precision'),
('Shotgun', 'PinpointSlug'),
('Shotgun', 'Aggressive'),
('Shotgun', 'HeavyBurst'),

('BreechGrenadeLauncher', 'DoubleFire'),
('BreechGrenadeLauncher', 'Lightweight'),
('BreechGrenadeLauncher', 'Wave'),
('BreechGrenadeLauncher', 'MicroMissile'),

('Glaive', 'RapidFire'),
('Glaive', 'Adaptive'),
('Glaive', 'Aggressive'),

('Sidearm', 'RocketAssisted'),

-- power weapons
('MachineGun', 'RapidFire'),
('MachineGun', 'Aggressive'),
('MachineGun', 'Adaptive'),
('MachineGun', 'HighImpact'),

('GrenadeLauncher', 'RapidFire'),
('GrenadeLauncher', 'Adaptive'),
('GrenadeLauncher', 'CompressedWave'),

('RocketLauncher', 'Aggressive'),
('RocketLauncher', 'Adaptive'),
('RocketLauncher', 'HighImpact'),
('RocketLauncher', 'Precision'),

('LinearFusionRifle', 'Precision'),
('LinearFusionRifle', 'AdaptiveBurst');


--- ******************************************************************************************
--- DDL: INSERT WEAPON STAT DATA

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


