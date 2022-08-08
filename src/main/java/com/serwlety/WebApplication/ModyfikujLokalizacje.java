package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/modyfikujLokalizacje")
public class ModyfikujLokalizacje extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Modyfikowanie lokalizacji książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wpisz dane książki do modyfikacji:</h1>");

        out.println("<form method=\"POST\" action=\"modyfikujLokalizacje\">");
        out.println("Id książki: <input type=\"TEXT\" name=\"IdKsiazka\"><br><br>");
        out.println("Nowy numer regału: <input type=\"TEXT\" name=\"NrRegalu\"><br><br>");
        out.println("Nowy numer półki: <input type=\"TEXT\" name=\"NrPolki\"><br><br>");
        out.println("<input type=\"SUBMIT\" value=\"Modyfikuj\">");
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
        out.println("<title>Modyfikuj lokalizację książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");

        int idKsiazka = 0;
        int nrRegalu = 0;
        int nrPolki = 0;

        boolean czyDanePoprawne = true;

        //IdKsiazka
        if (czyPoprawnyInteger(request.getParameter("IdKsiazka"))) {
            idKsiazka = Integer.parseInt(request.getParameter("IdKsiazka"));
        } else {
            czyDanePoprawne = false;
            out.println("<p>Pole IdKsiazka jest nieprawidłowe!</p>");
        }

        //NrRegalu
        if (czyPoprawnyInteger(request.getParameter("NrRegalu"))) {
            nrRegalu = Integer.parseInt(request.getParameter("NrRegalu"));
        } else {
            czyDanePoprawne = false;
            out.println("<p>Pole NrRegalu jest nieprawidłowe!</p>");
        }

        //NrPolki
        if (czyPoprawnyInteger(request.getParameter("NrPolki"))) {
            nrPolki = Integer.parseInt(request.getParameter("NrPolki"));
        } else {
            czyDanePoprawne = false;
            out.println("<p>Pole NrPolki jest nieprawidłowe!</p>");
        }


        if (czyDanePoprawne) {
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
                int ilosc = resultSet.getInt("Ilosc");

                boolean czyKsiazkaIstnieje;

                if (ilosc > 0) {
                    czyKsiazkaIstnieje = true;
                } else {
                    czyKsiazkaIstnieje = false;
                    out.println("<p>Książka o podanym ID nie istnieje!</p>");
                    resultSet.close();
                    preparedStatement.close();
                    con.close();
                }


                if (czyKsiazkaIstnieje) {
                    sqlCommand = "SELECT IdRegal FROM Ksiazka WHERE IdKsiazka = ?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, idKsiazka);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int idRegal = resultSet.getInt("IdRegal");
                    System.out.println(idRegal);

                    sqlCommand = "UPDATE Regal SET NrRegalu=?, NrPolki=? WHERE IdRegal=?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, nrRegalu);
                    preparedStatement.setInt(2, nrPolki);
                    preparedStatement.setInt(3, idRegal);
                    preparedStatement.executeUpdate();

                    resultSet.close();
                    preparedStatement.close();
                    con.close();

                    out.println("<p>Lokalizacja książki została zmodyfikowana!</p>");
                    out.println("<button onclick=\"location.href='./'\">Strona główna</button>");
                }

                out.flush();
                out.close();
            } catch (SQLException throwables) {
                out.println("<p>Nie udało się zmodyfikować lokalizacji książki!</p>");
                throwables.printStackTrace();
            }
        }
        out.println("<button onclick=\"location.href='./modyfikujLokalizacje'\">Wstecz</button>");
        out.println("</body></html>");
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
