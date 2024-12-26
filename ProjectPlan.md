# Weather Wise App

## The Vision
Our Weather App aims to provide users with quick, reliable, and visually appealing access to real-time and forecasted weather information. It helps users plan their activities effectively, offering offline support, favorite location management, and personalized weather alerts.

## Features
- **Real-time weather**: Provide hourly weather conditions for a selected location.
- **Favourite locations**: Allow users to save and manage their favourited locations.
- **Offline support**: Sync favourited locations’ weather for the immediate hour.

---

## Iteration 1: Real-time Weather
### User Stories
**"As a user, I want to see the current temperature for my city so that I can plan my day."**

#### Dev Tasks:
- Set up project structure in Java (1 hour)
- Integrate OpenWeatherMap API to fetch weather data of inputted city (2 hours)
- Parse and format JSON response from API (2 hours)

**"As a user, I want to see my weather on the home screen so I can view it at a glance."**

#### Dev Tasks:
- Create basic UI to display data (2 hours)
- Take in data from the first user story and display it (1 hour)

**"As a user, I want to see any errors if my weather cannot be shown so I know if there is a problem."**

#### Dev Tasks:
- Create probable exceptions (2 hours)
- Convert exceptions to useful error messages (2 hours)

#### Total Estimate for Iteration 1:
12 hours  
**Duration**: 1 week (12 hours ÷ 12 hours/week)

---

## Iteration 2: Favourited Locations
### User Stories
**"As a user, I want to be able to choose my favourite locations in major cities to know the world around me."**

#### Dev Tasks:
- Create UI to store a location (3 hours)
- Create a simple relational database in SQL and connect it to the data layer (2 hours)
- Create logic to integrate the database into the project (2 hours)

**"As a user, I want to be able to see the weather of my favourite cities."**

#### Dev Tasks:
- Create UI to select a city from favourited cities (2 hours)
- Refactor City Manager to also hold favourites (1 hour)

**"As a user, I want to be able to select cities from a drop-down menu and have the choice to filter it so I can easily see the weather in major cities."**

#### Dev Tasks:
- Create a list to hold 200 major city names and align it with a drop-down (2 hours)
- Make UI for the drop-down menu and implement fuzzy/auto-complete search (2 hours)

**"As a user, I want to know about any cities where I cannot see the weather, so I have an idea why it’s a problem."**

#### Dev Tasks:
- Create probable exceptions (2 hours)

#### Total Estimate for Iteration 2:
16 hours  
**Duration**: 1.33 weeks (16 hours ÷ 12 hours/week)

---

## Iteration 3: Offline Support
### User Stories
**"As a user, I want to be able to view immediate weather in case I’m offline."**

#### Dev Tasks:
- Store immediate-hour temperature of favourited cities in the DB (3 hours)
- Take the stored data and display it in the UI as offline reading (2 hours)
- Create UI icon for online and offline status (1 hour)

**"As a user, I want to know if any offline data is unavailable."**

#### Dev Tasks:
- Create probable exceptions (2 hours)

#### Total Estimate for Iteration 3:
8 hours  
**Duration**: 0.67 weeks (8 hours ÷ 12 hours/week)

---

## Version Control Summary
- **Iteration 1 duration**: ~1 week + 3 days of testing = ~1.5 weeks
- **Iteration 2 duration**: ~1.33 weeks + 3 days of testing = ~2 weeks
- **Iteration 3 duration**: ~0.67 weeks + 3 days of testing = ~1 week

---

## Due Dates
- **Iteration 1**: December 29, 2024
- **Iteration 2**: January 12, 2025
- **Iteration 3**: January 19, 2025

Note: The project began on December 19, 2024
