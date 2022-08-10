package main.java.com.mywebsite.Data;

public class Person
{
    public String header_id;
    public String header_firstName;
    public String header_lastName;
    public String header_password;
    public int id;
    public String firstName;
    public String lastName;
    public String password;
    public boolean isAdmin;
    public Person() {}
    public Person(String firstName, String lastName, boolean header)
    {
        if(header) {
            this.header_firstName = firstName;
            this.header_lastName = lastName;
        } else {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
    public Person(int id, String firstName, String lastName, String password, boolean header)
    {
        if(header) {
            this.header_id = String.valueOf(id);
            this.header_firstName = firstName;
            this.header_lastName = lastName;
            this.header_password = password;
        } else {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
        }
    }
//    public Person(String firstName, String lastName)
//    {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//    public Person(String firstName, String lastName, String password, boolean isAdmin)
//    {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.password = password;
//        this.isAdmin = isAdmin;
//    }
}
