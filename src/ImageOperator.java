import java.awt.image.BufferedImage;

public class ImageOperator {

    public void convertToPGM(BufferedImage inputImage) {
        PGMConverter pgmConverter = new PGMConverter();
        pgmConverter.convertToPGM(inputImage);
    }

    public BufferedImage nearestNeighbor(BufferedImage inputImage, int newWidth, int newHeight) {
        SpatialResolutionChanger spatialResolutionChanger = new SpatialResolutionChanger();
        BufferedImage outputImage = spatialResolutionChanger.nearestNeighbor(inputImage, newWidth, newHeight);
        return outputImage;
    }

    public BufferedImage linearInterpolation(BufferedImage inputImage, int newWidth, int newHeight) {
        SpatialResolutionChanger spatialResolutionChanger = new SpatialResolutionChanger();
        BufferedImage outputImage = spatialResolutionChanger.imageInterpolation(inputImage, newWidth, newHeight, 0);
        return outputImage;
    }

    public BufferedImage bilinearInterpolation(BufferedImage inputImage, int newWidth, int newHeight) {
        SpatialResolutionChanger spatialResolutionChanger = new SpatialResolutionChanger();
        BufferedImage outputImage = spatialResolutionChanger.imageInterpolation(inputImage, newWidth, newHeight, 1);
        return outputImage;
    }

    public BufferedImage changeGrayLevel(BufferedImage inputImage, int newDepth) {
        GrayLevelChanger grayLevelChanger = new GrayLevelChanger();
        BufferedImage outputImage = grayLevelChanger.changeGrayLevel(inputImage, newDepth);
        return outputImage;
    }
}
