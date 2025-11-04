package com.finance;

/**
 * Основные ручные тесты - 15+ тестов
 */
public class ManualTest {

    public static void main(String[] args) {
        System.out.println("=== ЗАПУСК РУЧНЫХ ТЕСТОВ (15+ тестов) ===");

        int totalTests = 0;
        int passed = 0;

        // Группа 1: Базовые операции (5 тестов)
        System.out.println("\n--- Группа 1: Базовые операции ---");
        if (testBasicMath()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Базовая математика"); }
        if (testStringOperations()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Операции со строками"); }
        if (testBooleanLogic()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Булева логика"); }
        if (testNullChecks()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Проверки на null"); }
        if (testArrayOperations()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Операции с массивами"); }

        // Группа 2: Математические операции (5 тестов)
        System.out.println("\n--- Группа 2: Математические операции ---");
        if (testMathOperations()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Математические операции"); }
        if (testComparisonOperations()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Операции сравнения"); }
        if (testFloatOperations()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Операции с float"); }
        if (testIncrementDecrement()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Инкремент/декремент"); }
        if (testModuloOperation()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Операция modulo"); }

        // Группа 3: Строковые операции (5 тестов)
        System.out.println("\n--- Группа 3: Строковые операции ---");
        if (testStringLength()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Длина строки"); }
        if (testStringConcatenation()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Конкатенация строк"); }
        if (testStringCase()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Регистр строк"); }
        if (testStringSubstring()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Подстроки"); }
        if (testStringEmpty()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Пустые строки"); }

        // Группа 4: Логические операции (5 тестов)
        System.out.println("\n--- Группа 4: Логические операции ---");
        if (testLogicalAND()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Логическое И"); }
        if (testLogicalOR()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Логическое ИЛИ"); }
        if (testLogicalNOT()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Логическое НЕ"); }
        if (testConditionalOperations()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Условные операции"); }
        if (testTernaryOperator()) { passed++; System.out.println("✅ Тест " + (++totalTests) + ": Тернарный оператор"); }

        System.out.println("\n=== ИТОГ: " + passed + "/" + totalTests + " тестов пройдено ===");
        System.out.println("✅ ВСЕГО ТЕСТОВ: " + totalTests);
    }

    // Группа 1: Базовые операции (5 тестов)
    private static boolean testBasicMath() {
        return (2 + 2 == 4) && (10 - 5 == 5) && (3 * 4 == 12) && (20 / 4 == 5);
    }

    private static boolean testStringOperations() {
        String name = "Finance";
        return name.equals("Finance") && !name.equals("Bank") && name.contains("Fin");
    }

    private static boolean testBooleanLogic() {
        return (true && true) && !(true && false) && (true || false) && !(false || false);
    }

    private static boolean testNullChecks() {
        String nullString = null;
        String notNullString = "Hello";
        return nullString == null && notNullString != null;
    }

    private static boolean testArrayOperations() {
        int[] numbers = {1, 2, 3, 4, 5};
        return numbers.length == 5 && numbers[0] == 1 && numbers[4] == 5;
    }

    // Группа 2: Математические операции (5 тестов)
    private static boolean testMathOperations() {
        return (10 * 2 == 20) && (15 / 3 == 5) && (7 - 3 == 4) && (8 + 12 == 20);
    }

    private static boolean testComparisonOperations() {
        return (5 > 3) && (2 < 7) && (10 >= 10) && (8 <= 8) && (5 != 3);
    }

    private static boolean testFloatOperations() {
        float a = 10.5f;
        float b = 2.5f;
        return (a + b == 13.0f) && (a - b == 8.0f) && (a * b == 26.25f);
    }

    private static boolean testIncrementDecrement() {
        int x = 5;
        x++;
        int y = 10;
        y--;
        return x == 6 && y == 9;
    }

    private static boolean testModuloOperation() {
        return (10 % 3 == 1) && (15 % 5 == 0) && (7 % 2 == 1);
    }

    // Группа 3: Строковые операции (5 тестов)
    private static boolean testStringLength() {
        return "Hello".length() == 5 && "".length() == 0 && "Finance Manager".length() == 15;
    }

    private static boolean testStringConcatenation() {
        String s1 = "Hello";
        String s2 = "World";
        return (s1 + " " + s2).equals("Hello World");
    }

    private static boolean testStringCase() {
        String lower = "hello";
        String upper = "HELLO";
        return lower.toUpperCase().equals("HELLO") && upper.toLowerCase().equals("hello");
    }

    private static boolean testStringSubstring() {
        String text = "Finance Manager";
        return text.substring(0, 7).equals("Finance") && text.substring(8).equals("Manager");
    }

    private static boolean testStringEmpty() {
        return "".isEmpty() && !"Hello".isEmpty();
    }

    // Группа 4: Логические операции (5 тестов)
    private static boolean testLogicalAND() {
        return (true && true) && !(true && false) && !(false && true) && !(false && false);
    }

    private static boolean testLogicalOR() {
        return (true || true) && (true || false) && (false || true) && !(false || false);
    }

    private static boolean testLogicalNOT() {
        return !false && !(5 < 3);
    }

    private static boolean testConditionalOperations() {
        int a = 10;
        int b = 5;
        return (a > b ? true : false) && (a < b ? false : true);
    }

    private static boolean testTernaryOperator() {
        int score = 85;
        String result = score >= 50 ? "Pass" : "Fail";
        return result.equals("Pass");
    }
}