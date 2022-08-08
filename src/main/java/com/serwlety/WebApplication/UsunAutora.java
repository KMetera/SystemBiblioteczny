package com.serwlety.WebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/usunAutora")
public class UsunAutora extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Usuwanie autora</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wpisz ID autora do usunięcia:</h1>");

        out.println("<form method=\"POST\" action=\"usunAutora\">");
        out.println("Id autora: <input type=\"TEXT\" name=\"IdAutor\"><br><br>");
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
        out.println("<title>Usuwanie autora</title>");
        out.println("<link href=\"css/style.css\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");

        int idAutor = 0;
        boolean czyDanePoprawne = true;

        //IdAutor
        if (czyPoprawnyInteger(request.getParameter("IdAutor"))) {
            idAutor = Integer.parseInt(request.getParameter("IdAutor"));
        } else {
            czyDanePoprawne = false;
            out.println("<p>Pole IdAutor jest nieprawidłowe!</p>");
        }
        
        if(czyDanePoprawne){
            Connection con;
            String pathDB = "jdbc:derby://localhost:1527/BooksDb";
            try {
                DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
                con = DriverManager.getConnection(pathDB);

                //Sprawdzanie czy dany autor istnieje
                String sqlCommand = "SELECT COUNT(IdAutor) AS IloscAutorow FROM Autor WHERE IdAutor = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sqlCommand);
                preparedStatement.setInt(1, idAutor);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int iloscAutorow = resultSet.getInt("IloscAutorow");

                if (iloscAutorow == 0) {
                    out.println("<p>Nie istnieje autor o podanym ID!</p>");
                    resultSet.close();
                    preparedStatement.close();
                    con.close();
                } else {
                    //Sprawdzanie czy w bibliotece są obecne książki danego autora
                    sqlCommand = "SELECT COUNT(IdKsiazka) AS IloscKsiazek FROM Ksiazka WHERE IdAutor = ?";
                    preparedStatement = con.prepareStatement(sqlCommand);
                    preparedStatement.setInt(1, idAutor);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int iloscKsiazekAutora = resultSet.getInt("IloscKsiazek");
                    if (iloscKsiazekAutora > 0) {
                        out.println("<p>Nie można usunąć autora, ponieważ w bibliotece " + (iloscKsiazekAutora == 1 ? "znajduje" : "znajdują") + " się jego " + iloscKsiazekAutora + (iloscKsiazekAutora == 1 ? " książka" : iloscKsiazekAutora >= 5 ? " książek" : " książki") + "!</p>");
                        resultSet.close();
                        preparedStatement.close();
                        con.close();
                    } else {
                        sqlCommand = "DELETE FROM Autor WHERE IdAutor = ?";
                        preparedStatement = con.prepareStatement(sqlCommand);
                        preparedStatement.setInt(1, idAutor);
                        preparedStatement.executeUpdate();

                        resultSet.close();
                        preparedStatement.close();
                        con.close();

                        out.println("<p>Autor o podanym ID został usunięty!</p>");
                        out.println("<button onclick=\"location.href='./'\">Strona główna</button>");
                    }
                }
            } catch (SQLException throwables) {
                out.println("<p>Nie udało się usunąć Autora!</p>");
                throwables.printStackTrace();
            }
        }

        out.println("<button onclick=\"location.href='./usunAutora'\">Wstecz</button>");
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
