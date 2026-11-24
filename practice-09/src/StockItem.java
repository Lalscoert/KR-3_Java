import java.util.Objects;

public class StockItem {

    private final String code;
    private final String name;
    private final Category category;
    private int quantity;

    public StockItem(String code, String name,
                     Category category, int quantity) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                "Инвентарный код обязателен"
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Название обязательно"
            );
        }

        if (category == null) {
            throw new IllegalArgumentException(
                "Категория обязательна"
            );
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(
                "Количество не может быть отрицательным"
            );
        }

        this.code = code.trim();
        this.name = name.trim();
        this.category = category;
        this.quantity = quantity;
    }

    // приход оборудования
    public void increase(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Количество должно быть положительным"
            );
        }

        quantity = Math.addExact(quantity, amount);
    }

    // выдача оборудования
    public void decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Количество должно быть положительным"
            );
        }

        if (amount > quantity) {
            throw new IllegalArgumentException(
                "Недостаточно оборудования на складе"
            );
        }

        quantity -= amount;
    }

    public StockItem copy() {
        return new StockItem(code, name, category, quantity);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof StockItem)) return false;

        StockItem other = (StockItem) obj;
        return code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " | " + name
            + " | " + category
            + " | Остаток: " + quantity;
    }
}
