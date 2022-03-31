package com.seraleman.regala_product_be.components.element.services;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.IElementDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementServiceImpl implements IElementService {

    @Autowired
    private IElementDao elementDao;

    @Override
    public List<Element> getAllElements() {
        return elementDao.findAll();
    }

    @Override
    public List<Element> getAllElementsByCollectionId(String collectionId) {
        return elementDao.findAllByCollectionId(collectionId);
    }

    @Override
    public List<Element> getAllElementsByPrimariesPrimaryId(String primaryId) {
        return elementDao.findAllByPrimariesPrimaryId(primaryId);
    }

    @Override
    public Element getElementById(String id) {
        return elementDao.findById(id).orElse(null);
    }

    @Override
    public Element saveElement(Element element) {
        return elementDao.save(element);
    }

    @Override
    public void deleteElementById(String id) {
        elementDao.deleteById(id);
    }

}
