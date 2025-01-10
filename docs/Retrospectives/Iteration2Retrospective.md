# Iteration 2 Retrospective

**What went well:**  
Everything for iteration 2 was done on time, ahead by quite a few days. The major overhaul necessary for the feature was implemented without any glaring problems. Although some changes were made to the planning, it was reasonably well followed; the drop-down was finished with 500 major cities, the database was set up well and connected, and the UI was nicely done. The current database setup also allows for easy updates. When more cities are added, it would be as simple as using the onUpgrade method of the SQLiteDatabaseHelper while preserving favourites. The overarching project structure was also maintained, allowing for a separation of concerns across the three layers. 

---

**What didn't go well:**  
The greatest obstacle came with testing, particularly with testing the DatabaseHelper class. The main problem was that preserving the internal storage of favourites was impossible after running the instrumented test for this class. Since the DB was stored internally, it allowed for multiple instances (the main DB and a stub for testing). All was done well with the two existing together; however, android tests are configured to clean up after running. This meant that regardless of using different databases and ensuring isolation between production and testing, the internal storage and any stored favourites were permanently deleted. Converting to an in-memory database for testing proved no better; the main DB was still lost. I chose against entirely changing the database design and accepting the quirk of deletion for this iteration.

---

**What could be improved:**  
The branching was better than iteration one; however, a few branches failed to serve their purpose or were used for purposes other than their intended ones. Although it was an improvement from earlier, it did show that creating user stories and dev tasks still requires improvement; minimizing overlap for iteration 3 will be needed.

---

**New thoughts to add to Iteration 2:**
- Fuzzy search mechanic (low priority)
- Add 500 more cities or until a noticeable drop in performance
- Expand on the weather API to include 5 day/3-hourly forecast as a new, separate user story
- Improve overall UI