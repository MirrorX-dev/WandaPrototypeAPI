package com.wandaprototype.IaaS.information.db;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DbManagerSSH {

	/** Listado para Querys: */
	public static List<String> partido_query = new ArrayList<String>();
	
	/** knowHost extrae la huella para evitar un intento de ataque Man-In-The-Middle */
	private static String knownHostPublicKey = "158.101.98.158 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDTULimuMguOvnMBPqaCbD7me4622EHZUMteOhOcKmP/puHSGFFDbXegEizQ1nJRng3coxt7lk+VYQXtmxECDuvyLvOCPng47jWttOD5ppST6xkrTquUqTwBQmnIgRPQ322KFuuL5yzr6BlzzrzlhlGGX8gsCmqzfUXMo5Pof7nXhVKl4dMIczeLeCREv9r4PCMtzLRKAI/d0Of7i/Bhfs1IYMlpTRBq3SxUoZOgRMfpo/ONyOoTIBKTWJRFPj4mf/laRv73BgnlkQzlaRCXR6Ytb3qI7CCz6ktWh53t9w0kI5wDFQiJBSQSJD74WtTSzVEIk+vYqNDjPcir0H2Br5e3Z7hPkJa4hqPNvP4CQRz9ntua/LLY5ELZrZiys2blCd1P0Fs/ZK44XLEXjkAezZ12ymjx3v+UyY1cXhhTZ34+uq8nDsJNc4jfBhH6XrgEjkv2sZZBd/rA9estQ3A+IXvwMQu9dx/il3DveMvkd77Wu9HlwUZ0Idts0Bb6l0L1Jk=";
	
	private static PreparedStatement ps = null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static Connection con;
	private static Session session;
	
	private static String databaseUsername = "dbpeople";
	private static String databasePassword = "helloworld2022";
	
	/**
	 * Construye un tunel de acceso seguro mediante SSH de tipo cifrado para
	 * conectarse al servidor de forma segura.
	 * Inicia sesión con un usuario guardado
	 * @return Devuelve Connnection, permite trabajar sobre la conexión y driver de la base de datos.
	 * @throws JSchException
	 * @throws SQLException
	 */
	public static Connection conectar() throws JSchException, SQLException {
		String jumpserverHost = "158.101.98.158";
		String jumpserverUsername = "opc";

		String databaseHost = "localhost";
		int databasePort = 3306;

		JSch jsch = new JSch();
		jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
		//jsch.addIdentity("C:\\Users\\MirrorX\\Documents\\Oracle Cloud\\ssh-key-2022-05-13.key");
		jsch.addIdentity("/dev/keys/ssh.key");

		session = jsch.getSession(jumpserverUsername, jumpserverHost);
		session.connect();

		int forwardedPort = session.setPortForwardingL(0, databaseHost, databasePort);

		String url = "jdbc:mysql://localhost:" + forwardedPort;
		con = DriverManager.getConnection(url, databaseUsername, databasePassword);
		return con;
	}


	/**
	 * Cierra el hilo de conexión con la base de datos.
	 */
	public static void CloseDataBaseConnection() {
        try {
            if (con != null && !con.isClosed()) {
                System.out.println("Closing Database Connection");
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(ps);
                DbUtils.closeQuietly(st);
                DbUtils.closeQuietly(con);
                //con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

	/**
	 * Cierra el hilo de conexión con el tunel SSH hacia el servidor.
	 */
    public static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }
    
   // QUERYS HERE:
    /**
	 * Comprueba la conexión.
	 * Se genera una conexión tunel ssh hacia el servidor y se usa
	 * el driver para realizar consultas SQL.
	 */
    public static boolean checkConnection() throws JSchException {
        boolean exist = false;
        try {
        	conectar();
            st = con.createStatement();
            String sql = "select schema_name as database_name\r\n" + 
            		"from information_schema.schemata";
            st.setMaxRows(1);
            rs = st.executeQuery(sql);
            

            if (rs.next()) {
                exist = true;
                System.out.println("Conexión a la bbdd con éxito: "+exist);
            }
            
        } catch (SQLException ex) {
        	ex.printStackTrace();
            exist = false;       
            System.err.println("Error al conectar a la base de datos: "+exist+" Intentado con driver antiguo");        
        } finally {
        }
        
        return exist;
    }
    
    /**
     * Permite publicar datos pasados por parametros.
     * Puede ser reutilizado para otros estadios
     * siguiendo la estructura y orden del dato
     * introducido.
     * @param table
     * @param idpartido
     * @param compe
     * @param elocal
     * @param evisit
     * @param jor
     * @param fechapa
     * @param hora
     * @param estad
     */
    public void postData(String table, String idpartido, String compe, String elocal, String evisit, String jor, String fechapa, String hora, String estad) {	
		try {
			//Comprueba la conectividad con la bbdd. #1 consulta.
			//if (checkConnection()!=false)  {
				try {
					Statement st = con.createStatement();
					st.setMaxRows(1);
					st.execute("INSERT INTO `wandaprototype`.`"+table+"` (`idpartido`, `competicion`, `equipolocal`, `equipovisit`, `jornada`, `fechapartido`, `horapartido`, `estadiopartido`) VALUES ('"+idpartido+"','"+compe+"', '"+elocal+"', '"+evisit+"', '"+jor+"','"+fechapa+"','"+hora+"','"+estad+"')");
					
				} catch (Exception e) {
					System.err.println("Got an exception!");
					System.err.println(e.getMessage());
				}
			//}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
        }
	}
    
    
    /**
     * Permite publicar datos pasados por parametros.
     * Puede ser reutilizado para otras versiones de estadios_partidos
     * siguiendo la estructura y orden del dato
     * introducido.
     * @param table
     * @param version_number
     */
	public void postDataVersion(String table, int version_number) {
		try {
			try {
				Statement st = con.createStatement();
				st.setMaxRows(1);
				st.execute("INSERT INTO `wandaprototype`.`"+table+"` (`version_number`) VALUES ('"+version_number+"')");

			} catch (Exception e) {
				System.err.println("Error al intentar publicar los datos!");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
    
	/**
	 * Obtiene mediante una consulta reutilizable para cualquier estadio
	 * devuelve un estado de tipo booleano.
	 * Si existe el identificador, devuelve el valor adjunto.
	 * @param table
	 * @param idpartido
	 * @return true/false si existe.
	 */
    public boolean getDataPerID(String table, String idpartido) {
		boolean output=false;			
			try {
					try {
						String query = "select idpartido from wandaprototype."+table+" where idpartido = ?";
						ps = con.prepareStatement(query);
						ps.setString(1, idpartido);
						ps.setMaxRows(1);
						rs = ps.executeQuery();
						
						//The result of the select was Empty or With Data equivalent to return false or true.
						if (!rs.isBeforeFirst() ) {    
					          //System.out.println("empty"); 
					          output = false;
					    } else {
					          //System.out.println("with data"); 
					          output = true;
					    }
							
					} catch (Exception a) {
						System.err.println("[Error] | La operación PreparedStatement no puedo completarse +[INFO | log]");
						a.printStackTrace();
					}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
	        }
		
		
		return output;

	}
    
    /**
     * Obtiene mediante una consulta reutilizable para cualquier estadio
	 * devuelve un estado de tipo booleano.
	 * Si existe el identificador, devuelve el valor adjunto. 
     * @param miversion
     * @param mifecha
     * @param table
     * @return true/false si existe.
     */
	public boolean ComprobarUltimaVersion(int miversion, Timestamp mifecha, String table) {
		boolean output = false;
		try {
			int lastVersion;
			Timestamp lastDate = null;

			String query = "SELECT * FROM wandaprototype." + table + " WHERE version_number=(SELECT MAX(version_number) FROM wandaprototype." + table + ")";
			rs = st.executeQuery(query);
			while (rs.next()) {
				lastVersion = rs.getInt("version_number");
				lastDate = rs.getTimestamp("version_date");
				System.out.println(lastVersion);
				System.out.println(lastDate);
			}

			if (lastDate.after(mifecha)) {
				// System.out.println("La fecha del último registro es más reciente");
				output = true;
				// output por defecto = false. No es más reciente
			}

		} catch (Exception a) {
			a.printStackTrace();
		}

		return output;
	}
	
	/**
	 * Consulta reutilizable, comprueba si existen datos en una
	 * tabla. Principalmente utilizado para evitar errores
	 * en la clasificación de los datos.
	 * @param table
	 * @return true/false si existe.
	 */
	public Boolean ComprobarContieneDatos(String table) {
		boolean output = false;
		try {
			int count = -1; //-1 ERROR Identi. Value, for Debugging.
			String query = "SELECT EXISTS (SELECT 1 FROM wandaprototype." + table + ") as count";
			rs = st.executeQuery(query);
			while (rs.next()) {
				count = rs.getInt("count");
				//System.out.println(count);
			}

			if (count<=0) {
				output = false;
			} else {
				output = true;
			}

		} catch (Exception a) {
			a.printStackTrace();
		}

		return output;
	}
	
	/**
	 * Obtiene la ultima versión de la tabla que pasemos por valor
	 * si el valor existe, devuelve la ultima versión.
	 * @param table
	 * @return 0 si no existe, ó Nº versión del servidor. Ej: 46
	 */
	public int getUltimaVersion(String table) {
		int lastVersion = 0;
		try {
			String query = "SELECT * FROM wandaprototype." + table + " WHERE version_number=(SELECT MAX(version_number) FROM wandaprototype." + table + ")";
			rs = st.executeQuery(query);
			while (rs.next()) {
				lastVersion = rs.getInt("version_number");
				//System.out.println(lastVersion);
			}

		} catch (Exception a) {
			a.printStackTrace();
		}

		return lastVersion;
	}
    
}
