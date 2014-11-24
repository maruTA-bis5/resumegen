package net.bis5.resumegen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GenerateServlet
 */
@WebServlet("/generate")
public class GenerateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RirekigenCore core = createRirekigenCore(request);
		if (core.isValidateRequest()) {
			response.getWriter().write(core.getValidateResult());
		} else {
			InputStream fileStream = core.getInputStream();
			if (fileStream != null) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "filename=\"" + "RirekiGen.xls" + "\"");

				BufferedOutputStream os = new BufferedOutputStream(response.getOutputStream());
				BufferedInputStream reader = new BufferedInputStream(fileStream);

				int read = -1;
				while ((read = reader.read()) >= 0) {
					os.write(read);
				}
				os.flush();
				reader.close();
				// XXX 出力が終わっても一時ファイルが削除されない
			} else {
				response.sendRedirect("/index.html");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RirekigenCore core = createRirekigenCore(request);
		String respJson = core.doProcess();
		response.setContentType("application/javascript");
		response.getWriter().write(respJson);
		response.getWriter().flush();
	}

	private RirekigenCore createRirekigenCore(HttpServletRequest request) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// Map<String, String> params = JSON.decode(request.getInputStream());
		Map<String, String> params = new HashMap<>();
		for (String key : Collections.list(request.getParameterNames())) {
			params.put(key, (String) request.getParameter(key));
		}
		RirekigenCore core = new RirekigenCore(params, getServletContext());
		return core;
	}
}
