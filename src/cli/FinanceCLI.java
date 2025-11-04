package com.finance.cli;

import com.finance.service.*;
import com.finance.storage.DataStorage;
import com.finance.storage.FileStorage;
import com.finance.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FinanceCLI {
    private AuthService authService;
    private TransactionService transactionService;
    private BudgetService budgetService;
    private WalletService walletService;
    private EnhancedAlertService alertService;
    private FilterService filterService;
    private ExportService exportService;
    private FileStorage fileStorage;
    private Scanner scanner;
    private boolean running;

    public FinanceCLI() {
        DataStorage dataStorage = new DataStorage();
        this.authService = new AuthService(dataStorage);
        this.transactionService = new TransactionService();
        this.budgetService = new BudgetService();
        this.walletService = new WalletService(dataStorage);
        this.alertService = new EnhancedAlertService(transactionService, budgetService);
        this.filterService = new FilterService();
        this.exportService = new ExportService();
        this.fileStorage = new FileStorage();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║               СИСТЕМА УПРАВЛЕНИЯ ЛИЧНЫМИ ФИНАНСАМИ          ║");
        System.out.println("║                      (Finance Manager v1.0)                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        while (running) {
            if (!authService.isLoggedIn()) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
        scanner.close();
    }

    private void showAuthMenu() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("🔐 АВТОРИЗАЦИЯ");
        System.out.println("─".repeat(50));
        System.out.println("1. Вход в систему");
        System.out.println("2. Регистрация нового пользователя");
        System.out.println("3. Выход из приложения");
        System.out.print("🎯 Выберите действие: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                running = false;
                System.out.println("👋 До свидания!");
                break;
            default:
                System.out.println("❌ Неверный выбор! Попробуйте снова.");
        }
    }

    private void showMainMenu() {
        showEnhancedMainMenu();

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                addIncome();
                break;
            case "2":
                addExpense();
                break;
            case "3":
                setBudget();
                break;
            case "4":
                showStatistics();
                break;
            case "5":
                showFilterMenu();
                break;
            case "6":
                showExportMenu();
                break;
            case "7":
                transferMoney();
                break;
            case "8":
                showHelp();
                break;
            case "9":
                logout();
                break;
            default:
                System.out.println("❌ Неверный выбор! Попробуйте снова.");
        }
    }

    private void showEnhancedMainMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("💰 СИСТЕМА УПРАВЛЕНИЯ ФИНАНСАМИ");
        System.out.println("═".repeat(60));
        System.out.println("👤 Текущий пользователь: " + authService.getCurrentUser().getUsername());
        System.out.printf("💳 Баланс: %,12.2f%n", authService.getCurrentUser().getWallet().getBalance());
        System.out.println("─".repeat(60));

        // Показ оповещений
        showAlerts();

        System.out.println("\n📋 ОСНОВНЫЕ КОМАНДЫ:");
        System.out.println(" 1. 💰 Добавить доход");
        System.out.println(" 2. 💸 Добавить расход");
        System.out.println(" 3. 📊 Установить/изменить бюджет");
        System.out.println(" 4. 📈 Показать статистику");
        System.out.println(" 5. 🔍 Фильтр и анализ");
        System.out.println(" 6. 📤 Экспорт данных");
        System.out.println(" 7. 🔄 Перевод между пользователями");
        System.out.println(" 8. ❓ Помощь (help)");
        System.out.println(" 9. 🚪 Выход");
        System.out.print("🎯 Выберите действие: ");
    }

    private void showAlerts() {
        List<String> alerts = alertService.checkAlerts(authService.getCurrentUser());
        if (!alerts.isEmpty()) {
            System.out.println("\n🚨 ОПОВЕЩЕНИЯ:");
            for (String alert : alerts) {
                System.out.println(" • " + alert);
            }
        }
    }

    private void login() {
        System.out.print("👤 Логин: ");
        String username = scanner.nextLine();
        System.out.print("🔑 Пароль: ");
        String password = scanner.nextLine();

        try {
            if (authService.login(username, password)) {
                System.out.println("✅ Успешный вход!");
                // Загрузка данных пользователя
                Wallet wallet = fileStorage.loadUserData(username);
                if (wallet != null) {
                    // Восстанавливаем данные кошелька
                    authService.getCurrentUser().getWallet().getTransactions().addAll(wallet.getTransactions());
                    authService.getCurrentUser().getWallet().getBudgets().addAll(wallet.getBudgets());
                    System.out.println("📁 Данные пользователя загружены");
                }
            } else {
                System.out.println("❌ Неверный логин или пароль!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void register() {
        System.out.print("👤 Придумайте логин: ");
        String username = scanner.nextLine();
        System.out.print("🔑 Придумайте пароль: ");
        String password = scanner.nextLine();

        try {
            if (authService.register(username, password)) {
                System.out.println("✅ Регистрация успешна! Теперь вы можете войти в систему.");
            } else {
                System.out.println("❌ Пользователь с таким логином уже существует!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void addIncome() {
        try {
            System.out.print("🏷️  Категория дохода: ");
            String category = scanner.nextLine();
            System.out.print("💵 Сумма: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("📝 Описание: ");
            String description = scanner.nextLine();

            transactionService.addIncome(authService.getCurrentUser(), category, amount, description);
            System.out.println("✅ Доход добавлен!");
            checkAlerts();
        } catch (NumberFormatException e) {
            System.out.println("❌ Неверный формат суммы!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void addExpense() {
        try {
            System.out.print("🏷️  Категория расхода: ");
            String category = scanner.nextLine();
            System.out.print("💵 Сумма: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("📝 Описание: ");
            String description = scanner.nextLine();

            transactionService.addExpense(authService.getCurrentUser(), category, amount, description);
            System.out.println("✅ Расход добавлен!");
            checkAlerts();
        } catch (NumberFormatException e) {
            System.out.println("❌ Неверный формат суммы!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void setBudget() {
        try {
            System.out.print("🏷️  Категория для бюджета: ");
            String category = scanner.nextLine();
            System.out.print("💰 Лимит бюджета: ");
            double limit = Double.parseDouble(scanner.nextLine());

            budgetService.setBudget(authService.getCurrentUser(), category, limit);
            System.out.println("✅ Бюджет установлен!");
        } catch (NumberFormatException e) {
            System.out.println("❌ Неверный формат суммы!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showStatistics() {
        User user = authService.getCurrentUser();

        System.out.println("\n" + "═".repeat(70));
        System.out.println("📊 ФИНАНСОВАЯ СТАТИСТИКА");
        System.out.println("═".repeat(70));
        System.out.printf("💰 Общий доход: %,15.2f%n", transactionService.getTotalIncome(user));
        System.out.printf("💸 Общие расходы: %,15.2f%n", transactionService.getTotalExpenses(user));
        System.out.printf("💳 Текущий баланс: %,15.2f%n", user.getWallet().getBalance());
        System.out.println("─".repeat(70));

        System.out.println("\n📈 Доходы по категориям:");
        List<String> incomeCategories = transactionService.getIncomeCategories(user);
        if (incomeCategories.isEmpty()) {
            System.out.println("   Нет данных о доходах");
        } else {
            for (String category : incomeCategories) {
                double amount = transactionService.getIncomeByCategory(user, category);
                System.out.printf("   🏷️  %-20s: %,12.2f%n", category, amount);
            }
        }

        System.out.println("\n📉 Расходы по категориям:");
        List<String> expenseCategories = transactionService.getExpenseCategories(user);
        if (expenseCategories.isEmpty()) {
            System.out.println("   Нет данных о расходах");
        } else {
            for (String category : expenseCategories) {
                double amount = transactionService.getExpensesByCategory(user, category);
                System.out.printf("   🏷️  %-20s: %,12.2f%n", category, amount);
            }
        }

        System.out.println("\n🎯 Бюджеты по категориям:");
        List<Budget> budgets = user.getWallet().getBudgets();
        if (budgets.isEmpty()) {
            System.out.println("   Бюджеты не установлены");
        } else {
            for (Budget budget : budgets) {
                double spent = transactionService.getExpensesByCategory(user, budget.getCategory());
                double remaining = budgetService.getRemainingBudget(user, budget.getCategory());
                String status = remaining >= 0 ? "✅" : "❌";
                System.out.printf("   %s 🏷️  %-20s Лимит: %,10.2f Потрачено: %,10.2f Осталось: %,10.2f%n",
                        status, budget.getCategory(), budget.getLimit(), spent, remaining);
            }
        }
        System.out.println("═".repeat(70));
    }

    private void showFilterMenu() {
        System.out.println("\n🔍 ФИЛЬТР И АНАЛИЗ ДАННЫХ");
        System.out.println("1. Анализ по категориям");
        System.out.println("2. Анализ по периоду");
        System.out.println("3. Комбинированный фильтр");
        System.out.print("🎯 Выберите тип анализа: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                filterByCategories();
                break;
            case "2":
                filterByPeriod();
                break;
            case "3":
                combinedFilter();
                break;
            default:
                System.out.println("❌ Неверный выбор!");
        }
    }

    private void filterByCategories() {
        System.out.println("\n🎯 Введите категории для фильтрации (через запятую):");
        System.out.print("Категории: ");
        String input = scanner.nextLine();

        List<String> categories = Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        filterService.printFilteredStatistics(authService.getCurrentUser(), categories, null, null);
    }

    private void filterByPeriod() {
        try {
            System.out.println("\n📅 Введите период для фильтрации (формат: ГГГГ-ММ-ДД):");
            System.out.print("Начальная дата: ");
            String startInput = scanner.nextLine();
            System.out.print("Конечная дата: ");
            String endInput = scanner.nextLine();

            LocalDate startDate = startInput.isEmpty() ? null : LocalDate.parse(startInput);
            LocalDate endDate = endInput.isEmpty() ? null : LocalDate.parse(endInput);

            filterService.printFilteredStatistics(authService.getCurrentUser(), null, startDate, endDate);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Неверный формат даты! Используйте ГГГГ-ММ-ДД");
        }
    }

    private void combinedFilter() {
        try {
            System.out.println("\n🎯 Комбинированный фильтр");
            System.out.print("Категории (через запятую): ");
            String categoriesInput = scanner.nextLine();
            System.out.print("Начальная дата (ГГГГ-ММ-ДД): ");
            String startInput = scanner.nextLine();
            System.out.print("Конечная дата (ГГГГ-ММ-ДД): ");
            String endInput = scanner.nextLine();

            List<String> categories = Arrays.stream(categoriesInput.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            LocalDate startDate = startInput.isEmpty() ? null : LocalDate.parse(startInput);
            LocalDate endDate = endInput.isEmpty() ? null : LocalDate.parse(endInput);

            filterService.printFilteredStatistics(authService.getCurrentUser(), categories, startDate, endDate);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Неверный формат даты! Используйте ГГГГ-ММ-ДД");
        }
    }

    private void showExportMenu() {
        System.out.println("\n📤 ЭКСПОРТ ДАННЫХ");
        System.out.println("1. Экспорт в CSV (для Excel)");
        System.out.println("2. Экспорт в JSON (резервная копия)");
        System.out.print("🎯 Выберите формат: ");

        String choice = scanner.nextLine();
        System.out.print("💾 Введите имя файла: ");
        String filename = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    if (!filename.toLowerCase().endsWith(".csv")) {
                        filename += ".csv";
                    }
                    exportService.exportToCsv(authService.getCurrentUser(), filename);
                    System.out.println("✅ Данные экспортированы в CSV файл: " + filename);
                    break;
                case "2":
                    if (!filename.toLowerCase().endsWith(".json")) {
                        filename += ".json";
                    }
                    exportService.exportToJson(authService.getCurrentUser(), filename);
                    System.out.println("✅ Данные экспортированы в JSON файл: " + filename);
                    break;
                default:
                    System.out.println("❌ Неверный выбор!");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка экспорта: " + e.getMessage());
        }
    }

    private void transferMoney() {
        try {
            System.out.print("👤 Логин получателя: ");
            String toUsername = scanner.nextLine();
            System.out.print("💵 Сумма перевода: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("📝 Описание перевода: ");
            String description = scanner.nextLine();

            if (walletService.transferMoney(
                    authService.getCurrentUser().getUsername(),
                    toUsername, amount, description)) {
                System.out.println("✅ Перевод выполнен успешно!");
                checkAlerts();
            } else {
                System.out.println("❌ Ошибка перевода: недостаточно средств или пользователь не найден!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Неверный формат суммы!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showHelp() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("📖 СПРАВКА ПО КОМАНДАМ");
        System.out.println("═".repeat(70));

        System.out.println("\n💰 ДОХОДЫ И РАСХОДЫ:");
        System.out.println(" • Добавление доходов/расходов: укажите категорию, сумму и описание");
        System.out.println(" • Категории создаются автоматически при первом использовании");

        System.out.println("\n📊 БЮДЖЕТЫ:");
        System.out.println(" • Установите лимиты для категорий расходов");
        System.out.println(" • Получайте уведомления при接近 80% и превышении лимита");

        System.out.println("\n🔍 ФИЛЬТРАЦИЯ:");
        System.out.println(" • Анализ данных за определённый период");
        System.out.println(" • Фильтр по нескольким категориям");
        System.out.println(" • Детальная статистика по выборке");

        System.out.println("\n📤 ЭКСПОРТ:");
        System.out.println(" • Экспорт данных в CSV для Excel");
        System.out.println(" • Экспорт в JSON для резервного копирования");

        System.out.println("\n🔄 ПЕРЕВОДЫ:");
        System.out.println(" • Переводы между зарегистрированными пользователями");
        System.out.println(" • Автоматическое отражение как расхода у отправителя и дохода у получателя");

        System.out.println("\n💾 СОХРАНЕНИЕ:");
        System.out.println(" • Данные автоматически сохраняются при выходе");
        System.out.println(" • Автоматическая загрузка при входе в систему");
        System.out.println("═".repeat(70));
    }

    private void checkAlerts() {
        List<String> alerts = alertService.checkAlerts(authService.getCurrentUser());
        if (!alerts.isEmpty()) {
            System.out.println("\n🚨 ОПОВЕЩЕНИЯ:");
            for (String alert : alerts) {
                System.out.println(" • " + alert);
            }
        }
    }

    private void logout() {
        // Сохранение данных при выходе
        if (authService.isLoggedIn()) {
            fileStorage.saveUserData(authService.getCurrentUser());
            System.out.println("💾 Данные сохранены.");
        }
        authService.logout();
        System.out.println("👋 Выход выполнен. Возврат к меню авторизации.");
    }
}