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

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

 run the following command to run the tests:
  ```
  mvn test
  ```

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
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

