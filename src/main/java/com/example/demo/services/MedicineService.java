package com.example.demo.services;

import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicineService {
    MedicineRepository repository;

    public MedicineService(MedicineRepository repository) {
        this.repository = repository;
    }

    public List<Medicine> getAllMedicine(){
        return repository.findAll();
    }

    public Medicine getMedicineById(Long id){
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public Medicine createMedicine(Medicine medicine){
        repository.findById(medicine.getId()).ifPresent(__ -> {
            throw new IllegalArgumentException();
        });
        return repository.save(medicine);
    }

    @Transactional
    public Medicine updateMedicine(Medicine medicine){
        return repository.findById(medicine.getId()).map(repository::save).orElseThrow();
    }

    @Transactional
    public void deleteMedicineById(Long id){
        repository.delete(repository.findById(id).orElseThrow());
    }
}
