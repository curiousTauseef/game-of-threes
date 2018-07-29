package com.takeaway.gameofthrees;

import java.util.Random;
import static com.takeaway.gameofthrees.StaticDataProvider.*;
/**
 * Contain Helper Methods
 */
public class GameHelperGenerator {


    public Integer generateNumberDivisibleByThree() {
        Random random=new Random();
        return random.ints(MIN,(MAX+1)).findFirst().getAsInt();
    }


    public Boolean checkThatNumberDivisibleByThree(Integer number) {
        return (number % 3) == 0;
    }

    public Integer correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(Integer number) {
        if (!checkThatNumberDivisibleByThree(number)) {
            number = ((number + 1) % 3) == 0 ? number + 1 : number - 1;
        }
        return number;
    }


}
