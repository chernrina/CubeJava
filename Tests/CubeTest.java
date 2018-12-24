import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CubeTest {
    private Cube c = new Cube(3);

    @Test
    void toStringTest() {
        assertEquals("WWWWWWWWW YYYYYYYYY RRRRRRRRR OOOOOOOOO BBBBBBBBB GGGGGGGGG",
                c.toString());
    }

    @Test
    void scanTest() {
        assertEquals("              G G G \n              G G G \n              G G G \n" +
                "Y Y Y  R R R  W W W  O O O  \nY Y Y  R R R  W W W  O O O  \nY Y Y  R R R  W W W  O O O  \n" +
                "              B B B \n              B B B \n              B B B \n", c.scan());
    }

    @Test
    void createCubeTest() {
        char[][][] value = new char[][][]{{{'W', 'W', 'W'}, {'R', 'R', 'R'}, {'W', 'W', 'W'}},
                {{'Y', 'Y', 'Y'}, {'O', 'O', 'O'}, {'Y', 'Y', 'Y'}},
                {{'R', 'R', 'R'}, {'Y', 'Y', 'Y'}, {'R', 'R', 'R'}},
                {{'O', 'O', 'O'}, {'W', 'W', 'W'}, {'O', 'O', 'O'}},
                {{'B', 'B', 'B'}, {'B', 'B', 'B'}, {'B', 'B', 'B'}},
                {{'G', 'G', 'G'}, {'G', 'G', 'G'}, {'G', 'G', 'G'}}};

        char[][][] value1 = new char[][][]{{{'W', 'W', 'W'}, {'W', 'W', 'O'}, {'W', 'W', 'W'}},
                {{'Y', 'Y', 'Y'}, {'Y', 'Y', 'Y'}, {'Y', 'Y', 'Y'}},
                {{'R', 'R', 'R'}, {'R', 'R', 'R'}, {'R', 'R', 'R'}},
                {{'O', 'O', 'O'}, {'W', 'O', 'O'}, {'O', 'O', 'O'}},
                {{'B', 'B', 'B'}, {'B', 'B', 'B'}, {'B', 'B', 'B'}},
                {{'G', 'G', 'G'}, {'G', 'G', 'G'}, {'G', 'G', 'G'}}};
        Cube c1 = new Cube(3);
        c1.createCube(value);
        assertEquals("WWWRRRWWW YYYOOOYYY RRRYYYRRR OOOWWWOOO BBBBBBBBB GGGGGGGGG", c1.toString());
    }

    @Test
    void rotateCubeAndFaceTest() {

        c.rotateFace("Front", 2, 2);
        assertEquals("WWBWWBWWB GYYGYYGYY RRRRRRRRR OOOOOOOOO BBYBBYBBY GGWGGWGGW", c.toString());

        c.rotateFace("Left", 0, 0);
        assertEquals("RRRWWBWWB OOOGYYGYY GYYRRRRRR WWBOOOOOO BBYBBYBBY WWWGGGGGG", c.toString());

        c.rotateFace("Up", 1, 0);
        assertEquals("RRRWWBWWB OOOGYYGYY GBYRBRRYR WGBOGOOGO BBYOOWBBY WWWRRYGGG", c.toString());

        assertThrows(IllegalArgumentException.class, () -> c.rotateFace("Facade", 3, 2));
        assertThrows(IllegalArgumentException.class, () -> c.rotateFace("Left", c.size, 2));
        assertThrows(IllegalArgumentException.class, () -> c.rotateFace("Left", 3, 6));

        c.rotateCube(2);
        assertEquals("BBYOOWBBY GGGYRRWWW YRRBBYGRR OOWGGGOOB YYGYYGOOO RRRWWBWWB", c.toString());

        c.rotateCube(4);
        assertEquals("YWYBOBBOB WYGWRGWRG RBBRWWRWW GGOYYOYYO RYRRBRYBG WGBOGOOGO", c.toString());

        assertThrows(IllegalArgumentException.class, () -> c.rotateCube(6));

    }

    @Test
    void randomTest() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                c.rotateCube(i);
            }
        }
        for (int i = 0; i < 4; i++) {
            c.rotateFace("Front", 0, 2);
        }
        for (int i = 0; i < 4; i++) {
            c.rotateFace("Front", 1, 1);
        }
        for (int i = 0; i < 4; i++) {
            c.rotateFace("Right", c.size - 1, 2);
        }
        assertEquals("WWWWWWWWW YYYYYYYYY RRRRRRRRR OOOOOOOOO BBBBBBBBB GGGGGGGGG", c.toString());
    }

    @Test
    void confuseTest() {
        c.confuse();
        int size = c.size * c.size + 1;
        String s = c.toString() + ' ';
        assertEquals(true, s.split("W").length == size);
        assertEquals(true, s.split("Y").length == size);
        assertEquals(true, s.split("R").length == size);
        assertEquals(true, s.split("O").length == size);
        assertEquals(true, s.split("B").length == size);
        assertEquals(true, s.split("G").length == size);
    }

    @Test
    void haveMiddleCube() {
        assertEquals(new Pair(new Pair<>(1, 2), 1),
                c.haveMiddleCube("Up", 'G', 'O'));
        assertEquals(new Pair<>(new Pair<>(0, 1), 1),
                c.haveMiddleCube("Front", 'W', 'G'));
        assertEquals(new Pair<>(new Pair<>(1, 2), 2),
                c.haveMiddleCube("Front", 'O', 'W'));
        assertEquals(new Pair(new Pair<>(-1, -1), -1),
                c.haveMiddleCube("Down", 'B', 'G'));
        assertEquals(new Pair<>(new Pair<>(1, 2), 2),
                c.haveMiddleCube("Back", 'R', 'Y'));
        c.rotateCube(0);
        assertEquals(new Pair<>(new Pair<>(1, 0), 1),
                c.haveMiddleCube("Front", 'R', 'Y'));
        c.rotateFace("Front", 2, 0);
        assertEquals(new Pair<>(new Pair<>(2, 1), 1),
                c.haveMiddleCube("Down", 'B', 'W'));
        assertEquals(new Pair(new Pair<>(-1, -1), -1),
                c.haveMiddleCube("Front", 'W', 'Y'));
        assertEquals(new Pair(new Pair<>(-1, -1), -1),
                c.haveMiddleCube("Down", 'O', 'R'));
        assertEquals(new Pair(new Pair<>(-1, -1), -1),
                c.haveMiddleCube("Right", 'G', 'B'));
        c.rotateCube(1);
        assertEquals(new Pair<>(new Pair<>(0, 1), 1),
                c.haveMiddleCube("Right", 'O', 'G'));
    }


    @Test
    void haveCorner() {
        assertEquals(new Pair<>(new Pair<>(0, 0), new Pair<>(1, 3)),
                c.haveCorner("Front", 'W', 'G', 'R'));
        assertEquals(new Pair<>(new Pair<>(0, 2), new Pair<>(2, 3)),
                c.haveCorner("Back", 'G', 'Y', 'R'));
        assertEquals(new Pair<>(new Pair<>(2, 0), new Pair<>(3, 2)),
                c.haveCorner("Front", 'B', 'R', 'W'));
        assertEquals(new Pair<>(new Pair<>(2, 2), new Pair<>(1, 3)),
                c.haveCorner("Front", 'W', 'B', 'O'));
        assertEquals(new Pair<>(new Pair<>(-1, -1), new Pair<>(-1, -1)),
                c.haveCorner("Back", 'j', 'd', 'v'));
        assertEquals(new Pair<>(new Pair<>(-1, -1), new Pair<>(-1, -1)),
                c.haveCorner("Back", 'G', 'W', 'O'));
        assertEquals(new Pair<>(new Pair<>(-1, -1), new Pair<>(-1, -1)),
                c.haveCorner("Front", 'B', 'O', 'R'));
        assertEquals(new Pair<>(new Pair<>(-1, -1), new Pair<>(-1, -1)),
                c.haveCorner("Front", 'W', 'W', 'W'));
    }


    @Test
    void equalsTest() {
        assertEquals(true, c.equals(new Cube(3)));
        assertEquals(false, c.equals(null));
        assertEquals(false, c.equals(new Cube(4)));
        c.rotateCube(4);
        assertEquals(true, c.equals(new Cube(3)));
        c.rotateFace("Front", 0, 0);
        assertEquals(false, c.equals(new Cube(3)));
    }
}