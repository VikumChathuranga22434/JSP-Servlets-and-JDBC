package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception { 
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			//get a connection 
			myConn = dataSource.getConnection();
			
			//create sql statement
			String sql = "select * from student order by last_name";
			
			myStmt = myConn.createStatement();
			
			//execute query
			myRs = myStmt.executeQuery(sql);
			
			//process Result set
			while(myRs.next()) {
				
				//retrieve data from result set row
				int id = myRs.getInt("id");
				String firstname = myRs.getString("first_name");
				String lastname = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//create new student object
				Student tempStudent = new Student(id, firstname, lastname, email);
				
				//add student object to the student list
				students.add(tempStudent);
				
			}
			
			return students; 
			
		}finally {
			//close JDBC object
			close(myConn, myStmt, myRs);
		}
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try {
			
			if(myRs != null) {
				
				myRs.close();
				
			}if (myStmt != null) {
				
				myStmt.close();
				
			}if (myConn != null) {
				
				myConn.close(); // doesn't really close it ... just put back in to connection pool
				
			}
			
		}catch (Exception exc) {
			
			exc.printStackTrace();
			
		}
	}

	public void addStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql for insert
			String sql = "insert into student "
						  +"(first_name, Last_name, email)"
						  +"values(?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			//set the param values for the student
			myStmt.setString(1, theStudent.getFirstname());
			myStmt.setString(2, theStudent.getLastname());
			myStmt.setString(3, theStudent.getEmail()); 
			
			//execute sql statement
			myStmt.execute();
			
		}
		finally{
			//clean up JDBC object
			close(myConn, myStmt, null);
		}
		
	}

	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			// get connectiontoo database
			myConn = dataSource.getConnection();
						
			// create sql to get selected student
			String sql = "select * from student where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set the parameter
			myStmt.setInt(1, studentId);
			
			//execute the query
			myRs = myStmt.executeQuery();
			
			//retreive data from result set
			if(myRs.next()) {
				String firstname = myRs.getString("first_name");
				String lastname = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//use the studentId during construction
				theStudent = new Student(studentId, firstname, lastname, email);
				
			}else {
				throw new Exception("Couldn't find Student id : " + studentId);
			}
						
			return theStudent;
		}
		finally {
			
			// clean up the JDBC ojects
			close(myConn, myStmt, myRs);
			
		}
		
	}

	public void updateStudent(Student theStudent) throws Exception {
		
		
		// make connection
		Connection myConn = null;
		PreparedStatement myStmt = null;
			
		try {
			
			//create connection
			myConn = dataSource.getConnection();
			
			// update query
			String sql = "update student set first_name=?, last_name=?, email=? where id=?";
			
			// prepare Ststement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setString(1, theStudent.getFirstname());
			myStmt.setString(2, theStudent.getLastname());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute query
			myStmt.execute();
		}	
		finally {
			
			// clean up the JDBC ojects
			close(myConn, myStmt, null);
				
		}
		
	}

	public void deleteStudent(String theStudentId) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			
			//convert studentId to int
			int studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
						
			//create sql to delete student
			String sql = "delete from student where id=?";
						
			//prepare stmt
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute the statement
			myStmt.execute();
									
		}finally {
			// close database connection
			close(myConn, myStmt, null);
		}
		
	}

}
