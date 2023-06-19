import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        String input = in.nextLine();
        in.close();

        double result = evaluateExpression(input);
        System.out.println("Ответ: " + result+" апельсинов");
    }

    private static double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replace(',', '.');
        expression = expression.replace('р', ' ');
        expression = expression.replace('$', ' ');

        if (!expression.contains("(")) {
            return evaluateSimpleExpression(expression);
        }

        int openingIndex = expression.lastIndexOf("(");
        int closingIndex = findClosingParenthesis(expression, openingIndex);

        String prefix = expression.substring(0, openingIndex);
        String function = "";
        if (prefix.endsWith("toDollars")) {
            function = "toDollars";
        } else if (prefix.endsWith("toRubles")) {
            function = "toRubles";
        }

        String innerExpression = expression.substring(openingIndex + 1, closingIndex);
        double innerResult;
        if (function.equals("toDollars")) {
            innerResult = evaluateSimpleExpression(innerExpression) / 80;
        } else if (function.equals("toRubles")) {
            innerResult = evaluateSimpleExpression(innerExpression) * 80;
        } else {
            innerResult = evaluateSimpleExpression(innerExpression);
        }

        String modifiedExpression = prefix.substring(0, prefix.length() - function.length())
                + innerResult
                + expression.substring(closingIndex + 1);

        return evaluateExpression(modifiedExpression);
    }

    private static double evaluateSimpleExpression(String expression) {
        double result = 0;
        char operator = '+';

        String[] values = expression.split("[+-]");
        for (String value : values) {
            double num = Double.parseDouble(value);

            if (operator == '+') {
                result += num;
            } else if (operator == '-') {
                result -= num;
            }

            if (expression.indexOf(value) + value.length() < expression.length()) {
                operator = expression.charAt(expression.indexOf(value) + value.length());
            }
        }

        return result;
    }

    private static int findClosingParenthesis(String expression, int openingIndex) {
        int count = 1;
        for (int i = openingIndex + 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                count++;
            } else if (expression.charAt(i) == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}
