/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import DAO.LoginDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import views.LoginView;
import models.User;
import views.Dashboard;
import views.StudentView;
import views.TeacherView;


public class LoginController {

    private LoginView view;
    private Dashboard dbView;
    private List<User> dataList = new ArrayList<>();
    /**
     * The Log dao.
     */
    LoginDAO logDAO = new LoginDAO(); // Assuming LoginDAO constructor exists

    /**
     * Instantiates a new Login controller.
     */
    public LoginController() {

    }

    /**
     * Instantiates a new Login controller.
     *
     * @param view the view
     */
    public LoginController(LoginView view) {
        this.view = view;
    }

    /**
     * Instantiates a new Login controller.
     *
     * @param dbView the db view
     */
    public LoginController(Dashboard dbView) {
        this.dbView = dbView;
    }

    /**
     * Instantiates a new Login controller.
     *
     * @param view   the view
     * @param logDAO the log dao
     */
    public LoginController(LoginView view, LoginDAO logDAO) {
        this.view = view;
        this.logDAO = logDAO;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        int max = logDAO.getMax();
        return max;
    }

    /**
     * Show new data.
     */
    public void showNewData() {
        dataList = LoginDAO.select();
        showTable();
    }

    /**
     * Show table.
     */
    public void showTable() {
        DefaultTableModel tableModel = dbView.getTableAccount();
        tableModel.setRowCount(0);

        for (User user : dataList) {
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getUser_id(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
            });
        }
    }

    /**
     * Save user.
     */
    public void saveUser() {
        int user_id = Integer.parseInt(dbView.getUserId());
        String username = dbView.getUserName();
        String password = dbView.getPassword();
        int role = Integer.parseInt(dbView.getRole());

        User user = new User();
        user.setUser_id(user_id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        LoginDAO.insert(user);
        dbView.clearUser();
        showNewData();
    }

    /**
     * Update user.
     */
    public void updateUser() {
        int acc_id = Integer.parseInt(dbView.getAccId());

        int user_id = Integer.parseInt(dbView.getUserId());
        String username = dbView.getUserName();
        String password = dbView.getPassword();
        int role = Integer.parseInt(dbView.getRole());

        User user = new User();
        user.setId(acc_id);
        user.setUser_id(user_id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        LoginDAO.update(user);
        dbView.clearUser();
        showNewData();

    }

    /**
     * Delete user.
     */
    public void deleteUser() {
        int acc_id = Integer.parseInt(dbView.getAccId());
        if (logDAO.isIDExits(acc_id)) {
            int yesOrNo = dbView.showConfirmDeleteDialog("Course and score records will also be deleted", "Teacher Delete");
            if (yesOrNo == dbView.OK_Option()) {
                LoginDAO.delete(acc_id);
            }
            showNewData();
            dbView.clearUser();
        } else {
            dbView.showMessage("Teacher id doesn't exists");
        }
    }

    /**
     * Search user.
     */
    public void searchUser() {
        String searchTxt = dbView.getSearchUser();
        dataList = LoginDAO.search(searchTxt);
        showTable();
    }

    /**
     * Login.
     */
    public void login() {

        String username = view.getUsername();
        String password = view.getPassword();

        User user = LoginDAO.getUserByUserName(username);

        if (user != null && user.getPassword().equals(password)) {
            switch (user.getRole()) {
                // Perform student-specific actions
                case 1:
                    view.showMessage("Login Successfull By Teacher Account!");
                    TeacherView teacherView = new TeacherView();
                    teacherView.setVisible(true);
                    view.dispose();
                    break;
                case 2:
                    int user_id = user.getUser_id();
                    view.showMessage("Login Successfull By Student Account!");
                    StudentView studentView = new StudentView();
                    studentView.setUserId(user_id);
                    studentView.setVisible(true);
                    view.dispose();
                    break;
                case 3:
                    view.showMessage("Login Successfull By Admin Account!");
                    Dashboard dash = new Dashboard();
                    dash.setVisible(true);
                    view.dispose();
                    break;
                default:
                    // Invalid role
                    view.showErrorMessage("Invalid role!");
                    break;
            }
        } else {
            // Invalid username or password
            view.showErrorMessage("Invalid username or password!");
        }
    }
}
