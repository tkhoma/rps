# Rock Paper Scissors

## Usage
After you downloaded the code you can build it with Gradle
```
$ ./gradlew clean build
```
and execute it with
```
$./gradlew bootRun
```
the application starts a webserver that listens on port 8080

In order to create game call script
```
  ./createGame.sh
```

It returns location where your new game could be found.
When you call it for the first time it will return:
```
Location: http://localhost:8080/games/1
```

You can call 
```
./getGame.sh 1
```
It will show data about the game.

You can call 
```
./playGame.sh 1 rock
```
1 is the id of the game, and rock is the move

* Rock beats Scissors
* Scissors beats Paper
* Paper beats Rock

You can delete the game by calling 
```
./deleteGame.sh 1
```

You can get all server statistics by calling 
```
./statistics.sh
```

