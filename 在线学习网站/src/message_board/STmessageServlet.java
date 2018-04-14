package message_board;

import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.sql.DataSource;
import message_board.messagebean;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name="STmessageservlet",urlPatterns={"/STmessage.do"})


public class STmessageServlet extends HttpServlet {
    DataSource dataSource;
    Connection db=null;

    public void init() {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource)context.lookup("java:comp/env/jdbc/mysqlDS");
        }catch(NamingException ne){
            System.out.println("Exception:"+ne);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Name = null;
        String Mail = null;
        String Message = null;

        ArrayList<messagebean> messagebeanlist=new ArrayList<messagebean>();

        try {
            db=dataSource.getConnection();
            Statement stat=db.createStatement();
            ResultSet rs=stat.executeQuery("select * from message");

            while(rs.next()){
                messagebean bean=new messagebean();

                Name=rs.getString("Name");
                Mail=rs.getString("Mail");
                Message=rs.getString("Message");

                bean.setName(Name);
                bean.setMail(Mail);
                bean.setMessage(Message);

                messagebeanlist.add(bean);
            }
            if(!messagebeanlist.isEmpty()) {
                request.setAttribute("messagebeanlist", messagebeanlist);
                RequestDispatcher ss=request.getRequestDispatcher("STabout.jsp");
                ss.forward(request, response);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
