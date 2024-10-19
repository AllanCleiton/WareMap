package com.allancleitonppma.gmail.WareMap;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.allancleitonppma.gmail.WareMap.core.LoadOrder;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.services.UtilService;

@SpringBootApplication
public class WareMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(WareMapApplication.class, args);

		/*
		 * final int numberOfChambers = 6; int choice = 0; String path;
		 * 
		 * 
		 * 
		 * UtilService service = new UtilService(); Scanner sc = new Scanner(System.in);
		 * 
		 * 
		 * System.out.
		 * println("****SISTEMA DE GERACAO AUTOMATICA DE SEPARACAO DE CARGA****\n");
		 * System.out.print("    INSIRA ARQUIVO FONTE -> "); path = sc.nextLine();
		 * 
		 * List<Chamber> chambers = service.chargeCameras(path, numberOfChambers, sc);
		 * 
		 * 
		 * //limpar("cmd", "/c", "cls");
		 * 
		 * do { System.out.println("Escolha uma das opções abaixo.\n");
		 * System.out.println("Separacao personalizada:............(1).");
		 * System.out.println("Separacao simples:..................(2).");
		 * System.out.println("Imprimir Camara:....................(3).");
		 * System.out.println("Sair:...............................(4).");
		 * 
		 * choice = sc.nextInt(); switch (choice) { case 1: { break; } case 2: { break;
		 * } case 3: { //limpar("cmd", "/c", "cls");
		 * System.out.println("***************IMPRESSÃO DE CÂMARAS***************\n");
		 * System.out.print("Informe o número da câmara: "); int numCam = sc.nextInt();
		 * System.out.println("\n---------------------CAMÂRA " + numCam +
		 * "---------------------"); service.printRoadOfChamber(chambers, c ->
		 * c.getChamber() == numCam); break; } default: System.out.println("Sair..."); }
		 * } while (choice != 4);
		 * 
		 * 
		 * 
		 * sc.close();
		 */

		Scanner sc = new Scanner(System.in);

		UtilService service = new UtilService();

		LoadOrder order = service.getloadOrder("c://temp//ordemdecarga.txt", 8689);

		Separation fS = service.simpleSeparation(order, service.chargeCameras("c://temp//test.txt", 5, sc));

		// System.out.println(fS);

		sc.close();
	}

	public static void limpar(String arg0, String arg1, String arg2) {
		try {
			ProcessBuilder builder = new ProcessBuilder(arg0, arg1, arg2);
			Process processo = builder.start();

			// Aguarda o processo terminar
			int status = processo.waitFor();

			// Lê a saída do processo
			BufferedReader reader = new BufferedReader(new InputStreamReader(processo.getInputStream()));
			String linha;
			while ((linha = reader.readLine()) != null) {
				System.out.println(linha);
			}

			reader.close();
			if (processo.isAlive()) {
				System.out.println(status);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
