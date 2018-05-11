package com.example.marcos.cpfl.Connection;

public class Constants {

    //    conexão principal
    private static final String ROOT_URL = "http://10.133.64.122:8080/cpfl/v1/";

    //    conexão secundária acessando os folders users e news
    private static final String ROOT_USERS= "users/";
//    private static final String ROOT_NEWS = "news/";

//    acessando os métodos de POST e GET utilizando ROOT_URL + ROOT_USERS ou ROOT_NEWS e o restante to path em ""

    //    CALLS DE USER
    public static final String URL_REGISTER = ROOT_URL+ROOT_USERS+"createUser.php";
//    public static final String URL_LOGIN = ROOT_URL+ROOT_USERS+"userLogin.php";

    //    CALLS DE NOTICIAS
//    public static final String URL_REGISTER_NEWS = ROOT_URL+ROOT_NEWS+"registerNews.php";
//    public static final String URL_SHOW_NEWS = ROOT_URL+ROOT_NEWS+"getAllNews.php";
}
