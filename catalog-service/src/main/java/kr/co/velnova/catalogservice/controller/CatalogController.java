package kr.co.velnova.catalogservice.controller;

import kr.co.velnova.catalogservice.service.CatalogService;
import kr.co.velnova.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {

    private final CatalogService service;

    @GetMapping("/catalogs")
    public List<ResponseCatalog> getCatalogs(){
        List<ResponseCatalog> result = new ArrayList<>();
        service.getAllCatalogs().forEach(catalogEntity -> {
            result.add(new ModelMapper().map(catalogEntity, ResponseCatalog.class));
        });

        return result;
    }
}
