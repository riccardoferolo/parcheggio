package Principale.Parcheggio;

import Principale.Parcheggio.Models.ChargeRequest;
import Principale.Parcheggio.Models.User;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import Principale.Parcheggio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Principale.Parcheggio.Repository.UserRepository;
import Principale.Parcheggio.Services.*;
import Principale.Parcheggio.Services.ChargeRequestService;
import Principale.Parcheggio.Controllers.ChargeRequestController;
import Principale.Parcheggio.Controllers.UserController;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.*;

import Principale.Parcheggio.Models.User;

import static java.lang.System.exit;
import static java.lang.System.out;


@SpringBootApplication
public class ParcheggioApplication {

	@Autowired
	private ChargeRequestController chargeRequestController;

	@Autowired
	private ChargeRequestService cService;

	Scanner s = new Scanner(System.in);

    public static void main(String[] args) throws ParseException {

		int scelta;
		//SpringApplication.run(ParcheggioApplication.class, args);
		Scanner sc = new Scanner(System.in);

		// Avvia il contesto di Spring e ottieni un'istanza gestita di ParcheggioApplication
		ApplicationContext context = SpringApplication.run(ParcheggioApplication.class, args);

		// Ottieni l'istanza di ParcheggioApplication dal contesto di Spring
		ParcheggioApplication app = context.getBean(ParcheggioApplication.class);

		do {
			// Mostra il menu
			System.out.println("MENU:");
			System.out.println("1. Creare utente");
			System.out.println("2. Login utente");
			System.out.println("3. Cerca per username");
			System.out.println("4. Entra Ora");
			System.out.println("5. Prenota");
			System.out.println("8. cancella prenotazioni e basta");
			System.out.println("9. cancella tutto");
			System.out.println("10. Esci");
			System.out.print("Scegli un'opzione: ");

			// Gestisci input dell'utente
			while (!sc.hasNextInt()) {
				System.out.println("Per favore, inserisci un numero valido.");
				sc.next(); // Consuma l'input non valido
			}
			scelta = sc.nextInt();
			sc.nextLine(); // Pulisce il buffer dopo nextInt()
			String x;
			switch (scelta) {
				case 1:
					x = app.registra();
					System.out.println("Response from API: " + x);
					break;

				case 2:
					x = app.login();
					System.out.println("Response from API: " + x);
					break;

				case 3:
					app.Cerca_User();
					break;

				case 4:
					x = app.prenotaOra();
					System.out.println("Response from API: " + x);
					break;

				case 5:
					x = app.PrenotaPremium();
					System.out.println("Response from API: " + x);
					break;

				case 8:
					x = app.cancellaPrenotazioni();
					System.out.println("Response from API: " + x);
					break;

				case 9:
					x = app.Cancellatutto();
					System.out.println("Response from API: " + x);
					break;
				case 10:
					System.out.println("Uscita dal programma...");
					break;

				default:
					System.out.println("Errore: opzione non valida.");
			}

		} while (scelta != 10);

		exit(0);
	}

	public String registra() {
		int errore = 0;
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8080/api/users/register";

		Map<String, Object> requestBody = new HashMap<>();
		String x = "";

		do {
			try {
				errore = 0;

				System.out.print("Inserisci un username: ");
				requestBody.put("username", s.nextLine().trim());

				System.out.print("Inserisci una password: ");
				requestBody.put("password", s.nextLine().trim());

				System.out.print("Inserisci un'email: ");
				requestBody.put("email", s.nextLine().trim());

				System.out.print("Inserisci un saldo: ");
				while (!s.hasNextInt()) {
					System.out.println("Per favore, inserisci un numero valido.");
					s.next(); // Consuma l'input non valido
				}
				requestBody.put("saldo", s.nextInt());
				s.nextLine(); // Pulisce il buffer dopo nextInt()

				System.out.print("Inserisci un ruolo: ");
				requestBody.put("ruolo", s.nextLine().trim());

				// Invio della richiesta POST
				x = restTemplate.postForObject(apiUrl, requestBody, String.class);

			} catch (Exception e) {
				System.out.println("Errore durante la registrazione: " + e.getMessage());
				errore = 1;
			}

		} while (errore == 1);

		return x;
	}

	public String login() {
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8080/api/users/login";
		Map<String, Object> requestBody = new HashMap<>();

		System.out.println("Inserisci la email: ");
		requestBody.put("email", s.next());

		System.out.println("Inserisci la password: ");
		requestBody.put("password", s.next());

		try {
			// Invio della richiesta POST e restituzione della risposta
			return restTemplate.postForObject(apiUrl, requestBody, String.class);
		} catch (Exception e) {
			// Gestione degli errori del server
			return "Errore durante il login: " + e.getMessage();
		}
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

	public String prenotaOra() throws ParseException {
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8080/chargerequests/new";

		Map<String, Object> requestBody = new HashMap<>();

		ChargeRequest x = new ChargeRequest();

		System.out.println("Inserisci la percentuale richiesta: ");
		int Percentage = s.nextInt();
		ChargeRequestController c = new ChargeRequestController();
		String durataS = c.calcola(Percentage);
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
		Time durata = new Time(timeFormatter.parse(durataS).getTime());
		LocalDate data = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String dataStr = data.format(formatter);

		Time orario = Time.valueOf(LocalTime.now());

		// Conversione a LocalTime per effettuare i calcoli
		LocalTime localOraInizio = orario.toLocalTime();
		LocalTime localDurata = durata.toLocalTime();

		// Calcolo dell'ora di fine
		LocalTime OraFine = localOraInizio.plusHours(localDurata.getHour())
				.plusMinutes(localDurata.getMinute())
				.plusSeconds(localDurata.getSecond());

		if(cService.isReservationAvailable(data, orario, Time.valueOf(OraFine))){
			requestBody.put("giorno", dataStr); // Già una stringa
			requestBody.put("Percentuale", Percentage); // Integer va bene
			requestBody.put("ora", orario.toString()); // Converte in String
			requestBody.put("durata", durata.toString()); // Converte in String
			requestBody.put("oraFine", OraFine.toString()); // Converte in String
			s.nextLine(); // Consuma eventuali caratteri residui
			System.out.print("Inserisci un username: ");
			requestBody.put("username", s.nextLine().trim()); // Già una stringa
			System.out.print("ciao");
			try {
				// Invio della richiesta POST e restituzione della risposta
				return restTemplate.postForObject(apiUrl, requestBody, String.class);
			} catch (Exception e) {
				// Gestione degli errori del server
				return "Errore durante il login: " + e.getMessage();
			}
		}

		return "PARCHEGGIO PIENO, RIPROVARE DOPO";
	}

	public String PrenotaPremium() throws ParseException {
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8080/chargerequests/new";

		Scanner scanner = new Scanner(System.in);

		Map<String, Object> requestBody = new HashMap<>();

		System.out.println("Inserisci la percentuale richiesta: ");
		int percentage = scanner.nextInt();
		scanner.nextLine(); // Consuma il carattere newline

		ChargeRequestController controller = new ChargeRequestController();
		String durataS = controller.calcola(percentage);
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
		Time durata = new Time(timeFormatter.parse(durataS).getTime());

		System.out.println("Inserisci la data in questo formato (dd/MM/yyyy): ");
		String dataStr = scanner.nextLine();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");// Specifica il formato corretto
		// Formatter per il formato di input (dd/MM/yyyy)
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Parsing della stringa in LocalDate
		LocalDate data2 = LocalDate.parse(dataStr, inputFormatter);

		// Conversione della data in formato LocalDate (yyyy/MM/dd)
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String dataFormattata = data2.format(outputFormatter);

		LocalDate data;
		try {
			data = LocalDate.parse(dataFormattata, outputFormatter);
		} catch (Exception e) {
			return "Errore: il formato della data è errato. Usa il formato dd/MM/yyyy.";
		}


		System.out.println("Inserisci l'orario di arrivo in questo formato (HH:mm:ss): ");
		String orarioS = scanner.nextLine();
		Time orario;
		try {
			orario = Time.valueOf(orarioS);
		} catch (IllegalArgumentException e) {
			return "Errore: il formato dell'orario è errato. Usa il formato HH:mm:ss.";
		}

		// Conversione a LocalTime per effettuare i calcoli
		LocalTime localOraInizio = orario.toLocalTime();
		LocalTime localDurata = durata.toLocalTime();

		// Calcola oraFine
		LocalTime orafine = localOraInizio.plusHours(localDurata.getHour())
				.plusMinutes(localDurata.getMinute())
				.plusSeconds(localDurata.getSecond());

// Assicurati che sia nel formato "HH:mm:ss"
		DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
		String oraFineStr = orafine.format(timeFormatter2);
		Time oraFine = new Time(timeFormatter.parse(oraFineStr).getTime());

		if (cService.isReservationAvailable(data, orario, oraFine)) {
			requestBody.put("giorno", dataFormattata); // Già una stringa
			requestBody.put("Percentuale", percentage); // Integer va bene
			requestBody.put("ora", orario.toString()); // Converte in String
			requestBody.put("durata", durata.toString()); // Converte in String
			requestBody.put("oraFine", oraFine.toString()); // Converte in String
			System.out.print("Inserisci un username: ");
			requestBody.put("username", scanner.nextLine().trim()); // Già una stringa

			try {
				// Invio della richiesta POST e restituzione della risposta
				return restTemplate.postForObject(apiUrl, requestBody, String.class);
			} catch (Exception e) {
				// Gestione degli errori del server
				e.printStackTrace();
				return "Errore durante il login: " + e.getMessage();
			}
		}

		return "PARCHEGGIO PIENO, RIPROVARE DOPO";
	}


	public String Cancellatutto(){

		RestTemplate restTemplate = new RestTemplate();
		String Reservation_url = "http://localhost:8080/reservations/delete/all";
		String payment_url = "http://localhost:8080/api/payments/delete-all";
		String charge_url = "http://localhost:8080/chargerequests/delete-all";
		String user_url = "http://localhost:8080/api/users/delete-all";
		String x = "";
		
		try {
			restTemplate.delete(Reservation_url);
			restTemplate.delete(payment_url);
			restTemplate.delete(charge_url);
			restTemplate.delete(user_url);
			x = "eliminato tutto con successo.";
		} catch (Exception e) {
			System.err.println("Errore durante l'eliminazione della risorsa: " + e.getMessage());
			e.printStackTrace();
		}

		return x;
	}

	private String cancellaPrenotazioni(){
		RestTemplate restTemplate = new RestTemplate();
		String Reservation_url = "http://localhost:8080/reservations/delete/all";
		String payment_url = "http://localhost:8080/api/payments/delete-all";
		String charge_url = "http://localhost:8080/chargerequests/delete-all";
		String x = "";
		try {
			restTemplate.delete(Reservation_url);
			restTemplate.delete(payment_url);
			restTemplate.delete(charge_url);
			x = "eliminato le prenotazioni, i pagamenti e  con successo";
		} catch (Exception e) {
			System.err.println("Errore durante l'eliminazione della risorsa: " + e.getMessage());
			e.printStackTrace();
		}
		 return x;
	}
}
