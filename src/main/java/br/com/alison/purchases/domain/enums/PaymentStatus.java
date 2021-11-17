package br.com.alison.purchases.domain.enums;

public enum PaymentStatus {
    PENDING(1L, "Pending"),
    OK(2L,"Ok"),
    CANCELED(3L,"Canceled");

    private Long code;
    private String description;

    private PaymentStatus(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public Long getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus toEnum(Long code){
        if(code ==null){
            return null;
        }

        for(PaymentStatus paymentStatus: PaymentStatus.values()){
            if(code.equals(paymentStatus.getCode())){
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
