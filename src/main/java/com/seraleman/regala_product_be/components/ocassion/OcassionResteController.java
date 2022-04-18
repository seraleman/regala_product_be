package com.seraleman.regala_product_be.components.ocassion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.ocassion.compromise.IOcassionCompromise;
import com.seraleman.regala_product_be.components.ocassion.services.IOcassionService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.response.IResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ocassion")
public class OcassionResteController {

    private static final String ENTITY = "Ocassion";

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IOcassionCompromise ocassionCompromise;

    @Autowired
    private IOcassionService ocassionService;

    @Autowired
    private IResponse response;

    @PostMapping("/")
    public ResponseEntity<?> createOcassion(@Valid @RequestBody Ocassion ocassion,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            ocassion.setCreated(ldt);
            ocassion.setUpdated(localDateTime.getLocalDateTime());
            return response.created(ocassionService.saveOcassion(ocassion));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getOcassions() {
        try {
            List<Ocassion> ocassions = ocassionService.getOcassions();
            if (ocassions.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(ocassions, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOcassionById(@PathVariable String id) {
        try {
            Ocassion ocassion = ocassionService.getOcassionById(id);
            if (ocassion == null) {
                return response.notFound(id, ENTITY);
            }
            Map<String, Object> responseCompromisedEntities = ocassionCompromise
                    .deleteOcassionInCompromisedEntities(ocassion);

            // ocassionService.deleteOcassionById(id);

            return response.deletedWithCompromisedEntities(
                    responseCompromisedEntities, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOcassionById(@PathVariable String id) {
        try {
            Ocassion ocassion = ocassionService.getOcassionById(id);
            if (ocassion == null) {
                return response.notFound(id, ENTITY);
            }
            return response.found(ocassion);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOcassionById(@PathVariable String id,
            @Valid @RequestBody Ocassion ocassion,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Ocassion currentOcassion = ocassionService.getOcassionById(id);
            if (currentOcassion == null) {
                return response.notFound(id, ENTITY);
            }
            currentOcassion.setDescription(ocassion.getDescription());
            currentOcassion.setName(ocassion.getName());
            currentOcassion.setCreated(ocassion.getCreated());
            currentOcassion.setUpdated(localDateTime.getLocalDateTime());

            return response.updatedWithCompromisedEntities(
                    ocassionService.saveOcassion(currentOcassion),
                    ocassionCompromise.updateOcassionInCompromisedEntities(currentOcassion),
                    ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/delete/allOcassions")
    public ResponseEntity<?> deleteAllOcassions() {
        ocassionService.deleteAllOcassions();
        return response.deletedAll(ENTITY);
    }

}
