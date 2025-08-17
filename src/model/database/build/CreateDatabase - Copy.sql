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
/*
CREATE TABLE modifiers ( -- TODO: what about this?
id INTEGER AUTO_INCREMENT PRIMARY KEY,
mod_name VARCHAR(64) NOT NULL,
damage_mult DOUBLE DEFAULT 1,
fire_rate_mult DOUBLE DEFAULT 1,
reload_speed_mult DOUBLE DEFAULT 1,
body_damage_mult DOUBLE DEFAULT 1,
precision_damage_mult DOUBLE DEFAULT 1
);
*/
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
/*
CREATE TABLE saved_sims_times ( -- TODO what about this?
      id INTEGER,
      sim_timestamp DOUBLE CHECK (sim_timestamp >= 0),
      damage_instance INTEGER DEFAULT 0 CHECK (damage_instance >= 0),
      FOREIGN KEY (id)
          REFERENCES sims (id)
          ON DELETE CASCADE ON UPDATE CASCADE,
      PRIMARY KEY (id , sim_timestamp)
);
*/
/*
CREATE TABLE sim_time_events ( -- TODO what about this?
     id INTEGER AUTO_INCREMENT PRIMARY KEY,
     save_id INTEGER,
     sim_timestamp DOUBLE,
     sim_event VARCHAR(64) NOT NULL,
     FOREIGN KEY (save_id , sim_timestamp)
         REFERENCES saved_sims_times (id , sim_timestamp)
         ON DELETE CASCADE ON UPDATE CASCADE
);
*/
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