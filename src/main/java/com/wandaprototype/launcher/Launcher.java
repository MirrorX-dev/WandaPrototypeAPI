package com.wandaprototype.launcher;

import com.wandaprototype.IaaS.information.db.DbManagerSSH;
import com.wandaprototype.IaaS.scrambler.Matchs.ScramblerMatchs_WandaMetropolitano;

/**
 * Launcher permite la ejecuci�n organizada de las clases y funciones
 * correspondientes instanciadas en su necesidad de ejecuci�n.
 * @author MirrorX
 */
public class Launcher {

	/**
	 * La secuencia del lanzador se define en esta funci�n:
	 */
	private static void launcherSequence() {
		try {
			if (DbManagerSSH.checkConnection()) {
				Scramblers_Estadio_WandaMetropolitano();
				DbManagerSSH.CloseDataBaseConnection();
				DbManagerSSH.CloseSSHConnection();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * La recopilaci�n de cada estadio se realiza mediante la ejecuci�n de hilos.
	 * Permite que la ejecuci�n del programa se realiza de forma m�s r�pida y segura.
	 */
	private static void Scramblers_Estadio_WandaMetropolitano() {
		// Hilo Scrambler Partidos en el Wanda Metropolitano.
		Thread thread_ScramblerMatchs = new Thread() {
			public void run() {
				try {
					ScramblerMatchs_WandaMetropolitano.main(null);
					
					//Versiones Partidos.
					Integer lastversion;
					new ScramblerMatchs_WandaMetropolitano();
					
					
					if ((!ScramblerMatchs_WandaMetropolitano.checkConfirmation.isEmpty()) && ScramblerMatchs_WandaMetropolitano.checkConfirmation.get(0)==true) {
						System.out.print("Nuevos datos [Detectados]: "+ScramblerMatchs_WandaMetropolitano.checkConfirmation.get(0)+". \n");
						if (new DbManagerSSH().ComprobarContieneDatos("wandametropolitano_versions")==true) {
							lastversion = 1+Integer.valueOf(new DbManagerSSH().getUltimaVersion("wandametropolitano_versions"));
							new DbManagerSSH().postDataVersion("wandametropolitano_versions", lastversion);
						} else {
							lastversion = 99;
							new DbManagerSSH().postDataVersion("wandametropolitano_versions", lastversion);
						}			
					} else {
						System.out.print("No existen nuevos datos [No detectados]: "+ScramblerMatchs_WandaMetropolitano.checkConfirmation.size()+". \n");
					}
					
				} catch (Exception e) {
					// TODO: Registrar logs.
					e.printStackTrace();
				}
			}
		};
		
		/**
		 * Ejecuta los hilos.
		 */
		thread_ScramblerMatchs.run();
		
		//Secuencial:
		
	}

	/**
	 * Cuando llamamos al m�todo main en una ejecuci�n Java
	 * permite llamar a la ejecuci�n de las funciones declaradas
	 * en el Launcher
	 * Finaliza su ejecuci�n una vez terminado.
	 * @param args
	 */
	public static void main(String[] args) {
		launcherSequence();
		System.exit(1);
	}

}
