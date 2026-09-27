package com.ibm.grocery.domain;

public enum StockMovementType {
    RECEIPT(1),
    RETURN(1),
    SALE(-1),
    WASTAGE(-1),
    ADJUSTMENT_INCREASE(1),
    ADJUSTMENT_DECREASE(-1);

    private final int direction;

    StockMovementType(int direction) {
        this.direction = direction;
    }

    public int direction() {
        return direction;
    }

    public boolean reducesStock() {
        return direction < 0;
    }
}
