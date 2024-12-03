public class ProductPromotion {
    public String productName;
    public int promotionPeriod;

    public ProductPromotion(String productName, int promotionPeriod) {
        this.productName = productName;
        this.promotionPeriod = promotionPeriod;
    }

    public String getProductName() {
        return productName;
    }

    public int getPromotionPeriod() {
        return promotionPeriod;
    }

}
