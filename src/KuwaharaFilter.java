import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class KuwaharaFilter {

    public static BufferedImage applyFilter(BufferedImage inputImage, int kernelSize) {

        PixelProcessor kuwaharaConventional = (image, X, Y) -> {

            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 0);

            List<Integer>[] quadrant = new ArrayList[4];
            for(int i = 0; i<4; i++) {
                quadrant[i] = new ArrayList<>();
            }

            for(int y = 0; y < kernelSize; y++) {
                for(int x = 0; x < kernelSize; x++) {
                    int quadrantIndex = (y < kernelSize / 2) ? ((x < kernelSize / 2) ? 0 : 1) : ((x < kernelSize / 2) ? 2 : 3);
                    quadrant[quadrantIndex].add(imageSegment[x][y]);
                }
            }

            double min = Double.MAX_VALUE;
            int index = 0;
            for(int i = 0; i < 4; i++) {
                double stdDev = localStdDev(quadrant[i]);
                if(stdDev < min) {
                    min = stdDev;
                    index = i;
                }
            }

            int gray = (int) Math.round(localMean(quadrant[index]));
            return ImageUtil.convertGrayToRGB(gray);

        };

        PixelProcessor kuwaharaConventionalColor = (image, X, Y) -> {

            int[][] redSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 1);
            int[][] greenSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 2);
            int[][] blueSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 3);

            // index 0 = red, 1 = green, 3 = blue
            List<Integer>[][] quadrant = new ArrayList[3][4];
            for(int c = 0; c < 3; c++) {
                for(int i = 0; i < 4; i++) {
                    quadrant[c][i] = new ArrayList<>();
                }
            }

            for(int y = 0; y < kernelSize; y++) {
                for(int x = 0; x < kernelSize; x++) {
                    int quadrantIndex = (y < kernelSize / 2) ? ((x < kernelSize / 2) ? 0 : 1) : ((x < kernelSize / 2) ? 2 : 3);
                    quadrant[0][quadrantIndex].add(redSegment[x][y]);
                    quadrant[1][quadrantIndex].add(greenSegment[x][y]);
                    quadrant[2][quadrantIndex].add(blueSegment[x][y]);
                }
            }

            int[] output = new int[3];

            for(int c = 0; c < 3; c++) {
                int index = 0;
                double min = Double.MAX_VALUE;
                for(int i = 0; i < 4; i++) {
                    double stdDev = localStdDev(quadrant[c][i]);
                    if(stdDev < min) {
                        min = stdDev;
                        index = i;
                    }
                }
                output[c] = (int) Math.round(localMean(quadrant[c][index]));
            }

            return ImageUtil.convertColorValuesToRGB(output[0], output[1], output[2]);
        };
        if(ImageUtil.isGreyscale(inputImage)) {
            return ImageUtil.convolution(inputImage, kernelSize, kuwaharaConventional);
        } else {
            return ImageUtil.convolution(inputImage, kernelSize, kuwaharaConventionalColor);
        }

    }

    private static double localMean(List<Integer> quadrant) {
        double sum = 0.0;
        for(int elem : quadrant) {
            sum += elem;
        }
        return sum/ quadrant.size();
    }

    private static double localStdDev(List<Integer> quadrant) {
        double mean = localMean(quadrant);
        double stdDev = 0.0;
        for(int elem : quadrant) {
            stdDev += Math.pow(elem - mean, 2);
        }
        return Math.sqrt(stdDev/ quadrant.size());
    }

    public static double[][] createGaussianKernel(double sigma, int sizeMultiplier) {
        int k = (int) (2 * Math.round(sizeMultiplier*sigma) + 1);
        double[][] kernel = new double[k][k];
        double sum = 0;

        int half = k/2;

        double constant = 1.0 / (2 * Math.PI * sigma * sigma);

        for(int x = -half; x <= half; x++) {
            for(int y = -half; y <= half; y++) {
                double exponent = -((Math.pow(x,2) + Math.pow(y,2)) / (2.0 * Math.pow(sigma,2)));
                kernel[x + half][y + half] = constant * Math.exp(exponent);
                sum += kernel[x + half][y + half];
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                kernel[i][j] /= sum;
            }
        }

        return kernel;
    }

    public static BufferedImage proposedKuwahara(BufferedImage inputImage, double sigma, int N, int q) {

        double[][] kernel = createGaussianKernel(sigma, 3);
        double[][] kernelQuarter = createGaussianKernel(sigma/4, 12);   // NOT USED

        int kernelSize = kernel.length;
        int half = kernelSize /2;

        List<AngularCoordinate>[] coordinate = new ArrayList[N];
        for(int i = 0; i<N; i++) {
            coordinate[i] = new ArrayList<>();
        }

        double[][][] wi = new double[N][kernelSize][kernelSize];

        for(int i = 1; i <= N; i++) {
            for(int y = 0; y < kernelSize; y++) {
                for(int x = 0; x < kernelSize; x++) {
                    int xCentered = x - half;
                    int yCentered = y - half;
                    double theta = Math.atan2(yCentered, xCentered);
                    double angle = Math.toDegrees(theta) + 180;
                    double uAngular = N*Math.toRadians(angle)/(2*Math.PI);

                    if((uAngular > (i - 0.5) && uAngular < (i + 0.5)) || (i == N && uAngular > 0 && uAngular < 0.5) || (xCentered == 0 && yCentered == 0)) {
                        wi[i-1][x][y] = N * kernel[x][y];

                        AngularCoordinate pos = new AngularCoordinate(x,y);
                        coordinate[i-1].add(pos);
                    }
                }
            }
        }


        PixelProcessor generalizedKuwahara = (image, X, Y) -> {

            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 0);

            double[] mean = new double[N];
            double[] variance = new double[N];
            double[] stdDev = new double[N];

            double[] sumWeight = new double[N];
            double m = 0.0;
            double n = 0.0;

            for(int i = 0; i < N; i++) {
                wi[i][half][half] /= N;
                for(AngularCoordinate elem : coordinate[i]) {
                    int x = elem.x;
                    int y = elem.y;
                    mean[i] += wi[i][x][y] * imageSegment[x][y];
                    variance[i] += Math.pow(imageSegment[x][y],2) * wi[i][x][y];
                    sumWeight[i] += wi[i][x][y];
                }
                mean[i] /= sumWeight[i];
                variance[i] /= sumWeight[i];
                stdDev[i] = variance[i] - Math.pow(mean[i],2);

                if(stdDev[i] == 0) {
                    stdDev[i] = Double.MIN_VALUE;
                }

                m += mean[i] * Math.pow(stdDev[i], -q);
                n += Math.pow(stdDev[i], -q);
            }

            double output = m/n;
            int gray = (int) Math.round(output);
            return ImageUtil.convertGrayToRGB(gray);
        };

        PixelProcessor generalizedKuwaharaColor = (image, X, Y) -> {
            int[][] redSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 1);
            int[][] greenSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 2);
            int[][] blueSegment = ImageUtil.extractNeighbors(image, X, Y, kernelSize, 3);

            // index 0 = red, 1 = green, 3 = blue
            double[][] mean = new double[3][N];
            double[][] variance = new double[3][N];
            double[][] stdDev = new double[3][N];

            double[] sumWeight = new double[N];
            double[] rgbStdDev = new double[N];

            double[] m = new double[3];
            double n = 0.0;
            int[] output = new int[3];

            for(int i = 0; i < N; i++) {
                wi[i][half][half] /= N;
                for(AngularCoordinate elem : coordinate[i]) {
                    int x = elem.x;
                    int y = elem.y;
                    mean[0][i] += wi[i][x][y] * redSegment[x][y];
                    mean[1][i] += wi[i][x][y] * greenSegment[x][y];
                    mean[2][i] += wi[i][x][y] * blueSegment[x][y];

                    variance[0][i] += Math.pow(redSegment[x][y],2) * wi[i][x][y];
                    variance[1][i] += Math.pow(greenSegment[x][y],2) * wi[i][x][y];
                    variance[2][i] += Math.pow(blueSegment[x][y],2) * wi[i][x][y];

                    sumWeight[i] += wi[i][x][y];
                }
                mean[0][i] /= sumWeight[i];
                mean[1][i] /= sumWeight[i];
                mean[2][i] /= sumWeight[i];

                variance[0][i] /= sumWeight[i];
                variance[1][i] /= sumWeight[i];
                variance[2][i] /= sumWeight[i];

                stdDev[0][i] = variance[0][i] - Math.pow(mean[0][i],2);
                stdDev[1][i] = variance[1][i] - Math.pow(mean[1][i],2);
                stdDev[2][i] = variance[2][i] - Math.pow(mean[2][i],2);

                for(int c = 0; c < 3; c++) {
                    if(stdDev[c][i] == 0) {
                        stdDev[c][i] = Double.MIN_VALUE;
                    }
                }

                rgbStdDev[i] = Math.sqrt(Math.pow(stdDev[0][i],2) + Math.pow(stdDev[1][i],2) + Math.pow(stdDev[2][i],2));

                m[0] += mean[0][i] * Math.pow(rgbStdDev[i], -q);
                m[1] += mean[1][i] * Math.pow(rgbStdDev[i], -q);
                m[2] += mean[2][i] * Math.pow(rgbStdDev[i], -q);
                n += Math.pow(rgbStdDev[i], -q);

            }
            output[0] = (int) Math.round(m[0]/n);
            output[1] = (int) Math.round(m[1]/n);
            output[2] = (int) Math.round(m[2]/n);

            int rgb = ImageUtil.convertColorValuesToRGB(output[0], output[1], output[2]);
            return rgb;
        };

        if(ImageUtil.isGreyscale(inputImage)) {;
            return ImageUtil.convolution(inputImage, kernelSize, generalizedKuwahara);
        } else {
            return ImageUtil.convolution(inputImage, kernelSize, generalizedKuwaharaColor);
        }

    }
}

class AngularCoordinate {
    public int x;
    public int y;
    public AngularCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
