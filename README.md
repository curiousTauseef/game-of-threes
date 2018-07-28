# Game Of Threes

Let's have fun with game development using websocket to implement multiplayer game.<br/>
The Main idea of the game is to show up the power of number three by suggesting a random number even <br/> by player or randomly generated , by checking this number we can find that it can be increased or<br/> decreased by one to be divisible by 3.
that would be the game , one player start with a number and the other operate <br/>on it to be divisible by three and pass it back to the first player ... etc.

### Description
When a player starts, it incepts a random (whole) number and sends it to the second<br/>
player as an approach of starting the game.<br/>
The receiving player can now always choose between adding one of {­1, 0, 1}
<br/>to get
to a number that is divisible by 3. Divide it by three. <br/>
The resulting whole number is then sent back to the original sender.<br/>
The same rules are applied until one player reaches the number 1(after the division).<br/>
For each "move", a sufficient output should get generated (mandatory: the added, and<br/>
the resulting number).
Both players should be able to play automatically without user input. One of the players<br/>
should optionally be adjustable by a user.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* Java
* Maven
* [ _optional_ ] IntelliJ if you prefere to run / build ... etc via IDE

### Covered Cases
* Two Players can play against each other without User input
* One Player can receive Input from the user
* One Player can start the game and wait for the other player to join
* Players can proceed many times after the initial game end with new sessions after disconnect and reconnect again

### How to play
* Get the server up by running mvn spring-boot:run
* in a browser navigate to : http://localhost:8080 to open a client (open another one for the other player)
* you need to decide how to play
* start the websocket connection in one or the two players
* click send directly to watch the game without user interaction otherwise enter a number to interact
* in case just one player started and input introduced, the player will wait for the other player to be associated
* the winner will be announced if 1 is produced.
* Note: you can initiate the game many time without restart the server by restarting only the websocket connection 

## Installing

##### Clone the repository

  ```
    git clone git@github.com:maha-hamza/game-of-threes.git
  ```
  
##### Getting the environment ready
   build the environment using maven:
  ```
   mvn clean install
  ```
#####   run the application
 ```
  mvn spring-boot:run
 ```
 
## Running the tests

 run the following command to run the tests:
  ```
  mvn test
  ```
## Built With

* [SpringBoot](https://spring.io/guides/gs/spring-boot/) - To accelerate and facilitate application development
* [Maven](https://maven.apache.org/) - Dependency Management
* [SpringBoot Websocket without Stomb](https://www.devglan.com/spring-boot/spring-websocket-integration-example-without-stomp) - to do websocket integration with customized protocol

## Author

* **Maha M. Hamza** - *Software Developer* - (https://github.com/maha-hamza)

See also the list of Other Projects in (https://github.com/maha-hamza?tab=repositories).

## License

Feel free to fork the project and suggest updates.

