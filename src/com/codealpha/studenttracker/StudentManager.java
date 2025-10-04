package com.codealpha.studenttracker;

import java.io.*;
import java.util.*;

public class StudentManager {
    private List<Student> students = new ArrayList<>();
    private final String FILE_NAME = "students.txt";

    public StudentManager() {
        loadFromFile();
    }

    public void addStudent(Student s) {
        students.add(s);
        saveToFile();
    }

    public void updateStudent(String id, Student newData) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                students.set(i, newData);
                break;
            }
        }
        saveToFile();
    }

    public void deleteStudent(String id) {
        students.removeIf(s -> s.getId().equals(id));
        saveToFile();
    }

    public Student findStudent(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    public List<Student> getStudents() {
        return students;
    }

    public int getStudentCount() { return students.size(); }

    public double getAverageAge() {
        if (students.isEmpty()) return 0;
        return students.stream().mapToInt(Student::getAge).average().orElse(0);
    }

    public double getAverageGrade() {
        if (students.isEmpty()) return 0;
        return students.stream().mapToDouble(Student::getGrade).average().orElse(0);
    }

    // === File Handling ===
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                bw.write(s.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        students.clear();
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromString(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
