package com.seraleman.regala_product_be.components.collection;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.collection.services.ICollectionService;

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
@RequestMapping("/collection")
public class CollectionRestController {

    @Autowired
    private ICollectionService collectionService;

    @GetMapping("/")
    public ResponseEntity<?> getAllCollections() {
        return collectionService.getAllCollections();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectionById(@PathVariable Long id) {
        return collectionService.getCollectionById(id);
    }

    @PostMapping("/")
    public ResponseEntity<?> createCollection(@Valid @RequestBody Collection collection, BindingResult result) {
        return collectionService.createCollection(collection, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollection(@PathVariable Long id, @Valid @RequestBody Collection collection,
            BindingResult result) {
        return collectionService.updateCollectionById(id, collection, result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollectionById(@PathVariable Long id) {
        return collectionService.deleteCollectionById(id);
    }
}
