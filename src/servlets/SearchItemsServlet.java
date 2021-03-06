package servlets;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.Items;
import servlets.tools.templates.online.OnlineGetServlet;

/**
 * * @author Anagbla Joan */
public class SearchItemsServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		if(request.getParameter("query")==null )
			response.getWriter().print(Items.searchItems(
					"",
					(String) request.getSession().getAttribute("userId"))
					);
		else
		response.getWriter().print(Items.searchItems(
				request.getParameter("query"),
				(String) request.getSession().getAttribute("userId")));		
	}

}