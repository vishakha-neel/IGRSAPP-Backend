package com.example.igrsapp.Controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class CaptchaImage {
    String captchaString = "";

    // Function to generate random captcha image and returns the BufferedImage

	public BufferedImage getCaptchaImage() {
        try {
            Color backgroundColor = Color.white;
            Color borderColor = Color.black;
            Color textColor = Color.black;
            Color circleColor = new Color(190, 160, 150);
            Font textFont = new Font("Verdana", Font.BOLD, 20);
            int charsToPrint = 4;
            int width = 160;
            int height = 50;
            int circlesToDraw = 25;
            float horizMargin = 10.0f;
            double rotationRange = 0.7; 
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(backgroundColor);
            g.fillRect(0, 0, width, height);

            // lets make some noisey circles
            g.setColor(circleColor);
            for (int i = 0; i < circlesToDraw; i++) {
                int L = (int) (Math.random() * height / 2.0);
                int X = (int) (Math.random() * width - L);
                int Y = (int) (Math.random() * height - L);
                g.draw3DRect(X, Y, L * 2, L * 2, true);
            }
            g.setColor(textColor);
            g.setFont(textFont);
            FontMetrics fontMetrics = g.getFontMetrics();
            int maxAdvance = fontMetrics.getMaxAdvance();
            int fontHeight = fontMetrics.getHeight();
            int randomIndex;
            char characterToShow = 0;

            // i removed 1 and l and i because there are confusing to users...
            // Z, z, and N also get confusing when rotated
            // this should ideally be done for every language...
            // 0, O and o removed because there are confusing to users...
            // i like controlling the characters though because it helps prevent confusion
            
            String elegibleChars = "ABDEGHQRT";
            char[] chars = elegibleChars.toCharArray();
            
            String elegibleSChars = "abdefghqr";
            char[] schars = elegibleSChars.toCharArray();
            
            String elegibleNum = "2345789";
            char[] nums = elegibleNum.toCharArray();
            
            String elegibleSpChars = "#$&";
            char[] spChars = elegibleSpChars.toCharArray();
            
            float spaceForLetters = -horizMargin * 2 + width;
            float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);
            StringBuffer finalString = new StringBuffer();
            	int i;
               
            for (i = 0; i < charsToPrint; i++) {
                double randomValue = Math.random();
                int index 
                = (int)(8
                        * Math.random()); 
              
                switch(index) {
                
                case 1:
                	  randomIndex = (int) Math.round(randomValue * (chars.length - 1));
                      characterToShow = chars[randomIndex];
                      break;
                case 2:
              	  randomIndex = (int) Math.round(randomValue * (schars.length - 1));
                    characterToShow = schars[randomIndex];
                    break;
                case 3:
              	  randomIndex = (int) Math.round(randomValue * (nums.length - 1));
                    characterToShow = nums[randomIndex];
                    break;
                case 4:
              	  randomIndex = (int) Math.round(randomValue * (spChars.length - 1));
                    characterToShow = spChars[randomIndex];
                    break;
                case 5:
              	  randomIndex = (int) Math.round(randomValue * (schars.length - 1));
                    characterToShow = schars[randomIndex];
                    break;
              case 6:
            	  randomIndex = (int) Math.round(randomValue * (nums.length - 1));
                  characterToShow = nums[randomIndex];
                  break;
              case 7:
              	  randomIndex = (int) Math.round(randomValue * (chars.length - 1));
                    characterToShow = chars[randomIndex];
                    break;
              case 8:
            	  randomIndex = (int) Math.round(randomValue * (nums.length - 1));
                  characterToShow = nums[randomIndex];
                  break;
              default:
            	  randomIndex = (int) Math.round(randomValue * (spChars.length - 1));
                  characterToShow = spChars[randomIndex];
                  break;
                }
                
                	
               
              
                
                
                finalString.append(characterToShow);
                // this is a separate canvas used for the character so that
                // we can rotate it independently
                int charWidth = fontMetrics.charWidth(characterToShow);
                int charDim = Math.max(maxAdvance, fontHeight);
                int halfCharDim = (int) (charDim / 2);
                BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
                Graphics2D charGraphics = charImage.createGraphics();
                charGraphics.translate(halfCharDim, halfCharDim);
                double angle = (Math.random() - 0.5) * rotationRange;
                charGraphics.transform(AffineTransform.getRotateInstance(angle));
                charGraphics.translate(-halfCharDim, -halfCharDim);
                charGraphics.setColor(textColor);
                charGraphics.setFont(textFont);
                int charX = (int) (0.5 * charDim - 0.5 * charWidth);
                charGraphics.drawString("" + characterToShow, charX, (int) ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));
                float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
                int y = (int) ((height - charDim) / 2);
                g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
                charGraphics.dispose();
            }
            g.setColor(borderColor);
            g.drawRect(0, 0, width - 1, height - 1);
            g.dispose();
        //  HttpServletRequest request = null;
            captchaString = finalString.toString();
            System.out.println(captchaString);
     //  request.setAttribute("captcha", captchaString);
            return bufferedImage;
        } catch (Exception ioe) {
            throw new RuntimeException("Unable to build image", ioe);
        }
    }

    // Function to return the Captcha string
    public String getCaptchaString() {
        return captchaString;
    }
}
