import javafx.util.Pair;

import java.util.*;

public class Solver {

    private Cube cube;
    private char[] cornersFront = {'W', 'Y'};
    private char[] cornersSide = {'O', 'R'};
    private char[] sides = {'W', 'Y', 'R', 'O'};
    private String[] faces = {"Front", "Back", "Left", "Right"};

    public Solver(Cube ans) {
        cube = ans;
        decision();
    }

    public Cube getCube() {
        return cube;
    }

    private void decision() {
        System.out.println("Начальное положение:");
        System.out.println(cube.scan());
        firstLayer();
        secondLayer();
        thirdLayer();
        System.out.println("Решенный кубик:");
        System.out.println(cube.scan());
        System.out.println("Количество поворотов граней:" + (cube.getRotates() - 272));
        //в данном случае кубик запутывается за 272 поворота
    }

    /**
     * сборка первого слоя со стороны Down
     * сборка креста, постановка креста в правильное положение (совпадение цветов на боковый гранях)
     * постановка нижних углов на свои места
     */
    private void firstLayer() {
        String layer = "Down";
        char colorOfLayer = 'B';
        List foundAll = new ArrayList();
        while (foundAll.size() != 4) {
            for (char color : cube.getColors()) { //проверка, сколько изначально кубиков на своем месте
                Map<Pair<Integer, Integer>, Integer> same = cube.haveMiddleCube(layer, colorOfLayer, color);
                if (!same.isEmpty() &&
                        !foundAll.contains(new Pair<>(colorOfLayer, color))) {
                    for (Pair<Integer, Integer> pair : same.keySet()) {
                        if (same.get(pair) == 1) foundAll.add(new Pair<>(colorOfLayer, color));
                        else if (pair.getKey() != 2) {
                            if (pair.getValue() == 0) cube.rotateCube(0);
                            if (pair.getValue() == 2) cube.rotateCube(1);
                            cube.rotateFace("Left", 2, 3);
                            cube.rotateFace("Front", 2, 0);
                            cube.rotateFace("Front", 2, 3);
                            cube.rotateFace("Front", 2, 1);
                            if (pair.getValue() == 0) cube.rotateCube(1);
                            if (pair.getValue() == 2) cube.rotateCube(0);
                            foundAll.add(new Pair<>(colorOfLayer, color));
                        }
                    }
                }
            }
            for (String face : cube.getFacesName()) { //поиск оставшихся кубиков
                if (foundAll.size() == 4) break;
                switch (face) {
                    case "Front": {
                        cross(foundAll);
                        break;
                    }
                    case "Back": {
                        cube.rotateCube(0);
                        cube.rotateCube(0);
                        cross(foundAll);
                        break;
                    }
                    case "Up": {
                        cube.rotateFace("Front", 0, 0);
                        cross(foundAll);
                        cube.rotateCube(0);
                        cube.rotateCube(0);
                        cross(foundAll);
                    }
                    default:
                        break;
                }
            }
        }
        for (String face : cube.getFacesName()) { // правильная постановка креста (совпадение цветов)
            switch (face) {
                case "Up":
                    break;
                case "Down":
                    break;
                default: {
                    if (face.equals("Back")) {
                        cube.rotateCube(0);
                        cube.rotateCube(0);
                    }
                    if (face.equals("Left")) cube.rotateCube(0);
                    if (face.equals("Right")) cube.rotateCube(1);
                    Map<Pair<Integer, Integer>, Integer> sameColor = cube.haveMiddleCube("Front",
                            cube.getColors()[cube.getFaces().get("Front")], colorOfLayer);
                    if (sameColor.isEmpty()) {
                        Map<Pair<Integer, Integer>, Integer> search = cube.haveMiddleCube(layer, colorOfLayer,
                                cube.getColors()[cube.getFaces().get("Front")]);
                        for (Pair<Integer, Integer> pair : search.keySet()) {
                            if (pair.getKey() == 2) {
                                cube.rotateFace("Left", 2, 3);
                                cube.rotateFace("Front", 2, 1);
                                cube.rotateFace("Front", 2, 1);
                                cube.rotateFace("Left", 2, 2);
                                cube.rotateFace("Front", 2, 1);
                                cube.rotateFace("Front", 2, 1);
                                cube.rotateFace("Left", 2, 3);
                            }
                            switch (pair.getValue()) {
                                case 0: {
                                    cube.rotateFace("Front", 0, 2);
                                    cube.rotateFace("Front", 2, 1);
                                    cube.rotateFace("Front", 0, 3);
                                    cube.rotateFace("Front", 2, 0);
                                    cube.rotateFace("Front", 0, 2);
                                    break;
                                }
                                case 2: {
                                    cube.rotateFace("Front", 2, 2);
                                    cube.rotateFace("Front", 2, 0);
                                    cube.rotateFace("Front", 2, 3);
                                    cube.rotateFace("Front", 2, 1);
                                    cube.rotateFace("Front", 2, 2);
                                }
                            }
                        }
                    }
                    if (face.equals("Back")) {
                        cube.rotateCube(0);
                        cube.rotateCube(0);
                    }
                    if (face.equals("Left")) cube.rotateCube(1);
                    if (face.equals("Right")) cube.rotateCube(0);
                }
            }
        }
        while (cube.getColors()[cube.getFaces().get("Front")] != 'W') {
            cube.rotateCube(0);
        }
        downCorners();
    }

    /**
     * @param foundAll множество пар (координаты кубиков, которые стоят на месте)
     *                 поиск кубика на стороне front, постановка его на свободное место в "крест"
     */
    private void cross(List foundAll) {
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(0, 1));
        for (char color : sides) {
            Map<Pair<Integer, Integer>, Integer> same = cube.haveMiddleCube("Front",
                    'B', color);
            if (!same.isEmpty()) {
                if (!foundAll.contains(new Pair<>('B', color))) {
                    for (Pair<Integer, Integer> pair : same.keySet()) {
                        int way = 0;
                        int key = 0;
                        switch (pair.getValue()) {
                            case 0: {
                                cube.rotateFace("Front", 0, 2);
                                cube.rotateFace("Front", 0, 0);
                                cube.rotateFace("Front", 0, 3);
                                key = 0;
                                if (same.get(pair) == 1) way = 2;
                                else way = 1;
                                break;
                            }
                            case 2: {
                                cube.rotateFace("Front", 2, 2);
                                cube.rotateFace("Front", 0, 1);
                                cube.rotateFace("Front", 2, 3);
                                key = 0;
                                if (same.get(pair) == 1) way = 2;
                                else way = 1;
                                break;
                            }
                            case 1: {
                                key = pair.getKey();
                                way = same.get(pair);
                            }
                        }
                        switch (key) {
                            case 0: {
                                if (way == 1) {
                                    int step = 0;
                                    while (cube.getValue()[cube.getFaces().get("Down")][0][1] == 'B') {
                                        cube.rotateCube(1);
                                        cube.rotateFace("Front", 0, 0);
                                        step++;
                                    }
                                    cube.rotateFace("Left", 2, 2);
                                    cube.rotateFace("Front", 2, 0);
                                    cube.rotateFace("Front", 2, 3);
                                    cube.rotateFace("Front", 2, 1);
                                    foundAll.add(new Pair<>('B', color));
                                    while (step != 0) {
                                        cube.rotateCube(0);
                                        step--;
                                    }
                                } else {
                                    int step = 0;
                                    while (cube.getValue()[cube.getFaces().get("Down")][0][1] == 'B') {
                                        cube.rotateCube(1);
                                        cube.rotateFace("Front", 0, 0);
                                        step++;
                                    }
                                    cube.rotateFace("Left", 2, 3);
                                    cube.rotateFace("Left", 2, 3);
                                    foundAll.add(new Pair<>('B', color));
                                    while (step != 0) {
                                        cube.rotateCube(0);
                                        step--;
                                    }
                                }
                                break;
                            }
                            case 2: {
                                if (way == 2) {
                                    foundAll.add(new Pair<>('B', color));

                                } else {
                                    cube.rotateFace("Left", 2, 3);
                                    cube.rotateFace("Front", 2, 0);
                                    cube.rotateFace("Front", 2, 3);
                                    cube.rotateFace("Front", 2, 1);
                                    foundAll.add(new Pair<>('B', color));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * поиск и постановка нижних углов на свои места
     */
    private void downCorners() {
        List foundAll = new ArrayList();
        while (foundAll.size() != 4) {
            for (int i = 0; i < 2; i++) {
                if (foundAll.size() == 4) break;
                if (i == 1) {
                    cube.rotateCube(0);
                    cube.rotateCube(0);
                }
                for (char color : cornersFront) {
                    for (char colorSide : cornersSide) {
                        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> corner = cube.haveCorner("Front",
                                'B', color, colorSide);
                        if (!corner.isEmpty()) {
                            if (!foundAll.contains(new Pair<>(color, colorSide))) {
                                for (Pair pair : corner.keySet()) {
                                    Pair<Integer, Integer> value = corner.get(pair);
                                    int keyOfValue;
                                    char needColor;
                                    if (pair.equals(new Pair(0, 2))) {
                                        cube.rotateFace("Front", 0, 1);
                                        if (value.getKey() == 1) {
                                            keyOfValue = 2;
                                            if (value.getValue() == 2) needColor = color;
                                            else needColor = colorSide;
                                        } else if (value.getValue() == 1) {
                                            keyOfValue = 1;
                                            if (value.getKey() == 2) needColor = color;
                                            else needColor = colorSide;
                                        } else {
                                            keyOfValue = 3;
                                            if (value.getValue() == 3) needColor = color;
                                            else needColor = colorSide;
                                        }
                                    } else if (pair.equals(new Pair(2, 0))) {
                                        if ((value.getValue() == 3 && i == 0 && colorSide == 'R' && value.getKey() == 2) ||
                                                (value.getValue() == 3 && i == 1 && colorSide == 'O' && value.getKey() == 2)) {
                                            foundAll.add(new Pair<>(color, colorSide));
                                            break;
                                        } else {
                                            cube.rotateFace("Front", 0, 2);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 0, 3);
                                            if (value.getKey() == 1) {
                                                keyOfValue = 1;
                                                if (value.getValue() == 3) needColor = color;
                                                else needColor = colorSide;
                                            } else if (value.getValue() == 1) {
                                                keyOfValue = 3;
                                                if (value.getKey() == 3) needColor = color;
                                                else needColor = colorSide;
                                            } else {
                                                keyOfValue = 2;
                                                if (value.getKey() == 2) needColor = color;
                                                else needColor = colorSide;
                                            }
                                        }
                                    } else if (pair.equals(new Pair(2, 2))) {
                                        if ((value.getValue() == 3 && i == 0 && colorSide == 'O' && value.getKey() == 2) ||
                                                (value.getValue() == 3 && i == 1 && colorSide == 'R' && value.getKey() == 2)) {
                                            foundAll.add(new Pair<>(color, colorSide));
                                            break;
                                        } else {
                                            cube.rotateFace("Front", 2, 2);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 2, 3);
                                            if (value.getKey() == 1) {
                                                keyOfValue = 3;
                                                if (value.getValue() == 3) needColor = color;
                                                else needColor = colorSide;
                                            } else if (value.getValue() == 1) {
                                                keyOfValue = 1;
                                                if (value.getKey() == 3) needColor = color;
                                                else needColor = colorSide;
                                            } else {
                                                keyOfValue = 2;
                                                if (value.getValue() == 2) needColor = color;
                                                else needColor = colorSide;
                                            }
                                        }
                                    } else {
                                        if (value.getKey() == 1) {
                                            keyOfValue = 1;
                                            if (value.getValue() == 2) needColor = color;
                                            else needColor = colorSide;
                                        } else if (value.getValue() == 1) {
                                            keyOfValue = 2;
                                            if (value.getKey() == 2) needColor = color;
                                            else needColor = colorSide;
                                        } else {
                                            keyOfValue = 3;
                                            if (value.getKey() == 3) needColor = color;
                                            else needColor = colorSide;
                                        }
                                    }
                                    switch (keyOfValue) {
                                        case 1: {
                                            int step = 0;
                                            while (cube.getColors()[cube.getFaces().get("Left")] != needColor) {
                                                cube.rotateCube(0);
                                                cube.rotateFace("Front", 0, 1);
                                                step++;
                                            }
                                            cube.rotateFace("Left", 2, 2);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Left", 2, 3);
                                            while (step != 0) {
                                                cube.rotateCube(1);
                                                step--;
                                            }
                                            foundAll.add(new Pair<>(color, colorSide));
                                            break;
                                        }
                                        case 3: {
                                            int step = 0;
                                            while (cube.getValue()[cube.getFaces().get("Down")][0][0]
                                                    == 'B') {
                                                cube.rotateCube(0);
                                                cube.rotateFace("Front", 0, 1);
                                                step++;
                                            }
                                            cube.rotateFace("Front", 0, 2);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 0, 3);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 0, 1);
                                            while (step != 0) {
                                                cube.rotateCube(1);
                                                cube.rotateFace("Front", 0, 0);
                                                step--;
                                            }
                                        }
                                        case 2: {
                                            int step = 0;
                                            while (cube.getColors()[cube.getFaces().get("Front")] != needColor) {
                                                cube.rotateCube(0);
                                                cube.rotateFace("Front", 0, 1);
                                                step++;
                                            }
                                            cube.rotateFace("Front", 0, 2);
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Front", 0, 3);
                                            while (step != 0) {
                                                cube.rotateCube(1);
                                                step--;
                                            }
                                            foundAll.add(new Pair<>(color, colorSide));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (i == 1) {
                    cube.rotateCube(0);
                    cube.rotateCube(0);
                }
            }
        }
        while (cube.getColors()[cube.getFaces().get("Front")] != 'W') {
            cube.rotateCube(0);
        }
    }

    /**
     * сборка второго слоя
     * поиск нужных кубиков, постановка их на свои места
     */
    private void secondLayer() {
        List foundAll = new ArrayList();
        while (foundAll.size() != 4) {
            for (int i = 0; i < 2; i++) {
                if (foundAll.size() == 4) break;
                if (i == 1) {
                    cube.rotateCube(0);
                    cube.rotateCube(0);
                }
                for (char color : cornersFront) {
                    for (char colorSide : cornersSide) {
                        Map<Pair<Integer, Integer>, Integer> foundOnUp = cube.haveMiddleCube("Up", color, colorSide);
                        if (!foundOnUp.isEmpty()) {
                            for (Pair<Integer, Integer> pair : foundOnUp.keySet()) {
                                if (pair.getValue() % 2 == 0)
                                    cube.rotateFace("Front", 0, pair.getValue() / 2);
                            }
                        }
                        Map<Pair<Integer, Integer>, Integer> found = cube.haveMiddleCube("Front", color, colorSide);
                        if (!found.isEmpty()) {
                            if (!foundAll.contains(new Pair<>(color, colorSide))) {
                                for (Pair<Integer, Integer> pair : found.keySet()) {
                                    int value = found.get(pair);
                                    char nextColor = ' ';
                                    boolean flag = true;
                                    switch (pair.getValue()) {
                                        case 2: {
                                            if (cube.getValue()[cube.getFaces().get("Front")][1][2] ==
                                                    cube.getColors()[cube.getFaces().get("Front")]
                                                    && cube.getValue()[cube.getFaces().get("Right")][1][0] ==
                                                    cube.getColors()[cube.getFaces().get("Left")]) {
                                                foundAll.add(new Pair<>(color, colorSide));
                                                flag = false;
                                                break;
                                            }
                                            cube.rotateCube(1);
                                        }
                                        case 0: {
                                            if (pair.getValue() != 2) {
                                                if (cube.getValue()[cube.getFaces().get("Front")][1][0] ==
                                                        cube.getColors()[cube.getFaces().get("Front")]
                                                        && cube.getValue()[cube.getFaces().get("Left")][1][2] ==
                                                        cube.getColors()[cube.getFaces().get("Left")]) {
                                                    foundAll.add(new Pair<>(color, colorSide));
                                                    flag = false;
                                                    break;
                                                }
                                            }
                                            cube.rotateFace("Front", 0, 2);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 0, 3);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Left", 2, 2);
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Left", 2, 3);
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Front", 0, 0);
                                            if (value == 1) {
                                                if (pair.getValue() == 2) nextColor = color;
                                                else nextColor = colorSide;
                                            } else {
                                                if (pair.getValue() == 2) nextColor = colorSide;
                                                else nextColor = color;
                                            }
                                            break;
                                        }
                                        case 1: {
                                            if (value == 1) nextColor = color;
                                            else nextColor = colorSide;
                                        }
                                    }
                                    if (flag) {
                                        int step = 0;
                                        while (cube.getValue()[cube.getFaces().get("Front")][1][1] != nextColor) {
                                            cube.rotateCube(0);
                                            cube.rotateFace("Front", 0, 1);
                                            step++;
                                        }
                                        if (cube.getValue()[cube.getFaces().get("Up")][2][1] ==
                                                cube.getColors()[cube.getFaces().get("Left")]) {
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Front", 0, 2);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 0, 3);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Left", 2, 2);
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Left", 2, 3);
                                        } else {
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Front", 2, 2);
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Front", 2, 3);
                                            cube.rotateFace("Front", 0, 0);
                                            cube.rotateFace("Left", 2, 3);
                                            cube.rotateFace("Front", 0, 1);
                                            cube.rotateFace("Left", 2, 2);
                                        }
                                        while (step != 0) {
                                            cube.rotateCube(1);
                                            step--;
                                        }
                                        foundAll.add(new Pair<>(color, colorSide));
                                        if (pair.getValue() == 2) cube.rotateCube(0);
                                    }
                                }
                            }
                        }
                    }
                }
                if (i == 1) {
                    cube.rotateCube(0);
                    cube.rotateCube(0);
                }
            }
        }
        while (cube.getColors()[cube.getFaces().get("Front")] != 'W') {
            cube.rotateCube(0);
        }
    }

    /**
     * сборка третьего слоя
     * (верхний крест, при необходимости перестановка верхних двухцветных кубиков,
     * постановка углов на свои места, разворот их при необходимости)
     */
    private void thirdLayer() {
        List foundAll = new ArrayList();
        for (char color : sides) { //сколько изначально кубиков стоят правильно
            Map<Pair<Integer, Integer>, Integer> found = cube.haveMiddleCube("Up", 'G', color);
            if (!found.isEmpty()) {
                for (Pair pair : found.keySet()) {
                    if (found.get(pair) == 1) {
                        foundAll.add(new Pair<>('G', color));
                    }
                }
            }
        }
        switch (foundAll.size()) { // разворот оставшихся
            case 2: {
                if (cube.getValue()[cube.getFaces().get("Up")][0][1] == 'G') {
                    if (cube.getValue()[cube.getFaces().get("Up")][1][0] == 'G') {
                        turnCross(1);
                    } else if (cube.getValue()[cube.getFaces().get("Up")][2][1] == 'G') {
                        cube.rotateFace("Front", 0, 0);
                        turnCross(2);
                    } else {
                        cube.rotateFace("Front", 0, 0);
                        turnCross(1);
                    }
                } else if (cube.getValue()[cube.getFaces().get("Up")][1][0] == 'G') {
                    if (cube.getValue()[cube.getFaces().get("Up")][2][1] == 'G') {
                        cube.rotateFace("Front", 0, 1);
                        turnCross(1);
                    } else {
                        turnCross(2);
                    }
                } else {
                    cube.rotateFace("Front", 0, 0);
                    cube.rotateFace("Front", 0, 0);
                    turnCross(1);
                }
                break;
            }
            case 0: {
                turnCross(2);
                cube.rotateFace("Front", 0, 0);
                cube.rotateFace("Front", 0, 0);
                turnCross(1);
            }
        }
        while (cube.getValue()[cube.getFaces().get("Front")][0][1] !=
                cube.getColors()[cube.getFaces().get("Front")]) {
            cube.rotateFace("Front", 0, 0);
        }
        int num = 1;
        for (int i = 1; i < 4; i++) { // сколько углов изначально стоят правильно
            Map<Pair<Integer, Integer>, Integer> found = cube.haveMiddleCube(faces[i], sides[i], 'G');
            for (Pair pair : found.keySet()) {
                if (found.get(pair) == 1) num++;
            }
        }
        switch (num) { // постановка оставшихся на свои места
            case 1: {
                if ((cube.getValue()[cube.getFaces().get("Left")][0][1] ==
                        cube.getColors()[cube.getFaces().get("Right")])) {
                    turnCross(3);
                    cube.rotateFace("Front", 0, 1);
                }
                turnCross(3);
                cube.rotateFace("Front", 0, 1);
                break;
            }
            case 2: {
                if (cube.getValue()[cube.getFaces().get("Left")][0][1] ==
                        cube.getColors()[cube.getFaces().get("Left")]) {
                    cube.rotateFace("Front", 0, 1);
                    turnCross(3);
                    cube.rotateFace("Front", 0, 0);
                } else if (cube.getValue()[cube.getFaces().get("Right")][0][1] ==
                        cube.getColors()[cube.getFaces().get("Right")]) {
                    cube.rotateFace("Front", 0, 0);
                    cube.rotateFace("Front", 0, 0);
                    turnCross(3);
                    cube.rotateFace("Front", 0, 0);
                    cube.rotateFace("Front", 0, 0);
                } else {
                    cube.rotateFace("Front", 0, 1);
                    turnCross(3);
                    cube.rotateFace("Front", 0, 1);
                    cube.rotateFace("Front", 0, 1);
                    turnCross(3);
                }
            }
        }
        upCorners();
    }

    /**
     * @param way =1 поворот двух соседних кубиков на кресте //чтобы сверху был цвет стороны Up
     *            =2 поворот двух кубиков,стоящих напротив
     *            иначе перестановка соседний кубиков на кресте
     */
    private void turnCross(int way) {
        if (way == 1) {
            cube.rotateFace("Left", 2, 2);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 2, 2);
            cube.rotateFace("Front", 0, 0);
            cube.rotateFace("Front", 2, 3);
            cube.rotateFace("Left", 2, 3);
        } else if (way == 2) {
            cube.rotateFace("Left", 2, 2);
            cube.rotateFace("Front", 2, 2);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 2, 3);
            cube.rotateFace("Front", 0, 0);
            cube.rotateFace("Left", 2, 3);
        } else {
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 0, 2);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 0, 3);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 0, 2);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 0, 3);
        }
    }

    /**
     * постановка верхних углов на свои места и их разворот
     */
    private void upCorners() {
        List foundAll = new ArrayList();
        for (char color : cornersFront) { //постановка углов на свои места
            if (!foundAll.contains(new Pair(color, 'O'))) {
                Map<Pair<Integer, Integer>, Pair<Integer, Integer>> foundFront = cube.haveCorner("Front",
                        color, 'O', 'G');
                Map<Pair<Integer, Integer>, Pair<Integer, Integer>> foundBack = cube.haveCorner("Back",
                        color, 'O', 'G');
                for (Pair pair : foundFront.keySet()) {
                    if (!pair.equals(new Pair(0, 2))) {
                        cube.rotateCube(0);
                        turnCornersUp(2);
                        cube.rotateCube(1);
                        foundAll.add(new Pair<>(color, 'O'));
                    } else {
                        foundAll.add(new Pair<>(color, 'O'));
                    }
                }
                for (Pair pair : foundBack.keySet()) {
                    if (pair.equals(new Pair(0, 0))) {
                        turnCornersUp(1);
                        foundAll.add(new Pair<>(color, 'O'));
                    } else {
                        turnCornersUp(2);
                        foundAll.add(new Pair<>(color, 'O'));
                    }
                }
            }
            cube.rotateCube(1);
        }
        for (int i = 0; i < 3; i++) { // разворот при необходимости
            char color = cube.getValue()[cube.getFaces().get("Up")][2][2];
            if (color == cube.getColors()[cube.getFaces().get("Front")]) {
                rotate(1);
                cube.rotateFace("Front", 0, 0);
                rotate(2);
                cube.rotateFace("Front", 0, 1);
            } else if (color == cube.getColors()[cube.getFaces().get("Right")]) {
                rotate(2);
                cube.rotateFace("Front", 0, 0);
                rotate(1);
                cube.rotateFace("Front", 0, 1);
            }
            cube.rotateCube(0);
        }
        while (cube.getColors()[cube.getFaces().get("Front")] != 'W') {
            cube.rotateCube(0);
        }
    }

    /**
     * @param way = 1 (перестановка углов по часовой)
     *            иначе против часовой
     * углы меняются из положений (0,0),(0,2),(2,2) на стороне Up (перед пользователем сторона Front)
     */
    private void turnCornersUp(int way) {
        if (way == 1) {
            cube.rotateFace("Left", 0, 2);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Left", 2, 2);
            cube.rotateFace("Front", 0, 0);
            cube.rotateFace("Left", 0, 3);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Left", 2, 3);
            cube.rotateFace("Front", 0, 0);
        } else { //против
            cube.rotateFace("Front", 2, 2);
            cube.rotateFace("Front", 0, 0);
            cube.rotateFace("Front", 0, 2);
            cube.rotateFace("Front", 0, 1);
            cube.rotateFace("Front", 2, 3);
            cube.rotateFace("Front", 0, 0);
            cube.rotateFace("Front", 0, 3);
            cube.rotateFace("Front", 0, 1);
        }
    }

    /**
     * @param way =1 разворот углового кубика, если сверху цвет Front
     *            иначе, если цвет Right
     */
    private void rotate(int way) {
        if (way == 1) { //сверху фронт
            cube.rotateFace("Front", 2, 3);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Front", 2, 2);
            cube.rotateFace("Left", 2, 2);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Left", 2, 3);
        } else {
            cube.rotateFace("Left", 2, 2);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Left", 2, 3);
            cube.rotateFace("Front", 2, 3);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Front", 2, 0);
            cube.rotateFace("Front", 2, 2);
        }
    }
}