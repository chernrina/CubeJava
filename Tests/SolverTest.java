import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolverTest {

    @Test
    void decision() {
        Cube cube = new Cube(3);
        cube.confuse();
        cube.confuse();
        cube.rotateFace("Left",2,2);
        cube.rotateFace("Front",0,2);
        cube.confuse();
        Solver solve = new Solver(cube);
        assertEquals("WWWWWWWWW YYYYYYYYY RRRRRRRRR OOOOOOOOO BBBBBBBBB GGGGGGGGG",
                solve.getCube().toString());
    }
}
