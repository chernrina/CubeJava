import javafx.util.Pair;

import java.util.*;

public class Cube {

    final int size;

    private final char[][][] value; //Хранит значение граней

    private final Map<String, Integer> faces = new HashMap<>();

    private static final char[] colors = {'W', 'Y', 'R', 'O', 'B', 'G'};

    private static final String[] facesName = {"Front", "Back", "Left", "Right", "Down", "Up"};

    private static Random random;

    private int rotates = 0;

    public int getRotates() {
        return rotates;
    }

    public Cube(int size) {
        this.size = size;
        random = new Random();
        value = new char[6][size][size];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    value[i][j][k] = colors[i];
                }
            }
            faces.put(facesName[i], i);
        }
    }

    public void createCube(char[][][] value) { //Возможно задать несобираемый кубик!
        if (value.length != 6) throw new IllegalArgumentException();
        Map<Character, Integer> map = new HashMap<>();
        map.put('W', 0);
        map.put('Y', 0);
        map.put('R', 0);
        map.put('O', 0);
        map.put('B', 0);
        map.put('G', 0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    boolean charIsIncorrect = true;
                    for (char c : colors) {
                        if (c == value[i][j][k]) {
                            charIsIncorrect = false;
                            break;
                        }
                    }
                    if (charIsIncorrect) throw new IllegalArgumentException();
                    map.put(value[i][j][k], map.get(value[i][j][k]) + 1);
                }
            }
        }
        int num = size * size;
        if (map.get('W') != num || map.get('Y') != num || map.get('R') != num ||
                map.get('O') != num || map.get('B') != num || map.get('G') != num) throw new IllegalArgumentException();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    this.value[i][j][k] = value[i][j][k];
                }
            }
            faces.put(facesName[i], i);
        }
    }

    private void turn(String name, int direction) { //Поворачивает грань против(direction == 0) и по(direction == 1) часовой стрелки грань name
        if (!(Arrays.asList(facesName).contains(name))) throw new IllegalArgumentException();
        char[][] rotate = new char[size][size];
        switch (direction) {
            case 0: {
                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++) {
                        rotate[i][j] = value[faces.get(name)][j][size - 1 - i]; //против часовой
                    }
                break;
            }
            case 1: {
                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++) {
                        rotate[i][j] = value[faces.get(name)][size - 1 - j][i]; //по часовой
                    }
                break;
            }
            default:
                throw new IllegalArgumentException();
        }
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                value[faces.get(name)][i][j] = rotate[i][j];
            }
    }

    public void rotateCube(int direction) { //Поворачивает кубик. У пользователь перед глазами всегда фасад
        String[] names;
        switch (direction) {
            case 0: { //Вправо
                names = new String[]{"Front", "Right", "Back", "Left", "Up", "Down"};
                break;
            }
            case 1: { //Влево
                names = new String[]{"Left", "Back", "Right", "Front", "Down", "Up"};
                break;
            }
            case 2: { //Вверх
                names = new String[]{"Front", "Up", "Back", "Down", "Left", "Right"};
                this.turn("Up", 0);
                this.turn("Up", 0);
                this.turn("Back", 0);
                this.turn("Back", 0);
                break;
            }
            case 3: { //Вниз
                names = new String[]{"Down", "Back", "Up", "Front", "Right", "Left"};
                this.turn("Down", 0);
                this.turn("Down", 0);
                this.turn("Back", 0);
                this.turn("Back", 0);
                break;
            }
            case 4: { //Против часовой
                names = new String[]{"Left", "Down", "Right", "Up", "Front", "Back"};
                for (int i = 0; i < 4; i++) this.turn(names[i], 0);
                break;
            }
            case 5: { //По часовой
                names = new String[]{"Up", "Right", "Down", "Left", "Back", "Front"};
                for (int i = 0; i < 4; i++) this.turn(names[i], 1);
                break;
            }
            default:
                throw new IllegalArgumentException();
        }
        //Переименовка 4 граней
        int temp = faces.get(names[3]);
        for (int i = 3; i > 0; i--) faces.put(names[i], faces.get(names[i - 1]));
        faces.put(names[0], temp);
        //Поворот 2 граней на 90 градусов
        this.turn(names[4], 0);
        this.turn(names[5], 1);
    }

    public void rotateFace(String face, int number, int direction) { //Поворачивает грань
        rotates++;
        //face - имя грани
        //number - номер промежуточной грани от 0 по size - 1
        //direction(0 - вправо, 1 - влево, 2 - вверх, 3 - вниз)
        if (number < 0 || number >= size || direction < 0 || direction > 4) throw new IllegalArgumentException();
        boolean nameIsIncorrect = true;
        for (int i = 0; i < 6; i++) {
            if (Objects.equals(facesName[i], face)) {
                nameIsIncorrect = false;
                break;
            }
        }
        if (nameIsIncorrect) throw new IllegalArgumentException();
        int recover;
        switch (face) {
            case "Back": {
                this.rotateCube(0);
                this.rotateCube(0);
                recover = 7; // 1 and 1 (7 % 6 and 7 / 6)
                break;
            }
            case "Left": {
                this.rotateCube(0);
                recover = 1;
                break;
            }
            case "Right": {
                this.rotateCube(1);
                recover = 0;
                break;
            }
            case "Down": {
                this.rotateCube(2);
                recover = 3;
                break;
            }
            case "Up": {
                this.rotateCube(3);
                recover = 2;
                break;
            }
            default:
                recover = -1;
        }
        String[] names;
        switch (direction) {
            case 0: { //Вправо
                names = new String[]{"Front", "Right", "Back", "Left"};
                break;
            }
            case 1: { //Влево
                names = new String[]{"Front", "Left", "Back", "Right"};
                break;
            }
            case 2: { //Вверх
                names = new String[]{"Front", "Up", "Back", "Down"};
                break;
            }
            case 3: { //Вниз
                names = new String[]{"Front", "Down", "Back", "Up"};
                break;
            }
            default:
                names = new String[0];
        }
        char[] temp = new char[size];
        if (direction == 0 || direction == 1) {
            for (int i = 0; i < size; i++) temp[i] = value[faces.get(names[3])][number][i];
            for (int j = 3; j > 0; j--) {
                for (int i = 0; i < size; i++) {
                    value[faces.get(names[j])][number][i] = value[faces.get(names[j - 1])][number][i];
                }
            }
            for (int i = 0; i < size; i++) {
                value[faces.get(names[0])][number][i] = temp[i];
            }
            if (number == 0) this.turn("Up", direction); //вообще '(0 + direction) % 2'
            if (number == size - 1) this.turn("Down", (1 + direction) % 2);
        } else {
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
            if (number == 0) this.turn("Left", (0 + direction) % 2);
            if (number == size - 1) this.turn("Right", (1 + direction) % 2);
        }
        if (recover != -1) this.rotateCube(recover % 6);
        if (recover / 6 != 0) this.rotateCube(1);
    }

    public void confuse() { //Случайным образом "перемешивает" кубик
        for (int i = 0; i < 30 * size; i++) {
            this.rotateFace(facesName[random.nextInt(5)], 0, random.nextInt(2));
        }
    }

    /**
     * @param face    имя грани, на которой ищем кубик (2 цвета)
     * @param oneSide цвет с одной стороны
     * @param twoSide цвет с другой стороны
     * @return пара: ключ - пара (номер строки, номер столбца) - координаты кубика на face,
     * значение = 1 - если цвет oneSide на стороне face, 2 - если на стороне face цвет twoSide
     */
    public Map<Pair<Integer, Integer>, Integer> haveMiddleCube(String face, char oneSide, char twoSide) {
        Map<Pair<Integer, Integer>, Integer> ans = new HashMap<>();
        if (oneSide == twoSide) return ans;
        switch (oneSide) {
            case 'W':
                if (twoSide == 'Y') return ans;
                break;
            case 'Y':
                if (twoSide == 'W') return ans;
                break;
            case 'R':
                if (twoSide == 'O') return ans;
                break;
            case 'O':
                if (twoSide == 'R') return ans;
                break;
            case 'B':
                if (twoSide == 'G') return ans;
                break;
            case 'G':
                if (twoSide == 'B') return ans;
        }
        int indexOfFace = faces.get(face);
        String nextFace = "";
        int i = 0;
        int row = 0;
        int column = 0;
        while (i != 4) { // 4 положения двухцветных кубиков
            i++;
            switch (i) {
                case 1: {
                    row = 0;
                    column = 1;
                    break;
                }
                case 2: {
                    row = 1;
                    column = 0;
                    break;
                }
                case 3: {
                    row = 1;
                    column = 2;
                    break;
                }
                case 4: {
                    row = 2;
                    column = 1;
                }
            }
            int ansI = row;
            int ansJ = column;
            int compare;
            if (value[indexOfFace][ansI][ansJ] == oneSide ||
                    value[indexOfFace][ansI][ansJ] == twoSide) {
                if (value[indexOfFace][ansI][ansJ] == oneSide) compare = 1;
                else compare = 2;
                switch (face) {
                    case "Right": {
                        if (ansJ == 2) {
                            nextFace = "Back";
                            ansI = 0;
                            ansJ = 1;
                        } else {
                            switch (ansI) {
                                case 0: {
                                    nextFace = "Up";
                                    break;
                                }
                                case 2: {
                                    nextFace = "Down";
                                    break;
                                }
                                default:
                                    nextFace = "Front";
                            }
                            ansI = 1;
                            ansJ = 2;
                        }
                        break;
                    }
                    case "Left": {
                        if (ansJ == 0) {
                            nextFace = "Back";
                            ansI = 1;
                            ansJ = 2;
                        } else {
                            switch (ansI) {
                                case 0: {
                                    nextFace = "Up";
                                    break;
                                }
                                case 2: {
                                    nextFace = "Down";
                                    break;
                                }
                                default:
                                    nextFace = "Front";
                            }
                            ansI = 1;
                            ansJ = 0;
                        }
                        break;
                    }
                    case "Front": {
                        switch (ansI) {
                            case 0: {
                                ansI += 2;
                                nextFace = "Up";
                                break;
                            }
                            case 2: {
                                ansI -= 2;
                                nextFace = "Down";
                            }
                        }
                        switch (ansJ) {
                            case 0: {
                                ansJ += 2;
                                nextFace = "Left";
                                break;
                            }
                            case 2: {
                                ansJ -= 2;
                                nextFace = "Right";
                            }
                        }
                        break;
                    }
                    case "Back": {
                        switch (ansJ) {
                            case 0: {
                                ansJ += 2;
                                nextFace = "Right";
                                break;
                            }
                            case 2: {
                                ansJ -= 2;
                                nextFace = "Left";
                            }
                        }
                        if (ansI == 0) nextFace = "Up";
                        if (ansI == 2) nextFace = "Down";
                        break;
                    }
                    case "Up": {
                        if (ansJ == 0) nextFace = "Left";
                        if (ansJ == 2) nextFace = "Right";
                        if (ansI == 2) nextFace = "Front";
                        if (ansI == 0) nextFace = "Back";
                        ansI = 0;
                        ansJ = 1;
                        break;
                    }
                    case "Down": {
                        switch (ansI) {
                            case 0: {
                                nextFace = "Front";
                                break;
                            }
                            case 2:
                                nextFace = "Back";
                        }
                        if (ansJ == 0) nextFace = "Left";
                        if (ansJ == 2) nextFace = "Right";
                        ansI = 2;
                        ansJ = 1;
                    }
                }
                if ((compare == 1 && value[faces.get(nextFace)][ansI][ansJ] == twoSide) ||
                        (compare == 2 && value[faces.get(nextFace)][ansI][ansJ] == oneSide)) {
                    ans.put(new Pair<>(row, column), compare);
                    return ans;
                }
            }
        }
        return ans;
    }


    /**
     * @param face      имя грани
     * @param oneSide   первый цвет
     * @param twoSide   второй цвет
     * @param threeSide третий цвет
     * @return пару: ключ -  пара(строка; столбец)-координаты найденного кубика,
     * значение - пара(1-на стороне front первый цвет ; 1-справа(столбец = 2) или слева(столбец = 0) первый цвет
     *                 2-на стороне front второй цвет   2- =|= второй цвет
     *                 3-на стороне front третий цвет   3- =|= третий цвет
     */
    public Map<Pair<Integer, Integer>, Pair<Integer, Integer>>
    haveCorner(String face, char oneSide, char twoSide, char threeSide) {
        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> ans = new HashMap<>();
        int indexOfFace = faces.get(face);
        String nextFace = "";
        int i = 0;
        while (i != 4) {
            int row = 0;
            int column = 0;
            i++;
            switch (i) {
                case 2: {
                    column = 2;
                    break;
                }
                case 3: {
                    row = 2;
                    break;
                }
                case 4: {
                    row = 2;
                    column = 2;
                }
            }
            int ansI = row;
            int ansJ = column;
            int compare = 0;
            if (value[indexOfFace][ansI][ansJ] == oneSide) compare = 1;
            if (value[indexOfFace][ansI][ansJ] == twoSide) compare = 2;
            if (value[indexOfFace][ansI][ansJ] == threeSide) compare = 3;
            if (compare != 0) {
                switch (face) {
                    case "Front": {
                        switch (ansI) {
                            case 0: {
                                ansI = 2;
                                nextFace = "Up";
                                break;
                            }
                            case 2: {
                                ansI = 0;
                                nextFace = "Down";
                            }
                        }
                        break;
                    }
                    case "Back": {
                        if (ansI == 0) nextFace = "Up";
                        else nextFace = "Down";
                        switch (ansJ) {
                            case 0: {
                                ansJ = 2;
                                break;
                            }
                            case 2:
                                ansJ = 0;
                        }
                    }
                    default:
                        break;
                }
                int newCompare = 0;
                if (compare == 1) {
                    if (value[faces.get(nextFace)][ansI][ansJ] == twoSide) newCompare = 3;
                    if (value[faces.get(nextFace)][ansI][ansJ] == threeSide) newCompare = 2;
                } else if (compare == 2) {
                    if (value[faces.get(nextFace)][ansI][ansJ] == oneSide) newCompare = 3;
                    if (value[faces.get(nextFace)][ansI][ansJ] == threeSide) newCompare = 1;
                } else {
                    if (value[faces.get(nextFace)][ansI][ansJ] == oneSide) newCompare = 2;
                    if (value[faces.get(nextFace)][ansI][ansJ] == twoSide) newCompare = 1;
                }
                if (newCompare != 0) {
                    String finalFace = "";
                    if (ansJ == 0) finalFace = "Left";
                    else finalFace = "Right";
                    switch (nextFace) {
                        case "Up": {
                            if (ansI == 2) {
                                if (ansJ == 0) ansJ = 2;
                                else ansJ = 0;
                                ansI = 0;
                            }
                            break;
                        }
                        case "Down": {
                            if (ansI == 0) {
                                if (ansJ == 0) ansJ = 2;
                                else ansJ = 0;
                                ansI = 2;
                            }
                        }
                        default:
                            break;
                    }
                    boolean found = false;
                    switch (newCompare) {
                        case 1: {
                            if (value[faces.get(finalFace)][ansI][ansJ] == oneSide) found = true;
                            break;
                        }
                        case 2: {
                            if (value[faces.get(finalFace)][ansI][ansJ] == twoSide) found = true;
                            break;
                        }
                        case 3: {
                            if (value[faces.get(finalFace)][ansI][ansJ] == threeSide) found = true;
                        }
                    }
                    if (found) {
                        ans.put(new Pair<>(row, column), new Pair<>(compare, newCompare));
                        return ans;
                    }
                }

            }
        }
        return ans;
    }


    public char[][] getFace(String name) {
        return value[faces.get(name)];
    }

    public char[] getColors() {
        return colors;
    }

    public Map<String, Integer> getFaces() {
        return faces;
    }

    public String[] getFacesName() {
        return facesName;
    }

    public char[][][] getValue() {
        return value;
    }

    public String scan() { //развёртка кубика
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append("              ");
            for (int j = 0; j < size; j++) result.append(value[faces.get("Up")][i][j]).append(" ");
            result.append('\n');
        }
        String[] middle = {"Back", "Left", "Front", "Right"};
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < 4; k++) {
                for (int j = 0; j < size; j++) result.append(value[faces.get(middle[k])][i][j]).append(" ");
                result.append(" ");
            }
            result.append("\n");
        }
        for (int i = 0; i < size; i++) {
            result.append("              ");
            for (int j = 0; j < size; j++) result.append(value[faces.get("Down")][i][j]).append(" ");
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int k = 0; k < 6; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++)
                    result.append(value[faces.get(facesName[k])][i][j]);
            }
            if (k / 5 != 1) result.append(" ");
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || other.getClass() != this.getClass()) return false;

        Cube c = (Cube) other;

        if (c.size != this.size) return false;

        boolean cubeIsEquals = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (Objects.equals(this.toString(), c.toString())) {
                    cubeIsEquals = true;
                    break;
                }
                c.rotateCube(5);
            }
            if (cubeIsEquals) break;
            c.rotateCube((i % 2) + 1);
        }
        return cubeIsEquals;
    }

    @Override
    public int hashCode() {
        return this.size;
    }
}