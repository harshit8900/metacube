import java.util.*;

interface Stack<T> {
    void push(T item);
    T pop();
    T peek();
    boolean isEmpty();
}

class ArrayStack<T> implements Stack<T> {
    private List<T> stack = new ArrayList<>();
    
    public void push(T item) { stack.add(item); }
    public T pop() { return isEmpty() ? null : stack.remove(stack.size() - 1); }
    public T peek() { return isEmpty() ? null : stack.get(stack.size() - 1); }
    public boolean isEmpty() { return stack.isEmpty(); }
}

class ExpressionEvaluator {
    public static int evaluate(String expr) {
        String[] tokens = expr.split(" ");
        Stack<Integer> values = new ArrayStack<>();
        Stack<String> operators = new ArrayStack<>();
        
        Map<String, Integer> precedence = Map.of(
            "!", 3, "*", 2, "/", 2, "+", 1, "-", 1,
            "==", 0, "!=", 0, "<", 0, ">", 0, "<=", 0, ">=", 0,
            "&&", -1, "||", -2
        );
        
        for (String token : tokens) {
            if (token.matches("\\d+")) {
                values.push(Integer.parseInt(token));
            } else if (precedence.containsKey(token)) {
                while (!operators.isEmpty() && precedence.get(token) <= precedence.get(operators.peek())) {
                    processOperator(values, operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.peek().equals("(")) {
                    processOperator(values, operators.pop());
                }
                operators.pop();
            }
        }
        
        while (!operators.isEmpty()) processOperator(values, operators.pop());
        return values.pop();
    }
    
    private static void processOperator(Stack<Integer> values, String op) {
        int b = values.pop();
        int a = values.pop();
        int result = switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> 0;
        };
        values.push(result);
    }
}

interface Queue<T> {
    void enqueue(T item);
    T dequeue();
    boolean isEmpty();
    boolean isFull();
}

class CircularQueue<T> implements Queue<T> {
    private T[] queue;
    private int front, rear, size, capacity;
    
    public CircularQueue(int capacity) {
        this.capacity = capacity;
        queue = (T[]) new Object[capacity];
        front = rear = size = 0;
    }
    
    public void enqueue(T item) {
        if (isFull()) return;
        queue[rear] = item;
        rear = (rear + 1) % capacity;
        size++;
    }
    
    public T dequeue() {
        if (isEmpty()) return null;
        T item = queue[front];
        front = (front + 1) % capacity;
        size--;
        return item;
    }
    
    public boolean isEmpty() { return size == 0; }
    public boolean isFull() { return size == capacity; }
}

class CounselingProcess {
    static class Program {
        String name;
        int capacity;
        Program(String name, int capacity) { this.name = name; this.capacity = capacity; }
    }
    
    static class Student {
        String name;
        List<String> preferences;
        String allocated = "Not Allocated";
        Student(String name, List<String> preferences) {
            this.name = name;
            this.preferences = preferences;
        }
    }
    
    public static void allocatePrograms(List<Program> programs, List<Student> students) {
        Map<String, Integer> programSeats = new HashMap<>();
        for (Program p : programs) programSeats.put(p.name, p.capacity);
        
        Queue<Student> queue = new CircularQueue<>(students.size());
        for (Student s : students) queue.enqueue(s);
        
        while (!queue.isEmpty()) {
            Student student = queue.dequeue();
            for (String choice : student.preferences) {
                if (programSeats.get(choice) > 0) {
                    student.allocated = choice;
                    programSeats.put(choice, programSeats.get(choice) - 1);
                    break;
                }
            }
        }
        
        for (Student s : students) {
            System.out.println(s.name + " -> " + s.allocated);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Evaluating Expression: " + ExpressionEvaluator.evaluate("3 + 5 * ( 2 - 8 )"));
        
        List<Program> programs = List.of(new Program("CSE", 2), new Program("ECE", 1));
        List<Student> students = List.of(
            new Student("Alice", List.of("CSE", "ECE")),
            new Student("Bob", List.of("ECE", "CSE")),
            new Student("Charlie", List.of("CSE", "ECE"))
        );
        allocatePrograms(programs, students);
    }
}
