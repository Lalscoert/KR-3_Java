import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public final class CustomerImportService {

    private final CustomerCsvParser parser =
        new CustomerCsvParser();

    private final CustomerValidator validator =
        new CustomerValidator();

    // импорт клиентов из csv-файла
    public ImportResult importFile(Path path) throws IOException {

        if (path == null) {
            throw new IllegalArgumentException(
                "Путь к файлу не указан"
            );
        }

        ImportResult result = new ImportResult();

        // множество для проверки повторяющихся инн
        Set<String> usedInns = new HashSet<>();

        // открываем файл с автоматическим закрытием
        try (BufferedReader reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8)) {

            // первая строка содержит заголовок
            String header = reader.readLine();

            if (header == null || header.isBlank()) {
                throw new IOException(
                    "CSV-файл не содержит заголовка"
                );
            }

            if (header.split(";", -1).length != 3) {
                throw new IOException(
                    "Некорректный заголовок CSV-файла"
                );
            }

            String line;
            int lineNumber = 1;

            // обрабатываем каждую строку отдельно
            while ((line = reader.readLine()) != null) {

                lineNumber++;

                try {
                    // преобразуем строку в объект
                    Customer customer = parser.parse(line);

                    // проверяем данные клиента
                    validator.validate(customer);

                    // проверяем повторение инн
                    if (usedInns.contains(customer.getInn())) {
                        throw new IllegalArgumentException(
                            "Дубликат ИНН: " + customer.getInn()
                        );
                    }

                    // сохраняем корректного клиента
                    result.addAccepted(customer);

                    usedInns.add(customer.getInn());

                } catch (IllegalArgumentException e) {

                    // записываем ошибку и продолжаем импорт
                    result.addRejected(
                        lineNumber,
                        e.getMessage()
                    );
                }
            }
        }

        return result;
    }
}
