package com.example.akelny;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Users {

    public class user {
        public String name;
        public String username;
        public String password;
        public String email;
        public String phoneNum;

        public user(String name, String username, String password, String email, String Phone) {
            this.name= name;
            this.username = username;
            this.password = password;
            this.email = email;
            this.phoneNum = Phone;
        }
    }


    public ArrayList<user> users = new ArrayList<>();

    public void Users() { // intialzing array
        users.add(new user("Amy Samy", "AmySamy1234", "Ayms123", "AmySamy@aucegypt.edu", "01270"));
        users.add(new user("Afifi", "Afifi1412", "Obsidian", "Mohamed_afifi@aucegypt.edu", "01115"));
        users.add(new user("Ali Sobhy", "AliSobhy22", "AlisPass", "AliSobhy@aucegypt.edu", "01001"));
        users.add(new user("Menna Wagdy", "MennaWagdy4321", "MennaPass", "MennaWagdy@aucegypt.edu", "01014"));
    }

    public boolean validateNumber(String number) {
        boolean validated = false;
        String regex;
        regex = "002010\\d{8}|002011\\d{8}|002012\\d{8}|002015\\d{8} ";
        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(number);
        validated = mt.matches();


        return validated;
    }

    public boolean validateEmail(String email) {
        boolean validated = false;
        String regex;
        regex = "\\w*@\\w*.edu";
        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(email);
        validated = mt.matches();

        return validated;
    }


    public boolean ValidateUser(String user, String pass) //login
    {
        boolean found1 = false, found2 = false, found3 = false, found4 = false;

        for (int i = 0; i < users.size(); i++) {
            user temp;
            temp = users.get(i);
            if (temp.username.equals(user))
                found1 = true;
            if (temp.password.equals(pass))
                found2 = true;
        }
        if (found1 && found2)
            return true;
        return false;
    }


    boolean verifyPassword(String password, String username) {

        if (password.length() < 8) {
            return false;
        }
        /*boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (c == '!' || c == '"' || c == '#' || c == '$' || c == '%' || c == '&' || c == '(' || c == ')' || c == '*' || c == '+' || c == ',' || c == '-' || c == '.' || c == '/' || c == ':' || c == ';' || c == '<' || c == '=' || c == '>' || c == '?' || c == '@' || c == '['  || c == ']' || c == '^' || c == '_' || c == '`' || c == '{' || c == '|' || c == '}' || c == '~') {
                hasSpecial = true;
            }
        }
        if (!hasLower || !hasUpper || !hasDigit || !hasSpecial) {
            return false;
        }
        if (Character.isDigit(password.charAt(0)) || Character.isDigit(password.charAt(password.length() - 1))) {
            return false;
        }
        if (password.equals(username) || new StringBuilder(password).reverse().toString().equals(username)) {
            return false;
        }*/
        return true;
    }

    public int register(String name, String username, String password, String confirmPassword, String email, String phoneNum) {


        for (int i = 0; i < users.size(); i++) {
            user temp;
            temp = users.get(i);
            if (temp.username.equals(username))
                return 1; //Username already exists
        }

        /*if (!verifyPassword(password, username))
        {
            System.out.println(password);
            return 2; //Password is invalid
        }*/
        if (!password.equals(confirmPassword))
            return 3; //passwords do not match

        if (!validateNumber(phoneNum)) {
            return 4;//number is not valid (note, it should start with 0020)
        }

        if (!validateEmail(email)) {
            return 5;//email is not valid (note, it should end with edu)
        }

        user userToRegister = new user(name, username, password, email, phoneNum);
        users.add(userToRegister);
        return 0;//successful registration
    }
}