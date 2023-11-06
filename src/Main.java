import java.util.TreeMap;
import java.util.Scanner;
import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        Scanner inStr = new Scanner(System.in);
        System.out.println("Input: ");
        String input = inStr.nextLine();
        inStr.close();
        calc(input);
    }

    public static String calc(String input) {
        Converter1 converter1 = new Converter1();
        Converter2 converter2 = new Converter2();
        String[] actions = {"+", "-", "/", "*"};


        //Определение знака действия:
        int actionIndex = -1;
        for (int i = 0; i < actions.length; i++) { // бежим по введенной  строке и сравниваем
            if (input.contains(actions[i])) {
                actionIndex = i;
                break;
            }
        }

        if (actionIndex == -1) { //если знак не обнаружен выбрасываем исключение
            try{
                throw new IOException();
            }catch (IOException e){
                System.out.println("throws Exception //т.к. строка не является математической операцией");
                return(null);
            }
        }

        for (int i = 0; i < actions.length; i++) { // бежим по введенной  строке и ищем первый с конца знак,
            boolean no = input.endsWith(actions[i]); // сравниваем его с actions если совпало -> исключение
            if (no == true){                       // на случай если в конце висит знак
                try{
                    throw new IOException();
                }catch (IOException e){
                    System.out.println("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)\n");
                    return(null);
                }
            }
        }



        //Делим строчку по найденному арифметическому знаку
        String[] data = input.split("[-/*+]");

        if (data.length>2){ //
            try{
                throw new IOException();
            }catch (IOException e){
                System.out.println("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)\n");
                return(null);
            }
        }



        if(converter2.ifRoman(data[0]) == converter2.ifRoman(data[1])){ //если два числа в одинаковой системе счисления то идем дальше
            int a,b;
            //Определяем, римские ли это числа
            boolean ifRoman = converter2.ifRoman(data[0]);

            if(ifRoman){        //если римские, то конвертируем их в арабские
                a = converter2.romanToInt(data[0]);
                b = converter2.romanToInt(data[1]);
            }else{              // в противном случае конвертируем из строки в число
                a = Integer.parseInt(data[0]);
                b = Integer.parseInt(data[1]);
            }
            if ((a> 10 || a < 0)||(b>10 || b <0)){  //если не лежит в разрешенном диапазоне то выкидываем исключение
                try{
                    throw new IOException();
                }catch (IOException e){
                    System.out.println("throws Exception //т.к. числа могут быть только от 1 до 10");
                    return(null);
                }
            }

            int result;
            // Калькуляция
            switch (actions[actionIndex]){
                case "+":
                    result = a+b;
                    break;
                case "-":
                    result = a-b;
                    break;
                case "*":
                    result = a*b;
                    break;
                default:
                    result = a/b;
                    break;
            }

            if(ifRoman){ //если считали римское число
                if (result<=0) {  //и если результат оказался <= 0; то выкидываем исключение
                    try{
                        throw new ArithmeticException();
                    }catch (ArithmeticException e){
                        System.out.println("throws Exception //т.к. в римской системе нет отрицательных чисел и нуля");
                    }
                }else { // в противном случае выводим результат
                    System.out.println("Output:");
                    System.out.println(converter1.intToRoman(result));
                }
            }
            else{ //в другом случае выводим арабские
                System.out.println("Output:");
                System.out.println(result);
            }
        }else{ // в другом случае (когда два числа в разных системах) выкидываем исключение
            try{
                throw new IOException();
            }catch (IOException e){
                System.out.println("throws Exception //т.к. используются одновременно разные системы счисления");
            }
        }
        return(null);

    }

}

class Converter1 {
    TreeMap<Integer, String> arabianKeyMap = new TreeMap<>();
    public Converter1() { // карта сопоставления арабских цифр римским.
        arabianKeyMap.put(100, "C");
        arabianKeyMap.put(90, "XC");
        arabianKeyMap.put(50, "L");
        arabianKeyMap.put(40, "XL");
        arabianKeyMap.put(10, "X");
        arabianKeyMap.put(9, "IX");
        arabianKeyMap.put(5, "V");
        arabianKeyMap.put(4, "IV");
        arabianKeyMap.put(1, "I");
    }
    public String intToRoman(int number) {
        String roman = "";
        int arabianKey;
        do {
            arabianKey = arabianKeyMap.floorKey(number);
            roman += arabianKeyMap.get(arabianKey);
            number -= arabianKey;
        } while (number != 0);
        return roman;
    }
}

class Converter2 {
    TreeMap<Character, Integer> romanKeyMap = new TreeMap<>();
    public Converter2() { //карта сопоставления римских цифр арабским.
        romanKeyMap.put('I', 1);
        romanKeyMap.put('V', 5);
        romanKeyMap.put('X', 10);
        romanKeyMap.put('L', 50);
        romanKeyMap.put('C', 100);

    }
    public boolean ifRoman(String number) { // приходит строка, конвертируем ее в char и берем первый символ
        return romanKeyMap.containsKey(number.charAt(0)); // сравниваем его если есть совпадения то это римские (true)
    }
    public int romanToInt(String s) {
        int end = s.length() - 1;
        char[] arr = s.toCharArray();
        int arabian;
        int result = romanKeyMap.get(arr[end]);
        for (int i = end - 1; i >= 0; i--) {
            arabian = romanKeyMap.get(arr[i]);
            if (arabian < romanKeyMap.get(arr[i + 1])) {
                result -= arabian;
            } else {
                result += arabian;
            }
        }
        return result;
    }
}