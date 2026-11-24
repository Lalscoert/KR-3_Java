public final class StockMovement {

    private final String itemCode;
    private final MovementType type;
    private final int quantity;

    public StockMovement(
            String itemCode,
            MovementType type,
            int quantity) {

        if (itemCode == null || itemCode.isBlank()) {
            throw new IllegalArgumentException(
                "Код оборудования обязателен"
            );
        }

        if (type == null) {
            throw new IllegalArgumentException(
                "Тип операции обязателен"
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Количество должно быть положительным"
            );
        }

        this.itemCode = itemCode.trim();
        this.type = type;
        this.quantity = quantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public MovementType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return type + " | Код: " + itemCode
            + " | Количество: " + quantity;
    }
}
