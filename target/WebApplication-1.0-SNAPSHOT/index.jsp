<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Menedżer książek</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
<h1><%= "Menedżer zarządzania książkami<br>Publicznej Biblioteki w Cegłowie" %></h1>
<h2>Książki:</h2>
<ol>
    <li><a href="listaKsiazek">Wyświetl listę książek</a></li>
    <li><a href="wyszukajKsiazke">Wyszukaj książkę</a></li>
    <li><a href="dodajKsiazke">Dodaj książkę</a></li>
    <li><a href="modyfikujLokalizacje">Modyfikuj lokalizację książki</a></li>
    <li><a href="usunKsiazke">Usuń książkę</a></li>
</ol>

<h2>Autorzy:</h2>
<ol>
    <li><a href="listaAutorow">Wyświetl listę autorów</a></li>
    <li><a href="dodajAutora">Dodaj autora</a></li>
    <li><a href="usunAutora">Usuń autora</a></li>
</ol>
</body>
</html>