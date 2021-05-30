import java.io.IOException;
import java.util.Arrays;

public class Main {
    final float WIDTH =100, HEIGHT = 50;

    final float R1 = 1, R2 = 2, K2 = 5;

    final float K1 = (float)WIDTH*K2*3/(8*(R1+R2));
    final float K = (float)HEIGHT*K2*3/(8*(R1+R2));


    final float THETA_SPACING = 0.07f, PHI_SPACING = 0.02f;

    public static void main(String[] args) throws IOException {
        try {
            float A = 1f, B = 1f;

            while(true) {
                A+=.05;
                B+=.05;
                new Main().donutify(A,B);
                try {
                    if (System.getProperty("os.name").contains("Windows"))
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    else
                        Runtime.getRuntime().exec("clear");
                } catch (IOException | InterruptedException ex) {}

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void donutify(float A, float B) throws IOException {

        char[][] output = new char[(int)HEIGHT][(int)WIDTH];
        float[][] zBuffer = new float[(int)HEIGHT][(int)WIDTH];

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                output[i][j] = ' ';
                zBuffer[i][j] = 0;
            }
        }

        //going around the donut
        for (float theta = 0; theta < 2 * Math.PI; theta+=THETA_SPACING) {

            float cosA = (float)Math.cos(A);
            float cosB = (float)Math.cos(B);
            float sinA = (float)Math.sin(A);
            float sinB = (float)Math.sin(B);
            float cosTheta = (float)Math.cos(theta);
            float sinTheta = (float)Math.sin(theta);

            //going around the cross section of the donut
            for (float phi = 0; phi < 2 * Math.PI; phi+=PHI_SPACING) {
                float cosPhi = (float)Math.cos(phi);
                float sinPhi = (float)Math.sin(phi);

                float x = (R2 + R1*cosTheta) * (cosB*cosPhi + sinA*sinB*sinPhi) - (R1*cosA*sinB*sinTheta);
                float y = (R2 + R1*cosTheta) * (cosPhi*sinB - cosB*sinA*sinPhi) + (R1*cosA*cosB*sinTheta);
                float z = K2 + (cosA * (R2+R1*cosTheta) * sinPhi) + (R1*sinA*sinTheta);

                float ooz = 1/z;

                int xp = (int)(WIDTH/2 + K1*ooz*x);
                int yp = (int)(HEIGHT/2 + K*ooz*y);

                float L = cosPhi*cosTheta*sinB - cosA*cosTheta*sinPhi - sinA*sinTheta + cosB * (cosA*sinTheta - cosTheta*sinA*sinPhi);

                if(L > 0) {
                    if(ooz > zBuffer[yp][xp]) {
                        zBuffer[yp][xp] = ooz;
                        int lum_index = (int)(L*8);
                        output[yp][xp] = ".,-~:;=!*#$@".charAt(lum_index);
                    }
                }

            }
        }

        //print out the board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(output[i][j]);
            }
            System.out.println();
        }
    }
}