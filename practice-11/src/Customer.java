public final class Customer {

    private final String name;
    private final String inn;
    private final String email;

    public Customer(String name, String inn, String email) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Имя клиента обязательно"
            );
        }

        if (inn == null || inn.isBlank()) {
            throw new IllegalArgumentException(
                "ИНН обязателен"
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "Email обязателен"
            );
        }

        this.name = name.trim();
        this.inn = inn.trim();
        this.email = email.trim();
    }

    public String getName() {
        return name;
    }

    public String getInn() {
        return inn;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " | ИНН: " + inn
            + " | Email: " + email;
    }
}
