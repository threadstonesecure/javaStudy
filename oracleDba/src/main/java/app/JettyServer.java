package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;

/**
 * Created by denglt on 2015/12/22.
 */
public class JettyServer {

	private static final String WEBROOT = "/webapp/";
	private int port;

	public JettyServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		WebAppContext webAppContext = new WebAppContext();
		URL webRoot = this.getClass().getResource(WEBROOT);
		webAppContext.setResourceBase(webRoot.toString());
		webAppContext.setContextPath("/");

		webAppContext.setAttribute(
				"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/[^/]*jstl.*\\.jar$");
		org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList
				.setServerDefault(server);
		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classlist.addBefore(
				"org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");

		server.setHandler(webAppContext);
		server.start();
		server.join();

	}

	public static void main(String[] args) throws Exception {
		JettyServer server = new JettyServer(8090);
		server.start();
	}
}
