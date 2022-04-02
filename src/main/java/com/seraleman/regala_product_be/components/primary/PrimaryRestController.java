package com.seraleman.regala_product_be.components.primary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.ElementComposition;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.primary.services.IPrimaryService;
import com.seraleman.regala_product_be.components.primary.services.updatePrimaryInEntities.IUpdatePrimaryInEntities;
import com.seraleman.regala_product_be.services.localDataTime.ILocalDateTimeService;
import com.seraleman.regala_product_be.services.response.IResponseService;
import com.seraleman.regala_product_be.services.validate.IValidateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/primary")
public class PrimaryRestController {

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private IResponseService response;

    @Autowired
    private IUpdatePrimaryInEntities updatePrimary;

    @Autowired
    private ILocalDateTimeService localDateTime;

    @Autowired
    private IElementService elementService;

    @Autowired
    private IValidateService validateService;

    @GetMapping("/")
    public ResponseEntity<?> getAllPrimaries() {
        try {
            List<Primary> primaries = primaryService.getAllPrimaries();
            if (primaries.isEmpty()) {
                return response.empty();
            }
            return response.list(primaries);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getAllPrimariesByCollectionId(@PathVariable String collectionId) {
        try {
            List<Primary> primaries = (List<Primary>) primaryService.getAllPrimariesByCollectionId(collectionId);
            if (primaries.isEmpty()) {
                return response.empty();
            }
            return response.list(primaries);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrimaryById(@PathVariable String id) {
        try {
            Primary primary = primaryService.getPrimaryById(id);
            if (primary == null) {
                return response.notFound(id);
            }
            return response.found(primary);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createPrimary(@Valid @RequestBody Primary primary, BindingResult result) {

        BindingResult resultCollection = validateService.validateCollection(result, primary.getCollection());
        if (resultCollection.hasErrors()) {
            return response.invalidObject(result);
        }

        try {
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            primary.setCreated(ldt);
            primary.setUpdated(ldt);
            return response.created(primaryService.savePrimary(primary));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/withElement")
    public ResponseEntity<?> createPrimaryWithElement(@Valid @RequestBody Primary primary,
            BindingResult result) {

        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            LocalDateTime ldt = localDateTime.getLocalDateTime();
            primary.setCreated(ldt);
            primary.setUpdated(ldt);
            Primary createdPrimary = primaryService.savePrimary(primary);

            ElementComposition component = new ElementComposition();
            component.setPrimary(createdPrimary);
            component.setQuantity(1f);

            List<ElementComposition> primaries = new ArrayList<>();
            primaries.add(component);

            Element newElement = new Element();

            newElement.setCollection(createdPrimary.getCollection());
            newElement.setCreated(localDateTime.getLocalDateTime());
            newElement.setDescription(createdPrimary.getName());
            newElement.setName(createdPrimary.getName());
            newElement.setPrimaries(primaries);

            data.put("createdPrimary", createdPrimary);
            data.put("createdElement", elementService.saveElement(newElement));
            response.put("data", data);
            response.put("message", "Primario y elemento creados");

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrimary(@PathVariable String id, @Valid @RequestBody Primary primary,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Map<String, Object> updatedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            Primary currentPrimary = primaryService.getPrimaryById(id);
            if (currentPrimary == null) {
                return response.notFound(id);
            }
            currentPrimary.setBudget(primary.getBudget());
            currentPrimary.setCollection(primary.getCollection());
            currentPrimary.setName(primary.getName());
            currentPrimary.setUpdated(localDateTime.getLocalDateTime());

            data.put("updatedPrimary", primaryService.savePrimary(currentPrimary));
            data.put("updatedEntities", updatePrimary.updatePrimaryInEntities(currentPrimary));
            updatedResponse.put("data", data);
            updatedResponse.put("message", "object updated");

            return new ResponseEntity<Map<String, Object>>(updatedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrimaryById(@PathVariable String id) {
        try {
            Map<String, Object> responseCouldNotDelete = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            List<Element> elements = elementService.getAllElementsByPrimariesPrimaryId(id);

            if (elements.isEmpty()) {
                Primary primary = primaryService.getPrimaryById(id);
                if (primary == null) {
                    return response.notFound(id);
                }
                primaryService.deletePrimaryById(id);
                return response.deleted();
            }

            data.put("isInElements", elements.size());

            responseCouldNotDelete.put("data", data);
            responseCouldNotDelete.put("message",
                    "no se puede eliminar el primario porque está presente en otra entidad");

            return new ResponseEntity<Map<String, Object>>(responseCouldNotDelete, HttpStatus.PRECONDITION_REQUIRED);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteUnusedPrimaries")
    public ResponseEntity<?> deleteUnusedPrimaries() {
        try {
            Map<String, Object> deletedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            List<Primary> primaries = primaryService.getAllPrimaries();
            List<Primary> undeletedPrimaryList = new ArrayList<>();

            Integer deletedPrimaries = 0;
            for (Primary primary : primaries) {
                List<Element> elements = elementService.getAllElementsByPrimariesPrimaryId(primary.getId());

                if (elements.isEmpty()) {
                    primaryService.deletePrimaryById(primary.getId());
                    deletedPrimaries++;
                } else {
                    undeletedPrimaryList.add(primary);
                }
            }

            Integer undeletedPrimary = primaries.size() - deletedPrimaries;
            data.put("deletePrimaries", deletedPrimaries);
            data.put("undeletedPrimary", undeletedPrimary);
            data.put("undeletedPrimaryList", undeletedPrimaryList);

            deletedResponse.put("data", data);

            if (deletedPrimaries == 0) {
                deletedResponse.put("message",
                        "no se eliminaron primarios porque todos están presentes en otras entidades");
            } else if (deletedPrimaries == 1) {
                if (undeletedPrimary == 0) {
                    deletedResponse.put("message", "se eliminó un primario");
                } else if (undeletedPrimary == 1) {
                    deletedResponse.put("message",
                            "se eliminó un primario, el primario no eliminado pertenece a otros elementos");
                } else {
                    deletedResponse.put("message", "se eliminó un primario, los "
                            .concat(String.valueOf(undeletedPrimary))
                            .concat(" primarios no eliminados pertenecen a otros elementos"));
                }
            } else {
                if (undeletedPrimary == 0) {
                    deletedResponse.put("message", "se eliminaron todos los primarios");
                } else if (undeletedPrimary == 1) {
                    deletedResponse.put("message", "se eliminaron "
                            .concat(String.valueOf(deletedPrimaries))
                            .concat(" primarios, el primario no eliminado pertenece a otros elementos"));
                } else {
                    deletedResponse.put("message", "se eliminaron "
                            .concat(String.valueOf(undeletedPrimary))
                            .concat(" primarios, los ")
                            .concat(String.valueOf(undeletedPrimary))
                            .concat(" primarios no eliminados pertenecen a otros elementos"));
                }
            }

            return new ResponseEntity<Map<String, Object>>(deletedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }
}
