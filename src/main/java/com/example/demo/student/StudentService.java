package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        System.out.println(student);
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("student with id"+studentId+"does not exists");
        }
        studentRepository.deleteById(studentId);

    }

    @Transactional
    public void updateStudent(Long studentId, Student newDataStudent) {
        //boolean exists = studentRepository.existsById(studentId);
        /*if(!exists) {
            throw new IllegalStateException("student with id"+studentId+"does not exists");
        }*/
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("student with id: "+studentId+"Does not exist"));
        if (
                newDataStudent.getEmail() != null &&
                !newDataStudent.getEmail().isEmpty() &&
                !Objects.equals(newDataStudent.getEmail(), student.getEmail())
        ) {
            Optional<Student> studentEmailOptional = studentRepository.findStudentByEmail(newDataStudent.getEmail());
            if (studentEmailOptional.isPresent()){
                throw new IllegalStateException("email Taken");
            }
            student.setEmail(newDataStudent.getEmail());
        }
        if (
                newDataStudent.getDob() != null &&
                !Objects.equals(newDataStudent.getDob(), student.getDob())
        ) {
            student.setDob(newDataStudent.getDob());
        }
        if (
                newDataStudent.getName() != null &&
                !newDataStudent.getName().isEmpty() &&
                !Objects.equals(newDataStudent.getName(), student.getName())
        ) {
            student.setName(newDataStudent.getName());
        }
    }
}
