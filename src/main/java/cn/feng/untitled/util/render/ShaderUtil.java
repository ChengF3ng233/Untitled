package cn.feng.untitled.util.render;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.data.FileUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil extends MinecraftInstance {
    private final int programID;
    private final String kawaseUpGlow = """
            #version 120

            uniform sampler2D inTexture, textureToCheck;
            uniform vec2 halfpixel, offset, iResolution;
            uniform bool check;
            uniform float lastPass;
            uniform float exposure;

            void main() {
                if(check && texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                vec2 uv = vec2(gl_FragCoord.xy / iResolution);

                vec4 sum = texture2D(inTexture, uv + vec2(-halfpixel.x * 2.0, 0.0) * offset);
                sum.rgb *= sum.a;
                vec4 smpl1 =  texture2D(inTexture, uv + vec2(-halfpixel.x, halfpixel.y) * offset);
                smpl1.rgb *= smpl1.a;
                sum += smpl1 * 2.0;
                vec4 smp2 = texture2D(inTexture, uv + vec2(0.0, halfpixel.y * 2.0) * offset);
                smp2.rgb *= smp2.a;
                sum += smp2;
                vec4 smp3 = texture2D(inTexture, uv + vec2(halfpixel.x, halfpixel.y) * offset);
                smp3.rgb *= smp3.a;
                sum += smp3 * 2.0;
                vec4 smp4 = texture2D(inTexture, uv + vec2(halfpixel.x * 2.0, 0.0) * offset);
                smp4.rgb *= smp4.a;
                sum += smp4;
                vec4 smp5 = texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);
                smp5.rgb *= smp5.a;
                sum += smp5 * 2.0;
                vec4 smp6 = texture2D(inTexture, uv + vec2(0.0, -halfpixel.y * 2.0) * offset);
                smp6.rgb *= smp6.a;
                sum += smp6;
                vec4 smp7 = texture2D(inTexture, uv + vec2(-halfpixel.x, -halfpixel.y) * offset);
                smp7.rgb *= smp7.a;
                sum += smp7 * 2.0;
                vec4 result = sum / 12.0;
                gl_FragColor = vec4(result.rgb / result.a, mix(result.a, 1.0 - exp(-result.a * exposure), step(0.0, lastPass)));
            }""";
    private final String glowShader = """
            #version 120

            uniform sampler2D textureIn, textureToCheck;
            uniform vec2 texelSize, direction;
            uniform vec3 color;
            uniform bool avoidTexture;
            uniform float exposure, radius;
            uniform float weights[256];

            #define offset direction * texelSize

            void main() {
                if (direction.y == 1 && avoidTexture) {
                    if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                }
                vec4 innerColor = texture2D(textureIn, gl_TexCoord[0].st);
                innerColor.rgb *= innerColor.a;
                innerColor *= weights[0];
                for (float r = 1.0; r <= radius; r++) {
                    vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);
                    vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);

                    colorCurrent1.rgb *= colorCurrent1.a;
                    colorCurrent2.rgb *= colorCurrent2.a;

                    innerColor += (colorCurrent1 + colorCurrent2) * weights[int(r)];
                }

                gl_FragColor = vec4(innerColor.rgb / innerColor.a, mix(innerColor.a, 1.0 - exp(-innerColor.a * exposure), step(0.0, direction.y)));
            }
            """;
    private final String chams =
            """
                    #version 120

                    uniform sampler2D textureIn;
                    uniform vec4 color;
                    void main() {
                        float alpha = texture2D(textureIn, gl_TexCoord[0].st).a;
                        gl_FragColor = vec4(color.rgb, color.a * mix(0.0, alpha, step(0.0, alpha)));
                    }
                    """;
    private final String roundRectTexture = """
            #version 120

            uniform vec2 location, rectSize;
            uniform sampler2D textureIn;
            uniform float radius, alpha;

            float roundedBoxSDF(vec2 centerPos, vec2 size, float radius) {
                return length(max(abs(centerPos) -size, 0.)) - radius;
            }


            void main() {
                float distance = roundedBoxSDF((rectSize * .5) - (gl_TexCoord[0].st * rectSize), (rectSize * .5) - radius - 1., radius);
                float smoothedAlpha =  (1.0-smoothstep(0.0, 2.0, distance)) * alpha;
                gl_FragColor = vec4(texture2D(textureIn, gl_TexCoord[0].st).rgb, smoothedAlpha);
            }""";
    private final String roundRectOutline = """
            #version 120

            uniform vec2 location, rectSize;
            uniform vec4 color, outlineColor;
            uniform float radius, outlineThickness;

            float roundedSDF(vec2 centerPos, vec2 size, float radius) {
                return length(max(abs(centerPos) - size + radius, 0.0)) - radius;
            }

            void main() {
                float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness *.5) - 1.0, radius);

                float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * .5));

                vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);
                gl_FragColor = mix(outlineColor, insideColor, blendAmount);

            }""";
    private final String kawaseUpBloom = """
            #version 120

            uniform sampler2D inTexture, textureToCheck;
            uniform vec2 halfpixel, offset, iResolution;
            uniform int check;

            void main() {
                vec2 uv = gl_FragCoord.xy / iResolution;

                vec2 offset1 = vec2(-halfpixel.x, 0.0) * offset;
                vec2 offset2 = vec2(-halfpixel.x, halfpixel.y) * offset;
                vec2 offset3 = vec2(0.0, halfpixel.y * 2.0) * offset;
                vec2 offset4 = vec2(halfpixel.x, halfpixel.y) * offset;
                vec2 offset5 = vec2(halfpixel.x * 2.0, 0.0) * offset;
                vec2 offset6 = vec2(halfpixel.x, -halfpixel.y) * offset;
                vec2 offset7 = vec2(0.0, -halfpixel.y * 2.0) * offset;
                vec2 offset8 = vec2(-halfpixel.x, -halfpixel.y) * offset;

                vec4 sum = texture2D(inTexture, uv + offset1);
                sum.rgb *= sum.a;
                vec4 smpl1 = texture2D(inTexture, uv + offset2);
                smpl1.rgb *= smpl1.a;
                sum += smpl1 * 2.0;
                vec4 smp2 = texture2D(inTexture, uv + offset3);
                smp2.rgb *= smp2.a;
                sum += smp2;
                vec4 smp3 = texture2D(inTexture, uv + offset4);
                smp3.rgb *= smp3.a;
                sum += smp3 * 2.0;
                vec4 smp4 = texture2D(inTexture, uv + offset5);
                smp4.rgb *= smp4.a;
                sum += smp4;
                vec4 smp5 = texture2D(inTexture, uv + offset6);
                smp5.rgb *= smp5.a;
                sum += smp5 * 2.0;
                vec4 smp6 = texture2D(inTexture, uv + offset7);
                smp6.rgb *= smp6.a;
                sum += smp6;
                vec4 smp7 = texture2D(inTexture, uv + offset8);
                smp7.rgb *= smp7.a;
                sum += smp7 * 2.0;

                vec4 result = sum / 12.0;
                float checkAlpha = texture2D(textureToCheck, gl_TexCoord[0].st).a;
                gl_FragColor = vec4(result.rgb / result.a, mix(result.a, result.a * (1.0 - checkAlpha), float(check)));
            }
            """;
    private final String kawaseDownBloom = """
            #version 120

            uniform sampler2D inTexture;
            uniform vec2 offset, halfpixel, iResolution;

            void main() {
                vec2 uv = gl_FragCoord.xy / iResolution;

                vec4 sum = texture2D(inTexture, uv);
                sum.rgb *= sum.a;
                sum *= 4.0;
                vec4 smp1 = texture2D(inTexture, uv - halfpixel.xy * offset);
                smp1.rgb *= smp1.a;
                sum += smp1;
                vec4 smp2 = texture2D(inTexture, uv + halfpixel.xy * offset);
                smp2.rgb *= smp2.a;
                sum += smp2;
                vec4 smp3 = texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);
                smp3.rgb *= smp3.a;
                sum += smp3;
                vec4 smp4 = texture2D(inTexture, uv - vec2(halfpixel.x, -halfpixel.y) * offset);
                smp4.rgb *= smp4.a;
                sum += smp4;
                vec4 result = sum / 8.0;
                gl_FragColor = vec4(result.rgb / result.a, result.a);
            }
            """;
    private final String kawaseUp = """
            #version 120

            uniform sampler2D inTexture, textureToCheck;
            uniform vec2 halfpixel, offset, iResolution;
            uniform int check;

            void main() {
                vec2 uv = gl_FragCoord.xy / iResolution;

                vec4 sum = texture2D(inTexture, uv + vec2(-halfpixel.x * 2.0, 0.0) * offset);
                sum += texture2D(inTexture, uv + vec2(-halfpixel.x, halfpixel.y) * offset) * 2.0;
                sum += texture2D(inTexture, uv + vec2(0.0, halfpixel.y * 2.0) * offset);
                sum += texture2D(inTexture, uv + vec2(halfpixel.x, halfpixel.y) * offset) * 2.0;
                sum += texture2D(inTexture, uv + vec2(halfpixel.x * 2.0, 0.0) * offset);
                sum += texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset) * 2.0;
                sum += texture2D(inTexture, uv + vec2(0.0, -halfpixel.y * 2.0) * offset);
                sum += texture2D(inTexture, uv + vec2(-halfpixel.x, -halfpixel.y) * offset) * 2.0;

                vec4 average = sum / 12.0;
                gl_FragColor = vec4(average.rgb, mix(1.0, texture2D(textureToCheck, gl_TexCoord[0].st).a, check));
            }""";
    private final String kawaseDown = """
            #version 120

            uniform sampler2D inTexture;
            uniform vec2 offset, halfpixel, iResolution;

            void main() {
                vec2 uv = gl_FragCoord.xy / iResolution;

                vec4 sum = texture2D(inTexture, uv) * 4.0;
                sum += texture2D(inTexture, uv - halfpixel.xy * offset);
                sum += texture2D(inTexture, uv + halfpixel.xy * offset);
                sum += texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);
                sum += texture2D(inTexture, uv - vec2(halfpixel.x, -halfpixel.y) * offset);

                gl_FragColor = vec4(sum.rgb * 0.125, 1.0);
            }""";
    private final String gradientMask = """
            #version 120

            uniform vec2 location, rectSize;
            uniform sampler2D tex;
            uniform vec3 color1, color2, color3, color4;
            uniform float alpha;

            #define NOISE .5/255.0

            vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
                vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
                color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898,78.233))) * 43758.5453));
                return color;
            }

            void main() {
                vec2 coords = (gl_FragCoord.xy - location) / rectSize;
                float texColorAlpha = texture2D(tex, gl_TexCoord[0].st).a;
                gl_FragColor = vec4(createGradient(coords, color1, color2, color3, color4), texColorAlpha * alpha);
            }""";
    private final String mask = """
            #version 120

            uniform vec2 location, rectSize;
            uniform sampler2D u_texture, u_texture2;
            void main() {
                vec2 coords = (gl_FragCoord.xy - location) / rectSize;
                float texColorAlpha = texture2D(u_texture, gl_TexCoord[0].st).a;
                vec3 tex2Color = texture2D(u_texture2, gl_TexCoord[0].st).rgb;
                gl_FragColor = vec4(tex2Color, texColorAlpha);
            }""";
    private final String gradient = """
            #version 120

            uniform vec2 location, rectSize;
            uniform sampler2D tex;
            uniform vec4 color1, color2, color3, color4;
            #define NOISE .5/255.0

            vec4 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4){
                vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);
                //Dithering the color
                color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
                return color;
            }

            void main() {
                vec2 coords = (gl_FragCoord.xy - location) / rectSize;
                gl_FragColor = createGradient(coords, color1, color2, color3, color4);
            }""";
    private final String roundedRectGradient = """
            #version 120

            uniform vec2 location, rectSize;
            uniform vec4 color1, color2, color3, color4;
            uniform float radius;

            #define NOISE .5/255.0

            float roundSDF(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b , 0.0)) - r;
            }

            vec4 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4){
                vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);
                //Dithering the color
                color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
                return color;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                vec2 halfSize = rectSize * .5;
               \s
               // use the bottom leftColor as the alpha
                float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius)));
                vec4 gradient = createGradient(st, color1, color2, color3, color4);\
                gl_FragColor = vec4(gradient.rgb, gradient.a * smoothedAlpha);
            }""";
    private final String roundedRect = """
            #version 120

            uniform vec2 location, rectSize;
            uniform vec4 color;
            uniform float radius;
            uniform bool blur;

            float roundSDF(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b, 0.0)) - r;
            }


            void main() {
                vec2 rectHalf = rectSize * .5;
                // Smooth the result (free antialiasing).
                float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;
                gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);

            }""";

    public ShaderUtil(String fragmentShaderLoc, String vertexShaderLoc) {

        int program = glCreateProgram();
        try {
            int fragmentShaderID = switch (fragmentShaderLoc) {
                case "kawaseUpGlow" ->
                        createShader(new ByteArrayInputStream(kawaseUpGlow.getBytes()), GL_FRAGMENT_SHADER);
                case "glow" -> createShader(new ByteArrayInputStream(glowShader.getBytes()), GL_FRAGMENT_SHADER);
                case "chams" -> createShader(new ByteArrayInputStream(chams.getBytes()), GL_FRAGMENT_SHADER);
                case "roundRectTexture" ->
                        createShader(new ByteArrayInputStream(roundRectTexture.getBytes()), GL_FRAGMENT_SHADER);
                case "roundRectOutline" ->
                        createShader(new ByteArrayInputStream(roundRectOutline.getBytes()), GL_FRAGMENT_SHADER);
                case "kawaseUpBloom" ->
                        createShader(new ByteArrayInputStream(kawaseUpBloom.getBytes()), GL_FRAGMENT_SHADER);
                case "kawaseDownBloom" ->
                        createShader(new ByteArrayInputStream(kawaseDownBloom.getBytes()), GL_FRAGMENT_SHADER);
                case "kawaseUp" -> createShader(new ByteArrayInputStream(kawaseUp.getBytes()), GL_FRAGMENT_SHADER);
                case "kawaseDown" -> createShader(new ByteArrayInputStream(kawaseDown.getBytes()), GL_FRAGMENT_SHADER);
                case "gradientMask" ->
                        createShader(new ByteArrayInputStream(gradientMask.getBytes()), GL_FRAGMENT_SHADER);
                case "mask" -> createShader(new ByteArrayInputStream(mask.getBytes()), GL_FRAGMENT_SHADER);
                case "gradient" -> createShader(new ByteArrayInputStream(gradient.getBytes()), GL_FRAGMENT_SHADER);
                case "roundedRect" ->
                        createShader(new ByteArrayInputStream(roundedRect.getBytes()), GL_FRAGMENT_SHADER);
                case "roundedRectGradient" ->
                        createShader(new ByteArrayInputStream(roundedRectGradient.getBytes()), GL_FRAGMENT_SHADER);
                default ->
                        createShader(mc.getResourceManager().getResource(new ResourceLocation(fragmentShaderLoc)).getInputStream(), GL_FRAGMENT_SHADER);
            };
            glAttachShader(program, fragmentShaderID);

            int vertexShaderID = createShader(mc.getResourceManager().getResource(new ResourceLocation(vertexShaderLoc)).getInputStream(), GL_VERTEX_SHADER);
            glAttachShader(program, vertexShaderID);


        } catch (IOException e) {
            e.printStackTrace();
        }

        glLinkProgram(program);
        int status = glGetProgrami(program, GL_LINK_STATUS);

        if (status == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;
    }

    public ShaderUtil(String fragmentShadersrc, boolean notUsed) {
        int program = glCreateProgram();
        int fragmentShaderID = createShader(new ByteArrayInputStream(fragmentShadersrc.getBytes()), GL_FRAGMENT_SHADER);
        int vertexShaderID = 0;
        try {
            vertexShaderID = createShader(mc.getResourceManager().getResource(new ResourceLocation("untitled/shader/vertex.vsh")).getInputStream(), GL_VERTEX_SHADER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        glAttachShader(program, fragmentShaderID);
        glAttachShader(program, vertexShaderID);


        glLinkProgram(program);
        int status = glGetProgrami(program, GL_LINK_STATUS);
        if (status == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;

    }

    public ShaderUtil(String fragmentShaderLoc) {
        this(fragmentShaderLoc, "untitled/shader/vertex.vsh");
    }

    public static void drawQuads(float x, float y, float width, float height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x, y);
        glTexCoord2f(0, 1);
        glVertex2f(x, y + height);
        glTexCoord2f(1, 1);
        glVertex2f(x + width, y + height);
        glTexCoord2f(1, 0);
        glVertex2f(x + width, y);
        glEnd();
    }

    public static void drawQuads() {
        ScaledResolution sr = new ScaledResolution(mc);
        float width = (float) sr.getScaledWidth_double();
        float height = (float) sr.getScaledHeight_double();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2f(0, 0);
        glTexCoord2f(0, 0);
        glVertex2f(0, height);
        glTexCoord2f(1, 0);
        glVertex2f(width, height);
        glTexCoord2f(1, 1);
        glVertex2f(width, 0);
        glEnd();
    }

    public static void drawQuads(float width, float height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2f(0, 0);
        glTexCoord2f(0, 0);
        glVertex2f(0, height);
        glTexCoord2f(1, 0);
        glVertex2f(width, height);
        glTexCoord2f(1, 1);
        glVertex2f(width, 0);
        glEnd();
    }

    public void init() {
        glUseProgram(programID);
    }

    public void unload() {
        glUseProgram(0);
    }

    public int getUniform(String name) {
        return glGetUniformLocation(programID, name);
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(programID, name);
        switch (args.length) {
            case 1:
                glUniform1f(loc, args[0]);
                break;
            case 2:
                glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(programID, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, FileUtil.readInputStream(inputStream));
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            System.out.println(glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }

        return shader;
    }
}
