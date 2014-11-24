package net.bis5.resumegen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.StringUtils;

public class Bootstrap {

    public static final String ENV_PORT = "RESUMEGEN_HTTP_PORT";

    public static final String DEF_PORT = "8080";

    private static final Logger LOG = Logger.getLogger( Bootstrap.class.getCanonicalName());

    public static void main( String... args) throws Exception {
        Tomcat tomcat = new Tomcat();

        String port = System.getenv( ENV_PORT);
        if ( StringUtils.isEmpty( port)) {
            port = DEF_PORT;
        }
        try {
            int portNum = 0;
            if ( (portNum = Integer.valueOf( port)) != 0) {
                tomcat.setPort( portNum);
            }
        }
        catch ( NumberFormatException e) {
            LOG.log( Level.SEVERE, ENV_PORT + " is not a port number");
            return;
        }

        Path workDir = Paths.get( "../../src/main/webapp");
        Context cxt = tomcat.addWebapp( "/resumegen", workDir.toString());
        Tomcat.addServlet( cxt, "generate", new GenerateServlet());
        cxt.addServletMapping( "/generate", "generate");

        tomcat.start();
        tomcat.getServer().await();
    }

}
