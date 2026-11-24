import java.util.ArrayList;
import java.util.List;

public final class ImportResult {

    private final List<Customer> customers =
        new ArrayList<>();

    private final List<String> errors =
        new ArrayList<>();

    // добавляем успешно импортированного клиента
    public void addAccepted(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                "Клиент не указан"
            );
        }

        customers.add(customer);
    }

    // записываем ошибку конкретной строки
    public void addRejected(int lineNumber, String reason) {

        if (lineNumber < 1) {
            throw new IllegalArgumentException(
                "Неверный номер строки"
            );
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                "Причина ошибки обязательна"
            );
        }

        errors.add(
            "Строка " + lineNumber + ": " + reason
        );
    }

    // количество принятых записей
    public int getAcceptedCount() {
        return customers.size();
    }

    // количество отклонённых записей
    public int getRejectedCount() {
        return errors.size();
    }

    // возвращаем копию списка клиентов
    public List<Customer> getCustomers() {
        return List.copyOf(customers);
    }

    // возвращаем копию списка ошибок
    public List<String> getErrors() {
        return List.copyOf(errors);
    }

    // выводим итоговый отчёт
    public void printReport() {

        System.out.println("\n=== РЕЗУЛЬТАТ ИМПОРТА ===");

        System.out.println(
            "Принято записей: " + getAcceptedCount()
        );

        System.out.println(
            "Отклонено записей: " + getRejectedCount()
        );

        System.out.println("\nУспешно импортированные клиенты:");

        for (Customer customer : customers) {
            System.out.println(customer);
        }

        System.out.println("\nОшибки импорта:");

        for (String error : errors) {
            System.out.println(error);
        }
    }
}
