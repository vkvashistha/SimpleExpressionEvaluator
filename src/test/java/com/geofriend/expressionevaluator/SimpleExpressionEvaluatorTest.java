package com.geofriend.expressionevaluator;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

@Test
public class SimpleExpressionEvaluatorTest {
    public void testMathOperators() {
        Assert.assertEquals("5.0", SimpleExpressionEvaluator.evaluate("2+3"));
        Assert.assertEquals("1.0", SimpleExpressionEvaluator.evaluate("3-2"));
        Assert.assertEquals("6.0", SimpleExpressionEvaluator.evaluate("2*3"));
        Assert.assertEquals("2.0", SimpleExpressionEvaluator.evaluate("4/2"));
    }

    public void testPureInequalityComparisonForPositiveNumbers() {
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("2>3"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("3>2"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("2>2"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("2<3"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("3<2"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("3<3"));
    }

    public void testImpureInequalityComparisonForPositiveNumbers() {
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("2>=3"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("3>=2"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("2>=2"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("2<=3"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("3<=2"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("3<=3"));
    }

    // Test Case Failing
    public void testNegativeNumberComparison() {
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("-3>2"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(3)>(-2)"));
    }

    public void testCompoundedAndExpression() {
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("(2>3)&&(3>2)"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("(3>2)&&(2>3)"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(2<3)&&(3>2)"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("((2<3)&&(3>2))"));
    }

    public void testCompoundedORExpression() {
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(2>3)||(3>2)"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(3>2)||(2>3)"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("(2>3)||(3<2)"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(2<3)||(3>2)"));
    }

    public void testCompoundedAndORExpression() {
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("((2>3)&&(3>2))||((2>3)||(3>2))"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("((3>2)&&(2>3))||((3>2)||(2>3))"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("((2<3)&&(3>2))||((2>3)||(3<2))"));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("((2<3)&&(3>2))||((2<3)||(3>2))"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("((3>2)&&(2>3))||((2>3)||(3<2))"));
    }

    public void testCompoundedLogicalExpressionAndLiterals() {
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("((2>3)&&(3>2))||true"));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("(3>2)&&false"));
    }

    // Test Case Failing
    public void testCompoundedLogicalExpressionAndMathematicalExpression() {
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(1+2)>2"));
    }

    public void testLogicalExpressionWithSymbolTable() {
        HashMap<String, Object> st = new HashMap<>();
        st.put("a","2");
        st.put("b","3");
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(a>b)||(b>a)",st));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(b>a)||(a>b)",st));
        Assert.assertEquals("false", SimpleExpressionEvaluator.evaluate("(a>b)||(b<a)",st));
        Assert.assertEquals("true", SimpleExpressionEvaluator.evaluate("(a<b)||(b>a)",st));
    }
}
