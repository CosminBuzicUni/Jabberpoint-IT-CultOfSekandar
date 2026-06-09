# JabberPoint

JabberPoint is a small Java presentation app that was improved with a focused redesign. The current codebase applies three design patterns where they fit the problem best:

- Composite for slide content structure
- Observer for presentation-to-view updates
- Command for controller actions

The code was also cleaned up to better match the NHL Stenden coding style used in the course: clear naming, small focused classes, readable structure, and consistent comments around public behavior.

## What was changed

- Added Composite support for hierarchical slide content through `SlideItem` and `CompositeSlideItem`
- Kept the Observer-based presentation update flow between `Presentation` and `SlideViewerComponent`
- Added Command-based actions for all user inputs: navigation (next, previous, goto), exit, and file operations (open, new, save)
- Added unit tests and JaCoCo coverage reporting
- Added CI/CD support so the project can be built and tested automatically

## Build and test

This project uses the Gradle wrapper, so you do not need a local Gradle install when `gradlew` / `gradlew.bat` is available in the repo.

### Test executables (must-know)

- Primary test executable (Windows): `./gradlew.bat`
- Primary test executable (macOS/Linux): `./gradlew`
- Alternative local test script: `./run-tests.ps1`

Run all Gradle tests:

```powershell
.\gradlew.bat test
```

Run tests with the PowerShell script:

```powershell
.\run-tests.ps1
```

Generate the coverage report:

```powershell
.\gradlew.bat test jacocoTestReport
```

## Run the project locally

Compile the application classes:

```powershell
.\gradlew.bat classes
```

Start the app:

```powershell
java -cp build/classes/java/main JabberPoint
```

Executable path for local development:

- Classpath: `build/classes/java/main`
- Main class: `JabberPoint`
- Full command: `java -cp build/classes/java/main JabberPoint`

Note: the built-in demo presentation expects image files from the original JabberPoint materials. If those images are missing, run the app with your own XML presentation that only uses text items, or add the missing image files.

If the wrapper scripts are not available for some reason, install Gradle locally and run the same tasks with `gradle` instead.

## CI/CD

The GitHub Actions pipeline runs on pushes and pull requests to `main`. It compiles the project, runs the unit tests, and publishes JaCoCo/JUnit artifacts for review.

### DTAP mapping used in this project

- Development (D): `build` job compiles sources and creates the executable artifact.
- Testing (T): `test` job runs unit tests and coverage (`jacocoTestReport`).
- Acceptance (A): `acceptance` environment consumes the artifact and runs acceptance gate steps.
- Production (P): `production` environment consumes the artifact and runs deployment steps.

Executable artifact path in pipeline:

- Built file: `JabberPoint.jar`
- Uploaded artifact name: `app`
- Produced in the `build` job and downloaded in `acceptance` and `production`.
