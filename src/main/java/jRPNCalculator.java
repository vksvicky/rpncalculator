import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by vivek on 27/06/2014.
 */
public class jRPNCalculator {
    // List of supported operators
    public static final String[] OPERATORS = {"+", "-", "*", "/"};

    // Test if a token is operator
    public static Boolean isOperator(String token) {
        for (String op : OPERATORS) {
            if (op.equals(token)) {
                return true;
            }
        }
        return false;
    }

    // Operation based on operator
    public static Double operation(String op, Double e1, Double e2) {
        if (op.equals("+")) {
            return e1 + e2;
        } else if (op.equals("-")) {
            return e2 - e1;
        } else if (op.equals("*")) {
            return e1 * e2;
        } else if (op.equals("/")) {
            return e2 / e1;
        } else {
            throw new IllegalArgumentException("Invalid operator.");
        }
    }

    // Evaluate RPN expr (given as array of tokens)
    public static Double eval(String[] tokens) {
        LinkedList<Double> stack = new LinkedList<Double>();
        for (String token : tokens) {
            if (isOperator(token)) {
                stack.push(operation(token, stack.pop(), stack.pop()));
            } else {
                stack.push(Double.parseDouble(token));
            }
        }
        return stack.pop();
    }

    // Main	function
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            System.out.println("Expression: " + eval(line.split(" ")));
        }
    }
}
