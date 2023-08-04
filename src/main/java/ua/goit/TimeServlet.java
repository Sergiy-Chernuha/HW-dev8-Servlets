package ua.goit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezone = req.getParameter("timezone");
        DateTimeFormatter formatter;
        ZonedDateTime dateTime;
        String formatDateTime;

        if (Objects.isNull(timezone)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
            dateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X");
            if (timezone.contains(" ")) {
                String replaced = timezone.replace(" ", "+");
                dateTime = ZonedDateTime.now(ZoneId.of(replaced));
            } else {
                dateTime = ZonedDateTime.now(ZoneId.of(timezone));
            }
        }

        formatDateTime = dateTime.format(formatter);
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(formatDateTime);
        resp.getWriter().close();
    }
}
