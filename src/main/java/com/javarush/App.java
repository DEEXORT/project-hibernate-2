import config.SessionCreator;
import dao.*;
import entity.*;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import services.CustomerService;
import services.RepositoryImpl;
import services.StoreService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
public class App {
    private static SessionCreator sessionCreator;
    private static RepositoryImpl<Customer> customerRepository;
    private static AddressRepository addressRepository;
    private static CityRepository cityRepository;
    private static StoreRepository storeRepository;
    private static RentalRepository rentalRepository;
    private static StaffRepository staffRepository;
    private static PaymentRepository paymentRepository;
    private static CustomerService customerService;
    private static StoreService storeService;


    public static void main(String[] args) {
        sessionCreator = new SessionCreator();
        customerRepository = new RepositoryImpl<>(sessionCreator, Customer.class);
        addressRepository = new AddressRepository(sessionCreator, Address.class);
        cityRepository = new CityRepository(sessionCreator, City.class);
        storeRepository = new StoreRepository(sessionCreator, Store.class);
        rentalRepository = new RentalRepository(sessionCreator, Rental.class);
        staffRepository = new StaffRepository(sessionCreator, Staff.class);
        paymentRepository = new PaymentRepository(sessionCreator, Payment.class);

        customerService = new CustomerService(sessionCreator, customerRepository,
                addressRepository, cityRepository, storeRepository, rentalRepository);
        storeService = new StoreService(sessionCreator, storeRepository, rentalRepository, staffRepository, paymentRepository);

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
                .store(storeRepository.findById(1L))
                .build();

        exampleSaveCustomer(customer);
        Rental rental = exampleRentalFilm(customer);
        exampleReturnFilm(rental);
    }

    public static Customer exampleSaveCustomer(Customer customer) {
        customerService.save(customer);
        log.info("Customer saved : {}", customer);
        return customer;
    }

    public static Rental exampleRentalFilm(Customer customer) {
        // Going to the store and contacting a free store staff
        Store store = storeRepository.findById(1L);

        // Getting inventory from store with available film for rent
        Inventory inventory = rentalRepository.getAnyInventoryWithAvailableFilm(store);
        BigDecimal price = BigDecimal.valueOf(2.99);
        log.info("Get available inventory: {}", inventory);

        // Contacting a free store staff and rental the inventory with payment
        Rental rental = storeService.rentalFilm(customer, inventory, store, price);
        log.info("Rental saved : {}", rental);
        return rental;
    }

    public static void exampleReturnFilm(Rental rental) {
//        Rental anyUnreturnedFilm = rentalRepository.getAnyUnreturnedFilm();

        storeService.returnRentedFilm(rental);
        log.info("Rental returned : {}", rental);
    }
}
