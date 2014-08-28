/*
Better Color - A simple library for generating pleasing pastel colors.

This is a Java port of the wonderful javascript color library - randomColor by David Merfield.
BetterColor at the moment, just supports generating colors without any other options that are
provided in the original library. I cherry-picked the API which was useful to me. In future, I might
get around to making a complete port :)

RandomColor: https://github.com/davidmerfield/randomColor
Author: Prakhar Srivastav
Usage: BetterColor.getColor() -> Returns float[] of H, S, V
 */
package com.prakharme.prakharsriv.colorphun.util;

import java.util.HashMap;
import java.util.Random;

public class BetterColor {

    // NESTED CLASS
    public static class ColorInfo {
        private int[] hueRange;
        private String name;
        private int[][] lowerBounds;
        private int[] saturationRange;
        private int[] brightnessRange;

        public ColorInfo(String name, int[] hueRange, int[][] lowerBounds, int[] saturationRange, int[] brightnessRange) {
            this.name = name;
            this.brightnessRange = brightnessRange;
            this.hueRange = hueRange;
            this.lowerBounds = lowerBounds;
            this.saturationRange = saturationRange;
        }

        public int[] getHueRange(){
            return this.hueRange;
        }

        public int[] getSaturationRange() {
            return this.saturationRange;
        }

        public int[][] getLowerBounds() {
            return this.lowerBounds;
        }
    }

    private static HashMap<String, ColorInfo> colorDictionary;

    private static boolean removeHue(int hue) {
        return (hue >= 150 && hue <= 180) || (hue >= 55 && hue <= 70);
    }

    public static float[] getColor() {

        int H, S, V;
        loadColorDictionary();

        do  {
            H = pickHue();
            S = pickSaturation(H);
            V = pickBrightness(H, S);
        } while (S == -1 || V == 0 || removeHue(H));

        return new float[] {(float) H, (float) S, (float) V};
    }

    private static void loadColorDictionary() {
        colorDictionary = new HashMap<String, ColorInfo>();
        defineColor("RED", new int[] {-26,18},
                new int[][] { new int[] {20,100}, new int[] {30,92}, new int[] {40,89}, new int[] {50,85},
                        new int[] {60,78}, new int[] {70,70}, new int[] {80,60}, new int[] {90,55}, new int[] {100,50} }
        );
        defineColor("ORANGE", new int[] {19,46},
                new int[][] { new int[] {20,100}, new int[] {30,93}, new int[] {40,88},
                        new int[] {50,86}, new int[] {60,85}, new int[] {70,70}, new int[] {100,70} }
        );
        defineColor("YELLOW", new int[] {47,62},
                new int[][] { new int[] {25,100}, new int[] {40,94}, new int[] {50,89}, new int[] {60,86},
                        new int[] {70,84}, new int[] {80,82}, new int[] {90,80}, new int[] {100,75} }
        );
        defineColor("GREEN", new int[] {63,178},
                new int[][] { new int[] {30,100}, new int[] {40,90}, new int[] {50,85}, new int[] {60,81},
                        new int[] {70,74}, new int[] {80,64}, new int[] {90,50}, new int[] {100,40} }
        );
        defineColor("BLUE", new int[] {179,257},
                new int[][] { new int[] {20,100}, new int[] {30,86}, new int[] {40,80}, new int[] {50,74},
                        new int[] {60,60}, new int[] {70,52}, new int[] {80,44}, new int[] {90,39}, new int[] {100,35} }
        );
        defineColor("PURPLE", new int[] {258,282},
                new int[][] { new int[] {20,100}, new int[] {30,87}, new int[] {40,79}, new int[] {50,70},
                        new int[] {60,65}, new int[] {70,59}, new int[] {80,52}, new int[] {90,45}, new int[] {100,42} }
        );
        defineColor("PINK", new int[] {283,334},
                new int[][] { new int[] {20,100}, new int[] {30,90}, new int[] {40,86}, new int[] {60,84},
                        new int[] {80,80}, new int[] {90,75}, new int[] {100,73} }
        );
    }

    private static void defineColor(String name, int[] hueRange, int[][] lowerBounds) {
        int sMin = lowerBounds[0][0];
        int sMax = lowerBounds[lowerBounds.length - 1][0];
        int bMin = lowerBounds[lowerBounds.length - 1][1];
        int bMax = lowerBounds[0][1];

        int[] saturationRange = {sMin, sMax};
        int[] brightnessRange = {bMin, bMax};

        ColorInfo colorInfo = new ColorInfo(name, hueRange, lowerBounds, saturationRange, brightnessRange);
        colorDictionary.put(name, colorInfo);
    }

    private static int randomWithin(int start, int end) {
        Random randomGenerator = new Random();
        return start + randomGenerator.nextInt(end + 1 - start);
    }

    private static int pickHue() {
        return randomWithin(0, 360);
    }

    private static boolean isLightRange(int hue) {
        return (hue >= 55 && hue <= 110) || (hue >= 145 && hue <= 180);
    }

    private static int pickSaturation(int hue) {
        if (isLightRange(hue)) {
            return randomWithin(60, 80);
        }
        int saturationRange[] = getSaturationRange(hue);
        if (saturationRange != null) {
            return randomWithin(saturationRange[1] - 10, saturationRange[1]);
        }
        return -1;
    }

    private static int pickBrightness(int hue, int saturation) {
        if (isLightRange(hue)) {
            return randomWithin(10, 20);
        }
        int bMin = getMinimumBrightness(hue, saturation);
        int bMax = bMin + 20;
        return randomWithin(bMin, bMax);
    }

    private static int getMinimumBrightness(int hue, int saturation) {
        ColorInfo colorInfo = getColorInfo(hue);
        if (colorInfo != null) {
            int[][] lowerBounds = colorInfo.getLowerBounds();

            for (int i = 0; i < lowerBounds.length; i++) {
                int s1 = lowerBounds[i][0];
                int v1 = lowerBounds[i][1];
                int s2 = lowerBounds[i+1][0];
                int v2 = lowerBounds[i+1][1];

                if (saturation >= s1 && saturation <= s2) {
                    int m = (v2 - v1) / (s2 - s1);
                    int b = v1 - m * s1;

                    return m * saturation + b;
                }
            }
        }
        return 0;
    }

    private static int[] getSaturationRange(int hue) {
        ColorInfo colorInfo = getColorInfo(hue);
        if (colorInfo != null) {
            return colorInfo.getSaturationRange();
        }
        return null;
    }

    private static ColorInfo getColorInfo(int hue) {
        if (hue >= 334 && hue <= 360) {
            hue -= 360;
        }

        for (String colorName : colorDictionary.keySet()) {
            ColorInfo colorInfo = colorDictionary.get(colorName);
            int[] hueRange = colorInfo.getHueRange();
            if (hue >= hueRange[0] && hue <= hueRange[1]) {
                return colorInfo;
            }
        }
        return null; // color not found
    }

}
