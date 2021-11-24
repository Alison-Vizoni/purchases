package br.com.alison.purchases.service.validation;

import br.com.alison.purchases.domain.enums.ClientType;
import br.com.alison.purchases.dto.ClientNewDTO;
import br.com.alison.purchases.service.exceptions.FieldMessage;
import br.com.alison.purchases.service.validation.utils.BR;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClientInsertValidator implements ConstraintValidator<ClientInsert, ClientNewDTO> {

    @Override
    public void initialize(ClientInsert clientInsert) {
    }

    @Override
    public boolean isValid(ClientNewDTO clientNewDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(clientNewDTO.getClientType().equals(ClientType.LEGAL_PERSON.getCode()) && !BR.isValidCPF(clientNewDTO.getCpfOrCnpj())){
            list.add(new FieldMessage("cpfOrCnpj", "Invalid CPF"));
        }

        if(clientNewDTO.getClientType().equals(ClientType.LEGAL_ENTITY.getCode()) && !BR.isValidCNPJ(clientNewDTO.getCpfOrCnpj())){
            list.add(new FieldMessage("cpfOrCnpj", "Invalid CNPJ"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
