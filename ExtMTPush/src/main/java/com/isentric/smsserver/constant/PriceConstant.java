package com.isentric.smsserver.constant;

public class PriceConstant {

    // SMS Pricing
    public static final double PRICE_LOCAL_SMS = 0.10;
    public static final double PRICE_PREMIUM_SMS = 0.50;
    public static final double PRICE_INTERNATIONAL_SMS = 0.30;

    // Bulk Pricing
    public static final double PRICE_BULK_SMS_TIER1 = 0.08;  // 1-1000 SMS
    public static final double PRICE_BULK_SMS_TIER2 = 0.06;  // 1001-10000 SMS
    public static final double PRICE_BULK_SMS_TIER3 = 0.04;  // 10001+ SMS

    // Thresholds
    public static final int BULK_TIER1_THRESHOLD = 1000;
    public static final int BULK_TIER2_THRESHOLD = 10000;

    // Currency
    public static final String CURRENCY_MYR = "MYR";
    public static final String CURRENCY_USD = "USD";

    // Tax Rate
    public static final double TAX_RATE = 0.06;  // 6% SST

    public static double calculateBulkPrice(int quantity) {
        if (quantity <= BULK_TIER1_THRESHOLD) {
            return quantity * PRICE_BULK_SMS_TIER1;
        } else if (quantity <= BULK_TIER2_THRESHOLD) {
            return (BULK_TIER1_THRESHOLD * PRICE_BULK_SMS_TIER1) +
                   ((quantity - BULK_TIER1_THRESHOLD) * PRICE_BULK_SMS_TIER2);
        } else {
            return (BULK_TIER1_THRESHOLD * PRICE_BULK_SMS_TIER1) +
                   ((BULK_TIER2_THRESHOLD - BULK_TIER1_THRESHOLD) * PRICE_BULK_SMS_TIER2) +
                   ((quantity - BULK_TIER2_THRESHOLD) * PRICE_BULK_SMS_TIER3);
        }
    }

    public static double applyTax(double amount) {
        return amount * (1 + TAX_RATE);
    }
}

