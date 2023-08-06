package ua.goit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Objects;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezone = request.getParameter("timezone");

        if (Objects.isNull(timezone)
                || isCorrectZoneId(URLEncoder.encode(request.getParameter("timezone"), "UTF-8")))
        {
            chain.doFilter(request, response);
        } else {
            response.setStatus(400);
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write("Invalid timezone");
            response.getWriter().close();
        }
    }

    private boolean isCorrectZoneId(String timezone) {
        try {
            ZoneId.of(timezone);

            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
