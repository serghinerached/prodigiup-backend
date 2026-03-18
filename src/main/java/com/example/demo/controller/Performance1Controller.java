package com.example.demo.controller;

import com.example.demo.model.Performance1;
import com.example.demo.repository.Performance1Repository;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;

@RestController
@RequestMapping("/api/performance1s")
@CrossOrigin(origins = "*")
public class Performance1Controller {

    private final Performance1Repository repository;

    public Performance1Controller(Performance1Repository repository) {
        this.repository = repository;
    }

    // GET all performance1s
    @GetMapping
    public List<Performance1> getAllPerformance1s() {
        return repository.findAll();
    }

    // CREATE performance1
    @PostMapping
    public Performance1 createPerformance1(@RequestBody @NonNull Performance1 performance1) {
        return repository.save(performance1);
    }

    // GET performance1 by id
    @GetMapping("/{id}")
    public Performance1 getPerformance1(@PathVariable @NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance1 not found"));
    }

    // DELETE performance1
    @DeleteMapping("/{id}")
    public void deletePerformance1(@PathVariable @NonNull Long id) {
        repository.deleteById(id);
    }

    // IMPORT EXCEL
    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream()) {

            repository.deleteAllInBatch();

            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            List<Performance1> performance1s = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;
                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.trim().isEmpty()) continue;
                Performance1 performance1 = new Performance1();
                performance1.setNumber(number.trim());

                performance1.setService(formatter.formatCellValue(row.getCell(11)));

                // OPENED
                Cell c1 = row.getCell(1);
                if (c1 != null && c1.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c1)) {
                    performance1.setOpened(c1.getLocalDateTimeCellValue());
                }

                // RESOLVED
                Cell c7 = row.getCell(9);
                if (c7 != null && c7.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c7)) {
                    performance1.setResolved(c7.getLocalDateTimeCellValue());
                }

                performance1s.add(performance1);
            }

            workbook.close();
            repository.saveAll(performance1s);

            return performance1s.size() + " performance1s importés";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    }
}