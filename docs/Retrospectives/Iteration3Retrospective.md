# Iteration 3 Retrospective

**What went well:**  
The things added to improve iteration 3 based on thoughts from iteration 2 went significantly well. Namely, an extended forecast is now available for the next five days with three hour steps. This was never planned beforehand and was brought in through agile methodology. Additionally, the thought to increase cities supported to around 1000 was grossly overshot and the app now supports over 126,000 cities, searching for them near-instantly with an optimized query that orders by population and indices. 

---

**What didn't go well:**
The offline support feature was forgone due to time restrictions and coming to the conclusion that the major refactoring required was not worth it. Most users have access to internet, be it Wifi or Cellular network. Trading the feature with the many additions to the first two features panned out well.

---

**What could be improved:**  
Although the cities supported is now over 126,000, the one downside from searching with city name and country code is that it blocks duplicate results. Since there's no depth to the search beyond these two parameters, and country with the same cities is being excluded. This dropped the cities supported by OpenWeatherMap API from over 200,000 to the 126,000-plus it is now. To improve, we can add stronger parameters and finer searches.

---

**New thoughts to add to future updates?:**
- Call API for a city with more precision to allow for duplicate city-country pairs as well
- Add more details to extended forecasting
- Add a default city that loads up every time the app is launched
- Add more intricate ordering of favourites beyond population (user's top three, by continent, etc.)