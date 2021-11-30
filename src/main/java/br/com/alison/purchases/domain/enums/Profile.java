package br.com.alison.purchases.domain.enums;

public enum Profile {
    ADMIN(1L, "ROLE_ADMIN"),
    CLIENT(2L,"ROLE_CLIENT");

    private Long code;
    private String description;

    private Profile(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public Long getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Profile toEnum(Long code){
        if(code ==null){
            return null;
        }

        for(Profile paymentStatus: Profile.values()){
            if(code.equals(paymentStatus.getCode())){
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
