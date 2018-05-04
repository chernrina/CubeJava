import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CubeTest {
    private Cube c = new Cube(3);

    @Test
    void toStringTest() {
        assertEquals("WWWWWWWWWYYYYYYYYYRRRRRRRRROOOOOOOOOBBBBBBBBBGGGGGGGGG", c.toString());
    }

    @Test
    void scanTest() {
        assertEquals("              G G G \n              G G G \n              G G G \n" +
                "Y Y Y  R R R  W W W  O O O  \nY Y Y  R R R  W W W  O O O  \nY Y Y  R R R  W W W  O O O  \n" +
                "              B B B \n              B B B \n              B B B \n" ,c.scan());
    }

    @Test
    void createCubeTest() {
        char[][][] value = new char[][][] { { { 'W', 'W', 'W' }, { 'R', 'R', 'R' }, { 'W', 'W', 'W' } },
                                            { { 'Y', 'Y', 'Y' }, { 'O', 'O', 'O' }, { 'Y', 'Y', 'Y' } },
                                            { { 'R', 'R', 'R' }, { 'Y', 'Y', 'Y' }, { 'R', 'R', 'R' } },
                                            { { 'O', 'O', 'O' }, { 'W', 'W', 'W' }, { 'O', 'O', 'O' } },
                                            { { 'B', 'B', 'B' }, { 'B', 'B', 'B' }, { 'B', 'B', 'B' } },
                                            { { 'G', 'G', 'G' }, { 'G', 'G', 'G' }, { 'G', 'G', 'G' } } };
        Cube c1 = new Cube(3);
        c1.createCube(value);
        assertEquals("WWWRRRWWWYYYOOOYYYRRRYYYRRROOOWWWOOOBBBBBBBBBGGGGGGGGG", c1.toString());
    }

    @Test
    void rotateCubeAndFaceTest() {
        c.rotateFace("Front", 2, 2);
        assertEquals("WWBWWBWWBGYYGYYGYYRRRRRRRRROOOOOOOOOBBYBBYBBYGGWGGWGGW", c.toString());

        c.rotateFace("Left", 0, 0);
        assertEquals("RRRWWBWWBOOOGYYGYYGYYRRRRRRWWBOOOOOOBBYBBYBBYWWWGGGGGG", c.toString());

        c.rotateFace("Up", 1, 0);
        assertEquals("RRRWWBWWBOOOGYYGYYGBYRBRRYRWGBOGOOGOBBYOOWBBYWWWRRYGGG", c.toString());

        assertThrows(IllegalArgumentException.class, () -> c.rotateFace("Facade", 3, 2));
        assertThrows(IllegalArgumentException.class, () -> c.rotateFace("Left", c.size, 2));
        assertThrows(IllegalArgumentException.class, () -> c.rotateFace("Left", 3, 6));

        c.rotateCube(2);
        assertEquals("BBYOOWBBYGGGYRRWWWYRRBBYGRROOWGGGOOBYYGYYGOOORRRWWBWWB", c.toString());

        c.rotateCube(4);
        assertEquals("YWYBOBBOBWYGWRGWRGRBBRWWRWWGGOYYOYYORYRRBRYBGWGBOGOOGO", c.toString());

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
            c.rotateFace("Right", c.size-1, 2);
        }
        assertEquals("WWWWWWWWWYYYYYYYYYRRRRRRRRROOOOOOOOOBBBBBBBBBGGGGGGGGG", c.toString());
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