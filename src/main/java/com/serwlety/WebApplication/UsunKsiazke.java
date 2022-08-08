package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/usunKsiazke")
public class UsunKsiazke extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Usuwanie książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wpisz ID książki do usunięcia:</h1>");

        out.println("<form method=\"POST\" action=\"usunKsiazke\">");
        out.println("Id książki: <input type=\"TEXT\" name=\"IdKsiazka\"><br><br>");
        out.println("<input type=\"SUBMIT\" value=\"Usuń\">");
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
        out.println("<title>Usuwanie książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");

        int idKsiazka = 0;
        boolean czyDanePoprawne = true;

        //IdKsiazka
        if (czyPoprawnyInteger(request.getParameter("IdKsiazka"))) {
            idKsiazka = Integer.parseInt(request.getParameter("IdKsiazka"));
        } else {
            czyDanePoprawne = false;
            out.println("<p>Pole IdKsiazka jest nieprawidłowe!</p>");
        }

        if(czyDanePoprawne){
            Connection con;
            String pathDB = "jdbc:derby://localhost:1527/BooksDb";
            try {
                DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
                con = DriverManager.getConnection(pathDB);
                String sqlCommand = "SELECT COUNT(IdKsiazka) AS Ilosc FROM Ksiazka WHERE IdKsiazka = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sqlCommand);
                preparedStatement.setInt(1, idKsiazka);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int value = resultSet.getInt("Ilosc");

                boolean czyKsiazkaIstnieje;

                if (value > 0) {
                    czyKsiazkaIstnieje = true;
                } else {
                    czyKsiazkaIstnieje = false;
                    out.println("<p>Książka o podanym ID nie istnieje!</p>");
                    resultSet.close();
                    preparedStatement.close();
                    con.close();
                }

                if (czyKsiazkaIstnieje) {
                    //Pobieranie idRegal
                    sqlCommand = "SELECT IdRegal FROM Ksiazka WHERE IdKsiazka = ?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, idKsiazka);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int idRegal = resultSet.getInt("IdRegal");

                    //Usuwanie książki
                    sqlCommand = "DELETE FROM Ksiazka WHERE IdKsiazka = ?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, idKsiazka);
                    preparedStatement.executeUpdate();

                    //Usuwanie regału
                    sqlCommand = "DELETE FROM Regal WHERE IdRegal = ?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, idRegal);
                    preparedStatement.executeUpdate();

                    resultSet.close();
                    preparedStatement.close();
                    con.close();

                    out.println("<p>Książka o podanym ID została usunięta!</p>");
                    out.println("<button onclick=\"location.href='./'\">Strona główna</button>");
                }
            } catch (SQLException throwables) {
                out.println("<p>Nie udało się usunąć książki!</p>");
                throwables.printStackTrace();
            }
        }

        out.println("<button onclick=\"location.href='./usunKsiazke'\">Wstecz</button>");
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
