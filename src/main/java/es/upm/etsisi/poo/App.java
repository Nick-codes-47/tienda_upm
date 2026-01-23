package es.upm.etsisi.poo;

import es.upm.etsisi.poo.AppExceptions.AppException;
import es.upm.etsisi.poo.Commands.Command;
import es.upm.etsisi.poo.Models.Product.Catalog;
import es.upm.etsisi.poo.Models.Product.Core.BaseProduct;
import es.upm.etsisi.poo.Models.Product.Core.ProductID;
import es.upm.etsisi.poo.Models.Product.ProductEnums.Category;
import es.upm.etsisi.poo.Models.User.Core.UserRegister;
import es.upm.etsisi.poo.Models.User.Core.Cashier;
import es.upm.etsisi.poo.Models.User.Core.Customer;
import es.upm.etsisi.poo.Handlers.CashierHandler;
import es.upm.etsisi.poo.Handlers.CustomerHandler;
import es.upm.etsisi.poo.Handlers.ProductHandler;
import es.upm.etsisi.poo.Handlers.TicketHandler;
import es.upm.etsisi.poo.Handlers.Request;
import es.upm.etsisi.poo.Handlers.RequestHandler;
import es.upm.etsisi.poo.Persistence.PersistenceService;
import es.upm.etsisi.poo.Services.TicketService;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Clase principal de la aplicación.
 * <p>
 * Actúa como el controlador central del sistema, gestionando el ciclo de vida de la aplicación,
 * la inicialización de los registros (Catálogo, Cajeros, Clientes), la carga/guardado de datos
 * y el bucle principal de procesamiento de comandos.
 */
public class App {

    public final Catalog catalog = new Catalog();
    public final UserRegister<Cashier> cashiers = new UserRegister<>();
    public final UserRegister<Customer> customers = new UserRegister<>();

    private final PersistenceService persistence = new PersistenceService();

    public final TicketService ticketService = new TicketService(cashiers);

    private RequestHandler[] handlers;
    private final HashMap<String, Integer> handlerIds = new HashMap<>();
    private final HashMap<String, Consumer<Request>> builtinCommands = new HashMap<>();
    private InputDriver input;

    private boolean exitRequested = false;

    /**
     * Constructor por defecto de la aplicación.
     */
    public App() {}

    /**
     * Inicializa la aplicación y comienza el bucle principal de ejecución.
     * <p>
     * Carga los datos, configura los manejadores de comandos y procesa las entradas
     * del usuario hasta que se solicita la salida.
     *
     * @param inputFile Ruta al archivo de entrada para ejecución por lotes (batch),
     * o {@code null} para ejecución interactiva por consola.
     */
    public void init(String inputFile) {
        AppLogger.info("Loading data...");

        initBuiltinCommandsMap();
        initHandlersMap();

        initInput(inputFile);

        load();

        printWelcome();

        while (!exitRequested) {
            Request request = input.next();
            if (!request.handlerId.isEmpty()) {
                handleRequest(request);
                System.out.println();
            }
        }
    }

    /**
     * Punto de entrada principal del programa (Main entry point).
     *
     * @param args Argumentos de línea de comandos. El primer argumento puede ser
     * la ruta a un archivo de script para ejecutar comandos automáticamente.
     */
    public static void main(String[] args) {
        App app = new App();

        try {
            if (args.length > 0) {
                app.init(args[0]);
            } else {
                app.init(null);
            }
        } catch (RuntimeException exception) {
            app.save(); // guardado de emergencia en caso de error crítico
            AppLogger.error(exception.getMessage());
        }
    }

    /**
     * Imprime el mensaje de bienvenida al iniciar la aplicación.
     */
    private void printWelcome() {
        AppLogger.info("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");
    }

    /**
     * Inicializa el controlador de entrada (InputDriver).
     *
     * @param inputFile Ruta del archivo de entrada o null para entrada estándar.
     */
    private void initInput(String inputFile) {
        if (inputFile != null) {
            input = new InputDriver(inputFile);
        } else {
            input = new InputDriver();
        }
    }

    /**
     * Carga los datos persistidos (Productos, Clientes, Cajeros) desde el almacenamiento.
     * Utiliza {@code @SuppressWarnings("unchecked")} debido a los casteos de tipos genéricos deserializados.
     */
    @SuppressWarnings("unchecked")
    private void load() {
        Object[] data = persistence.loadAll();

        if (data[0] != null) catalog.loadData((HashMap<ProductID, BaseProduct<?>>) data[0]);
        if (data[1] != null) customers.loadData((HashMap<String, Customer>) data[1]);
        if (data[2] != null) cashiers.loadData((HashMap<String, Cashier>) data[2]);
    }

    /**
     * Guarda el estado actual de la aplicación de forma segura en el sistema de persistencia.
     */
    public void save() {
        AppLogger.info("Saving data...");
        persistence.saveAll(
                catalog.getProducts(),
                customers.getRawMap(),
                cashiers.getRawMap()
        );
    }
    /**
     * Enruta y maneja una petición (Request) entrante.
     * <p>
     * Determina si la petición corresponde a un comando interno (built-in) o a un
     * manejador específico (Handler) y delega la ejecución.
     *
     * @param request La petición parseada que contiene el ID del manejador y del comando.
     */
    private void handleRequest(Request request) {
        if (handlerIds.containsKey(request.handlerId)) {
            execute(handlers[handlerIds.get(request.handlerId)].getCommand(request.commandId), request);
        } else if (builtinCommands.containsKey(request.handlerId)) {
            builtinCommands.get(request.handlerId).accept(request);
        } else {
            AppLogger.error(String.format("Invalid command %s\n", request.handlerId));
        }
    }

    /**
     * Ejecuta un comando específico de forma segura, capturando excepciones de la aplicación.
     *
     * @param command El comando a ejecutar.
     * @param request La petición que contiene los argumentos para el comando.
     */
    private void execute(Command command, Request request) {
        if (command == null)
            return;

        try {
            command.execute(request.args);
            AppLogger.info(String.format("%s %s: ok\n", request.handlerId, request.commandId));
        } catch (AppException e) {
            AppLogger.error(e.getMessage());
        } catch (NumberFormatException e) {
            AppLogger.error("Error in the number format: " + e.getMessage());
        }
    }

    /**
     * Muestra la ayuda general de la aplicación, listando todos los comandos disponibles
     * y las categorías de productos.
     */
    private void help() {
        StringBuilder output = new StringBuilder();

        includeCommands(output);

        includeCategories(output);

        AppLogger.info(output.toString());
    }

    /**
     * Añade la lista de comandos disponibles al StringBuilder proporcionado.
     */
    private void includeCommands(StringBuilder output) {
        output.append("Commands:\n");
        for (RequestHandler requestHandler : handlers) {
            for (Command command : requestHandler.getCommands()) {
                output.append(String.format("  %s %s\n", requestHandler.HANDLER_ID, command.help()));
            }
        }
    }

    /**
     * Añade la información de categorías y descuentos al StringBuilder.
     */
    private static void includeCategories(StringBuilder output) {
        output.append("Categories: ").append(Category.getCategories()).append("\n");
        output.append("Discounts if there are >=2 units in the category: ")
                .append(Category.getCategoriesAndDiscount()).append("\n\n");
    }

    /**
     * Finaliza la ejecución de la aplicación.
     * <p>
     * Guarda los datos antes de salir y establece la bandera de salida para terminar el bucle principal.
     */
    private void exit() {
        save();

        AppLogger.info("Closing Application.\nGoodbye!");
        exitRequested = true;
    }

    /**
     * Comando simple para hacer eco del ID del comando (útil para pruebas o logs).
     */
    private void echo(Request request) {
        AppLogger.info(request.commandId);
    }

    /**
     * Inicializa los manejadores (Handlers) específicos de la aplicación
     * (Clientes, Cajeros, Tickets, Productos).
     */
    private void initHandlersMap() {
        handlers = new RequestHandler[] {
                new CustomerHandler(this),
                new CashierHandler(this),
                new TicketHandler(this),
                new ProductHandler(this)
        };

        for (int i = 0; i < handlers.length; i++) {
            handlerIds.put(handlers[i].HANDLER_ID, i);
        }
    }

    /**
     * Inicializa los comandos internos básicos (help, echo, exit).
     */
    private void initBuiltinCommandsMap() {
        builtinCommands.put("help", (request) -> help());
        builtinCommands.put("echo", this::echo);
        builtinCommands.put("exit", (request) -> this.exit());
    }
}