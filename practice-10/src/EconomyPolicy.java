public final class EconomyPolicy implements DeliveryPolicy {

    private static final double COST_PER_KM = 25.0;

    private static final double BASE_COST = 50.0;
    private static final double COST_PER_KG = 3.0;

    private static final double MAX_DISTANCE = 50.0;
    private static final double MAX_WEIGHT = 20.0;

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
                "Эконом-доставка недоступна"
            );
        }

        return BASE_COST
            + request.getDistanceKm() * COST_PER_KM
            + request.getWeightKg() * COST_PER_KG;
    }
}
