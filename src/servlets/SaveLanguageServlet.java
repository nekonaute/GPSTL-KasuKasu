package servlets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.ExchangePoints;
import servlets.tools.templates.online.OnlineGetServlet;
/**
 * 
 * @author ouiza
 *
 */
public class SaveLanguageServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		 
	}
}