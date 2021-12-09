package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.City;
import br.com.alison.purchases.domain.State;
import br.com.alison.purchases.dto.CityDTO;
import br.com.alison.purchases.dto.StateDTO;
import br.com.alison.purchases.service.CityService;
import br.com.alison.purchases.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/states")
public class StateResource {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<StateDTO>> findAll(){
        List<State> stateList = stateService.findAll();
        List<StateDTO> stateDTOList = stateList.stream().map(StateDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(stateDTOList);
    }

    @GetMapping("/{idState}/cities")
    public ResponseEntity<List<CityDTO>> findAllCitiesFromState(@PathVariable Long idState){
        List<City> cityList = cityService.findByIdState(idState);
        List<CityDTO> cityDTOList = cityList.stream().map(CityDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(cityDTOList);
    }

}
