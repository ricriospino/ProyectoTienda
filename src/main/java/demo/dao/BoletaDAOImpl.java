package demo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import demo.bd.PoolConexiones;
import demo.bean.BoletaBean;
import demo.bean.CarritoCompraBean;
import demo.bean.ClienteNaturalyJuridicoBean;
import demo.bean.EmpleadoBean;


public class BoletaDAOImpl implements BoletaDAO {

	@Override
	public int insertarBoleta(BoletaBean boleta) {
		//INSERT INTO tb_boleta(id_empleado,fecha_boleta,id_cliente,estado_boleta,total_venta) 
		//VALUES (1,2020-09-02 22:18:10,5,1,100);
		
		Connection cnx =null;
		PreparedStatement pst = null;
		int registros = 0;
		
try{
			
			//Paso 1: Crear La Conexion
			//cnx = new Miconexion().getConexion(); 
			cnx = PoolConexiones.getConexion();
			
			//Paso 2: Preparamos la sentencia SQL
			String sql ="INSERT INTO tb_boleta(id_empleado,fecha_boleta,id_cliente,estado_boleta,total_venta)" + 
					"VALUES (?,?,?,?,?)";
			pst = (PreparedStatement) cnx.prepareStatement(sql);
			pst.setInt(1, boleta.getIdEmpleado());
			pst.setDate(2, (Date) boleta.getFechaBoleta());
			pst.setInt(3, boleta.getIdCliente());
			pst.setByte(4, boleta.getEstadoBoleta());
			pst.setDouble(5, boleta.getTotalBoleta());
			
			
			//paso 3: Enviamos sentencia SQL a la base de datos
			registros = pst.executeUpdate();//para insert , update,delete
			
			System.out.println("Registros Insertados: "+ registros);
	
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try{
				//cerrar la conexion a la base de datos
				if(pst !=null) pst.close();
				if(cnx !=null) cnx.close();
			
			}catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
	
		return registros;
	}
	
	@Override
	public boolean insertarVenta(EmpleadoBean empleado, ClienteNaturalyJuridicoBean cliente,
			List<CarritoCompraBean> lstCarrito, double totalBoleta) {
	
		
		Connection cnx =null;
		PreparedStatement pst = null;
		int registrosAfectados = 0;
		boolean exito = false;
		
		try {
			
			cnx = PoolConexiones.getConexion();		
			cnx.setAutoCommit(false);
			//insertar cabecera
			int boleta_emitida = 1;
			long idBoleta;
			
			String sql ="INSERT INTO tb_boleta(id_empleado,fecha_boleta,id_cliente,estado_boleta,total_venta)" + 
					"VALUES (?,?,?,?,?)";
			
			pst = (PreparedStatement) cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setLong(1, empleado.getId());
			//pst.setDate(2, convert(new java.util.Date())); solo devuelve la fecha 
			pst.setTimestamp(2, getFechaHoraActual() );
			pst.setInt(3, cliente.getId());
			pst.setInt(4, boleta_emitida);
			pst.setDouble(5, totalBoleta);
			registrosAfectados = pst.executeUpdate();
			
			if(registrosAfectados == 0) {
				// forzar un error para java y que entre al catch
				throw new SQLException("Cabecera boleta no insertada!");
			}
			
			//obtiene el id generado
			try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	idBoleta = generatedKeys.getLong(1);
		            }else {
		                throw new SQLException("Creacion de Boleta fallida, no tiene Id.");
		            }
		    }
			
			System.out.println("id boleta " + idBoleta);
			
			//insertar detalle (recorrer un for)
			for(CarritoCompraBean carrito: lstCarrito) {
				
				 sql ="INSERT INTO tb_detalle_boleta(id_boleta,id_producto,cantidad,sub_total)" + 
						"VALUES (?,?,?,?)";
				pst = (PreparedStatement) cnx.prepareStatement(sql);
				pst.setLong(1, idBoleta);
				pst.setLong(2, carrito.getId_producto());
				pst.setInt(3, carrito.getCantidad());
				pst.setDouble(4, carrito.getSubTotal());
				pst.executeUpdate();
				
				
			}
			
			// actualizar stocks (recorrer con un for)
					
			
			for (CarritoCompraBean carrito: lstCarrito) {
				
				sql ="UPDATE tb_producto SET stock_actual = ? WHERE id_producto = ? ";
				
				pst = (PreparedStatement) cnx.prepareStatement(sql);
				pst.setInt(1, carrito.getStockActual());
				pst.setLong(2, carrito.getId_producto());
				pst.executeUpdate();
			}
			
			// si termina correctamente commitea 
			cnx.commit();
			exito = true;
			
		} catch (Exception e) {
			try {
				// si falla roollback
				cnx.rollback();
				exito = false;
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			try {
				// si falla o no falla cierra la conexion 
				cnx.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return false;
	}
	// SOLO FECHAS 
    private static java.sql.Date convert(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
    
    //FECHA Y HORA  
    private static java.sql.Timestamp getFechaHoraActual() {	
        return new java.sql.Timestamp(new java.util.Date().getTime());
  
    }
	
}

