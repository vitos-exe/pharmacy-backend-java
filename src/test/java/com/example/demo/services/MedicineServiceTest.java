package com.example.demo.services;

import com.example.demo.model.Medicine;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.MedicineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@DataJpaTest
@Import(MedicineService.class)
class MedicineServiceTest {

    @Autowired
    MedicineService service;

    @Autowired
    MedicineRepository repository;

    @Test
    void testGetAllMedicine() {
        List<Medicine> allMedicine = service.getAllMedicine();
        assertEquals(2, allMedicine.size());
        assertEquals("Ibuprofen", allMedicine.get(0).getName());
        assertEquals(7.5, allMedicine.get(1).getPrice());
    }

    @Test
    void testGetMedicineById_Success() {
        Medicine ibuprofen = service.getMedicineById(1L);
        assertNotNull(ibuprofen);
        assertEquals("Ibuprofen", ibuprofen.getName());
    }

    @Test
    void testGetMedicineById_Fail() {
        service.getAllMedicine().forEach(System.out::println);
        assertThrows(
                NoSuchElementException.class,
                () -> service.getMedicineById(3L)
        );
    }

    @Test
    void testCreateMedicine_Success() {
        Medicine newMedicine = new Medicine(
                3L,
                "Paracetamol",
                15,
                8.0,
                "Paracetamol"
        );

        service.createMedicine(newMedicine);

        Optional<Medicine> justPersisted = repository.findById(3L);
        assertTrue(justPersisted.isPresent());
        assertEquals(newMedicine.getName(), justPersisted.get().getName());
    }

    @Test
    void testCreateMedicine_Fail() {
        Medicine newMedicine = new Medicine(
                2L,
                "Paracetamol",
                15,
                8.0,
                "Paracetamol"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createMedicine(newMedicine)
        );
    }

    @Test
    void updateMedicine_Success() {
        Medicine updatedMedicine = new Medicine(
                1L,
                "Paracetamol",
                50,
                3.0,
                null
        );

        service.updateMedicine(updatedMedicine);
        Optional<Medicine> updateMedicine = repository.findById(1L);
        assertTrue(updateMedicine.isPresent());
        assertEquals(updatedMedicine.getName(), repository.findById(1L).get().getName());
        assertEquals(updatedMedicine.getQuantity(), repository.findById(1L).get().getQuantity());
        assertEquals(updatedMedicine.getPrice(), repository.findById(1L).get().getPrice());

    }

    @Test
    void updateMedicine_Fail(){
        Medicine updatedMedicine = new Medicine(
                3L,
                "Paracetamol",
                50,
                3.0,
                null
        );

        assertThrows(
                NoSuchElementException.class,
                () -> service.updateMedicine(updatedMedicine)
        );
    }

    @Test
    void testDeleteMedicineById_Success() {
        service.deleteMedicineById(1L);
        assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    void testDeleteMedicineById_Exception() {
        assertThrows(
                NoSuchElementException.class,
                () -> service.deleteMedicineById(3L)
        );
    }
}