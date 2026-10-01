# JQuiz

JQuiz is an Android quiz application developed in Kotlin as a two-person academic project at **Université Paris Cité**.

The application dynamically generates different types of quizzes from structured datasets and provides multiple difficulty levels, learning features, game history, notifications and UI customization.

## Features

### Quiz modes

JQuiz supports four types of questions:

- **Multiple choice** — select the correct answer among several options
- **Precision** — provide or select a precise value
- **Dynamic sort** — reorder elements according to a given property
- **Matching** — match related elements together

For example, a matching quiz can associate historical events with their corresponding dates.

### Knowledge domains

Questions are generated from five domains:

- Geography
- History
- Sports
- Astronomy
- Chemistry

Each domain has its own data model, such as countries, historical events, football teams, planets and chemical elements.

### Difficulty

Difficulty is based on the notoriety of the information used to generate a question.

For example, highly recognizable countries can be used for easier geography questions, while less familiar ones are selected for higher difficulty levels.

### Learning mode

A learning mode allows users to review information before taking a quiz.

The selected domains are presented as flip cards, with the entity on the front and its associated information on the back.

### Additional features

- Game history
- Configurable quiz difficulty
- Reminder notifications with configurable frequency
- Theme and appearance settings
- Local persistence of application data and preferences

## Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Room**
- **DataStore**
- **WorkManager**
- **Gradle**
- **Android Studio**

The application follows an MVVM-oriented architecture, separating UI, application state and data access.

## Data

The application database is initialized from JSON datasets bundled with the project.

The seed datasets were generated with the assistance of ChatGPT for the purposes of this academic project and are not intended to be used as authoritative reference datasets.

Database initialization is handled by:

```text
data/seed/DatabaseSeeder
```

## Project Structure

The application is organized around:

- UI screens and reusable Compose components
- ViewModels for application state and business logic
- Room entities and DAOs for persistent data
- Generic quiz-generation logic
- DataStore for user preferences
- WorkManager for scheduled notifications

## Getting Started

Clone the repository:

```bash
git clone https://github.com/ynanme/jquiz.git
cd jquiz
```

Open the project in **Android Studio** and let Gradle synchronize the dependencies.

You can then run the application on an Android emulator or a compatible physical device.

## Authors

Developed by:

- [Yendoumban NANMEDIGOU](https://github.com/ynanme)
- [Mohamed Idris BENARBIA](https://github.com/JupiterRoue45)

as an academic group project at **Université Paris Cité**.