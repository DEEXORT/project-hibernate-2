package com.javarush;

import com.javarush.config.SessionCreator;
import com.javarush.dao.AddressDao;
import com.javarush.dao.CityDao;
import com.javarush.dao.CustomerDao;
import com.javarush.dao.PaymentDao;
import com.javarush.dao.RentalDao;
import com.javarush.dao.StaffDao;
import com.javarush.dao.StoreDao;
import com.javarush.entity.Customer;
import com.javarush.entity.Address;
import com.javarush.entity.City;
import com.javarush.entity.Store;
import com.javarush.entity.Rental;
import com.javarush.entity.Staff;
import com.javarush.entity.Payment;
import com.javarush.entity.Country;
import com.javarush.entity.Inventory;
import com.javarush.services.CustomerService;
import com.javarush.services.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
public class App {
    private static SessionCreator sessionCreator;
    private static CustomerDao customerDao;
    private static AddressDao addressDao;
    private static CityDao cityDao;
    private static StoreDao storeDao;
    private static RentalDao rentalDao;
    private static StaffDao staffDao;
    private static PaymentDao paymentDao;
    private static CustomerService customerService;
    private static StoreService storeService;


    public static void main(String[] args) {
        sessionCreator = new SessionCreator();
        customerDao = new CustomerDao(sessionCreator, Customer.class);
        addressDao = new AddressDao(sessionCreator, Address.class);
        cityDao = new CityDao(sessionCreator, City.class);
        storeDao = new StoreDao(sessionCreator, Store.class);
        rentalDao = new RentalDao(sessionCreator, Rental.class);
        staffDao = new StaffDao(sessionCreator, Staff.class);
        paymentDao = new PaymentDao(sessionCreator, Payment.class);

        customerService = new CustomerService(sessionCreator, customerDao,
                addressDao, cityDao, storeDao);
        storeService = new StoreService(rentalDao, staffDao, paymentDao);

        // Initial data
        Country country = Country.builder().country("Java Country").build();
        City city = City.builder().city("Hibernate City").country(country).build();
        Address address = Address.builder()
                .address("Cascade Avenue")
                .city(city)
                .phone("777-777")
                .location(new GeometryFactory().createPoint(new Coordinate(56.8, 12.41)))
                .postalCode("12345")
                .address2("address2")
                .district("district")
                .build();
        Customer customer = Customer.builder()
                .active(true)
                .email("test@mail.ru")
                .firstName("Test3")
                .lastName("Test2")
                .address(address)
                .createDate(LocalDateTime.now())
                .store(storeDao.findById(1L))
                .build();

        exampleSaveCustomer(customer);
        Rental rental = exampleRentalFilm(customer);
        exampleReturnFilm(rental);
    }

    public static void exampleSaveCustomer(Customer customer) {
        customerService.save(customer);
        log.info("Customer saved : {}", customer);
    }

    public static Rental exampleRentalFilm(Customer customer) {
        // Going to the store and contacting a free store staff
        Store store = storeDao.findById(1L);

        // Getting inventory from store with available film for rent
        Inventory inventory = rentalDao.getAnyInventoryWithAvailableFilm(store);
        BigDecimal price = BigDecimal.valueOf(2.99);
        log.info("Get available inventory with ID = {}", inventory.getId());

        // Contacting a free store staff and rental the inventory with payment
        Rental rental = storeService.rentalFilm(customer, inventory, store, price);
        log.info("Rental saved with ID = {}", rental.getId());
        return rental;
    }

    public static void exampleReturnFilm(Rental rental) {
        storeService.returnRentedFilm(rental);
        log.info("Rental with ID = {} is returned {}", rental.getId(), rental.getReturnDate());
    }
}
