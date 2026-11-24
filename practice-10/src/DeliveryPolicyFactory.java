public final class DeliveryPolicyFactory {

    public DeliveryPolicy create(DeliveryType type) {

        if (type == null) {
            throw new IllegalArgumentException(
                "Способ доставки не указан"
            );
        }

        switch (type) {

            case PICKUP:
                return new PickupPolicy();

            case STANDARD:
                return new StandardCourierPolicy();

            case EXPRESS:
                return new ExpressPolicy();

            case ECONOMY:
                return new EconomyPolicy();

            default:
                throw new IllegalArgumentException(
                    "Неизвестный способ доставки: " + type
                );
        }
    }
}
