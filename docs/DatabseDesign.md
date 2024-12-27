# Conversion of ER Diagram to Relational Tables

The **experiences** relationship between **cities** and **weather** need not be stored. Instead, we may store the primary key of either side. Since the **Weather** table has full participation and can only exist alongside a city, we must use cityID as the primary key for both tables.

This gives us the following tables:

1. `City(cityID, name, country, is_favourite)`
   - PK: cityID
2. `Weather(cityID, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)`
   - PK: cityID references **City** table

## Normalization

### 1. **City**(cityID, name, country, is_favourite)

We infer the functional dependency:  
`cityID → name, country, is_favourite`

**Notes:**
- Every attribute is atomic.
- The details of any city can be determined by its cityID.
- No attributes determine the key.

Therefore, **Table 1** is in **0NF**, **1NF**, **2NF**, **3NF**, and **BCNF**.

### 2. **Weather**(weatherID, cityID, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)

We infer the functional dependency:  
`cityID → temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod`

**Notes:**
- Each attribute is atomic.
- The details of any weather forecast can be determined by its cityID.
- No attributes determine the key.

Therefore, **Table 2** is in **0NF**, **1NF**, **2NF**, **3NF**, and **BCNF**.

`Since both tables are in BCNF, the original two tables are finalized.`