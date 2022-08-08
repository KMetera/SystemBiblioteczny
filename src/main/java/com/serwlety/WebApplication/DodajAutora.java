package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;

@WebServlet("/dodajAutora")
public class DodajAutora extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dodawanie autora</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wpisz dane autora do dodania:</h1>");

        out.println("<form method=\"POST\" action=\"dodajAutora\">");
        out.println("Imię: <input type=\"TEXT\" name=\"Imie\"><br><br>");
        out.println("Nazwisko: <input type=\"TEXT\" name=\"Nazwisko\"><br><br>");
        out.println("Data urodzenia: <input type=\"DATE\" name=\"DataUrodzenia\"><br><br>");
        out.println("Kraj pochodzenia: <input type=\"TEXT\" name=\"KrajPochodzenia\"><br><br>");
        out.println("<input type=\"SUBMIT\" value=\"Dodaj\">");
        out.println("</form>");

        out.println("<button onclick=\"location.href='./'\">Wstecz</button>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dodawanie autora</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");

        Enumeration<String> listaNazwParametrow = request.getParameterNames();
        boolean czyWszystkieDane = true;

        while (listaNazwParametrow.hasMoreElements()) {
            String element = listaNazwParametrow.nextElement();
//            System.out.println(element + " " + request.getParameter(element));
            if (request.getParameter(element).equals("")) {
                czyWszystkieDane = false;
                out.println("<p>Nie dodano danych do pola " + element + "!</p>");
            }
        }

        if (czyWszystkieDane) {
            String imie = request.getParameter("Imie");
            String nazwisko = request.getParameter("Nazwisko");
            Date dataUrodzenia = Date.valueOf(request.getParameter("DataUrodzenia"));
            String krajPochodzenia = request.getParameter("KrajPochodzenia");

            Connection con;
            String pathDB = "jdbc:derby://localhost:1527/BooksDb";

            try {
                DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
                con = DriverManager.getConnection(pathDB);

                //MaxIdAutor+1
                String sqlCommand = "SELECT COALESCE(MAX(IdAutor),0) AS MaxIdAutor FROM Autor";
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlCommand);
                resultSet.next();
                int maxIdAutor = resultSet.getInt("MaxIdAutor") + 1;

                //Insert Autor
                sqlCommand = "INSERT INTO Autor (IdAutor, Imie, Nazwisko, DataUrodzenia, KrajPochodzenia) VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sqlCommand);
                preparedStatement.setInt(1, maxIdAutor);
                preparedStatement.setString(2, imie);
                preparedStatement.setString(3, nazwisko);
                preparedStatement.setDate(4, dataUrodzenia);
                preparedStatement.setString(5, krajPochodzenia);
                preparedStatement.executeUpdate();

                resultSet.close();
                statement.close();
                preparedStatement.close();
                con.close();

                out.println("<p>Autor został dodany!</p>");
                out.println("<button onclick=\"location.href='./'\">Strona główna</button>");
            } catch (SQLException throwables) {
                out.println("<p>Nie udało się dodać autora!</p>");
                throwables.printStackTrace();
            }

        }
        out.println("<button onclick=\"location.href='./dodajAutora'\">Wstecz</button>");

        out.println("</body></html>");

        out.flush();
        out.close();
    }
}
