import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CustomerImportService service =
            new CustomerImportService();

        System.out.println("=== ИМПОРТ КЛИЕНТОВ ИЗ CSV ===");

        System.out.print(
            "Введите путь к CSV (Enter — пример): "
        );

        String input = scanner.nextLine().trim();

        // выбираем файл для импорта
        Path path = input.isEmpty()
            ? Path.of("practice-11", "data", "sample.csv")
            : Path.of(input);

        try {
            // запускаем импорт клиентов
            ImportResult result = service.importFile(path);

            // выводим результаты импорта
            result.printReport();

        } catch (IOException e) {
            System.out.println(
                "Ошибка чтения файла: " + e.getMessage()
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                "Ошибка: " + e.getMessage()
            );
        }

        scanner.close();
    }
}
