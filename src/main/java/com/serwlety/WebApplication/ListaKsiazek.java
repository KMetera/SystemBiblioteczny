package com.serwlety.WebApplication;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/listaKsiazek")
public class ListaKsiazek extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Lista książek</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");

        String pathDB = "jdbc:derby://localhost:1527/BooksDb";
        String sqlCommand = "SELECT IdKsiazka, Tytul, Imie, Nazwisko, RokWydania, IloscStron, NrRegalu, NrPolki FROM Ksiazka JOIN Autor ON Ksiazka.IdAutor = Autor.IdAutor JOIN Regal ON Ksiazka.IdRegal=Regal.IdRegal ORDER BY IdKsiazka";
        Connection con;

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            con = DriverManager.getConnection(pathDB);
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            out.println("<h1>Lista książek w bibliotece:</h1>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Id</th>");
            out.println("<th>Tytuł</th>");
            out.println("<th>Imię autora</th>");
            out.println("<th>Nazwisko autora</th>");
            out.println("<th>Rok wydania</th>");
            out.println("<th>Ilość stron</th>");
            out.println("<th>Numer regału</th>");
            out.println("<th>Numer półki</th>");
            out.println("</tr>");


            while (resultSet.next()) {
                int idKsiazka = Integer.parseInt(resultSet.getString("IdKsiazka"));
                String tytul = resultSet.getString("Tytul");
                String imieAutora = resultSet.getString("Imie");
                String nazwiskoAutora = resultSet.getString("Nazwisko");
                int rokWydania = Integer.parseInt(resultSet.getString("RokWydania"));
                int iloscStron = Integer.parseInt(resultSet.getString("IloscStron"));
                int nrRegalu = Integer.parseInt(resultSet.getString("NrRegalu"));
                int nrPolki = Integer.parseInt(resultSet.getString("NrPolki"));

                out.println("<tr>");
                out.println("<td>" + idKsiazka + "</td>");
                out.println("<td>" + tytul + "</td>");
                out.println("<td>" + imieAutora + "</td>");
                out.println("<td>" + nazwiskoAutora + "</td>");
                out.println("<td>" + rokWydania + "</td>");
                out.println("<td>" + iloscStron + "</td>");
                out.println("<td>" + nrRegalu + "</td>");
                out.println("<td>" + nrPolki + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            resultSet.close();
            statement.close();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        out.println("<br>");
        out.println("<button onclick=\"location.href='./'\">Wstecz</button>");
        out.println("</body></html>");

        out.flush();
        out.close();
    }
}