<!DOCTYPE html>
<html>

	<head>
		<title>Add Student</title>
		<link type="text/css" rel="stylesheet" href="css/style.css">
		<link type="text/css" rel="stylesheet" href="css/add-student.css">
	</head>

	<body>
		
		<div id="wrapper">
			<div id="header">
				<h2>FooBar University</h2>
			</div>
		</div>
		
		<div id="container">
			<h3>Add Student</h3>
			
			<form action="StudentControllerServlet" method="get">
				<input type="hidden" name="command" value="ADD"/>
				
				<table>
					<tbody>
						<tr>
							<td><label for="firstname">First Name : </label></td>
							<td><input type="text" name="firstname"/></td>
						</tr>
						
						<tr>
							<td><label for="lastname">Last Name : </label></td>
							<td><input type="text" name="lastname"/></td>
						</tr>
						
						<tr>
							<td><label for="email">Email : </label></td>
							<td><input type="text" name="email"/></td>
						</tr>
						
						<tr>
							<td><label for="Submit"></label></td>
							<td><input type="submit" value="Save" class="save"/></td>
						</tr>
						
					</tbody>
				</table>
				
				<div style="clear: both;"></div>
				
				<p>
					<a href="StudentControllerServlet">Back To List</a>
				</p>
				
			</form>
			
		</div>
		
	</body>

</html>