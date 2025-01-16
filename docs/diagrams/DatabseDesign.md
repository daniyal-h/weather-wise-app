# Conversion of ER Diagram to Relational Tables

The **experiences** and **forecasts** relationships between **cities**, **weather**, and **forecast** are one-to-one and need not be stored. Due to full participation from **weather** and **forecast** to **cities**, we set `cityID` as the primary key to all tables.

This gives us the following tables:

1. `Cities(cityID, name, country_code, country, population, is_favourite)`  
   - PK: `cityID`
   
2. `Weather(cityID, last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)`  
   - PK: `cityID`
   - FK: `cityID` references **Cities** table

3. `Forecast(cityID, last_updated, forecastJSON)`  
   - PK: `cityID`
   - FK: `cityID` references **Cities** table

---

## Normalization

### 1. **Cities**(cityID, name, country_code, country, population, is_favourite)  

We infer the functional dependency:  
`cityID → name, country_code, country, population, is_favourite`   
`country_code → country` 

**Notes:**
- Every attribute is atomic.
- The details of any city can be determined by its `cityID`.
- The table is in 2NF but not 3NF since `country` only depends on `country_code`. We must create a new table to eliminate this transitivity:

### 1a. **Cities**(cityID, name, country_code, population, is_favourite)
   - PK: `cityID`
   - FK: `country_code` references **Countries**
### 1b. **Countries**(country_code, country)
   - PK: `country_code`

**Notes:**
- All attributes entirely depend on their key.
- No attributes determine the key.

**Conclusion:** Table 1a and 1b are in **BCNF**.

---

### 2. **Weather**(cityID, last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)

We infer the functional dependency:  
`cityID → last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod`

**Notes:**
- Each attribute is atomic.
- The details of any weather data can be determined by its `cityID`.
- No attributes determine the key.

**Conclusion:** Table 2 is in **BCNF**.

---

### 3. **Forecast**(cityID, last_updated, forecastJSON)

We infer the functional dependency:  
`cityID → last_updated, forecastJSON`

**Notes:**
- Each attribute is atomic.
- The details of any forecast data can be determined by its `cityID`.
- No attributes determine the key.

**Conclusion:** Table 3 is in **BCNF**.

---

## Normalized Tables (all in BCNF):

1. `Cities(cityID, name, country_code, population, is_favourite)`  
   - PK: `cityID`
   - FK: `country_code` references **Countries** table

2. `Countries(country_code, country)`
   - PK: `country_code`

3. `Weather(cityID, last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)`  
   - PK: `cityID` 
   - FK: `cityID` references **Cities** table

4. `Forecast(cityID, last_updated, forecastJSON)`  
   - PK: `cityID`
   - FK: `cityID` references **Cities** table

Note: The relationship between **Cities** and **Countries** is now set as **LocatedIn** with full participation from **Cities** and partial participation from **Countries.** It is one-to-many since each city belongs to one country and each country may have many cities (check normalized ER Diagram for more information).