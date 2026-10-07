# Library Management System — Advanced Java Practical

A complete JSP + Servlet + JDBC + MySQL web application built with the MVC
pattern and the DAO pattern. No Spring Boot, no frameworks — pure Java EE.

## Tech stack
Java, JSP, Servlets (Jakarta Servlet API 5.0 / 6.0), HTML5, CSS3, JavaScript,
JDBC, MySQL, JavaBeans, DAO pattern, MVC architecture, Apache Tomcat.

---

## 1. Project structure

```
LibraryManagementSystem/
├── src/main/java/com/library/
│   ├── controller/   (18 servlets)
│   ├── model/        (Admin, Book, Member, Transaction JavaBeans)
│   ├── dao/           (AdminDAO, BookDAO, MemberDAO, TransactionDAO)
│   └── util/          (DBConnection, FineConfig, AuthFilter)
├── WebContent/
│   ├── *.jsp           (17 JSP pages)
│   ├── css/style.css
│   ├── js/script.js
│   └── WEB-INF/
│       ├── web.xml
│       └── lib/        (put mysql-connector-j here)
├── database/
│   └── library_management.sql
└── README.md
```

---

## 2. IMPORTANT — Tomcat 9 vs Tomcat 10

**This build is already configured for Tomcat 9** (`javax.servlet.*` imports,
`web.xml` at Servlet 4.0 / `javax` namespace). If your Tomcat is 9.x, you
don't need to change anything — just deploy as-is.

If you later move to **Tomcat 10 or 11**, they use the newer `jakarta.servlet.*`
package instead. To convert:
1. In every `.java` file under `controller/` and `util/AuthFilter.java`,
   change `import javax.servlet.*` → `import jakarta.servlet.*` (and the
   `javax.servlet.http.*` imports → `jakarta.servlet.http.*`).
2. In `WEB-INF/web.xml`, change the root `<web-app>` element's namespace
   from `http://xmlns.jcp.org/xml/ns/javaee` to
   `https://jakarta.ee/xml/ns/jakartaee`, and the version to `5.0`.

Easiest fix in your editor: **Find/Replace** `javax.servlet` with
`jakarta.servlet` across the project. Everything else (JSPs, DAO classes,
SQL) works unchanged on either version.

---

## 2b. This build is configured for ORACLE, not MySQL

`DBConnection.java` and the DAO queries are already switched to Oracle syntax.
Use `database/library_management_oracle.sql` (not the MySQL one) to create
your schema. Steps:

1. **Create a schema/user** (skip if you already have one). Connect as SYS:
   ```
   sqlplus sys/your_sys_password@localhost:1521/XEPDB1 as sysdba
   CREATE USER library_admin IDENTIFIED BY library123;
   GRANT CONNECT, RESOURCE, CREATE VIEW, UNLIMITED TABLESPACE TO library_admin;
   ```
2. **Run the Oracle script** as that user:
   ```
   sqlplus library_admin/library123@localhost:1521/XEPDB1 @database/library_management_oracle.sql
   ```
3. **Get the Oracle JDBC driver jar.** You already have Oracle installed, so
   you don't need to download anything — it ships inside your Oracle home,
   typically at:
   ```
   %ORACLE_HOME%\jdbc\lib\ojdbc11.jar        (Windows)
   $ORACLE_HOME/jdbc/lib/ojdbc11.jar         (Linux)
   ```
   Copy that jar into `WebContent/WEB-INF/lib/`.
4. **Set your credentials** in `DBConnection.java` — `DB_URL`, `DB_USER`,
   `DB_PASSWORD` — to match what you created in step 1. If your Oracle is
   an older XE with a SID instead of a pluggable database, use
   `jdbc:oracle:thin:@localhost:1521:XE` instead of the `/XEPDB1` form.
5. Everything else (Eclipse import, Tomcat, login, testing) is unchanged —
   follow Section 3 below as-is.

**Note on syntax differences already handled for you:** Oracle doesn't
support MySQL's `LIMIT` or `CURDATE()`, so the queries in
`MemberDAO.generateNextMemberId()` and `TransactionDAO` (recent
transactions, overdue check) already use the Oracle-compatible
`FETCH FIRST n ROWS ONLY` and `TRUNC(SYSDATE)` instead. If you ever switch
back to MySQL, you'd need to revert those two spots and swap back to
`database/library_management.sql` + the MySQL block commented at the top
of `DBConnection.java`.

---

## 3. Step-by-step setup

### Step 1 — Install MySQL
Install MySQL Server (5.7+ or 8.x) and MySQL Workbench (optional, for a GUI).
Verify it's running: `mysql -u root -p`

### Step 2 — Create the database and tables
Run the provided script:
```
mysql -u root -p < database/library_management.sql
```
Or open `database/library_management.sql` in MySQL Workbench and execute it.
This creates the `library_management` database, all 4 tables, the default
admin login, and sample books/members/one sample transaction for testing.

### Step 3 — Add the MySQL Connector/J driver
Download **mysql-connector-j** (the JDBC driver, e.g. `mysql-connector-j-8.3.0.jar`)
from the official MySQL site, and copy the `.jar` file into:
```
WebContent/WEB-INF/lib/
```
In Eclipse, this jar is automatically picked up as long as it's inside
`WEB-INF/lib` of the deployed project.

### Step 4 — Configure your DB username/password
Open `src/main/java/com/library/util/DBConnection.java` and edit:
```java
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "YOUR_MYSQL_PASSWORD";
```
Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password.

### Step 5 — Import into Eclipse
1. Eclipse → File → New → **Dynamic Web Project**.
2. Name it `LibraryManagementSystem`, target runtime = your installed Tomcat,
   Dynamic web module version = 5.0 (Tomcat 10) or 4.0 (Tomcat 9).
3. Copy the contents of `src/main/java/` into the project's `src` (or
   `Java Resources/src`) folder, preserving the `com/library/...` package
   structure.
4. Copy the contents of `WebContent/` into the project's `WebContent`
   (or `src/main/webapp`) folder, replacing the generated `web.xml`
   with the one provided.
5. Right-click the project → Properties → Java Build Path → Libraries →
   confirm the Tomcat server library and the `mysql-connector-j` jar
   (in WEB-INF/lib) are both listed.

### Step 6 — Configure Apache Tomcat
1. Window → Preferences → Server → Runtime Environments → Add → point to
   your Tomcat 9/10 installation folder.
2. Right-click the project → Run As → Run on Server → select your Tomcat.

### Step 7 — Start Tomcat and open the app
Once Tomcat starts, open:
```
http://localhost:8080/LibraryManagementSystem/
```
You should land on the login page (`login.jsp` is the welcome file).

### Step 8 — Login
Username: `admin` — Password: `admin123`

### Step 9 — Test every feature
1. **Add a book** — Books → Add Book → fill the form → Save.
2. **Add a member** — Members → Add Member → fill the form → Save.
3. **Issue a book** — Issue Book → pick the member and book you just
   created → set issue/due dates → Issue Book. Confirm the book's
   Available count drops by 1 on the Books page.
4. **Return a book** — Return Book → click Return on the transaction.
   Confirm Available count increases by 1, and the transaction moves to
   "Returned" in Transaction History.
5. **Verify fine calculation** — Issue a book with a **due date in the
   past** (e.g. yesterday), then return it immediately. The Transactions
   page will show a non-zero fine (₹5/day × days late).
6. **Test Edit/Delete/Search** — Edit a book or member, delete a test
   record, and try the Search page for books, members, and transactions.

---

## 4. Fine calculation logic

Configured in one place: `com.library.util.FineConfig`
```java
public static final double FINE_PER_DAY = 5.0;      // ₹5 per late day
public static final int DEFAULT_LOAN_DAYS = 7;        // default due-date offset
```
When a book is returned (`TransactionDAO.returnBook`), the number of days
between the due date and today's date is calculated. If positive, the fine
is `lateDays * FINE_PER_DAY`, stored in the `fine` column and shown on the
Transactions page.

Example: Due date = 10 Sep, Return date = 13 Sep → 3 late days → fine = ₹15.

---

## 5. Troubleshooting

| Problem | Likely cause / fix |
|---|---|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | `mysql-connector-j.jar` isn't in `WEB-INF/lib`. Re-add it, then clean & restart Tomcat. |
| `SQLException: Access denied for user 'root'@'localhost'` | Wrong password in `DBConnection.java`. Update `DB_PASSWORD` to match your MySQL install. |
| `Communications link failure` / connection refused | MySQL service isn't running. Start it (`services.msc` on Windows, or `sudo systemctl start mysql` on Linux). |
| `Unknown database 'library_management'` | You haven't run `library_management.sql` yet, or ran it against the wrong MySQL instance. |
| HTTP 404 on any servlet URL | Check the URL pattern in `web.xml` matches exactly what the JSP `action`/`href` uses (case-sensitive), and that the project deployed cleanly (check Tomcat console for compile errors). |
| HTTP 500 / white error page | Open the Tomcat console/log — it will show the real Java exception (usually a NullPointerException from a missing form field, or a SQLException). The custom `error.jsp` only shows a friendly message; the real stack trace is in the server log. |
| JSP won't compile / "The type X is not visible" | Make sure the corresponding `<%@ page import="com.library.model.X" %>` is present at the top of the JSP. |
| Foreign key constraint fails on delete | You're trying to delete a book or member that still has transaction rows referencing it. Either delete the related transactions first, or note this is expected referential-integrity behavior (explain this in viva!). |
| Tomcat won't start / port 8080 in use | Another process is using port 8080 (maybe another Tomcat instance or Skype). Change the port in `server.xml` or stop the conflicting process. |
| `jakarta.servlet` not found (red squiggly imports) | You're on Tomcat 9 — see Section 2 above and switch to `javax.servlet`. |
| Login always fails even with admin/admin123 | Confirm the `admin` table actually has the row (`SELECT * FROM admin;`). If you re-ran the SQL script it resets to admin/admin123 by default. |

---

## 6. Viva preparation

### Core concepts, explained simply

**What is JSP?**
JavaServer Pages — HTML pages that can embed Java code, used as the "View"
in MVC. Here JSPs display data (books, members, transactions) that the
servlet already fetched; they don't talk to the database directly.

**What is a Servlet?**
A Java class that runs on the server and handles HTTP requests (GET/POST).
Acts as the "Controller" — it reads form input, calls the DAO layer, and
forwards to a JSP to render the result.

**What is JDBC?**
Java Database Connectivity — the standard Java API for connecting to and
querying a relational database (here, MySQL) using `Connection`,
`PreparedStatement`, and `ResultSet`.

**What is a JavaBean?**
A plain Java class with a no-arg constructor, private fields, and
public getters/setters. `Book`, `Member`, `Transaction`, `Admin` are the
JavaBeans (also called "Model") that carry data between layers.

**What is DAO (Data Access Object)?**
A design pattern that isolates all SQL/JDBC code into dedicated classes
(`BookDAO`, `MemberDAO`, ...) so servlets never touch SQL directly. This
keeps database logic separate from business/controller logic.

**What is MVC?**
Model-View-Controller: Model = JavaBeans, View = JSP, Controller = Servlet.
Flow: **JSP (form) → Servlet → DAO → JDBC → MySQL**, then the servlet
forwards to a JSP again to show the result.

**What is CRUD?**
Create, Read, Update, Delete — the four basic data operations, implemented
here for both Books and Members (Add/View/Edit/Delete).

**How does book issue work?**
`IssueBookServlet` validates the form, then calls
`TransactionDAO.issueBook()`, which — inside one JDBC transaction —
checks `available_quantity > 0`, inserts a new row into `transactions`
with status `ISSUED`, and decrements `available_quantity` by 1. If no
copies are available it rolls back and reports "Book is currently
unavailable."

**How does book return work?**
`ReturnBookServlet` calls `TransactionDAO.returnBook(transactionId)`,
which looks up the transaction, refuses to act if it's already
`RETURNED`, computes any fine, sets `return_date`, `fine`, and
`status = RETURNED`, and increments the book's `available_quantity`
by 1 — all inside one JDBC transaction so partial updates can't happen.

**How is fine calculated?**
`lateDays = returnDate - dueDate` (only if positive), `fine = lateDays × ₹5`
(rate configured in `FineConfig.FINE_PER_DAY`).

**How does the MySQL connection work?**
`DBConnection.getConnection()` loads the `com.mysql.cj.jdbc.Driver` class
once (in a static block) and returns a new `Connection` via
`DriverManager.getConnection(url, user, password)` for every DAO call.

**Why PreparedStatement instead of Statement?**
It precompiles the SQL and binds parameters separately from the query
text, which prevents SQL injection and is faster for repeated queries.

**Why foreign keys?**
They enforce referential integrity at the database level — a transaction
can't reference a member or book that doesn't exist, and MySQL will
refuse to silently orphan data.

**How does session authentication work?**
On successful login, `LoginServlet` stores the admin's username in
`HttpSession`. `AuthFilter` (mapped to `/*` in `web.xml`) checks every
request for that session attribute; if it's missing (not logged in), the
filter redirects to `login.jsp`. `LogoutServlet` calls
`session.invalidate()` and redirects back to login.

---

### 25 likely Advanced Java viva questions

1. **What is the difference between JSP and Servlet?**
   Servlets are pure Java classes that generate output programmatically;
   JSP is HTML-first with embedded Java, compiled into a servlet behind
   the scenes. JSP is easier for the "view" layer.

2. **What is the servlet life cycle?**
   `init()` (once, on first load) → `service()` → `doGet()`/`doPost()`
   (per request) → `destroy()` (once, on shutdown/undeploy).

3. **What is the difference between `doGet()` and `doPost()`?**
   `doGet` is for retrieving data (parameters in the URL, idempotent);
   `doPost` is for submitting/changing data (parameters in the request
   body, not idempotent, no length limit).

4. **What is a Filter and how is it different from a Servlet?**
   A Filter intercepts requests/responses before they reach a servlet
   (e.g. for authentication, logging). Unlike a servlet, it doesn't
   generate a response itself — it calls `chain.doFilter()` to pass
   control along.

5. **How did you implement login protection here?**
   Via `AuthFilter`, mapped to `/*`, which checks `HttpSession` for an
   `admin` attribute and redirects unauthenticated requests to `login.jsp`.

6. **What is `HttpSession` and how long does it last here?**
   Server-side storage tied to a client via a cookie (`JSESSIONID`).
   Configured here with a 30-minute timeout (`session-config` in `web.xml`).

7. **Why did you close JDBC resources in try-with-resources?**
   To guarantee `Connection`/`PreparedStatement`/`ResultSet` are closed
   even if an exception occurs, preventing connection leaks.

8. **What is connection pooling, and does this project use it?**
   Reusing a pool of open DB connections instead of opening a new one per
   request. This project uses simple `DriverManager` connections for
   simplicity (typical for a college practical); in production you'd use
   a pool like HikariCP or Tomcat's JNDI DataSource.

9. **What does `available_quantity` represent and why track it separately
   from `quantity`?**
   `quantity` is the total copies owned; `available_quantity` is how many
   are currently on the shelf (not issued). Tracking both lets you show
   "3 of 5 copies available" and prevents issuing more copies than exist.

10. **How do you prevent double-returning the same transaction?**
    `returnBook()`'s UPDATE statement includes `WHERE status != 'RETURNED'`
    and checks the affected row count; if 0 rows were updated it reports
    `ALREADY_RETURNED` instead of silently succeeding.

11. **How do you prevent `available_quantity` from going negative?**
    The book-quantity UPDATE in `issueBook()` includes
    `WHERE available_quantity > 0`, and `adjustAvailableQuantity()` checks
    `available_quantity + delta >= 0` before applying the change.

12. **What is the difference between `forward()` and `sendRedirect()`?**
    `forward()` is server-side, same request/response, URL doesn't change,
    faster. `sendRedirect()` sends a new HTTP request from the browser to a
    new URL, so the URL bar changes and it's a fresh request (used after
    POST actions here, to prevent duplicate form re-submission on refresh).

13. **Why redirect after a POST (Post/Redirect/Get)?**
    So refreshing the resulting page doesn't resubmit the form (e.g.
    re-adding the same book twice).

14. **What is `web.xml` used for?**
    The deployment descriptor — maps servlet classes to URL patterns,
    configures the filter, session timeout, welcome file, and error pages.

15. **What is the welcome file in this project?**
    `login.jsp`, configured under `<welcome-file-list>` so it loads at the
    context root URL.

16. **How is a many-to-many-like relationship modeled here?**
    It isn't many-to-many; `transactions` is a linking/junction table with
    foreign keys to both `members` and `books`, recording each individual
    issue event (one row per issue).

17. **What is `ON DELETE CASCADE` doing in your schema, and is it a risk?**
    It automatically deletes transaction rows when their member or book is
    deleted, so you don't get orphaned foreign keys. The risk is silently
    losing transaction history — an alternative would be `ON DELETE
    RESTRICT` to block deletion instead.

18. **How would you show only overdue books?**
    `refreshOverdueStatuses()` runs an UPDATE that flips any `ISSUED`
    transaction past its due date to `OVERDUE`; the Transactions page then
    filters `WHERE status = 'OVERDUE'`.

19. **What Java class did you use for dates, and why?**
    `java.sql.Date` for JDBC read/write (it maps to SQL DATE columns), and
    `java.time.LocalDate` for date arithmetic (computing late days) via
    `ChronoUnit.DAYS.between()`.

20. **How does client-side validation differ from server-side, and why
    have both?**
    Client-side (JavaScript in `script.js`) gives instant feedback and
    reduces round-trips, but can be bypassed (disabled JS, direct HTTP
    calls). Server-side (in the servlets) is the real gatekeeper that
    actually protects the database.

21. **How do you generate the Member ID automatically?**
    `MemberDAO.generateNextMemberId()` reads the highest existing
    `member_id`, strips non-digits, increments, and prefixes `MEM`
    (e.g. `MEM1004` → `MEM1005`).

22. **What would happen if two admins issued the last copy of a book at
    the exact same time?**
    The `issueBook()` method uses `SELECT ... FOR UPDATE` inside a
    transaction to lock the book row, so the second transaction waits and
    then correctly sees `available_quantity = 0` and is rejected.

23. **Why use `BigDecimal` for the fine amount instead of `double`?**
    `BigDecimal` avoids floating-point rounding errors for money values,
    important even for simple per-day fines.

24. **What's the difference between `Statement` and `PreparedStatement`
    performance-wise?**
    `PreparedStatement` is precompiled once by the DB and can be reused
    with different parameters efficiently; `Statement` is parsed and
    compiled fresh every single execution.

25. **How is this project's architecture different from using Spring
    Boot?**
    Spring Boot auto-configures the server, dependency injection, and
    often uses annotations + an embedded connection pool/ORM (JPA/
    Hibernate) instead of raw JDBC. This project deliberately uses plain
    Servlets/JSP/JDBC and explicit `web.xml` configuration so every wiring
    step is visible and explainable — which is the point of an Advanced
    Java (pre-Spring) practical.

---

## 7. Final quality checklist

- [x] Frontend (HTML5/CSS3/JS) — professional sidebar dashboard, responsive
- [x] Java backend — 18 servlets, MVC architecture
- [x] Database connection — JDBC + MySQL via `DBConnection`
- [x] Complete CRUD — Books and Members (Create/Read/Update/Delete)
- [x] Login/Logout — session-based, protected by `AuthFilter`
- [x] Issue/Return — with quantity checks and rollback-safe transactions
- [x] Fine calculation — configurable, ₹5/day, `BigDecimal`-safe
- [x] Search — books, members, transactions, all via `PreparedStatement`
- [x] Transaction history — with status filters
- [x] No SQL in JSPs — all queries live in DAO classes
- [x] No dummy buttons — every action hits a real servlet + DB write
