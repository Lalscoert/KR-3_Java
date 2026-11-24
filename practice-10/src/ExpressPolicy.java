public final class ExpressPolicy implements DeliveryPolicy {

    private static final double BASE_COST = 250.0;
    private static final double COST_PER_KM = 30.0;
    private static final double COST_PER_KG = 10.0;

    private static final double MAX_DISTANCE = 30.0;
    private static final double MAX_WEIGHT = 10.0;

    @Override
    public boolean supports(DeliveryRequest request) {

        return request != null
            && request.getDistanceKm() <= MAX_DISTANCE
            && request.getWeightKg() <= MAX_WEIGHT;
    }

    @Override
    public double calculateCost(DeliveryRequest request) {

        if (!supports(request)) {
            throw new IllegalArgumentException(
                "Экспресс-доставка недоступна"
            );
        }

        return BASE_COST
            + request.getDistanceKm() * COST_PER_KM
            + request.getWeightKg() * COST_PER_KG;
    }
}
