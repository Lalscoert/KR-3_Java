import java.math.BigDecimal;

public final class Sale {

    private final String id;
    private final String manager;
    private final BigDecimal amount;

    public Sale(String id, String manager, BigDecimal amount) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                "ID продажи обязателен"
            );
        }

        if (manager == null || manager.isBlank()) {
            throw new IllegalArgumentException(
                "Имя менеджера обязательно"
            );
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                "Сумма продажи должна быть положительной"
            );
        }

        this.id = id.trim();
        this.manager = manager.trim();
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getManager() {
        return manager;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return id + " | " + manager + " | " + amount;
    }
}
