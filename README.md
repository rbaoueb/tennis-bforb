# Tennis Kata

[![codecov](https://codecov.io/gh/rbaoueb/tennis-bforb/branch/main/graph/badge.svg)](https://codecov.io/gh/rbaoueb/tennis-bforb)
[![CodeFactor](https://www.codefactor.io/repository/github/rbaoueb/tennis-bforb/badge)](https://www.codefactor.io/repository/github/rbaoueb/tennis-bforb)


Kata implementation for simple tennis score computer

## Topic
The scoring system consist in one game, divided by points :

Each player starts a game with 0 point.

If the player wins the 1st ball, he will have 15 points. 2nd balls won : 30 points. 3rd ball won : 40points.

If a player have 40 points and wins the ball, he wins the game, however there are special rules.

If both players have 40 points the players are “deuce”.

If the game is in deuce, the winner of the ball will have advantage

If the player with advantage wins the ball he wins the game

If the player without advantage wins the ball they are back at “deuce”.

## Report
The javadoc report of the project can be reachable through this [link](https://rbaoueb.github.io/tennis-bforb/)


## Installation
you can import the source project on your IDE or build the jar and push it to your maven repository :

```bash
git clone https://github.com/rbaoueb/tennis-bforb.git
cd tennis-bforb
mvn clean install
```

then run the generated dependency by the command line:
```bash
java -jar tennis-bforb-1.0.0-SNAPSHOT.jar
```