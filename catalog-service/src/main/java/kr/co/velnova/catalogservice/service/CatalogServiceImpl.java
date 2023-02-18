package kr.co.velnova.catalogservice.service;

import kr.co.velnova.catalogservice.jpa.CatalogEntity;
import kr.co.velnova.catalogservice.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository repository;

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return repository.findAll();
    }
}
