import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CubeTest {
    private Cube c = new Cube(3);

    @Test
    void toStringTest() {
        assertEquals("WWWWWWWWWYYYYYYYYYRRRRRRRRROOOOOOOOOBBBBBBBBBGGGGGGGGG", c.toString());
    }

    @Test
    void rotateCubeAndFaceTest() {
        c.rotateFace(0, 2, 0);
        assertEquals("WWBWWBWWBGYYGYYGYYRRRRRRRRROOOOOOOOOBBYBBYBBYGGWGGWGGW", c.toString());

        c.rotateFace(1, 0, 0);
        assertEquals("RRRWWBWWBOOOGYYGYYGYYRRRRRRWWBOOOOOOBBYBBYBBYWWWGGGGGG", c.toString());

        c.rotateFace(2, 1, 1);
        assertEquals("RRRWWBWWBOOOGYYGYYGBYRBRRYRWGBOGOOGOBBYOOWBBYWWWRRYGGG", c.toString());

        assertThrows(IllegalArgumentException.class, () -> c.rotateFace(c.size, 3, 2));

        c.rotateCube(2);
        assertEquals("BBYOOWBBYGGGYRRWWWYRRBBYGRROOWGGGOOBYYGYYGOOORRRWWBWWB", c.toString());

        c.rotateCube(4);
        assertEquals("YWYBOBBOBWYGWRGWRGRBBRWWRWWGGOYYOYYORYRRBRYBGWGBOGOOGO", c.toString());

        assertThrows(IllegalArgumentException.class, () -> c.rotateCube(6));
    }

    @Test
    void randomTest() {
        c.rotateCube(4);
        c.rotateCube(4);
        c.rotateCube(4);
        c.rotateCube(4);
        c.rotateFace(0, 0, 0);
        c.rotateFace(0, 0, 0);
        c.rotateFace(0, 0, 0);
        c.rotateFace(0, 0, 0);
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
}