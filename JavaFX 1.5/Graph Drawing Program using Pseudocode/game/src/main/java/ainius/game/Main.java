package ainius.game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private Canvas canvas;
    private TextArea codeArea;
    private TextArea messageBox; // Declare a TextArea for displaying messages
    private double currentX = 0;
    private double currentY = 0;
    private Color color = Color.BLACK;
    private Map<String, Double> variables;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create canvas for drawing
        canvas = new Canvas(600, 600);

        // Create text area for code input
        codeArea = new TextArea();
        codeArea.setPrefRowCount(10);
        codeArea.setWrapText(true);

        // Create message box
        messageBox = new TextArea();
        messageBox.setEditable(false);
        messageBox.setWrapText(true);
        messageBox.setMaxWidth(150);
        messageBox.setMaxHeight(400);

        // Create button for executing the code
        Button executeButton = new Button("Execute Code");
        executeButton.setOnAction(e -> executeCode());

        // VBox to hold code area and execute button
        VBox controlsBox = new VBox(10);
        controlsBox.setPadding(new Insets(10));
        controlsBox.setPrefHeight(200);
        controlsBox.getChildren().addAll(executeButton, codeArea);

        BorderPane.setAlignment(messageBox, Pos.TOP_RIGHT);
        BorderPane.setMargin(messageBox, new Insets(10));

        root.setLeft(canvas);
        root.setBottom(controlsBox);
        root.setRight(messageBox);

        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.setTitle("PencilCode Simulator");
        primaryStage.show();

        drawGrid();
    }

    private void executeCode() {
        // Clear canvas
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawGrid();
        currentX = 0;
        currentY = 0;
        color = Color.BLACK;
        variables = new HashMap<>(); // Reset variables
        messageBox.clear();

        // Execute code from text area
        String code = codeArea.getText();
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            i = executeLine(lines, i);
        }
    }

    private int executeLine(String[] lines, int index) {
        String line = lines[index].trim();

        // Pattern to match variable declarations and assignments (varName = value)
        Pattern varPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\d+(\\.\\d+)?)");
        Matcher varMatcher = varPattern.matcher(line);
        if (varMatcher.matches()) {
            String varName = varMatcher.group(1);
            double value = Double.parseDouble(varMatcher.group(2));
            variables.put(varName, value);
            return index;
        }

        // Pattern to match addition operation (varName = x + y)
        Pattern addPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\w+)\\s*\\+\\s*(\\w+)");
        Matcher addMatcher = addPattern.matcher(line);
        if (addMatcher.matches()) {
            String varName = addMatcher.group(1);
            String operand1 = addMatcher.group(2);
            String operand2 = addMatcher.group(3);

            if (variables.containsKey(operand1) && variables.containsKey(operand2)) {
                double result = variables.get(operand1) + variables.get(operand2);
                variables.put(varName, result);
            } else {
                System.out.println("Error: One or more operands not found");
            }
            return index;
        }
        //TODO: FIX ?
        /*
        Pattern to match addition operation with a number (varName = varName + 5)
        Pattern addNumberPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\w+)\\s*\\+\\s*(\\d+(\\.\\d+)?)");
        Matcher addNumberMatcher = addNumberPattern.matcher(line);
        if (addNumberMatcher.matches()) {
            String varName = addNumberMatcher.group(1);
            String operand1 = addNumberMatcher.group(2);

            if (variables.containsKey(operand1)) {
                double operand2 = Double.parseDouble(addNumberMatcher.group(3));
                double result = variables.get(operand1) + operand2;
                variables.put(varName, result);
            } else {
                System.out.println("Error: Operand not found");
            }
            return index;
        }

         */

        // Pattern to match subtraction operation (varName = x - y)
        Pattern subPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\w+)\\s*-\\s*(\\w+)");
        Matcher subMatcher = subPattern.matcher(line);
        if (subMatcher.matches()) {
            String varName = subMatcher.group(1);
            String operand1 = subMatcher.group(2);
            String operand2 = subMatcher.group(3);

            if (variables.containsKey(operand1) && variables.containsKey(operand2)) {
                double result = variables.get(operand1) - variables.get(operand2);
                variables.put(varName, result);
            } else {
                System.out.println("Error: One or more operands not found");
            }
            return index;
        }
        //TODO: FIX ?
        /*
        Pattern to match subtraction operation with a number (varName = varName - 5)
        Pattern subNumberPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\w+)\\s*-\\s*(\\d+(\\.\\d+)?)");
        Matcher subNumberMatcher = subNumberPattern.matcher(line);
        if (subNumberMatcher.matches()) {
            String varName = subNumberMatcher.group(1);
            String operand1 = subNumberMatcher.group(2);

            if (variables.containsKey(operand1)) {
                double operand2 = Double.parseDouble(subNumberMatcher.group(3));
                double result = variables.get(operand1) - operand2;
                variables.put(varName, result);
            } else {
                System.out.println("Error: Operand not found");
            }
            return index;
        }

         */

        // Pattern to match if statements with comparison operations
        Pattern ifPattern = Pattern.compile("if\\(([^<>=]+)([<>]=?)([^<>=]+)\\)");
        Matcher ifMatcher = ifPattern.matcher(line);
        if (ifMatcher.matches()) {
            double operand1 = evaluateExpression(ifMatcher.group(1));
            double operand2 = evaluateExpression(ifMatcher.group(3));
            String operator = ifMatcher.group(2);
            boolean conditionMet = false;

            switch (operator) {
                case ">":
                    conditionMet = operand1 > operand2;
                    break;
                case "<":
                    conditionMet = operand1 < operand2;
                    break;
                case ">=":
                    conditionMet = operand1 >= operand2;
                    break;
                case "<=":
                    conditionMet = operand1 <= operand2;
                    break;
            }

            if (conditionMet) {
                return (index);
            }
            return index + 1;
        }

        // Pattern to match if statements with comparison operations
        // Pattern to match line(x, y)
        Pattern linePattern = Pattern.compile("line\\(([^,]+),([^,]+)\\)");
        Matcher lineMatcher = linePattern.matcher(line);
        if (lineMatcher.matches()) {
            double x = evaluateExpression(lineMatcher.group(1));
            double y = evaluateExpression(lineMatcher.group(2));
            drawLine(currentX, currentY, x, y);
            currentX = x;
            currentY = y;
            return index;
        }

        // Pattern to match while loop (while(x < y){)
        Pattern whilePattern = Pattern.compile("while\\(([^<>=]+)([<>]=?)([^<>=]+)\\)\\{");
        Matcher whileMatcher = whilePattern.matcher(line);
        if (whileMatcher.matches()) {
            double operand1 = evaluateExpression(whileMatcher.group(1));
            double operand2 = evaluateExpression(whileMatcher.group(3));
            String operator = whileMatcher.group(2);

            int loopEndIndex = findLoopEnd(lines, index);

            while (checkCondition(operand1, operator, operand2)) {
                for (int i = index + 1; i < loopEndIndex; i++) {
                    i = executeLine(lines, i);
                }
                operand1 = evaluateExpression(whileMatcher.group(1));
                operand2 = evaluateExpression(whileMatcher.group(3));
            }
            return loopEndIndex;
        }

        // Pattern to match for loop (for(5){)
        Pattern forPattern = Pattern.compile("for\\((\\d+)\\)\\{");
        Matcher forMatcher = forPattern.matcher(line);
        if (forMatcher.matches()) {
            int iterations = Integer.parseInt(forMatcher.group(1));
            int loopEndIndex = findLoopEnd(lines, index);

            for (int i = 0; i < iterations; i++) {
                for (int j = index + 1; j < loopEndIndex; j++) {
                    j = executeLine(lines, j);
                }
            }
            return loopEndIndex;
        }

        // Pattern to match color(color)
        Pattern colorPattern = Pattern.compile("color\\((RED|BLACK|GREEN)\\)");
        Matcher colorMatcher = colorPattern.matcher(line);
        if (colorMatcher.matches()) {
            String colorName = colorMatcher.group(1);
            switch (colorName) {
                case "RED":
                    color = Color.RED;
                    break;
                case "BLACK":
                    color = Color.BLACK;
                    break;
                case "GREEN":
                    color = Color.GREEN;
                    break;
            }
            return index;
        }

        // Pattern to match jump(x, y)
        Pattern jumpPattern = Pattern.compile("jump\\(([^,]+),([^,]+)\\)");
        Matcher jumpMatcher = jumpPattern.matcher(line);
        if (jumpMatcher.matches()) {
            double x = evaluateExpression(jumpMatcher.group(1));
            double y = evaluateExpression(jumpMatcher.group(2));
            currentX = x;
            currentY = y;
            return index;
        }

        // Pattern to match home()
        Pattern homePattern = Pattern.compile("home\\(\\)");
        Matcher homeMatcher = homePattern.matcher(line);
        if (homeMatcher.matches()) {
            currentX = 0;
            currentY = 0;
            return index;
        }

        // Pattern to match message command
        Pattern msgPattern = Pattern.compile("log\\(\"(.*)\"\\)");
        Matcher msgMatcher = msgPattern.matcher(line);
        if (msgMatcher.matches()) {
            String message = msgMatcher.group(1);
            messageBox.appendText(message + "\n");
            return index;
        }

        return index;
    }


    private double evaluateExpression(String expr) {
        expr = expr.trim();
        if (variables.containsKey(expr)) {
            return variables.get(expr);
        }
        return Double.parseDouble(expr);
    }

    // Method to find the end of the current while loop
    private int findLoopEnd(String[] lines, int startIndex) {
        int openBraces = 0;
        for (int i = startIndex + 1; i < lines.length; i++) {
            if (lines[i].contains("{")) {
                openBraces++;
            } else if (lines[i].contains("}")) {
                if (openBraces == 0) {
                    return i;
                } else {
                    openBraces--;
                }
            }
        }
        return -1; // Not found
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        canvas.getGraphicsContext2D().setStroke(color);
        canvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }

    private void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);

        // Draw vertical grid lines
        for (int i = 0; i <= 600; i += 50) {
            gc.strokeLine(i, 0, i, 600);
        }

        // Draw horizontal grid lines
        for (int i = 0; i <= 600; i += 50) {
            gc.strokeLine(0, i, 600, i);
        }

        // Draw x and y axes
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0, 0, 0, 600); // y axis
        gc.strokeLine(0, 0, 600, 0); // x axis

        // Draw labels
        for (int i = 0; i <= 600; i += 50) {
            // x-axis labels
            gc.strokeText(String.valueOf(i), i, 10);
            // y-axis labels
            gc.strokeText(String.valueOf(i), 5, i);
        }
    }

    private boolean checkCondition(double operand1, String operator, double operand2) {
        switch (operator) {
            case ">":
                return operand1 > operand2;
            case "<":
                return operand1 < operand2;
            case ">=":
                return operand1 >= operand2;
            case "<=":
                return operand1 <= operand2;
            default:
                return false;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}


/* PVZ.:
x = 100
y = 100
yo = 200
t1 = 300
t2 = 600
t3 = 400
a = 25
b = 50
while(x < t2){
    for(2){
        line(x,y)
        x = x + b
        y = y + a
    }
    if(x > t1)
        color(RED)
    if(x > t3)
        color(GREEN)
}
log("loop 1 is done")

home()
y = y - yo
for(5){
    x = x - b
    line(x,y)
    if(x <= t1)
        color(BLACK)
    if(x <= t1)
        y = y + a
    if(x <= t3)
        y = y - a
}
log("loop 2 is done")
color(RED)
jump(x,y)
x = 300
y = 100
for(3){
    x = x - b
line(x,y)
}
log("code is finished")
*/