package service.companies;

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

    public void create(Company company) throws SQLException {
        createSt.setString(1, company.getCompanyName());
        createSt.setString(2, company.getResidence());

        createSt.executeUpdate();
    }
    public void update(Company company) throws SQLException {

        updateSt.setString(1, company.getCompanyName());
        updateSt.setString(2, company.getResidence());
        updateSt.setLong(3, company.getId());

        updateSt.executeUpdate();
    }


    public boolean removeByCompanyName(Company company) {
        try {

            deleteSt.setString(1,company.getCompanyName());
            return deleteSt.executeUpdate() == 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Company selectCompanyByName(Company company) throws SQLException {

        selectByNameSt.setString(1, company.getCompanyName());

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            do {
                Company result = new Company();
                result.setId(rs.getLong("company_id"));
                result.setCompanyName(rs.getString("company_name"));
                result.setResidence(rs.getString("residence"));

                return result;
            } while (rs.next());
        }
    }
}
