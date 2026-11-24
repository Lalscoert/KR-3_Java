import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Warehouse {

    private static final int MIN_RESERVE = 25;

    // оборудование по уникальному инвентарному коду
    private final Map<String, StockItem> items =
        new LinkedHashMap<>();

    // история складских операций
    private final List<StockMovement> movements =
        new ArrayList<>();

    // регистрация нового оборудования
    public void addItem(StockItem item) {

        if (item == null) {
            throw new IllegalArgumentException(
                "Оборудование не указано"
            );
        }

        if (items.containsKey(item.getCode())) {
            throw new IllegalArgumentException(
                "Оборудование с таким кодом уже существует"
            );
        }

        StockItem copy = item.copy();
        items.put(copy.getCode(), copy);

        if (copy.getQuantity() > 0) {
            movements.add(new StockMovement(
                copy.getCode(),
                MovementType.RECEIVE,
                copy.getQuantity()
            ));
        }
    }

    // приход оборудования
    public void receive(String code, int quantity) {

        StockItem item = requireItem(code);

        item.increase(quantity);

        movements.add(new StockMovement(
            item.getCode(),
            MovementType.RECEIVE,
            quantity
        ));
    }

    // выдача оборудования
    public void issue(String code, int quantity) {

        StockItem item = requireItem(code);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Количество должно быть положительным"
            );
        }

        if (quantity > item.getQuantity()) {
            throw new IllegalArgumentException(
                "Недостаточно оборудования на складе"
            );
        }

        // Вариант №4: минимальный резерв 25 единиц
        if (item.getQuantity() - quantity < MIN_RESERVE) {
            throw new IllegalArgumentException(
                "После выдачи должно остаться минимум 25 единиц"
            );
        }

        item.decrease(quantity);

        movements.add(new StockMovement(
            item.getCode(),
            MovementType.ISSUE,
            quantity
        ));
    }

    // поиск по инвентарному коду
    public StockItem findByCode(String code) {
        return requireItem(code).copy();
    }

    // поиск оборудования по категории
    public List<StockItem> findByCategory(Category category) {

        if (category == null) {
            throw new IllegalArgumentException(
                "Категория обязательна"
            );
        }

        List<StockItem> result = new ArrayList<>();

        for (StockItem item : items.values()) {
            if (item.getCategory() == category) {
                result.add(item.copy());
            }
        }

        return result;
    }

    // подсчёт оборудования по категориям
    public Map<Category, Integer> totalsByCategory() {

        Map<Category, Integer> totals =
            new EnumMap<>(Category.class);

        for (StockItem item : items.values()) {

            Category category = item.getCategory();

            totals.merge(
                category,
                item.getQuantity(),
                Math::addExact
            );
        }

        return totals;
    }

    // получение уникальных категорий
    public Set<Category> getUniqueCategories() {

        Set<Category> result =
            EnumSet.noneOf(Category.class);

        for (StockItem item : items.values()) {
            result.add(item.getCategory());
        }

        return result;
    }

    // получение списка всех товаров
    public List<StockItem> getAllItems() {

        List<StockItem> result = new ArrayList<>();

        for (StockItem item : items.values()) {
            result.add(item.copy());
        }

        return result;
    }

    // получение истории операций
    public List<StockMovement> getMovements() {
        return List.copyOf(movements);
    }

    // проверка существования оборудования
    private StockItem requireItem(String code) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                "Инвентарный код обязателен"
            );
        }

        StockItem item = items.get(code.trim());

        if (item == null) {
            throw new IllegalArgumentException(
                "Оборудование не найдено: " + code
            );
        }

        return item;
    }
}
