package ru.headrich.topjava.util;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Created by Montana on 08.07.2016.
 */
public class MyHTTPServletResponseWrapper extends HttpServletResponseWrapper{

    public MyHTTPServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public boolean isClosed(){

        return isCommitted();
        //lol вот заем врапперы нужны. Так там не было метода исклозе, я захотел его добавить и обернул его унаследовавшись.
        //затем добавил нужный мне метод и вуаля.
    }

}
