package model.companies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyDaoService {
    private final PreparedStatement createSt;
    final private PreparedStatement updateSt;
    final private PreparedStatement deleteSt;
    final private PreparedStatement selectByNameSt;

    public CompanyDaoService(Connection connection) throws SQLException {
       createSt = connection.prepareStatement(
                "INSERT INTO companies(company_name, residence) VALUES (?,?)");
        updateSt = connection.prepareStatement(
                "UPDATE companies SET company_name = ?, residence = ? WHERE company_id = ?");

        deleteSt = connection.prepareStatement("DELETE FROM companies WHERE company_name LIKE ?");
        selectByNameSt = connection.prepareStatement("SELECT* FROM companies WHERE company_name LIKE ?");
    }


    public String create(Company company) throws SQLException {
        createSt.setString(1, company.getCompanyName());
        createSt.setString(2, company.getResidence());

        createSt.executeUpdate();

        String result = "Company created!";
        return result;
    }

    public String update(Company company) throws SQLException {
        String result;
        try {
        updateSt.setString(1, company.getCompanyName());
        updateSt.setString(2, company.getResidence());
        updateSt.setLong(3, company.getId());

        updateSt.executeUpdate();
            result = "Company updated!";

        } catch (Exception ex) {
            result = "Project " + company.getCompanyName() + " isn't updated!";
        }
        return result;
    }


    public String removeByCompanyName(String name) {
        String result;
        try {
            deleteSt.setString(1, name);
            deleteSt.executeUpdate();

            result = "Company deleted!";

        } catch (Exception ex) {
            result = "Company " + name + " isn't deleted!";
        }
        return result;
    }


    public Company selectCompanyByName(String name) throws SQLException {

        selectByNameSt.setString(1, name);

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }

            Company result = new Company();
            result.setId(rs.getLong("company_id"));
            result.setCompanyName(rs.getString("company_name"));
            result.setResidence(rs.getString("residence"));

            return result;
        }
    }

}
