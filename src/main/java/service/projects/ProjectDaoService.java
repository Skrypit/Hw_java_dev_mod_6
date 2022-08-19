package service.projects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDaoService {

    final private PreparedStatement createSt;
    final private PreparedStatement updateSt;
    final private PreparedStatement deleteSt;
    final private PreparedStatement selectByNameSt;
    final private PreparedStatement showListOfProjectsSt;
    final private PreparedStatement showSumOfAllSalariesSt;

    public ProjectDaoService(Connection connection) throws SQLException {
        createSt = connection.prepareStatement(
                "INSERT INTO projects(project_name, type, description) VALUES (?,?,?)");
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

    public void create(Project project) throws SQLException {
        createSt.setString(1, project.getProjectName());
        createSt.setString(2, project.getType());
        createSt.setString(3, project.getDescription());

        createSt.executeUpdate();
    }
    public void update(Project project) throws SQLException {

        updateSt.setString(1, project.getProjectName());
        updateSt.setString(2, project.getType());
        updateSt.setString(3, project.getDescription());
        updateSt.setLong(4, project.getId());

        updateSt.executeUpdate();
    }

    public boolean removeTheProject(Project project) {
        try {
            deleteSt.setString(1, project.getProjectName());

            return deleteSt.executeUpdate() == 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Project selectProjectByName(Project project) throws SQLException {

        selectByNameSt.setString(1, project.getProjectName());

        try (ResultSet rs = selectByNameSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            do {
                Project result = new Project();
                result.setId(rs.getLong("project_id"));
                result.setType(rs.getString("type"));
                result.setDescription(rs.getString("description"));

                return result;

            } while (rs.next());
        }
    }

    public boolean showSumOfAllSalaries(String projectName) {
        try {
            showSumOfAllSalariesSt.setString(1, projectName);

        } catch (Exception ex) {
            return false;
        }

        try (ResultSet rs = showSumOfAllSalariesSt.executeQuery()) {
            if (!rs.next()) {
                System.out.println("Project with name " + projectName + " are not found.");
                return false;
            }

            String totalCost = rs.getString("project_cost");
            System.out.println("The total sum of salaries on this project = " + totalCost);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean showListOfProjects() throws SQLException {
        try (ResultSet rs = showListOfProjectsSt.executeQuery()) {
            if (!rs.next()) {
                System.out.println("Information not found.");
                return false;
            }
            do {
                String creationDate = rs.getString("creation_date");
                String projectName = rs.getString("project_name");
                String totalDevelopers = rs.getString("total_developers");

                System.out.println(creationDate + " " + projectName + " " + totalDevelopers + " developers"
                );

            } while (rs.next());
            return true;
        }
    }

}

