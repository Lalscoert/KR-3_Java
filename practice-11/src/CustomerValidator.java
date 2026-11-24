public final class CustomerValidator {

    private static final int MAX_NAME_LENGTH = 25;

    public void validate(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                "Данные клиента отсутствуют"
            );
        }

        // проверяем обязательное имя клиента
        String name = customer.getName();

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                "Имя клиента обязательно"
            );
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                "Имя клиента не может быть длиннее 25 символов"
            );
        }

        // проверяем длину и формат инн
        String inn = customer.getInn();

        if (!inn.matches("(\\d{10}|\\d{12})")) {
            throw new IllegalArgumentException(
                "ИНН должен содержать 10 или 12 цифр"
            );
        }

        // проверяем формат электронной почты
        String email = customer.getEmail();

        int atPosition = email.indexOf('@');

        if (atPosition <= 0
                || atPosition != email.lastIndexOf('@')
                || atPosition == email.length() - 1) {

            throw new IllegalArgumentException(
                "Некорректный формат email"
            );
        }
    }
}
