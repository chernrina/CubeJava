import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

class Cube {

    final int size;

    private Character[][][] value; //Хранит значение граней

    private HashMap<Character, Integer> faces = new HashMap<>();

    private final Character[] colors = {'W', 'Y', 'R', 'O', 'B', 'G'};

    private final Character[] facesName = {'F', 'B', 'L', 'R', 'D', 'U'};

    Cube(int size) {
        this.size = size;
        value = new Character[6][size][size];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    value[i][j][k] = colors[i];
                }
            }
            faces.put(facesName[i], i);
        }
    }

    private void turn(char name, int direction) { //Поворачивает грань против(direction == 0) и по(direction == 1) часовой стрелки грань name
        if (!(Arrays.asList(facesName).contains(name))) throw new IllegalArgumentException();
        Character[][] rotate = new Character[size][size];
        switch (direction) {
            case 0: {
                for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) {
                    rotate[i][j] = value[faces.get(name)][j][size - 1 - i]; //против часовой
                }
                break;
            }
            case 1: {
                for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) {
                    rotate[i][j] = value[faces.get(name)][size - 1 - j][i]; //по часовой
                }
                break;
            }
            default: throw new IllegalArgumentException();
        }
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) {
            value[faces.get(name)][i][j] =rotate[i][j];
        }
    }

    void rotateCube(int direction) { //Поворачивает кубик
        Character[] names;
        switch (direction) {
            case 0: { //Вправо
                names = new Character[]{'F', 'R', 'B', 'L', 'U', 'D'};
                break;
            }
            case 1: { //Влево
                names = new Character[]{'L', 'B', 'R', 'F', 'D', 'U'};
                break;
            }
            case 2: { //Вверх
                names = new Character[]{'F', 'U', 'B', 'D', 'L', 'R'};
                this.turn('U', 0);
                this.turn('U', 0);
                this.turn('B', 0);
                this.turn('B', 0);
                break;
            }
            case 3: { //Вниз
                names = new Character[]{'D', 'B', 'U', 'F', 'R', 'L'};
                this.turn('D', 0);
                this.turn('D', 0);
                this.turn('B', 0);
                this.turn('B', 0);
                break;
            }
            case 4: { //Против часовой
                names = new Character[]{'L', 'D', 'R', 'U', 'F', 'B'};
                for (int i = 0; i < 4; i++) this.turn(names[i], 0);
                break;
            }
            case 5: { //По часовой
                names = new Character[]{'U', 'R', 'D', 'L', 'B', 'F'};
                for (int i = 0; i < 4; i++) this.turn(names[i], 1);
                break;
            }
            default: throw new IllegalArgumentException();
        }
        //Переименовка 4 граней
        int temp = faces.get(names[3]);
        for (int i = 3; i > 0; i--) faces.put(names[i], faces.get(names[i - 1]));
        faces.put(names[0], temp);
        //Поворот 2 граней на 90 градусов
        this.turn(names[4], 0);
        this.turn(names[5], 1);
    }

    void rotateFace(int axis, int number, int direction) { //Поворачивает грань
        //axis(0 - X, 1 - Y, 2 - Z)(O - левый верхний ближний угол)(Ось X - вправо, Y - вниз, Z - вглубь)
        //number - номер грани от 0 по size - 1
        //direction(0 - вправо || вверх || против часовой; 1 - влево || вниз || по часовой)
        if (!(axis >= 0 && axis <= 2 && number >= 0 && number < size && (direction == 0 || direction == 1))) {
            throw new IllegalArgumentException();
        }
        int recover;
        switch (axis) {
            case 1: {
                this.rotateCube(4);
                recover = 5;
                break;
            }
            case 2: {
                this.rotateCube(1);
                recover = 0;
                break;
            }
            default: recover = -1;
        }
        Character[] names;
        switch (direction) {
            case 0: {
                names = new Character[]{'F', 'U', 'B', 'D'};
                break;
            }
            case 1: {
                names = new Character[]{'F', 'D', 'B', 'U'};
                break;
            }
            default: names = new Character[0];
        }
        Character[] temp = new Character[size];
        for (int i = 0; i < size; i++) temp[i] = value[faces.get(names[3])][i][number];
        for (int i = 0; i < size; i++) {
            value[faces.get(names[3])][i][number] = value[faces.get(names[2])][size - 1 - i][size - 1 - number];
        }
        for (int i = 0; i < size; i++) {
            value[faces.get(names[2])][size - 1 - i][size - 1 - number] = value[faces.get(names[1])][i][number];
        }
        for (int i = 0; i < size; i++) {
            value[faces.get(names[1])][i][number] = value[faces.get(names[0])][i][number];
        }
        for (int i = 0; i < size; i++) {
            value[faces.get(names[0])][i][number] = temp[i];
        }
        if (number == 0) this.turn('L', direction); //вообще '(0 + direction) % 2'
        if (number == size - 1) this.turn('R', (1 + direction) % 2);
        if (recover != -1) this.rotateCube(recover);
    }

    void confuse() { //Случайным образом "перемешивает" кубик
        for (int i = 0; i < 30 * size; i++) {
            this.rotateFace(new Random().nextInt(3), new Random().nextInt(size), new Random().nextInt(2));
        }
    }

    Character[][] getFace(char name) {
        return value[faces.get(name)];
    }

    void printlnCube() {
        for (int i = 0; i < 6; i++) {
            System.out.println(facesName[i] + ":");
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) System.out.print(value[faces.get(facesName[i])][j][k] + " ");
                System.out.println();
            }
        }
    }
}