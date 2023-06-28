package com.example.demo.services;

import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class MedicineService {
    private final static Supplier<NoSuchElementException> medicineNotFoundSupplier =
            () -> new NoSuchElementException("Medicine was not found");

    MedicineRepository repository;

    public MedicineService(MedicineRepository repository) {
        this.repository = repository;
    }

    public List<Medicine> getAllMedicine(){
        return repository.findAll();
    }

    public Medicine getMedicineById(Long id){
        return repository.findById(id).orElseThrow(medicineNotFoundSupplier);
    }

    @Transactional
    public Medicine createMedicine(Medicine medicine){
        Optional.ofNullable(medicine.getId()).flatMap(repository::findById).ifPresent(__ -> {
            throw new IllegalArgumentException("Medicine with this id already exists");
        });
        return repository.save(medicine);
    }

    @Transactional
    public Medicine updateMedicine(Medicine medicine){
        repository.findById(medicine.getId()).ifPresentOrElse(
                __ -> repository.save(medicine),
                () -> {throw medicineNotFoundSupplier.get();}
        );
        return medicine;
    }

    @Transactional
    public void deleteMedicineById(Long id){
        repository.delete(repository.findById(id).orElseThrow(medicineNotFoundSupplier));
    }
}
