package com.devglan.config;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Contain Helper Methods
 */
public class GameHelperGenerator {


    public Integer generateNumberDivisibleByThree() {
        Random random=new Random();
        return random.ints(3,(97)).findFirst().getAsInt();
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

    public Boolean checkWinner(Integer number){
        return number /3 == 1;

    }



}
