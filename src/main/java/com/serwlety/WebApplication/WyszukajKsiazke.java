package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/wyszukajKsiazke")
public class WyszukajKsiazke extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Wyszukiwanie książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wyszukaj książkę:</h1>");

        out.println("<form method=\"GET\" action=\"wyszukajKsiazke\">");
        out.println("<input type=\"TEXT\" name=\"dane\"><br>");
        out.println("<p>Wybierz po czym chcesz wyszukać:</p>");
        out.println("<input type=\"RADIO\" id=\"Id\" name=\"wyszukiwanie\" value=\"Id\" checked=\"checked\">");
        out.println("<label for=\"Id\">Id książki</label><br>");
        out.println("<input type=\"RADIO\" id=\"Tytul\" name=\"wyszukiwanie\" value=\"Tytul\">");
        out.println("<label for=\"Tytul\">Tytuł książki</label><br>");
        out.println("<input type=\"RADIO\" id=\"Nazwisko\" name=\"wyszukiwanie\" value=\"Nazwisko\">");
        out.println("<label for=\"Nazwisko\">Nazwisko autora</label><br><br>");

        out.println("<input type=\"SUBMIT\" value=\"Wyszukaj\">");
        out.println("</form>");
        out.println("<button onclick=\"location.href='./'\">Wstecz</button><br>");

        if (!request.getQueryString().equals("=dane") || !(request.getQueryString() == null)) {
            Connection con;
            String pathDB = "jdbc:derby://localhost:1527/BooksDb";

            try {
                DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
                con = DriverManager.getConnection(pathDB);
                String parametr = request.getParameter("wyszukiwanie");
                String wartosc = request.getParameter("dane");
                String sqlCommand = "";
                PreparedStatement preparedStatement = null;
                System.out.println(parametr);
                System.out.println(wartosc);

                if (parametr.equals("Id")) {
                    sqlCommand = "SELECT IdKsiazka, Tytul, Imie, Nazwisko, RokWydania, IloscStron, NrRegalu, NrPolki FROM Ksiazka JOIN Autor ON Ksiazka.IdAutor=Autor.IdAutor JOIN Regal ON Ksiazka.IdRegal=Regal.IdRegal WHERE IdKsiazka=?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, Integer.parseInt(wartosc));
                } else if (parametr.equals("Tytul")) {
                    sqlCommand = "SELECT IdKsiazka, Tytul, Imie, Nazwisko, RokWydania, IloscStron, NrRegalu, NrPolki FROM Ksiazka JOIN Autor ON Ksiazka.IdAutor=Autor.IdAutor JOIN Regal ON Ksiazka.IdRegal=Regal.IdRegal WHERE Tytul=?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    System.out.println(request.getParameter(parametr));
                    preparedStatement.setString(1, wartosc);
                } else if (parametr.equals("Nazwisko")) {
                    sqlCommand = "SELECT IdKsiazka, Tytul, Imie, Nazwisko, RokWydania, IloscStron, NrRegalu, NrPolki FROM Ksiazka JOIN Autor ON Ksiazka.IdAutor=Autor.IdAutor JOIN Regal ON Ksiazka.IdRegal=Regal.IdRegal WHERE Nazwisko=?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setString(1, wartosc);
                }
                ResultSet resultSet = preparedStatement.executeQuery();

                out.println("<table>");
                out.println("<tr>");
                out.println("<th>Id książki</th>");
                out.println("<th>Tytuł</th>");
                out.println("<th>Imię autora</th>");
                out.println("<th>Nazwisko autora</th>");
                out.println("<th>Rok wydania</th>");
                out.println("<th>Ilość stron</th>");
                out.println("<th>Numer regału</th>");
                out.println("<th>Numer półki</th>");
                out.println("</tr>");

                while (resultSet.next()) {
                    int idKsiazka = resultSet.getInt("IdKsiazka");
                    String tytul = resultSet.getString("Tytul");
                    String imie = resultSet.getString("Imie");
                    String nazwisko = resultSet.getString("Nazwisko");
                    int rokWydania = resultSet.getInt("RokWydania");
                    int iloscStron = resultSet.getInt("IloscStron");
                    int nrRegalu = resultSet.getInt("NrRegalu");
                    int nrPolki = resultSet.getInt("NrPolki");

                    System.out.println(idKsiazka + " " + tytul + " " + imie + " " + nazwisko + " " + rokWydania + " " + iloscStron + " " + nrRegalu + " " + nrPolki);
                    out.println("<tr>");
                    out.println("<td>" + idKsiazka + "</td>");
                    out.println("<td>" + tytul + "</td>");
                    out.println("<td>" + imie + "</td>");
                    out.println("<td>" + nazwisko + "</td>");
                    out.println("<td>" + rokWydania + "</td>");
                    out.println("<td>" + iloscStron + "</td>");
                    out.println("<td>" + nrRegalu + "</td>");
                    out.println("<td>" + nrPolki + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");

                resultSet.close();
                preparedStatement.close();
                con.close();

            } catch (SQLException throwables) {
                out.println("<p>Nie udało się wyszukać książki!</p>");
                throwables.printStackTrace();
            }
        }

        out.println("</body></html>");
        out.flush();
        out.close();
    }

    public boolean czyPoprawnyInteger(String integer) {
        try {
            Integer.parseInt(integer);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
