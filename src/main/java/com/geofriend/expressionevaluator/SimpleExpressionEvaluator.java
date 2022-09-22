package com.geofriend.expressionevaluator;

import java.util.*;

public class SimpleExpressionEvaluator {

    public static String evaluate(String expression) {
        return evaluate(expression, new HashMap<>());
    }

    public static String evaluate(String expression, Map<String, Object> data) {
        List<String> tokens = addParenthesis(tokenize(expression));
        return evaluate(tokens, data);
    }

    private static List<String> tokenize(String expression) {

        List<String> reservedTokens =
                Arrays.asList("(", ")", "==", "&&", "||", "<", ">", "!", "<=", ">=", "!=", "+", "/", "*", "-");
        List<String> operatorSymbols = Arrays.asList("(", ")", "=", "&", "|", "<", ">", "!", "+", "/", "*", "-");
        ArrayList<String> tokens = new ArrayList<String>();
        StringBuilder operand = new StringBuilder();
        for (char ch : expression.toCharArray()) {
            if (ch == ' ') continue;
            String symbol = operand.toString() + ch;
            if (reservedTokens.contains(symbol)) {
                tokens.add(symbol);
                operand = new StringBuilder();
            } else if (operatorSymbols.contains(""+ch) && !operand.isEmpty()) {
                tokens.add(operand.toString());
                operand = new StringBuilder();
                operand.append(ch);
            } else if (reservedTokens.contains(operand.toString()) && !operatorSymbols.contains(""+ch)) {
                tokens.add(operand.toString());
                operand = new StringBuilder();
                operand.append(ch);
            } else {
                operand.append(ch);
            }
        }

        if (!operand.isEmpty()) {
            tokens.add(operand.toString());
        }
        return tokens;
    }

    private static List<String> addParenthesis(List<String> tokens) {
        ArrayList<String> finalTokens = new ArrayList<String>();
        List<String> reservedTokens =
                Arrays.asList("(", ")", "==", "&&", "||", "<", ">", "!", "<=", ">=", "!=", "+", "-", "/", "*");
        String leftOperand = null;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (i == 0 && !token.equals("(")) {
                finalTokens.add("(");
            }
            if (!reservedTokens.contains(token)) {
                if (leftOperand == null) {
                    leftOperand = token;
                    if (i != 0) {
                        finalTokens.add("(");
                    }
                    finalTokens.add(leftOperand);
                } else {
                    finalTokens.add(token);
                    finalTokens.add(")");
                    leftOperand = null;
                }
            } else {
                finalTokens.add(token);
            }
        }
        if (!finalTokens.isEmpty() && !Objects.equals(finalTokens.get(finalTokens.size() - 1), ")")) {
            finalTokens.add(")");
        }
        return finalTokens;
    }

    private static String getValueOfDefault(
            Map<String, Object> data,
            String key
    ) {
        if (key.startsWith("$")) {
            if ("$currentTimeStamp".equals(key)) {
                return String.valueOf(System.currentTimeMillis());
            } else {
                return getValueOfDefault(data, key.replace("$", ""));
            }
        }
        if (data.containsKey(key)) {
            return data.get(key).toString();
        } else {
            return key;
        }
    }

    private static String evaluate(List<String> tokens, Map<String, Object> data) {
        Stack<String> stack = new Stack<String>();
        for (String token : tokens) {
            if (")".equals(token)) {
                String rightOperand = stack.pop();
                String op = "";
                if (!stack.isEmpty()) {
                    op = stack.pop();
                }
                switch (op) {
                    case "(": {
                        stack.push(rightOperand);
                        break;
                    }
                    case "!": {
                        if (!stack.isEmpty()) {
                            stack.pop();
                        }
                        String result = evaluate(data, "", op, rightOperand);
                        stack.push(result);
                        break;
                    }
                    default: {
                        String leftOperand = "";
                        if (!stack.isEmpty()) {
                            leftOperand = stack.pop();
                        }
                        if (!stack.isEmpty() && stack.peek().equals("(")) {
                            stack.pop();
                        }
                        String result = evaluate(data, leftOperand, op, rightOperand);
                        stack.push(result);
                    }
                }
            } else {
                stack.push(token);
            }
        }
        if(stack.size() == 1) {
            return stack.peek();
        } else {
            return String.valueOf(evaluateLiterals(stack));
        }
    }

    private static boolean evaluateLiterals(Stack<String> stack) {
        boolean result = true;
        List<String> operators = Arrays.asList("&&", "||", "!");
        String operator = "&&";
        while (!stack.isEmpty()) {
            String literal = stack.pop();
            if (!stack.isEmpty() && stack.peek().equals("!")) {
                stack.pop();
                stack.push(String.valueOf(!Boolean.parseBoolean(literal)));
                continue;
            }
            if (operators.contains(literal)) {
                operator = literal;
            } else if (!literal.equals("(")) {
                if (Objects.equals(operator, "&&")) {
                    result = result && Boolean.parseBoolean(literal);
                } else if (Objects.equals(operator, "||")) {
                    result = result || Boolean.parseBoolean(literal);
                }
            }
        }
        return result;
    }

    private static String evaluate(Map<String, Object> data, String leftOperand, String operator, String rightOperand) {
        if (leftOperand.isEmpty() && operator.equals("!")) {
            return String.valueOf(!Boolean.parseBoolean(rightOperand));
        }

        String leftValue = getValueOfDefault(data, leftOperand);
        String rightValue = getValueOfDefault(data, rightOperand);
        if (Objects.equals(leftValue, "nil") || Objects.equals(rightValue, "nil")) {
            return "false";
        }

        String regex = "^((?!-0?(\\.0+)?(e|$))-?(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)(e-?(0|[1-9]\\d*))?|0x[0-9a-f]+)$";
        boolean areBothOperandNumeric = leftValue.matches(regex) && rightValue.matches(regex);
        switch (operator) {
            case "<": {

                return String.valueOf(areBothOperandNumeric && (Double.parseDouble(leftValue) < Double.parseDouble(rightValue)));
            }


            case "<=":
                return String.valueOf(areBothOperandNumeric && (Double.parseDouble(leftValue) <= Double.parseDouble(rightValue)));


            case "==":
                return String.valueOf(leftValue.equals(rightValue));


            case "!=":
                return String.valueOf(!leftValue.equals(
                        rightValue));


            case ">":
                return String.valueOf(areBothOperandNumeric && (Double.parseDouble(leftValue) > Double.parseDouble(rightValue)));


            case ">=":
                return String.valueOf(areBothOperandNumeric && (Double.parseDouble(leftValue) >= Double.parseDouble(rightValue)));


            case "-":
                return String.valueOf(Double.parseDouble(leftValue) - Double.parseDouble(rightValue));


            case "+": {
                if (areBothOperandNumeric) {
                    return String.valueOf(Double.parseDouble(leftValue) + Double.parseDouble(rightValue));
                } else {
                    return "0";
                }
            }


            case "*":
                if (areBothOperandNumeric) {
                    return String.valueOf(Double.parseDouble(leftValue) * Double.parseDouble(rightValue));
                } else return "0";


            case "/":
                if (areBothOperandNumeric) {
                    return String.valueOf(Double.parseDouble(leftValue) / Double.parseDouble(rightValue));
                } else return "0";

            case "&&":
                return String.valueOf(Boolean.parseBoolean(leftValue) && Boolean.parseBoolean(rightValue));

            case "||":
                return String.valueOf(Boolean.parseBoolean(leftValue) || Boolean.parseBoolean(rightValue));

            default:
                return "";
        }


    }
}
