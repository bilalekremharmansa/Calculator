package com.example.bilalekremharmansa.calculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CalculatorUnitTest {

    private Calculator c;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() {
        c = new Calculator();
    }

    @Test
    public void additionWithPositiveIntegerNumbers() {
        c.setOperationLine("3+5");
        assertEquals("8", c.evaluate());
    }

    @Test
    public void additionWithNegativeIntegerNumbers() {
        c.setOperationLine("3+_5");
        assertEquals("-2", c.evaluate());
    }

    @Ignore
    public void additionWithPositiveDoubleNumbers() {
        c.setOperationLine("3,10+5,0.2");
        assertEquals("8.12", c.evaluate());
    }


    //@Test(expected = ArithmeticException.class)
    @Ignore
    public void divideByZero() {
        c.setOperationLine("5/0");
        //assertEquals("", c.evaluate());
    }

    @Ignore
    public void infixToPostfixWithoutParanthesis() {
        c.setOperationLine(new StringBuilder("_5*_6+_7*8"));
        //assertEquals("_5 _6 * _7 8 * +", c.infixToPostFix().toString());
    }
/*
    @Test
    public void infixToPostfixWithParanthesis() {
        c.setOperationLine(new StringBuilder("(10+20)*(30+40)/(50-60)"));
        assertEquals("10 20 + 30 40 + * 50 60 - /", c.infixToPostFix().toString());
    }

    @Test
    public void infixToPostfixWithParanthesisV2() {
        c.setOperationLine(new StringBuilder("(3+4)*((5*6)+(9-2))"));
        assertEquals("3 4 + 5 6 * 9 2 - + *", c.infixToPostFix().toString());
    }

    @Test
    public void infixToPostfixNegativeNumbers() {
        c.setOperationLine(new StringBuilder("(-3+4)*((-5*6)+(9-2))"));
        assertEquals("3 4 + 5 6 * 9 2 - + *", c.infixToPostFix().toString());
    }
    */

    @Test
    public void postfixPositiveOutput() {
        c.setOperationLine(new StringBuilder("5*6+7*8"));
        assertEquals("86", c.evaluate());
    }

    @Test
    public void postfixNegativeOutput() {
        c.setOperationLine(new StringBuilder("5*_6+7*8"));
        assertEquals("26", c.evaluate());
    }

    @Test
    public void postfixOutputPositiveWithParanthesis() {
        c.setOperationLine(new StringBuilder("(10+20)*(30+40)/(50-60)"));
        assertEquals("-210", c.evaluate());
    }
    @Test
    public void postfixOutputNegativeWithParanthesis() {
        c.setOperationLine(new StringBuilder("(10+_20)*(_30+40)/(50-60)"));
        assertEquals("10", c.evaluate());
    }

    @Test
    public void postfixOutputWithParanthesisV2() {
        c.setOperationLine(new StringBuilder("(3+4)*((5*6)+(9-2))"));
        assertEquals("259", c.evaluate());
    }

    @Test
    public void infixToPostfixMultiParanthesis(){
        c.setOperationLine(new StringBuilder("5-8-(2-(((3*2))))"));
        assertEquals("1", c.evaluate());
    }


}