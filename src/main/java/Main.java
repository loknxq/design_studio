import model.Client;
import model.DesignOrder;
import model.Designer;
import model.OrderStatus;
import service.ClientService;
import service.DesignOrderService;
import service.DesignerService;
import util.DatabaseManager;
import util.ExcelExporter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientService clientService = new ClientService();
    private static final DesignerService designerService = new DesignerService();
    private static final DesignOrderService orderService = new DesignOrderService();

    public static void main(String[] args) {

        while (true) {
            System.out.println("========================================");
            System.out.println("          ДИЗАЙН-СТУДИЯ");
            System.out.println("========================================");
            System.out.println("1. Клиенты");
            System.out.println("2. Дизайнеры");
            System.out.println("3. Дизайн-заказы");
            System.out.println("4. Поиск");
            System.out.println("5. Фильтрация");
            System.out.println("6. Сортировка");
            System.out.println("7. Статистика");
            System.out.println("8. Экспорт данных в Excel");
            System.out.println("9. Вывести таблицы базы данных");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");
            int choice = readInt();
            switch (choice) {
                case 1 -> clientMenu();
                case 2 -> designerMenu();
                case 3 -> orderMenu();
                case 4 -> searchMenu();
                case 5 -> filterMenu();
                case 6 -> sortMenu();
                case 7 -> showStatistics();
                case 8 -> exportToExcel();
                case 9 -> showAllTables();
                case 0 -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный пункт меню");
            }
        }
    }

    private static void clientMenu() {
        while (true) {
            System.out.println("\n--- КЛИЕНТЫ ---");
            System.out.println("1. Создать");
            System.out.println("2. Показать всех");
            System.out.println("3. Найти по ID");
            System.out.println("4. Изменить");
            System.out.println("5. Удалить");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> createClient();
                case 2 -> showAllClients();
                case 3 -> showClientById();
                case 4 -> updateClient();
                case 5 -> deleteClient();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт");
            }
        }
    }

    private static void createClient() {
        try {
            System.out.print("ФИО: ");
            String name = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Телефон: ");
            String phone = scanner.nextLine();
            Client client = new Client(name, email, phone);
            clientService.create(client);
            System.out.println("Клиент создан, ID = " + client.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showAllClients() {
        try {
            List<Client> clients = clientService.getAll();
            if (clients.isEmpty()) {
                System.out.println("Список пуст");
            } else {
                clients.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showClientById() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            System.out.println(clientService.getById(id));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void updateClient() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            Client client = clientService.getById(id);
            System.out.print("Новое ФИО (" + client.getFullName() + "): ");
            String name = scanner.nextLine();
            System.out.print("Новый email (" + client.getEmail() + "): ");
            String email = scanner.nextLine();
            System.out.print("Новый телефон (" + client.getPhone() + "): ");
            String phone = scanner.nextLine();
            client.setFullName(name);
            client.setEmail(email);
            client.setPhone(phone);
            clientService.update(client);
            System.out.println("Клиент обновлён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteClient() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            clientService.delete(id);
            System.out.println("Клиент удалён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void designerMenu() {
        while (true) {
            System.out.println("\n--- ДИЗАЙНЕРЫ ---");
            System.out.println("1. Создать");
            System.out.println("2. Показать всех");
            System.out.println("3. Найти по ID");
            System.out.println("4. Изменить");
            System.out.println("5. Удалить");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> createDesigner();
                case 2 -> showAllDesigners();
                case 3 -> showDesignerById();
                case 4 -> updateDesigner();
                case 5 -> deleteDesigner();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт");
            }
        }
    }

    private static void createDesigner() {
        try {
            System.out.print("ФИО: ");
            String name = scanner.nextLine();
            System.out.print("Специализация: ");
            String spec = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            Designer designer = new Designer(name, spec, email);
            designerService.create(designer);
            System.out.println("Дизайнер создан, ID = " + designer.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showAllDesigners() {
        try {
            List<Designer> list = designerService.getAll();
            if (list.isEmpty()) {
                System.out.println("Список пуст");
            } else {
                list.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showDesignerById() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            System.out.println(designerService.getById(id));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void updateDesigner() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            Designer designer = designerService.getById(id);
            System.out.print("Новое ФИО (" + designer.getFullName() + "): ");
            String name = scanner.nextLine();
            System.out.print("Новая специализация (" + designer.getSpecialization() + "): ");
            String spec = scanner.nextLine();
            System.out.print("Новый email (" + designer.getEmail() + "): ");
            String email = scanner.nextLine();
            designer.setFullName(name);
            designer.setSpecialization(spec);
            designer.setEmail(email);
            designerService.update(designer);
            System.out.println("Дизайнер обновлён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteDesigner() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            designerService.delete(id);
            System.out.println("Дизайнер удалён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void orderMenu() {
        while (true) {
            System.out.println("\n--- ДИЗАЙН-ЗАКАЗЫ ---");
            System.out.println("1. Создать");
            System.out.println("2. Показать все");
            System.out.println("3. Найти по ID");
            System.out.println("4. Изменить");
            System.out.println("5. Удалить");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> createOrder();
                case 2 -> showAllOrders();
                case 3 -> showOrderById();
                case 4 -> updateOrder();
                case 5 -> deleteOrder();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт");
            }
        }
    }

    private static void createOrder() {
        try {
            System.out.print("Название: ");
            String title = scanner.nextLine();
            System.out.print("Описание: ");
            String desc = scanner.nextLine();
            System.out.print("Цена: ");
            BigDecimal price = new BigDecimal(scanner.nextLine());
            System.out.print("Статус (NEW/IN_PROGRESS/DONE/CANCELLED): ");
            OrderStatus status = OrderStatus.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("ID клиента: ");
            int clientId = readInt();
            System.out.print("ID дизайнера: ");
            int designerId = readInt();
            DesignOrder order = new DesignOrder(title, desc, price, status, LocalDate.now(), clientId, designerId);
            orderService.create(order);
            System.out.println("Заказ создан, ID = " + order.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showAllOrders() {
        try {
            List<DesignOrder> list = orderService.getAll();
            if (list.isEmpty()) {
                System.out.println("Список пуст");
            } else {
                list.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showOrderById() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            System.out.println(orderService.getById(id));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void updateOrder() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            DesignOrder order = orderService.getById(id);
            System.out.print("Новое название (" + order.getTitle() + "): ");
            order.setTitle(scanner.nextLine());
            System.out.print("Новое описание (" + order.getDescription() + "): ");
            order.setDescription(scanner.nextLine());
            System.out.print("Новая цена (" + order.getPrice() + "): ");
            order.setPrice(new BigDecimal(scanner.nextLine()));
            System.out.print("Новый статус (" + order.getStatus() + "): ");
            order.setStatus(OrderStatus.valueOf(scanner.nextLine().toUpperCase()));
            System.out.print("Новый ID клиента (" + order.getClientId() + "): ");
            order.setClientId(readInt());
            System.out.print("Новый ID дизайнера (" + order.getDesignerId() + "): ");
            order.setDesignerId(readInt());
            orderService.update(order);
            System.out.println("Заказ обновлён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteOrder() {
        try {
            System.out.print("ID: ");
            int id = readInt();
            orderService.delete(id);
            System.out.println("Заказ удалён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void searchMenu() {
        while (true) {
            System.out.println("\n--- ПОИСК ---");
            System.out.println("1. По названию заказа");
            System.out.println("2. По описанию заказа");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Текст: ");
                    String text = scanner.nextLine();
                    List<DesignOrder> result = orderService.searchByTitle(text);
                    if (result.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        result.forEach(System.out::println);
                    }
                }
                case 2 -> {
                    System.out.print("Текст: ");
                    String text = scanner.nextLine();
                    List<DesignOrder> result = orderService.searchByDescription(text);
                    if (result.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        result.forEach(System.out::println);
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт");
            }
        }
    }

    private static void filterMenu() {
        while (true) {
            System.out.println("\n--- ФИЛЬТРАЦИЯ ---");
            System.out.println("1. По статусу");
            System.out.println("2. По диапазону цены");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Статус (NEW/IN_PROGRESS/DONE/CANCELLED): ");
                    OrderStatus status = OrderStatus.valueOf(scanner.nextLine().toUpperCase());
                    List<DesignOrder> result = orderService.filterByStatus(status);
                    if (result.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        result.forEach(System.out::println);
                    }
                }
                case 2 -> {
                    System.out.print("Мин. цена: ");
                    BigDecimal min = new BigDecimal(scanner.nextLine());
                    System.out.print("Макс. цена: ");
                    BigDecimal max = new BigDecimal(scanner.nextLine());
                    List<DesignOrder> result = orderService.filterByPriceRange(min, max);
                    if (result.isEmpty()) {
                        System.out.println("Ничего не найдено");
                    } else {
                        result.forEach(System.out::println);
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт");
            }
        }
    }

    private static void sortMenu() {
        while (true) {
            System.out.println("\n--- СОРТИРОВКА ---");
            System.out.println("1. По цене");
            System.out.println("2. По дате");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> orderService.sortByPrice().forEach(System.out::println);
                case 2 -> orderService.sortByDate().forEach(System.out::println);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт");
            }
        }
    }

    private static void showStatistics() {
        try {
            Map<String, Long> stats = orderService.getStatistics();
            System.out.println("\n--- СТАТИСТИКА ---");
            stats.forEach((key, value) -> System.out.println(key + ": " + value));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void exportToExcel() {
        try {
            List<DesignOrder> orders = orderService.getAll();
            ExcelExporter.exportOrders(orders, "orders.xlsx");
            System.out.println("Экспортировано в orders.xlsx");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showAllTables() {
        try {
            System.out.println("\n--- КЛИЕНТЫ ---");
            clientService.getAll().forEach(System.out::println);
            System.out.println("\n--- ДИЗАЙНЕРЫ ---");
            designerService.getAll().forEach(System.out::println);
            System.out.println("\n--- ЗАКАЗЫ ---");
            orderService.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка: введите целое число. Повторите: ");
            }
        }
    }
}