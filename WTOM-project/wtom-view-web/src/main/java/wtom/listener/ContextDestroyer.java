package wtom.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.lang.reflect.Method;

@WebListener
public class ContextDestroyer implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Iniciando a limpeza de drivers e threads do JDBC para evitar Memory Leaks...");

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == event.getServletContext().getClassLoader()) {
                try {
                    DriverManager.deregisterDriver(driver);
                    System.out.println("Desregistado driver JDBC: " + driver);
                } catch (SQLException e) {
                    System.err.println("Erro ao desregistar driver " + driver + ": " + e);
                }
            }
        }

        try {
            Class<?> clazz = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            Method method = clazz.getDeclaredMethod("shutdown", boolean.class);
            method.setAccessible(true);
            method.invoke(null, true);
            System.out.println("Thread de Limpeza de Conexões Abandonadas do MySQL desligada via Reflection.");
        } catch (ClassNotFoundException e) {
            System.out.println("O MySQL driver class não foi encontrado. Pulando a limpeza da thread.");
        } catch (NoSuchMethodException e) {
            System.err.println("Método de desligamento da thread do MySQL não encontrado.");
        } catch (Exception e) {
            System.err.println("Falha ao desligar a thread de limpeza do MySQL via Reflection: " + e.getMessage());
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("ContextDestroyer inicializado.");
    }
}
