import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //Заготовки для тестов
        Cube c = new Cube(3);
        c.rotateFace(0, 2, 0);
        c.rotateFace(1, 0, 0);
        c.rotateFace(2, 1, 1);
        c.rotateFace(c.size, 3, 2);
        c.rotateCube(2);
        c.rotateCube(4);
        c.rotateCube(0);
        c.rotateCube(6);
        System.out.println(Arrays.deepToString(c.getFace('F')));
        Cube randomCube = new Cube(10);
        randomCube.confuse();
        randomCube.printlnCube();
    }
}
