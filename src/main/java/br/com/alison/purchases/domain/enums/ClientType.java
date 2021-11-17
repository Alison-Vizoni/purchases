package br.com.alison.purchases.domain.enums;

public enum ClientType {
    LEGAL_PERSON(1L, "Legal Person"),
    LEGAL_ENTITY(2L, "Legal Entity");

    private Long code;
    private String description;

    private ClientType(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public Long getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ClientType toEnum(Long code){
        if(code ==null){
            return null;
        }

        for(ClientType clientType: ClientType.values()){
            if(code.equals(clientType.getCode())){
                return clientType;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
