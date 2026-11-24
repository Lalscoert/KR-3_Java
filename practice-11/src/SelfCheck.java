import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SelfCheck {

    private static int passed = 0;
    private static int failed = 0;

    private static final CustomerCsvParser parser =
        new CustomerCsvParser();

    private static final CustomerValidator validator =
        new CustomerValidator();

    private static final CustomerImportService service =
        new CustomerImportService();

    public static void main(String[] args) throws IOException {

        // проверяем правильный разбор строки
        Customer customer = parser.parse(
            " Ivan ; 1234567890 ; ivan@mail.ru "
        );

        check("Разбор имени",
            customer.getName().equals("Ivan"));

        check("Разбор ИНН",
            customer.getInn().equals("1234567890"));

        check("Разбор email",
            customer.getEmail().equals("ivan@mail.ru"));

        // проверяем допустимые значения
        validator.validate(customer);
        check("Корректный ИНН из 10 цифр", true);

        Customer longInn = parser.parse(
            "Olga;123456789012;olga@mail.ru"
        );

        validator.validate(longInn);
        check("Корректный ИНН из 12 цифр", true);

        // проверяем неправильные значения
        expectError("Слишком короткий ИНН",
            () -> validateLine("Ivan;123;ivan@mail.ru"));

        expectError("ИНН с буквами",
            () -> validateLine("Ivan;12345678ab;ivan@mail.ru"));

        expectError("Email без @",
            () -> validateLine("Ivan;1234567890;invalidemail"));

        expectError("Email с двумя @",
            () -> validateLine("Ivan;1234567890;a@@mail.ru"));

        expectError("Email без имени пользователя",
            () -> validateLine("Ivan;1234567890;@mail.ru"));

        expectError("Email без домена",
            () -> validateLine("Ivan;1234567890;ivan@"));

        expectError("Пустое имя",
            () -> parser.parse(";1234567890;ivan@mail.ru"));

        expectError("Недостаточно полей",
            () -> parser.parse("Ivan;1234567890"));

        expectError("Слишком много полей",
            () -> parser.parse(
                "Ivan;1234567890;ivan@mail.ru;extra"
            ));

        expectError("Пустая строка",
            () -> parser.parse(""));

        // проверяем импорт тестового файла
        Path path = Path.of(
            "practice-11", "data", "sample.csv"
        );

        ImportResult result = service.importFile(path);

        check("Принято 3 клиента",
            result.getAcceptedCount() == 3);

        check("Отклонено 4 клиента",
            result.getRejectedCount() == 4);

        check("Импорт продолжается после ошибки",
            result.getCustomers().stream().anyMatch(
                c -> c.getName().equals("Petr")
            ));

        check("Ошибки содержат номера строк",
            result.getErrors().stream().anyMatch(
                e -> e.contains("Строка 4:")
            ));

        check("Дубликат ИНН обнаружен",
            result.getErrors().stream().anyMatch(
                e -> e.contains("Дубликат ИНН")
            ));

        // проверяем защиту списка клиентов
        expectUnsupported("Список клиентов неизменяемый",
            () -> result.getCustomers().clear());

        // проверяем отсутствие пути
        try {
            service.importFile(null);
            check("Отсутствующий путь", false);
        } catch (IllegalArgumentException e) {
            check("Отсутствующий путь", true);
        }

        // проверяем ошибку открытия файла
        try {
            service.importFile(
                Path.of("practice-11", "data", "missing.csv")
            );
            check("Несуществующий файл", false);
        } catch (IOException e) {
            check("Несуществующий файл", true);
        }

        // проверяем файл без заголовка
        Path emptyFile = Files.createTempFile(
            "customers-empty-", ".csv"
        );

        try {
            Files.writeString(
                emptyFile, "", StandardCharsets.UTF_8
            );

            try {
                service.importFile(emptyFile);
                check("Пустой CSV-файл", false);
            } catch (IOException e) {
                check("Пустой CSV-файл", true);
            }

        } finally {
            Files.deleteIfExists(emptyFile);
        }

        // проверяем границы имени
        validateLine("A".repeat(25) + ";1234567890;a@mail.ru");
        check("Имя из 25 символов допустимо", true);

        expectError("Имя из 26 символов запрещено",
            () -> validateLine(
                "A".repeat(26) + ";1234567890;a@mail.ru"
            )
        );


        System.out.println("\n========================");
        System.out.println("Успешных проверок: " + passed);
        System.out.println("Ошибок: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void validateLine(String line) {
        Customer customer = parser.parse(line);
        validator.validate(customer);
    }

    private static void expectError(
            String name, Runnable action) {

        try {
            action.run();
            check(name, false);
        } catch (IllegalArgumentException e) {
            check(name, true);
        }
    }

    private static void expectUnsupported(
            String name, Runnable action) {

        try {
            action.run();
            check(name, false);
        } catch (UnsupportedOperationException e) {
            check(name, true);
        }
    }

    private static void check(
            String name, boolean success) {

        if (success) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }
}
