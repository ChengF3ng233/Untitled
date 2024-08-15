package net.optifine.util;

import lombok.Getter;

@Getter
public class IntArray {
    private int[] array = null;
    private int position = 0;
    private int limit = 0;

    public IntArray(int size) {
        this.array = new int[size];
    }

    public void put(int x) {
        this.array[this.position] = x;
        ++this.position;

        if (this.limit < this.position) {
            this.limit = this.position;
        }
    }

    public void put(int pos, int x) {
        this.array[pos] = x;

        if (this.limit < pos) {
            this.limit = pos;
        }
    }

    public void position(int pos) {
        this.position = pos;
    }

    public void put(int[] ints) {
        int i = ints.length;

        for (int anInt : ints) {
            this.array[this.position] = anInt;
            ++this.position;
        }

        if (this.limit < this.position) {
            this.limit = this.position;
        }
    }

    public int get(int pos) {
        return this.array[pos];
    }

    public void clear() {
        this.position = 0;
        this.limit = 0;
    }

}
