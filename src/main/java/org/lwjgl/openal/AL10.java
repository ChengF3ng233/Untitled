/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openal;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeType;

import java.nio.*;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryStack.stackGet;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memUTF8Safe;

public class AL10 {

    public static final int
        AL_INVALID = 0xFFFFFFFF,
        AL_NONE    = 0x0,
        AL_FALSE   = 0x0,
        AL_TRUE    = 0x1;

    public static final int
        AL_NO_ERROR          = 0x0,
        AL_INVALID_NAME      = 0xA001,
        AL_INVALID_ENUM      = 0xA002,
        AL_INVALID_VALUE     = 0xA003,
        AL_INVALID_OPERATION = 0xA004,
        AL_OUT_OF_MEMORY     = 0xA005;

    public static final int
        AL_DOPPLER_FACTOR = 0xC000,
        AL_DISTANCE_MODEL = 0xD000;

    public static final int
        AL_VENDOR     = 0xB001,
        AL_VERSION    = 0xB002,
        AL_RENDERER   = 0xB003,
        AL_EXTENSIONS = 0xB004;

    public static final int
        AL_INVERSE_DISTANCE         = 0xD001,
        AL_INVERSE_DISTANCE_CLAMPED = 0xD002;

    public static final int
        AL_SOURCE_ABSOLUTE = 0x201,
        AL_SOURCE_RELATIVE = 0x202;

    public static final int
        AL_POSITION = 0x1004,
        AL_VELOCITY = 0x1006,
        AL_GAIN     = 0x100A;

    public static final int
        AL_CONE_INNER_ANGLE = 0x1001,
        AL_CONE_OUTER_ANGLE = 0x1002,
        AL_PITCH            = 0x1003,
        AL_DIRECTION        = 0x1005,
        AL_LOOPING          = 0x1007,
        AL_BUFFER           = 0x1009,
        AL_SOURCE_STATE     = 0x1010,
        AL_CONE_OUTER_GAIN  = 0x1022,
        AL_SOURCE_TYPE      = 0x1027;

    public static final int
        AL_INITIAL = 0x1011,
        AL_PLAYING = 0x1012,
        AL_PAUSED  = 0x1013,
        AL_STOPPED = 0x1014;

    public static final int AL_ORIENTATION = 0x100F;

    public static final int
        AL_BUFFERS_QUEUED    = 0x1015,
        AL_BUFFERS_PROCESSED = 0x1016;

    public static final int
        AL_MIN_GAIN = 0x100D,
        AL_MAX_GAIN = 0x100E;

    public static final int
        AL_REFERENCE_DISTANCE = 0x1020,
        AL_ROLLOFF_FACTOR     = 0x1021,
        AL_MAX_DISTANCE       = 0x1023;

    public static final int
        AL_FREQUENCY = 0x2001,
        AL_BITS      = 0x2002,
        AL_CHANNELS  = 0x2003,
        AL_SIZE      = 0x2004;

    public static final int
        AL_FORMAT_MONO8    = 0x1100,
        AL_FORMAT_MONO16   = 0x1101,
        AL_FORMAT_STEREO8  = 0x1102,
        AL_FORMAT_STEREO16 = 0x1103;

    public static final int
        AL_UNUSED    = 0x2010,
        AL_PENDING   = 0x2011,
        AL_PROCESSED = 0x2012;

    protected AL10() {
        throw new UnsupportedOperationException();
    }

    @NativeType("ALenum")
    public static int alGetError() {
        long __functionAddress = AL.getICD().alGetError;
        return invokeI(__functionAddress);
    }

    @NativeType("ALvoid")
    public static void alEnable(@NativeType("ALenum") int target) {
        long __functionAddress = AL.getICD().alEnable;
        invokeV(target, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDisable(@NativeType("ALenum") int target) {
        long __functionAddress = AL.getICD().alDisable;
        invokeV(target, __functionAddress);
    }

    @NativeType("ALboolean")
    public static boolean alIsEnabled(@NativeType("ALenum") int target) {
        long __functionAddress = AL.getICD().alIsEnabled;
        return invokeZ(target, __functionAddress);
    }

    @NativeType("ALboolean")
    public static boolean alGetBoolean(@NativeType("ALenum") int paramName) {
        long __functionAddress = AL.getICD().alGetBoolean;
        return invokeZ(paramName, __functionAddress);
    }

    @NativeType("ALint")
    public static int alGetInteger(@NativeType("ALenum") int paramName) {
        long __functionAddress = AL.getICD().alGetInteger;
        return invokeI(paramName, __functionAddress);
    }

    @NativeType("ALfloat")
    public static float alGetFloat(@NativeType("ALenum") int paramName) {
        long __functionAddress = AL.getICD().alGetFloat;
        return invokeF(paramName, __functionAddress);
    }

    @NativeType("ALdouble")
    public static double alGetDouble(@NativeType("ALenum") int paramName) {
        long __functionAddress = AL.getICD().alGetDouble;
        return invokeD(paramName, __functionAddress);
    }

    public static void nalGetBooleanv(int paramName, long dest) {
        long __functionAddress = AL.getICD().alGetBooleanv;
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetBooleanv(@NativeType("ALenum") int paramName, @NativeType("ALboolean *") ByteBuffer dest) {
        if (CHECKS) {
            check(dest, 1);
        }
        nalGetBooleanv(paramName, memAddress(dest));
    }

    public static void nalGetIntegerv(int paramName, long dest) {
        long __functionAddress = AL.getICD().alGetIntegerv;
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetIntegerv(@NativeType("ALenum") int paramName, @NativeType("ALint *") IntBuffer dest) {
        if (CHECKS) {
            check(dest, 1);
        }
        nalGetIntegerv(paramName, memAddress(dest));
    }

    public static void nalGetFloatv(int paramName, long dest) {
        long __functionAddress = AL.getICD().alGetFloatv;
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetFloatv(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") FloatBuffer dest) {
        if (CHECKS) {
            check(dest, 1);
        }
        nalGetFloatv(paramName, memAddress(dest));
    }

    public static void nalGetDoublev(int paramName, long dest) {
        long __functionAddress = AL.getICD().alGetDoublev;
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetDoublev(@NativeType("ALenum") int paramName, @NativeType("ALdouble *") DoubleBuffer dest) {
        if (CHECKS) {
            check(dest, 1);
        }
        nalGetDoublev(paramName, memAddress(dest));
    }

    public static long nalGetString(int paramName) {
        long __functionAddress = AL.getICD().alGetString;
        return invokeP(paramName, __functionAddress);
    }

    @Nullable
    @NativeType("ALchar const *")
    public static String alGetString(@NativeType("ALenum") int paramName) {
        long __result = nalGetString(paramName);
        return memUTF8Safe(__result);
    }

    @NativeType("ALvoid")
    public static void alDistanceModel(@NativeType("ALenum") int modelName) {
        long __functionAddress = AL.getICD().alDistanceModel;
        invokeV(modelName, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDopplerFactor(@NativeType("ALfloat") float dopplerFactor) {
        long __functionAddress = AL.getICD().alDopplerFactor;
        invokeV(dopplerFactor, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDopplerVelocity(@NativeType("ALfloat") float dopplerVelocity) {
        long __functionAddress = AL.getICD().alDopplerVelocity;
        invokeV(dopplerVelocity, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alListenerf(@NativeType("ALenum") int paramName, @NativeType("ALfloat") float value) {
        long __functionAddress = AL.getICD().alListenerf;
        invokeV(paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alListeneri(@NativeType("ALenum") int paramName, @NativeType("ALint") int values) {
        long __functionAddress = AL.getICD().alListeneri;
        invokeV(paramName, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alListener3f(@NativeType("ALenum") int paramName, @NativeType("ALfloat") float value1, @NativeType("ALfloat") float value2, @NativeType("ALfloat") float value3) {
        long __functionAddress = AL.getICD().alListener3f;
        invokeV(paramName, value1, value2, value3, __functionAddress);
    }

    public static void nalListenerfv(int paramName, long values) {
        long __functionAddress = AL.getICD().alListenerfv;
        invokePV(paramName, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alListener(@NativeType("ALenum") int paramName, @NativeType("ALfloat const *") FloatBuffer values) {
        alListenerfv(paramName, values);
    }

    @NativeType("ALvoid")
    public static void alListenerfv(@NativeType("ALenum") int paramName, @NativeType("ALfloat const *") FloatBuffer values) {
        if (CHECKS) {
            check(values, 1);
        }
        nalListenerfv(paramName, memAddress(values));
    }

    public static void nalGetListenerf(int paramName, long value) {
        long __functionAddress = AL.getICD().alGetListenerf;
        invokePV(paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListenerf(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") FloatBuffer value) {
        if (CHECKS) {
            check(value, 1);
        }
        nalGetListenerf(paramName, memAddress(value));
    }

    @NativeType("ALvoid")
    public static float alGetListenerf(@NativeType("ALenum") int paramName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            FloatBuffer value = stack.callocFloat(1);
            nalGetListenerf(paramName, memAddress(value));
            return value.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalGetListeneri(int paramName, long value) {
        long __functionAddress = AL.getICD().alGetListeneri;
        invokePV(paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListeneri(@NativeType("ALenum") int paramName, @NativeType("ALint *") IntBuffer value) {
        if (CHECKS) {
            check(value, 1);
        }
        nalGetListeneri(paramName, memAddress(value));
    }

    @NativeType("ALvoid")
    public static int alGetListeneri(@NativeType("ALenum") int paramName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer value = stack.callocInt(1);
            nalGetListeneri(paramName, memAddress(value));
            return value.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalGetListener3f(int paramName, long value1, long value2, long value3) {
        long __functionAddress = AL.getICD().alGetListener3f;
        invokePPPV(paramName, value1, value2, value3, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListener3f(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") FloatBuffer value1, @NativeType("ALfloat *") FloatBuffer value2, @NativeType("ALfloat *") FloatBuffer value3) {
        if (CHECKS) {
            check(value1, 1);
            check(value2, 1);
            check(value3, 1);
        }
        nalGetListener3f(paramName, memAddress(value1), memAddress(value2), memAddress(value3));
    }

    public static void nalGetListenerfv(int paramName, long values) {
        long __functionAddress = AL.getICD().alGetListenerfv;
        invokePV(paramName, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListenerfv(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") FloatBuffer values) {
        if (CHECKS) {
            check(values, 1);
        }
        nalGetListenerfv(paramName, memAddress(values));
    }

    public static void nalGenSources(int n, long srcNames) {
        long __functionAddress = AL.getICD().alGenSources;
        invokePV(n, srcNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGenSources(@NativeType("ALuint *") IntBuffer srcNames) {
        nalGenSources(srcNames.remaining(), memAddress(srcNames));
    }

    @NativeType("ALvoid")
    public static int alGenSources() {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer srcNames = stack.callocInt(1);
            nalGenSources(1, memAddress(srcNames));
            return srcNames.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalDeleteSources(int n, long sources) {
        long __functionAddress = AL.getICD().alDeleteSources;
        invokePV(n, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDeleteSources(@NativeType("ALuint *") IntBuffer sources) {
        nalDeleteSources(sources.remaining(), memAddress(sources));
    }

    @NativeType("ALvoid")
    public static void alDeleteSources(@NativeType("ALuint *") int source) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer sources = stack.ints(source);
            nalDeleteSources(1, memAddress(sources));
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    @NativeType("ALboolean")
    public static boolean alIsSource(@NativeType("ALuint") int sourceName) {
        long __functionAddress = AL.getICD().alIsSource;
        return invokeZ(sourceName, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcef(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat") float value) {
        long __functionAddress = AL.getICD().alSourcef;
        invokeV(source, param, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSource3f(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat") float v1, @NativeType("ALfloat") float v2, @NativeType("ALfloat") float v3) {
        long __functionAddress = AL.getICD().alSource3f;
        invokeV(source, param, v1, v2, v3, __functionAddress);
    }

    public static void nalSourcefv(int source, int param, long values) {
        long __functionAddress = AL.getICD().alSourcefv;
        invokePV(source, param, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSource(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat const *") FloatBuffer values) {
        alSourcefv(source, param, values);
    }

    @NativeType("ALvoid")
    public static void alSourcefv(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat const *") FloatBuffer values) {
        if (CHECKS) {
            check(values, 1);
        }
        nalSourcefv(source, param, memAddress(values));
    }

    @NativeType("ALvoid")
    public static void alSourcei(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALint") int value) {
        long __functionAddress = AL.getICD().alSourcei;
        invokeV(source, param, value, __functionAddress);
    }

    public static void nalGetSourcef(int source, int param, long value) {
        long __functionAddress = AL.getICD().alGetSourcef;
        invokePV(source, param, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourcef(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat *") FloatBuffer value) {
        if (CHECKS) {
            check(value, 1);
        }
        nalGetSourcef(source, param, memAddress(value));
    }

    @NativeType("ALvoid")
    public static float alGetSourcef(@NativeType("ALuint") int source, @NativeType("ALenum") int param) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            FloatBuffer value = stack.callocFloat(1);
            nalGetSourcef(source, param, memAddress(value));
            return value.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalGetSource3f(int source, int param, long v1, long v2, long v3) {
        long __functionAddress = AL.getICD().alGetSource3f;
        invokePPPV(source, param, v1, v2, v3, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSource3f(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat *") FloatBuffer v1, @NativeType("ALfloat *") FloatBuffer v2, @NativeType("ALfloat *") FloatBuffer v3) {
        if (CHECKS) {
            check(v1, 1);
            check(v2, 1);
            check(v3, 1);
        }
        nalGetSource3f(source, param, memAddress(v1), memAddress(v2), memAddress(v3));
    }

    public static void nalGetSourcefv(int source, int param, long values) {
        long __functionAddress = AL.getICD().alGetSourcefv;
        invokePV(source, param, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourcefv(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat *") FloatBuffer values) {
        if (CHECKS) {
            check(values, 1);
        }
        nalGetSourcefv(source, param, memAddress(values));
    }

    public static void nalGetSourcei(int source, int param, long value) {
        long __functionAddress = AL.getICD().alGetSourcei;
        invokePV(source, param, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourcei(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALint *") IntBuffer value) {
        if (CHECKS) {
            check(value, 1);
        }
        nalGetSourcei(source, param, memAddress(value));
    }

    @NativeType("ALvoid")
    public static int alGetSourcei(@NativeType("ALuint") int source, @NativeType("ALenum") int param) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer value = stack.callocInt(1);
            nalGetSourcei(source, param, memAddress(value));
            return value.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalGetSourceiv(int source, int param, long values) {
        long __functionAddress = AL.getICD().alGetSourceiv;
        invokePV(source, param, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourceiv(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALint *") IntBuffer values) {
        if (CHECKS) {
            check(values, 1);
        }
        nalGetSourceiv(source, param, memAddress(values));
    }

    public static void nalSourceQueueBuffers(int sourceName, int numBuffers, long bufferNames) {
        long __functionAddress = AL.getICD().alSourceQueueBuffers;
        invokePV(sourceName, numBuffers, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceQueueBuffers(@NativeType("ALuint") int sourceName, @NativeType("ALuint *") IntBuffer bufferNames) {
        nalSourceQueueBuffers(sourceName, bufferNames.remaining(), memAddress(bufferNames));
    }

    @NativeType("ALvoid")
    public static void alSourceQueueBuffers(@NativeType("ALuint") int sourceName, @NativeType("ALuint *") int bufferName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer bufferNames = stack.ints(bufferName);
            nalSourceQueueBuffers(sourceName, 1, memAddress(bufferNames));
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalSourceUnqueueBuffers(int sourceName, int numEntries, long bufferNames) {
        long __functionAddress = AL.getICD().alSourceUnqueueBuffers;
        invokePV(sourceName, numEntries, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceUnqueueBuffers(@NativeType("ALuint") int sourceName, @NativeType("ALuint *") IntBuffer bufferNames) {
        nalSourceUnqueueBuffers(sourceName, bufferNames.remaining(), memAddress(bufferNames));
    }

    @NativeType("ALvoid")
    public static int alSourceUnqueueBuffers(@NativeType("ALuint") int sourceName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer bufferNames = stack.callocInt(1);
            nalSourceUnqueueBuffers(sourceName, 1, memAddress(bufferNames));
            return bufferNames.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    @NativeType("ALvoid")
    public static void alSourcePlay(@NativeType("ALuint") int source) {
        long __functionAddress = AL.getICD().alSourcePlay;
        invokeV(source, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcePause(@NativeType("ALuint") int source) {
        long __functionAddress = AL.getICD().alSourcePause;
        invokeV(source, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceStop(@NativeType("ALuint") int source) {
        long __functionAddress = AL.getICD().alSourceStop;
        invokeV(source, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceRewind(@NativeType("ALuint") int source) {
        long __functionAddress = AL.getICD().alSourceRewind;
        invokeV(source, __functionAddress);
    }

    public static void nalSourcePlayv(int n, long sources) {
        long __functionAddress = AL.getICD().alSourcePlayv;
        invokePV(n, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcePlayv(@NativeType("ALuint const *") IntBuffer sources) {
        nalSourcePlayv(sources.remaining(), memAddress(sources));
    }

    public static void nalSourcePausev(int n, long sources) {
        long __functionAddress = AL.getICD().alSourcePausev;
        invokePV(n, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcePausev(@NativeType("ALuint const *") IntBuffer sources) {
        nalSourcePausev(sources.remaining(), memAddress(sources));
    }

    public static void nalSourceStopv(int n, long sources) {
        long __functionAddress = AL.getICD().alSourceStopv;
        invokePV(n, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceStop(@NativeType("ALuint const *") IntBuffer sources) {
        alSourceStopv(sources);
    }

    @NativeType("ALvoid")
    public static void alSourceStopv(@NativeType("ALuint const *") IntBuffer sources) {
        nalSourceStopv(sources.remaining(), memAddress(sources));
    }

    public static void nalSourceRewindv(int n, long sources) {
        long __functionAddress = AL.getICD().alSourceRewindv;
        invokePV(n, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceRewindv(@NativeType("ALuint const *") IntBuffer sources) {
        nalSourceRewindv(sources.remaining(), memAddress(sources));
    }

    public static void nalGenBuffers(int n, long bufferNames) {
        long __functionAddress = AL.getICD().alGenBuffers;
        invokePV(n, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGenBuffers(@NativeType("ALuint *") IntBuffer bufferNames) {
        nalGenBuffers(bufferNames.remaining(), memAddress(bufferNames));
    }

    @NativeType("ALvoid")
    public static int alGenBuffers() {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer bufferNames = stack.callocInt(1);
            nalGenBuffers(1, memAddress(bufferNames));
            return bufferNames.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalDeleteBuffers(int n, long bufferNames) {
        long __functionAddress = AL.getICD().alDeleteBuffers;
        invokePV(n, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDeleteBuffers(@NativeType("ALuint const *") IntBuffer bufferNames) {
        nalDeleteBuffers(bufferNames.remaining(), memAddress(bufferNames));
    }

    @NativeType("ALvoid")
    public static void alDeleteBuffers(@NativeType("ALuint const *") int bufferName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer bufferNames = stack.ints(bufferName);
            nalDeleteBuffers(1, memAddress(bufferNames));
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    @NativeType("ALboolean")
    public static boolean alIsBuffer(@NativeType("ALuint") int bufferName) {
        long __functionAddress = AL.getICD().alIsBuffer;
        return invokeZ(bufferName, __functionAddress);
    }

    public static void nalGetBufferf(int bufferName, int paramName, long value) {
        long __functionAddress = AL.getICD().alGetBufferf;
        invokePV(bufferName, paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetBufferf(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int paramName, @NativeType("ALfloat *") FloatBuffer value) {
        if (CHECKS) {
            check(value, 1);
        }
        nalGetBufferf(bufferName, paramName, memAddress(value));
    }

    @NativeType("ALvoid")
    public static float alGetBufferf(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int paramName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            FloatBuffer value = stack.callocFloat(1);
            nalGetBufferf(bufferName, paramName, memAddress(value));
            return value.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalGetBufferi(int bufferName, int paramName, long value) {
        long __functionAddress = AL.getICD().alGetBufferi;
        invokePV(bufferName, paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetBufferi(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int paramName, @NativeType("ALint *") IntBuffer value) {
        if (CHECKS) {
            check(value, 1);
        }
        nalGetBufferi(bufferName, paramName, memAddress(value));
    }

    @NativeType("ALvoid")
    public static int alGetBufferi(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int paramName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            IntBuffer value = stack.callocInt(1);
            nalGetBufferi(bufferName, paramName, memAddress(value));
            return value.get(0);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static void nalBufferData(int bufferName, int format, long data, int size, int frequency) {
        long __functionAddress = AL.getICD().alBufferData;
        invokePV(bufferName, format, data, size, frequency, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") ByteBuffer data, @NativeType("ALsizei") int frequency) {
        nalBufferData(bufferName, format, memAddress(data), data.remaining(), frequency);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") ShortBuffer data, @NativeType("ALsizei") int frequency) {
        nalBufferData(bufferName, format, memAddress(data), data.remaining() << 1, frequency);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") IntBuffer data, @NativeType("ALsizei") int frequency) {
        nalBufferData(bufferName, format, memAddress(data), data.remaining() << 2, frequency);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") FloatBuffer data, @NativeType("ALsizei") int frequency) {
        nalBufferData(bufferName, format, memAddress(data), data.remaining() << 2, frequency);
    }

    public static int nalGetEnumValue(long enumName) {
        long __functionAddress = AL.getICD().alGetEnumValue;
        return invokePI(enumName, __functionAddress);
    }

    @NativeType("ALuint")
    public static int alGetEnumValue(@NativeType("ALchar const *") ByteBuffer enumName) {
        if (CHECKS) {
            checkNT1(enumName);
        }
        return nalGetEnumValue(memAddress(enumName));
    }

    @NativeType("ALuint")
    public static int alGetEnumValue(@NativeType("ALchar const *") CharSequence enumName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            stack.nASCII(enumName, true);
            long enumNameEncoded = stack.getPointerAddress();
            return nalGetEnumValue(enumNameEncoded);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static long nalGetProcAddress(long funcName) {
        long __functionAddress = AL.getICD().alGetProcAddress;
        return invokePP(funcName, __functionAddress);
    }

    @NativeType("void *")
    public static long alGetProcAddress(@NativeType("ALchar const *") ByteBuffer funcName) {
        if (CHECKS) {
            checkNT1(funcName);
        }
        return nalGetProcAddress(memAddress(funcName));
    }

    @NativeType("void *")
    public static long alGetProcAddress(@NativeType("ALchar const *") CharSequence funcName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            stack.nASCII(funcName, true);
            long funcNameEncoded = stack.getPointerAddress();
            return nalGetProcAddress(funcNameEncoded);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static boolean nalIsExtensionPresent(long extName) {
        long __functionAddress = AL.getICD().alIsExtensionPresent;
        return invokePZ(extName, __functionAddress);
    }

    @NativeType("ALCboolean")
    public static boolean alIsExtensionPresent(@NativeType("ALchar const *") ByteBuffer extName) {
        if (CHECKS) {
            checkNT1(extName);
        }
        return nalIsExtensionPresent(memAddress(extName));
    }

    @NativeType("ALCboolean")
    public static boolean alIsExtensionPresent(@NativeType("ALchar const *") CharSequence extName) {
        MemoryStack stack = stackGet(); int stackPointer = stack.getPointer();
        try {
            stack.nASCII(extName, true);
            long extNameEncoded = stack.getPointerAddress();
            return nalIsExtensionPresent(extNameEncoded);
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    @NativeType("ALvoid")
    public static void alGetIntegerv(@NativeType("ALenum") int paramName, @NativeType("ALint *") int[] dest) {
        long __functionAddress = AL.getICD().alGetIntegerv;
        if (CHECKS) {
            check(dest, 1);
        }
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetFloatv(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") float[] dest) {
        long __functionAddress = AL.getICD().alGetFloatv;
        if (CHECKS) {
            check(dest, 1);
        }
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetDoublev(@NativeType("ALenum") int paramName, @NativeType("ALdouble *") double[] dest) {
        long __functionAddress = AL.getICD().alGetDoublev;
        if (CHECKS) {
            check(dest, 1);
        }
        invokePV(paramName, dest, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alListenerfv(@NativeType("ALenum") int paramName, @NativeType("ALfloat const *") float[] values) {
        long __functionAddress = AL.getICD().alListenerfv;
        if (CHECKS) {
            check(values, 1);
        }
        invokePV(paramName, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListenerf(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") float[] value) {
        long __functionAddress = AL.getICD().alGetListenerf;
        if (CHECKS) {
            check(value, 1);
        }
        invokePV(paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListeneri(@NativeType("ALenum") int paramName, @NativeType("ALint *") int[] value) {
        long __functionAddress = AL.getICD().alGetListeneri;
        if (CHECKS) {
            check(value, 1);
        }
        invokePV(paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListener3f(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") float[] value1, @NativeType("ALfloat *") float[] value2, @NativeType("ALfloat *") float[] value3) {
        long __functionAddress = AL.getICD().alGetListener3f;
        if (CHECKS) {
            check(value1, 1);
            check(value2, 1);
            check(value3, 1);
        }
        invokePPPV(paramName, value1, value2, value3, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetListenerfv(@NativeType("ALenum") int paramName, @NativeType("ALfloat *") float[] values) {
        long __functionAddress = AL.getICD().alGetListenerfv;
        if (CHECKS) {
            check(values, 1);
        }
        invokePV(paramName, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGenSources(@NativeType("ALuint *") int[] srcNames) {
        long __functionAddress = AL.getICD().alGenSources;
        invokePV(srcNames.length, srcNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDeleteSources(@NativeType("ALuint *") int[] sources) {
        long __functionAddress = AL.getICD().alDeleteSources;
        invokePV(sources.length, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcefv(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat const *") float[] values) {
        long __functionAddress = AL.getICD().alSourcefv;
        if (CHECKS) {
            check(values, 1);
        }
        invokePV(source, param, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourcef(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat *") float[] value) {
        long __functionAddress = AL.getICD().alGetSourcef;
        if (CHECKS) {
            check(value, 1);
        }
        invokePV(source, param, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSource3f(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat *") float[] v1, @NativeType("ALfloat *") float[] v2, @NativeType("ALfloat *") float[] v3) {
        long __functionAddress = AL.getICD().alGetSource3f;
        if (CHECKS) {
            check(v1, 1);
            check(v2, 1);
            check(v3, 1);
        }
        invokePPPV(source, param, v1, v2, v3, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourcefv(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALfloat *") float[] values) {
        long __functionAddress = AL.getICD().alGetSourcefv;
        if (CHECKS) {
            check(values, 1);
        }
        invokePV(source, param, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourcei(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALint *") int[] value) {
        long __functionAddress = AL.getICD().alGetSourcei;
        if (CHECKS) {
            check(value, 1);
        }
        invokePV(source, param, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetSourceiv(@NativeType("ALuint") int source, @NativeType("ALenum") int param, @NativeType("ALint *") int[] values) {
        long __functionAddress = AL.getICD().alGetSourceiv;
        if (CHECKS) {
            check(values, 1);
        }
        invokePV(source, param, values, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceQueueBuffers(@NativeType("ALuint") int sourceName, @NativeType("ALuint *") int[] bufferNames) {
        long __functionAddress = AL.getICD().alSourceQueueBuffers;
        invokePV(sourceName, bufferNames.length, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceUnqueueBuffers(@NativeType("ALuint") int sourceName, @NativeType("ALuint *") int[] bufferNames) {
        long __functionAddress = AL.getICD().alSourceUnqueueBuffers;
        invokePV(sourceName, bufferNames.length, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcePlayv(@NativeType("ALuint const *") int[] sources) {
        long __functionAddress = AL.getICD().alSourcePlayv;
        invokePV(sources.length, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourcePausev(@NativeType("ALuint const *") int[] sources) {
        long __functionAddress = AL.getICD().alSourcePausev;
        invokePV(sources.length, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceStopv(@NativeType("ALuint const *") int[] sources) {
        long __functionAddress = AL.getICD().alSourceStopv;
        invokePV(sources.length, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alSourceRewindv(@NativeType("ALuint const *") int[] sources) {
        long __functionAddress = AL.getICD().alSourceRewindv;
        invokePV(sources.length, sources, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGenBuffers(@NativeType("ALuint *") int[] bufferNames) {
        long __functionAddress = AL.getICD().alGenBuffers;
        invokePV(bufferNames.length, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alDeleteBuffers(@NativeType("ALuint const *") int[] bufferNames) {
        long __functionAddress = AL.getICD().alDeleteBuffers;
        invokePV(bufferNames.length, bufferNames, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetBufferf(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int paramName, @NativeType("ALfloat *") float[] value) {
        long __functionAddress = AL.getICD().alGetBufferf;
        if (CHECKS) {
            check(value, 1);
        }
        invokePV(bufferName, paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alGetBufferi(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int paramName, @NativeType("ALint *") int[] value) {
        long __functionAddress = AL.getICD().alGetBufferi;
        if (CHECKS) {
            check(value, 1);
        }
        invokePV(bufferName, paramName, value, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") short[] data, @NativeType("ALsizei") int frequency) {
        long __functionAddress = AL.getICD().alBufferData;
        invokePV(bufferName, format, data, data.length << 1, frequency, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") int[] data, @NativeType("ALsizei") int frequency) {
        long __functionAddress = AL.getICD().alBufferData;
        invokePV(bufferName, format, data, data.length << 2, frequency, __functionAddress);
    }

    @NativeType("ALvoid")
    public static void alBufferData(@NativeType("ALuint") int bufferName, @NativeType("ALenum") int format, @NativeType("ALvoid const *") float[] data, @NativeType("ALsizei") int frequency) {
        long __functionAddress = AL.getICD().alBufferData;
        invokePV(bufferName, format, data, data.length << 2, frequency, __functionAddress);
    }

}