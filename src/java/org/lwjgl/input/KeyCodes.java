package org.lwjgl.input;

import java.awt.event.KeyEvent;

import org.lwjgl.glfw.GLFW;

public class KeyCodes {

    public static int glfwToLwjgl(int glfwKeyCode) {
        if (glfwKeyCode > GLFW.GLFW_KEY_LAST) {
            return glfwKeyCode;
        }
        return glfwKeyCode == GLFW.GLFW_KEY_UNKNOWN ? Keyboard.KEY_UNLABELED:
            glfwKeyCode == GLFW.GLFW_KEY_ESCAPE ? Keyboard.KEY_ESCAPE:
            glfwKeyCode == GLFW.GLFW_KEY_BACKSPACE ? Keyboard.KEY_BACK:
            glfwKeyCode == GLFW.GLFW_KEY_TAB ? Keyboard.KEY_TAB:
            glfwKeyCode == GLFW.GLFW_KEY_ENTER ? Keyboard.KEY_RETURN:
            glfwKeyCode == GLFW.GLFW_KEY_SPACE ? Keyboard.KEY_SPACE:
            glfwKeyCode == GLFW.GLFW_KEY_LEFT_CONTROL ? Keyboard.KEY_LCONTROL:
            glfwKeyCode == GLFW.GLFW_KEY_LEFT_SHIFT ? Keyboard.KEY_LSHIFT:
            glfwKeyCode == GLFW.GLFW_KEY_LEFT_ALT ? Keyboard.KEY_LMENU:
            glfwKeyCode == GLFW.GLFW_KEY_LEFT_SUPER ? Keyboard.KEY_LMETA:
            glfwKeyCode == GLFW.GLFW_KEY_RIGHT_CONTROL ? Keyboard.KEY_RCONTROL:
            glfwKeyCode == GLFW.GLFW_KEY_RIGHT_SHIFT ? Keyboard.KEY_RSHIFT:
            glfwKeyCode == GLFW.GLFW_KEY_RIGHT_ALT ? Keyboard.KEY_RMENU:
            glfwKeyCode == GLFW.GLFW_KEY_RIGHT_SUPER ? Keyboard.KEY_RMETA:
            glfwKeyCode == GLFW.GLFW_KEY_1 ? Keyboard.KEY_1:
            glfwKeyCode == GLFW.GLFW_KEY_2 ? Keyboard.KEY_2:
            glfwKeyCode == GLFW.GLFW_KEY_3 ? Keyboard.KEY_3:
            glfwKeyCode == GLFW.GLFW_KEY_4 ? Keyboard.KEY_4:
            glfwKeyCode == GLFW.GLFW_KEY_5 ? Keyboard.KEY_5:
            glfwKeyCode == GLFW.GLFW_KEY_6 ? Keyboard.KEY_6:
            glfwKeyCode == GLFW.GLFW_KEY_7 ? Keyboard.KEY_7:
            glfwKeyCode == GLFW.GLFW_KEY_8 ? Keyboard.KEY_8:
            glfwKeyCode == GLFW.GLFW_KEY_9 ? Keyboard.KEY_9:
            glfwKeyCode == GLFW.GLFW_KEY_0 ? Keyboard.KEY_0:
            glfwKeyCode == GLFW.GLFW_KEY_A ? Keyboard.KEY_A:
            glfwKeyCode == GLFW.GLFW_KEY_B ? Keyboard.KEY_B:
            glfwKeyCode == GLFW.GLFW_KEY_C ? Keyboard.KEY_C:
            glfwKeyCode == GLFW.GLFW_KEY_D ? Keyboard.KEY_D:
            glfwKeyCode == GLFW.GLFW_KEY_E ? Keyboard.KEY_E:
            glfwKeyCode == GLFW.GLFW_KEY_F ? Keyboard.KEY_F:
            glfwKeyCode == GLFW.GLFW_KEY_G ? Keyboard.KEY_G:
            glfwKeyCode == GLFW.GLFW_KEY_H ? Keyboard.KEY_H:
            glfwKeyCode == GLFW.GLFW_KEY_I ? Keyboard.KEY_I:
            glfwKeyCode == GLFW.GLFW_KEY_J ? Keyboard.KEY_J:
            glfwKeyCode == GLFW.GLFW_KEY_K ? Keyboard.KEY_K:
            glfwKeyCode == GLFW.GLFW_KEY_L ? Keyboard.KEY_L:
            glfwKeyCode == GLFW.GLFW_KEY_M ? Keyboard.KEY_M:
            glfwKeyCode == GLFW.GLFW_KEY_N ? Keyboard.KEY_N:
            glfwKeyCode == GLFW.GLFW_KEY_O ? Keyboard.KEY_O:
            glfwKeyCode == GLFW.GLFW_KEY_P ? Keyboard.KEY_P:
            glfwKeyCode == GLFW.GLFW_KEY_Q ? Keyboard.KEY_Q:
            glfwKeyCode == GLFW.GLFW_KEY_R ? Keyboard.KEY_R:
            glfwKeyCode == GLFW.GLFW_KEY_S ? Keyboard.KEY_S:
            glfwKeyCode == GLFW.GLFW_KEY_T ? Keyboard.KEY_T:
            glfwKeyCode == GLFW.GLFW_KEY_U ? Keyboard.KEY_U:
            glfwKeyCode == GLFW.GLFW_KEY_V ? Keyboard.KEY_V:
            glfwKeyCode == GLFW.GLFW_KEY_W ? Keyboard.KEY_W:
            glfwKeyCode == GLFW.GLFW_KEY_X ? Keyboard.KEY_X:
            glfwKeyCode == GLFW.GLFW_KEY_Y ? Keyboard.KEY_Y:
            glfwKeyCode == GLFW.GLFW_KEY_Z ? Keyboard.KEY_Z:
            glfwKeyCode == GLFW.GLFW_KEY_UP ? Keyboard.KEY_UP:
            glfwKeyCode == GLFW.GLFW_KEY_DOWN ? Keyboard.KEY_DOWN:
            glfwKeyCode == GLFW.GLFW_KEY_LEFT ? Keyboard.KEY_LEFT:
            glfwKeyCode == GLFW.GLFW_KEY_RIGHT ? Keyboard.KEY_RIGHT:
            glfwKeyCode == GLFW.GLFW_KEY_INSERT ? Keyboard.KEY_INSERT:
            glfwKeyCode == GLFW.GLFW_KEY_DELETE ? Keyboard.KEY_DELETE:
            glfwKeyCode == GLFW.GLFW_KEY_HOME ? Keyboard.KEY_HOME:
            glfwKeyCode == GLFW.GLFW_KEY_END ? Keyboard.KEY_END:
            glfwKeyCode == GLFW.GLFW_KEY_PAGE_UP ? Keyboard.KEY_PRIOR:
            glfwKeyCode == GLFW.GLFW_KEY_PAGE_DOWN ? Keyboard.KEY_NEXT:
            glfwKeyCode == GLFW.GLFW_KEY_F1 ? Keyboard.KEY_F1:
            glfwKeyCode == GLFW.GLFW_KEY_F2 ? Keyboard.KEY_F2:
            glfwKeyCode == GLFW.GLFW_KEY_F3 ? Keyboard.KEY_F3:
            glfwKeyCode == GLFW.GLFW_KEY_F4 ? Keyboard.KEY_F4:
            glfwKeyCode == GLFW.GLFW_KEY_F5 ? Keyboard.KEY_F5:
            glfwKeyCode == GLFW.GLFW_KEY_F6 ? Keyboard.KEY_F6:
            glfwKeyCode == GLFW.GLFW_KEY_F7 ? Keyboard.KEY_F7:
            glfwKeyCode == GLFW.GLFW_KEY_F8 ? Keyboard.KEY_F8:
            glfwKeyCode == GLFW.GLFW_KEY_F9 ? Keyboard.KEY_F9:
            glfwKeyCode == GLFW.GLFW_KEY_F10 ? Keyboard.KEY_F10:
            glfwKeyCode == GLFW.GLFW_KEY_F11 ? Keyboard.KEY_F11:
            glfwKeyCode == GLFW.GLFW_KEY_F12 ? Keyboard.KEY_F12:
            glfwKeyCode == GLFW.GLFW_KEY_F13 ? Keyboard.KEY_F13:
            glfwKeyCode == GLFW.GLFW_KEY_F14 ? Keyboard.KEY_F14:
            glfwKeyCode == GLFW.GLFW_KEY_F15 ? Keyboard.KEY_F15:
            glfwKeyCode == GLFW.GLFW_KEY_F16 ? Keyboard.KEY_F16:
            glfwKeyCode == GLFW.GLFW_KEY_F17 ? Keyboard.KEY_F17:
            glfwKeyCode == GLFW.GLFW_KEY_F18 ? Keyboard.KEY_F18:
            glfwKeyCode == GLFW.GLFW_KEY_F19 ? Keyboard.KEY_F19:
            glfwKeyCode == GLFW.GLFW_KEY_KP_1 ? Keyboard.KEY_NUMPAD1:
            glfwKeyCode == GLFW.GLFW_KEY_KP_2 ? Keyboard.KEY_NUMPAD2:
            glfwKeyCode == GLFW.GLFW_KEY_KP_3 ? Keyboard.KEY_NUMPAD3:
            glfwKeyCode == GLFW.GLFW_KEY_KP_4 ? Keyboard.KEY_NUMPAD4:
            glfwKeyCode == GLFW.GLFW_KEY_KP_5 ? Keyboard.KEY_NUMPAD5:
            glfwKeyCode == GLFW.GLFW_KEY_KP_6 ? Keyboard.KEY_NUMPAD6:
            glfwKeyCode == GLFW.GLFW_KEY_KP_7 ? Keyboard.KEY_NUMPAD7:
            glfwKeyCode == GLFW.GLFW_KEY_KP_8 ? Keyboard.KEY_NUMPAD8:
            glfwKeyCode == GLFW.GLFW_KEY_KP_9 ? Keyboard.KEY_NUMPAD9:
            glfwKeyCode == GLFW.GLFW_KEY_KP_0 ? Keyboard.KEY_NUMPAD0:
            glfwKeyCode == GLFW.GLFW_KEY_KP_ADD ? Keyboard.KEY_ADD:
            glfwKeyCode == GLFW.GLFW_KEY_KP_SUBTRACT ? Keyboard.KEY_SUBTRACT:
            glfwKeyCode == GLFW.GLFW_KEY_KP_MULTIPLY ? Keyboard.KEY_MULTIPLY:
            glfwKeyCode == GLFW.GLFW_KEY_KP_DIVIDE ? Keyboard.KEY_DIVIDE:
            glfwKeyCode == GLFW.GLFW_KEY_KP_DECIMAL ? Keyboard.KEY_DECIMAL:
            glfwKeyCode == GLFW.GLFW_KEY_KP_EQUAL ? Keyboard.KEY_NUMPADEQUALS:
            glfwKeyCode == GLFW.GLFW_KEY_KP_ENTER ? Keyboard.KEY_NUMPADENTER:
            glfwKeyCode == GLFW.GLFW_KEY_NUM_LOCK ? Keyboard.KEY_NUMLOCK:
            glfwKeyCode == GLFW.GLFW_KEY_SEMICOLON ? Keyboard.KEY_SEMICOLON:
            glfwKeyCode == GLFW.GLFW_KEY_BACKSLASH ? Keyboard.KEY_BACKSLASH:
            glfwKeyCode == GLFW.GLFW_KEY_COMMA ? Keyboard.KEY_COMMA:
            glfwKeyCode == GLFW.GLFW_KEY_PERIOD ? Keyboard.KEY_PERIOD:
            glfwKeyCode == GLFW.GLFW_KEY_SLASH ? Keyboard.KEY_SLASH:
            glfwKeyCode == GLFW.GLFW_KEY_GRAVE_ACCENT ? Keyboard.KEY_GRAVE:
            glfwKeyCode == GLFW.GLFW_KEY_CAPS_LOCK ? Keyboard.KEY_CAPITAL:
            glfwKeyCode == GLFW.GLFW_KEY_SCROLL_LOCK ? Keyboard.KEY_SCROLL:
            glfwKeyCode == GLFW.GLFW_KEY_WORLD_1 ? Keyboard.KEY_CIRCUMFLEX: // "World" keys could be anything depending on
                                                                   // keyboard layout, pick something arbitrary
            glfwKeyCode == GLFW.GLFW_KEY_WORLD_2 ? Keyboard.KEY_YEN:
            glfwKeyCode == GLFW.GLFW_KEY_PAUSE ? Keyboard.KEY_PAUSE:
            glfwKeyCode == GLFW.GLFW_KEY_MINUS ? Keyboard.KEY_MINUS:
            glfwKeyCode == GLFW.GLFW_KEY_EQUAL ? Keyboard.KEY_EQUALS:
            glfwKeyCode == GLFW.GLFW_KEY_LEFT_BRACKET ? Keyboard.KEY_LBRACKET:
            glfwKeyCode == GLFW.GLFW_KEY_RIGHT_BRACKET ? Keyboard.KEY_RBRACKET:
            glfwKeyCode == GLFW.GLFW_KEY_APOSTROPHE ? Keyboard.KEY_APOSTROPHE:
            // public static final int KEY_AT = 0x91: /* (NEC PC98) */
            // public static final int KEY_COLON = 0x92: /* (NEC PC98) */
            // public static final int KEY_UNDERLINE = 0x93: /* (NEC PC98) */

            // public static final int KEY_KANA = 0x70: /* (Japanese keyboard) */
            // public static final int KEY_CONVERT = 0x79: /* (Japanese keyboard) */
            // public static final int KEY_NOCONVERT = 0x7B: /* (Japanese keyboard) */
            // public static final int KEY_YEN = 0x7D: /* (Japanese keyboard) */
            // public static final int KEY_CIRCUMFLEX = 0x90: /* (Japanese keyboard) */
            // public static final int KEY_KANJI = 0x94: /* (Japanese keyboard) */
            // public static final int KEY_STOP = 0x95: /* (NEC PC98) */
            // public static final int KEY_AX = 0x96: /* (Japan AX) */
            // public static final int KEY_UNLABELED = 0x97: /* (J3100) */
            // public static final int KEY_SECTION = 0xA7: /* Section symbol (Mac) */
            // public static final int KEY_NUMPADCOMMA = 0xB3: /* , on numeric keypad (NEC PC98) */
            // public static final int KEY_SYSRQ = 0xB7:
            // public static final int KEY_FUNCTION = 0xC4: /* Function (Mac) */
            // public static final int KEY_CLEAR = 0xDA: /* Clear key (Mac) */

            // public static final int KEY_APPS = 0xDD: /* AppMenu key */
            // public static final int KEY_POWER = 0xDE:
            // public static final int KEY_SLEEP = 0xDF:

            Keyboard.KEY_NONE;
    }

    public static int lwjglToGlfw(int lwjglKeyCode) {
        if (lwjglKeyCode > GLFW.GLFW_KEY_LAST) {
            return lwjglKeyCode;
        }
        return lwjglKeyCode == Keyboard.KEY_NONE ? 0:
            lwjglKeyCode == Keyboard.KEY_UNLABELED ? GLFW.GLFW_KEY_UNKNOWN: // arbitrary mapping to fix text input here
            lwjglKeyCode == Keyboard.KEY_ESCAPE ? GLFW.GLFW_KEY_ESCAPE:
            lwjglKeyCode == Keyboard.KEY_BACK ? GLFW.GLFW_KEY_BACKSPACE:
            lwjglKeyCode == Keyboard.KEY_TAB ? GLFW.GLFW_KEY_TAB:
            lwjglKeyCode == Keyboard.KEY_RETURN ? GLFW.GLFW_KEY_ENTER:
            lwjglKeyCode == Keyboard.KEY_SPACE ? GLFW.GLFW_KEY_SPACE:
            lwjglKeyCode == Keyboard.KEY_LCONTROL ? GLFW.GLFW_KEY_LEFT_CONTROL:
            lwjglKeyCode == Keyboard.KEY_LSHIFT ? GLFW.GLFW_KEY_LEFT_SHIFT:
            lwjglKeyCode == Keyboard.KEY_LMENU ? GLFW.GLFW_KEY_LEFT_ALT:
            lwjglKeyCode == Keyboard.KEY_LMETA ? GLFW.GLFW_KEY_LEFT_SUPER:
            lwjglKeyCode == Keyboard.KEY_RCONTROL ? GLFW.GLFW_KEY_RIGHT_CONTROL:
            lwjglKeyCode == Keyboard.KEY_RSHIFT ? GLFW.GLFW_KEY_RIGHT_SHIFT:
            lwjglKeyCode == Keyboard.KEY_RMENU ? GLFW.GLFW_KEY_RIGHT_ALT:
            lwjglKeyCode == Keyboard.KEY_RMETA ? GLFW.GLFW_KEY_RIGHT_SUPER:
            lwjglKeyCode == Keyboard.KEY_1 ? GLFW.GLFW_KEY_1:
            lwjglKeyCode == Keyboard.KEY_2 ? GLFW.GLFW_KEY_2:
            lwjglKeyCode == Keyboard.KEY_3 ? GLFW.GLFW_KEY_3:
            lwjglKeyCode == Keyboard.KEY_4 ? GLFW.GLFW_KEY_4:
            lwjglKeyCode == Keyboard.KEY_5 ? GLFW.GLFW_KEY_5:
            lwjglKeyCode == Keyboard.KEY_6 ? GLFW.GLFW_KEY_6:
            lwjglKeyCode == Keyboard.KEY_7 ? GLFW.GLFW_KEY_7:
            lwjglKeyCode == Keyboard.KEY_8 ? GLFW.GLFW_KEY_8:
            lwjglKeyCode == Keyboard.KEY_9 ? GLFW.GLFW_KEY_9:
            lwjglKeyCode == Keyboard.KEY_0 ? GLFW.GLFW_KEY_0:
            lwjglKeyCode == Keyboard.KEY_A ? GLFW.GLFW_KEY_A:
            lwjglKeyCode == Keyboard.KEY_B ? GLFW.GLFW_KEY_B:
            lwjglKeyCode == Keyboard.KEY_C ? GLFW.GLFW_KEY_C:
            lwjglKeyCode == Keyboard.KEY_D ? GLFW.GLFW_KEY_D:
            lwjglKeyCode == Keyboard.KEY_E ? GLFW.GLFW_KEY_E:
            lwjglKeyCode == Keyboard.KEY_F ? GLFW.GLFW_KEY_F:
            lwjglKeyCode == Keyboard.KEY_G ? GLFW.GLFW_KEY_G:
            lwjglKeyCode == Keyboard.KEY_H ? GLFW.GLFW_KEY_H:
            lwjglKeyCode == Keyboard.KEY_I ? GLFW.GLFW_KEY_I:
            lwjglKeyCode == Keyboard.KEY_J ? GLFW.GLFW_KEY_J:
            lwjglKeyCode == Keyboard.KEY_K ? GLFW.GLFW_KEY_K:
            lwjglKeyCode == Keyboard.KEY_L ? GLFW.GLFW_KEY_L:
            lwjglKeyCode == Keyboard.KEY_M ? GLFW.GLFW_KEY_M:
            lwjglKeyCode == Keyboard.KEY_N ? GLFW.GLFW_KEY_N:
            lwjglKeyCode == Keyboard.KEY_O ? GLFW.GLFW_KEY_O:
            lwjglKeyCode == Keyboard.KEY_P ? GLFW.GLFW_KEY_P:
            lwjglKeyCode == Keyboard.KEY_Q ? GLFW.GLFW_KEY_Q:
            lwjglKeyCode == Keyboard.KEY_R ? GLFW.GLFW_KEY_R:
            lwjglKeyCode == Keyboard.KEY_S ? GLFW.GLFW_KEY_S:
            lwjglKeyCode == Keyboard.KEY_T ? GLFW.GLFW_KEY_T:
            lwjglKeyCode == Keyboard.KEY_U ? GLFW.GLFW_KEY_U:
            lwjglKeyCode == Keyboard.KEY_V ? GLFW.GLFW_KEY_V:
            lwjglKeyCode == Keyboard.KEY_W ? GLFW.GLFW_KEY_W:
            lwjglKeyCode == Keyboard.KEY_X ? GLFW.GLFW_KEY_X:
            lwjglKeyCode == Keyboard.KEY_Y ? GLFW.GLFW_KEY_Y:
            lwjglKeyCode == Keyboard.KEY_Z ? GLFW.GLFW_KEY_Z:
            lwjglKeyCode == Keyboard.KEY_UP ? GLFW.GLFW_KEY_UP:
            lwjglKeyCode == Keyboard.KEY_DOWN ? GLFW.GLFW_KEY_DOWN:
            lwjglKeyCode == Keyboard.KEY_LEFT ? GLFW.GLFW_KEY_LEFT:
            lwjglKeyCode == Keyboard.KEY_RIGHT ? GLFW.GLFW_KEY_RIGHT:
            lwjglKeyCode == Keyboard.KEY_INSERT ? GLFW.GLFW_KEY_INSERT:
            lwjglKeyCode == Keyboard.KEY_DELETE ? GLFW.GLFW_KEY_DELETE:
            lwjglKeyCode == Keyboard.KEY_HOME ? GLFW.GLFW_KEY_HOME:
            lwjglKeyCode == Keyboard.KEY_END ? GLFW.GLFW_KEY_END:
            lwjglKeyCode == Keyboard.KEY_PRIOR ? GLFW.GLFW_KEY_PAGE_UP:
            lwjglKeyCode == Keyboard.KEY_NEXT ? GLFW.GLFW_KEY_PAGE_DOWN:
            lwjglKeyCode == Keyboard.KEY_F1 ? GLFW.GLFW_KEY_F1:
            lwjglKeyCode == Keyboard.KEY_F2 ? GLFW.GLFW_KEY_F2:
            lwjglKeyCode == Keyboard.KEY_F3 ? GLFW.GLFW_KEY_F3:
            lwjglKeyCode == Keyboard.KEY_F4 ? GLFW.GLFW_KEY_F4:
            lwjglKeyCode == Keyboard.KEY_F5 ? GLFW.GLFW_KEY_F5:
            lwjglKeyCode == Keyboard.KEY_F6 ? GLFW.GLFW_KEY_F6:
            lwjglKeyCode == Keyboard.KEY_F7 ? GLFW.GLFW_KEY_F7:
            lwjglKeyCode == Keyboard.KEY_F8 ? GLFW.GLFW_KEY_F8:
            lwjglKeyCode == Keyboard.KEY_F9 ? GLFW.GLFW_KEY_F9:
            lwjglKeyCode == Keyboard.KEY_F10 ? GLFW.GLFW_KEY_F10:
            lwjglKeyCode == Keyboard.KEY_F11 ? GLFW.GLFW_KEY_F11:
            lwjglKeyCode == Keyboard.KEY_F12 ? GLFW.GLFW_KEY_F12:
            lwjglKeyCode == Keyboard.KEY_F13 ? GLFW.GLFW_KEY_F13:
            lwjglKeyCode == Keyboard.KEY_F14 ? GLFW.GLFW_KEY_F14:
            lwjglKeyCode == Keyboard.KEY_F15 ? GLFW.GLFW_KEY_F15:
            lwjglKeyCode == Keyboard.KEY_F16 ? GLFW.GLFW_KEY_F16:
            lwjglKeyCode == Keyboard.KEY_F17 ? GLFW.GLFW_KEY_F17:
            lwjglKeyCode == Keyboard.KEY_F18 ? GLFW.GLFW_KEY_F18:
            lwjglKeyCode == Keyboard.KEY_F19 ? GLFW.GLFW_KEY_F19:
            lwjglKeyCode == Keyboard.KEY_NUMPAD1 ? GLFW.GLFW_KEY_KP_1:
            lwjglKeyCode == Keyboard.KEY_NUMPAD2 ? GLFW.GLFW_KEY_KP_2:
            lwjglKeyCode == Keyboard.KEY_NUMPAD3 ? GLFW.GLFW_KEY_KP_3:
            lwjglKeyCode == Keyboard.KEY_NUMPAD4 ? GLFW.GLFW_KEY_KP_4:
            lwjglKeyCode == Keyboard.KEY_NUMPAD5 ? GLFW.GLFW_KEY_KP_5:
            lwjglKeyCode == Keyboard.KEY_NUMPAD6 ? GLFW.GLFW_KEY_KP_6:
            lwjglKeyCode == Keyboard.KEY_NUMPAD7 ? GLFW.GLFW_KEY_KP_7:
            lwjglKeyCode == Keyboard.KEY_NUMPAD8 ? GLFW.GLFW_KEY_KP_8:
            lwjglKeyCode == Keyboard.KEY_NUMPAD9 ? GLFW.GLFW_KEY_KP_9:
            lwjglKeyCode == Keyboard.KEY_NUMPAD0 ? GLFW.GLFW_KEY_KP_0:
            lwjglKeyCode == Keyboard.KEY_ADD ? GLFW.GLFW_KEY_KP_ADD:
            lwjglKeyCode == Keyboard.KEY_SUBTRACT ? GLFW.GLFW_KEY_KP_SUBTRACT:
            lwjglKeyCode == Keyboard.KEY_MULTIPLY ? GLFW.GLFW_KEY_KP_MULTIPLY:
            lwjglKeyCode == Keyboard.KEY_DIVIDE ? GLFW.GLFW_KEY_KP_DIVIDE:
            lwjglKeyCode == Keyboard.KEY_DECIMAL ? GLFW.GLFW_KEY_KP_DECIMAL:
            lwjglKeyCode == Keyboard.KEY_NUMPADEQUALS ? GLFW.GLFW_KEY_KP_EQUAL:
            lwjglKeyCode == Keyboard.KEY_NUMPADENTER ? GLFW.GLFW_KEY_KP_ENTER:
            lwjglKeyCode == Keyboard.KEY_NUMLOCK ? GLFW.GLFW_KEY_NUM_LOCK:
            lwjglKeyCode == Keyboard.KEY_SEMICOLON ? GLFW.GLFW_KEY_SEMICOLON:
            lwjglKeyCode == Keyboard.KEY_BACKSLASH ? GLFW.GLFW_KEY_BACKSLASH:
            lwjglKeyCode == Keyboard.KEY_COMMA ? GLFW.GLFW_KEY_COMMA:
            lwjglKeyCode == Keyboard.KEY_PERIOD ? GLFW.GLFW_KEY_PERIOD:
            lwjglKeyCode == Keyboard.KEY_SLASH ? GLFW.GLFW_KEY_SLASH:
            lwjglKeyCode == Keyboard.KEY_GRAVE ? GLFW.GLFW_KEY_GRAVE_ACCENT:
            lwjglKeyCode == Keyboard.KEY_CAPITAL ? GLFW.GLFW_KEY_CAPS_LOCK:
            lwjglKeyCode == Keyboard.KEY_SCROLL ? GLFW.GLFW_KEY_SCROLL_LOCK:
            lwjglKeyCode == Keyboard.KEY_PAUSE ? GLFW.GLFW_KEY_PAUSE:
            lwjglKeyCode == Keyboard.KEY_CIRCUMFLEX ? GLFW.GLFW_KEY_WORLD_1: // "World" keys could be anything depending on
                                                                   // keyboard layout, pick something arbitrary
            lwjglKeyCode == Keyboard.KEY_YEN ? GLFW.GLFW_KEY_WORLD_2:

            lwjglKeyCode == Keyboard.KEY_MINUS ? GLFW.GLFW_KEY_MINUS:
            lwjglKeyCode == Keyboard.KEY_EQUALS ? GLFW.GLFW_KEY_EQUAL:
            lwjglKeyCode == Keyboard.KEY_LBRACKET ? GLFW.GLFW_KEY_LEFT_BRACKET:
            lwjglKeyCode == Keyboard.KEY_RBRACKET ? GLFW.GLFW_KEY_RIGHT_BRACKET:
            lwjglKeyCode == Keyboard.KEY_APOSTROPHE ? GLFW.GLFW_KEY_APOSTROPHE:
            // public static final int KEY_AT = 0x91: /* (NEC PC98) */
            // public static final int KEY_COLON = 0x92: /* (NEC PC98) */
            // public static final int KEY_UNDERLINE = 0x93: /* (NEC PC98) */

            // public static final int KEY_KANA = 0x70: /* (Japanese keyboard) */
            // public static final int KEY_CONVERT = 0x79: /* (Japanese keyboard) */
            // public static final int KEY_NOCONVERT = 0x7B: /* (Japanese keyboard) */
            // public static final int KEY_YEN = 0x7D: /* (Japanese keyboard) */

            // public static final int KEY_CIRCUMFLEX = 0x90: /* (Japanese keyboard) */
            // public static final int KEY_KANJI = 0x94: /* (Japanese keyboard) */
            // public static final int KEY_STOP = 0x95: /* (NEC PC98) */
            // public static final int KEY_AX = 0x96: /* (Japan AX) */
            // public static final int KEY_UNLABELED = 0x97: /* (J3100) */
            // public static final int KEY_SECTION = 0xA7: /* Section symbol (Mac) */
            // public static final int KEY_NUMPADCOMMA = 0xB3: /* , on numeric keypad (NEC PC98) */
            // public static final int KEY_SYSRQ = 0xB7:
            // public static final int KEY_FUNCTION = 0xC4: /* Function (Mac) */

            // public static final int KEY_CLEAR = 0xDA: /* Clear key (Mac) */

            // public static final int KEY_APPS = 0xDD: /* AppMenu key */
            // public static final int KEY_POWER = 0xDE:
            // public static final int KEY_SLEEP = 0xDF:

            GLFW.GLFW_KEY_UNKNOWN;
    }

    public static int awtToLwjgl(int awtCode) {
        return awtCode == KeyEvent.VK_ESCAPE ? Keyboard.KEY_ESCAPE:
            awtCode == KeyEvent.VK_1 ? Keyboard.KEY_1:
            awtCode == KeyEvent.VK_2 ? Keyboard.KEY_2:
            awtCode == KeyEvent.VK_3 ? Keyboard.KEY_3:
            awtCode == KeyEvent.VK_4 ? Keyboard.KEY_4:
            awtCode == KeyEvent.VK_5 ? Keyboard.KEY_5:
            awtCode == KeyEvent.VK_6 ? Keyboard.KEY_6:
            awtCode == KeyEvent.VK_7 ? Keyboard.KEY_7:
            awtCode == KeyEvent.VK_8 ? Keyboard.KEY_8:
            awtCode == KeyEvent.VK_9 ? Keyboard.KEY_9:
            awtCode == KeyEvent.VK_0 ? Keyboard.KEY_0:
            awtCode == KeyEvent.VK_MINUS ? Keyboard.KEY_MINUS:
            awtCode == KeyEvent.VK_EQUALS ? Keyboard.KEY_EQUALS:
            awtCode == KeyEvent.VK_BACK_SPACE ? Keyboard.KEY_BACK:
            awtCode == KeyEvent.VK_TAB ? Keyboard.KEY_TAB:
            awtCode == KeyEvent.VK_Q ? Keyboard.KEY_Q:
            awtCode == KeyEvent.VK_W ? Keyboard.KEY_W:
            awtCode == KeyEvent.VK_E ? Keyboard.KEY_E:
            awtCode == KeyEvent.VK_R ? Keyboard.KEY_R:
            awtCode == KeyEvent.VK_T ? Keyboard.KEY_T:
            awtCode == KeyEvent.VK_Y ? Keyboard.KEY_Y:
            awtCode == KeyEvent.VK_U ? Keyboard.KEY_U:
            awtCode == KeyEvent.VK_I ? Keyboard.KEY_I:
            awtCode == KeyEvent.VK_O ? Keyboard.KEY_O:
            awtCode == KeyEvent.VK_P ? Keyboard.KEY_P:
            awtCode == KeyEvent.VK_OPEN_BRACKET ? Keyboard.KEY_LBRACKET:
            awtCode == KeyEvent.VK_CLOSE_BRACKET ? Keyboard.KEY_RBRACKET:
            awtCode == KeyEvent.VK_ENTER ? Keyboard.KEY_RETURN:
            awtCode == KeyEvent.VK_CONTROL ? Keyboard.KEY_LCONTROL:
            awtCode == KeyEvent.VK_A ? Keyboard.KEY_A:
            awtCode == KeyEvent.VK_S ? Keyboard.KEY_S:
            awtCode == KeyEvent.VK_D ? Keyboard.KEY_D:
            awtCode == KeyEvent.VK_F ? Keyboard.KEY_F:
            awtCode == KeyEvent.VK_G ? Keyboard.KEY_G:
            awtCode == KeyEvent.VK_H ? Keyboard.KEY_H:
            awtCode == KeyEvent.VK_J ? Keyboard.KEY_J:
            awtCode == KeyEvent.VK_K ? Keyboard.KEY_K:
            awtCode == KeyEvent.VK_L ? Keyboard.KEY_L:
            awtCode == KeyEvent.VK_SEMICOLON ? Keyboard.KEY_SEMICOLON:
            awtCode == KeyEvent.VK_QUOTE ? Keyboard.KEY_APOSTROPHE:
            awtCode == KeyEvent.VK_DEAD_GRAVE ? Keyboard.KEY_GRAVE:
            awtCode == KeyEvent.VK_SHIFT ? Keyboard.KEY_LSHIFT:
            awtCode == KeyEvent.VK_BACK_SLASH ? Keyboard.KEY_BACKSLASH:
            awtCode == KeyEvent.VK_Z ? Keyboard.KEY_Z:
            awtCode == KeyEvent.VK_X ? Keyboard.KEY_X:
            awtCode == KeyEvent.VK_C ? Keyboard.KEY_C:
            awtCode == KeyEvent.VK_V ? Keyboard.KEY_V:
            awtCode == KeyEvent.VK_B ? Keyboard.KEY_B:
            awtCode == KeyEvent.VK_N ? Keyboard.KEY_N:
            awtCode == KeyEvent.VK_M ? Keyboard.KEY_M:
            awtCode == KeyEvent.VK_COMMA ? Keyboard.KEY_COMMA:
            awtCode == KeyEvent.VK_PERIOD ? Keyboard.KEY_PERIOD:
            awtCode == KeyEvent.VK_SLASH ? Keyboard.KEY_SLASH:
            awtCode == KeyEvent.VK_MULTIPLY ? Keyboard.KEY_MULTIPLY:
            awtCode == KeyEvent.VK_ALT ? Keyboard.KEY_LMENU:
            awtCode == KeyEvent.VK_SPACE ? Keyboard.KEY_SPACE:
            awtCode == KeyEvent.VK_CAPS_LOCK ? Keyboard.KEY_CAPITAL:
            awtCode == KeyEvent.VK_F1 ? Keyboard.KEY_F1:
            awtCode == KeyEvent.VK_F2 ? Keyboard.KEY_F2:
            awtCode == KeyEvent.VK_F3 ? Keyboard.KEY_F3:
            awtCode == KeyEvent.VK_F4 ? Keyboard.KEY_F4:
            awtCode == KeyEvent.VK_F5 ? Keyboard.KEY_F5:
            awtCode == KeyEvent.VK_F6 ? Keyboard.KEY_F6:
            awtCode == KeyEvent.VK_F7 ? Keyboard.KEY_F7:
            awtCode == KeyEvent.VK_F8 ? Keyboard.KEY_F8:
            awtCode == KeyEvent.VK_F9 ? Keyboard.KEY_F9:
            awtCode == KeyEvent.VK_F10 ? Keyboard.KEY_F10:
            awtCode == KeyEvent.VK_NUM_LOCK ? Keyboard.KEY_NUMLOCK:
            awtCode == KeyEvent.VK_SCROLL_LOCK ? Keyboard.KEY_SCROLL:
            awtCode == KeyEvent.VK_NUMPAD7 ? Keyboard.KEY_NUMPAD7:
            awtCode == KeyEvent.VK_NUMPAD8 ? Keyboard.KEY_NUMPAD8:
            awtCode == KeyEvent.VK_NUMPAD9 ? Keyboard.KEY_NUMPAD9:
            awtCode == KeyEvent.VK_SUBTRACT ? Keyboard.KEY_SUBTRACT:
            awtCode == KeyEvent.VK_NUMPAD4 ? Keyboard.KEY_NUMPAD4:
            awtCode == KeyEvent.VK_NUMPAD5 ? Keyboard.KEY_NUMPAD5:
            awtCode == KeyEvent.VK_NUMPAD6 ? Keyboard.KEY_NUMPAD6:
            awtCode == KeyEvent.VK_ADD ? Keyboard.KEY_ADD:
            awtCode == KeyEvent.VK_NUMPAD1 ? Keyboard.KEY_NUMPAD1:
            awtCode == KeyEvent.VK_NUMPAD2 ? Keyboard.KEY_NUMPAD2:
            awtCode == KeyEvent.VK_NUMPAD3 ? Keyboard.KEY_NUMPAD3:
            awtCode == KeyEvent.VK_NUMPAD0 ? Keyboard.KEY_NUMPAD0:
            awtCode == KeyEvent.VK_DECIMAL ? Keyboard.KEY_DECIMAL:
            awtCode == KeyEvent.VK_F11 ? Keyboard.KEY_F11:
            awtCode == KeyEvent.VK_F12 ? Keyboard.KEY_F12:
            awtCode == KeyEvent.VK_F13 ? Keyboard.KEY_F13:
            awtCode == KeyEvent.VK_F14 ? Keyboard.KEY_F14:
            awtCode == KeyEvent.VK_F15 ? Keyboard.KEY_F15:
            awtCode == KeyEvent.VK_KANA ? Keyboard.KEY_KANA:
            awtCode == KeyEvent.VK_CONVERT ? Keyboard.KEY_CONVERT:
            awtCode == KeyEvent.VK_NONCONVERT ? Keyboard.KEY_NOCONVERT:
            awtCode == KeyEvent.VK_CIRCUMFLEX ? Keyboard.KEY_CIRCUMFLEX:
            awtCode == KeyEvent.VK_AT ? Keyboard.KEY_AT:
            awtCode == KeyEvent.VK_COLON ? Keyboard.KEY_COLON:
            awtCode == KeyEvent.VK_UNDERSCORE ? Keyboard.KEY_UNDERLINE:
            awtCode == KeyEvent.VK_KANJI ? Keyboard.KEY_KANJI:
            awtCode == KeyEvent.VK_STOP ? Keyboard.KEY_STOP:
            awtCode == KeyEvent.VK_DIVIDE ? Keyboard.KEY_DIVIDE:
            awtCode == KeyEvent.VK_PAUSE ? Keyboard.KEY_PAUSE:
            awtCode == KeyEvent.VK_HOME ? Keyboard.KEY_HOME:
            awtCode == KeyEvent.VK_UP ? Keyboard.KEY_UP:
            awtCode == KeyEvent.VK_PAGE_UP ? Keyboard.KEY_PRIOR:
            awtCode == KeyEvent.VK_LEFT ? Keyboard.KEY_LEFT:
            awtCode == KeyEvent.VK_RIGHT ? Keyboard.KEY_RIGHT:
            awtCode == KeyEvent.VK_END ? Keyboard.KEY_END:
            awtCode == KeyEvent.VK_DOWN ? Keyboard.KEY_DOWN:
            awtCode == KeyEvent.VK_PAGE_DOWN ? Keyboard.KEY_NEXT:
            awtCode == KeyEvent.VK_INSERT ? Keyboard.KEY_INSERT:
            awtCode == KeyEvent.VK_DELETE ? Keyboard.KEY_DELETE:
            awtCode == KeyEvent.VK_META ? Keyboard.KEY_LWIN:
            Keyboard.KEY_NONE;
    }

    public static int lwjglToAwt(int lwjglCode) {
        return lwjglCode == Keyboard.KEY_ESCAPE ? KeyEvent.VK_ESCAPE:
            lwjglCode == Keyboard.KEY_1 ? KeyEvent.VK_1:
            lwjglCode == Keyboard.KEY_2 ? KeyEvent.VK_2:
            lwjglCode == Keyboard.KEY_3 ? KeyEvent.VK_3:
            lwjglCode == Keyboard.KEY_4 ? KeyEvent.VK_4:
            lwjglCode == Keyboard.KEY_5 ? KeyEvent.VK_5:
            lwjglCode == Keyboard.KEY_6 ? KeyEvent.VK_6:
            lwjglCode == Keyboard.KEY_7 ? KeyEvent.VK_7:
            lwjglCode == Keyboard.KEY_8 ? KeyEvent.VK_8:
            lwjglCode == Keyboard.KEY_9 ? KeyEvent.VK_9:
            lwjglCode == Keyboard.KEY_0 ? KeyEvent.VK_0:
            lwjglCode == Keyboard.KEY_MINUS ? KeyEvent.VK_MINUS:
            lwjglCode == Keyboard.KEY_EQUALS ? KeyEvent.VK_EQUALS:
            lwjglCode == Keyboard.KEY_BACK ? KeyEvent.VK_BACK_SPACE:
            lwjglCode == Keyboard.KEY_TAB ? KeyEvent.VK_TAB:
            lwjglCode == Keyboard.KEY_Q ? KeyEvent.VK_Q:
            lwjglCode == Keyboard.KEY_W ? KeyEvent.VK_W:
            lwjglCode == Keyboard.KEY_E ? KeyEvent.VK_E:
            lwjglCode == Keyboard.KEY_R ? KeyEvent.VK_R:
            lwjglCode == Keyboard.KEY_T ? KeyEvent.VK_T:
            lwjglCode == Keyboard.KEY_Y ? KeyEvent.VK_Y:
            lwjglCode == Keyboard.KEY_U ? KeyEvent.VK_U:
            lwjglCode == Keyboard.KEY_I ? KeyEvent.VK_I:
            lwjglCode == Keyboard.KEY_O ? KeyEvent.VK_O:
            lwjglCode == Keyboard.KEY_P ? KeyEvent.VK_P:
            lwjglCode == Keyboard.KEY_LBRACKET ? KeyEvent.VK_OPEN_BRACKET:
            lwjglCode == Keyboard.KEY_RBRACKET ? KeyEvent.VK_CLOSE_BRACKET:
            lwjglCode == Keyboard.KEY_RETURN ? KeyEvent.VK_ENTER:
            lwjglCode == Keyboard.KEY_LCONTROL ? KeyEvent.VK_CONTROL:
            lwjglCode == Keyboard.KEY_A ? KeyEvent.VK_A:
            lwjglCode == Keyboard.KEY_S ? KeyEvent.VK_S:
            lwjglCode == Keyboard.KEY_D ? KeyEvent.VK_D:
            lwjglCode == Keyboard.KEY_F ? KeyEvent.VK_F:
            lwjglCode == Keyboard.KEY_G ? KeyEvent.VK_G:
            lwjglCode == Keyboard.KEY_H ? KeyEvent.VK_H:
            lwjglCode == Keyboard.KEY_J ? KeyEvent.VK_J:
            lwjglCode == Keyboard.KEY_K ? KeyEvent.VK_K:
            lwjglCode == Keyboard.KEY_L ? KeyEvent.VK_L:
            lwjglCode == Keyboard.KEY_SEMICOLON ? KeyEvent.VK_SEMICOLON:
            lwjglCode == Keyboard.KEY_APOSTROPHE ? KeyEvent.VK_QUOTE:
            lwjglCode == Keyboard.KEY_GRAVE ? KeyEvent.VK_DEAD_GRAVE:
            lwjglCode == Keyboard.KEY_LSHIFT ? KeyEvent.VK_SHIFT:
            lwjglCode == Keyboard.KEY_BACKSLASH ? KeyEvent.VK_BACK_SLASH:
            lwjglCode == Keyboard.KEY_Z ? KeyEvent.VK_Z:
            lwjglCode == Keyboard.KEY_X ? KeyEvent.VK_X:
            lwjglCode == Keyboard.KEY_C ? KeyEvent.VK_C:
            lwjglCode == Keyboard.KEY_V ? KeyEvent.VK_V:
            lwjglCode == Keyboard.KEY_B ? KeyEvent.VK_B:
            lwjglCode == Keyboard.KEY_N ? KeyEvent.VK_N:
            lwjglCode == Keyboard.KEY_M ? KeyEvent.VK_M:
            lwjglCode == Keyboard.KEY_COMMA ? KeyEvent.VK_COMMA:
            lwjglCode == Keyboard.KEY_PERIOD ? KeyEvent.VK_PERIOD:
            lwjglCode == Keyboard.KEY_SLASH ? KeyEvent.VK_SLASH:
            lwjglCode == Keyboard.KEY_MULTIPLY ? KeyEvent.VK_MULTIPLY:
            lwjglCode == Keyboard.KEY_LMENU ? KeyEvent.VK_ALT:
            lwjglCode == Keyboard.KEY_SPACE ? KeyEvent.VK_SPACE:
            lwjglCode == Keyboard.KEY_CAPITAL ? KeyEvent.VK_CAPS_LOCK:
            lwjglCode == Keyboard.KEY_F1 ? KeyEvent.VK_F1:
            lwjglCode == Keyboard.KEY_F2 ? KeyEvent.VK_F2:
            lwjglCode == Keyboard.KEY_F3 ? KeyEvent.VK_F3:
            lwjglCode == Keyboard.KEY_F4 ? KeyEvent.VK_F4:
            lwjglCode == Keyboard.KEY_F5 ? KeyEvent.VK_F5:
            lwjglCode == Keyboard.KEY_F6 ? KeyEvent.VK_F6:
            lwjglCode == Keyboard.KEY_F7 ? KeyEvent.VK_F7:
            lwjglCode == Keyboard.KEY_F8 ? KeyEvent.VK_F8:
            lwjglCode == Keyboard.KEY_F9 ? KeyEvent.VK_F9:
            lwjglCode == Keyboard.KEY_F10 ? KeyEvent.VK_F10:
            lwjglCode == Keyboard.KEY_NUMLOCK ? KeyEvent.VK_NUM_LOCK:
            lwjglCode == Keyboard.KEY_SCROLL ? KeyEvent.VK_SCROLL_LOCK:
            lwjglCode == Keyboard.KEY_NUMPAD7 ? KeyEvent.VK_NUMPAD7:
            lwjglCode == Keyboard.KEY_NUMPAD8 ? KeyEvent.VK_NUMPAD8:
            lwjglCode == Keyboard.KEY_NUMPAD9 ? KeyEvent.VK_NUMPAD9:
            lwjglCode == Keyboard.KEY_SUBTRACT ? KeyEvent.VK_SUBTRACT:
            lwjglCode == Keyboard.KEY_NUMPAD4 ? KeyEvent.VK_NUMPAD4:
            lwjglCode == Keyboard.KEY_NUMPAD5 ? KeyEvent.VK_NUMPAD5:
            lwjglCode == Keyboard.KEY_NUMPAD6 ? KeyEvent.VK_NUMPAD6:
            lwjglCode == Keyboard.KEY_ADD ? KeyEvent.VK_ADD:
            lwjglCode == Keyboard.KEY_NUMPAD1 ? KeyEvent.VK_NUMPAD1:
            lwjglCode == Keyboard.KEY_NUMPAD2 ? KeyEvent.VK_NUMPAD2:
            lwjglCode == Keyboard.KEY_NUMPAD3 ? KeyEvent.VK_NUMPAD3:
            lwjglCode == Keyboard.KEY_NUMPAD0 ? KeyEvent.VK_NUMPAD0:
            lwjglCode == Keyboard.KEY_DECIMAL ? KeyEvent.VK_DECIMAL:
            lwjglCode == Keyboard.KEY_F11 ? KeyEvent.VK_F11:
            lwjglCode == Keyboard.KEY_F12 ? KeyEvent.VK_F12:
            lwjglCode == Keyboard.KEY_F13 ? KeyEvent.VK_F13:
            lwjglCode == Keyboard.KEY_F14 ? KeyEvent.VK_F14:
            lwjglCode == Keyboard.KEY_F15 ? KeyEvent.VK_F15:
            lwjglCode == Keyboard.KEY_KANA ? KeyEvent.VK_KANA:
            lwjglCode == Keyboard.KEY_CONVERT ? KeyEvent.VK_CONVERT:
            lwjglCode == Keyboard.KEY_NOCONVERT ? KeyEvent.VK_NONCONVERT:
            lwjglCode == Keyboard.KEY_CIRCUMFLEX ? KeyEvent.VK_CIRCUMFLEX:
            lwjglCode == Keyboard.KEY_AT ? KeyEvent.VK_AT:
            lwjglCode == Keyboard.KEY_COLON ? KeyEvent.VK_COLON:
            lwjglCode == Keyboard.KEY_UNDERLINE ? KeyEvent.VK_UNDERSCORE:
            lwjglCode == Keyboard.KEY_KANJI ? KeyEvent.VK_KANJI:
            lwjglCode == Keyboard.KEY_STOP ? KeyEvent.VK_STOP:
            lwjglCode == Keyboard.KEY_DIVIDE ? KeyEvent.VK_DIVIDE:
            lwjglCode == Keyboard.KEY_PAUSE ? KeyEvent.VK_PAUSE:
            lwjglCode == Keyboard.KEY_HOME ? KeyEvent.VK_HOME:
            lwjglCode == Keyboard.KEY_UP ? KeyEvent.VK_UP:
            lwjglCode == Keyboard.KEY_PRIOR ? KeyEvent.VK_PAGE_UP:
            lwjglCode == Keyboard.KEY_LEFT ? KeyEvent.VK_LEFT:
            lwjglCode == Keyboard.KEY_RIGHT ? KeyEvent.VK_RIGHT:
            lwjglCode == Keyboard.KEY_END ? KeyEvent.VK_END:
            lwjglCode == Keyboard.KEY_DOWN ? KeyEvent.VK_DOWN:
            lwjglCode == Keyboard.KEY_NEXT ? KeyEvent.VK_PAGE_DOWN:
            lwjglCode == Keyboard.KEY_INSERT ? KeyEvent.VK_INSERT:
            lwjglCode == Keyboard.KEY_DELETE ? KeyEvent.VK_DELETE:
            lwjglCode == Keyboard.KEY_LWIN ? KeyEvent.VK_META:
            Keyboard.KEY_NONE;
    }
}
