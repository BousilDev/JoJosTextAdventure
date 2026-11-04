# JoJo's Text Adventure

A Scala 3 text adventure inspired by *JoJo's Bizarre Adventure*. Play as Jotaro
Kujo and lead the Joestar group from Tokyo to Cairo within 50 turns, fighting
DIO's assassins and unlocking new routes and vehicles along the way.

## Features

- Explore a connected journey through Japan, South Asia, and Egypt.
- Fight enemies with Stand abilities, energy management, and unlockable moves.
- Use items and vehicles to reveal new routes.
- Play in the terminal or use the optional Scala Swing interface.

## Requirements

- Java 21 or newer
- [Scala CLI](https://scala-cli.virtuslab.org/)

The repository includes `scala-swing_3-3.0.0.jar` for the graphical interface.

## Running the game

### Text interface

From the repository root, run:

```shell
scala-cli run ./o1 --main-class o1.adventure.ui.AdventureTextUI
```

### Graphical interface

Run the GUI with the bundled Scala Swing library:

```shell
scala-cli run ./o1 \
  --main-class o1.adventure.ui.AdventureGUI \
  --jar scala-swing_3-3.0.0.jar
```

## Commands

Commands are entered in lowercase, with a command followed by an optional
target:

| Command | Example | Purpose |
| --- | --- | --- |
| `go` | `go singapore` | Move to an available exit |
| `battle` | `battle kakyoin noriaki` | Start a battle with an enemy |
| `use` | `use ora` | Use a Stand fight move during battle |
| `drive` | `drive airplane` | Use a vehicle to unlock routes |
| `get` / `drop` | `get map` | Pick up or drop an item |
| `examine` | `examine map` | Inspect an item |
| `inventory` | `inventory` | Show carried items |
| `rest` | `rest` | Spend a turn resting |
| `skip` | `skip` | Skip a battle turn |
| `help` | `help` | Show in-game help |
| `quit` | `quit` | End the game |

During battles, use the displayed fight-move names, such as `ora` or
`star finger`, as targets for `use`.

## Walkthrough

See [`walkthrough.txt`](walkthrough.txt) for the intended route, battle
progression, and gameplay tips. The world maps are also available as
[`world_map.gif`](world_map.gif) and [`forest_map.gif`](forest_map.gif).

## Packaging

Create a standalone executable JAR for the text interface:

```shell
scala-cli --power package ./o1 \
  --main-class o1.adventure.ui.AdventureTextUI \
  --assembly \
  -o JoJosTextAdventure.jar
```

Run the packaged game with:

```shell
java -jar JoJosTextAdventure.jar
```

## Project structure

```text
o1/adventure/       Game model, world, actions, items, and battles
o1/adventure/ui/    Text and Swing user interfaces
walkthrough.txt     Route and gameplay guide
doc/                Generated Scala API documentation
```

The project is also built automatically by GitHub Actions on pushes to
`main`.
