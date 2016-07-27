/**
 * Created by Montana on 25.07.2016.
 */
public class B extends Main {
    String variable = null;

    public B(){
        System.out.println("variable value = " + variable);
    }

    protected void printVariable(){
        variable = "variable is initialized in B Class";
    }
}