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
@RequestMapping("/api/performance1")
@CrossOrigin(origins = "*")
public class Performance1Controller {

    private final Performance1Repository repository;

    public Performance1Controller(Performance1Repository repository) {
        this.repository = repository;
    }

    // GET all Performance1s
    @GetMapping
    public List<Performance1> getAllPerformance1s() {
        return repository.findAll();
    }

    // CREATE Performance1
    @PostMapping
    public Performance1 createPerformance1(@RequestBody @NonNull Performance1 Performance1) {
        return repository.save(Performance1);
    }

    // GET Performance1 by id
    @GetMapping("/{id}")
    public Performance1 getPerformance1(@PathVariable  @NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance1 not found"));
    }

    // DELETE Performance1
    @DeleteMapping("/{id}")
    public void deletePerformance1(@PathVariable  @NonNull Long id) {
        repository.deleteById(id);
    }

   //--------------------------------------------
    
   @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream()) {

            // vider la table
            repository.deleteAllInBatch();
            repository.resetIdSequence();

            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            List<Performance1> Performance1s = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue; // ignorer header

                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.trim().isEmpty()) {
                    continue; // ignorer ligne vide
                }

                Performance1 Performance1 = new Performance1();
                Performance1.setNumber(number.trim());

                // SERVICE
                String cell1 = formatter.formatCellValue(row.getCell(1));
                Performance1.setService(cell1.trim());

                // OPENED
                Cell cell2 = row.getCell(2);
                if (cell2 != null && cell2.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell2)) {
                    Performance1.setOpened(cell2.getDateCellValue());
                }

                // RESOLVED
                Cell cell3 = row.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell3)) {
                    Performance1.setResolved(cell3.getDateCellValue());
                }

                // MTTR 8 DAYS
                Cell cell4 = row.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.NUMERIC) {
                    Performance1.setMttr8days((int) cell4.getNumericCellValue());
                }
                

                Performance1s.add(Performance1);
            }

            workbook.close();

            // insertion batch (beaucoup plus rapide)
            repository.saveAll(Performance1s);

            return Performance1s.size() + " Performance1s importés";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'import : " + e.getMessage();
        }
    }


}