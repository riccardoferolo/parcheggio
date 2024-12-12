package Principale.Parcheggio;

import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Principale.Parcheggio.Repository.UserRepository;
import Principale.Parcheggio.Controllers.UserController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import Principale.Parcheggio.Models.User;

import java.util.Optional;
import java.util.Scanner;



@SpringBootApplication
public class ParcheggioApplication {

	Scanner s = new Scanner(System.in);

	public static void main(String[] args) {

		SpringApplication.run(ParcheggioApplication.class, args);
		Scanner sc = new Scanner(System.in);

		ParcheggioApplication app = new ParcheggioApplication();

		System.out.println("MENU:");
		System.out.println("1.Creare utente");
		System.out.println("2.Login utente");
		System.out.println("3.Cerca per username");
		int scelta = sc.nextInt();

		switch (scelta) {
			case 1:
				String x = app.registra();
				// Stampa della risposta
				System.out.println("Response from API: " + x);
				break;
			case 2:
				String x2 = app.login();

				System.out.println("Response from API: " + x2);
				break;

			case 3:
				app.Cerca_User();
				break;

			default:System.out.println("errore");
		}

	}

	public String registra(){
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8080/api/users/register";

		Map<String, Object> requestBody = new HashMap<>();

		System.out.println("Inserisci un id: ");
		requestBody.put("id", s.nextInt());

		System.out.println("Inserisci un username: ");
		requestBody.put("username", s.next());

		System.out.println("Inserisci un password: ");
		requestBody.put("password", s.next());

		System.out.println("Inserisci un email: ");
		requestBody.put("email", s.next());

		System.out.println("Inserisci un saldo: ");
		requestBody.put("saldo", s.nextInt());

		System.out.println("Inserisci un ruolo: ");
		requestBody.put("ruolo", s.next());

		// Invio della richiesta POST
		return restTemplate.postForObject(apiUrl, requestBody, String.class);
	}

	public String login(){
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8080/api/users/login";
		Map<String, Object> requestBody = new HashMap<>();

		System.out.println("Inserisci la email: ");
		requestBody.put("email", s.next());

		System.out.println("Inserisci la password: ");
		requestBody.put("password", s.next());

		return restTemplate.postForObject(apiUrl, requestBody, String.class);

	}

	public static void Cerca_User() {
		// Creazione del RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// URL base dell'API
		String apiUrl = "http://localhost:8080/api/users/username";

		// Scanner per leggere l'input utente
		Scanner scanner = new Scanner(System.in);
		System.out.println("Inserisci Username: ");
		String username = scanner.nextLine(); // Legge l'input

		// Aggiunta del parametro di query all'URL
		String urlWithQueryParam = apiUrl + "?username=" + username;

		try {
			// Esegue la richiesta GET
			String response = restTemplate.getForObject(urlWithQueryParam, String.class);

			// Stampa la risposta
			System.out.println("Risultato API: " + response);
		} catch (Exception e) {
			// Gestione degli errori (es. utente non trovato)
			System.out.println("Errore durante la chiamata all'API: " + e.getMessage());
		}
	}

}
