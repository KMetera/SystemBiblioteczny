package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/listaAutorow")
public class ListaAutorow extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathDB = "jdbc:derby://localhost:1527/BooksDb";
        String sqlCommand = "SELECT IdAutor, Imie, Nazwisko, DataUrodzenia, KrajPochodzenia FROM Autor ORDER BY Nazwisko, Imie";
        Connection con;

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            con = DriverManager.getConnection(pathDB);
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Lista autorów</title>");
            out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
            out.println("</head>");
            out.println("<body>");

            out.println("<h1>Lista książek w bibliotece:</h1>");

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Id</th>");
            out.println("<th>Imię autora</th>");
            out.println("<th>Nazwisko autora</th>");
            out.println("<th>Data urodzenia</th>");
            out.println("<th>Kraj pochodzenia</th>");
            out.println("</tr>");


            while (resultSet.next()) {
                int idAutor = Integer.parseInt(resultSet.getString("IdAutor"));
                String imie = resultSet.getString("Imie");
                String nazwisko = resultSet.getString("Nazwisko");
                Date dataUrodzenia = Date.valueOf(resultSet.getString("DataUrodzenia"));
                String krajPochodzenia = resultSet.getString("KrajPochodzenia");

                out.println("<tr>");
                out.println("<td>" + idAutor + "</td>");
                out.println("<td>" + imie + "</td>");
                out.println("<td>" + nazwisko + "</td>");
                out.println("<td>" + dataUrodzenia + "</td>");
                out.println("<td>" + krajPochodzenia + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<br/>");
            out.println("<button onclick=\"location.href='./'\">Wstecz</button>");
            out.println("</body></html>");

            resultSet.close();
            statement.close();
            out.flush();
            out.close();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
