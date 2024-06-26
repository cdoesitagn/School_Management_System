/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.Students;
import DAO.StudentsDAO;
import views.TeacherView;


public class TeacherController {

    private TeacherView view;
    private List<Students> dataList = new ArrayList<>();
    /**
     * The Std.
     */
    StudentsDAO std = new StudentsDAO();

    /**
     * Instantiates a new Teacher controller.
     */
    public TeacherController() {
    }

    /**
     * Instantiates a new Teacher controller.
     *
     * @param view the view
     */
    public TeacherController(TeacherView view) {
        this.view = view;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        int max = std.getMax();
        return max;
    }

    /**
     * Show new data.
     */
    public void showNewData() {
        dataList = StudentsDAO.select();
        showTable();
    }

    /**
     * Show table.
     */
    public void showTable() {
        DefaultTableModel tableModel = view.getTable();
        tableModel.setRowCount(0);

        for (Students students : dataList) {
            tableModel.addRow(new Object[]{
                students.getStudent_id(),
                students.getFullname(),
                students.getBirthday(),
                students.getGender(),
                students.getEmail(),
                students.getPhoneNumber(),
                students.getAddress(),
                students.getImage_path()
            });
        }
    }

    /**
     * Save student.
     */
    public void saveStudent() {
        String fullname = view.getFullName();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(view.getBirthDate());
        String gender = view.getGender();
        String email = view.getEmail();
        String phoneNumber = view.getPhoneNumber();
        String address = view.getAddress();
        String image_path = view.getJLabelImagePath();

        Students student = new Students();
        student.setFullname(fullname);
        student.setEmail(email);
        student.setBirthday(date);
        student.setPhoneNumber(phoneNumber);
        student.setGender(gender);
        student.setAddress(address);
        student.setImage_path(image_path);

        StudentsDAO.insert(student);
        view.clearStudent();
        showNewData();
    }

    /**
     * Update student.
     */
    public void updateStudent() {
        int student_id = Integer.parseInt(view.getID());
        if (std.isIDExits(student_id)) {
            if (!view.checkPhoneEmailUpdate()) {
                String fullname = view.getFullName();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(view.getBirthDate());
                String gender = view.getGender();
                String email = view.getEmail();
                String phoneNumber = view.getPhoneNumber();
                String address = view.getAddress();
                String image_path = view.getJLabelImagePath();

                Students student = new Students();
                student.setStudent_id(student_id);
                student.setFullname(fullname);
                student.setEmail(email);
                student.setBirthday(date);
                student.setPhoneNumber(phoneNumber);
                student.setGender(gender);
                student.setAddress(address);
                student.setImage_path(image_path);

                StudentsDAO.update(student);
                view.clearStudent();
                showNewData();
            }
        } else {
            view.showMessage("Student id doesn't exists");
        }
    }

    /**
     * Delete student.
     */
    public void deleteStudent() {
        int student_id = Integer.parseInt(view.getID());
        if (std.isIDExits(student_id)) {
            int yesOrNo = view.showConfirmDeleteDialog("Course and score records will also be deleted", "Student Delete");
            if (yesOrNo == view.OK_Option()) {
                StudentsDAO.delete(student_id);
            }
            showNewData();
            view.clearStudent();
        } else {
            view.showMessage("Student id doesn't exists");
        }
    }

    /**
     * Search student.
     */
    public void searchStudent() {
        String searchTxt = view.getSearchField();
        dataList = StudentsDAO.search(searchTxt);
        showTable();
    }
}
