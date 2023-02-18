package kr.co.velnova.catalogservice.service;

import kr.co.velnova.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
