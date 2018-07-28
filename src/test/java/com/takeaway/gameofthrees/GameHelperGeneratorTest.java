package com.takeaway.gameofthrees;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class GameHelperGeneratorTest {

    private GameHelperGenerator gameHelperGenerator;

    @Before
    public void setUp() throws Exception {
        gameHelperGenerator=new GameHelperGenerator();
    }


    @Test
    public void shouldGenerateRandomNumberLessThanHundred(){
        assertThat(gameHelperGenerator.generateNumberDivisibleByThree())
                .isLessThan(100);
    }

    @Test
    public void shouldGenerateRandomNumberGreaterThanOrEqualThree(){
        assertThat(gameHelperGenerator.generateNumberDivisibleByThree())
                .isGreaterThanOrEqualTo(3);
    }

   @Test
   public void shouldHaveaNumberDivisibleByThree(){
        Integer numberToBeChecked=18;

       assertThat(gameHelperGenerator.checkThatNumberDivisibleByThree(numberToBeChecked))
               .isEqualTo(true);
   }

    @Test
    public void shouldNotHaveaNumberDivisibleByThree(){
        Integer numberToBeChecked=19;

        assertThat(gameHelperGenerator.checkThatNumberDivisibleByThree(numberToBeChecked))
                .isEqualTo(false);
    }

    @Test
    public void shouldCorrectTheNumberByAddingOneIfItsNotDivisibleByThree(){
        Integer numberToBeChecked=56;
        Integer expectedGeneratedNumber=57;

        assertThat(gameHelperGenerator.correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(numberToBeChecked))
                .isEqualTo(expectedGeneratedNumber);

    }

    @Test
    public void shouldCorrectTheNumberBySubtractingOneIfItsNotDivisibleByThree(){
        Integer numberToBeChecked=19;
        Integer expectedGeneratedNumber=18;


        assertThat(gameHelperGenerator.correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(numberToBeChecked))
                .isEqualTo(expectedGeneratedNumber);

    }

    @Test
    public void shouldCorrectTheNumberByReturningItIfItsNotDivisibleByThree(){
        Integer numberToBeChecked=18;
        Integer expectedGeneratedNumber=18;


        assertThat(gameHelperGenerator.correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(numberToBeChecked))
                .isEqualTo(expectedGeneratedNumber);

    }

    @Test
    public void shouldHaveAWinner(){
        Integer number=3;
        assertThat(gameHelperGenerator.checkWinner(number)).isTrue();
    }

    @Test
    public void shouldNotHaveAWinnerYet(){
        Integer number=6;
        assertThat(gameHelperGenerator.checkWinner(number)).isFalse();
    }


}
