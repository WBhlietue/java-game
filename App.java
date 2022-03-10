import java.lang.Thread;
import java.util.*;

public class App {
    public int[][] panels = new int[10][10];
    Parts head;
    public static App instance;
    Scanner scanner = new Scanner(System.in);
    String input;
    int x = 1, y = 0;
    boolean playing = true;
    public int length = 1;
    Random random = new Random();

    int _x = 0, _y = 0;

    public App() {
        if (instance == null) {
            instance = this;
        }
        head = new Parts();
        head.bodyNum = 3;
    }

    public void Move() {
        if (playing) {
            head.Set(x, y);
            Print();
        }
    }

    public void Main() {
        new Test();
        Init();
        RandomFood();
        Print();
        while (playing) {
            input = scanner.next();
            switch (input) {
                case "a":
                    x = -1;
                    y = 0;
                    break;
                case "d":
                    x = 1;
                    y = 0;
                    break;
                case "w":
                    x = 0;
                    y = -1;
                    break;
                case "s":
                    x = 0;
                    y = 1;
                    break;
                case "q":
                    GameOver();
                    return;
                default:
                    x = 0;
                    y = 0;
                    break;
            }

        }
        GameOver();
    }

    void Init() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                panels[i][j] = 0;
            }
        }
        head.Init(1, 1);

    }

    void Print() {
        System.out.print("\033[H\033[2J");
        System.out.println("length: " + length);
        System.out.print("   ");
        for(int i = 0; i < 10; i++){
            System.out.print(i + " ");
        }
        System.out.println("");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < 10; j++) {
                if (panels[i][j] == 0) {
                    System.out.print(" |");
                } else if (panels[i][j] == 1) {
                    System.out.print("*|");
                } else if (panels[i][j] == 2) {
                    System.out.print("o|");
                } else if (panels[i][j] == 3) {
                    System.out.print("@|");
                }
            }
            System.out.print("\n");
        }
    }

    void RandomFood() {
        if (length == 100) {
            playing = false;
            return;
        }
        int x, y;
        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
        } while (panels[y][x] == 1 || panels[y][x] == 2);
        _x = x;
        _y = y;
        panels[y][x] = 2;
    }

    public void GameOver() {
        playing = false;
        System.out.print("\033[H\033[2J");
        System.out.println("Game Over!");
    }

    public static void main(String[] args) {
        App app = new App();

        app.Main();
    }
}

class Parts {
    private int x;
    private int y;
    public Parts nextParts;
    private App app;
    boolean next = false;
    private int preX = 0, preY = 0;

    public int bodyNum = 1;

    public Parts() {
        app = App.instance;
    }

    public void Init(int _x, int _y) {
        x = _x;
        y = _y;
        app.panels[y][x] = bodyNum;
    }

    public void Add() {
        if (nextParts == null) {
            app.length++;
            next = true;
        } else {
            nextParts.Add();
        }

    }

    public void SetPos(int _x, int _y) {
        app.panels[y][x] = 0;
        preX = x;
        preY = y;
        x = _x;
        y = _y;
        app.panels[y][x] = bodyNum;
        if (nextParts != null) {
            nextParts.SetPos(preX, preY);
        }
        if (next) {
            nextParts = new Parts();
            nextParts.Init(preX, preY);
            next = false;
        }
    }

    public void Set(int _x, int _y) {
        if (x + _x < 0 || x + _x >= 10) {
            return;
        }
        if (y + _y < 0 || y + _y >= 10) {
            return;
        }
        if (nextParts != null) {
            if (x + _x == nextParts.x && y + _y == nextParts.y) {
                return;
            }
        }
        if (app.panels[y + _y][x + _x] == 1) {
            app.playing = false;
        }
        if (app.panels[y + _y][x + _x] == 2) {
            Add();
            app.RandomFood();
        }
        app.panels[y][x] = 0;
        preX = x;
        preY = y;

        x += _x;
        y += _y;
        app.panels[y][x] = bodyNum;
        if (nextParts != null) {
            nextParts.SetPos(preX, preY);
        }
        if (next) {
            nextParts = new Parts();
            nextParts.Init(preX, preY);
            next = false;
        }
    }
}

class Test extends Thread {
    public void run() {
        App app = App.instance;
        while (true) {
            try {
                Thread.sleep(750);
                if (app.playing) {
                    app.Move();
                } else {
                    app.GameOver();
                    return;
                }
            } catch (Exception e) {

            }
        }
    }

    public Test() {
        start();
    }

}