import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        String input = in.nextLine();
        in.close();
        String newinput = input.replaceAll("[,.]", "");
        int forR=0;
        int forS=0;
        for (int i = 0; i < newinput.length()-1; i++) {
            if (Character.isDigit(newinput.charAt(i))) {
                if (i!=newinput.length()-1&&newinput.charAt(i+1)!='р'&&!Character.isDigit(newinput.charAt(i+1))){
                   forR=newinput.charAt(i);
                }
            }
        }
        for (int i = newinput.length()-1; i>=0; i--) {
            if (Character.isDigit(newinput.charAt(i))) {
                if (i!=0&&newinput.charAt(i - 1) != '$' && !Character.isDigit(newinput.charAt(i - 1))) {
                    forS=newinput.charAt(i);
                }
            }
        }
        if (forS==forR){
            System.out.println("ОШИБКА.Проверьте правильность введённых данных.");
            return;
        }

        String secin= newinput.replaceAll("[^р$()]", "");

        for (int i = 0; i < secin.length()-1; i++) {
            if (secin.charAt(i) == '$'&& secin.charAt(i+1)!='$'&&secin.charAt(i+1)!='('&&secin.charAt(i+1)!=')'){
                System.out.println("ОШИБКА.Проверьте правильность введённых данных.");

            }
        }
        for (int i = 0; i < secin.length()-1; i++) {
            if (secin.charAt(i) == 'р'&& secin.charAt(i+1)!='р'&&secin.charAt(i+1)!='('&&secin.charAt(i+1)!=')'){
                System.out.println("ОШИБКА.Проверьте правильность введённых данных.");

            }
        }


        double result = evaluateExpression(input);
        System.out.println("Ответ: " + result+" ");
    }

    private static double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replace(',', '.');
        expression = expression.replaceAll("[р$]", "");

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
