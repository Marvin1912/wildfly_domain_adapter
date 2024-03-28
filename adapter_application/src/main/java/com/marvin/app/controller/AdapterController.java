package com.marvin.app.controller;

import com.marvin.importer.costs.CostImporter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdapterController {

    private final CostImporter costImporter;

    public AdapterController(CostImporter costImporter) {
        this.costImporter = costImporter;
    }

    @PostMapping("/import/costs")
    public ResponseEntity<Void> triggerCostImport() {
        costImporter.importFiles();
        return ResponseEntity.ok().build();
    }

}
