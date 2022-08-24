package model.developers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeveloperDaoService {

    final private PreparedStatement createSt;
    final private PreparedStatement updateSt;
    final private PreparedStatement deleteSt;
    final private PreparedStatement selectByNameSt;
    final private PreparedStatement selectByLanguageSt;
    final private PreparedStatement selectByLevelSt;
    final private PreparedStatement selectByProjectSt;

    public DeveloperDaoService(Connection connection) throws SQLException {
        createSt = connection.prepareStatement(
                "INSERT INTO developers(first_name, last_name, age, sex) VALUES (?,?,?,?)");
        updateSt = connection.prepareStatement(
                "UPDATE developers SET first_name = ?, last_name = ?, age = ?, sex = ? WHERE developer_id = ?");

        deleteSt = connection.prepareStatement("DELETE FROM developers WHERE first_name LIKE ? AND last_name LIKE ?");

        selectByNameSt = connection.prepareStatement("SELECT* FROM developers WHERE first_name LIKE ? AND last_name LIKE ?");

        selectByLanguageSt = connection.prepareStatement("SELECT* FROM developers " +
                "JOIN developer_skills ON developers.developer_id = developer_skills.developer_id " +
                "JOIN skills ON skills.skill_id = developer_skills.skill_id " +
                "WHERE skills.language LIKE ?");
        selectByLevelSt = connection.prepareStatement("SELECT* " +
                "FROM developers " +
                "JOIN developer_skills ON developers.developer_id = developer_skills.developer_id " +
                "JOIN skills ON skills.skill_id = developer_skills.skill_id " +
                " WHERE skills.level LIKE ?");
        selectByProjectSt = connection.prepareStatement("SELECT* " +
                "FROM developers " +
                "JOIN developers_projects ON developers.developer_id = developers_projects.developer_id " +
                "JOIN projects ON projects.project_id = developers_projects.project_id " +
                "WHERE projects.project_name LIKE ?");
    }

    public String create(Developer developer) throws SQLException {
        String result;
        try {
            createSt.setString(1, developer.getFirstName());
            createSt.setString(2, developer.getLastName());
            createSt.setInt(3, developer.getAge());
            createSt.setString(4, developer.getSex().name());

            createSt.executeUpdate();

            result = "Developer created!";
        } catch (Exception ex) {
            result = "Developer " + developer.getLastName() + " isn't created!";
        }
        return result;
    }


    public String update(Developer developer) throws SQLException {
        String result;
        try {
            updateSt.setString(1, developer.getFirstName());
            updateSt.setString(2, developer.getLastName());
            updateSt.setInt(3, developer.getAge());
            updateSt.setString(4, developer.getSex().name());
            updateSt.setLong(5, developer.getId());

            updateSt.executeUpdate();
            result = "Developer updated!";

        } catch (Exception ex) {
            result = "Developer " + developer.getLastName() + " isn't updated!";
        }
        return result;
    }

    public String removeTheDeveloper(String firstName, String lastName) {
        String result;
        try {
            deleteSt.setString(1, firstName);
            deleteSt.setString(2, lastName);
            deleteSt.executeUpdate();
            result = "Developer deleted!";

        } catch (Exception ex) {
            result = "Developer " + firstName + " " + lastName + " isn't deleted!";
        }
        return result;
    }

    public Developer selectDevelopersByName(String firstName, String lastName) throws SQLException {

        selectByNameSt.setString(1, firstName);
        selectByNameSt.setString(2, lastName);

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            do {
                Developer result = new Developer();
                result.setId(rs.getLong("developer_id"));
                result.setFirstName(rs.getString("first_name"));
                result.setLastName(rs.getString("last_name"));
                result.setAge(Integer.parseInt(rs.getString("age")));
                result.setSex(Developer.Sex.valueOf(rs.getString("sex")));

                return result;
            } while (rs.next());
        }
    }

    public List<Developer> selectDevelopersByLanguage(String language) throws SQLException {

        selectByLanguageSt.setString(1, language);

        try (ResultSet rs = selectByLanguageSt.executeQuery()) {

            List<Developer> result = new ArrayList<>();

            while (rs.next()) {
                Developer developer = new Developer();
                developer.setId(rs.getLong("developer_id"));
                developer.setFirstName(rs.getString("first_name"));
                developer.setLastName(rs.getString("last_name"));
                developer.setAge(Integer.parseInt(rs.getString("age")));
                developer.setSex(Developer.Sex.valueOf(rs.getString("sex")));

                result.add(developer);
            }
            return result;
        }
    }

    public List<Developer> selectDevelopersByLevel(String level) throws SQLException {

        selectByLevelSt.setString(1, level);

        List<Developer> result = new ArrayList<>();

        ResultSet rs = selectByLevelSt.executeQuery();

        while (rs.next()) {
            Developer developer = new Developer();
            developer.setId(rs.getLong("developer_id"));
            developer.setFirstName(rs.getString("first_name"));
            developer.setLastName(rs.getString("last_name"));
            developer.setAge(Integer.parseInt(rs.getString("age")));
            developer.setSex(Developer.Sex.valueOf(rs.getString("sex")));
            result.add(developer);
        }
        return result;
    }

    public List<Developer> selectDeveloperByProject(String projectName) throws SQLException {

        selectByProjectSt.setString(1, projectName);

        List<Developer> result = new ArrayList<>();

        ResultSet rs = selectByProjectSt.executeQuery();
        while (rs.next()) {
            Developer developer = new Developer();
            developer.setId(rs.getLong("developer_id"));
            developer.setFirstName(rs.getString("first_name"));
            developer.setLastName(rs.getString("last_name"));
            developer.setAge(Integer.parseInt(rs.getString("age")));
            developer.setSex(Developer.Sex.valueOf(rs.getString("sex")));
            result.add(developer);
        }
        return result;
    }
}


/*SELECT*
                FROM developers
                JOIN developers_projects ON developers.developer_id = developers_projects.developer_id
                JOIN projects ON projects.project_id = developers_projects.project_id
                WHERE projects.project_name ='Roses to all'

SELECT* FROM developers
                JOIN developer_skills ON developers.developer_id = developer_skills.developer_id
                JOIN skills ON skills.skill_id = developer_skills.skill_id
                WHERE skills.language = 'Java'

SELECT*
                FROM developers
                JOIN developer_skills ON developers.developer_id = developer_skills.developer_id
                JOIN skills ON skills.skill_id = developer_skills.skill_id
                 WHERE skills.level = 'Middle'
                */