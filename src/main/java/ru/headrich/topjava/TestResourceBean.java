package ru.headrich.topjava;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Name;

/**
 * Created by Montana on 19.07.2016.
 */
@Component("testResourceBean")
public class TestResourceBean  {
    String name="fadak";
    //@Resource(name = "testDependResourceBean") //resource для jndi ресурсов типа datasource,
    @Autowired
    TestDependResourceBean tdrb;
            //= new TestDependResourceBean("Petrov");

    public TestResourceBean(String name, TestDependResourceBean tdrb) {
        this.name = name;
        this.tdrb = tdrb;
    }

    public TestResourceBean(TestDependResourceBean tdrb) {
        this.tdrb = tdrb;
    }

    public TestDependResourceBean getTdrb() {
        return  tdrb;
    }

    public void setTdrb(TestDependResourceBean tdrb) {
        this.tdrb = tdrb;
    }

    public String getFullyName(){

         /*   try {
                InitialContext ctx = new InitialContext();
                 tdrb = (TestDependResourceBean) ctx.lookup("java:comp/env/testDependResourceBean");
                tdrb.setLastname("Ivanov");

            } catch (NamingException e) {
                e.printStackTrace();
            }*/

        return name+ tdrb.getLastname();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public TestResourceBean() {
    }
}
