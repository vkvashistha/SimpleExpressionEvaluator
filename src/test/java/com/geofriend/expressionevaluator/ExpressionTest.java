/**
 * MIT License
 *
 * Copyright (c) 2022 vkvashistha (vkvashistha@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.geofriend.expressionevaluator;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.geofriend.expressionevaluator.Expression.eval;

@Test
public class ExpressionTest {
    public void testMathOperators() {
        Assert.assertEquals("5.0", eval("2+3").asString());
        Assert.assertEquals("1.0", eval("3-2").asString());
        Assert.assertEquals("6.0", eval("2*3").asString());
        Assert.assertEquals("2.0", eval("4/2").asString());
    }

    public void testPureInequalityComparisonForPositiveNumbers() {
        Assert.assertEquals("false", eval("2>3").asString());
        Assert.assertEquals("true", eval("3>2").asString());
        Assert.assertEquals("false", eval("2>2").asString());
        Assert.assertEquals("true", eval("2<3").asString());
        Assert.assertEquals("false", eval("3<2").asString());
        Assert.assertEquals("false", eval("3<3").asString());
    }

    public void testImpureInequalityComparisonForPositiveNumbers() {
        Assert.assertEquals("false", eval("2>=3").asString());
        Assert.assertEquals("true", eval("3>=2").asString());
        Assert.assertEquals("true", eval("2>=2").asString());
        Assert.assertEquals("true", eval("2<=3").asString());
        Assert.assertEquals("false", eval("3<=2").asString());
        Assert.assertEquals("true", eval("3<=3").asString());
    }

    // Test Case Failing
    public void testNegativeNumberComparison() {
        Assert.assertEquals("false", eval("-3>2").asString());
        Assert.assertEquals("true", eval("(3)>(-2)").asString());
    }

    public void testCompoundedAndExpression() {
        Assert.assertEquals("false", eval("(2>3)&&(3>2)").asString());
        Assert.assertEquals("false", eval("(3>2)&&(2>3)").asString());
        Assert.assertEquals("true", eval("(2<3)&&(3>2)").asString());
        Assert.assertEquals("true", eval("((2<3)&&(3>2))").asString());
    }

    public void testCompoundedORExpression() {
        Assert.assertEquals("true", eval("(2>3)||(3>2)").asString());
        Assert.assertEquals("true", eval("(3>2)||(2>3)").asString());
        Assert.assertEquals("false", eval("(2>3)||(3<2)").asString());
        Assert.assertEquals("true", eval("(2<3)||(3>2)").asString());
    }

    public void testCompoundedAndORExpression() {
        Assert.assertEquals("true", eval("((2>3)&&(3>2))||((2>3)||(3>2))").asString());
        Assert.assertEquals("true", eval("((3>2)&&(2>3))||((3>2)||(2>3))").asString());
        Assert.assertEquals("true", eval("((2<3)&&(3>2))||((2>3)||(3<2))").asString());
        Assert.assertEquals("true", eval("((2<3)&&(3>2))||((2<3)||(3>2))").asString());
        Assert.assertEquals("false", eval("((3>2)&&(2>3))||((2>3)||(3<2))").asString());
    }

    public void testCompoundedLogicalExpressionAndLiterals() {
        Assert.assertEquals("true", eval("((2>3)&&(3>2))||true").asString());
        Assert.assertEquals("false", eval("(3>2)&&false").asString());
    }

    // Test Case Failing
    public void testCompoundedLogicalExpressionAndMathematicalExpression() {

        Assert.assertEquals("false", eval("(2+(1+1))>5").asString());
        Assert.assertEquals("true", eval("(2+(1+1))>3").asString());
    }

    public void testLogicalExpressionWithSymbolTable() {
        HashMap<String, Object> st = new HashMap<>();
        st.put("a","2");
        st.put("b","3");
//        Assert.assertEquals("false", eval("(a>b)",st).asString());
        Assert.assertEquals("true", eval("(b>a)",st).asString());
        Assert.assertEquals("true", eval("(a>b)||(b>a)",st).asString());
        Assert.assertEquals("true", eval("(b>a)||(a>b)",st).asString());
        Assert.assertEquals("false", eval("(a>b)||(b<a)",st).asString());
        Assert.assertEquals("true", eval("(a<b)||(b>a)",st).asString());
    }
}
