INSERT INTO weapon_stats (weapon_id, reserves, fire_rate, reload_speed, magazine, stow_speed, ready_speed, body_damage, precision_damage)
SELECT w.weapon_id, s.reserves, s.fire_rate, s.reload_speed, s.magazine, s.stow_speed, s.ready_speed, s.body_damage, s.precision_damage
FROM weapons w
JOIN (
VALUES

-- ===========================
-- Primaries: Auto Rifles
-- ===========================
('AutoRifle', 'Rapid-Fire', 70, 720, 900, 70, 500, 500, 386, 654),
('AutoRifle', 'Adaptive',   57, 600, 1120, 57, 500, 500, 426, 746),
('AutoRifle', 'Support',    60, 600, 1120, 60, 500, 500, 530, 743),
('AutoRifle', 'Precision',  45, 450, 1120, 45, 500, 500, 567, 961),
('AutoRifle', 'High-Impact',45, 360, 1120, 45, 500, 500, 680, 1220),

-- ===========================
-- Primaries: SMGs
-- ===========================
('SMG', 'Precision',  30, 600, 1190, 30, 500, 500, 663, 1060),
('SMG', 'Aggressive', 30, 720, 1190, 30, 500, 500, 540, 948),
('SMG', 'Adaptive',   41, 900, 1190, 41, 500, 500, 457, 757),
('SMG', 'Lightweight',40, 900, 1190, 40, 500, 500, 421, 796),

-- ===========================
-- Primaries: Pulse Rifles
-- ===========================
('PulseRifle', 'Rapid-Fire',      48, 540, 930, 48, 500, 500, 447, 827),
('PulseRifle', 'Lightweight',     48, 450, 1160, 48, 500, 500, 587, 937),
('PulseRifle', 'Legacy PR-55',    45, 450, 1160, 45, 500, 500, 587, 937),
('PulseRifle', 'Adaptive',        48, 390, 1160, 48, 500, 500, 655, 1084),
('PulseRifle', 'High-Impact',     46, 340, 1160, 46, 500, 500, 640, 1147),
('PulseRifle', 'Aggressive Burst',64, 450, 1160, 64, 500, 500, 461, 900),
('PulseRifle', 'Heavy Burst',     30, 324, 1160, 30, 500, 500, 780, 1446),

-- ===========================
-- Primaries: Scout Rifles
-- ===========================
('ScoutRifle', 'Rapid-Fire', 20, 260, 990, 20, 500, 500, 897, 1750),
('ScoutRifle', 'Lightweight',20, 200, 1240, 20, 500, 500, 1218, 2065),
('ScoutRifle', 'Precision',  20, 180, 1240, 20, 500, 500, 1234, 2213),
('ScoutRifle', 'High-Impact',20, 150, 1240, 20, 500, 500, 1346, 2704),
('ScoutRifle', 'Aggressive', 20, 120, 380,  20, 500, 500, 2256, 3825),

-- ===========================
-- Primaries: Hand Cannons
-- ===========================
('HandCannon', 'Aggressive', 14, 120, 1640, 14, 500, 500, 1653, 3063),
('HandCannon', 'Precision',  19, 180, 1640, 19, 500, 500, 1528, 2382),
('HandCannon', 'Heavy Burst',22, 257, 1640, 22, 500, 500, 810, 1781),
('HandCannon', 'Lightweight',14, 140, 1640, 14, 500, 500, 1501, 2693),
('HandCannon', 'Adaptive',   14, 140, 1640, 14, 500, 500, 1501, 2693),

-- ===========================
-- Primaries: Sidearms
-- ===========================
('Sidearm', 'Adaptive Burst',42, 491, 1030, 42, 500, 500, 764, 1415),
('Sidearm', 'Rapid-Fire',    20, 450, 820,  20, 500, 500, 955, 1526),
('Sidearm', 'Heavy Burst',   36, 325, 1030, 36, 500, 500, 1260, 2013),
('Sidearm', 'Precision',     18, 260, 1030, 18, 500, 500, 1527, 2441),
('Sidearm', 'Adaptive',      18, 300, 1030, 18, 500, 500, 1375, 2197),
('Sidearm', 'Lightweight',   18, 360, 1030, 18, 500, 500, 1184, 1892),

-- ===========================
-- Primaries: Bows
-- ===========================
('Bow', 'Lightweight',1, 55, 600, 1, 500, 500, 3654, 5839),
('Bow', 'Precision',  1, 51, 600, 1, 500, 500, 4589, 5983),

-- ===========================
-- Specials: Sniper Rifles
-- ===========================
('SniperRifle', 'Rapid-Fire',39, 140, 1580, 7, 500, 500, 3655, 12578),
('SniperRifle', 'Adaptive',  31, 90,  1980, 7, 500, 500, 5117, 16605),
('SniperRifle', 'Aggressive',27, 72,  1980, 5, 500, 500, 5806, 20319),

-- ===========================
-- Specials: Trace Rifles
-- ===========================
('TraceRifle', 'Adaptive',645, 900, 1440, 104, 500, 500, 596, 801),

-- ===========================
-- Specials: Fusion Rifles
-- ===========================
('FusionRifle', 'Rapid-Fire',24, 53, 1060, 8, 500, 500, 21681, 21681),
('FusionRifle', 'Adaptive',  22, 50, 1320, 8, 500, 500, 22953, 22953),
('FusionRifle', 'Aggressive',22, 52, 1320, 8, 500, 500, 24000, 24000),
('FusionRifle', 'Precision', 23, 45, 1320, 8, 500, 500, 23800, 23800),
('FusionRifle', 'High-Impact',21, 40, 1320, 8, 500, 500, 28545, 28545),

-- ===========================
-- Specials: Shotguns
-- ===========================
('Shotgun', 'Rapid-Fire',  31, 140, 320, 8, 500, 500, 14616, 16187),
('Shotgun', 'Lightweight', 21, 80,  430, 8, 500, 500, 18120, 20074),
('Shotgun', 'Precision',   23, 65,  430, 7, 500, 500, 18948, 20993),
('Shotgun', 'Pinpoint Slug',23, 65, 430, 8, 500, 500, 11927, 20931),
('Shotgun', 'Aggressive',  24, 55,  430, 6, 500, 500, 22008, 24377),
('Shotgun', 'Heavy Burst', 44, 147, 430, 16, 500, 500, 6560, 11511),

-- ===========================
-- Specials: Breech Grenade Launchers
-- ===========================
('BreechGrenadeLauncher', 'Double Fire',   23, 100, 1620, 1, 500, 500, 22268, 22268),
('BreechGrenadeLauncher', 'Lightweight',   25, 90,  1620, 1, 500, 500, 17607, 17607),
('BreechGrenadeLauncher', 'Wave',          25, 90,  1620, 1, 500, 500, 12906, 12906),
('BreechGrenadeLauncher', 'Micro-Missile', 25, 90,  1620, 1, 500, 500, 21199, 21199),
-- ('BreechGrenadeLauncher', 'Area Denial', 25, 90, 1620, 1, 500, 500, 17607, 17607),

-- ===========================
-- Specials: Glaives
-- ===========================
('Glaive', 'Rapid-Fire',31, 80, 1570, 7, 500, 500, 10635, 10635),
('Glaive', 'Adaptive',  29, 55, 1570, 6, 500, 500, 13506, 13506),
('Glaive', 'Aggressive',30, 45, 1570, 6, 500, 500, 15500, 15500),

-- ===========================
-- Specials: Sidearms (Special Variant)
-- ===========================
('Sidearm', 'Rocket-Assisted',48, 100, 880, 13, 500, 500, 7493, 8312),

-- ===========================
-- Heavies: Machine Guns
-- ===========================
('MachineGun', 'Rapid-Fire',687, 900, 2860, 115, 500, 500, 1229, 1603),
('MachineGun', 'Aggressive',587, 600, 3570, 91, 500, 500, 1735, 2262),
('MachineGun', 'Adaptive',  495, 450, 3570, 75, 500, 500, 2000, 2803),
('MachineGun', 'High-Impact',483, 360, 3570, 75, 500, 500, 2289, 3298),

-- ===========================
-- Heavies: Grenade Launchers
-- ===========================
('GrenadeLauncher', 'Rapid-Fire',   35, 150, 1920, 8, 500, 500, 18031, 18031),
('GrenadeLauncher', 'Adaptive',     30, 120, 2080, 8, 500, 500, 21202, 21202),
('GrenadeLauncher', 'Compressed Wave',29, 120, 2160, 7, 500, 500, 21443, 21443),

-- ===========================
-- Heavies: Rocket Launchers
-- ===========================
('RocketLauncher', 'Aggressive',11, 75, 2350, 1, 500, 500, 53949, 53949),
('RocketLauncher', 'Adaptive',  10, 68, 2350, 1, 500, 500, 53949, 53949),
('RocketLauncher', 'High-Impact',12, 62, 2350, 1, 500, 500, 49061, 49061),
('RocketLauncher', 'Precision',  12, 62, 2270, 1, 500, 500, 45579, 45579),

-- ===========================
-- Heavies: Linear Fusion Rifles
-- ===========================
('LinearFusionRifle', 'Precision',    25, 56, 1320, 7, 500, 500, 8525, 29333),
('LinearFusionRifle', 'Adaptive Burst',24, 42, 1320, 7, 500, 500, 12141, 41778)

) AS s(weapon_type, weapon_frame, reserves, fire_rate, reload_speed, magazine, stow_speed, ready_speed, body_damage, precision_damage)
  USING (weapon_type, weapon_frame);
