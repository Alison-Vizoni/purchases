package br.com.alison.purchases.service.validation;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.dto.ClientDTO;
import br.com.alison.purchases.repository.ClientRepository;
import br.com.alison.purchases.service.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientUpdateValidator implements ConstraintValidator<ClientUpdate, ClientDTO> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void initialize(ClientUpdate clientUpdate) {
    }

    @Override
    public boolean isValid(ClientDTO clientDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long uriId = Long.parseLong(map.get("id"));

        Client clientEmail = clientRepository.findByEmail(clientDTO.getEmail());
        if(clientEmail != null && !clientEmail.getId().equals(uriId)){
            list.add(new FieldMessage("email", "Email already exists"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
