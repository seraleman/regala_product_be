package com.seraleman.regala_product_be.components.ocassion;

import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.ocassion.services.IOcassionService;
import com.seraleman.regala_product_be.services.ILocalDateTimeService;
import com.seraleman.regala_product_be.services.IResponseService;

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

    @Autowired
    private IOcassionService ocassionService;

    @Autowired
    private IResponseService response;

    @Autowired
    private ILocalDateTimeService localDateTime;

    @GetMapping("/")
    public ResponseEntity<?> getAllOcassions() {
        try {
            List<Ocassion> ocassions = ocassionService.getAllOcassions();
            if (ocassions.isEmpty()) {
                return response.empty();
            }
            return response.list(ocassions);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOcassionById(@PathVariable String id) {
        try {
            Ocassion ocassion = ocassionService.getOcassionById(id);
            if (ocassion == null) {
                return response.notFound(id);
            }
            return response.found(ocassion);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createOcassion(@Valid @RequestBody Ocassion ocassion, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            ocassion.setCreated(localDateTime.getLocalDateTime());
            return response.created(ocassionService.saveOcassion(ocassion));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOcassionById(@PathVariable String id, @Valid @RequestBody Ocassion ocassion,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Ocassion currentOcassion = ocassionService.getOcassionById(id);
            if (currentOcassion == null) {
                return response.notFound(id);
            }
            currentOcassion.setDescription(ocassion.getDescription());
            currentOcassion.setName(ocassion.getName());
            currentOcassion.setUpdated(localDateTime.getLocalDateTime());
            return response.updated(ocassionService.saveOcassion(ocassion));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOcassionById(@PathVariable String id) {
        try {
            Ocassion ocassion = ocassionService.getOcassionById(id);
            if (ocassion == null) {
                return response.notFound(id);
            }
            ocassionService.deleteOcassionById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteOcassions")
    public ResponseEntity<?> deleteAllOcassions() {
        ocassionService.deleteAllOcassions();
        return response.deleted();
    }

}
