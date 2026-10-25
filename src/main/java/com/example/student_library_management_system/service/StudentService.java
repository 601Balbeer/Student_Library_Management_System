package com.example.student_library_management_system.service;

import com.example.student_library_management_system.model.Card;
import com.example.student_library_management_system.model.Student;
import com.example.student_library_management_system.repository.StudentRepository;
import com.example.student_library_management_system.requestdto.StudentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    /** Creates a student and a library card with a three-year term. */
    public String saveStudent(StudentRequestDto studentRequestDto)
    {
        //convert the request DTO into a model class
        Student student=new Student();

        student.setName(studentRequestDto.getName());
        student.setDob(studentRequestDto.getDob());
        student.setDept(studentRequestDto.getDept());
        student.setSem(studentRequestDto.getSem());
        student.setAddress(studentRequestDto.getAddress());
        student.setEmail(studentRequestDto.getEmail());
        student.setMobile(studentRequestDto.getMobile());
        student.setGender(studentRequestDto.getGender());

        //whenever a student adds card also gets added as part of cascading
        Card card=new Card();
        card.setExpiryDate(LocalDate.now().plusYears(3).toString());

        student.setCard(card);
        card.setStudent(student);

        studentRepository.save(student);
        return "Student saved successfully";
    }

    /** Finds a student by identifier or throws when the student is missing. */
    public Student findByStudentId(int id)
    {
        Optional<Student> studentOptional=studentRepository.findById(id);
        if(studentOptional.isPresent())
        {
            return studentOptional.get();
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student ID not found");
        }
    }


    /** Returns all students. */
    public List<Student> findAll()
    {
        List<Student> studentList=studentRepository.findAll();
        return studentList;
    }

    /** Returns one page of students, sorted by name in ascending order. */
    public List<Student> findStudentByPage(int pageNo, int PageSize)
    {
       List<Student> studentList= studentRepository.findAll(PageRequest.of(pageNo, PageSize,Sort.by("name").ascending())).getContent();
       return studentList;
    }





    /** Deletes the student with the supplied identifier. */
    public String deleteById(int id)
    {
        studentRepository.deleteById(id);
        return "Student deleted successfully";
    }

    /** Updates a student's details from the supplied request data. */
    public String updateStudent(int id,StudentRequestDto newStudentRequestDto)
    {
      Student existingStudent = findByStudentId(id);
      if(existingStudent!=null)
      {
          existingStudent.setName(newStudentRequestDto.getName());
          existingStudent.setDob(newStudentRequestDto.getDob());
          existingStudent.setGender(newStudentRequestDto.getGender());
          existingStudent.setMobile(newStudentRequestDto.getMobile());
          existingStudent.setEmail(newStudentRequestDto.getEmail());
          existingStudent.setAddress(newStudentRequestDto.getAddress());
          existingStudent.setSem(newStudentRequestDto.getSem());
          existingStudent.setDept(newStudentRequestDto.getDept());

          studentRepository.save(existingStudent);

          return "Student updated successfully";
      }
      else
      {
          return "Student with id:"+id+" is not found";
      }



    }
    /** Finds a student by email address. */
    public Student findStudentByEmail(String email)
    {
        Student student=studentRepository.getByEmail(email);
        return student;
    }
    /** Returns students belonging to the supplied department. */
    public List<Student> findAllStudentByDept(String dept)
    {
        List<Student> studentList=studentRepository.getStudentByDept(dept);
        return studentList;
    }












}
