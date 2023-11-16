import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

@WebServlet("/committeeHome")
public class TACommitteeHome extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { 
	    try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection connObject = DriverManager.getConnection("jdbc:mysql://10.0.0.224:3306/ta", "ta", "root");
		    PrintWriter printWriter = res.getWriter();
            Cookie[] cookies = req.getCookies();
            if (connObject != null) {
			    res.setContentType("text/html");
			    String username="", usertype="";
    
                for(int i=0; i < cookies.length; i++) {
                      if("TAusername".equals(cookies[i].getName())) {
                          username = cookies[i].getValue();
                      }
                      else if("TAusertype".equals(cookies[i].getName())) {
                          usertype = cookies[i].getValue();
                      }
                }

                if(usertype.equals("committee")) {
                    Statement userStatement = connObject.createStatement();
			        ResultSet userResultSet = userStatement.executeQuery("SELECT * FROM ta_committee WHERE email='" + username + "'");
                    if(userResultSet.next()){
                        req.setAttribute("applicationsList", getApplicationsList(connObject));
                        req.getRequestDispatcher("/taCommitteeHome.jsp").forward(req, res);
                    } else{
                        req.getRequestDispatcher("/login.jsp").forward(req, res);
                    }
                } else{
                    req.getRequestDispatcher("/login.jsp").forward(req, res);
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

    public List<TaApplicationData> getApplicationsList(Connection con) {

        String applicationDataQuery = "SELECT application.*, course.course_name, selectedDepartment.department_name as selected_department_name, "+
        "presentDepartment.department_name as present_department_name, CASE WHEN application.instructor_feedback_id IS NOT NULL "+
        "THEN (SELECT overall_feedback from instructor_feedback WHERE instructor_feedback.id=application.instructor_feedback_id) "+
        "ELSE 'NONE' END AS overall_feedback FROM ta_application as application, course, "+
        "department as selectedDepartment, department as presentDepartment "+ 
        "WHERE application.course_id=course.id AND application.department_id=selectedDepartment.id AND "+
        "application.present_department=presentDepartment.id";

        
        Statement applicationsStatement=null;
        Statement feedbackStatement=null;
        ResultSet feedbackResultSet=null;
        ResultSet applicationsResultSet=null;
        List<TaApplicationData> applicationsList = new ArrayList<TaApplicationData>();
        System.out.println("==== In getApplicationsList Method Start==== ");
        try 
        {
            applicationsStatement = con.createStatement();
            feedbackStatement = con.createStatement();
            applicationsResultSet = applicationsStatement.executeQuery(applicationDataQuery);
            while(applicationsResultSet.next()){
                TaApplicationData application = new TaApplicationData();
                int instructorFeedbackId = applicationsResultSet.getInt("instructor_feedback_id");
                System.out.println("FeedbackId : "+applicationsResultSet.getInt("instructor_feedback_id"));
                if(applicationsResultSet.getInt("instructor_feedback_id") == 0) {
                    application.setInstructorFeedbackExists(false);
                } else {
                    String instructorFeedbackQuery="SELECT instructor_feedback.*, course.course_name FROM instructor_feedback, course WHERE instructor_feedback.id='"+instructorFeedbackId+"' AND instructor_feedback.course_id=course.id";
                    feedbackResultSet = feedbackStatement.executeQuery(instructorFeedbackQuery);
                    feedbackResultSet.next();

                    application.setInstructorFeedbackExists(true);
                    application.setInstructorFeedbackName(feedbackResultSet.getString("instructor_name"));
                    application.setInstructorFeedbackCourseName(feedbackResultSet.getString("course_name"));
                    application.setPerformanceRating(feedbackResultSet.getInt("performance_rating"));
                    application.setTechnicalSkillRating(feedbackResultSet.getInt("technical_skill"));
                    application.setCommunicationSkillRating(feedbackResultSet.getInt("communication_skill"));
                    application.setInstructorOverallFeedback(feedbackResultSet.getString("overall_feedback"));
                }
                application.setDepartmentName(applicationsResultSet.getString("selected_department_name"));
                application.setCourseName(applicationsResultSet.getString("course_name"));
                application.setTaApplicationId(applicationsResultSet.getInt("id"));
                application.setFirstname(applicationsResultSet.getString("firstname"));
                application.setLastname(applicationsResultSet.getString("lastname"));
                application.setEmail(applicationsResultSet.getString("email"));
                application.setZnumber(applicationsResultSet.getString("znumber"));
                application.setCgpa(applicationsResultSet.getFloat("cgpa"));
                application.setPresentDepartmentName(applicationsResultSet.getString("present_department_name"));
                application.setEducationLevel(applicationsResultSet.getString("education_level"));
                application.setGraduationDate(applicationsResultSet.getDate("graduation_date"));
                application.setCv(applicationsResultSet.getString("cv"));
                application.setPreviousExperience(applicationsResultSet.getBoolean("previous_experience"));
                application.setExpCourse(applicationsResultSet.getString("exp_course"));
                application.setExpDuration(applicationsResultSet.getInt("exp_duration"));
                application.setRecommended(applicationsResultSet.getBoolean("recommended"));
                application.setStatus(applicationsResultSet.getString("status"));
                applicationsList.add(application);
            }
        }catch (Exception e) {
            e.printStackTrace();    
        }
        System.out.println("==== In getApplicationsList Method End ==== ");
        return applicationsList;
    }

}