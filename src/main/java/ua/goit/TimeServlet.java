package ua.goit;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

        engine = new TemplateEngine();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezone = req.getParameter("timezone");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        String formattedDateTime;

        if (Objects.isNull(timezone)) {
            String timezoneFromCookie = getCookieValue(req, "lastTimezone");

            if (!Objects.isNull(timezoneFromCookie) && !timezoneFromCookie.equals("UTC")) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X");
                dateTime = ZonedDateTime.now(ZoneId.of(timezoneFromCookie));
            }

        } else if (timezone.equals("UTC")) {
            resp.addCookie(new Cookie("lastTimezone", timezone));
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X");

            if (timezone.contains(" ")) {
                timezone = timezone.replace(" ", "+");
            }

            resp.addCookie(new Cookie("lastTimezone", timezone));
            dateTime = ZonedDateTime.now(ZoneId.of(timezone));
        }

        formattedDateTime = dateTime.format(formatter);
        resp.setContentType("text/html; charset=utf-8");

        Map<String, Object> contextMap = new LinkedHashMap<>();
        contextMap.put("CurrentTime", formattedDateTime);

        Context simpleContext = new Context(req.getLocale(), contextMap);

        engine.process("test", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }

    private String getCookieValue(HttpServletRequest req, String searchCookie) {
        String cookies = req.getHeader("Cookie");

        if (cookies == null) {
            return null;
        }

        String[] separateCookies = cookies.split(";");
        for (String oneCookies : separateCookies) {
            String[] keyValue = oneCookies.split("=");

            if (searchCookie.equals(keyValue[0].trim())) {
                return keyValue[1];
            }
        }

        return null;
    }
}
