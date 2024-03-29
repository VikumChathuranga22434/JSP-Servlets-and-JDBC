package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException; 
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		//create our student Db util ... and pass in the conn pool/ dataSource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}catch (Exception exc) {
			throw new ServletException(exc);
		}
	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try { 
			//read the command parameter
			String theCommand = request.getParameter("command");
			
			//If the command missing, then default to listing student
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			//route to the appropriate method
			switch (theCommand) {
				
			case "LIST" : 
				//List the student ... in MVC fashion
				listStudents(request, response);
				break;
			
			case "ADD" : 
				addStudent(request, response);
				break;
				
			case "LOAD" : 
				loadStudent(request, response);
				break;
				
			case "UPDATE" : 
				updateStudent(request, response);
				break;
				
			case "DELETE" : 
				deleteStudent(request, response);
				break;
				
			default : 
				listStudents(request,response);	
				
			}
			
			
		}catch (Exception exc) {
			throw new ServletException(exc); 
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		// delete the student from the database
		studentDbUtil.deleteStudent(theStudentId);
		
		// send them back to "list-students.jsp" page
		listStudents(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		
		// create a new student object
		Student theStudent = new Student(id, firstname, lastname, email);
		
		// perform update on database
		studentDbUtil.updateStudent(theStudent);	
		
		// send them back to the "list-students.jsp" page
		listStudents(request, response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		//get student from database (DB util)
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		//place Student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("update-student-form.jsp");
		dispatcher.forward(request, response);
				
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//read student info form from data
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		
		//create a new student object
		Student theStudent = new Student(firstname, lastname, email);
		
		//add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		//send back to main page(the Student list)
		listStudents(request, response);
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//list students from DB Util
		List<Student> students = studentDbUtil.getStudents();
		
		//add students to request
		request.setAttribute("STUDENT_LIST", students);
		
		//send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}
