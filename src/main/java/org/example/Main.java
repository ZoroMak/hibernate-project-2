package org.example;

import org.example.dao.*;
import org.example.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FeatureDAO featureDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RatingDAO ratingDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;



    private final SessionFactory sessionFactory;
    public Main(){
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "M04_ZOR_alex");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        this.actorDAO = new ActorDAO(sessionFactory);
        this.addressDAO = new AddressDAO(sessionFactory);
        this.categoryDAO = new CategoryDAO(sessionFactory);
        this.cityDAO = new CityDAO(sessionFactory);
        this.countryDAO = new CountryDAO(sessionFactory);
        this.customerDAO = new CustomerDAO(sessionFactory);
        this.featureDAO = new FeatureDAO(sessionFactory);
        this.filmDAO = new FilmDAO(sessionFactory);
        this.filmTextDAO = new FilmTextDAO(sessionFactory);
        this.inventoryDAO = new InventoryDAO(sessionFactory);
        this.languageDAO = new LanguageDAO(sessionFactory);
        this.paymentDAO = new PaymentDAO(sessionFactory);
        this.ratingDAO = new RatingDAO(sessionFactory);
        this.rentalDAO = new RentalDAO(sessionFactory);
        this.staffDAO = new StaffDAO(sessionFactory);
        this.storeDAO = new StoreDAO(sessionFactory);

    }

    public static void main(String[] args) {
        Main main = new Main();
        Customer customer = main.createCustomer();

        main.createNewFilm();
        
        //main.customerRentFilm(customer);

        //main.customerReturnInventoryToStore();
    }

    private void createNewFilm() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Language language = languageDAO.getByName("English");

            Film film = new Film();
            film.setRating(Rating.NC17);
            film.setSpecialFeatures(Set.of(Feature.Trailers));
            film.setLength((short) 120);
            film.setDescription("my first film");
            film.setTitle("Super film");
            film.setYear(Year.now());
            film.setLanguage(language);
            film.setRentalDuration((byte) 4);
            film.setRentalRace(BigDecimal.valueOf(4.5));
            film.setReplacementCost(BigDecimal.valueOf(19.99));

            //Выбрать категории фильма так как метод преобразует строки в сеты
            //вводим просто строку
            //Action, Comedy, Horror
            Set<String> categories = new HashSet<>();
            categories.add("Action");
            categories.add("Comedy");
            categories.add("Horror");
            film.setCategories(categoryDAO.getSetByName(categories));

//            System.out.println(film.getCategories().stream().map(Category::getName).collect(Collectors.joining(" ")));

            //Выбрать актеров которые будут играть в нашем фильме
            //Дальше создаем Set и добавляем туда выбраннных актеров
            //С помощью запроса найдем актеров в таблице
            Set<String> actors = new HashSet<>();
            actors.add("WALTER TORN");
            actors.add("GINA DEGENERES");
            actors.add("MATTHEW CARREY");
            actors.add("MARY KEITEL");
            film.setActors(actorDAO.getSetByName(actors));

            //System.out.println(film.getActors().stream().map(Actor::getFirstName).collect(Collectors.joining(" ")));

            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setFilm(film);
            filmText.setId(film.getId());
            filmText.setDescription("my first film");
            filmText.setTitle("Super film");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        }
    }

    private void customerRentFilm(Customer customer) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Inventory inventory = inventoryDAO.getFilmForRental();

            System.out.println(inventory.getId());

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setInventory(inventory);
            rental.setCustomer(customer);
            rental.setStaff(customer.getStore().getManager());

            rentalDAO.save(rental);


            session.getTransaction().commit();
        }catch (Exception e){
            System.out.println("Упс что-то не так");
        }
    }

    private Customer createCustomer(){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Store store = storeDAO.getItems(0, 1).getFirst();

            City city = cityDAO.getByName("Kragujevac");

            Address address = new Address();
            address.setAddress("Indep str, 48");
            address.setDistrict("Super");
            address.setCity(city);
            address.setPhone("999-111-222-444");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setActive(true);
            customer.setEmail("zorov.ma@mail.ru");
            customer.setAddress(address);
            customer.setStore(store);
            customer.setFirstName("Maxim");
            customer.setLastName("Zorov");

            customerDAO.save(customer);

            session.getTransaction().commit();

            return customer;
        }

    }

    private void customerReturnInventoryToStore(){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Rental rental = rentalDAO.getAnyUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());

            rentalDAO.save(rental);

            System.out.println(rental.getId());

            session.getTransaction().commit();
        }
    }
}