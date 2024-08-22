package com.marvin.export;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExportController {

    private final Exporter exporter;

    public ExportController(Exporter exporter) {
        this.exporter = exporter;
    }

    @GetMapping(path = "/export/costs")
    public ResponseEntity<Void> triggerCostExport() {
        exporter.exportCosts();
        return ResponseEntity.ok().build();
    }

}
