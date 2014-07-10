import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by vivek on 27/06/2014.
 */
public class j7RPNCalculator {
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Op {
        String value();
    }

    private static class PrimOps {
        @Op("+")
        public static double plus(double lhs, double rhs) {
            return check(lhs + rhs);
        }

        @Op("-")
        public static double minus(double lhs, double rhs) {
            return check(lhs - rhs);
        }

        @Op("*")
        public static double multiplies(double lhs, double rhs) {
            return check(lhs * rhs);
        }

        @Op("/")
        public static double divides(double lhs, double rhs) {
            return check(lhs / rhs);
        }

        @Op("%")
        public static double modulus(double lhs, double rhs) {
            return check(lhs % rhs);
        }

        private static double check(double result) {
            if (Double.isNaN(result) || Double.isInfinite(result))
                throw new ArithmeticException();
            return result;
        }
    }

    private static final Map<String, Method> MATH_METHODS;

    static {
        Map<String, Method> methods = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Method method : PrimOps.class.getMethods()) {
            Op op = method.getAnnotation(Op.class);
            if (op != null)
                methods.put(op.value(), method);
        }
        for (Method method : Math.class.getMethods()) {
            if (method.getReturnType() != double.class)
                continue;
            for (Class<?> type : method.getParameterTypes())
                if (type != double.class)
                    continue;
            String name = method.getName();
            methods.put(name, methods.containsKey(name) ? null : method);
        }
        MATH_METHODS = Collections.unmodifiableMap(methods);
    }

    private final Deque<Double> stack = new ArrayDeque<>();

    public static void main(String[] args) throws IOException {
        j7RPNCalculator self = new j7RPNCalculator();
        try (Scanner stdin = new Scanner(System.in)) {
            while (stdin.hasNextLine())
                self.process(stdin.nextLine());
        }
    }

    public void process(String line) {
        try (Scanner scanner = new Scanner(line)) {
            while (true) {
                if (scanner.hasNextDouble())
                    stack.push(scanner.nextDouble());
                else if (scanner.hasNext())
                    stack.push(dispatch(scanner.next()));
                else
                    break;
            }
            if (!stack.isEmpty())
                System.out.println(stack.peek());
        } catch (IllegalArgumentException | ArithmeticException | EmptyStackException e) {
            System.err.println(e);
        }
    }

    private double dispatch(String token) {
        Method method = getMethodFor(token);
        int arity = method.getParameterTypes().length;
        if (stack.size() < arity)
            throw new EmptyStackException();
        Deque<Double> args = new ArrayDeque<>();
        for (int i = 0; i < arity; ++i)
            args.push(stack.pop());
        boolean succeeded = false;
        try {
            double result = (Double) method.invoke(null, args.toArray());
            succeeded = true;
            return result;
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException)
                throw (RuntimeException) cause;
            throw new AssertionError(cause);
        } finally {
            if (!succeeded)
                for (Double arg : args)
                    stack.push(arg);
        }
    }

    private static Method getMethodFor(String token) {
        Method method = MATH_METHODS.get(token);
        if (method == null)
            throw new IllegalArgumentException(token);
        return method;
    }
}
