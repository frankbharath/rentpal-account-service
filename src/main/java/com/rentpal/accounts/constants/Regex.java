package com.rentpal.accounts.constants;

public class Regex {
    public static final String EMAIL="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String PASSWORD="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,64}$";
    public static final String ALPHANUM="^[a-zA-Z0-9]+$";
}
