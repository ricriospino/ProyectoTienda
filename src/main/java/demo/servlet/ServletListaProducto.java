package demo.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import demo.bean.ProductoBean;
import demo.dao.ProductoDAO;
import demo.dao.ProductoDAOImpl;

/**
 * Servlet implementation class ServletListaProducto
 */
@WebServlet("/ServletListaProducto")
public class ServletListaProducto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletListaProducto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		System.out.println(" ini : ServletListaProducto - doGe()");
		
		ProductoDAO dao = new ProductoDAOImpl();	
		List<ProductoBean>lstProducto = dao.listarProducto();
		
		request.setAttribute("listaProducto", lstProducto);	
		RequestDispatcher despachador = null;		
		String lista = request.getParameter("lista");
		
		if("L".equals(lista)) {
			despachador = request.getRequestDispatcher("/listados/listaProducto.jsp");
			
		}else if ("C".equals(lista)) {
			
			despachador = request.getRequestDispatcher("ventaFinal.jsp");		
		}
			
		despachador.forward(request, response);
			
		System.out.println("fin: ServletListaProducto - doGet ");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
