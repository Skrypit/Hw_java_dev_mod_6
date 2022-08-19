package service.customers;

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

    public void create(Customer customer) throws SQLException {
        createSt.setString(1, customer.getFirstName());
        createSt.setString(2, customer.getLastName());
        createSt.setString(3, customer.getEmail());

        createSt.executeUpdate();
    }

    public void update(Customer customer) throws SQLException {

        updateSt.setString(1, customer.getFirstName());
        updateSt.setString(2, customer.getLastName());
        updateSt.setString(3, customer.getEmail());
        updateSt.setLong(4, customer.getId());

        updateSt.executeUpdate();
    }

    public boolean removeTheCustomer(Customer customer) {
        try {
            deleteSt.setString(1, customer.getFirstName());
            deleteSt.setString(2, customer.getLastName());
            return deleteSt.executeUpdate() == 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Customer selectCustomerByName(Customer customer) throws SQLException {

        selectByNameSt.setString(1, customer.getFirstName());
        selectByNameSt.setString(2, customer.getLastName());

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            do {
                Customer result = new Customer();
                result.setId(rs.getLong("customer_id"));
                result.setFirstName(rs.getString("first_name"));
                result.setLastName(rs.getString("last_name"));
                result.setEmail(rs.getString("email"));

                return result;
            } while (rs.next());
        }
    }

}

