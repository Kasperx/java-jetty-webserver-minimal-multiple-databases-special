package main.java.com.mywebsite.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Person
{
    int position;
    String vorname;
    String nachname;
    String activity;
    String activity_name;
    int header_position;
    String header_vorname;
    String header_nachname;
    String header_activity;
    String header_activity_name;
}