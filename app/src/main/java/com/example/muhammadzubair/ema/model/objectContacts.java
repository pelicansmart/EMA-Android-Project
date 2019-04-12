package com.example.muhammadzubair.ema.model;

public class objectContacts
{
    public String Name;
    public String Number;
    private String listName;

    public objectContacts(String name, String nmuber) {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

}
