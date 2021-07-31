# EvolutionSimulator

This is my first project written for the "Object-oriented programming" course (pl. "Programowanie obiektowe") at AGH University of Science and Technology. You can read more about the details [here](https://github.com/maximax579/obiektowe-lab/tree/master/lab8).

Application is an evolution simulator - the rectangular grid world is inhabitet by animals and plants. Every epoch is one day in which every animal moves one position (the direction depends on animal's genome) and loses some energy. 
An animal can also eat a plant and gain new energy or breed with another individual. The program has a rich startup configuration and shows many statistics about the simulated world and animals. 

## Features

- Rich startup configuration
- Rich statistics panel for the whole world
- Individual statistics for every animal
- Ability to run two independent simulations
- Simulation can be paused and resumed at any time
- Every animal has a specific genome that determines its behaviour
- The genome of two parents is inherited by their child
- Beautiful world animation and UI

## Screenshots

![Main window screenshot](https://github.com/maximax579/EvolutionSimulator/blob/master/screenshots/Main%20window.png)

![Configuration screenshot](https://github.com/maximax579/EvolutionSimulator/blob/master/screenshots/Configuration.png)

## Requirements and usage

Program was written in Java 15 with JavaFX 15.0.1 and FXML. The easiest way to run this application is to use IntelliJ IDEA, download the source code and build with Gradle.

## Author

Copyright (C) 2021 Maksymilian Wojnar
