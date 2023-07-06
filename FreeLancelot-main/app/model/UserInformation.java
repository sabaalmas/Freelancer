package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInformation {

    String id;
    String username;
    String company;
    String country;
    String registrationDate;
    String limitedAccount;
    String role;
    String emailVerified;
    String primaryCurrency;

    List <ProjectInfo> projectList = new ArrayList<>();

    public void setId(String id)
    {
        this.id=id;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public void setCompany(String company)
    {
        this.company=company;
    }

    public void setCountry(String country)
    {
        this.country=country;
    }

    public void setProjectList(List <ProjectInfo> pl)
    {
        this.projectList=pl;
    }

    public String getId()
    {
        return this.id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getCompany()
    {
        return this.company;
    }


    public String getCountry()
    {
        return this.country;
    }

    public List <ProjectInfo> getProjectList()
    {
        return this.projectList;
    }


    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        Date date = new Date(Long.parseLong(registrationDate+"000"));

        String pattern = "MMM-dd-yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        registrationDate = simpleDateFormat.format(date);
        this.registrationDate = registrationDate;
    }

    public void setLimitedAccount(String limitedAccount)
    {
        this.limitedAccount=limitedAccount;
    }

    public String getLimitedAccount()
    {
        return this.limitedAccount;
    }

    public void setRole(String role)
    {
        this.role=role;
    }
    public String getRole()
    {
        return this.role;
    }

    public void setEmailVerified(String emailVerified)
    {
        this.emailVerified=emailVerified;
    }

    public String getEmailVerified()
    {
        return this.emailVerified;
    }


    public void setPrimaryCurrency(String primaryCurrency)
    {
        this.primaryCurrency=primaryCurrency;
    }
    public String getPrimaryCurrency()
    {
        return this.primaryCurrency;
    }
}