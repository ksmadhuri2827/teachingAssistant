import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/addRemoveCourse")
public class AddRemoveCourse extends HttpServlet{
  public void doPost(HttpServletRequest req, HttpServletResponse res)
  throws ServletException,IOException {
	  
	  try 
	  {
		  Class.forName("com.mysql.jdbc.Driver");
		  Connection connObject = DriverManager.getConnection("jdbc:mysql://127.8.9.0:3306/ta", "ta", "root");
			PrintWriter printWriter = res.getWriter();
            Cookie[] cookies = req.getCookies();
            if (connObject != null) {
			    res.setContentType("text/html");

                /** Fetching Username and Usertype from cookies */
                String username="", usertype="";
                for(int i=0; i < cookies.length; i++) {
                      if("TAusername".equals(cookies[i].getName())) {
                          username = cookies[i].getValue();
                      }
                      else if("TAusertype".equals(cookies[i].getName())) {
                          usertype = cookies[i].getValue();
                      }
                }

                /** Redirecting to their respective pages for other users */
                if(username.equals("none")) {
                    req.getRequestDispatcher("/login.jsp").forward(req, res);
                } else if(usertype.equals("committee")) {
                    req.getRequestDispatcher("/committeeHome.jsp").forward(req, res);
                } else if(usertype.equals("instructor")) {
                    req.getRequestDispatcher("/instructorHome.jsp").forward(req, res);
                } else {

                    String action = req.getParameter("action");
                    if(action.equals("add")){
                        String courseName = req.getParameter("courseName");
                        int departmentId = Integer.parseInt(req.getParameter("departmentId"));
                        int instructorId = Integer.parseInt(req.getParameter("instructorId"));
                        boolean status = false;
                        if("on".equals(req.getParameter("status"))){
                            status = true;
                        } else {
                            status = false;
                        }

                        String query = "INSERT INTO course (course_name, status, department_id, instructor_id) VALUES(?,?,?,?)";
                        PreparedStatement addCoursePS = connObject.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        addCoursePS.setString(1,courseName);
                        addCoursePS.setBoolean(2,status);
                        addCoursePS.setInt(3,departmentId);
                        addCoursePS.setInt(4,instructorId);
                        int updatedRows = addCoursePS.executeUpdate();
                        if(updatedRows==1){
                            ResultSet generatedKeys = addCoursePS.getGeneratedKeys();
                            if(generatedKeys.next()){
                                long insertedId = generatedKeys.getLong(1);
                                printWriter.print(insertedId);
                                System.out.println("Successfully added new course!");
                                System.out.println("Inserted ID for course : "+insertedId);
                            } else {
                                printWriter.print("No Id");
                            }
                        }else{
                            printWriter.print("failed");
                            System.out.println("Failed to add course !");
                        }
                    } else {
                        System.out.println(req.getParameter("ids"));
                        String ids = req.getParameter("ids");
                        String query = "DELETE FROM course WHERE id IN ("+ids+")";
                        Statement courseStatement = connObject.createStatement();
                        int updatedRows = courseStatement.executeUpdate(query);
                        if(updatedRows>=1){
                            printWriter.print("success");
                            System.out.println("successfully removed course");
                        }else{
                            printWriter.print("failed");
                            System.out.println("Failed to remove course");
                        }
                    }
                }
				
            } else {
                printWriter.print("Not connected to the database!");
            }

        } 
		catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

  }
}