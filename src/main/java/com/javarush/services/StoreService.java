package com.javarush;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
public class StoreService {
    private final SessionCreator sessionCreator;
    private final StoreRepository storeRepository;
    private final RentalRepository rentalRepository;
    private final StaffRepository staffRepository;
    private final PaymentRepository paymentRepository;

    public void returnRentedFilm(Rental rental) {
        if (rental.getReturnDate() == null) {
            rental.setReturnDate(LocalDateTime.now());
            rentalRepository.update(rental);
        } else {
            throw new RuntimeException("Rental has already been returned");
        }
    }

    private Staff getAnyFreeStaff(Store store) {
        return staffRepository.getAnyStaffFromStore(store.getId());
    }

    public Rental rentalFilm(Customer customer, Inventory inventory, Store store, BigDecimal amount) {
        Staff staff = getAnyFreeStaff(store);
        Rental rental = Rental.builder()
                .inventory(inventory)
                .customer(customer)
                .rentalDate(LocalDateTime.now())
                .staff(staff)
                .build();
        Payment payment = Payment.builder()
                .customer(customer)
                .staff(staff)
                .rental(rental)
                .amount(amount)
                .paymentDate(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);
        return rental;
    }
}
