import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TestUtils {
    public static boolean logsContains(ByteArrayOutputStream outputStream, String expected) {
        System.setOut(System.out);
        return outputStream.toString().trim().contains(expected);
    }
    public static void setOutputStream(ByteArrayOutputStream outputStream) {
        System.setOut(new PrintStream(outputStream));
    }

    public static void preventConsoleOutput(){
        //Создание отдельного потока
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    public static Scanner createScanner(String input){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        return new Scanner(inputStream);
    }
}

