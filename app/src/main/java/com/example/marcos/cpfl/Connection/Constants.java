package com.example.marcos.cpfl.Connection;

public class Constants {

    //    conexão principal
    private static final String ROOT_URL = "http://10.133.214.167:8080/cpfl/v1/";

    //    conexão secundária acessando os folders users e news
    private static final String ROOT_USERS= "users/";
    private static final String ROOT_TAXES = "taxes/";

//    acessando os métodos de POST e GET utilizando ROOT_URL + ROOT_USERS ou ROOT_NEWS e o restante to path em ""

    //    CALLS DE USER
    public static final String URL_REGISTER = ROOT_URL+ROOT_USERS+"createUser.php";
    public static final String URL_LOGIN = ROOT_URL+ROOT_USERS+"userLogin.php";
    public static final String URL_CHECK_USER = ROOT_URL+ROOT_USERS+"isUserRegistered.php";

    //    CALLS DE TAXAS
    public static final String URL_SHOW_TAXES = ROOT_URL+ROOT_TAXES+"getTaxes.php";
    public static final String URL_UPDATE_TAXES = ROOT_URL+ROOT_TAXES+"updateTaxes.php";
    public static final String URL_REGISTER_FATURA = ROOT_URL+ROOT_TAXES+"gerarFatura.php";
}
