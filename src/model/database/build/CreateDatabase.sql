CREATE TABLE weapons (
    weapon_id INT AUTO_INCREMENT PRIMARY KEY,
    weapon_type TEXT NOT NULL,
    frame TEXT NOT NULL
);

CREATE TABLE weapon_stats (
    weapon_id INT,
    reserves INT DEFAULT 0 CHECK (reserves >= 0),
    fire_rate INT DEFAULT 0 CHECK (fire_rate >= 0),
    reload_speed INT DEFAULT 0 CHECK (reload_speed >= 0),
    magazine INT DEFAULT 0 CHECK (magazine >= 0),
    stow_speed INT DEFAULT 0,
    ready_speed INT DEFAULT 0,
    body_damage INT DEFAULT 0 CHECK (body_damage >= 0),
    precision_damage INT DEFAULT 0 CHECK (precision_damage >= 0),
    FOREIGN KEY (weapon_id)
        REFERENCES weapons (weapon_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (weapon_id)
);

CREATE TABLE slotability (
    weapon_id INT,
    in_kinetic BOOLEAN DEFAULT FALSE,
    in_energy BOOLEAN DEFAULT FALSE,
    in_power BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (weapon_id)
        REFERENCES weapons (weapon_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (weapon_id)
);

CREATE TABLE weapon_info (
    weapon_id INT,
    weapon_desc TEXT,
    image BLOB,
    FOREIGN KEY (weapon_id)
        REFERENCES weapons (weapon_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (weapon_id)
);

CREATE TABLE modifiers (
id INT AUTO_INCREMENT PRIMARY KEY,
mod_name VARCHAR(64) NOT NULL,
damage_mult DOUBLE DEFAULT 1,
fire_rate_mult DOUBLE DEFAULT 1,
reload_speed_mult DOUBLE DEFAULT 1,
body_damage_mult DOUBLE DEFAULT 1,
precision_damage_mult DOUBLE DEFAULT 1
);

CREATE TABLE sims (
id INT AUTO_INCREMENT PRIMARY KEY,
save_name VARCHAR(64) UNIQUE,
kinetic INT,
energy INT,
power INT,
average_dps DOUBLE DEFAULT 0,
total_damage INT DEFAULT 0,
save_date DATETIME DEFAULT CURRENT_TIMESTAMP,
script BLOB,
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

CREATE TABLE saved_sims_times (
      id INT,
      sim_timestamp DOUBLE CHECK (sim_timestamp >= 0),
      damage_instance INT DEFAULT 0 CHECK (damage_instance >= 0),
      FOREIGN KEY (id)
          REFERENCES sims (id)
          ON DELETE CASCADE ON UPDATE CASCADE,
      PRIMARY KEY (id , sim_timestamp)
);

CREATE TABLE sim_time_events (
     id INT AUTO_INCREMENT PRIMARY KEY,
     save_id INT,
     sim_timestamp DOUBLE,
     sim_event VARCHAR(64) NOT NULL,
     FOREIGN KEY (save_id , sim_timestamp)
         REFERENCES saved_sims_times (id , sim_timestamp)
         ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE pulse_rifle_specifics (
           weapon_id INT,
           burst_count INT DEFAULT 3 CHECK (burst_count > 0),
           burst_recovery INT DEFAULT 300 CHECK (burst_recovery > 0),
           FOREIGN KEY (weapon_id)
               REFERENCES weapons (weapon_id)
               ON DELETE CASCADE ON UPDATE CASCADE,
           PRIMARY KEY (weapon_id)
);

CREATE TABLE fusion_rifle_specifics (
            weapon_id INT,
            charge_time INT DEFAULT 1000 CHECK (charge_time > 0),
            bolt_count INT DEFAULT 5 CHECK (bolt_count > 0),
            charge_recovery INT DEFAULT 500 CHECK (charge_recovery > 0),
            FOREIGN KEY (weapon_id)
                REFERENCES weapons (weapon_id)
                ON DELETE CASCADE ON UPDATE CASCADE,
            PRIMARY KEY (weapon_id)
);

CREATE TABLE ammo_types (
    weapon_id INT,
    ammo INT,
    FOREIGN KEY (weapon_id)
        REFERENCES weapons (weapon_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (weapon_id)
)