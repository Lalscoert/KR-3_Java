public final class DeliveryRequest {

    private final DeliveryType type;
    private final double distanceKm;
    private final double weightKg;

    public DeliveryRequest(
            DeliveryType type,
            double distanceKm,
            double weightKg) {

        if (type == null) {
            throw new IllegalArgumentException(
                "Способ доставки обязателен"
            );
        }

        if (!Double.isFinite(distanceKm) || distanceKm < 0) {
            throw new IllegalArgumentException(
                "Расстояние должно быть неотрицательным числом"
            );
        }

        if (!Double.isFinite(weightKg) || weightKg <= 0) {
            throw new IllegalArgumentException(
                "Вес должен быть положительным числом"
            );
        }

        this.type = type;
        this.distanceKm = distanceKm;
        this.weightKg = weightKg;
    }

    public DeliveryType getType() {
        return type;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getWeightKg() {
        return weightKg;
    }
}
