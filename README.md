# 🧭 CS 301 Group Project — Transgender Safety & Aid Platform

---

## 📘 Project Name
**Trans Safety (temp title)**  
A community-driven platform designed to help transgender individuals locate safe, inclusive, and affirming businesses and services. The project connects users to verified resources such as healthcare providers, retail stores, and personal care services—while allowing them to share reviews, highlight safe spaces, and contribute to a supportive network of trusted locations.

---

## 👥 Group Members / Contributors
Original Project Idea: Hayley
| Name | Role | GitHub Username |
|------|------|-----------------|
| Aaron Alaman | Project Manager / Backend Developer / Tester / Documentation | @aaronalaman |
| Justin Brown | Frontend Developer / Tester / Documentation | @sixplanet312 |

---

## 🧭 Brief Background About the Project
Transgender individuals often face challenges finding safe, affirming, and inclusive spaces for everyday needs such as healthcare, retail, and personal care. Many existing platforms lack proper verification or community-driven insight to ensure these environments are genuinely supportive. Transgender Safety & Aid (TSA) addresses this issue by providing a centralized platform that connects users to verified, trans-friendly businesses and services. Through features like community reviews, safe space tagging, and categorization by service type, TSA empowers users to make informed decisions and fosters a stronger sense of safety and belonging within their communities.

---

## ⚙️ Current Features 
1. Filter & Search Functionality — users can search for businesses by name, location, or keywords and apply filters for service type (Upcoming).
2. Service Categorization — businesses are organized by categories such as healthcare, retail, or personal care.
3. User Account Creation & Login — enables users to create accounts, securely log in, and access personalized features (In Progress).
4. User Reviews & Ratings — allows users to submit reviews and ratings for businesses.
5. Defined Safe Spaces Marking — businesses and services are marked as “safe-to-go” or affirming.
6. Community Tagging & Highlight Features — users can highlight specific affirming aspects such as gender-neutral bathrooms or staff training.
7. Database Integration — MongoDB connection stores all user accounts, business listings, and reviews (COMPLETED).

---

## 🧩 Planned Features / Future Work
1. Verification & Anti-Griefing System — new accounts must verify identity, be verified by existing users, or wait a set period before contributing safe-space markers.
2. Report & Moderation Tools — allow users to flag false or malicious entries and enable moderators to review and take action.
3. Mobile App Development — extend the desktop platform to mobile devices for broader accessibility.
4. Enhanced Filtering & Sorting Options — advanced search filters, sorting by user ratings, or location proximity.
5. Community Engagement Features — additional tagging, discussion forums, or support networks within the app.

---

## 🗓️ Schedule Alignment & Progress Review  

| **Week** | **Milestone / Deliverables** | **Owners** | **Evidence (PRs / Links / Demos)** | **Status** |
|-----------|------------------------------|-------------|------------------------------------|-------------|
| **Oct 26 – Nov 1** | Set up MongoDB and establish connection. Configure JavaFX with Maven and dependencies. Verify CRUD operations via `DBTest.java`. | Aaron | GitHub commits, `DBTest.java` test results, MongoDB connection screenshot/log | ✅ **Completed** <br> MongoDB successfully set up and integrated with JavaFX. CRUD operations verified and functional. |
| **Nov 2 – Nov 8 & Nov 9 – Nov 15** | Implement user account creation and login functionality. Add database integration for user data storage and retrieval. Include input validation and error handling for authentication. Begin UI layout and design of necessary windows. | Team | PRs for authentication logic, screenshots/demos of login UI, database connection tests (`DBTest.java`) | ⚙️ **In Progress** <br> Focus shifted to user account creation earlier than planned. Awaiting verification on Justin’s local machine to ensure `DBTest.java` success. Once confirmed, `users` collection implementation will proceed. |
| **Nov 16 – Nov 22** | Implement search and filter functionality. Add service categories (healthcare, retail, personal care, etc.). Display dynamic search results. | Team | Commit history, screenshots or demo of search/filter interface | ⏳ **Not Started (Upcoming)** |
| **Nov 23 – Nov 29** | Develop user review and rating system. Connect reviews to corresponding businesses in the database. | Team | PRs or commits showing review logic integration, UI demo of review submission | ⏳ **Not Started** |
| **Nov 30 – Dec 6** | Define Safe Space Markings (per Requirements Specification). Continue refining UI design and accessibility improvements. Adjust schedule as needed for holidays or extended tasks. | Team | Commit references, updated UI screenshots, documentation changes | ⏳ **Not Started** <br> Timeline may shift due to availability or feature extension. |
| **Dec 7 – Dec 10 (Final)** | Final review and polish. Ensure full functionality with each feature. Resolve any remaining issues. Finalize documentation and submit project. | Team | Final commits, video demo, setup guide, final report | ⏳ **Not Started** |

---

## 🧠 Definition of Done (DoD)
A feature is **done** when:
- Code is reviewed and merged into `main`  
- All acceptance criteria are met  
- Unit tests pass in CI/CD  
- Demo shows functional feature without breaking existing functionality  
- Documentation (README / inline comments) is up to date  

---

## 💻 How to Run
***WIP***
Note (will finalize for submission later):
- java
- mvn
- javafx
- mongodb driver 
- mongo compass environment download

---
