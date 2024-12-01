package com.allancleiton.waremap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.allancleiton.waremap.config.ParameterProduct;
import com.allancleiton.waremap.config.parameters.Cold_in_state;
import com.allancleiton.waremap.config.parameters.Cold_out_state;
import com.allancleiton.waremap.config.parameters.Floor_separation;
import com.allancleiton.waremap.config.parameters.Frozen;
import com.allancleiton.waremap.config.parameters.GeneralParameter;
import com.allancleiton.waremap.entities.Category;
import com.allancleiton.waremap.entities.Separation;
import com.allancleiton.waremap.entities.DTO.EntryProduct;
import com.allancleiton.waremap.entities.enums.SeparationSet;
import com.allancleiton.waremap.services.IntegrationService;
import com.allancleiton.waremap.services.SeparationFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class WareMapApplication {
	public static void main(String[] args) {
		SpringApplication.run(WareMapApplication.class, args);
		Scanner sc = new Scanner(System.in);		
		String path = "temp"; //src/main/resources/temp
		SeparationFactory factory = null;
		Properties log = new Properties();
		
		 try (InputStream input = new FileInputStream(path + "/config/logs/log.properties")) {
			 log.load(input); 
	        } catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		int choice = 0;		
		try {
			String c;
			System.out.println("\n\n Bem vindo ao Sistema de separação automática de carga WareMap.");
			
			if(!(log.getProperty("exit").equals("true"))) {
				System.out.print(" Deseja iniciar a partir da ultima cessão? S/N:  ");
				c = sc.nextLine().toUpperCase();

				if(c.equals("S")) {
					factory = new SeparationFactory(new IntegrationService(path, 1), sc);
				}else {
					factory = new SeparationFactory(new IntegrationService(path, 0), sc);
				}
			}else {
				factory = new SeparationFactory(new IntegrationService(path, 0), sc);
			}
			
			log.setProperty("exit", "false"); 
			try (OutputStream output = new FileOutputStream(path + "/config/logs/log.properties")) {
	            // Armazena o objeto Properties no arquivo com uma possível mensagem de comentários (null nesse caso).
	            log.store(output, null);
	        }
			
			System.out.println(" Presione ENTER para continuar...");
			sc.nextLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//factory.getAllProducts().forEach(System.out::println);
		clearScreen();
		
		do {
			System.out.println(Color.ANSI_PURPLE_BACKGROUND + " WELCOME TO THE WAREMAPE APLICATION          "+ Color.ANSI_RESET);
			System.out.println(" Gerar Separação:..................(1).");
			System.out.println(" Configurações:....................(2).");
			System.out.println(" Sair:.............................(3).");
			System.out.print(" -> "); 
			choice = sc.nextInt();
			clearScreen();
			switch (choice) {
			case 1: {
				gerarSeparacao(sc, path, factory);
				break;
			}
			case 2: {
				configuracao(path ,sc);
				break;
			}
			case 3: {
				System.out.println(" Saindo.");
				log.setProperty("exit", "true"); 
				try (OutputStream output = new FileOutputStream(path + "/config/logs/log.properties")) {
		            // Armazena o objeto Properties no arquivo com uma possível mensagem de comentários (null nesse caso).
		            log.store(output, null);
		        } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				factory.getRepository().shutDownDb(path);
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choice);
			}
		} while (choice != 3);

		sc.close();


	}

	public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Comando para Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Comando para Linux e macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar o terminal: " + e.getMessage());
        }
     }

	public static void tipycalSeparation(Scanner sc, String defaultPath, SeparationFactory factory) {
		Separation separation = null;
		//SeparationFactory factory = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean verify = false;
		boolean success = false; 
	
		while (!verify) {
			try {
				
				success = new File(defaultPath + "/separations").mkdir();
				
				if(success) {
					prefix = defaultPath + "/separations/";		
	
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = prefix + oc;
					
				}else {
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = defaultPath + "/separations/" + oc;				
				}

				
				//factory = new SeparationFactory(new IntegrationService(defaultPath), sc);
								
				separation = factory.simpleSeparation();		 	
				
				verify = separation.createArquiveWithSeparation(finalPath);
				
				if(verify) {
					System.out.println(Color.ANSI_PURPLE_BACKGROUND + " Separação de carga.                         "+ Color.ANSI_RESET);
					System.out.println(" Separação gerada com sucesso!");
					System.out.println(" Disponivel em: " + finalPath);
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();
					
				}
				
				//SALVA NO BANCO DE DADOS cache.db AS ALTERAÇÕES FEITAS APOS A SEPARAÇÃO DE CARGA.
				factory.getRepository().saveChanges(factory.getAllProducts(), defaultPath);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}

	public static void stateSeparation(Scanner sc, String defaultPath, SeparationFactory factory) {
		SeparationSet<Separation, Separation, Separation> separations = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean success = false; 
		boolean p = false,q = false,r = false;
	
		while (!(p || q || r)) {
			try {
				success = new File(defaultPath + "/separations").mkdir();
				
				if(success) {
					prefix = defaultPath + "/separations/";		
	
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					clearScreen();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = prefix + oc;
					
				}else {
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					clearScreen();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = defaultPath + "/separations/" + oc;				
				}

				separations = factory.stateSeparation(defaultPath); 			  

				p = separations.getForklift().createArquiveWithSeparation(finalPath.replace(".txt", "_forklift") + ".txt");
				q = separations.getFloor().createArquiveWithSeparation(finalPath.replace(".txt", "_floor") + ".txt");
				r = separations.getCold().createArquiveWithSeparation(finalPath.replace(".txt", "_cold") + ".txt");
				
				if(p || q || r) {
					System.out.println(Color.ANSI_PURPLE_BACKGROUND + " Separação de carga.                         "+ Color.ANSI_RESET);

					System.out.println(" Separação gerada com sucesso!");
					System.out.println(" Disponivel em:\n " + finalPath);
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();
				}
				
				//SALVA NO BANCO DE DADOS cache.db AS ALTERAÇÕES FEITAS APOS A SEPARAÇÃO DE CARGA.
				factory.getRepository().saveChanges(factory.getAllProducts(), defaultPath);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}
	
	public static void outStateSeparation(Scanner sc, String defaultPath, SeparationFactory factory) {
		SeparationSet<Separation, Separation, Separation> separations = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean success = false; 
		boolean p = false,q = false,r = false;
	
		while (!(p || q || r)) {
			try {
				success = new File(defaultPath + "/separations").mkdir();
				
				if(success) {
					prefix = defaultPath + "/separations/";		
	
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					clearScreen();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = prefix + oc;
					
				}else {
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					clearScreen();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = defaultPath + "/separations/" + oc;				
				}

				separations = factory.outOfStateSeparation(defaultPath); 			  

				p = separations.getForklift().createArquiveWithSeparation(finalPath.replace(".txt", "_forklift") + ".txt");
				q = separations.getFloor().createArquiveWithSeparation(finalPath.replace(".txt", "_floor") + ".txt");
				r = separations.getCold().createArquiveWithSeparation(finalPath.replace(".txt", "_cold") + ".txt");
				
				if(p || q || r) {
					System.out.println(Color.ANSI_PURPLE_BACKGROUND + " Separação de carga.                         "+ Color.ANSI_RESET);

					System.out.println(" Separação gerada com sucesso!");
					System.out.println(" Disponivel em:\n " + finalPath);
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				}
				
				//SALVA NO BANCO DE DADOS cache.db AS ALTERAÇÕES FEITAS APOS A SEPARAÇÃO DE CARGA.
				factory.getRepository().saveChanges(factory.getAllProducts(), defaultPath);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}
	
	public static void parametrosGerais(String defaultPath ,Scanner sc) {
		int choiceGeralConfig = 0; 
		GeneralParameter cold_out = null;
		GeneralParameter frozen = null;
		GeneralParameter cold_in = null;
		Floor_separation floorSeparation = null;
		
		try {
			
			do {
				System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                              " + Color.ANSI_RESET);
				System.out.println(" Fifo congelados:...................(1).");
				System.out.println(" Fifo resfriado fora do estado:.....(2).");
				System.out.println(" Fifo resfriado dentro do estado:...(3).");
				System.out.println(" Qtde max p/ separação chão:........(4).");
				System.out.println(" Help:..............................(5).");
				System.out.println(" Voltar:............................(6).");
				System.out.print(" -> ");
				choiceGeralConfig = sc.nextInt();
				sc.nextLine();
				clearScreen();
				switch (choiceGeralConfig) {
					case 1: {
						System.out.println(Color.ANSI_CYAN_BACKGROUND + " FIFO CONGELADOS.                            " + Color.ANSI_RESET);
						int divisor, multiplicador;
						System.out.println(" Padrão do parametro: f(x) -> (x / D) * M");
						System.out.print(" Informe o valor do divisor: ");
						divisor = sc.nextInt();
						System.out.print(" Informe o valor do Multiplicador: ");
						multiplicador = sc.nextInt();
						sc.nextLine();
						frozen = new ObjectMapper().readValue(new File(defaultPath + "/config/geralParameters/frozen.json"), Frozen.class);
						frozen.setDivisor(divisor);
						frozen.setMultiplicador(multiplicador);
						frozen.salveParameters(defaultPath);
						System.out.println(" Sucesso.");
						System.out.println(" Precione Enter para continuar...");
						sc.nextLine();
						clearScreen();
						break;
						}
					case 2: {
						System.out.println(Color.ANSI_CYAN_BACKGROUND + " FIFO RESFRIADO P/ FORA DO ESTADO.           " + Color.ANSI_RESET);
						int divisor, multiplicador;
						System.out.println("Padrão do parametro: f(x) -> (x / D) * M");
						System.out.print(" Informe o valor do divisor: ");
						divisor = sc.nextInt();
						System.out.print(" Informe o valor do Multiplicador: ");
						multiplicador = sc.nextInt();
						sc.nextLine();

						cold_out = new ObjectMapper().readValue(new File(defaultPath + "/config/geralParameters/cold_out_state.json"), Cold_out_state.class);
						cold_out.setDivisor(divisor);
						cold_out.setMultiplicador(multiplicador);
						cold_out.salveParameters(defaultPath);
						System.out.println(" Sucesso.");
						System.out.println(" Precione Enter para continuar...");
						sc.nextLine();
						clearScreen();
						break;
						}
					case 3: {
						System.out.println(Color.ANSI_CYAN_BACKGROUND + " FIFO RESFRIADO P/ DENTRO DO ESTADO.         " + Color.ANSI_RESET);
						int divisor, multiplicador;
						System.out.println("Padrão do parametro: f(x) -> (x / D) * M");
						System.out.print(" Informe o valor do divisor: ");
						divisor = sc.nextInt();
						System.out.print(" Informe o valor do Multiplicador: ");
						multiplicador = sc.nextInt();
						sc.nextLine();

						cold_in = new ObjectMapper().readValue(new File(defaultPath + "/config/geralParameters/cold_in_state.json"), Cold_in_state.class);
						cold_in.setDivisor(divisor);
						cold_in.setMultiplicador(multiplicador);
						cold_in.salveParameters(defaultPath);
						System.out.println(" Sucesso.");
						System.out.println(" Precione Enter para continuar...");
						sc.nextLine();
						clearScreen();
						break;
						}
					case 4: {
						System.out.println(Color.ANSI_CYAN_BACKGROUND + " QUANTIDADE MÁXIMA P/ SEPARAÇÃO DO CHÃO      " + Color.ANSI_RESET);
						int valor;
						System.out.println("Padrão do parametro: f(x) -> (x <= V)");
						System.out.print(" Informe o valor do divisor: ");
						valor = sc.nextInt();
						sc.nextLine();

						floorSeparation = new ObjectMapper().readValue(new File(defaultPath + "/config/geralParameters/floor_separation.json"), Floor_separation.class);
						floorSeparation.setParameter(valor);
						floorSeparation.salveParameters(defaultPath);
						System.out.println(" Sucesso.");
						System.out.println(" Precione Enter para continuar...");
						sc.nextLine();
						clearScreen();
						break;
						}
					case 5:{
						System.out.println(Color.ANSI_CYAN_BACKGROUND + " HELP-ME.                                    " + Color.ANSI_RESET);
						
						System.out.println(" Os parâmetros  gerais, segem a função\n"
								+ " abaixo para determinar o valor do Fifo a ser\n"
								+ " seguido para cada produto registrado,\n"
								+ " onde X representa o produto, D representa\n"
								+ " o divisor e M representa o Multiplicador.\n\n"
								+ " f(x) -> (x / D) * M)\n\n"
								+ " Ex: Para um produto com validade de 120 dias.\n"
								+ " f(120) -> (120 / 3) * 2 = 80\n\n"
								+ " Neste exemplo, 80 será o valor considerado\n"
								+ " como Fifo sobre os produtos que tem 120 dias\n"
								+ " de validade.\n");
						System.out.println(" Precione Enter para continuar...");
						sc.nextLine();
						clearScreen();
						break;
					}
					case 6:{
						break;
					}
					default:
						System.out.println(" Opção inválida! " + choiceGeralConfig);
					}
				
				
				
			} while (choiceGeralConfig != 6);
			

			//System.out.println(config.getProperty(" congelado"));
		} catch (IOException e) {
			System.out.println(" Erro ao carregar o arquivo: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static void configuracao(String defaultPath, Scanner sc) {
		int choiceConfig = 0;
		do {
			System.out.println(Color.ANSI_CYAN_BACKGROUND + " ESCOLHA UMA OPÇÃO.                          " + Color.ANSI_RESET);
			System.out.println(" Parametros Gerais:.................(1).");
			System.out.println(" Parametros de Categorias:..........(2).");
			System.out.println(" Voltar:............................(3).");
			System.out.print(" -> ");
			choiceConfig = sc.nextInt();
			sc.nextLine();
			clearScreen();
			switch (choiceConfig) {
			case 1: {
				parametrosGerais(defaultPath ,sc);
				break;
			}case 2: {
				configCategories(defaultPath, sc);
				break;
			}
			case 3: {
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choiceConfig);
			}
			
		} while (choiceConfig != 3);
	}
	
	public static void gerarSeparacao(Scanner sc, String defaultPath, SeparationFactory factory) {
		int choiceSeparation = 0;
		do {
			System.out.println(Color.ANSI_GREEEN_BACKGROUND + " ESCOLHA O TIPO DE CARGA.                    "+ Color.ANSI_RESET);
			System.out.println(" Dentro do estado:.................(1).");
			System.out.println(" Fora do estado:...................(2).");
			System.out.println(" Simples:..........................(3).");
			System.out.println(" Voltar:...........................(4).");
			System.out.print(" -> ");
			choiceSeparation = sc.nextInt();
			sc.nextLine();
			
			clearScreen();
			
			switch (choiceSeparation) {
			case 1: {
				stateSeparation(sc, defaultPath, factory);
				break;
			}case 2: {
				outStateSeparation(sc, defaultPath, factory);
				break;
			}case 3: {
				tipycalSeparation(sc, defaultPath, factory);
				break;
			}case 4:{
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choiceSeparation);
			}
			
		} while (choiceSeparation != 4);
	}
	
	public static void configCategories(String defaultPath, Scanner sc) {
		ParameterProduct parameterProduct = null;
		try {
			parameterProduct = new ParameterProduct(defaultPath);
			
			
			int choiceConfig = 0;
			do {
				System.out.println(Color.ANSI_CYAN_BACKGROUND + " CATEGORIAS DE PRODUTOS.                     " + Color.ANSI_RESET);
				System.out.println(" Listar Categorias:.................(1).");
				System.out.println(" Editar Categoria:..................(2).");
				System.out.println(" Adicionar Categoria:...............(3).");
				System.out.println(" Remover Categoria:.................(4).");
				System.out.println(" Voltar:............................(5).");
				System.out.print(" -> ");
				choiceConfig = sc.nextInt();
				sc.nextLine();
				clearScreen();
				switch (choiceConfig) {
				case 1: {
					System.out.println(Color.ANSI_CYAN_BACKGROUND + " LISTAR CATEGORIAS.                          " + Color.ANSI_RESET);
					if(parameterProduct != null) {
						System.out.println(" Categorias[ ");
						for(Category category : parameterProduct.getCategories()) {
							System.out.println(String.format("  Categoria de %d dias.", category.getValidity()));
						}
						System.out.println(" ]");
					}
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();

					break;
				}case 2: {
					System.out.println(Color.ANSI_CYAN_BACKGROUND + " EDITAR CATEGORIAS.                          " + Color.ANSI_RESET);
					if(parameterProduct != null) {
						int choice,i = 1;
						List<Entry<Integer, Integer>> tuple = new ArrayList<>();
						
						for(Category category : parameterProduct.getCategories()) {
							System.out.println(String.format(" Categoria de %d"+ (category.getValidity() < 100 ? "  " : " ") + "dias.............(%d)", category.getValidity(), i));
							tuple.add(new SimpleEntry<>(i, category.getValidity()));
							i++;
						}
						System.out.println(" Cancelar..........................(" + i +")");

						System.out.println();
						System.out.println(" Informe o número correspondente\n"
										 + " a categoria que deseja editar.\n"
										 + " Ou digite 0 para cancelar.");
						System.out.print(" -> ");
						choice = sc.nextInt();
						sc.nextLine();
						
						if(choice == 0)break;
						clearScreen();
						int validity = 0;
						
						for (Entry<Integer, Integer> entry : tuple) {
							int key = entry.getKey();
							int value = entry.getValue();
							if (choice == key) {
								validity = value;
							}
						}

						Category cat = null;
						for (Category c : parameterProduct.getCategories()) {
							if (c.getValidity() == validity) {
								cat = c;
							}
						}
						//implements here!
						choice = 0;
						do {
							System.out.println(Color.ANSI_YELLOW_BACKGROUND + " CATEGORIA "+cat.getValidity()+" DIAS.                          " + Color.ANSI_RESET);
							System.out.println(" Adicionar produto:.................(1).");
							System.out.println(" Pesquisar produto:.................(2).");
							System.out.println(" Listar produtos:...................(3).");
							System.out.println(" Remover produto:...................(4).");
							System.out.println(" Voltar:............................(5).");
							System.out.print(" -> ");
							choice = sc.nextInt();
							sc.nextLine();
							clearScreen();
							
							switch(choice) {
								case 1:{
									int code;
									boolean isFrozen;
									System.out.println(Color.ANSI_YELLOW_BACKGROUND + " ADICIONAR PRODUTO.                          " + Color.ANSI_RESET);
									System.out.print(" Digite o código do produto: ");
									code = sc.nextInt();
									
									int op = 0;
									do {
										
									
										System.out.println(" Para definir como congelado:.......(1).");
										System.out.println(" Para definir como resfriado:.......(2).");
										System.out.print(" -> ");
										op = sc.nextInt();
										sc.nextLine();
										if(op != 1 && op != 2) {
											System.out.println(" Opção inválida! precione ENTER....");
											sc.nextLine();
											clearScreen();
										}
									
									}while(op != 1 && op != 2);
									
									if(op == 1) {
										isFrozen = true;
										clearScreen();
										System.out.println(String.format(" Novo produto: %d congelado.", code));
	
									}else {
										isFrozen = false;
										clearScreen();
										System.out.println(String.format(" Novo produto: %d resfriado.", code));
	
									}
									
									op = 0;
									do {
										System.out.println(" Para confirmar:....................(1).");
										System.out.println(" Para cancelar:.....................(2).");
										System.out.print(" -> ");
										op = sc.nextInt();
										sc.nextLine();
										if(op != 1 && op != 2) {
											System.out.println(" Opção inválida! precione ENTER....");
											sc.nextLine();
											clearScreen();
										}
										
									}while(op != 1 && op != 2);
									
									if(op == 2) {break;}
									
									System.out.println(cat.toString());
									
									if(op == 1) {
										if(cat.setEntryProduct(new EntryProduct(code, isFrozen))) {
											parameterProduct.salveParameters(defaultPath);
											System.out.println(" Novo produto adicionado com sucesso! ");
											System.out.println(" Precione ENTER para continuar...");
											sc.nextLine();
											
											break;
										}else{
											System.out.println(" Não foi possivel adicionar o produto!");
											System.out.println(" Precione ENTER para continuar...");
											sc.nextLine();
											break;
										}
									}
	
									break;
								}
								case 2:{
									int code;
									System.out.println(Color.ANSI_YELLOW_BACKGROUND + " PESQUISAR PRODUTO.                          " + Color.ANSI_RESET);
									System.out.print(" Digite o código: ");
									code = sc.nextInt();
									sc.nextLine();
									
									EntryProduct p = cat.getEntryProduct(code);
									if(p != null) {
										System.out.println(" Encontrado: ");
										System.out.println(" Produto: " + p.getCode() + " Tipo: " + (p.getIsFrozen() ? "Congelado" : "Resfriado"));
										System.out.println(" Digite ENTER para continuar...");
										sc.nextLine();
										clearScreen();
									}else {
										System.out.println(" Não encontrado.");
										System.out.println(" Digite ENTER para continuar...");
										sc.nextLine();
										clearScreen();
									}
									break;
								}
								case 3:{
									System.out.println(Color.ANSI_YELLOW_BACKGROUND + " LISTAR PRODUTOS.                             " + Color.ANSI_RESET);
									
									for(EntryProduct entry : cat.getEntries()) {
										System.out.println(" Produto: " + entry.getCode() + " Tipo: " + (entry.getIsFrozen() ? "Congelado" : "Resfriado"));

									}
									System.out.println(" Digite ENTER para continuar...");
									sc.nextLine();
									clearScreen();
									
									break;
								}
								case 4:{
									int code;
									System.out.println(Color.ANSI_YELLOW_BACKGROUND + " REMOVER PRODUTOS.                            " + Color.ANSI_RESET);
									System.out.println(" Digite código: ");
									code = sc.nextInt();
									sc.nextLine();
									
									EntryProduct p = cat.getEntryProduct(code);
									if(p != null) {
										if(cat.removeEntryProduct(p)) {
											System.out.println(" Produto removido! ");
											System.out.println(" Digite ENTER para continuar...");
											sc.nextLine();
											clearScreen();
										}else {
											System.out.println(" Erro ao remover o produto.");
											System.out.println(" Produto: " + p.getCode() + " Tipo: " + (p.getIsFrozen() ? "Congelado" : "Resfriado"));
											System.out.println(" Digite ENTER para continuar...");
											sc.nextLine();
											clearScreen();
										}
									}else {
										System.out.println(" Produto não encontrado.");
										System.out.println(" Produto: " + code);
										System.out.println(" Digite ENTER para continuar...");
										sc.nextLine();
										clearScreen();

									}
									
									break;
								}
								case 5:{
									break;
								}
								default:{
									System.out.println("Opção inválida! ");
									break;
								}
							}
						}while(choice != 5);
						
					}
					break;
				}
				case 3: {
					int validity;
					System.out.println(Color.ANSI_CYAN_BACKGROUND + " ADICIONAR CATEGORIA.                        " + Color.ANSI_RESET);
					System.out.println(" Informe o Valor da validade da Categoria.");
					System.out.println(" Ou digite 0 para cancelar. ");
					System.out.print(" -> ");
					validity = sc.nextInt();
					sc.nextLine();
					
					if(validity == 0)break;
					clearScreen();
					parameterProduct.createdCategory(new Category(validity, new HashSet<EntryProduct>()));
					parameterProduct.salveParameters(defaultPath);
					System.out.println(" Categoria "+validity+ " dias Criada.");
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();

					
					break;
				}
				case 4:{
					System.out.println(Color.ANSI_CYAN_BACKGROUND + " REMOVER CATEGORIA.                          " + Color.ANSI_RESET);
					if(parameterProduct != null) {
						int choice,i = 1;
						List<Entry<Integer, Integer>> index = new ArrayList<>();
						
						for(Category category : parameterProduct.getCategories()) {
							System.out.println(String.format(" Categoria de %d"+ (category.getValidity() < 100 ? "  " : " ") + "dias.............(%d)", category.getValidity(), i));
							index.add(new SimpleEntry<>(i, category.getValidity()));
							i++;
						}
						System.out.println(" Cancelar..........................(" + i +")");

						System.out.println();
						System.out.println(" Informe o número correspondente\n"
										 + " a categoria que deseja remover.\n"
										 + " Ou digite 0 para cancelar.");
						System.out.print(" -> ");
						choice = sc.nextInt();
						sc.nextLine();
						clearScreen();

						if(choice == 0)break;
						clearScreen();
						int validity = 0;
						
						for (Entry<Integer, Integer> entry : index) {
							int key = entry.getKey();
							int value = entry.getValue();
							if (choice == key) {
								validity = value;
							}
						}
					
						Category cat = null;
						for (Category c : parameterProduct.getCategories()) {
							if (c.getValidity() == validity) {
								cat = c;
							}
						}
						
						
						if(cat != null && parameterProduct.removeCategory(cat)) {
							parameterProduct.salveParameters(defaultPath);
							System.out.println(" Categoria removida com sucesso.");
							System.out.println(" Pressione Enter para continuar...");
						    sc.nextLine();
						    clearScreen();
						}else{
							System.out.println(" Não foi possivel remover a categoria.");
							System.out.println(" Pressione Enter para continuar...");
						    sc.nextLine();
						    clearScreen();
						};
						
						
					}
					break;
				}
				case 5:{
					break;
				}
				default:
					System.out.println(" Opção inválida! " + choiceConfig);
				}
				
			} while (choiceConfig != 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
