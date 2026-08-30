# Train Ticket Reservation System

A console-based Java application for managing train reservations, built with Maven and backed by a MySQL database.

## Features

- **Train management** — add new trains, update train information, view all trains, and deactivate a train instead of permanently deleting it
- **Journey management** — view scheduled journeys and cancel a scheduled journey
- **Booking history** — view booking history and filter it by PNR, customer, or date
- **Admin profile** — view and update the admin profile

## Tech Stack

- **Language:** Java 17
- **Build tool:** Maven
- **Database:** MySQL (via `mysql-connector-j`)
- **Runner:** `exec-maven-plugin`

## Project Structure

```
train_ticket_reservation_system/
├── database/     # Database scripts / schema
├── src/          # Java source code
├── pom.xml       # Maven project configuration
└── README.md
```

## Prerequisites

- JDK 17 or later
- Apache Maven
- MySQL Server

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/Kavindu0331/train_ticket_reservation_system.git
   cd train_ticket_reservation_system
   ```

2. **Set up the database**

   Create a MySQL database and run the setup script(s) in the `database/` folder against it. Update your database connection details (host, username, password) to match your local MySQL setup wherever they're configured in the source code.

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn exec:java
   ```

   This runs the app's entry point (`com.trainreservation.Main`) as configured in `pom.xml`.

## Dependencies

| Dependency | Version | Purpose |
|---|---|---|
| `mysql-connector-j` | 9.4.0 | JDBC driver for connecting to MySQL |

## Contributing

This is a group project split into member-owned modules.


##Happy Coding...

## License

No license specified yet — add one (e.g. MIT) if you intend for others to reuse this code.
