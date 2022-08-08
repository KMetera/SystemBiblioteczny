package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;

@WebServlet("/dodajKsiazke")
public class DodajKsiazke extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dodawanie książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wpisz dane książki do dodania:</h1>");

        out.println("<form method=\"POST\" action=\"dodajKsiazke\">");
        out.println("Id książki: <input type=\"TEXT\" name=\"IdKsiazka\"><br><br>");
        out.println("Tytuł: <input type=\"TEXT\" name=\"Tytul\"><br><br>");
        out.println("Id autora: <input type=\"TEXT\" name=\"IdAutor\"><br><br>");
        out.println("Rok wydania: <input type=\"TEXT\" name=\"RokWydania\"><br><br>");
        out.println("Ilość stron: <input type=\"TEXT\" name=\"IloscStron\"><br><br>");
        out.println("Numer regału: <input type=\"TEXT\" name=\"NrRegalu\"><br><br>");
        out.println("Numer półki: <input type=\"TEXT\" name=\"NrPolki\"><br><br>");
        out.println("<input type=\"SUBMIT\" value=\"Dodaj\">");
        out.println("</form>");

        out.println("<button onclick=\"location.href='./'\">Wstecz</button>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dodawanie książki</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        boolean czyDanePoprawne = true;
        boolean czyWszystkieDane = true;

        Enumeration<String> listaNazwParametrow = req.getParameterNames();
        while (listaNazwParametrow.hasMoreElements()) {
            String element = listaNazwParametrow.nextElement();
            if (req.getParameter(element).equals("")) {
                czyWszystkieDane = false;
                out.println("<p>Nie dodano danych do pola " + element + "!</p>");
            }
        }

        if (czyWszystkieDane) {
            int idKsiazka = 0;
            String tytul = "";
            int idAutor = 0;
            int rokWydania = 0;
            int iloscStron = 0;
            int nrRegalu = 0;
            int nrPolki = 0;

            //IdKsiazka
            if (czyPoprawnyInteger(req.getParameter("IdKsiazka"))) {
                idKsiazka = Integer.parseInt(req.getParameter("IdKsiazka"));
            } else {
                czyDanePoprawne = false;
                out.println("<p>Pole IdKsiazka jest nieprawidłowe!</p>");
            }

            //Tytul
            tytul = req.getParameter("Tytul");

            //IdAutor
            if (czyPoprawnyInteger(req.getParameter("IdAutor"))) {
                idAutor = Integer.parseInt(req.getParameter("IdAutor"));
            } else {
                czyDanePoprawne = false;
                out.println("<p>Pole IdAutor jest nieprawidłowe!</p>");
            }

            //RokWydania
            if (czyPoprawnyInteger(req.getParameter("RokWydania"))) {
                rokWydania = Integer.parseInt(req.getParameter("RokWydania"));
            } else {
                czyDanePoprawne = false;
                out.println("<p>Pole RokWydania jest nieprawidłowe!</p>");
            }

            //IloscStron
            if (czyPoprawnyInteger(req.getParameter("IloscStron"))) {
                iloscStron = Integer.parseInt(req.getParameter("IloscStron"));
            } else {
                czyDanePoprawne = false;
                out.println("<p>Pole IloscStron jest nieprawidłowe!</p>");
            }

            //NrRegalu
            if (czyPoprawnyInteger(req.getParameter("NrRegalu"))) {
                nrRegalu = Integer.parseInt(req.getParameter("NrRegalu"));
            } else {
                czyDanePoprawne = false;
                out.println("<p>Pole NrRegalu jest nieprawidłowe!</p>");
            }

            //NrPolki
            if (czyPoprawnyInteger(req.getParameter("NrPolki"))) {
                nrPolki = Integer.parseInt(req.getParameter("NrPolki"));
            } else {
                czyDanePoprawne = false;
                out.println("<p>Pole NrPolki jest nieprawidłowe!</p>");
            }


            if (czyDanePoprawne) {
                boolean czyIstniejeJuzKsiazka;
                boolean czyIstniejeAutor;

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

                    if (value > 0) {
                        czyIstniejeJuzKsiazka = true;
                        out.println("<p>Istnieje już książka o podanym ID!</p>");
                    } else {
                        czyIstniejeJuzKsiazka = false;
                    }

                    sqlCommand = "SELECT COUNT(IdAutor) AS Ilosc FROM Autor WHERE IdAutor = ?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, idAutor);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    value = resultSet.getInt("Ilosc");

                    if (value == 0) {
                        czyIstniejeAutor = false;
                        out.println("<p>Autor o podanym ID nie istnieje!</p>");
                    } else {
                        czyIstniejeAutor = true;
                    }

                    if (!czyIstniejeJuzKsiazka && czyIstniejeAutor) {
                        //MaxIdRegal+1
                        sqlCommand = "SELECT COALESCE(Max(IdRegal),0) FROM Regal";
                        Statement statement = con.createStatement();
                        resultSet = statement.executeQuery(sqlCommand);
                        resultSet.next();
                        int maxIdRegal = resultSet.getInt(1) + 1;

                        //Insert do Regal
                        sqlCommand = "INSERT INTO Regal (IdRegal, NrRegalu, NrPolki) VALUES (?,?,?)";
                        preparedStatement = con.prepareStatement(sqlCommand);
                        preparedStatement.setInt(1, maxIdRegal);
                        preparedStatement.setInt(2, nrRegalu);
                        preparedStatement.setInt(3, nrPolki);
                        preparedStatement.executeUpdate();

                        //Insert do Ksiazka
                        sqlCommand = "INSERT INTO Ksiazka (IdKsiazka, Tytul, IdAutor, RokWydania, IloscStron, IdRegal) VALUES (?,?,?,?,?,?)";
                        preparedStatement = con.prepareStatement(sqlCommand);
                        preparedStatement.setInt(1, idKsiazka);
                        preparedStatement.setString(2, tytul);
                        preparedStatement.setInt(3, idAutor);
                        preparedStatement.setInt(4, rokWydania);
                        preparedStatement.setInt(5, iloscStron);
                        preparedStatement.setInt(6, maxIdRegal);
                        preparedStatement.executeUpdate();

                        resultSet.close();
                        statement.close();
                        preparedStatement.close();
                        con.close();

                        out.println("<p>Książka została dodana!</p>");
                        out.println("<button onclick=\"location.href='./'\">Strona główna</button>");
                    }
                } catch (SQLException throwables) {
                    out.println("<p>Nie udało się dodać książki!</p>");
                    throwables.printStackTrace();
                }
            }
        }

        out.println("<button onclick=\"location.href='./dodajKsiazke'\">Wstecz</button>");

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
