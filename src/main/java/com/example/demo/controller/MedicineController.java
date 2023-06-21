package com.example.demo.controller;

import com.example.demo.model.Medicine;
import com.example.demo.services.MedicineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    MedicineService service;

    public MedicineController(MedicineService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Medicine> getAllMedicine(){
        return service.getAllMedicine();
    }

    @GetMapping("/{id}")
    public Medicine getMedicineById(@PathVariable Long id){
        return service.getMedicineById(id);
    }

    @PostMapping("/")
    public Medicine postMedicine(@RequestBody Medicine medicine){
        return service.createMedicine(medicine);
    }

    @PutMapping("/{id}")
    public Medicine putMedicine(@PathVariable Long id, @RequestBody Medicine medicine){
        if (!Objects.equals(medicine.getId(), id)){
            throw new IllegalArgumentException();
        }

        return service.updateMedicine(medicine);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicine(@PathVariable Long id){
        service.deleteMedicineById(id);
    }
}
