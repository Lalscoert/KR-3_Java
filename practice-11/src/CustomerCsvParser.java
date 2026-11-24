public final class CustomerCsvParser {

    public Customer parse(String line) {

        // проверяем наличие строки
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException(
                "Строка клиента пустая"
            );
        }

        // разделяем строку по точке с запятой
        String[] fields = line.split(";", -1);

        // проверяем количество полей
        if (fields.length != 3) {
            throw new IllegalArgumentException(
                "Ожидалось 3 поля, получено: " + fields.length
            );
        }

        // извлекаем данные клиента
        String name = fields[0].trim();
        String inn = fields[1].trim();
        String email = fields[2].trim();

        // создаём объект клиента
        return new Customer(name, inn, email);
    }
}
