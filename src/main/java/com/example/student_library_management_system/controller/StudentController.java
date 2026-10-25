package com.example.student_library_management_system.controller;

import com.example.student_library_management_system.model.Student;
import com.example.student_library_management_system.requestdto.StudentRequestDto;
import com.example.student_library_management_system.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student/apis")
public class StudentController {

    private final StudentService studentService;

    /** Creates the controller with its student service. */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /** Creates a student and the student's library card. */
    @PostMapping("/save")
    public String saveStudent(@RequestBody StudentRequestDto studentRequestDto)
    {
        return studentService.saveStudent(studentRequestDto);
    }

    /** Returns a student by identifier. */
    @GetMapping("/findById/{id}")
    public Student findByStudentId(@PathVariable int id) {
        return studentService.findByStudentId(id);
    }

    /** Returns all students. */
    @GetMapping("/findAll")
    public List<Student> findAllStudent()
    {
        List<Student> studentList=studentService.findAll();
        return studentList;
    }

    /** Deletes a student by identifier. */
    @DeleteMapping("/delete/{id}")
    public String deleteStudentById(@PathVariable int id)
    {
        String response=studentService.deleteById(id);
        return response;
    }

    /** Updates a student's details. */
    @PutMapping("/update/{id}")
    public String updateStudent(@PathVariable int id,@RequestBody StudentRequestDto studentRequestDto)
    {
        String response=studentService.updateStudent(id,studentRequestDto);
        return response;
    }

    /** Returns one page of students. */
    @GetMapping("/findByPage")
    public List<Student> getStudentByPage(@RequestParam int pageNo,@RequestParam int pageSize)
    {
        List<Student> studentList=studentService.findStudentByPage(pageNo,pageSize);
        return studentList;
    }

    /** Finds a student by email address. */
    @GetMapping("/findByEmail")
    public Student findByStudentEmail(@RequestParam String email)
    {
        Student student=studentService.findStudentByEmail(email);
        return student;
    }

    /** Finds students by department. */
    @GetMapping("/findByDept")
    public List<Student> findByStudentDept(@RequestParam String dept)
    {
        List<Student> studentList=studentService.findAllStudentByDept(dept);
        return studentList;
    }




}
