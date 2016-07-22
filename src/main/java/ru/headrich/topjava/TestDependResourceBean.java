package ru.headrich.topjava;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

/**
 * Created by Montana on 20.07.2016.
 */
@Component
public class TestDependResourceBean {
    String lastname;

    public TestDependResourceBean(String lastname) {
        this.lastname = lastname;
    }

    public TestDependResourceBean() {
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @PostConstruct
    public void LogIt(){
        System.out.println("______________________________TestDependResourceBean is Constructed____________________________________");
    }
}
