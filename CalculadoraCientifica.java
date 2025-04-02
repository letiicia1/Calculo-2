import java.util.Scanner;
import java.util.Stack;

class CalculadoraCientifica {

    static Stack<String> pilhaOperadores = new Stack<>();

    static void inserir(String a) {
        pilhaOperadores.push(a);
    }

    static String desempilhar() {
        return pilhaOperadores.isEmpty() ? "" : pilhaOperadores.pop();
    }

    static int prioridade(String a) {
        if (a.equals("*") || a.equals("/") || a.equals("e") || a.equals("ln") || a.equals("sqrt") || a.equals("pow") || a.equals("sin") || a.equals("cos") || a.equals("tan"))
            return 2;
        else if (a.equals("+") || a.equals("-"))
            return 1;
        return 0;
    }

    static String converterPosFixa(String entrada) {
        StringBuilder resultado = new StringBuilder();
        String[] partes = entrada.split(" ");

        for (String parte : partes) {
            if (parte.equals("(")) {
                inserir(parte);
            } else if (parte.equals(")")) {
                while (!pilhaOperadores.isEmpty() && !pilhaOperadores.peek().equals("(")) {
                    resultado.append(desempilhar()).append(" ");
                }
                desempilhar(); // Remove o '('
            } else if (prioridade(parte) > 0) {
                while (!pilhaOperadores.isEmpty() && prioridade(pilhaOperadores.peek()) >= prioridade(parte)) {
                    resultado.append(desempilhar()).append(" ");
                }
                inserir(parte);
            } else {
                resultado.append(parte).append(" ");
            }
        }

        while (!pilhaOperadores.isEmpty()) {
            resultado.append(desempilhar()).append(" ");
        }

        return resultado.toString().trim();
    }

    static double resolverPosFixa(String expressao, double x) {
        Stack<Double> pilha = new Stack<>();
        String[] tokens = expressao.split(" ");

        for (String token : tokens) {
            switch (token) {
                case "sin": pilha.push(Math.sin(pilha.pop())); break;
                case "cos": pilha.push(Math.cos(pilha.pop())); break;
                case "tan": pilha.push(Math.tan(pilha.pop())); break;
                case "ln": pilha.push(Math.log(pilha.pop())); break;
                case "e": pilha.push(Math.exp(pilha.pop())); break;
                case "+": pilha.push(pilha.pop() + pilha.pop()); break;
                case "-": {
                    double b = pilha.pop(), a = pilha.pop();
                    pilha.push(a - b);
                    break;
                }
                case "*": pilha.push(pilha.pop() * pilha.pop()); break;
                case "/": {
                    double divisor = pilha.pop();
                    pilha.push(pilha.pop() / divisor);
                    break;
                }
                case "sqrt": pilha.push(Math.sqrt(pilha.pop())); break;
                case "pow": {
                    double exp = pilha.pop(), base = pilha.pop();
                    pilha.push(Math.pow(base, exp));
                    break;
                }
                default:
                    pilha.push(token.equals("x") ? x : Double.parseDouble(token));
                    break;
            }
        }
        return pilha.pop();
    }

    static double regraDoTrapezio(String entrada, double x, double y) {
        int n = 1000; // Número de divisões
        double h = (y - x) / n;
        double soma = 0;

        for (int i = 1; i < n; i++) {
            double xi = x + i * h;
            soma += resolverPosFixa(entrada, xi);
        }

        return (h / 2) * (resolverPosFixa(entrada, x) + 2 * soma + resolverPosFixa(entrada, y));
    }

    public static void opcao2() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite uma expressão separada por espaços: ");
        String entrada = sc.nextLine();
        System.out.print("Entrar com limites: ");
        double a = sc.nextDouble(), y = sc.nextDouble();
        sc.nextLine();
        
        String posFixa = converterPosFixa(entrada);
        double resultado = regraDoTrapezio(posFixa, a, y);
        System.out.println("Resultado da área da expressão: " + resultado + "\n");
    }

    public static void opcao1() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite uma expressão separada por espaços: ");
        String entrada = sc.nextLine().trim();

        if (entrada.isEmpty()) {
            System.out.println("Erro: expressão inválida!");
            return;
        }

        System.out.print("Entrar com valor do X (se não houver colocar 0): ");
        double a = sc.nextDouble();
        sc.nextLine();
        
        String posFixa = converterPosFixa(entrada);
        if (posFixa.isEmpty()) {
            System.out.println("Erro: Expressão pós-fixa vazia!");
            return;
        }

        double resultado = resolverPosFixa(posFixa, a);
        System.out.println("Resultado da expressão: " + resultado + "\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x;
        do {
            System.out.println("O que você deseja fazer?");
            System.out.println("0 - Sair");
            System.out.println("1 - Calcular uma expressão");
            System.out.println("2 - Calcular a área da expressão");
            System.out.print("Opção: ");
            x = sc.nextInt();
            sc.nextLine();

            switch (x) {
                case 1: opcao1(); break;
                case 2: opcao2(); break;
            }
        } while (x != 0);
        sc.close();
    }
}
