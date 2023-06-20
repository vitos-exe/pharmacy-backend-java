package com.example.demo.controllers;

import com.example.demo.model.Medicine;
import com.example.demo.services.MedicineService;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        if (medicine.getId() == null){
            medicine.setId(id);
        }

        if (medicine.getId() != id){
            throw new IllegalArgumentException();
        }

        return service.updateMedicine(medicine);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicine(@PathVariable Long id){
        service.deleteMedicineById(id);
    }
}
