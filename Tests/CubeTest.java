import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CubeTest {
    private Cube c = new Cube(3);
    @Test
    void rotateFaceAndCube() {
        assertEquals(true,
        Arrays.deepEquals(new Character[][] {{ 'W', 'W', 'W' }, { 'W', 'W', 'W' }, { 'W', 'W', 'W' }}, c.getFace('F')) &&
        Arrays.deepEquals(new Character[][] {{ 'Y', 'Y', 'Y' }, { 'Y', 'Y', 'Y' }, { 'Y', 'Y', 'Y' }}, c.getFace('B')) &&
        Arrays.deepEquals(new Character[][] {{ 'R', 'R', 'R' }, { 'R', 'R', 'R' }, { 'R', 'R', 'R' }}, c.getFace('L')) &&
        Arrays.deepEquals(new Character[][] {{ 'O', 'O', 'O' }, { 'O', 'O', 'O' }, { 'O', 'O', 'O' }}, c.getFace('R')) &&
        Arrays.deepEquals(new Character[][] {{ 'B', 'B', 'B' }, { 'B', 'B', 'B' }, { 'B', 'B', 'B' }}, c.getFace('D')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'G', 'G' }, { 'G', 'G', 'G' }, { 'G', 'G', 'G' }}, c.getFace('U')));

        c.rotateFace(0, 2, 0);
        assertEquals(true,
        Arrays.deepEquals(new Character[][] {{ 'W', 'W', 'B' }, { 'W', 'W', 'B' }, { 'W', 'W', 'B' }}, c.getFace('F')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'Y', 'Y' }, { 'G', 'Y', 'Y' }, { 'G', 'Y', 'Y' }}, c.getFace('B')) &&
        Arrays.deepEquals(new Character[][] {{ 'R', 'R', 'R' }, { 'R', 'R', 'R' }, { 'R', 'R', 'R' }}, c.getFace('L')) &&
        Arrays.deepEquals(new Character[][] {{ 'O', 'O', 'O' }, { 'O', 'O', 'O' }, { 'O', 'O', 'O' }}, c.getFace('R')) &&
        Arrays.deepEquals(new Character[][] {{ 'B', 'B', 'Y' }, { 'B', 'B', 'Y' }, { 'B', 'B', 'Y' }}, c.getFace('D')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'G', 'W' }, { 'G', 'G', 'W' }, { 'G', 'G', 'W' }}, c.getFace('U')));

        c.rotateFace(1, 0, 0);
        assertEquals(true,
        Arrays.deepEquals(new Character[][] {{ 'R', 'R', 'R' }, { 'W', 'W', 'B' }, { 'W', 'W', 'B' }}, c.getFace('F')) &&
        Arrays.deepEquals(new Character[][] {{ 'O', 'O', 'O' }, { 'G', 'Y', 'Y' }, { 'G', 'Y', 'Y' }}, c.getFace('B')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'Y', 'Y' }, { 'R', 'R', 'R' }, { 'R', 'R', 'R' }}, c.getFace('L')) &&
        Arrays.deepEquals(new Character[][] {{ 'W', 'W', 'B' }, { 'O', 'O', 'O' }, { 'O', 'O', 'O' }}, c.getFace('R')) &&
        Arrays.deepEquals(new Character[][] {{ 'B', 'B', 'Y' }, { 'B', 'B', 'Y' }, { 'B', 'B', 'Y' }}, c.getFace('D')) &&
        Arrays.deepEquals(new Character[][] {{ 'W', 'W', 'W' }, { 'G', 'G', 'G' }, { 'G', 'G', 'G' }}, c.getFace('U')));

        c.rotateFace(2, 1, 1);
        assertEquals(true,
        Arrays.deepEquals(new Character[][] {{ 'R', 'R', 'R' }, { 'W', 'W', 'B' }, { 'W', 'W', 'B' }}, c.getFace('F')) &&
        Arrays.deepEquals(new Character[][] {{ 'O', 'O', 'O' }, { 'G', 'Y', 'Y' }, { 'G', 'Y', 'Y' }}, c.getFace('B')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'B', 'Y' }, { 'R', 'B', 'R' }, { 'R', 'Y', 'R' }}, c.getFace('L')) &&
        Arrays.deepEquals(new Character[][] {{ 'W', 'G', 'B' }, { 'O', 'G', 'O' }, { 'O', 'G', 'O' }}, c.getFace('R')) &&
        Arrays.deepEquals(new Character[][] {{ 'B', 'B', 'Y' }, { 'O', 'O', 'W' }, { 'B', 'B', 'Y' }}, c.getFace('D')) &&
        Arrays.deepEquals(new Character[][] {{ 'W', 'W', 'W' }, { 'R', 'R', 'Y' }, { 'G', 'G', 'G' }}, c.getFace('U')));

        assertThrows(IllegalArgumentException.class, () -> c.rotateFace(c.size, 3, 2));

        c.rotateCube(2);
        assertEquals(true,
        Arrays.deepEquals(new Character[][] {{ 'B', 'B', 'Y' }, { 'O', 'O', 'W' }, { 'B', 'B', 'Y' }}, c.getFace('F')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'G', 'G' }, { 'Y', 'R', 'R' }, { 'W', 'W', 'W' }}, c.getFace('B')) &&
        Arrays.deepEquals(new Character[][] {{ 'Y', 'R', 'R' }, { 'B', 'B', 'Y' }, { 'G', 'R', 'R' }}, c.getFace('L')) &&
        Arrays.deepEquals(new Character[][] {{ 'O', 'O', 'W' }, { 'G', 'G', 'G' }, { 'O', 'O', 'B' }}, c.getFace('R')) &&
        Arrays.deepEquals(new Character[][] {{ 'Y', 'Y', 'G' }, { 'Y', 'Y', 'G' }, { 'O', 'O', 'O' }}, c.getFace('D')) &&
        Arrays.deepEquals(new Character[][] {{ 'R', 'R', 'R' }, { 'W', 'W', 'B' }, { 'W', 'W', 'B' }}, c.getFace('U')));

        c.rotateCube(4);
        assertEquals(true,
        Arrays.deepEquals(new Character[][] {{ 'Y', 'W', 'Y' }, { 'B', 'O', 'B' }, { 'B', 'O', 'B' }}, c.getFace('F')) &&
        Arrays.deepEquals(new Character[][] {{ 'W', 'Y', 'G' }, { 'W', 'R', 'G' }, { 'W', 'R', 'G' }}, c.getFace('B')) &&
        Arrays.deepEquals(new Character[][] {{ 'R', 'B', 'B' }, { 'R', 'W', 'W' }, { 'R', 'W', 'W' }}, c.getFace('L')) &&
        Arrays.deepEquals(new Character[][] {{ 'G', 'G', 'O' }, { 'Y', 'Y', 'O' }, { 'Y', 'Y', 'O' }}, c.getFace('R')) &&
        Arrays.deepEquals(new Character[][] {{ 'R', 'Y', 'R' }, { 'R', 'B', 'R' }, { 'Y', 'B', 'G' }}, c.getFace('D')) &&
        Arrays.deepEquals(new Character[][] {{ 'W', 'G', 'B' }, { 'O', 'G', 'O' }, { 'O', 'G', 'O' }}, c.getFace('U')));

        assertThrows(IllegalArgumentException.class, () -> c.rotateCube(6));
    }
}