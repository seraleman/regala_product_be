package com.seraleman.regala_product_be.components.primary;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.primary.services.IPrimaryService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public ResponseEntity<?> getAllPrimaries() {
        return primaryService.getAllPrimaries();
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getAllPrimariesByCollectionId(@PathVariable String collectionId) {
        return primaryService.getAllPrimariesByCollectionId(collectionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrimaryById(@PathVariable String id) {
        return primaryService.getPrimaryById(id);
    }

    @PostMapping("/")
    public ResponseEntity<?> createPrimary(@Valid @RequestBody Primary primary, BindingResult result) {
        return primaryService.createPrimary(primary, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrimary(@PathVariable String id, @Valid @RequestBody Primary primary,
            BindingResult result) {
        return primaryService.updatePrimaryById(id, primary, result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrimaryById(@PathVariable String id) {
        return primaryService.deletePrimaryById(id);
    }

}
