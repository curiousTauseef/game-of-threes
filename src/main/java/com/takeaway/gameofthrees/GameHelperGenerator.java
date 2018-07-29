package com.takeaway.gameofthrees;

import java.util.Random;

import static com.takeaway.gameofthrees.datafeeder.StaticDataProvider.*;

/**
 * Contain Helper Methods that check the main operations of the game
 *
 * @author Maha M. Hamza
 */
public class GameHelperGenerator {

    /**
     * Generate Random Number to be used in case of no user input introduced
     *
     * @return Integer Represents Random number within range specified
     */
    public Integer generateNumberDivisibleByThree() {
        Random random = new Random();
        return random.ints(MIN, (MAX + 1)).findFirst().getAsInt();
    }

    /**
     * Checking the given number is divisible by three or not
     *
     * @param number to be checked if it's divisible by three or not
     * @return true in case the number is divisible by three otherwise false
     */
    public Boolean checkThatNumberDivisibleByThree(Integer number) {
        return (number % 3) == 0;
    }

    /**
     * Making the given number divisible by three
     *
     * @param number to be corrected
     * @return the correct number which is divisible by three
     */
    public Integer correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(Integer number) {
        if (!checkThatNumberDivisibleByThree(number)) {
            number = ((number + 1) % 3) == 0 ? number + 1 : number - 1;
        }
        return number;
    }


}
