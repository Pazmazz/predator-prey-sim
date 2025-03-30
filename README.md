# Predator-Prey Simulation

This application simulates a simple world of bugs which inhabit the roles of _predators_ and _prey_

## Libraries used

_This application does not use any external third-party libraries, or any library/API that is not built directly into Java_

- `java.util`

  - General utility

- `java.awt`

  - Provides abstract interfaces for manipulating GUI components

- `javax.swing`
  - Provides API for creating and manipulating GUI components

## Native non-trivial classes used

- `Thread`

  - Required for creating the main game thread for a given game instance. Inside every new game instance is where the game loop executes.

- `Map`
- `HashMap`
- `JPanel`

  - Responsible for creating the GUI container components

- `JFrame`

  - Responsible for creating the top-level window GUI component

- `Pattern`

  - Required for parsing the string content inside the Console print methods for text color directives (such as `"$text-blue"`)

- `Matcher`
  - Works in tandem with `Pattern` for matching regex
