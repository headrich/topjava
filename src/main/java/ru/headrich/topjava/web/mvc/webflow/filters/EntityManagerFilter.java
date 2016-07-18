package ru.headrich.topjava.web.mvc.webflow.filters;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Montana on 10.07.2016.
 */
public class EntityManagerFilter implements Filter {
    //мы можем сам управлять  временем жизни сессии, и сам открывать закрывать ее до и после выполнения запроса соответсвенно.
    //если мы будем использовать Em это годится. В случае с хиберовской сессией, можно делегирвоать эту задачу хибернейту- current_session_context_class=thread - будет создавать экземпляр сессии на поток
    //в случае сервлетов -один запрос от клиента - один поток.
    //пока нам это не нужно. Но принять к сведению надо. Хотя если свзывать сессию хибера с сессией хттп, то это годится. И тогда можно сделать не фильтром, а листенером.
    // Если нам нужен один экземпляр сессии, а она не thread-safe то нужно обернуть ее в ThreadLocal  и выдвать ее оттуда.
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
