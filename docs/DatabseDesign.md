# Conversion of ER Diagram to Relational Tables

The **experiences** and **forecasts** relationships between **cities**, **weather**, and **forecast** need not be stored explicitly. Instead, we store the primary key of either side. Since the **Weather** and **Forecast** tables have full participation and can only exist alongside a city, we must use `cityID` as the primary key for all related tables.

This gives us the following tables:

1. `City(cityID, name, country, is_favourite)`  
   - PK: `cityID`
   
2. `Weather(cityID, last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)`  
   - PK: `cityID` references **City** table

3. `Forecast(cityID, last_updated, forecastJSON)`  
   - PK: `cityID` references **City** table

---

## Normalization

### 1. **City**(cityID, name, country, is_favourite)  

We infer the functional dependency:  
`cityID → name, country, is_favourite`

**Notes:**
- Every attribute is atomic.
- The details of any city can be determined by its `cityID`.
- No attributes determine the key.

**Conclusion:** Table 1 is in **0NF**, **1NF**, **2NF**, **3NF**, and **BCNF**.

---

### 2. **Weather**(cityID, last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod)

We infer the functional dependency:  
`cityID → last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunset, sunrise, tod`

**Notes:**
- Each attribute is atomic.
- The details of any weather data can be determined by its `cityID`.
- No attributes determine the key.

**Conclusion:** Table 2 is in **0NF**, **1NF**, **2NF**, **3NF**, and **BCNF**.

---

### 3. **Forecast**(cityID, last_updated, forecastJSON)

We infer the functional dependency:  
`cityID → last_updated, forecastJSON`

**Notes:**
- Each attribute is atomic.
- The details of any forecast data can be determined by its `cityID`.
- No attributes determine the key.

**Conclusion:** Table 3 is in **0NF**, **1NF**, **2NF**, **3NF**, and **BCNF**.

---

Since all tables are in **BCNF**, the original three tables are finalized.