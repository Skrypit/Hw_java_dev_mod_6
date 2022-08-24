package model.customers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoService {
    private final PreparedStatement createSt;
    final private PreparedStatement updateSt;
    final private PreparedStatement deleteSt;
    final private PreparedStatement selectByNameSt;

    public CustomerDaoService(Connection connection) throws SQLException {
        createSt = connection.prepareStatement(
                "INSERT INTO customers(first_name, last_name, email) VALUES (?,?,?)");

        updateSt = connection.prepareStatement(
                "UPDATE customers SET first_name = ?, last_name = ?, email = ? WHERE customer_id = ?");

        deleteSt = connection.prepareStatement(
                "DELETE FROM customers WHERE first_name LIKE ? AND last_name LIKE ?");
        selectByNameSt = connection.prepareStatement(
                "SELECT* FROM customers WHERE first_name LIKE ? AND last_name LIKE ?");
    }

    public String create(Customer customer) throws SQLException {
        String result;
        try {
            createSt.setString(1, customer.getFirstName());
            createSt.setString(2, customer.getLastName());
            createSt.setString(3, customer.getEmail());

            createSt.executeUpdate();

            result = "Customer created!";
        } catch (Exception ex) {
            result = "Customer " + customer.getFirstName() + " isn't created!";
        }
        return result;
    }

    public String update(Customer customer) throws SQLException {
        String result;
        try {
        updateSt.setString(1, customer.getFirstName());
        updateSt.setString(2, customer.getLastName());
        updateSt.setString(3, customer.getEmail());
        updateSt.setLong(4, customer.getId());

        updateSt.executeUpdate();
        result = "Customer updated!";

        } catch (Exception ex) {
            result = "Customer " + customer.getFirstName() + " " + customer.getFirstName() +  " isn't updated!";
        }
        return result;
    }

    public String removeTheCustomer(String firstName, String lastName) {
        String result;
        try {
            deleteSt.setString(1, firstName);
            deleteSt.setString(2, lastName);
            deleteSt.executeUpdate();
            result = "Customer " + firstName + " " + lastName + " deleted!";

        } catch (Exception ex) {
            result = "Customer " + firstName + " " + lastName +" isn't deleted!";
        }
        return result;
    }

    public Customer selectCustomerByName(String firstName, String lastName) throws SQLException {

        selectByNameSt.setString(1, firstName);
        selectByNameSt.setString(2, lastName);

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Customer result = new Customer();
            result.setId(rs.getLong("customer_id"));
            result.setFirstName(rs.getString("first_name"));
            result.setLastName(rs.getString("last_name"));
            result.setEmail(rs.getString("email"));

            return result;
        }
    }

}

