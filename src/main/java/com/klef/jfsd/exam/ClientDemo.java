package com.klef.jfsd.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class ClientDemo {

    public static void main(String[] args) {

        // Set up the Hibernate session factory
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .buildSessionFactory();

        // Create a session
        Session session = factory.getCurrentSession();

        try {
            // Insert a customer into the database
            Customer customer = new Customer("Pardhu", "pardhumadhu12@gmail.com", 19, "Bangalore");

            // Begin a transaction
            session.beginTransaction();

            // Save the customer object
            session.save(customer);

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Customer saved with ID: " + customer.getId());

            // Query for customers aged greater than 25 using JPA Criteria API
            session = factory.getCurrentSession();
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);

            // Apply age restriction (greater than 25)
            query.select(root).where(builder.greaterThan(root.get("age"), 25));

            List<Customer> customers = session.createQuery(query).getResultList();

            System.out.println("Customers with age > 25:");
            for (Customer c : customers) {
                System.out.println(c.getName());
            }

            // Query for customers in 'New York' using JPA Criteria API
            session = factory.getCurrentSession();
            session.beginTransaction();

            query = builder.createQuery(Customer.class);
            root = query.from(Customer.class);

            // Apply location restriction (equal to 'New York')
            query.select(root).where(builder.equal(root.get("location"), "New York"));

            List<Customer> customersLocation = session.createQuery(query).getResultList();

            System.out.println("Customers in 'New York':");
            for (Customer c : customersLocation) {
                System.out.println(c.getName());
            }

            // Commit transaction
            session.getTransaction().commit();

        } finally {
            factory.close();
        }
    }
}
