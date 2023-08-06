package ua.goit;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver ();

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
        DateTimeFormatter formatter;
        ZonedDateTime dateTime;
        String formatDateTime;

        if (Objects.isNull(timezone) || timezone.equals("UTC")) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
            dateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X");
            timezone = URLEncoder.encode(req.getParameter("timezone"), "UTF-8");

            dateTime = ZonedDateTime.now(ZoneId.of(timezone));
        }

        formatDateTime = dateTime.format(formatter);
        resp.setContentType("text/html; charset=utf-8");

        Map<String, Object> contextMap = new LinkedHashMap<>();
        contextMap.put("CurrentTime", formatDateTime);

        Context simpleContext = new Context(req.getLocale(), contextMap);

        engine.process("test", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
