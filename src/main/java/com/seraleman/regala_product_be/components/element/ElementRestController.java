package com.seraleman.regala_product_be.components.element;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.element.services.IElementService;

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
@RequestMapping("/element")
public class ElementRestController {

    @Autowired
    private IElementService elementService;

    @GetMapping("/")
    public ResponseEntity<?> getAllElements() {
        return elementService.getAllElements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getElementById(@PathVariable Long id) {
        return elementService.getElementById(id);
    }

    @PostMapping("/")
    public ResponseEntity<?> createElement(@Valid @RequestBody Element element, BindingResult result) {
        return elementService.createElement(element, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateElementById(@PathVariable Long id, @Valid @RequestBody Element element,
            BindingResult result) {
        return elementService.updateElementById(id, element, result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteElementById(@PathVariable Long id) {
        return elementService.deleteElementById(id);
    }

}
