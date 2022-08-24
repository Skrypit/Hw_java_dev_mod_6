package model.projects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoService {

    final private PreparedStatement createSt;
    final private PreparedStatement createDevProjectRelSt;
    final private PreparedStatement updateSt;
    final private PreparedStatement deleteSt;
    final private PreparedStatement selectByNameSt;
    final private PreparedStatement showListOfProjectsSt;
    final private PreparedStatement showSumOfAllSalariesSt;

    public ProjectDaoService(Connection connection) throws SQLException {
        createSt = connection.prepareStatement(
                "INSERT INTO projects(project_name, type, description, creation_date) VALUES (?,?,?,?)");
        createDevProjectRelSt = connection.prepareStatement(
                "INSERT INTO developers_projects(developer_id, project_id) VALUES (?,?)");
        updateSt = connection.prepareStatement(
                "UPDATE projects SET project_name = ?, type = ?, description = ? WHERE project_id = ?");
        deleteSt = connection.prepareStatement(
                "DELETE FROM projects WHERE project_name LIKE ? ");
        selectByNameSt = connection.prepareStatement(
                "SELECT* FROM projects WHERE project_name LIKE ? ");
        showListOfProjectsSt = connection.prepareStatement(
                "SELECT creation_date, project_name, SUM(developers.developer_id) AS total_developers " +
                        "FROM projects " +
                        "JOIN developers_projects ON developers_projects.project_id = projects.project_id " +
                        "JOIN developers ON developers.developer_id = developers_projects.developer_id " +
                        "GROUP BY projects.project_id");
        showSumOfAllSalariesSt = connection.prepareStatement(
                "SELECT projects.project_name, SUM(developers.salary) AS project_cost " +
                        "FROM projects " +
                        "JOIN developers_projects ON developers_projects.project_id = projects.project_id " +
                        "JOIN developers ON developers.developer_id = developers_projects.developer_id " +
                        "WHERE projects.project_name LIKE ?");
    }

    public String addDeveloper(long projectId, long developerId) {
        String result;
        try {
            createDevProjectRelSt.setLong(1, developerId);
            createDevProjectRelSt.setLong(2, projectId);
            createDevProjectRelSt.executeUpdate();

            result = "Developer added to this project!";

        } catch (SQLException ex) {
            result = "Warning! Developer isn't added!" + " " + developerId + " " + projectId;
            ex.printStackTrace();
        }
        return result;

    }

    public String create(Project project) throws SQLException {
        String result;
        try {
            createSt.setString(1, project.getProjectName());
            createSt.setString(2, project.getType());
            createSt.setString(3, project.getDescription());
            createSt.setString(4, project.getCreationDate().toString());

            createSt.executeUpdate();

            result = "Project created!";

        } catch (Exception ex) {
            result = "Project " + project.getProjectName() + " isn't created!";
        }
        return result;
    }

    public String update(Project project) throws SQLException {
        String result;
        try {
            updateSt.setString(1, project.getProjectName());
            updateSt.setString(2, project.getType());
            updateSt.setString(3, project.getDescription());
            updateSt.setLong(4, project.getId());

            updateSt.executeUpdate();
            result = "Project updated!";

        } catch (Exception ex) {
            result = "Project " + project.getProjectName() + " isn't updated!";
        }
        return result;

    }

    public String removeTheProject(String projectName) {
        String result;
        try {
            deleteSt.setString(1, projectName);
            deleteSt.executeUpdate();

            result = "Project deleted!";

        } catch (Exception ex) {
            result = "Project " + projectName + " isn't deleted!";
        }
        return result;
    }

    public Project selectProjectByName(String name) throws SQLException {

        selectByNameSt.setString(1, name);

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            do {
                Project result = new Project();
                result.setId(rs.getLong("project_id"));
                result.setProjectName(rs.getString("project_name"));
                result.setType(rs.getString("type"));
                result.setDescription(rs.getString("description"));

                return result;

            } while (rs.next());
        }
    }

    public String showSumOfAllSalaries(String projectName) {
        String result;
        try {
            showSumOfAllSalariesSt.setString(1, projectName);

        } catch (Exception ex) {
            return null;
        }

        try (ResultSet rs = showSumOfAllSalariesSt.executeQuery()) {
            if (!rs.next()) {
                result = "Project with name " + projectName + " are not found.";
                return result;
            }

            String totalCost = rs.getString("project_cost");
            result = totalCost;
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<ProjectFormat> showListOfProjects() throws SQLException {

        try (ResultSet rs = showListOfProjectsSt.executeQuery()) {

            List<ProjectFormat> result = new ArrayList<>();

            while (rs.next()) {
                ProjectFormat project = new ProjectFormat();

                project.setCreationDate(LocalDate.parse(rs.getString("creation_date")));
                project.setProjectName(rs.getString("project_name"));
                project.setTotalDevelopers(rs.getInt("total_developers"));

                result.add(project);
            }
            return result;
        }
    }
}

